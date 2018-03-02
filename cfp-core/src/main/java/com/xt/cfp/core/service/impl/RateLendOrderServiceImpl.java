package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.RateEnum.RateLendOrderPointEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.RateLendOrder;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.ext.RateLendOrderVO;
import com.xt.cfp.core.service.RateLendOrderService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RateLendOrderServiceImpl implements RateLendOrderService {

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private MyBatisDao myBatisDao;

	private Logger logger = Logger.getLogger(RateLendOrderServiceImpl.class);

	@Override
	public RateLendOrder findByLendOrderId(Long lendOrderId, String rateType, String status) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("lendOrderId", lendOrderId);
		paramMap.put("rateType", rateType);
		if (StringUtils.isNotBlank(status)) {
			paramMap.put("status", status);
		}

		return myBatisDao.get("RATE_LEND_ORDER.findByLendOrderId", paramMap);
	}

	@Override
	public List<RateLendOrder> findAllByLendOrderId(Long lendOrderId, String status) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("lendOrderId", lendOrderId);
		if (StringUtils.isNotBlank(status)) {
			paramMap.put("status", status);
		}
		return myBatisDao.getList("RATE_LEND_ORDER.findAllByLendOrderId", paramMap);
	}

	@Override
	public void updateRateLendOrder(RateLendOrder rateLendOrder) {
		myBatisDao.update("RATE_LEND_ORDER.updateByPrimaryKeySelective", rateLendOrder);
	}

	@Override
	public List<RateLendOrder> findByLoanApplicationIdAndRepaymentPlanId(Long loanApplicationId, Long repaymentPlanId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (loanApplicationId != null) {
			paramMap.put("loanApplicationId", loanApplicationId);
		}
		if (repaymentPlanId != null) {
			paramMap.put("repaymentPlanId", repaymentPlanId);
		}
		return myBatisDao.getList("RATE_LEND_ORDER.findByLoanApplicationIdAndRepaymentPlanId", paramMap);
	}

	@Override
	public RateLendOrder insert(RateLendOrder rateLendOrder) {
		myBatisDao.insert("RATE_LEND_ORDER.insert", rateLendOrder);
		return rateLendOrder;
	}

	@Override
	@Transactional
	public void createRateLendOrder(RateUser rateUser, Long lendOrderId, Long loanApplicationId, RateLendOrderTypeEnum typeEnum, String awardPoint, BigDecimal rateValue,
			RateLendOrderStatusEnum statuEnum) {
		RateLendOrder rateLendOrder = new RateLendOrder();
		rateLendOrder.setLendOrderId(lendOrderId);
		rateLendOrder.setRateUserId(rateUser == null ? null : rateUser.getRateUserId());
		rateLendOrder.setLoanApplicationId(loanApplicationId);
		rateLendOrder.setRateType(typeEnum.getValue());
		rateLendOrder.setAwardPoint(awardPoint);
		rateLendOrder.setRateValue(rateValue);
		rateLendOrder.setStatus(statuEnum.getValue());
		rateLendOrder.setCreateTime(new Date());
		insert(rateLendOrder);
	}

	@Override
	@Transactional
	public void createActivity(Long userId, LendOrder lendOrder, Long loanApplicationId) {
		Date activityStartDate = DateUtil.parseStrToDate("2016-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date activityEndDate = DateUtil.parseStrToDate("2016-09-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date oldUserDate = DateUtil.parseStrToDate("2016-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		logger.info("八月活动奖励userId:" + userId + "------投标金额：" + lendOrder.getBuyBalance() + "------loanApplicationId:" + loanApplicationId);
		if (now.getTime() >= activityStartDate.getTime() && now.getTime() < activityEndDate.getTime() && lendOrder.getTimeLimit() == 3) {
			BigDecimal rateValue = BigDecimal.ZERO;
			UserInfo user = userInfoService.getUserByUserId(userId);
			logger.info("userId:" + userId + "用户注册时间：" + DateUtil.getFormattedDateUtil(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			if (user.getCreateTime().getTime() < oldUserDate.getTime()) {
				if (lendOrder.getBuyBalance().compareTo(new BigDecimal("10000")) >= 0 && lendOrder.getBuyBalance().compareTo(new BigDecimal("50000")) < 0) {
					rateValue = rateValue.add(new BigDecimal("0.8"));
				} else if (lendOrder.getBuyBalance().compareTo(new BigDecimal("50000")) >= 0 && lendOrder.getBuyBalance().compareTo(new BigDecimal("100000")) < 0) {
					rateValue = rateValue.add(new BigDecimal("1.0"));
				} else if (lendOrder.getBuyBalance().compareTo(new BigDecimal("100000")) >= 0 && lendOrder.getBuyBalance().compareTo(new BigDecimal("200000")) < 0) {
					rateValue = rateValue.add(new BigDecimal("1.1"));
				} else if (lendOrder.getBuyBalance().compareTo(new BigDecimal("200000")) >= 0 && lendOrder.getBuyBalance().compareTo(new BigDecimal("500000")) < 0) {
					rateValue = rateValue.add(new BigDecimal("1.2"));
				} else if (lendOrder.getBuyBalance().compareTo(new BigDecimal("500000")) >= 0 && lendOrder.getBuyBalance().compareTo(new BigDecimal("1000000")) < 0) {
					rateValue = rateValue.add(new BigDecimal("1.3"));
				} else if (lendOrder.getBuyBalance().compareTo(new BigDecimal("1000000")) >= 0) {
					rateValue = rateValue.add(new BigDecimal("1.5"));
				}
			} else {
				if (lendOrder.getBuyBalance().compareTo(new BigDecimal("10000")) >= 0) {
					rateValue = rateValue.add(new BigDecimal("0.5"));
				}
			}
			logger.info("userId" + userId + "--活动奖励利率：" + rateValue);

			if (rateValue.compareTo(BigDecimal.ZERO) != 0) {
				createRateLendOrder(null, lendOrder.getLendOrderId(), loanApplicationId, RateLendOrderTypeEnum.ACTIVITY, RateLendOrderPointEnum.CYCLE_RAPAYMENT.getValue(), rateValue,
						RateLendOrderStatusEnum.VALID);
			}

		}

	}

	@Override
	public List<RateLendOrderVO> getRateLendOrderCreditByUserId(Long userId) {
		return myBatisDao.getList("RATE_LEND_ORDER.getRateLendOrderCreditByUserId", userId);
	}

	@Override
	public List<RateLendOrder> findByLendOrderIdAndType(Long lendOrderId, String rateType) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("lendOrderId", lendOrderId);
		paramMap.put("rateType", rateType);
		return myBatisDao.getList("RATE_LEND_ORDER.findByLendOrderIdAndType", paramMap);
	}

}
