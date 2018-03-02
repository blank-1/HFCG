package com.external.deposites.service.fyInterFaceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.datasource.WithdrawalDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.service.internal.AbstractApiService;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;

@Service
public class FyWithdrawalService extends AbstractApiService{
	
	private Logger logger = LoggerFactory.getLogger(FyWithdrawalService.class);
	
	public WithdrawalDataSource withdrawal(AbstractDataSource dataSource) throws HfApiException{
		logger.debug("商户P2P免登陆提现：{}", dataSource.toString());
        try {
            String url = PropertiesUtils.property("hf-config", "cg.hf.api.withdrawal.url");
            basicValidation(dataSource);
            WithdrawalDataSource dataSource1 = HfUtils.http().url(url)
                    .request(dataSource).response(IResponse.ResponseType.XML).form();
            dataSource1.clearSundry();
            return dataSource1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
	}
}
