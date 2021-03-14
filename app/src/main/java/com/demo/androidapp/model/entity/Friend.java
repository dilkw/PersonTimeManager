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

    //fuid
    @ColumnInfo(name = "fuid")
    private String fuid;

    //friendName
    @ColumnInfo(name = "friend_name")
    private String friend_name;

    //myUid
    @ColumnInfo(name = "uid")
    private String uid;

    @Ignore
    public Friend() {
    }

    public Friend(long id, String fuid, String friend_name, String uid) {
        this.id = id;
        this.fuid = fuid;
        this.friend_name = friend_name;
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFuid() {
        return fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
                ", uid='" + fuid + '\'' +
                '}';
    }
}
