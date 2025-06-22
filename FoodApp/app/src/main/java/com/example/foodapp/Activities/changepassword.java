package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.Models.PhoneData;
import com.example.foodapp.Models.UpdateUserReq;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class changepassword extends AppCompatActivity {

    EditText et_pass;
    Button btn_verify;

    void init(){
        et_pass = findViewById(R.id.changepass_et_pass);
        btn_verify = findViewById(R.id.changepass_btn_verify);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changepassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btn_verify.setOnClickListener((v) -> {
            String password = String.valueOf(et_pass);

            AuthAPI authAPI = new AuthAPI();
            authAPI.UpdateUser(new SupabaseClient.SBC_Callback() {
                @Override
                public void onFailure(IOException e) {
                    runOnUiThread(() -> {
                        Log.e("PROFILE", e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(String responseBody) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Пароль обновлен", Toast.LENGTH_SHORT).show();

                        Intent intent = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAffinity();
                    });
                }
            }, new UpdateUserReq(password));
        });
    }

    public void btn_back(View view) {
        onBackPressed();
    }
}