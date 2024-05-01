/**
 * The ReviewLocationResponse class represents a response object containing details of a review for a
 * location, including user information and subratings.
 */
package com.sutd.t4app.data.model.apiresponses;
import java.util.List;
import java.util.Map;

public class ReviewLocationResponse {
    private List<ReviewLocationResponse.TripReviews> data;

    // Getters and Setters
    public List<ReviewLocationResponse.TripReviews> getData() {
        return data;
    }

    public void setData(List<ReviewLocationResponse.TripReviews> data) {
        this.data = data;
    }
    public class TripReviews{
        private long id;
        private String lang;
        private long location_id;
        private String published_date;
        private int rating;
        private int helpful_votes;
        private String rating_image_url;
        private String url;
        private String text;
        private String title;
        private String trip_type;
        private String travel_date;
        private User user;
        private Map<String, Subrating> subratings;

        // Getters and setters

        public String gettext(){return this.text;}

        public int getRating(){return this.rating;}

    }



    public static class User {
        private String username;
        private UserLocation user_location;
        private Avatar avatar;

        // Getters and setters
    }

    public static class UserLocation {
        private String id;
        private String name;

        // Getters and setters
    }

    public static class Avatar {
        private String thumbnail;
        private String small;
        private String medium;
        private String large;
        private String original;

        // Getters and setters
    }

    public static class Subrating {
        private String name;
        private String rating_image_url;
        private int value;
        private String localized_name;

        // Getters and setters
    }
}
