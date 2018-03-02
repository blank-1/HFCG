package com.xt.cfp.wechat.controller;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.ResetPasswordDataSource;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.UserInfoExtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/12
 */
@Controller
@RequestMapping("/apiCombine/")
public class ApiCombineController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IhfApi ihfApi;
    @Autowired
    private UserInfoExtService userInfoExtService;

    @RequestMapping(value = "doResetPassword")
    public Object doResetPassword(@RequestParam String type, Model model) {
        try {
            UserInfoExt currentUser = userInfoExtService.getUserInfoExt4currentUser();
            ResetPasswordDataSource dataSource = new ResetPasswordDataSource();
            dataSource.setBusi_tp(type);
            dataSource.setLogin_id(currentUser.getMobileNo());
            dataSource.setBack_url(PropertiesUtils.property("hf-config", "cg.hf.api.change.password.app.page_notify_url"));
            dataSource = ihfApi.resetPassword(dataSource);
            model.addAttribute("params", dataSource);

        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "cg/doResetPassword";
    }

    /**
     * 开户引道页面
     */
    @RequestMapping(value = "openAccountNotice")
    public Object openAccountNotice() {
        return "cg/w_openBomb";
    }

    /**
     * 开户成功
     */
    @RequestMapping(value = "openSuccess")
    public Object openSuccess(@RequestParam(defaultValue = "/person/account/overview") String fromUrl, Model model) {
        model.addAttribute("fromUrl", fromUrl);
        return "cg/w_openSuccess";
    }
}
