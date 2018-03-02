package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.service.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by luqinglin on 2015/6/16.
 */

public class MakeLoanTest extends TestCase {

    private UserAccountOperateService userAccountOperateService;

    private UserAccountService userAccountService;

    private CustomerCardService customerCardService;

    private WithDrawService withDrawService;

    private LoanApplicationService loanApplicationService;
    private ScheduleService scheduleService;

    @Before
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        scheduleService = (ScheduleService) applicationContext.getBean("scheduleServiceImpl");
    }

    @Test
    @Transactional
    @Rollback(true)// 事务自动回滚，默认是true。可以不写
    public void testexecute() throws Exception {
        scheduleService.doTask();

    }


}
