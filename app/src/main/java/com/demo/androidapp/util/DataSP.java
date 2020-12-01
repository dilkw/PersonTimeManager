package com.demo.androidapp.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.demo.androidapp.R;
import com.demo.androidapp.model.Auth;

//SharedPreferences存取数据操作类
public class DataSP {

//    private String userName;    //用户名
//
//    private String password;    //密码

    private Context context;
    private SharedPreferences sharedPreferences;
    private String shName;

    public DataSP(Context context) {
        this.context = context;
        shName = this.context.getResources().getString(R.string.shName);
        sharedPreferences = this.context.getSharedPreferences(shName,Context.MODE_PRIVATE);
    }

    //保存数据
    public void save(String userName,String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.shUserName_key),userName);
        editor.putString(context.getResources().getString(R.string.shPasswordName_key),password);
        editor.apply();
    }

    //获取数据
    public Auth load() {
        String userName = this.sharedPreferences.getString(context.getResources().getString(R.string.shUserName_key),"userName");
        String password = this.sharedPreferences.getString(context.getResources().getString(R.string.shPasswordName_key),"password");
        return new Auth(userName,password);
    }



}
