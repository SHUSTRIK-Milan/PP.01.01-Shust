package com.example.foodapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Models.Product;
import com.example.foodapp.R;
import com.example.foodapp.Supabase.CartProductsAPI;
import com.example.foodapp.Supabase.ProductAPI;
import com.example.foodapp.Supabase.SupabaseClient;
import com.example.foodapp.Utilities;

import java.io.IOException;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    public enum Template{
        SHORT,
        LONG,
        CART
    }

    private Activity activity;
    private Context context;
    private Template template;
    private List<Product> productList;
    private OnItemClickListener itemClick, favoriteClick;

    private Integer requestQueue = 0;

    public ProductsAdapter(Activity activity, Template template, List<Product> productList, OnItemClickListener itemClick, OnItemClickListener favoriteClick) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.template = template;
        this.productList = productList;
        this.itemClick = itemClick;
        this.favoriteClick = favoriteClick;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.template_product;
        if(template == Template.LONG) layout = R.layout.template_product_long;
        if(template == Template.CART) layout = R.layout.template_product_cart;

        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        Glide.with(context)
                .load(SupabaseClient.GetImageURL(product.getImage_url(), "images"))
                .error(R.drawable.error_vpn)
                .into(holder.img);
        holder.title.setText(product.getName());
        holder.price.setText("$"+String.valueOf(product.getPrice()));

        if(holder.desc != null) holder.desc.setText(product.getDesc());
        if(template == Template.CART){
            int count = product.getCartProduct().getCount();
            holder.count.setText(String.valueOf(count));

            holder.minus.setOnClickListener((v) -> {
                int new_count = product.getCartProduct().getCount();

                if(new_count == 1){
                    deleteCartProduct(product);
                    return;
                }
                new_count -= 1;

                holder.count.setText(String.valueOf(new_count));

                changeCartProductCount(product, new_count);
            });

            holder.plus.setOnClickListener((v) -> {
                int new_count = product.getCartProduct().getCount();

                if(new_count == 99) return; //delete from cart
                new_count += 1;

                holder.count.setText(String.valueOf(new_count));

                changeCartProductCount(product, new_count);
            });

            holder.delete.setOnClickListener((v) -> {
                deleteCartProduct(product);
            });
        }

        Utilities.markFavorite(holder.favorite, product.isFavorite());

        holder.itemView.setOnClickListener(v -> {
            itemClick.onItemClick(position, v);
        });

        holder.favorite.setOnClickListener(v -> {
            favoriteClick.onItemClick(position, v);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, price, count;
        ImageView img;
        ImageButton favorite;
        Button minus, plus, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_template_title);
            desc = itemView.findViewById(R.id.item_template_desc);
            price = itemView.findViewById(R.id.item_template_price);
            img = itemView.findViewById(R.id.item_template_img);
            favorite = itemView.findViewById(R.id.item_template_favorite);
            count = itemView.findViewById(R.id.item_template_count);
            minus = itemView.findViewById(R.id.item_template_btn_minus);
            plus = itemView.findViewById(R.id.item_template_btn_plus);
            delete = itemView.findViewById(R.id.item_template_btn_del);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    void deleteCartProduct(Product product){
        CartProductsAPI cartProductsAPI = new CartProductsAPI();
        cartProductsAPI.DeleteCartProduct(new SupabaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {}

            @Override
            public void onResponse(String responseBody) {
                activity.runOnUiThread(() -> {
                    Utilities.UpdateProductsList(activity, null);
                });
            }
        }, product.getCartProduct().getId());
    }

    void changeCartProductCount(Product product, int new_count){
        product.getCartProduct().setCount(new_count);

        requestQueue++;
        new Handler().postDelayed(() -> {
            CartProductsAPI cartProductsAPI = new CartProductsAPI();
            cartProductsAPI.ChangeCount(new SupabaseClient.SBC_Callback() {
                @Override
                public void onFailure(IOException e) {}

                @Override
                public void onResponse(String responseBody) {
                    requestQueue--;
                }
            }, product.getCartProduct().getId(), new_count);
        }, requestQueue * 200);
    }
}
