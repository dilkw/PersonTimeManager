package com.demo.androidapp.util;

import android.util.Log;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 定义OkHttp3拦截器，处理数据包得header，从中取出cookie
 */
public class InterceptorOfReceivedCookie implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Log.d("response", "onResponse: cookieStr1" + response.headers().toString() + "\n" + response.headers(MyApplication.getMyApplicationContext().getResources().getString(R.string.headerCookie_name)));
        String cookieStr = response.header(MyApplication.getMyApplicationContext().getResources().getString(R.string.headerCookie_name));
        Log.d("response", "onResponse: cookieStr2" + cookieStr);
        if (cookieStr != null && !cookieStr.equals("")) {
            MyApplication.getApplication().saveCookie(cookieStr);
        }
        return response;
    }
}
