package com.external.deposites.service.internal;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.*;
import com.external.deposites.model.response.*;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <pre>
 * 资金账户 转变（转账、划拨、冻结、解冻等）
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/28
 */
@Service
public class TransferService extends AbstractApiService {

    /**
     * 转账到可用（商户为未转结）
     */
    public TransferAccountsResponse transferAccounts(TransferAccountsDataSource dataSource) throws HfApiException {
        logger.info("转账：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            String apiUrl = PropertiesUtils.property("hf-config", "cg.hf.api.transfer.user_and_user.url");

            String merchantAccountId = PropertiesUtils.property("hf-config", "cg.hf.account_id");
            if (Objects.equals(dataSource.getOut_cust_no(), merchantAccountId)
                    || Objects.equals(dataSource.getIn_cust_no(), merchantAccountId)) {
                apiUrl = PropertiesUtils.property("hf-config", "cg.hf.api.transfer.merchant_and_user.url");
            }

            return HfUtils.http()
                    .url(apiUrl)
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(TransferAccountsResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 转账到用户冻结中
     */
    public TransferAccountsResponse transferAccountsToFreeze(PreAuthorizationDataSource dataSource) throws HfApiException {
        logger.info("转账到冻结中 ：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            String apiUrl = PropertiesUtils.property("hf-config", "cg.hf.api.transfer.user_and_user_freeze.url");

            String merchantAccountId = PropertiesUtils.property("hf-config", "cg.hf.account_id");
            if (Objects.equals(dataSource.getOut_cust_no(), merchantAccountId)
                    || Objects.equals(dataSource.getIn_cust_no(), merchantAccountId)) {
                apiUrl = PropertiesUtils.property("hf-config", "cg.hf.api.transfer.merchant_and_user_freeze.url");
            }

            return HfUtils.http()
                    .url(apiUrl)
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(TransferAccountsResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 预授权
     */
    public PreAuthorizationResponse perAuth(AbstractDataSource dataSource) throws HfApiException {
        logger.info("预授权：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.pre_auth.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(PreAuthorizationResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 预授权撤消
     */
    public CancelPreAuthResponse cancelPreAuth(AbstractDataSource dataSource) throws HfApiException {
        logger.info("预授权撤消：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.cancel_pre_auth.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(CancelPreAuthResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }


    /**
     * 划拨：冻结到冻结
     */
    public TransferFreeze2FreezeResponse transferFreezeToFreeze(TransferFreeze2FreezeDataSource dataSource) throws HfApiException {
        logger.info("划拨 冻结到冻结 ：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.transfer.freeze_to_freeze.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(TransferFreeze2FreezeResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 冻结用户资产
     */
    public FreezeResponse freeze(FreezeDataSource dataSource) throws HfApiException {
        logger.info("冻结用户资产 ：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.freeze.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(FreezeResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 解冻
     */
    public UnfreezeResponse unfreeze(FreezeDataSource dataSource) throws HfApiException {
        logger.info("解冻用户资产 ：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.unfreeze.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(UnfreezeResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
}
