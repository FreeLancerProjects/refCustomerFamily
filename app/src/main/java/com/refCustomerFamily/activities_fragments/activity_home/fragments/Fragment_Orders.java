package com.refCustomerFamily.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_home.HomeActivity;
import com.refCustomerFamily.adapters.OrderAdapter;
import com.refCustomerFamily.databinding.FragmentOrdersBinding;
import com.refCustomerFamily.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Orders extends Fragment {

    private HomeActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private String lang;
    private OrderAdapter orderAdapter;
    public static Fragment_Orders newInstance() {
        return new Fragment_Orders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.recViewOrders.setAdapter(new OrderAdapter(this.getContext()));
        binding.recViewOrders.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }


}
