package com.example.foodapp.Supabase;

import com.example.foodapp.DataBinding;

import okhttp3.Request;

public class CategoryAPI extends SupabaseClient{
    public void GetAllCategories(SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "categories?select=*")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }
}
