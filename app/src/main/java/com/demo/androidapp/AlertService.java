package com.demo.androidapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlertService extends Service {

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (alertTime != 0) {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(),SERVICE_REQUEST_CODE,intent,flags);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,pendingIntent);
            Intent intent1 = new Intent("android.net.conn.CONNECTIVITY_CHANGE");
            sendBroadcast(intent1); // 发送广播
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public long getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(long alertTime) {
        this.alertTime = alertTime;
    }
}