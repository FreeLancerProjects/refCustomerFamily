package com.refCustomerFamily.activities_fragments.activity_family;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_order_product.AddOrderProductActivity;
import com.refCustomerFamily.activities_fragments.activity_cart.CartActivity;
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.activities_fragments.familyorderstepsactivity.FamilyOrderStepsActivity;
import com.refCustomerFamily.adapters.CategoryAdapter;
import com.refCustomerFamily.adapters.FamilyProductAdapter;
import com.refCustomerFamily.databinding.ActivityFamilyBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.FamilyCategory;
import com.refCustomerFamily.models.FamilyCategoryProductDataModel;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.ProductModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.share.Common;
import com.refCustomerFamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private FamilyProductAdapter familyAdapter;
    private FamilyModel familyModel;
    private List<FamilyCategory> familyCategoryList;
    private List<ProductModel> productModelList;
    private List<ProductModel> selectedProductList;
    private int parent_pos = -1;
    private double total = 0.0;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang", "ar")));
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
        selectedProductList = new ArrayList<>();
        productModelList = new ArrayList<>();
        familyCategoryList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences=Preferences.newInstance();
        userModel=preferences.getUserData(this);
        binding.setLang(lang);
        binding.setModel(familyModel);
        categoryAdapter = new CategoryAdapter(familyCategoryList, this);
        binding.recViewCategory.setAdapter(categoryAdapter);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        familyAdapter = new FamilyProductAdapter(productModelList, this);
        binding.recViewFamily.setAdapter(familyAdapter);
        binding.recViewFamily.setLayoutManager(new LinearLayoutManager(this));

        if (familyModel.getBanner() != null && !familyModel.getBanner().isEmpty() && !familyModel.getBanner().equals("0")) {
            Picasso.get().load(Uri.parse(Tags.IMAGE_URL + familyModel.getBanner())).fit().into(binding.imageSliderTop, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    binding.flNoImage.setVisibility(View.GONE);

                }

                @Override
                public void onError(Exception e) {
                    binding.flNoImage.setVisibility(View.VISIBLE);
                }
            });

        } else {
            binding.flNoImage.setVisibility(View.VISIBLE);

        }


        binding.addToCart.setOnClickListener(view -> {
            if (selectedProductList.size() > 0) {
                if (userModel!=null){
                    Intent intent = new Intent(this, AddOrderProductActivity.class);
                intent.putExtra("data", familyModel);
                intent.putExtra("cost", total);
                intent.putExtra("products", (Serializable) selectedProductList);
                startActivityForResult(intent, 100);
            }
            else {
                    Common.CreateDialogAlert2(FamilyActivity.this,getResources().getString(R.string.please_sign_in_or_sign_up));
                }
            }

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
                        showFamilyProducts(familyCategoryList.get(0), 0);
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


    public void showFamilyProducts(FamilyCategory familyCategory, int adapterPosition) {
        parent_pos = adapterPosition;
        productModelList.clear();
        productModelList.addAll(familyCategory.getProducts());
        if (familyCategoryList.size() > 0) {
            familyAdapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }

    public void updateProduct(ProductModel model, int adapterPosition) {
        familyCategoryList.get(parent_pos).getProducts().set(adapterPosition, model);
        familyAdapter.notifyItemChanged(adapterPosition);
    }

    public void addToCart(ProductModel model, int adapterPosition) {
        int itemPos = isItemInCart(model);
        if (itemPos == -1) {
            selectedProductList.add(model);

        } else {
            selectedProductList.set(itemPos, model);

        }
        total = calculateTotal();
        binding.tvTotal.setText(String.format(Locale.ENGLISH, "%s %s", total, getString(R.string.sar)));
        familyAdapter.notifyItemChanged(adapterPosition);

    }

    private int isItemInCart(ProductModel productModel) {
        int pos = -1;
        for (int index = 0; index < selectedProductList.size(); index++) {
            ProductModel model = selectedProductList.get(index);
            if (model.getId() == productModel.getId()) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private double calculateTotal() {
        double total = 0.0;
        for (ProductModel model : selectedProductList) {
            total += model.getPrice() * model.getCount();
        }
        return total;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedProductList.clear();
            total = 0.0;
            binding.tvTotal.setText(String.format(Locale.ENGLISH, "%s %s", total, getString(R.string.sar)));
            int order_id = data.getIntExtra("order_id", 0);
            OrderModel.Data order=new OrderModel.Data();
            order.setId(order_id);
            Intent intent = new Intent(this, FamilyOrderStepsActivity.class);
            intent.putExtra("data", order);
            startActivity(intent);

            finish();
        }
    }
}