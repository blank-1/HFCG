package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.TemplateType;

/**
 * Created by luqinglin on 2015/7/2.
 */
public interface SmsService {

    /**
     * 发送短信
     * @param phoneNo
     * @param content
     * @return
     */
    String sendMsg(String phoneNo, String content);

    /**
     * @author hu
     * @param phoneNo
     * @param content
     * @return
     */
    String sendMsgByWXTL(String phoneNo, String content);

    /**
     * 校验短信验证码
     * @param phoneNo
     * @param smsCode
     * @param templateType
     * @param  flag 是否删除redis记录
     * @return
     */
    boolean validateMsg(String phoneNo, String smsCode, TemplateType templateType,boolean flag);


}
