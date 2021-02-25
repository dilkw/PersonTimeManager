package com.demo.androidapp.model.common;

public class ReturnData<T> {

    //响应码
    private int code;

    //响应状态
    private String msg;

    //相应返回数据
    private T data;

    public ReturnData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ReturnData(T data) {
        this.code = RCodeEnum.OK.getCode();
        this.msg = RCodeEnum.OK.getMessage();
        this.data = data;
    }

    public ReturnData(RCodeEnum rCodeEnum, T data) {
        this.code = rCodeEnum.getCode();
        this.msg = rCodeEnum.getMessage();
        this.data = data;
    }

    public ReturnData(RCodeEnum rCodeEnum) {
        this.code = rCodeEnum.getCode();
        this.msg = rCodeEnum.getMessage();
        this.data = null;
    }

    public ReturnData() {
        this.code = RCodeEnum.OK.getCode();
        this.msg = RCodeEnum.OK.getMessage();
        this.data = null;
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
//        Gson gson = new Gson();
//        return gson.fromJson(data,Object.class);
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
