package com.refCustomerFamily.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_home.HomeActivity;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.order_fragments.PackageFragment;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.order_fragments.MarketsFragment;
import com.refCustomerFamily.activities_fragments.activity_home.fragments.order_fragments.OrderFragment;
import com.refCustomerFamily.adapters.ViewPagerAdapter;
import com.refCustomerFamily.databinding.FragmentOrdersBinding;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Orders extends Fragment {

    private HomeActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;


    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titles;

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
        userModel = preferences.getUserData(getActivity());
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();


        fragmentList.add(OrderFragment.newInstance());
        fragmentList.add(MarketsFragment.newInstance());
        fragmentList.add(PackageFragment.newInstance());

        titles.add(getString(R.string.productive_families));
        titles.add(getString(R.string.markets));
        titles.add(getString(R.string.delivery_package));
        binding.tab.setupWithViewPager(binding.pager);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragments_Titles(fragmentList, titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(3);

    }


}

