package com.sutd.t4app.ui.reviews;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.UserProfile;
import com.sutd.t4app.databinding.FragmentDashboardBinding;
import com.sutd.t4app.databinding.FragmentRestuarantProfileBinding;
import com.sutd.t4app.ui.ProfileQuestions.UserProfileViewModel;
import com.sutd.t4app.ui.restaurant.ReviewListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@AndroidEntryPoint
public class ReviewsFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private ReviewViewModel viewModel;
    private SuggestionAdapter adapter;
    RatingBar foodrating;
    RatingBar serviceRating;
    RatingBar atmosRating;

    private Button uploadImageButton;
    private Uri imageUri;
    private ImageView selectedImage;
    private EditText reviews;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private UserProfileViewModel UserViewModel;
    private UserProfile cachedUserProfile;
    @Inject
    App realmApp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        UserViewModel= new ViewModelProvider(this).get(UserProfileViewModel.class);
        adapter = new SuggestionAdapter(new ArrayList<>(), R.layout.suggestion_item, restaurant -> {
            viewModel.selectRestaurant(restaurant); // Update ViewModel with the selected restaurant
            binding.searchEditText.setText(restaurant.getName());
            binding.suggestionRecyclerView.setVisibility(View.GONE);
        });
        binding.suggestionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.suggestionRecyclerView.setAdapter(adapter);




        EditText searchEditText = root.findViewById(R.id.searchEditText);
        RecyclerView suggestionRecyclerView = root.findViewById(R.id.suggestionRecyclerView);
        uploadImageButton=root.findViewById(R.id.uploadImageButton);
        uploadImageButton.setOnClickListener(v -> openImageChooser());
        selectedImage=root.findViewById(R.id.selectedImage);

        Button postreviewbutton= root.findViewById(R.id.post_review);

        viewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                adapter.updateData(restaurants); // Update adapter
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) { // Only query if there are at least 2 characters
                    suggestionRecyclerView.setVisibility(View.VISIBLE);
                    viewModel.updateSearchText(s.toString());
                } else {
                    suggestionRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        TextView foodRatingLabel = root.findViewById(R.id.foodRating).findViewById(R.id.categoryLabel);
        foodRatingLabel.setText("Food");

        foodrating = root.findViewById(R.id.foodRating).findViewById(R.id.categoryRatingBar);

        TextView serviceRatingLabel = root.findViewById(R.id.serviceRating).findViewById(R.id.categoryLabel);
        serviceRatingLabel.setText("Service");
        serviceRating = root.findViewById(R.id.serviceRating).findViewById(R.id.categoryRatingBar);

        TextView atmosphereRatingLabel = root.findViewById(R.id.atmosphereRating).findViewById(R.id.categoryLabel);
        atmosphereRatingLabel.setText("Atmosphere");
        atmosRating = root.findViewById(R.id.atmosphereRating).findViewById(R.id.categoryRatingBar);

        reviews = root.findViewById(R.id.reviewTextInput);

        postreviewbutton.setOnClickListener(v -> {

            submitReview(v);
        });




        return root;
    }


    private void openImageChooser() {
        // Launch the image picker using the Activity Result API
        imagePickerLauncher.launch("image/*");
    }


    private void submitReview(View v) {
        if (cachedUserProfile != null) {

            if (reviews.getText().toString().trim().isEmpty()) {
                reviews.setError("Please enter a restaurant name");
            }else {
                reviews.setError(null);
                double averageRating = (foodrating.getRating() + serviceRating.getRating() + atmosRating.getRating()) / 3.0;
                String review = String.valueOf(reviews.getText());
                int newReviewCount = cachedUserProfile.getReviewCount() + 1;

                cachedUserProfile.setReviewCount(newReviewCount); // Modify the cached copy
                UserViewModel.updateUserProfile(cachedUserProfile);


                if (imageUri != null) {
                    // Upload image to Google Cloud Storage
                    uploadImageToGCS(imageUri, averageRating, review);
                } else {
                    // If no image is selected, directly store review in MongoDB Realm
                    storeReviewInRealm(averageRating, review, null);
                }
                Navigation.findNavController(v).navigate(R.id.rest_back_to_home);
            }
        }
    }


    private void uploadImageToGCS(Uri imageUri, double averageRating, String review) {
        InputStream imageInputStream = null;
        InputStream inputStream = null;
        try {
            // Load the service account credentials from the JSON file
            inputStream = getContext().getAssets().open("gobble-418717-fd2747d68173.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

            // Build StorageOptions with the credentials
            StorageOptions storageOptions = StorageOptions.newBuilder().setCredentials(credentials).build();

            // Initialize Storage service using the options
            Storage storage = storageOptions.getService();

            // Specify the bucket name
            String bucketName = "gobblecdn";

            // Define a blob name (object name) for the uploaded image
            String blobName = "images/image_" + System.currentTimeMillis(); // Example blob name

            // Create BlobInfo object with the specified bucket name and blob name
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, blobName).build();

            // Get the ContentResolver
            ContentResolver contentResolver = getContext().getContentResolver();

            // Open an InputStream from the ContentResolver
            imageInputStream = contentResolver.openInputStream(imageUri);

            // Read the content of the image into a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Upload the image bytes to Google Cloud Storage
            Blob blob = storage.create(blobInfo, imageBytes);

            // Get the URL of the uploaded image
            String imageUrl = blob.getMediaLink();
            Log.d("image link","" +imageUrl);

            // Image uploaded successfully, store review in MongoDB Realm with image URL
            storeReviewInRealm(averageRating, review, imageUrl);

            // Close the InputStreams
            imageInputStream.close();
            inputStream.close();
    } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        } finally {
            try {
                if (imageInputStream != null) {
                    imageInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Method to store review in MongoDB Realm
    private void storeReviewInRealm(double averageRating, String review, String imageUrl) {
        // Store review information in MongoDB Realm
        viewModel.setRating(averageRating);
        viewModel.setReviewText(review);
        viewModel.setImageUrl(imageUrl);
        viewModel.addReview();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ActivityResultLauncher for image picking
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        selectedImage.setImageURI(imageUri);
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Assuming UserProfileViewModel is correctly instantiated
        UserViewModel.getUserProfilesLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                cachedUserProfile = userProfile; // Update the local cache whenever the data changes
                Log.d("ReviewFragment", "Cached user profile updated.");
            } else {
                Log.e("ReviewFragment", "Received null UserProfile.");
            }
        });
    }






}
