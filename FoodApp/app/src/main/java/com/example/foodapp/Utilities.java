package com.example.foodapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.foodapp.Activities.general;
import com.example.foodapp.Activities.productPage;
import com.example.foodapp.Activities.resetpass;
import com.example.foodapp.Adapters.ProductsAdapter;
import com.example.foodapp.Models.Auth;
import com.example.foodapp.Models.Category;
import com.example.foodapp.Models.PhoneData;
import com.example.foodapp.Models.Photo;
import com.example.foodapp.Models.PriceConfig;
import com.example.foodapp.Models.Product;
import com.example.foodapp.Models.Profile;
import com.example.foodapp.Supabase.AuthAPI;
import com.example.foodapp.Supabase.CategoryAPI;
import com.example.foodapp.Supabase.FavoriteAPI;
import com.example.foodapp.Supabase.PriceConfigAPI;
import com.example.foodapp.Supabase.ProductAPI;
import com.example.foodapp.Supabase.ProfileAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public interface Callback<T> {
        void onResult(T result);
        void onError(Exception e);
    }
    public static boolean isEmail(String str){
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(str);
        return matcher.matches();
    }

    public static void passwordShow(EditText et_password, ImageButton ibtn_password_show){
        String tag = (String) ibtn_password_show.getTag();

        if(Objects.equals(tag, "close")){
            ibtn_password_show.setTag("open");
            ibtn_password_show.setImageResource(R.drawable.icon_eye_open);

            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else{
            ibtn_password_show.setTag("close");
            ibtn_password_show.setImageResource(R.drawable.icon_eye_close);

            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public static ProductsAdapter createProductsAdapter(Activity activity, ProductsAdapter.Template template, List<Product> productList){
        ProductsAdapter productsAdapter = new ProductsAdapter(activity, template, productList, (position, view) -> {
            Product product = productList.get(position);
            Intent intent = new Intent(activity.getApplicationContext(), productPage.class);
            intent.putExtra("product_id", product.getId());

            activity.startActivity(intent);
        }, (position, view) -> {
            Utilities.markFavorite(view, !(Boolean) view.getTag());

            Product product = productList.get(position);
            for(int i = 0; i < DataBinding.getProductsList().size(); i++){
                Product updateProduct = DataBinding.getProductsList().get(i);
                if(product.getId() == updateProduct.getId()) product = updateProduct;
            }

            FavoriteAPI favoriteAPI = new FavoriteAPI();

            if(product.isFavorite()){
                favoriteAPI.DeleteFavorite(new SupabaseClient.SBC_Callback() {
                    @Override
                    public void onFailure(IOException e) {}

                    @Override
                    public void onResponse(String responseBody) {
                        activity.runOnUiThread(() -> {
                            Utilities.UpdateProductsList(activity, null);
                        });
                    }
                }, product.getFavorite().getId());
            }else{
                favoriteAPI.AddFavorite(new SupabaseClient.SBC_Callback() {
                    @Override
                    public void onFailure(IOException e) {}

                    @Override
                    public void onResponse(String responseBody) {
                        activity.runOnUiThread(() -> {
                            Utilities.UpdateProductsList(activity, null);
                        });
                    }
                }, product.getId());
            }
        });
        return productsAdapter;
    }

    public static void markFavorite(View view, boolean isFavorite){
        if(isFavorite){
            view.setTag(true);
            view.setBackgroundTintList(ContextCompat.getColorStateList(view.getContext(), R.color.orange));
        }else{
            view.setTag(false);
            view.setBackgroundTintList(ContextCompat.getColorStateList(view.getContext(), R.color.white));
        }
    }

    public static <T> T objectFromJson(Class<T> req_class, String responseBody){
        Gson gson = new Gson();
        T object = gson.fromJson(responseBody, req_class);

        return object;
    }

    public static byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        try{
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        }catch (IOException ignored){}

        return buffer.toByteArray();
    }

    public static Photo getPhoto(Context context, Uri imageUri) {
        InputStream inputStream = null;
        try{
            inputStream = context.getContentResolver().openInputStream(imageUri);
        }catch (IOException ignored){}

        assert inputStream != null;
        byte[] imageBytes = Utilities.getBytes(inputStream);
        String fileName = DataBinding.getUserUuid() + ".jpg";

        return new Photo(imageBytes, fileName);
    }


    public static void getAvatar(Context context, ImageView imageView){
        Glide.with(context)
                .load(SupabaseClient.GetImageURL(DataBinding.getProfile().getAvatar_url(), "avatars"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.app_logo)
                .into(imageView);
    }

    public static void authUser(Auth auth){
        DataBinding.setAuthUser(auth.getUser());
        DataBinding.setBearerToken(auth.getAccess_token());
        DataBinding.setUserUuid(auth.getUser().getId());
    }

    public static void authProfile(Activity activity, @Nullable Utilities.Callback<Boolean> callback){
        ProfileAPI profileAPI = new ProfileAPI();
        profileAPI.GetProfile(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                activity.runOnUiThread(() -> {
                    Log.e("GetProfile", e.getMessage());
                    Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    assert callback != null;
                    callback.onError(e);
                });
            }

            @Override
            public void onResponse(String responseBody) {
                activity.runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Profile>>(){}.getType();
                    List<Profile> profileList = gson.fromJson(responseBody, type);
                    DataBinding.setProfile(profileList.get(0));

                    assert callback != null;
                    callback.onResult(true);
                });
            }
        });
    }

    public static void phoneLogin(Context context, PhoneData phoneData){
        SharedPreferences sharedPref = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", phoneData.getEmail());
        editor.putString("password", phoneData.getPassword());
        editor.putString("code", phoneData.getCode());

        editor.apply();
    }

    public static PhoneData phoneCheck(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String password = sharedPref.getString("password", "");
        String code = sharedPref.getString("code", "");

        return new PhoneData(email, password, code);
    }

    public static void phoneUnLogin(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email");
        editor.remove("password");
        editor.remove("code");

        editor.apply();
    }

    public static void logout(Activity activity){
        phoneUnLogin(activity.getApplicationContext());

        AuthAPI authAPI = new AuthAPI();
        authAPI.Logout(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {}

            @Override
            public void onResponse(String responseBody) {}
        });
    }

    public static void UpdateCategoriesList(Activity activity, @Nullable Callback<Boolean> callback){
        CategoryAPI categoryAPI = new CategoryAPI();
        categoryAPI.GetAllCategories(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                activity.runOnUiThread(() -> {
                    Log.e("GetAllCategories", e.getLocalizedMessage());
                });
            }

            @Override
            public void onResponse(String responseBody) {
                activity.runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Category>>(){}.getType();

                    List<Category> categoryList = gson.fromJson(responseBody, type);
                    categoryList.sort(Comparator.comparingInt(Category::getOrder));

                    DataBinding.setCategoriesList(categoryList);

                    try{
                        //Toast.makeText(activity.getApplicationContext(), "Категории обновлены", Toast.LENGTH_SHORT).show();
                        callback.onResult(true);
                    }catch (Exception ignored){}
                });
            }
        });
    }

    public static void UpdateProductsList(Activity activity, @Nullable Callback<Boolean> callback){
        ProductAPI productAPI = new ProductAPI();
        productAPI.GetAllProducts(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                activity.runOnUiThread(() -> {
                    Log.e("GetAllProducts", e.getLocalizedMessage());
                });
            }

            @Override
            public void onResponse(String responseBody) {
                activity.runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Product>>(){}.getType();
                    DataBinding.setProductsList(gson.fromJson(responseBody, type));

                    try{
                        //Toast.makeText(activity.getApplicationContext(), "Продукты обновлены", Toast.LENGTH_SHORT).show();
                        callback.onResult(true);
                    }catch (Exception ignored){}
                });
            }
        });
    }

    public static void UpdateCategoriesAndProducts(Activity activity, Utilities.Callback<Boolean> callback){
        Utilities.UpdateCategoriesList(activity, new Utilities.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                Utilities.UpdateProductsList(activity, new Utilities.Callback<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        activity.runOnUiThread(() -> {
                            callback.onResult(true);
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        activity.runOnUiThread(() -> {
                            callback.onError(e);
                        });
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                activity.runOnUiThread(() -> {
                    callback.onError(e);
                });
            }
        });
    }

    public static void GetPriceConfig(Activity activity, @Nullable Callback<PriceConfig> callback){
        PriceConfigAPI priceConfigAPI = new PriceConfigAPI();
        priceConfigAPI.GetPriceConfig(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                Log.e("GetPriceConfig", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String responseBody) {
                activity.runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<PriceConfig>>(){}.getType();
                    List<PriceConfig> priceConfigs = gson.fromJson(responseBody, type);

                    try{
                        callback.onResult(priceConfigs.get(0));
                    }catch (Exception ignored){}
                });
            }
        });
    }
}
