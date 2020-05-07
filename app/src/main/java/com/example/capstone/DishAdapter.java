package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {

    private OnDishListener odl;
    public DishAdapter(OnDishListener odl){
        super();
        this.odl = odl;
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
        holder.name.setText("Dish " + position);
        holder.description.setText("This is a description of dish " + position + ".");
    }

    @Override
    public int getItemCount() {
        return 15;
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
