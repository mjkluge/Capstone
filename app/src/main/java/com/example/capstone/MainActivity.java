package com.example.capstone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // The client object for connecting to the Google API
    private FusedLocationProviderClient fusedLocationProviderClient;

    // The RecyclerView and associated objects for displaying the nearby restaurants
    private RecyclerView RestaurantPage;
    private RecyclerView.Adapter RestaurantAdapter;

    // The base URL for the Foursquare API
    private String foursquareBaseURL = "https://api.foursquare.com/v2/";

    // The client ID and client secret for authenticating with the Foursquare API
    private String foursquareClientID;
    private String foursquareClientSecret;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public Location location;
    public Location getOutLocation() {return location;}


    public List<FoursquareResults> frs = new ArrayList<FoursquareResults>();
    public List<FoursquareVenue> details;
    public ArrayList<dish> dishList = new ArrayList<dish>();

    public ArrayList<dish> getdishList(){return dishList;}
    public List<FoursquareResults> getFrs() { return frs; }
    public List<FoursquareResults> filteredRes;
    public List<dish> filteredDishes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        dishList.clear();
        details = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        setContentView(R.layout.activity_main);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), frs, details, dishList);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        foursquareClientID = getResources().getString(R.string.foursquare_client_id);
        foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);
        SearchView searchview = findViewById(R.id.searchbar);

        // Gets the stored Foursquare API client ID and client secret from XML

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FiltersPopup.class);
                startActivity(intent);
            }
        });
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int hit = 0;
                filteredRes = new ArrayList<>();
                filteredDishes = new ArrayList<>();
                for(FoursquareResults r:frs) {
                    if(r.venue.name.equals(query)){
                        hit++;
                        filteredRes.add(r);
                        r.filtered = true;
                        for(dish d:dishList){
                            if(d.id.equals(r.venue.id)){
                                filteredDishes.add(d);
                                d.filtered = true;
                            }
                        }
                    }else if(r.venue.price.message.equals(query)) {
                        hit++;
                        filteredRes.add(r);
                        r.filtered = true;
                        for(dish d:dishList){
                            if(d.id.equals(r.venue.id)){
                                filteredDishes.add(d);
                                d.filtered = true;
                            }
                        }
                    }
                }
                if(hit == 0){

                }else{

                }
                //send res to restaurant adapter

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



    // Builds Retrofit and FoursquareService objects for calling the Foursquare API and parsing with GSON


    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                location = task.getResult();
                if (location != null) {
                    // The user's current latitude, longitude, and location accuracy
                    String userLL = location.getLatitude() + "," + location.getLongitude();
                    double userLLAcc = location.getAccuracy();
                    Log.d("Debug",userLL + "" + userLLAcc);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(foursquareBaseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    final FoursquareService foursquare = retrofit.create(FoursquareService.class);

                    // Calls the Foursquare API to snap the user's location to a Foursquare venue
                    Call<FoursquareJSON> stpCall = foursquare.snapToPlace(
                            foursquareClientID,
                            foursquareClientSecret,
                            userLL,
                            userLLAcc);
                    Callback<FoursquareJSON> restaurantCallback = new Callback<FoursquareJSON>() {
                        @Override
                        public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                            // Gets the venue object from the JSON response
                            FoursquareJSON fjson = response.body();
                            FoursquareResponse fr = fjson.response;
                            List<FoursquareVenue> frs = fr.venues;
                            FoursquareVenue fv = frs.get(0);
                            Log.d("Debug",fv.id + fv.name);

                        }

                        @Override
                        public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                            Log.d("Debug","Unable to get response");
                        }
                    };
                    stpCall.enqueue(restaurantCallback);
                    // Calls the Foursquare API to explore nearby restaurants
                    Call<FoursquareJSON> restCall = foursquare.getRecommendations(
                            foursquareClientID,
                            foursquareClientSecret,
                            userLL,
                            userLLAcc);
                    restCall.enqueue(new Callback<FoursquareJSON>() {
                        @Override
                        public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                            // Gets the venue object from the JSON response
                            FoursquareJSON fjson = response.body();
                            Log.d("Pizza", response.toString());
                            FoursquareResponse fr = fjson.response;
                            FoursquareGroup fg = fr.group;
                            frs.clear();
                            frs.addAll(fg.results);
                                Log.d("Debug",frs.toString());
                                Details(foursquare);
                                getMenues(foursquare);

                        }

                                @Override
                        public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                            Log.e(null, "Cant connect to foursquare");
                        }

                    });
                   // Log.d("debug","made it here");
                    //Details(foursquare);
                } else {
                    Log.e(null, "Location was null");
                }
            }
            public void getMenues(FoursquareService foursquare) {

                //for(FoursquareResults r:frs) {
                //comment out below statement and uncomment above and lower squiggly for actual usage. this is for lower amount of calls
                final FoursquareResults r = frs.get(0);

                Call<FoursquareJSON> menuCall = foursquare.getMenu(
                        r.venue.id,
                        foursquareClientID,
                        foursquareClientSecret
                );
                menuCall.enqueue(new Callback<FoursquareJSON>() {

                    @Override
                    public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {
                        FoursquareJSON fjson2 = response.body();
                        Log.d("details",response.toString());
                        FoursquareResponse fr = fjson2.response;
                        FoursquareMenu fm = fr.menu;
                        FoursquareMenus fms = fm.menus;
                        for (int i = 0; i <fms.count ; i++) {
                            FoursquareItems fsi = fms.items.get(i);
                            FoursquareMenusEntries fsme = fsi.entries;
                            for (int j = 0; j <fsme.items.size() ; j++) {
                                FoursquareInnerItems fsie = fsme.items.get(j);
                                for (int k = 0; k <fsie.entries.count ; k++) {
                                    dish thisisDish = fsie.entries.items.get(k);
                                    thisisDish.id = r.venue.id;
                                    Log.d("test", thisisDish.id);
                                    dishList.add(thisisDish);

                                }
                            }
                        }
                        //DishPage dishpage = (DishPage) sectionsPagerAdapter.getItem(1);
                        //dishpage.updateList();
                        //dishpage.getView().invalidate();
                        //dishpage.updateList(dishList);

                        


                        //Log.d("menu",fsi.toString());
                        // FoursquareItems f1 = fsi.get(0);
                        // Log.d("Menu",f1.entries.items.get(0).entries.items.get(0).entryId);
                         //Log.d("Menu", menuLvl1.get(0).get(0).entries.items.get(0).entries.items.get(0).entryId);




                    }

                    @Override
                    public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                        Log.d("Debug",t.getMessage());
                    }
                });
              //  }
            }
            private void Details(FoursquareService foursquare) {

                //for(FoursquareResults r:frs) {
                //comment out below statement and uncomment above and lower squiggly for actual usage. this is for lower amount of calls
                FoursquareResults r = frs.get(0);

                    Call<FoursquareJSON> detailsCall = foursquare.getDetails(
                            r.venue.id,
                            foursquareClientID,
                            foursquareClientSecret
                           );
                    detailsCall.enqueue(new Callback<FoursquareJSON>() {

                        @Override
                        public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {
                            FoursquareJSON fjson2 = response.body();
                            Log.d("details",response.toString());
                            FoursquareResponse fr = fjson2.response;
                            FoursquareVenue fv = fr.venue;
                            details.add(fv);
                            Iterator<FoursquareResults> iterator = frs.iterator();
                            for(FoursquareResults r:frs){
                                if(r.venue.id.equals(fv.id)){
                                    r.venue.bestPhoto = fv.bestPhoto;
                                    r.venue.rating = fv.rating;
                                    r.venue.price = fv.price;
                                }
                            }
                            Log.d("details",fr.venue.id + fr.description);

                           // Log.d("details",fv.name);
                            //Log.d("details",fv.price.message);
                           // Log.d("details", String.valueOf(fv.rating));
                            //Log.d("photo",fv.bestPhoto.prefix +"50x50"+ fv.bestPhoto.suffix);




                        }

                        @Override
                        public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                            Log.d("Debug","Unable to get details response");
                        }
                    });
                }
            //}
        });
    }

}
