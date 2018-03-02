package com.xt.cfp.app.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.app.annotation.DoNotNeedLogin;
import com.xt.cfp.app.controller.BaseController;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.UserStatus;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.ApplicationContextUtil;
import com.xt.cfp.core.util.WebUtil;

@Aspect
@Component
public class UserCheckAop extends BaseController {
	
    /**
     * 检查某个操作是否需要用户登录
     *
     * @param point
     */
    @Before("execution(* com.xt.cfp.app.controller.*.*(..))")
    @ResponseBody
    public Object checkLogin(JoinPoint point){
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        //只处理不带有DoNotNeedLogin注解的方法
        if (method.getAnnotation(DoNotNeedLogin.class) == null && method.getAnnotation(RequestMapping.class) != null) {

            HttpServletRequest request = WebUtil.getHttpServletRequest();
            HttpServletResponse response = WebUtil.getHttpServletResponse();
            UserInfo userInfo = getCurrentUser(request);
            //如果用户未登录，跳转到注册页面
            if (userInfo == null) {
                return returnResultMap(false, null, "needlogin", UserErrorCode.LONGIN_EXIST.getDesc());
            }
            
            //如果用户的状态是不正常的，跳转到异常页面
            UserInfoService userInfoService = ApplicationContextUtil.getBean(UserInfoService.class);
            UserInfo userByUserId = userInfoService.getUserByUserId(userInfo.getUserId());
            if (!UserStatus.NORMAL.getValue().equals(userByUserId.getStatus())) {
                return returnResultMap(false, null, "needlogin", UserErrorCode.STATUS_IS_NOT_NORMAL.getDesc());
            }

            //如果用户类型不是普通用户，跳转到异常页面
            if (!userInfo.getType().equals(UserType.COMMON.getValue())) {
                return returnResultMap(false, null, "needlogin", UserErrorCode.CAN_NOT_LOGIN.getDesc());
            }
        }
        return point;
    }
}
