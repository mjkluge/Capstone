package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DishPage extends Fragment implements DishAdapter.OnDishListener{



    private static final String ARG_SECTION_NUMBER = "section_number";
    //private RecyclerView dishRecyclerView;
    //private DishAdapter dishAdapter;

    public static DishPage newInstance(int index) {
        DishPage fragment = new DishPage();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void updateList(){
        RecyclerView dishRecyclerView = getView().findViewById(R.id.restaurantRecyclerView);
        if(dishRecyclerView != null){
            DishAdapter adapter = (DishAdapter) dishRecyclerView.getAdapter();
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
        //if(dishAdapter == null){
        //    dishAdapter = new DishAdapter(this, dishes);
       // }else {
        //    dishAdapter.setDishList(dishes);
        //}

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

    //public RecyclerView getDishRecyclerView() {
    //    return dishRecyclerView;
    //}

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dish, container, false);
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

        RecyclerView dishRecyclerView = (RecyclerView) view.findViewById(R.id.dishRecyclerView);
        dishRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dishRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<dish> pizza = ((MainActivity) this.getActivity()).getdishList();

        final DishAdapter dishAdapter = new DishAdapter(this, ((MainActivity)this.getActivity()).getdishList());


        dishRecyclerView.setAdapter(dishAdapter);

        Button button = view.findViewById(R.id.refreshButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dishAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onDishClick(int position) {
        Intent intent = new Intent(getActivity(), DishPopUp.class);
        startActivity(intent);
    }

}