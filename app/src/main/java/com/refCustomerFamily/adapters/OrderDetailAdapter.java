package com.refCustomerFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ItemOrderBinding;
import com.refCustomerFamily.databinding.ItemOrderDetailBinding;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.OrderModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailAdapterVH> {

    private List<OrderModel> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public OrderDetailAdapter(Context context) {
        this.context = context;
    }

    public OrderDetailAdapter(List<OrderModel> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OrderDetailAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_detail, parent, false);
        return new OrderDetailAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapterVH holder, int position) {
        holder.binding.setLang(Language_Helper.getLanguage(context));



    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class OrderDetailAdapterVH extends RecyclerView.ViewHolder {
        public ItemOrderDetailBinding binding;

        public OrderDetailAdapterVH(@NonNull ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
