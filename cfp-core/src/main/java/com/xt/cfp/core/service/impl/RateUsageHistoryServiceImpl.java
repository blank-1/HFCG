/**
 * 
 */
package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.RateEnum.RateUsageHisStateEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RateUsageHistory;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.ext.RateUsageHistoryVO;
import com.xt.cfp.core.service.RateUsageHistoryService;

/**
 * @author Lianghuan
 *
 */
@Service
@Transactional
public class RateUsageHistoryServiceImpl implements RateUsageHistoryService {

	@Autowired
	private MyBatisDao myBatisDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xt.cfp.core.service.RateUsageHistoryService#updateRateUsageHistory
	 * (com.xt.cfp.core.pojo.RateUsageHistory)
	 */
	@Override
	public void updateRateUsageHistory(RateUsageHistory rateUsageHistory) {
		myBatisDao.update("RATE_USAGE_HISTORY.updateByPrimaryKeySelective", rateUsageHistory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xt.cfp.core.service.RateUsageHistoryService#findByList(java.lang.
	 * Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public List<RateUsageHistory> findByParams(Long lendOrderId, Long rateUserId, String status) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("lendOrderId", lendOrderId);
		paramMap.put("rateUserId", rateUserId);
		paramMap.put("status", status);
		return myBatisDao.getList("RATE_USAGE_HISTORY.findByParams", paramMap);
	}

	@Override
	public RateUsageHistory insert(RateUsageHistory rateUsageHistory) {
		myBatisDao.insert("RATE_USAGE_HISTORY.insert", rateUsageHistory);
		return rateUsageHistory;
	}

	@Override
	public void insertRateUsageHis(RateUser rateUser, Long userId, Long lendOrderId, Long loanApplicationId, RateUsageHisStateEnum state) {
		RateUsageHistory rateHis = new RateUsageHistory();
		rateHis.setRateUserId(rateUser.getRateUserId());
		rateHis.setUserId(userId);
		rateHis.setLendOrderId(lendOrderId);
		rateHis.setLoanApplicationId(loanApplicationId);
		rateHis.setThisUsedTimes(rateUser.getUsedTimes());
		rateHis.setSurplusTimes(rateUser.getSurplusTimes());
		rateHis.setStatus(state.getValue());
		rateHis.setCreateTime(new Date());
		insert(rateHis);
	}

	@Override
	public List<RateUsageHistoryVO> getHistoryByRateUserId(Long rateUserId) {
		return myBatisDao.getList("RATE_USAGE_HISTORY.getHistoryByRateUserId", rateUserId);
	}

}
