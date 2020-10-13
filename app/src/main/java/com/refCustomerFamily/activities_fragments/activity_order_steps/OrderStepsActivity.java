package com.refCustomerFamily.activities_fragments.activity_order_steps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ActivityOrderStepsBinding;
import com.refCustomerFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class OrderStepsActivity extends AppCompatActivity {

    private ActivityOrderStepsBinding binding;
    private String lang;


    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_steps);

        initView();

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void back() {
        finish();
    }
}