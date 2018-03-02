/**
 * 
 */
package com.xt.cfp.core.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lianghuan
 *
 */
public class FinancePlanAgreement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9055703784383847759L;

	private Long financeAgreementId;
	
	private Long lendOrderId ;
	
	private String financeAgreementCode ;
	
	private String financeAgreementName ;
	
	private String storgePath ;
	
	private String href ;
	
	private Integer version ;
	
	private Date createTime ;
	
	private  char financeAgreementType ;
	
	private char financeAgreementStatus ;

	public Long getFinanceAgreementId() {
		return financeAgreementId;
	}

	public void setFinanceAgreementId(Long financeAgreementId) {
		this.financeAgreementId = financeAgreementId;
	}

	public Long getLendOrderId() {
		return lendOrderId;
	}

	public void setLendOrderId(Long lendOrderId) {
		this.lendOrderId = lendOrderId;
	}

	public String getFinanceAgreementCode() {
		return financeAgreementCode;
	}

	public void setFinanceAgreementCode(String financeAgreementCode) {
		this.financeAgreementCode = financeAgreementCode;
	}

	public String getFinanceAgreementName() {
		return financeAgreementName;
	}

	public void setFinanceAgreementName(String financeAgreementName) {
		this.financeAgreementName = financeAgreementName;
	}

	public String getStorgePath() {
		return storgePath;
	}

	public void setStorgePath(String storgePath) {
		this.storgePath = storgePath;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public char getFinanceAgreementType() {
		return financeAgreementType;
	}

	public void setFinanceAgreementType(char financeAgreementType) {
		this.financeAgreementType = financeAgreementType;
	}

	public char getFinanceAgreementStatus() {
		return financeAgreementStatus;
	}

	public void setFinanceAgreementStatus(char financeAgreementStatus) {
		this.financeAgreementStatus = financeAgreementStatus;
	}
	
	
}
