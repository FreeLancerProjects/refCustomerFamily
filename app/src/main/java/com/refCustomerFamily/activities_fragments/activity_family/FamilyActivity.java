package com.refCustomerFamily.activities_fragments.activity_family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_cart.CartActivity;
import com.refCustomerFamily.adapters.CategoryAdapter;
import com.refCustomerFamily.adapters.FamilyOrderAdapter;
import com.refCustomerFamily.databinding.ActivityFamilyBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyActivity extends AppCompatActivity {

    private static final String TAG = "FamilyActivity";
    private ActivityFamilyBinding binding;
    private String lang;
    private CategoryAdapter categoryAdapter;
    private FamilyOrderAdapter familyAdapter;
    private int familyId;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family);

        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        familyId = getIntent().getIntExtra("familyId", 0);

        categoryAdapter = new CategoryAdapter(this);
        binding.recViewCategory.setAdapter(categoryAdapter);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        familyAdapter = new FamilyOrderAdapter(this);
        binding.recViewFamily.setAdapter(familyAdapter);
        binding.recViewFamily.setLayoutManager(new LinearLayoutManager(this));


        binding.addToCart.setOnClickListener(view -> {

            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);

        });

        binding.back.setOnClickListener(view -> {
            back();
        });


        getFamilyByFamilyId();

    }

    private void getFamilyByFamilyId() {

        Api.getService(Tags.base_url).getFamilyByFamilyId(familyId).enqueue(new Callback<FamilyModel>() {
            @Override
            public void onResponse(Call<FamilyModel> call, Response<FamilyModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.setModel(response.body());
                }
            }

            @Override
            public void onFailure(Call<FamilyModel> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });


    }

    private void back() {
        finish();
    }


}