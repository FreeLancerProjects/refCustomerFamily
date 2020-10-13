package com.refCustomerFamily.activities_fragments.activity_product_family;

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
import com.refCustomerFamily.activities_fragments.activity_family.FamilyActivity;
import com.refCustomerFamily.activities_fragments.activity_login.LoginActivity;
import com.refCustomerFamily.adapters.FamilyAdapter;
import com.refCustomerFamily.adapters.ProductFamilyCategoryAdapter;
import com.refCustomerFamily.databinding.ActivityProductFamilyBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.models.SubCategoryFamilyModel;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFamilyActivity extends AppCompatActivity {


    private static final String TAG = "ProductFamilyActivity";
    private ActivityProductFamilyBinding binding;
    private String lang;
    private ProductFamilyCategoryAdapter productFamilyCategoryAdapter;
    private FamilyAdapter familyAdapter;
    private List<SubCategoryFamilyModel.Data.Category> categoryList;
    private List<FamilyModel> familyList;


    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_family);

        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        categoryList = new ArrayList<>();
        familyList = new ArrayList<>();


        productFamilyCategoryAdapter = new ProductFamilyCategoryAdapter(categoryList,this);
        binding.recViewCategory.setAdapter(productFamilyCategoryAdapter);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        familyAdapter = new FamilyAdapter(familyList,this);
        binding.recViewFamily.setAdapter(familyAdapter);
        binding.recViewFamily.setLayoutManager(new LinearLayoutManager(this));


        binding.back.setOnClickListener(view -> {

            back();
        });

        getCategoriesWithFamilies();

    }

    private void getCategoriesWithFamilies() {

        Api.getService(Tags.base_url).getCategoriesWithFamilies("off", 0, 0).enqueue(new Callback<SubCategoryFamilyModel>() {
            @Override
            public void onResponse(Call<SubCategoryFamilyModel> call, Response<SubCategoryFamilyModel> response) {
                binding.progBarCategory.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body().getData().getCategories());
                    if (categoryList.size()>0){
                        productFamilyCategoryAdapter.notifyDataSetChanged();
                        binding.tvNoData.setVisibility(View.GONE);
                    }else {
                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.progBarCategory.setVisibility(View.GONE);

                    switch (response.code()){
                        case 500:
                            Toast.makeText(ProductFamilyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ProductFamilyActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<SubCategoryFamilyModel> call, Throwable t) {
                try {
                    binding.progBarCategory.setVisibility(View.GONE);

                    Log.e(TAG, t.getMessage() + "__");
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ProductFamilyActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        }
                        else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }
                        else {
                            Toast.makeText(ProductFamilyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){

                }
            }
        });

    }

    public void showFamilies(SubCategoryFamilyModel.Data.Category category) {

        familyList.clear();
        if (category.getCategories_families().size()>0){
            familyList.addAll(category.getCategories_families());
            familyAdapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
            familyAdapter.setCategoryTitle(category.getTitle());
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }

    }

    public void navigateToFamilyActivity(FamilyModel familyModel) {
        Intent intent = new Intent(this, FamilyActivity.class);
        intent.putExtra("data",familyModel);
        startActivity(intent);
    }

    private void back() {
        finish();
    }


}