package com.xt.cfp.core.service;

import org.springframework.util.MethodInvoker;

import java.util.Date;

/**
 * 定时器服务
 * User: yulei
 * Date: 14-5-6
 * Time: 下午1:29
 */
public interface SchedulerService {

    /**
     * 向定时服务中添加一个任务
     * @param methodInvoker
     * @param jobName
     * @param startTime
     */
    void addSimpleTimerTask(MethodInvoker methodInvoker, String jobName, Date startTime);

}
