package com.sutd.t4app.ui.map;
/**
 * The MapFragment class in an Android app displays a WebView to show a custom map using Google Maps.
 */
import static androidx.databinding.DataBindingUtil.setContentView;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sutd.t4app.MainActivity;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;

import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback {



    private MapView mapView;
    private GoogleMap googleMap;
    private HomeFragmentViewModel homeViewmodel;

    private List<Restaurant> restaurantList;


    // TODO: Rename and change types of parameters


    public MapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewmodel= new ViewModelProvider(requireActivity()).get(HomeFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView=view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);




        return view;
    }
    private void addMarkersToMap(List<Restaurant> restaurantList) {
        if (googleMap != null) {
            for (Restaurant restaurant : restaurantList) {
                LatLng location = new LatLng(Double.parseDouble(restaurant.getLat()), Double.parseDouble(restaurant.getLng()));
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title(restaurant.getName());
                googleMap.addMarker(markerOptions);
            }

            // Set camera position to show all markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Restaurant restaurant : restaurantList) {
                LatLng location = new LatLng(Double.parseDouble(restaurant.getLat()), Double.parseDouble(restaurant.getLng()));
                builder.include(location);
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            googleMap.moveCamera(cameraUpdate);
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
        // Disable 3D buildings
        googleMap.setBuildingsEnabled(false);

        // Disable indoor maps
        googleMap.setIndoorEnabled(false);

        // Disable traffic layer (if not needed)
        googleMap.setTrafficEnabled(false);

        // Set map type (e.g., normal map type)
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        homeViewmodel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> list) {
                // Update UI or handle restaurant list change
                if (list != null && !list.isEmpty()) {
                    restaurantList=list;
                    // Now you have the restaurant list, you can add markers to the map
                    addMarkersToMap(restaurantList);
                }
            }
        });


        // Set marker click listener
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Show info window
                marker.showInfoWindow();
                return true; // Return true to consume the click event
            }
        });

        // Set info window click listener
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Retrieve the restaurant object associated with the clicked marker
                Restaurant clickedRestaurant = getRestaurantFromMarker(marker);
                if (clickedRestaurant != null) {
                    // Navigate to the restaurant page with the clicked restaurant object
                    navigateToRestaurantPage(clickedRestaurant);
                }
            }
        });

    }


    private Restaurant getRestaurantFromMarker(Marker marker) {
        // Iterate through the list of restaurants to find the one associated with the clicked marker
        for (Restaurant restaurant : restaurantList) {
            LatLng restaurantLocation = new LatLng(Double.parseDouble(restaurant.getLat()), Double.parseDouble(restaurant.getLng()));
            if (restaurantLocation.equals(marker.getPosition())) {
                return restaurant;
            }
        }
        return null; // Return null if no restaurant is associated with the clicked marker
    }
    private void navigateToRestaurantPage(Restaurant clickedRestaurant) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("restaurant", clickedRestaurant);
        Navigation.findNavController(requireView()).navigate(R.id.navigation_restaurant, bundle);
    }



}