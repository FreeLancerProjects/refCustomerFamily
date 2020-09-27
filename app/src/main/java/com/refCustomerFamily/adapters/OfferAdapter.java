package com.refCustomerFamily.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.DialogUpdatePriceBinding;
import com.refCustomerFamily.databinding.ItemMainOffersBinding;
import com.refCustomerFamily.databinding.ItemOffersBinding;
import com.refCustomerFamily.models.OfferModel;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferAdapterVH> {

    private List<OfferModel> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public OfferAdapter(Context context) {
        this.context = context;
    }

    public OfferAdapter(List<OfferModel> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public OfferAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOffersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_offers, parent, false);
        return new OfferAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapterVH holder, int position) {

        holder.binding.updateBtn.setOnClickListener(view -> {

            Dialog dialog = new Dialog(context);
            DialogUpdatePriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_update_price, null, false);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_bg_white_1);
            dialog.setContentView(binding.getRoot());
            dialog.show();
            dialog.setCancelable(true);


        });

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class OfferAdapterVH extends RecyclerView.ViewHolder {
        public ItemOffersBinding binding;

        public OfferAdapterVH(@NonNull ItemOffersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
