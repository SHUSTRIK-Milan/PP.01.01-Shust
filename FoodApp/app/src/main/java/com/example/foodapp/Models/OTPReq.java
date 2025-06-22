package com.example.foodapp.Models;

public class OTPReq {
    private String type;
    private String email;
    private String token;

    public OTPReq(String type, String email, String token) {
        this.type = type;
        this.email = email;
        this.token = token;
    }
}
