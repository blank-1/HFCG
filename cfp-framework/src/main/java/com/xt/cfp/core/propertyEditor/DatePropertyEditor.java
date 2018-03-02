package com.xt.cfp.core.propertyEditor;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.FormatErrorCode;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.beans.PropertyEditorSupport;

public class DatePropertyEditor extends PropertyEditorSupport {
    private final static String FORMAT = "yyyy-MM-dd";
    private final static String FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss";
    private final static String FORMAT_DETAIL1 = "yyyy-MM-dd HH:mm";

    private final static DateTimeFormatter FORMATTER_CHAIN_1 = DateTimeFormat.forPattern(FORMAT);
    private final static DateTimeFormatter FORMATTER_CHAIN_2 = DateTimeFormat.forPattern(FORMAT_DETAIL);
    private final static DateTimeFormatter FORMATTER_CHAIN_3 = DateTimeFormat.forPattern(FORMAT_DETAIL1);

    public DatePropertyEditor() {}

    @Override
    public void setAsText(String text) {
        if (StringUtils.isEmpty(text))
            return;

        boolean formated = false;
        Exception exception = null;

        if (!formated)
            try {
                super.setValue(FORMATTER_CHAIN_1.parseDateTime(text).toDate());
                formated = true;
            } catch (IllegalArgumentException e) {
                exception = e;
            }

        if (!formated)
            try {
                super.setValue(FORMATTER_CHAIN_2.parseDateTime(text).toDate());
                formated = true;
            } catch (IllegalArgumentException e) {
                exception = e;
            }

        if (!formated)
            try {
                super.setValue(FORMATTER_CHAIN_3.parseDateTime(text).toDate());
                formated = true;
            } catch (IllegalArgumentException e) {
                exception = e;
            }

        if (!formated && exception != null)
            throw SystemException.wrap(exception, FormatErrorCode.CANNOT_FORMAT)
                    .set("formatType", "DATE").set("srcText", text).set("formatPattern", new String[]
                            {
                                    FORMAT, FORMAT_DETAIL

                            });
    }
}
