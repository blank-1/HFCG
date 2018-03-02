package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * 系统异常  如： 启动参数不对
 *
 * @author leixiongfei
 * @date 2013-7-11 下午1:44:06
 */
public enum SystemErrorCode implements ErrorCode {
	PARAM_MISS("0", "参数缺失"),
	PROGRAM_ERROR_START("1", "启动错误"),
	STREAM_CAN_NOT_CLOSE("2", "无法关闭流"),
	UPLOAD_PHOTO("3","上传头像异常"),
    SESSION_NOT_EXISIT("4", "非法请求"),
    ILLEGAL_REQUEST("5", "非法请求"),
    SYSTEM_ERROR_CODE("6", "系统异常"),
    CANNOT_SET_TIMERTASK_BEFORE_NOW("7", "无法在当前时间以前设定系统定时器"),
    CANNOT_SET_TIMERTASK("8", "无法设定系统定时器"),
    UNKNOW_ERROR("9", "未知错误"),
    FILE_ERROR("10", "文件或路径不存在"),
    IO_ERROR("11", "文件IO异常"),
    FUNCTION_NOT_SUPPORT("12", "当前程序版本还未加入该功能"),
    ILLEGAL_PARAMETER("13", "非法参数"),
	;

	private String desc;
    private String code;

    private SystemErrorCode(String code, String desc) {
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
