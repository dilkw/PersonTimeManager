package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.demo.androidapp.MyApplication;


/**
 * 任务信息实体类
 */
@Entity(tableName = "bill")
public class Bill {

    //id
    @PrimaryKey(autoGenerate = true)
    private long id;

    //创建时间
    @ColumnInfo(name = "created_time")
    private long createdTime;

    //用户uid
    @ColumnInfo(name = "uid")
    private String userId = MyApplication.getApplication().getUID();

    //任务内容
    @ColumnInfo(name = "content")
    private String content;

    //分类(支出true/收入false)
    @ColumnInfo(name = "category")
    private boolean category;

    //分类(支出/收入)
    @ColumnInfo(name = "money")
    private float money;


    @Ignore
    public Bill() {
    }

    public Bill(long id, long createdTime, String userId, String content, boolean category, float money) {
        this.id = id;
        this.createdTime = createdTime;
        this.userId = userId;
        this.content = content;
        this.category = category;
        this.money = money;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", category=" + category +
                ", money=" + money +
                '}';
    }
}
