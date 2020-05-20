package com.example.capstone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) mSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int hit = 0;
                ArrayList<FoursquareResults> filteredRes = new ArrayList<>();
                ArrayList<Dish> filteredDishes = new ArrayList<>();
                for(FoursquareResults r:frs) {
                    if(r.venue.name.equals(query)){
                        hit++;
                        filteredRes.add(r);
                        r.filtered = true;
                        for(Dish d:dishList){
                            if(d.entryId.equals(r.venue.id)){
                                filteredDishes.add(d);
                                d.filtered = true;
                            }
                        }
                    }
                    else if(r.venue.price != null &&  r.venue.price.message != null && r.venue.price.message.equals(query)) {
                        hit++;
                        filteredRes.add(r);
                        r.filtered = true;
                        for(Dish d:dishList){
                            if(d.entryId.equals(r.venue.id)){
                                filteredDishes.add(d);
                                d.filtered = true;
                            }
                        }
                    }
                    else{
                        r.filtered = false;
                        for(Dish d:dishList){
                            if(d.entryId.equals(r.venue.id)){
                                d.filtered = false;
                            }
                        }
                    }
                }
                if(hit == 0){

                }
                else{

                }
                //send res to restaurant adapter

                //RestaurantPage rp = (RestaurantPage) sectionsPagerAdapter.getItem(0);
                //rp.filterList();
                SingletonObserver.getInstance().change();
                SingletonObserver.getInstance().notifyObservers();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }



    // The client ID and client secret for authenticating with the Foursquare API
    private String foursquareClientID;
    private String foursquareClientSecret;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public Location location;
    private ViewPager viewPager;

    public Location getOutLocation() {return location;}


    public List<FoursquareResults> frs = new ArrayList<FoursquareResults>();
    public List<FoursquareVenue> details;
    public ArrayList<Dish> dishList = new ArrayList<Dish>();

    public ArrayList<Dish> getdishList(){return dishList;}
    public List<FoursquareResults> getFrs() { return frs; }


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
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        foursquareClientID = getResources().getString(R.string.foursquare_client_id);
        foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);
        Toolbar mToolbar = findViewById(R.id.restaurantToolbar);
        setSupportActionBar(mToolbar);

        //viewPager.setCurrentItem(2);
        // Gets the stored Foursquare API client ID and client secret from XML

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FiltersPopup.class);
                startActivity(intent);
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
                                getMenus(foursquare);

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
            public void getMenus(FoursquareService foursquare) {

                //for(final FoursquareResults r:frs) {
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
                                    Dish thisisDish = fsie.entries.items.get(k);
                                    thisisDish.venueId = r.venue.id;
                                    Log.d("test", thisisDish.venueId);
                                    dishList.add(thisisDish);
                                    //get our details
                                    new DishDetailsTask().execute(thisisDish);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                        Log.d("Debug",t.getMessage());
                    }
                });
                //}
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
                //}
            }
        });
    }

    private class DishDetailsTask extends AsyncTask<Dish, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            //pd = new ProgressDialog(MainActivity.this);
            //pd.setMessage("Please wait");
            //pd.setCancelable(false);
            // pd.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(Dish... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                Dish mydish = params[0];
                URL url = new URL("http://192.168.1.105:8080/foodfinder/get_dish_details?id=" + mydish.entryId);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                JSONArray jArray = new JSONArray(buffer.toString());
                JSONObject jObject = jArray.getJSONObject(0);
                mydish.avgReview = Float.parseFloat(jObject.getString("avgRating"));
                mydish.calories = jObject.getInt("calories");
                mydish.price = jObject.getString("price");
                mydish.vegetarian = jObject.getString("vegetarian").equals("1");
                mydish.vegan = jObject.getString("vegan").equals("1");
                mydish.dairyFree = jObject.getString("dairyfree").equals("1");
                mydish.nutFree = jObject.getString("nutfree").equals("1");
                mydish.glutenFree = jObject.getString("glutenfree").equals("1");

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //if (pd.isShowing()){
            //   pd.dismiss();
            //}
            //txtJson.setText(result);
        }
    }

}
