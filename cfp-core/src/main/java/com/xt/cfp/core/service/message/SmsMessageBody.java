package com.xt.cfp.core.service.message;

import com.xt.cfp.core.service.SmsService;
import com.xt.cfp.core.util.ApplicationContextUtil;

/**
 * Created by lenovo on 2015/8/17.
 */
public class SmsMessageBody extends MessageBody {

    private String mobileNo;

    private String smsContent;

    public SmsMessageBody(String mobileNo, String smsContent) {
        this.mobileNo = mobileNo;
        this.smsContent = smsContent;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    @Override
    public void handle() {
        //消息处理
        SmsService smsService = (SmsService)ApplicationContextUtil.getBean("smsServiceImpl");
        //发送短信
        //smsService.sendMsgByThird(mobileNo,smsContent);
        
        smsService.sendMsgByWXTL(mobileNo, smsContent);
    }
}
