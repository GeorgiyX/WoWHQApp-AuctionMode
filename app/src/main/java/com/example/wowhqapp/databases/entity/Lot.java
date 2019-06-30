package com.example.wowhqapp.databases.entity;

public interface Lot {
    public String getItem();

    public void setItem(String item);

    public Long getGameId();

    public void setGameId(Long gameId);

    public Long  getPet();

    public void setPet(Long  pet);

    public String getIcon();

    public void setIcon(String icon);

    public Long getBid();

    public void setBid(Long bid);

    public Long getBuyout();

    public void setBuyout(Long buyout);

    public String getOwner();

    public void setOwner(String owner);

    public String getTime();

    public void setTime(String time);

    public String getSlug();

    public void setSlug(String slug);

    public Long getQuantity();

    public void setQuantity(Long quantity);
}
