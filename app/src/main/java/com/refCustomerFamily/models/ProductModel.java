package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductModel implements Serializable {

    private int id;
    private int family_id;
    private int sub_category_id;
    private int price;
    private int old_price;
    private int offer_value;
    private float rating_value;
    private String title = "";
    private String desc = "";
    private String main_image = "";
    private String offer_type = "";
    private List<ImageModel> images = new ArrayList<>();
    private String have_offer = "";
    private String offer_started_at = "";
    private String offer_finished_at = "";
    private String is_shown;
    private double price;
    private double old_price;
    private String have_offer;
    private String offer_type;
    private double offer_value;
    private double rating_value;
    private int count = 1;


    public static class ImageModel implements Serializable {
        private int id;
        private String image;
        private int product_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }
    }

    public int getId() {
        return id;
    }

    public int getFamily_id() {
        return family_id;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public int getPrice() {
        return price;
    }

    public int getOld_price() {
        return old_price;
    }

    public int getOffer_value() {
        return offer_value;
    }

    public float getRating_value() {
        return rating_value;
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

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public String getOffer_started_at() {
        return offer_started_at;
    }

    public String getOffer_finished_at() {
        return offer_finished_at;
    }

    public String getIs_shown() {
        return is_shown;
    }

    public List<ImageModel> getImages() {
        return images;
    }



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
