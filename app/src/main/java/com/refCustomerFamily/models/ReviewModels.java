package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class ReviewModels implements Serializable {
    private List<Reviews> reviews;

    public List<Reviews> getReviews() {
        return reviews;
    }

    public class Reviews implements Serializable
    {
        private String author_name;
        private String profile_photo_url;
        private String relative_time_description;
        private double rating;
        private String text;

        public String getAuthor_name() {
            return author_name;
        }

        public String getProfile_photo_url() {
            return profile_photo_url;
        }

        public String getRelative_time_description() {
            return relative_time_description;
        }

        public double getRating() {
            return rating;
        }

        public String getText() {
            return text;
        }
    }
}
