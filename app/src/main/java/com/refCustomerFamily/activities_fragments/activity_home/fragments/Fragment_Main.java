package com.refCustomerFamily.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_Product.AddProductActivity;
import com.refCustomerFamily.activities_fragments.activity_home.HomeActivity;
import com.refCustomerFamily.activities_fragments.activity_package.PackageActivity;
import com.refCustomerFamily.activities_fragments.activity_product_family.ProductFamilyActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_google_stores.GoogleStoresActivity;
import com.refCustomerFamily.adapters.CategoryAdapter;
import com.refCustomerFamily.adapters.ProductAdapter;
import com.refCustomerFamily.adapters.SlidingImage_Adapter;
import com.refCustomerFamily.databinding.FragmentMainBinding;
import com.refCustomerFamily.models.SliderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {

    private HomeActivity activity;
    private FragmentMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;
    private String lang;
    private int current_page = 0, NUM_PAGES;
    private boolean isLoading = false;
    private UserModel userModel;
    private SlidingImage_Adapter slidingImage__adapter;


    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();
        get_slider();
        change_slide_image();

        return binding.getRoot();

    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        binding.tab.setupWithViewPager(binding.pager);
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);



        binding.layout1.setOnClickListener(view -> {

            Intent intent = new Intent(this.getContext(), ProductFamilyActivity.class);
            startActivity(intent);

        });

        binding.layout2.setOnClickListener(view -> {

            Intent intent = new Intent(activity, GoogleStoresActivity.class);
            intent.putExtra("lat",activity.user_lat);
            intent.putExtra("lng",activity.user_lng);

            startActivity(intent);

        });
        binding.layout3.setOnClickListener(view -> {

            Intent intent = new Intent(activity, PackageActivity.class);

            startActivity(intent);

        });

    }

    private void get_slider() {

        Api.getService(Tags.base_url).getSliders().enqueue(new Callback<SliderModel>() {
            @Override
            public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                binding.progBarSlider.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    if (response.body().getData().getCategories().size() > 0) {
                        NUM_PAGES = response.body().getData().getCategories().size();
                        slidingImage__adapter = new SlidingImage_Adapter(activity, response.body().getData().getCategories());
                        binding.pager.setAdapter(slidingImage__adapter);

                    } else {

                        binding.pager.setVisibility(View.GONE);
                    }
                } else if (response.code() == 404) {
                    binding.pager.setVisibility(View.GONE);
                } else {
                    binding.pager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SliderModel> call, Throwable t) {
                try {
                    binding.progBarSlider.setVisibility(View.GONE);
                    binding.pager.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {

                }

            }
        });

    }
    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    private void NavigateToAddOfferACtivity() {

        Intent intent = new Intent(this.getContext(), AddProductActivity.class);
        startActivity(intent);

    }

}
