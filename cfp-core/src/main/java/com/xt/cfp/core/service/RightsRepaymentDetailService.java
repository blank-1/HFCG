package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.ext.PaymentDayListDataVO;
import com.xt.cfp.core.pojo.ext.PaymentDaySummaryDataVO;
import com.xt.cfp.core.pojo.ext.PaymentMonthListDataVO;
import com.xt.cfp.core.pojo.ext.RepairRightsRepaymentDetailData;

public interface RightsRepaymentDetailService {

	/**
	 * 根据债权id查找债权还款明细记录，并按照期号正序排序
	 * 注意，这里的正序排序规则非常重要，已涉及相关业务计算，绝不可修改排序算法
	 * @param creditorRightsId
	 * @return
	 */
	List<RightsRepaymentDetail> getDetailListByRightsId(Long creditorRightsId);

	/**
	 * 根据债权id和期号查找唯一一条债权还款明细记录
	 * @param creditorRightsId
	 * @param sectionCode
	 * @return
	 */
	RightsRepaymentDetail getDetailByRightsId(Long creditorRightsId,int sectionCode);
	
	List<RightsRepaymentDetail> findBy(Map<String,Object>  paramMap);

	/**
	 * 更新债权还款明细
	 * @param rightsDetailMap
	 */
	void update(Map rightsDetailMap);

	/**
	 * 根据还款计划id查找对应的债权还款明细记录
 	 * @param repaymentPlanId
	 * @return
	 */
	List<RightsRepaymentDetail> getRightsRepaymentDetailsByPlanId(long repaymentPlanId);

	/**
	 * 新建债权还款明细记录
	 * @param rightsRepaymentDetail
	 * @return
	 */
	RightsRepaymentDetail addRightsRepaymentDetail(RightsRepaymentDetail rightsRepaymentDetail);

	List<RepairRightsRepaymentDetailData> repairData();
	/**
	 * 更新债权还款明细
	 * @param rightsDetailMap
	 */
	void updateMulty(Map rightsDetailMap);
	
	/**
	 * 根据还款计划id查找对应的债权还款明细记录以及订单信息
 	 * @param repaymentPlanId
	 * @return
	 */
	List<RightsRepaymentDetail> getRightsRepaymentDetailsAndOrderByPlanId(long repaymentPlanId);
	
	/**
	 * 根据还款计划id查找对应的债权还款明细记录以及订单信息
	 * @param repaymentPlanId
	 * @return
	 */
	List<RightsRepaymentDetail> getAllRightsRepaymentDetailsByPlanId(long repaymentPlanId);
	
	RightsRepaymentDetail findById(Long rightsRepaymentId) ;
	
	/**
	 * 回款日历，获取指定月份的回款总额
	 * @param param
	 * @return
	 */
	BigDecimal getSumShouldBalanceByMonth(Map<String, Object> param);
	/**
	 * 回款日历，获取指定月份的回款列表数据
	 * @param param
	 * @return
	 */
	List<PaymentMonthListDataVO> getMonthListDataByMonth(Map<String, Object> param);
	/**
	 * 回款日历，获取指定天的回款汇总数据
	 * @param param
	 * @return
	 */
	PaymentDaySummaryDataVO getDaySummaryDataByDay(Map<String, Object> param);
	/**
	 * 回款日历，获取指定天的回款列表数据
	 * @param param
	 * @return
	 */
	List<PaymentDayListDataVO> getDayListDataByDay(Map<String, Object> param);
	
}
