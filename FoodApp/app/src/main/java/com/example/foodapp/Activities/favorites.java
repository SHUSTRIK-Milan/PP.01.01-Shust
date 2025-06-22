package com.example.foodapp.Activities;

import android.os.Bundle;
import android.os.Handler;

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
import com.example.foodapp.Models.Product;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.ProductAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class favorites extends AppCompatActivity {


    RecyclerView rv_favorites;
    SwipeRefreshLayout sr_refresh;

    void init(){
        rv_favorites = findViewById(R.id.favorites_rv_favorites);
        sr_refresh = findViewById(R.id.favorites_sr_refresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        sr_refresh.setOnRefreshListener(() -> {
            CreateFavoriteList();

            new Handler().postDelayed(() -> {
                sr_refresh.setRefreshing(false);
            }, 500);
        });

        CreateFavoriteList();
    }

    void CreateFavoriteList(){
        List<Product> favoriteList = new ArrayList<>();
        for(int i = 0; i < DataBinding.getProductsList().size(); i++){
            Product product = DataBinding.getProductsList().get(i);
            if(product.isFavorite()) favoriteList.add(product);
        }

        rv_favorites.setAdapter(Utilities.createProductsAdapter(favorites.this, ProductsAdapter.Template.LONG, favoriteList));
        rv_favorites.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }
}