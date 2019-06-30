package com.example.wowhqapp.databases.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.wowhqapp.WowhqApplication;
import com.example.wowhqapp.databases.dao.AuctionsDao;
import com.example.wowhqapp.databases.dao.BestLotsDao;
import com.example.wowhqapp.databases.dao.WoWTokenDao;
import com.example.wowhqapp.databases.entity.BestLot;
import com.example.wowhqapp.databases.entity.SimpleLot;
import com.example.wowhqapp.databases.entity.WoWToken;

@Database(entities = {WoWToken.class, SimpleLot.class, BestLot.class}, version = 2)
public abstract class AucAndTokenDataBase extends RoomDatabase {

    public abstract WoWTokenDao getWoWTokenDao();
    public abstract AuctionsDao getAuctionDao();
    public abstract BestLotsDao getBestLotsDao();


    public static AucAndTokenDataBase getDB(){
        return WowhqApplication.getInstance().getAucAndTokenDatabase();
    }
}
