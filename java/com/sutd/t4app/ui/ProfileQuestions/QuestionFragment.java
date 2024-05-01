package com.sutd.t4app.ui.ProfileQuestions;
/**
 * The `QuestionFragment` class in the Android app handles user profile creation and storage using
 * Realm database.
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.sutd.t4app.LoginSignUpActivity;
import com.sutd.t4app.MainActivity;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.databinding.QuestionsBinding;
import com.sutd.t4app.utility.RealmUtility;
import com.sutd.t4app.data.model.Restaurant;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;


@AndroidEntryPoint
public class QuestionFragment extends Fragment {
    private QuestionsBinding binding;
    @Inject
     App realmApp;
    private Realm realm;
    private RealmResults<UserProfile> realmResults;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = QuestionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeRealm();
        setupFinalizeProfileButton();

        return root;
    }

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        Log.v("CHECK 1", "we have initialiase realm " + realm);
                        QuestionFragment.this.realm = realm;
                        observeUserProfile();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("QuestionFragment", "Error obtaining Realm configuration", e);
            }
        });
    }
    private void observeUserProfile() {
        Log.v("CHECK 2", "we have called obsercerUserProfile ");
        // Perform your Realm query
        User user = realmApp.currentUser();
        realmResults = realm.where(UserProfile.class).equalTo("userId", user.getId()).findAll();;
        Log.v("CHECK 3", "we have received results realm " + realmResults);

        realmResults.addChangeListener(new RealmChangeListener<RealmResults<UserProfile>>() {
            @Override
            public void onChange(RealmResults<UserProfile> results) {
                Log.v("CHECK 4", "we have received updated results realm " + results);

                // Handle changes
                Log.d("QuestionFragment", "UserProfile data updated: " + results.size());
            }
        });
    }

    private void setupFinalizeProfileButton() {

        Button finalizeProfileButton = binding.finalizeProfileButton;
        finalizeProfileButton.setOnClickListener(v -> {
            UserProfile userProfile = createUserProfileFromForm();
            Log.d("CHECK 5", "userprofile" + userProfile);

            saveUserProfile(userProfile);

            if (getActivity() instanceof MainActivity) {
                Navigation.findNavController(v).navigate(R.id.navigation_home);
                // Do something specific for MainActivity
            } else if (getActivity() instanceof LoginSignUpActivity) {
                // Do something specific for LoginSignUpActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

        });
    }

    private UserProfile createUserProfileFromForm() {
        UserProfile userProfile = new UserProfile();

        // Cuisine preferences (CheckBoxes)
        userProfile.setCuisinePreferences(getSelectedCuisines());

        // Dietary preferences (CheckBoxes)
        userProfile.setDietaryPreferences(getSelectedDietaryPreferences());

        // Location preference (Spinner)
        userProfile.setLocationPreference(getLocationPreference());

        // Budget preference (SeekBar)
        userProfile.setBudgetPreference(getBudgetPreference());

        // Food preferences (CheckBoxes)
        userProfile.setFoodPreferences(getFoodPreferences());

        // Cuisine adventurousness (Spinner)
        userProfile.setCuisineAdventurousness(getCuisineAdventurousness());

        // Spicy tolerance (Spinner)
        userProfile.setSpicyTolerance(getSpicyTolerance());

        // Sweet tooth (Spinner)
        userProfile.setSweetTooth(getSweetTooth());

        // Specialty dishes (EditText)
        userProfile.setSpecialtyDishes(getSpecialtyDishes());

        // Health and wellness importance (Spinner)
        userProfile.setHealthWellnessImportance(getHealthWellnessImportance());

        // Ambience preferences (CheckBoxes)
        userProfile.setAmbiencePreferences(getAmbiencePreferences());

        // Meal time preferences (Spinner)
        userProfile.setMealTimePreferences(getMealTimePreferences());

        // Ingredient preferences (EditText)
        userProfile.setIngredientPreferences(getIngredientPreferences());

        // Ingredient dislikes (EditText)
        userProfile.setIngredientDislikes(getIngredientDislikes());

        return userProfile;
    }

    private String getSelectedCuisines() {
        List<String> selectedCuisines = new ArrayList<>();
        if (binding.radioItalian.isChecked()) selectedCuisines.add("Italian");
        if (binding.radioMexican.isChecked()) selectedCuisines.add("Mexican");
        if (binding.radioAsian.isChecked()) selectedCuisines.add("Asian");
        return selectedCuisines.toString();
    }

    private String getSelectedDietaryPreferences(){
        List<String> selecteddiet= new ArrayList<>();
        if (binding.checkboxVegetarian.isChecked()) selecteddiet.add("Vegetarian");
        if (binding.checkboxVegan.isChecked()) selecteddiet.add("Vegan");
        if (binding.checkboxGlutenFree.isChecked()) selecteddiet.add("Gluten-Free");
        return selecteddiet.toString();

    }
    private String getLocationPreference() {
        return binding.locationSpinner.getSelectedItem().toString();
    }

    private String getBudgetPreference() {
        // Assuming you have a SeekBar for budget
        int budgetValue = binding.budgetSeekBar.getProgress();
        return "$" + budgetValue;
    }

    private String getFoodPreferences(){
        List<String> foodpreference= new ArrayList<>();
        if (binding.spicyCheckbox.isChecked()) foodpreference.add("Spicy");
        if (binding.sweetCheckbox.isChecked()) foodpreference.add("Sweet");
        if (binding.sourCheckbox.isChecked()) foodpreference.add("Sour");
        if (binding.saltyCheckbox.isChecked()) foodpreference.add("Salty");
        if (binding.savoryCheckbox.isChecked()) foodpreference.add("Savory");
        return foodpreference.toString();
    }

    private String getCuisineAdventurousness(){
        return binding.cuisineExplorationSpinner.getSelectedItem().toString();

    }

    private String getSpicyTolerance(){
        return binding.spicyToleranceSpinner.getSelectedItem().toString();
    }

    private String getSweetTooth(){
        return binding.sweetToothSpinner.getSelectedItem().toString();
    }

    private String getSpecialtyDishes(){
        return binding.specialtyDishesEditText.getText().toString();
    }

    private String getHealthWellnessImportance(){
        return binding.healthWellnessSpinner.getSelectedItem().toString();
    }

    private String getAmbiencePreferences(){
        List<String> ambience= new ArrayList<>();
        if (binding.checkboxCasual.isChecked()) ambience.add("Casual");
        if (binding.checkboxCozy.isChecked()) ambience.add("Cozy");
        if (binding.checkboxRomantic.isChecked()) ambience.add("Romantic");
        if (binding.checkboxFormal.isChecked()) ambience.add("Formal");
        if (binding.checkboxEnergetic.isChecked()) ambience.add("Energetic");
        return ambience.toString();


    }
    private String getMealTimePreferences(){
        return binding.mealTimePreferenceSpinner.getSelectedItem().toString();
    }

    private String getIngredientPreferences(){
        return binding.ingredientPreferencesEditText.getText().toString();
    }

    private String getIngredientDislikes(){
        return binding.ingredientDislikesEditText.getText().toString();
    }



    private void saveUserProfile(final UserProfile userProfile) {

        // Log the UserProfile properties
        Log.v("UserProfile", "Saving UserProfile with data: " +
                "\nUsername: " + userProfile.getUsername() +
                "\nEmail: " + userProfile.getEmail() +
                "\nCuisine Preferences: " + userProfile.getCuisinePreferences() +
                "\nDietary Preferences: " + userProfile.getDietaryPreferences()+
                "\nLocation: " + userProfile.getLocationPreference()+
                "\nBudget: " + userProfile.getBudgetPreference()+
                "\nFood Preferences: " + userProfile.getFoodPreferences()+
                "\nCuisine Adventurousness: " + userProfile.getCuisineAdventurousness()+
                "\nSpice Tolerance: " + userProfile.getSpicyTolerance()+
                "\nSweet Tooth?: " + userProfile.getSweetTooth()+
                "\nSpecial Dish: " + userProfile.getSpecialtyDishes()+
                "\nHealth Importance: " + userProfile.getHealthWellnessImportance()+
                "\nAmbience Preference: " + userProfile.getAmbiencePreferences()+
                "\nMeal Time Preference: " + userProfile.getMealTimePreferences()+
                "\nIngredient Preference: " + userProfile.getIngredientPreferences()+
                "\nDislikes: " + userProfile.getIngredientDislikes())

                ;


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realmApp.currentUser();
                UserProfile item = realm.where(UserProfile.class)
                        .equalTo("userId", user.getId())
                        .findFirst();
                if (userProfile != null) {

                    item.setCuisinePreferences(userProfile.getCuisinePreferences());
                    item.setDietaryPreferences(userProfile.getDietaryPreferences());
                    item.setLocationPreference(userProfile.getLocationPreference());
                    item.setBudgetPreference(userProfile.getBudgetPreference());
                    item.setFoodPreferences(userProfile.getFoodPreferences());
                    item.setCuisineAdventurousness(userProfile.getCuisineAdventurousness());
                    item.setSpicyTolerance(userProfile.getSpicyTolerance());
                    item.setSweetTooth(userProfile.getSweetTooth());
                    item.setSpecialtyDishes(userProfile.getSpecialtyDishes());
                    item.setHealthWellnessImportance(userProfile.getHealthWellnessImportance());
                    item.setAmbiencePreferences(userProfile.getAmbiencePreferences());
                    item.setMealTimePreferences(userProfile.getMealTimePreferences());
                    item.setIngredientPreferences(userProfile.getIngredientPreferences());
                    item.setIngredientDislikes(userProfile.getIngredientDislikes());
                }


            }
        }, () -> {
            // Transaction was a success.
            Log.v("UserProfile", "User profile saved successfully");
        }, error -> {
            // Transaction failed and was automatically canceled.
            Log.e("UserProfile", "Error saving user profile", error);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        realm.close();
    }
}
