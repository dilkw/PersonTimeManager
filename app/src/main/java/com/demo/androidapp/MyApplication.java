package com.demo.androidapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.demo.androidapp.api.Api;
import com.demo.androidapp.api.impl.RetrofitClient;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.util.DataSP;

public class MyApplication extends Application {

    private static String USER_NAME = "";    //用户名
    private static String PASSWORD = "";     //密码
    private static String UID = "";          //uid
    private static String COOKIE = "";       //cookie校验码

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
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getUID() {
        return UID;
    }

    public String getCOOKIE() {
        return COOKIE;
    }

    public static Api getApi() {
        return api;
    }

    public void loadData() {
        Auth auth = dataSP.load();
        if(auth != null) {
            USER_NAME = auth.getUserName();
            PASSWORD = auth.getPassword();
            UID = auth.getUid();
        }
        COOKIE = dataSP.getCookie();
    }

    public void saveCookie(String cookieStr) {
        MyApplication.COOKIE = cookieStr;
        if (dataSP == null)
            dataSP = new DataSP(mContext);
        dataSP.saveCookie(cookieStr);
    }

    public void saveData(String userName, String password,String uid) {
        MyApplication.USER_NAME = userName;
        MyApplication.PASSWORD = password;
        MyApplication.UID = uid;
        if (dataSP == null)
            dataSP = new DataSP(mContext);
        dataSP.save(USER_NAME,PASSWORD,UID);
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

    public void signOut() {
        USER_NAME = "userName";
        PASSWORD = "";
        UID = "";
        COOKIE = "";
        if (dataSP == null) {
            dataSP = new DataSP(mContext);
        }
        dataSP.delete();
    }
}
