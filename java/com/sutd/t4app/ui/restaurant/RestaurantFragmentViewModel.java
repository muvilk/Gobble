package com.sutd.t4app.ui.restaurant;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sutd.t4app.BuildConfig;
import com.sutd.t4app.di.api.TripAdvisorService;
import com.sutd.t4app.di.api.YelpService;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.data.model.apiresponses.LocationSearchResponse;
import com.sutd.t4app.data.model.apiresponses.ReviewLocationResponse;
import com.sutd.t4app.data.model.apiresponses.YelpReviewResponse;
import com.sutd.t4app.data.model.apiresponses.YelpSearchResponse;
import com.sutd.t4app.utility.RealmUtility;

import org.bson.types.ObjectId;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.sync.SyncConfiguration;

@HiltViewModel
public class RestaurantFragmentViewModel extends ViewModel {
    private final App realmApp;
    private final MutableLiveData<List<Review>> reviewsLiveData = new MutableLiveData<>();
    private Restaurant currRes;
    private UserProfile currUser;
    private Realm realm;
    private TripAdvisorService tripAdvisorService;
    private YelpService yelpService;
    private LocationSearchResponse.Location Tripsearchresults;
    private List<ReviewLocationResponse.TripReviews> TripreviewSearch;
    private YelpSearchResponse.Business Yelpsearchresults;
    private List<YelpReviewResponse.YelpReviews> YelpreviewSearch;
    private MutableLiveData<Restaurant> restaurantLiveData = new MutableLiveData<>();

    @Inject
    public RestaurantFragmentViewModel(App realmApp, TripAdvisorService tripadvisorService, YelpService yelpservice) {
        this.tripAdvisorService = tripadvisorService;
        this.yelpService = yelpservice;
        this.realmApp = realmApp;

    }

    public void setcurrRes(Restaurant res) {
        this.currRes = res;
        initializeRealm();

    }

    private void fetchAndUpdateLiveData() {
        if (realm != null && !realm.isClosed()) {
            RealmResults<Review> reviews = realm.where(Review.class)
                    .equalTo("Restaurant_id", currRes.getRestaurantId())
                    .findAllAsync();
            reviews.addChangeListener(new RealmChangeListener<RealmResults<Review>>() {
                @Override
                public void onChange(RealmResults<Review> reviews) {
                    if (!realm.isClosed() && realm != null) {
                        List<Review> detachedReviews = realm.copyFromRealm(reviews);
                        reviewsLiveData.postValue(detachedReviews);
                        reviews.removeAllChangeListeners(); // Optional: Remove listener if not needed anymore
                    }
                }
            });
        }
    }

    public LiveData<List<Review>> getReviewsLiveData() {
        // This is assumed to be called in a proper thread or initially
        if (reviewsLiveData.getValue() == null && realm != null) {
            RealmResults<Review> reviews = realm.where(Review.class)
                    .equalTo("Restaurant_id", currRes.getRestaurantId())
                    .findAllAsync();

            // Observe RealmResults and post updates to LiveData
            reviews.addChangeListener(new RealmChangeListener<RealmResults<Review>>() {
                @Override
                public void onChange(RealmResults<Review> reviews) {
                    if (realm != null && !realm.isClosed()) {
                        // Make sure to copy the results from Realm to a non-Realm List
                        List<Review> detachedReviews = realm.copyFromRealm(reviews);
                        reviewsLiveData.postValue(detachedReviews);
                    }
                }
            });
        }
        return reviewsLiveData;
    }

    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        RestaurantFragmentViewModel.this.realm = realm;
                        Log.d("RestaurantFragmentViewModel", "Realm instance has been initialized successfully.");
                        gatherreviews(); // Observes data and updates LiveData
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

    public void gatherreviews() {
        // after initialisation check if there are any existing reviews from realm of the particular res
        if (realm != null) {
            RealmResults<Review> reviews = realm.where(Review.class).equalTo("Restaurant_id", this.currRes.getRestaurantId()).findAllAsync();
            this.currUser = realm.where(UserProfile.class).equalTo("userId", realmApp.currentUser().getId()).findFirst();
            if (realm != null && !realm.isClosed()) {
                this.reviewsLiveData.postValue(realm.copyFromRealm(reviews));

            }
            RealmResults<Review> resultsyelp = realm.where(Review.class)
                    .equalTo("Restaurant_id", this.currRes.getRestaurantId())
                    .equalTo("source", "Yelp")
                    .findAll();

            RealmResults<Review> results = realm.where(Review.class)
                    .equalTo("Restaurant_id", this.currRes.getRestaurantId())
                    .equalTo("source", "TripAdvisor")
                    .findAll();

            Log.d("Result length", "" + results);

            // after showing OR if null, gather new reviews from Yelp or Tripadvisor,

            // but first need to know if the res has the id from both yelp and tripadvisor
            String tripID = this.currRes.getTripAdvisorId();
            String yelpID = this.currRes.getYelpId();
            if (tripID.length() == 0) {
                String tripkey = BuildConfig.TRIP_API;
                String latlong = this.currRes.getLat() + "," + this.currRes.getLng();
                tripAdvisorService.searchLocation(this.currRes.getName(), "restaurant", "Singapore", latlong, "en", BuildConfig.TRIP_API)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            this.Tripsearchresults = result.getData().get(0);
                            String tripid = this.Tripsearchresults.getLocation_id();
                            Log.d("CHECK", "" + tripid);
                            this.currRes.setTripAdvisorId(tripid);
                            // now we do a realm transaction to update the id
                            realm.executeTransactionAsync(r -> {
                                Restaurant restaurant = r.where(Restaurant.class).equalTo("RestaurantId", this.currRes.getRestaurantId()).findFirst();
                                if (restaurant != null) {
                                    restaurant.setTripAdvisorId(tripid);
                                }
                            }, () -> {
                                Log.d("Realm", "Trip ID updated successfully!");
                            }, error -> {
                                Log.e("Realm Error", "Failed to update Trip ID: " + error.getMessage());
                            });

                            Log.d("FIRST TRIP SEARCH RESULTS", "" + this.Tripsearchresults);
                        }, throwable -> {
                            // Handle error
                            Log.d("TripAdvisor Error", "Error occurred: " + throwable.getMessage());
                        });
            }
            if (yelpID.length() == 0) {
                String yelpkey = BuildConfig.YELP_API;
                double latitude = Double.parseDouble(this.currRes.getLat());
                double longti = Double.parseDouble(this.currRes.getLng());
                yelpService.searchBusinesses(yelpkey, "Singapore", latitude, longti, this.currRes.getName())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            this.Yelpsearchresults = result.getBusinesses().get(0);
                            String yelpid = this.Yelpsearchresults.getId();
                            this.currRes.setYelpId(yelpid);
                            realm.executeTransactionAsync(r -> {
                                Restaurant restaurant = r.where(Restaurant.class).equalTo("RestaurantId", this.currRes.getRestaurantId()).findFirst();
                                if (restaurant != null) {
                                    restaurant.setYelpId(yelpid);
                                }
                            }, () -> {
                                Log.d("Realm", "Yelp ID updated successfully!");
                            }, error -> {
                                Log.e("Realm Error", "Failed to update Yelp ID: " + error.getMessage());
                            });
                            Log.d("FIRST YELP SEARCH RESULTS", "" + Yelpsearchresults);
                        }, throwable -> {
                            // Handle error
                            Log.d("YelpAdvisor Error", "Error occurred: " + throwable.getMessage());
                        });
            }
            // once they have their own IDs, we find the reviews from each service


            if (results.isEmpty()) {
                try {
                    int tripAdvisorId = Integer.parseInt(this.currRes.getTripAdvisorId());
                    final String address = this.currRes.getAddress();
                    final String ambience = this.currRes.getAmbience();
                    final String cuisine = this.currRes.getCuisine();
                    final String dietaryOptions = this.currRes.getDietaryOptions();
                    final String closestLandmark = this.currRes.getClosestLandmark();
                    final String priceRange = this.currRes.getPriceRange();
                    final String username = this.currUser.getUsername();
                    final String userImgLink = this.currUser.getUser_img_link();
                    final String restaurantId = this.currRes.getRestaurantId();
                    final String type = this.currRes.getType();
                    final String userid = this.currUser.getUserId();
                    tripAdvisorService.getReviews(tripAdvisorId, BuildConfig.TRIP_API, "en").subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                this.TripreviewSearch = result.getData();
                                for (ReviewLocationResponse.TripReviews rev : this.TripreviewSearch) {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            Review review = realm.createObject(Review.class, new ObjectId());

                                            if (review != null) {
                                                review.setReview(rev.gettext());
                                                review.setRating(Double.valueOf(rev.getRating()));
                                                review.setAddress(address);
                                                review.setAmbience(ambience);
                                                review.setCuisine(cuisine);
                                                review.setDietaryOptions(dietaryOptions);
                                                review.setClosestLandmark(closestLandmark);
                                                review.setPriceRange(priceRange);
                                                review.setUsername(username);
                                                review.setUserImgLink(userImgLink);
                                                review.setRestaurantId(restaurantId);
                                                review.setType(type);
                                                review.setSource("TripAdvisor");
                                                review.setImgPostLink("");
                                                review.setUserId(userid);
                                            }


                                        }
                                    }, () -> {
                                        // Transaction was a success.
                                        Log.v("UserProfile", "User profile saved successfully");
                                        fetchAndUpdateLiveData();
                                    }, error -> {
                                        // Transaction failed and was automatically canceled.
                                        Log.e("UserProfile", "Error saving user profile", error);
                                    });

                                }

                                // get review text and overall rating AND if the source is not in-house please remove likes
                                Log.d("FIRST YELP SEARCH RESULTS", "" + TripreviewSearch);
                            }, throwable -> {
                                // Handle error
                                Log.d("TripAdvisor Error Getting Reviews", "Error occurred: " + throwable.getMessage());
                            });

                } catch (NumberFormatException e) {
                    System.out.println("Error converting to integer: " + e.getMessage());
                }
            }


            Log.d("Result length", "" + resultsyelp);

            if (resultsyelp.isEmpty()) {
                final String address = this.currRes.getAddress();
                final String ambience = this.currRes.getAmbience();
                final String cuisine = this.currRes.getCuisine();
                final String dietaryOptions = this.currRes.getDietaryOptions();
                final String closestLandmark = this.currRes.getClosestLandmark();
                final String priceRange = this.currRes.getPriceRange();
                final String username = this.currUser.getUsername();
                final String userImgLink = this.currUser.getUser_img_link();
                final String restaurantId = this.currRes.getRestaurantId();
                final String type = this.currRes.getType();
                final String userid = this.currUser.getUserId();
                yelpService.getReviews(this.currRes.getYelpId(), BuildConfig.YELP_API).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            this.YelpreviewSearch = result.getReviews();
                            for (YelpReviewResponse.YelpReviews rev : this.YelpreviewSearch) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Review review = realm.createObject(Review.class, new ObjectId());

                                        if (review != null) {
                                            review.setReview(rev.getText());
                                            review.setRating(Double.valueOf(rev.getRating()));
                                            review.setAddress(address);
                                            review.setAmbience(ambience);
                                            review.setCuisine(cuisine);
                                            review.setDietaryOptions(dietaryOptions);
                                            review.setClosestLandmark(closestLandmark);
                                            review.setPriceRange(priceRange);
                                            review.setUsername(username);
                                            review.setUserImgLink(userImgLink);
                                            review.setRestaurantId(restaurantId);
                                            review.setType(type);
                                            review.setSource("Yelp");
                                            review.setImgPostLink("");
                                            review.setUserId(userid);
                                        }


                                    }
                                }, () -> {
                                    // Transaction was a success.
                                    Log.v("UserProfile", "User profile saved successfully");
                                    fetchAndUpdateLiveData();
                                }, error -> {
                                    // Transaction failed and was automatically canceled.
                                    Log.e("UserProfile", "Error saving user profile", error);
                                });

                            }

                            Log.d("FIRST YELP SEARCH RESULTS", "" + YelpreviewSearch);
                        }, throwable -> {
                            // Handle error
                            Log.d("Yelp Error Getting Reviews", "Error occurred: " + throwable.getMessage());
                        });
            }
        }


    }

    public LiveData<Restaurant> getRestaurantLiveData() {
        return restaurantLiveData;

    }
}