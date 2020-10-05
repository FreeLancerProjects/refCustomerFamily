package com.refCustomerFamily.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ItemFamilyOrderBinding;
import com.refCustomerFamily.models.FamilyOrderModel;
import com.refCustomerFamily.models.ProductModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class FamilyProductAdapter extends RecyclerView.Adapter<FamilyProductAdapter.FamilyOrderAdapterVH> {

    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;



    public FamilyProductAdapter(List<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public FamilyOrderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFamilyOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_family_order, parent, false);
        return new FamilyOrderAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyOrderAdapterVH holder, int position) {

        ProductModel productModel = list.get(position);
        holder.binding.setModel(productModel);
        holder.binding.setLang(lang);
        holder.binding.tvOldPrice.setText(String.format(Locale.ENGLISH,"%s %s",productModel.getOld_price(),context.getString(R.string.sar)));

        if (!productModel.getHave_offer().equals("without_offer")){
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.itemView.setOnClickListener(view -> {



        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FamilyOrderAdapterVH extends RecyclerView.ViewHolder {
        public ItemFamilyOrderBinding binding;

        public FamilyOrderAdapterVH(@NonNull ItemFamilyOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
