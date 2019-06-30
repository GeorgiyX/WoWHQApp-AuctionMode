package com.example.wowhqapp.databases.entity;
// package com.example;

import com.squareup.moshi.Json;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class WowClass implements ITalentsEntity {
    @PrimaryKey
    @Json(name = "id")
    private int id;
    @NonNull
    @Json(name = "name")
    private String name;
    @Json(name = "icon")
    private String icon;

    /**
     * No args constructor for use in serialization
     */
    public WowClass() {
    }

    /**
     * @param id
     * @param name
     * @param icon
     */
    @Ignore
    public WowClass(int id, @NonNull String name, String icon) {
        super();
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

}
