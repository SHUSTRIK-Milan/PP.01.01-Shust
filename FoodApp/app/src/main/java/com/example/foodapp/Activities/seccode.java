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
import com.example.foodapp.Models.PhoneData;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class seccode extends AppCompatActivity {

    TextView tv1;
    EditText et_code;
    Button btn_confirm;

    String email, password, code;

    void init(){
        tv1 = findViewById(R.id.seccode_tv_1);
        et_code = findViewById(R.id.seccode_et_code);
        btn_confirm = findViewById(R.id.seccode_btn_confirm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seccode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intent = getIntent();
        boolean first = intent.getBooleanExtra("first", true);

        PhoneData phoneData = Utilities.phoneCheck(getApplicationContext());
        email = phoneData.getEmail();
        password = phoneData.getPassword();

        if(first){
            tv1.setText("Enter Code");
            btn_confirm.setOnClickListener(view -> {
                String code = String.valueOf(et_code.getText());

                if(code.length() < 4){
                    Toast.makeText(getApplicationContext(), "Введите код!", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent.putExtra("first", false);
                intent.putExtra("code", code);
                startActivity(intent);
            });
        }else{
            tv1.setText("Confirm Code");
            btn_confirm.setOnClickListener(view -> {
                String code = String.valueOf(et_code.getText());

                if(code.length() < 4){
                    Toast.makeText(getApplicationContext(), "Введите код!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String first_code = intent.getStringExtra("code");
                if(first_code == null) first_code = phoneData.getCode();

                if(code.equals(first_code)){
                    phoneData.setCode(code);
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
                                Auth auth = Utilities.objectFromJson(Auth.class, responseBody);

                                Utilities.phoneLogin(getApplicationContext(), phoneData);
                                Utilities.authUser(auth);
                                Utilities.authProfile(seccode.this, new Utilities.Callback<Boolean>() {
                                    @Override
                                    public void onResult(Boolean result) {
                                        Intent general = new Intent(getApplicationContext(), general.class);
                                        startActivity(general);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                            });
                        }
                    }, phoneData);
                }else{
                    Toast.makeText(getApplicationContext(), "Неккоретный код!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void seccode_btn_exit(View view) {
        Utilities.logout(this);
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
}