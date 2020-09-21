package com.refFamily.activities_fragments.activity_orderdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.refFamily.R;
import com.refFamily.adapters.OrderDetailAdapter;
import com.refFamily.databinding.ActivityOrderDetailBinding;
import com.refFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private String lang;
    private OrderDetailAdapter adapter;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_detail);

        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        adapter = new OrderDetailAdapter(this);
        binding.recViewOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOrderDetail.setAdapter(adapter );


        binding.back.setOnClickListener(view -> {

            back();
        });

    }

    private void back() {
        finish();
    }


}