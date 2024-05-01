package com.sutd.t4app.ui.reviews;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.Restaurant;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {
    private List<Restaurant> resList;
    private int layoutID;
    private OnItemClickListener listener; // Define an interface for click events

    public interface OnItemClickListener {
        void onItemClick(Restaurant restaurant);
    }

    public SuggestionAdapter(List<Restaurant> resList, int layoutID, OnItemClickListener listener) {
        this.resList = resList;
        this.layoutID = layoutID;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutID, parent, false);
        return new ViewHolder(view, listener); // Pass the listener to the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant res = resList.get(position);
        holder.bind(res); // Bind restaurant data to the ViewHolder
    }

    @Override
    public int getItemCount() {
        return resList != null ? resList.size() : 0;
    }

    public void updateData(List<Restaurant> newRes) {
        this.resList.clear();
        this.resList.addAll(newRes);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;
        TextView restaurantInfo;
        RatingBar ratingBar;

        ViewHolder(View view, OnItemClickListener listener) {
            super(view);

            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantInfo = view.findViewById(R.id.restaurantInfo);
            ratingBar = view.findViewById(R.id.ratingBar);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick((Restaurant) itemView.getTag()); // Use the tag to get the clicked item
                }
            });
        }

        void bind(Restaurant res) {
            itemView.setTag(res); // Set tag for click handling
            restaurantName.setText(res.getName());
            restaurantInfo.setText(res.getAddress());
            ratingBar.setRating((float) res.getRatings().doubleValue());
        }
    }
}

