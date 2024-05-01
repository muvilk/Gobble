package com.sutd.t4app.ui.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.data.model.Review;
import com.sutd.t4app.ui.home.RestaurantExploreAdapter;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder>{
    private List<Review> reviewList;
    private int layoutID;
    public ReviewListAdapter(List<Review> reviewList, int layoutID) {
        this.reviewList = reviewList;
        this.layoutID = layoutID;
    }


    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.layoutID, parent, false);
        Log.d("INflated or what","yes");
        return new ReviewListAdapter.ViewHolder(view, reviewList);
    }
    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ViewHolder holder, int position) {
        Log.d("We are reading position","index " + position);
        Log.d("Review state","RES: " + reviewList);
        if(reviewList.size() > 0){

            Review review = reviewList.get(position);
            Log.d("AdapterDebug", "Binding restaurant at position " + position + ": " + review.getReview());

            if ("In-House".equals(review.getSource())){
                holder.Username.setText(review.getUsername());
                Log.d("DEBUGGING"," resName " + holder.Username.getText() );

                if(holder.UserReview != null) {
                    holder.UserReview.setText(review.getReview());
                    Log.d("DEBUGGING"," resLandmark " + holder.UserReview.getText() );
                }
                if(holder.UserRatings != null) {
                    holder.UserRatings.setRating((float) review.getRating().doubleValue());
                    Log.d("DEBUGGING", " resCuisine " + holder.UserRatings.getRating());
                }

                if(holder.ReviewLikes != null){
                    holder.ReviewLikes.setText(review.getLikes().toString());

                }
                Picasso.get().setLoggingEnabled(true);


                if(review.getUserImgLink()== "" ||review.getUserImgLink()== null ){
                    holder.imageViewUser.setImageResource(R.drawable.defaultuser);

                }else{
                    Picasso.get()
                            .load(review.getUserImgLink()) // Assuming `getImageUrl()` is a method in your `Review` class
                            .resize(350, 170)  // specify your desired size
                            .centerInside()
                            .into(holder.imageViewUser);
                    holder.imageViewUser.setVisibility(View.VISIBLE);

                }
                Log.d("CHECK HEREE ASDADS", "" + review.getImgPostLink());
                if(review.getImgPostLink()== "" || review.getImgPostLink()== null){
                    // make the postImageView invisible
                    holder.postImageView.setVisibility(View.GONE);

                }else{
                    Picasso.get()
                            .load(review.getImgPostLink()) // Assuming `getImageUrl()` is a method in your `Review` class
                            .resize(350, 170)  // specify your desired size
                            .centerInside()
                            .into(holder.postImageView);
                    holder.postImageView.setVisibility(View.VISIBLE);

                }
            }
            if ("Yelp".equals(review.getSource())){
                holder.Username.setText("Yelp User");
                holder.UserReview.setText(review.getReview());
                holder.UserRatings.setRating((float) review.getRating().doubleValue());
                holder.imageViewUser.setImageResource(R.drawable.yelpemblem);
                holder.postImageView.setVisibility(View.GONE);
                holder.ReviewLikes.setVisibility(View.GONE);
                holder.Likeicon.setVisibility(View.GONE);
            }
            if ("TripAdvisor".equals(review.getSource())){
                holder.Username.setText("TripAdvisor User");
                holder.UserReview.setText(review.getReview());
                holder.UserRatings.setRating((float) review.getRating().doubleValue());
                holder.imageViewUser.setImageResource(R.drawable.tripadvisor_icon_logo);
                holder.postImageView.setVisibility(View.GONE);
                holder.ReviewLikes.setVisibility(View.GONE);
                holder.Likeicon.setVisibility(View.GONE);
            }


        }

    }
    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public void updateData(List<Review> newReviews) {
        this.reviewList.clear();
        this.reviewList.addAll(newReviews);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Username;
        TextView UserReview;
        RatingBar UserRatings;
        ImageView imageViewUser;
        TextView ReviewLikes;
        ImageView postImageView;
        ImageView Likeicon;




        ViewHolder(View view, List<Review> reviewList) {
            super(view);

            Username = view.findViewById(R.id.Username);
            UserReview = view.findViewById(R.id.User_review);
            UserRatings = view.findViewById(R.id.user_rating);
            ReviewLikes = view.findViewById(R.id.likeCountTextView);
            imageViewUser = view.findViewById(R.id.imageViewUser);
            postImageView = view.findViewById(R.id.postImageView);
            Likeicon = view.findViewById(R.id.likeButton);


        }
    }
}
