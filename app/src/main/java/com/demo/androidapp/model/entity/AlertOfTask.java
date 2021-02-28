package com.demo.androidapp.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "task_alert")
public class AlertOfTask {

    @PrimaryKey (autoGenerate = true)
    private long id;

    @ColumnInfo(name = "task_id")
    private long taskId;

    @ColumnInfo(name = "alert_time")
    private Date alertTime;

    @ColumnInfo(name = "uid")
    private String uid;

    public AlertOfTask(long id, long taskId, Date alertTime, String uid) {
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

    public Date getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Date alertTime) {
        this.alertTime = alertTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
