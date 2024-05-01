package com.sutd.t4app.ui.home;
/**
 * The `RestaurantRanking` class in Java ranks restaurants based on user preferences such as cuisine,
 * dietary options, ambience, price range, and location.
 */

import android.util.Log;

import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantRanking {
    public List<RestaurantScore> rankRestaurants(List<Restaurant> restaurants, UserProfile userProfile) {
        List<RestaurantScore> ranker = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            int score = 0;
            Log.d("RankingDebug", "Ranking restaurant: " + restaurant.getName());

            // Cuisine Matching
            int cuisineMatchScore = matchCuisine(restaurant.getCuisine(), parseList(userProfile.getCuisinePreferences()));
            score += cuisineMatchScore * 10;
            Log.d("RankingDebug", "Restaurant: " + restaurant.getName() + ", Cuisine Match Score: " + cuisineMatchScore);

            // Dietary Options Matching
            int dietaryOptionsMatchScore = matchDietaryOptions(restaurant.getDietaryOptions(), parseList(userProfile.getDietaryPreferences()));
            score += dietaryOptionsMatchScore * 5;
            Log.d("RankingDebug", "Restaurant: " + restaurant.getName() + ", Dietary Options Match Score: " + dietaryOptionsMatchScore);

            // Ambience Matching
            int ambienceMatchScore = matchAmbience(parseList(restaurant.getAmbience()), parseList(userProfile.getAmbiencePreferences()));
            score += ambienceMatchScore * 3;
            Log.d("RankingDebug", "Restaurant: " + restaurant.getName() + ", Ambience Match Score: " + ambienceMatchScore);

            // Price Range Matching
            int priceRangeMatchScore = matchPriceRange(restaurant.getPriceRange(), userProfile.getBudgetPreference());
            score += priceRangeMatchScore;
            Log.d("RankingDebug", "Restaurant: " + restaurant.getName() + ", Price Range Match Score: " + priceRangeMatchScore);

            // Location Matching
            int locationMatchScore = matchLocation(restaurant.getArea(), userProfile.getLocationPreference());
            score += locationMatchScore;
            Log.d("RankingDebug", "Restaurant: " + restaurant.getName() + ", Location Match Score: " + locationMatchScore);

            Log.d("RankingDebug", "Restaurant: " + restaurant.getName() + ", TotalScore: " + score);

            // Add the restaurant score to the list
            ranker.add(new RestaurantScore(restaurant, score));
        }




        // Extract the sorted restaurants
        return ranker;
    }
    private List<String> parseList(String listString) {
        if (listString == null || listString.isEmpty()) {
            Log.e("DEBUG NULL OBJECT", "Input string is null or empty.");
            return Collections.emptyList(); // Return an empty list if the input string is null or empty
        }
        Log.d("DEBUG NULL OBJECT", "" + listString);
        // Assuming the listString is in the format "[item1,item2,...]"
        listString = listString.substring(1, listString.length() - 1); // Remove the brackets
        return Arrays.asList(listString.split(","));
    }

    private int matchCuisine(String restaurantCuisine, List<String> userPreferences) {
        int matches=0;
        for (String Cuisines : restaurantCuisine.split(",")){
            if(userPreferences.contains(Cuisines.trim())){
                matches++;
            }
        }
        return matches;
    }

    private int matchDietaryOptions(String restaurantOptions, List<String> userPreferences) {
        // Split the restaurant's dietary options and count matches
        int matches = 0;
        for (String option : restaurantOptions.split(",")) {
            if (userPreferences.contains(option.trim())) {
                matches++;
            }
        }
        return matches;
    }

    private int matchAmbience(List<String> restaurantAmbience, List<String> userPreferences) {
        int matches = 0;
        for (String ambience : restaurantAmbience) {
            if (userPreferences.contains(ambience)) {
                matches++;
            }
        }
        return matches;
    }

    private int matchPriceRange(String restaurantPrice, String userBudget) {

        if (restaurantPrice == null || userBudget == null) {
            Log.d("DEBUG NULL", "One of the price range strings is null.");
            return 0;
        }
        // Remove the dollar sign and parse the price and budget into integers
        double restaurantMaxPrice = Double.parseDouble(restaurantPrice.replace("$", ""));
        double userBudgetInt = Double.parseDouble(userBudget.replace("$", ""));

        if (userBudgetInt >= restaurantMaxPrice) {
            // Exact match adds more points
            return 10;

        } else if (userBudgetInt >= restaurantMaxPrice - 5 && userBudgetInt < restaurantMaxPrice) {
            // User's budget is within $5 range below the restaurant's price, adds fewer points
            return 5;
        }
        // No points added if the conditions are not met (user's budget is significantly below restaurant's price)
        return 0;
    }


    private int matchLocation(String restaurantLocation, String userLocationPreference) {
        return restaurantLocation.equalsIgnoreCase(userLocationPreference) ? 1 : 0;
    }

}