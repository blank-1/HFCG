package com.external.deposites.service.fyInterFaceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.external.deposites.api.HfValidations;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.fydatasource.LegalPersonDataSource;
import com.external.deposites.model.fydatasource.PersonalDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.service.internal.AbstractApiService;
import com.external.deposites.service.validation.EnterpriseBorrowAccountValidation;
import com.external.deposites.service.validation.PersonalBorrowAccountValidation;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;

/**
 * <pre>
 * 开户服务
 * </pre>
 *
 * @author zuowansheng
 */
@Service
public class FyOpenAccountService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 页面 个人开户
     */
    public PersonalDataSource openAccountBySelf(AbstractDataSource dataSource) throws HfApiException {
        logger.debug("页面个人开户：{}", dataSource.toString());
        try {
            String url = PropertiesUtils.property("hf-config", "cg.hf.api.open_account.person.self.url");
            personalValidate(dataSource);
            PersonalDataSource dataSource1 = HfUtils.http().url(url)
                    .request(dataSource).response(IResponse.ResponseType.XML).form();
            dataSource1.clearSundry();
            return dataSource1;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
    
    /**
     * 法人开户
     * @param dataSource
     * @param isH5
     * @return
     * @throws HfApiException
     */
    public LegalPersonDataSource legalPersonOpenAccountBySelf(LegalPersonDataSource dataSource) throws HfApiException{
    	logger.debug("页面法人开户：{}", dataSource.toString());
        try {
            enterpriseValidate(dataSource);
            String url=  PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.self.url");
            LegalPersonDataSource dataSource1 = HfUtils.http().url(url)
                    .request(dataSource).response(IResponse.ResponseType.XML).form();
            dataSource1.clearSundry();
            return dataSource1;

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