package com.sutd.t4app.ui.home;
/**
 * The `RestaurantScore` class represents a pairing of a `Restaurant` object with an integer score.
 */

import com.sutd.t4app.data.model.Restaurant;

public class RestaurantScore {
    Restaurant restaurant;
    int score;

    public RestaurantScore(Restaurant restaurant, int score) {
        this.restaurant = restaurant;
        this.score = score;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public int getScore() {
        return this.score;
    }
}