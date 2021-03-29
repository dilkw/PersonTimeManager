package com.demo.androidapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.Navigation;

import java.util.concurrent.ConcurrentHashMap;

public class AlertReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //设置通知内容并在onReceive()这个函数执行时开启

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID,
                    "channelName", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("description");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification.Builder(context)
//                .setContentTitle("任务提醒")
//                .setContentText("闹铃提醒")
//                .setSmallIcon(R.drawable.ic_alert)
//                .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(),R.mipmap.ic_launcher))
//                .build();
//        notification.defaults = Notification.DEFAULT_ALL;
//        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.contentIntent = pIntent;
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NotificationChannel.DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle("提醒")
                .setContentText("test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent(context))
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NotificationManager.IMPORTANCE_DEFAULT,builder.build());
        //manager.notify(1, notification);
    }

    private PendingIntent getPendingIntent(Context context) {
        Bundle bundle = new Bundle();
        return Navigation.findNavController((Activity) context,R.id.add_task_fragment)
                .createDeepLink()
                .setGraph(R.navigation.navigation)
                .setArguments(bundle)
                .createPendingIntent();
    }
}