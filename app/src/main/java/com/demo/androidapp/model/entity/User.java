package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    //id
    @PrimaryKey(autoGenerate = false)
    private int id;

    //状态
    @ColumnInfo(name = "name")
    private String name;

    //状态
    @ColumnInfo(name = "state")
    private String uid;

    //状态
    @ColumnInfo(name = "email")
    private String email;

    //状态
    @ColumnInfo(name = "created_at")
    private long created_at;

    public User(int id, String name, String uid, String email, long created_at) {
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
