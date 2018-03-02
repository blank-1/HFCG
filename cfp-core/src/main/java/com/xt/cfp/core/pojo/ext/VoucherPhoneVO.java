package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.VoucherPhone;

public class VoucherPhoneVO extends VoucherPhone {
	
	private String prizeName;
	private String remark;
	
	public String getPrizeName() {
		return prizeName;
	}
	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
