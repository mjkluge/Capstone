package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    List<FoursquareResults> frs;
    private OnRestaurantListener orl;
    public RestaurantAdapter(List<FoursquareResults> frs, OnRestaurantListener orl){
        super();
        this.frs = frs;
        this.orl = orl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dish_row, parent, false);
        return new MyViewHolder(view, orl);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FoursquareResults result = frs.get(position);
        holder.name.setText(result.venue.name);
        holder.description.setText("Rating: " + result.venue.rating);
    }

    @Override
    public int getItemCount() {
        return frs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView description;
        OnRestaurantListener orl;
        public MyViewHolder(@NonNull View itemView, OnRestaurantListener orl) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            this.orl = orl;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            orl.onRestaurantClick(getAdapterPosition());
        }
    }
    public interface OnRestaurantListener{
        void onRestaurantClick(int position);
    }
}