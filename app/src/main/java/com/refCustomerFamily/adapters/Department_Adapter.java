package com.refCustomerFamily.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.refCustomerFamily.R;
import com.refCustomerFamily.databinding.DepartmentRowBinding;
import com.refCustomerFamily.databinding.PostRowBinding;
import com.refCustomerFamily.models.MarketCatogryModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Department_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MarketCatogryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = -1;
    private Fragment fragment;

    public Department_Adapter(List<MarketCatogryModel.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DepartmentRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.department_row, parent, false);
        return new EventsHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        EventsHolder msgRightHolder = (EventsHolder) holder;


    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }


    public class EventsHolder extends RecyclerView.ViewHolder {
        public DepartmentRowBinding binding;

        public EventsHolder(@NonNull DepartmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}