package com.demo.androidapp;

import android.app.Application;

public class MyApplication extends Application {

//    private String USER_NAME;   //用户名
//    private String PASSWORD;    //密码

    @Override
    public void onCreate() {
        super.onCreate();
//        DataSP dataSP = new DataSP(getApplicationContext());
//        Auth auth = dataSP.load();
//        this.USER_NAME = auth.getUserName();
//        this.PASSWORD = auth.getPassword();
    }

//    public String getUSER_NAME() {
//        return USER_NAME;
//    }
//
//    public String getPASSWORD() {
//        return PASSWORD;
//    }
}
