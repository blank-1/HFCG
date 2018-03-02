package com.external.llpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.external.llpay.wap.PaymentInfoWap;
import com.external.llpay.wap.security.RSAUtil;
import com.external.llpay.wap.util.FuncUtils;
import com.external.yeepay.TZTService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.PayConstants.CallBackType;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.ApplicationContextUtil;
import com.xt.cfp.core.util.LogUtils;
import com.xt.cfp.core.util.WebUtil;

/**
 * 常用工具函数
 * @author guoyx e-mail:guoyx@lianlian.com
 * @date:2013-6-25 下午05:23:05
 * @version :1.0
 *
 */
public class LLPayUtil {

    private static Logger log = Logger.getLogger(LLPayUtil.class);
    /**
     * 取得银通公钥
     */
    public static String getPubKey(){
        return Configuration.getInstance().getValue("LL_YT_PUB_KEY");
    }
    /**
     * 取得不需要省市的银行
     */
    public static String getNotNeedCityInfoBankIds(){
        return Configuration.getInstance().getValue("LL_NOT_NEED_CITYINFO_BANK_ID");
    }

    /**
     * 取得提现接收异步通知地址
     */
    public static String getNotifyUrlWithdraw(){
        return Configuration.getInstance().getValue("LL_NOTIFY_URL_WITHDRAW");
    }

    /**
     * 取得业务类型
     */
    public static String getBusiPartner(){
        return Configuration.getInstance().getValue("LL_BUSI_PARTNER");
    }

    /**
     * 取得大额行号接口
     */
    public static String getCnapsCodeQueryUrl(){
        return Configuration.getInstance().getValue("LL_CNAPS_CODE_QUERY_URL");
    }
     /**
     * 取得接口版本号
     */
    public static String getLLVersion(){
        return Configuration.getInstance().getValue("LL_VERSION");
    }
    /**
     * 取得结束后返回地址web
     */
    public static String getReturnUrl(){
        return Configuration.getInstance().getValue("LL_URL_RETURN");
    }
    /**
     * 取得结束后返回地址wap
     */
    public static String getWapReturnUrl(){
        return Configuration.getInstance().getValue("LL_WAP_URL_RETURN");
    }
    /**
     * 取得异步通知地址
     */
    public static String getNotifyUrl(){
        return Configuration.getInstance().getValue("LL_NOTIFY_URL");
    }
    /**
     * 取得连连支付WEB收银台认证支付服务地址
     */
    public static String getPayUrl(){
        return Configuration.getInstance().getValue("LL_PAY_URL");
    }
    /**
     * 取得连连支付WEP收银台认证支付服务地址
     */
    public static String getPayUrlWap(){
        return Configuration.getInstance().getValue("LL_WAP_PAY_URL");
    }
    /**
     * 取得银行卡解约接口
     */
    public static String getBankUnbindUrl(){
        return Configuration.getInstance().getValue("LL_BANKCARD_UNBIND_URL");
    }
    /**
     * 取得银行卡提现申请地址
     */
    public static String getCardDandPayUrl(){
        return Configuration.getInstance().getValue("LL_CARD_DAND_PAY");
    }
    /**
     * 取得商户编号
     */
    public static String getOidPartner(){
        return Configuration.getInstance().getValue("LL_OID_PARTNER");
    }

    /**
     * 取得签名方式
     */
    public static String getSignType(){
        return Configuration.getInstance().getValue("LL_SIGN_TYPE");
    }
    /**
     * 取得商户私钥
     */
    public static String getTraderPriKey(){
        return Configuration.getInstance().getValue("LL_TRADER_PRI_KEY");
    }
    /**
     * 取得商户私钥
     */
    public static String getMd5Key(){
        return Configuration.getInstance().getValue("LL_MD5_KEY");
    }


    /**
     * 卡bin接口请求地址
     */
    public static String getQueryCardBinURL() {
        return Configuration.getInstance().getValue("LL_QUERY_BANKCARD_URL");
    }
    /**
     * 卡bin接口请求地址
     */
    public static String getqueryBankcardListURL() {
        return Configuration.getInstance().getValue("LL_QUERY_USER_BANKCARD_URL");
    }
    /**
     * 商户支付结果查询服务
     */
    public static String getOrderOueryURL() {
        return Configuration.getInstance().getValue("LL_ORDER_QUERY_URL");
    }

    /**
     * 商户支付结果查询服务
     */
    public static String getWebURL() {
        return Configuration.getInstance().getValue("LL_WEB_URL");
    }

    /**
     * 格式化字符串
     */
    public static String formatString(String text) {
        return (text == null ? "" : text.trim());
    }

    /**
     * 取得连连支付WEB收银台网关支付服务地址
     */
    public static String getGateWayPayUrl(){
        return Configuration.getInstance().getValue("LL_GATEWAY_PAY_URL");
    }

    /**
     *
     * 大额行号查询接口
     * @return
     */
    public static List<BankInfo> getCNAPSCode(Map<String, String> params){
        String oidPartner				= getOidPartner();
        String signType			        = getSignType();
        String traderPriKey				= getTraderPriKey();
        String md5Key				    = getMd5Key();
        String orderQueryUrl			= getCnapsCodeQueryUrl();

        String cardNo              		= formatString(params.get("card_no"));
        String braBankName              		= formatString(params.get("brabank_name"));
        String cityCode              		= formatString(params.get("city_code"));
        if (StringUtils.isEmpty(cardNo)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("card_no",cardNo);
        }

        if (StringUtils.isEmpty(braBankName)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("brabank_name",braBankName);
        }
        if (StringUtils.isEmpty(cityCode)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("city_code",cityCode);
        }

        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", oidPartner);
        reqObj.put("card_no", cardNo);
        reqObj.put("brabank_name", braBankName);
        reqObj.put("sign_type", signType);
        reqObj.put("city_code", cityCode);
        String sign = LLPayUtil.addSign(reqObj, traderPriKey,md5Key);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        log.info(LogUtils.createSimpleLog("大额行号查询请求报文", reqJSON));
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(
                orderQueryUrl, reqJSON);
        log.info(LogUtils.createSimpleLog("大额行号查询响应报文", resJSON));


        JSONObject res = (JSONObject)JSONObject.parse(resJSON);
        if (!"0000".equals(res.get("ret_code"))){
            log.info(LogUtils.createSimpleLog("大额行号查询失败",(String)res.get("ret_msg")));
            return null;
        }
        JSONArray card_list = (JSONArray)res.get("card_list");

        Gson gson = new Gson();
        List<BankInfo> bankInfos = new ArrayList<BankInfo>();
        for (int i=0;i<card_list.size();i++){

            BankInfo bankInfo = (BankInfo)gson.fromJson(((JSONObject)card_list.get(i)).toJSONString(), BankInfo.class);
            bankInfos.add(bankInfo);
        }
        return bankInfos;
    }

    /**
     * 卡前置支付处理
     * @param rechargeOrder
     */
    private static PaymentInfo prepositPay(LendOrder lendOrder, RechargeOrder rechargeOrder,String productName,UserInfoExt userInfoExt,CustomerCard card) {

        HttpServletRequest req = WebUtil.getHttpServletRequest();
        UserInfoService userInfoService = (UserInfoService)ApplicationContextUtil.getBean("userInfoServiceImpl");
        UserInfo userInfo = userInfoService.getUserByUserId(rechargeOrder.getUserId());
        // 构造支付请求对象
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setVersion(LLPayUtil.getLLVersion());
        paymentInfo.setOid_partner(LLPayUtil.getOidPartner());
        paymentInfo.setUser_id(rechargeOrder.getUserId()+"");
        paymentInfo.setSign_type(LLPayUtil.getSignType());
        paymentInfo.setBusi_partner(LLPayUtil.getBusiPartner());
        paymentInfo.setNo_order(rechargeOrder.getRechargeCode());
        paymentInfo.setDt_order(getDateTimeStr(rechargeOrder.getCreateTime()));
        paymentInfo.setName_goods(productName);
        paymentInfo.setInfo_order(rechargeOrder.getDesc());
        paymentInfo.setMoney_order(TZTService.isTesting() ? "0.01" : rechargeOrder.getAmount()+"");
        paymentInfo.setNotify_url(LLPayUtil.getNotifyUrl());
        paymentInfo.setUrl_return(LLPayUtil.getReturnUrl());
        paymentInfo.setUserreq_ip(LLPayUtil.getIpAddr(req).replaceAll("\\.",
                "_").trim());
        //充值单和支付单
        paymentInfo.setUrl_order(lendOrder!=null?getWebURL()+"/person/to_lendorder_detail?lendOrderId="+lendOrder.getLendOrderId():getWebURL()+"/person/toIncome");
        paymentInfo.setValid_order("10080");// 单位分钟，可以为空，默认7天
        paymentInfo.setRisk_item(createRiskItem(rechargeOrder.getUserId().toString(),userInfo.getMobileNo(),userInfo.getCreateTime(),userInfoExt.getRealName(),userInfoExt.getIdCard()));
        paymentInfo.setTimestamp(LLPayUtil.getCurrentDateTimeStr());
        // 商戶从自己系统中获取用户身份信息（认证支付必须将用户身份信息传输给连连，且修改标记flag_modify设置成1：不可修改）
        paymentInfo.setId_type("0");
        paymentInfo.setId_no(userInfoExt.getIdCard());
        paymentInfo.setAcct_name(userInfoExt.getRealName());
        paymentInfo.setFlag_modify("1");


        if (!StringUtils.isEmpty(card.getAgreeNo()))
        {// 协议号和卡号同时存在时，优先将协议号送给连连，不要将协议号和卡号都送给连连
            paymentInfo.setNo_agree(card.getAgreeNo());
        } else
        {
            paymentInfo.setCard_no(card.getCardCode());
        }
        paymentInfo.setBack_url(lendOrder!=null?getWebURL()+"/finance/toRePayForLender?lendOrderId="+lendOrder.getLendOrderId():getWebURL()+"/person/toIncome");
        // 加签名
        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
                .toJSONString(paymentInfo)),LLPayUtil.getTraderPriKey(),LLPayUtil.getMd5Key());
        paymentInfo.setSign(sign);

        return paymentInfo;
    }

    /**
     * wap获取连连sign
     * @param rechargeOrder
     * @param productName
     * @param userInfoExt
     * @param card
     * @return
     * @throws UnsupportedEncodingException
     */
	private static String getLLWapSign(RechargeOrder rechargeOrder, String productName, UserInfoExt userInfoExt, CustomerCard card)
			throws UnsupportedEncodingException {

        UserInfoService userInfoService = (UserInfoService)ApplicationContextUtil.getBean("userInfoServiceImpl");
        UserInfo userInfo = userInfoService.getUserByUserId(rechargeOrder.getUserId());
		StringBuffer strBuf = new StringBuffer();
		if (!FuncUtils.isNull(userInfoExt.getRealName())) {
			strBuf.append("acct_name=");
			strBuf.append(userInfoExt.getRealName());
			strBuf.append("&app_request=3");
		} else {
			strBuf.append("app_request=3");
		}
        strBuf.append("&bg_color=36b1e9");
		strBuf.append("&busi_partner=101001");
		if (!FuncUtils.isNull(card.getCardCode())) {
			strBuf.append("&card_no=");
			strBuf.append(card.getCardCode());
		}
		strBuf.append("&dt_order=");
		strBuf.append(getDateTimeStr(rechargeOrder.getCreateTime()));
		if (!FuncUtils.isNull(userInfoExt.getIdCard())) {
			strBuf.append("&id_no=");
			strBuf.append(userInfoExt.getIdCard());
		}
		if (!FuncUtils.isNull(rechargeOrder.getDesc())) {
			strBuf.append("&info_order=");
			strBuf.append(rechargeOrder.getDesc());
		}
		strBuf.append("&money_order=");
		strBuf.append(TZTService.isTesting() ? "0.01" : rechargeOrder.getAmount()+"");
		if (!FuncUtils.isNull(productName)) {
			strBuf.append("&name_goods=");
			strBuf.append(productName);
		}

		if (!StringUtils.isEmpty(card.getAgreeNo())) {// 协议号和卡号同时存在时，优先将协议号送给连连，不要将协议号和卡号都送给连连
			strBuf.append("&no_agree=");
			strBuf.append(card.getAgreeNo());
		}

		strBuf.append("&no_order=");
		strBuf.append(rechargeOrder.getRechargeCode());
		strBuf.append("&notify_url=");
		strBuf.append(LLPayUtil.getNotifyUrl());
		strBuf.append("&oid_partner=");
		strBuf.append(LLPayUtil.getOidPartner());
        String riskItem = createRiskItem(rechargeOrder.getUserId().toString(),userInfo.getMobileNo(),userInfo.getCreateTime(),userInfoExt.getRealName(),userInfoExt.getIdCard());
		if (!FuncUtils.isNull(riskItem)) {
			strBuf.append("&risk_item=");
			strBuf.append(riskItem);
		}
		strBuf.append("&sign_type=");
		strBuf.append(LLPayUtil.getSignType());
		if (!FuncUtils.isNull(LLPayUtil.getWapReturnUrl())) {
			strBuf.append("&url_return=");
			strBuf.append(LLPayUtil.getWapReturnUrl());
		}
		if (!FuncUtils.isNull(rechargeOrder.getUserId() + "")) {
			strBuf.append("&user_id=");
			strBuf.append(rechargeOrder.getUserId() + "");
		}
		strBuf.append("&valid_order=");
		strBuf.append("10080");
		String sign_src = strBuf.toString();
		if (sign_src.startsWith("&")) {
			sign_src = sign_src.substring(1);
		}
		String sign = "";
		if ("RSA".equals(LLPayUtil.getSignType())) {
			sign = RSAUtil.sign(getTraderPriKey(), sign_src);
		} else {
			sign_src += "&key=" + getMd5Key();
			sign = Md5Algorithm.getInstance().md5Digest(sign_src.getBytes("utf-8"));
		}
		return sign;
	}

	/**
	 * 卡前置支付处理 wap
	 * @param rechargeOrder
	 * @param productName
	 * @param userInfoExt
	 * @param card
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static PaymentInfoWap prepositPayWap(RechargeOrder rechargeOrder, String productName, UserInfoExt userInfoExt, CustomerCard card)
			throws UnsupportedEncodingException {
        UserInfoService userInfoService = (UserInfoService)ApplicationContextUtil.getBean("userInfoServiceImpl");
        UserInfo userInfo = userInfoService.getUserByUserId(rechargeOrder.getUserId());
		String sign = getLLWapSign(rechargeOrder, productName, userInfoExt, card);
		PaymentInfoWap payInfo = new PaymentInfoWap();
		payInfo.setApp_request("3");
		payInfo.setBg_color("36b1e9");
		payInfo.setBusi_partner("101001");
		payInfo.setCard_no(card.getCardCode());
		payInfo.setDt_order(getDateTimeStr(rechargeOrder.getCreateTime()));
		payInfo.setId_no(userInfoExt.getIdCard());
		payInfo.setInfo_order(rechargeOrder.getDesc());
		payInfo.setMoney_order(TZTService.isTesting() ? "0.01" : rechargeOrder.getAmount()+"");
		payInfo.setName_goods(productName);
		String no_agree = StringUtils.isEmpty(card.getAgreeNo()) ? "" : card.getAgreeNo();
		payInfo.setNo_agree(no_agree);
		payInfo.setNo_order(rechargeOrder.getRechargeCode());
		payInfo.setNotify_url(LLPayUtil.getNotifyUrl());
		payInfo.setOid_partner(LLPayUtil.getOidPartner());
		payInfo.setAcct_name(userInfoExt.getRealName());
        String riskItem = createRiskItem(rechargeOrder.getUserId().toString(),userInfo.getMobileNo(),userInfo.getCreateTime(),userInfoExt.getRealName(),userInfoExt.getIdCard());
		payInfo.setRisk_item(riskItem);
		payInfo.setSign_type(LLPayUtil.getSignType());
		payInfo.setUrl_return(LLPayUtil.getWapReturnUrl());
		payInfo.setUser_id(rechargeOrder.getUserId() + "");
		payInfo.setValid_order("10080");
		payInfo.setSign(sign);

		return payInfo;
	}

    /**
     * @param bean
     * @return String
     * @Description bean对象转换成map对象
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> convertBeanToMap(Object bean) throws IllegalArgumentException,
            IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (bean != null) {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {// 获取bean的属性和值
                field.setAccessible(true);
                if (field.get(bean) != null) {
                    if (field.get(bean) instanceof Double) {
                        map.put(field.getName(), formatDb2Str((Double) field.get(bean)));
                    } else {
                        map.put(field.getName(), field.get(bean).toString());
                    }
                }

            }
        }
        return map;
    }

    public static String formatDb2Str(Double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    /**
     * 发送支付请求
     * @param lendOrder
     * @param rechargeOrder
     * @param productName
     * @param userInfoExt
     * @param card
     */
    public static String llPay(LendOrder lendOrder, RechargeOrder rechargeOrder,String productName,UserInfoExt userInfoExt,CustomerCard card){

        try {
            Map<String, Object> map = convertBeanToMap(prepositPay(lendOrder, rechargeOrder, productName, userInfoExt, card));
            StringBuffer sb = new StringBuffer();
            Set es = map.entrySet();
            Iterator it = es.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                String k = (String)entry.getKey();
                String v = (String)entry.getValue();
                try {
                    v = URLEncoder.encode(v, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append(k + "=" + v + "&");
            }

            //去掉最后一个&
            String reqPars = sb.substring(0, sb.lastIndexOf("&"));
            return getPayUrl() + "?" + reqPars;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 发送支付请求(wap)
	 *
	 * @param rechargeOrder
	 * @param productName
	 * @param userInfoExt
	 * @param card
	 * @return
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public static String llPayWap(RechargeOrder rechargeOrder, String productName, UserInfoExt userInfoExt, CustomerCard card)
			throws IllegalArgumentException, UnsupportedEncodingException {
		PaymentInfoWap payInfo = prepositPayWap(rechargeOrder, productName, userInfoExt, card);
		return JSONObject.toJSONString(payInfo);
	}


    /**
     * 线上风控参数
     * @return
     */
    private static String createRiskItem(String userId,String mobile,Date registerDate,String userName,String idCardNo)
    {
        JSONObject riskItemObj = new JSONObject();
        riskItemObj.put("user_info_id_type", "0");
        riskItemObj.put("user_info_id_no", idCardNo);
        riskItemObj.put("user_info_full_name", userName);
        riskItemObj.put("user_info_dt_register", getDateTimeStr(registerDate));
        riskItemObj.put("user_info_bind_phone", mobile);
        riskItemObj.put("user_info_mercht_userno", userId);
        riskItemObj.put("frms_ware_category", "2009");
        riskItemObj.put("user_info_identify_state", "1");
        riskItemObj.put("user_info_identify_type", "3");
        return riskItemObj.toString();
    }
    /**
     * 订单查询接口
     * @param params
     * @return
     */
    public static Map<String, String> orderQuery(Map<String, String> params) {

        log.info("##### orderQuery() #####");
        Map<String, String> result			= new HashMap<String, String>();

        String oidPartner				= getOidPartner();
        String signType			        = getSignType();
        String traderPriKey				= getTraderPriKey();
        String md5Key				    = getMd5Key();
        String orderQueryUrl			= getOrderOueryURL();

        String orderNo              		= formatString(params.get("no_order"));
        String orderTime              		= formatString(params.get("dt_order"));
        if (StringUtils.isEmpty(orderNo)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("no_order",orderNo);
        }

        if (StringUtils.isEmpty(orderTime)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("dt_order",orderTime);
        }

        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", oidPartner);
        reqObj.put("no_order", orderNo);
        reqObj.put("dt_order", orderTime);
        reqObj.put("sign_type", signType);
        String sign = LLPayUtil.addSign(reqObj, traderPriKey,md5Key);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        log.info(LogUtils.createSimpleLog("查询订单请求报文", reqJSON));
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(
                orderQueryUrl, reqJSON);
        log.info(LogUtils.createSimpleLog("查询订单响应报文", resJSON));

        Gson gson = new Gson();
        Map infoMap = gson.fromJson(resJSON, new TypeToken<Map<String, String>>(){}.getType());
        return infoMap;
    }

    /**
     * 银行卡解约接口
     * @param params
     * @return
     */
    public static Map<String, String> bankcardunbind(Map<String, String> params) {

        log.info("##### bankcardunbind() #####");
        Map<String, String> result			= new HashMap<String, String>();

        String oidPartner				= getOidPartner();
        String signType			        = getSignType();
        String traderPriKey				= getTraderPriKey();
        String md5Key				    = getMd5Key();
        String bankCardUnbindUrl			= getBankUnbindUrl();

        String userId              		= formatString(params.get("user_id"));
        String noAgree              		= formatString(params.get("no_agree"));
        if (StringUtils.isEmpty(userId)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("user_id",userId);
        }

        if (StringUtils.isEmpty(noAgree)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("no_agree",noAgree);
        }

        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", oidPartner);
        reqObj.put("user_id", userId);
        reqObj.put("no_agree", noAgree);
        reqObj.put("sign_type", signType);
        reqObj.put("pay_type", "D");
        String sign = LLPayUtil.addSign(reqObj, traderPriKey,md5Key);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        log.info(LogUtils.createSimpleLog("银行卡解约请求报文", reqJSON));
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(
                bankCardUnbindUrl, reqJSON);
        log.info(LogUtils.createSimpleLog("银行卡解约响应报文", resJSON));

        Gson gson = new Gson();
        Map infoMap = gson.fromJson(resJSON, new TypeToken<Map<String, String>>(){}.getType());
        return infoMap;
    }

    /**
     * 卡bin接口
     * @param params
     * @return
     */
    public static Map<String, String> queryCardBin(Map<String, String> params) {

        log.info("##### queryCardBin() #####");

        Map<String, String> result			= new HashMap<String, String>();

        String oidPartner				= getOidPartner();
        String signType			        = getSignType();
        String traderPriKey				= getTraderPriKey();
        String md5Key				    = getMd5Key();
        String queryCardBinUrl			= getQueryCardBinURL();

        String cardNo              		= formatString(params.get("card_no"));
        if (StringUtils.isEmpty(cardNo)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("card_no",cardNo);
        }

        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", oidPartner);
        reqObj.put("card_no", cardNo);
        reqObj.put("pay_type", "D");
        reqObj.put("flag_amt_limit", "1");
        reqObj.put("sign_type", signType);
        String sign = LLPayUtil.addSign(reqObj, traderPriKey, md5Key);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        log.info(LogUtils.createSimpleLog("银行卡卡bin信息查询请求报文", reqJSON));
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(
                queryCardBinUrl, reqJSON);
        log.info(LogUtils.createSimpleLog("银行卡卡bin信息查询响应报文", resJSON));

        Gson gson = new Gson();
        Map infoMap = gson.fromJson(resJSON, new TypeToken<Map<String, String>>(){}.getType());
        return infoMap;
    }

    /**
     * 用户已绑定银行列表查询
     * @param params
     * @return
     */
    public static Map<String, String> queryBankcardList(Map<String, String> params) {

        log.info("##### queryBankcardList() #####");

        Map<String, String> result			= new HashMap<String, String>();

        String oidPartner				= getOidPartner();
        String signType			        = getSignType();
        String traderPriKey				= getTraderPriKey();
        String md5Key				    = getMd5Key();
        String queryBankcardListUrl			= getqueryBankcardListURL();

        String userId              		= formatString(params.get("user_id"));
        if (StringUtils.isEmpty(userId)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("user_id",userId);
        }

        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", oidPartner);
        reqObj.put("user_id", userId);
        reqObj.put("offset", "0");
        reqObj.put("sign_type", signType);
        reqObj.put("pay_type", "D");
        String sign = LLPayUtil.addSign(reqObj, traderPriKey,md5Key);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        log.info(LogUtils.createSimpleLog("用户已绑定银行列表查询请求报文", reqJSON));
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(
                queryBankcardListUrl, reqJSON);
        log.info(LogUtils.createSimpleLog("用户已绑定银行列表查询响应报文", resJSON));

        Gson gson = new Gson();
        Map infoMap = gson.fromJson(resJSON, new TypeToken<Map<String, String>>(){}.getType());
        return infoMap;
    }



    /**
     * str空判断
     * @param str
     * @return
     * @author guoyx
     */
    public static boolean isnull(String str)
    {
        if (null == str || str.equalsIgnoreCase("null") || str.equals(""))
        {
            return true;
        } else
            return false;
    }

    /**
     * 获取当前时间str，格式yyyyMMddHHmmss
     * @return
     * @author guoyx
     */
    public static String getCurrentDateTimeStr()
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }

    /**
     * 获取当前时间str，格式yyyyMMddHHmmss
     * @return
     * @author guoyx
     */
    public static String getDateTimeStr(Date date)
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeString = dataFormat.format(date);
        return timeString;
    }
    /**
     *
     * 功能描述：获取真实的IP地址
     * @param request
     * @return
     * @author guoyx
     */
    public static String getIpAddr(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        if (!isnull(ip) && ip.contains(","))
        {
            String[] ips = ip.split(",");
            ip = ips[ips.length - 1];
        }
        //转换IP 格式
        if(!isnull(ip)){
            ip=ip.replace(".", "_");
        }
        return ip;
    }

    /**
     * 生成待签名串
     * @param jsonObject
     * @return
     * @author guoyx
     */
    public static String genSignData(JSONObject jsonObject)
    {
        StringBuffer content = new StringBuffer();

        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);
            if ("sign".equals(key))
            {
                continue;
            }
            String value = jsonObject.getString(key);
            // 空串不参与签名
            if (isnull(value))
            {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);

        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&"))
        {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }

    /**
     * 加签
     *
     * @param reqObj
     * @param rsa_private
     * @param md5_key
     * @return
     * @author guoyx
     */
    public static String addSign(JSONObject reqObj, String rsa_private,
                                 String md5_key)
    {
        if (reqObj == null)
        {
            return "";
        }
        String sign_type = reqObj.getString("sign_type");
        if (SignTypeEnum.MD5.getCode().equals(sign_type))
        {
            return addSignMD5(reqObj, md5_key);
        } else
        {
            return addSignRSA(reqObj, rsa_private);
        }
    }

    /**
     * 签名验证
     *
     * @param reqStr
     * @return
     */
    public static boolean checkSign(String reqStr, String rsa_public, String md5_key)
    {
        JSONObject reqObj = JSON.parseObject(reqStr);
        if (reqObj == null)
        {
            return false;
        }
        String sign_type = reqObj.getString("sign_type");
        if (SignTypeEnum.MD5.getCode().equals(sign_type))
        {
            return checkSignMD5(reqObj, md5_key);
        } else
        {
            return checkSignRSA(reqObj, rsa_public);
        }
    }

    /**
     * RSA签名验证
     *
     * @param reqObj
     * @return
     * @author guoyx
     */
    private static boolean checkSignRSA(JSONObject reqObj, String rsa_public)
    {

        log.info("进入商户[" + reqObj.getString("oid_partner")
                + "]RSA签名验证");
        if (reqObj == null)
        {
            return false;
        }
        String sign = reqObj.getString("sign");
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        log.info("商户[" + reqObj.getString("oid_partner") + "]待签名原串"
                + sign_src);
        log.info("商户[" + reqObj.getString("oid_partner") + "]签名串"
                + sign);
        try
        {
            if (TraderRSAUtil.checksign(rsa_public, sign_src, sign))
            {
                log.info("商户[" + reqObj.getString("oid_partner")
                        + "]RSA签名验证通过");
                return true;
            } else
            {
                log.info("商户[" + reqObj.getString("oid_partner")
                        + "]RSA签名验证未通过");
                return false;
            }
        } catch (Exception e)
        {
            log.info("商户[" + reqObj.getString("oid_partner")
                    + "]RSA签名验证异常" + e.getMessage());
            return false;
        }
    }

    /**
     * MD5签名验证
     *
     * @param reqObj
     * @param md5_key
     * @return
     * @author guoyx
     */
    private static boolean checkSignMD5(JSONObject reqObj, String md5_key)
    {
        log.info("进入商户[" + reqObj.getString("oid_partner")
                + "]MD5签名验证");

        if (reqObj == null)
        {
            return false;
        }
        String sign = reqObj.getString("sign");
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        log.info("商户[" + reqObj.getString("oid_partner") + "]待签名原串"
                + sign_src);
        log.info("商户[" + reqObj.getString("oid_partner") + "]签名串"
                + sign);
        sign_src += "&key=" + md5_key;
        try
        {
            if (sign.equals(Md5Algorithm.getInstance().md5Digest(
                    sign_src.getBytes("utf-8"))))
            {
                log.info("商户[" + reqObj.getString("oid_partner")
                        + "]MD5签名验证通过");
                return true;
            } else
            {
                log.info("商户[" + reqObj.getString("oid_partner")
                        + "]MD5签名验证未通过");
                return false;
            }
        } catch (UnsupportedEncodingException e)
        {
            log.info("商户[" + reqObj.getString("oid_partner")
                    + "]MD5签名验证异常" + e.getMessage());
            return false;
        }
    }

    /**
     * RSA加签名
     *
     * @param reqObj
     * @param rsa_private
     * @return
     * @author guoyx
     */
    private static String addSignRSA(JSONObject reqObj, String rsa_private)
    {
        log.info("进入商户[" + reqObj.getString("oid_partner")
                + "]RSA加签名");

        if (reqObj == null)
        {
            return "";
        }
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        log.info("商户[" + reqObj.getString("oid_partner") + "]加签原串"
                + sign_src);
        try
        {
            return TraderRSAUtil.sign(rsa_private, sign_src);
        } catch (Exception e)
        {
            log.info("商户[" + reqObj.getString("oid_partner")
                    + "]RSA加签名异常" + e.getMessage());
            return "";
        }
    }

    /**
     * MD5加签名
     *
     * @param reqObj
     * @param md5_key
     * @return
     * @author guoyx
     */
    private static String addSignMD5(JSONObject reqObj, String md5_key)
    {
        log.info("进入商户[" + reqObj.getString("oid_partner")
                + "]MD5加签名");
        if (reqObj == null)
        {
            return "";
        }
        // 生成待签名串
        String sign_src = genSignData(reqObj);
        log.info("商户[" + reqObj.getString("oid_partner") + "]加签原串"
                + sign_src);
        sign_src += "&key=" + md5_key;
        try
        {
            return Md5Algorithm.getInstance().md5Digest(
                    sign_src.getBytes("utf-8"));
        } catch (Exception e)
        {
            log.info("商户[" + reqObj.getString("oid_partner")
                    + "]MD5加签名异常" + e.getMessage());
            return "";
        }
    }

    /**
     * 读取request流
     * @param request
     * @return
     * @author guoyx
     */
    public static String readReqStr(HttpServletRequest request)
    {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(request
                    .getInputStream(), "utf-8"));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (null != reader)
                {
                    reader.close();
                }
            } catch (IOException e)
            {

            }
        }
        return sb.toString();
    }

    public static  Map<String, String> bankCardCheck(String cardNo){
        if (StringUtils.isEmpty(cardNo))
            return null;

        Map<String, String> params = new HashMap<String,String>();
        params.put("card_no",cardNo);
        Map<String, String> resultMap = LLPayUtil.queryCardBin(params);
        return resultMap;
    }



    public static void main(String[] args) {
        String cardNo = "6226200105602941";
        Map<String, String> params = new HashMap<String,String>();
//        params.put("card_no",cardNo);
//        Map<String, String> resultMap = LLPayUtil.queryCardBin(params);
//        System.out.println(resultMap);
/*        params.put("card_no",cardNo);
        params.put("city_code","110000");
        params.put("brabank_name","民生银行");
        Map<String, String> resultMap = LLPayUtil.queryCardBin(params);
        System.out.println(resultMap);
        List<BankInfo> result_Map = LLPayUtil.getCNAPSCode(params);
        System.out.println(result_Map);*/
//      System.out.println(resultMap);

        params.put("no_order","663469113adf4d62a6f28c9325f4037e");
        params.put("dt_order","20151113235151");
        Map<String, String> s = LLPayUtil.orderQuery(params);
        System.out.println(s);

//    	System.out.println(LLPayUtil.withdraw(params));
    }

    /**
     * 解析回调通知
     * 处理异常时会直接通知第三方（处理成功的需要手动通知第三方）
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public static PayDataBean notifyOrganizetion(HttpServletRequest request, HttpServletResponse response, String cbType) throws IOException {

        response.setCharacterEncoding("UTF-8");
        if(CallBackType.WITH_DRAW.getValue().equals(cbType)){
        	System.out.println("进入提现异步通知数据接收处理");
        }else if(CallBackType.PAY.getValue().equals(cbType)){
        	System.out.println("进入支付异步通知数据接收处理");
        }
        RetBean retBean = new RetBean();
        String reqStr = LLPayUtil.readReqStr(request);
        if (LLPayUtil.isnull(reqStr))
        {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            response.getWriter().write(JSON.toJSONString(retBean));
            response.getWriter().flush();
            return null;
        }
        if(CallBackType.WITH_DRAW.getValue().equals(cbType)){
        	System.out.println("接收提现异步通知数据：【" + reqStr + "】");
        }else if(CallBackType.PAY.getValue().equals(cbType)){
        	System.out.println("接收支付异步通知数据：【" + reqStr + "】");
        }
        try
        {
            if (!LLPayUtil.checkSign(reqStr,LLPayUtil.getPubKey(),
                    LLPayUtil.getMd5Key()))
            {
                retBean.setRet_code("9999");
                retBean.setRet_msg("交易失败");
                response.getWriter().write(JSON.toJSONString(retBean));
                response.getWriter().flush();
                if(CallBackType.WITH_DRAW.getValue().equals(cbType)){
                	System.out.println("提现异步通知验签失败");
                }else if(CallBackType.PAY.getValue().equals(cbType)){
                	System.out.println("支付异步通知验签失败");
                }
                return null;
            }
        } catch (Exception e)
        {
        	if(CallBackType.WITH_DRAW.getValue().equals(cbType)){
        		System.out.println("提现异步通知报文解析异常：" + e);
            }else if(CallBackType.PAY.getValue().equals(cbType)){
            	System.out.println("支付异步通知报文解析异常：" + e);
            }
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            response.getWriter().write(JSON.toJSONString(retBean));
            response.getWriter().flush();
            return null;
        }

        //解析异步通知对象
        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
        return payDataBean;
    }
    /**
	 * withdraw() : 提现接口
	 */

	public static Map<String, String> withdraw(Map<String, String> params) {
		WithdrawInfo reqBean = new WithdrawInfo();
        reqBean.setApi_version(LLPayUtil.getLLVersion());
        reqBean.setOid_partner(LLPayUtil.getOidPartner());
        reqBean.setSign_type(LLPayUtil.getSignType());
        reqBean.setUser_id(params.get("user_id"));
        reqBean.setAcct_name(params.get("name").trim());
        reqBean.setCard_no(params.get("cardNo").trim());
        //reqBean.setBank_code("01020000");
        reqBean.setNo_order(params.get("withDrawId"));
        reqBean.setDt_order(params.get("withDrawDate"));
        if("0".equals(params.get("bankFlag"))){
	        if(params.get("prcptcd")!=null && !"".equals(params.get("prcptcd"))){
	        	reqBean.setPrcptcd(params.get("prcptcd").trim());
	        }else{
	        	reqBean.setProvince_code(params.get("province_code"));
	            reqBean.setCity_code(params.get("city_code"));
	            reqBean.setBrabank_name(params.get("brabank_name"));
	        }
        }
        reqBean.setMoney_order(TZTService.isTesting() ? "0.01" : params.get("acount")+"");
        reqBean.setFlag_card("0");
        reqBean.setInfo_order(params.get("withDrawInfo"));
        reqBean.setNotify_url(LLPayUtil.getNotifyUrlWithdraw());
        reqBean.setSign(addSign(JSON.parseObject(JSON.toJSONString(reqBean)),LLPayUtil.getTraderPriKey(),LLPayUtil.getMd5Key()));
        String reqJson = JSON.toJSONString(reqBean);
        log.info(LogUtils.createSimpleLog("提现请求报文", reqJson));
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(LLPayUtil.getCardDandPayUrl(), reqJson);
        if(resJSON == null)
        	throw new SystemException(PayErrorCode.NOT_SC_STATE).set("error:","Network connection error");
        log.info(LogUtils.createSimpleLog("提现响应报文", resJSON));
        Gson gson = new Gson();
        Map infoMap = gson.fromJson(resJSON, new TypeToken<Map<String, String>>(){}.getType());
        if ("0000".equals(infoMap.get("ret_code"))) {
            infoMap.put("status", "SUCCESS");
            infoMap.put("ybdrawflowid", params.get("withDrawId"));
        } else if("5503".equals(infoMap.get("ret_code"))){
            //5503 商户体现不足
            infoMap.put("customError","【" + infoMap.get("ret_code") + "】:【" + infoMap.get("ret_msg") + "】");
        } else if("5006".equals(infoMap.get("ret_code"))){
            //5006 商户账户余额不足
            infoMap.put("customError","【" + infoMap.get("ret_code") + "】:【" + infoMap.get("ret_msg") + "】");
        } else if("8901".equals(infoMap.get("ret_code"))){
            //8901没有记录
            infoMap.put("customError","【" + infoMap.get("ret_code") + "】:【" + infoMap.get("ret_msg") + "】");
        } else if("5004".equals(infoMap.get("ret_code"))){
            //5004 商户状态异常 无法退款
            infoMap.put("customError","【" + infoMap.get("ret_code") + "】:【" + infoMap.get("ret_msg") + "】");
        } else if("1008".equals(infoMap.get("ret_code"))){
            //1008 商户请求ip错误
            infoMap.put("customError","【" + infoMap.get("ret_code") + "】:【" + infoMap.get("ret_msg") + "】");
        } else {
            infoMap.put("status", "ERROR");
            infoMap.put("error_code", infoMap.get("ret_code"));
            infoMap.put("error_msg", infoMap.get("ret_msg"));
        }
        return infoMap;
	}

	/**
     * 提现订单查询接口
     * @param params
     * @return
     */
    public static Map<String, String> withDrawOrderQuery(Map<String, String> params) {

        log.info("##### withDraworderQuery() #####");
        Map<String, String> result			= new HashMap<String, String>();

        String oidPartner				= getOidPartner();
        String signType			        = getSignType();
        String traderPriKey				= getTraderPriKey();
        String md5Key				    = getMd5Key();
        String orderQueryUrl			= getOrderOueryURL();

        String orderNo              		= formatString(params.get("no_order"));
        if (StringUtils.isEmpty(orderNo)){
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("no_order",orderNo);
        }

        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", oidPartner);
        reqObj.put("no_order", orderNo);
        reqObj.put("sign_type", signType);
        reqObj.put("type_dc", "1");
        String sign = LLPayUtil.addSign(reqObj, traderPriKey,md5Key);
        reqObj.put("sign", sign);
        String reqJSON = reqObj.toString();
        log.info(LogUtils.createSimpleLog("代付查询订单请求报文", reqJSON));
        String resJSON = HttpRequestSimple.getInstance().postSendHttp(
                orderQueryUrl, reqJSON);
        if(resJSON == null)
        	 throw new SystemException(PayErrorCode.NOT_SC_STATE).set("error:","Network connection error");
        log.info(LogUtils.createSimpleLog("代付查询订单响应报文", resJSON));

        Gson gson = new Gson();
        Map infoMap = gson.fromJson(resJSON, new TypeToken<Map<String, String>>(){}.getType());
        if("0000".equals(infoMap.get("ret_code"))){
        	if("WAITING".equals(infoMap.get("result_pay")) || "PROCESSING".equals(infoMap.get("result_pay")))
        		infoMap.put("status", "DOING");
        	else
        		infoMap.put("status", infoMap.get("result_pay"));
        	infoMap.put("ybdrawflowid", params.get("withDrawId"));
        }else{
            infoMap.put("status", "DOING");
        }
        return infoMap;
    }

	/**
	 * 校验卡号连连是否支持
	 *
	 * @param cardNo
	 * @return
	 */
	public static boolean checkLLCardSupport(String cardNo) {
		boolean flag = false;
		Map<String, String> result = bankCardCheck(cardNo);

		if (!"0000".equals(result.get("ret_code")))
			return flag; // 无效的银行卡

		if (result.get("card_type").equals("3"))
			return flag;// 不支持信用卡

		// 校验-如果该卡不在支持的银行列表中
		String bankCode = result.get("bank_code");

		ConstantDefineService constantDefineService = (ConstantDefineService) ApplicationContextUtil.getBean("constantDefineServiceImpl");
		List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
		for (ConstantDefine constantDefine : constantDefines) {
			if (bankCode.equals(constantDefine.getConstantValue())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	 /**
     * 发送网关支付请求
     * @param lendOrder
     * @param rechargeOrder
     * @param bankCode
     * @param productName
     * @param userInfoExt
	 * @throws Exception
     */
    public static String llGateWayPay(LendOrder lendOrder, RechargeOrder rechargeOrder,String bankCode, String productName,UserInfoExt userInfoExt) throws Exception{

        try {
            Map<String, Object> map = convertBeanToMap(prepositGateWayPay(lendOrder, rechargeOrder, bankCode,productName, userInfoExt));
            StringBuffer sb = new StringBuffer();
            Set es = map.entrySet();
            Iterator it = es.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                String k = (String)entry.getKey();
                String v = (String)entry.getValue();
                try {
                    v = URLEncoder.encode(v, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append(k + "=" + v + "&");
            }

            //去掉最后一个&
            String reqPars = sb.substring(0, sb.lastIndexOf("&"));
            return getGateWayPayUrl() + "?" + reqPars;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }

    /**
     * 卡前置支付处理
     * @param rechargeOrder
     * @param bankCode
     */
    private static PaymentInfo prepositGateWayPay(LendOrder lendOrder, RechargeOrder rechargeOrder,String bankCode,String productName, UserInfoExt userInfoExt) {

        HttpServletRequest req = WebUtil.getHttpServletRequest();
        UserInfoService userInfoService = (UserInfoService)ApplicationContextUtil.getBean("userInfoServiceImpl");
        UserInfo userInfo = userInfoService.getUserByUserId(rechargeOrder.getUserId());
        // 构造支付请求对象
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setVersion(LLPayUtil.getLLVersion());
        paymentInfo.setOid_partner(LLPayUtil.getOidPartner());
        paymentInfo.setUser_id(rechargeOrder.getUserId()+"");
        paymentInfo.setSign_type(LLPayUtil.getSignType());
        paymentInfo.setBusi_partner(LLPayUtil.getBusiPartner());
        paymentInfo.setNo_order(rechargeOrder.getRechargeCode());
        paymentInfo.setDt_order(getDateTimeStr(rechargeOrder.getCreateTime()));
        paymentInfo.setName_goods(productName);
        paymentInfo.setInfo_order(rechargeOrder.getDesc());
        paymentInfo.setMoney_order(TZTService.isTesting() ? "0.01" : rechargeOrder.getAmount()+"");
        paymentInfo.setNotify_url(LLPayUtil.getNotifyUrl());
        paymentInfo.setUrl_return(LLPayUtil.getReturnUrl());
        paymentInfo.setUserreq_ip(LLPayUtil.getIpAddr(req).replaceAll("\\.",
                "_").trim());
        //充值单和支付单
        paymentInfo.setUrl_order(lendOrder!=null?getWebURL()+"/person/to_lendorder_detail?lendOrderId="+lendOrder.getLendOrderId():getWebURL()+"/person/toIncome");
        paymentInfo.setValid_order("10080");// 单位分钟，可以为空，默认7天
        paymentInfo.setRisk_item(createRiskItem(rechargeOrder.getUserId().toString(),userInfo.getMobileNo(),userInfo.getCreateTime(),userInfoExt.getRealName(),userInfoExt.getIdCard()));
        paymentInfo.setTimestamp(LLPayUtil.getCurrentDateTimeStr());
        paymentInfo.setBank_code(bankCode);
        // 加签名
        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
                .toJSONString(paymentInfo)),LLPayUtil.getTraderPriKey(),LLPayUtil.getMd5Key());
        paymentInfo.setSign(sign);

        return paymentInfo;
    }

}
