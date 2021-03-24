package com.demo.androidapp.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.demo.androidapp.R;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.model.entity.User;

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
    public void save(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(context.getResources().getString(R.string.shId_key),user.getId());
        editor.putLong(context.getResources().getString(R.string.shCreateTime_key),user.getCreated_at());
        editor.putString(context.getResources().getString(R.string.shUid_key),user.getUid());
        editor.putString(context.getResources().getString(R.string.shUserName_key),user.getName());
        editor.putString(context.getResources().getString(R.string.shPasswordName_key),user.getPassword());
        editor.putString(context.getResources().getString(R.string.shUid_key),user.getUid());
        editor.putString(context.getResources().getString(R.string.shEmail_key),user.getEmail());
        editor.putString(context.getResources().getString(R.string.shState_key),user.getState());
        editor.putString(context.getResources().getString(R.string.shImgUrl_key),user.getImg_url());
        editor.apply();
    }

    //获取数据
    public User load() {
        long id = this.sharedPreferences.getLong(context.getResources().getString(R.string.shId_key),0);
        long createTime = this.sharedPreferences.getLong(context.getResources().getString(R.string.shCreateTime_key),0);
        String userName = this.sharedPreferences.getString(context.getResources().getString(R.string.shUserName_key),"userName");
        String password = this.sharedPreferences.getString(context.getResources().getString(R.string.shPasswordName_key),"password");
        String uid = this.sharedPreferences.getString(context.getResources().getString(R.string.shUid_key),"uid");
        String cookieStr = this.sharedPreferences.getString(context.getResources().getString(R.string.shCookie_key),"");
        String email = this.sharedPreferences.getString(context.getResources().getString(R.string.shEmail_key),"");
        String state = this.sharedPreferences.getString(context.getResources().getString(R.string.shState_key),"");
        String imgUrl = this.sharedPreferences.getString(context.getResources().getString(R.string.shImgUrl_key),"");
        if (id != 0) {
            User user = new User(id, userName, uid, email, state, createTime,imgUrl);
            user.setPassword(password);
            user.setCookie(cookieStr);
            return user;
        }else {
            return null;
        }
    }

    //删除数据
    public void delete() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getResources().getString(R.string.shUserName_key));
        editor.remove(context.getResources().getString(R.string.shPasswordName_key));
        editor.remove(context.getResources().getString(R.string.shUid_key));
        editor.remove(context.getResources().getString(R.string.shEmail_key));
        editor.remove(context.getResources().getString(R.string.shId_key));
        editor.remove(context.getResources().getString(R.string.shState_key));
        editor.remove(context.getResources().getString(R.string.shCreateTime_key));
        editor.remove(context.getResources().getString(R.string.shCookie_key));
        editor.remove(context.getResources().getString(R.string.shImgUrl_key));
        editor.apply();
    }

}
