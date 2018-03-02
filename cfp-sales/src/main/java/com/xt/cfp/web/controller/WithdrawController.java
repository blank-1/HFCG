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
import com.xt.cfp.core.pojo.ext.phonesell.WithdrawVO;
import com.xt.cfp.core.service.SalesAdminInfoService;
import com.xt.cfp.core.service.WithDrawService;
import com.xt.cfp.core.util.Pagination;

@Controller
@RequestMapping("/phoneSell/withdraw/")
public class WithdrawController extends BaseController {
	
	private static Logger logger = Logger.getLogger(WithdrawController.class);
	
	@Autowired
    private WithDrawService withDrawService;
	@Autowired
    private SalesAdminInfoService salesAdminInfoService;
	
	/**
     * 跳转至提现记录页面
     * @param request
     * @return
     */
	@RequestMapping(value = "withdrawList")
    public String showIncomeList(HttpServletRequest request){
		SalesAdminInfo adminInfo=(SalesAdminInfo)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		if(adminInfo==null){
			return "login";
		}
        return "jsp/withdraw/withdrawList";
    }
	
	/**
	 * 提现记录页面
	 * @param request
	 * @param userCode
	 * @param userName
	 * @param card
	 * @param status
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
    @RequestMapping(value = "showWithdrawList")
    @ResponseBody
    public Object showPrepaidList(HttpServletRequest request,String userCode,String userName,String card,String status,
						    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
						            @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        Map<String, Object> customParams = new HashMap<String, Object>();
        if(userCode!=null&&!"".equals(userCode)){
        	customParams.put("userCode", userCode);
        }
        if(userName!=null&&!"".equals(userName)){
        	customParams.put("userName", userName);
        }
        if(card!=null&&!"".equals(card)){
        	customParams.put("card", card);
        }
        if(status!=null&&!"".equals(status)&&!"0".equals(status)){
        	customParams.put("status", status);
        }
        SalesAdminInfo adminInfo=(SalesAdminInfo)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		String codes=salesAdminInfoService.getAllSubordinateAdminById(Integer.parseInt(adminInfo.getAdminId()+""),adminInfo.getAdminCode());
        customParams.put("codes", codes);
        Pagination<WithdrawVO> results = withDrawService.phonesellWithDrawPaging(pageNo, pageSize, customParams);
        return results;
    }
}
