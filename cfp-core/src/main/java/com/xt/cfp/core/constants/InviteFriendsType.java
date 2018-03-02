package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum InviteFriendsType implements EnumsCanDescribed {
	LINK_INVITE("0", "链接邀请"), 
	EMAIL_INVITE("1", "邮件邀请"), 
	OTHER_INVITE("2", "其他方式邀请"), ;

	private final String value;
	private final String desc;

	private InviteFriendsType(String value, String desc) {
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
}
