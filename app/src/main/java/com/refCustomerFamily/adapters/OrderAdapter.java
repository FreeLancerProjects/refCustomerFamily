package com.refCustomerFamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_orderdetail.OrderDetailActivity;
import com.refCustomerFamily.databinding.ItemMainOffersBinding;
import com.refCustomerFamily.databinding.ItemOrderBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderAdapterVH> {

    private List<OrderModel> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;
    private OrderDetailAdapter orderDetailAdapter;


    public OrderAdapter(Context context) {
        this.context = context;
    }

    public OrderAdapter(List<OrderModel> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OrderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order, parent, false);
        return new OrderAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapterVH holder, int position) {
        holder.binding.setLang(Language_Helper.getLanguage(context));
        orderDetailAdapter = new OrderDetailAdapter(context);
        holder.binding.recViewOrderDetail.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recViewOrderDetail.setAdapter(orderDetailAdapter );

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, OrderDetailActivity.class);
            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class OrderAdapterVH extends RecyclerView.ViewHolder {
        public ItemOrderBinding binding;

        public OrderAdapterVH(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
