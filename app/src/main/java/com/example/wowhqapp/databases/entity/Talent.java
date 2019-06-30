
package com.example.wowhqapp.databases.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

@Entity(
        indices = {
                @Index(value = {"specId"})
        },
        foreignKeys = @ForeignKey(
                entity = WowSpec.class,
                parentColumns = "id",
                childColumns = "specId"
        )
)
public class Talent implements ITalentsEntity {
    @PrimaryKey
    @Json(name = "id")
    private int id;
    @Json(name = "spec_id")
    private int specId;
    @NonNull
    @Json(name = "name")
    private String name;
    @NonNull
    @Json(name = "description")
    private String description;
    @NonNull
    @Json(name = "castTime")
    private String castTime;
    @Json(name = "range")
    private String range;
    @Json(name = "powerCost")
    private String powerCost;
    @Json(name = "cooldown")
    private String cooldown;
    @Json(name = "row")
    private int row;
    @Json(name = "col")
    private int col;
    @Json(name = "icon")
    private String icon;

    /**
     * No args constructor for use in serialization
     */
    public Talent() {
    }

    /**
     * @param id
     * @param col
     * @param castTime
     * @param range
     * @param description
     * @param name
     * @param specId
     * @param powerCost
     * @param cooldown
     * @param row
     * @param icon
     */
    @Ignore
    public Talent(int id, int specId, @NonNull String name,
                  @NonNull String description, @NonNull String castTime, String range,
                  String powerCost, String cooldown, int row, int col, String icon) {
        super();
        this.id = id;
        this.specId = specId;
        this.name = name;
        this.description = description;
        this.castTime = castTime;
        this.range = range;
        this.powerCost = powerCost;
        this.cooldown = cooldown;
        this.row = row;
        this.col = col;
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

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
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

    @NonNull
    public String getCastTime() {
        return castTime;
    }

    public void setCastTime(@NonNull String castTime) {
        this.castTime = castTime;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getPowerCost() {
        return powerCost;
    }

    public void setPowerCost(String powerCost) {
        this.powerCost = powerCost;
    }

    public String getCooldown() {
        return cooldown;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
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
