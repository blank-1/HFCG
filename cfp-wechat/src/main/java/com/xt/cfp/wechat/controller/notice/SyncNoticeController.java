package com.xt.cfp.wechat.controller.notice;

import com.external.deposites.model.response.RechargeResponse;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;
import com.xt.cfp.wechat.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 同步回调
 */
@Controller
@RequestMapping("/notice/sync")
public class SyncNoticeController extends BaseController {

    @DoNotNeedLogin
    @RequestMapping(value = "appPersonalRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object appPersonalRecharge(RechargeResponse rechargeResponse, Model model) {
        model.addAttribute("success", rechargeResponse.isSuccess());
        model.addAttribute("message", rechargeResponse.getResp_desc());
        model.addAttribute("amt", rechargeResponse.getAmt());
        return "cg/rechargeSuccess";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "resetPassword")
    public Object resetPassword() {
        return "cg/resetPasswordSuccess";
    }
}
