package com.xt.cfp.core.service;

import com.external.llpay.LLPayRequest;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.ClientEnum;
import com.xt.cfp.core.constants.RechargeChannelEnum;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.pojo.ext.phonesell.PrepaidVO;
import com.xt.cfp.core.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/25.
 */
public interface RechargeOrderService {
    /**
     * 向指定账户充值
     *
     * @param rechargeOrder
     * @param accId
     * @return
     */
    RechargeOrder recharge(RechargeOrder rechargeOrder, long accId, String ownerType, long ownerId);

    /**
     * 充值
     *
     * @param rechargeOrder
     * @param accountChangedType
     * @return
     */
    RechargeOrder recharge(RechargeOrder rechargeOrder, AccountConstants.AccountChangedTypeEnum accountChangedType);

    /**
     * 充值记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param rechargeOrder
     * @param customParams
     * @return
     */
    Pagination<RechargeOrder> getRechargeOrderPaging(int pageNo, int pageSize, RechargeOrder rechargeOrder, Map<String, Object> customParams);

    /**
     * 电销充值记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param customParams
     * @return
     */
    Pagination<PrepaidVO> getPrepaidPaging(int pageNo, int pageSize, Map<String, Object> customParams);

    /**
     * 电销统计数据
     *
     * @param customParams
     * @return
     */
    List<String> getAccountAllByStatus(Map<String, Object> customParams);

    /**
     * (易宝支付使用)
     * 新建充值订单
     *
     * @param amount
     * @param bankCardId
     * @param payId
     * @param userId
     * @param channelCode
     * @return
     */
    RechargeOrder addRechargeOrder(BigDecimal amount, Long bankCardId, Long payId, Long userId, String channelCode, String resource);

    /**
     * (连连支付使用)
     * 新建充值订单
     *
     * @param amount
     * @param card
     * @param payId
     * @param userId
     * @param channelCode
     * @return
     */
    RechargeOrder addRechargeOrderForLL(BigDecimal amount, CustomerCard card, Long payId, Long userId, String channelCode, String resource);

    RechargeOrder addRechargeOrderForHF(BigDecimal amount, CustomerCard card, Long payId, Long userId, String channelCode, String resource, String code);

    /**
     * 存管，新建充值订单
     */
    RechargeOrder addRechargeOrderForCg(BigDecimal amount, CustomerCard card, Long userId, String resource);

    /**
     * 根据支付订单id查询充值订单
     *
     * @param payId
     * @return
     */
    RechargeOrder getRechargeOrderByPayId(Long payId);

    /**
     * 确认充值结果
     *
     * @param rechargeCode
     * @param externalNo
     * @param rechargeResult
     * @return
     */
    RechargeOrder confirmRecharge(String rechargeCode, String externalNo, String rechargeResult);

    /**
     * 根据充值单号获取充值单数据，可以选择是否锁定（数据库悲观所）
     *
     * @param rechargeOrderCode
     * @param lock
     * @return
     */
    RechargeOrder getRechargeOrderByOrderCode(String rechargeOrderCode, boolean lock);

    /**
     * 更新充值订单
     *
     * @param rechargeOrder
     */
    void updateRecharge(RechargeOrder rechargeOrder);

    /**
     * 处理支付结果回调
     *
     * @param result
     */
    void handleCallbackNotification(Map<String, String> result);

    /**
     * 获取充值记录列表
     *
     * @param pageNo       页码
     * @param pageSize     条数
     * @param customParams 查询条件
     */
    Pagination<RechargeOrder> findAllRechargeOrderByPage(int pageNo, int pageSize, Map<String, Object> customParams);

    /**
     * 更改支付订单信息
     */
    RechargeOrder updateRechargeOrder(RechargeOrder rechargeOrder);

    /**
     * 根据ID加载一条数据
     *
     * @param rechargeId 充值订单ID
     */
    RechargeOrder findRechargeOrderById(Long rechargeId);

    /**
     * 根据卡id获取充值订单数据
     *
     * @param customerCardId
     * @return
     */
    List<RechargeOrder> findRechargeOrdersByCardId(Long customerCardId);

    /**
     * 后台创建充值订单
     *
     * @param rechargeOrder
     * @return
     */
    RechargeOrder createRechargeOrderByAdminOperation(RechargeOrder rechargeOrder);

    /**
     * 导出excel所有充值列表
     *
     * @param response
     * @param params
     * @param request
     */
    void exportExcel(HttpServletResponse response, Map<String, Object> params, HttpServletRequest request);

    /**
     * 导出借款申请下的投标充值列表
     *
     * @param response
     * @param loanApplicationId
     */
    void exportExcelByLoanAppId(HttpServletResponse response, Long loanApplicationId);

    /**
     * 根据条件查询充值订单
     *
     * @param rechargeOrder
     * @return
     */
    List<RechargeOrder> findBy(RechargeOrder rechargeOrder);

    /**
     * 获取用户充值总额
     *
     * @param userId
     * @return
     */
    BigDecimal getAllRechargeValueByUserId(Long userId);

    /**
     * 处理网银支付结果回调
     *
     * @param result
     */
    void handleCallbackPaymentOnlineNotification(Map<String, String> result);

    /**
     * 获取用户充值总额
     *
     * @param userId
     * @return
     */
    BigDecimal getAllRechargeValueByUserId(Long userId, String month);

    String updateOrderStatus(RechargeOrder rechargeOrder);

    /**
     * POS线下交易充值方法
     *
     * @param externalNo
     * @param amount
     * @param userId
     * @param resource
     * @return Map<String,String>
     */
    Map<String, String> handlePOSRechageNotification(String externalNo,
                                                     String amount, Long userId, String resource);


    /**
     * 新建充值订单
     *
     * @param amount
     * @param bankCardId
     * @param payId
     * @param userId
     * @param channelCode
     * @return
     */
    RechargeOrder addRechargeOrderForFuiou(BigDecimal amount, Long bankCardId,
                                           Long payId, Long userId, String channelCode, String resource,
                                           String externalNo);


    /**
     * 构建支付请求
     *
     * @param accountPayValue  账户余额
     * @param rechargePayValue 充值金额
     * @param lendOrder        订单
     * @param currentUser      用户
     * @param userInfoExt      用户详细信息
     * @param voucherVOs       财富券
     * @param voucherPayValue  财富券金额
     * @param customerCard     新增银行卡
     * @return
     */
    LLPayRequest createPayRequest(BigDecimal accountPayValue, BigDecimal rechargePayValue, LendOrder lendOrder, UserInfo currentUser, UserInfoExt userInfoExt, List<VoucherVO> voucherVOs, BigDecimal voucherPayValue, CustomerCard customerCard, ClientEnum client, RateUser rateUser, RateProduct rateProduct);

    /**
     * 恒丰支付请求
     *
     * @param accountPayValue
     * @param rechargePayValue
     * @param lendOrder
     * @param currentUser
     * @param voucherVOs
     * @param voucherPayValue
     * @param customerCard
     * @param client
     * @param rateUser
     * @param rateProduct
     * @param rechargeChannelEnum
     * @param businessCode
     */
    void createHFPayRequest(BigDecimal accountPayValue, BigDecimal rechargePayValue, LendOrder lendOrder, UserInfo currentUser, List<VoucherVO> voucherVOs, BigDecimal voucherPayValue, CustomerCard customerCard, ClientEnum client, RateUser rateUser, RateProduct rateProduct, RechargeChannelEnum rechargeChannelEnum, String businessCode);

    /**
     * 判断当前流水号对应的业务是否已经加锁
     */
    boolean isPOSssnLock(String externalNo);

    /**
     * 释放富友充值的锁
     */
    void releasePOSssnLock(String externalNo);


    /**
     * 创建充值请求
     *
     * @param rechargeAmount
     * @param currentUser
     * @param userInfoExt
     * @param card
     * @param applyType      (web wap)
     * @return
     */
    LLPayRequest createRechargeRequest(BigDecimal rechargeAmount, UserInfo currentUser, UserInfoExt userInfoExt, CustomerCard card, ClientEnum client);

    /**
     * 创建恒丰充值请求
     *
     * @param rechargeAmount
     * @param currentUser
     * @param card
     * @param rechargeChannelEnum
     * @param client              RechargeOrder
     */
    RechargeOrder createHFRechargeRequest(BigDecimal rechargeAmount, UserInfo currentUser, CustomerCard card, RechargeChannelEnum rechargeChannelEnum, ClientEnum client, Long payId, String businessCode);

    /**
     * 创建网关充值请求
     *
     * @param rechargeAmount
     * @param bankCode
     * @param currentUser
     * @param userInfoExt
     * @param applyType      (web)
     * @return
     */
    LLPayRequest createGatewayRechargeRequest(BigDecimal rechargeAmount, String bankCode, UserInfo currentUser, UserInfoExt userInfoExt, ClientEnum client);


    /**
     * 根据可限制唯一的条件查询一条充值单记录（充值单号，充值渠道）
     */
    RechargeOrder getRechargeOrderByOrderCodeChannel(
            Map<String, String> params, boolean lock);

    /**
     * 创建网关支付请求
     *
     * @param accountPayValue  账户余额
     * @param rechargePayValue 充值金额
     * @param lendOrder        订单
     * @param currentUser      用户
     * @param userInfoExt      用户详细信息
     * @param voucherVOs       财富券
     * @param voucherPayValue  财富券金额
     * @param bankCode         银行卡编码
     * @return
     */
    LLPayRequest createPayGatewayRequest(BigDecimal accountPayValue,
                                         BigDecimal rechargePayValue, LendOrder lendOrder,
                                         UserInfo currentUser, UserInfoExt userInfoExt,
                                         List<VoucherVO> voucherVOs, BigDecimal voucherPayValue,
                                         ClientEnum client, String bankCode, RateUser rateUser, RateProduct rateProduct);
}
