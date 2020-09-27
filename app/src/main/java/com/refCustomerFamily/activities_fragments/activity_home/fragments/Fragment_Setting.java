package com.refCustomerFamily.activities_fragments.activity_home.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_about_app.AboutAppActivity;
import com.refCustomerFamily.activities_fragments.activity_home.HomeActivity;
import com.refCustomerFamily.activities_fragments.activity_language.LanguageActivity;
import com.refCustomerFamily.activities_fragments.activity_login.LoginActivity;
import com.refCustomerFamily.activities_fragments.activity_profile.UpdateProfileActivity;
import com.refCustomerFamily.activities_fragments.activity_subscription.SubscriptionActivity;
import com.refCustomerFamily.databinding.FragmentSettingBinding;
import com.refCustomerFamily.interfaces.Listeners;
import com.refCustomerFamily.models.DefaultSettings;
import com.refCustomerFamily.models.MarketCatogryModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.activities_fragments.activity_profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

public class Fragment_Setting extends Fragment implements Listeners.SettingAction {

    private HomeActivity activity;
    private FragmentSettingBinding binding;
    private List<MarketCatogryModel.Data> dataList;
    private String lang;
    private Preferences preferences;
    private DefaultSettings defaultSettings;


    public static Fragment_Setting newInstance() {
        return new Fragment_Setting();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        initView();


        return binding.getRoot();
    }

    private void initView() {
        dataList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        defaultSettings = preferences.getAppSetting(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setActions(this);


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSubscriptions() {
        Intent intent = new Intent(activity, SubscriptionActivity.class);
        intent.putExtra("data",preferences.getUserData(activity));
        startActivity(intent);
    }

    @Override
    public void onProfile() {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra("data",preferences.getUserData(activity));
        startActivity(intent);
    }

    @Override
    public void onEditProfile() {
        Intent intent = new Intent(activity, UpdateProfileActivity.class);
        intent.putExtra("data",preferences.getUserData(activity));
        startActivityForResult(intent,2);
    }

    @Override
    public void onLanguageSetting() {
        Intent intent = new Intent(activity, LanguageActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    @Override
    public void onTerms() {
        Intent intent=new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }

    @Override
    public void onPrivacy() {
        Intent intent=new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type",3);
        startActivity(intent);
    }

    @Override
    public void onRate() {
        String appId = activity.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        final List<ResolveInfo> otherApps = activity.getPackageManager()
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
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
    }

    @Override
    public void logout() {
        if(preferences.getUserData(activity)!=null){
            Intent intent=new Intent(activity, LoginActivity.class);
            activity.finish();
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(activity, LoginActivity.class);
            activity.finish();
            startActivity(intent);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);


            if (uri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(activity,uri);
                String name = ringtone.getTitle(activity);
                binding.tvRingtoneName.setText(name);

                if (defaultSettings==null){
                    defaultSettings = new DefaultSettings();
                }

                defaultSettings.setRingToneUri(uri.toString());
                defaultSettings.setRingToneName(name);
                preferences.createUpdateAppSetting(activity,defaultSettings);


            }
        } else if (requestCode == 200 && resultCode == RESULT_OK ) {

            activity.setResult(RESULT_OK);
            activity.finish();
        }
    }
}
