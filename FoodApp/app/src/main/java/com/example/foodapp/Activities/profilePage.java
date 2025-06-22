package com.example.foodapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.PhoneData;
import com.example.foodapp.Models.Photo;
import com.example.foodapp.Models.UpdateUserReq;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.ProfileAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class profilePage extends AppCompatActivity {

    ActivityResultLauncher<Intent> imagePicker;

    ImageView iv_avatar;
    EditText et_fullname, et_email, et_password;

    void init(){
        iv_avatar = findViewById(R.id.profile_iv_avatar);
        et_fullname = findViewById(R.id.profile_et_fullname);
        et_email = findViewById(R.id.profile_et_email);
        et_password = findViewById(R.id.profile_et_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        et_fullname.setText(DataBinding.getProfile().getFull_name());
        et_email.setText(DataBinding.getAuthUser().getEmail());

        Utilities.getAvatar(getApplicationContext(), iv_avatar);

        imagePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Uri imageUri = data.getData();
                        Photo photo = Utilities.getPhoto(getApplicationContext(), imageUri);

                        SupabaseClient supabaseClient = new SupabaseClient();

                        try {
                            supabaseClient.UploadPhoto(new SupabaseClient.SBC_Callback() {
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
                                        Toast.makeText(getApplicationContext(), "Изображение загружено", Toast.LENGTH_SHORT).show();

                                        ProfileAPI profileAPI = new ProfileAPI();
                                        profileAPI.ChangeProfileAvatarUrl(new SupabaseClient.SBC_Callback() {
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
                                                    Toast.makeText(getApplicationContext(), "Аватар изменен", Toast.LENGTH_SHORT).show();
                                                });
                                            }
                                        }, photo.getFilename());
                                    });
                                }
                            }, photo);
                        } catch (IOException e) {
                            Log.e("PhotoUpload", e.getLocalizedMessage());
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    public void profile_iv_avatar(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePicker.launch(intent);
    }


    public void profile_btn_changedata(View view) {
        String fullname = String.valueOf(et_fullname.getText());
        String password = String.valueOf(et_password.getText());

        if(!password.isEmpty()){
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

                        PhoneData phoneData = Utilities.phoneCheck(getApplicationContext());
                        phoneData.setPassword(password);
                        Utilities.phoneLogin(getApplicationContext(), phoneData);
                    });
                }
            }, new UpdateUserReq(password));
        }

        if(!fullname.equals(DataBinding.getProfile().getFull_name())){
            ProfileAPI profileAPI = new ProfileAPI();
            profileAPI.ChangeProfileFullName(new SupabaseClient.SBC_Callback() {
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
                        Toast.makeText(getApplicationContext(), "Имя обновлено", Toast.LENGTH_SHORT).show();
                    });
                }
            }, fullname);
        }

        profile_btn_refresh(null);
    }

    public void profile_btn_refresh(View view) {
        Utilities.authProfile(this, new Utilities.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

            @Override
            public void onError(Exception e) {}
        });
    }

    public void profile_btn_exit(View view) {
        Utilities.logout(this);

        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
}