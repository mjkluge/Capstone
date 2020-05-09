package com.example.capstone;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> implements Observer {
    private String foursquareClientID;
    private String foursquareClientSecret;

    List<FoursquareResults> frs;

    public void setDishList(ArrayList<Dish> dishList) {
        this.dishList = dishList;
    }

    public ArrayList<Dish> dishList;


    private OnDishListener odl;
    public DishAdapter(OnDishListener odl, ArrayList<Dish> dishList){
        super();
        this.odl = odl;
        this.dishList = dishList;



    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dish_row, parent, false);
        return new MyViewHolder(view, odl);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Log.d("Menu", menuLvl1.get(0).get(0).entries.items.get(0).entries.items.get(0).name);
        holder.name.setText(dishList.get(position).name);
        holder.description.setText(dishList.get(position).description);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView description;
        OnDishListener odl;
        public MyViewHolder(@NonNull View itemView, OnDishListener odl) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            this.odl = odl;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            odl.onDishClick(getAdapterPosition());
        }
    }
    public interface OnDishListener {
        void onDishClick(int position);
    }

}
