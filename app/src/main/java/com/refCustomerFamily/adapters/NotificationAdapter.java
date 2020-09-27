package com.refCustomerFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ItemNotificationBinding;
import com.refCustomerFamily.models.NotificationModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterVH> {

    private List<NotificationModel> notificationList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public NotificationAdapter(Context context) {
        this.context = context;
    }

    public NotificationAdapter(List<NotificationModel> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public NotificationAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_notification, parent, false);
        return new NotificationAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapterVH holder, int position) {
        holder.binding.setLang(lang);
//        holder.binding.setModel(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class NotificationAdapterVH extends RecyclerView.ViewHolder {
        public ItemNotificationBinding binding;

        public NotificationAdapterVH(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
