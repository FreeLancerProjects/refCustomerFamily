package com.refCustomerFamily.activities_fragments.activity_orderdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_chat.ChatActivity;
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.adapters.OrderDetailAdapter;
import com.refCustomerFamily.databinding.ActivityOrderDetailBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        getDataFromIntent();
        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setModel(orderModel);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);


        Log.e("statussss:" ,orderModel.getStatus());
        if (orderModel.getStatus().equals("family_accepted_order")) {
            binding.imgChat.setVisibility(View.GONE);
            binding.imgCall.setVisibility(View.GONE);
            binding.linearBtn.setVisibility(View.GONE);
            binding.viewStatusBtn.setVisibility(View.VISIBLE);
        } else {
            binding.imgChat.setVisibility(View.VISIBLE);
            binding.imgCall.setVisibility(View.VISIBLE);
            binding.linearBtn.setVisibility(View.VISIBLE);
            binding.viewStatusBtn.setVisibility(View.GONE);
        }

        binding.back.setOnClickListener(view -> {

            back();
        });

        binding.viewStatusBtn.setOnClickListener(view -> {

            Intent intent = new Intent(OrderDetailActivity.this, OrderStepsActivity.class);
            startActivity(intent);


        });
        binding.imgChat.setOnClickListener(view -> {

            Intent intent = new Intent(OrderDetailActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();

        });

        binding.imgCall.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderModel.getClient().getPhone_code() + orderModel.getClient().getPhone()));
            startActivity(intent);

        });

        binding.acceptBtn.setOnClickListener(view -> {


            Api.getService(Tags.base_url).familyAcceptOrder("Bearer "+userModel.getUser().getToken(),orderModel.getClient_id(),
                    orderModel.getId(),orderModel.getFamily_id()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body()!= null){
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderDetailActivity.this, OrderStepsActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("onFailure:",t.getMessage());
                }
            });



        });


    }

    private void back() {
        finish();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderModel = (OrderModel.Data) getIntent().getSerializableExtra("DATA");
        }


    }


}