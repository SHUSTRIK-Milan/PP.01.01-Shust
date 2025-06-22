package com.example.foodapp.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foodapp.Adapters.ProductsAdapter;
import com.example.foodapp.CategoriesBottomSheetFragment;
import com.example.foodapp.DataBinding;
import com.example.foodapp.Models.Category;
import com.example.foodapp.Models.Favorite;
import com.example.foodapp.Models.Product;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.CategoryAPI;
import com.example.foodapp.Supabase.FavoriteAPI;
import com.example.foodapp.Supabase.ProductAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class general extends AppCompatActivity {

    ImageButton ibtn_profile;
    LinearLayout ll_categories;
    SwipeRefreshLayout sr_refresh;

    public Map<Integer, Boolean>  blockCategoriesList = new HashMap<>();

    public float productsMinPrice = 0;
    public float productsMaxPrice = 10000;

    private void init(){
        ibtn_profile = findViewById(R.id.general_ibtn_profile);
        ll_categories = findViewById(R.id.general_ll_categories);
        sr_refresh = findViewById(R.id.general_sr_refresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_general);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        sr_refresh.setOnRefreshListener(() -> {
            UpdateList();
        });

        if(DataBinding.getCategoriesList().isEmpty() || DataBinding.getProductsList().isEmpty()){
            UpdateList();
        }else{
            CreateProductsList();
        }

        Utilities.getAvatar(getApplicationContext(), ibtn_profile);
    }

    void UpdateList(){
        Utilities.UpdateCategoriesAndProducts(this, new Utilities.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                sr_refresh.setRefreshing(false);
                CreateProductsList();
            }

            @Override
            public void onError(Exception e) {}
        });
    }

    public void CreateProductsList(){
        ll_categories.removeAllViews();
        List<Category> categoryList = DataBinding.getCategoriesList();

        for(int i = 0; i < categoryList.size(); i++){
            Category category = categoryList.get(i);

            if(Boolean.TRUE.equals(blockCategoriesList.get(category.getId()))) continue;

            TextView textView = new TextView(getApplicationContext());
            textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
            textView.setText(category.getName());
            textView.setTextSize(24);
            textView.setTypeface(null, Typeface.BOLD);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(textParams);

            RecyclerView recyclerView = new RecyclerView(getApplicationContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            LinearLayout.LayoutParams recyclerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            recyclerView.setLayoutParams(recyclerParams);

            ll_categories.addView(textView);
            ll_categories.addView(recyclerView);

            List<Product> productsList = DataBinding.getProductsList();
            List<Product> sortedProductsList = new ArrayList<>();

            for(int j = 0; j < productsList.size(); j++) {
                Product product = productsList.get(j);

                if(product.getCategories().getId() == category.getId() && product.getPrice() >= productsMinPrice && product.getPrice() <= productsMaxPrice) sortedProductsList.add(product);
            }

            recyclerView.setAdapter(Utilities.createProductsAdapter(general.this, ProductsAdapter.Template.SHORT, sortedProductsList));
        }
    }

    /*public void CreateRecyclerView(){
        CategoryAPI categoryAPI = new CategoryAPI();
        categoryAPI.GetAllCategories(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("GetAllCategories", e.getLocalizedMessage());
                });
            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Category>>(){}.getType();
                    categoryList = gson.fromJson(responseBody, type);

                    categoryList.sort(Comparator.comparingInt(Category::getOrder));

                    ll_categories.removeAllViews();
                    for(int i = 0; i < categoryList.size(); i++){
                        Category category = categoryList.get(i);

                        if(Boolean.TRUE.equals(blockCategoriesList.get(category.getId()))) continue;

                        TextView textView = new TextView(getApplicationContext());
                        textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                        textView.setText(category.getName());
                        textView.setTextSize(24);
                        textView.setTypeface(null, Typeface.BOLD);

                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        textView.setLayoutParams(textParams);

                        RecyclerView recyclerView = new RecyclerView(getApplicationContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        LinearLayout.LayoutParams recyclerParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        recyclerView.setLayoutParams(recyclerParams);

                        ll_categories.addView(textView);
                        ll_categories.addView(recyclerView);

                        ProductAPI productAPI = new ProductAPI();
                        productAPI.GetProductsByCategoryWithPriceFilter(new SupabaseClient.SBC_Callback() {
                            @Override
                            public void onFailure(IOException e) {
                                runOnUiThread(() -> {
                                    Log.e("GetProductsByCategory", e.getLocalizedMessage());
                                });
                            }

                            @Override
                            public void onResponse(String responseBody) {
                                runOnUiThread(() -> {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<Product>>(){}.getType();
                                    List<Product> productList = gson.fromJson(responseBody, type);

                                    recyclerView.setAdapter(Utilities.createProductsAdapter(general.this, ProductsAdapter.Template.SHORT, productList));
                                });
                            }
                        }, category.getId(), productsMinPrice, productsMaxPrice);
                    }
                });
            }
        });
    }*/

    public void general_ibtn_profile(View view) {
        Intent profile = new Intent(getApplicationContext(), profilePage.class);
        startActivity(profile);
    }

    public void general_btn_search(View view) {
        Intent search = new Intent(getApplicationContext(), search.class);
        startActivity(search);
    }

    boolean categories_open = false;
    CategoriesBottomSheetFragment categories = new CategoriesBottomSheetFragment();
    public void general_btn_categories(View view) {
        if(categories_open){
            categories.dismiss();
        }else{
            categories.show(getSupportFragmentManager(), "categories_sheet");
        }
        categories_open = !categories_open;
    }
}