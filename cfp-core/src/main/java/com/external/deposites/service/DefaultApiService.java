package com.external.deposites.service;

import com.external.deposites.model.datasource.*;
import com.external.deposites.model.response.*;
import com.external.deposites.service.internal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.fydatasource.LegalPersonDataSource;
import com.external.deposites.model.fydatasource.PerRechargeDataSource;
import com.external.deposites.model.fydatasource.PersonalDataSource;
import com.external.deposites.service.fyInterFaceService.FyOpenAccountService;
import com.external.deposites.service.fyInterFaceService.FyQueryBalanceService;
import com.external.deposites.service.fyInterFaceService.FyRechargeService;
import com.external.deposites.service.fyInterFaceService.FyTransferService;

/**
 * <pre>
 * 存管接口的默认实现
 * 回调地址在调用处传入
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
@Service
public class DefaultApiService implements IhfApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OpenAccountService openAccountService;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private OtherService otherService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private QueryService queryService;
    @Autowired
    private FyOpenAccountService fyOpenAccountService;
    @Autowired
    private FyRechargeService fyRechargeService;
    @Autowired
    private FyTransferService fyTransferService;
    
    private FyQueryBalanceService fyQueryBalanceService;

    @Override
    public CommonOpenAccount4ApiResponse openAccount(AbstractOpenAccount4ApiDataSource dataSource) throws HfApiException {
        try {
            return openAccountService.openAccount(dataSource);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }


    @Override
    public PreAuthorizationResponse preAuth(PreAuthorizationDataSource dataSource) throws HfApiException {
        return transferService.perAuth(dataSource);
    }

    @Override
    public CancelPreAuthResponse cancelPreAuth(CancelPreAuthDataSource dataSource) throws HfApiException {
        return transferService.cancelPreAuth(dataSource);
    }

    /**
     * PC 端个人用户免登录快捷充值
     */
    @Override
    public RechargeDataSource pcQuickRecharge(RechargeDataSource rechargeDataSource) throws HfApiException {
        return rechargeService.pcRecharge4personal(rechargeDataSource);
    }

    /**
     * app 端个人用户免登录快捷充值
     */
    @Override
    public RechargeDataSource appRecharge4personal(RechargeDataSource rechargeDataSource) throws HfApiException {
        return rechargeService.appRecharge4personal(rechargeDataSource);
    }

    /**
     * e-bank 端用户免登录网银充值
     */
    @Override
    public RechargeDataSource ebankRecharge(RechargeDataSource rechargeDataSource) throws HfApiException {
        return rechargeService.ebankRecharge(rechargeDataSource);
    }

    /**
     * e-bank 端用户免登录网银充值
     */
    @Override
    public Ebank2RechargeDataSource ebankRecharge2(Ebank2RechargeDataSource rechargeDataSource) throws HfApiException {
        return rechargeService.ebankRecharge2(rechargeDataSource);
    }

    @Override
    public QueryBalanceResponse queryBalance(BalanceDataSource balanceDataSource) throws HfApiException {
        return queryService.queryBalance(balanceDataSource);
//    	return fyQueryBalanceService.queryBalance(balanceDataSource);
    }

    @Override
    public TransferAccountsResponse transferAccounts(TransferAccountsDataSource dataSource) throws HfApiException {
        return transferService.transferAccounts(dataSource);
    }

    @Override
    public TransferAccountsResponse transferAccountsToFreeze(PreAuthorizationDataSource dataSource) throws HfApiException {
        return transferService.transferAccountsToFreeze(dataSource);
    }

    @Override
    public TransferFreeze2FreezeResponse transferFreezeToFreeze(TransferFreeze2FreezeDataSource dataSource) throws HfApiException {
        return transferService.transferFreezeToFreeze(dataSource);
    }

    @Override
    public FreezeResponse freeze(FreezeDataSource dataSource) throws HfApiException {
        return transferService.freeze(dataSource);
    }

    @Override
    public UnfreezeResponse unfreeze(FreezeDataSource dataSource) throws HfApiException {
        return transferService.unfreeze(dataSource);
    }

    @Override
    public <T extends AbstractOpenAccount4PCDataSource> T openAccountBySelf(AbstractOpenAccount4PCDataSource dataSource, boolean isH5) throws HfApiException {
        return openAccountService.openAccountBySelf(dataSource, isH5);
    }

    @Override
    public WithdrawPCDataSource pcWithdraw(WithdrawPCDataSource dataSource) throws HfApiException {
        return withdrawService.withdraw(dataSource, false);
    }

    @Override
    public WithdrawPCDataSource appWithdraw(WithdrawPCDataSource dataSource) throws HfApiException {
        return withdrawService.withdraw(dataSource, true);
    }

    @Override
    public ResetPasswordDataSource resetPassword(ResetPasswordDataSource dataSource) throws HfApiException {
        return otherService.resetPassword(dataSource);
    }

    @Override
    public QueryRechargeWithdrawResponse queryRecharge(QueryRechargeWithdrawDataSource dataSource) throws HfApiException {
        dataSource.setBusi_tp(QueryRechargeWithdrawDataSource.BusiTpEnum.PW11.name());
        return queryService.queryRechargeOrWithdraw(dataSource);
    }

    @Override
    public QueryBusinessResponse queryBusiness(QueryBusinessDataSource dataSource) throws HfApiException {
        return queryService.queryBusiness(dataSource);
    }

    @Override
    public QueryRechargeWithdrawResponse queryWithdraw(QueryRechargeWithdrawDataSource dataSource) throws HfApiException {
        dataSource.setBusi_tp(QueryRechargeWithdrawDataSource.BusiTpEnum.PWTX.name());
        return queryService.queryRechargeOrWithdraw(dataSource);
    }

    @Override
    public PersonalDataSource openAccountBySelfs(PersonalDataSource dataSource) throws HfApiException {
        return fyOpenAccountService.openAccountBySelf(dataSource);
    }


    @Override
    public LegalPersonDataSource legalPersonOpenAccountBySelf(LegalPersonDataSource dataSource) throws HfApiException {
        return fyOpenAccountService.legalPersonOpenAccountBySelf(dataSource);
    }


    @Override
    public PerRechargeDataSource personRecharge(PerRechargeDataSource dataSource) throws HfApiException {
        return fyRechargeService.personerRecharge(dataSource);
    }


    @Override
    public TransferFreeze2FreezeResponse transferFreezeToFreezes(TransferFreeze2FreezeDataSource dataSource) throws HfApiException {
        return fyTransferService.transferFreezeToFreeze(dataSource);
    }


    @Override
    public UnfreezeResponse unfreezes(FreezeDataSource dataSource) throws HfApiException {
        return fyTransferService.unfreeze(dataSource);
    }


    @Override
    public PerRechargeDataSource ebPersonRecharge(PerRechargeDataSource dataSource) throws HfApiException {
        return fyRechargeService.ebankRecharge(dataSource);
    }
}