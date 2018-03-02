package com.xt.cfp.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.ext.LendOrderDetailVO;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;

@Controller
@RequestMapping("/jsp/lendList")
public class LendOrderController extends BaseController{

	@Autowired
	LendOrderService lendOrderService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    
	@RequestMapping(value = "/tofinancialPlanList")
    public ModelAndView tofinancialPlanList() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("/jsp/lendList/financialPlan/financialPlanList");
        return mv;
    }
	
	@RequestMapping(value = "/financialPlanList")
	@ResponseBody
    public Object financialPlanList(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "20") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "searchFinancialName", required=false) String searchFinancialName,
			@RequestParam(value = "searchPeriods", required=false) String searchPeriods,
			@RequestParam(value = "searchLeanUserName", required=false) String searchLeanUserName,
			@RequestParam(value = "searchT", required=false) String searchT,
			@RequestParam(value = "searchBeginTime", required=false) String searchBeginTime,
			@RequestParam(value = "searchEndTime", required=false) String searchEndTime,
			@RequestParam(value = "searchState", required=false) String searchState,
			@RequestParam(value = "userId", required=false) Long userId) {
       
		return lendOrderService.getFinancialPlanList(pageSize, pageNo, searchFinancialName, searchPeriods, searchLeanUserName, searchT, searchBeginTime, searchEndTime, searchState, userId);
    }
	
	@RequestMapping(value = "/tofinancialPlanDetail")
    public ModelAndView tofinancialPlanDetail(@RequestParam(value = "lendOrderId", required=false) Long lendOrderId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("lendOrderExtProduct",lendOrderService.findFinancialPlanById(lendOrderId));
        mv.setViewName("/jsp/lendList/financialPlan/financialPlanDetail");
        return mv;
    }
	
	
	@RequestMapping(value = "/findCreditorRightsByDetailList")
	@ResponseBody
    public Object findCreditorRightsByDetailList(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
    		@RequestParam(value = "lendOrderId", required=false) Long lendOrderId) {
    	
        return lendOrderService.findCreditorRightsByDetailList(pageSize, pageNo, lendOrderId);
    }
	
    /**
     * 跳转到：订单-列表
     */
    @RequestMapping(value = "/to_order_list")
    public ModelAndView to_order_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/order/order_list");
        return mv;
    }
    
    /**
     * 执行：分页列表
     * @param pageSize 页数
     * @param pageNo 页码
     */
	@RequestMapping(value = "/order_list")
    @ResponseBody
    public Object order_list(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "searchOrderCode", required=false) String searchOrderCode,
			@RequestParam(value = "searchLendUserName", required=false) String searchLendUserName,
			@RequestParam(value = "searchOrderPayState", required=false) String searchOrderPayState,
			@RequestParam(value = "searchLendOrderName", required=false) String searchLendOrderName,
			@RequestParam(value = "searchBeginTime", required=false) String searchBeginTime,
			@RequestParam(value = "searchEndTime", required=false) String searchEndTime,
			@RequestParam(value = "searchProductType", required=false) String searchProductType) throws Exception {
		
		// 填充参数
		LendOrder lendOrder = new LendOrder();
        Map params = new HashMap();
        
        if(null != searchOrderCode && !"".equals(searchOrderCode)){
        	params.put("searchOrderCode", searchOrderCode);
        }
        if(null != searchLendUserName && !"".equals(searchLendUserName)){
        	params.put("searchLendUserName", searchLendUserName);
        }
        if(null != searchLendOrderName && !"".equals(searchLendOrderName)){
        	params.put("searchLendOrderName", searchLendOrderName);
        }
        if(null != searchBeginTime && !"".equals(searchBeginTime)){
        	params.put("searchBeginTime", searchBeginTime);
        }
        if(null != searchEndTime && !"".equals(searchEndTime)){
        	params.put("searchEndTime", searchEndTime);
        }
        if(null != searchProductType && !"".equals(searchProductType)){
        	params.put("searchProductType", searchProductType);
        }
        if(null != searchOrderPayState && !"".equals(searchOrderPayState)){
        	params.put("searchOrderPayState", searchOrderPayState);
        }
        return lendOrderService.getLendOrderPaging(pageNo, pageSize, lendOrder, params);
    }
	
    /**
     * 跳转到：订单-详情
     */
    @RequestMapping(value = "/to_order_detail")
    public ModelAndView to_order_detail(
    		@RequestParam(value = "lendOrderId", required=false) Long lendOrderId) {
    	ModelAndView mv = new ModelAndView();
    	
    	// 第一部分
    	LendOrder lendOrder = lendOrderService.findById(lendOrderId);
    	mv.addObject("lendOrder", lendOrder);
    	String productType = lendOrder.getProductType();
    	
    	if(LendProductTypeEnum.RIGHTING.getValue().equals(productType)){// 1投资类
        	// 第二部分 债权情况（上）
        	LendOrderDetailVO detailVO_2AZQ = lendOrderService.getOrderDetail2AZQ(lendOrderId);
        	mv.addObject("detailVO_2AZQ", detailVO_2AZQ);
        	
        	// 第二部分 债权情况（下）
        	LendOrderDetailVO detailVO_2BZQ = lendOrderService.getOrderDetail2BZQ(lendOrderId);
        	mv.addObject("detailVO_2BZQ", detailVO_2BZQ);
    	}else if (LendProductTypeEnum.FINANCING.getValue().equals(productType)) {// 2省心计划
        	// 第二部分 理财情况（上）
        	LendOrderDetailVO detailVO_2ALC = lendOrderService.getOrderDetail2ALC(lendOrderId);
        	mv.addObject("detailVO_2ALC", detailVO_2ALC);
        	
        	// 第二部分 理财情况（下）
        	LendOrderDetailVO detailVO_2BLC = lendOrderService.getOrderDetail2BLC(lendOrderId);
        	mv.addObject("detailVO_2BLC", detailVO_2BLC);
		}
    	
    	// 第三部分（上）
    	LendOrderDetailVO detailVO_3A = lendOrderService.getOrderDetail3A(lendOrderId);
    	mv.addObject("detailVO_3A", detailVO_3A);
    	
    	// 第三部分（下）
    	List<LendOrderDetailVO> detailVO_3B = lendOrderService.getOrderDetail3B(lendOrderId);
    	mv.addObject("detailVO_3B", detailVO_3B);
    	
        mv.setViewName("jsp/order/order_detail");
        return mv;
    }
    
    @RequestMapping(value = "/tofinancialPlanRecreateAgreement")
    @ResponseBody
    public String tofinancialPlanRecreateAgreement(Long lendOrderId) {
    	String result = "" ;
    	//生成合同
		try {
			LendOrder lendOrder = lendOrderService.findById(lendOrderId);
			financePlanProcessModule.beginCalcInterest(lendOrder,lendOrder.getBuyTime());
			result = "success" ;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result ;
    } 
}
