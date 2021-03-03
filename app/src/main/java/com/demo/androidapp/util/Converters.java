package com.demo.androidapp.util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

//room组件日期转换（date、long互转）
public class Converters {

//    @TypeConverter
//    public static long fromTimestamp(Timestamp value) {
//        return value.getTime();
//    }
//
//    @TypeConverter
//    public static Timestamp dateToTimestamp(long date) {
//        Log.d("imageView", "dateToTimestamp: 数据类型转换");
//        return new Timestamp(date);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @TypeConverter
//    public static String dateToString(Date date) {
//        DateTimeUtil dateTimeUtil = new DateTimeUtil();
//        return dateTimeUtil.sqlDateToStrYMDHM(date);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @TypeConverter
//    public static long stringToTime(String dateStr) {
//        DateTimeUtil dateTimeUtil = new DateTimeUtil();
//        return dateTimeUtil.strToLong(dateStr);
//    }
}
