package com.demo.androidapp.model.entity;


import androidx.databinding.BindingBuildInfo;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;


/**
 * 任务信息实体类
 */
@Entity(tableName = "task")
public class Task {

    //id
    @PrimaryKey(autoGenerate = true)
    private Long id;

    //创建时间
    @ColumnInfo(name = "created_at")
    private Date created_at;

    //更新时间
    @ColumnInfo(name = "updated_at")
    private Date updatedTime;

    //删除时间
    @ColumnInfo(name = "deleted_at")
    private Date deletedTime;

    //用户uid
    @ColumnInfo(name = "uid")
    private String userId;

    //任务内容
    @ColumnInfo(name = "task")
    private String task;

    //分类
    @ColumnInfo(name = "category")
    private String category;

    //状态
    @ColumnInfo(name = "state")
    private boolean state;

    //结束时间
    @ColumnInfo(name = "time")
    private Date time;

    //提醒
    @ColumnInfo(name = "alert")
    private boolean alert;

    //重复做
    @ColumnInfo(name = "redo")
    private boolean redo;

    @Ignore
    public Task() {
    }

    public Task(Long id, Date created_at, Date updatedTime, Date deletedTime, String userId, String task, String category, boolean state, Date time, boolean alert, boolean redo) {
        this.id = id;
        this.created_at = created_at;
        this.updatedTime = updatedTime;
        this.deletedTime = deletedTime;
        this.userId = userId;
        this.task = task;
        this.category = category;
        this.state = state;
        this.time = time;
        this.alert = alert;
        this.redo = redo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
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

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean getAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean getRedo() {
        return redo;
    }

    public void setRedo(boolean redo) {
        this.redo = redo;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", created_at=" + created_at +
                ", updatedTime=" + updatedTime +
                ", deletedTime=" + deletedTime +
                ", userId='" + userId + '\'' +
                ", task='" + task + '\'' +
                ", category='" + category + '\'' +
                ", state=" + state +
                ", time=" + time +
                ", alert=" + alert +
                ", redo=" + redo +
                '}';
    }
}
