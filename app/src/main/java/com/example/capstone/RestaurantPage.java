package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantPage extends Fragment implements RestaurantAdapter.OnRestaurantListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView restaurantRecyclerView;

    public static RestaurantPage newInstance(int index) {
        RestaurantPage fragment = new RestaurantPage();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    public RecyclerView getRestaurantRecyclerView() {
        return restaurantRecyclerView;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        //final TextView textView = root.findViewById(R.id.section_label);
        //pageViewModel.getText().observe(this, new Observer<String>() {
        //    @Override
        //    public void onChanged(@Nullable String s) {
        //        textView.setText(s);
        //    }
        //});
        return root;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        restaurantRecyclerView = (RecyclerView) view.findViewById(R.id.myRecyclerView);
        restaurantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        restaurantRecyclerView.setLayoutManager(mLayoutManager);

        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(((MainActivity)this.getActivity()).getFrs(), this);

        restaurantRecyclerView.setAdapter(restaurantAdapter);

    }

    @Override
    public void onRestaurantClick(FoursquareResults results) {
        //Log.d(TAG, "onRestaurantClick: Restaurant " + position);
        Intent intent = new Intent(getActivity(), RestaurantPopUp.class);
        intent.putExtra("name", results.venue.name);
        startActivity(intent);

    }
}