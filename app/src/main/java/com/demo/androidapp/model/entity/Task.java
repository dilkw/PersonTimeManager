package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.demo.androidapp.MyApplication;


/**
 * 任务信息实体类
 */
@Entity(tableName = "task")
public class Task {

    //id
    @PrimaryKey(autoGenerate = false)
    private long id;

    //创建时间
    @ColumnInfo(name = "created_at")
    private long created_at;

    //用户uid
    @ColumnInfo(name = "uid")
    private String userId = MyApplication.getApplication().getUID();

    //任务内容
    @ColumnInfo(name = "task")
    private String task;

    //分类
    @ColumnInfo(name = "category")
    private String category = "未分类";

    //状态
    @ColumnInfo(name = "state")
    private boolean state;

    //结束时间
    @ColumnInfo(name = "end_time")
    private long end_time;

    //提醒
    @ColumnInfo(name = "alert")
    private boolean alert;

    //结束时间
    @ColumnInfo(name = "alert_time")
    private long alert_time;

    @Ignore
    public Task() {
    }

    public Task(long id, long created_at, String userId, String task, String category, boolean state, long end_time, boolean alert, long alert_time) {
        this.id = id;
        this.created_at = created_at;
        this.userId = userId;
        this.task = task;
        this.category = category;
        this.state = state;
        this.end_time = end_time;
        this.alert = alert;
        this.alert_time = alert_time;
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public long getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(long alert_time) {
        this.alert_time = alert_time;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", userId='" + userId + '\'' +
                ", task='" + task + '\'' +
                ", category='" + category + '\'' +
                ", state=" + state +
                ", end_time=" + end_time +
                ", alert=" + alert +
                ", alert_time=" + alert_time +
                '}';
    }
}
