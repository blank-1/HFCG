package com.xt.cfp.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserAccountHis;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TimeInterval;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/19.
 */
@Controller
@RequestMapping(value = "/cashFlow")
public class CashFlowController extends BaseController {

    private static Logger logger = Logger.getLogger(CashFlowController.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private LoanApplicationService loanApplicationService;


    /**
     * 跳转至账户流水页面
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "showCashFlow")
    public String showCrashFlow(HttpServletRequest request,
                               @RequestParam(required = false) Long userId){
        request.setAttribute("userId", userId);
        return "jsp/bondSource/crashFlowList";
    }

    /**
     * 跳转至借款账户流水页面
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "showCashFlowLoan")
    public String showCashFlowLoan(HttpServletRequest request,
                               @RequestParam(required = false) Long loanApplicationId){
        request.setAttribute("loanApplicationId", loanApplicationId);
        //操作类型
        request.setAttribute("operate",AccountConstants.AccountOperateEnum.values());
        //费用类型
        request.setAttribute("businessType",AccountConstants.BusinessTypeEnum.values());
        return "jsp/loanManage/loan/showCrashFlowLoan";
    }

    /**
     * 查询现金流
     * @param request
     * @param userId
     * @param changeType
     * @param startDate
     * @param endDate
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "crashFlowList")
    @ResponseBody
    public Object crashFlowList(HttpServletRequest request,
                                @RequestParam(value = "userId", required = true) Long userId,
                                @RequestParam(value = "changeType", defaultValue = "0") String changeType,
                                @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                                @RequestParam(value = "endDate" ,defaultValue = "", required = false) String endDate,
                                @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                @RequestParam(value = "page", defaultValue = "1") int pageNo){

        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));
        String sqlChangType="";
        if (!"0".equals(changeType)){
            String[] changeTypes = changeType.split(",");
            for(String type:changeTypes){
                sqlChangType+=type+",";
            }
            changeType = sqlChangType.substring(0,sqlChangType.length()-1);
        }
        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        customParams.put("changeType", changeType);
        UserAccount cashAccount = userAccountService.getCashAccount(userId);

        Pagination<UserAccountHis> bondSourceUserPaging = userAccountService.getCrashFlowPaging(pageNo, pageSize, cashAccount.getAccId(), customParams);
        List<UserAccountHis> userAccountHises= bondSourceUserPaging.getRows();
        //根据页面显示请求做vo包装
        bondSourceUserPaging.setRows(wapper(userAccountHises));
        return bondSourceUserPaging;
    }


  /**
     * 账务流水
     * @param request
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "crashFlowLoanList")
    @ResponseBody
    public Object crashFlowLoanList(HttpServletRequest request,
                                @RequestParam(value = "loanApplicationId", required = true) Long loanApplicationId,
                                @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                                @RequestParam(value = "endDate" ,defaultValue = "", required = false) String endDate,
                                String businessType,
                                String operate,
                                @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                @RequestParam(value = "page", defaultValue = "1") int pageNo){
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));

        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);

        //获取借款申请
        UserAccountHis uah = new UserAccountHis();
        uah.setOwnerType(AccountConstants.AccountChangedTypeEnum.LOAN.getValue());
        uah.setOwnerId(loanApplicationId);
        if(org.apache.commons.lang.StringUtils.isNotEmpty(businessType)){
            uah.setBusType(businessType);
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(operate)){
            uah.setChangeType(operate);
        }
        uah.setAccTypeCode(AccountConstants.AccountTypeEnum.BORROW_ACCOUNT.getValue());
        uah.setOwnerId(loanApplicationId);
        Pagination<UserAccountHis> crashFlowPaging = userAccountService.getCrashFlowPaging(pageNo, pageSize, uah, customParams);
        List<UserAccountHis> userAccountHises= crashFlowPaging.getRows();
        //根据页面显示请求做vo包装
        crashFlowPaging.setRows(wapper(userAccountHises));
        return crashFlowPaging;
    }

    /**
     * 将常量包装一下，前台展示
     * @param userAccountHises
     * @return
     */
    private List<UserAccountHis> wapper(List<UserAccountHis> userAccountHises) {
        if (userAccountHises == null)
            return null;
        //把业务类型转换成描述
        for (UserAccountHis userAccountHis:userAccountHises){
            userAccountHis.setBusType(AccountConstants.BusinessTypeEnum.getBusinessTypeByValue(userAccountHis.getBusType()).getDesc());
            userAccountHis.setChangeType(AccountConstants.AccountOperateEnum.getAccountOperateByValue(userAccountHis.getChangeType()).getDesc());
        }
        return userAccountHises;
    }
    
    /**
     * 平台流水list
     * @param request
     * @param userAccountHis
     * @param startDate
     * @param endDate
     * @param pageSize
     * @param pageNo
     * @return
     */
	@RequestMapping(value = "getSystemFlowList")
	@ResponseBody
	public Object getSystemFlowList(HttpServletRequest request, UserAccountHis userAccountHis,
			@RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
			@RequestParam(value = "endDate", defaultValue = "", required = false) String endDate,
			@RequestParam(value = "rows", defaultValue = "10") int pageSize, @RequestParam(value = "page", defaultValue = "1") int pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		//封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timeInterval", timeInterval);
		params.put("userAccountHis", userAccountHis);
		params.put("customParams", map);
		Pagination<UserAccountHis> bondSourceUserPaging = userAccountService.getSystemFlowList(pageNo, pageSize, params);
		return bondSourceUserPaging;
	}

}
