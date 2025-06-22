package com.example.foodapp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodapp.Activities.general;
import com.example.foodapp.Models.Category;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class CategoriesBottomSheetFragment extends BottomSheetDialogFragment {
    general general;
    Map<Integer, Integer> checkboxsIDs = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sheet_categories, container, false);
        general = (general) requireActivity();

        LinearLayout ll_filter = view.findViewById(R.id.filter_ll_filters);
        EditText et_minprice = view.findViewById(R.id.filter_et_minprice);
        EditText et_maxprice = view.findViewById(R.id.filter_et_maxprice);

        for(int i = 0; i < DataBinding.getCategoriesList().size(); i++) {
            Category category = DataBinding.getCategoriesList().get(i);

            ll_filter.addView(createCheckBox(view.getContext(), category));
        }

        et_minprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                float price = 0;
                try{
                    price = Float.parseFloat(editable.toString());
                }catch (Exception ignored){}

                general.productsMinPrice = price;
                general.CreateProductsList();
            }
        });

        et_maxprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                float price = 10000;
                try{
                    price = Float.parseFloat(editable.toString());
                }catch (Exception ignored){}

                general.productsMaxPrice = price;
                general.CreateProductsList();
            }
        });

        return view;
    }

    CheckBox createCheckBox(Context context, Category category){
        CheckBox checkbox = new CheckBox(context);

        Integer id = checkboxsIDs.get(category.getId());
        if(id == null){
            id = View.generateViewId();
            checkboxsIDs.put(category.getId(), id);
        }

        checkbox.setText(category.getName());
        checkbox.setChecked(true);
        checkbox.setId(id);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                general.blockCategoriesList.put(category.getId(), !b);
                general.CreateProductsList();
            }
        });
        return checkbox;
    }
}
