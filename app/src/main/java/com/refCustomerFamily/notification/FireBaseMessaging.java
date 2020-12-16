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
import com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity;
import com.refCustomerFamily.activities_fragments.chat_activity.ChatActivity;
import com.refCustomerFamily.activities_fragments.familyorderstepsactivity.FamilyOrderStepsActivity;
import com.refCustomerFamily.models.NotFireModel;
import com.refCustomerFamily.models.OrderModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.refCustomerFamily.R;
import com.refCustomerFamily.models.ChatUserModel;
import com.refCustomerFamily.models.MessageModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.tags.Tags;

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

        for (String key : map.keySet()) {
            Log.e("keys", key + "    value " + map.get(key));
        }

        if (getSession().equals(Tags.session_login)) {
            Log.e("sllslslls", "lslslsls");
            if (map.get("to_user_id") != null) {
                Log.e("sllslslls", getCurrentUser_id() + "");

                int to_id = Integer.parseInt(map.get("to_user_id"));
                if (getCurrentUser_id() == to_id) {
                    Log.e("sllslslls", "lslslsls");

                    manageNotification(map);
                }
            } else {
                manageNotification(map);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNewNotificationVersion(map);
        } else {
            createOldNotificationVersion(map);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createNewNotificationVersion(Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

        if (not_type != null && not_type.equals("chat")) {
            try{   String file_link = "";
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            int msg_id = Integer.parseInt(map.get("id"));
            int room_id = Integer.parseInt(map.get("chat_room_id"));
            int from_user_id = Integer.parseInt(map.get("from_user_id"));
            int to_user_id = Integer.parseInt(map.get("to_user_id"));
            String type = map.get("message_kind");
            String user_type = map.get("user_type");
            int order_id = Integer.parseInt(map.get("order_id"));

            Log.e("llfll", room_id + "");

                long date = Long.parseLong(map.get("date"));
                String isRead = map.get("is_read");

                String message = map.get("message");
                String from_user_name = map.get("fromUserName");


                MessageModel messageModel = new MessageModel(msg_id, to_user_id + "", from_user_id + "", type, message, file_link, room_id + "", isRead + "");


                Log.e("mkjjj", current_class);


                if (current_class.equals("com.refCustomerFamily.activities_fragments.chat_activity.ChatActivity")) {

                    String chat_user_id = getChatUser_id();

                    if (chat_user_id.equals(from_user_id + "")) {
                        EventBus.getDefault().post(messageModel);
                    } else {
                        try{
                            LoadChatImage(messageModel,user_type,order_id, from_user_name, sound_Path, 1);

                        }catch (Exception e){

                        }
                    }


                } else {

                    EventBus.getDefault().post(messageModel);
                    LoadChatImage(messageModel,user_type,order_id, from_user_name, sound_Path, 1);


                }

            }catch (Exception e){

            }

        }
        else if (not_type.equals("general_notification")) {

            String title = map.get("title");
            String body = map.get("body");

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            String CHANNEL_ID = "my_channel_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);

            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );
            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.mipmap.ic_launcher);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


//            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
//
//            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            builder.setContentIntent(pendingIntent);

            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {

                manager.createNotificationChannel(channel);
                manager.notify(Tags.not_tag, Tags.not_id, builder.build());


            }


        }
        else if (not_type.equals("order")) {

            String title = map.get("title");
            String body = map.get("message");
            String order_id = map.get("order_id");
            String order_type = map.get("order_type");
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
           // Log.e("mkjjj", current_class);

            if (current_class.equals("com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity") || current_class.equals("com.refCustomerFamily.activities_fragments.familyorderstepsactivity.FamilyOrderStepsActivity")) {
                if (order_id.equals(getorderid())) {
                    EventBus.getDefault().post(new NotFireModel(true));

                } else {
                    Loadnworder(title, body, order_id, order_type);

                }
            }
            else {
                Loadnworder(title, body, order_id, order_type);

            }
        }
    }

    private String getorderid() {
        return preferences.getordrUserData(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Loadnworder(String title, String body, String order_id, String order_type) {
        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);

        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmap);

        Intent intent = null;
        OrderModel.Data ordermodel = new OrderModel.Data();
        ordermodel.setId(Integer.parseInt(order_id));
        if (order_type.equals("family")) {
            intent = new Intent(this, FamilyOrderStepsActivity.class);
        } else {
            intent = new Intent(this, OrderStepsActivity.class);
        }


        intent.putExtra("data", ordermodel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));


//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (manager != null) {
//
//                manager.createNotificationChannel(channel);
//                manager.notify(Tags.not_tag, Tags.not_id, builder.build());
//
//
//
//
//            }
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) {
                    builder.setLargeIcon(bitmap);
                    // builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));


                    EventBus.getDefault().post(new NotFireModel(true));
                    manager.createNotificationChannel(channel);
                    manager.notify(new Random().nextInt(200), builder.build());
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };


        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        // Log.e("ldlfllf", image);
                        Picasso.get().load(R.drawable.logo).resize(250, 250).into(target);


                    }
                }, 1);

    }

    private void LoadChatImage(MessageModel messageModel,String user_type,int order_id, String fromusername, String sound_path, int type) {


        Target target = new Target() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                if (type == 1) {
                    sendChatNotification_VersionNew(messageModel,user_type,order_id, fromusername, sound_path, bitmap);

                } else {
                    sendChatNotification_VersionOld(messageModel,user_type,order_id, fromusername, sound_path, bitmap);

                }
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {


                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_nav_notification);

                if (type == 1) {
                    sendChatNotification_VersionNew(messageModel,user_type,order_id, fromusername, sound_path, bitmap);

                } else {
                    sendChatNotification_VersionOld(messageModel,user_type,order_id, fromusername, sound_path, bitmap);

                }

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        new Handler(Looper.getMainLooper()).postDelayed(() -> Picasso.get().load(Uri.parse(Tags.IMAGE_URL + preferences.getUserData(this).getData().getLogo())).into(target), 100);

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createOldNotificationVersion(Map<String, String> map) {


        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

        if (not_type != null && not_type.equals("chat")) {
            String file_link = "";

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();


            int msg_id = Integer.parseInt(map.get("id"));
            int room_id = Integer.parseInt(map.get("chat_room_id"));
            int from_user_id = Integer.parseInt(map.get("from_user_id"));
            int to_user_id = Integer.parseInt(map.get("to_user_id"));
            String type = map.get("message_kind");
            String user_type = map.get("user_type");
            int order_id = Integer.parseInt(map.get("order_id"));

            long date = Long.parseLong(map.get("date"));
            String isRead = map.get("is_read");

            String message = map.get("message");

            String from_user_name = map.get("fromUserName");


            MessageModel messageModel = new MessageModel(msg_id, to_user_id + "", from_user_id + "", type, message, file_link, room_id + "", isRead + "");

            if (current_class.equals("com.refCustomerFamily.activities_fragments.chat_activity.ChatActivity")) {

                String chat_user_id = getChatUser_id();
                if (chat_user_id.equals(from_user_id + "")) {
                    EventBus.getDefault().post(messageModel);
                } else {
                    LoadChatImage(messageModel,user_type,order_id, from_user_name, sound_Path, 0);
                }


            } else {


                EventBus.getDefault().post(messageModel);
                LoadChatImage(messageModel,user_type,order_id, from_user_name, sound_Path, 0);


            }

        }
        else if (not_type.equals("general_notification")) {

            String title = map.get("title");
            String body = map.get("body");

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            String CHANNEL_ID = "my_channel_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

//            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
//
//            channel.setShowBadge(true);
//            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
//                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
//                    .build()
//            );

            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.mipmap.ic_launcher);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


            //    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

            //  PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            //builder.setContentIntent(pendingIntent);


            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {

                // manager.createNotificationChannel(channel);
                manager.notify(Tags.not_tag, Tags.not_id, builder.build());


            }


        }
        else if (not_type.equals("order")) {

            String title = map.get("title");
            String body = map.get("message");
            String order_id = map.get("order_id");
            String order_type = map.get("order_type");
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            Log.e("mkjjj", current_class);

            if (current_class.equals("com.refCustomerFamily.activities_fragments.activity_order_steps.OrderStepsActivity") || current_class.equals("com.refCustomerFamily.activities_fragments.familyorderstepsactivity.FamilyOrderStepsActivity")) {
                if (order_id.equals(getorderid())) {
                    EventBus.getDefault().post(new NotFireModel(true));

                } else {
                    Loadoladorder(title, body, order_id, order_type);

                }
            } else {
                Loadoladorder(title, body, order_id, order_type);

            }


        }
    }

    private void Loadoladorder(String title, String body, String order_id, String order_type) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;


//            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
//
//            channel.setShowBadge(true);
//            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
//                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
//                    .build()
//            );
        // builder.setChannelId(CHANNEL_ID);

        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmap);

        Intent intent = null;
        OrderModel.Data ordermodel = new OrderModel.Data();
        ordermodel.setId(Integer.parseInt(order_id));
        if (order_type.equals("family")) {
            intent = new Intent(this, FamilyOrderStepsActivity.class);
        } else {
            intent = new Intent(this, OrderStepsActivity.class);
        }


        intent.putExtra("data", ordermodel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));


//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (manager != null) {
//
//                manager.createNotificationChannel(channel);
//                manager.notify(Tags.not_tag, Tags.not_id, builder.build());
//
//
//
//
//            }
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) {
                    builder.setLargeIcon(bitmap);
                    //   builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));


                    EventBus.getDefault().post(new NotFireModel(true));
                    // manager.createNotificationChannel(channel);
                    manager.notify(new Random().nextInt(200), builder.build());
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };


        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        // Log.e("ldlfllf", image);
                        Picasso.get().load(R.drawable.logo).resize(250, 250).into(target);


                    }
                }, 1);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendChatNotification_VersionNew(MessageModel messageModel,String user_type,int order_id, String fromusername, String sound_path, Bitmap bitmap) {


        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_nav_notification);
        builder.setContentTitle(fromusername);
        builder.setLargeIcon(bitmap);
        Intent intent = new Intent(this, ChatActivity.class);
        ChatUserModel chatUserModel = new ChatUserModel(fromusername, null, messageModel.getFrom_user_id() + "", Integer.parseInt(messageModel.getRoom_id()),order_id,user_type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("chat_user_data", chatUserModel);
        intent.putExtra("from_fire", true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        builder.setContentText(messageModel.getMessage());


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(12352, builder.build());
        }


    }

    private void sendChatNotification_VersionOld(MessageModel messageModel,String usertype,int order_id, String fromusername, String sound_path, Bitmap bitmap) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_nav_notification);
        builder.setContentTitle(fromusername);

        Intent intent = new Intent(this, ChatActivity.class);
        ChatUserModel chatUserModel = new ChatUserModel(fromusername, null, messageModel.getFrom_user_id() + "", Integer.parseInt(messageModel.getRoom_id()),order_id,usertype);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("chat_user_data", chatUserModel);
        intent.putExtra("from_fire", true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        builder.setContentText(messageModel.getMessage());


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(12352, builder.build());
        }


    }


    private int getCurrentUser_id() {
        return preferences.getUserData(this).getData().getId();
    }

    private String getChatUser_id() {
        if (preferences.getChatUserData(this) != null) {
            return preferences.getChatUserData(this).getId();

        } else {
            return "-1";

        }
    }

    private String getSession() {
        return preferences.getSession(this);
    }


}
