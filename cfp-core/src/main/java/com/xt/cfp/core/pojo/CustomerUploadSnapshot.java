package com.xt.cfp.core.pojo;


public class CustomerUploadSnapshot {
	
	//1已删除 0未删除
	public final static String CUSTOMERUPLOADSNAPSHOT_DELETED = "1";
	public final static String  CUSTOMERUPLOADSNAPSHOT_NOTDELETED = "0";
	
	
    private Long snapshotId;

    private Long attachId;

    private Long loanApplicationId;

    private Long seqNum;

    private String type;

    private String status;
    
    private Long mainLoanApplicationId;//借款申请主表ID

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Long getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Long seqNum) {
		this.seqNum = seqNum;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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