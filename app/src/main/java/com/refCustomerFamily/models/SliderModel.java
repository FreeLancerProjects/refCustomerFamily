package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class SliderModel implements Serializable {
    private Data data;


    public Data getData() {
        return data;
    }


    public static class Data implements Serializable {

        List<Sliders> sliders;

        public List<Sliders> getCategories() {
            return sliders;
        }

        public static class Sliders implements Serializable{

            private int id;
            private String title;
            private String desc;
            private String image;
            private String level;
            private int parent_id;
            private String is_shown;


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
        }

    }


}