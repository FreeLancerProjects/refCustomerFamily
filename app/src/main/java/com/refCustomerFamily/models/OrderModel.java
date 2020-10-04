package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private int id;
    private String order_code;
    private String status;
    private String order_type;
    private String client_id;
    private String driver_id;
    private String market_id;
    private String google_place_id;
    private String bill_cost;
    private String bill_step;
    private String payment_method;
    private String bill_image;
    private String from_name;
    private String from_address;
    private String from_latitude;
    private String from_longitude;
    private String to_name;
    private String to_address;
    private String to_latitude;
    private String to_longitude;
    private String order_description;
    private String coupon;


    private UserModel.User client;

    private List<OrderImages> order_images;

    public int getId() {
        return id;
    }

    public String getOrder_code() {
        return order_code;
    }



    public String getOrder_type() {
        return order_type;
    }


    public String getDriver_id() {
        return driver_id;
    }

    public String getMarket_id() {
        return market_id;
    }


    public String getBill_cost() {
        return bill_cost;
    }

    public String getBill_step() {
        return bill_step;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getBill_image() {
        return bill_image;
    }

    public UserModel.User getClient() {
        return client;
    }

    public String getStatus() {
        return status;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getGoogle_place_id() {
        return google_place_id;
    }

    public String getFrom_name() {
        return from_name;
    }

    public String getFrom_address() {
        return from_address;
    }

    public String getFrom_latitude() {
        return from_latitude;
    }

    public String getFrom_longitude() {
        return from_longitude;
    }

    public String getTo_name() {
        return to_name;
    }

    public String getTo_address() {
        return to_address;
    }

    public String getTo_latitude() {
        return to_latitude;
    }

    public String getTo_longitude() {
        return to_longitude;
    }

    public String getOrder_description() {
        return order_description;
    }

    public String getCoupon() {
        return coupon;
    }

    public List<OrderImages> getOrder_images() {
        return order_images;
    }



    public static class OrderImages implements Serializable {
        private int id;
        private String image;

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
    }

}
