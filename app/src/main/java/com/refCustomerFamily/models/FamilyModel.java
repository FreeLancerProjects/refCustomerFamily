package com.refCustomerFamily.models;

import java.io.Serializable;

public class FamilyModel implements Serializable {

    private int id;
    private double rating;
    private String user_type;
    private String phone;
    private String phone_code;
    private String name;
    private String email;
    private String logo;
    private String banner;
    private String address;
    private String nationality_title;
    private String card_id;
    private String card_image;
    private String account_bank_number;
    private String ipad_number;
    private String address_registered_for_bank_account;
    private String notification_status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationality_title() {
        return nationality_title;
    }

    public void setNationality_title(String nationality_title) {
        this.nationality_title = nationality_title;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getAccount_bank_number() {
        return account_bank_number;
    }

    public void setAccount_bank_number(String account_bank_number) {
        this.account_bank_number = account_bank_number;
    }

    public String getIpad_number() {
        return ipad_number;
    }

    public void setIpad_number(String ipad_number) {
        this.ipad_number = ipad_number;
    }

    public String getAddress_registered_for_bank_account() {
        return address_registered_for_bank_account;
    }

    public void setAddress_registered_for_bank_account(String address_registered_for_bank_account) {
        this.address_registered_for_bank_account = address_registered_for_bank_account;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
    }
}
