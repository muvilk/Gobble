package com.sutd.t4app.ui.profile;
/**
 * The ProfileFragment class in an Android app handles user profile information and includes
 * functionality for logging out using Google Sign-In.
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.sutd.t4app.LoginSignUpActivity;
import com.sutd.t4app.R;
import com.sutd.t4app.databinding.FragmentNotificationsBinding;
import com.sutd.t4app.ui.ProfileQuestions.UserProfileViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.mongodb.App;
import io.realm.mongodb.User;


@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    @Inject
    App realmApp;
    private GoogleSignInClient mGoogleSignInClient;

    private FragmentNotificationsBinding binding;
    private Button questions;
    private Button EditProfile;
    private Button Settings;
    private TextView reviewCountTextView;
    private UserProfileViewModel viewModel;
    private TextView NameTextView;
    private TextView EmailTextView;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button logout = root.findViewById(R.id.Logout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("608019695893-le4ojn1imiute9040pj9mulgnhe6gkjt.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        Log.d("work?","Profile page");


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("work?","yes working cliking");
                signOut();
            }
        });
        reviewCountTextView = root.findViewById(R.id.reviewCountTextView);
        NameTextView=root.findViewById(R.id.SettingsName);
        EmailTextView=root.findViewById(R.id.SettingsEmail);
        viewModel= new ViewModelProvider(this).get(UserProfileViewModel.class);

//        String currentUserId = "bshfbefnwoef2121100101";
        viewModel.getUserProfilesLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                reviewCountTextView.setText(String.valueOf(userProfile.getReviewCount()));
                NameTextView.setText(userProfile.getUsername());
                EmailTextView.setText(userProfile.getEmail());

            }
        });




        questions=root.findViewById(R.id.ProfileQuestions);
        questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.toQuestionspage);
            }
        });
        EditProfile=root.findViewById(R.id.editProfilebutton);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.toeditProfile);
            }
        });

        Settings=root.findViewById(R.id.Settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.tosettings);
            }
        });




        return root;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    // Sign-out was successful, redirect to LoginActivity

                    Log.d("Log out", "Logged off from Google");
                });

        User user = realmApp.currentUser();
        if (user!=null){
            Log.d("signup check","all valid and user is NOT NULL");
            // that means we already this fella in the database naturally coz we have log him in
            // now we log out and use wtv he is providing
            user.logOutAsync(result -> {
                if (result.isSuccess()) {
                    Log.v("User", "Successfully logged out.");
                    Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
                    getActivity().finish();  // Call finish() to close the current activity
                    startActivity(intent);
                    // At this point, currentUser() will be null if there are no other users logged in.
                } else {
                    Log.e("User", "Failed to log out, reason: " + result.getError());
                }
            });

        }

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Force refresh user profile
        viewModel.getUserProfilesLiveData();  // Assuming you make this method public in ViewModel
    }

}