package com.external.deposites.service.internal;

import com.external.deposites.api.HfValidations;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.datasource.*;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.model.response.CommonOpenAccount4ApiResponse;
import com.external.deposites.service.validation.EnterpriseBorrowAccountValidation;
import com.external.deposites.service.validation.PersonalBorrowAccountValidation;
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
public class OpenAccountService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * api 开户
     */
    public CommonOpenAccount4ApiResponse openAccount(AbstractOpenAccount4ApiDataSource dataSource) throws HfApiException {
        logger.debug("api开户：{}", dataSource.toString());

        try {
            if (dataSource instanceof OpenAccount4ApiPersonalDataSource) {
                personalValidate(dataSource);
            } else if (dataSource instanceof OpenAccount4ApiEnterpriseDataSource) {
                enterpriseValidate(dataSource);
            } else {
                throw new IllegalArgumentException("传入的开户参数源不对");
            }

            return HfUtils.http()
                    .url(dataSource instanceof OpenAccount4ApiPersonalDataSource
                            ? PropertiesUtils.property("hf-config", "cg.hf.api.open_account.person.url")
                            : PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.url")
                    ).request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .merge(CommonOpenAccount4ApiResponse.class);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 页面 开户
     */
    public <T extends AbstractOpenAccount4PCDataSource> T openAccountBySelf(AbstractOpenAccount4PCDataSource dataSource, boolean isH5) throws HfApiException {
        logger.debug("页面开户：{}", dataSource.toString());
        try {
            if (dataSource instanceof OpenAccount4PCPersonalDataSource) {
                personalValidate(dataSource);
            }else if (dataSource instanceof OpenAccount4PCEnterpriseDataSource) {
                enterpriseValidate(dataSource);
            } else {
                throw new IllegalArgumentException("传入的开户参数源不对");
            }
            String url = PropertiesUtils.property("hf-config", "cg.hf.api.open_account.h5.person.self.url");
            if (!isH5) {
                url = dataSource instanceof OpenAccount4PCPersonalDataSource
                        ? PropertiesUtils.property("hf-config", "cg.hf.api.open_account.person.self.url")
                        : PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.self.url");
            }
            dataSource = HfUtils.http().url(url)
                    .request(dataSource).response(IResponse.ResponseType.XML).form();
            dataSource.clearSundry();
            return (T) dataSource;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }

    /**
     * 个人开户参数等校验
     */
    private void personalValidate(AbstractDataSource dataSource) throws UnqualifiedException {
        //1 基本校验
        basicValidation(dataSource);

        //2 业务类型校验
        HfValidations.businessValidation(PersonalBorrowAccountValidation.class).validate(true, dataSource);
    }

    /**
     * 企业开户参数等校验
     */
    private void enterpriseValidate(AbstractDataSource dataSource) throws UnqualifiedException {
        //1 基本校验
        basicValidation(dataSource);

        //2 业务类型校验
        HfValidations.businessValidation(EnterpriseBorrowAccountValidation.class).validate(true, dataSource);
    }


}