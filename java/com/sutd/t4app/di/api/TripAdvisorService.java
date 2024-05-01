package com.sutd.t4app.di.api;
import com.sutd.t4app.data.model.apiresponses.LocationSearchResponse;
import com.sutd.t4app.data.model.apiresponses.ReviewLocationResponse;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Call;
// This code snippet defines a Java interface named `TripAdvisorService` that serves as a contract for
// making API calls to the TripAdvisor API.
public interface TripAdvisorService {
    @GET("search")
    Observable<LocationSearchResponse> searchLocation(
            @Query("searchQuery") String searchQuery,
            @Query("category") String category,
            @Query("address") String address,
            @Query("latLong") String latLong,
            @Query("language") String language,
            @Query("key") String apiKey
    );
    @GET("{locationId}/reviews")
    Observable<ReviewLocationResponse> getReviews(
            @Path("locationId") int locationId,
            @Query("key") String apiKey,
            @Query("language") String language
    );
}
