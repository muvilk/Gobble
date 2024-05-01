package com.sutd.t4app.di.api;

import com.sutd.t4app.data.model.apiresponses.LocationSearchResponse;
import com.sutd.t4app.data.model.apiresponses.ReviewLocationResponse;
import com.sutd.t4app.data.model.apiresponses.YelpReviewResponse;
import com.sutd.t4app.data.model.apiresponses.YelpSearchResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
// This code snippet defines an interface named `YelpService` that specifies a method
// `searchBusinesses` using Retrofit annotations for making API calls. Here's what each part of the
// method signature does:
public interface YelpService {
    @GET("search")
    Observable<YelpSearchResponse> searchBusinesses(
            @Header("Authorization") String authToken,
            @Query("location") String location,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("term") String term
    );

    @GET("{business_id_or_alias}/reviews")
    Observable<YelpReviewResponse> getReviews(
            @Path("business_id_or_alias") String business_id_or_alias,
            @Header("Authorization") String authToken);
}
