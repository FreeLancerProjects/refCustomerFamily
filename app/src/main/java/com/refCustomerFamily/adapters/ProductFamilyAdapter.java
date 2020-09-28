package com.refCustomerFamily.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_family.FamilyActivity;
import com.refCustomerFamily.databinding.ItemCategoryBinding;
import com.refCustomerFamily.databinding.ItemProductFamilyBinding;
import com.refCustomerFamily.models.MarketCatogryModel;
import com.refCustomerFamily.models.ProductFamilyModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductFamilyAdapter extends RecyclerView.Adapter<ProductFamilyAdapter.ProductFamilyAdapterVH> {

    private List<ProductFamilyModel> productFamilylist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;

    int i = -1;

    public ProductFamilyAdapter(Context context) {
        this.context = context;
    }

    public ProductFamilyAdapter(List<ProductFamilyModel> productFamilylist, Context context) {
        this.productFamilylist = productFamilylist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public ProductFamilyAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductFamilyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_product_family, parent, false);
        return new ProductFamilyAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductFamilyAdapterVH holder, int position) {


        holder.binding.setLang(lang);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, FamilyActivity.class);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class ProductFamilyAdapterVH extends RecyclerView.ViewHolder {
        public ItemProductFamilyBinding binding;

        public ProductFamilyAdapterVH(@NonNull ItemProductFamilyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
