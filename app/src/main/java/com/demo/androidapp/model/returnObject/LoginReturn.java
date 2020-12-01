package com.demo.androidapp.model.returnObject;

import java.sql.Timestamp;

//登录返回数据类型
public class LoginReturn {

    private String uid;                 //用户id

    private String userName;            //用户名

    private String active;              //是否被激活

    private String created_at;       //创建时间

    public LoginReturn(String uid, String userName, String active, String created_at) {
        this.uid = uid;
        this.userName = userName;
        this.active = active;
        this.created_at = created_at;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
