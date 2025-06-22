package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.Product;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.CartProductsAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;

public class productPage extends AppCompatActivity {

    ImageView iv_img;
    TextView tv_title, tv_desc, tv_price;
    Button btn_cart;
    Product product;

    void init(){
        iv_img = findViewById(R.id.product_iv_img);
        tv_title = findViewById(R.id.product_tv_title);
        tv_desc = findViewById(R.id.product_tv_desc);
        tv_price = findViewById(R.id.product_tv_price);
        btn_cart = findViewById(R.id.product_btn_cart);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intent = getIntent();
        int product_id = intent.getIntExtra("product_id", 0);
        for(int i = 0; i < DataBinding.getProductsList().size(); i++){
            Product updateProduct = DataBinding.getProductsList().get(i);
            if(product_id == updateProduct.getId()) product = updateProduct;
        }

        Glide.with(this)
                .load(SupabaseClient.GetImageURL(product.getImage_url(), "images"))
                .error(R.drawable.app_logo)
                .into(iv_img);

        tv_title.setText(product.getName());
        tv_desc.setText(product.getDesc());
        tv_price.setText("$ " + String.valueOf(product.getPrice()));

        if(product.inCart()){
            btn_cart.setText("Delete from cart");
            btn_cart.setTag(true);
        }else{
            btn_cart.setText("Add to cart");
            btn_cart.setTag(false);
        }

        btn_cart.setOnClickListener((v) -> {
            if((Boolean) btn_cart.getTag()){
                CartProductsAPI cartProductsAPI = new CartProductsAPI();
                cartProductsAPI.DeleteCartProduct(new SupabaseClient.SBC_Callback() {
                    @Override
                    public void onFailure(IOException e) {}

                    @Override
                    public void onResponse(String responseBody) {
                        runOnUiThread(() -> {
                            Utilities.UpdateProductsList(productPage.this, new Utilities.Callback<Boolean>() {
                                @Override
                                public void onResult(Boolean result) {
                                    runOnUiThread(() -> {
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    });
                                }

                                @Override
                                public void onError(Exception e) {}
                            });
                        });
                    }
                }, product.getCartProduct().getId());
            }else{
                CartProductsAPI cartProductsAPI = new CartProductsAPI();
                cartProductsAPI.AddCartProduct(new SupabaseClient.SBC_Callback() {
                    @Override
                    public void onFailure(IOException e) {}

                    @Override
                    public void onResponse(String responseBody) {
                        runOnUiThread(() -> {
                            Utilities.UpdateProductsList(productPage.this, new Utilities.Callback<Boolean>() {
                                @Override
                                public void onResult(Boolean result) {
                                    runOnUiThread(() -> {
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    });
                                }

                                @Override
                                public void onError(Exception e) {}
                            });
                        });
                    }
                }, product.getId());
            }
        });
    }

    public void btn_back(View view) {
        onBackPressed();
    }
}