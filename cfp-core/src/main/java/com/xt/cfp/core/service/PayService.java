package com.xt.cfp.core.service;


import com.xt.cfp.core.constants.PayConstants;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.PayResult;
import com.xt.cfp.core.util.Pair;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付相关的服务接口
 * Created by yulei on 2015/7/7.
 */
public interface PayService {

    /**
     * 新建支付订单
     * @param amount 金额
     * @param now 时间
     * @param userId 用户id
     * @param busTypeEnum 业务类型
     * @param amountDetails 金额构成  @return
     */
    PayOrder addPayOrder(BigDecimal amount, Date now, Long userId, PayConstants.BusTypeEnum busTypeEnum, Pair<String, BigDecimal>... amountDetails);

    /**
     * 新建支付订单并与给定的处接订单进行绑定
     * @param lendOrder
     * @param now
     * @param busTypeEnum
     * @param amountDetails
     * @return
     */
    PayOrder addPayOrderForLendOrder(LendOrder lendOrder, Date now, PayConstants.BusTypeEnum busTypeEnum, Pair<String, BigDecimal>... amountDetails);

    /**
     * 完成支付，并处理业务
     * 1.当支付单状态是未支付状态时，首先确认所有支付明细是否完成支付，如果都完成了，在做业务操作
     * 2.当支付单状态是支付中状态时，抛出异常
     * @param payId
     * @param date
     * @return
     */
    PayResult doPay(Long payId, Date date);

    /**
     * 根据主键查找支付订单
     * @param payId
     * @param assertTrue
     * @return
     */
    PayOrder getPayOrderById(Long payId, boolean assertTrue);

    /**
     * 根据支付单Id查找支付明细列表
     * @param payId
     * @return
     */
    List<PayOrderDetail> findPayOrderDetailByPayId(Long payId);

    
    List<PayOrderDetail> getPaymentOrderDetail(long lendOrderId);

    
    /**
     * 添加-支付订单。
     */
    PayOrder addPayOrder(PayOrder payOrder);
    
    /**
     * 添加-支付订单明细。
     */
    PayOrderDetail addPayOrderDetail(PayOrderDetail payOrderDetail);


    /**
     * 根据支付id、支付金额类型获取支付明细数据
     * @param payId
     * @param amountType
     * @return
     */
    PayOrderDetail getPayOrderDetailByPayIdAndAmountType(Long payId, String amountType);

    /**
     * 提现
     * @param withDrawId 提现单号
     */
    void withDraw(Long withDrawId);

    /**
     * 根据出借订单id查询当前未支付的支付单
     * @param lendOrderId
     * @return
     */
    PayOrder getCurrentUnpayPayOrderByLendOrderId(Long lendOrderId);
    /**
     * 根据出借订单id查询当前未支付的支付单
     * @param lendOrderId
     * @return
     */
    PayOrder getPaidPayOrderByLendOrderId(Long lendOrderId);

    /**
     * 获得出借订单
     * @param detailId
     * @return
     */
    LendOrder getLendOrderByPayOrderDetailId(Long detailId);

    /**
     * 创建支付单
     * @param accountPayValue 余额
     * @param rechargePayValue 充值金额
     * @param voucherPayValue 财富券金额
     * @param currentUser 用户
     * @param lendOrder 订单
     * @return PayOrder 支付订单
     */
    PayOrder createPayOrder(BigDecimal accountPayValue, BigDecimal rechargePayValue, BigDecimal voucherPayValue, UserInfo currentUser, LendOrder lendOrder);

    /**
     * 修改支付订单状态
     * @param payOrder
     */
    void update(PayOrder payOrder);

    /**
     * 省心计划后台投标支付订单
     * @param lendOrder
     * @param loanApplicationId
     * @param productPublishId
     * @param amount
     * @param now
     * @param lendProduct
     * @return
     */
    PayOrder createBidPayOrderForFinanceLendOrder(LendOrder lendOrder, Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct,String resource);
    /**
     * 省心计划后台购买债权支付订单
     * @param lendOrder
     * @param loanApplicationId
     * @param productPublishId
     * @param amount
     * @param now
     * @param lendProduct
     * @return
     */
    PayOrder createRightsPayOrderForFinanceLendOrder(LendOrder lendOrder,Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct, String resource,CreditorRightsTransferApplication transferApplication);

    /**
     * 发送省心计划短信接口
     * @Param lendOrder 订单信息
     * @param smsFinanceplanVm 短信模板
     * */
	void sendFinanceResultMsg(LendOrder lendOrder,
			TemplateType smsFinanceplanVm);

    /**
     * 处理支付成功但订单状态为处理中的订单金额
     */
    void handleUndonePayOrderForTimer();

}
