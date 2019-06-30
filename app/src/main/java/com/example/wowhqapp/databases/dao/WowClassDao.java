package com.example.wowhqapp.databases.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.wowhqapp.databases.entity.WowClass;

import java.util.List;


@Dao
public interface WowClassDao {
    // -------Get--------------------
    @Query("SELECT * FROM wowclass")
    List<WowClass> getAll();

    @Query("SELECT * FROM wowclass WHERE id=:id")
    WowClass getById(int id);

    //-------Insert------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WowClass... wowClasses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Iterable<WowClass> wowClasses);


    // -------Update-----------------
    @Update
    void update(WowClass... wowClasses);

    @Update
    void  update(Iterable<WowClass> wowClasses);


    // -------Delete-----------------
    @Delete
    void delete(WowClass... wowClasses);

    @Delete
    void delete(Iterable<WowClass> wowClasses);

    @Query("DELETE FROM wowclass WHERE id=:id")
    void deleteById(int id);

    @Query("DELETE FROM wowclass")
    void deleteAll();
}
