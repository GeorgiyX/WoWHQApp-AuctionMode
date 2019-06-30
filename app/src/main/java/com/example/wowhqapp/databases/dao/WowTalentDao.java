package com.example.wowhqapp.databases.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.wowhqapp.databases.entity.Talent;
import com.example.wowhqapp.databases.entity.WowSpec;

import java.util.List;

@Dao
public interface WowTalentDao {
    // -------Get--------------------
    @Query("SELECT * FROM talent")
    List<Talent> getAll();

    @Query("SELECT * FROM talent WHERE id=:id")
    Talent getById(int id);

    @Query("SELECT * FROM wowspec WHERE id=:specId")
    WowSpec getSpec(int specId);

    @Query("SELECT * FROM talent WHERE specId=:specId ORDER BY `row`, col ASC")
    List<Talent> getBySpecId(int specId);


    //-------Insert------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Talent... talents);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Iterable<Talent> talents);


    // -------Update-----------------
    @Update
    void update(Talent... talents);

    @Update
    void update(Iterable<Talent> talents);


    // -------Delete-----------------
    @Delete
    void delete(Talent... talents);

    @Delete
    void delete(Iterable<Talent> talents);

    @Query("DELETE FROM talent WHERE id=:id")
    void deleteById(int id);

    @Query("DELETE FROM talent")
    void deleteAll();
}
