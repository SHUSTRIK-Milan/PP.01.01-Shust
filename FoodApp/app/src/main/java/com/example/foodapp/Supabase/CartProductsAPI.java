package com.example.foodapp.Supabase;

import com.example.foodapp.DataBinding;

import okhttp3.Request;
import okhttp3.RequestBody;

public class CartProductsAPI extends SupabaseClient{
    public void ChangeCount(SBC_Callback callback, int cartProductId, int count){
        RequestBody body = RequestBody.create(mediaType, "{ \"count\": \""+ count + "\"}");
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "cart_products?id=eq." + cartProductId)
                .patch(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }

    public void AddCartProduct(SBC_Callback callback, int productId){
        RequestBody body = RequestBody.create(mediaType, "{ \"cart_id\": \""+ DataBinding.getProfile().getCart().getId() +"\", \"product_id\": \"" + productId + "\", \"count\": 1}");
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "cart_products")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }

    public void DeleteCartProduct(SBC_Callback callback, int cartProductId){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "cart_products?id=eq." + cartProductId)
                .delete()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }

    public void DeleteCartProductByCartID(SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "cart_products?cart_id=eq." + DataBinding.getProfile().getCart().getId())
                .delete()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }
}
