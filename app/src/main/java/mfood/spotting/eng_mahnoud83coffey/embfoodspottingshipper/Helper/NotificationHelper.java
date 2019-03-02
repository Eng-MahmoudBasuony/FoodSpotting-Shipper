package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.R;


public class NotificationHelper extends ContextWrapper {


private static final String EMB_CHANNEL_ID="com.example.eng_mahnoud83coffey.embfoodspottingshipper.Helper.Food.Spotting";
private static final String EMB_CHANNEL_NAME="Food Spotting";
private NotificationManager manager;

public NotificationHelper(Context base)
        {
        super(base);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) //only Working this Function if API 26 OR Higher
        createChannel();
        }

@TargetApi(Build.VERSION_CODES.O)
private void createChannel()
        {
        NotificationChannel notificationChannel=new NotificationChannel(EMB_CHANNEL_ID,EMB_CHANNEL_NAME
        , NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(notificationChannel);

        }


public NotificationManager getManager() {

        if (manager==null)
        manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        return manager;
        }



@TargetApi(Build.VERSION_CODES.O)
public Notification.Builder getFoodSpottingChannelNotification(String title, String body, PendingIntent content, Uri soundUri)
        {


        return new Notification.Builder(getApplicationContext(),EMB_CHANNEL_ID)
        .setContentIntent(content)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(R.drawable.ic_local_shipping_black_24dp)
        .setSound(soundUri)
        .setAutoCancel(false);

        }


@TargetApi(Build.VERSION_CODES.O)
public Notification.Builder getFoodSpottingChannelNotification(String title, String body,  Uri soundUri)
        {


        return new Notification.Builder(getApplicationContext(),EMB_CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(R.drawable.ic_local_shipping_black_24dp)
        .setSound(soundUri)
        .setAutoCancel(false);

        }




        }
