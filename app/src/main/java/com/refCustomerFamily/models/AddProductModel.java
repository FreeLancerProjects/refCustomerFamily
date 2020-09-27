package com.refCustomerFamily.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.refCustomerFamily.BR;
import com.refCustomerFamily.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddProductModel extends BaseObservable implements Serializable {
    private List<String> imagesUri;
    private String productName;
    private String productDetails;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_details = new ObservableField<>();

    public AddProductModel() {
        productName = "";
        productDetails = "";
        imagesUri = new ArrayList<>();
    }

    public boolean step1(Context context){

        if (!productName.trim().isEmpty()&&!productDetails.trim().isEmpty()){
            error_name.set(null);
            error_details.set(null);
            return true;
        }else {
            if (productName.trim().isEmpty()){
                error_name.set(context.getString(R.string.field_req));
            }

            if (productDetails.trim().isEmpty()){
                error_details.set(context.getString(R.string.field_req));
            }
            return false;
        }

    }



    public List<String> getImagesUri() {
        return imagesUri;
    }

    public void setImagesUri(List<String> imagesUri) {
        this.imagesUri = imagesUri;
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        notifyPropertyChanged(BR.productName);
    }

    @Bindable
    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
        notifyPropertyChanged(BR.productDetails);

    }
}
