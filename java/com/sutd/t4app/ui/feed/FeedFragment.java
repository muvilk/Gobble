package com.sutd.t4app.ui.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.databinding.FeedPageBinding;
import com.sutd.t4app.ui.home.ReviewAdapter;
import com.sutd.t4app.utility.RealmUtility;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * The FeedFragment class is a fragment in an Android app that inflates a layout using data binding.
 */


@AndroidEntryPoint
public class FeedFragment extends Fragment {
    private FeedPageBinding binding;

    App realmApp;
    Realm realm;
    private ReviewAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding= FeedPageBinding.inflate(inflater,container,false);
        View root= binding.getRoot();
        adapter = new ReviewAdapter(new ArrayList<>(), R.layout.review_item );
        gatherreviews();
        return root;

    }


    public void gatherreviews() {
        // after initialisation check if there are any existing reviews from realm of the particular res
        if (realm != null) {
            RealmResults<Review> reviews = realm.where(Review.class).equalTo("Source", "In-House").findAllAsync();
            adapter.updateData(reviews);
        }


    }

        private void initializeRealm() {
        RealmUtility.getDefaultSyncConfig(realmApp, new RealmUtility.ConfigCallback() {
            @Override
            public void onConfigReady(SyncConfiguration configuration) {
                // Asynchronously initialize the Realm instance with the configuration
                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        FeedFragment.this.realm = realm;
                        Log.d("RestaurantFragmentViewModel", "Realm instance has been initialized successfully.");
                        gatherreviews(); // Observes data and updates LiveData
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                // Handle any errors, such as login failure
                Log.e("YourViewModel", "Error obtaining Realm configuration", e);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
