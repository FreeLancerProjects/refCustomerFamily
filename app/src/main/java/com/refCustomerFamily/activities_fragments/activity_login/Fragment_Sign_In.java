package com.refCustomerFamily.activities_fragments.activity_login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.refCustomerFamily.adapters.CountriesAdapter;
import com.refCustomerFamily.databinding.DialogAlertBinding;
import com.refCustomerFamily.databinding.DialogCountriesBinding;
import com.refCustomerFamily.databinding.FragmentSignInBinding;
import com.refCustomerFamily.interfaces.Listeners;
import com.refCustomerFamily.models.CountryModel;
import com.refCustomerFamily.models.LoginModel;
import com.refCustomerFamily.preferences.Preferences;
import com.mukesh.countrypicker.CountryPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Sign_In extends Fragment implements Listeners.SkipListener {
    private FragmentSignInBinding binding;
    private LoginActivity activity;
    private String lang;
    private CountryPicker countryPicker;
    private LoginModel loginModel;
    private Preferences preferences;
    private List<CountryModel> countryModelList = new ArrayList<>();
    private CountriesAdapter countriesAdapter;
    private AlertDialog dialog;
    private String phone_code = "+966";
    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        preferences = Preferences.newInstance();
        loginModel = new LoginModel();
        activity = (LoginActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setLoginModel(loginModel);
        loginModel = new LoginModel();
        binding.setLoginModel(loginModel);
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0"))
                {
                    binding.edtPhone.setText("");
                }
            }
        });
        binding.tvSkip.setOnClickListener(v -> {
            navigateToHomeActivity();
        });
        createCountriesDialog();
        getPhoneCodes();


    }

    private void getPhoneCodes() {

        countryModelList.add(new CountryModel("+966","1"));
        countryModelList.add(new CountryModel("+20","2"));
        activity.runOnUiThread(() -> countriesAdapter.notifyDataSetChanged());
        if (countryModelList.size()>0){
            binding.tvCode.setText(countryModelList.get(0).getCode());

        }else {

            binding.tvCode.setText(phone_code);

        }

    }


    private void createCountriesDialog() {

        dialog = new AlertDialog.Builder(activity)
                .create();
        countriesAdapter = new CountriesAdapter(countryModelList,activity);

        DialogCountriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_countries, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(countriesAdapter);

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());

    }

    @Override
    public void skip() {
        binding.tvSkip.setEnabled(false);
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();


    }
    private void navigateToHomeActivity() {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
    }
    public void setItemData(CountryModel countryModel) {
        dialog.dismiss();
        phone_code = countryModel.getCode();
        binding.tvCode.setText(countryModel.getCode());
    }


}
