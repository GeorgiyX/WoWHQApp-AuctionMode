package com.example.wowhqapp.databases.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.wowhqapp.WowhqApplication;
import com.example.wowhqapp.databases.dao.WowClassDao;
import com.example.wowhqapp.databases.dao.WowSpecDao;
import com.example.wowhqapp.databases.dao.WowTalentDao;
import com.example.wowhqapp.databases.entity.Talent;
import com.example.wowhqapp.databases.entity.WowClass;
import com.example.wowhqapp.databases.entity.WowSpec;

@Database(entities = {WowClass.class, WowSpec.class, Talent.class}, version = 2)
public abstract class TalentsCalculatorDatabase extends RoomDatabase {

    public abstract WowSpecDao getWowSpecsDao();

    public abstract WowClassDao getWowClassesDao();

    public abstract WowTalentDao getWowTalentsDao();

    public static TalentsCalculatorDatabase from(Context context) {
        return WowhqApplication.from(context).getTalentsCalculatorDatabase();
    }
}
