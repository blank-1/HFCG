package service;

import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.service.impl.ScheduleServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/16
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
//@WebAppConfiguration
public class ApiInvokeTaskTest {

    @Autowired
    public ScheduleServiceImpl scheduleService;

    @Test
    public void test() throws InterruptedException {
        for (int i = 0; true; i++) {
            System.out.println("----------------------------------- " + i + " -----------------------------");
            scheduleService.doTask();
            Thread.sleep(10000L);
        }

    }
}
