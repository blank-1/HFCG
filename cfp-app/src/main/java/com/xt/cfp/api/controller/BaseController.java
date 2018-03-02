package com.xt.cfp.api.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaseController extends MultiActionController{
	
    /**
     * 【融360】
     * 返回json格式结果。
     * @param isSuccess 是否成功
     * @param data 数据
     * @param errorCode 错误编号
     * @param errorMsg 错误消息
     */
    protected Object returnR360ResultMap(boolean isSuccess, Object data, String errorCode, String errorMsg) {
        Map resultMap = new HashMap();
        if (isSuccess) {
            resultMap.put("return", 1);
            resultMap.put("data", data);
        } else {
            resultMap.put("return", 0);
            resultMap.put("errorCode", errorCode);
            resultMap.put("errorMsg", errorMsg);
        }
        return JSON.toJSON(resultMap);
    }
	
    /**
     * 【网贷之家】
     * 返回json格式结果。
     * @param isSuccess 是否成功
     * @param data 数据
     * @param errorCode 错误编号
     * @param errorMsg 错误消息
     */
    protected Object returnResultMap(boolean isSuccess, Object data, String errorCode, String errorMsg) {
        Map resultMap = new HashMap();
        if (isSuccess) {
//            resultMap.put("result", "success");
            resultMap.put("data", data);
        } else {
            resultMap.put("result", "error");
            resultMap.put("errorCode", errorCode);
            resultMap.put("errorMsg", errorMsg);
        }
        return JSON.toJSON(resultMap);
    }
    
    /**
     * 【网贷之家】
     * @param data 数据
     * @return
     */
    protected Object returnSuccessMap(Object data) {
        return JSON.toJSON(data);
    }
    
    /**
     * 【网贷天眼】
     * @param isSuccess
     * @param data
     * @param errorCode
     * @param errorMsg
     * @return
     */
    protected Object returnWdtyLoginResult(boolean isSuccess, Object data) {
        Map resultMap = new HashMap();
        if (isSuccess) {
        	resultMap.put("result", "1");
        } else {
            resultMap.put("result", "-1");
        }
        resultMap.put("data", data);
        return JSON.toJSON(resultMap);
    }
    
	/**
	 * 【APP接口工具】
	 * 读取json文件
	 * @param path
	 * @return
	 */
	public JSONObject getJSONObjectByJson(String path) {
        File file = new File(path);
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        JSONObject jsonArray = null;
        if (null != buffer) {
            jsonArray = JSON.parseObject(buffer.toString());
        }
        return jsonArray;
    }
}
