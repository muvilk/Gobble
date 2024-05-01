
package com.sutd.t4app.ui.reviews;

        import android.util.Log;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;
        import androidx.lifecycle.ViewModel;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;
        import androidx.lifecycle.ViewModel;

        import com.sutd.t4app.data.model.Restaurant;
        import com.sutd.t4app.data.model.Review;
        import com.sutd.t4app.data.model.UserProfile;
        import com.sutd.t4app.ui.home.HomeFragmentViewModel;
        import com.sutd.t4app.utility.RealmUtility;

        import org.bson.types.ObjectId;

        import java.util.List;

        import javax.inject.Inject;

        import dagger.hilt.android.lifecycle.HiltViewModel;
        import io.realm.Case;
        import io.realm.Realm;
        import io.realm.RealmChangeListener;
        import io.realm.RealmResults;
        import io.realm.mongodb.App;
        import io.realm.mongodb.sync.SyncConfiguration;


@HiltViewModel
public class ReviewViewModel extends ViewModel {
    private MutableLiveData<Restaurant> selectedRestaurant = new MutableLiveData<>();
    private final App realmApp;
    Realm realm;
    private MutableLiveData<UserProfile> userProfilesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> restaurantsLiveData = new MutableLiveData<>();
    private RealmResults<Restaurant> realmResults;

    private MutableLiveData<String> searchText = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> searchResults = new MutableLiveData<>();
    private MutableLiveData<Restaurant> restaurantSelected= new MutableLiveData<>();
    private MutableLiveData<Double> rating= new MutableLiveData<>();
    private MutableLiveData<String> reviewText= new MutableLiveData<>();
    private MutableLiveData<String> imglink= new MutableLiveData<>();

    private MutableLiveData<Integer> foodRating= new MutableLiveData<>();
    private MutableLiveData<Integer> serviceRating= new MutableLiveData<>();
    private MutableLiveData<Integer> atmosphereRating= new MutableLiveData<>();


    @Inject
    public ReviewViewModel(App realmApp) {
        this.realmApp = realmApp;
        initializeRealm();
    }

    public void setImageUrl(String link){
        imglink.setValue(link);
    }

    public LiveData<Restaurant> getSelectedRestaurant() {
        return selectedRestaurant;
    }

    // Method to update selected restaurant
    public void selectRestaurant(Restaurant restaurant) {
        selectedRestaurant.setValue(restaurant);
        Log.d("","SELECTED: "+restaurant.getName());
    }
    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        ReviewViewModel.this.realm = realm;
                        Log.d("HomeFragmentViewModel", "Realm instance has been initialized successfully.");
                        fetchUserProfiles(); // retrieve the exact userprofile based on the current login
                        // after retrieving both things, we upload the review to Review
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


    private void fetchUserProfiles() {
        String currentUserId=realmApp.currentUser().getId();
        if (realm != null && currentUserId != null) {
            UserProfile userProfile = realm.where(UserProfile.class).equalTo("userId", currentUserId)
                    .findFirst();

            if (userProfile != null) {
                UserProfile detachedUserProfile = realm.copyFromRealm(userProfile);

                userProfilesLiveData.postValue(detachedUserProfile);
            } else {
                // Handle the case where the user profile is not found, e.g., post null or a default UserProfile object
                userProfilesLiveData.postValue(null);
            }
        }
    }


    public void updateSearchText(String newText) {
        searchText.setValue(newText);
        performSearch(newText);
    }

    public void setReviewText(String reviewtxt){
        reviewText.setValue(reviewtxt);
    }


    public void performSearch(String query) {
        RealmResults<Restaurant> results = realm.where(Restaurant.class)
                .contains("Name", query, Case.INSENSITIVE)
                .findAllAsync();

        // Listen for changes and update LiveData
        results.addChangeListener(new RealmChangeListener<RealmResults<Restaurant>>() {
            @Override
            public void onChange(RealmResults<Restaurant> updatedResults) {
                searchResults.postValue(realm.copyFromRealm(updatedResults));
            }
        });
    }

    public void addReview(){
        // perform a check on

        if(userProfilesLiveData.getValue() != null){

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    com.sutd.t4app.data.model.Review review = realm.createObject(com.sutd.t4app.data.model.Review.class,new ObjectId());
                    review.setSource("In-House");
                    review.setUserId(userProfilesLiveData.getValue().getUserId());
                    review.setUserImgLink(userProfilesLiveData.getValue().getUser_img_link());
                    review.setUsername(userProfilesLiveData.getValue().getUsername());
                    review.setReview(reviewText.getValue());
                    review.setType(selectedRestaurant.getValue().getType());
                    review.setRestaurantId(selectedRestaurant.getValue().getRestaurantId());
                    review.setPriceRange(selectedRestaurant.getValue().getPriceRange());
                    review.setCuisine(selectedRestaurant.getValue().getCuisine());
                    review.setAmbience(selectedRestaurant.getValue().getAmbience());
                    review.setAddress(selectedRestaurant.getValue().getAddress());
                    review.setClosestLandmark(selectedRestaurant.getValue().getClosestLandmark());
                    review.setDietaryOptions(selectedRestaurant.getValue().getDietaryOptions());
                    review.setRating(rating.getValue());
                    review.setImgPostLink(imglink.getValue());

                    ;
                }
            }, () -> {
                // Transaction was a success.
                Log.v("REVIEW SUCESS", "review saved successfully,");
                // PROCEED TO BE QUESTIONED
            }, error -> {
                // Transaction failed and was automatically canceled.
                Log.e("REVIEW FAILURE", "", error);
            });
        }


    }

    public LiveData<List<Restaurant>> getSearchResults(){
        return this.searchResults;
    }


    public void setRating(Double rate) {
        rating.setValue(rate);
    }


    public LiveData<Integer> getFoodRating() {
        return foodRating;
    }

    public LiveData<Integer> getServiceRating() {
        return serviceRating;
    }

    public LiveData<Integer> getAtmosphereRating() {
        return atmosphereRating;
    }

    public void setFoodRating(int rating) {
        foodRating.setValue(rating);
    }

    public void setServiceRating(int rating) {
        serviceRating.setValue(rating);
    }

    public void setAtmosphereRating(int rating) {
        atmosphereRating.setValue(rating);
    }

}