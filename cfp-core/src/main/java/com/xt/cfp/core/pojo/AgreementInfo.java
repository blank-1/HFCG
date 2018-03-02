package com.xt.cfp.core.pojo;

import java.util.Date;

public class AgreementInfo {
    private Long agreementId;

    private String agreementCode;

    private String agreementName;

    private Date createTime;

    private String agreementType;

    private Long creditorRightsId;

    private Long newRightsId;

    private String agreementStatus;

    private String storgePath;

    private String href;

    //债权转让合同号码
    private String agreementAssignmentCode ;
    
    //合同版本号
    private Integer version ;
    
    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public String getAgreementCode() {
        return agreementCode;
    }

    public void setAgreementCode(String agreementCode) {
        this.agreementCode = agreementCode == null ? null : agreementCode.trim();
    }

    public String getAgreementName() {
        return agreementName;
    }

    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName == null ? null : agreementName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType == null ? null : agreementType.trim();
    }

    public Long getCreditorRightsId() {
        return creditorRightsId;
    }

    public void setCreditorRightsId(Long creditorRightsId) {
        this.creditorRightsId = creditorRightsId;
    }

    public Long getNewRightsId() {
        return newRightsId;
    }

    public void setNewRightsId(Long newRightsId) {
        this.newRightsId = newRightsId;
    }

    public String getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(String agreementStatus) {
        this.agreementStatus = agreementStatus == null ? null : agreementStatus.trim();
    }

    public String getStorgePath() {
        return storgePath;
    }

    public void setStorgePath(String storgePath) {
        this.storgePath = storgePath == null ? null : storgePath.trim();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

	/**
	 * @return the agreementAssignmentCode
	 */
	public String getAgreementAssignmentCode() {
		return agreementAssignmentCode;
	}

	/**
	 * @param agreementAssignmentCode the agreementAssignmentCode to set
	 */
	public void setAgreementAssignmentCode(String agreementAssignmentCode) {
		this.agreementAssignmentCode = agreementAssignmentCode;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}