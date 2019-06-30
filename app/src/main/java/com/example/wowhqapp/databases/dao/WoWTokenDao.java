package com.example.wowhqapp.databases.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.wowhqapp.databases.entity.WoWToken;

import java.util.List;

import io.reactivex.Flowable;


@Dao
public interface WoWTokenDao {
    @Query("SELECT * FROM wowtoken WHERE region =:region")
    Flowable<List<WoWToken>> getByRegion(String region);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WoWToken woWToken);

    @Update
    void update(WoWToken woWToken);

    @Delete
    void delete(WoWToken woWToken);

    @Query("DELETE FROM WoWToken")
    void deleteAll();
}
