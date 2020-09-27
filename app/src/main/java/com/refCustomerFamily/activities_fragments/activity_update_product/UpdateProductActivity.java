package com.refCustomerFamily.activities_fragments.activity_update_product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.refCustomerFamily.adapters.SliderAdapter;
import com.refCustomerFamily.databinding.ActivityUpdateProductBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.AddProductModel;
import com.refCustomerFamily.models.SliderModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class UpdateProductActivity extends AppCompatActivity {

    private static final String TAG = "DATA";
    private ActivityUpdateProductBinding binding;
    private String lang;
    private SliderAdapter sliderAdapter;
    private List<SliderModel.Data> sliderModelList;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_product);
        initView();


        getSliderData();
    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        sliderModelList = new ArrayList<>();



        binding.updateBtn.setOnClickListener(view -> {


            Intent intent = new Intent(this, AddProductActivity.class);
            intent.putExtra(TAG,new AddProductModel());
            startActivity(intent);

        });

        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void back() {
        finish();
    }

    //initiate slider ui
    private void sliderInit() {
        //top slider
        sliderAdapter = new SliderAdapter(this, sliderModelList);
        binding.imageSliderTop.setSliderAdapter(sliderAdapter);
        binding.imageSliderTop.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSliderTop.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSliderTop.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSliderTop.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSliderTop.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSliderTop.setScrollTimeInSec(3);
        binding.imageSliderTop.setAutoCycle(true);
        binding.imageSliderTop.startAutoCycle();

    }


    // get data slider from API
    private void getSliderData() {

    }

}