package com.sutd.t4app.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.Restaurant;
import com.sutd.t4app.data.model.TikTok;

import java.util.ArrayList;
import java.util.List;

public class TikTokAdapter extends RecyclerView.Adapter<TikTokAdapter.ViewHolder> {

    private List<TikTok> tikTokList= new ArrayList<>();
    private Activity context;
    private int layoutID;
    private OnTikTokClickListener listener;

    public interface OnTikTokClickListener {
        void onRestaurantSelected(TikTok tiktok);
        void onTikTokLinkSelected(String url);
    }

    public TikTokAdapter(List<TikTok> tikTokList,int layoutID, OnTikTokClickListener listener) {
        this.layoutID=layoutID;
        this.tikTokList = tikTokList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutID, parent, false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TikTok tikTok = tikTokList.get(position);
        Log.d("TikTokAdapter", "Binding view holder for position " + position + " with TikTok: " + tikTok.getNameTikTok());
        holder.bind(tikTok);

    }

    public void setOnTikTokClickListener(OnTikTokClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        int count= tikTokList != null ? tikTokList.size() : 0;
        Log.d("TikTokAdapter", "Item count: " + count);
        return count;
    }

    public void updateDataTikTok(List<TikTok> newTiktoks){
        tikTokList.clear();
        tikTokList.addAll(newTiktoks);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewNameTikTok;
        OnTikTokClickListener listener;


        ViewHolder(View itemView,OnTikTokClickListener listener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageViewTikTok);
            this.textViewNameTikTok = itemView.findViewById(R.id.textViewNameTiktok);
            this.listener = listener;
        }

        void bind(TikTok tikTok) {
            Picasso.get()
                    .load(tikTok.getImg())
                    .placeholder(R.drawable.food2t4app)  // Placeholder image while loading
                    .into(imageView);
            textViewNameTikTok.setText(tikTok.getNameTikTok());
            itemView.setOnClickListener(v -> {
                showPopupMenu(v, tikTok);
            });
        }

        private void openLinkInCustomTab(String url, Context context) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        }

        private void showPopupMenu(View view, TikTok tikTok) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_go_to_tiktok) {
                    listener.onTikTokLinkSelected(tikTok.getLink());
                    return true;
                } else if (id == R.id.action_go_to_restaurant) {
                    Log.d("TikTokAdapter", "Restaurant ID selected: " + tikTok);
                    listener.onRestaurantSelected(tikTok);
                    return true;
                } else {
                    return false;
                }
            });
            popup.show();
        }

    }
}