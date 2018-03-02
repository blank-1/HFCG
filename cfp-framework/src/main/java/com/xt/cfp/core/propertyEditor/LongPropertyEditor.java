package com.xt.cfp.core.propertyEditor;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import org.apache.commons.lang.math.NumberUtils;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

/**
 * Created by yulei on 2015/7/4.
 */
public class LongPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String realValue = text.replace(",", "").replace(" ", "");

        //参数信息不合法
        if (!NumberUtils.isNumber(realValue))
            throw new SystemException(ValidationErrorCode.ERROR_PARAM_ILLEGAL).set("value", text);

        super.setValue(new Long(realValue));
    }
}
