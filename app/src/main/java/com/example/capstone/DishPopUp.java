package com.example.capstone;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DishPopUp extends AppCompatActivity {

    private ArrayList<Review> reviews = new ArrayList<>();
    private DishReviewAdapter reviewAdapter;
    private String dishId = "";
    private RatingBar ratingBar;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_dish_pop_up);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView reviewRecyclerView = findViewById(R.id.DishReviewRecyclerView);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setLayoutManager(mLayoutManager);

        dishId = getIntent().getStringExtra("dishId");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");

        TextView titleView = (TextView)findViewById(R.id.dishNameView);
        titleView.setText(name);
        TextView descView = (TextView)findViewById(R.id.dishDescription);
        descView.setText(description);

        reviewAdapter = new DishReviewAdapter(reviews);

        ratingBar = findViewById(R.id.ratingBar);

        new GetReviewsTask().execute(dishId);

        reviewRecyclerView.setAdapter(reviewAdapter);

        Button submitButton = findViewById(R.id.sendButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "My Review";
                EditText reviewField = findViewById(R.id.editReview);
                String review = reviewField.getText().toString();
                RatingBar ratingBar = findViewById(R.id.enterRatingBar);
                String rating = "" + (int)ratingBar.getRating();
                new PostReviewTask().execute(title,review,rating,dishId);
            }
        });

        final SwipeRefreshLayout refresh = findViewById(R.id.dishPopupRefreshLayout);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetReviewsTask().execute(dishId);
                refresh.setRefreshing(false);
            }
        });
    }



    private class GetReviewsTask extends AsyncTask<String, String, List<Review>> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected List<Review> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                String dishId = params[0];
                URL url = new URL("http://192.168.1.105:8080/foodfinder/get_reviews?id=" + dishId);
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
                ArrayList<Review> reviews = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    Review review = new Review();
                    review.reviewId = jObject.getString("reviewId");
                    review.title = jObject.getString("title");
                    review.content = jObject.getString("content");
                    review.rating = jObject.getInt("rating");
                    review.UserId = jObject.getString("userId");
                    review.dishId = jObject.getString("dishId");
                    reviews.add(review);
                }


                return reviews;


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
        protected void onPostExecute(List<Review> result) {
            super.onPostExecute(result);
            float totalStars = 0;
            for (Review r:result) {totalStars += r.rating;}
            if(result.size() != 0) {ratingBar.setRating(totalStars / result.size());}
            reviews.clear();
            reviews.addAll(result);
            reviewAdapter.notifyDataSetChanged();
        }
    }

    public class PostReviewTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String response = "";
            try {
                String urlString = "http://192.168.1.105:8080/foodfinder/post_review";
                response += postHttpContent(urlString, params[0],params[1],params[2],params[3]);
            } catch (IOException e) {
                System.out.println("IO Error");
                Log.e("error", e.toString());
            } catch (JSONException e) {
                System.out.println("JSON Error");
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        }

        public String postHttpContent(String urlStr, String title, String content, String rating, String dishId) throws IOException, JSONException {
            String response = "";
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestMethod("POST");

            JSONObject userdata = new JSONObject();
            userdata.put("title",title);
            userdata.put("content", content);
            userdata.put("rating",rating);
            userdata.put("dish_id", dishId);

            JSONArray data = new JSONArray();
            data.put(userdata);
            System.out.println("Array is: " + data);

            try {
                DataOutputStream localDataOutputStream = new DataOutputStream(httpConn.getOutputStream());
                localDataOutputStream.writeBytes(data.toString());
                localDataOutputStream.flush();
                localDataOutputStream.close();
                System.out.println("Data writting: " + data);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Data Output error");
            }

            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                do {
                    line = br.readLine();
                    response += line;
                } while ((line = br.readLine()) != null);
            } else {
                response = "Error ";
                throw new IOException();
            }
            return response + " * Uploaded!";
        }
    }
}
