package com.example.wowhqapp.network;

import com.example.wowhqapp.databases.entity.WowClass;
import com.example.wowhqapp.databases.entity.WowSpec;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WowSpecApi {
    @GET("/api/specs/{lang}")
    Call<List<WowSpec>> getAll(@Path("lang") String lang);
}
