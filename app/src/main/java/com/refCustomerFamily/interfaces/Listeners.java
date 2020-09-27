package com.refCustomerFamily.interfaces;


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

    }
}
