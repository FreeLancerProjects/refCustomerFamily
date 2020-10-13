package com.refCustomerFamily.activities_fragments.activity_subscription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.refCustomerFamily.R;
import com.refCustomerFamily.adapters.SubscriptionAdapter;
import com.refCustomerFamily.databinding.ActivitySubscriptionBinding;
import com.refCustomerFamily.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class SubscriptionActivity extends AppCompatActivity {


    private ActivitySubscriptionBinding binding;
    private String lang;
    private SubscriptionAdapter adapter;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        adapter = new SubscriptionAdapter(this);
        binding.recViewSub.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewSub.setAdapter(adapter);

        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void back() {
        finish();
    }
}