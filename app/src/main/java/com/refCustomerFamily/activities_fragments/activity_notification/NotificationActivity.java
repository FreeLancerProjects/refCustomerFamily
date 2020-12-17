package com.refCustomerFamily.activities_fragments.activity_notification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.refCustomerFamily.R;
import com.refCustomerFamily.adapters.NotificationAdapter;
import com.refCustomerFamily.databinding.ActivityNotificationBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.NotificationModel;
import com.refCustomerFamily.models.OfferModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.share.Common;
import com.refCustomerFamily.tags.Tags;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private String lang;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel.Data> dataList;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);


        initView();
    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        dataList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        notificationAdapter = new NotificationAdapter(dataList, this);
        binding.recViewNotifications.setAdapter(notificationAdapter);
        binding.recViewNotifications.setLayoutManager(new LinearLayoutManager(this));

        getNotification();
        binding.back.setOnClickListener(view -> {

            back();
        });
    }

    private void getNotification() {
        dataList.clear();
        notificationAdapter.notifyDataSetChanged();
        binding.progBarNotification.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getNotification("Bearer " + userModel.getData().getToken(), userModel.getData().getId()).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.progBarNotification.setVisibility(View.GONE);
                    dataList.addAll(response.body().getData());
                    notificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                binding.progBarNotification.setVisibility(View.GONE);
                Log.e("Notification", t.getMessage());
            }
        });


    }

    public void CreateAddRateAlertDialog(final NotificationModel.Data notificationModel) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rate, null);
        ImageView img_close = view.findViewById(R.id.img_close);
        CircleImageView image = view.findViewById(R.id.image);
        TextView tv_name = view.findViewById(R.id.tv_name);
        image.setVisibility(View.GONE);

        final EditText edt_comment = view.findViewById(R.id.edt_comment);
        final TextView tv_rate = view.findViewById(R.id.tv_rate);
        final Button btn_rate = view.findViewById(R.id.btn_rate);
        final SimpleRatingBar rate = view.findViewById(R.id.simplarate);
        //  Picasso.get().load(Uri.parse(Tags.IMAGE_URL + notificationModel.getFrom_user_image())).fit().into(image);
        //tv_name.setText(notificationModel.getFrom_user_full_name());
        if (notificationModel.getAction_type().equals("rate_driver")) {
            tv_name.setText(getResources().getString(R.string.driver));
        } else {
            tv_name.setText(getResources().getString(R.string.family));

        }

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edt_comment.getText().toString().trim();

                AddRate(dialog, notificationModel, rate.getRating(), comment);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        // dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void back() {
        finish();
    }

    private void AddRate(final AlertDialog alertDialog, NotificationModel.Data notificationModel, double rate, String comment) {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Log.e("ldkdkkd", notificationModel.getTo_user_id() + "  " + notificationModel.getFrom_user_id());
        Api.getService(Tags.base_url)
                .addRate("Bearer " + userModel.getData().getToken(), notificationModel.getTo_user_id() + "", notificationModel.getFrom_user_id() + "", notificationModel.getOrder_id() + "", rate, notificationModel.getId() + "", comment)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            alertDialog.dismiss();
                            dialog.dismiss();
                            getNotification();
                        } else {
                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                            Toast.makeText(NotificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                            Toast.makeText(NotificationActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } catch (Exception re) {
                        }
                    }
                });


    }

}