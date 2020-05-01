package com.example.capstone;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareService {

    // A request to snap the current user to a place via the Foursquare API.
    @GET("venues/search?v=20161101&limit=1")
    Call<FoursquareJSON> snapToPlace(@Query("client_id") String clientID,
                                     @Query("client_secret") String clientSecret,
                                     @Query("ll") String ll,
                                     @Query("llAcc") double llAcc);

    // A request to search for recommendations via the Foursquare API.
    @GET("search/recommendations?v=20161101&intent=food")
    Call<FoursquareJSON> getRecommendations(@Query("client_id") String clientID,
                                      @Query("client_secret") String clientSecret,
                                      @Query("ll") String ll,
                                      @Query("llAcc") double llAcc);


    @GET ("venues/46d36dfdf964a520574a1fe3")
    Call<FoursquareJSON> getDetails(@Query("client_id") String clientID,
                                 @Query("client_secret") String clientSecret,
                                 @Query("VENUE_ID") String venueID);
}
