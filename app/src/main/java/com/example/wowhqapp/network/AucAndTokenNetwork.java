package com.example.wowhqapp.network;


import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class AucAndTokenNetwork {

    private static AucAndTokenNetwork mInstance;
    private static final String BASE_URL = "http://3.121.42.184/api/";
    private Retrofit mRetrofit;

    private AucAndTokenNetwork() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static AucAndTokenNetwork getInstance() {
        if (mInstance == null) {
            mInstance = new AucAndTokenNetwork();
        }
        return mInstance;
    }

    public WoWTokenApi getWoWTokenAPI(){
        return mRetrofit.create(WoWTokenApi.class);
    }
    public AuctionsApi getAuctionsAPI(){
        return mRetrofit.create(AuctionsApi.class);
    }

}
