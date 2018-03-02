package com.external.deposites.service.fyInterFaceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.response.QueryBalanceResponse;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.service.internal.AbstractApiService;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;

/**
 * 余额查询接口
 * @author HuYongkui
 *
 */
@Service
public class FyQueryBalanceService extends AbstractApiService{
	private Logger logger = LoggerFactory.getLogger(FyQueryBalanceService.class);
	
	public QueryBalanceResponse queryBalance(AbstractDataSource dataSource) throws HfApiException {
        logger.info("余额查询", dataSource);
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
}
