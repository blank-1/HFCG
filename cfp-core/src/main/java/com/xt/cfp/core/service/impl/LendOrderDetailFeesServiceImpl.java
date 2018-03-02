package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.LendOrderFeesDetail;
import com.xt.cfp.core.service.LendOrderDetailFeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ren yulin on 15-7-8.
 */
@Service
public class LendOrderDetailFeesServiceImpl implements LendOrderDetailFeesService {
    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public List<LendOrderFeesDetail> getDetailByLendOrderIdAndSectionCode(Long lendOrderId, int sectionCode) {
        Map map = new HashMap();
        map.put("lendOrderId",lendOrderId);
        map.put("sectionCode",sectionCode);
        return myBatisDao.getList("LEND_ORDER_FEES_DETAIL.getDetailByLendOrderIdAndSectionCode",map);
    }

    @Override
    public void insert(LendOrderFeesDetail lendOrderFeesDetail) {
        myBatisDao.insert("LEND_ORDER_FEES_DETAIL.insert",lendOrderFeesDetail);
    }

    @Override
    public void update(Map orderFeesDetailMap) {
        myBatisDao.update("LEND_ORDER_FEES_DETAIL.updateByMap",orderFeesDetailMap);
    }
}
