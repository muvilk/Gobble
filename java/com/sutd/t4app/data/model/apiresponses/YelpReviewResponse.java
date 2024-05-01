package com.sutd.t4app.data.model.apiresponses;


import java.util.List;

public class YelpReviewResponse {
    private List<YelpReviews> reviews;
    private int total;
    private List<String> possibleLanguages;

    // Getters and Setters
    public List<YelpReviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<YelpReviews> reviews) {
        this.reviews = reviews;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getPossibleLanguages() {
        return possibleLanguages;
    }

    public void setPossibleLanguages(List<String> possibleLanguages) {
        this.possibleLanguages = possibleLanguages;
    }

    public static class YelpReviews {
        private String id;
        private String url;
        private String text;
        private int rating;
        private String timeCreated;
        private User user;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getTimeCreated() {
            return timeCreated;
        }

        public void setTimeCreated(String timeCreated) {
            this.timeCreated = timeCreated;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public static class User {
        private String id;
        private String profileUrl;
        private String imageUrl;
        private String name;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
