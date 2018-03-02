package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RightsPrepayDetail;
import com.xt.cfp.core.pojo.SupplementaryDifference;
import com.xt.cfp.core.service.RightsPrepayDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ren yulin on 15-7-8.
 */
@Service
public class RightsPrepayDetailServiceImpl implements RightsPrepayDetailService {
    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public void insert(RightsPrepayDetail rightsPrepayDetail) {
        myBatisDao.insert("RIGHTS_PREPAY_DETAIL.insert",rightsPrepayDetail);
    }

    @Override
    public RightsPrepayDetail findByNewRightsId(long newRightsId) {
        return myBatisDao.get("RIGHTS_PREPAY_DETAIL.findByNewRightsId", newRightsId);
    }

    @Override
    public void update(Map prepayDetailMap) {
        myBatisDao.update("RIGHTS_PREPAY_DETAIL.updateByMap",prepayDetailMap);
    }

    @Override
    public void recordSupplementaryDifference(SupplementaryDifference sd) {
        myBatisDao.insert("SUPPLEMENTARY_DIFFERENCE.insert",sd);
    }
}
