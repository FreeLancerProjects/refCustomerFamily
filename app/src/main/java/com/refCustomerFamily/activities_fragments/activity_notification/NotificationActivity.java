package com.refCustomerFamily.activities_fragments.activity_notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.refCustomerFamily.R;
import com.refCustomerFamily.adapters.NotificationAdapter;
import com.refCustomerFamily.databinding.ActivityNotificationBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.NotificationModel;
import com.refCustomerFamily.models.OfferModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private String lang;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel.Data> dataList;
    private UserModel userModel;
    private Preferences preferences;
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
        dataList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        notificationAdapter = new NotificationAdapter(dataList,this);
        binding.recViewNotifications.setAdapter(notificationAdapter);
        binding.recViewNotifications.setLayoutManager(new LinearLayoutManager(this));

        getNotification();
        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void getNotification() {

        binding.progBarNotification.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getNotification("Bearer "+userModel.getUser().getToken(),userModel.getUser().getId()).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.isSuccessful() && response.body() != null){
                    binding.progBarNotification.setVisibility(View.GONE);
                    dataList.addAll(response.body().getData());
                    notificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                binding.progBarNotification.setVisibility(View.GONE);
                Log.e("Notification",t.getMessage());
            }
        });


    }

    private void back() {
        finish();
    }
}