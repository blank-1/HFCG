package com.xt.cfp.app.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.propertyEditor.BigDecimalPropertyEditor;
import com.xt.cfp.core.propertyEditor.DatePropertyEditor;
import com.xt.cfp.core.propertyEditor.LongPropertyEditor;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.StringUtils;

public class BaseController extends MultiActionController {
	
	private String WWW_BASEPATH = "https://www.caifupad.com";// 正式环境
	private String M_BASEPATH = "http://m.caifupad.com";// 正式环境
	
    @Autowired
    private RedisCacheManger redisCacheManger;
    
    @Autowired
    private UserInfoService userInfoService;

    protected String getWWW_BASEPATH(HttpServletRequest request) {
    	//读取config json文件
		String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/config.json";
		JSONObject jsonObject = this.getJSONObjectByJson(path);
		if(null != jsonObject && !"".equals(jsonObject.getString("WWW_BASEPATH"))){
			WWW_BASEPATH = jsonObject.getString("WWW_BASEPATH");
		}
		return WWW_BASEPATH;
	}

    protected String getM_BASEPATH(HttpServletRequest request) {
		//读取config json文件
		String path = request.getSession().getServletContext().getRealPath("/") + "resources" + "/json/config.json";
		JSONObject jsonObject = this.getJSONObjectByJson(path);
		if(null != jsonObject && !"".equals(jsonObject.getString("M_BASEPATH"))){
			M_BASEPATH = jsonObject.getString("M_BASEPATH");
		}
		return M_BASEPATH;
	}

	@InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DatePropertyEditor());
        binder.registerCustomEditor(BigDecimal.class, new BigDecimalPropertyEditor());
        binder.registerCustomEditor(Long.class, new LongPropertyEditor());
    }

    /**
     * 获取Request对象
     */
    public HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    
    /**
     * 获得当前登录用户信息
     * @param request
     * @return
     */
    public UserInfo getCurrentUser(HttpServletRequest request){
    	String userToken = request.getParameter("userToken");
    	if(null != userToken && !"".equals(userToken)){
    		String userId = redisCacheManger.getRedisCacheInfo(userToken);
    		if(null != userId){
    			return userInfoService.getUserByUserId(Long.valueOf(userId));
    		}else {
				return null;
			}
    	}
    	return null;
    }

    /**
     * 手机验证
     * @param mobileNo
     * @return
     */
    boolean mobileNoValidate(String mobileNo){
       return StringUtils.mobileNoValidate(mobileNo);
    }

    /**
     * 用户名验证
     * @param loginName
     * @return
     */
    boolean loginNameValidate(String loginName){
        String regex = "([a-z]|[A-Z]|[0-9_-]|[\\u4e00-\\u9fa5]){4,20}+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(loginName);
        boolean b = m.matches();
        return b;
    }

    /**
     * 密码验证
     * @param password
     * @return
     */
    boolean passwordValidate(String password){
        String regex = "^[0-9a-zA-Z]{6,16}+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean b = m.matches();
        return b;
    }
    /**
     * 身份证验证
     * @param password
     * @return
     */
    boolean idCardValidate(String password){
        String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        boolean b = m.matches();
        return b;
    }
    
    /**
     * 判断是否有特殊字符
     * @param str
     * @return true 有，false 没有
     */
    protected boolean isSpecialCharacters(String str){
        str = str.trim();
        String regEx="[<>]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String tagerStr = m.replaceAll("").trim();
        return !tagerStr.equals(str);
    }
    
    /**
     * 返回json格式结果。
     * @param isSuccess 是否成功
     * @param data 数据
     * @param errorCode 错误编号
     * @param errorMsg 错误消息
     */
    protected Object returnResultMap(boolean isSuccess, Object data, String errorCode, String errorMsg) {
        Map resultMap = new HashMap();
        if (isSuccess) {
            resultMap.put("result", "success");
            resultMap.put("data", data);
        } else {
            resultMap.put("result", "error");
            resultMap.put("errorCode", errorCode);
            resultMap.put("errorMsg", errorMsg);
        }
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
