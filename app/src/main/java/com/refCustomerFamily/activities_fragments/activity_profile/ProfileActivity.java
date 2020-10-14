package com.refCustomerFamily.activities_fragments.activity_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_sign_up.FragmentMapTouchListener;
import com.refCustomerFamily.databinding.ActivityProfileBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "DATA";
    private ActivityProfileBinding binding;
    private String lang;
    FragmentMapTouchListener fragmentMapTouchListener;
    private UserModel userModel;
    private Preferences preferences;
    private ImageView imclose;
    private double lat = 0.0, lng = 0.0;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        initView();

    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        userModel = (UserModel) getIntent().getSerializableExtra(TAG);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.setModel(userModel);
        binding.updateBtn.setOnClickListener(view -> {

            Intent intent = new Intent(this, UpdateProfileActivity.class);
            intent.putExtra(TAG, userModel);
            startActivity(intent);


        });


        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void back() {
        finish();
    }



}