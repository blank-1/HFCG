package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;

public class RepaymentPlansAndDetailVO {
	private RepaymentPlan repaymentPlan;
	private RightsRepaymentDetail rightsRepaymentDetail;
	
	public RepaymentPlan getRepaymentPlan() {
		return repaymentPlan;
	}
	public void setRepaymentPlan(RepaymentPlan repaymentPlan) {
		this.repaymentPlan = repaymentPlan;
	}
	public RightsRepaymentDetail getRightsRepaymentDetail() {
		return rightsRepaymentDetail;
	}
	public void setRightsRepaymentDetail(RightsRepaymentDetail rightsRepaymentDetail) {
		this.rightsRepaymentDetail = rightsRepaymentDetail;
	}
}
