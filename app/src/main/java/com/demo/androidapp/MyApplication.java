package com.demo.androidapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.demo.androidapp.api.Api;
import com.demo.androidapp.api.impl.RetrofitClient;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.util.DataSP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApplication extends Application {

    private static String USER_NAME = "";    //用户名
    private static String PASSWORD = "";     //密码
    private static String UID = "";          //uid
    private static String COOKIE = "";       //cookie校验码

    private static User user;

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
        if (!USER_NAME.equals("userName")){
            signIn();
        }
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
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

    public void signIn(User user) {
        MyApplication.user = user;
        MyApplication.USER_NAME = user.getName();
        MyApplication.PASSWORD = user.getPassword();
        MyApplication.UID = user.getUid();
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

    public void signIn(){
        api.signIn(this.getUSER_NAME(),this.getPASSWORD());
    }

    public void signOut() {
        api.signOut();
        USER_NAME = "userName";
        PASSWORD = "";
        UID = "";
        COOKIE = "";
        if (dataSP == null) {
            dataSP = new DataSP(mContext);
        }
        dataSP.delete();
    }

//    public void reLogin(String ) {
//        USER_NAME = "userName";
//        PASSWORD = "";
//        UID = "";
//        COOKIE = "";
//        if (dataSP == null) {
//            dataSP = new DataSP(mContext);
//        }
//        dataSP.delete();
//    }
}
