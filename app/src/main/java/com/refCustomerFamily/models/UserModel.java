package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable {
        private int id;
        private String name;
        private String email;
        private String city;
        private String phone_code;
        private String phone;
        private String image;
        private String logo;
        private String token;
        private String latitude;
        private String longitude;
        private String address;
        private String card_image;
        private String user_type;
        private String rating;
        private String notification_status;
        private String is_confirmed;
        private String phone_is_shown;
        private String logout_time;
        private String forget_password_code;
        private String software_type;
        private String email_verified_at;
        private String deleted_at;
        private String created_at;
        private String updated_at;
        private String details;

        private String fireBaseToken;

        public User() {
        }

        public User(int id, String name, String phone_code, String phone, String logo, String token) {
            this.id = id;
            this.name = name;
            this.phone_code = phone_code;
            this.phone = phone;
            this.logo = logo;
            this.token = token;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getCity() {
            return city;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return image;
        }

        public String getLogo() {
            return logo;
        }

        public String getToken() {
            return token;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getType() {
            return user_type;
        }


        public String getCard_image() {
            return card_image;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getRating() {
            return rating;
        }

        public String getNotification_status() {
            return notification_status;
        }

        public String getIs_confirmed() {
            return is_confirmed;
        }

        public String getPhone_is_shown() {
            return phone_is_shown;
        }

        public String getLogout_time() {
            return logout_time;
        }

        public String getForget_password_code() {
            return forget_password_code;
        }

        public String getSoftware_type() {
            return software_type;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }



        public String getDetails() {
            return details;
        }



    }
}
