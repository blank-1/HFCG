package com.xt.cfp.core.propertyEditor;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.velocity.runtime.parser.node.MathUtils;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

/**
 * Created by yulei on 2015/7/4.
 */
public class BigDecimalPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String realValue = text.replace(",", "").replace(" ", "");

        if(StringUtils.isNotEmpty(realValue)){
            if (!NumberUtils.isNumber(realValue))
                throw new SystemException(ValidationErrorCode.ERROR_NOT_A_NUMRIC).set("value", text);

            super.setValue(new BigDecimal(realValue));
        }
    }
}
