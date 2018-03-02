package com.external.deposites.service.internal;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.datasource.QueryBusinessDataSource;
import com.external.deposites.model.datasource.QueryRechargeWithdrawDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.model.response.QueryBalanceResponse;
import com.external.deposites.model.response.QueryBusinessResponse;
import com.external.deposites.model.response.QueryRechargeWithdrawResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 查询服务
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/27
 */
@Service
public class QueryService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public QueryBalanceResponse queryBalance(AbstractDataSource dataSource) throws HfApiException {
        logger.info("查询余额：{}", dataSource);
        try {
            basicValidation(dataSource);

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.query.balance.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .arrayFields("results")
                    .merge(QueryBalanceResponse.class);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 查询充值提现信息
     */
    public QueryRechargeWithdrawResponse queryRechargeOrWithdraw(QueryRechargeWithdrawDataSource dataSource) throws HfApiException {
        logger.info("查询充值信息：{}", dataSource);
        try {
            basicValidation(dataSource);

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.api.query.recharge_and_withdraw.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .arrayFields("results")
                    .merge(QueryRechargeWithdrawResponse.class);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    public QueryBusinessResponse queryBusiness(QueryBusinessDataSource dataSource) throws HfApiException {
        logger.info("查询充值信息：{}", dataSource);
        try {
            basicValidation(dataSource);

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.query.queryTxn.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .arrayFields("results")
                    .merge(QueryBusinessResponse.class);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
}
