package com.example.railrush.Services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RailrushClient {
    public static final String BASE_URL = "http://52.23.201.69/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
