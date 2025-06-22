package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.foodapp.Activities.cartPage;
import com.example.foodapp.Activities.favorites;
import com.example.foodapp.Activities.profilePage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomMenuFragment extends Fragment {
    public BottomMenuFragment() {
        // Required empty public constructor
    }

    ImageButton general, profile, basket, favorite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        general = view.findViewById(R.id.bottom_btn_general);
        profile = view.findViewById(R.id.bottom_btn_profilePage);
        basket = view.findViewById(R.id.bottom_btn_basket);
        favorite = view.findViewById(R.id.bottom_btn_favorite);

        general.setOnClickListener(view1 -> {
            changeActivity(com.example.foodapp.Activities.general.class, (ImageView) view1);
        });
        profile.setOnClickListener(view1 -> {
            changeActivity(profilePage.class, (ImageView) view1);
        });
        basket.setOnClickListener(view1 -> {
            changeActivity(cartPage.class, (ImageView) view1);
        });
        favorite.setOnClickListener(view1 -> {
            changeActivity(favorites.class, (ImageView) view1);
        });

    }

    <T> void changeActivity(Class<T> activity_class, ImageView view){
        Intent intent = new Intent(getContext(), activity_class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }
}