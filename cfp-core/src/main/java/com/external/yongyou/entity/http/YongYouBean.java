package com.external.yongyou.entity.http;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

/**
 * 内部使用，只对id，商户号签名
 * */
public class YongYouBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 请求流水id
	private String appId;

	// 响应内容
	private String responseStr;
	
	private List<?> responseList ;
	// 响应编码
	private String responseCode;
	// 签名（md5）
	private String sign;
	// 导出数据的开始时间
	private String startTime  ; 
	// 导出数据的结束时间
	private String endTime ;
	// 密钥
	private String encryKey ;
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getResponseStr() {
		return responseStr;
	}

	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public TreeMap<String, Object> getSignMap() {
		TreeMap<String, Object> obj = new TreeMap<String, Object>();
		obj.put("appId", appId);
		obj.put("startTime", this.getStartTime());
		obj.put("endTime", this.getEndTime());
		return obj;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the responseList
	 */
	public List<?> getResponseList() {
		return responseList;
	}

	/**
	 * @param responseList the responseList to set
	 */
	public void setResponseList(List<?> responseList) {
		this.responseList = responseList;
	}

	public String getEncryKey() {
		return encryKey;
	}

	public void setEncryKey(String encryKey) {
		this.encryKey = encryKey;
	}
	

}
