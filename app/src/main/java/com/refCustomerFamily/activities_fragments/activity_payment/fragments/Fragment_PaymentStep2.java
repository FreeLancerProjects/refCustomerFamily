package com.refCustomerFamily.activities_fragments.activity_payment.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.FragmentPaymentStep2Binding;


public class Fragment_PaymentStep2 extends Fragment {

    private static final String TAG = "DATA";

    private FragmentPaymentStep2Binding binding;


    public Fragment_PaymentStep2() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment__payment_step2, container, false);
        initView();



        return binding.getRoot();
    }

    private void initView() {

        Bundle bundle = getArguments();
        if (bundle != null) {


        }

    }

}