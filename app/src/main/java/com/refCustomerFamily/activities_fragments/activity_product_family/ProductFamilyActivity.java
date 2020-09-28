package com.refCustomerFamily.activities_fragments.activity_product_family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.refCustomerFamily.adapters.CategoryAdapter;
import com.refCustomerFamily.adapters.ProductFamilyAdapter;
import com.refCustomerFamily.adapters.ProductFamilyCategoryAdapter;
import com.refCustomerFamily.databinding.ActivityProductFamilyBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.AddProductModel;

import java.util.ArrayList;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductFamilyActivity extends AppCompatActivity {

    private ActivityProductFamilyBinding binding;
    private String lang;
    private ProductFamilyCategoryAdapter productFamilyCategoryAdapter;
    private ProductFamilyAdapter familyAdapter;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_family);

        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        productFamilyCategoryAdapter = new ProductFamilyCategoryAdapter(this);
        binding.recViewCategory.setAdapter(productFamilyCategoryAdapter);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        familyAdapter = new ProductFamilyAdapter(this);
        binding.recViewFamily.setAdapter(familyAdapter);
        binding.recViewFamily.setLayoutManager(new LinearLayoutManager(this));


        binding.back.setOnClickListener(view -> {

            back();
        });




    }

    private void back() {
        finish();
    }

}