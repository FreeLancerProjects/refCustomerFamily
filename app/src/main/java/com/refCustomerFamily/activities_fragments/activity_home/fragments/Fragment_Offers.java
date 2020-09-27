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
import com.refCustomerFamily.adapters.OfferAdapter;
import com.refCustomerFamily.databinding.FragmentOffersBinding;
import com.refCustomerFamily.databinding.FragmentOrdersBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import io.paperdb.Paper;

public class Fragment_Offers extends Fragment {

    private HomeActivity activity;
    private FragmentOffersBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;


    public static Fragment_Offers newInstance() {
        return new Fragment_Offers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Language_Helper.getLanguage(this.getContext());
        binding.setLang(lang);
        binding.recViewOffers.setAdapter(new OfferAdapter(this.getContext()));
        binding.recViewOffers.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

}