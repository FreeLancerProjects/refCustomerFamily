package com.refCustomerFamily.models;

import java.io.Serializable;

public class CountryModel implements Serializable {

    private String code;
    private String id;


    CountryModel() {
    }

    public CountryModel(String code, String id) {
        this.code = code;
        this.id = id;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
