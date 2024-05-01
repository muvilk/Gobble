/**
 * The `YelpSearchResponse` class represents the response structure for Yelp search API, containing
 * information about businesses, total count, and region details.
 */
package com.sutd.t4app.data.model.apiresponses;

import java.util.List;

public class YelpSearchResponse {
    private List<Business> businesses;
    private int total;
    private Region region;

    // Getters and Setters
    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public static class Business {
        private String id;
        private String alias;
        private String name;
        private String image_url;
        private boolean is_closed;
        private String url;
        private int review_count;
        private List<Category> categories;
        private double rating;
        private Coordinates coordinates;
        private String price;
        private Location location;
        private String phone;
        private String display_phone;
        private double distance;

        // Getters and Setters...

        public String getId(){return this.id;}
    }

    public static class Category {
        private String alias;
        private String title;

        // Getters and Setters...
    }

    public static class Coordinates {
        private double latitude;
        private double longitude;

        // Getters and Setters...
    }

    public static class Location {
        private String address1;
        private String address2;
        private String address3;
        private String city;
        private String zip_code;
        private String country;
        private String state;
        private List<String> display_address;

        // Getters and Setters...
    }

    public static class Region {
        private Center center;

        // Getters and Setters...
    }

    public static class Center {
        private double longitude;
        private double latitude;

        // Getters and Setters...
    }
}