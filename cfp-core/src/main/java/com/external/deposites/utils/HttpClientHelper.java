package com.external.deposites.utils;

import com.external.deposites.api.ApiParameter;
import com.xt.cfp.core.util.ReflectionUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.Reflection;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);

    public static String doHttp(String urlStr, String charSet, Object parameters, int timeOut) throws Exception {
        String responseString = "";
        PostMethod xmlpost;
        int statusCode = 0;
        HttpClient httpClient = new HttpClient();
        xmlpost = new PostMethod(urlStr);
        HttpConnectionManagerParams httpParams = httpClient.getHttpConnectionManager().getParams();
        httpParams.setConnectionTimeout(timeOut);
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);

        try {
            //组合请求参数
            List<NameValuePair> nameValuePairs = getNameValuePairs2(parameters);

            NameValuePair[] nvps = new NameValuePair[nameValuePairs.size()];
            xmlpost.setRequestBody(nameValuePairs.toArray(nvps));
            statusCode = httpClient.executeMethod(xmlpost);
            responseString = xmlpost.getResponseBodyAsString();
            if (statusCode < HttpURLConnection.HTTP_OK || statusCode >= HttpURLConnection.HTTP_MULT_CHOICE) {
                logger.error("失败返回码[" + statusCode + "]", new Exception());
                throw new Exception("请求接口失败，失败码[ " + statusCode + " ]");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return responseString;
    }

    private static List<NameValuePair> getNameValuePairs(Object parameters) throws IllegalAccessException, InvocationTargetException {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Method[] ms = parameters.getClass().getMethods();
        for (int i = 0; i < ms.length; i++) {
            Method m = ms[i];
            String name = m.getName();
            if (name.startsWith("get")) {
                String param = name.substring(3, name.length());
                param = param.substring(0, 1).toLowerCase() + param.substring(1, param.length());
                if (param.equals("class")) {
                    continue;
                }
                Object value = "";
                try {
                    value = m.invoke(parameters, null);
                    NameValuePair nvp = new NameValuePair(param, value.toString());
                    nameValuePairs.add(nvp);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return nameValuePairs;
    }

    private static List<NameValuePair> getNameValuePairs2(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Map<String, Field> fieldMap = ReflectionUtil.allAccessibleFields(obj.getClass());
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            ApiParameter apiParameter = field.getAnnotation(ApiParameter.class);
            if (apiParameter == null) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                continue;
            }
            NameValuePair nameValuePair = new NameValuePair(field.getName(), value.toString());
            nameValuePairs.add(nameValuePair);
        }
        return nameValuePairs;
    }

}
