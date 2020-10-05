package com.refCustomerFamily.activities_fragments.activity_family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_cart.CartActivity;
import com.refCustomerFamily.adapters.CategoryAdapter;
import com.refCustomerFamily.adapters.FamilyProductAdapter;
import com.refCustomerFamily.databinding.ActivityFamilyBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.FamilyCategory;
import com.refCustomerFamily.models.FamilyCategoryProductDataModel;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.models.ProductModel;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyActivity extends AppCompatActivity {

    private static final String TAG = "FamilyActivity";
    private ActivityFamilyBinding binding;
    private String lang;
    private CategoryAdapter categoryAdapter;
    private FamilyProductAdapter familyAdapter;
    private FamilyModel familyModel;
    private List<FamilyCategory> familyCategoryList;
    private List<ProductModel> productModelList;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            familyModel = (FamilyModel) intent.getSerializableExtra("data");
        }
    }


    private void initView() {
        productModelList = new ArrayList<>();
        familyCategoryList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(familyModel);
        categoryAdapter = new CategoryAdapter(familyCategoryList, this);
        binding.recViewCategory.setAdapter(categoryAdapter);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        familyAdapter = new FamilyProductAdapter(productModelList,this);
        binding.recViewFamily.setAdapter(familyAdapter);
        binding.recViewFamily.setLayoutManager(new LinearLayoutManager(this));


        binding.addToCart.setOnClickListener(view -> {

            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);

        });

        binding.back.setOnClickListener(view -> {
            back();
        });

        getFamilyCategories();

    }

    private void getFamilyCategories() {
        Api.getService(Tags.base_url).getFamilyCategory_Products(familyModel.getId()).enqueue(new Callback<FamilyCategoryProductDataModel>() {
            @Override
            public void onResponse(Call<FamilyCategoryProductDataModel> call, Response<FamilyCategoryProductDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                   familyCategoryList.clear();
                    familyCategoryList.addAll(response.body().getData().getCategories());
                    if (familyCategoryList.size() > 0) {
                        categoryAdapter.notifyDataSetChanged();
                        binding.tvNoData.setVisibility(View.GONE);
                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.progBar.setVisibility(View.GONE);

                    switch (response.code()) {
                        case 500:
                            Toast.makeText(FamilyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(FamilyActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<FamilyCategoryProductDataModel> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);

                    Log.e(TAG, t.getMessage() + "__");
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(FamilyActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            Toast.makeText(FamilyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {

                }
            }
        });

    }


    private void back() {
        finish();
    }


    public void showFamilyProducts(FamilyCategory familyCategory) {
        productModelList.clear();
        productModelList.addAll(familyCategory.getProducts());
        if (familyCategoryList.size()>0){
            familyAdapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }
}