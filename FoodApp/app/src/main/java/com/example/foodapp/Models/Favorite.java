package com.example.foodapp.Models;

public class Favorite {
    private int id;
    private String profile_id;
    private int product_id;

    public Favorite(int id, String profile_id, int product_id) {
        this.id = id;
        this.profile_id = profile_id;
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
