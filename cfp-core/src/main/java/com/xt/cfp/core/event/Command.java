package com.xt.cfp.core.event;


import com.xt.cfp.core.event.pojo.TaskExecLog;

/**
 * Created by Renyulin on 14-6-11 下午3:19.
 */
public abstract class Command {
    protected TaskExecLog taskExecLog = new TaskExecLog();

    protected Event event;

    public abstract Object execute() throws Exception;

    public TaskExecLog getTaskExecLog() {
        return taskExecLog;
    }

    public void setTaskExecLog(TaskExecLog taskExecLog) {
        this.taskExecLog = taskExecLog;
    }
}
