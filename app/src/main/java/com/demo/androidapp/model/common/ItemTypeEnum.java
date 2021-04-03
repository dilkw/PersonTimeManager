package com.demo.androidapp.model.common;

public enum ItemTypeEnum {

    TEXT_ITEM_LEFT(1,"文本接收"),
    TEXT_ITEM_RIGHT(2,"文本发送"),

    TASK_ITEM_LEFT(3,"任务接收"),
    TASK_ITEM_RIGHT(4,"任务发送"),

    CLOCK_ITEM_LEFT(5,"时钟接收"),
    CLOCK_ITEM_RIGHT(6,"时钟发送"),
    ;

    private int type;

    private String massage;

    ItemTypeEnum(int type, String massage) {
        this.type = type;
        this.massage = massage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
