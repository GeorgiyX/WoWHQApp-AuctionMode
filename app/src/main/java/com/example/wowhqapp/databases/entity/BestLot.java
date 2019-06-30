package com.example.wowhqapp.databases.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.squareup.moshi.Json;

@Entity(indices = {@Index("item")})
public class BestLot implements Lot{

    @PrimaryKey(autoGenerate = true)
    public long id;
    @Json(name = "item")
    private String item;
    @Json(name = "gameId")
    private Long gameId;
    @Json(name = "pet")
    private Long pet;
    @Json(name = "icon")
    private String icon;
    @Json(name = "bid")
    private Long bid;
    @Json(name = "buyout")
    private Long buyout;
    @Json(name = "owner")
    private String owner;
    @Json(name = "time:")
    private String time;
    @Json(name = "quantity:")
    private Long quantity;
    @Json(name = "slug:")
    private String slug;
    private String timeOfSearch;


    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public BestLot() {
    }

    /**
     *
     * @param icon
     * @param time
     * @param buyout
     * @param pet
     * @param gameId
     * @param item
     * @param owner
     * @param bid
     */
    public BestLot(String item, Long gameId, Long pet, String icon, Long bid, Long buyout, String owner, String time, Long quantity, String slug) {
        super();
        this.item = item;
        this.gameId = gameId;
        this.pet = pet;
        this.icon = icon;
        this.bid = bid;
        this.buyout = buyout;
        this.owner = owner;
        this.time = time;
        this.slug =slug;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long  getPet() {
        return pet;
    }

    public void setPet(Long  pet) {
        this.pet = pet;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getBuyout() {
        return buyout;
    }

    public void setBuyout(Long buyout) {
        this.buyout = buyout;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public Long getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getTimeOfSearch(){ return timeOfSearch;}

    public void setTimeOfSearch(String timeOfSearch) {this.timeOfSearch = timeOfSearch;}

}


