package com.demo.androidapp.util;

import android.util.Log;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.api.LiveDataCallAdapter;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * 定义OkHttp3拦截器，处理数据包得header，从中取出cookie
 */
public class InterceptorOfReceivedCookie implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("imageView", "intercept: InterceptorOfReceivedCookie");
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            //重新发起请求
            MyApplication.getApplication().reSignIn();
            Request newRequest = response.request().newBuilder()
                    .removeHeader("Cookie")   //移除旧的token
                    .addHeader("Cookie", MyApplication.getApplication().getUser().getCookie())  //添加新的token
                    .build();
            response.close();
            return chain.proceed(newRequest);//重新发起请求，此时是新的token
        }

        if (MyApplication.getApplication().getUser() != null
                && MyApplication.getApplication().getUser().getCookie().equals("")) {
            String cookieStr = response.header(MyApplication.getMyApplicationContext().getResources().getString(R.string.headerCookie_name));
            if (cookieStr != null && !cookieStr.equals("")) {
                MyApplication.getApplication().saveCookie(cookieStr);
            }
        }
        return response;
    }
}
