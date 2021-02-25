package com.demo.androidapp.model.common;

public enum RCodeEnum {

    //服务器对应的状态码
    OK(200,"正常"),
    ERROR(201,"网络请求错误"),
    INACTIVE(30008,"帐号未激活"),
    USERNAME_EXIT(30002,"用户名已被注册"),
    EMAIL_ERROR(30003,"邮箱地址错误"),
    USER_EMAIL_EXIT(30004,"邮箱地址已经被使用"),

    //移动端定义状态码
    EMAIL_NULL(40000,"邮箱为空"),

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
