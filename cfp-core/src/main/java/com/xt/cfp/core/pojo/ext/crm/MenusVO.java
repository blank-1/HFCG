package com.xt.cfp.core.pojo.ext.crm;

import java.util.List;

import com.xt.cfp.core.pojo.CrmFunction;

public class MenusVO {
	
	private CrmFunction func;
	private List<MenusVO> funs;
	
	public List<MenusVO> getFuns() {
		return funs;
	}
	public void setFuns(List<MenusVO> funs) {
		this.funs = funs;
	}
	public CrmFunction getFunc() {
		return func;
	}
	public void setFunc(CrmFunction func) {
		this.func = func;
	}
}
