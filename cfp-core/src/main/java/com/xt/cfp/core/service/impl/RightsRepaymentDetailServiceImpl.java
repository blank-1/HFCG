package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.ext.PaymentDayListDataVO;
import com.xt.cfp.core.pojo.ext.PaymentDaySummaryDataVO;
import com.xt.cfp.core.pojo.ext.PaymentMonthListDataVO;
import com.xt.cfp.core.pojo.ext.RepairRightsRepaymentDetailData;
import com.xt.cfp.core.service.RightsRepaymentDetailService;

@Service
public class RightsRepaymentDetailServiceImpl implements RightsRepaymentDetailService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Override
	public List<RightsRepaymentDetail> getDetailListByRightsId(Long creditorRightsId) {
		return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.getDetailListByRightsId",creditorRightsId);
	}

    @Override
    public RightsRepaymentDetail getDetailByRightsId(Long creditorRightsId, int sectionCode) {
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("creditorRightsId",creditorRightsId);
        paraMap.put("sectionCode",sectionCode);
        return myBatisDao.get("RIGHTSREPAYMENTDETAIL.getDetailByRightsId",paraMap);
    }

    @Override
    public void update(Map rightsDetailMap) {
        myBatisDao.update("RIGHTSREPAYMENTDETAIL.updateMap",rightsDetailMap);
    }

    @Override
    public List<RightsRepaymentDetail> getRightsRepaymentDetailsByPlanId(long repaymentPlanId) {
        return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.getRightsRepaymentDetailsByPlanId",repaymentPlanId);
    }

	/**
	 * 添加-债权还款明细。
	 */
	@Override
	public RightsRepaymentDetail addRightsRepaymentDetail(RightsRepaymentDetail rightsRepaymentDetail) {
		myBatisDao.insert("RIGHTSREPAYMENTDETAIL.insert", rightsRepaymentDetail);
		return rightsRepaymentDetail;
	}

	@Override
	public List<RightsRepaymentDetail> findBy(Map<String,Object> paramMap) {
		return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.findBy",paramMap);
	}

	@Override
	public List<RepairRightsRepaymentDetailData> repairData() {
		return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.repairRightsRepaymentDetailData");
	}

	@Override
	public void updateMulty(Map rightsDetailMap) {
		myBatisDao.update("RIGHTSREPAYMENTDETAIL.updateMultiyMap",rightsDetailMap);
	}

	@Override
	public List<RightsRepaymentDetail> getRightsRepaymentDetailsAndOrderByPlanId(
			long repaymentPlanId) {
		  return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.getRightsRepaymentDetailsAndOrderByPlanId",repaymentPlanId);
	}

	@Override
	public List<RightsRepaymentDetail> getAllRightsRepaymentDetailsByPlanId(
			long repaymentPlanId) {
		return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.getAllRightsRepaymentDetailsByPlanId",repaymentPlanId);
	}

	@Override
	public RightsRepaymentDetail findById(Long rightsRepaymentId) {
		return myBatisDao.get("RIGHTSREPAYMENTDETAIL.selectByPrimaryKey", rightsRepaymentId);
	}

	@Override
	public BigDecimal getSumShouldBalanceByMonth(Map<String, Object> param) {
		return myBatisDao.get("RIGHTSREPAYMENTDETAIL.getSumShouldBalanceByMonth", param);
	}

	@Override
	public List<PaymentMonthListDataVO> getMonthListDataByMonth(Map<String, Object> param) {
		return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.getMonthListDataByMonth", param);
	}

	@Override
	public PaymentDaySummaryDataVO getDaySummaryDataByDay(Map<String, Object> param) {
		return myBatisDao.get("RIGHTSREPAYMENTDETAIL.getDaySummaryDataByDay", param);
	}

	@Override
	public List<PaymentDayListDataVO> getDayListDataByDay(Map<String, Object> param) {
		return myBatisDao.getList("RIGHTSREPAYMENTDETAIL.getDayListDataByDay", param);
	}

}
