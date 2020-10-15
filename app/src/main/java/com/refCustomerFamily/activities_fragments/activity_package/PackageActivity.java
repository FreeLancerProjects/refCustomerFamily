package com.refCustomerFamily.activities_fragments.activity_package;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_chat.ChatActivity;
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_add_coupon.AddCouponActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_map_search.MapSearchActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters.AddOrderImagesAdapter;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.AddOrderTextModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.FavoriteLocationModel;
import com.refCustomerFamily.databinding.ActivityPackageBinding;
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

public class PackageActivity extends AppCompatActivity {
    private ActivityPackageBinding binding;
    private String lang;
    private String[] timesList;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private List<Uri> imagesList;
    private AlertDialog dialog;
    private AddOrderImagesAdapter addOrderImagesAdapter;
    private AddOrderTextModel addOrderTextModel;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package);
        initView();

    }


    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        addOrderTextModel = new AddOrderTextModel();
        imagesList = new ArrayList<>();
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addOrderImagesAdapter = new AddOrderImagesAdapter(imagesList, this);
        binding.recViewImages.setAdapter(addOrderImagesAdapter);


        timesList = new String[]{getString(R.string.hour1),
                getString(R.string.hour2),
                getString(R.string.hour3),
                getString(R.string.day1),
                getString(R.string.day2),
                getString(R.string.day3)

        };

        binding.tvTime.setText(timesList[0]);
        binding.imageCamera.setOnClickListener(v -> createDialogAlert());

        binding.llPickUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapSearchActivity.class);
            intent.putExtra("type", 1);
            startActivityForResult(intent, 200);
        });
        binding.llDropOff.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapSearchActivity.class);
            intent.putExtra("type", 1);
            startActivityForResult(intent, 300);
        });
        binding.liDeliveryTime.setOnClickListener(v -> {
            CreateTimeDialog();

        });
        binding.tvAddCoupon.setOnClickListener(view -> {
            Intent intent = new Intent(PackageActivity.this, AddCouponActivity.class);
            startActivity(intent);
        });
        binding.close.setOnClickListener(v -> {
            super.onBackPressed();
        });


        addOrderTextModel.setUser_id(userModel.getData().getId());
        addOrderTextModel.setFamily_id(0);
        addOrderTextModel.setOrder_type("package");
        addOrderTextModel.setGoogle_place_id("");
        addOrderTextModel.setBill_cost(0);
        addOrderTextModel.setCoupon_id("0");
        addOrderTextModel.setPayment_method("cash");
        addOrderTextModel.setHour_arrival_time("1");
        addOrderTextModel.setOrder_notes("");
        Calendar calendar = Calendar.getInstance(new Locale(lang));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String timeArrival = dateFormat.format(new Date(calendar.getTimeInMillis()));
        addOrderTextModel.setEnd_shipping_time(timeArrival);


        binding.btnSend.setOnClickListener(view -> {

            String order_text = binding.edtOrder.getText().toString();
            if (!order_text.isEmpty() && !addOrderTextModel.getFrom_address().isEmpty() && !addOrderTextModel.getTo_address().isEmpty()) {
                Common.CloseKeyBoard(this, binding.edtOrder);
                binding.edtOrder.setError(null);
                binding.tvAddress1.setError(null);
                binding.tvAddress2.setError(null);
                addOrderTextModel.setOrder_description(order_text);
                if (imagesList.size() > 0) {
                    sendOrderTextWithImage();
                } else {
                    sendOrderTextWithoutImage();
                }
            } else {
                if (order_text.isEmpty()) {
                    binding.edtOrder.setError(getString(R.string.field_req));

                } else {
                    binding.edtOrder.setError(null);

                }

                if (addOrderTextModel.getFrom_address().isEmpty()) {
                    binding.tvAddress1.setError(getString(R.string.field_req));

                } else {
                    binding.tvAddress1.setError(null);

                }

                if (addOrderTextModel.getTo_address().isEmpty()) {
                    binding.tvAddress2.setError(getString(R.string.field_req));

                } else {
                    binding.tvAddress2.setError(null);

                }

            }
        });


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
                            Intent intent = new Intent(PackageActivity.this, OrderStepsActivity.class);
                            intent.putExtra("data", order);
                            startActivity(intent);
                            finish();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(PackageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PackageActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(PackageActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PackageActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(PackageActivity.this, ChatActivity.class);
                            intent.putExtra("order_id", response.body().getOrder().getId());
                            startActivity(intent);
                            finish();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(PackageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PackageActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(PackageActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PackageActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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

    private void CreateTimeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delivery_time, null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(timesList.length - 1);
        numberPicker.setDisplayedValues(timesList);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(1);
        btn_select.setOnClickListener(v -> {
            String val = timesList[numberPicker.getValue()];
            binding.tvTime.setText(val);
            setTime(numberPicker.getValue());
            dialog.dismiss();
        });


        btn_cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }

    private void setTime(int value) {
        Calendar calendar = Calendar.getInstance(new Locale(lang));
        switch (value) {
            case 0:
                addOrderTextModel.setHour_arrival_time("1");
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case 1:
                addOrderTextModel.setHour_arrival_time("2");
                calendar.add(Calendar.HOUR_OF_DAY, 2);

                break;
            case 2:
                addOrderTextModel.setHour_arrival_time("3");
                calendar.add(Calendar.HOUR_OF_DAY, 3);

                break;
            case 3:
                addOrderTextModel.setHour_arrival_time("4");
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                break;
            case 4:
                addOrderTextModel.setHour_arrival_time("5");
                calendar.add(Calendar.DAY_OF_MONTH, 2);

                break;
            case 5:
                addOrderTextModel.setHour_arrival_time("6");
                calendar.add(Calendar.DAY_OF_MONTH, 3);

                break;

        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String timeArrival = dateFormat.format(new Date(calendar.getTimeInMillis()));
        addOrderTextModel.setEnd_shipping_time(timeArrival);

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
            addOrderTextModel.setFrom_name(model.getAddress());
            addOrderTextModel.setFrom_address(model.getAddress());
            addOrderTextModel.setFrom_latitude(model.getLat());
            addOrderTextModel.setFrom_longitude(model.getLng());
            binding.tvAddress1.setText(model.getAddress());
        } else if (requestCode == 300 && resultCode == Activity.RESULT_OK && data != null) {
            FavoriteLocationModel model = (FavoriteLocationModel) data.getSerializableExtra("data");
            addOrderTextModel.setTo_address(model.getAddress());
            addOrderTextModel.setTo_latitude(model.getLat());
            addOrderTextModel.setTo_longitude(model.getLng());
            binding.tvAddress2.setText(model.getAddress());

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