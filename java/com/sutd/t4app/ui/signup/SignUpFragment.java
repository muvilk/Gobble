package com.sutd.t4app.ui.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.sutd.t4app.R;
import com.sutd.t4app.databinding.FragmentHomeBinding;
import com.sutd.t4app.databinding.FragmentNotificationsBinding;
import com.sutd.t4app.ui.home.HomeFragmentViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import com.sutd.t4app.databinding.FragmentSignUpBinding;


// we will use FormValidation here to validate our user details, which we will do after setting

@AndroidEntryPoint
public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private SignUpFragmentViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText firstName = root.findViewById(R.id.firstNameEditText);
        TextInputLayout firstNameInputLayout = root.findViewById(R.id.firstNameTextInputLayout);

        EditText lastName = root.findViewById(R.id.lastNameEditText);
        TextInputLayout lastNameTextInputLayout = root.findViewById(R.id.lastNameEditTextTextInputLayout);

        EditText email = root.findViewById(R.id.emailEditText);
        TextInputLayout emailTextInputLayout = root.findViewById(R.id.emailTextInputLayout);

        EditText pass = root.findViewById(R.id.passwordEditText);
        TextInputLayout passTextInputLayout = root.findViewById(R.id.passwordTextInputLayout);

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(SignUpFragmentViewModel.class);


        Button submit = root.findViewById(R.id.signUpButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checking","cehcking");
                viewModel.validateAndSave(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),pass.getText().toString());

            }
        });
        viewModel.getFirstNameError().observe(getViewLifecycleOwner(), firstNameError -> {
            // will be fine coz if its invalid itll be null anyways
            firstNameInputLayout.setError(firstNameError);
        });
        viewModel.getLasttNameError().observe(getViewLifecycleOwner(), lastNameError -> {
            lastNameTextInputLayout.setError(lastNameError);

        });
        viewModel.getEmailError().observe(getViewLifecycleOwner(), emailError -> {
            emailTextInputLayout.setError(emailError);

        });
        viewModel.getPasswordError().observe(getViewLifecycleOwner(), passError -> {
            passTextInputLayout.setError(passError);
        });

        viewModel.getNavigationTrigger().observe(getViewLifecycleOwner(), nextFragmentTag -> {
            if (nextFragmentTag != null && !nextFragmentTag.isEmpty()) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_login_signup);
                navController.navigate(R.id.preferences_qn_navigation);
            }
        });


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.cleanUp();
        binding = null;

    }


}