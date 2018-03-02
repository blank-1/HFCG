package com.xt.cfp.wechat.controller;

import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.InterestCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    private PayService payService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private RateLendOrderService rateLendOrderService;

    /**
     * 跳转至出借订单详情页面
     *
     * @return
     */
    @RequestMapping(value = "/showOrderDetail")
    public String lendOrderDetail(HttpServletRequest request,Long lendOrderId){
        if(lendOrderId==null){
            //todo 没有订单id 跳至错误页
        }
        LendOrder order = lendOrderService.findById(lendOrderId);
        if (order==null){
            //todo 没有订单 跳至错误页
        }
        request.setAttribute("order",order);
        if (order.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())){
            //投标详情页
            //预期收益
            BigDecimal expectedInteresting = InterestCalculation.getExpectedInteresting(order.getBuyBalance(), order.getProfitRate(), order.getTimeLimitType().charAt(0), order.getTimeLimit());
            request.setAttribute("expectedInteresting",expectedInteresting);
            //借款申请描述
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(order.getLendOrderId());
            LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
            LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(lendOrderBidDetail.getLoanApplicationId());
            request.setAttribute("loanApplication",loanApplication);
            List<PayOrderDetail> paymentOrderDetail = payService.getPaymentOrderDetail(order.getLendOrderId());
            request.setAttribute("paymentOrderDetail",paymentOrderDetail);
            return "/order/orderDetail";
        }else{
            //理财详情页
            return "redirect:/finance/getAllMyFinanceListDetail?lendOrderId="+lendOrderId;
        }
    }

    @RequestMapping(value = "/paySuccess")
    public String paySuccess(HttpServletRequest request,Long orderId){
        BigDecimal account=new BigDecimal(0);
        BigDecimal rate=new BigDecimal(0);
        if(null!=orderId){
            LendOrder lendOrder=lendOrderService.findById(orderId);
            LendOrderBidDetail ld=lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId()).get(0);
            LoanApplication loanApplication =loanApplicationService.findById(ld.getLoanApplicationId());
            if(null!=lendOrder){
                request.setAttribute("amount",lendOrder.getBuyBalance());
                request.setAttribute("title",lendOrder.getLendOrderName());
                request.setAttribute("count",lendOrder.getTimeLimit());
                request.setAttribute("loanType",loanApplication.getLoanType());
                List<PayOrderDetail> pays=payService.getPaymentOrderDetail(orderId);
                PayOrderDetail detail=null;
                if(null!=pays&&pays.size()>0){
                    for (PayOrderDetail pay : pays) {
                        PayOrder order=payService.getPayOrderById(pay.getPayId(),false);
                        if(order.getStatus().equals("1")){
                            detail=pay;
                            break;
                        }
                    }
                }
                if(null!=detail){
                    List<Voucher> vouchers=voucherService.getVoucherList(detail.getDetailId());
                    if(null!=vouchers&&vouchers.size()>0){
                        for (Voucher voucher : vouchers) {
                            account=account.add(voucher.getVoucherValue());
                        }
                    }
                }
            }
            List<RateLendOrder> rates=rateLendOrderService.findByLendOrderIdAndType(orderId,"1");
            if(null!=rates&&rates.size()>0){
                for (RateLendOrder r : rates) {
                    rate=rate.add(r.getRateValue());
                }
            }
        }
        request.setAttribute("account",account);
        request.setAttribute("rate",rate);
        return "/order/paySuccess";
    }
}
