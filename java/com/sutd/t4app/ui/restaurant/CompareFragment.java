package com.sutd.t4app.ui.restaurant;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.utility.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
//import io.realm.mongodb.UserProfile;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.RealmChangeListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * The `CompareFragment` class in the restaurant package of a T4app Android application handles
 * comparing restaurant profiles and user preferences using OpenAI API for recommendation.
 */
public class CompareFragment extends Fragment {

    private CompareViewModel mViewModel;
    private HomeFragmentViewModel hViewModel;

    @Inject
    App realmApp;
    private Realm realm;
    private RealmResults<UserProfile> realmResults;
    private RealmResults<Restaurant> realmRestaurantResults;

    private List<Restaurant> restaurantList = new ArrayList<>();

    public static CompareFragment newInstance() {
        return new CompareFragment();
    }
    View root;
    TextInputLayout textInputLayout;
    MaterialAutoCompleteTextView autoCompleteTextView;
    MaterialButton btnStartComparing;
    TextView restaurant2Name;
    TextView restaurant2Price;
    TextView restaurant2Service;
    TextView restaurant2Food;
    TextView restaurant2Ambience;
    TextView restaurant2Overall;
    TextView restaurant1Name;
    TextView restaurant1Price;
    TextView restaurant1Service;
    TextView restaurant1Food;
    TextView restaurant1Ambience;
    TextView restaurant1Overall;

    UserProfile currentUser;

    OkHttpClient client;
    Request request;
    String prompt;

    Restaurant r1;
    Restaurant r2;

    private MutableLiveData<UserProfile> userProfilesLiveData = new MutableLiveData<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_compare, container, false);

        FrameLayout frameLayout = root.findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        initializeRealm();
        Bundle arguments = getArguments();
        String value = null;
        Log.d("Cf", "before before passing r1");
        if (arguments != null) {
            Log.d("Cf", "before passing r1");
            r1 = arguments.getParcelable("restaurant");
            Log.d("Cf", "after passing r1");

            restaurant1Name = root.findViewById(R.id.Restaurant1Name);
            restaurant1Price = root.findViewById(R.id.Restaurant1Price);
            restaurant1Service = root.findViewById(R.id.Restaurant1Service);
            restaurant1Food = root.findViewById(R.id.Restaurant1Food);
            restaurant1Ambience = root.findViewById(R.id.Restaurant1Ambience);
            restaurant1Overall = root.findViewById(R.id.Restaurant1Overall);

            restaurant1Name.setText(r1.getName());
            restaurant1Price.setText(r1.getPriceRange());
            restaurant1Service.setText(r1.getServiceRating().toString());
            restaurant1Food.setText(r1.getFoodRating().toString());
            restaurant1Ambience.setText(r1.getAmbienceRating().toString());
            restaurant1Overall.setText(r1.getRatings().toString());
            Log.d("Cf", "after passing all r1");
        }

        textInputLayout = root.findViewById(R.id.compareInputLayout);
        autoCompleteTextView = root.findViewById(R.id.inputTV);
        btnStartComparing = root.findViewById(R.id.restaurantInputButton);
        restaurant2Name = root.findViewById(R.id.Restaurant2Name);
        restaurant2Price = root.findViewById(R.id.Restaurant2Price);
        restaurant2Service = root.findViewById(R.id.Restaurant2Service);
        restaurant2Food = root.findViewById(R.id.Restaurant2Food);
        restaurant2Ambience = root.findViewById(R.id.Restaurant2Ambience);
        restaurant2Overall = root.findViewById(R.id.Restaurant2Overall);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant restaurant = (Restaurant) adapterView.getItemAtPosition(i);
                r2 = restaurant;
                restaurant2Name.setText(r2.getName());
                restaurant2Price.setText(r2.getPriceRange());
                restaurant2Service.setText(r2.getServiceRating().toString());
                restaurant2Food.setText(r2.getFoodRating().toString());
                restaurant2Ambience.setText(r2.getAmbienceRating().toString());
                restaurant2Overall.setText(r2.getRatings().toString());
            }
        });
        btnStartComparing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().isEmpty()){
                    textInputLayout.setError("Please select a restaurant");
                }else {
                    Toast.makeText(getActivity(), "Please wait >.<", Toast.LENGTH_SHORT).show();
                    // Make the request
                    // Create request
                    String apikey = "Bearer " + BuildConfig.OPENAI_API;
                    Log.d("openai", apikey);

                    Log.d("openai", "b4 prompt");
                    prompt = "Output your response in HTML format for the following question." + "Evaluate the user's dining preferences:" + getUserSummary(currentUser) +  " and profiles of the two restaurants:" + getRestSummary(r1) + " and" + getRestSummary(r2) +
                            "Your task is to recommend only one of the restaurant that the user should visit for their next dining experience, considering their preferences and the restaurant profiles." +
                            "Provide a percentage match for each restaurant and suggest a menu item from the chosen restaurant that aligns with the user's preferences, explaining why it is recommended." +
                            "Format your recommendation as follows: (one sentence for each of them)" +
                            "<b>Recommended Restaurant</b>: Name" + "<br/><br/><b>Match Percentage</b>: %" + "<br/><br/><b>Suggested Menu Item</b>: Name - <font size=smaller>Description</font><br/><br/><i>Reason for recommendation</i>";

                    Log.d("openai", "after prompt");


                    // Create JSON body
                    String jsonBody = "{"
                            + "\"model\":\"gpt-3.5-turbo\","
                            + "\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt  + "\"}],"
                            + "\"temperature\":1,"
                            + "\"max_tokens\":256,"
                            + "\"top_p\":1,"
                            + "\"frequency_penalty\":0,"
                            + "\"presence_penalty\":0"
                            + "}";
                    Log.d("openai", "b4 req");
                    request = new Request.Builder()
                            .url("https://api.openai.com/v1/chat/completions")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", apikey)
                            .post(RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8")))
                            .build();
                    Log.d("openai", "after req");
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("openai", "onFailure");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("openai", "onResponse");
                            if (!response.isSuccessful()) {
                                Log.d("openai", "response fail");
                                Log.d("openai", response.toString());
                                throw new IOException("Unexpected code " + response);

                            } else {
                                // Get the response
                                String responseData = response.body().string();
                                Log.d("openai", responseData);
                                // Parse the response and get the text
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    JSONArray choicesArray = jsonObject.getJSONArray("choices");
                                    JSONObject firstChoiceObject = choicesArray.getJSONObject(0);
                                    JSONObject messageObject = firstChoiceObject.getJSONObject("message");
                                    String content = messageObject.getString("content");

                                    // Update the TextView on the main thread
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("openai", "update llm text");
                                            TextView llmText = getView().findViewById(R.id.llmOutput);
                                            Spanned spanned;
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                spanned = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT);
                                            } else {
                                                spanned = Html.fromHtml(content);
                                            }
                                            llmText.setText(spanned);
                                            //llmText.setText(content);
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }


            }
        });



        Log.d("openai", "onCreateView");
        TextView llmText = root.findViewById(R.id.llmOutput);


        //call openAI API - add prompt "How are you"
        // Create OkHttpClient
        client = new OkHttpClient();
        Log.d("openai", "request builder");

        return root;


    }

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        Log.v("Cf", "we have initialiase realm " + realm);
                        CompareFragment.this.realm = realm;
                        //observeUserProfile();
                        observeRestaurant();
                        currentUser = fetchUserProfiles("bshfbefnwoef212100001");
                        Log.d("Cf", currentUser.toString());
                        Log.d("Cf", "currentUser");

                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("QuestionFragment", "Error obtaining Realm configuration", e);
            }
        });
    }


    private UserProfile fetchUserProfiles(String currentUserId) {
        Log.d("Cf", "fetchUserProfiles");
        UserProfile u = new UserProfile();
        if (realm != null && currentUserId != null) {
            u = realm.where(UserProfile.class).equalTo("userId", currentUserId)
                    .findFirst();

            if (u != null) {
                    UserProfile detachedUserProfile = realm.copyFromRealm(u);

                    userProfilesLiveData.postValue(detachedUserProfile);
                } else {
                    // Handle the case where the user profile is not found, e.g., post null or a default UserProfile object
                    userProfilesLiveData.postValue(null);
                }
        }
        return u;

    }

    private void observeUserProfile() {
        Log.v("Cf", "we have called observerUserProfile ");

        // Perform your Realm query
        realmResults = realm.where(UserProfile.class).findAllAsync();
        Log.v("Cf", "we have received results realm " + realmResults);

        realmResults.addChangeListener(new RealmChangeListener<RealmResults<UserProfile>>() {
            @Override
            public void onChange(RealmResults<UserProfile> results) {
                Log.v("Cf", "we have received updated results realm " + results);

                // Handle changes
                Log.d("QuestionFragment", "UserProfile data updated: " + results.size());
            }
        });
    }

    private void observeRestaurant() {
        Log.v("Cf", "we have called observeRestaurant");

        // Perform your Realm query
        realmRestaurantResults = realm.where(Restaurant.class).findAllAsync();
        Log.v("Cf", "we have received results realm " + realmResults);

        realmRestaurantResults.addChangeListener(new RealmChangeListener<RealmResults<Restaurant>>() {
            @Override
            public void onChange(RealmResults<Restaurant> results) {
                Log.v("Cf", "we have received updated restaurant results realm ..." + results);

                restaurantList.clear();
                restaurantList.addAll(results);
                if (autoCompleteTextView.getAdapter() != null) {
                    Log.v("Cf", "getAdapter() != null...");
                    ((ArrayAdapter) autoCompleteTextView.getAdapter()).notifyDataSetChanged();
                } else {
                    Log.v("Cf", "else");
                    Log.v("Cf", restaurantList.toString());

                    // Create and set the adapter if not already set
                    ArrayAdapter<Restaurant> adapterItems = new ArrayAdapter<Restaurant>(getActivity(), R.layout.restaurant_options, restaurantList) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            Log.v("Cf", "getView ....");
                            View view = super.getView(position, convertView, parent);
                            Log.v("Cf", "position "+ getItem(position));
                            Log.v("Cf", "getName()>> "+ getItem(position).getName());

                            TextView text1 = (TextView) view.findViewById(R.id.name);
                            text1.setText(getItem(position).getName()); // Assuming `Restaurant` has a `getName()` method
                            return view;
                        }
                    };

                    //update this.restaurantList with data from result
                    autoCompleteTextView.setAdapter(adapterItems);
                }


                // Handle changes
                Log.d("QuestionFragment", "Restaurant data updated: " + results.size());
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        // TODO: Use the ViewModel
    }

    private String getRestSummary(Restaurant r) {
        return "Name of restaurant: " + r.getName() + ";Type of Cuisine: " + r.getCuisine() + ";Price Range " + r.getPriceRange() + ";Dietary Options Available: " + r.getDietaryOptions() +
                ";Food Rating:(out of 5) " + r.getFoodRating() + ";Service Rating:(out of 5) " + r.getServiceRating() + ";Ambience Description: " + r.getAmbience() + ";Ambience Rating:(out of 5) " + r.getAmbienceRating()
                + ";Top dish on menu: " + r.getTopMenu1() + ", " + r.getTopMenu2() + ", " + r.getTopMenu3() + ", " + r.getTopMenu4() + ";Overall number of stars(rating out of 5): " + r.getRatings();
    }

    private String getUserSummary(UserProfile u) {
        return "Ambience Preferences: " + u.getAmbiencePreferences() + ";Budget Preferences: " + u.getBudgetPreference() + ";Cuisine Adventurousness: " + u.getCuisineAdventurousness() + ";Cuisines Preferences: " + u.getCuisinePreferences() +
                ";Dietary Preferences: " + u.getDietaryPreferences() + ";Food Preferences: " + u.getFoodPreferences() + ";Health Wellness Importance: " + u.getHealthWellnessImportance() + ";Ingredients Disliked: " + u.getIngredientDislikes() +
                ";Ingredient Preferences: " + u.getIngredientPreferences() + ";Specialty Dishes: " + u.getSpecialtyDishes() + ";Spice Tolerance:" + u.getSpicyTolerance() + ";Level of Sweet Tooth: " + u.getSweetTooth();
    }


}