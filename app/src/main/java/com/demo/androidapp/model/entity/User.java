package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {

    //id
    @PrimaryKey(autoGenerate = false)
    private long id;

    //昵称
    @ColumnInfo(name = "name")
    private String name;

    //uid
    @ColumnInfo(name = "uid")
    private String uid;

    //邮箱
    @ColumnInfo(name = "email")
    private String email;

    //状态
    @ColumnInfo(name = "state")
    private String state;

    //创建时间
    @ColumnInfo(name = "created_at")
    private long created_at;

    //创建时间
    @ColumnInfo(name = "img_url")
    private String img_url;

    //创建时间
    @Ignore
    private String password;

    //cookie
    @Ignore
    private String cookie;

    @Ignore
    public User() {}

    public User(long id, String name, String uid, String email, String state, long created_at,String img_url) {
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.state = state;
        this.created_at = created_at;
        this.img_url = img_url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", state='" + state + '\'' +
                ", created_at=" + created_at +
                ", img_url='" + img_url + '\'' +
                ", password='" + password + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}
