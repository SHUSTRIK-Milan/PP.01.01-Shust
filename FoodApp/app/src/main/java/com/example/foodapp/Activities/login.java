package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.LoginReq;
import com.example.foodapp.Models.Auth;
import com.example.foodapp.Models.PhoneData;
import com.example.foodapp.Models.Photo;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class login extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login;
    ImageButton ibtn_password_show;

    private void init(){
        et_email = findViewById(R.id.login_et_email);
        et_password = findViewById(R.id.login_et_password);
        btn_login = findViewById(R.id.login_btn_login);
        ibtn_password_show = findViewById(R.id.login_ibtn_password_show);
    }

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

        init();

        ibtn_password_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.passwordShow(et_password, ibtn_password_show);
            }
        });
    }

    public void btn_back(View view) {
        onBackPressed();
    }

    public void login_btn_login(View view) {
        String email = String.valueOf(et_email.getText());
        String password = String.valueOf(et_password.getText());

        if(!Utilities.isEmail(email)){
            Toast.makeText(getApplicationContext(), "Введите почту корректно!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() <= 0){
            Toast.makeText(getApplicationContext(), "Введите пароль корректно!", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneData phoneData = new PhoneData(email, password, "");
        AuthAPI authAPI = new AuthAPI();
        authAPI.Login(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("AUTH", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Utilities.phoneLogin(getApplicationContext(), phoneData);
                    Intent code = new Intent(getApplicationContext(), seccode.class);
                    startActivity(code);
                });
            }
        }, phoneData);
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