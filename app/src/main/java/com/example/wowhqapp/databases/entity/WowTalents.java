
package com.example.wowhqapp.databases.entity;

import java.util.List;

import com.squareup.moshi.Json;

public class WowTalents {

    @Json(name = "talents")
    private List<Talent> talents = null;

    /**
     * No args constructor for use in serialization
     */
    public WowTalents() {
    }

    /**
     * @param talents
     */
    public WowTalents(List<Talent> talents) {
        super();
        this.talents = talents;
    }

    public List<Talent> getTalents() {
        return talents;
    }

    public void setTalents(List<Talent> talents) {
        this.talents = talents;
    }

}
