package com.refCustomerFamily.notification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.refCustomerFamily.R;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

public class FireBaseMessaging extends FirebaseMessagingService {

    Preferences preferences = Preferences.newInstance();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> map = remoteMessage.getData();

        for (String key:map.keySet())
        {
            Log.e("keys",key+"    value "+map.get(key));
        }

//        if (getSession().equals(Tags.session_login))
//        {
//            if (map.get("to_user_id")!=null)
//            {
//                int to_id = Integer.parseInt(map.get("to_user_id"));
//
//                if (getCurrentUser_id()==to_id)
//                {
//                    manageNotification(map);
//                }
//            }
//            else if(map.get("id")!=null){
//
//                manageNotification(map);
//
//            }
//        }
        }





    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            //createNewNotificationVersion(map);
        }else
            {
            //    createOldNotificationVersion(map);

            }
    }



    private String getSession()
    {
        return preferences.getSession(this);
    }


}
