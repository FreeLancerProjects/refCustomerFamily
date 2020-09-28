package com.refCustomerFamily.activities_fragments.activity_family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_cart.CartActivity;
import com.refCustomerFamily.adapters.CategoryAdapter;
import com.refCustomerFamily.adapters.FamilyOrderAdapter;
import com.refCustomerFamily.adapters.ProductFamilyAdapter;
import com.refCustomerFamily.adapters.ProductFamilyCategoryAdapter;
import com.refCustomerFamily.databinding.ActivityFamilyBinding;
import com.refCustomerFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class FamilyActivity extends AppCompatActivity {

    private ActivityFamilyBinding binding;
    private String lang;
    private CategoryAdapter categoryAdapter;
    private FamilyOrderAdapter familyAdapter;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family);

        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        categoryAdapter = new CategoryAdapter(this);
        binding.recViewCategory.setAdapter(categoryAdapter);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        familyAdapter = new FamilyOrderAdapter(this);
        binding.recViewFamily.setAdapter(familyAdapter);
        binding.recViewFamily.setLayoutManager(new LinearLayoutManager(this));


        binding.addToCart.setOnClickListener(view -> {

            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);

        });

        binding.back.setOnClickListener(view -> {
            back();
        });



    }

    private void back() {
        finish();
    }
}