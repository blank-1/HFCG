package com.external.deposites.service.internal;

import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.datasource.Ebank2RechargeDataSource;
import com.external.deposites.model.datasource.RechargeDataSource;
import com.external.deposites.model.datasource.WithdrawPCDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 提现服务
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
@Service
public class WithdrawService extends AbstractApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public WithdrawPCDataSource withdraw(WithdrawPCDataSource dataSource, boolean isH5) throws HfApiException {
        logger.debug("{}提现：{}", isH5 ? "H5" : "PC", dataSource.toString());
        try {
            //1 基本校验
            basicValidation(dataSource);
            //2 xxx 业务类型校验

            String url = isH5
                    ? PropertiesUtils.property("hf-config", "cg.hf.api.withdraw.app.url")
                    : PropertiesUtils.property("hf-config", "cg.hf.api.withdraw.pc.url");
            return HfUtils.http()
                    .url(url)
                    .request(dataSource)
                    .response(IResponse.ResponseType.XML)
                    .form();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HfApiException(e.getMessage(), e);
        }
    }
}