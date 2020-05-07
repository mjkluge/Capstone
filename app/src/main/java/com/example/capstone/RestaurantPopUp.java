package com.example.capstone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class RestaurantPopUp extends AppCompatActivity {

    private RecyclerView reviewRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_pop_up);

        DisplayMetrics dn = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);

        int width = dn.widthPixels;
        int height = dn.heightPixels;
        getWindow().setLayout((int)(width), (int)(height));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("McDonald's");
        findViewById(R.id.toolbar_layout).setBackground(getDrawable(R.drawable.mcd));


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        reviewRecyclerView = findViewById(R.id.ReviewRecyclerView);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setLayoutManager(mLayoutManager);

        ReviewAdapter reviewAdapter = new ReviewAdapter();

        reviewRecyclerView.setAdapter(reviewAdapter);
    }


}
