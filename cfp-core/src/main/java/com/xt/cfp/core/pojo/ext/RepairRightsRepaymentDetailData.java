package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

public class RepairRightsRepaymentDetailData {

	private BigDecimal errorproportion  ;
	private Long newrightsid ;
	
	private BigDecimal newproportion ;
	
	private BigDecimal oldproportion ;
	
	private Long oldrightsid ;

	public BigDecimal getErrorproportion() {
		return errorproportion;
	}

	public void setErrorproportion(BigDecimal errorproportion) {
		this.errorproportion = errorproportion;
	}

	public Long getNewrightsid() {
		return newrightsid;
	}

	public void setNewrightsid(Long newrightsid) {
		this.newrightsid = newrightsid;
	}

	public BigDecimal getNewproportion() {
		return newproportion;
	}

	public void setNewproportion(BigDecimal newproportion) {
		this.newproportion = newproportion;
	}

	public BigDecimal getOldproportion() {
		return oldproportion;
	}

	public void setOldproportion(BigDecimal oldproportion) {
		this.oldproportion = oldproportion;
	}

	public Long getOldrightsid() {
		return oldrightsid;
	}

	public void setOldrightsid(Long oldrightsid) {
		this.oldrightsid = oldrightsid;
	}
	
	
	
}
