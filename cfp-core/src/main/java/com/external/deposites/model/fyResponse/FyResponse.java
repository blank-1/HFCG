package com.external.deposites.model.fyResponse;

import java.util.List;

import com.xt.cfp.core.constants.ResponseEnum;
import com.xt.cfp.core.pojo.Trade;

/**
 * 富有接口调用返回信息
 * @author 刘伟
 *
 */
public class FyResponse {
	//调用状态
	private ResponseEnum responseEnum;
	
	private  List<Trade> failList;
	//描述
	private String message;

	public ResponseEnum getResponseEnum() {
		return responseEnum;
	}

	public void setResponseEnum(ResponseEnum responseEnum) {
		this.responseEnum = responseEnum;
	}
	/**
	 * 返回信息集中处理
	 * @param responseEnum
	 * @param failList
	 * @param message
	 * @return
	 */
	public static FyResponse getInstance(ResponseEnum responseEnum,String message ,List<Trade> failList) {
		FyResponse fyResponse = getInstance();
		fyResponse.setFailList(failList);
		fyResponse.setMessage(message);
		fyResponse.setResponseEnum(responseEnum);
		return fyResponse;
	}
	
	/**
	 * 返回信息集中处理
	 * @param responseEnum
	 * @param failList
	 * @return
	 */
	public static FyResponse getInstance(ResponseEnum responseEnum,List<Trade> failList) {
		FyResponse fyResponse = getInstance();
		fyResponse.setFailList(failList);
		fyResponse.setResponseEnum(responseEnum);
		return fyResponse;
	}
	public static FyResponse getInstance() {
		return new FyResponse();
	}

	public List<Trade> getFailList() {
		return failList;
	}

	public void setFailList(List<Trade> failList) {
		this.failList = failList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
