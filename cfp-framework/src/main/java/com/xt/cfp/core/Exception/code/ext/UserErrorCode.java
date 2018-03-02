package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * Created by luqinglin on 2015/6/12.
 */
public enum UserErrorCode implements ErrorCode {
    MOBILENO_EXIST("0", "手机号已存在"),
    LOGINNAME_EXIST("1", "用户名已存在"),
    ERROR_OLD_PASSWORD("2", "错误的原密码"),
    USER_NOT_EXIST("3", "用户不存在"),
    LONGIN_EXIST("4", "还未登录"),
    USERTYPE_NOT_EXIST("5", "不存在的用户类型"),
    VALIDATE_CODE_NULL("6", "请重新发送短信验证码"),
    MOBILENO_NOT_EXIST("7", "手机号未注册"),
    LOGINNAME_NOT_EXIST("8", "用户名未注册"),
    ERROR_PASS_LOGIN("9", "用户名密码不匹配"),
    VALIDATE_CODE_ERROR("10", "短信验证码错误"),
    NO_BIDPASS("7", "还未设定交易密码"),
    ERROR_BIDPASS("8", "交易密码错误"),
    MOBILENO_NOEXIST("11", "手机号不存在"),
    HAS_NOT_IDVERIFIED("12", "还未进行身份认证"),
    CAN_NOT_LOGIN("13", "该用户无法登陆网站"),
    STATUS_IS_NOT_NORMAL("14", "用户状态异常"),
    REGIST_INFO_ILLEGAL("15", "用户注册信息不合法"),
    INFO_NOT_EQUAL("16", "信息不相符"),
    INVITECODE_NOT_EXIST("17", "邀请码不存在"),
    USER_AUTHENTICATION_TIMEOUT("18", "用户身份验证超时"),
    IMGAE_CODE_NOT_EXIST("19","图片验证码不存在"),
    IMGAE_CODE_TIME_OUT("20","图片验证码已过期"),
    IMGAE_CODE_ERROR("21","图片验证码错误"),
    MOBILE_SMSCODE_MAX("22","短信验证码达到上限"),
    HAS_NO_CARD_AVALIBLE("23","没有可用的银行卡"),
    STATUS_IS_INVALID("24","用户登录状态失效"),
    USERTYPE_ERROR("25", "用户类型错误"),
    LOGIN_ON_OTHERS("26", "已在其它设备上登录"),
    HAS_NOT_BIND_HF("27", "未在恒丰平台上开户"),
    ;

    private final String code;
    private final String desc;

    UserErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
