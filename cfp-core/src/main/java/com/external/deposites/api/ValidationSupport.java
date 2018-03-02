package com.external.deposites.api;

import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.xt.cfp.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 通用校验器，基本的、业务的校验器都要去继承这个，当然也可以直接实现IValidation。
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/17
 */
public abstract class ValidationSupport implements IValidation {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void validate(boolean fastFail, final AbstractDataSource dataSource) throws UnqualifiedException {

        List<ValidationResult> validationResults = new ArrayList<ValidationResult>() {
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                for (ValidationResult validationResult : this) {
                    if (StringUtils.isNull(validationResult.getMessage())) {
                        sb.append("field :")
                                .append(validationResult.getFieldName())
                                .append(" ==> ")
                                .append(validationResult.getPattern().pattern())
                                .append("|");
                    } else {
                        sb.append(validationResult.getMessage()).append("|");
                    }
                }
                return sb.toString();
            }
        };

        //XXX 存在空转
        for (IConditionType condition : loadConditions()) {
            ValidationResult validate = condition.validate(dataSource);

            if (!validate.isSuccess()) {
                validationResults.add(validate);
                logger.info("条件验证不通过：{}", StringUtils.isNull(validate.getMessage()) ? validate.toString() : validate.getMessage());
                if (fastFail) {
                    break;
                }
            }
        }

        if (!validationResults.isEmpty()) {
            throw new UnqualifiedException("条件验证不通过：" + validationResults.toString());
        }

    }
}
