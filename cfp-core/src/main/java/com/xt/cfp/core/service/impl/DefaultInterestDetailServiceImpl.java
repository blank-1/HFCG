package com.xt.cfp.core.service.impl;


import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.DefaultInterestDetail;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-4-21 上午11:26.
 */
@Service("defaultInterestDetailService")
@Transactional(rollbackFor=Exception.class,propagation= Propagation.SUPPORTS)
public class DefaultInterestDetailServiceImpl implements DefaultInterestDetailService {

    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public List<DefaultInterestDetail> getInterestDetailsByLoanAppAndState(long repaymentPlanId, char repaymentState) {
        Map map = new HashMap();
        map.put("repaymentPlanId", repaymentPlanId);
        map.put("repaymentState", repaymentState);
        return myBatisDao.getList("DEFAULTINTERESTDETAIL.getInterestDetailsByLoanAppAndState");
    }

    @Override
    @Transactional(rollbackFor=Exception.class,readOnly=false,propagation= Propagation.REQUIRED)
    public void modify(DefaultInterestDetail defaultInterestDetail) {
        myBatisDao.update("DEFAULTINTERESTDETAIL.update",defaultInterestDetail);
    }

    @Override
    public void insert(DefaultInterestDetail defaultInterestDetail) {
        myBatisDao.insert("DEFAULTINTERESTDETAIL.insert",defaultInterestDetail);
    }

	@Override
	public List<DefaultInterestDetail> findBy(Map parameters) throws Exception {
        return myBatisDao.getList("DEFAULTINTERESTDETAIL.findBy",parameters);
	}

    @Override
    public BigDecimal getDefaultInterestByUserId(Long userId) {
        Map<String,BigDecimal> map =  myBatisDao.get("DEFAULTINTERESTDETAIL.getDefaultInterestByUserId",userId);
        BigDecimal subtract = map.get("INTEREST").subtract(map.get("PAID"));
        return subtract.subtract(map.get("REDUCTION"));
    }

    @Override
    public BigDecimal getDefaultInterestByRepaymentPlanId(Long repaymentId) {

        return myBatisDao.get("DEFAULTINTERESTDETAIL.getDefaultInterestByRepaymentPlanId", repaymentId);
    }

    @Override
    public BigDecimal getDefaultInterestPaidByRepaymentPlanId(Long repaymentId) {
        return myBatisDao.get("DEFAULTINTERESTDETAIL.getDefaultInterestPaidByRepaymentPlanId", repaymentId);
    }
}
