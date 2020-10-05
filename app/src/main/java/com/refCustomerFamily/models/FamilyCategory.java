package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class FamilyCategory implements Serializable {
    private int id;
    private String title;
    private String desc;
    private String image;
    private int family_id;
    private int category_id;
    private List<ProductModel> products;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public int getFamily_id() {
        return family_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public List<ProductModel> getProducts() {
        return products;
    }
}
