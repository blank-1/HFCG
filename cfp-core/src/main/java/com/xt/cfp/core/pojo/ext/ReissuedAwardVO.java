package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

import com.xt.cfp.core.pojo.AwardDetail;

public class ReissuedAwardVO extends AwardDetail {

	private BigDecimal shouldaward ;
	private BigDecimal shouldrepay ;
	
	private Long RIGHTS_REPAYMENT_DETAIL_ID;
	private Long LEND_ORDER_ID ;
	private Long LOAN_APPLICATION_ID;
	private Long USER_ACCOUNT_ID ;
	private Long USER_ID;
	private String LOGIN_NAME;
	private String LOAN_PUBLISH_TITLE;
	
	public BigDecimal getShouldaward() {
		return shouldaward;
	}
	public void setShouldaward(BigDecimal shouldaward) {
		this.shouldaward = shouldaward;
	}
	public BigDecimal getShouldrepay() {
		return shouldrepay;
	}
	public void setShouldrepay(BigDecimal shouldrepay) {
		this.shouldrepay = shouldrepay;
	}
	public Long getRIGHTS_REPAYMENT_DETAIL_ID() {
		return RIGHTS_REPAYMENT_DETAIL_ID;
	}
	public void setRIGHTS_REPAYMENT_DETAIL_ID(Long rIGHTS_REPAYMENT_DETAIL_ID) {
		RIGHTS_REPAYMENT_DETAIL_ID = rIGHTS_REPAYMENT_DETAIL_ID;
	}
	public Long getLEND_ORDER_ID() {
		return LEND_ORDER_ID;
	}
	public void setLEND_ORDER_ID(Long lEND_ORDER_ID) {
		LEND_ORDER_ID = lEND_ORDER_ID;
	}
	public Long getLOAN_APPLICATION_ID() {
		return LOAN_APPLICATION_ID;
	}
	public void setLOAN_APPLICATION_ID(Long lOAN_APPLICATION_ID) {
		LOAN_APPLICATION_ID = lOAN_APPLICATION_ID;
	}
	public Long getUSER_ACCOUNT_ID() {
		return USER_ACCOUNT_ID;
	}
	public void setUSER_ACCOUNT_ID(Long uSER_ACCOUNT_ID) {
		USER_ACCOUNT_ID = uSER_ACCOUNT_ID;
	}
	public Long getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(Long uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getLOGIN_NAME() {
		return LOGIN_NAME;
	}
	public void setLOGIN_NAME(String lOGIN_NAME) {
		LOGIN_NAME = lOGIN_NAME;
	}
	public String getLOAN_PUBLISH_TITLE() {
		return LOAN_PUBLISH_TITLE;
	}
	public void setLOAN_PUBLISH_TITLE(String lOAN_PUBLISH_TITLE) {
		LOAN_PUBLISH_TITLE = lOAN_PUBLISH_TITLE;
	}
	
	
}
