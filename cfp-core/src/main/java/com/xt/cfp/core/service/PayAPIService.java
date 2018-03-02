package com.xt.cfp.core.service;

import java.io.IOException;

/**
 * 调用支付API接口——绑卡支付、解绑等
 * 
 * @author junqinghuang
 * 
 */
public interface PayAPIService {

	/**
	 * 调用支付API绑卡支付请求接口
	 * 
	 * @param amount
	 *            支付金额
	 * @param bindid
	 *            绑卡id
	 * @param currency
	 *            币种，默认156
	 * @param identityid
	 *            用户身份标识
	 * @param identitytype
	 *            用户身份标识类型
	 * @param orderid
	 *            商户订单id
	 * @param other
	 *            其他支付身份信息
	 * @param productcatalog
	 *            商品类别
	 * @param productdesc
	 *            商品描述
	 * @param productname
	 *            商品名称
	 * @param transtime
	 *            交易时间,精确到秒的时间戳
	 * @param userip
	 *            用户ip地址
	 * @param callbackurl
	 *            商户提供的商户后台系统异步支付回调地址
	 * @param fcallbackurl
	 *            商户提供的商户前台系统异步支付回调地址
	 * @param terminaltype
	 *            终端设备类型 0:IMEI、1:MAC、 2:UUID、 3:OTHER
	 * @param terminalid
	 *            终端id
	 * @return
	 * @throws Exception
	 */
	String bindCardPayRequest(int amount, String bindid, int currency,
							  String identityid, int identitytype, String orderid, String other,
							  String productcatalog, String productdesc, String productname,
							  Integer transtime, String userip, String callbackurl,
							  String fcallbackurl, int terminaltype, String terminalid)
			throws Exception;

	/**
	 * 调用发送短信验证码接口
	 * 
	 * @param orderid
	 *            客户订单号
	 * @return
	 * @throws Exception
	 */
	String sendvalideCode(String orderid) throws Exception;

	/**
	 * 调用确认支付接口
	 * 
	 * @param orderid
	 *            客户订单号
	 * @param validecode
	 *            用户收到的短信验证码，该参数如果未传，则表示无需短信验证码校验，直接完成支付
	 * @return
	 * @throws Exception
	 */
	String confirmPay(String orderid, String validecode) throws Exception;

	/**
	 * 调用支付API解绑接口
	 * 
	 * @param bindid
	 *            解绑ID
	 * @param identityid
	 *            用户身份标识
	 * @param identitytype
	 *            用户身份标识类型
	 * @return
	 * @throws Exception
	 */
	String unbindCard(String bindid, String identityid, int identitytype)
			throws Exception;

	/**
	 * 调用支付API异步支付结果查询接口
	 * 
	 * @param orderid
	 *            商户订单号
	 * @return
	 * @throws IOException
	 */
	String queryPay(String orderid) throws Exception;

	/**
	 * 调用支付API银行卡检查接口
	 * 
	 * @param cardno
	 *            银行卡号
	 * @return
	 * @throws Exception
	 */
	String bankCardCheck(String cardno) throws Exception;

	/**
	 * 调用支付API根据用户身份标识获取绑卡列表接口
	 * 
	 * @param identityid
	 *            用户身份标识
	 * @param identitytype
	 *            用户身份标识类型
	 * @return
	 * @throws Exception
	 */
	String bindList(String identityid, int identitytype) throws Exception;


}
