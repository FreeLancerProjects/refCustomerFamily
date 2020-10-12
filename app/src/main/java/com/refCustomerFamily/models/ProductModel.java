package com.refCustomerFamily.models;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private int id;
    private String title;
    private String desc;
    private String main_image;
    private int family_id;
    private int sub_category_id;
    private double price;
    private double old_price;
    private String have_offer;
    private String offer_type;
    private double offer_value;
    private double rating_value;
    private int count = 1;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getMain_image() {
        return main_image;
    }

    public int getFamily_id() {
        return family_id;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public double getPrice() {
        return price;
    }

    public double getOld_price() {
        return old_price;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public double getOffer_value() {
        return offer_value;
    }

    public double getRating_value() {
        return rating_value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
