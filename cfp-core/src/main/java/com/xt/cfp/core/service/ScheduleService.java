package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.Schedule;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    List<Schedule> findByCondition(Map map);

    Schedule addSchedule(Schedule schedule);

    Schedule updateSchedule(Schedule schedule);

    /**
     * 执行恒丰流水的定时任务
     */
    void doTask();

    /**
     * 刷新用户充值单状态
     */
    void updateRechargeOrderStatus();
}
