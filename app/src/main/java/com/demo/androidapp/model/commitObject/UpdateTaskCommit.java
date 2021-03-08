package com.demo.androidapp.model.commitObject;

import com.demo.androidapp.model.entity.Task;

public class UpdateTaskCommit {

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
}
