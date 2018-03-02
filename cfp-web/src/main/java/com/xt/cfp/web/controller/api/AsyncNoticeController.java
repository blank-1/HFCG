package com.xt.cfp.web.controller.api;

import com.external.deposites.model.response.*;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 异步通知
 */
@Controller
@RequestMapping("/api/notice/async/")
public class AsyncNoticeController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private CgBizService cgBizService;


    /**
     * PC快捷充值
     */
    @ResponseBody
    @RequestMapping(value = "personalRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object personalRecharge(RechargeResponse rechargeResponse) {
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("receive parameters : " + element + " == " + parameter);
        }
        System.out.println(this.getRequest().getParameterMap());
        Map<String, Object> a = new HashMap<>(1);
        return a;
    }

    /**
     * app快捷充值
     */
    @ResponseBody
    @RequestMapping(value = "appPersonalRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object appPersonalRecharge(RechargeResponse rechargeResponse) {
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("receive parameters : " + element + " == " + parameter);
        }
        System.out.println(this.getRequest().getParameterMap());
        Map<String, Object> a = new HashMap<>(1);
        a.put("test", rechargeResponse);
        logger.debug("verify sign: ", rechargeResponse.verifySign(true));
        return a;
    }

    /**
     * e-bank充值
     */
    @ResponseBody
    @RequestMapping(value = "ebankRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object ebankRecharge(RechargeResponse rechargeResponse) {
        boolean flag=rechargeResponse.isSuccess();
        try {
            if(rechargeResponse.verifySign(true)){
                if (flag) {
                    cgBizService.recharge4Buy(rechargeResponse);
                }
            }else{
                logger.warn("数据验签异常");
            }

        } catch (Exception e) {
            logger.error("恒丰回调通知失败:"+e.getMessage(),e);
        }
        logger.info(rechargeResponse.toString());
        return null;
    }

    /**
     * 快捷充值
     */
    @ResponseBody
    @RequestMapping(value = "quickRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object quickRecharge(PCRechargeResponse rechargeResponse) {
        boolean flag=rechargeResponse.isSuccess();
        try {
            if(rechargeResponse.verifySign(true)){
                if (flag) {
                    cgBizService.recharge4Buy(rechargeResponse);
                }
            }else{
                logger.warn("数据验签异常");
            }

        } catch (Exception e) {
            logger.error("恒丰回调通知失败:"+e.getMessage(),e);
        }
        logger.info(rechargeResponse.toString());
        return null;
    }



    /**
     * e-bank充值
     */
    @ResponseBody
    @RequestMapping(value = "ebankRecharge2", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object ebankRecharge2(Ebank2RechargeResponse rechargeResponse) {
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("e-bank2 receive parameters : " + element + " == " + parameter);
        }
        System.out.println(this.getRequest().getParameterMap());
        Map<String, Object> a = new HashMap<>(1);
        a.put("ebankRecharge2", rechargeResponse);
        return a;
    }

    /**
     * 个人自助开户
     */
    @ResponseBody
    @RequestMapping(value = "personalOpenAccount", params = {"mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code",
            "mobile_no", "cust_nm", "certif_id", "city_id", "parent_bank_id", "capAcntNo"})
    public Object personalOpenAccount(OpenAccount4PCPersonResponse response) {

        try {
            if(response.verifySign(true)){
                if(response.isSuccess()){
                    cgBizService.openAccount4PersonNotice(response);
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        logger.info(response.toString());
        return null;
    }

    /**
     * 企业自助开户
     * params = {"mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code",
     * "mobile_no", "cust_nm", "certif_id", "city_id", "parent_bank_id", "capAcntNo"}
     */
    @ResponseBody
    @RequestMapping(value = "enterpriseOpenAccount")
    public Object enterpriseOpenAccount(OpenAccount4PCEnterpriseResponse rechargeResponse) {
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("enterpriseOpenAccount receive parameters : " + element + " == " + parameter);
        }
        Map<String, Object> a = new HashMap<>(1);
        if (!rechargeResponse.isSuccess()) {
            logger.warn("不成功");
            a.put("error", "不成功");
            return a;
        }
        if (rechargeResponse.verifySign(true)) {
            a.put("enterpriseOpenAccount", rechargeResponse);
        } else {
            logger.warn("数据有问题");
        }
        return a;
    }

    /**
     * 提现
     */
    @ResponseBody
    @RequestMapping(value = "pcWithdraw")
    public Object pcWithdraw(WithdrawPCResponse response) {
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("pcWithdraw receive parameters : " + element + " == " + parameter);
        }
        Map<String, Object> a = new HashMap<>(1);
        if (!response.isSuccess()) {
            logger.warn("不成功");
            a.put("error", "不成功");
            return a;
        }
        if (response.verifySign(true)) {
            a.put("pcWithdraw", response);
        } else {
            logger.warn("数据有问题");
        }
        return a;
    }
}
