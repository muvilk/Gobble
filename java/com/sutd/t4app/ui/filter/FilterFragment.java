package com.sutd.t4app.ui.filter;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.slider.Slider;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.databinding.FilterBottomUpBinding;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;
import com.sutd.t4app.ui.reviews.ReviewViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.sync.SyncConfiguration;

import com.sutd.t4app.utility.RealmUtility;

import javax.inject.Inject;

@AndroidEntryPoint
/*
 * The `FilterFragment` class in an Android app allows users to apply various filters to search for
 * restaurants based on cuisine, dietary options, location, price range, and star ratings.
 */
public class FilterFragment extends Fragment {
    @Inject
    App realmApp;

    private CheckBox americanCheckbox, asianCheckbox, asianFusionCheckbox, breakfastCheckbox, chineseCheckbox, mexicanCheckbox, italianCheckbox, thaiCheckbox, indianCheckbox, japaneseCheckBox;
    private CheckBox glutenFreeCheckbox, halalCheckbox, veganFriendlyCheckbox, vegetarianCheckbox, seafoodCheckBox, healthyCheckBox;
    private CheckBox centralCheckbox, NorthCheckBox, NorthEastCheckBox, WestCheckBox, EastCheckBox;
    private Slider priceSlider;
    private ImageView filterStar1, filterStar2, filterStar3, filterStar4, filterStar5;
    private Button showResultsButton;

    private List<String> selectedFilter = new ArrayList<>();
    private int selectedPrice = 0;
    private int overallStarRating = 0;
    private ReviewViewModel viewModel;
    private FilterBottomUpBinding binding;
    private Realm realm;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FilterBottomUpBinding.inflate(inflater, container, false);
        initializeRealm();
        return binding.getRoot();

    }
    /**
     * The `initializeRealm` method initializes a Realm instance asynchronously and updates UI
     * components based on filtered results.
     */
    private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                // Obtain the Realm instance asynchronously based on the provided configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(@NonNull Realm realm) {
                        FilterFragment.this.realm = realm;
                        // Now that Realm is initialized, you can proceed with setting up UI components that rely on Realm
                        List<Restaurant> filteredRestaurants = applyFilters();
                        getActivity().runOnUiThread(() -> {
                            // Update UI components here based on the filtered results
                            Toast.makeText(getContext(), "Filtered Restaurants: " + filteredRestaurants.size(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }

            @Override
            public void onError(@NonNull Exception e) {
                // Handle error in obtaining Realm configuration
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Initialize views
        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        americanCheckbox = view.findViewById(R.id.AmericanCheckbox);
        asianCheckbox = view.findViewById(R.id.AsianCheckbox);
        asianFusionCheckbox = view.findViewById(R.id.AsianFusionCheckbox);
        breakfastCheckbox = view.findViewById(R.id.BreakfastCheckbox);
        chineseCheckbox = view.findViewById(R.id.ChineseCheckbox);
        italianCheckbox=view.findViewById(R.id.ItalianCheckbox);
        mexicanCheckbox= view.findViewById(R.id.MexicanCheckbox);
        thaiCheckbox=view.findViewById(R.id.ThaiCheckbox);
        indianCheckbox=view.findViewById(R.id.IndianCheckbox);
        japaneseCheckBox=view.findViewById(R.id.JapaneseCheckbox);

        glutenFreeCheckbox = view.findViewById(R.id.GlutenFreeCheckbox);
        halalCheckbox = view.findViewById(R.id.HalalCheckbox);
        veganFriendlyCheckbox = view.findViewById(R.id.VeganFriendlyCheckbox);
        vegetarianCheckbox = view.findViewById(R.id.VegetarianCheckbox);
        seafoodCheckBox= view.findViewById(R.id.SeafoodCheckbox);
        healthyCheckBox=view.findViewById((R.id.HealthyCheckbox));


        priceSlider = view.findViewById(R.id.priceSlider);
        filterStar1 = view.findViewById(R.id.filterStar1);
        filterStar2 = view.findViewById(R.id.filterStar2);
        filterStar3 = view.findViewById(R.id.filterStar3);
        filterStar4 = view.findViewById(R.id.filterStar4);
        filterStar5 = view.findViewById(R.id.filterStar5);

        centralCheckbox=view.findViewById(R.id.CentralCheckbox);
        NorthCheckBox=view.findViewById(R.id.NorthCheckbox);
        NorthEastCheckBox=view.findViewById(R.id.NorthEastCheckbox);
        WestCheckBox=view.findViewById(R.id.WestCheckbox);
        EastCheckBox=view.findViewById(R.id.EastCheckbox);

        showResultsButton = view.findViewById(R.id.showResultsButton);

        // Set onClickListener for checkboxes
//        setupCheckbox(topRatedCheckbox, selectedFilter);
//        setupCheckbox(recommendedCheckbox, selectedFilter);
//        setupCheckbox(nearMeCheckbox, selectedFilter);
//        setupCheckbox(americanCheckbox, selectedFilter);
//        setupCheckbox(asianCheckbox, selectedFilter);
//        setupCheckbox(asianFusionCheckbox, selectedFilter);
//        setupCheckbox(breakfastCheckbox, selectedFilter);
//        setupCheckbox(chineseCheckbox, selectedFilter);
//        setupCheckbox(glutenFreeCheckbox, selectedFilter);
//        setupCheckbox(halalCheckbox, selectedFilter);
//        setupCheckbox(vegetarianCheckbox, selectedFilter);
//        setupCheckbox(veganFriendlyCheckbox, selectedFilter);

        // Set listener for price slider
        priceSlider.addOnChangeListener((slider, value, fromUser) -> selectedPrice = (int) value);

        // Set onClickListener for star rating
        filterStar1.setOnClickListener(v -> setRating(filterStar1, 1, "filter"));
        filterStar2.setOnClickListener(v -> setRating(filterStar2, 2, "filter"));
        filterStar3.setOnClickListener(v -> setRating(filterStar3, 3, "filter"));
        filterStar4.setOnClickListener(v -> setRating(filterStar4, 4, "filter"));
        filterStar5.setOnClickListener(v -> setRating(filterStar5, 5, "filter"));

        FilterViewModel filterViewModel= new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        // Set onClickListener for show results button
        showResultsButton.setOnClickListener(v -> {
            List<Restaurant> filteredRestaurants = applyFilters();
            Toast.makeText(getContext(), "Filtered Restaurants: " + filteredRestaurants.size(), Toast.LENGTH_SHORT).show();
            //add navigation back to home later
            filterViewModel.setFilteredRestaurants(filteredRestaurants);
            Navigation.findNavController(v).navigate(R.id.rest_back_to_home);
        });
    }

//    private void setupCheckbox(CheckBox checkBox, List<String> selectedFilter) {
//        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            // Handle checkbox state change
//            if (isChecked) {
//                selectedFilter.add(checkBox.getText().toString());
//            } else {
//                selectedFilter.remove(checkBox.getText().toString());
//            }
//        });
//    }

    private int updateRating(int currentRating, int rating, ImageView clickedStar, ImageView... stars) {
        // Toggle the clicked star and update UI
        if (currentRating == rating) {
            currentRating = 0;
        } else {
            currentRating = rating;
        }

        // Reset all stars to empty
        for (ImageView star : stars) {
            star.setImageResource(R.drawable.star_empty);
        }

        // Fill stars up to the selected rating
        for (ImageView star : stars) {
            if (Integer.parseInt(star.getTag().toString()) <= currentRating) {
                star.setImageResource(R.drawable.star_fill);
            }
        }

        return currentRating;
    }
    private void setRating(ImageView star, int rating, String category) {
        switch (category) {
            case "filter":
                overallStarRating = updateRating(overallStarRating, rating, star, filterStar1, filterStar2, filterStar3, filterStar4, filterStar5);
                viewModel.setFoodRating(overallStarRating);
                break;
            
        }
    }

    // The below Java code is a method named `applyFilters` that is responsible for applying filters to
    // a list of restaurants.

    private List<Restaurant> applyFilters() {
        if (realm == null) {
            Log.e("FilterFragment", "Realm instance is not initialized yet.");
            return new ArrayList<>();
        }// Return an empty list or handle this case as needed
        RealmQuery<Restaurant> query = realm.where(Restaurant.class);
        RealmResults<Restaurant> allRestaurants = query.findAll(); // Get all restaurants before applying filters

        // Log the initial size of the restaurant list
        Log.d("InitialData", "Initial number of restaurants: " + allRestaurants.size());
        // TODO: Problem: it seems like it is not pulling any data  "Initial number of restaurants: 0"



        // Cuisine filters: Checking if the 'Cuisine' string contains any of the selected cuisines
        List<String> selectedCuisines = new ArrayList<>();
        if (americanCheckbox.isChecked()) selectedCuisines.add("Western");
        if (asianCheckbox.isChecked()) selectedCuisines.add("Asian");
        if (asianFusionCheckbox.isChecked()) selectedCuisines.add("Asian Fusion");
        if(breakfastCheckbox.isChecked()) selectedCuisines.add("BreakFast");
        if(chineseCheckbox.isChecked()) selectedCuisines.add("Chinese");
        if(italianCheckbox.isChecked()) selectedCuisines.add("Italian");
        if(indianCheckbox.isChecked()) selectedCuisines.add("Indian");
        if(mexicanCheckbox.isChecked()) selectedCuisines.add("Mexican");
        if(thaiCheckbox.isChecked()) selectedCuisines.add("Thai");
        if(japaneseCheckBox.isChecked()) selectedCuisines.add("Japanese");

        // Adding query condition for cuisines
        if (selectedCuisines.size() > 0) {
            query.beginGroup();
            for (int i = 0; i < selectedCuisines.size(); i++) {
                if (i > 0) query.or(); // Only add 'or' condition if there's more than one cuisine selected
                query.contains("Cuisine", selectedCuisines.get(i));
            }
            query.endGroup();
        }

        // Dietary options filters: Similar approach as for cuisines
        List<String> selectedDietaryOptions = new ArrayList<>();
        if (glutenFreeCheckbox.isChecked()) selectedDietaryOptions.add("Gluten Free");
        if (halalCheckbox.isChecked()) selectedDietaryOptions.add("Halal");
        if(veganFriendlyCheckbox.isChecked()) selectedDietaryOptions.add("Vegan");
        if(vegetarianCheckbox.isChecked()) selectedDietaryOptions.add("Vegetarian");
        if(seafoodCheckBox.isChecked()) selectedDietaryOptions.add("Seafood");
        if(healthyCheckBox.isChecked()) selectedDietaryOptions.add("Healthy");

        // Adding query condition for dietary options
        if (selectedDietaryOptions.size() > 0) {
            query.beginGroup();
            for (int i = 0; i < selectedDietaryOptions.size(); i++) {
                if (i > 0) query.or(); // Only add 'or' condition if there's more than one dietary option selected
                query.contains("DietaryOptions", selectedDietaryOptions.get(i));
            }
            query.endGroup();
        }

        //Location Options filter
        List<String> selectedLocations= new ArrayList<>();
        if(centralCheckbox.isChecked()) selectedLocations.add("Central");
        if(NorthCheckBox.isChecked()) selectedLocations.add("North");
        if(NorthEastCheckBox.isChecked()) selectedLocations.add("North-East");
        if(WestCheckBox.isChecked()) selectedLocations.add("West");
        if(EastCheckBox.isChecked()) selectedLocations.add("East");

        if(selectedLocations.size()>0){
            query.beginGroup();
            for(int i = 0; i < selectedLocations.size(); i++) {
                if(i > 0) query.or();
                query.equalTo("Area", selectedLocations.get(i));
            }
            query.endGroup();
        }

        // Overall star rating filter
        if (overallStarRating > 0) {
            query.greaterThanOrEqualTo("Ratings", (double) overallStarRating);
        }

        if (selectedPrice > 0) {
            query.beginGroup();
            for (int price = selectedPrice; price <= 300; price += 10) {
                String priceString = "$" + price;
                query.or().equalTo("PriceRange", priceString);
            }
            query.endGroup();
        }




        RealmResults<Restaurant> result = query.findAll();
        Log.d("FilterDebug", "Filtered Query Result Size: " + result.size());

        // Before returning the filtered list, log the size of the list to debug
        List<Restaurant> filteredList = realm.copyFromRealm(result);
        Log.d("FilterDebug", "Number of filtered restaurants: " + filteredList.size());




        return filteredList; // Convert RealmResults to List
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null) {
            realm.close(); // Close Realm instance to avoid memory leaks
        }
        binding = null;
    }

}