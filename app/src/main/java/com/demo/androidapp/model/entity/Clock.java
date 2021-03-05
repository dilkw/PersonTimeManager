package com.demo.androidapp.model.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.demo.androidapp.MyApplication;


/**
 * 任务信息实体类
 */
@Entity(tableName = "clock")
public class Clock {

    //id
    @PrimaryKey(autoGenerate = true)
    private long id;

    //创建时间
    @ColumnInfo(name = "created_at")
    private long created_at;

    //创建时间
    @ColumnInfo(name = "clock_minuet")
    private long clockMinuet;

    //用户uid
    @ColumnInfo(name = "uid")
    private String userId = MyApplication.getApplication().getUID();

    //任务内容
    @ColumnInfo(name = "task")
    private String task;

    //状态(完成/未完成)
    @ColumnInfo(name = "state")
    private boolean state;

    //提醒
    @ColumnInfo(name = "alert")
    private boolean alert;

    //提醒
    @ColumnInfo(name = "alert_time")
    private long alertTime;

    @Ignore
    public Clock() {
    }

    public Clock(long id, long created_at, long clockMinuet, String userId, String task, boolean state, boolean alert, long alertTime) {
        this.id = id;
        this.created_at = created_at;
        this.clockMinuet = clockMinuet;
        this.userId = userId;
        this.task = task;
        this.state = state;
        this.alert = alert;
        this.alertTime = alertTime;
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

    public long getClockMinuet() {
        return clockMinuet;
    }

    public void setClockMinuet(long clockMinuet) {
        this.clockMinuet = clockMinuet;
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public long getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(long alertTime) {
        this.alertTime = alertTime;
    }


    @Override
    public String toString() {
        return "Clock{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", clockMinuet=" + clockMinuet +
                ", userId='" + userId + '\'' +
                ", task='" + task + '\'' +
                ", state=" + state +
                ", alert=" + alert +
                ", alertTime=" + alertTime +
                '}';
    }
}
