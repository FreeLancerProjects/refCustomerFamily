package com.refCustomerFamily.activities_fragments.activity_setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_about_app.AboutAppActivity;
import com.refCustomerFamily.activities_fragments.activity_language.LanguageActivity;
import com.refCustomerFamily.activities_fragments.activity_login.LoginActivity;
import com.refCustomerFamily.activities_fragments.activity_sign_up.SignUpActivity;
import com.refCustomerFamily.databinding.ActivitySettingsBinding;
import com.refCustomerFamily.interfaces.Listeners;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.DefaultSettings;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.BuildConfig;
import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity implements Listeners.SettingAction {
    private ActivitySettingsBinding binding;
    private String lang;
    private Preferences preferences;
    private DefaultSettings defaultSettings;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initView();
    }

    private void initView() {
        preferences = Preferences.newInstance();
        defaultSettings = preferences.getAppSetting(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setActions(this);
        binding.close.setOnClickListener(v -> finish());
        binding.tvVersion.setText(BuildConfig.VERSION_NAME);


        if (defaultSettings!=null){
            if (defaultSettings.getRingToneName()!=null&&!defaultSettings.getRingToneName().isEmpty()){
                binding.tvRingtoneName.setText(defaultSettings.getRingToneName());
            }else {
                binding.tvRingtoneName.setText(getString(R.string.default1));
            }
        }else {
            binding.tvRingtoneName.setText(getString(R.string.default1));

        }
    }


    @Override
    public void onSubscriptions() {

    }

    @Override
    public void onProfile() {

    }

    @Override
    public void onEditProfile() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("data",preferences.getUserData(this));
        startActivityForResult(intent,2);
    }

    @Override
    public void onLanguageSetting() {
        Intent intent = new Intent(this, LanguageActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    @Override
    public void onTerms() {
        Intent intent=new Intent(SettingsActivity.this, AboutAppActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    @Override
    public void onPrivacy() {
        Intent intent=new Intent(SettingsActivity.this, AboutAppActivity.class);
        intent.putExtra("type",3);
        startActivity(intent);
    }

    @Override
    public void onRate() {
        String appId = getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        final List<ResolveInfo> otherApps = getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            startActivity(webIntent);
        }
    }

    @Override
    public void onTone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 100);
    }

    @Override
    public void about() {
        Intent intent = new Intent(SettingsActivity.this, AboutAppActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @Override
    public void logout() {
        if(preferences.getUserData(this)!=null){
            Intent intent=new Intent(SettingsActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(SettingsActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void share() {

    }

    @Override
    public void contactUs() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);


            if (uri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(this,uri);
                String name = ringtone.getTitle(this);
                binding.tvRingtoneName.setText(name);

                if (defaultSettings==null){
                    defaultSettings = new DefaultSettings();
                }

                defaultSettings.setRingToneUri(uri.toString());
                defaultSettings.setRingToneName(name);
                preferences.createUpdateAppSetting(this,defaultSettings);


            }
        } else if (requestCode == 200 && resultCode == RESULT_OK ) {

            setResult(RESULT_OK);
            finish();
        }
    }
}