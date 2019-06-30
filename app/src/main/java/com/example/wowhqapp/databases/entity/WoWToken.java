package com.example.wowhqapp.databases.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

@Entity(indices = {@Index("region")})
public class WoWToken {

    @Json(name = "region")
    @NonNull
    @PrimaryKey
    private String region;
    @Json(name = "date")
    private String date;
    @Json(name = "current_price_blizzard_api")
    private Long currentPriceBlizzardApi;
    @Json(name = "last_change")
    private Long lastChange;
    @Json(name = "one_day_low")
    private Long oneDayLow;
    @Json(name = "one_day_high")
    private Long oneDayHigh;

    /**
     * No args constructor for use in serialization
     *
     */
    public WoWToken() {
    }

    /**
     *
     * @param region
     * @param currentPriceBlizzardApi
     * @param oneDayLow
     * @param oneDayHigh
     * @param date
     * @param lastChange
     */
    @Ignore
    public WoWToken(String region, String date, Long currentPriceBlizzardApi, Long lastChange, Long oneDayLow, Long oneDayHigh) {
        super();
        this.region = region;
        this.date = date;
        this.currentPriceBlizzardApi = currentPriceBlizzardApi;
        this.lastChange = lastChange;
        this.oneDayLow = oneDayLow;
        this.oneDayHigh = oneDayHigh;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCurrentPriceBlizzardApi() {
        return currentPriceBlizzardApi;
    }

    public void setCurrentPriceBlizzardApi(Long currentPriceBlizzardApi) {
        this.currentPriceBlizzardApi = currentPriceBlizzardApi;
    }

    public Long getLastChange() {
        return lastChange;
    }

    public void setLastChange(Long lastChange) {
        this.lastChange = lastChange;
    }

    public Long getOneDayLow() {
        return oneDayLow;
    }

    public void setOneDayLow(Long oneDayLow) {
        this.oneDayLow = oneDayLow;
    }

    public Long getOneDayHigh() {
        return oneDayHigh;
    }

    public void setOneDayHigh(Long oneDayHigh) {
        this.oneDayHigh = oneDayHigh;
    }

}