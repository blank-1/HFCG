package com.xt.cfp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.SalesAdminInfo;
import com.xt.cfp.core.pojo.ext.phonesell.PrepaidVO;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.service.SalesAdminInfoService;
import com.xt.cfp.core.util.Pagination;

@Controller
@RequestMapping("/phoneSell/prepaid/")
public class PrepaidController extends BaseController{
	
	private static Logger logger = Logger.getLogger(PrepaidController.class);

    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private SalesAdminInfoService salesAdminInfoService;
    
    /**
     * 跳转至充值记录页面
     * @param request
     * @return
     */
	@RequestMapping(value = "prepaidList")
    public String showIncomeList(HttpServletRequest request,String adminCode){
		SalesAdminInfo adminInfo=(SalesAdminInfo)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		if(adminInfo==null){
			return "login";
		}
		request.setAttribute("adminCode", adminCode);
        return "jsp/prepaid/prepaidList";
    }
	
	/**
	 * 充值记录页面
	 * @param request
	 * @param adminCode
	 * @param userCode
	 * @param userName
	 * @param card
	 * @param status
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
    @RequestMapping(value = "showPrepaidList")
    @ResponseBody
    public Object showPrepaidList(HttpServletRequest request,String adminCode,String userCode,String userName,String card,String status,
						    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
						            @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        Map<String, Object> customParams = new HashMap<String, Object>();
        if(adminCode!=null&&!"".equals(adminCode)){
        	customParams.put("adminCode", adminCode);
        }
        if(userCode!=null&&!"".equals(userCode)){
        	customParams.put("userCode", userCode);
        }
        if(userName!=null&&!"".equals(userName)){
        	customParams.put("userName", userName);
        }
        if(card!=null&&!"".equals(card)){
        	customParams.put("card", card);
        }
        if(status!=null&&!"".equals(status)&&!"-1".equals(status)){
        	customParams.put("status", status);
        }
        SalesAdminInfo adminInfo=(SalesAdminInfo)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		String codes=salesAdminInfoService.getAllSubordinateAdminById(Integer.parseInt(adminInfo.getAdminId()+""),adminInfo.getAdminCode());
        customParams.put("codes", codes);
        Pagination<PrepaidVO> results = rechargeOrderService.getPrepaidPaging(pageNo, pageSize, customParams);
        return results;
    }
}
