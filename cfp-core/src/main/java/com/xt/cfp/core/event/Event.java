package com.xt.cfp.core.event;


import com.xt.cfp.core.event.pojo.EventTriggerInfo;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.service.EventTriggerInfoService;
import com.xt.cfp.core.event.service.TaskExecLogService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renyulin on 14-6-11 下午6:54.
 */
public class Event {
    private EventTriggerInfoService eventTriggerInfoService;
    private TaskExecLogService taskExecLogService;
    private EventTriggerInfo eventTriggerInfo;
    private List<Command> commands = new ArrayList<Command>();
    protected List<TaskExecLog> taskExecLogs = new ArrayList<TaskExecLog>();

    public static long EVENT_REPAYMENT = 1;//还款
    public static long EVENT_TURNCREDITOR = 2;//债权转让
    public static long EVENT_DELAYREPAYMENT = 3;//逾期平台垫付
    public static long EVENT_EARLYREPAYMENT = 4;//提前还贷

    public void addCommand(Command command) {
        commands.add(command);
        taskExecLogs.add(command.getTaskExecLog());
    }


    public void fire() throws Exception {
        for (Command command : commands) {
            command.execute();
        }
        eventTriggerInfo.setHappenResult(EventTriggerInfo.HAPPENRESULT_SUCCESS);
        eventTriggerInfoService.add(eventTriggerInfo);
        for (TaskExecLog taskExecLog:taskExecLogs) {
            taskExecLog.setEventTriggerInfoId(eventTriggerInfo.getEventTriggerInfoId());
            taskExecLogService.add(taskExecLog);
        }

    }

    public EventTriggerInfo getEventTriggerInfo() {
        return eventTriggerInfo;
    }

    public void setEventTriggerInfo(EventTriggerInfo eventTriggerInfo) {
        this.eventTriggerInfo = eventTriggerInfo;
    }

    public EventTriggerInfoService getEventTriggerInfoService() {
        return eventTriggerInfoService;
    }

    public void setEventTriggerInfoService(EventTriggerInfoService eventTriggerInfoService) {
        this.eventTriggerInfoService = eventTriggerInfoService;
    }

    public TaskExecLogService getTaskExecLogService() {
        return taskExecLogService;
    }

    public void setTaskExecLogService(TaskExecLogService taskExecLogService) {
        this.taskExecLogService = taskExecLogService;
    }
}
