package com.sutd.t4app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sutd.t4app.R;
import com.sutd.t4app.databinding.EditProfileBinding;
import com.sutd.t4app.databinding.QuestionsBinding;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.ui.ProfileQuestions.UserProfileViewModel;

public class EditProfileFragment extends Fragment {

    private EditProfileBinding binding;
    private UserProfileViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = EditProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel= new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        loadUserProfile();

        binding.updateProfileButton.setOnClickListener(v -> updateProfile());




        return root;
    }

    private void loadUserProfile() {
        viewModel.getUserProfilesLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                binding.firstNameEditText.setText(userProfile.getUsername());
                binding.emailEditText.setText(userProfile.getEmail());
                binding.passwordEditText.setText(userProfile.getPassword());
            }
        });
    }

    private void updateProfile() {
        String username = binding.firstNameEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();

        viewModel.getUserProfilesLiveData().getValue().setUsername(username);
        viewModel.getUserProfilesLiveData().getValue().setEmail(email);
        viewModel.getUserProfilesLiveData().getValue().setPassword(password);

        viewModel.updateUserProfile(viewModel.getUserProfilesLiveData().getValue());

        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.backtoprofile);
    }


}
