package com.external.deposites.api;

import java.util.regex.Pattern;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/23
 */
public class ValidationResult {
    private final HfValidations.BasicValidation basicValidation;
    private boolean success;
    private String message;
    private String fieldName;
    private Pattern pattern;

    public ValidationResult(HfValidations.BasicValidation basicValidation) {
        this.basicValidation = basicValidation;
    }

    public boolean isSuccess() {
        return success;
    }

    public ValidationResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public HfValidations.BasicValidation getBasicValidation() {
        return basicValidation;
    }

    public String getMessage() {
        return message;
    }

    public ValidationResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public ValidationResult setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public ValidationResult setPattern(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "basicValidation=" + basicValidation +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", pattern=" + pattern +
                '}';
    }
}
