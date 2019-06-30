package com.example.wowhqapp.network;

import com.example.wowhqapp.databases.entity.Auctions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuctionsApi {
    @GET("allauc/{slug}/{region}/{lang}/{page}")
    Call<Auctions> getAuctionsByPage(@Path("slug") String slug,
                                            @Path("region") String region,
                                            @Path("lang") String lang,
                                            @Path("page") String page);
}
