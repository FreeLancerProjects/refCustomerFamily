package com.refCustomerFamily.activities_fragments.stores.google_place_modul.models;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel implements Serializable {
    private GooglePlaceData data;

    public GooglePlaceData getData() {
        return data;
    }

    public static class GooglePlaceData implements Serializable{
        private List<CategoryModel> google_categories;

        public List<CategoryModel> getGoogle_categories() {
            return google_categories;
        }
    }
}
