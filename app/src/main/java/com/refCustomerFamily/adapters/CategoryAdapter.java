package com.refCustomerFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.ItemCategoryBinding;
import com.refCustomerFamily.models.MarketCatogryModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterVH> {

    private List<MarketCatogryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;

    int i = -1;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public CategoryAdapter(List<MarketCatogryModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public CategoryAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_category, parent, false);
        return new CategoryAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterVH holder, int position) {

        holder.binding.setLang(lang);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = position;
                notifyDataSetChanged();

            }
        });

        if (position == 0) {
            holder.binding.name.setBackground(context.getResources().getDrawable(R.drawable.main_category_bg_1));
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.white));

        }
        if (i == position) {
            holder.binding.name.setBackground(context.getResources().getDrawable(R.drawable.main_category_bg_1));
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.binding.name.setBackground(context.getResources().getDrawable(R.drawable.main_category_bg));
            holder.binding.name.setTextColor(context.getResources().getColor(R.color.gray12));
        }


    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class CategoryAdapterVH extends RecyclerView.ViewHolder {
        public ItemCategoryBinding binding;

        public CategoryAdapterVH(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
