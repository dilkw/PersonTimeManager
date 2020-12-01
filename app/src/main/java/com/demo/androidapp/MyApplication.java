package com.demo.androidapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.demo.androidapp.api.Api;
import com.demo.androidapp.api.impl.RetrofitClient;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.util.DataSP;

public class MyApplication extends Application {

    private static String USER_NAME;   //用户名
    private static String PASSWORD;    //密码

    private static Api api;

    @SuppressLint("StaticFieldLeak")
    private static MyApplication myApplication;

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        myApplication = getApplication();
        loadData(mContext);
        api = RetrofitClient.getInstance().getApi();
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public static Api getApi() {
        return api;
    }

    public void loadData(Context context) {
        USER_NAME = "";
        PASSWORD = "";
        DataSP dataSP = new DataSP(context);
        Auth auth = dataSP.load();
        if(auth != null) {
            USER_NAME = auth.getUserName();
            PASSWORD = auth.getPassword();
        }
    }

    public void saveData(Context context, String userName, String password) {
        USER_NAME = userName;
        PASSWORD = password;
        DataSP dataSP = new DataSP(context);
        dataSP.save(USER_NAME,PASSWORD);
    }

    public static MyApplication getApplication() {
        if(myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }
}
