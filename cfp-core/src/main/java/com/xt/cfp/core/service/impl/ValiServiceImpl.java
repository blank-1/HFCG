package com.xt.cfp.core.service.impl;


import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.service.ValiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "valiService")
public class ValiServiceImpl implements ValiService {

    private static Logger logger = Logger.getLogger(ValiServiceImpl.class);

    @Autowired
    private MyBatisDao myBatisDao;

    @Override
    @Transactional
    public Long queryValiData() {
        logger.debug("#check database!");
        return myBatisDao.get("queryValiServerData", null);
    }

}
