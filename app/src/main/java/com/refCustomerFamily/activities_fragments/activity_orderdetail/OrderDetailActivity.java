package com.refCustomerFamily.activities_fragments.activity_orderdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_map.MapActivity;
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.activities_fragments.chat_activity.ChatActivity;
import com.refCustomerFamily.activities_fragments.familyorderstepsactivity.FamilyOrderStepsActivity;
import com.refCustomerFamily.adapters.Image_Adapter;
import com.refCustomerFamily.databinding.ActivityOrderDetailBinding;
import com.refCustomerFamily.interfaces.Listeners;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.ChatUserModel;
import com.refCustomerFamily.models.NotFireModel;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.ProductModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.share.Common;
import com.refCustomerFamily.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityOrderDetailBinding binding;
    private String lang;
    private OrderModel.Data orderModel;
    private UserModel userModel;
    private Preferences preferences;
    private List<ProductModel.ImageModel> imageModels;
    private Image_Adapter image_adapter;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    ImagePopup imagePopup;
    public double user_lat = 0.0, user_lng = 0.0;


    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        getDataFromIntent();

        initView();
        getOrderDetials();
    }

    private void initView() {
        EventBus.getDefault().register(this);

        imagePopup = new ImagePopup(this);
        imagePopup.setFullScreen(true);
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(false);
        imagePopup.setImageOnClickClose(true);
        imageModels = new ArrayList<>();
        binding.setBackListener(this);

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        //  binding.setModel(orderModel);
        binding.setBackListener(this);
        preferences = Preferences.newInstance();
        preferences.create_update_orderUserData(this, orderModel.getId() + "");

        userModel = preferences.getUserData(this);
        if (orderModel.getOrder_images() != null) {
            imageModels.addAll(orderModel.getOrder_images());
            //   Log.e("lsllsls", orderModel.getOrder_images().get(0).getImage());
        }
        image_adapter = new Image_Adapter(imageModels, this);

        binding.recview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recview.setAdapter(image_adapter);

        // Log.e("statussss:", orderModel.getStatus());



        binding.tvfromaddres.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("lat",Double.parseDouble(orderModel.getFrom_latitude()));
            intent.putExtra("lng",Double.parseDouble(orderModel.getFrom_longitude()));
            intent.putExtra("address",orderModel.getFrom_address());
            intent.putExtra("type","from");

            startActivity(intent);

        });
        binding.tvtoaddres.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("lat",Double.parseDouble(orderModel.getTo_latitude()));
            intent.putExtra("lng",Double.parseDouble(orderModel.getTo_longitude()));
            intent.putExtra("address",orderModel.getTo_address());
            intent.putExtra("type","to");

            startActivity(intent);

        });

//        binding.acceptBtn.setOnClickListener(view -> {
//            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
//            dialog.setCancelable(false);
//            dialog.show();
//
//            Api.getService(Tags.base_url).familyAcceptOrder("Bearer " + userModel.getData().getToken(), orderModel.getClient_id(),
//                    orderModel.getId(), userModel.getData().getId()).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        dialog.dismiss();
//                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(OrderDetailActivity.this, OrderStepsActivity.class);
//                        intent.putExtra("data", orderModel);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                        try {
//                            Log.e("llllll", response.errorBody().string());
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dialog.dismiss();
//                    Log.e("onFailure:", t.getMessage());
//                }
//            });
//
//
//        });
//        binding.refuseBtn.setOnClickListener(view -> {
//
//
//            Api.getService(Tags.base_url).familyrefuesOrder("Bearer " + userModel.getData().getToken(), orderModel.getClient_id(),
//                    orderModel.getId(), userModel.getData().getId()).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
//
//                        finish();
//                    } else {
//                        Toast.makeText(OrderDetailActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                        try {
//                            Log.e("llllll", response.errorBody().string());
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.e("onFailure:", t.getMessage());
//                }
//            });
//
//
//        });

        binding.viewStatusBtn.setOnClickListener(view -> {
if(orderModel.getOrder_type().equals("family")){
    Intent intent = new Intent(OrderDetailActivity.this, FamilyOrderStepsActivity.class);
    intent.putExtra("data", orderModel);
    startActivity(intent);
}
else {
    Intent intent = new Intent(OrderDetailActivity.this, OrderStepsActivity.class);
    intent.putExtra("data", orderModel);
    startActivity(intent);

}
        });
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderModel = (OrderModel.Data) getIntent().getSerializableExtra("DATA");
            user_lat = intent.getDoubleExtra("lat", 0.0);
            user_lng = intent.getDoubleExtra("lng", 0.0);
        }


    }

    public void showimage(String layoutPosition) {
        imagePopup.initiatePopupWithPicasso(Tags.IMAGE_URL + layoutPosition);
        imagePopup.viewPopup();
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

                            Toast.makeText(OrderDetailActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
        this.orderModel = body.getOrder();
        if(orderModel.getBill_image()==null){
            binding.image.setVisibility(View.GONE);
        }
        binding.setModel(body.getOrder());
        if(!orderModel.getOrder_type().equals("family")){
            binding.tv1.setText(getResources().getString(R.string.market));
        }
        String ship =String.format(Locale.ENGLISH, "%s %s", String.format(Locale.ENGLISH, "%.2f", (SphericalUtil.computeDistanceBetween(new LatLng(user_lat, user_lng), new LatLng(  Double.parseDouble(orderModel.getFrom_latitude()), Double.parseDouble(orderModel.getFrom_longitude()))) / 1000)), getString(R.string.km));
        String arrivew =String.format(Locale.ENGLISH, "%s %s", String.format(Locale.ENGLISH, "%.2f", (SphericalUtil.computeDistanceBetween(new LatLng(user_lat, user_lng), new LatLng(   Double.parseDouble(orderModel.getTo_latitude()), Double.parseDouble(orderModel.getTo_longitude()))) / 1000)), getString(R.string.km));

        //        float[] results = new float[1];
//        Location.distanceBetween(user_lat, user_lng,
//                Double.parseDouble(orderModel.getFrom_latitude()), Double.parseDouble(orderModel.getFrom_longitude()), results);
        binding.tvlocationship.setText(ship);
//        Location.distanceBetween(user_lat, user_lng,
//                Double.parseDouble(orderModel.getTo_latitude()), Double.parseDouble(orderModel.getTo_longitude()), results);
        binding.tvlocationarrive.setText(arrivew);

        if (orderModel.getStatus().equals("new") || orderModel.getStatus().equals("driver_give_order_to_client")) {

            binding.imgChat.setVisibility(View.GONE);
            binding.imgCall.setVisibility(View.GONE);
            binding.linearBtn.setVisibility(View.VISIBLE);
            binding.viewStatusBtn.setVisibility(View.GONE);
        } else {
            binding.imgChat.setVisibility(View.VISIBLE);
            binding.imgCall.setVisibility(View.VISIBLE);
            binding.linearBtn.setVisibility(View.GONE);
            binding.viewStatusBtn.setVisibility(View.VISIBLE);
        }
        binding.imgChat.setVisibility(View.GONE);
        binding.imgCall.setVisibility(View.GONE);
        binding.linearBtn.setVisibility(View.GONE);
        binding.viewStatusBtn.setVisibility(View.VISIBLE);

    }

    @Override
    public void back() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(NotFireModel notFireModel) {
        getOrderDetials();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        preferences.clearorder(this);
    }
}