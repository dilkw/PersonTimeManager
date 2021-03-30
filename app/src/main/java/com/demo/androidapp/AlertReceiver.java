package com.demo.androidapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    private static final String CHANNEL_ID = "myChannelId";

    public AlertReceiver() {
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive:接收到广播 ");
        if (intent.getAction().equals("AlarmClock")) {
            Log.d(TAG, "onReceive:接收到提醒广播 ");
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.putExtra("name",intent.getStringExtra("name"));
            intent1.putExtra("id",intent.getLongExtra("id",0));
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ");
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "channelName", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("description");

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }
            String contentTitle = "";
            String contentText = "";
            if (intent.getStringExtra("name").equals("clock")) {
                contentTitle = "时钟任务";
            }
            contentText = intent.getStringExtra("text");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_alert)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            Log.d(TAG, "发送通知" + notificationManagerCompat.getNotificationChannels().toString());
            notificationManagerCompat.notify(0, builder.build());
        }
    }
}