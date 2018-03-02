package com.xt.cfp.wechat.controller;

import com.xt.cfp.core.constants.PayConstants;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yulei on 2015/7/14.
 */
@Controller
@RequestMapping(value = "/pay")
public class PayController extends BaseController{

    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping("/doPay")
    public String doPay(@RequestParam(value = "cardNo", required = false) String cardNo,
                        @RequestParam(value = "phone", required = false) String phone,
                        HttpServletRequest request) throws Exception {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        if (cardNo != null) {

        //如果是重新绑定银行卡
        } else {//如果是用现有银行卡
            //走支付流程
            CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        }

        return null;
    }
}
