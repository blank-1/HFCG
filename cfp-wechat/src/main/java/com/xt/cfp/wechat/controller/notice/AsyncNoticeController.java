package com.xt.cfp.wechat.controller.notice;

import com.external.deposites.model.response.AppRechargeResponse;
import com.external.deposites.model.response.RechargeResponse;
import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.constants.RechargeStatus;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.service.impl.CgBizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * <pre>
 * 异步回调
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/7
 */
@Controller
@RequestMapping(value = "/notice/async/")
public class AsyncNoticeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private CgBizService cgBizService;

    @ResponseBody
    @RequestMapping(value = "appPersonalRecharge", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object appPersonalRecharge(AppRechargeResponse rechargeResponse) {
        logger.info("app 充值{}", rechargeResponse.toString());
        boolean ok;
        try {
            if (!rechargeResponse.isSuccess()) {
                logger.info("处理不成功：{}", rechargeResponse.getResp_desc());
            }
            if (rechargeResponse.verifySign(true)) {
                RechargeOrder rechargeOrder = rechargeOrderService.confirmRecharge(rechargeResponse.getMchnt_txn_ssn(), "", RechargeStatus.SUCCESS.getValue());
                ok = Objects.equals(RechargeStatus.SUCCESS.getValue(), rechargeOrder.getStatus());
            } else {
                logger.info("参数验证不通过");
                ok = false;
            }

        } catch (Exception e) {
            logger.error("app用户充值失败：" + e.getMessage(), e);
            ok = false;
        }
        return HfUtils.response(ok);
    }

    /**
     * app 充值并认购
     */
    @ResponseBody
    @RequestMapping(value = "appRecharge4Buy", params = {"login_id", "amt", "mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code"})
    public Object appRecharge4Buy(AppRechargeResponse rechargeResponse) {
        logger.info("app 充值并购买 {}", rechargeResponse.toString());
        boolean ok;
        try {
            if (!rechargeResponse.isSuccess()) {
                logger.info("处理不成功：{}", rechargeResponse.getResp_desc());
            }
            if (rechargeResponse.verifySign(true)) {
                cgBizService.recharge4Buy(rechargeResponse);
                ok = true;
            } else {
                logger.info("参数验证不通过");
                ok = false;
            }

        } catch (Exception e) {
            logger.error("app用户充值失败：" + e.getMessage(), e);
            ok = false;
        }
        return HfUtils.response(ok);
    }
}
