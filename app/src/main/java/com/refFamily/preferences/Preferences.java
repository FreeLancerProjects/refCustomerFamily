package com.refFamily.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.refFamily.models.DefaultSettings;
import com.refFamily.models.UserModel;
import com.refFamily.tags.Tags;
import com.google.gson.Gson;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }


       public void createUpdateAppSetting(Context context, DefaultSettings settings) {
           SharedPreferences preferences = context.getSharedPreferences("settingsRef", Context.MODE_PRIVATE);
           Gson gson = new Gson();
           String data = gson.toJson(settings);
           SharedPreferences.Editor editor = preferences.edit();
           editor.putString("settings", data);
           editor.apply();
       }
    public DefaultSettings getAppSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settingsRef", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString("settings", ""), DefaultSettings.class);
    }


    public void create_update_userdata(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }

    public void create_update_session(Context context, String session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", session);
        editor.apply();


    }


    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }


    public void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
        create_update_session(context, Tags.session_logout);
    }
    public void saveLoginFragmentState(Context context,int state)
    {
        SharedPreferences preferences = context.getSharedPreferences("fragment_state",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("state",state);
        editor.apply();
    }
    public int getFragmentState(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("fragment_state",Context.MODE_PRIVATE);
        return preferences.getInt("state",0);
    }


}
