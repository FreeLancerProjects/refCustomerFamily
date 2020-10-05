package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class FamilyCategoryProductDataModel implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private List<FamilyCategory> categories;

        public List<FamilyCategory> getCategories() {
            return categories;
        }
    }
}
