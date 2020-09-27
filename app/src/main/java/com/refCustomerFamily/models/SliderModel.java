package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class SliderModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data implements Serializable {
        private int id;
        private String image;



        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }


    }
}