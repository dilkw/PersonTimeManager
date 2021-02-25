package com.demo.androidapp.model.returnObject;

import java.sql.Date;

//登录返回数据类型
public class LoginAndRegisterReturn {

    private String uid;                 //用户id

    private String name;            //用户名

    private String state;              //是否被激活

    private Date created_at;       //创建时间

    public LoginAndRegisterReturn(String uid, String name, String state, Date created_at) {
        this.uid = uid;
        this.name = name;
        this.state = state;
        this.created_at = created_at;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "LoginAndRegisterReturn{" +
                "uid='" + uid + '\'' +
                ", userName='" + name + '\'' +
                ", state='" + state + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
