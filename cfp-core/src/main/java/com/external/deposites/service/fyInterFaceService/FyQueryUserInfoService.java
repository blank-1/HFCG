package com.external.deposites.service.fyInterFaceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.fyResponse.FyDetailResponse;
import com.external.deposites.model.fyResponse.FyRechargeAndWithdrawResponse;
import com.external.deposites.model.fyResponse.FyTradeResponse;
import com.external.deposites.model.fyResponse.FyUserInfoResponse;
import com.external.deposites.model.fydatasource.FyDetailDataSource;
import com.external.deposites.model.fydatasource.FyRechargeAndWithdrawDataSource;
import com.external.deposites.model.fydatasource.FyUserInfoDataSource;
import com.external.deposites.model.fydatasource.TransactionQueryDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.model.response.QueryBalanceResponse;
import com.external.deposites.service.internal.AbstractApiService;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;

/**
 * <pre>
 * 充值服务
 * </pre>
 *
 * @author zuowansheng
 */
@Service
public class FyQueryUserInfoService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 用户信息查询
     */
    public FyUserInfoResponse queryUserInfo(FyUserInfoDataSource dataSource) throws HfApiException {
        logger.info("用户信息查询：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验

            String apiUrl = PropertiesUtils.property("hf-config", "cg.hf.api.queryUserInfo.url");
            return HfUtils.http()
                    .url(apiUrl)
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML).arrayFields("results")
                    .merge(FyUserInfoResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
    
    //查询交易状态
    public FyTradeResponse queryPayStatus(TransactionQueryDataSource transactionQueryDataSource)throws HfApiException {
    	 try {
             //1 基本校验
             basicValidation(transactionQueryDataSource);
             //xxx 业务类型校验
             //transactionQueryDataSource.setMchnt_cd("jzcf001");
             String apiUrl = PropertiesUtils.property("hf-config", "cg.hf.api.query.queryTxn.url");
             //String apiUrl ="http://cg.zjcgxt.com/jzh/queryTxn.action";
             return HfUtils.http()
                     .url(apiUrl)
                     .request(transactionQueryDataSource)
                     .response(IResponse.ResponseType.XML).arrayFields("results")
                     .merge(FyTradeResponse.class);
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
             throw new HfApiException(e.getMessage(), e);
         }
    	
	}
    
    public FyDetailResponse queryDetail(FyDetailDataSource dataSource)throws HfApiException {
   	 try {
            //1 基本校验
            basicValidation(dataSource);
            //xxx 业务类型校验
            String apiUrl = PropertiesUtils.property("hf-config", "cg.hf.api.queryDetail.url");
            return HfUtils.http()
                    .url(apiUrl)
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML).arrayFields("results")
                    .merge(FyDetailResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
   	
	}
    
    
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

	public FyRechargeAndWithdrawResponse queryRechargeAndWithdraw(FyRechargeAndWithdrawDataSource dataSource) throws HfApiException {
		 try {
	            basicValidation(dataSource);

	            return HfUtils.http()
	                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.rechargeAndWithdrawal.url"))
	                    .request(dataSource)
	                    .response(IResponse.ResponseType.XML)
	                    .arrayFields("results")
	                    .merge(FyRechargeAndWithdrawResponse.class);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            throw new HfApiException(e.getMessage(), e);
	        }
	}
}