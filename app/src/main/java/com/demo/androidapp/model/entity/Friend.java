package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.demo.androidapp.MyApplication;


/**
 * 任务信息实体类
 */
@Entity(tableName = "friend")
public class Friend {

    //id
    @PrimaryKey(autoGenerate = false)
    private long id;

    //friendUid
    @ColumnInfo(name = "friend_uid")
    private String uid;

    //friendUid
    @ColumnInfo(name = "friend_name")
    private String friend_name;

    //myUid
    @ColumnInfo(name = "user_uid")
    private String user_uid = MyApplication.getApplication().getUID();

    @Ignore
    public Friend() {
    }

    public Friend(long id, String uid, String friend_name, String user_uid) {
        this.id = id;
        this.uid = uid;
        this.friend_name = friend_name;
        this.user_uid = user_uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                '}';
    }
}
