package com.xt.cfp.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.GeneratePDF;
import com.xt.cfp.core.util.PropertiesUtils;
import com.xt.cfp.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;

/**
 * Created by ren yulin on 15-7-23.
 */
@Controller
@RequestMapping("/agreement")
public class AgreementController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private ProvinceInfoService provinceInfoService;
    @Autowired
    private CityInfoService cityInfoService;


    /**
     * 跳转到：充值协议
     */
    @RequestMapping(value = "/to_recharge")
    public ModelAndView to_recharge(@RequestParam(value = "userId") Long userId) {
        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(userId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("realName", userInfoVO.getRealName());
        mv.addObject("idCard", userInfoVO.getIdCard());
        mv.setViewName("agreement/recharge");
        return mv;
    }

    @RequestMapping(value = "/recharge")
    @ResponseBody
    public Object recharge(@RequestParam(value = "userId") Long userId) {
        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(userId);
        String htmlUrl = "http://localhost/cfp/agreement/to_recharge?userId=" + userId;
        try {
            GeneratePDF.create(htmlUrl, "d:\\temp", userInfoVO.getLoginName() + "_充值协议");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 跳转到：出借咨询与服务协议
     */
//    @RequestMapping(value = "/to_service")
//    public ModelAndView to_service(@RequestParam(value = "creditorRightsId") Long creditorRightsId) {
//        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId);
//
//        LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());
//        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(lendOrder.getLendUserId());
//
//        Date buyDate = creditorRights.getCreateTime();
//
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("realName", userInfoVO.getRealName());
//        mv.addObject("idCard", userInfoVO.getIdCard());
//        mv.addObject("mobileNO", userInfoVO.getMobileNo());
//        if (userInfoVO.getProvince() != null && userInfoVO.getCity() != null) {
//            mv.addObject("address", provinceInfoService.findById(userInfoVO.getProvince()) + "-" + cityInfoService.findById(userInfoVO.getCity()) + userInfoVO.getDetail());
//        }
//        mv.addObject("balance", lendOrder.getBuyBalance());
//        mv.addObject("year", DateUtil.getYear(buyDate));
//        mv.addObject("month", DateUtil.getMonth(buyDate));
//        mv.addObject("day", DateUtil.getDay(buyDate));
//
//        mv.setViewName("agreement/service");
//        return mv;
//    }
    @RequestMapping(value = "/fetchTransAgreementHtml",method = RequestMethod.POST)
    public ModelAndView fetchTransAgreementHtml(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        if (!StringUtils.isNull(json)) {
            JSONObject jsonObject = JSON.parseObject(json);
            Date buyDate = jsonObject.getDate("buyDate");
            mv.addObject("realName", jsonObject.get("realName"));
            mv.addObject("idCard", jsonObject.get("idCard"));
            mv.addObject("mobileNO", jsonObject.get("mobileNO"));
            if (!StringUtils.isNull(jsonObject.getString("address"))) {
                mv.addObject("address", jsonObject.getString("address"));
            }
            mv.addObject("balance", jsonObject.get("balance"));
            mv.addObject("year", DateUtil.getYear(buyDate));
            mv.addObject("month", DateUtil.getMonth(buyDate));
            mv.addObject("day", DateUtil.getDay(buyDate));
            mv.addObject("feesItems", jsonObject.get("feesItems"));
            mv.addObject("path", PropertiesUtils.getInstance().get("BACKGROUND_PATH"));

        }
        mv.setViewName("agreement/service");
        return mv;
    }

    @RequestMapping(value = "/service")
    @ResponseBody
    public Object service(@RequestParam(value = "json") String json) {
        System.out.println("enter agreement/service");
        String htmlUrl = PropertiesUtils.getInstance().get("FRONT_PATH") + "/agreement/to_service?json=" + json;

        try {
            GeneratePDF.create(htmlUrl, PropertiesUtils.getInstance().get("AGREEMENT_PATH"),"_出借咨询与服务协议");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println("agreement/service complete ");
        return null;
    }
    
    
    /**
     * 借款及服务协议（线上借款标-新）
     */
    @RequestMapping(value = "/service_loan", method = RequestMethod.POST)
    public ModelAndView service_loan(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
        	mv.addObject("loanRealName", jsonObject.get("loanRealName"));
        	mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
        	mv.addObject("resultBalance", jsonObject.get("resultBalance"));
        	mv.addObject("resultBalanceBig", jsonObject.get("resultBalanceBig"));
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("repaymentType", jsonObject.get("repaymentType"));
        	mv.addObject("annualRate", jsonObject.get("annualRate"));
        	mv.addObject("loanUseage", jsonObject.get("loanUseage"));
        	mv.addObject("loanAddress", jsonObject.get("loanAddress"));
        	mv.addObject("loanMobileNo", jsonObject.get("loanMobileNo"));
        	mv.addObject("lendAddress", jsonObject.get("lendAddress"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	
        	mv.addObject("loanCardCode", jsonObject.get("loanCardCode"));
        	mv.addObject("loanBankName", jsonObject.get("loanBankName"));
        	mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
        	mv.addObject("lendBankName", jsonObject.get("lendBankName"));
        	
        	mv.addObject("loanEmail", jsonObject.get("loanEmail"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	
        	mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
        	mv.addObject("shouldBalance2", jsonObject.get("shouldBalance2"));
        	mv.addObject("repaymentDay", jsonObject.get("repaymentDay"));
        	
        	mv.addObject("feesItems", jsonObject.get("feesItems"));
        }
        
        mv.setViewName("agreement/service_loan");
        return mv;
    }
    
    /**
     * 借款及服务协议（线上债权标-新）
     */
    @RequestMapping(value = "/service_creditor", method = RequestMethod.POST)
    public ModelAndView service_creditor(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
        	mv.addObject("loanRealName", jsonObject.get("loanRealName"));
        	mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
        	mv.addObject("resultBalance", jsonObject.get("resultBalance"));
        	mv.addObject("resultBalanceBig", jsonObject.get("resultBalanceBig"));
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("repaymentType", jsonObject.get("repaymentType"));
        	mv.addObject("annualRate", jsonObject.get("annualRate"));
        	mv.addObject("loanUseage", jsonObject.get("loanUseage"));
        	mv.addObject("loanAddress", jsonObject.get("loanAddress"));
        	mv.addObject("loanMobileNo", jsonObject.get("loanMobileNo"));
        	mv.addObject("lendAddress", jsonObject.get("lendAddress"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	
        	mv.addObject("loanCardCode", jsonObject.get("loanCardCode"));
        	mv.addObject("loanBankName", jsonObject.get("loanBankName"));
        	mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
        	mv.addObject("lendBankName", jsonObject.get("lendBankName"));
        	
        	mv.addObject("loanEmail", jsonObject.get("loanEmail"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	
        	mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
        	mv.addObject("shouldBalance2", jsonObject.get("shouldBalance2"));
        	mv.addObject("repaymentDay", jsonObject.get("repaymentDay"));
        	
        	mv.addObject("feesItems", jsonObject.get("feesItems"));
        }
        
        mv.setViewName("agreement/service_creditor");
        return mv;
    }
    
    /**
     * 定向委托投资管理协议
     */
    @RequestMapping(value = "/directional_commissioned", method = RequestMethod.POST)
    public ModelAndView directional_commissioned(@RequestParam(value = "json")String json) {
        ModelAndView mv = new ModelAndView();
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
        	mv.addObject("lendUserName", jsonObject.get("lendUserName"));
        	mv.addObject("enterpriseName", jsonObject.get("enterpriseName"));
        	mv.addObject("loanUserName", jsonObject.get("loanUserName"));
        	mv.addObject("productName", jsonObject.get("productName"));
        	mv.addObject("loanStartTime", jsonObject.get("loanStartTime"));
        	mv.addObject("loanEndTime", jsonObject.get("loanEndTime"));
        	mv.addObject("yearRate", jsonObject.get("yearRate"));
        	mv.addObject("accountMoney", jsonObject.get("accountMoney"));
        	mv.addObject("investmentMoney", jsonObject.get("investmentMoney"));
        	mv.addObject("companyName", jsonObject.get("companyName"));
        	mv.addObject("fee", jsonObject.get("fee"));
        	mv.addObject("profitRate", jsonObject.get("profitRate"));
        }
        mv.setViewName("agreement/directional_commissioned");
        return mv;
    }
    
    /**
     * 债权转让及受让协议
     */
    @RequestMapping(value = "/service_creditor_assignment", method = RequestMethod.POST)
    public ModelAndView service_creditor_assignment(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	
        	mv.addObject("sourceMobileNo", jsonObject.get("sourceMobileNo"));
        	mv.addObject("sourceEmail", jsonObject.get("sourceEmail"));
        	mv.addObject("sourceRealName", jsonObject.get("sourceRealName"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	
        	mv.addObject("loanApplicationCode", jsonObject.get("loanApplicationCode"));
        	mv.addObject("loanRealName", jsonObject.get("loanRealName"));
        	mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
        	
        	mv.addObject("resultBalance", jsonObject.get("resultBalance"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("cycleCount", jsonObject.get("cycleCount"));
        	mv.addObject("buyBalance", jsonObject.get("buyBalance"));
        	mv.addObject("buyBalanceBig", jsonObject.get("buyBalanceBig"));
        	mv.addObject("annualRate", jsonObject.get("annualRate"));
        	mv.addObject("lendTime", jsonObject.get("lendTime"));
        	
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("repaymentType", jsonObject.get("repaymentType"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
        	
        	mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
        	mv.addObject("lendBankName", jsonObject.get("lendBankName"));
        	
        	mv.addObject("feesItems", jsonObject.get("feesItems"));
        	
        }
        
        mv.setViewName("agreement/service_creditor_assignment");
        return mv;
    }
    
    /**
     * 借款及服务协议（企业）
     */
    @RequestMapping(value = "/enterprise_service_loan", method = RequestMethod.POST)
    public ModelAndView enterprise_service_loan(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
        	mv.addObject("resultBalance", jsonObject.get("resultBalance"));
        	mv.addObject("resultBalanceBig", jsonObject.get("resultBalanceBig"));
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("repaymentType", jsonObject.get("repaymentType"));
        	mv.addObject("annualRate", jsonObject.get("annualRate"));
        	mv.addObject("loanUseage", jsonObject.get("loanUseage"));
        	mv.addObject("loanAddress", jsonObject.get("loanAddress"));
        	mv.addObject("lendAddress", jsonObject.get("lendAddress"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	
        	mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
        	mv.addObject("lendBankName", jsonObject.get("lendBankName"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	
        	mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
        	mv.addObject("shouldBalance2", jsonObject.get("shouldBalance2"));
        	mv.addObject("repaymentDay", jsonObject.get("repaymentDay"));
        	
        	mv.addObject("feesItems", jsonObject.get("feesItems"));
        	
            mv.addObject("enterpriseName", jsonObject.get("enterpriseName"));
            mv.addObject("organizationCode", jsonObject.get("organizationCode"));
            mv.addObject("legalPersonName", jsonObject.get("legalPersonName"));
            mv.addObject("legalPersonCode", jsonObject.get("legalPersonCode"));
        }
        
        mv.setViewName("agreement/enterprise_service_loan");
        return mv;
    }
    
    
    /**
     * 借款及服务协议（含保证人）
     */
    @RequestMapping(value = "/service_creditor_guarantee", method = RequestMethod.POST)
    public ModelAndView service_creditor_guarantee(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
        	mv.addObject("loanRealName", jsonObject.get("loanRealName"));
        	mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
        	mv.addObject("resultBalance", jsonObject.get("resultBalance"));
        	mv.addObject("resultBalanceBig", jsonObject.get("resultBalanceBig"));
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("repaymentType", jsonObject.get("repaymentType"));
        	mv.addObject("annualRate", jsonObject.get("annualRate"));
        	mv.addObject("loanUseage", jsonObject.get("loanUseage"));
        	mv.addObject("loanAddress", jsonObject.get("loanAddress"));
        	mv.addObject("loanMobileNo", jsonObject.get("loanMobileNo"));
        	mv.addObject("lendAddress", jsonObject.get("lendAddress"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	
        	mv.addObject("loanCardCode", jsonObject.get("loanCardCode"));
        	mv.addObject("loanBankName", jsonObject.get("loanBankName"));
        	mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
        	mv.addObject("lendBankName", jsonObject.get("lendBankName"));
        	
        	mv.addObject("loanEmail", jsonObject.get("loanEmail"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	
        	mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
        	mv.addObject("shouldBalance2", jsonObject.get("shouldBalance2"));
        	mv.addObject("repaymentDay", jsonObject.get("repaymentDay"));
        	
        	mv.addObject("feesItems", jsonObject.get("feesItems"));
        	
        	mv.addObject("guaranteeCompany", jsonObject.get("guaranteeCompany"));
        }
        
        mv.setViewName("agreement/service_creditor_guarantee");
        return mv;
    }
    
    /**
     * 债权转让及受让协议(含保证人)
     */
    @RequestMapping(value = "/service_creditor_assignment_guarantee", method = RequestMethod.POST)
    public ModelAndView service_creditor_assignment_guarantee(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	
        	mv.addObject("sourceMobileNo", jsonObject.get("sourceMobileNo"));
        	mv.addObject("sourceEmail", jsonObject.get("sourceEmail"));
        	mv.addObject("sourceRealName", jsonObject.get("sourceRealName"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	
        	mv.addObject("loanApplicationCode", jsonObject.get("loanApplicationCode"));
        	mv.addObject("loanRealName", jsonObject.get("loanRealName"));
        	mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
        	
        	mv.addObject("resultBalance", jsonObject.get("resultBalance"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("cycleCount", jsonObject.get("cycleCount"));
        	mv.addObject("buyBalance", jsonObject.get("buyBalance"));
        	mv.addObject("buyBalanceBig", jsonObject.get("buyBalanceBig"));
        	mv.addObject("annualRate", jsonObject.get("annualRate"));
        	mv.addObject("lendTime", jsonObject.get("lendTime"));
        	
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("repaymentType", jsonObject.get("repaymentType"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
        	
        	mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
        	mv.addObject("lendBankName", jsonObject.get("lendBankName"));
        	
        	mv.addObject("feesItems", jsonObject.get("feesItems"));
        	
        	mv.addObject("guaranteeCompany", jsonObject.get("guaranteeCompany"));
        	
        }
        
        mv.setViewName("agreement/service_creditor_assignment_guarantee");
        return mv;
    }
    
    /**
     * 省心计划委托协议
     */
    @RequestMapping(value = "/permission_financeplan", method = RequestMethod.POST)
    public ModelAndView permission_financeplan(@RequestParam(value = "json") String json) {
    	ModelAndView mv = new ModelAndView();
    	
    	if (!StringUtils.isNull(json)) {
    		JSONObject jsonObject = JSON.parseObject(json);
    		mv.addObject("realName", jsonObject.get("realName"));
    		mv.addObject("idCard", jsonObject.get("idCard"));
    		mv.addObject("mobileNo", jsonObject.get("mobileNo"));
    		mv.addObject("detail", jsonObject.get("detail"));
    		mv.addObject("loginName", jsonObject.get("loginName"));
    		mv.addObject("buyBalanceBig", jsonObject.get("buyBalanceBig"));
    		mv.addObject("buyBalance", jsonObject.get("buyBalance"));
    		mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
			mv.addObject("agreementCode", jsonObject.get("agreementCode"));
    		mv.addObject("profitReturnConfig", jsonObject.get("profitReturnConfig"));
    		mv.addObject("userId",jsonObject.get("userId"));
    	}
    	
    	mv.setViewName("agreement/permission_financeplan");
    	return mv;
    }

	@RequestMapping(value = "/service_loan_cash")
	public ModelAndView service_loan_cash(String json) {
		ModelAndView mv = new ModelAndView();

		if (!StringUtils.isNull(json)) {
			JSONObject jsonObject = JSON.parseObject(json);
			mv.addObject("agreementCode", jsonObject.get("agreementCode"));
			mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
			mv.addObject("lendRealName", jsonObject.get("lendRealName"));
			mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
			mv.addObject("loanRealName", jsonObject.get("loanRealName"));
			mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
			mv.addObject("resultBalance", jsonObject.get("resultBalance"));
			mv.addObject("resultBalanceBig", jsonObject.get("resultBalanceBig"));
			mv.addObject("paymentDate", jsonObject.get("paymentDate"));
			mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
			mv.addObject("dueTime", jsonObject.get("dueTime"));
			mv.addObject("repaymentType", jsonObject.get("repaymentType"));
			mv.addObject("annualRate", jsonObject.get("annualRate"));
			mv.addObject("loanUseage", jsonObject.get("loanUseage"));
			mv.addObject("loanAddress", jsonObject.get("loanAddress"));
			mv.addObject("loanMobileNo", jsonObject.get("loanMobileNo"));
			mv.addObject("lendAddress", jsonObject.get("lendAddress"));
			mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));

			mv.addObject("loanCardCode", jsonObject.get("loanCardCode"));
			mv.addObject("loanBankName", jsonObject.get("loanBankName"));
			mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
			mv.addObject("lendBankName", jsonObject.get("lendBankName"));

			mv.addObject("loanEmail", jsonObject.get("loanEmail"));
			mv.addObject("lendEmail", jsonObject.get("lendEmail"));

			mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
			mv.addObject("shouldBalance2", jsonObject.get("shouldBalance2"));
			mv.addObject("interest2", jsonObject.get("interest2"));
			mv.addObject("repaymentDay", jsonObject.get("repaymentDay"));

			mv.addObject("feesItems", jsonObject.get("feesItems"));
		}

		mv.setViewName("agreement/service_loan_cash");
		return mv;
	}

	/**
	 * 车贷合同
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/service_loan_person_car")
	public ModelAndView service_loan_person_car(@RequestParam(value = "json") String json) {
		ModelAndView mv = new ModelAndView();

		if (!StringUtils.isNull(json)) {
			JSONObject jsonObject = JSON.parseObject(json);
			mv.addObject("agreementCode", jsonObject.get("agreementCode"));
			mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
			mv.addObject("lendRealName", jsonObject.get("lendRealName"));
			mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
			mv.addObject("loanRealName", jsonObject.get("loanRealName"));
			mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
			mv.addObject("resultBalance", jsonObject.get("resultBalance"));
			mv.addObject("resultBalanceBig", jsonObject.get("resultBalanceBig"));
			mv.addObject("paymentDate", jsonObject.get("paymentDate"));
			mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
			mv.addObject("dueTime", jsonObject.get("dueTime"));
			mv.addObject("repaymentType", jsonObject.get("repaymentType"));
			mv.addObject("annualRate", jsonObject.get("annualRate"));
			mv.addObject("loanUseage", jsonObject.get("loanUseage"));
			mv.addObject("loanAddress", jsonObject.get("loanAddress"));
			mv.addObject("loanMobileNo", jsonObject.get("loanMobileNo"));
			mv.addObject("lendAddress", jsonObject.get("lendAddress"));
			mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));

			mv.addObject("loanCardCode", jsonObject.get("loanCardCode"));
			mv.addObject("loanBankName", jsonObject.get("loanBankName"));
			mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
			mv.addObject("lendBankName", jsonObject.get("lendBankName"));

			mv.addObject("loanEmail", jsonObject.get("loanEmail"));
			mv.addObject("lendEmail", jsonObject.get("lendEmail"));

			mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
			mv.addObject("shouldBalance2", jsonObject.get("shouldBalance2"));
			mv.addObject("repaymentDay", jsonObject.get("repaymentDay"));

			mv.addObject("feesItems", jsonObject.get("feesItems"));
		}

		mv.setViewName("agreement/service_loan_person_car");
		return mv;
	}
    
    /**
     * 借款及服务协议（个人房产直投）
     */
    @RequestMapping(value = "/service_loan_direct_house")//, method = RequestMethod.POST
    public ModelAndView service_loan_direct_house(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
        	mv.addObject("loanRealName", jsonObject.get("loanRealName"));
        	mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
        	mv.addObject("resultBalance", jsonObject.get("resultBalance"));
        	mv.addObject("resultBalanceBig", jsonObject.get("resultBalanceBig"));
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("repaymentType", jsonObject.get("repaymentType"));
        	mv.addObject("annualRate", jsonObject.get("annualRate"));
        	mv.addObject("loanUseage", jsonObject.get("loanUseage"));
        	mv.addObject("loanAddress", jsonObject.get("loanAddress"));
        	mv.addObject("loanMobileNo", jsonObject.get("loanMobileNo"));
        	mv.addObject("lendAddress", jsonObject.get("lendAddress"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	
        	mv.addObject("loanCardCode", jsonObject.get("loanCardCode"));
        	mv.addObject("loanBankName", jsonObject.get("loanBankName"));
        	mv.addObject("lendCardCode", jsonObject.get("lendCardCode"));
        	mv.addObject("lendBankName", jsonObject.get("lendBankName"));
        	
        	mv.addObject("loanEmail", jsonObject.get("loanEmail"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	
        	mv.addObject("repaymentPlanList", jsonObject.get("repaymentPlanList"));
        	mv.addObject("shouldBalance2", jsonObject.get("shouldBalance2"));
        	mv.addObject("repaymentDay", jsonObject.get("repaymentDay"));
        	
        	mv.addObject("feesItems", jsonObject.get("feesItems"));
        }
        
        mv.setViewName("agreement/service_loan_direct_house");
        return mv;
    }
    
    /**
     * 授权委托书（个人房产直投）
     */
    @RequestMapping(value = "/service_loan_direct_house_entrust")//, method = RequestMethod.POST
    public ModelAndView service_loan_direct_house_entrust(@RequestParam(value = "json") String json) {
        ModelAndView mv = new ModelAndView();
        
        if (!StringUtils.isNull(json)) {
        	JSONObject jsonObject = JSON.parseObject(json);
        	mv.addObject("agreementCode", jsonObject.get("agreementCode"));
        	mv.addObject("agreementStartDate", jsonObject.get("agreementStartDate"));
        	mv.addObject("lendRealName", jsonObject.get("lendRealName"));
        	mv.addObject("lendIdCard", jsonObject.get("lendIdCard"));
        	mv.addObject("loanRealName", jsonObject.get("loanRealName"));
        	mv.addObject("loanIdCard", jsonObject.get("loanIdCard"));
        	mv.addObject("paymentDate", jsonObject.get("paymentDate"));
        	mv.addObject("lastRepaymentDate", jsonObject.get("lastRepaymentDate"));
        	mv.addObject("dueTime", jsonObject.get("dueTime"));
        	mv.addObject("loanAddress", jsonObject.get("loanAddress"));
        	mv.addObject("loanMobileNo", jsonObject.get("loanMobileNo"));
        	mv.addObject("lendAddress", jsonObject.get("lendAddress"));
        	mv.addObject("lendMobileNo", jsonObject.get("lendMobileNo"));
        	mv.addObject("loanEmail", jsonObject.get("loanEmail"));
        	mv.addObject("lendEmail", jsonObject.get("lendEmail"));
        	mv.addObject("addressDetail", jsonObject.get("addressDetail"));
        	mv.addObject("houseCardNumber", jsonObject.get("houseCardNumber"));
        }
        
        mv.setViewName("agreement/service_loan_direct_house_entrust");
        return mv;
    }
    
}
