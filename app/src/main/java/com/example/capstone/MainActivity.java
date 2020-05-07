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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
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
    private List<FoursquareResults> frs = new ArrayList<FoursquareResults>();
    public Location location;
    public Location getOutLocation() {return location;}
    public List<FoursquareResults> getFrs() {
        return frs;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        foursquareClientID = getResources().getString(R.string.foursquare_client_id);
        foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Gets the stored Foursquare API client ID and client secret from XML
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
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
                    FoursquareService foursquare = retrofit.create(FoursquareService.class);

                    // Calls the Foursquare API to snap the user's location to a Foursquare venue
                    Call<FoursquareJSON> stpCall = foursquare.snapToPlace(
                            foursquareClientID,
                            foursquareClientSecret,
                            userLL,
                            userLLAcc);
                    stpCall.enqueue(new Callback<FoursquareJSON>() {
                        @Override
                        public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                            // Gets the venue object from the JSON response
                            FoursquareJSON fjson = response.body();
                            FoursquareResponse fr = fjson.response;
                            List<FoursquareVenue> frs = fr.venues;
                            FoursquareVenue fv = frs.get(0);
                            Log.d("Debug",fv.name);

                        }

                        @Override
                        public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                            Log.d("Debug","Unable to get response");
                        }
                    });
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
                            FoursquareResponse fr = fjson.response;
                            FoursquareGroup fg = fr.group;
                            frs = fg.results;
                                Log.d("Debug",frs.toString());
                        }

                        @Override
                        public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                            Log.e(null, "Cant connect to foursquare");
                        }

                    });
                } else {
                    Log.e(null, "Location was null");
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(null, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) searchItem.getActionView();

        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                return false;
            }
        });*/
        return true;
    }
}
