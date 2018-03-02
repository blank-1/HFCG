package com.xt.cfp.web.controller;

import java.math.BigDecimal;
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

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.PayOrderDetail;
import com.xt.cfp.core.pojo.SalesAdminInfo;
import com.xt.cfp.core.pojo.ext.phonesell.LendOrderVO;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.SalesAdminInfoService;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.Pagination;

@Controller
@RequestMapping("/phoneSell/order/")
public class OrderController extends BaseController {
	
	private static Logger logger = Logger.getLogger(OrderController.class);
	
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
    private LendProductService lendProductService;
	@Autowired
    private PayService payService;
	@Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
	@Autowired
    private LoanApplicationService loanApplicationService;
	@Autowired
    private LoanProductService loanProductService;
	@Autowired
    private SalesAdminInfoService salesAdminInfoService;
	
	/**
	 * 跳转至投资记录页面
	 * @param reque
	 * @return
	 */
	@RequestMapping(value = "orderList")
    public String showIncomeList(HttpServletRequest request){
		SalesAdminInfo adminInfo=(SalesAdminInfo)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		if(adminInfo==null){
			return "login";
		}
        return "jsp/order/orderList";
    }
	
	/**
	 * 投资记录页面
	 * @param request
	 * @param userCode
	 * @param userName
	 * @param orderNo
	 * @param productName
	 * @param status
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value = "showOrderList")
    @ResponseBody
    public Object showPrepaidList(HttpServletRequest request,String userCode,String userName,String orderNo,String productName,String status,
						    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
						            @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        Map<String, Object> customParams = new HashMap<String, Object>();
        if(userCode!=null&&!"".equals(userCode)){
        	customParams.put("userCode", userCode);
        }
        if(userName!=null&&!"".equals(userName)){
        	customParams.put("userName", userName);
        }
        if(orderNo!=null&&!"".equals(orderNo)){
        	customParams.put("orderNo", orderNo);
        }
        if(productName!=null&&!"".equals(productName)){
        	customParams.put("productName", productName);
        }
        if(status!=null&&!"".equals(status)&&!"-1".equals(status)){
        	customParams.put("status", status);
        }
        SalesAdminInfo adminInfo=(SalesAdminInfo)request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		String codes=salesAdminInfoService.getAllSubordinateAdminById(Integer.parseInt(adminInfo.getAdminId()+""),adminInfo.getAdminCode());
        customParams.put("codes", codes);
        Pagination<LendOrderVO> results = lendOrderService.getPhonesellOrder(pageNo, pageSize, customParams);
        return results;
    }
	
	/**
	 * 投资记录详情
	 * @param request
	 * @param lendOrderId
	 * @return
	 */
	@RequestMapping(value = "to_lendorder_detail")
    public String to_lendorder_detail(HttpServletRequest request, Long lendOrderId) {
        if (lendOrderId == null) {
        	return "login";
        }
        LendOrder order = lendOrderService.findById(lendOrderId);
        if (order == null) {
            // todo 没有订单 跳至错误页
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderUserId",lendOrderId);
        }
        
        LendProduct lendProduct = lendProductService.findById(order.getLendProductId());
        request.setAttribute("order", order);
        request.setAttribute("lendProduct", lendProduct);

        //预期收益
        BigDecimal expectedInteresting = InterestCalculation.getExpectedInteresting(order.getBuyBalance(), order.getProfitRate(), order.getTimeLimitType().charAt(0), order.getTimeLimit());
        expectedInteresting = BigDecimalUtil.down(expectedInteresting,2);
        List<PayOrderDetail> paymentOrderDetail = payService.getPaymentOrderDetail(order.getLendOrderId());
        request.setAttribute("paymentOrderDetail", paymentOrderDetail);
        if (order.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            //投标详情页
            //借款申请描述
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(order.getLendOrderId());
            LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
            LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(lendOrderBidDetail.getLoanApplicationId());
            request.setAttribute("loanApplication", loanApplication);

            //预期收益
            LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
            try {
                expectedInteresting = InterestCalculation.getAllInterest(order.getBuyBalance(), order.getProfitRate(), order.getTimeLimitType().charAt(0),
                        loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                expectedInteresting = BigDecimalUtil.down(expectedInteresting,2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        request.setAttribute("expectedInteresting", expectedInteresting);
        return "jsp/order/orderDetail";

    }
}
