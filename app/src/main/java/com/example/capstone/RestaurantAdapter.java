package com.example.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    List<FoursquareResults> FoursquareResults;

    public RestaurantAdapter(Context applicationContext, List<FoursquareResults> frs) {
        FoursquareResults = frs;
    }

    public RestaurantAdapter() {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dish_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.restaurantName.setText(FoursquareResults.get(position).venue.name);
    holder.restaurantDescription.setText("Rating: " + FoursquareResults.get(position).venue.rating);
    }

    @Override
    public int getItemCount() {
        return FoursquareResults.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName, restaurantDescription;
        ImageView restaurantImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.name);
            restaurantDescription = itemView.findViewById(R.id.description);
            restaurantImage = itemView.findViewById(R.id.imageButton1);
        }
    }
}
