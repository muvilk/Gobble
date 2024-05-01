package com.sutd.t4app.ui.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sutd.t4app.data.model.Restaurant;

import java.util.List;

public class FilterViewModel extends ViewModel {
    private MutableLiveData<List<Restaurant>> filteredRestaurantsLiveData = new MutableLiveData<>();

    public void setFilteredRestaurants(List<Restaurant> filteredRestaurants) {
        filteredRestaurantsLiveData.setValue(filteredRestaurants);
    }

    public LiveData<List<Restaurant>> getFilteredRestaurantsLiveData() {
        return filteredRestaurantsLiveData;
    }



}
