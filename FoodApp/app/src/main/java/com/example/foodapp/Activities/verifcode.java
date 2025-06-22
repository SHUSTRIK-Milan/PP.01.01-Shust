package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.Models.Auth;
import com.example.foodapp.Models.OTPReq;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.ProfileAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class verifcode extends AppCompatActivity {

    EditText et_code;
    TextView tv_resend;
    Button btn_verify;

    void init(){
        et_code = findViewById(R.id.verifcode_et_code);
        tv_resend = findViewById(R.id.verifcode_tv_resend);
        btn_verify = findViewById(R.id.verifcode_btn_verify);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verifcode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String fullname = getIntent().getStringExtra("fullname");

        btn_verify.setOnClickListener((v) -> {
            String otp = String.valueOf(et_code.getText());

            AuthAPI authAPI = new AuthAPI();
            authAPI.VerifyOTP(new SupabaseClient.SBC_Callback() {
                @Override
                public void onFailure(IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Wrong code", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(String responseBody) {
                    runOnUiThread(() -> {
                        Auth auth = Utilities.objectFromJson(Auth.class, responseBody);
                        Utilities.authUser(auth);

                        ProfileAPI profileAPI = new ProfileAPI();
                        profileAPI.ChangeProfileFullName(new SupabaseClient.SBC_Callback() {
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
                                    Intent login = new Intent(getApplicationContext(), login.class);
                                    startActivity(login);
                                });
                            }
                        }, fullname);
                    });
                }
            }, new OTPReq("email", email, otp));
        });

        tv_resend.setOnClickListener((v) -> {
            AuthAPI authAPI = new AuthAPI();
            authAPI.SendOTP(new SupabaseClient.SBC_Callback() {
                @Override
                public void onFailure(IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(String responseBody) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Code will be send", Toast.LENGTH_LONG).show();
                    });
                }
            }, email);
        });
    }

    public void btn_back(View view) {
        onBackPressed();
    }
}