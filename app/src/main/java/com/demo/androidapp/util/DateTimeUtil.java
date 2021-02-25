package com.demo.androidapp.util;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public String intToStrDateTime(int year,int moth,int day,int hour,int minute) {
        return "" + year + "-" + moth + "-" + day + " " + hour + ":" + minute;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Date intToDateTime(int year, int moth, int day, int hour, int minute) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.of(year,moth,day,hour,minute);
        String str = localDateTime.format(dateTimeFormatter);
        return Date.valueOf(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String dateToStrYMDHM(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public Date strToDateYMDHM(String str){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date==null ? null : new Date(date.getTime());
    }
}
