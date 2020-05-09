package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    List<FoursquareResults> frs;
    private List<FoursquareResults> filteredFrs;
    private OnRestaurantListener orl;
    public RestaurantAdapter(List<FoursquareResults> frs, OnRestaurantListener orl){
        super();
        this.frs = frs;
        this.orl = orl;
        copyFilteredItems();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.restaurant_row, parent, false);
        return new MyViewHolder(view, orl);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FoursquareResults result = frs.get(position);
        holder.name.setText(result.venue.name);
        holder.description.setText("Rating: " + result.venue.rating);

        if(result.venue.bestPhoto != null) {
            String url = result.venue.bestPhoto.prefix + "193x193" + result.venue.bestPhoto.suffix;
            Picasso.get().load(url).into(holder.image);}
        //String url = "https://fastly.4sqi.net/img/general/150x150/24970585_naC8Im6g99t13jIW1649X2h0_IiIZeOKbll68ag1bOU.jpg";


        //holder.description.setText("Rating: " + result.venue.rating);
        holder.ratingBar.setRating((float)result.venue.rating);

    }

    @Override
    public int getItemCount() {
        return frs.size();
    }

    public void filter(){
        copyFilteredItems();
        notifyDataSetChanged();
    }

    private void copyFilteredItems(){
        filteredFrs.clear();
        for (FoursquareResults f : frs) {
            if(f.filtered){
                filteredFrs.add(f);
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView description;
        ImageButton image;
        RatingBar ratingBar;
        OnRestaurantListener orl;
        public MyViewHolder(@NonNull View itemView, OnRestaurantListener orl) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            ratingBar = itemView.findViewById(R.id.rating);
            this.orl = orl;
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.imageButton);
        }

        @Override
        public void onClick(View v) {
            orl.onRestaurantClick(frs.get(getAdapterPosition()));

        }
    }
    public interface OnRestaurantListener{
        void onRestaurantClick(FoursquareResults position);
    }
}