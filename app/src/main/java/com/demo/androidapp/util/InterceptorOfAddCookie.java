package com.demo.androidapp.util;

import android.content.Context;
import android.util.Log;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 定义OkHttp3拦截器，处理数据包的header，向header种添加cookie
 */
public class InterceptorOfAddCookie implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain.request().body() != null) {
            Log.d("imageView", "interceptmmmmmmmmmmmmmmmmmmmm:" + chain.request().body().toString());
        }
        Context context = MyApplication.getMyApplicationContext();
        Request.Builder builder = chain.request().newBuilder();
        String cookieStr = MyApplication.getApplication().getCOOKIE();
        if (cookieStr!= null && !cookieStr.equals("")) {
            Log.d("imageView", "intercept: 添加cookie" + cookieStr);
            builder.addHeader("Cookie",cookieStr);
        }
        Log.d("imageView", "intercept: 添加cookie失败" + cookieStr);
        return chain.proceed(builder.build());
    }
}
