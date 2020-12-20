package com.refCustomerFamily.activities_fragments.activity_home.fragments.order_fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.refCustomerFamily.R;
import com.refCustomerFamily.adapters.PackageAdapter;
import com.refCustomerFamily.databinding.FragmentPackegesBinding;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageOrderFragment extends Fragment {

    private FragmentPackegesBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private PackageAdapter orderAdapter;
    private List<OrderModel.Data> orderList;

    public static PackageOrderFragment newInstance() {

        return new PackageOrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_packeges, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(getActivity());
        orderList = new ArrayList<>();
        Paper.init(getActivity());
        binding.recViewOrders.setAdapter(orderAdapter);

        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recViewOrders.setLayoutManager(new LinearLayoutManager(this.getContext()));
        orderAdapter = new PackageAdapter(orderList, getActivity());
        binding.recViewOrders.setAdapter(orderAdapter);

        // getOrders();
    }

    public void getOrders() {
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
        binding.progBarOrders.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).getOrderByStatus("Bearer " + userModel.getData().getToken(),
                userModel.getData().getId(), "package", "client", "current").enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                binding.progBarOrders.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    orderList.addAll(response.body().getData());
                    orderAdapter.notifyDataSetChanged();

                    if (orderList.size() == 0) {
                        binding.linearNoData.setVisibility(View.VISIBLE);
                    } else {
                        binding.linearNoData.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("Fragment_Orders: ", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                binding.progBarOrders.setVisibility(View.GONE);
                Log.e("Fragment_Orders: ", t.getMessage());
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders();
    }
}