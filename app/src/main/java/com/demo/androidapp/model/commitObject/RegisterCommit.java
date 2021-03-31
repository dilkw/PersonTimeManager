package com.demo.androidapp.model.commitObject;

public class RegisterCommit {

    private String name;

    private String password;

    private String password_confirm;

    private String email;

    private String img_url;

    public RegisterCommit() {
    }

    public RegisterCommit(String name, String password, String password_confirm, String email,String img_url) {
        this.name = name;
        this.password = password;
        this.password_confirm = password_confirm;
        this.email = email;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "RegisterCommit{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", password_confirm='" + password_confirm + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
