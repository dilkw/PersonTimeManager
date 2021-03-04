package com.demo.androidapp.model.commitObject;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.demo.androidapp.model.entity.Task;

public class UpdateTaskCommit {

    private long id;

    //任务内容
    private String task;

    //分类
    private String category;

    //状态
    private boolean state;

    //结束时间
    private long time;

    //提醒
    private boolean alert;

    //重复做
    private boolean redo;

    public UpdateTaskCommit() {
    }

    public UpdateTaskCommit taskToUpdateTaskCommit(Task task) {
        return new UpdateTaskCommit(task.getId(),
                                    task.getTask(),
                                    task.getCategory(),
                                    task.getState(),
                                    task.getTime(),
                                    task.getAlert(),
                                    task.getRedo());

    }

    public UpdateTaskCommit(long id,String task, String category, boolean state, long time, boolean alert, boolean redo) {
        this.id = id;
        this.task = task;
        this.category = category;
        this.state = state;
        this.time = time;
        this.alert = alert;
        this.redo = redo;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isRedo() {
        return redo;
    }

    public void setRedo(boolean redo) {
        this.redo = redo;
    }

    @Override
    public String toString() {
        return "UpdateTaskCommit{" +
                "task='" + task + '\'' +
                ", category='" + category + '\'' +
                ", state=" + state +
                ", time=" + time +
                ", alert=" + alert +
                ", redo=" + redo +
                '}';
    }
}
