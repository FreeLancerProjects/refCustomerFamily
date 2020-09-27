package com.refCustomerFamily.activities_fragments.activity_add_Product.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.FragmentAddProductStep3Binding;
import com.refCustomerFamily.models.AddProductModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_AddProductStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AddProductStep3 extends Fragment {
    private static final String TAG = "DATA";

    private FragmentAddProductStep3Binding binding;
    private AddProductModel addProductModel;

    public static Fragment_AddProductStep3 newInstance(AddProductModel addProductModel) {
        Bundle bundle = new Bundle();
        Fragment_AddProductStep3 fragment_addProductStep3 = new Fragment_AddProductStep3();
        bundle.putSerializable(TAG, addProductModel);
        fragment_addProductStep3.setArguments(bundle);
        return new Fragment_AddProductStep3();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__add_product_step3, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            addProductModel = (AddProductModel) bundle.getSerializable(TAG);
        }
        binding.setModel(addProductModel);

    }
}