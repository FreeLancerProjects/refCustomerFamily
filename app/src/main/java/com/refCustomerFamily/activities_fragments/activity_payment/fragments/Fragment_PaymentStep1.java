package com.refCustomerFamily.activities_fragments.activity_payment.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_Product.fragments.Fragment_AddProductStep1;
import com.refCustomerFamily.databinding.FragmentPaymentStep1Binding;
import com.refCustomerFamily.databinding.FragmentPaymentStep2Binding;
import com.refCustomerFamily.models.AddProductModel;


public class Fragment_PaymentStep1 extends Fragment {

    private static final String TAG = "DATA";
    private FragmentPaymentStep1Binding binding;



    public static Fragment_PaymentStep1 newInstance() {
        Bundle bundle = new Bundle();

//        bundle.putSerializable(TAG,addProductModel);

        Fragment_PaymentStep1 fragment_paymentStep1 =  new Fragment_PaymentStep1();
        fragment_paymentStep1.setArguments(bundle);
        return fragment_paymentStep1;
    }


    public Fragment_PaymentStep1() {
        // Required empty public constructor
    }


    public static Fragment_PaymentStep1 newInstance(String param1, String param2) {
        Fragment_PaymentStep1 fragment = new Fragment_PaymentStep1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__payment_step1, container, false);
        initView();



        return binding.getRoot();
    }

    private void initView() {

        Bundle bundle = getArguments();
        if (bundle != null) {


        }

    }
}