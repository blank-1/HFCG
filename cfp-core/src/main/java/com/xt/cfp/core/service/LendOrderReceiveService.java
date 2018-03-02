package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderReceive;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanPublish;
import com.xt.cfp.core.util.Pair;

/**
 * Created by ren yulin on 15-7-8.
 */
public interface LendOrderReceiveService {
    LendOrderReceive getByOrderAndDate(long lendOrderId, String today);

    LendOrderReceive getByOrderAndSectionCode(long lendOrderId, int sectionCode);

    LendOrderReceive getLastByOrder(Long lendOrderId);

    List<LendOrderReceive> getByLendOrderId(Long lendOrderId);

    void update(Map map);

    LendOrderReceive getFirstByOrder(Long lendOrderId);

    /**
     * 获取用户的待回本金(productType 为空时包含省心计划)
     * @param userId
     * @return
     */
    BigDecimal getCapitalReciveByUserId(Long lendOrderId, Long userId, LendProductTypeEnum ... productType);

    /**
     * 获取用户的待回收益(productType 为空时包含省心计划)
     * @param userId
     * @return
     */
    BigDecimal getInterestReciveByUserId(Long lendOrderId, Long userId, LendProductTypeEnum ... productType);

    /**
     * 获取当前用户收益的走势数据
     * @param userId
     */
    Map<String,Pair<BigDecimal,BigDecimal>> getTrendByUserId(Long userId,String productType);

    /**
     * 获取当前用户收益的走势数据(新)
     * @param userId
     */
    Map<String,Pair<BigDecimal,BigDecimal>> getTrendByUserIdNew(Long userId,String productType);

    /**
     * 计算预期收益
     * @param lendOrderId
     * @param userId
     * @param productType
     * @return
     */
    BigDecimal getExceptProfitByUserId(Long lendOrderId,Long userId,LendProductTypeEnum ... productType);

    void insert(LendOrderReceive lendOrderReceiveDetail);

    void delete(long lendOrderReceiveDetailId);

    /**
     * 发送还款成功短信(to 出借人)
     *
     */
    void sendToLenderRepaymentSuccessMsg(int sectionCode, LoanApplication loanApplication, Date repaymentDate, BigDecimal balance,String mobileNo);

    /**
     * 根据主键查询（可锁表）
     * */
    LendOrderReceive getByIdLock(Long receiveId, boolean isLock);
    /**
     * 发送提前还款成功短信(to 出借人)
     */
    void sendToLenderAheadRepaymentSuccessMsg(LendOrder lendOrder,LoanPublish loanPublish, String mobileNo);
    /**
     * 发送提前还款成功站内信(to 出借人)
     */
	void sendToLenderAheadRepaymentSuccessWebMsg(Date payTime,
			LoanPublish loanPublish, List<Long> userIds);

	   /**
     * 获取用户的待回本金(productType 为空时包含省心计划)
     * @param userId
     * @return
     */
    BigDecimal getChildCapitalReciveByUserId(Long lendOrderId, Long userId );

    /**
     * 获取用户的待回收益(productType 为空时包含省心计划)
     * @param userId
     * @return
     */
    BigDecimal getChildInterestReciveByUserId(Long lendOrderId, Long userId );
    
}
