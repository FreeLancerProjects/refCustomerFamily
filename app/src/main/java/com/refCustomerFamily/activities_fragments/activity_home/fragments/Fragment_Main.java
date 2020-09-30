package com.refCustomerFamily.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.refCustomerFamily.activities_fragments.activity_home.HomeActivity;
import com.refCustomerFamily.activities_fragments.activity_product_family.ProductFamilyActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_google_stores.GoogleStoresActivity;
import com.refCustomerFamily.databinding.FragmentMainBinding;
import com.refCustomerFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Main extends Fragment {

    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;
    private String lang;


    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();

        return binding.getRoot();

    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.layout1.setOnClickListener(view -> {

            Intent intent = new Intent(activity, ProductFamilyActivity.class);
            startActivity(intent);

        });

        binding.layout2.setOnClickListener(view -> {

            Intent intent = new Intent(activity, GoogleStoresActivity.class);
            intent.putExtra("lat",activity.user_lat);
            intent.putExtra("lng",activity.user_lng);

            startActivity(intent);

        });

    }


    private void NavigateToAddOfferActivity() {

        Intent intent = new Intent(this.getContext(), AddProductActivity.class);
        startActivity(intent);

    }

}
