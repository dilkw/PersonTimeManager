package com.demo.androidapp.model.common;

public enum RCodeEnum {

    //服务器对应的状态码
    OK(200,"正常"),
    DB_OK(2000,"DB正常"),
    GET_TASKS_SERVER_OK(2001,"SERVER正常"),

    ERROR(201,"请求错误"),
    DATA_ERROR(202,"提交数据错误"),
    DB_ERROR(2010,"DB请求错误"),
    SERVER_ERROR(2011,"SERVER请求错误"),

    //移动端定义状态码
    EMAIL_NULL(40000,"邮箱为空"),
    DELETE_TASK_ERROR(40001,"删除失败"),

//    ERROR(500, "fail"),

    INVALID_PARAMS(400, "invalid params"),

    ERROR_AUTH_CHECK_TOKEN_FAI(10001, "Token鉴权失败"),

    ERROR_AUTH_CHECK_TOKEN_TIMEOUT(10002, "Token已超时"),

    ERROR_AUTH_TOKEN(10003, "Token生成失败"),

    ERROR_AUTH(10004, "Token错误"),

    ERROR_PASSWORD_CONFIRM(30001,"两次密码输入不一致"),

    ERROR_NICKNAME_USED(30002, "用户名已经被使用"),

    ERROR_EMAIL_ADDRESS(30003, "邮箱地址有误"),

    ERROR_EMAIL_USED(30004, "邮箱地址已经被使用"),

    ERROR_ENCRYPT_PASSWORD(30005, "密码加密失败"),

    ERROR_SIGN_UP(30006, "注册失败"),

    ERROR_ACCOUNT_PARAM(30007, "账号或密码错误"),

    ERROR_ACCOUNT_INACTIVE(30008, "账号未激活"),

    ERROR_ACCOUNT_SUSPEND(30009, "账号被封禁"),

    ERROR_SIGN_IN(30010, "登录失败"),

    ERROR_ACCOUNT_ACTIVE(30011, "激活帐户失败"),

    ERROR_VALID_CODE(30012, "错误验证码"),

    ERROR_ACCOUNT_RESET(30013, "用户信息修改失败"),

    ERROR_FOUND_USER(30014, "找不到该用户"),

    BAN_ACCOUNT_RESET(30015, "账号重置冻结"),

    ERROR_ADD_TODO(30500, "任务添加失败"),

    NULL_TODO(30501, "任务列表为空"),

    ERROR_FOUND_TODO(30502, "找不到任务"),

    ERROR_DELETE_TODO(30503, "删除任务错误"),

    ERROR_EDIT_TODO(30504, "编辑失败"),

    ERROR_ADD_SELF(31000, "不能添加自己为好友"),

    NULL_FRIEND(31001, "好友为空"),

    EXIST_FRIEND(31002, "已经是好友"),

    ERROR_ADD_FRIEND(31003, "添加好友失败"),

    PENDING_ADD_FRIEND(31004, "好友等待通过"),

    NULL_NEW_FRIEND(31005, "新好友为空"),

    ERROR_DELE_SELF(31006, "不能删除自己"),

    ERROR_DELE_FRIEND(31007, "删除好友失败"),

    NULL_CLOCK(31500, "时钟为空"),

    ERROR_ADD_CLOCK(31501, "添加时钟失败"),

    ERROR_FOUND_CLOCK(31502, "找不到时钟"),

    ERROR_EDIT_CLOCK(31503, "修改时钟失败"),

    NULL_BILL(32000, "账单为空"),

    ERROR_FOUND_BILL(32001, "找不到账单"),

    ERROR_ADD_BILL(32002, "添加账单失败"),

    ERROR_EDIT_BILL(32003, "修改账单失败"),

    ERROR_DELE_BILL(32004, "删除账单错误"),
    ;

    public int code;

    private String message;

    RCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RCodeEnum returnRCodeEnumByCode(int code) {
        for (RCodeEnum rCodeEnum : values()) {
            if (rCodeEnum.getCode() == code) {
                return rCodeEnum;
            }
        }
        return OK;
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
