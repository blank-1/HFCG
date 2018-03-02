package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ReflectionErrorCode;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import jodd.util.PrettyStringBuilder;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: yulei
 * Date: 13-7-18
 * Time: 下午5:59
 */
public class BeanUtil {

    /**
     * 根据map组装对象
     *
     * @param type
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T wrapBeanByMap(Class<T> type, Map map) {
        T t = null;

        try {
            t = type.newInstance();
        } catch (Exception e) {
            throw new SystemException(ReflectionErrorCode.CANNOT_INSTANCE).set("type", type.getName());
        }

        try {
            ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
            DateConverter dateConverter = new DateConverter();
            dateConverter.setPatterns(new String[]{
                    "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"
            });
            convertUtilsBean.register(dateConverter, Date.class);

            BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
            beanUtilsBean.populate(t, map);
        } catch (Exception e) {
            throw new SystemException(ReflectionErrorCode.CANNOT_POPULATE).set("type", type.getName())
                    .set("data", PrettyStringBuilder.str(map));
        }

        return t;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Map map = new HashMap();
        map.put("a", new String[]{"2013-10-12 12:31:12"});
        map.put("b", new BigDecimal[]{
                new BigDecimal("100")
        });

        Test test = BeanUtil.wrapBeanByMap(Test.class, map);

        System.out.println(PrettyStringBuilder.str(test.getB()));
    }

    public static Map<String, Object> bean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }

    public static class Test {
        private Date a;
        private BigDecimal b;

        public BigDecimal getB() {
            return b;
        }

        public void setB(BigDecimal b) {
            this.b = b;
        }

        public Date getA() {
            return a;
        }

        public void setA(Date a) {
            this.a = a;
        }
    }
}
