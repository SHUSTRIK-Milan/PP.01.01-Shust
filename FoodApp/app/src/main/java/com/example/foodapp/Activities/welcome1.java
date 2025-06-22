package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.LoginReq;
import com.example.foodapp.Models.PhoneData;
import com.example.foodapp.R;
import com.example.foodapp.Utilities;

import java.util.Objects;

public class welcome1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        DataBinding.clearDataBinding();

        new Handler().postDelayed(() -> {
            PhoneData phoneData = Utilities.phoneCheck(getApplicationContext());
            if(!Objects.equals(phoneData.getEmail(), "")){
                Intent code = new Intent(getApplicationContext(), seccode.class);
                code.putExtra("first", false);
                startActivity(code);
            }else{
                Intent welcome2 = new Intent(getApplicationContext(), welcome2.class);
                welcome2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                welcome2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(welcome2);
                finish();
            }
        }, 2000);
    }
}