package com.external.deposites.service.internal;

import com.external.deposites.api.HfValidations;
import com.external.deposites.api.IValidation;
import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.datasource.AbstractDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 所有和接口有关的服务要继承的类
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/24
 */
public abstract class AbstractApiService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 基本通用条件校验
     */
    protected void basicValidation(AbstractDataSource dataSource) throws UnqualifiedException {
        // xxx 这么写相当差劲
        dataSource.initCommonParameters();

        IValidation basicValidation = HfValidations.basicValidation();
        try {
            basicValidation.validate(true, dataSource);
        } catch (UnqualifiedException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
