package com.refCustomerFamily.activities_fragments.activity_add_Product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.FragmentAddProductStep2Binding;
import com.refCustomerFamily.models.AddProductModel;

public class Fragment_AddProductStep2 extends Fragment {
    private static final String TAG = "DATA";
    private FragmentAddProductStep2Binding binding;
    private AddProductModel addProductModel = null;

    public static Fragment_AddProductStep2 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, addProductModel);
        Fragment_AddProductStep2 fragment_addProductStep2 = new Fragment_AddProductStep2();
        fragment_addProductStep2.setArguments(bundle);
        return fragment_addProductStep2;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_product_step2, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        binding.foundNotfoundLayout.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            addProductModel = (AddProductModel) bundle.getSerializable(TAG);
        }
        binding.setModel(addProductModel);


        binding.foundBtn.setOnClickListener(view -> {
            binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
            binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
            binding.foundNotfoundLayout.setVisibility(View.VISIBLE);


        });

        binding.notFoundBtn.setOnClickListener(view -> {
            binding.notFoundBtn.setBackground(getResources().getDrawable(R.drawable.custom_bg));
            binding.foundBtn.setBackground(getResources().getDrawable(R.drawable.small_stroke_primary));
            binding.foundNotfoundLayout.setVisibility(View.GONE);


        });

    }


}