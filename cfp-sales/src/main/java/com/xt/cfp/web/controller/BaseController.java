package com.xt.cfp.web.controller;

import com.alibaba.fastjson.JSON;
import com.xt.cfp.core.propertyEditor.BigDecimalPropertyEditor;
import com.xt.cfp.core.propertyEditor.DatePropertyEditor;
import com.xt.cfp.core.propertyEditor.LongPropertyEditor;
import com.xt.cfp.core.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController extends MultiActionController {

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
