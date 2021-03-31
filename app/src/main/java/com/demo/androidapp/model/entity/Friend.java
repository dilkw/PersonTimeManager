package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


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
    @ColumnInfo(name = "fname")
    private String fname;

    //myUid
    @ColumnInfo(name = "uid")
    private String uid;

    //好友头像
    @ColumnInfo(name = "fimgurl")
    private String fimgurl;

    @Ignore
    public Friend() {
    }

    public Friend(long id, String fuid, String fname, String uid,String fimgurl) {
        this.id = id;
        this.fuid = fuid;
        this.fname = fname;
        this.uid = uid;
        this.fimgurl = fimgurl;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFimgurl() {
        return fimgurl;
    }

    public void setFimgurl(String fimgurl) {
        this.fimgurl = fimgurl;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", fuid='" + fuid + '\'' +
                ", fname='" + fname + '\'' +
                ", uid='" + uid + '\'' +
                ", fimgurl='" + fimgurl + '\'' +
                '}';
    }
}
