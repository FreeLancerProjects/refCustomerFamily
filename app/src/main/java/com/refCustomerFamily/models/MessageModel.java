package com.refCustomerFamily.models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private MessageModel data;
    private int id;
    private String to_user_id;
    private String from_user_id;
    private String message_kind;
    private String message;
    private String image;
    private String room_id;
    private String is_read;
    private long date;
    private User from_user;

    public MessageModel(int id, String to_user_id, String from_user_id, String message_kind, String message, String image,  String room_id, String is_read) {
        this.id = id;
        this.to_user_id = to_user_id;
        this.from_user_id = from_user_id;
        this.message_kind = message_kind;
        this.message = message;
        this.image = image;
        this.room_id = room_id;
        this.is_read = is_read;

    }

    public MessageModel getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getType() {
        return message_kind;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }


    public String getRoom_id() {
        return room_id;
    }

    public String getIs_read() {
        return is_read;
    }

    public long getDate() {
        return date;
    }

    public User getFrom_user() {
        return from_user;
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

}