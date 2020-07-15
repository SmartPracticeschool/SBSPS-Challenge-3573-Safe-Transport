package com.example.railrush.Services;

import com.example.railrush.Models.Count;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CrowdInterface {
    @GET("/crowdCount/{trainNo}")
    Call<Count> getCrowdCount(@Path("trainNo") String trainNo);
}
