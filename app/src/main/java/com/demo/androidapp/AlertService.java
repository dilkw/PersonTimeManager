package com.demo.androidapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.Navigation;

public class AlertService extends Service {

    private static final String TAG = "AlertService";
    private long alertTime = 0;

    private PendingIntent pendingIntent;

    private final static int SERVICE_REQUEST_CODE = 0;

    public AlertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
//        Context context = getApplicationContext();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("0",
//                    "channelName", NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription("description");
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(channel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NotificationChannel.DEFAULT_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_alert)
//                .setContentTitle("提醒")
//                .setContentText("test")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(getPendingIntent(context))
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(NotificationManager.IMPORTANCE_DEFAULT,builder.build());
        return super.onStartCommand(intent,flags,startId);
    }

//    @SuppressLint("ResourceType")
//    private PendingIntent getPendingIntent(Context context) {
//        Bundle bundle = new Bundle();
//        return Navigation.findNavController(MainActivity.this,R.id.add_task_fragment)
//                .createDeepLink()
//                .setGraph(R.navigation.navigation)
//                .setArguments(bundle)
//                .createPendingIntent();
//    }

    public long getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(long alertTime) {
        this.alertTime = alertTime;
    }
}