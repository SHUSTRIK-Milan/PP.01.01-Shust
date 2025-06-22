package com.example.foodapp.Models;

public class Profile {
    private String id;
    private String avatar_url;
    private String full_name;
    private boolean admin;
    public Cart carts;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Cart getCart() {
        return carts;
    }

    public void setCart(Cart cart) {
        this.carts = cart;
    }
}
