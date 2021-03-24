package com.demo.androidapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.demo.androidapp.api.Api;
import com.demo.androidapp.api.impl.RetrofitClient;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.util.DataSP;

import java.io.IOException;

public class MyApplication extends Application {

    private static User user;

    private String COOKIE = "";

    private static Api api;

    private DataSP dataSP;

    @SuppressLint("StaticFieldLeak")
    private static MyApplication myApplication;

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        myApplication = getApplication();
        dataSP = new DataSP(mContext);
        loadData();
        api = RetrofitClient.getInstance().getApi();
        Log.d("imageView", "onCreate: MyApplication--------api创建");
        if (api == null) {
            Log.d("imageView", "onCreate: MyApplication--------api为空");
        }
    }

    public User getUser() {
        return user;
    }

    public static Api getApi() {
        return api;
    }

    //加载用户登录成功后的信息（包括cookie）
    public void loadData() {
        if ((user = dataSP.load()) != null){
            COOKIE = dataSP.getCookie();
            user.setCookie(COOKIE);
            Log.d("imageView", "loadData: " + user.toString());
        }
    }

    //保存cookie
    public void saveCookie(String cookieStr) {
        Log.d("imageView", "saveCookie: 保存cookie");
        COOKIE = cookieStr;
        user.setCookie(cookieStr);
        if (dataSP == null) {
            dataSP = new DataSP(mContext);
            dataSP.saveCookie(cookieStr);
        }
    }
    //用于当cookie失效时将cookie清空，便于更新cookie
    public void deleteCookie() {
        Log.d("imageView", "saveCookie: 删除cookie");
        COOKIE = "";
        user.setCookie("");
        if (dataSP == null) {
            dataSP = new DataSP(mContext);
            dataSP.deleteCookie();
        }
    }

    //登录成功初始化user对象
    public void signIn(User user) {
        this.user = user;
        if (dataSP == null)
            dataSP = new DataSP(mContext);
        Log.d("imageView", "MyApplication.signIn: " + user.toString());
        this.user.setCookie(COOKIE);
        dataSP.save(user);
    }

    //用于cookie失效重新登录
    public int reSignIn() throws IOException {
        deleteCookie();
        return api.reSignIn(user.getName(),user.getPassword()).execute().body().getCode();
    }

    //退出登录
    public void signOut() {
        api.signOut();
        COOKIE = "";
        user = null;
        if (dataSP == null) {
            dataSP = new DataSP(mContext);
        }
        dataSP.delete();
    }

    public static MyApplication getApplication() {
        if(myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    public static Context getMyApplicationContext() {
        return mContext;
    }




}
