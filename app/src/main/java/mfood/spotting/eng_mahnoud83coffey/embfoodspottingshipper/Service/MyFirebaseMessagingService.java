package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Common.Common;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Helper.NotificationHelper;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.HomeActivity;
import mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.R;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData()!=null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                sendNotificationAPI26(remoteMessage);
            else
                sendNotification(remoteMessage);

        }




    }


    private void sendNotificationAPI26(RemoteMessage remoteMessage)
    {


        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        //Here will fix to click to Notification -> go to Order list
        PendingIntent pendingIntent;
        NotificationHelper helper;
        Notification.Builder builder;


        if (Common.currentShipper!=null)
        {
            Intent intent=new Intent(this, HomeActivity.class);
           // intent.putExtra(Common.PHONE_TEXT, Common.currentUser.getPhone());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri uriDefaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            helper=new NotificationHelper(this);


            builder=helper.getFoodSpottingChannelNotification(title,body,pendingIntent,uriDefaultSound);

            //Gen Random Id for notification to show all Notification
            helper.getManager().notify(new Random().nextInt(),builder.build());

        }else //Fix Crash if Notification Send from News System (Common.currentUser == null)
        {
            Uri uriDefaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            helper=new NotificationHelper(this);

            builder=helper.getFoodSpottingChannelNotification(title,body,uriDefaultSound);

            //Gen Random Id for notification to show all Notification
            helper.getManager().notify(new Random().nextInt(),builder.build());
        }
    }

    private void sendNotification(RemoteMessage remoteMessage)
    {


        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        Intent intent=new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Uri uriDefaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//Sound
        Bitmap img = BitmapFactory.decodeResource(MyFirebaseMessagingService.this.getResources(), R.drawable.imagetest);


        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.imagetest)
                .setSound(uriDefaultSound)
                .setLargeIcon(img)
                .setAutoCancel(true)
                .setColor(Color.RED)
                .build();



        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify( new Random().nextInt(), notification);


    }




}
