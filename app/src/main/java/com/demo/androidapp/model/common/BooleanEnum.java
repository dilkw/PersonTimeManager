package com.demo.androidapp.model.common;

public enum BooleanEnum {

    IS_COMPLETE(false,"未完成"),
    IS_INCOMPLETE(true,"已完成"),

    IS_NOT_ALERT(false,"否"),
    IS_ALERT(true,"是"),

    IS_REDO(true,"是"),
    IS_NOT_REDO(true,"否"),
    ;

    private boolean tf;

    private String massage;

    BooleanEnum(boolean tf, String massage) {
        this.tf = tf;
        this.massage = massage;
    }

    public boolean isTf() {
        return tf;
    }

    public void setTf(boolean tf) {
        this.tf = tf;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
