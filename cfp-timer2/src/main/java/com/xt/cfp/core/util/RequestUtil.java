package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.FormatErrorCode;
import com.xt.cfp.core.Exception.code.ext.HttpErrorCode;
import com.xt.cfp.core.Exception.code.ext.ReflectionErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import jodd.util.NameValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: yulei
 * Date: 13-7-10
 * Time: 下午17:34
 */
public class RequestUtil {

    private static final List<Class> SIMPLE_TYPES;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    static {
        SIMPLE_TYPES = new ArrayList<Class>();
        SIMPLE_TYPES.add(Integer.class);
        SIMPLE_TYPES.add(Long.class);
        SIMPLE_TYPES.add(Double.class);
        SIMPLE_TYPES.add(Float.class);
        SIMPLE_TYPES.add(Boolean.class);
        SIMPLE_TYPES.add(Short.class);
        SIMPLE_TYPES.add(String.class);
    }

    /**
     * 检查 request 中是否包含指定的参数，如果没有就抛出异常
     *
     * @param servletRequest
     * @param paraNames
     */
    public static void checkRequiredPara(HttpServletRequest servletRequest, String... paraNames) {
        for (String paraName : paraNames) {
            if (!servletRequest.getParameterMap().containsKey(paraName)
                    || servletRequest.getParameterMap().get(paraName) == null
                    || servletRequest.getParameterMap().get(paraName).equals("")) {
                throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("paraName", paraName);
            }
        }
    }

    /**
     * 从request中获取整数类型的数据
     *
     * @param servletRequest
     * @param paraName
     * @return
     */
    public static Long getLongPara(HttpServletRequest servletRequest, String paraName) {
        String tmp = servletRequest.getParameter(paraName);

        if (StringUtils.isNotEmpty(tmp))
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED).set("paraName", paraName);

        try {
            return Long.valueOf(tmp);
        } catch (NumberFormatException e) {
            throw new SystemException(FormatErrorCode.NOT_LONG).set("string", tmp);
        }
    }

    /**
     * 校验请求方式
     * @param request
     * @param requestMethod
     * @return
     */
    public static void validateRequestMethod(HttpServletRequest request,RequestMethod requestMethod){
        String method = request.getMethod();
        if(RequestMethod.GET.equals(requestMethod)&&!"GET".equals(method)){
            throw new SystemException(HttpErrorCode.CAN_HANDLE_POST);
        }
        if (!RequestMethod.GET.equals(requestMethod)&&"GET".equals(method)){
            throw new SystemException(HttpErrorCode.CAN_HANDLE_GET);
        }
    }

    /**
     * 校验请求Referer
     * @param request
     * @param preUrls
     * @return
     */
    public static void validateRequestReferer(HttpServletRequest request,String ... preUrls){
        String referer = request.getHeader("Referer");
        if (StringUtils.isEmpty(referer))
            throw new SystemException(HttpErrorCode.ILLEGAL_REQUEST);

        String referer1 = referer.substring(0,referer.indexOf("?")!=-1?referer.indexOf("?"):referer.length());

        boolean checked = false;
        for (String url :preUrls){
            if (referer1.indexOf(url)!=-1){
                checked = true;
                break;
            }
        }
        if (!checked)
            throw new SystemException(HttpErrorCode.ILLEGAL_REQUEST);
    }

    /**
     * 判断一个参数是否在 request 中存在，如果存在返回true，否则返回false
     *
     * @param servletRequest
     * @param paraName
     * @return
     */
    public static boolean isExist(HttpServletRequest servletRequest, String paraName) {
        String tmp = servletRequest.getParameter(paraName);

        if (StringUtils.isNotEmpty(tmp))
            return true;

        return false;
    }

    /**
     * 从request中获取指定名称指定类型的条件数据
     *
     * @param servletRequest
     * @param needReturnCollection
     * @param nameValues
     * @return
     */
    public static Map<String, Object> getConditionsFromRequest(HttpServletRequest servletRequest, boolean needReturnCollection, NameValue<String, Class>... nameValues) {
        Map<String, Object> re = new HashMap<String, Object>();

        //定义一些循环中需要用到的引用变量
        String[] paras = null;
        Class type = null;
        List list = null;
        Object reValue = null;

        for (NameValue<String, Class> nameValue : nameValues)
            if ((paras = servletRequest.getParameterValues(nameValue.getName())) != null && paras.length > 0) {
                if (needReturnCollection)
                    reValue = convertStringsToTypeList(paras, nameValue.getValue());
                else
                    reValue = convertStringToTypeObject(paras[0], nameValue.getValue());

                re.put(nameValue.getName(), reValue);
            }

        return re;
    }

    /**
     * 把一个字符串按照指定的 “简单类型”（比如Long、Integer等） 转换
     *
     * @param value
     * @param type
     * @param <T>
     * @return
     */
    private static <T> T convertStringToTypeObject(String value, Class<T> type) {
        T t = null;

        if (SIMPLE_TYPES.contains(type)) {
            try {
                Method method = type.getMethod("valueOf", String.class);
                t = (T) method.invoke(null, value);
            } catch (Exception e) {
                throw new SystemException(FormatErrorCode.CANNOT_FORMAT).set("target", value).set("toType", type.getName());
            }
        } else {
            throw new SystemException(FormatErrorCode.CANNOT_FORMAT).set("target", value).set("toType", type.getName());
        }

        return t;
    }

    /**
     * 把一些字符串数据转换成一些指定的 “简单类型”（比如Long、Integer等）的数据，以list的格式返回
     *
     * @param paras
     * @param type
     * @param <T>
     * @return
     */
    private static <T> List<T> convertStringsToTypeList(String[] paras, Class<T> type) {
        List<T> list = new ArrayList();

        if (SIMPLE_TYPES.contains(type)) {
            try {
                Method method = type.getMethod("valueOf", String.class);
                for (String para : paras)
                    if (para != null && !para.equals(""))
                        list.add((T) method.invoke(null, para));
            } catch (Exception e) {
                throw new SystemException(FormatErrorCode.CANNOT_FORMAT).set("target", paras).set("toType", type.getName());
            }
        } else {
            throw new SystemException(FormatErrorCode.CANNOT_FORMAT).set("target", paras).set("toType", type.getName());
        }

        return list;
    }

    /**
     * 根据request中的类似（**, **, **,）的字符串参数， 获取对应类型的 list
     *
     * @param servletRequest
     * @param paraName
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getTypeListFromSpecialStringPara(HttpServletRequest servletRequest, String paraName, Class<T> type) {
        String para = servletRequest.getParameter(paraName);
        String[] specialParas = para.split(",");
        try {
            return convertStringsToTypeList(specialParas, type);
        } catch (SystemException se) {
            throw new SystemException(FormatErrorCode.CANNOT_FORMAT).set("target", para).set("toType", type.getName());
        }
    }

    /**
     * 根据 request 中的数据组合成一个固定类型的对象，并返回
     *
     * @param req
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getObjectByRequestParas(HttpServletRequest req, Class<T> type) {
        T re = null;

        try {
            re = type.newInstance();
        } catch (Exception e) {
            throw new SystemException(ReflectionErrorCode.CANNOT_INSTANCE).set("type", type.getName());
        }

        String tmp = null;
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            tmp = req.getParameter(field.getName());
            if (tmp != null)
                if (!field.getType().isPrimitive() && !field.getType().isSynthetic()) {//只有不是基本类型和复合类型的情况下才设定值
                    try {
                        //处理简单类型
                        if (SIMPLE_TYPES.contains(field.getType())) {
                            if (!field.getType().equals(String.class)) //除了 string 以外的其他类型都需要调用valueOf处理
                                field.set(re, field.getType().getMethod("valueOf", String.class).invoke(null, tmp));
                            else
                                field.set(re, tmp);
                        }

                        //处理bigDecimal
                        if (BigDecimal.class.equals(field.getType())) {
                            field.set(re, new BigDecimal(tmp));
                        }

                        //处理Date
                        if (Date.class.equals(field.getType())) {
                            field.set(re, sdf.parse(tmp));
                        }
                    } catch (Exception e) {
                        throw new SystemException(ReflectionErrorCode.CANNOT_SETFIELD).set("fieldName", field.getName())
                                .set("fieldType", field.getType().getName()).set("value", tmp);
                    }
                }
        }

        return re;
    }

    /**
     * 把一个包含多个值的参数数据，转换成制定类型的列表
     *
     * @param servletRequest
     * @param paraName
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getTypeListFromPara(HttpServletRequest servletRequest, String paraName, Class<T> type) {
        String[] para = servletRequest.getParameterValues(paraName);

        return convertStringsToTypeList(para, type);
    }

}
