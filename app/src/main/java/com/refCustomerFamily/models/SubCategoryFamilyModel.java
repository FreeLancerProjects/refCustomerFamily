package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class SubCategoryFamilyModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {


        List<Category> categories;

        public List<Category> getCategories() {
            return categories;
        }

        public static class Category implements Serializable{

            private int id;
            private String title;
            private String desc;
            private String image;
            private String level;
            private int parent_id;
            private String is_shown;
            private List<FamilyModel> categories_families;

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

            public String getLevel() {
                return level;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public void setIs_shown(String is_shown) {
                this.is_shown = is_shown;
            }

            public String getIs_shown() {
                return is_shown;
            }

            public List<FamilyModel> getCategories_families() {
                return categories_families;
            }
        }




    }


}
