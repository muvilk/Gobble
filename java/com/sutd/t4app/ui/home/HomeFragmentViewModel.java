package com.sutd.t4app.ui.home;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import dagger.hilt.android.lifecycle.HiltViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.TikTok;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.utility.RealmUtility;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;
@HiltViewModel
/*
 * The `HomeFragmentViewModel` class in the Android app handles Realm
 * database initialization, and restaurant ranking based on user profiles.
 */
public class HomeFragmentViewModel extends ViewModel {
    private MutableLiveData<UserProfile> userProfilesLiveData = new MutableLiveData<>();
    private final App realmApp;
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private Realm realm;
    private RealmResults<Restaurant> realmResults;
    private RealmResults<TikTok> tiktokresult;
    private final MutableLiveData<List<Restaurant>> rankedRestaurantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<TikTok>> TikTokLiveData = new MutableLiveData<>();
    private final MutableLiveData<Restaurant> specificRestaurantLiveData = new MutableLiveData<>();



    @Inject
    public HomeFragmentViewModel(App realmApp) {

        this.realmApp = realmApp;
        initializeRealm();


    }


    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        HomeFragmentViewModel.this.realm = realm;
                        Log.d("HomeFragmentViewModel", "Realm instance has been initialized successfully.");
                        observeRestaurants(); // Observes data and updates LiveData
                        fetchUserProfiles();
                        // TODO: 14/4/24 fetchTikTok()
                        fetchTikTok();
                    }
                });
            }
            @Override
            public void onError(Exception e) {
                // Handle any errors, such as login failure
                Log.e("YourViewModel", "Error obtaining Realm configuration", e);
            }
        });
    }

    private void observeRestaurants() {
        if (realm != null) {
            // Perform your Realm query
            realmResults = realm.where(Restaurant.class).findAllAsync();
            realmResults.addChangeListener(new RealmChangeListener<RealmResults<Restaurant>>() {
                @Override
                public void onChange(RealmResults<Restaurant> results) {
                    // This is automatically called on the main thread when data changes
                    Log.d("SyncCheck", "Data synced or updated: " + results.size());
                    // Detach the results from Realm and update LiveData
                    restaurantsLiveData.postValue(realm.copyFromRealm(results));
                }
            });
        }}
    // TODO: 14/4/24 fetchTikTok

    private void fetchTikTok() {
        if (realm != null) {
            Log.d("TikTokFetch", "Fetching TikTok data...");
            tiktokresult = realm.where(TikTok.class).findAllAsync();
            tiktokresult.addChangeListener(new RealmChangeListener<RealmResults<TikTok>>() {
                @Override
                public void onChange(RealmResults<TikTok> tikToks) {
                    Log.d("TikTokFetch", "TikTok data fetched: " + tikToks.size() + " entries.");
                    TikTokLiveData.postValue(realm.copyFromRealm(tikToks));
                }
            });
        } else {
            Log.e("TikTokFetch", "Realm is not initialized.");
        }
    }


    private void fetchUserProfiles() {
        String currentUserId=realmApp.currentUser().getId();
        if (realm != null && currentUserId != null) {
            UserProfile userProfile = realm.where(UserProfile.class).equalTo("userId", currentUserId)
                    .findFirst();

            if (userProfile != null) {
                UserProfile detachedUserProfile = realm.copyFromRealm(userProfile);
                Log.d("UserProfileDataExplore", "User profile found: " + userProfile.toString());

                userProfilesLiveData.postValue(detachedUserProfile);
            } else {
                // Handle the case where the user profile is not found, e.g., post null or a default UserProfile object
                userProfilesLiveData.postValue(null);
                Log.d("UserProfileDataExplore", "No user profile found for ID: " + currentUserId);
            }
        }
    }



    public LiveData<Restaurant> getRestaurantById(String restaurantId) {
        MutableLiveData<Restaurant> specificRestaurant = new MutableLiveData<>();

        // This will only attach an observer once, preventing multiple observations
        restaurantsLiveData.observeForever(new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                Log.d("ViewModel", "onChanged triggered with restaurant list size: " + restaurants.size());
                for (Restaurant restaurant : restaurants) {
                    if (restaurant.getRestaurantId().equals(restaurantId)) {
                        specificRestaurant.setValue(restaurant);
                        Log.d("ViewModel", "Restaurant fetched: " + restaurant.getName() + " with ID: " + restaurantId);
                        restaurantsLiveData.removeObserver(this); // Remove observer after fetching data
                        break;
                    }
                }
            }
        });

        return specificRestaurant;
    }


    public LiveData<Restaurant> getSpecificRestaurantLiveData() {
        return specificRestaurantLiveData;
    }

    public void fetchRestaurantandUser(){
        observeRestaurants();
        fetchUserProfiles();
    }
    // TODO: 14/4/24 getTikTokLiveData()

    public LiveData<List<TikTok>> getTikTokLiveData(){
        return TikTokLiveData;
    }


    public LiveData<UserProfile> getUserProfilesLiveData() {
        return userProfilesLiveData;
    }

    public LiveData<List<Restaurant>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }
    public void rankAndUpdateRestaurants(UserProfile userProfile) {
        // Use getRestaurantsLiveData().getValue() to get the current list of restaurants
        List<Restaurant> unrankedRestaurants = getRestaurantsLiveData().getValue();
        if (unrankedRestaurants == null) {
            unrankedRestaurants = new ArrayList<>(); // Handle null case, possibly by re-fetching or showing an error
        }

        RestaurantRanking ranking = new RestaurantRanking();
        List<RestaurantScore> scores= ranking.rankRestaurants(unrankedRestaurants,userProfile);
        RestaurantRanker ranker = new RestaurantRanker();
        for (RestaurantScore score : scores) {
            ranker.addRestaurantScore(score);
        }

        // Get the sorted restaurants
        List<Restaurant> rankedRestaurants = ranker.getRankedRestaurants();
        rankedRestaurantsLiveData.postValue(rankedRestaurants);
    }

    public MutableLiveData<List<Restaurant>> getRankedRestaurantsLiveData() {
        return rankedRestaurantsLiveData;
    }

    protected void cleanUp() {
        if(realm != null) {
            Log.d("CLOSE REALM", "it is closed");
            realm.close();
            realm = null;
        }
    }
}