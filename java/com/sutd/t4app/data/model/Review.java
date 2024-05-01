package com.sutd.t4app.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;
public class Review extends RealmObject {
    @PrimaryKey
    @Required
    private ObjectId _id;

    private String Address;

    private String Ambience;

    private String ClosestLandmark;

    private String Cuisine;

    private String DietaryOptions;

    private Integer Likes;

    private String PriceRange;

    private String Restaurant_id;

    private String Review;

    private String Type;

    private String User_id;

    private String img_post_link;

    private Double rating;

    private String source;

    private String user_img_link;

    private String username;

    // Standard getters & setters
    public Review(){
        this.Likes = 0;

    }
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }

    public String getAddress() { return Address; }
    public void setAddress(String Address) { this.Address = Address; }

    public String getAmbience() { return Ambience; }
    public void setAmbience(String Ambience) { this.Ambience = Ambience; }

    public String getClosestLandmark() { return ClosestLandmark; }
    public void setClosestLandmark(String ClosestLandmark) { this.ClosestLandmark = ClosestLandmark; }

    public String getCuisine() { return Cuisine; }
    public void setCuisine(String Cuisine) { this.Cuisine = Cuisine; }

    public String getDietaryOptions() { return DietaryOptions; }
    public void setDietaryOptions(String DietaryOptions) { this.DietaryOptions = DietaryOptions; }

    public Integer getLikes() { return Likes; }
    public void setLikes(Integer Likes) { this.Likes = Likes; }

    public String getPriceRange() { return PriceRange; }
    public void setPriceRange(String PriceRange) { this.PriceRange = PriceRange; }

    public String getRestaurantId() { return Restaurant_id; }
    public void setRestaurantId(String Restaurant_id) { this.Restaurant_id = Restaurant_id; }

    public String getReview() { return Review; }
    public void setReview(String Review) { this.Review = Review; }

    public String getType() { return Type; }
    public void setType(String Type) { this.Type = Type; }

    public String getUserId() { return User_id; }
    public void setUserId(String User_id) { this.User_id = User_id; }

    public String getImgPostLink() { return img_post_link; }
    public void setImgPostLink(String img_post_link) { this.img_post_link = img_post_link; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getUserImgLink() { return user_img_link; }
    public void setUserImgLink(String user_img_link) { this.user_img_link = user_img_link; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

