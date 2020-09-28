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
import com.refCustomerFamily.databinding.ItemFamilyOrderBinding;
import com.refCustomerFamily.databinding.ItemProductFamilyBinding;
import com.refCustomerFamily.models.FamilyOrderModel;
import com.refCustomerFamily.models.ProductFamilyModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class FamilyOrderAdapter extends RecyclerView.Adapter<FamilyOrderAdapter.FamilyOrderAdapterVH> {

    private List<FamilyOrderModel> familyOrderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;

    int i = -1;

    public FamilyOrderAdapter(Context context) {
        this.context = context;
    }

    public FamilyOrderAdapter(List<FamilyOrderModel> familyOrderlist, Context context) {
        this.familyOrderlist = familyOrderlist;
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

    public class FamilyOrderAdapterVH extends RecyclerView.ViewHolder {
        public ItemFamilyOrderBinding binding;

        public FamilyOrderAdapterVH(@NonNull ItemFamilyOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
