package com.external.deposites.model;

import com.external.deposites.api.Validated;

/**
 * <pre>
 * 接口参数项
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/30
 */
public class FieldValidationItem {
    private String fieldName;
    private Validated validated;
    private Object value;

    public FieldValidationItem(String fieldName,Validated validated, Object value) {
        this.fieldName=fieldName;
        this.validated = validated;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Validated getValidated() {
        return validated;
    }

    public void setValidated(Validated validated) {
        this.validated = validated;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
