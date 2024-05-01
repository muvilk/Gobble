package com.sutd.t4app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sutd.t4app.databinding.ActivityLoginSignupBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginSignUpActivity extends AppCompatActivity {
    private ActivityLoginSignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("check","LoginSignUp works");

        super.onCreate(savedInstanceState);

        binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_login_signup);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
