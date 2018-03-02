package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.constants.AwardPointEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AwardDetail;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.ext.ReissuedAwardVO;
import com.xt.cfp.core.service.AwardDetailService;
import com.xt.cfp.core.service.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ren yulin on 15-7-14.
 */
@Service
public class AwardDetailServiceImpl implements AwardDetailService {
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private UserAccountService userAccountService;

    @Override
    public AwardDetail findByRightsRepaymentDetailId(long rightsRepaymentDetailId) {
        List<AwardDetail> list = myBatisDao.getList("AWARD_DETAIL.findByRightsRepaymentDetailId", rightsRepaymentDetailId);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public void insert(AwardDetail awardDetail) {
        myBatisDao.insert("AWARD_DETAIL.insert",awardDetail);
    }

    @Override
    public AwardDetail insertAwardDetail(Date now, RightsRepaymentDetail rightsRepaymentDetail, BigDecimal theAward2Lend,
                                         LendOrder lendOrder, long loanApplicationId,AwardPointEnum pointEnum,RateLendOrderTypeEnum awardTypeEnum) {
        UserAccount lendCashAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
        AwardDetail awardDetail = new AwardDetail();
        if (rightsRepaymentDetail != null) {
            awardDetail.setRightsRepaymentDetailId(rightsRepaymentDetail.getRightsRepaymentDetailId());
        }
        awardDetail.setAwardBalance(theAward2Lend);
        awardDetail.setAwardTime(now);
        awardDetail.setAwardPoint(pointEnum.getValue());
        awardDetail.setLendOrderId(lendOrder.getLendOrderId());
        awardDetail.setLoanApplicationId(loanApplicationId);
        awardDetail.setUserAccountId(lendCashAccount.getAccId());
        awardDetail.setAwardType(awardTypeEnum.getValue());
        this.insert(awardDetail);
        return awardDetail;
    }

	@Override
	public List<ReissuedAwardVO> getReissuedAwardListByMobile(List<String> mobileList) {
		Map<String,Object> param = new HashMap<>();
		param.put("mobileList", mobileList);
		return myBatisDao.getList("AWARD_DETAIL.getLostAwardData",param);
	}

	@Override
	public void update(AwardDetail awardDetail) {
		myBatisDao.update("AWARD_DETAIL.updateAwardBalance",awardDetail);
	}

	@Override
	public AwardDetail findByRightsRepaymentDetailIdLock(
			Long rightsRepaymentDetailId, boolean block) {
		if(block){
			return myBatisDao.get("AWARD_DETAIL.findByRightsRepaymentDetailIdLock", rightsRepaymentDetailId);
		}else{
			List<AwardDetail> list = myBatisDao.getList("AWARD_DETAIL.findByRightsRepaymentDetailId", rightsRepaymentDetailId);
	        if (list == null || list.size() == 0) {
	            return null;
	        } else {
	            return list.get(0);
	        }
		}
	}

	@Override
	public List<AwardDetail> findByLendOrderId(Long lendOrderId) {
		return myBatisDao.getList("AWARD_DETAIL.findByLendOrderId", lendOrderId);
	}
	
	@Override
	public AwardDetail findByRightsRepaymentDetailIdAndRateType(long rightsRepaymentDetailId,String rateType) {
		Map map = new HashMap();
		map.put("rightsRepaymentDetailId", rightsRepaymentDetailId);
		map.put("rateType", rateType);
		List<AwardDetail> list =  myBatisDao.getList("AWARD_DETAIL.findByRightsRepaymentDetailIdAndRateType", map);
		 if (list == null || list.size() == 0) {
	            return null;
	        } else {
	            return list.get(0);
	        }
	}
}
