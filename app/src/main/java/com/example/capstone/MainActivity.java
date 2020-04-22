package com.example.capstone;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.capstone.ui.main.SectionsPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        // The client object for connecting to the Google API
        private GoogleApiClient mGoogleApiClient;
        private FusedLocationProviderClient fusedLocationClient;

        // The RecyclerView and associated objects for displaying the nearby restaurants
        private RecyclerView RestaurantPage;
        private RecyclerView.Adapter RestaurantAdapter;

        // The base URL for the Foursquare API
        private String foursquareBaseURL = "https://api.foursquare.com/v2/";

        // The client ID and client secret for authenticating with the Foursquare API
        private String foursquareClientID;
        private String foursquareClientSecret;
        private SectionsPagerAdapter sectionsPagerAdapter;

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


            // Creates a connection to the Google API for location services
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            // Gets the stored Foursquare API client ID and client secret from XML
            foursquareClientID = getResources().getString(R.string.foursquare_client_id);
            foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onConnected(Bundle connectionHint) {

            // Checks for location permissions at runtime (required for API >= 23)
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // Makes a Google API request for the user's last known location
                final Task<Location> mLastLocation = fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            // The user's current latitude, longitude, and location accuracy
                            String userLL = location.getLatitude() + "," + location.getLongitude();
                            double userLLAcc = location.getAccuracy();

                            // Builds Retrofit and FoursquareService objects for calling the Foursquare API and parsing with GSON
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

                                }

                                @Override
                                public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                                    //some sort of failure message
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
                                    List<FoursquareResults> frs = fg.results;

                                    // Displays the results in the RecyclerView
                                    RestaurantAdapter = new RestaurantAdapter(); //TODO:add argument
                                    RestaurantPage rp = (RestaurantPage) sectionsPagerAdapter.getItem(0); //get the fragment from tab 0 (restaurant page)
                                    rp.getRestaurantRecyclerView().setAdapter(RestaurantAdapter);
                                 //   RestaurantPage.setAdapter(RestaurantAdapter);
                                }

                                @Override
                                public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                                    Log.e(null,"Cant connect to foursquare");
                                }

                            });
                        } else {
                            Log.e(null,"Location was null");
                        }
                    }
                });

            }else{
                Log.e(null,"Cant access lovation");

            }
        }
        @Override
        protected void onResume() {
            super.onResume();

            // Reconnects to the Google API
            mGoogleApiClient.connect();
        }

        @Override
        protected void onPause() {
            super.onPause();

            // Disconnects from the Google API
            mGoogleApiClient.disconnect();
        }

        @Override
        public void onConnectionSuspended(int i) {}

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.e(null,"Cant connect to google services");

        }

        }