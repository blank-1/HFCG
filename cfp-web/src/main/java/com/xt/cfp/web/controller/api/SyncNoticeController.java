package com.xt.cfp.web.controller.api;

import com.external.deposites.model.response.*;
import com.external.deposites.utils.SecurityUtils;
import com.xt.cfp.core.constants.ResponseStatusEnum;
import com.xt.cfp.core.pojo.Trade;
import com.xt.cfp.core.service.TradeService;
import com.xt.cfp.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 同步回调
 */
@Controller
@RequestMapping("/api/notice/sync")
public class SyncNoticeController extends BaseController {

    @Autowired
    private TradeService tradeService;

    @ResponseBody
    @RequestMapping(value = "personalRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object personalRecharge(RechargeResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        a.put("rechargeResponse", rechargeResponse);

        System.out.println("PC sync receive parameters : " + a);
        return a;
    }

    @ResponseBody
    @RequestMapping(value = "appPersonalRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object appPersonalRecharge(RechargeResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        a.put("appPersonalRecharge", rechargeResponse);

        System.out.println("APP sync receive parameters : " + a);
        return a;
    }

    @ResponseBody
    @RequestMapping(value = "ebankRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object ebankRecharge(RechargeResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        a.put("ebankRecharge", rechargeResponse);

        System.out.println("e-bank sync receive parameters : " + a);
        return a;
    }

    @ResponseBody
    @RequestMapping(value = "quickRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object quickRecharge(PCRechargeResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        a.put("ebankRecharge", rechargeResponse);

        System.out.println("e-bank sync receive parameters : " + a);
        return a;
    }

    @ResponseBody
    @RequestMapping(value = "ebankRecharge2", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object ebankRecharge2(Ebank2RechargeResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        a.put("ebankRecharge2", rechargeResponse);

        System.out.println("e-bank2 sync receive parameters : " + a);
        return a;
    }

    /**
     * 个人自助开户
     */
    @ResponseBody
    @RequestMapping(value = "personalOpenAccount", params = {"mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code",
            "mobile_no", "cust_nm", "certif_id", "city_id", "parent_bank_id", "capAcntNo"})
    public Object personalOpenAccount(OpenAccount4PCPersonResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        String inputStr = rechargeResponse.regSignVal();
        if ("0000".equals(rechargeResponse.getResp_code())) {//接口返回状态成功
            logger.info(rechargeResponse.getSignature());
            if (rechargeResponse.verifySign(true)) {//验签成功
                a.put("personalOpenAccount", rechargeResponse);
                tradeUpdate(rechargeResponse.regSignVal(),rechargeResponse.getMchnt_txn_ssn(),ResponseStatusEnum.Success);
            } else {//验签失败

            }
        }
        System.out.println(this.getRequest().getParameterMap());
        return a;
    }

    /**
     * 企业自助开户
     * , params = {"mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code",
     * "mobile_no", "cust_nm", "certif_id", "city_id", "parent_bank_id", "capAcntNo"}
     */
    @ResponseBody
    @RequestMapping(value = "enterpriseOpenAccount")
    public Object enterpriseOpenAccount(OpenAccount4PCEnterpriseResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        if (!rechargeResponse.isSuccess()) {
            logger.warn("不成功:{}", rechargeResponse.getResp_desc());
            a.put("error", "不成功");
            return a;
        }
        if (rechargeResponse.verifySign(true)) {
            tradeUpdate(rechargeResponse.regSignVal(),rechargeResponse.getMchnt_txn_ssn(),ResponseStatusEnum.Success);
            a.put("personalOpenAccount", rechargeResponse);
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
            a.put("error", "不成功s");
            return a;
        }
        if (response.verifySign(true)) {
            a.put("pcWithdraw", response);
        } else {
            logger.warn("数据有问题");
        }
        return a;
    }

    /**
     * 重置密码后的空回调，表标成功
     */
    @ResponseBody
    @RequestMapping(value = "resetPassword")
    public Object resetPassword() {
        // 不会有参数的
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("resetPassword receive parameters : " + element + " == " + parameter);
        }
        Map<String, Object> a = new HashMap<>(1);
        a.put("msg", "没有参数的回调，只是返回商户页面");
        return a;
    }

    /**
     * 报备表中的数据设置为已开户成功
     * @param inputStr
     * @param serial_number
     * @param responseStatusEnum
     */
    private void  tradeUpdate(String inputStr,String serial_number ,ResponseStatusEnum responseStatusEnum){
        Trade trade = new Trade();
        trade.setResponse_message(inputStr);
        trade.setTrade_status(responseStatusEnum.getValue());
        trade.setSerial_number(serial_number);
        tradeService.updateByPrimaryKeySelective(trade);
    }
}
