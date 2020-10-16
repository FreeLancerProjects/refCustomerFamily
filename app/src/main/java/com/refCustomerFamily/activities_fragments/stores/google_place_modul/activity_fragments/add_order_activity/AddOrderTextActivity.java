package com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.add_order_activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.activities_fragments.activity_package.PackageActivity;
import com.refCustomerFamily.activities_fragments.chat_activity.ChatActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_add_coupon.AddCouponActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_map_search.MapSearchActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters.AddOrderImagesAdapter;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.AddOrderTextModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.FavoriteLocationModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.NearbyModel;
import com.refCustomerFamily.databinding.ActivityAddOrderTextBinding;
import com.refCustomerFamily.databinding.DialogSelectImage2Binding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.SingleOrderDataModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.share.Common;
import com.refCustomerFamily.tags.Tags;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrderTextActivity extends AppCompatActivity {
    private ActivityAddOrderTextBinding binding;
    private NearbyModel.Result placeModel;
    private String lang;
    private boolean canSend = false;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private List<Uri> imagesList;
    private AlertDialog dialog;
    private AddOrderImagesAdapter addOrderImagesAdapter;
    private AddOrderTextModel addOrderTextModel;
    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_order_text);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        placeModel = (NearbyModel.Result) intent.getSerializableExtra("data");

    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        addOrderTextModel = new AddOrderTextModel();
        imagesList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(placeModel);
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addOrderImagesAdapter = new AddOrderImagesAdapter(imagesList, this);
        binding.recViewImages.setAdapter(addOrderImagesAdapter);

        binding.edtOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    canSend = true;
                } else {
                    canSend = false;
                }

                updateBtnUI();
            }
        });
        binding.imageCamera.setOnClickListener(v -> createDialogAlert());
        binding.tvAddCoupon.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCouponActivity.class);
            startActivityForResult(intent, 100);
        });
        binding.close.setOnClickListener(v -> {
            super.onBackPressed();
        });
        binding.btnNext.setOnClickListener(v -> {
            if (userModel != null) {
                if (canSend) {
                    String order_text = binding.edtOrder.getText().toString();
                    if (!order_text.isEmpty()) {
                        Common.CloseKeyBoard(this, binding.edtOrder);
                        binding.edtOrder.setError(null);
                        addOrderTextModel.setOrder_description(order_text);
                        Intent intent = new Intent(this, MapSearchActivity.class);
                        intent.putExtra("type", 1);
                        startActivityForResult(intent, 200);
                    } else {
                        binding.edtOrder.setError(getString(R.string.field_req));
                    }
                }
            } else {
                Common.CreateDialogAlert2(AddOrderTextActivity.this, getResources().getString(R.string.please_sign_in_or_sign_up));
            }


        });

        addOrderTextModel.setUser_id(userModel.getData().getId());
        addOrderTextModel.setFamily_id(0);
        addOrderTextModel.setOrder_type("google");
        addOrderTextModel.setGoogle_place_id(placeModel.getPlace_id());
        addOrderTextModel.setBill_cost(0);
        addOrderTextModel.setFrom_address(placeModel.getVicinity());
        addOrderTextModel.setFrom_latitude(placeModel.getGeometry().getLocation().getLat());
        addOrderTextModel.setFrom_longitude(placeModel.getGeometry().getLocation().getLng());
        addOrderTextModel.setFrom_name(placeModel.getName());
        addOrderTextModel.setCoupon_id("0");
        addOrderTextModel.setPayment_method("cash");
        addOrderTextModel.setOrder_notes("");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String timeArrival = dateFormat.format(new Date(calendar.getTimeInMillis()));
        addOrderTextModel.setEnd_shipping_time(timeArrival);
        addOrderTextModel.setHour_arrival_time("1");


    }

    private void updateBtnUI() {
        if (canSend) {
            binding.btnNext.setBackgroundResource(R.color.colorPrimary);
        } else {
            binding.btnNext.setBackgroundResource(R.color.gray8);

        }
    }

    private void sendOrderTextWithoutImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .sendTextOrder("Bearer " + userModel.getData().getToken(), addOrderTextModel.getUser_id(), addOrderTextModel.getFamily_id(), addOrderTextModel.getOrder_type(), addOrderTextModel.getGoogle_place_id(), String.valueOf(addOrderTextModel.getBill_cost()), addOrderTextModel.getTo_address(), addOrderTextModel.getTo_latitude(), addOrderTextModel.getTo_longitude(), addOrderTextModel.getFrom_name(), addOrderTextModel.getFrom_address(), addOrderTextModel.getFrom_latitude(), addOrderTextModel.getFrom_longitude(), addOrderTextModel.getEnd_shipping_time(), addOrderTextModel.getCoupon_id(), addOrderTextModel.getOrder_description(), addOrderTextModel.getOrder_notes(), addOrderTextModel.getPayment_method(), addOrderTextModel.getHour_arrival_time())
                .enqueue(new Callback<SingleOrderDataModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderDataModel> call, Response<SingleOrderDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            int order_id = response.body().getOrder().getId();
                            OrderModel.Data order = new OrderModel.Data();
                            order.setId(order_id);
                            Intent intent = new Intent(AddOrderTextActivity.this, OrderStepsActivity.class);
                            intent.putExtra("data", order);
                            startActivity(intent);
                            finish();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(AddOrderTextActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddOrderTextActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleOrderDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(AddOrderTextActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddOrderTextActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void sendOrderTextWithImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getUser_id()));
        RequestBody order_type_part = Common.getRequestBodyText(addOrderTextModel.getOrder_type());
        Log.e("order_type", addOrderTextModel.getOrder_type() + "_");

        RequestBody family_id_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getFamily_id()));
        RequestBody google_place_id_part = Common.getRequestBodyText(addOrderTextModel.getGoogle_place_id());
        RequestBody bill_cost_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getBill_cost()));
        RequestBody from_address_part = Common.getRequestBodyText(addOrderTextModel.getFrom_address());
        RequestBody from_lat_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getFrom_latitude()));
        RequestBody from_lng_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getFrom_longitude()));
        RequestBody from_name_part = Common.getRequestBodyText(addOrderTextModel.getFrom_name());
        RequestBody to_address_part = Common.getRequestBodyText(addOrderTextModel.getTo_address());
        RequestBody to_lat_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getTo_latitude()));
        RequestBody to_lng_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getTo_longitude()));
        RequestBody arrival_time_part = Common.getRequestBodyText(String.valueOf(addOrderTextModel.getEnd_shipping_time()));
        RequestBody coupon_id_part = Common.getRequestBodyText(addOrderTextModel.getCoupon_id());
        RequestBody details_part = Common.getRequestBodyText(addOrderTextModel.getOrder_description());
        RequestBody notes_part = Common.getRequestBodyText(addOrderTextModel.getOrder_notes());
        RequestBody payment_part = Common.getRequestBodyText(addOrderTextModel.getPayment_method());
        RequestBody hours_part = Common.getRequestBodyText(addOrderTextModel.getHour_arrival_time());


        Api.getService(Tags.base_url)
                .sendTextOrderWithImage("Bearer " + userModel.getData().getToken(), user_id_part, order_type_part, family_id_part, google_place_id_part, bill_cost_part, to_address_part, to_lat_part, to_lng_part, from_name_part, from_address_part, from_lat_part, from_lng_part, arrival_time_part, coupon_id_part, details_part, payment_part, notes_part, hours_part, getMultiPartImages())
                .enqueue(new Callback<SingleOrderDataModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderDataModel> call, Response<SingleOrderDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Intent intent = new Intent(AddOrderTextActivity.this, ChatActivity.class);
                            intent.putExtra("order_id", response.body().getOrder().getId());
                            startActivity(intent);
                            finish();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(AddOrderTextActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddOrderTextActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleOrderDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(AddOrderTextActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddOrderTextActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private List<MultipartBody.Part> getMultiPartImages() {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : imagesList) {
            if (uri != null) {
                MultipartBody.Part part = Common.getMultiPartImage(this, uri, "images[]");
                parts.add(part);
            }

        }
        return parts;
    }

    public void createDialogAlert() {
        dialog = new AlertDialog.Builder(this)
                .create();

        DialogSelectImage2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image2, null, false);
        binding.llCamera.setOnClickListener(v -> checkCameraPermission());
        binding.llGallery.setOnClickListener(v -> checkReadPermission());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();
            cropImage(uri);


        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                cropImage(uri);

            }


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();

                if (imagesList.size() > 0) {
                    imagesList.add(imagesList.size() - 1, uri);
                    addOrderImagesAdapter.notifyItemInserted(imagesList.size() - 1);

                } else {
                    imagesList.add(uri);
                    imagesList.add(null);
                    addOrderImagesAdapter.notifyItemRangeInserted(0, imagesList.size());
                }


                dialog.dismiss();

                binding.recViewImages.postDelayed(() -> {
                    binding.recViewImages.smoothScrollToPosition(imagesList.size() - 1);
                }, 100);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            //coupon
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            FavoriteLocationModel model = (FavoriteLocationModel) data.getSerializableExtra("data");
            addOrderTextModel.setTo_address(model.getAddress());
            addOrderTextModel.setTo_latitude(model.getLat());
            addOrderTextModel.setTo_longitude(model.getLng());
            if (imagesList.size() > 0) {
                sendOrderTextWithImage();
            } else {
                sendOrderTextWithoutImage();
            }
        }


    }


    private void cropImage(Uri uri) {

        CropImage.activity(uri).setAspectRatio(1, 1).setGuidelines(CropImageView.Guidelines.ON).start(this);

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    public void delete(int adapterPosition) {
        imagesList.remove(adapterPosition);
        if (imagesList.size() == 1) {
            imagesList.clear();
            addOrderImagesAdapter.notifyDataSetChanged();
        } else {
            addOrderImagesAdapter.notifyItemRemoved(adapterPosition);
        }
    }
}