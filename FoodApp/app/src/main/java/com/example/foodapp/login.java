package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void btn_back(View view) {
        onBackPressed();
    }

    public void login_btn_login(View view) {
//        Intent login = new Intent(getApplicationContext(), login.class);
//        startActivity(login);
    }

    public void login_btn_signup(View view) {
        Intent signup = new Intent(getApplicationContext(), signup.class);
        startActivity(signup);
    }

    public void login_btn_resetpass(View view) {
        Intent resetpass = new Intent(getApplicationContext(), resetpass.class);
        startActivity(resetpass);
    }
}