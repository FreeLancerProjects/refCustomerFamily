package com.refCustomerFamily.activities_fragments.activity_add_Product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.refCustomerFamily.databinding.FragmentAddProductStep1Binding;
import com.refCustomerFamily.models.AddProductModel;

public class Fragment_AddProductStep1 extends Fragment {
    private static final String TAG = "DATA";
    private AddProductActivity activity;
    private FragmentAddProductStep1Binding binding;
    private AddProductModel addProductModel;


    public static Fragment_AddProductStep1 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,addProductModel);
        Fragment_AddProductStep1 fragment_addProductStep1 =  new Fragment_AddProductStep1();
        fragment_addProductStep1.setArguments(bundle);
        return fragment_addProductStep1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_product_step1, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        Bundle bundle = getArguments();
        if (bundle!=null){
            addProductModel = (AddProductModel) bundle.getSerializable(TAG);
        }
        binding.setModel(addProductModel);
    }
}