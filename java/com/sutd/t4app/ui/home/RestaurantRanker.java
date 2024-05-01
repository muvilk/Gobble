package com.sutd.t4app.ui.home;
/**
 * The `RestaurantRanker` class implements a ranking system for restaurants based on their score from ResturantRanking.
 */
import android.util.Log;

import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.UserProfile;

import java.util.*;
import java.util.stream.Collectors;

public class RestaurantRanker {
    private PriorityQueue<RestaurantScore> maxHeap;

    public RestaurantRanker() {
        // Initialize max heap with a comparator that orders RestaurantScores by score in descending order
        maxHeap = new PriorityQueue<>((score1, score2) -> Integer.compare(score2.getScore(), score1.getScore()));
    }

    public void addRestaurantScore(RestaurantScore restaurantScore) {
        maxHeap.add(restaurantScore);
        Log.d("PriorityQueueDebug", "Added to PriorityQueue: " + restaurantScore.getRestaurant().getName() + " with score " + restaurantScore.getScore());
    }

    public List<Restaurant> getRankedRestaurants() {
        List<Restaurant> rankedRestaurants = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            RestaurantScore currentScore = maxHeap.poll();
            // Log each restaurant score as it is extracted
            Log.d("PriorityQueueDebug", "Extracted from PriorityQueue: " + currentScore.getRestaurant().getName() + " with score " + currentScore.getScore());
            rankedRestaurants.add(currentScore.getRestaurant());
        }
        Log.d("PriorityQueueDebug", "Maxheap: " + rankedRestaurants);

        return rankedRestaurants;
    }

}