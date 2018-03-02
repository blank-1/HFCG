package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CapitalFlow;
import com.xt.cfp.core.service.CapitalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CapitalFlowServiceImpl implements CapitalFlowService {

    @Autowired
    private MyBatisDao myBatisDao;

    @Override
    public List<CapitalFlow> findByCondition(Map map) {
        return myBatisDao.getList("CAPITAL_FLOW.findByCondition",map);
    }

    @Transactional
    @Override
    public void addCapital(CapitalFlow cap) {
        myBatisDao.insert("CAPITAL_FLOW.insertSelective",cap);
    }

    @Transactional
    @Override
    public void updateCapital(CapitalFlow cap) {
        myBatisDao.update("CAPITAL_FLOW.updateByPrimaryKeySelective",cap);
    }
}
