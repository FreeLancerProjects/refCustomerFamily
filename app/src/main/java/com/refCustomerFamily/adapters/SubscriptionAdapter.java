package com.refCustomerFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ItemNotificationBinding;
import com.refCustomerFamily.databinding.ItemSubscritionBinding;
import com.refCustomerFamily.models.SubscriptionModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionAdapterVH> {

    private List<SubscriptionModel> subscriptionList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;

    public SubscriptionAdapter(Context context) {
        this.context = context;
    }

    public SubscriptionAdapter(List<SubscriptionModel> subscriptionList, Context context) {
        this.subscriptionList = subscriptionList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public SubscriptionAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscritionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_subscrition, parent, false);
        return new SubscriptionAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapterVH holder, int position) {

        holder.binding.setLang(lang);
//        holder.binding.setModel(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class SubscriptionAdapterVH extends RecyclerView.ViewHolder {
        public ItemSubscritionBinding binding;

        public SubscriptionAdapterVH(@NonNull ItemSubscritionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

}
