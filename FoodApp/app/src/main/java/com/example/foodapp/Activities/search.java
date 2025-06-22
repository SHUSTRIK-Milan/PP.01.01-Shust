package com.example.foodapp.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Objects;

public class search extends AppCompatActivity {

    EditText search;
    RecyclerView searched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        search = findViewById(R.id.search_et_search);
        search.requestFocus();

        searched = findViewById(R.id.search_rv_searched);
        searched.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Search(editable.toString());
            }
        });
    }

    void Search(String text){
        List<Product> searchedProductList = new ArrayList<>();

        if(!Objects.equals(text, "")){
            for(int i = 0; i < DataBinding.getProductsList().size(); i++){
                Product product = DataBinding.getProductsList().get(i);
                if(product.getName().toLowerCase().contains(text.toLowerCase())) searchedProductList.add(product);
            }
        }

        searched.setAdapter(Utilities.createProductsAdapter(search.this, ProductsAdapter.Template.LONG, searchedProductList));
    }

    public void btn_back(View view) {
        onBackPressed();
    }
}