package com.example.foodapp.Supabase;

import android.util.Log;

import com.example.foodapp.DataBinding;

import okhttp3.Request;
import okhttp3.RequestBody;

public class ProfileAPI extends SupabaseClient{

    public void GetProfile(SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "profiles?select=*,carts(*)&id=eq."+DataBinding.getUserUuid())
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer "+DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }
    public void ChangeProfileFullName(SBC_Callback callback, String fullName){
        String json = "{ \"full_name\": \""+fullName+"\" }";
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "profiles?id=eq." + DataBinding.getUserUuid())
                .method("PATCH", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        Log.e("PATCH_BODY", body.toString());
        CallRequest(request, callback);
    }

    public void ChangeProfileAvatarUrl(SBC_Callback callback, String avatarUrl){
        String json = "{ \"avatar_url\": \""+avatarUrl+"\" }";
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "profiles?id=eq." + DataBinding.getUserUuid())
                .method("PATCH", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        Log.e("PATCH_BODY", body.toString());
        CallRequest(request, callback);
    }

    public void ChangeProfileAdmin(SBC_Callback callback, boolean admin){
        String json = "{ \"admin\": \""+admin+"\" }";
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "profiles?id=eq." + DataBinding.getUserUuid())
                .method("PATCH", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        Log.e("PATCH_BODY", body.toString());
        CallRequest(request, callback);
    }
}
