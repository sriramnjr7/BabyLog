package com.example.maceo.babylog;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

public class NotificationHelper extends ContextWrapper {


    private static final String CHANNEL_ID = "com.example.maceo.babylog";
    private static final String CHANNEL_NAME = "Baby Log";
    private static final String GROUP_KEY_DAILY_REMINDER = "com.example.maceo.babylog.DAILY_REMINDER";


    NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel babyLogChannel = null;
        babyLogChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        babyLogChannel.enableVibration(true);
        babyLogChannel.enableLights(true);
        babyLogChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(babyLogChannel);

    }

    public NotificationManager getManager() {
        if (manager == null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification getChannelNotification(String title, String body, PendingIntent pendingIntent){

        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.babylog_icon)
                .setAutoCancel(true)
                .setGroupSummary(true)
                /*.setStyle(new Notification.InboxStyle()
                    .)*/
                .setGroup(GROUP_KEY_DAILY_REMINDER)
                .setContentIntent(pendingIntent)
                .build();
    }

}
