package com.example.capstone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {
    private String foursquareClientID;
    private String foursquareClientSecret;

    List<FoursquareResults> frs;
    private String foursquareBaseURL = "https://api.foursquare.com/v2/";

    public DishAdapter(List<FoursquareResults> frs){
        super();
        this.frs = frs;

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
        //Please dont steal these. The proper way to do this was not working
        foursquareClientID = "MEBZPE1NYBH34SQD22J3NHWL250ZOIU24L3U3LFGTOFRSRIV";
        foursquareClientSecret = "1HMOHKIMFPQ2A2G32HRBRPEABFJCNAXQZOMCRFVURA4MNGVO";
        holder.name.setText("Dish " + position);
        holder.description.setText("This is a description of dish " + position + ".");
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
        }
    }

}
