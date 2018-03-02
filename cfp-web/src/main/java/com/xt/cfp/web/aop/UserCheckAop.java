package com.xt.cfp.web.aop;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.UserStatus;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.ApplicationContextUtil;
import com.xt.cfp.core.util.WebUtil;
import com.xt.cfp.web.annotation.DoNotNeedLogin;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

/**
 * Created by yulei on 2015/7/2.
 */
@Aspect
@Component
public class
        UserCheckAop {

    /**
     * 检查某个操作是否需要用户登录
     *
     * @param point
     */
    @Before("execution(* com.xt.cfp.web.controller.*.*(..))")
    public void checkLogin(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        //只处理不带有DoNotNeedLogin注解的方法
        if (method.getAnnotation(DoNotNeedLogin.class) == null && method.getAnnotation(RequestMapping.class) != null) {

            HttpServletRequest request = WebUtil.getHttpServletRequest();
            HttpServletResponse response = WebUtil.getHttpServletResponse();
            UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);

            //如果用户未登录，跳转到注册页面
            if (userInfo == null) {
//				if (method.getAnnotation(ResponseBody.class) != null) {
//					String json = "{'errorCode': '" + UserErrorCode.LONGIN_EXIST.getCode() + "','errorMsg':'" + UserErrorCode.LONGIN_EXIST.getDesc() + "'}";
//					response.setStatus(500);
//					response.getWriter().write(json);
//				} else {
//					response.sendRedirect(request.getContextPath() + "/user/regist/home");
//				}
//				return;
                throw new SystemException(UserErrorCode.LONGIN_EXIST).set("method", method.getName());
            }
            //如果用户的状态是不正常的，跳转到异常页面
            UserInfoService userInfoService = ApplicationContextUtil.getBean(UserInfoService.class);
            UserInfo userByUserId = userInfoService.getUserByUserId(userInfo.getUserId());
            if (!UserStatus.NORMAL.getValue().equals(userByUserId.getStatus())) {
                throw new SystemException(UserErrorCode.STATUS_IS_NOT_NORMAL).set("method", method.getName());
            }

            //如果用户类型不是普通用户，跳转到异常页面
            if (!userInfo.getType().equals(UserType.COMMON.getValue())) {
                throw new SystemException(UserErrorCode.CAN_NOT_LOGIN).set("method", method.getName());
            }

        }
    }
}
