package com.example.foodapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foodapp.Adapters.ProductsAdapter;
import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.PriceConfig;
import com.example.foodapp.Models.Product;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.CartProductsAPI;
import com.example.foodapp.Supabase.PriceConfigAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class cartPage extends AppCompatActivity {

    RecyclerView rv_cart;
    SwipeRefreshLayout sr_refresh;
    TextView tv_tax, tv_delivery, tv_products, tv_total;
    Button btn_order;

    List<Product> cartList;

    void init(){
        rv_cart = findViewById(R.id.cart_rv_cart);
        sr_refresh = findViewById(R.id.cart_sr_refresh);
        tv_tax = findViewById(R.id.cart_tv_tax);
        tv_delivery = findViewById(R.id.cart_tv_delivery);
        tv_products = findViewById(R.id.cart_tv_products);
        tv_total = findViewById(R.id.cart_tv_total);
        btn_order = findViewById(R.id.cart_btn_order);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btn_order.setOnClickListener((v) -> {
            if(cartList.isEmpty()){
                Toast.makeText(getApplicationContext(), "No products in cart", Toast.LENGTH_SHORT).show();
                return;
            }

            CartProductsAPI cartProductsAPI = new CartProductsAPI();
            cartProductsAPI.DeleteCartProductByCartID(new SupabaseClient.SBC_Callback() {
                @Override
                public void onFailure(IOException e) {}

                @Override
                public void onResponse(String responseBody) {
                    runOnUiThread(() -> {
                        Utilities.UpdateCategoriesAndProducts(cartPage.this, new Utilities.Callback<Boolean>() {
                            @Override
                            public void onResult(Boolean result) {
                                Random random = new Random();
                                int number = random.nextInt(100);
                                Toast.makeText(getApplicationContext(), "Заказ #"+number+" сделан", Toast.LENGTH_SHORT).show();

                                CreateCartList();
                            }

                            @Override
                            public void onError(Exception e) {}
                        });
                    });
                }
            });
        });

        sr_refresh.setOnRefreshListener(() -> {
            CreateCartList();

            new Handler().postDelayed(() -> {
                sr_refresh.setRefreshing(false);
            }, 500);
        });

        CreateCartList();
    }

    void CreateCartList(){
        Utilities.authProfile(this, new Utilities.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                Utilities.GetPriceConfig(cartPage.this, new Utilities.Callback<PriceConfig>() {
                    @Override
                    public void onResult(PriceConfig priceConfig) {
                        float taxPercent = priceConfig.getTax();
                        float delivery = priceConfig.getDelivery_price();
                        float total = DataBinding.getProfile().getCart().getTotalPrice();

                        tv_products.setText("$" + String.valueOf(total));

                        float tax = total * (taxPercent/100);
                        if(cartList.isEmpty()) delivery = 0;
                        total = total + delivery + tax;

                        tv_tax.setText("$" + String.format("%.2f", tax) + " (" + taxPercent + "%)");
                        tv_delivery.setText("$" + String.valueOf(delivery));
                        tv_total.setText("$" + String.valueOf(total));
                    }

                    @Override
                    public void onError(Exception e) {}
                });
            }

            @Override
            public void onError(Exception e) {}
        });

        cartList = new ArrayList<>();
        for(int i = 0; i < DataBinding.getProductsList().size(); i++){
            Product product = DataBinding.getProductsList().get(i);
            if(product.inCart()) cartList.add(product);
        }

        rv_cart.setAdapter(Utilities.createProductsAdapter(cartPage.this, ProductsAdapter.Template.CART, cartList));
        rv_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }
}