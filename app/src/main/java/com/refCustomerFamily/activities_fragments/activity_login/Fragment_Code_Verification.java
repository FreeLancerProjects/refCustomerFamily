package com.refCustomerFamily.activities_fragments.activity_login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.FragmentCodeVerificationBinding;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Code_Verification extends Fragment {
    private static final String TAG = "DATA";
    private LoginActivity activity;
    private FragmentCodeVerificationBinding binding;
    private CountDownTimer timer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private Preferences preferences;
    private UserModel userModel;
    private boolean canResend;
    private String lang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code_verification, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    public static Fragment_Code_Verification newInstance(UserModel userModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        Fragment_Code_Verification fragment_code_verification = new Fragment_Code_Verification();
        fragment_code_verification.setArguments(bundle);
        return fragment_code_verification;
    }

    private void initView() {

        activity = (LoginActivity) getActivity();
        preferences = Preferences.newInstance();
        mAuth = FirebaseAuth.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");



    }




}
