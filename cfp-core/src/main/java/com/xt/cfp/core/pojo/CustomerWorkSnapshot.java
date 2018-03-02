package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 客户工作信息快照
 */
public class CustomerWorkSnapshot {
    private Long snapshotId;//客户快照ID
    private Long loanApplicationId;//借款申请ID
    private Long workingAddr;//工作单位地址
    private String companyName;//工作单位名称
    private String companyNature;//工作单位性质
    private String companyPhone;//单位电话
    private String post;//职务
    private Date joinDate;//入职日期
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

    public Long getWorkingAddr() {
        return workingAddr;
    }

    public void setWorkingAddr(Long workingAddr) {
        this.workingAddr = workingAddr;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyNature() {
        return companyNature;
    }

    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature == null ? null : companyNature.trim();
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone == null ? null : companyPhone.trim();
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post == null ? null : post.trim();
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
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