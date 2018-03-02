package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import jodd.util.NameValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luqinglin on 2015/6/17.
 */
public class ValidationUtil {

    /**
     * 检查参数是否null，如果为null，直接抛出异常
     *
     * @param nameValues
     */
    public static void checkRequiredPara(NameValue<String, Object>... nameValues) {
        for (NameValue<String, Object> nameValue : nameValues) {
            if (nameValue.getValue() == null)
                throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("paramName", nameValue.getName());
        }
    }

    /**
     * 检查may中是否包含必要的条目，如果为null，直接抛出异常
     *
     * @param map
     * @param keys
     */
    public static void checkRequiredEntry(Map map, String... keys) {
        for (String key : keys) {
            if (map.get(key) == null)
                throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_ENTRY).set("key", key);
        }
    }

    /**
     * 检查map中是否存在空值的key，如果发现就抛出异常
     * @param map
     */
    public static void checkNotExistNullValue(Map map) {
        List<String> nullValueList = new ArrayList<String>();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            if (value == null || value.equals("0"))
                nullValueList.add(key.toString());
        }

        if (nullValueList.size() > 0)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_ENTRY).set("keys", nullValueList.toArray());
    }

    /**
     * 检查必要的属性是否为null，如果为null，直接抛出异常
     *
     * @param obj
     * @param fieldNames
     */
    public static void checkRequiredField(Object obj, String... fieldNames) {
        for (String fieldName : fieldNames) {
            if (ReflectionUtil.invokeGetterMethod(obj, fieldName) == null)
                throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("fieldName", fieldName);
        }
    }

    /**
     * 检查必要的属性是否为null或“”，如果是，抛出异常
     * @param obj
     * @param fieldNames
     */
    public static void checkRequiredFieldNotEmpty(Object obj, String... fieldNames) {
        for (String fieldName : fieldNames) {
            Object o = ReflectionUtil.invokeGetterMethod(obj, fieldName);
            if (o == null || o.equals(""))
                throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_NOT_EMPTY_FIELD).set("fieldName", fieldName);
        }
    }

    /**
     * 检查必要的属性是否为null，如果为null，直接抛出异常
     *
     * @param obj
     * @param name
     * @param message
     */
    public static void checkNotNull(Object obj, String name, String message) {
        if (obj == null) {
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set(name, message);
        }
    }

    /**
     * 检查某对象是否null，如果是，抛出异常
     * @param obj
     * @param message
     * @param pairs
     */
    public static void checkNotNull(Object obj, String message, Pair<String, String>... pairs) {
        if (obj == null) {
            SystemException e = new SystemException(ValidationErrorCode.ERROR_NULL).set("customMsg", message);
            for (Pair<String, String> pair : pairs) {
                e.set(pair.getK(), pair.getV());
            }
            throw e;
        }
    }

    /**
     * 检查一个字符串对象是否为空字符串，如果是，抛出异常
     * @param obj
     * @param ObjName
     */
    public static void checkStrObjNotEmpty(String obj, String ObjName) {
        if (obj == null || obj.equals("")) {
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("stringObj", ObjName);
        }
    }
    /**
     * 检验两个对象值是否相等
     * @param o1
     * @param o2
     */
    public static void checkTwoValueIsEq(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            throw new SystemException(ValidationErrorCode.ERROR_NOT_SAME).set("o1", o1).set("o2", o2);
        }
    }
}
