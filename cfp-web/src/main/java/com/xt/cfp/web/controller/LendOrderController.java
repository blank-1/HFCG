package com.xt.cfp.web.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.constants.AwardPointEnum;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.RepaymentPlanStateEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by luqinglin on 2015/7/14.
 */
@Controller
@RequestMapping(value = "/lendOrder")
public class LendOrderController extends BaseController {
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;

    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private PayService payService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private RateLendOrderService rateLendOrderService ;

    /**
     * 跳转至出借订单详情页面
     *
     * @return
     */
    @RequestMapping(value = "/showOrderDetail")
    public String lendOrderDetail(HttpServletRequest request,Long lendOrderId){
    	if (lendOrderId == null) {
			// todo 没有订单id 跳至错误页
			throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderUserId", lendOrderId);
		}
        LendOrder order = lendOrderService.findById(lendOrderId);
        if (order==null){
            //todo 没有订单 跳至错误页
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderUserId",lendOrderId);
        }
        request.setAttribute("order",order);
        //校验-订单用户和当前用户必须相同
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        if (!order.getLendUserId().equals(currentUser.getUserId())) {
            throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId",order.getLendUserId()).set("user", currentUser.getUserId());
        }
        
      //查询加息券和奖励
      		RateLendOrder rateOrder = rateLendOrderService.findByLendOrderId(lendOrderId, RateLendOrderTypeEnum.RATE_COUPON.getValue(),null);
      		if(rateOrder != null){
      			request.setAttribute("rateOrder", rateOrder);
      		}
      		RateLendOrder activityOrder = rateLendOrderService.findByLendOrderId(lendOrderId, RateLendOrderTypeEnum.ACTIVITY.getValue(),null);
      		if(activityOrder != null){
      			request.setAttribute("activityOrder", activityOrder);
      		}
      		
        
		List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(order.getLendOrderId());
		LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(lendOrderBidDetail.getLoanApplicationId());
		List<PayOrderDetail> paymentOrderDetail = payService.getPaymentOrderDetail(order.getLendOrderId());
		request.setAttribute("paymentOrderDetail", paymentOrderDetail);
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());
        
		String point=StringUtils.isNull(loanApplicationListVO.getAwardPoint())?"":null;
    	if(point==null){
    		if(loanApplicationListVO.getAwardPoint().equals("1")){
    			point=AwardPointEnum.ATMAKELOAN.getDesc();
    		}else if(loanApplicationListVO.getAwardPoint().equals("2")){
    			point=AwardPointEnum.ATREPAYMENT.getDesc();
    		}else{
    			point=AwardPointEnum.ATCOMPLETE.getDesc();
    		}
    	}
		//奖励发放时机
    	loanApplicationListVO.setAwardPoint(point);
		// 预期收益
		BigDecimal expectedInteresting = BigDecimal.ZERO;
		if (order.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
        	//计算奖励金额
        	if(loanApplicationListVO.getAwardRate()!=null&&!loanApplicationListVO.getAwardRate().equals("")&&!loanApplicationListVO.getAwardRate().equals("0")){
            	LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
            	BigDecimal amount=order.getBuyBalance();
            	BigDecimal profit = BigDecimal.ZERO;
        		try {
        			profit = InterestCalculation.getAllInterest(amount,new BigDecimal(loanApplicationListVO.getAwardRate()),loanProduct.getDueTimeType(),
                            loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		profit = BigDecimalUtil.down(profit,2);
        		request.setAttribute("awardProfit", profit);
        	}
        	loanApplicationListVO.setAwardRate(loanApplicationListVO.getAwardRate()!=null&&!loanApplicationListVO.getAwardRate().equals("")&&!loanApplicationListVO.getAwardRate().equals("0")?loanApplicationListVO.getAwardRate()+"%":"");
        	request.setAttribute("loanApplication", loanApplicationListVO);
            
            //预期收益
            LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
            try {
                expectedInteresting = InterestCalculation.getAllInterest(order.getBuyBalance(), order.getProfitRate(), order.getTimeLimitType().charAt(0),
                        loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                expectedInteresting = BigDecimalUtil.down(expectedInteresting, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.setAttribute("expectedInteresting",expectedInteresting);

            return "/order/orderDetail";
		} else if (LendProductTypeEnum.CREDITOR_RIGHTS.getValue().equals(order.getProductType())) {
			loanApplicationListVO
			.setAwardRate(!StringUtils.isNull(loanApplicationListVO.getAwardRate()) && !loanApplicationListVO.getAwardRate().equals("0")
					&& !loanApplicationListVO.getAwardPoint().equals(AwardPointEnum.ATMAKELOAN.getValue()) ? loanApplicationListVO.getAwardRate() + "%" : "");

			request.setAttribute("loanApplication", loanApplicationListVO);
			// 预计收益提前部分还款
			CreditorRightsTransferApplication crta = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(order.getLendOrderId());
			boolean isLookProduct = false ;
			if(crta.getBusStatus().equals(CreditorRightsTransferAppStatus.TRANSFERRING.getValue())){
				isLookProduct = true ;
			}
			request.setAttribute("isLookProduct", isLookProduct);
			String[] temp_expectedInteresting = creditorRightsService.getExpectRightProfit(crta.getCreditorRightsApplyId(), order.getBuyBalance()).toString().split(",");
			expectedInteresting = new BigDecimal(String.valueOf(temp_expectedInteresting[0]));
			if (temp_expectedInteresting.length == 2) {
				BigDecimal awardProfit = new BigDecimal(String.valueOf(temp_expectedInteresting[1]));
				request.setAttribute("awardProfit", awardProfit);
			}
			request.setAttribute("creditorRightsApplyId", crta.getCreditorRightsApplyId());
			request.setAttribute("expectedInteresting",expectedInteresting);
			return "/order/orderDetail";
		} else {
            //省心计划页
            return "redirect:/finance/getAllMyFinanceListDetail?lendOrderId="+lendOrderId;
        }
    }
}
