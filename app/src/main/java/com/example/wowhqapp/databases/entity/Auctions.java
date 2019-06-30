package com.example.wowhqapp.databases.entity;

import com.squareup.moshi.Json;

import java.util.List;

public class Auctions {

    @Json(name = "auctions")
    private List<SimpleLot> auctions = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Auctions() {
    }

    /**
     *
     * @param auctions
     */
    public Auctions(List<SimpleLot> auctions) {
        super();
        this.auctions = auctions;
    }

    public List<SimpleLot> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<SimpleLot> auctions) {
        this.auctions = auctions;
    }

}