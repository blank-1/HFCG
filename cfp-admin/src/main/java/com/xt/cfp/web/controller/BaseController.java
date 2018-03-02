package com.xt.cfp.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xt.cfp.core.propertyEditor.BigDecimalPropertyEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.RoleInfo;
import com.xt.cfp.core.propertyEditor.DatePropertyEditor;

public class BaseController extends MultiActionController {
	
    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DatePropertyEditor());
        binder.registerCustomEditor(BigDecimal.class, new BigDecimalPropertyEditor());
    }

    /**
     * 获取Request对象
     */
    public HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    
    /**
     * 获取session对象
     */
    public HttpSession getSession() {
    	return this.getRequest().getSession();
    }

    /**
     * 获取当前登陆用户
     * @return
     */
    public AdminInfo getCurrentUser(){
        HttpSession session = getSession();
        if(session==null)
            return null;
        AdminInfo admin = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
        return admin;
    }
    
    /**
     * 当前使用的角色
     */
    public RoleInfo getCurrentRole() {
    	return (RoleInfo) getSession().getAttribute(AdminInfo.CURRENTROLE);
    }
    
    /**
     * 返回json格式结果。
     * @param isSuccess 是否成功
     * @param data 数据
     * @param errCode 错误编号
     * @param errMsg 错误消息
     */
    protected Object returnResultMap(boolean isSuccess, Object data, String errCode, String errMsg) {
        Map resultMap = new HashMap();
        if (isSuccess) {
            resultMap.put("result", "success");
            resultMap.put("data", data);
        } else {
            resultMap.put("result", "error");
            resultMap.put("errCode", errCode);
            resultMap.put("errMsg", errMsg);
        }
        return JSON.toJSON(resultMap);
    }

    
}


class ResultMap{
    private String result;
    private Object data;
    private String errCode;
    private String errMsg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}