package com.xt.cfp.core.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.xt.cfp.core.util.JsonView.JsonViewFactory;

public class JsonUtil {

	public static String json(boolean isSuccess, String info, Object bean) {

		return JsonViewFactory.create().success(isSuccess)
				.setDateFormat(Util.DATE_FORMATE).info(info).put("bean", bean)
				.toJson();
	}

	public static String json(Object bean) {

		return JsonViewFactory.create().setDateFormat(Util.DATE_FORMATE)
				.put("bean", bean).toJson();
	}

    public static Gson getGson(boolean serializeNulls) {
        if (serializeNulls)
            return JsonViewFactory.create().setDateFormat(Util.DATE_FORMATE).getGson();
        else
            return JsonViewFactory.createNoNullValue().setDateFormat(Util.DATE_FORMATE).getGson();
    }

	public static Map<String, Object> fromJson(String json) {
		Map<String, Object> map = new TreeMap<String, Object>();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json);

		if (je.isJsonObject()) {
			for (Map.Entry<String, JsonElement> entry : je.getAsJsonObject().entrySet())
				map.put(entry.getKey(), handle(entry.getValue()));
			return map;
		}
		return null;
	}
	
	private static Object handle(JsonElement jsonElement) {
		if (jsonElement.isJsonPrimitive()) {
			return handlePrimitive(jsonElement.getAsJsonPrimitive());
		} else if (jsonElement.isJsonArray()) {
			return null;
		} else if (jsonElement.isJsonNull()) {
			return null;
		} else if (jsonElement.isJsonObject()) {
			return null;
		}
		return null;
	}

	private static Object handlePrimitive(JsonPrimitive json) {
		if (json.isBoolean())
			return json.getAsBoolean();
		else if (json.isString())
			return json.getAsString();
		else {
			BigDecimal bigDec = json.getAsBigDecimal();
			// Find out if it is an int type
			try {
				bigDec.toBigIntegerExact();
				try {
					return bigDec.intValueExact();
				} catch (ArithmeticException e) {
				}
				return bigDec.longValue();
			} catch (ArithmeticException e) {
			}
			// Just return it as a double
			return bigDec.doubleValue();
		}
	}

    /**
     * 创建标准的用于json返回的Map对象
     * @param o
     * @param isSuccess
     * @param code
     * @param msg
     * @return
     */
    public static Map toStandardJsonMap(Object o, boolean isSuccess, String code, String msg) {
        Map<String, Object> re = new HashMap<String, Object>();
        re.put("success", isSuccess);
        re.put("code", code == null ? "" : code);
        re.put("info", msg == null ? "" : msg);
        if (o != null)
            re.put("result", o);
        return re;
    }
    
    /**
     * 将json转换为list 
     */
    public static List getListFromPageJSON(String jsonStr) {
        if (jsonStr == null || "".equals(jsonStr)) {
            return null;
        }
        int begin = jsonStr.indexOf("rows\":[");
        if (begin == -1) {
            return null;
        }
        String str = jsonStr.substring(begin + "\"rows\":".length() - 1, jsonStr.length() - 1);
        try {
            return JsonUtil.getListFormJSON(str);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /**
     * 将[{}]式的JSON串转换为List
     */
    public static List getListFormJSON(String jsonStr) {
        return JSON.parseArray(jsonStr);
    }

	/**
	 * 将map转换为list
	 */
	public static Object getJSONFromMap(Map map) {
		return JSON.toJSON(map);
	}

}
