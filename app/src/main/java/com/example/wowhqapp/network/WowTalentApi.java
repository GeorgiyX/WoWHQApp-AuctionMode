package com.example.wowhqapp.network;

import com.example.wowhqapp.databases.entity.WowTalents;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WowTalentApi {
    @GET("/api/talents/{class_id}/{spec_order}/{lang}")
    Call<WowTalents> getWowTalents(
            @Path("class_id") int classId,
            @Path("spec_order") int specOrder,
            @Path("lang") String lang
    );
}
