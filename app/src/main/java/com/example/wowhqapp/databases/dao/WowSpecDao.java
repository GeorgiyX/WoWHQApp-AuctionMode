package com.example.wowhqapp.databases.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.wowhqapp.databases.entity.WowClass;
import com.example.wowhqapp.databases.entity.WowSpec;

import java.util.List;

@Dao
public interface WowSpecDao {
    // -------Get--------------------
    @Query("SELECT * FROM wowspec")
    List<WowSpec> getAll();

    @Query("SELECT * FROM wowspec WHERE id=:id")
    WowSpec getById(int id);

    @Query("SELECT * FROM wowclass WHERE id=:classId")
    WowClass getClass(int classId);

    @Query("SELECT * FROM wowspec WHERE classId=:classId ORDER BY specOrder ASC")
    List<WowSpec> getByClassId(int classId);


    //-------Insert------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WowSpec... wowSpecs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Iterable<WowSpec> wowSpecs);


    // -------Update-----------------
    @Update
    void update(WowSpec... wowSpecs);

    @Update
    void update(Iterable<WowSpec> wowSpecs);


    // -------Delete-----------------
    @Delete
    void delete(WowSpec... wowSpecs);

    @Delete
    void delete(Iterable<WowSpec> wowSpecs);

    @Query("DELETE FROM wowspec WHERE id=:id")
    void deleteById(int id);

    @Query("DELETE FROM wowspec")
    void deleteAll();
}
