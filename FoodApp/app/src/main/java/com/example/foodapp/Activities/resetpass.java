package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.Models.ResetPasswordReq;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class resetpass extends AppCompatActivity {

    EditText et_email;
    Button btn_resetpass;

    void init(){
        et_email = findViewById(R.id.resetpass_et_email);
        btn_resetpass = findViewById(R.id.resetpass_btn_resetpass);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resetpass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btn_resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(et_email.getText());
                if(Utilities.isEmail(email)){
                    AuthAPI authAPI = new AuthAPI();
                    authAPI.ResetPassword(new SupabaseClient.SBC_Callback() {
                        @Override
                        public void onFailure(IOException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onResponse(String responseBody) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Письмо отправленно", Toast.LENGTH_SHORT).show();

                                Intent verifresetpass = new Intent(getApplicationContext(), verifresetpass.class);
                                verifresetpass.putExtra("email", email);
                                startActivity(verifresetpass);
                            });
                        }
                    }, new ResetPasswordReq(email));
                }else{
                    Toast.makeText(getApplicationContext(), "Введите почту корректно!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btn_back(View view) {
        onBackPressed();
    }
}