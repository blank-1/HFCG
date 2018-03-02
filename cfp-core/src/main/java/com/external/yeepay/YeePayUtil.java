package com.external.yeepay;

import org.apache.log4j.Logger;

public class YeePayUtil {
	
	private final Logger log = Logger.getLogger("YeePayUtil");
	
	/**
	 * 当方法返回空值时，可能是环境配置、连接错误、接口变动等原因导致api接口报错
	 * @param rechargeCode
	 * @return
	 */
	public static QueryResult selectYeePayOrder(String rechargeCode){
		QueryResult result=null;
		try {
			result=PaymentForOnlineService.queryByOrder(rechargeCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
