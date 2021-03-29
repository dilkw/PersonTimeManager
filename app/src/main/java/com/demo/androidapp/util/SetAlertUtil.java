package com.demo.androidapp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;

public class SetAlertUtil {

    public static void setAlertTime(AlarmManager alarmManager, long alertTime, PendingIntent pendingIntent) {
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,pendingIntent);
    }
}
