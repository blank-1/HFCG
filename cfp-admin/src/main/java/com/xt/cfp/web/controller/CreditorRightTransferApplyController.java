package com.xt.cfp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CreditorRightsTransferAppService;
import com.xt.cfp.core.service.LoanApplicationService;

@Controller
@RequestMapping("/jsp/rights/transferApply")
public class CreditorRightTransferApplyController {
	
	@Autowired
	private CreditorRightsService creditorRightsService;
	
	@Autowired
	private CreditorRightsTransferAppService creditorRightsTransferAppService;
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	/**
     * 跳转到：转让中债权-列表
     */
    @RequestMapping(value = "/to_transferApply_list")
    public ModelAndView to_transferApply_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/rights/transfer/transferApply_list");
        return mv;
    }
    
    /**
     * 执行：转让中债权列表数据
     * @param request
     * @param session
     * @param pageSize
     * @param pageNo
     * @param searchLoanApplicationName 借款标题
     * @param searchLoginName 转让人用户名
     * @param searchMobileNo 转让人手机号
     * @param searchT 转让时间查询块
     * @param searchBeginTime 转让开始时间
     * @param searchEndTime 转让结束时间
     * @param searchRightsState 债权状态
     * @param searchTurnState 转让状态
     * @return
     */
	@RequestMapping(value = "/transferApplyList")
	@ResponseBody
    public Object transferApplyList(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "20") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "searchLoanApplicationName", required=false) String searchLoanApplicationName,
			@RequestParam(value = "searchLoginName", required=false) String searchLoginName,
			@RequestParam(value = "searchMobileNo", required=false) String searchMobileNo,
			@RequestParam(value = "searchBeginTime", required=false) String searchBeginTime,
			@RequestParam(value = "searchEndTime", required=false) String searchEndTime,
			@RequestParam(value = "searchRightsState", required=false) String searchRightsState,
			@RequestParam(value = "searchTurnState", required=false) String searchTurnState) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("searchLoanApplicationName", searchLoanApplicationName);
		params.put("searchLoginName", searchLoginName);
		params.put("searchMobileNo", searchMobileNo);
		params.put("searchBeginTime", searchBeginTime);
		params.put("searchEndTime", searchEndTime);
		params.put("searchRightsState", searchRightsState);
		params.put("searchTurnState", searchTurnState);
       
		return creditorRightsTransferAppService.getTransferApplyList(pageSize, pageNo, params);
    }
	
	/**
	 * 跳转到：转让中债权-列表
	 * @param request
	 * @param loanApplicationId
	 * @return
	 */
    @RequestMapping(value = "/showRightTransferApplyDetail")
    public String showLoanDetail(HttpServletRequest request, Long loanApplicationId) {
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        request.setAttribute("loanApplication", loanApplication);
        request.setAttribute("loanApplicationState", LoanApplicationStateEnum.values());
        return "jsp/rights/transfer/transferApply_detail";
    }
	
}
