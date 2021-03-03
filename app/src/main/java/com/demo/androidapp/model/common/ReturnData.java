package com.demo.androidapp.model.common;

import android.util.Log;

public class ReturnData<T>{

    private int code;

    private String msg;

    //相应返回数据
    private T data;

    public ReturnData(int code, String msg, T data) {
        Log.d("imageView", "ReturnData: 构造code + msg + data");
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
