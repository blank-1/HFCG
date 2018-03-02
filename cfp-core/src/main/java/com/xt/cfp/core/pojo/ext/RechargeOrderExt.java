package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.RechargeOrder;

/**
 * Created by lenovo on 2015/6/26.
 */
public class RechargeOrderExt extends RechargeOrder {
    private String loginName;
    private String realName;
    private String channelName;
    private String shortBank;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getShortBank() {
		return shortBank;
	}

	public void setShortBank(String shortBank) {
		this.shortBank = shortBank;
	}

}
