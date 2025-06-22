package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
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

public class verifresetpass extends AppCompatActivity {

    EditText et_code;
    Button btn_verify;

    void init(){
        et_code = findViewById(R.id.verifreset_et_code);
        btn_verify = findViewById(R.id.verifreset_btn_verify);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verifresetpass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        String email = getIntent().getStringExtra("email");

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

                        Intent changepassword = new Intent(getApplicationContext(), changepassword.class);
                        startActivity(changepassword);
                    });
                }
            }, new OTPReq("email", email, otp));
        });
    }

    public void btn_back(View view) {
        onBackPressed();
    }
}