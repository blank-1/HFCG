package com.xt.cfp.core.event.service.impl;


import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.service.TaskExecLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Renyulin on 14-9-19 下午4:10.
 */
@Service()
public class TaskExecLogServiceImpl implements TaskExecLogService {
    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public void add(TaskExecLog taskExecLog) {
        myBatisDao.insert("TASK_EXEC_LOG.insert",taskExecLog);
    }
}
