package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.service.TestService;
import com.xt.cfp.core.util.DateUtil;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2015/8/18 0018.
 */
@Service(value = "testService")
public class TestServiceImpl implements TestService {

    private static final long serialVersionUID = 1l;

    @Override
    public void doTest() throws Exception {
        System.out.println("current time is : " + DateUtil.getNowPlusTime());
    }
}
