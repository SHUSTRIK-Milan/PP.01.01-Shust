package com.example.foodapp.Supabase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.Photo;
import com.example.foodapp.Models.SupabaseError;
import com.example.foodapp.Models.UpdateUserReq;
import com.example.foodapp.Utilities;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseClient {

    public interface SBC_Callback{
        void onFailure(IOException e);
        void onResponse(String responseBody);
    }

    public static String DOMAIN_NAME = "https://ulxdsxpsdfngolyltufw.supabase.co/";
    public static String REST_PATH = "rest/v1/";
    public static String AUTH_PATH = "auth/v1/";
    public static String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVseGRzeHBzZGZuZ29seWx0dWZ3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDkwMTU0MTcsImV4cCI6MjA2NDU5MTQxN30.eX9NT81rHxNIE72_co6YQpoN3s2UC1NMxcCUKsKqfPY";

    protected OkHttpClient client = new OkHttpClient();
    protected MediaType mediaType = MediaType.parse("application/json");

    protected void CallRequest(final Request request, final SBC_Callback callback){
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                Log.e("HTTP_RESPONSE", responseBody);

                if(response.isSuccessful()){
                    callback.onResponse(responseBody);
                }else{
                    SupabaseError error = Utilities.objectFromJson(SupabaseError.class, responseBody);

                    String error_msg = error.getMsg();
                    if(error_msg == null) return;
                    callback.onFailure(new IOException("Ошибка: " + error.getMsg()));
                }
            }
        });
    }

    public static String GetImageURL(final String IMAGE_NAME, final String FOLDER){
        return DOMAIN_NAME + "storage/v1/object/public/" + FOLDER + "/" + IMAGE_NAME;
    }

    public void UploadPhoto(SBC_Callback callback, Photo photo) throws IOException {
        RequestBody requestBody = RequestBody.create(photo.getImageBytes(), MediaType.parse("image/jpeg"));

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + "storage/v1/object/avatars/" + photo.getFilename() + "?upsert=true")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .addHeader("Content-Type", "image/jpeg")
                .put(requestBody)
                .build();
        CallRequest(request, callback);
    }

    public void DeletePhoto(SBC_Callback callback, String path){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + "storage/v1/object/" + path)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .delete()
                .build();
        CallRequest(request, callback);
    }
}
