package com.demo.androidapp.model;

import androidx.room.Entity;

//用户实体类
public class Auth {

    private String userName;    //用户名

    private String password;    //密码

    private String uid;         //uid

    private String cookieStr;   //cookie

    public Auth() {
        this.userName = "aaaaa";
        this.password = "123456aa";
        this.uid = "123456";
        this.cookieStr = "";
    }

    public Auth(String userName, String password,String uid,String cookieStr) {
        this.userName = userName;
        this.password = password;
        this.uid = uid;
        this.cookieStr = cookieStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public String getCookieStr() {
        return cookieStr;
    }

    public void setCookieStr(String cookieStr) {
        this.cookieStr = cookieStr;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
