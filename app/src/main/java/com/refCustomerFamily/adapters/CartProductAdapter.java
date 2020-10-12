package com.refCustomerFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_add_order_product.AddOrderProductActivity;
import com.refCustomerFamily.databinding.CartProductRowBinding;
import com.refCustomerFamily.models.ProductModel;

import java.util.List;

import io.paperdb.Paper;

public class CartProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private AddOrderProductActivity activity;

    public CartProductAdapter(List<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (AddOrderProductActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CartProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_product_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        ProductModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.tvIncrease.setOnClickListener(v -> {
            int pos = myHolder.getAdapterPosition();
            ProductModel productModel = list.get(pos);
            int count = productModel.getCount()+1;
            productModel.setCount(count);
            myHolder.binding.setModel(productModel);
            activity.updateItemCount(productModel,pos);
            notifyItemChanged(pos);

        });

        myHolder.binding.tvDecrease.setOnClickListener(v -> {
            int pos = myHolder.getAdapterPosition();
            ProductModel productModel = list.get(pos);
            int count = productModel.getCount();
            if (count>1){
                count -=1;
                productModel.setCount(count);
                myHolder.binding.setModel(productModel);
                activity.updateItemCount(productModel,pos);
                notifyItemChanged(pos);

            }

        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CartProductRowBinding binding;

        public MyHolder(@NonNull CartProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
