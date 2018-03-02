package com.xt.cfp.core.pojo;

/**
 * 权限
 */
public class FunctionInfo {
	private Long functionId;//权限ID[主键]
	private String functionCode;//权限编码
	private String functionName;//权限名称
	private String functionDesc;//权限描述
	private Long pFunctionId;//上级权限
	
	public Long getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFunctionDesc() {
		return functionDesc;
	}
	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}
	public Long getpFunctionId() {
		return pFunctionId;
	}
	public void setpFunctionId(Long pFunctionId) {
		this.pFunctionId = pFunctionId;
	}
}
