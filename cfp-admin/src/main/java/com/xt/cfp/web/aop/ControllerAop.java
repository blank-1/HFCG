package com.xt.cfp.web.aop;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.util.JsonView;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class ControllerAop {

	private Logger log = Logger.getLogger(ControllerAop.class);

	@Around("execution(* com.xt.cfp.web.controller.*.*(..))")
	public Object catchException(final ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = null;
		try {
			retVal = pjp.proceed();
		} catch (Exception e) {
            e.printStackTrace();
			
			boolean needThrow = true;

			if (e instanceof SystemException) {
				SystemException se = (SystemException) e;
				log.error(se.getDetailDesc(),se);
			} else {
				log.error(e.getMessage(),e);
			}

			Signature signature = pjp.getSignature();   
			if (signature instanceof MethodSignature) {
				MethodSignature methodSignature = (MethodSignature) signature;   
				Method method = methodSignature.getMethod();  
				ResponseBody rb = method.getAnnotation(ResponseBody.class);
				
				if (rb != null) {
					return JsonView.JsonViewFactory.create().success(false)
							.info(e.getMessage())
							.toJson();
				}

			}
			
			if (needThrow) {
				log.error("###error catched by aop will be throwed without deal.");
				throw e;
			}
		}
		return retVal;
	}
}
