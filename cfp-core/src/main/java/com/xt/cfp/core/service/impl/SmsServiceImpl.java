package com.xt.cfp.core.service.impl;

import com.external.wxtl.CodingUtils;
import com.external.wxtl.HttpUtil;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.service.MessagePublicService;
import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.service.message.Message;
import com.xt.cfp.core.service.message.MessageChannel;
import com.xt.cfp.core.service.message.SmsMessageBody;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.Property;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by luqinglin on 2015/7/2.
 */
@Property
@Service
public class SmsServiceImpl implements SmsService {

    private static Logger logger = Logger.getLogger(SmsServiceImpl.class);
    
    private static String[] NAMES={"萌", "贝", "淼", "财", "富", "派", "智", "乐", "蔓", "瑜", "苗", "欣", "珊", "雅", "辉", "爽", "树", "鹏", "琦", "琳", "欢" };
    
    @Autowired
    private RedisCacheManger redisCacheManger;
    @Autowired
    private MessagePublicService messagePublicService;

    /**
     * 发送短信接口url 
     */
    private static String smsSendUrl;
    /**
     * 用户名
     */
    private static String smsUserName;
    /**
     * 密码
     */
    private static String smsPwd;
    /**
     * 短信网关
     */
    private static String smsType;
    /**
     * 是否开启短信  0是开  1是关
     */
    private static String smsFlag;
    
    private static String wxtlUrl;
    
    private static String wxtlCid;
    
    private static String wxtlPwd;
    
    private static String wxtlPid;

    @Deprecated
    @Override
    public String sendMsg(String phoneNo, String content) {
    	if("0".equals(smsFlag)){

            SmsMessageBody smsMessageBody = new SmsMessageBody(phoneNo,content);
            Message message = new Message(smsMessageBody);
            //发布短信消息
            messagePublicService.publish(message, MessageChannel.MSG.getValue());

            return "";
        }else{
    		return "";
    	}
    }

    @Override
	public String sendMsgByWXTL(String phoneNo, String content) {
		String result="success";
        if("0".equals(smsFlag)){
            StringBuffer params = new StringBuffer();
            if(content.indexOf("%2B")>=0){
                content=content.replaceAll("%2B", "+");
            }
            if(content.indexOf("%20")>=0){
                content=content.replaceAll("%20", " ");
            }
            if(content.indexOf("%2F")>=0){
                content=content.replaceAll("%2F", "/");
            }
            if(content.indexOf("%3F")>=0){
                content=content.replaceAll("%3F", "?");
            }
            if(content.indexOf("%25")>=0){
                content=content.replaceAll("%25", "%");
            }
            if(content.indexOf("%26")>=0){
                content=content.replaceAll("%26", "&");
            }
            if(content.indexOf("%3D")>=0){
                content=content.replaceAll("%3D", "=");
            }
            try {
                if(content.indexOf("【财富派】")>=0){
                    String[] ct=content.split("】");
                    String str="财富管家("+getRandomStr()+")提示您:";
                    content=ct[0]+"】"+str+ct[1];
                }
                params.append("cid=").append(CodingUtils.encodeBase64URL(wxtlCid))
                        .append("&").append("pwd=").append(CodingUtils.encodeBase64URL(wxtlPwd))
                        .append("&").append("productid=").append(CodingUtils.encodeURL(wxtlPid))
                        .append("&").append("mobile=").append(CodingUtils.encodeBase64URL(phoneNo+""))
                        .append("&").append("content=").append(CodingUtils.encodeBase64URL(content))
                        .append("&").append("lcode=").append("")
                        .append("&").append("ssid=").append("")
                        .append("&").append("format=").append(32)
                        .append("&").append("sign=").append(CodingUtils.encodeBase64URL(""))
                        .append("&").append("custom=").append(CodingUtils.encodeURL(""));
                logger.info("天利请求参数=====================:"+params.toString());
                result=HttpUtil.sendPostRequestByParam(wxtlUrl,params.toString());
                logger.info("天利返回结果=====================:"+result);
                if(result.indexOf("\"status\":\"0\"")<0){
                    logger.error("请求参数:"+params.toString());
                    logger.error("手机号"+phoneNo+"发送失败，无线天利返回结果="+result);
                }

            } catch (Exception e) {
                e.printStackTrace();
                if(result.equals("success")){
                    logger.error("手机号"+phoneNo+"发送失败,未知错误。"+"发送内容"+content);
                }else{
                    logger.error("请求参数:"+params.toString());
                    logger.error("手机号"+phoneNo+"发送失败,无线天利返回结果="+result);
                }

            }
        }

		return result;
	}
    
    private String getRandomStr(){
    	return "小"+NAMES[new Random().nextInt(NAMES.length-1)];
    }
    
    @Override
    public boolean validateMsg(String phoneNo, String smsCode, TemplateType templateType,boolean flag) {
        if (StringUtils.isEmpty(smsCode))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("validateCode", "null");

        if (StringUtils.isEmpty(phoneNo))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("mobileNo", "null");
        //todo
		if(!"0".equals(smsFlag)){
        	return true;
        }
        String _validateCode = redisCacheManger.getRedisCacheInfo(templateType.getPrekey() + phoneNo);
        if(StringUtils.isEmpty(_validateCode))
            throw new SystemException(UserErrorCode.VALIDATE_CODE_NULL).set("validateCode",smsCode);
        //正确验证之后移除验证码
        if(_validateCode.equals(smsCode)){
            if (flag)
        	    redisCacheManger.destroyRedisCacheInfo(templateType.getPrekey() + phoneNo);
        	return true;
        }else{
        	return false;
        }
         
    }

    public String getSmsSendUrl() {
        return smsSendUrl;
    }

    @Property(name = "SMS_SEND_URL")
    public static void setSmsSendUrl(String smsSendUrl) {
        SmsServiceImpl.smsSendUrl = smsSendUrl;
    }

    public String getSmsUserName() {
        return smsUserName;
    }
    @Property(name = "SMS_USERNAME")
    public static void setSmsUserName(String smsUserName) {
        SmsServiceImpl.smsUserName = smsUserName;
    }

    public String getSmsPwd() {
        return smsPwd;
    }
    @Property(name = "SMS_PWD")
    public static void setSmsPwd(String smsPwd) {
        SmsServiceImpl.smsPwd = smsPwd;
    }

    public String getSmsType() {
        return smsType;
    }
    @Property(name = "SMS_TYPE")
    public static void setSmsType(String smsType) {
        SmsServiceImpl.smsType = smsType;
    }

	public static String getSmsFlag() {
		return smsFlag;
	}
	@Property(name = "SMS_FLAG")
	public static void setSmsFlag(String smsFlag) {
		SmsServiceImpl.smsFlag = smsFlag;
	}
	
	public static String getWxtlUrl() {
		return wxtlUrl;
	}
	@Property(name = "WXTL_SEND_URL")
	public static void setWxtlUrl(String wxtlUrl) {
		SmsServiceImpl.wxtlUrl = wxtlUrl;
	}
	
	public static String getWxtlCid() {
		return wxtlCid;
	}
	
	@Property(name = "WXTL_CID")
	public static void setWxtlCid(String wxtlCid) {
		SmsServiceImpl.wxtlCid = wxtlCid;
	}
	
	public static String getWxtlPwd() {
		return wxtlPwd;
	}
	
	@Property(name = "WXTL_PWD")
	public static void setWxtlPwd(String wxtlPwd) {
		SmsServiceImpl.wxtlPwd = wxtlPwd;
	}
	
	public static String getWxtlPid() {
		return wxtlPid;
	}
	
	@Property(name = "WXTL_PID")
	public static void setWxtlPid(String wxtlPid) {
		SmsServiceImpl.wxtlPid = wxtlPid;
	}
    
}
