package com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_google_stores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_shop_map.ShopMapActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_shop_query.ShopsQueryActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_shops.ShopsActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters.MainAdapter;
import com.refCustomerFamily.databinding.ActivityGoogleStoresBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.CategoryModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.MainItemData;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.NearbyModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class GoogleStoresActivity extends AppCompatActivity {
    ActivityGoogleStoresBinding binding;
    private double user_lat = 0.0, user_lng = 0.0;
    private String lang;
    private List<MainItemData> mainItemDataList;
    private MainAdapter mainAdapter;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_google_stores);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent  = getIntent();
        user_lat = intent.getDoubleExtra("lat",0.0);
        user_lng = intent.getDoubleExtra("lng",0.0);

    }

    private void initView() {
        mainItemDataList = new ArrayList<>();
        preferences= Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this));
        MainItemData itemData1 = new MainItemData(0);
        mainItemDataList.add(itemData1);
        String currency=getString(R.string.sar);

        mainAdapter = new MainAdapter(mainItemDataList,this,user_lat,user_lng,currency);
        binding.recViewCategory.setAdapter(mainAdapter);

        MainItemData itemData2 = new MainItemData(1);
        mainItemDataList.add(itemData2);
        mainAdapter.notifyItemInserted(mainItemDataList.size()-1);

        binding.consSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShopsActivity.class);
            intent.putExtra("lat", user_lat);
            intent.putExtra("lng", user_lng);
            intent.putExtra("type", true);
            startActivity(intent);
        });
    }

    public void placeItemData(NearbyModel.Result placeModel) {

        Intent intent  = new Intent(this, ShopMapActivity.class);
        intent.putExtra("data",placeModel);
        startActivity(intent);
    }

    public void setCategoryData(CategoryModel categoryModel) {
        Intent intent = new Intent(this, ShopsQueryActivity.class);
        intent.putExtra("lat",user_lat);
        intent.putExtra("lng",user_lng);
        intent.putExtra("data",categoryModel);
        startActivity(intent);
    }
}