package com.example.wowhqapp;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.wowhqapp.databases.database.TalentsCalculatorDatabase;
import com.example.wowhqapp.databases.database.AucAndTokenDataBase;
import com.example.wowhqapp.network.ApiTalentsRepository;

public class WowhqApplication extends Application {

    public static final String LOG_TAG = "LOG_TAG";

    public static WowhqApplication instance;
    private AucAndTokenDataBase mAucAndTokenDataBase;
    private TalentsCalculatorDatabase mTalentCalcDb;
    private ApiTalentsRepository mApiTalentsRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mAucAndTokenDataBase = Room.databaseBuilder(this, AucAndTokenDataBase.class, "AucAndTokenDB")
                //.allowMainThreadQueries() //Пока будет так
                .fallbackToDestructiveMigration()
                .build();
        // mApiRepo = new ApiRepo();
        mApiTalentsRepo = new ApiTalentsRepository();
        mTalentCalcDb = Room.databaseBuilder(
                this,
                TalentsCalculatorDatabase.class,
                "talents_calculator_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public TalentsCalculatorDatabase getTalentsCalculatorDatabase() {
        return mTalentCalcDb;
    }

    public AucAndTokenDataBase getAucAndTokenDatabase(){
        return  mAucAndTokenDataBase;
    }

    public ApiTalentsRepository getTalentsApi() {
        return mApiTalentsRepo;
    }

    public static WowhqApplication from(Context context) {
        return (WowhqApplication) context.getApplicationContext();
    }

    //Контекст приложухи, но без аргумента
    public static WowhqApplication getInstance(){
        return instance;
    }
}
