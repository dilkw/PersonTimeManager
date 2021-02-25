package com.demo.androidapp.util;

import androidx.room.TypeConverter;

import java.sql.Date;

//room组件日期转换（date、long互转）
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
