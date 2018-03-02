package com.xt.cfp.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xt.cfp.core.Exception.SystemException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.external.yeepay.QueryResult;
import com.external.yeepay.TZTService;
import com.external.yeepay.YeePayUtil;
import com.xt.cfp.core.constants.RechargeStatus;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.TimeInterval;

/**
 * Created by luqinglin on 2015/6/19.
 */
@Controller
@RequestMapping(value = "/rechargeOrder")
public class RechargeOrderController extends BaseController {

    private static Logger logger = Logger.getLogger(RechargeOrderController.class);

    @Autowired
    private RechargeOrderService rechargeOrderService;

    /**
     * 跳转至充值列表页面
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "showIncome")
    public String showIncomeList(HttpServletRequest request,
                                 @RequestParam(required = false) Long userId){
        request.setAttribute("userId", userId);
        return "jsp/bondSource/incomeList";
    }

    @RequestMapping(value = "showIncomeList")
    @ResponseBody
    public Object showIncomeList(HttpServletRequest request,
                                 @ModelAttribute RechargeOrder rechargeOrder,
                                 @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                                 @RequestParam(value = "endDate" ,defaultValue = "", required = false) String endDate,
                                 @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));


        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);

        Pagination<RechargeOrder> rechargeOrderPaging = rechargeOrderService.getRechargeOrderPaging(pageNo, pageSize, rechargeOrder, customParams);
        return rechargeOrderPaging;
    }
    
    /**
     * 跳转到：充值单列表
     */
    @RequestMapping(value = "/to_rechargeOrderList")
    public ModelAndView to_admin_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/financial/rechargeOrderList");
        return mv;
    }
    
    /**
     * 执行：分页列表
     * @param pageSize 页数
     * @param pageNo 页码
     * 
     */
	@RequestMapping(value = "/rechargeOrderList")
    @ResponseBody
    public Object rechargeOrderList(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "searchUserName", required = false) String searchUserName,
			@RequestParam(value = "searchBeginTime", required = false) String searchBeginTime,
			@RequestParam(value = "searchEndTime", required = false) String searchEndTime,
			@RequestParam(value = "searchRealName", required = false) String searchRealName,
			@RequestParam(value = "searchState", required = false) String searchState) throws Exception {
		
		// 填充参数
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("searchUserName", searchUserName);
        params.put("searchBeginTime", searchBeginTime);
        params.put("searchEndTime", searchEndTime);
        params.put("searchRealName", searchRealName);
        params.put("searchState", searchState);
        // 执行查询
        return rechargeOrderService.findAllRechargeOrderByPage(pageNo, pageSize, params);
    }
	
	/**
	 * 执行：对账
	 * @param rechargeId 充值订单ID
	 */
 	@RequestMapping("/contrastRechange") 
 	@ResponseBody
 	public Object contrastRechange(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rechargeId", required = false) Long rechargeId){
 		try {
 			// 合法性验证。
 			if(null == rechargeId || "".equals(rechargeId)){
 				return returnResultMap(false, null, "check", "充值订单ID不能为空！");
 			}
 			
 			// 根据充值订单加载一条
 			RechargeOrder rechargeOrder = rechargeOrderService.findRechargeOrderById(rechargeId);
 			if(null == rechargeOrder){
 				return returnResultMap(false, null, "check", "不存在的充值订单ID");
 			}
 			
 			String result=rechargeOrderService.updateOrderStatus(rechargeOrder);
 			if(!result.equals("success")){
 				return returnResultMap(false, null, "check", result);
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
            if (e instanceof SystemException){
                SystemException se = (SystemException)e;
                return returnResultMap(false, null, null, se.getMessage());
            }
 			return returnResultMap(false, null, null, e.getMessage());
 		}
 		return returnResultMap(true, null, null, null);
 	}
 	
	@RequestMapping(value = "exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "searchUserName", required = false) String searchUserName,
			@RequestParam(value = "searchBeginTime", required = false) String searchBeginTime,
			@RequestParam(value = "searchEndTime", required = false) String searchEndTime) {
		
		// 填充参数
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("searchUserName", searchUserName);
        params.put("searchBeginTime", searchBeginTime);
        params.put("searchEndTime", searchEndTime);
		rechargeOrderService.exportExcel(response, params,request);
	}
}
