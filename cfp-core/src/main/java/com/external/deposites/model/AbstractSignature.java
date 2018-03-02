package com.external.deposites.model;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.utils.SecurityUtils;
import com.xt.cfp.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <pre>
 * 数据 签名|验签
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/8
 */
public abstract class AbstractSignature {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @ApiParameter
    private String signature;// 签名信息
    /**
     * 不作签名的字段
     */
    private final ArrayList<String> ignoreSignFields = new ArrayList<>();
    private final List<String> commIgnoreSignFields = Arrays.asList("certif_tp", "signature");


    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * 数据验签
     *
     * @param isCallback 是否是回调的数据校验
     */
    public boolean verifySign(boolean isCallback) {
        return !isCallback || SecurityUtils.verifySign(this.regSignVal(), this.getSignature());
    }

    /**
     * 字典排序后的参数
     */
    public String regSignVal() {
        Map<String, Field> fieldMap = ReflectionUtil.allAccessibleFields(this.getClass());

        List<String> paramSet = new ArrayList<>(fieldMap.size());

        StringBuilder sb = new StringBuilder();
        try {
            Map<String, String> nameValue = filtrateParameters(fieldMap.values(), paramSet);
            Collections.sort(paramSet);
            for (String field : paramSet) {
                logger.info("sign parameter => {}:{}", field, nameValue.get(field));
                sb.append(nameValue.get(field)).append("|");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("参数明文：{}", sb.toString());
        return sb.toString();
    }

    /**
     * 取要进行sign的参数
     */
    private Map<String, String> filtrateParameters(Collection<Field> declaredFields, List<String> paramSet) throws IllegalAccessException {
        Map<String, String> nameValue = new HashMap<>(declaredFields.size());
        for (Field field : declaredFields) {
            ApiParameter apiParameter = field.getAnnotation(ApiParameter.class);
            if (apiParameter == null || apiParameter.ignoreSign()) {
                continue;

            }
            field.setAccessible(true);
            Object value = field.get(this);
            if (value == null) {
                value = "";
            }
            paramSet.add(field.getName());
            nameValue.put(field.getName(), value.toString());
        }

        if (!this.commIgnoreSignFields.isEmpty()) {
            for (String commIgnoreSignField : this.commIgnoreSignFields) {
                paramSet.remove(commIgnoreSignField);
                nameValue.remove(commIgnoreSignField);
            }
        }
        if (!this.ignoreSignFields.isEmpty()) {
            for (String ignoreSignField : this.ignoreSignFields) {
                paramSet.remove(ignoreSignField);
                nameValue.remove(ignoreSignField);
            }

        }
        return nameValue;
    }

    /**
     * 忽略不作签名的字段，如果这些字段用了@ApiParameter
     *
     * @param ignoreSignFields 字段
     * @see ApiParameter#ignoreSign()
     */
    public void addIgnoreSignFields(String... ignoreSignFields) {
        if (ignoreSignFields != null && ignoreSignFields.length > 0) {
            this.ignoreSignFields.addAll(Arrays.asList(ignoreSignFields));
            this.ignoreSignFields.trimToSize();
        }
    }

}
