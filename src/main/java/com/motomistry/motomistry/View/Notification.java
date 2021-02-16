package com.motomistry.motomistry.View;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notification {
    private RemoteViews notificationLayout,notificationLayoutExpanded;
    Context context;
    String CHANNEL_ID="my_chanal";

    public Notification(Context context) {
        this.context = context;
         notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
         notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.notification_large);
    }


    public void BasicNotic(String msg){
        android.app.Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_location)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .build();
    }
    public void sendNotification() {
        Intent notificationIntent = new Intent(context, DashbordActivity.class);
        notificationIntent.putExtra("menuFragment", "bookingHistory");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


        builder.setSmallIcon(R.drawable.pickup_truck);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

        builder.setContentTitle("Your service booking successful");
        builder.setContentText("Time to booking verification");
        builder.setSubText("Tap to view booking status");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
    public void ShowNotification(String goFrag,String msg1,String msg2) {
        Intent notificationIntent = new Intent(context, DashbordActivity.class);
        notificationIntent.putExtra("menuFragment", goFrag);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.pickup_truck);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

        builder.setContentTitle(msg1);
        builder.setContentText(msg2);
        builder.setSubText("Tap to view booking status");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
