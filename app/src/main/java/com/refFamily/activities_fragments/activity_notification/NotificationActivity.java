package com.refFamily.activities_fragments.activity_notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.refFamily.R;
import com.refFamily.adapters.NotificationAdapter;
import com.refFamily.databinding.ActivityNotificationBinding;
import com.refFamily.language.Language_Helper;

import io.paperdb.Paper;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);


        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);

        binding.recViewNotifications.setAdapter(new NotificationAdapter(this));
        binding.recViewNotifications.setLayoutManager(new LinearLayoutManager(this));


        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void back() {
        finish();
    }
}