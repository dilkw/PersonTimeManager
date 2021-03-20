package com.demo.androidapp.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.demo.androidapp.MyApplication;

@Entity(tableName = "task_alert")
public class AlertOfTask {

    @PrimaryKey (autoGenerate = true)
    private long id;

    @ColumnInfo(name = "task_id")
    private long taskId;

    @ColumnInfo(name = "alert_time")
    private String alertTime;

    @ColumnInfo(name = "uid")
    private String uid = MyApplication.getUser().getUid();

    @Ignore
    public AlertOfTask(){}

    public AlertOfTask(long id, long taskId, String alertTime, String uid) {
        this.id = id;
        this.taskId = taskId;
        this.alertTime = alertTime;
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
