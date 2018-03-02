package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

import com.xt.cfp.core.pojo.LendProduct;

/**
 * 出借产品
 */
public class LendProductVO extends LendProduct {
	private BigDecimal sumPublishBalance = BigDecimal.ZERO;
	private BigDecimal sumSaidBalance = BigDecimal.ZERO;
	private String maxPublishCode;
	private String searchTimeLimit;
	private String searchProductState;
	  //当前时间、用于查询操作
    private Date now; 

	public BigDecimal getSumPublishBalance() {
		return sumPublishBalance;
	}

	public void setSumPublishBalance(BigDecimal sumPublishBalance) {
		this.sumPublishBalance = sumPublishBalance;
	}

	public BigDecimal getSumSaidBalance() {
		return sumSaidBalance;
	}

	public void setSumSaidBalance(BigDecimal sumSaidBalance) {
		this.sumSaidBalance = sumSaidBalance;
	}

	public String getMaxPublishCode() {
		return maxPublishCode;
	}

	public void setMaxPublishCode(String maxPublishCode) {
		this.maxPublishCode = maxPublishCode;
	}

	public String getSearchTimeLimit() {
		return searchTimeLimit;
	}

	public void setSearchTimeLimit(String searchTimeLimit) {
		this.searchTimeLimit = searchTimeLimit;
	}

	public String getSearchProductState() {
		return searchProductState;
	}

	public void setSearchProductState(String searchProductState) {
		this.searchProductState = searchProductState;
	}

	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
	}
	
}
