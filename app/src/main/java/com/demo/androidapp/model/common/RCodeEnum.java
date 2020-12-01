package com.demo.androidapp.model.common;

public enum RCodeEnum {

    OK(200,"正常"),
    ERROE(201,"网络请求错误"),

    ;

    private int code;

    private String message;

    RCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
