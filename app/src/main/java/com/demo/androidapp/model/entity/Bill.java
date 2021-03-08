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
    @PrimaryKey(autoGenerate = false)
    private long id;

    //创建时间
    @ColumnInfo(name = "consume_time")
    private long consume_time;

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
    @ColumnInfo(name = "amount")
    private float amount;


    @Ignore
    public Bill() {
    }

    public Bill(long id, long consume_time, String userId, String content, boolean category, float amount) {
        this.id = id;
        this.consume_time = consume_time;
        this.userId = userId;
        this.content = content;
        this.category = category;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConsume_time() {
        return consume_time;
    }

    public void setConsume_time(long consume_time) {
        this.consume_time = consume_time;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", createdTime=" + consume_time +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", category=" + category +
                ", money=" + amount +
                '}';
    }
}
