package com.sutd.t4app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sutd.t4app.LoginSignUpActivity;
import com.sutd.t4app.databinding.ProfileSettingsBinding;
import com.sutd.t4app.ui.ProfileQuestions.UserProfileViewModel;

public class SettingFragment extends Fragment {
    private ProfileSettingsBinding binding;
    private UserProfileViewModel userProfileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ProfileSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userProfileViewModel= new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        binding.DeleteAccount.setOnClickListener(v -> {
            userProfileViewModel.deleteUserProfile();
            onDeleteSuccess();
        });




        return root;
    }
    private void onDeleteSuccess() {
        Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish(); // Ensure that the MainActivity stack is cleared
    }

}
