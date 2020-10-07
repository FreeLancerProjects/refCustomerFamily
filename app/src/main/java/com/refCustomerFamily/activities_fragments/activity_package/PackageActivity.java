package com.refCustomerFamily.activities_fragments.activity_package;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_family.FamilyActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_add_coupon.AddCouponActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_map_search.MapSearchActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters.AddOrderImagesAdapter;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.AddOrderTextModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.FavoriteLocationModel;
import com.refCustomerFamily.adapters.FamilyAdapter;
import com.refCustomerFamily.adapters.ProductFamilyCategoryAdapter;
import com.refCustomerFamily.databinding.ActivityPackageBinding;
import com.refCustomerFamily.databinding.ActivityProductFamilyBinding;
import com.refCustomerFamily.databinding.DialogSelectImage2Binding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.models.SubCategoryFamilyModel;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageActivity extends AppCompatActivity {


    private ActivityPackageBinding binding;
    private String lang;
    private long selected_time = 0;
    private String[] timesList;
    private FavoriteLocationModel model;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private List<Uri> imagesList;
    private AlertDialog dialog;
    private AddOrderImagesAdapter addOrderImagesAdapter;
    private AddOrderTextModel addOrderTextModel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package);

        initView();
        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();

        if (getIntent().getSerializableExtra("data") != null) {
            model = (FavoriteLocationModel) intent.getSerializableExtra("data");
            Log.e("mmmmmmm",model.getAddress()+"--  "+model.getLat()+"  ---"+model.getName());
            binding.tvAddress1.setText(model.getAddress());
            binding.tvAddress2.setText(model.getAddress());

        }
    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        addOrderTextModel = new AddOrderTextModel();
        imagesList = new ArrayList<>();
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        addOrderImagesAdapter = new AddOrderImagesAdapter(imagesList,this);
        binding.recViewImages.setAdapter(addOrderImagesAdapter);


        timesList = new String[]{getString(R.string.hour1),
                getString(R.string.hour2),
                getString(R.string.hour3),
                getString(R.string.day1),
                getString(R.string.day2),
                getString(R.string.day3)

        };
        binding.imageCamera.setOnClickListener(v -> createDialogAlert());

        binding.liPlaceOfDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapSearchActivity.class);
            intent.putExtra("type", 1);
            startActivityForResult(intent, 200);
        });
        binding.liDeliveryPlace.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapSearchActivity.class);
            intent.putExtra("type", 1);
            startActivityForResult(intent, 200);
        });
        binding.liDeliveryTime.setOnClickListener(v -> {
            CreateTimeDialog();

        });
        binding.tvAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PackageActivity.this, AddCouponActivity.class);
                startActivity(intent);
            }
        });
        binding.close.setOnClickListener(v -> {super.onBackPressed();});
        addOrderTextModel.setOrder_type("package");
        addOrderTextModel.setMarket_id(0);

        //addOrderTextModel.setUser_id(userModel.getUser().getId());
//        addOrderTextModel.setPlace_id(placeModel.getPlace_id());
//        addOrderTextModel.setPlace_name(placeModel.getName());
//        addOrderTextModel.setPlace_address(placeModel.getVicinity());
//        addOrderTextModel.setPlace_lat(placeModel.getGeometry().getLocation().getLat());
//        addOrderTextModel.setPlace_lng(placeModel.getGeometry().getLocation().getLng());
        addOrderTextModel.setPayment("cash");
        addOrderTextModel.setCoupon_id("0");
        addOrderTextModel.setComments("");
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
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = timesList[numberPicker.getValue()];
                binding.tvTime.setText(val);
                setTime(numberPicker.getValue());
                dialog.dismiss();
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }
    private void setTime(int value) {
        Calendar calendar = Calendar.getInstance(new Locale(lang));
        switch (value) {
            case 0:
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case 1:
                calendar.add(Calendar.HOUR_OF_DAY, 2);

                break;
            case 2:
                calendar.add(Calendar.HOUR_OF_DAY, 3);

                break;
            case 3:
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                break;
            case 4:
                calendar.add(Calendar.DAY_OF_MONTH, 2);

                break;
            case 5:
                calendar.add(Calendar.DAY_OF_MONTH, 3);

                break;
        }

        selected_time = calendar.getTimeInMillis() / 1000;
    }

    public void createDialogAlert()
    {
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
    public void checkReadPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }
    public void checkCameraPermission()
    {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }
    private void SelectImage(int req)
    {

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


        }
        else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                cropImage(uri);

            }


        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();

                if (imagesList.size()>0){
                    imagesList.add(imagesList.size()-1,uri);
                    addOrderImagesAdapter.notifyItemInserted(imagesList.size()-1);

                }else {
                    imagesList.add(uri);
                    imagesList.add(null);
                    addOrderImagesAdapter.notifyItemRangeInserted(0,imagesList.size());
                }


                dialog.dismiss();

                binding.recViewImages.postDelayed(()->{
                    binding.recViewImages.smoothScrollToPosition(imagesList.size()-1);
                },100);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        else if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null){
            //coupon
        }else if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null){
            FavoriteLocationModel model = (FavoriteLocationModel) data.getSerializableExtra("data");
            addOrderTextModel.setTo_address(model.getAddress());
            addOrderTextModel.setTo_lat(model.getLat());
            addOrderTextModel.setTo_lng(model.getLng());
            if (imagesList.size()>0){
                //sendOrderTextWithImage();
            }else {
               //sendOrderTextWithoutImage();
            }
        }



    }




    private void cropImage(Uri uri) {

        CropImage.activity(uri).setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(this);

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    public void delete(int adapterPosition) {
        imagesList.remove(adapterPosition);
        if (imagesList.size()==1){
            imagesList.clear();
            addOrderImagesAdapter.notifyDataSetChanged();
        }else {
            addOrderImagesAdapter.notifyItemRemoved(adapterPosition);
        }
    }
}