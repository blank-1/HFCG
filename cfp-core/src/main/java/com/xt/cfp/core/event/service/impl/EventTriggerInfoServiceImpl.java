package com.xt.cfp.core.event.service.impl;


import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.event.pojo.EventTriggerInfo;
import com.xt.cfp.core.event.service.EventTriggerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Renyulin on 14-6-13 上午11:21.
 */
@Service()
public class EventTriggerInfoServiceImpl implements EventTriggerInfoService {
    @Autowired
    private MyBatisDao myBatisDao;


    @Override
    public void add(EventTriggerInfo eventTriggerInfo) {
        myBatisDao.insert("EVENT_TRIGGER_INFO.insert",eventTriggerInfo);
    }


}
