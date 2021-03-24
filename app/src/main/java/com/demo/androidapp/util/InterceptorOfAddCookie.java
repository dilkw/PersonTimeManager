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
        Log.d("imageView", "InterceptorOfAddCookie:11111111111111111");
        Request.Builder builder = chain.request().newBuilder();
        if (MyApplication.getApplication().getUser() != null){
            String cookieStr = MyApplication.getApplication().getUser().getCookie();
            if (cookieStr != null && !cookieStr.equals("")) {
                builder.addHeader("Cookie",cookieStr);
            }
        }
        return chain.proceed(builder.build());
    }
}
