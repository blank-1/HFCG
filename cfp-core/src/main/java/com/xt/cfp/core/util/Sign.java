package com.xt.cfp.core.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.WechatErrorCode;
import com.xt.cfp.core.service.MessagePublicService;
import com.xt.cfp.core.service.message.Message;
import com.xt.cfp.core.service.message.MessageChannel;
import com.xt.cfp.core.service.message.WechatMessageBody;
/**
 * Created by wangxudong  2015/8/17.
 */
public class Sign {
	private static Logger logger = Logger.getLogger(Sign.class);
	
    public static void main(String[] args) {
//        String jsapi_ticket = "kgt8ON7yVITDhtdwci0qeQpnNBWC5Zk9icRQ0N6Dy8hWJNT0a1i86DMY72MuMcKL86rVq1KGH0p7Iv_SL-W6jw";
//        
//        // 注意 URL 一定要动态获取，不能 hardcode
//        String url = "http://m.caifupad.com/game/index";
//        Map<String, String> ret = sign(jsapi_ticket, url);
//        for (Map.Entry entry : ret.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
//    	System.out.println(sendWechatWithDrawMsg("ozz5uwgWBcsjGOg1xzBCwlzDRvkM", "4408", new BigDecimal(100.00), "2015年09月25日 14:06", "Cxp9hM3HG8vrhk1JtQ5PN2SHZ2Merq9bu_grtp9VccgMPmU8o0w9m0kTdyL5sgsh6Poe9sMRpL4lJBVv5Us_oj1lUrhXlA4xwHF40Ijhg9s"));
        //JSONObject jsonobj = sendWechatSms("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=769gP7UDP1HoQjd-X0iVVfQA1avSQFU7aNKJgMmZTA9EKpLZGkAPapdKsnGpZ_d5UQVSyDJcwAOXh9Z8YLfzTq5IGLN6lBYmLcZCrqwA-cM", "POST",JSON.parseObject(JsonUtil.json(t)).get("bean").toString()); 
    	//System.out.println(jsonobj);
        //System.out.println(JSON.parseObject(JsonUtil.json(t)).get("bean").toString());
        //sendWechatSms();

    	
    	// 测试发放优惠券
//    	System.out.println(sendWechatVoucherMsg("ozz5uwgWBcsjGOg1xzBCwlzDRivkM", "fistName", "endName", "voucherName", "endDate", "0itXsxNQ3hP8lLoRZrrOu1bBbbKhCgvfBYIrA-IroPUDSDjyzQOfk2WsPzz-Nryd2CRp1VL_DZ6RSezchsmALOAl_P6jUGmdUCe2QXT2OIZU-6AOy8D0kGATUsphlhy5SNUhAGAEZG"));
    	
    	// 测试优惠券过期提醒
//    	System.out.println(sendWechatVoucherExpireMsg("ozz5uwgWBcsjGOg1xzBCwlzDRivkM", "fistName", "endName", "useRange", "useRule", "0itXsxNQ3hP8lLoRZrrOu1bBbbKhCgvfBYIrA-IroPUDSDjyzQOfk2WsPzz-Nryd2CRp1VL_DZ6RSezchsmALOAl_P6jUGmdUCe2QXT2OIZU-6AOy8D0kGATUsphlhy5SNUhAGAEZG"));
    	
    	// 测试提现通知
//    	System.out.println(sendWechatWithDrawMsg("ozz5uwgWBcsjGOg1xzBCwlzDRivkM", "bankNo", new BigDecimal("100"), "date", "0itXsxNQ3hP8lLoRZrrOu1bBbbKhCgvfBYIrA-IroPUDSDjyzQOfk2WsPzz-Nryd2CRp1VL_DZ6RSezchsmALOAl_P6jUGmdUCe2QXT2OIZU-6AOy8D0kGATUsphlhy5SNUhAGAEZG"));
    	
    	// 测试回款通知
//    	System.out.println(sendWechatRepaymenMsg("ozz5uwgWBcsjGOg1xzBCwlzDRivkM", 6, new BigDecimal("800"), new BigDecimal("600"), new BigDecimal("400"), "0itXsxNQ3hP8lLoRZrrOu1bBbbKhCgvfBYIrA-IroPUDSDjyzQOfk2WsPzz-Nryd2CRp1VL_DZ6RSezchsmALOAl_P6jUGmdUCe2QXT2OIZU-6AOy8D0kGATUsphlhy5SNUhAGAEZG"));

    };
    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        //System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    


    /**
     * 获取access_token的url
     */
    private static String access_token_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    /**
     * appid
     */
    private static String appid=PropertiesUtils.getInstance().get("WECHAT_APPID");
    /**
     * secret
     */
    private static String secret=PropertiesUtils.getInstance().get("WECHAT_SECRET");
    
    //回款模版
    private static String wechatTemplateRepaymen = PropertiesUtils.getInstance().get("WECHAT_TEMPLATE_REPAYMEN");
    //提现模版
    private static String wechatTemplateWithdraw = PropertiesUtils.getInstance().get("WECHAT_TEMPLATE_WITHDRAW");
    //发放财富卷成功模版
    private static String wechatTemplateVoucher = PropertiesUtils.getInstance().get("WECHAT_TEMPLATE_VOUCHER");
    //财富卷到期提醒模版
    private static String wechatTemplateVoucherexpire = PropertiesUtils.getInstance().get("WECHAT_TEMPLATE_VOUCHEREXPIRE");
    
    
    /**
     * 获取jsapi_ticket的url
     */
    private static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    /**
     * 获取code
     */
    private static String open_id_url = "https://api.weixin.qq.com/sns/oauth2/access_token";
    
    private static String send_wechat_msg_Url = "https://api.weixin.qq.com/cgi-bin/message/template/send";
    
    /**
     * 获取
     * @return
     */
    public static String getAccessToken(){
        StringBuilder path = new StringBuilder(access_token_url);
        
        path.append("&appid=" + appid);
        path.append("&secret=" + secret);

        HttpClientHelper ph = new HttpClientHelper();
        ph.setCharset("utf-8");
        ph.setReqContent(path.toString());
        ph.setMethod("GET");
        boolean call = ph.call();
        if (!call)
            throw new SystemException(WechatErrorCode.WECHAT_SEND_FAILE).set("响应结果", ph.getErrInfo());
        	//throw new RuntimeException("请求失败");
        String resultJson = ph.getResContent();
        System.out.println(resultJson);
        if (org.apache.commons.lang.StringUtils.isEmpty(resultJson))
            throw new SystemException(WechatErrorCode.WECHAT_SEND_FAILE).set("resultJson", resultJson);
        JSONObject json = JSON.parseObject(resultJson);
        System.out.println(json.get("errcode"));
        if(json.get("errcode") != null){
        	//System.out.println(json.get("errmsg"));
        	return "";
        }
        else{
        	return json.getString("access_token");
        }
    }
    /**
     * 获取jsapi_ticket
     * @param access_token
     * @return
     */
    public static String getJsapiTicket(String access_token){
    	StringBuilder jsapiTicketPath = new StringBuilder(jsapi_ticket_url);
        
    	jsapiTicketPath.append("?access_token=" + access_token);
    	jsapiTicketPath.append("&type=jsapi");
    	if(access_token == null || "".equals(access_token))
    		throw new SystemException(WechatErrorCode.WECHAT_NO_NULL).set("access_token", access_token);
        HttpClientHelper jsapiTicketPh = new HttpClientHelper();
        jsapiTicketPh.setCharset("utf-8");
        jsapiTicketPh.setReqContent(jsapiTicketPath.toString());
        jsapiTicketPh.setMethod("GET");
        boolean jsapiTicketCall = jsapiTicketPh.call();
        if (!jsapiTicketCall)
            throw new SystemException(WechatErrorCode.WECHAT_SEND_FAILE).set("响应结果", jsapiTicketPh.getErrInfo());
        String jsapiTicketResultJson = jsapiTicketPh.getResContent();
        if (org.apache.commons.lang.StringUtils.isEmpty(jsapiTicketResultJson))
            throw new SystemException(WechatErrorCode.WECHAT_SEND_FAILE).set("resultJson", jsapiTicketResultJson);
        JSONObject json = JSON.parseObject(jsapiTicketResultJson);
        if("0".equals(json.getString("errcode"))){
        	return json.getString("ticket");
        }else{
        	return "";
        }
    }
    
    /**
     * 获取code
     * @param code
     * @return
     */
    public static String getOpenId(String code){
    	StringBuilder openIdPath = new StringBuilder(open_id_url);
    	openIdPath.append("?appid=" + appid);
    	openIdPath.append("&secret=" + secret);
    	openIdPath.append("&code=" + code);
    	openIdPath.append("&grant_type=authorization_code");

        HttpClientHelper openIdPh = new HttpClientHelper();
        openIdPh.setCharset("utf-8");
        openIdPh.setReqContent(openIdPath.toString());
        openIdPh.setMethod("GET");
        boolean openIdCall = openIdPh.call();
        if (!openIdCall)
            throw new SystemException(WechatErrorCode.WECHAT_SEND_FAILE).set("响应结果", openIdPh.getErrInfo());
        String jsapiTicketResultJson = openIdPh.getResContent();
        if (org.apache.commons.lang.StringUtils.isEmpty(jsapiTicketResultJson))
            throw new SystemException(WechatErrorCode.WECHAT_SEND_FAILE).set("resultJson", jsapiTicketResultJson);
        JSONObject json = JSON.parseObject(jsapiTicketResultJson);
       //{"errcode":40029,"errmsg":"invalid code"}
        logger.info("返回值"+json);
        if(json.get("errcode") != null){
        	return "";
        }
        else{
        	logger.info("openid"+json.getString("openid"));
        	return json.getString("openid");
        }
    }
    
    public static JSONObject wechatHttpClient(String requestUrl, String requestMethod, String outputStr) {  
    	JSONObject jsonObject = null;  
        StringBuffer buffer = new StringBuffer();  
        try {  
            URL url = new URL(requestUrl);  
            logger.info("微信公众号准备发送推送信息：" + requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();   
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  
  
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
            //jsonObject = JSONObject.fromObject(buffer.toString());  
            jsonObject = JSON.parseObject(buffer.toString());
            System.out.println(jsonObject);
            logger.info("微信公众号回调信息：" + jsonObject);
        } catch (ConnectException ce) {  
            ce.printStackTrace();
            logger.info("错误信息"+ce.getMessage());
        } catch (Exception e) {  
            e.printStackTrace();  
            logger.info("错误信息"+e.getMessage());
        }  
        return jsonObject;  
    }
    /**
     * 回款模版
     * @param openId
     * @param sectionCode
     * @param balance
     * @param shouldCapital2
     * @param shouldInterest2
     * @param access_token
     * @return
     */
    public static JSONObject sendWechatRepaymenMsg(String openId, int sectionCode, BigDecimal balance, BigDecimal shouldCapital2, BigDecimal shouldInterest2, String access_token){
    	WxTemplate t = new WxTemplate();  
        t.setUrl("");  
        t.setTouser(openId);  
        //t.setTopcolor("#000000");  
        t.setTemplate_id(wechatTemplateRepaymen);  
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
        TemplateData first = new TemplateData();  
        first.setColor("#173177");  
        first.setValue("您出借的项目第"+sectionCode+"期回款");  
        m.put("first", first);  
        TemplateData keyword1 = new TemplateData();  
        keyword1.setColor("#173177");  
        keyword1.setValue(balance+"元");  
        m.put("keyword1", keyword1);
        TemplateData keyword2 = new TemplateData();  
        keyword2.setColor("#173177");  
        keyword2.setValue(shouldCapital2+"元");  
        m.put("keyword2", keyword2);
        TemplateData keyword3 = new TemplateData();  
        keyword3.setColor("#173177");  
        keyword3.setValue(shouldInterest2+"元");  
        m.put("keyword3", keyword3); 
        TemplateData remark = new TemplateData();  
        remark.setColor("#173177");  
        remark.setValue("您可以在个人中心查看您的收益，感谢您的支持，如有疑问，请致电400-061-8080");  
        m.put("remark", remark);  
        t.setData(m);
    	return wechatHttpClient(send_wechat_msg_Url+"?access_token="+access_token, "POST", JSON.parseObject(JsonUtil.json(t)).get("bean").toString());
    }
    /**
     * 提现模版
     * @param openId
     * @param bankNo
     * @param balance
     * @param date
     * @param access_token
     * @return
     */
    public static JSONObject sendWechatWithDrawMsg(String openId, String bankNo, BigDecimal balance, String date, String access_token){
    	WxTemplate t = new WxTemplate();  
        t.setUrl("");  
        t.setTouser(openId);  
        //t.setTopcolor("#000000");  
        t.setTemplate_id(wechatTemplateWithdraw);  
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
        TemplateData first = new TemplateData();  
        first.setColor("#173177");  
        first.setValue("您好，您的提现申请已通过审核。");  
        m.put("first", first);  
        TemplateData keyword1 = new TemplateData();  
        keyword1.setColor("#173177");  
        keyword1.setValue("尾号"+bankNo);  
        m.put("keyword1", keyword1);
        TemplateData keyword2 = new TemplateData();  
        keyword2.setColor("#173177");  
        keyword2.setValue(balance+"元");  
        m.put("keyword2", keyword2);
        TemplateData keyword3 = new TemplateData();  
        keyword3.setColor("#173177");  
        keyword3.setValue(date);  
        m.put("keyword3", keyword3); 
        TemplateData remark = new TemplateData();  
        remark.setColor("#173177");  
        remark.setValue("款项会在1-3个工作日到达提现银行账户，详情请登录财富派查看。如有疑问，请致电：400-061-8080。");  
        m.put("remark", remark);  
        t.setData(m);
    	return wechatHttpClient(send_wechat_msg_Url+"?access_token="+access_token, "POST", JSON.parseObject(JsonUtil.json(t)).get("bean").toString());
    }
    
    /**
     * 优惠券领取成功通知模版（财富券、提现券、加息券）
     * @param openId 用户openid(来自腾讯)
     * @param fistName 小标题
     * @param endName 结尾描述
     * @param voucherName 优惠券名称
     * @param endDate 有效截止时间
     * @param access_token 访问令牌（来自腾讯）
     * @return
     */
    public static JSONObject sendWechatVoucherMsg(String openId, String fistName, String endName, String voucherName, String endDate, String access_token){
    	WxTemplate t = new WxTemplate();  
        t.setUrl("");  
        t.setTouser(openId);  
        //t.setTopcolor("#000000");  
        t.setTemplate_id(wechatTemplateVoucher);  
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
        TemplateData first = new TemplateData();  
        first.setColor("#173177");  
        first.setValue(fistName);  
        m.put("first", first);  
        TemplateData keyword1 = new TemplateData();  
        keyword1.setColor("#173177");  
        keyword1.setValue(voucherName);  
        m.put("keyword1", keyword1);
        TemplateData keyword2 = new TemplateData();  
        keyword2.setColor("#173177");  
        keyword2.setValue("直接使用，无需兑换。");  
        m.put("keyword2", keyword2);
        TemplateData keyword3 = new TemplateData();  
        keyword3.setColor("#173177");  
        keyword3.setValue(endDate);  
        m.put("keyword3", keyword3);
        TemplateData remark = new TemplateData();  
        remark.setColor("#173177");  
        remark.setValue(endName);  
        m.put("remark", remark);  
        t.setData(m);
    	return wechatHttpClient(send_wechat_msg_Url+"?access_token="+access_token, "POST", JSON.parseObject(JsonUtil.json(t)).get("bean").toString());
    }
    
    /**
     * 财富卷和提现劵到期模版
     * @param openId
     * @param fistName
     * @param endName
     * @param useRange
     * @param useRule
     * @param access_token
     * @return
     */
    public static JSONObject sendWechatVoucherExpireMsg(String openId, String fistName, String endName, String useRange, String useRule, String access_token){
    	WxTemplate t = new WxTemplate();  
        t.setUrl("");  
        t.setTouser(openId); 
        t.setTemplate_id(wechatTemplateVoucherexpire);  
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
        TemplateData first = new TemplateData();  
        first.setColor("#173177");  
        first.setValue(fistName);  
        m.put("first", first);  
        TemplateData keyword1 = new TemplateData();  
        keyword1.setColor("#173177");  
        keyword1.setValue(useRange);  
        m.put("orderTicketStore", keyword1);
        TemplateData keyword2 = new TemplateData();  
        keyword2.setColor("#173177");  
        keyword2.setValue(useRule);  
        m.put("orderTicketRule", keyword2);
        TemplateData remark = new TemplateData();  
        remark.setColor("#173177");  
        remark.setValue(endName);  
        m.put("remark", remark);  
        t.setData(m);
    	return wechatHttpClient(send_wechat_msg_Url+"?access_token="+access_token, "POST", JSON.parseObject(JsonUtil.json(t)).get("bean").toString());
    }
    /**
	 * 是否开启短信 0是关 1是开
	 */
	private static final String wechatFlag = PropertiesUtils.getInstance().get("EMAIL_FLAG");
	
    public static void sendWechatMsg(WechatMessageBody wechatMessageBody) {
		MessagePublicService messagePublicService = (MessagePublicService) ApplicationContextUtil.getBean("messagePublicServiceImpl");

		Message message = new Message(wechatMessageBody);
		// 发布微信消息
		messagePublicService.publish(message, MessageChannel.WECHAT_MSG.getValue());
	}
    
    /**
     * 回款模版（提前回款）
     * @param openId
     * @param sectionCode
     * @param balance
     * @param shouldCapital2
     * @param shouldInterest2
     * @param access_token
     * @return
     */
    public static JSONObject sendWechatEarlyRepaymenMsg(String openId, int sectionCode, BigDecimal balance, BigDecimal shouldCapital2, BigDecimal shouldInterest2, String access_token){
    	WxTemplate t = new WxTemplate();  
        t.setUrl("");  
        t.setTouser(openId);  
        //t.setTopcolor("#000000");  
        t.setTemplate_id(wechatTemplateRepaymen);  
        Map<String,TemplateData> m = new HashMap<String,TemplateData>();  
        TemplateData first = new TemplateData();  
        first.setColor("#173177");  
        first.setValue("您出借的项目第"+sectionCode+"期回款（提前回款）");  
        m.put("first", first);  
        TemplateData keyword1 = new TemplateData();  
        keyword1.setColor("#173177");  
        keyword1.setValue(balance+"元");  
        m.put("keyword1", keyword1);
        TemplateData keyword2 = new TemplateData();  
        keyword2.setColor("#173177");  
        keyword2.setValue(shouldCapital2+"元");  
        m.put("keyword2", keyword2);
        TemplateData keyword3 = new TemplateData();  
        keyword3.setColor("#173177");  
        keyword3.setValue(shouldInterest2+"元");  
        m.put("keyword3", keyword3); 
        TemplateData remark = new TemplateData();  
        remark.setColor("#173177");  
        remark.setValue("您可以在个人中心查看您的收益，感谢您的支持，如有疑问，请致电400-061-8080");  
        m.put("remark", remark);  
        t.setData(m);
    	return wechatHttpClient(send_wechat_msg_Url+"?access_token="+access_token, "POST", JSON.parseObject(JsonUtil.json(t)).get("bean").toString());
    }
}
      
