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
import com.refCustomerFamily.databinding.ItemMealItemBinding;
import com.refCustomerFamily.databinding.ItemProductFamilyBinding;
import com.refCustomerFamily.models.MealModel;
import com.refCustomerFamily.models.ProductFamilyModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealAdapterVH> {

    private List<MealModel> mealList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;

    int i = -1;

    public MealAdapter(Context context) {
        this.context = context;
    }

    public MealAdapter(List<MealModel> mealList, Context context) {
        this.mealList = mealList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MealAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_meal_item, parent, false);
        return new MealAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapterVH holder, int position) {


        holder.binding.setLang(lang);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class MealAdapterVH extends RecyclerView.ViewHolder {
        public ItemMealItemBinding binding;

        public MealAdapterVH(@NonNull ItemMealItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
