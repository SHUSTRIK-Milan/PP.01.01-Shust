package com.example.foodapp.Models;

public class Cart {
    private int id;
    private String profile_id;
    private float total_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileId() {
        return profile_id;
    }

    public void setProfileId(String profile_id) {
        this.profile_id = profile_id;
    }

    public float getTotalPrice() {
        return total_price;
    }

    public void setTotalPrice(float total_price) {
        this.total_price = total_price;
    }
}
