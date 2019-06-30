package com.example.wowhqapp.network;

import com.example.wowhqapp.databases.entity.WowClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WowClassApi {
    @GET("/api/classes/{lang}")
    Call<List<WowClass>> getAll(@Path("lang") String lang);
}
