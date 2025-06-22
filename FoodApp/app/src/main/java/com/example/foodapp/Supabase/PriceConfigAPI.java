package com.example.foodapp.Supabase;

import com.example.foodapp.DataBinding;

import okhttp3.Request;

public class PriceConfigAPI extends SupabaseClient{
    public void GetPriceConfig(SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "price_config?select=*&id=eq.1")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }
}
