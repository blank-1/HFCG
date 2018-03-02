package com.xt.cfp.core.service;

import com.xt.cfp.core.service.container.AccountValueChangedQueue;


/**
 * Created by yulei on 2015/6/12.
 */
public interface UserAccountOperateService {

    /**
     * 执行现金流
     *
     * @param accountValueChangedQueue
     */
    void execute(AccountValueChangedQueue accountValueChangedQueue);
}
