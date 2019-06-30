package com.example.wowhqapp.databases.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.wowhqapp.databases.entity.SimpleLot;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface AuctionsDao {
    @Query("SELECT * FROM SimpleLot ")
    Flowable<List<SimpleLot>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SimpleLot simpleLot);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<SimpleLot> simpleLots);

    @Update
    void update(SimpleLot simpleLot);

    @Delete
    void delete(SimpleLot simpleLot);

    @Query("DELETE FROM SimpleLot")
    void deleteAll();
}
