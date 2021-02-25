package com.demo.androidapp.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.demo.androidapp.R;
import com.demo.androidapp.model.Auth;

//SharedPreferences存取数据操作类
public class DataSP {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String shName;

    public DataSP(Context context) {
        this.context = context;
        shName = this.context.getResources().getString(R.string.shName);
        sharedPreferences = this.context.getSharedPreferences(shName,Context.MODE_PRIVATE);
    }

    //保存cookie
    public void saveCookie(String cookieStr) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.shCookie_key),cookieStr);
        editor.apply();
    }

    //删除cookie
    public void deleteCookie() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getResources().getString(R.string.shCookie_key));
        editor.apply();
    }

    //获取cookie
    public String getCookie() {
        return sharedPreferences.getString(context.getResources().getString(R.string.shCookie_key),"");
    }

    //保存/删除数据
    public void save(String userName,String password,String uid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.shUserName_key),userName);
        editor.putString(context.getResources().getString(R.string.shPasswordName_key),password);
        editor.putString(context.getResources().getString(R.string.shUid_key),uid);
        editor.apply();
    }

    //获取数据
    public Auth load() {
        String userName = this.sharedPreferences.getString(context.getResources().getString(R.string.shUserName_key),"userName");
        String password = this.sharedPreferences.getString(context.getResources().getString(R.string.shPasswordName_key),"password");
        String uid = this.sharedPreferences.getString(context.getResources().getString(R.string.shUid_key),"uid");
        String cookieStr = this.sharedPreferences.getString(context.getResources().getString(R.string.shCookie_key),"");
        return new Auth(userName,password,uid,cookieStr);
    }

    //删除数据
    public void delete() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getResources().getString(R.string.shUserName_key));
        editor.remove(context.getResources().getString(R.string.shPasswordName_key));
        editor.remove(context.getResources().getString(R.string.shUid_key));
        deleteCookie();
        editor.apply();
    }

}
