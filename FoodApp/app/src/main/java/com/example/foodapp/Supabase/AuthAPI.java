package com.example.foodapp.Supabase;

import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.LoginReq;
import com.example.foodapp.Models.OTPReq;
import com.example.foodapp.Models.ResetPasswordReq;
import com.example.foodapp.Models.UpdateUserReq;
import com.google.gson.Gson;

import okhttp3.Request;
import okhttp3.RequestBody;

public class AuthAPI extends SupabaseClient {
    public void Login(SBC_Callback callback, LoginReq loginReq){
        Gson gson = new Gson();
        String json = gson.toJson(loginReq);
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "token?grant_type=password")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .build();
        CallRequest(request, callback);
    }

    public void Signup(SBC_Callback callback, LoginReq loginReq){
        Gson gson = new Gson();
        String json = gson.toJson(loginReq);
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "signup")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .build();
        CallRequest(request, callback);
    }

    public void Logout(SBC_Callback callback){
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "logout")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .build();
        CallRequest(request, callback);
    }

    public void UpdateUser(SBC_Callback callback, UpdateUserReq userReq){
        Gson gson = new Gson();
        String json = gson.toJson(userReq);
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "user")
                .method("PUT", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + DataBinding.getBearerToken())
                .addHeader("Content-Type", "application/json")
                .build();
        CallRequest(request, callback);
    }

    public void ResetPassword(SBC_Callback callback, ResetPasswordReq resetPasswordReq){
        Gson gson = new Gson();
        String json = gson.toJson(resetPasswordReq);
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "recover")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .build();
        CallRequest(request, callback);
    }

    public void SendOTP(SBC_Callback callback, String email){
        RequestBody body = RequestBody.create(mediaType, "{ \"email\": \""+ email + "\"}");

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "otp")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .build();
        CallRequest(request, callback);
    }

    public void VerifyOTP(SBC_Callback callback, OTPReq otpReq){
        Gson gson = new Gson();
        String json = gson.toJson(otpReq);
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "verify")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .build();
        CallRequest(request, callback);
    }
}
