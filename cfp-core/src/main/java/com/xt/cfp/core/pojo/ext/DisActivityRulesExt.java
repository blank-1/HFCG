package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.constants.DisActivityEnums;
import com.xt.cfp.core.pojo.DisActivityRules;

public class DisActivityRulesExt extends DisActivityRules {
	private String productName;// 出借产品名称

	private String targetUser ;
	
	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCommiPaidNodeStr() {// 佣金发放节点(中文)
		if (DisActivityEnums.DarCommiPaidNodeEnum.MAKELOAN.getValue().equals(this.getCommiPaidNode())) {
			return DisActivityEnums.DarCommiPaidNodeEnum.MAKELOAN.getDesc();
		} else if (DisActivityEnums.DarCommiPaidNodeEnum.REPAYMENT.getValue().equals(this.getCommiPaidNode())) {
			return DisActivityEnums.DarCommiPaidNodeEnum.REPAYMENT.getDesc();
		} else {
			return this.getCommiPaidNode();
		}
	}

}
