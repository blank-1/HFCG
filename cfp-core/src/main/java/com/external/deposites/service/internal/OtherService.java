package com.external.deposites.service.internal;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.ResetPasswordDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/11
 */
@Service
public class OtherService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ResetPasswordDataSource resetPassword(ResetPasswordDataSource dataSource) throws HfApiException {
        logger.info("密码重置：{}", dataSource);
        try {
            ReflectionUtil.invokeSetterMethodIfExists(dataSource, "back_url",
                    HfUtils.niceUrl((String) ReflectionUtil.getFieldValueIfExists(dataSource, "back_url"),
                            PropertiesUtils.property("hf-config", "cg.hf.callback.domain")));

            basicValidation(dataSource);

            return HfUtils.http()
                    .url(PropertiesUtils.property("hf-config", "cg.hf.api.change.password.app.url"))
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .ignoreSignFields("back_url")
                    .form();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
}
