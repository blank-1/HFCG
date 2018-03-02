package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.CustomerCard;

public class CustomerCardVO extends CustomerCard {
	private String shortName;
	private String bankName;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}