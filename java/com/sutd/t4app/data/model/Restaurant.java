/**
 * The `Restaurant` class in Java represents a model for restaurant data with various attributes and
 * implements the `Parcelable` interface for object serialization.
 */
package com.sutd.t4app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.*;
import org.bson.types.ObjectId;
public class Restaurant extends RealmObject implements Parcelable {
    @PrimaryKey @Required private ObjectId _id;
    private String Address;
    private String Cuisine;
    private String Name;
    private Double Ratings;
    private String Type;
    private String ClosestLandmark;
    private String imgMainURL;
    private String TotalReviews;
    private String TopMenu1;
    private String TopMenu2;
    private String TopMenu3;
    private String TopMenu4;
    private String DietaryOptions;
    private String PriceRange;
    private Double FoodRating;
    private Double ServiceRating;
    private Double AmbienceRating;
    private String Ambience;
    private String lat; // Stored as String for simplified data
    private String lng;
    private String UserId1;
    private String Review1;
    private Double ReviewRating1;
    private String UserId2;
    private String Review2;
    private Double ReviewRating2;

    private String Description;
    private String Area;
    private String TripAdvisorId;
    private String YelpId;
    private String RestaurantId;



    // Standard getters & setters

    public Restaurant(){}

    protected Restaurant(Parcel in) {
        Address = in.readString();
        Cuisine = in.readString();

        Name = in.readString();
        if (in.readByte() == 0) {
            Ratings = null;
        } else {
            Ratings = in.readDouble();
        }
        Description= in.readString();
        TotalReviews=in.readString();
        TopMenu1=in.readString();
        TopMenu2=in.readString();
        TopMenu3=in.readString();
        TopMenu4=in.readString();
        DietaryOptions = in.readString();
        PriceRange = in.readString();
        FoodRating = in.readDouble();
        ServiceRating = in.readDouble();
        AmbienceRating = in.readDouble();
//        Ambience = new RealmList<>();
//        Ambience.addAll(in.createStringArrayList());
        lat = in.readString();
        lng = in.readString();
        Type = in.readString();
        ClosestLandmark = in.readString();
        imgMainURL=in.readString();
        UserId1 = in.readString();
        Review1 = in.readString();
        ReviewRating1 = in.readDouble();
        UserId2 = in.readString();
        Review2 = in.readString();
        ReviewRating2 = in.readDouble();
        Ambience=in.readString();
        Area=in.readString();
        TripAdvisorId=in.readString();
        YelpId=in.readString();
        RestaurantId = in.readString();

    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
    public String getTripAdvisorId(){return this.TripAdvisorId;}
    public void setTripAdvisorId(String newid){this.TripAdvisorId=newid;}
    public String getRestaurantId(){return this.RestaurantId;}
    public void setRestaurantId(String RestaurantId){this.RestaurantId=RestaurantId;}

    public String getYelpId(){return this.YelpId;}
    public void setYelpId(String newid){this.YelpId=newid;}


    public String getImgMainURL(){
        return this.imgMainURL;
    }
    public void setImgMainURL(String url){
        this.imgMainURL = url;
    }
    public ObjectId getId() { return this._id; }
    public void setId(ObjectId _id) { this._id = _id; }
    public String getAddress() { return this.Address; }
    public void setAddress(String Address) { this.Address = Address; }
    public String getCuisine() { return this.Cuisine; }
    public void setCuisine(String Cuisine) { this.Cuisine = Cuisine; }
    public String getName() { return this.Name; }
    public void setName(String Name) { this.Name = Name; }
    public Double getRatings() { return this.Ratings; }
    public void setRatings(Double Ratings) { this.Ratings = Ratings; }
    public String getType() { return this.Type; }
    public void setType(String Type) { this.Type = Type; }
    public String getClosestLandmark(){return this.ClosestLandmark;}
    public void setClosestLandmark(String ClosestLandmark){this.ClosestLandmark = ClosestLandmark;}
    public String getDescription() {return Description;}
    public void setDescription(String description) {Description = description;}
    public String getTotalReviews() {
        return TotalReviews;
    }

    public void setTotalReviews(String totalReviews) {
        TotalReviews = totalReviews;
    }
    public String getTopMenu1() {
        return TopMenu1;
    }

    public void setTopMenu1(String topMenu1) {
        TopMenu1 = topMenu1;
    }

    public String getTopMenu2() {
        return TopMenu2;
    }

    @Override
    public String toString() {
        return Name;
    }

    public void setTopMenu2(String topMenu2) {
        TopMenu2 = topMenu2;
    }

    public String getTopMenu3() {
        return TopMenu3;
    }

    public void setTopMenu3(String topMenu3) {
        TopMenu3 = topMenu3;
    }

    public String getTopMenu4() {
        return TopMenu4;
    }

    public void setTopMenu4(String topMenu4) {
        TopMenu4 = topMenu4;
    }

    public String getDietaryOptions() {
        return DietaryOptions;
    }

    public void setDietaryOptions(String dietaryOptions) {
        DietaryOptions = dietaryOptions;
    }

    public String getPriceRange() {
        return PriceRange;
    }

    public void setPriceRange(String priceRange) {
        PriceRange = priceRange;
    }

    public Double getFoodRating() {
        return FoodRating;
    }

    public void setFoodRating(Double foodRating) {
        FoodRating = foodRating;
    }

    public Double getServiceRating() {
        return ServiceRating;
    }

    public void setServiceRating(Double serviceRating) {
        ServiceRating = serviceRating;
    }

    public Double getAmbienceRating() {
        return AmbienceRating;
    }

    public void setAmbienceRating(Double ambienceRating) {
        AmbienceRating = ambienceRating;
    }

    public String getAmbience() {
        return Ambience;
    }

    public void setAmbience(String ambience) {
        Ambience = ambience;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getUserId1() {
        return UserId1;
    }

    public void setUserId1(String userId1) {
        UserId1 = userId1;
    }

    public String getReview1() {
        return Review1;
    }

    public void setReview1(String review1) {
        Review1 = review1;
    }

    public Double getReviewRating1() {
        return ReviewRating1;
    }

    public void setReviewRating1(Double reviewRating1) {
        ReviewRating1 = reviewRating1;
    }

    public String getUserId2() {
        return UserId2;
    }

    public void setUserId2(String userId2) {
        UserId2 = userId2;
    }

    public String getReview2() {
        return Review2;
    }

    public void setReview2(String review2) {
        Review2 = review2;
    }

    public Double getReviewRating2() {
        return ReviewRating2;
    }

    public void setReviewRating2(Double reviewRating2) {
        ReviewRating2 = reviewRating2;
    }
    public String getArea(){return Area;}
    public void setArea(String area){Area=area;}



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(Address);
        dest.writeString(Cuisine);
        dest.writeString(Name);
        if (Ratings == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Ratings);
        }
        dest.writeString(Description);
        dest.writeString(TotalReviews);
        dest.writeString(TopMenu1);
        dest.writeString(TopMenu2);
        dest.writeString(TopMenu3);
        dest.writeString(TopMenu4);
        dest.writeString(DietaryOptions);
        dest.writeString(PriceRange);
        dest.writeDouble(FoodRating);
        dest.writeDouble(ServiceRating);
        dest.writeDouble(AmbienceRating);
        // dest.writeStringList(Ambience);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(Type);
        dest.writeString(ClosestLandmark);
        dest.writeString(imgMainURL);
        dest.writeString(UserId1);
        dest.writeString(Review1);
        dest.writeDouble(ReviewRating1);
        dest.writeString(UserId2);
        dest.writeString(Review2);
        dest.writeDouble(ReviewRating2);
        dest.writeString(Ambience);
        dest.writeString(Area);
        dest.writeString(RestaurantId);
    }
}
