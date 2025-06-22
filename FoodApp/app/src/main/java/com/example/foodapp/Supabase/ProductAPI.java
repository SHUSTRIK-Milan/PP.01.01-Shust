package com.example.foodapp.Supabase;

import com.example.foodapp.DataBinding;

import okhttp3.Request;

public class ProductAPI extends SupabaseClient{
    public void GetAllProducts(SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "products?select=*,categories(*),favorites(*),cart_products(*)&favorites.profile_id=eq." + DataBinding.getUserUuid() + "&cart_products.cart_id=eq." + DataBinding.getProfile().getCart().getId())
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }

    public void GetProductsByCategory(SBC_Callback callback, Integer categoryId){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "products?select=*,categories(*),favorites(*)&category=eq." + categoryId + "&favorites.profile_id=eq." + DataBinding.getUserUuid())
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }

    public void GetProductsByCategoryWithPriceFilter(SBC_Callback callback, Integer categoryId, float minPrice, float maxPrice){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "products?select=*,categories(*),favorites(*)&category=eq." + categoryId + "&favorites.profile_id=eq." + DataBinding.getUserUuid() + "&price=gt." + minPrice + "&price=lt." + maxPrice)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }
}
