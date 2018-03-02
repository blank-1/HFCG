package com.external.deposites.api;


import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.*;
import com.external.deposites.model.fydatasource.LegalPersonDataSource;
import com.external.deposites.model.fydatasource.PerRechargeDataSource;
import com.external.deposites.model.fydatasource.PersonalDataSource;
import com.external.deposites.model.response.*;

/**
 * <pre>
 *     所有存管接口只能在这里出现
 * </pre>
 *
 * @author luyanfeng
 */
public interface IhfApi {

    /**
     * 开户
     *
     * @param dataSource
     */
    CommonOpenAccount4ApiResponse openAccount(AbstractOpenAccount4ApiDataSource dataSource) throws HfApiException;

    /**
     * 预授权
     */
    PreAuthorizationResponse preAuth(PreAuthorizationDataSource dataSource) throws HfApiException;

    /**
     * 预授权撤销
     */
    CancelPreAuthResponse cancelPreAuth(CancelPreAuthDataSource dataSource) throws HfApiException;

    /**
     * PC 个人用户免登录快捷充值
     */
    RechargeDataSource pcQuickRecharge(RechargeDataSource rechargeDataSource) throws HfApiException;

    /**
     * APP 个人用户免登录快捷充值
     */
    RechargeDataSource appRecharge4personal(RechargeDataSource rechargeDataSource) throws HfApiException;

    /**
     * E-bank 免登录网银充值
     */
    RechargeDataSource ebankRecharge(RechargeDataSource rechargeDataSource) throws HfApiException;

    /**
     * E-bank 免登录直接跳转网银界面充值接口
     */
    Ebank2RechargeDataSource ebankRecharge2(Ebank2RechargeDataSource rechargeDataSource) throws HfApiException;

    /**
     * 余额查询
     */
    QueryBalanceResponse queryBalance(BalanceDataSource balanceDataSource) throws HfApiException;

    /**
     * 查询充值流水
     */
    QueryRechargeWithdrawResponse queryRecharge(QueryRechargeWithdrawDataSource dataSource) throws HfApiException;

    /**
     * 查询交易流水
     */
    QueryBusinessResponse queryBusiness(QueryBusinessDataSource dataSource)throws HfApiException;

    /**
     * 查询提现流水
     */
    QueryRechargeWithdrawResponse queryWithdraw(QueryRechargeWithdrawDataSource dataSource) throws HfApiException;

    /**
     * <pre>
     * 转账、划拨： 到可用
     * 1、商户到用户（可用到可用）
     * 2、用户到商户（可用到未转结）
     * 3、用户到用户（可用/冻结到可用）:冻结到可用 需要合同号，也就是预授权接口的返回值
     * </pre>
     *
     * @param dataSource 参数源
     */
    TransferAccountsResponse transferAccounts(TransferAccountsDataSource dataSource) throws HfApiException;


    /**
     * 转账、划拨：可用到冻结
     */
    TransferAccountsResponse transferAccountsToFreeze(PreAuthorizationDataSource dataSource) throws HfApiException;

    /**
     * 划拨：冻结到冻结
     */
    TransferFreeze2FreezeResponse transferFreezeToFreeze(TransferFreeze2FreezeDataSource dataSource) throws HfApiException;

    /**
     * 无方向的冻结
     */
    FreezeResponse freeze(FreezeDataSource dataSource) throws HfApiException;

    /**
     * 解冻
     */
    UnfreezeResponse unfreeze(FreezeDataSource dataSource) throws HfApiException;

    /**
     * 自助开户
     * 是否是h5开户
     */
    <T extends AbstractOpenAccount4PCDataSource> T openAccountBySelf(AbstractOpenAccount4PCDataSource dataSource, boolean isH5) throws HfApiException;

    /**
     * pc web 提现
     */
    WithdrawPCDataSource pcWithdraw(WithdrawPCDataSource dataSource) throws HfApiException;

    /**
     * app web 提现
     */
    WithdrawPCDataSource appWithdraw(WithdrawPCDataSource dataSource) throws HfApiException;

    /**
     * 重置密码
     * 支付|登陆
     */
    ResetPasswordDataSource resetPassword(ResetPasswordDataSource dataSource) throws HfApiException;

    /**
     * 个人开户
     *
     * @param dataSource
     * @return
     * @throws HfApiException
     */
    PersonalDataSource openAccountBySelfs(PersonalDataSource dataSource) throws HfApiException;

    /**
     * 法人开户
     *
     * @param dataSource
     * @return
     * @throws HfApiException
     */
    LegalPersonDataSource legalPersonOpenAccountBySelf(LegalPersonDataSource dataSource) throws HfApiException;

    /**
     * 个人充值
     *
     * @param dataSource
     * @return
     */
    PerRechargeDataSource personRecharge(PerRechargeDataSource dataSource) throws HfApiException;

    /**
     * 冻结到冻结
     *
     * @param dataSource
     * @return
     */
    TransferFreeze2FreezeResponse transferFreezeToFreezes(TransferFreeze2FreezeDataSource dataSource) throws HfApiException;

    /**
     * 解冻
     *
     * @param dataSource
     * @return
     */
    UnfreezeResponse unfreezes(FreezeDataSource dataSource) throws HfApiException;

    /**
     * 免登陆网银充值
     *
     * @param dataSource
     * @return
     */
    PerRechargeDataSource ebPersonRecharge(PerRechargeDataSource dataSource) throws HfApiException;


}
