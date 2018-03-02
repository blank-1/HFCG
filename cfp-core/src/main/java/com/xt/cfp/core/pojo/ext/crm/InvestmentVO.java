package com.xt.cfp.core.pojo.ext.crm;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lenovo on 2015/12/6.
 */
public class InvestmentVO {
    //订单号
    private String orderNo;
    //是否首投
    private String isFirst;
    //产品名称
    private String productName;
    //购买金额
    private BigDecimal buyBalance;
    //客户登陆名
    private String customeLoginName;
    //客户姓名
    private String customeName;
    //客户手机号
    private String mobileNo;
    //购买时间
    private Date butTime;
    //来源
    private String source;
    //订单状态
    private String status;
    //加盟商
    private String fname;
    //邀请人
    private String staffName;
    //组织机构名称
    private String orgName;
    //根id
    private Long genId;
    private String genName;

    private Long userId;
    private Long lendOrderId;

    
    private char dueTimeType;
    
    public Long getGenId() {
        return genId;
    }

    public void setGenId(Long genId) {
        this.genId = genId;
    }

    public String getGenName() {
        return genName;
    }

    public void setGenName(String genName) {
        this.genName = genName;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getBuyBalance() {
        return buyBalance;
    }

    public void setBuyBalance(BigDecimal buyBalance) {
        this.buyBalance = buyBalance;
    }

    public String getCustomeLoginName() {
        return customeLoginName;
    }

    public void setCustomeLoginName(String customeLoginName) {
        this.customeLoginName = customeLoginName;
    }

    public String getCustomeName() {
        return customeName;
    }

    public void setCustomeName(String customeName) {
        this.customeName = customeName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getButTime() {
        return butTime;
    }

    public void setButTime(Date butTime) {
        this.butTime = butTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public char getDueTimeType(){
		return dueTimeType;
    }
	public void setDueTimeType(char dueTimeType) {
		this.dueTimeType=dueTimeType;		
	}
}
