package com.example.foodapp;

import android.app.Activity;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;

import com.example.foodapp.Models.Category;
import com.example.foodapp.Models.Product;
import com.example.foodapp.Models.Profile;
import com.example.foodapp.Models.User;
import com.example.foodapp.Supabase.CategoryAPI;
import com.example.foodapp.Supabase.ProductAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataBinding {
    private static String BEARER_TOKEN;
    private static String USER_UUID;

    private static Profile PROFILE;
    private static User AUTH_USER;

    private static List<Category> CategoriesList = new ArrayList<>();
    private static List<Product> ProductsList = new ArrayList<>();

    public static void clearDataBinding(){
        setBearerToken(null);
        setUserUuid(null);
        setProfile(null);
        setAuthUser(null);
        setCategoriesList(new ArrayList<>());
        setProductsList(new ArrayList<>());
    }

    public static String getBearerToken() {
        return BEARER_TOKEN;
    }

    public static void setBearerToken(String bearerToken) {
        BEARER_TOKEN = bearerToken;
    }

    public static String getUserUuid() {
        return USER_UUID;
    }

    public static void setUserUuid(String userUuid) {
        USER_UUID = userUuid;
    }

    public static Profile getProfile() {
        return PROFILE;
    }

    public static void setProfile(Profile PROFILE) {
        DataBinding.PROFILE = PROFILE;
    }

    public static User getAuthUser() {
        return AUTH_USER;
    }

    public static void setAuthUser(User authUser) {
        AUTH_USER = authUser;
    }

    public static List<Category> getCategoriesList() {
        return CategoriesList;
    }

    public static void setCategoriesList(List<Category> categoriesList) {
        CategoriesList = categoriesList;
    }

    public static List<Product> getProductsList() {
        return ProductsList;
    }

    public static void setProductsList(List<Product> productsList) {
        ProductsList = productsList;
    }
}
