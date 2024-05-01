package com.sutd.t4app.data.model;
/**
 * The UserProfile class represents a user profile with various attributes and corresponding getters
 * and setters.
 */
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserProfile extends RealmObject {
    @PrimaryKey @Required private ObjectId _id;
    private int reviewCount;

    private String userId;
    private String username;
    private Integer rep;
    private String email;
    private String password;
    private String cuisinePreferences;
    private String dietaryPreferences;
    private String locationPreference;
    private String budgetPreference;
    private String foodPreferences;
    private String cuisineAdventurousness;
    private String spicyTolerance;
    private String sweetTooth;
    private String specialtyDishes;
    private String healthWellnessImportance;
    private String ambiencePreferences;
    private String mealTimePreferences;
    private String ingredientPreferences;
    private String ingredientDislikes;
    private String user_img_link;


    // Default constructor
    public UserProfile() {
        this._id= new ObjectId();
        this.reviewCount=0; //initialise to zero for new users
    }
    public Integer getRep(){return this.rep;}
    public void setRep(Integer newrep){this.rep = newrep;}

//     Getters and Setters
    public int getReviewCount(){return reviewCount;}
    public void setReviewCount(int reviewCount){this.reviewCount=reviewCount;}
    public ObjectId get_id() {
        return _id;
    }
    public String getPassword(){
        return password;
    }
     public void setPassword(String password){
        this.password=password;
     }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCuisinePreferences() {
        return cuisinePreferences;
    }

    public void setCuisinePreferences(String cuisinePreferences) {
        this.cuisinePreferences = cuisinePreferences;
    }

    public String getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(String dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    public String getLocationPreference() {
        return locationPreference;
    }

    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public String getBudgetPreference() {
        return budgetPreference;
    }

    public void setBudgetPreference(String budgetPreference) {
        this.budgetPreference = budgetPreference;
    }

    public String getFoodPreferences() {
        return foodPreferences;
    }

    public void setFoodPreferences(String foodPreferences) {
        this.foodPreferences = foodPreferences;
    }

    public String getCuisineAdventurousness() {
        return cuisineAdventurousness;
    }

    public void setCuisineAdventurousness(String cuisineAdventurousness) {
        this.cuisineAdventurousness = cuisineAdventurousness;
    }

    public String getSpicyTolerance() {
        return spicyTolerance;
    }

    public void setSpicyTolerance(String spicyTolerance) {
        this.spicyTolerance = spicyTolerance;
    }

    public String getSweetTooth() {
        return sweetTooth;
    }

    public void setSweetTooth(String sweetTooth) {
        this.sweetTooth = sweetTooth;
    }

    public String getSpecialtyDishes() {
        return specialtyDishes;
    }

    public void setSpecialtyDishes(String specialtyDishes) {
        this.specialtyDishes = specialtyDishes;
    }

    public String getHealthWellnessImportance() {
        return healthWellnessImportance;
    }

    public void setHealthWellnessImportance(String healthWellnessImportance) {
        this.healthWellnessImportance = healthWellnessImportance;
    }

    public String getAmbiencePreferences() {
        return ambiencePreferences;
    }

    public void setAmbiencePreferences(String ambiencePreferences) {
        this.ambiencePreferences = ambiencePreferences;
    }

    public String getMealTimePreferences() {
        return mealTimePreferences;
    }

    public void setMealTimePreferences(String mealTimePreferences) {
        this.mealTimePreferences = mealTimePreferences;
    }


    public String  getIngredientPreferences() {
        return ingredientPreferences;
    }

    public void setIngredientPreferences(String ingredientPreferences) {
        this.ingredientPreferences = ingredientPreferences;
    }

    public String getIngredientDislikes() {
        return ingredientDislikes;
    }

    public void setIngredientDislikes(String ingredientDislikes) {
        this.ingredientDislikes = ingredientDislikes;
    }
    public String getUser_img_link(){return this.user_img_link;}
    public void setUser_img_link(String userimglink){this.user_img_link=userimglink;}
}
