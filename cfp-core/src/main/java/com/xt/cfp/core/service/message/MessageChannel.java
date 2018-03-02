package com.xt.cfp.core.service.message;

import com.xt.cfp.core.enums.EnumsCanDescribed;



public enum MessageChannel implements EnumsCanDescribed {

	MSG("msg", "短信渠道"), 
	EMAIL_MSG("emailmsg", "邮件渠道"),
	WECHAT_MSG("wechatmsg", "微信渠道");

	private final String value;
	private final String desc;

	MessageChannel(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String getValue() {
		return value;
	}

	public static String[] getAllChannel() {
		MessageChannel[] values = MessageChannel.values();
		String[] array = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			array[i] = values[i].getValue();
		}
		return array;
	}
}
