package com.xt.cfp.web.controller.api;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.OpenAccount4PCEnterpriseDataSource;
import com.external.deposites.model.datasource.OpenAccount4PCPersonalDataSource;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.constants.ResponseStatusEnum;
import com.xt.cfp.core.constants.TradeTypeEnum;
import com.xt.cfp.core.pojo.Trade;
import com.xt.cfp.core.service.TradeService;
import com.xt.cfp.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Enumeration;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/24
 */
@Controller
@RequestMapping(value = "/api/openAccount")
public class OpenAccountController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IhfApi ihfApi;

    @Autowired
    private TradeService tradeService;

    @RequestMapping
    public Object home() {
        return "forward:/api/openAccount/toOpenAccount";
    }

    @RequestMapping(value = "toOpenAccount")
    public Object toOpenAccount() {
        return "api/toOpenAccount";
    }

    /**
     * 开户
     */
    @RequestMapping(value = "doOpenAccount")
    public Object doOpenAccount(OpenAccount4PCPersonalDataSource dataSource, Model model) {
        try {
            dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.self.back_notify_url"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.self.page_notify_url"));
            OpenAccount4PCPersonalDataSource dataSource1 = ihfApi.openAccountBySelf(dataSource, false);
            model.addAttribute("params", dataSource1);
            setTrade(dataSource.regSignVal(), TradeTypeEnum.UserRegist, ResponseStatusEnum.Unresponsive, dataSource.getMchnt_txn_ssn(), Long.parseLong(dataSource.getUser_id_from()));
        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
        }
        return "api/doOpenAccount";
    }

    /**
     * 开户
     */
    @RequestMapping(value = "doOpenAccount2")
    public Object doOpenAccount2(OpenAccount4PCEnterpriseDataSource dataSource, Model model) {
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("receive parameters : " + element + " == " + parameter);
        }
        try {
            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.self.back_notify_url"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.self.page_notify_url"));
            OpenAccount4PCEnterpriseDataSource dataSource1 = ihfApi.openAccountBySelf(dataSource, false);
            model.addAttribute("params", dataSource1);
            setTrade(dataSource.regSignVal(), TradeTypeEnum.CorpRegist, ResponseStatusEnum.Unresponsive, dataSource.getMchnt_txn_ssn(), Long.parseLong(dataSource.getUser_id_from()));
        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return "api/doOpenAccount2";
    }

    @ResponseBody
    @RequestMapping(value = "signAccount")
    public Object signAccount(String mobile, String userId, String cardNo, String realName, String idCard, Model model) {
        OpenAccount4PCPersonalDataSource dataSource = new OpenAccount4PCPersonalDataSource();
        try {
            dataSource.setUser_id_from(userId);
            dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
            dataSource.setCertif_id(idCard);
            dataSource.setMobile_no(mobile);
            dataSource.setCust_nm(realName);
            dataSource.setCapAcntNo(cardNo);
            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.pc.person.self.back_notify_url"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.pc.person.self.page_notify_url"));
            dataSource = ihfApi.openAccountBySelf(dataSource, false);


        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return dataSource;
    }




    /**
     *
     * @param outputStr
     * @param tradeTypeEnum
     * @param responseStatusEnum
     * @param serial_number
     * @param id
     */
    private void setTrade(String outputStr, TradeTypeEnum tradeTypeEnum, ResponseStatusEnum responseStatusEnum, String serial_number, Long id) {
        Trade trade = new Trade();
        trade.setMessage_id(tradeTypeEnum.getValue());
        trade.setTrade_status(responseStatusEnum.getValue());
        trade.setRequest_message(outputStr);
        trade.setTrade_date(new Date());
        trade.setSerial_number(serial_number);
        trade.setUser_id(id);
        tradeService.addTrade(trade);
    }
}
