package com.external.deposites.service.fyInterFaceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.datasource.RechargeDataSource;
import com.external.deposites.model.fydatasource.PerRechargeDataSource;
import com.external.deposites.model.response.IResponse;
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
public class FyRechargeService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 页面 个人用户充值
     */
    public PerRechargeDataSource personerRecharge(AbstractDataSource dataSource) throws HfApiException {
        logger.debug("个人用户充值：{}", dataSource.toString());
        try {
        	 //1 基本校验
            basicValidation(dataSource);
            String url = PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.url");
            PerRechargeDataSource dataSource1 = HfUtils.http().url(url)
                    .request(dataSource).response(IResponse.ResponseType.XML).form();
            dataSource1.clearSundry();
            return dataSource1;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
    
    /**
     * 页面 网银免登陆充值
     */
    public PerRechargeDataSource ebankRecharge(AbstractDataSource dataSource) throws HfApiException {
        logger.debug("网银免登陆充值：{}", dataSource.toString());
        try {
        	 //1 基本校验
            basicValidation(dataSource);
            String url = PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank.url");
            PerRechargeDataSource dataSource1 = HfUtils.http().url(url)
                    .request(dataSource).response(IResponse.ResponseType.XML).form();
            dataSource1.clearSundry();
            return dataSource1;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

}