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
import com.refCustomerFamily.activities_fragments.activity_product_family.ProductFamilyActivity;
import com.refCustomerFamily.databinding.ItemProductFamilyBinding;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ProductFamilyAdapterVH> {

    private List<FamilyModel> familyList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private ProductFamilyActivity activity;
    private String categoryTitle;

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public FamilyAdapter(List<FamilyModel> familyList, Context context) {
        this.familyList = familyList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ProductFamilyActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public ProductFamilyAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductFamilyBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_product_family, parent, false);
        return new ProductFamilyAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductFamilyAdapterVH holder, int position) {

        holder.binding.setModel(familyList.get(position));
        holder.binding.setCategoryTitle(categoryTitle);
        holder.binding.setLang(lang);
        holder.itemView.setOnClickListener(view -> {
            FamilyModel familyModel = familyList.get(holder.getAdapterPosition());
            activity.navigateToFamilyActivity(familyModel);


        });


    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public class ProductFamilyAdapterVH extends RecyclerView.ViewHolder {
        public ItemProductFamilyBinding binding;

        public ProductFamilyAdapterVH(@NonNull ItemProductFamilyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
