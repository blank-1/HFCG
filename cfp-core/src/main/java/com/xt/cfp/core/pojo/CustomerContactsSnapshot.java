package com.xt.cfp.core.pojo;

/**
 * 客户联系人快照
 */
public class CustomerContactsSnapshot {
    private Long snapshotId;//客户快照ID
    private Long loanApplicationId;//借款申请ID
    private String concactName;//联系人姓名
    private String concatPhone;//联系人手机号
    private String relationType;//关系类型
    private String relation;//关系
    private String status;//状态
    private Long mainLoanApplicationId;//借款申请主表ID

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getConcactName() {
        return concactName;
    }

    public void setConcactName(String concactName) {
        this.concactName = concactName == null ? null : concactName.trim();
    }

    public String getConcatPhone() {
        return concatPhone;
    }

    public void setConcatPhone(String concatPhone) {
        this.concatPhone = concatPhone == null ? null : concatPhone.trim();
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType == null ? null : relationType.trim();
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation == null ? null : relation.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    
    public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}
}