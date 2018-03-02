package com.xt.cfp.app.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.Exception.SystemException;

public class CfpHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * 抛出异常时的处理
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Object object = handlerMethod.getMethod().getAnnotation(ResponseBody.class);
		String viewName = "/error";
		String errorChassName = null;
		String errorCode = null;
		String errorMsg = null;
		if (ex instanceof SystemException) {
			SystemException systemException = ((SystemException) ex);
			String fullName = systemException.getErrorCode().getClass().toString();
			errorChassName = fullName.substring(fullName.lastIndexOf(".") + 1);
			errorCode = systemException.getErrorCode().getCode();
			errorMsg = systemException.getErrorCode().getDesc();
		} else {
			errorChassName = "RuntimeException";
			errorCode = "-101";// 运行时异常
			errorMsg = "系统累了,试试其他页面吧";
			viewName = "/error";
		}
		logger.error("----------errorCode:" + errorCode + "-------------errorMsg:" + errorMsg, ex);
		if (object == null) {
			ModelAndView modelAndView = new ModelAndView();
			if (ex instanceof SystemException) {
				SystemException systemException = ((SystemException) ex);
				if (systemException.getErrorCode().equals(UserErrorCode.LONGIN_EXIST)) {
					modelAndView.setViewName("redirect:/user/toLogin");
					return modelAndView;
				}
			}

			modelAndView.addObject("errorCode", errorCode);
			modelAndView.addObject("errorChassName", errorChassName);
			modelAndView.addObject("errorMsg", errorMsg);
			modelAndView.addObject("result", "error");
			modelAndView.setViewName(viewName);
			return modelAndView;
		} else {
			try {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("errorChassName", errorChassName);
				jsonObj.put("errorCode", errorCode);
				jsonObj.put("errorMsg", errorMsg);
				jsonObj.put("result", "error");
				response.setStatus(500);
				response.getWriter().write(jsonObj.toJSONString());
				response.getWriter().flush();
				logger.error("ajaxerror----------errorCode:" + errorCode + "-------------errorMsg:" + errorMsg, ex);
			} catch (IOException e) {
				logger.error("aop catch ajax error",e);
			}
			return null;
		}
	}
}
