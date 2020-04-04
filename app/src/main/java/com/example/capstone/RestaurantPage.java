package com.example.capstone;

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

import com.example.capstone.ui.main.PageViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantPage extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView restaurantRecyclerView;
    LinearLayoutManager mLayoutManager;

    private PageViewModel pageViewModel;

    public static DishPage newInstance(int index) {
        DishPage fragment = new DishPage();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);


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

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        restaurantRecyclerView = (RecyclerView) view.findViewById(R.id.dishRecyclerView);
        restaurantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        restaurantRecyclerView.setLayoutManager(mLayoutManager);

        RestaurantAdapter restaurantAdapter = new RestaurantAdapter();

        restaurantRecyclerView.setAdapter(restaurantAdapter);



    }
}