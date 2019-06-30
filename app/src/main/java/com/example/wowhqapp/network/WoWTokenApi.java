package com.example.wowhqapp.network;

import com.example.wowhqapp.databases.entity.WoWToken;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WoWTokenApi {
    @GET("wowtoken/{region}")
    public Call<WoWToken> getWoWTokenByRegion(@Path("region") String region);
}
