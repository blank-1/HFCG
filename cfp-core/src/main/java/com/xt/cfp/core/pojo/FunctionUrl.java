package com.xt.cfp.core.pojo;

/**
 * 权限URL
 */
public class FunctionUrl {
	private Long urlId;//URLID[主键]
	private Long functionId;//权限ID[外键]
	private String urlInfo;//URL地址
	
	public Long getUrlId() {
		return urlId;
	}
	public void setUrlId(Long urlId) {
		this.urlId = urlId;
	}
	public Long getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
	public String getUrlInfo() {
		return urlInfo;
	}
	public void setUrlInfo(String urlInfo) {
		this.urlInfo = urlInfo;
	}
}
