package com.refCustomerFamily.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderModel implements Serializable {

    private List<Data> data;
    private Data order;

    public List<Data> getData() {
        return data;
    }

    public Data getOrder() {
        return order;
    }

    public static class Data implements Serializable {
        private int id;
        private int client_id;
        private int driver_id;
        private int family_id;
        private int coupon_id;
        private int bill_cost;
        private String order_type;
        private String status;
        private String google_place_id;
        private String bill_step;
        private String bill_image;
        private String payment_method;
        private String from_name;
        private String from_address;
        private String from_latitude;
        private String from_longitude;
        private String to_address;
        private String to_name;
        private String order_description;
        private String order_nots;
        private String end_shipping_time;
        private String currency;
        private String other_phone;
        private String coupon;
        private String cancel_reason;
        private List<ProductModel.ImageModel> order_images = new ArrayList<>();
        private User family;
        private User client;
        private User driver;
        private DriverChat driver_chat;
        private DriverChat family_chat;
        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public int getClient_id() {
            return client_id;
        }

        public int getDriver_id() {
            return driver_id;
        }

        public int getFamily_id() {
            return family_id;
        }

        public int getCoupon_id() {
            return coupon_id;
        }

        public int getBill_cost() {
            return bill_cost;
        }

        public String getOrder_type() {
            return order_type;
        }

        public String getStatus() {
            return status;
        }

        public String getFrom_name() {
            return from_name;
        }

        public void setFrom_name(String from_name) {
            this.from_name = from_name;
        }

        public String getGoogle_place_id() {
            return google_place_id;
        }

        public String getBill_step() {
            return bill_step;
        }

        public String getBill_image() {
            return bill_image;
        }

        public String getPayment_method() {
            return payment_method;
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

        public String getTo_address() {
            return to_address;
        }

        public String getTo_name() {
            return to_name;
        }

        public String getOrder_description() {
            return order_description;
        }

        public String getOrder_nots() {
            return order_nots;
        }

        public String getEnd_shipping_time() {
            return end_shipping_time;
        }

        public String getCurrency() {
            return currency;
        }

        public String getOther_phone() {
            return other_phone;
        }

        public String getCoupon() {
            return coupon;
        }

        public String getCancel_reason() {
            return cancel_reason;
        }

        public List<ProductModel.ImageModel> getOrder_images() {
            return order_images;
        }

        public User getFamily() {
            return family;
        }

        public User getClient() {
            return client;
        }

        public User getDriver() {
            return driver;
        }

        public DriverChat getDriver_chat() {
            return driver_chat;
        }

        public DriverChat getFamily_chat() {
            return family_chat;
        }

        public static class User implements Serializable {
            private int id;
            private String name;
            private String email;
            private String city;
            private String phone_code;
            private String phone;
            private String logo;
            private String banner;
            private String card_image;
            private String account_bank_number;
            private String ipad_number;
            private String nationality_title;
            private float rating;
            private String package_finished_at;
            private String notification_status;
            private String is_confirmed;
            private String card_id;
            private String address_registered_for_bank_account;
            private String phone_is_shown;
            private String is_block;
            private String is_login;
            private String logout_time;
            private String user_type;
            private String software_type;
            private String forget_password_code;
            private String email_verified_at;
            private String deleted_at;

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


            public String getLogo() {
                return logo;
            }

            public String getBanner() {
                return banner;
            }

            public String getCard_image() {
                return card_image;
            }

            public String getAccount_bank_number() {
                return account_bank_number;
            }

            public String getIpad_number() {
                return ipad_number;
            }

            public String getNationality_title() {
                return nationality_title;
            }

            public float getRating() {
                return rating;
            }

            public String getPackage_finished_at() {
                return package_finished_at;
            }

            public String getNotification_status() {
                return notification_status;
            }

            public String getIs_confirmed() {
                return is_confirmed;
            }

            public String getCard_id() {
                return card_id;
            }

            public String getAddress_registered_for_bank_account() {
                return address_registered_for_bank_account;
            }

            public String getPhone_is_shown() {
                return phone_is_shown;
            }

            public String getIs_block() {
                return is_block;
            }

            public String getIs_login() {
                return is_login;
            }

            public String getLogout_time() {
                return logout_time;
            }

            public String getUser_type() {
                return user_type;
            }

            public String getSoftware_type() {
                return software_type;
            }

            public String getForget_password_code() {
                return forget_password_code;
            }

            public String getEmail_verified_at() {
                return email_verified_at;
            }

            public String getDeleted_at() {
                return deleted_at;
            }
        }

        public class DriverChat implements Serializable {
            private int id;
            private int order_id;
            private int first_user_id;
            private int second_user_id;

            public int getId() {
                return id;
            }

            public int getOrder_id() {
                return order_id;
            }

            public int getFirst_user_id() {
                return first_user_id;
            }

            public int getSecond_user_id() {
                return second_user_id;
            }
        }

    }


}
