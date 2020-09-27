package com.refCustomerFamily.activities_fragments.activity_about_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ActivityAppDataBinding;
import com.refCustomerFamily.interfaces.Listeners;
import com.refCustomerFamily.language.Language_Helper;
import java.util.Locale;
import io.paperdb.Paper;

public class AboutAppActivity extends AppCompatActivity implements Listeners.BackListener{
    private ActivityAppDataBinding binding;
    private String lang;
    private int type;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_data);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("type"))
        {
            type = intent.getIntExtra("type",0);

        }
    }


    private void initView()
    {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);

        if (type==1)
        {
            binding.setTitle(getString(R.string.terms_and_conditions));
        }else if (type ==2)
        {
            binding.setTitle(getString(R.string.about_app));

        }
        else if (type ==3)
        {
            binding.setTitle(getString(R.string.privacy));

        }


        getAppData();

    }

    private void getAppData()
    {

//        Api.getService(Tags.base_url)
//                .getSetting()
//                .enqueue(new Callback<SettingModel>() {
//                    @Override
//                    public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
//                        binding.progBar.setVisibility(View.GONE);
//                        if (response.isSuccessful() && response.body() != null) {
//
//                            if (type==1)
//                            {
//
//                                binding.setContent(response.body().getData().getTerm_conditions());
//                            }else
//                            {
//                                binding.setContent(response.body().getData().getAbout_app());
//
//                            }
//
//                        } else {
//                            try {
//
//                                Log.e("error", response.code() + "_" + response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            if (response.code() == 500) {
//                                Toast.makeText(AboutAppActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                Toast.makeText(AboutAppActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SettingModel> call, Throwable t) {
//                        try {
//                            binding.progBar.setVisibility(View.GONE);
//
//                            if (t.getMessage() != null) {
//                                Log.e("error", t.getMessage());
//                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                    Toast.makeText(AboutAppActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(AboutAppActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                        } catch (Exception e) {
//                        }
//                    }
//                });

    }


    @Override
    public void back() {
        finish();
    }

}
