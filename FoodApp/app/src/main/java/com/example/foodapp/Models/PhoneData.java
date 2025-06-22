package com.example.foodapp.Models;

public class PhoneData extends LoginReq {
    private String code;

    public PhoneData(String email, String password, String code) {
        super(email, password);
        this.code = code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
