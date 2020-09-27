package com.refCustomerFamily.models;

import android.content.Context;
import android.net.Uri;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.refCustomerFamily.BR;
import com.refCustomerFamily.R;


public class SignUpModel extends BaseObservable {
    private Uri logo;
    private String name;
    private String email;
    private String address;
    private String phone_code;
    private String phone;

    
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();


    public boolean isDataValid(Context context)
    {
        if (!name.trim().isEmpty()&&
                !email.trim().isEmpty()&&
                !address.trim().isEmpty()&&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        ){
            error_name.set(null);
            error_email.set(null);
            error_address.set(null);

            return true;
        }else
            {
                if (name.trim().isEmpty())
                {
                    error_name.set(context.getString(R.string.field_required));

                }else
                    {
                        error_name.set(null);

                    }

                if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches())
                {
                    error_email.set(context.getString(R.string.inv_email));

                }else {
                    error_email.set(null);

                }
                if (address.trim().isEmpty())
                {
                    error_address.set(context.getString(R.string.field_required));

                }else
                {
                    error_address.set(null);

                }
                return false;
            }
    }
    public SignUpModel() {
        setName("");
        setEmail("");
        setLogo(null);

    }



    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }


    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }


    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }


    public Uri getLogo() {
        return logo;
    }

    public void setLogo(Uri logo) {
        this.logo = logo;
    }


    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

