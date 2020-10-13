package com.refCustomerFamily.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_home.HomeActivity;
import com.refCustomerFamily.activities_fragments.activity_login.LoginActivity;
import com.refCustomerFamily.activities_fragments.activity_splash_loading.SplashLoadingActivity;
import com.refCustomerFamily.databinding.ActivitySplashBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.tags.Tags;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Animation animation;
    private Preferences preferences;
    private int ad_id;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        getDataFromIntent();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.lanuch);
        binding.cons.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                new Handler().postDelayed(()->{
                    if (userModel!=null) {

                        Intent intent = new Intent(SplashActivity.this, SplashLoadingActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                },3000);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getData() != null) {
                List<String> segment = intent.getData().getPathSegments();

                ad_id = Integer.parseInt(segment.get(segment.size()- 1));


            }

        }
    }
}
