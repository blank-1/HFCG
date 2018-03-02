package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RepaymentRecord;
import com.xt.cfp.core.pojo.RepaymentRecordDetail;
import com.xt.cfp.core.service.RepaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ren yulin on 15-7-8.
 */
@Service
public class RepaymentRecordServiceImpl implements RepaymentRecordService {
    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public void addRepaymentRecord(RepaymentRecord repaymentRecord) {
        myBatisDao.insert("REPAYMENT_RECORD.insert",repaymentRecord);
    }

    @Override
    public void update(RepaymentRecord repaymentRecord) {
        myBatisDao.update("REPAYMENT_RECORD.updateByPrimaryKeySelective",repaymentRecord);
    }
    @Override
    public void addRecordDetail(RepaymentRecordDetail repaymentRecordDetail) {
        myBatisDao.insert("REPAYMENT_RECORD_DETAIL.insert",repaymentRecordDetail);
    }

    @Override
    public RepaymentRecord getRecentRepaymentRecordByCreditorRightId(long creditorRightsId) {
        return myBatisDao.get("getRecentRepaymentRecordByCreditorRightId",creditorRightsId);
    }

    @Override
    public RepaymentRecord getRecentRepaymentRecordByRepaymentId(long repaymentId) {
        return myBatisDao.get("getRecentRepaymentRecordByRepaymentId",repaymentId);
    }
}
