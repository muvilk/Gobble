package com.sutd.t4app;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sutd.t4app.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

import io.realm.Realm;

import io.realm.mongodb.App;

import io.realm.mongodb.Credentials;

import android.util.Log;


import javax.inject.Inject;
/**
 * The MainActivity class in this Android app sets up navigation using BottomNavigationView and
 * NavController.
 */

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_reviews, R.id.navigation_profile, R.id.navigation_map, R.id.navigation_restaurant, R.id.navigation_filter, R.id.navigation_questions)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.navigation_home) {
//                Log.d("NavigationView", "Item selected: " + item.getTitle());
//                if (navController.getCurrentDestination().getId() != R.id.navigation_home) {
//                    navController.navigate(R.id.navigation_home);
//                }
//                return true;
//            } else if (itemId == R.id.navigation_map) {
//                if (navController.getCurrentDestination().getId() != R.id.navigation_map) {
//                    navController.navigate(R.id.navigation_map);
//                }
//                return true;
//            } else {
//                return NavigationUI.onNavDestinationSelected(item, navController);
//            }
//        });
        navView.setOnItemSelectedListener(item -> {
            // Pop the back stack to the first fragment to clear previous navigation state
            // The first argument false will ensure the home fragment itself is not popped
            navController.popBackStack(navController.getGraph().getStartDestination(), false);

            // Navigate to the selected item ID
            navController.navigate(item.getItemId());
            return true;
        });





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}