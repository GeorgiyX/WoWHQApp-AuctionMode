package com.example.wowhqapp.network;

import android.content.Context;

import com.example.wowhqapp.WowhqApplication;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiTalentsRepository {
    private final WowClassApi mWowClassApi;
    private final WowSpecApi mWowSpecApi;
    private final WowTalentApi mWowTalentApi;

    private final OkHttpClient mOkHttpClient;

    public ApiTalentsRepository() {
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(
                        new HttpUrl.Builder().scheme("http")
                                .host("3.121.42.184")
                                .build()
                ).client(mOkHttpClient)
                .build();

        mWowClassApi = retrofit.create(WowClassApi.class);
        mWowSpecApi = retrofit.create(WowSpecApi.class);
        mWowTalentApi = retrofit.create(WowTalentApi.class);
    }

    public WowClassApi getWowClassApi() {
        return mWowClassApi;
    }

    public WowSpecApi getWowSpecApi() {
        return mWowSpecApi;
    }

    public WowTalentApi getWowTalentApi() {
        return mWowTalentApi;
    }

    public static ApiTalentsRepository from(Context context) {
        return WowhqApplication.from(context).getTalentsApi();
    }
}
