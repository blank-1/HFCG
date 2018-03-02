package com.xt.cfp.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class Util {

	public static final String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
	
	private final static Logger log = Logger.getLogger(Util.class);
	/**
	 * 将订单信息组合成url的参数形式
	 * @param order  订单信息
	 * @return
	 */
	public static String urlParam(Object order) {
		SortedMap<String, Object> map = null;
		if (order instanceof SortedMap) {
			map = (SortedMap<String, Object>) order;
		} else {
			map = beanToMap(order);
		}
		
		return urlParam(map);
	}
	
	/**
	 * 将map组合成url的参数形式
	 * @param map
	 * @return
	 */
	public static String urlParam(SortedMap<String, Object> map) {
		
		StringBuffer sb = new StringBuffer();
		
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value == null) continue;
			String strValue = objectToString(value);
			if (strValue.equals("")) continue;
			sb.append(entry.getKey()).append("=").append(strValue).append("&");
		}
		
		String str = sb.toString();
		if (str.endsWith("&")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	public static String urlParamWithoutKey (Map<String, Object> map){
		StringBuffer sb = new StringBuffer();
        Set<Entry<String, Object>> es = map.entrySet();
        Iterator<Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            String convertedValue = "";
            if (v instanceof Number){
            	convertedValue = v.toString();
            } else {
            	convertedValue = (String)v;
            }
            if (null != convertedValue && !"".equals(convertedValue) && !"sign".equals(k)) {
                sb.append(convertedValue);
            }
        }
        return sb.toString();
	}
	
	public static String urlParamWithUrlEncode(SortedMap<String, Object> map) {
		
		StringBuffer sb = new StringBuffer();
		
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value == null) continue;
			String strValue = objectToString(value);
			if (strValue.equals("")) continue;
			try {
				sb.append(entry.getKey()).append("=").append(URLEncoder.encode(strValue,"UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				break;
			}
		}
		
		String str = sb.toString();
		if (str.endsWith("&")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	//对象转换成字符串
	public static String objectToString(Object value) {
		if (value == null) return "";
		if (value instanceof Date) {
			Date date = (Date) value;
			return formate(date, DATE_FORMATE);
		} else if (value instanceof java.sql.Date) {
			java.sql.Date date = (java.sql.Date) value;
			return formate(date, DATE_FORMATE);
		} else if (value instanceof BigDecimal) {
			BigDecimal bg = (BigDecimal) value;
			
			return bg.toString();
		}
		return value.toString();
	}
	
	/**
	 * bean 转换成map
	 * @param order
	 * @return
	 */
	public static SortedMap<String, Object> beanToMap(Object bean) {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		if (bean == null) return map;
		List<PropertyDescriptor> pdList = readPropertyDescriptors(bean.getClass());
		for (PropertyDescriptor pd : pdList) {
			Method m1 = pd.getReadMethod();
			try {
				Object value = m1.invoke(bean, null);
				map.put(pd.getName(), value);
			} catch (Exception e) {
				throw new RuntimeException("反射异常");
			} 
			
		}
		return map;
	}
	
	/**
	 * bean 转换成map
	 * @param order
	 * @return
	 */
	public static SortedMap<String, Object> baseBeanToMap(Object bean) {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		if (bean == null) return map;
		Method[] methods = bean.getClass().getDeclaredMethods();
		for (Method md : methods) {
			if(md.getName().startsWith("get") && md.getParameterTypes().length == 0) {
				try {
					Object value = md.invoke(bean, null);
					if (value != null) {
						String property = md.getName().substring(3);
						property = property.substring(0, 1).toLowerCase() + property.substring(1);
						map.put(property, value);
					}
				} catch (Exception e) {
					throw new RuntimeException("反射异常");
				} 
			}
		}
		return map;
	}
	
	public static SortedMap<String, Object> beanToMapWithoutNull(Object bean) {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		if (bean == null) return map;
		List<PropertyDescriptor> pdList = readPropertyDescriptors(bean.getClass());
		for (PropertyDescriptor pd : pdList) {
			Method m1 = pd.getReadMethod();
			try {
				Object value = m1.invoke(bean, null);
				if (value != null){
					map.put(pd.getName(), value);
				}
			} catch (Exception e) {
				throw new RuntimeException("反射异常");
			} 
			
		}
		return map;
	}
	
	/**
	 * bean 转换成map
	 * @param order
	 * @return
	 */
	public static <T>T mapTBean(T bean, Map<String, Object> map) {
		if (map == null || map.size() < 1 || bean == null) return bean;
		for (Entry<String, Object> entry : map.entrySet()) {
			try {
				if (entry.getValue() == null || (entry.getValue().toString().equals("") && !isReturnString(bean, entry.getKey()))) {
					continue;
				}
				BeanUtils.setProperty(bean, entry.getKey(), entry.getValue());
			} catch (Exception e) {
				log.error("#error + " + bean.getClass().getName() + " has not property " + entry.getKey() + ", value: " + entry.getValue(), e);
			} 
		}
		return bean;
	}
	
	private static <T>boolean isReturnString(T bean, String property) {
		boolean isreturn = false;
		String p = property.substring(0, 1).toUpperCase() + property.substring(1);
		Method method;
		try {
			method = bean.getClass().getMethod("get" + p, null);
			if (method != null && method.getReturnType().equals(String.class)) {
				return true;
			}
		} catch (Exception e) {
		} 
		
		return isreturn;
	}

	private static PropertyDescriptor[] propertyDescriptors(Class<?> c) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);
		} catch (IntrospectionException e) {
			throw new RuntimeException("Bean introspection failed: "+ e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}
	
	private static List<PropertyDescriptor> readPropertyDescriptors(Class<?> c) {
		PropertyDescriptor[] pds = propertyDescriptors(c);
		if (pds == null || pds.length < 1) return new ArrayList<PropertyDescriptor>(0);
		List<PropertyDescriptor> pdList = new ArrayList<PropertyDescriptor>(pds.length);
		for (PropertyDescriptor pd : pds) {
			Method readMethod = pd.getReadMethod();
			if (readMethod != null && !pd.getName().equals("class")) {
				pdList.add(pd);
			}
		}
		return pdList;
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formate(Date date, String pattern) {
		if (date == null) return "";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formate(java.sql.Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String dateString, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 格式化金额
	 * @param money 10.00元  == 10000
	 * @return
	 */
	public static String money(Integer money) {
		if (money == null) return null;
		BigDecimal bg = new BigDecimal(money.toString());
		bg.setScale(2);
		bg.divide(new BigDecimal("100"));
		return bg.toString();
	}
	
	/**
	 * 格式化金额
	 * @param money 10.00元  == 10000
	 * @return
	 */
	public static Integer moneyToInt(BigDecimal money) {
		if (money == null) return null;
		money.setScale(2);
		money.divide(new BigDecimal("100"));
		return money.intValue();
	}
	
	/**
	 * 金额转换
	 * @param money 10.00元  == 10000
	 * @return
	 */
	public static BigDecimal bigDecimal(Integer money) {
		if (money == null) return null;
		BigDecimal bg = new BigDecimal(money.toString());
		BigDecimal r = bg.divide(new BigDecimal("100"));
		return r;
	}
	
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}
	
	/**
	 * 获取IP
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		if (request == null) return ""; //测试情况
		String ip = request.getHeader("x-forwarded-for");   
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getHeader("Proxy-Client-IP");   
        }   
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getHeader("WL-Proxy-Client-IP");   
        }   
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
            ip = request.getRemoteAddr();   
        }   
        if (ip != null) {
        	int t = ip.indexOf(",");
        	if (t > 0) {
        		ip = ip.substring(0, t);
        	}
        }
        return ip;  
	}
}
