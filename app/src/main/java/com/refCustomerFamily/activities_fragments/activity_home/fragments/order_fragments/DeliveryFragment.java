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
import com.refCustomerFamily.adapters.OrderAdapter;
import com.refCustomerFamily.databinding.FragmentDeliveryBinding;
import com.refCustomerFamily.databinding.FragmentOrderBinding;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryFragment extends Fragment {

    private FragmentDeliveryBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private OrderAdapter orderAdapter;
    private List<OrderModel.Data> orderList;

    public static DeliveryFragment newInstance() {

        return new DeliveryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(getActivity());
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList, getActivity());
        Paper.init(getActivity());
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.recViewOrders.setAdapter(orderAdapter);
        binding.recViewOrders.setLayoutManager(new LinearLayoutManager(this.getContext()));


        getOrders();
    }
    private void getOrders() {
        binding.progBarOrders.setVisibility(View.VISIBLE);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiZTk4NGMwN2Y3MjY0ZTA2NDg3NzljNDNkMGJiNDVhNjBlZmYyZjllMzFhM2IwNTcyYjY3YjFmNDg3ZGUyODI5YTRkNmQzY2IyMDRjZjAxM2YiLCJpYXQiOjE2MDI1MTMzMTMsIm5iZiI6MTYwMjUxMzMxMywiZXhwIjoxNjM0MDQ5MzEzLCJzdWIiOiI2OSIsInNjb3BlcyI6W119.MD2vOi0xGACJ186yesTHnOwl_eTvuftaAkHlHo_062wgc7Y-RqgPviA6ehi2EPO40pq_jGa4cCnrPj8nxilWDe2caCQtKEsZvG9Yss_bG1Ea52Xrq7GslbUDP81IrLBTRUBy8lBAkf-luF1SQv8gmsXbF3TwGR13veeYm6KQOThTZa96SpPxbyQS0NFC424rtecl7fH-NuXEHIBGHZJU55FJeO_jVv9I1OusmIvPT7W0iRRL4eJ0EVntKR5LXfaF7lnfOV9P2qLU74ot2ODmA4idRIZGSJIUaA1C7gSuaINYlDeF5jbRW_m27qin47g5IddZC6Uo3z-KEGhpevJMh4bQNhzAO8Xfsv9jNUI7sExie_LGDAsMr5EeiP8pMuFlvarb4hGvWukTHVfootw5ez28uaM4TZtQK4XTNpdvoASBlUmYstwCWN2TeYCowiF9iI8oqkBByJykfhSiToXKaQO6kRsACGrv2KaWy_h3gq7Pt-4VaVLmktu_zapUSLcGnjKE4dEXgcx4jptBsr0Msw8wBDq1F5YKQSmEtzrCplc0PxzJw0MO_4C249DzQrGBEbI1u5S_8dMSyVb8M4YFmpF5mG3UKQDrKsVYgawNYvYG42ChN76RpLmIpvyAsbnvcIDlD6agwYZXQn1Jq-qW48cy6jfLjCYRMUN4a9f_NnU";
        Api.getService(Tags.base_url).getOrderByStatus("Bearer " + token,
                69, "family", "client", "current").enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                binding.progBarOrders.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    orderList.addAll(response.body().getData());
                    orderAdapter.notifyDataSetChanged();

                if (orderList.size() == 0){
                    binding.tvNoData.setVisibility(View.VISIBLE);
                }else {
                    binding.tvNoData.setVisibility(View.GONE);
                }
                }else {
                    Log.e("Fragment_Orders: ",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                binding.progBarOrders.setVisibility(View.GONE);
                Log.e("Fragment_Orders: ",t.getMessage());
            }
        });


    }
}