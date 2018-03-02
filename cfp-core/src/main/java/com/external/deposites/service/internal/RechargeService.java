package com.external.deposites.service.internal;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.datasource.Ebank2RechargeDataSource;
import com.external.deposites.model.datasource.RechargeDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 开户服务
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
@Service
public class RechargeService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * PC 端个人用户免登录快捷充值
     */
    public RechargeDataSource pcRecharge4personal(AbstractDataSource dataSource) throws HfApiException {
        logger.debug("PC 端个人用户免登录快捷充值：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //2 业务类型校验
//            HfValidations.businessValidation(PersonalBorrowAccountValidation.class).validate(true, dataSource);
            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.pc.personal.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .form();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * App 端个人用户免登录快捷充值
     */
    public RechargeDataSource appRecharge4personal(AbstractDataSource dataSource) throws HfApiException {
        logger.debug("APP 端个人用户免登录快捷充值：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //todo 2 业务类型校验
            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.app.personal.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .form();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * E-bank PC 网银充值
     */
    public RechargeDataSource ebankRecharge(AbstractDataSource dataSource) throws HfApiException {
        logger.debug("e-bank PC 免登录网银充值：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //todo 2 业务类型校验

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .form();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * E-bank PC 网银充值2
     */
    public Ebank2RechargeDataSource ebankRecharge2(AbstractDataSource dataSource) throws HfApiException {
        logger.debug("e-bank PC 免登录网银充值：{}", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //todo 2 业务类型校验

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.recharge.E-bank2.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .form();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
}