package com.demo.androidapp.model.common;

public class ReturnData<T> {

    //枚举类
    private RCodeEnum rCodeEnum;

    //相应返回数据
    private T data;

    public ReturnData(int code, String msg, T data) {
        this.rCodeEnum = RCodeEnum.returnRCodeEnumByCode(code);
        this.data = data;
    }

    public ReturnData(T data) {
        this.rCodeEnum = RCodeEnum.OK;
        this.data = data;
    }

    public ReturnData(RCodeEnum rCodeEnum, T data) {
        this.rCodeEnum = rCodeEnum;
        this.data = data;
    }

    public ReturnData(RCodeEnum rCodeEnum) {
        this.rCodeEnum = rCodeEnum;
        this.data = null;
    }

    public ReturnData() {
        this.rCodeEnum = RCodeEnum.OK;
        this.data = null;
    }

    public int getCode() {
        return this.rCodeEnum.getCode();
    }

    public String getMsg() {
        return this.rCodeEnum.getMessage();
    }

    public RCodeEnum getRCodeEnum() {
        return rCodeEnum;
    }

    public void setRCodeEnum(RCodeEnum rCodeEnum) {
        this.rCodeEnum = rCodeEnum;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnData{" +
                "code=" + rCodeEnum.getCode() +
                ", msg='" + rCodeEnum.getMessage() + '\'' +
                ", data=" + data +
                '}';
    }
}
