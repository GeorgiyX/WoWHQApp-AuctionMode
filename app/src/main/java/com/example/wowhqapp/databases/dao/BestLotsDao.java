package com.example.wowhqapp.databases.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.wowhqapp.databases.entity.BestLot;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface BestLotsDao {
    @Query("SELECT * FROM BestLot ")
    Flowable<List<BestLot>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BestLot simpleLot);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<BestLot> simpleLots);

    @Update
    void update(BestLot simpleLot);

    @Delete
    void delete(BestLot simpleLot);

    @Query("DELETE FROM BestLot")
    void deleteAll();
}
