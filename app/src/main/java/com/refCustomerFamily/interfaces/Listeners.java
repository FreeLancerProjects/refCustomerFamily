package com.refCustomerFamily.interfaces;


import com.google.gson.internal.$Gson$Preconditions;

public interface Listeners {

    interface SignUpListener{

        void openSheet();
        void closeSheet();
        void checkDataValid();
        void checkReadPermission();
        void checkCameraPermission();

    }
    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }
    interface BackListener
    {
        void back();
    }
    interface LoginListener{
        void validate();
        void showCountryDialog();
        void familyApp();
        void delegateApp();
    }


    interface SettingAction{
        void onSubscriptions();
        void onProfile();
        void onEditProfile();
        void onLanguageSetting();
        void onTerms();
        void onPrivacy();
        void onRate();
        void onTone();
        void about();
        void logout();
        void share();
        void contactUs();

    }
    interface UpdateProfileListener
    {
        void updateProfile();
    }

}
