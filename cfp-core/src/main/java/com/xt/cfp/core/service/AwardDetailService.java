package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.xt.cfp.core.constants.AwardPointEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.pojo.AwardDetail;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.ext.ReissuedAwardVO;

/**
 * Created by ren yulin on 15-7-14.
 */
public interface AwardDetailService {

    AwardDetail findByRightsRepaymentDetailId(long rightsRepaymentDetailId);
     void insert(AwardDetail awardDetail);


    AwardDetail insertAwardDetail(Date now, RightsRepaymentDetail rightsRepaymentDetail, BigDecimal theAward2Lend,
                                  LendOrder lendOrder, long loanApplicationId,AwardPointEnum pointEnum,RateLendOrderTypeEnum awartTypeEnum);
    
    List<ReissuedAwardVO> getReissuedAwardListByMobile(List<String> mobileList);
    // 更新奖励金额，时间
	void update(AwardDetail awardDetail);
	//根据主键查询债权详情，设定加锁
	AwardDetail findByRightsRepaymentDetailIdLock(
			Long rightsRepaymentDetailId, boolean block);
	
	//根据出借订单ID查询
	List<AwardDetail> findByLendOrderId(Long lendOrderId);
	//根据债权还款明细ID和奖励类型查询一条数据
	AwardDetail findByRightsRepaymentDetailIdAndRateType(
			long rightsRepaymentDetailId, String rateType);
}
