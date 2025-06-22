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

import com.example.foodapp.Models.LoginReq;
import com.example.foodapp.Models.Auth;
import com.example.foodapp.Models.PhoneData;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.ProfileAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class signup extends AppCompatActivity {

    EditText et_fullname, et_email, et_password;
    Button btn_login;
    ImageButton ibtn_password_show;

    private void init(){
        et_fullname = findViewById(R.id.signup_et_fullname);
        et_email = findViewById(R.id.signup_et_email);
        et_password = findViewById(R.id.signup_et_password);
        btn_login = findViewById(R.id.signup_btn_signup);
        ibtn_password_show = findViewById(R.id.signup_ibtn_password_show);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
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

    public void signup_btn_signup(View view) {
        String email = String.valueOf(et_email.getText());
        String password = String.valueOf(et_password.getText());
        String fullname = String.valueOf(et_fullname.getText());

        if(fullname.length() <= 0){
            Toast.makeText(getApplicationContext(), "Введите имя корректно!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Utilities.isEmail(email)){
            Toast.makeText(getApplicationContext(), "Введите почту корректно!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() <= 0) {
            Toast.makeText(getApplicationContext(), "Введите пароль корректно!", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthAPI authAPI = new AuthAPI();
        authAPI.Signup(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("SIGNUP", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Intent verifcode = new Intent(getApplicationContext(), verifcode.class);
                    verifcode.putExtra("email", email);
                    verifcode.putExtra("password", password);
                    verifcode.putExtra("fullname", fullname);
                    startActivity(verifcode);
                });
            }
        }, new LoginReq(email, password));
    }

    public void signup_btn_login(View view) {
        Intent login = new Intent(getApplicationContext(), login.class);
        startActivity(login);
    }
}