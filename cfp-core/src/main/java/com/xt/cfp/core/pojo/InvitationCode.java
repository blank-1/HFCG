package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

public class InvitationCode {
    private Long invitationId;

    private Long userId;

    private String type;

    private String invitationCode;
    private String recUserId;
    
    private String phone;//员工电话
    private String realName ;
   
	private Long staffId;//员工ID
    private String status;//员工状态
    private String create_date;//员工状态
    private String franchiseName;//加盟商ID
    
	private Long franchiseId;//加盟商ID
    private String  countMoneny;//总金额
    private int  countUser;//总客户数
    private String  getCount;//【投标客户数
    private String  count;//订单数量
    private String  onCount;//折标金额
    
    private BigDecimal  amount;//购买金额
    private BigDecimal  amountArount;//购买周期
    
    private Integer  crruentUser;//当月新增客户
    private String  crruentUseramount;//当月新增投资

    
    public Integer getCrruentUser() {
		return crruentUser;
	}

	public void setCrruentUser(Integer crruentUser) {
		this.crruentUser = crruentUser;
	}

	public String getCrruentUseramount() {
		return crruentUseramount;
	}

	public void setCrruentUseramount(String crruentUseramount) {
		this.crruentUseramount = crruentUseramount;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
    public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountArount() {
		return amountArount;
	}

	public void setAmountArount(BigDecimal amountArount) {
		this.amountArount = amountArount;
	}

	public String getRecUserId() {
		return recUserId;
	}

	public void setRecUserId(String recUserId) {
		this.recUserId = recUserId;
	}

	
    public String getFranchiseName() {
		return franchiseName;
	}

	public void setFranchiseName(String franchiseName) {
		this.franchiseName = franchiseName;
	}


    public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Long getFranchiseId() {
		return franchiseId;
	}

	public void setFranchiseId(Long franchiseId) {
		this.franchiseId = franchiseId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getCountMoneny() {
		return countMoneny;
	}

	public void setCountMoneny(String countMoneny) {
		this.countMoneny = countMoneny;
	}

	public int getCountUser() {
		return countUser;
	}

	public void setCountUser(int countUser) {
		this.countUser = countUser;
	}

	public String getGetCount() {
		return getCount;
	}

	public void setGetCount(String getCount) {
		this.getCount = getCount;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getOnCount() {
		return onCount;
	}

	public void setOnCount(String onCount) {
		this.onCount = onCount;
	}

	

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }
}