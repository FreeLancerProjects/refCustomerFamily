package com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_google_stores.GoogleStoresActivity;
import com.refCustomerFamily.databinding.CategoryRowBinding;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.CategoryModel;
import com.refCustomerFamily.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

public class PlaceCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private GoogleStoresActivity activity;

    public PlaceCategoryAdapter(List<CategoryModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (GoogleStoresActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        CategoryModel categoryModel = list.get(position);
        myHolder.binding.setModel(categoryModel);
        myHolder.binding.setLang(lang);
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+categoryModel.getImage())).fit().into(myHolder.binding.image);
        myHolder.itemView.setOnClickListener(v -> {
            CategoryModel categoryModel2 = list.get(holder.getAdapterPosition());

            activity.setCategoryData(categoryModel2);
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CategoryRowBinding binding;

        public MyHolder(@NonNull CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
