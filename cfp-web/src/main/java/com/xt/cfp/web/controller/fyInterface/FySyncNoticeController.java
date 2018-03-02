package com.xt.cfp.web.controller.fyInterface;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.fyResponse.FyRechargeResponse;
import com.external.deposites.model.fydatasource.LegalPersonDataSource;
import com.external.deposites.model.fydatasource.PerRechargeDataSource;
import com.external.deposites.model.fydatasource.PersonalDataSource;
import com.external.deposites.model.response.Ebank2RechargeResponse;
import com.external.deposites.model.response.OpenAccount4PCPersonResponse;
import com.external.deposites.model.response.legalPersonOpenAccountResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.external.deposites.utils.SecurityUtils;
import com.xt.cfp.core.constants.ResponseEnum;
import com.xt.cfp.core.constants.ResponseStatusEnum;
import com.xt.cfp.core.constants.TradeTypeEnum;
import com.xt.cfp.core.pojo.Trade;
import com.xt.cfp.core.service.TradeService;
import com.xt.cfp.web.controller.BaseController;

/**
 * 同步回调
 */
@Controller
@RequestMapping("/fyInterFace/notice/sync")
public class FySyncNoticeController extends BaseController {
	@Autowired
	private IhfApi hfApi;
	@Autowired
	private TradeService tradeService;
	
	@RequestMapping(value="/testOpenAccOfPer")
	public Object TestOpenPersonAcc (PersonalDataSource dataSource, Model model){
		  dataSource.setCust_nm("测试005");
          dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
          dataSource.setCertif_id("342601199012121853");
          dataSource.setMobile_no("15249910005");
          dataSource.setUser_id_from("9807");
          dataSource.setPage_notify_url("http://60.173.242.198:8028/cfp-web/fyInterFace/notice/sync/personalOpenAccount");
          try {
        	  dataSource = hfApi.openAccountBySelfs(dataSource);
        	  model.addAttribute("params", dataSource);
        	  Trade trade = new Trade();
              String outputStr = dataSource.regSignVal();
              trade.setRequest_message(outputStr);
              trade.setTrade_date(new Date());
              trade.setMessage_id(TradeTypeEnum.UserRegist.getValue());
              trade.setTrade_status(ResponseStatusEnum.Unresponsive.getValue());
              trade.setSerial_number(dataSource.getMchnt_txn_ssn());
              trade.setUser_id(Long.parseLong(dataSource.getUser_id_from()));
              tradeService.addTrade(trade);
          } catch (HfApiException e) {
              e.printStackTrace();
          }
		return "api/doOpenAccount";
	}
	
    /**
     * 个人自助开户回调
     */
    @ResponseBody
    @RequestMapping(value = "/personalOpenAccount")
    public Object personalOpenAccount(OpenAccount4PCPersonResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        String inputStr = rechargeResponse.regSignVal();
        Trade trade = new Trade();
        if("0000".equals(rechargeResponse.getResp_code())){//接口返回状态成功
        	boolean b =  SecurityUtils.verifySign(inputStr, rechargeResponse.getSignature());//进行验签
        	if(b){//验签成功
        		a.put("personalOpenAccount", rechargeResponse);
        		trade.setTrade_status(ResponseStatusEnum.Success.getValue());
        	}else{//验签失败
        		trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
        	}
        }
		trade.setResponse_message(inputStr);
		trade.setSerial_number(rechargeResponse.getMchnt_txn_ssn());
		tradeService.updateByPrimaryKeySelective(trade);
        System.out.println(rechargeResponse);
        return a;
    }

    /**
     * 测试法人开户
     */
    @RequestMapping(value = "/testLegalPersonOpenAcc")
    public Object testLegalPersonOpenAcc(LegalPersonDataSource dataSource, Model model) {
        try {
            //设置回调地址
            dataSource.setPage_notify_url("http://60.173.242.198:8028/cfp-web/fyInterFace/notice/sync/legalPersonOpenAccount");
            dataSource.setCertif_id("342601199205051560");
            dataSource.setUser_id_from("9808");
            dataSource  = hfApi.legalPersonOpenAccountBySelf(dataSource);
            Trade trade = new Trade();
            String outputStr = dataSource.regSignVal();
            trade.setMessage_id(TradeTypeEnum.CorpRegist.getValue());
            trade.setTrade_status(ResponseStatusEnum.Unresponsive.getValue());
            trade.setRequest_message(outputStr);
            trade.setTrade_date(new Date());
            trade.setSerial_number(dataSource.getMchnt_txn_ssn());
            trade.setUser_id(Long.parseLong(dataSource.getUser_id_from()));
            tradeService.addTrade(trade);
            model.addAttribute("params", dataSource);

        } catch (final HfApiException e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
        }
        return "api/doOpenAccount2";
    }

    
    /**
    * 法人自助开户回调
    */
   @ResponseBody
   @RequestMapping(value = "/legalPersonOpenAccount")
   public Object legalPersonOpenAccount(legalPersonOpenAccountResponse rechargeResponse) {
       Map<String, Object> a = new HashMap<>(1);
       String inputStr = rechargeResponse.regSignVal();
       Trade trade = new Trade();
       if("0000".equals(rechargeResponse.getResp_code())){//接口返回状态成功
       	boolean b =  SecurityUtils.verifySign(inputStr, rechargeResponse.getSignature());//进行验签
       	logger.info(rechargeResponse.getSignature());
       	if(b){//验签成功
       		a.put("personalOpenAccount", rechargeResponse);
       		trade.setTrade_status(ResponseStatusEnum.Success.getValue());
       	}else{//验签失败
       		trade.setTrade_status(ResponseStatusEnum.Failue.getValue());
       	}
       }
        trade.setResponse_message(inputStr);
		trade.setSerial_number(rechargeResponse.getMchnt_txn_ssn());
		tradeService.updateByPrimaryKeySelective(trade);
       System.out.println(this.getRequest().getParameterMap());
       return a;
   }
   
   /**
    * 个人快捷充值
    * @return
    */
   @RequestMapping(value="/testPersonRecharge")
   public Object personRecharge(PerRechargeDataSource dataSource,Model model){
	   
	   try {
           //设置回调地址
           dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.page_notify_url"));
           dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.back_notify_url"));
           dataSource.setLogin_id("15111112612");
           dataSource.setAmt(Long.parseLong("1000000"));
           dataSource  = hfApi.personRecharge(dataSource);
           model.addAttribute("params", dataSource);

       } catch (final HfApiException e) {
           logger.error(e.getMessage(), e);
           model.addAttribute("error", e.getMessage());
       }
	return "api/doRecharge";
	   
   }
   
   /**
    * 个人快捷充值回调
    * @param rechargeResponse
    * @return
    */
   @ResponseBody
   @RequestMapping(value = "/personRechargeSync")
   public Object personRechargeSync(FyRechargeResponse rechargeResponse) {
       Map<String, Object> a = new HashMap<>(1);
       String inputStr = rechargeResponse.regSignVal();
       if("0000".equals(rechargeResponse.getResp_code())){//接口返回状态成功
       	boolean b =  SecurityUtils.verifySign(inputStr, rechargeResponse.getSignature());//进行验签
       	logger.info(rechargeResponse.getSignature());
       	if(b){//验签成功
       		a.put("personalOpenAccount", rechargeResponse);
       	}else{//验签失败
       		
       	}
       }
       System.out.println(this.getRequest().getParameterMap());
       return a;
   }
   
   /**
    * 免登陆网银充值
    * @param dataSource
    * @param model
    * @return
    */
   @RequestMapping(value="/testEBankRecharge")
   public Object testEBankRecharge(PerRechargeDataSource dataSource,Model model){
	   
	   try {
           //设置回调地址
           dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank.page_notify_url"));
           dataSource.setLogin_id("18110270585");
           dataSource.setAmt(Long.parseLong("10000"));
           dataSource  = hfApi.ebPersonRecharge(dataSource);
           model.addAttribute("params", dataSource);

       } catch (final HfApiException e) {
           logger.error(e.getMessage(), e);
           model.addAttribute("error", e.getMessage());
       }
	return "api/doRecharge";
	   
   }
   /**
    * 免登陆网银充值回调
    * @param rechargeResponse
    * @return
    */
   @ResponseBody
   @RequestMapping(value = "/eBankRechargeSync")
   public Object eBankRechargeSync(Ebank2RechargeResponse rechargeResponse) {
       Map<String, Object> a = new HashMap<>(1);
       String inputStr = rechargeResponse.regSignVal();
       if("0000".equals(rechargeResponse.getResp_code())){//接口返回状态成功
       	boolean b =  SecurityUtils.verifySign(inputStr, rechargeResponse.getSignature());//进行验签
       	logger.info(rechargeResponse.getSignature());
       	if(b){//验签成功
       		a.put("personalOpenAccount", rechargeResponse);
       	}else{//验签失败
       		
       	}
       }
       System.out.println(this.getRequest().getParameterMap());
       return a;
   }
}
