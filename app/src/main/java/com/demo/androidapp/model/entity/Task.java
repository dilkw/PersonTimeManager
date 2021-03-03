package com.demo.androidapp.model.entity;


import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.demo.androidapp.MyApplication;

import java.io.Serializable;


/**
 * 任务信息实体类
 */
@Entity(tableName = "task")
public class Task implements Serializable {

    //id
    @PrimaryKey(autoGenerate = false)
    private long id;

    //创建时间
    @ColumnInfo(name = "created_at")
    private long created_at;

    //用户uid
//    @ColumnInfo(name = "uid")
//    private String userId;

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
    private long time;

    //提醒
    @ColumnInfo(name = "alert")
    private boolean alert;

    //重复做
    @ColumnInfo(name = "redo")
    private boolean redo;

    @Ignore
    public Task() {
    }

    public Task(Long id, long created_at, String task, String category, boolean state, long time, boolean alert, boolean redo) {
        this.id = id;
        this.created_at = created_at;
//        this.userId = userId;
        this.task = task;
        this.category = category;
        this.state = state;
        this.time = time;
        this.alert = alert;
        this.redo = redo;
//        if (userId == null || userId.equals("")) {
//            Log.d("imageView", "Task: 添加uid");
//            this.userId = MyApplication.getApplication().getUID();
//        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

//    public String getUserId() {
//        Log.d("imageView", "Task: 获取uid");
//        if (userId == null || userId.equals("")) {
//            Log.d("imageView", "Task: 添加uid");
//            this.userId = MyApplication.getApplication().getUID();
//        }
//        return userId;
//    }

//    public void setUserId(String userId) {
//        Log.d("imageView", "Task: 设置uid");
//        this.userId = userId;
//    }

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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
//                ", userId='" + userId + '\'' +
                ", task='" + task + '\'' +
                ", category='" + category + '\'' +
                ", state=" + state +
                ", time=" + time +
                ", alert=" + alert +
                ", redo=" + redo +
                '}';
    }
}
