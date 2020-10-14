package com.refCustomerFamily.activities_fragments.familyorderstepsactivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ActivityFamilyorderStepsBinding;
import com.refCustomerFamily.databinding.ActivityOrderStepsBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.share.Common;
import com.refCustomerFamily.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyOrderStepsActivity extends AppCompatActivity {

    private ActivityFamilyorderStepsBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private Preferences preferences;
    private UserModel userModel;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_familyorder_steps);

        initView();
        getDataFromIntent();
        getOrderDetials();

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderModel = (OrderModel.Data) getIntent().getSerializableExtra("data");
        }


    }

    public void getOrderDetials() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getorderdetials("Bearer " + userModel.getData().getToken(), orderModel.getId())
                .enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        //  binding.progBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            updatedata(response.body());
                            // orderDetails.addAll(response.body().getOrderDetails());
//                            if (response.body().getOrderDetails().size() > 0) {
//                                // rec_sent.setVisibility(View.VISIBLE);
//
//                                binding.llNoStore.setVisibility(View.GONE);
//                                order_detials_adapter.notifyDataSetChanged();
//                                // updatestatus(response.body());
//                                //   total_page = response.body().getMeta().getLast_page();
//
//                            } else {
//                                binding.llNoStore.setVisibility(View.VISIBLE);
//
//                            }
                        } else {

                            Toast.makeText(FamilyOrderStepsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            //     binding.progBar.setVisibility(View.GONE);

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void updatedata(OrderModel body) {

        binding.setModel(body.getOrder());
        if (body.getOrder().getStatus().equals("family-accept-order")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));

        }
        else if (body.getOrder().getStatus().equals("family_prepare_order")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));


            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));


        } else if (body.getOrder().getStatus().equals("family_end_prepare_order")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));
            binding.tv3.setTextColor(getResources().getColor(R.color.black));


        }

        else if (body.getOrder().getStatus().equals(" driver_accepted_order")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image4.setBackground(getResources().getDrawable(R.drawable.circle_bg));

            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));
            binding.tv3.setTextColor(getResources().getColor(R.color.black));
            binding.tv4.setTextColor(getResources().getColor(R.color.black));

        }
        else if (body.getOrder().getStatus().equals("family_give_order_to_driver")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image4.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image5.setBackground(getResources().getDrawable(R.drawable.circle_bg));

            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));
            binding.tv3.setTextColor(getResources().getColor(R.color.black));
            binding.tv4.setTextColor(getResources().getColor(R.color.black));
            binding.tv5.setTextColor(getResources().getColor(R.color.black));

        }
   else if (body.getOrder().getStatus().equals("driver_in_way")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image4.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image5.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));
            binding.tv3.setTextColor(getResources().getColor(R.color.black));
            binding.tv4.setTextColor(getResources().getColor(R.color.black));
            binding.tv5.setTextColor(getResources().getColor(R.color.black));
            binding.tv6.setTextColor(getResources().getColor(R.color.black));


        } else if (body.getOrder().getStatus().equals("driver_give_order_to_client")) {
            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image3.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image4.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image5.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image7.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.image1.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image2.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image3.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image4.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image5.setBackground(getResources().getDrawable(R.drawable.circle_bg));
            binding.image6.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            binding.image7.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));

            binding.tv1.setTextColor(getResources().getColor(R.color.black));
            binding.tv2.setTextColor(getResources().getColor(R.color.black));
            binding.tv3.setTextColor(getResources().getColor(R.color.black));
            binding.tv4.setTextColor(getResources().getColor(R.color.black));
            binding.tv5.setTextColor(getResources().getColor(R.color.black));
            binding.tv6.setTextColor(getResources().getColor(R.color.black));
            binding.tv7.setTextColor(getResources().getColor(R.color.black));

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }
}