package com.demo.androidapp.model.common;

public enum StateEnum {

    // Active 激活用户
    //Active string = "active"
    ACTIVE("active","激活"),

    // Inactive 未激活用户
    //Inactive string = "inactive"
    INACTIVE("inactive","未激活"),


    // Suspend 被封禁用户
    //Suspend string = "suspend"
    SUSPEND("suspend","封禁");

    public String stateCode;

    private String stateMsg;

    StateEnum(String stateCode, String stateMsg) {
        this.stateCode = stateCode;
        this.stateMsg = stateMsg;
    }

    public static StateEnum returnStateEnumByStateCode(String stateCode) {
        for (StateEnum stateEnum : values()) {
            if (stateEnum.getStateCode().equals(stateCode)) {
                return stateEnum;
            }
        }
        return ACTIVE;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }
}
