package com.demo.androidapp.model;

public class ResetPwdModel {

//    Email           string `form:"email" json:"email" binding:"required,max=255"`
//    Password        string `form:"password" json:"password" binding:"required,min=8,max=20"`
//    PasswordConfirm string `form:"password_confirm" json:"password_confirm" binding:"required,min=8,max=20"`
//    Code            string `form:"code" json:"code" binding:"required,min=6,max=6"`

    private String email;               //邮箱

    private String password;            //新密码

    private String password_confirm;    //确认新密码

    private String code;                //验证码

    public ResetPwdModel() {
    }

    public ResetPwdModel(String email, String password, String password_confirm, String code) {
        this.email = email;
        this.password = password;
        this.password_confirm = password_confirm;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirm() {
        return password_confirm;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResetPwdModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password_confirm='" + password_confirm + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
