package com.external.yongyou.util;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.external.yongyou.entity.http.YongYouBean;
import com.xt.cfp.core.util.PropertiesUtils;

public class YongYouUtil {

	private static String UTF8 = "UTF-8" ;
	
	private static Logger logger = Logger.getLogger(YongYouUtil.class) ;
	/**
	 * 请求cfp-web用友报表流水所需的数据方法，不对外开放
	 * */
	public static  YongYouBean requestForYongYouData(String startTime ,String endTime) {
		String merchantaccount = getMerchantId();
		String yongyouPrivateKey		= getYongYouPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yongyouPublicKey			= getYongYouPublicKey();
		String reqUrl = getReqYongYouDataUrl() ;
		logger.info("reqUrl:" + reqUrl );
		Long start = System.currentTimeMillis() ;
		YongYouBean responseBean = null;
		try {
			logger.info("开始请求cfp-web报表数据接口......");
			YongYouBean reqBean = new YongYouBean();
			reqBean.setStartTime(startTime);
			reqBean.setEndTime(endTime);
			reqBean.setAppId(getAppId());
			TreeMap<String, Object> reqObj =  reqBean.getSignMap();
			reqObj.put("merchantId", merchantaccount);
			String sign = EncryUtil.handleRSA(reqObj, yongyouPrivateKey);
			reqBean.setStartTime(null);
			reqBean.setEndTime(null);
			reqObj.put("sign", sign);
			
			String jsonStr = JSONObject.toJSONString(reqObj);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryKey = RSA.encrypt(merchantAESKey, yongyouPublicKey);
			
			reqBean.setResponseStr(data);
			reqBean.setEncryKey(encryKey);
			
			logger.info("业务参数：" + jsonStr);
			logger.info("请求参数：" + JSONObject.toJSONString(reqBean));
			
			responseBean = (YongYouBean) HttpRequestSimple.getInstance().
					postSendHttp(reqUrl,reqBean);
			logger.info("返回参数：" + JSONObject.toJSONString(responseBean));
		} catch (Exception e) {
			logger.error("请求接口异常，异常原因：" , e );
		} finally{
			logger.info("请求cfp-web报表数据接口结束......，耗时：【 "+(System.currentTimeMillis() - start)+" ms】");
		}
		return responseBean ;
	}
	
	/**
	 * 生成请求流水id
	 * */
	 public static String getAppId() {
		return  ( com.xt.cfp.core.util.StringUtils.generateRandomNumber(5).toString()+ System.currentTimeMillis() );
	}

	/**
     * 生成待签名串
     * @param jsonObject
     * @return
     * @author guoyx
     */
    public static String genSignData(JSONObject jsonObject) throws Exception
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
            if (StringUtils.isBlank(value))
            {
                throw new Exception("加密参数为空！");
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
     * 校验响应签名
     * @param request
     * */
	public static YongYouBean resposneForYongYou(HttpServletRequest request) throws Exception{
		YongYouBean bean = null;
		String yongyouPublicKey = getYongYouPublicKey();
		String yongyouPrivateKey = getYongYouPrivateKey() ;
		try (ObjectInputStream input = new java.io.ObjectInputStream(
				request.getInputStream())) {
			bean = (YongYouBean) input.readObject();
			logger.info("请求的参数：" + JSONObject.toJSONString(bean));
			TreeMap<String,String> dataMap = EncryUtil.getDataMapDecryptAndSign(bean.getResponseStr(), bean.getEncryKey(),yongyouPublicKey , yongyouPrivateKey);
			bean.setStartTime(dataMap.get("startTime"));
			bean.setEndTime(dataMap.get("endTime"));
		} 
		return bean;
	}
	
	/**
	 * 获取身份信息
	 * */
	public static String getMerchantId(){
		return PropertiesUtils.getInstance().get("YONGYOU_MERCHANTID");
	}
	
	/**
	 * 获取请求地址
	 * */
	public static String getReqYongYouDataUrl(){
		return PropertiesUtils.getInstance().get("YONGYOU_REQURL");
	}
	
	/**
	 * 获取用友私钥
	 * */
	public static String getYongYouPrivateKey(){
		return PropertiesUtils.getInstance().get("YONGYOU_PRIVATEKEY");
	}
	
	
	/**
	 * 获取用友公钥
	 * */
	public static String getYongYouPublicKey(){
		return PropertiesUtils.getInstance().get("YONGYOU_PUBLICKEY");
	}

	/**
	 * 响应请求方法 
	 * @param respBean
	 * @param response
	 * @throws Exception 
	 * */
	public static void responseHttp(YongYouBean respBean, HttpServletResponse response) throws Exception {
		HttpRequestSimple.getInstance().postResponseHttp(response, respBean);
	}
	
	/**
	 * 取得商户AESKey
	 */
	public static String getMerchantAESKey() {
		return (RandomUtil.getRandom(16));
	}

}
