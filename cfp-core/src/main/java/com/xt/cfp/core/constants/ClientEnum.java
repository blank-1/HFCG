package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;
import com.xt.cfp.core.pojo.UserInfo;

public enum ClientEnum implements EnumsCanDescribed {

	WEB_CLIENT("0", "WEB", "SOURCE_PC"), 
	WAP_CLIENT("1", "WAP", "SOURCE_WEIXIN"),
	IOS_CLIENT("1", "IOS", "SOURCE_IOS"),
	ANDROID_CLIENT("1", "ANDROID", "SOURCE_ANDRIOD"),
	;

	private final String value;//分类0是连连的web，1是连连的wap。
	private final String desc;//描述
	private final String resource;//来源(对应properties配置文件中的key)

	private ClientEnum(String value, String desc, String resource) {
		this.value = value;
		this.desc = desc;
		this.resource = resource;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String getValue() {
		return value;
	}

	public String getResource() {
		return resource;
	}

	/**
	 * 默认微信来源，如果是APP接口来源赋相应的值。
	 */
	public static ClientEnum from(String platform) {
		ClientEnum clientEnum = ClientEnum.WAP_CLIENT;
		if (UserSource.ISO.getValue().equals(platform)) {
			clientEnum = ClientEnum.IOS_CLIENT;
		} else if (UserSource.ANDROID.getValue().equals(platform)) {
			clientEnum = ClientEnum.ANDROID_CLIENT;
		}
		return clientEnum;
	}
}
