package com.xt.cfp.core.pojo.ext;

import java.util.List;

public class IncreaseVO {
	
	private List<VoucherPhoneVO> users;//用户中奖记录
	private List<VoucherPhoneVO> list;//所有中奖名单
	private Integer chance;//抽奖次数
	
	public List<VoucherPhoneVO> getUsers() {
		return users;
	}
	public void setUsers(List<VoucherPhoneVO> users) {
		this.users = users;
	}
	public List<VoucherPhoneVO> getList() {
		return list;
	}
	public void setList(List<VoucherPhoneVO> list) {
		this.list = list;
	}
	public Integer getChance() {
		return chance;
	}
	public void setChance(Integer chance) {
		this.chance = chance;
	}
}
