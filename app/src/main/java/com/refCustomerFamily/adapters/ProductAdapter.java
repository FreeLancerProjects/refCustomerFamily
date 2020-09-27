package com.refCustomerFamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_update_product.UpdateProductActivity;
import com.refCustomerFamily.databinding.ItemMainOffersBinding;
import com.refCustomerFamily.models.MarketCatogryModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.OfferAdapterVH> {

    private List<MarketCatogryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public ProductAdapter(Context context) {
        this.context = context;
    }

    public ProductAdapter(List<MarketCatogryModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OfferAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMainOffersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_main_offers, parent, false);
        return new OfferAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapterVH holder, int position) {


        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, UpdateProductActivity.class);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class OfferAdapterVH extends RecyclerView.ViewHolder {
        public ItemMainOffersBinding binding;

        public OfferAdapterVH(@NonNull ItemMainOffersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
