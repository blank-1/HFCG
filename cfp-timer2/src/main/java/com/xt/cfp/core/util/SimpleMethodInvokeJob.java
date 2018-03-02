package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ReflectionErrorCode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.MethodInvoker;

/**
 * User: yulei
 * Date: 14-5-6
 * Time: 上午11:49
 */
public class SimpleMethodInvokeJob implements Job {

    public static final String PNAME_METHOD_INVOKER = "methodInvoker";

    private MethodInvoker methodInvoker;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            this.methodInvoker = (MethodInvoker) context.getTrigger().getJobDataMap().get(PNAME_METHOD_INVOKER);
            methodInvoker.prepare();
            methodInvoker.invoke();
        } catch (Exception e) {
            e.printStackTrace();
            if (methodInvoker != null)
                throw SystemException.wrap(e, ReflectionErrorCode.CANNOT_INVOKE_METHOD).set("method", methodInvoker.getTargetMethod())
                        .set("obj or class", methodInvoker.getTargetObject() != null ? methodInvoker.getTargetObject() : methodInvoker.getTargetClass());
            else
                throw SystemException.wrap(e, ReflectionErrorCode.CANNOT_INVOKE_METHOD).set("methodInvoker", "null");
        }

    }
}
