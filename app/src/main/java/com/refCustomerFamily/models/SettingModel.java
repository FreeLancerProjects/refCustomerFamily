package com.refCustomerFamily.models;

import java.io.Serializable;

public class SettingModel implements Serializable {
    private Setting settings;

    public Setting getSettings() {
        return settings;
    }

    public static class Setting implements Serializable {
        private String facebook;
        private String twitter;
        private String instagram;
        private String telegram;
        private String whatsapp;
        private String about_app_link;
        private String terms_condition_link;
        private String privacy_policy_link;

        public String getFacebook() {
            return facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getTelegram() {
            return telegram;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public String getAbout_app_link() {
            return about_app_link;
        }

        public String getTerms_condition_link() {
            return terms_condition_link;
        }

        public String getPrivacy_policy_link() {
            return privacy_policy_link;
        }

        public void setAbout_app_link(String about_app_link) {
            this.about_app_link = about_app_link;
        }

        public void setTerms_condition_link(String terms_condition_link) {
            this.terms_condition_link = terms_condition_link;
        }

        public void setPrivacy_policy_link(String privacy_policy_link) {
            this.privacy_policy_link = privacy_policy_link;
        }
    }
}
