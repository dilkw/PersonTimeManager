package com.demo.androidapp.util;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DateTimeUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String intToStrDateTime(int year, int moth, int day, int hour, int minute) {
        LocalDateTime localDateTime = LocalDateTime.of(year,moth,day,hour,minute);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(dateTimeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime intToLocalDateTime(int year, int moth, int day, int hour, int minute) {
        LocalDateTime localDateTime = LocalDateTime.of(year,moth,day,hour,minute);
        return localDateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long intToLong(int year, int moth, int day, int hour, int minute) {
        LocalDateTime localDateTime = LocalDateTime.of(year,moth,day,hour,minute);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date intToDateTime(int year, int moth, int day, int hour, int minute) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.of(year,moth,day,hour,minute);
        String str = localDateTime.format(dateTimeFormatter);
        return Date.valueOf(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String sqlDateToStrYMDHM(Date sqlDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(sqlDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String utilDateToStrYMDHM(java.util.Date utilDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(utilDate);
    }

    public static String strToDateFormatYMDHM(String str){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date==null ? null : date.toString();
    }



    //将long类型转换成LocalDateTime
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime longToLocalDateTime(long l) {
        Instant instant = Instant.ofEpochMilli(l);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    //将long类型转换成字符串（年-月-日 时:分)格式
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String longToStrYMDHM(long l) {
        if (l == 0) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return longToLocalDateTime(l * 1000L).format(dateTimeFormatter);
    }

    //将字符串（年-月-日 时:分)格式类型转换成long
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long strToLong(String dateStr) {
        if (dateStr == null || dateStr.equals("")) {
            return 0;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = LocalDateTime.parse(dateStr,dateTimeFormatter).atZone(zone).toInstant();
        return instant.toEpochMilli() / 1000L;
    }

    //将LocalDateTime类型转换成long(毫秒)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long localDateTimeToLong(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    //将LocalDateTime类型转换成long(秒)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long localDateTimeToSecLong(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli() / 1000L;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String localDateTimeToStrYMDHM(LocalDateTime localDateTime) {
        String string = longToStrYMDHM(localDateTimeToLong(localDateTime) / 1000L);
        return string;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getCurrentTimeToLong() {
        return localDateTimeToLong(LocalDateTime.now());
    }

    @SuppressLint("DefaultLocale")
    public static String secondToHMS(long second) {
        long h = second / (60 * 60);
        long m = (second - 3600 * h) / 60;
        long s = second % 60;
        return String.format("%02d:%02d:%02d",h,m,s);
    }

    public static String getRandom() {
        Random rand = new Random();
        return "?v=" + rand.nextInt(100) + 1;
    }
}
