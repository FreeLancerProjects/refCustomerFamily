package com.refCustomerFamily.activities_fragments.activity_product_family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.refCustomerFamily.R;
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
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
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
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("size:", response.body().getData().getCategories().size() + "");
                    categoryList.clear();
                    categoryList.addAll(response.body().getData().getCategories());
                    productFamilyCategoryAdapter.notifyDataSetChanged();
                } else {
                    Log.e("size  = 0", response.body().getData().getCategories().size() + "");
                }
            }

            @Override
            public void onFailure(Call<SubCategoryFamilyModel> call, Throwable t) {
                Log.e(TAG, t.getMessage() + "__");
            }
        });

    }

    public void showFamilies(int position) {
        familyList.clear();
        familyList.addAll(categoryList.get(position).getCategories_families());
        familyAdapter.notifyDataSetChanged();
    }


    private void back() {
        finish();
    }

}