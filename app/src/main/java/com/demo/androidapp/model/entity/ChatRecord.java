package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.demo.androidapp.MyApplication;

@Entity(tableName = "chat_record")
public class ChatRecord {

    //id
    @PrimaryKey(autoGenerate = true)
    private long id;

    //创建时间
    @ColumnInfo(name = "created_at")
    private long created_at;

    //用户uid
    @ColumnInfo(name = "uid")
    private String userId = MyApplication.getApplication().getUser().getUid();

    //好友uid
    @ColumnInfo(name = "fuid")
    private String friendUid;

    //内容
    @ColumnInfo(name = "task")
    private String task;

    //内容
    @ColumnInfo(name = "type")
    private String type;

    public ChatRecord(long id, long created_at, String userId, String friendUid, String task, String type) {
        this.id = id;
        this.created_at = created_at;
        this.userId = userId;
        this.friendUid = friendUid;
        this.task = task;
        this.type = type;
    }

    @Ignore
    public ChatRecord(long created_at, String userId, String friendUid, String task, String type) {
        this.created_at = created_at;
        this.userId = userId;
        this.friendUid = friendUid;
        this.task = task;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChatRecord{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", userId='" + userId + '\'' +
                ", friendUid='" + friendUid + '\'' +
                ", task='" + task + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
