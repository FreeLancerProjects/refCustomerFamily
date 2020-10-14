package com.refCustomerFamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.activities_fragments.activity_orderdetail.OrderDetailActivity;
import com.refCustomerFamily.databinding.ItemMarketBinding;
import com.refCustomerFamily.databinding.ItemOrderBinding;
import com.refCustomerFamily.databinding.ItemOrderGoogleBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.OrderAdapterVH> {

    private List<OrderModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public MarketAdapter(Context context) {
        this.context = context;
    }

    public MarketAdapter(List<OrderModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        preferences = Preferences.newInstance();
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");

    }

    @NonNull
    @Override
    public OrderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderGoogleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_google, parent, false);
        return new OrderAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapterVH holder, int position) {
        holder.binding.setLang(lang);
        holder.binding.setModel(orderlist.get(position));

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, OrderStepsActivity.class);
            intent.putExtra("data", orderlist.get(position));
            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class OrderAdapterVH extends RecyclerView.ViewHolder {
        public ItemOrderGoogleBinding binding;

        public OrderAdapterVH(@NonNull ItemOrderGoogleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
