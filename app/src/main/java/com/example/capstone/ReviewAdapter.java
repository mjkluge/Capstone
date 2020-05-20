package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    public ReviewAdapter(){
        super();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.name.setText("Alex Kane");
            holder.reviewText.setText("Я доволен этим рестораном!");
            holder.ratingBar.setRating(3);
        }
        if(position == 1){
            holder.name.setText("Mitchel Kluge");
            holder.reviewText.setText("Wowsers!");
            holder.ratingBar.setRating(5);
        }
        if(position == 2){
            holder.name.setText("Ronald McDonald");
            holder.reviewText.setText("This Restauraunt will never live up to MCD!");
            holder.ratingBar.setRating(1);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView reviewText;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            reviewText = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }
}