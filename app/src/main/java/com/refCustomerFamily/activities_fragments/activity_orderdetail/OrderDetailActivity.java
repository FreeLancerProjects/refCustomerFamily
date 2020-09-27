package com.refCustomerFamily.activities_fragments.activity_orderdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_chat.ChatActivity;
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.adapters.OrderDetailAdapter;
import com.refCustomerFamily.databinding.ActivityOrderDetailBinding;
import com.refCustomerFamily.language.Language_Helper;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        adapter = new OrderDetailAdapter(this);
        binding.recViewOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOrderDetail.setAdapter(adapter);


        binding.back.setOnClickListener(view -> {

            back();
        });
        binding.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderDetailActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });


        binding.acceptBtn.setOnClickListener(view -> {

            Intent intent = new Intent(OrderDetailActivity.this, OrderStepsActivity.class);
            startActivity(intent);

        });

    }

    private void back() {
        finish();
    }


}