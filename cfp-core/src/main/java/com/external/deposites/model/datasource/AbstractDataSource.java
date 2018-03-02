package com.external.deposites.model.datasource;


import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;
import com.external.deposites.exception.UnqualifiedException;
import com.external.deposites.model.AbstractSignature;
import com.external.deposites.model.FieldValidationItem;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.util.ReflectionUtil;
import com.xt.cfp.core.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 参数源
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
public abstract class AbstractDataSource extends AbstractSignature implements Serializable {

    /*private Logger logger = LoggerFactory.getLogger(this.getClass());*/

    private Map<HfValidations.BasicValidation, List<FieldValidationItem>> fvMap;


    /**
     * 缓存 要校验的字段
     * <p>
     * xxx 有问题
     *
     * @param basicValidation
     */
    public List<FieldValidationItem> queryCondition(HfValidations.BasicValidation basicValidation) throws UnqualifiedException {
        if (fvMap != null) {
            return fvMap.get(basicValidation);
        }

        Field[] declaredFields = this.getClass().getDeclaredFields();

        fvMap = new HashMap<>(declaredFields.length);

        for (Field field : declaredFields) {
            Validated annotation = field.getAnnotation(Validated.class);
            if (annotation != null) {
                try {
                    field.setAccessible(true);
                    for (HfValidations.BasicValidation validation : annotation.type()) {
                        List<FieldValidationItem> items = fvMap.get(validation);
                        if (items == null) {
                            items = new ArrayList<>();
                            fvMap.put(validation, items);
                        }
                        items.add(new FieldValidationItem(field.getName(), annotation, field.get(this)));
                    }
                } catch (IllegalAccessException e) {
                    throw new UnqualifiedException(e.getMessage(), e);
                }
            }
        }
        return fvMap.get(basicValidation);
    }

    /**
     * 初始化同用参数
     */
    public void initCommonParameters() {
        ReflectionUtil.invokeSetterMethodIfExists(this, "back_notify_url",
                HfUtils.niceUrl((String) ReflectionUtil.getFieldValueIfExists(this, "back_notify_url"),
                        PropertiesUtils.property("hf-config", "cg.hf.callback.domain")));
        ReflectionUtil.invokeSetterMethodIfExists(this, "page_notify_url",
                HfUtils.niceUrl((String) ReflectionUtil.getFieldValueIfExists(this, "page_notify_url"),
                        PropertiesUtils.property("hf-config", "cg.hf.callback.domain")));

        ReflectionUtil.invokeSetterMethodIfExists(this, "ver", PropertiesUtils.property("hf-config", "cg.hf.ver"));
        ReflectionUtil.invokeSetterMethod(this, "mchnt_cd", PropertiesUtils.property("hf-config", "cg.hf.mchnt_cd"));
        if (StringUtils.isNull((String) ReflectionUtil.getFieldValueIfExists(this, "mchnt_txn_ssn"))) {
            ReflectionUtil.invokeSetterMethodIfExists(this, "mchnt_txn_ssn", HfUtils.getUniqueSerialNum());
        }

    }

    public void clearSundry() {
        fvMap = null;
    }
}
