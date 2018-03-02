package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.RepaymentRecord;
import com.xt.cfp.core.pojo.RepaymentRecordDetail;

import java.util.Map;

/**
 * Created by ren yulin on 15-7-8.
 */
public interface RepaymentRecordService {
    void addRepaymentRecord(RepaymentRecord repaymentRecord);

    void update(RepaymentRecord repaymentRecord);

    void addRecordDetail(RepaymentRecordDetail repaymentRecordDetail);

    /**
     * 通过债权id获得最近还款记录
     * @param creditorRightsId
     * @return
     */
    RepaymentRecord getRecentRepaymentRecordByCreditorRightId(long creditorRightsId);
    /**
     * 通过还款计划id获得最近还款记录
     * @param repaymentId
     * @return
     */
    RepaymentRecord getRecentRepaymentRecordByRepaymentId(long repaymentId);
}
