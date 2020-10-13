package com.refCustomerFamily.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.Fragment_Offers;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.Fragment_Main;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.Fragment_Orders;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.Fragment_Setting;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.Fragment_Profile;
import com.refCustomerFamily.activities_fragments.activity_login.LoginActivity;
import com.refCustomerFamily.activities_fragments.activity_notification.NotificationActivity;
import com.refCustomerFamily.databinding.ActivityHomeBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Orders fragment_orders;
    private Fragment_Offers fragment_offers;
    private Fragment_Setting fragment_setting;
    private Fragment_Profile fragment_profile;
    private UserModel userModel;
    private String lang;
    private String token;
    public double user_lat=0.0,user_lng=0.0;


    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getDataFromIntent();
        initView();
        if (savedInstanceState == null) {
            displayFragmentMain();
        }

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        user_lat = intent.getDoubleExtra("lat",0.0);
        user_lng = intent.getDoubleExtra("lng",0.0);

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        displayFragmentMain();
                        break;
                    case R.id.orders:
                        displayFragmentOrder();
                        break;
                    case R.id.profile:
                        displayFragmentProfile();
                        break;
                    case R.id.setting:
                        displayFragmentSettings();
                        break;
                }

                return true;
            }
        });



        binding.flNotification.setOnClickListener(view -> {


            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

    }


    //    private void setUpBottomNavigation() {
//
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_home);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.store), R.drawable.ic_store);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.add), R.drawable.ic_add);
//        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.orders), R.drawable.ic_experience);
//        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.user);
//
//        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.color3));
//        binding.ahBottomNav.setTitleTextSizeInSp(13, 13);
//        binding.ahBottomNav.setForceTint(true);
//        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.white));
//
//        binding.ahBottomNav.addItem(item1);
//        binding.ahBottomNav.addItem(item2);
//        binding.ahBottomNav.addItem(item3);
//        binding.ahBottomNav.addItem(item4);
//        binding.ahBottomNav.addItem(item5);
//
//
//        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
//            return false;
//        });
//
//        updateBottomNavigationPosition(0);
//
//    }
//
//    public void updateBottomNavigationPosition(int pos) {
//
//        binding.ahBottomNav.setCurrentItem(pos, false);
//
//    }


    public void displayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }


            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }

            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }

            //  binding.setTitle(getString(R.string.home));
        } catch (Exception e) {
        }

    }

    public void displayFragmentOrder() {
        try {
            if (fragment_orders == null) {
                fragment_orders = Fragment_Orders.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_orders).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentSettings() {
        try {
            if (fragment_setting == null) {
                fragment_setting = Fragment_Setting.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_setting).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_setting, "fragment_setting").addToBackStack("fragment_setting").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentProfile() {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }


            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }

        } catch (Exception e) {
        }

    }

    public void displayFragmentOffers() {
        try {
            if (fragment_offers == null) {
                fragment_offers = Fragment_Offers.newInstance();
            }


            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }
            if (fragment_setting != null && fragment_setting.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_setting).commit();
            }
            if (fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_offers).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_offers, "fragment_offers").addToBackStack("fragment_offers").commit();

            }

        } catch (Exception e) {
        }

    }

    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {

        back();
    }

    private void back() {
        if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {
            if (userModel!=null){
                finish();
            }else {
                navigateToSignInActivity();
            }

        } else {
            displayFragmentMain();
        }
    }



}
