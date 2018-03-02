package com.external.deposites.api;

import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.FieldValidationItem;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.xt.cfp.core.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 校验器
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
public class HfValidations {

    private static HfValidations validations;

    private HfValidations() {
    }

    public static HfValidations instance() {
        if (validations == null) {
            synchronized (HfValidations.class) {
                if (validations == null) {
                    validations = new HfValidations();
                }
            }
        }
        return validations;
    }

    /**
     * 获取基本条件校验器
     */
    public static IValidation basicValidation() {
        return instance().new BasicValidationImpl();
    }

    /**
     * 获取业务条件校验器
     */
    public static IValidation businessValidation(Class<? extends IValidation> validationClass) {
        return ApplicationContextUtil.getBean(validationClass);
    }

    /**
     * 基本校验器的实现
     */
    private class BasicValidationImpl extends ValidationSupport {
        @Override
        public List<? extends IConditionType> loadConditions() {
            return Arrays.asList(BasicValidation.values());
        }
    }

    /**
     * 基本校验器
     */
    public enum BasicValidation implements IConditionType {
        NotNull(0, Pattern.compile(".+")),
//        Phone(1, Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$")),
        Phone(1, Pattern.compile("^\\d{11}$")),
        Email(2, Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+")),;

        private final int code;
        private final Pattern regexp;

        BasicValidation(int code, Pattern regexp) {
            this.code = code;
            this.regexp = regexp;
        }

        public int getCode() {
            return code;
        }

        public Pattern getRegexp() {
            return regexp;
        }

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public ValidationResult validate(AbstractDataSource bean) {
            ValidationResult validationResult = new ValidationResult(this);
            try {
                List<FieldValidationItem> fieldValidationItems = bean.queryCondition(this);
                if (fieldValidationItems == null || fieldValidationItems.isEmpty()) {
                    return validationResult.setSuccess(true);
                }
                for (FieldValidationItem fieldValidationItem : fieldValidationItems) {
                    Pattern regexp = this.getRegexp();
                    Matcher matcher = regexp.matcher(fieldValidationItem.getValue() == null ? "" : fieldValidationItem.getValue().toString());
                    if (!matcher.matches()) {
                        return validationResult.setSuccess(false)
                                .setFieldName(fieldValidationItem.getFieldName())
                                .setPattern(regexp)
                                .setMessage(fieldValidationItem.getValidated().message());
                    }
                }
                return validationResult.setSuccess(true);
            } catch (UnqualifiedException e) {
                validationResult.setMessage(e.getMessage()).setSuccess(false);
            }
            return validationResult;
        }
    }
}
