package com.demo.androidapp.api.impl;

import android.util.Log;

import com.demo.androidapp.api.Api;
import com.demo.androidapp.api.LiveDataCallAdapterFactory;
import com.demo.androidapp.util.InterceptorOfAddCookie;
import com.demo.androidapp.util.InterceptorOfReceivedCookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //private static final String BASE_URL = "https://sodacoco.com/api/v1/";
    private static final String BASE_URL = "http://192.168.1.7:8000/api/v1/";
    private static RetrofitClient retrofitClient;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {
        // 创建gson对象构建器
        GsonBuilder gsonBuilder = new GsonBuilder();
        // 实现将long 类型的日期格式转换为国际标准格式
        //gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            //public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                //Date date = new Date(json.getAsJsonPrimitive().getAsLong() * 1000);
                //Log.d("imageView", "deserialize: " + json.getAsJsonPrimitive().getAsLong());
                //return date;
            //}
        //});
        // 创建gson对象
        Gson gson = gsonBuilder.create();

        okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(new InterceptorOfAddCookie())
                .addInterceptor(new InterceptorOfReceivedCookie())
                .build();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if(retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }


    private class DateTimeSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}
