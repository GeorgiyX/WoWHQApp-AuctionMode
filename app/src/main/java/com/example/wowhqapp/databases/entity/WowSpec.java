package com.example.wowhqapp.databases.entity;
// package com.example;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

@Entity(
        indices = {
                @Index(value = {"classId"})
        },
        foreignKeys = @ForeignKey(
                entity = WowClass.class,
                parentColumns = "id",
                childColumns = "classId"
        )
)
public class WowSpec implements ITalentsEntity {
    @Json(name = "class_id")
    private int classId;
    @PrimaryKey
    @Json(name = "id")
    private int id;
    @Json(name = "spec_order")
    private int specOrder;
    @NonNull
    @Json(name = "name")
    private String name;
    @NonNull
    @Json(name = "description")
    private String description;
    @Json(name = "icon")
    private String icon;

    /**
     * No args constructor for use in serialization
     */
    public WowSpec() {
    }

    /**
     *
     * @param id
     * @param icon
     * @param classId
     * @param description
     * @param name
     * @param specOrder
     */
    @Ignore
    public WowSpec(int classId, int id, int specOrder, @NonNull String name, @NonNull String description, String icon) {
        super();
        this.classId = classId;
        this.id = id;
        this.specOrder = specOrder;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int specId) {
        this.id = specId;
    }

    public int getSpecOrder() {
        return specOrder;
    }

    public void setSpecOrder(int specOrder) {
        this.specOrder = specOrder;
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

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
