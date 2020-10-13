package com.refCustomerFamily.activities_fragments.activity_add_Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep1;
import com.refCustomerFamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep2;
import com.refCustomerFamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep3;
import com.refCustomerFamily.activities_fragments.activity_home.HomeActivity;
import com.refCustomerFamily.databinding.ActivityAddOfferBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.AddProductModel;

import io.paperdb.Paper;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "DATA";
    private ActivityAddOfferBinding binding;
    private FragmentManager fragmentManager;
    private String lang;
    private int step = 1;
    private AddProductModel addProductModel = null;
    private Fragment_AddProductStep1 fragment_addProductStep1;
    private Fragment_AddProductStep2 fragment_addProductStep2;
    private Fragment_AddProductStep3 fragment_addProductStep3;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        if (savedInstanceState == null) {
            displayFragmentStep1(addProductModel);
        }

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_offer);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        Intent intent1 = getIntent();
        if (intent1 !=null){
            addProductModel = (AddProductModel) intent1.getSerializableExtra(TAG);
        }

        if (addProductModel == null) {
            addProductModel = new AddProductModel();

        }


        binding.nextBtn.setOnClickListener(view -> {
            if (step == 1) {
                if (addProductModel.step1(this)){
                    displayFragmentStep2(addProductModel);

                }
            } else if (step == 2) {
                if (addProductModel.step1(this)){
                    displayFragmentStep3(addProductModel);

                }
            } else if (step == 3) {
                if (addProductModel.step1(this)){
                    Intent intent =  new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
            }


        });


        binding.prevBtn.setOnClickListener(view -> {
            if (step == 2) {
                displayFragmentStep1(addProductModel);
            } else if (step == 3) {
                displayFragmentStep2(addProductModel);

            }

        });

        binding.back.setOnClickListener(view -> {

            back();
        });

    }

    private void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (step == 1){
            finish();
        }else if (step == 2) {
            displayFragmentStep1(addProductModel);
        } else if (step == 3) {
            displayFragmentStep2(addProductModel);

        }

    }

    private void displayFragmentStep1(AddProductModel addProductModel) {
        updateStep1UI();
        step = 1;
        if (fragment_addProductStep1 == null) {
            fragment_addProductStep1 = Fragment_AddProductStep1.newInstance(addProductModel);
        }
        if (fragment_addProductStep2 != null && fragment_addProductStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep2).commit();
        }

        if (fragment_addProductStep3 != null && fragment_addProductStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep3).commit();
        }
        if (fragment_addProductStep1.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addProductStep1).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addProductStep1, "fragment_addProductStep1").addToBackStack("fragment_addProductStep1").commit();
        }

    }


    private void displayFragmentStep2(AddProductModel addProductModel) {
        updateStep2UI();
        step = 2;
        if (fragment_addProductStep2 == null) {
            fragment_addProductStep2 = Fragment_AddProductStep2.newInstance(addProductModel);
        }
        if (fragment_addProductStep1 != null && fragment_addProductStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep1).commit();
        }

        if (fragment_addProductStep3 != null && fragment_addProductStep3.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep3).commit();
        }
        if (fragment_addProductStep2.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addProductStep2).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addProductStep2, "fragment_addProductStep2").addToBackStack("fragment_addProductStep2").commit();
        }

    }

    private void displayFragmentStep3(AddProductModel addProductModel) {
        updateStep3UI();
        step = 3;
        if (fragment_addProductStep3 == null) {
            fragment_addProductStep3 = Fragment_AddProductStep3.newInstance(addProductModel);
        }
        if (fragment_addProductStep1 != null && fragment_addProductStep1.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep1).commit();
        }

        if (fragment_addProductStep2 != null && fragment_addProductStep2.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_addProductStep2).commit();
        }
        if (fragment_addProductStep3.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_addProductStep3).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_addProductStep3, "fragment_addProductStep3").addToBackStack("fragment_addProductStep3").commit();
        }

    }


    private void updateStep1UI() {
        binding.nextBtn.setText(getResources().getString(R.string.next));
        binding.prevBtn.setVisibility(View.GONE);
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
    }

    private void updateStep2UI() {
        binding.prevBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setText(getResources().getString(R.string.next));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_primary_line_bg));
    }

    private void updateStep3UI() {
        binding.prevBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setText(getResources().getString(R.string.add_offer));
        binding.step3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
        binding.step2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
    }

}