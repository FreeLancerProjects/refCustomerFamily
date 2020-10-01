package com.refCustomerFamily.activities_fragments.activity_cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.refCustomerFamily.R;
import com.refCustomerFamily.adapters.MealAdapter;
import com.refCustomerFamily.databinding.ActivityCartBinding;
import com.refCustomerFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {


    private ActivityCartBinding binding;
    private String lang;
    private MealAdapter adapter;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);

        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        adapter = new MealAdapter(this);
        binding.recViewMeal.setAdapter(adapter);
        binding.recViewMeal.setLayoutManager(new LinearLayoutManager(this));

        binding.back.setOnClickListener(view -> {
            back();
        });

        binding.nextBtn.setOnClickListener(view -> {



        });

    }

    private void back() {
        finish();
    }


}