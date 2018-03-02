package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

import com.xt.cfp.core.pojo.CommiProfit;

public class CommitProfitVO extends CommiProfit {

	    private BigDecimal firstRate;

	    private BigDecimal secondRate;

	    private BigDecimal thirdRate;

	    private String commiRatioType;
	    
	    private char disLevel;
	    
	    private String loginName ;
	    private String   realNameCustom;//客户姓名

	    
	    //
	  
	    private String mobileNo;//手机号
	    private String realName;//真实姓名
	    private BigDecimal   wasCommit;//被邀请人已投金额
	    private Date   turnTime;//
	    private String   level;//
	   
	    
	    
	    
	    
		public String getRealNameCustom() {
			return realNameCustom;
		}

		public void setRealNameCustom(String realNameCustom) {
			this.realNameCustom = realNameCustom;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public Date getTurnTime() {
			return turnTime;
		}

		public void setTurnTime(Date turnTime) {
			this.turnTime = turnTime;
		}

		public BigDecimal getWasCommit() {
			return wasCommit;
		}

		public void setWasCommit(BigDecimal wasCommit) {
			this.wasCommit = wasCommit;
		}

		public String getMobileNo() {
			return mobileNo;
		}

		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}

		public String getRealName() {
			return realName;
		}

		public void setRealName(String realName) {
			this.realName = realName;
		}


	    private String commiPaidNode ;
	    

		public BigDecimal getFirstRate() {
			return firstRate;
		}

		public void setFirstRate(BigDecimal firstRate) {
			this.firstRate = firstRate;
		}

		public BigDecimal getSecondRate() {
			return secondRate;
		}

		public void setSecondRate(BigDecimal secondRate) {
			this.secondRate = secondRate;
		}

		public BigDecimal getThirdRate() {
			return thirdRate;
		}

		public void setThirdRate(BigDecimal thirdRate) {
			this.thirdRate = thirdRate;
		}

		public String getCommiRatioType() {
			return commiRatioType;
		}

		public void setCommiRatioType(String commiRatioType) {
			this.commiRatioType = commiRatioType;
		}

		public char getDisLevel() {
			return disLevel;
		}

		public void setDisLevel(char disLevel) {
			this.disLevel = disLevel;
		}

		public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getCommiPaidNode() {
			return commiPaidNode;
		}

		public void setCommiPaidNode(String commiPaidNode) {
			this.commiPaidNode = commiPaidNode;
		}
	    
	    
}
