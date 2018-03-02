package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.service.SchedulerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.MethodInvoker;

import java.util.Date;

/**
 * User: yulei
 * Date: 14-5-6
 * Time: 下午1:30
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

    private static final Logger LOGGER = Logger.getLogger(SchedulerServiceImpl.class);

    private static final String SIMPLEJOB_GROUPNAME = "simpleJobGroup";
    private static final String SIMPLETRIGGER_GROUPNAME = "simpleTriggerGroup";

    /*@Resource(name = "startQuertz")
    private Scheduler scheduler;*/

    @Override
    public void addSimpleTimerTask(MethodInvoker methodInvoker, String jobName, Date startTime) {
        /**
         * 不允许执行时间早于当前时间
         *//*
        if (startTime.compareTo(new Date()) < 0)
            throw new SystemException(SystemErrorCode.CANNOT_SET_TIMERTASK_BEFORE_NOW).set("startTime", startTime);

        *//**
         * 设定定时任务
         *//*
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(SimpleMethodInvokeJob.PNAME_METHOD_INVOKER, methodInvoker);
        SimpleTrigger simpleTrigger = new SimpleTrigger(UUID.randomUUID().toString(), SIMPLETRIGGER_GROUPNAME);
        simpleTrigger.setStartTime(startTime);
        simpleTrigger.setJobName(jobName);
        simpleTrigger.setJobGroup(SIMPLEJOB_GROUPNAME);
        simpleTrigger.setJobDataMap(jobDataMap);

        try {
            if (this.scheduler.getJobDetail(jobName, SIMPLEJOB_GROUPNAME) == null) {
                JobDetail jobDetail = new JobDetail(jobName, SIMPLEJOB_GROUPNAME, SimpleMethodInvokeJob.class);
                this.scheduler.addJob(jobDetail, true);
            }
            this.scheduler.scheduleJob(simpleTrigger);
        } catch (SchedulerException e) {
            throw SystemException.wrap(e, SystemErrorCode.CANNOT_SET_TIMERTASK).set("reason", e.getMessage());
        }*/
    }

}
