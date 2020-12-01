package com.demo.androidapp.model.common;

import com.demo.androidapp.model.common.RCodeEnum;

public class ReturnData {

    //响应码
   private int code;

   //响应状态
   private String msg;

   //相应返回数据
   private Object content;

    public ReturnData(Object content) {
        this.code = RCodeEnum.OK.getCode();
        this.msg = RCodeEnum.OK.getMessage();
        this.content = content;
    }

    public ReturnData(RCodeEnum rCodeEnum, Object content) {
        this.code = rCodeEnum.getCode();
        this.msg = rCodeEnum.getMessage();
        this.content = content;
    }

    public ReturnData(RCodeEnum rCodeEnum) {
        this.code = rCodeEnum.getCode();
        this.msg = rCodeEnum.getMessage();
        this.content = null;
    }

    public ReturnData() {
        this.code = RCodeEnum.OK.getCode();
        this.msg = RCodeEnum.OK.getMessage();
        this.content = null;
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

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
