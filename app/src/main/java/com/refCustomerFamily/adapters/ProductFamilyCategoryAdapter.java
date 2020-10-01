package com.refCustomerFamily.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.activity_product_family.ProductFamilyActivity;
import com.refCustomerFamily.databinding.ItemCategoryBinding;
import com.refCustomerFamily.databinding.ItemProductFamilyCategoryBinding;
import com.refCustomerFamily.models.MarketCatogryModel;
import com.refCustomerFamily.models.SubCategoryFamilyModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductFamilyCategoryAdapter extends RecyclerView.Adapter<ProductFamilyCategoryAdapter.ProductFamilyCategoryAdapterVH> {

    private List<SubCategoryFamilyModel.Data.Category> categoryList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    int i = -1;

    public ProductFamilyCategoryAdapter(Context context) {
        this.context = context;
    }


    public ProductFamilyCategoryAdapter(List<SubCategoryFamilyModel.Data.Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public ProductFamilyCategoryAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductFamilyCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_product_family_category, parent, false);
        return new ProductFamilyCategoryAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductFamilyCategoryAdapterVH holder, int position) {

        holder.binding.setLang(lang);
        holder.binding.setModel(categoryList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = position;
                notifyDataSetChanged();

            }
        });

        if (position == 0) {
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        }
        if (i == position) {
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            if (context instanceof ProductFamilyActivity) {
                ProductFamilyActivity productFamilyActivity = (ProductFamilyActivity) context;
                ((ProductFamilyActivity) context).showFamilies(position);



            }


        } else {
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.gray6));
        }


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ProductFamilyCategoryAdapterVH extends RecyclerView.ViewHolder {
        public ItemProductFamilyCategoryBinding binding;

        public ProductFamilyCategoryAdapterVH(@NonNull ItemProductFamilyCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
