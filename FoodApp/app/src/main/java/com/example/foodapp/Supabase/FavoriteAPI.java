package com.example.foodapp.Supabase;

import com.example.foodapp.DataBinding;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FavoriteAPI extends SupabaseClient{
    public void GetFavoritesByUUID(SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "favorites?select=*&profile_id=eq."+DataBinding.getUserUuid())
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }

    public void AddFavorite(SBC_Callback callback, int productId){
        RequestBody body = RequestBody.create(mediaType, "{ \"profile_id\": \""+ DataBinding.getUserUuid() +"\", \"product_id\": \"" + productId + "\" }");
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "favorites")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }
    public void DeleteFavorite(SBC_Callback callback, int id){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "favorites?id=eq." + id)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .delete()
                .build();
        CallRequest(request, callback);
    }
}
