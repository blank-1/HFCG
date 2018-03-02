package com.external.deposites.service.fyInterFaceService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.datasource.FreezeDataSource;
import com.external.deposites.model.datasource.TransferAccountsDataSource;
import com.external.deposites.model.fydatasource.TransactionQueryDataSource;
import com.external.deposites.model.fydatasource.TransactionQueryResponse;
import com.external.deposites.model.fydatasource.TransactionQueryResponseIteam;
import com.external.deposites.model.response.FreezeResponse;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.model.response.TransferAccountsResponse;
import com.external.deposites.model.response.UnfreezeResponse;
import com.external.deposites.service.internal.AbstractApiService;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;

/**
 * 
 * @author liuwei
 * 支付接口：转账、划拨、冻结
 *
 */
@Service
public class FyPayService extends AbstractApiService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//异常重试URL
	private String queryUrl = PropertiesUtils.property("hf-config", "cg.hf.api.query.queryTxn.url");

    /**
     * 冻结
     * @param trade 
     */
    public FreezeResponse freeze(FreezeDataSource freezeDataSource) throws HfApiException {
        logger.debug("支付接口冻结：{}", freezeDataSource.toString());
        
    	String url = PropertiesUtils.property("hf-config", "cg.hf.api.freeze.url");
    	
    	FreezeResponse freezeResponse = null;
        try {
            freezeResponse = HfUtils.http()
            		.url(url)
            		.request(freezeDataSource)
            		.response(IResponse.ResponseType.XML)
            		.merge(FreezeResponse.class);
            if(freezeResponse!=null&&freezeResponse.isSuccess()){//接口返回状态成功
              return freezeResponse;
            }else {
            	//失败查询状态重试
        		TransactionQueryDataSource transactionQueryDataSource = initParams(freezeDataSource);
        		FreezeResponse queryFreezeResponse = queryPayStatus(transactionQueryDataSource);
        		 if(queryFreezeResponse!=null){
        			 if(queryFreezeResponse.isSuccess()) {//接口返回状态成功
        				 return queryFreezeResponse;
        			 }
        		 }
           }
             return freezeResponse;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    //查询交易状态
    public FreezeResponse queryPayStatus(TransactionQueryDataSource transactionQueryDataSource)
			throws UnqualifiedException {
		FreezeResponse freezeResponse = null;
		basicValidation(transactionQueryDataSource);
		TransactionQueryResponse transactionQueryResponse = HfUtils.http()
				.url(queryUrl)
				.request(transactionQueryDataSource)
				.response(IResponse.ResponseType.XML)
				.arrayFields("results")
				.merge(TransactionQueryResponse.class);
		freezeResponse = new FreezeResponse();
		freezeResponse.setMchnt_cd(transactionQueryResponse.getMchnt_cd());
		freezeResponse.setMchnt_txn_ssn(transactionQueryResponse.getMchnt_txn_ssn());
		if(transactionQueryResponse!=null&&transactionQueryResponse.isSuccess()) {
			if(transactionQueryResponse.getResults().iterator().hasNext()) {
				//转换返回值
				TransactionQueryResponseIteam transactionQueryResponseIteam = transactionQueryResponse.getResults().iterator().next();
				freezeResponse.setResp_code(transactionQueryResponseIteam.getTxn_rsp_cd());
				freezeResponse.setResp_desc(transactionQueryResponseIteam.getRsp_cd_desc());
				return freezeResponse;
			}
		}
		return freezeResponse;
	}
    
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


	private TransactionQueryDataSource initParams(FreezeDataSource freezeDataSource) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
	    String timeString = dataFormat.format(new Date());
		TransactionQueryDataSource transactionQueryDataSource = new TransactionQueryDataSource();
		transactionQueryDataSource.setMchnt_cd(freezeDataSource.getMchnt_cd());
		transactionQueryDataSource.setCust_no(freezeDataSource.getCust_no());
		transactionQueryDataSource.setTxn_ssn(freezeDataSource.getMchnt_txn_ssn());
		transactionQueryDataSource.setBusi_tp("PWDJ");
		transactionQueryDataSource.setStart_day(timeString);
		transactionQueryDataSource.setEnd_day(timeString);
		return transactionQueryDataSource;
	}

	/**
	 * 解冻接口
	 * @param freezeDataSource
	 * @return
	 * @throws HfApiException
	 */
	public UnfreezeResponse unfreeze(FreezeDataSource freezeDataSource) throws HfApiException {
        logger.info("解冻用户资产 ：{}", freezeDataSource.toString());
        try {
        	UnfreezeResponse unfreezeResponse =  HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.unfreeze.url"))
                    .request(freezeDataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(UnfreezeResponse.class);
            if(unfreezeResponse!=null&&unfreezeResponse.isSuccess()){//接口返回状态成功
                return unfreezeResponse;
              }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
		return null;
	}
}
