package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LenderVO;
import com.xt.cfp.core.util.Pagination;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ren yulin on 15-6-30.
 */
public interface LendOrderBidDetailService {

    /**
     * 根据出借订单ID和给定的状态查询出借明细记录
     * @param lendOrderId
     * @param lendOrderBidStatusEnums
     * @return
     */
    List<LendOrderBidDetail> findByLendOrderId(Long lendOrderId, LendOrderBidStatusEnum... lendOrderBidStatusEnums);

    List<LendOrderBidDetail> findByLendAndLoan(Long lendOrderId,Long loanApplicationId);

    List<LendOrderBidDetail> findByLoanApplicationId(long loanApplicationId);

    List<LendOrderBidDetail> findByLoanApplicationId(long loanApplicationId, LendOrderBidStatusEnum... lendOrderBidStatusEnums);


    /**
     * 计算该订单下针对该借款产品的出借累计金额
     * @param loanProductId
     * @param lendOrderId
     * @param customerAccountId
     * @return
     */
    BigDecimal sumByLoanPdtIdAndLendOrderId(long loanProductId,long lendOrderId, long customerAccountId);

    void updateStatus(long lendOrderBidDetailId,char status,long creditorRightsId);

    void insert(LendOrderBidDetail lendOrderBidDetail);

    BigDecimal sumCreByUserAndLoanApp(long userId, long loanApplicationId);

    BigDecimal sumCreByLoanApp(Long loanApplicationId, LendOrderBidStatusEnum lendOrderBidStatusEnums);

    /**
     * 查询指定标的，投资列表
     * @param pageNo
     * @param pageSize
     * @param loanApplicationId 借款申请ID
     * @param biding 
     * @param displayEnum 是否显示（1显示2不显示，对应lendorder标displayState）
     * @return
     */
    Pagination<LenderRecordVO> findLendOrderDetailPaging(int pageNo, int pageSize, Long loanApplicationId, LendOrderBidStatusEnum biding, DisplayEnum displayEnum);

    Pagination<LenderVO> findLendOrderDetail(int pageNo, int pageSize, Long loanApplicationId, LendOrderBidStatusEnum biding);
    
    Pagination<LenderRecordVO> getCreditorRightsLenderPaging(int pageNo, int pageSize, Long creditorRightsApplyId);
    
    /**
     * 获取该借款申请投标人数
     * @param loanApplicationId 借款申请ID
     * @param biding 出借订单状态
     * @return
     */
    Integer findLendOrderDetailCount(Long loanApplicationId, LendOrderBidStatusEnum biding);

	List<LendOrderBidDetail> getByLendOrderId(Long orderId, LendOrderBidStatusEnum statu);

    /**
     * 根据债权id查询出借订单明细
     * @param creditorRightsId
     * @return
     */
    LendOrderBidDetail getLendOrderBidDetailByCreditorRightsId(Long creditorRightsId);
    
    /**
     * 根据出借订单id和付款账户id查询出借订单明细
  	 * @param lendOrderId
  	 * @param accountId
     * @return
     * */
    List<LendOrderBidDetail> getLendOrderBidDetailByLendOrderBidIdAndCustomAccId(Long lendOrderId, Long accountId);
    
    /**
     * 获取该债权转让，投标人数
     * @param creditorRightsApplyId 债权转让申请ID
     * @return
     */
	Integer getCreditorRightsLenderCount(Long creditorRightsApplyId);

	/**
	 * 省心计划投资出借列表
	 * @param pageNo
	 * @param pageSize
	 * @param lendProductPublishId 省心产品发布ID
	 * @param biding 出借订单详情状态
	 * @return
	 */
	Pagination<LenderRecordVO> findSXJHLendOrderDetailPaging(int pageNo, int pageSize, Long lendProductPublishId, LendOrderBidStatusEnum biding);
}
