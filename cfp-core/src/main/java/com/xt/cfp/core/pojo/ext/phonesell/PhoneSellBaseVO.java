package com.xt.cfp.core.pojo.ext.phonesell;

import java.util.List;

public class PhoneSellBaseVO {
	
	private String adminCode;
	private String adminName;
	private String userCode;
	private String userName;
	private String time;
	private String amount;
	private String status;
	private List<String> nums;
	
	public String getAdminCode() {
		return adminCode;
	}
	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getNums() {
		return nums;
	}
	public void setNums(List<String> nums) {
		this.nums = nums;
	}
}
