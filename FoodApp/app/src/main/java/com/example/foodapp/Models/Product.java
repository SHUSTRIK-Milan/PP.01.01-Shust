package com.example.foodapp.Models;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private String desc;
    private float price;
    private String image_url;
    private Category categories;

    private List<Favorite> favorites;
    private List<CartProduct> cart_products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public boolean isFavorite(){
        boolean isFavorite = !this.favorites.isEmpty() && this.favorites.get(0) != null;
        return isFavorite;
    }

    public Favorite getFavorite(){
        if (isFavorite()) {
            return favorites.get(0);
        } else {
            return null;
        }
    }

    public List<CartProduct> getCartProducts() {
        return cart_products;
    }

    public void setCartProducts(List<CartProduct> cart_products) {
        this.cart_products = cart_products;
    }

    public boolean inCart(){
        boolean inCart = !this.cart_products.isEmpty() && this.cart_products.get(0) != null;
        return inCart;
    }

    public CartProduct getCartProduct(){
        if (inCart()) {
            return cart_products.get(0);
        } else {
            return null;
        }
    }
}
