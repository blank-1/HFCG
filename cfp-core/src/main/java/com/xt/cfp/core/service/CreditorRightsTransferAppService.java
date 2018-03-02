package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.pojo.CreditorRightsDealDetail;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.ext.CreditorRightTransferApplyVO;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.TimeInterval;

public interface CreditorRightsTransferAppService {


	/**
	 * 根据申请债权id查询申请记录
	 * @param creditorRightsId
	 */
	List<CreditorRightsTransferApplication> getTransferApplyByApplyCreditorRightsId(Long creditorRightsId);

	/**
	 * 获取当前债权有效的转让申请
	 * @param creditorRightsId
	 * @return
	 */
	CreditorRightsTransferApplication getEffectiveTransferApplyByCreditorRightsId(Long creditorRightsId);

	/**
	 * 根据转让申请id查询债权交易明细
	 * @param creditorRightsApplyId
	 * @return
	 */
	List<CreditorRightsDealDetail> getCreditorRightsDealDetailByTransferApplyId(Long creditorRightsApplyId,CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus creditorRightsTransferDetailStatus);

	/**
	 * 记录交易明细
	 * @param creditorRightsDealDetail
	 */
	void addCreditorRightsDealDetail(CreditorRightsDealDetail creditorRightsDealDetail);
	/**
	 * 记录交易明细
	 * @param creditorRightsId
	 * @param lendOrder
	 */
	void addCreditorRightsDealDetail(CreditorRightsTransferApplication crta, LendOrder lendOrder);

	/**
	 * 新建债权转让申请纪录
	 * @param creditorRightsTransferApplication
     */
	void addCreditorRightsTransferApplication(CreditorRightsTransferApplication creditorRightsTransferApplication);
	/**
	 * 根据多个条件获取一条转让纪录
	 * @param params
	 * @return
	 */
	CreditorRightsTransferApplication getCreditorRightsTransferApplication(Map<String,Object> params);
	
	void update(CreditorRightsTransferApplication creditorRightsTransferApplication);
	void update(CreditorRightsDealDetail creditorRightsDealDetail);

	/**
	 * 获取待匹配的债权申请列表
	 * @return
	 */
	List<CreditorRightsTransferApplication> getCreditorRightsTransferApplicationForMatch(Long loanProductId);

	/**
	 * 获得转让申请
	 * @param lendOrderId
	 * @return
	 */
	CreditorRightsTransferApplication getTransferApplicationByLendOrderId(Long lendOrderId);

	/**
	 *  获取该订单对应的转让明细
	 * @param lendOrderId
	 * @return
	 */
	CreditorRightsDealDetail getCreditorRightsDetailByLendOrderId(Long lendOrderId);
	/**
	 * 获取剩余可购买的债权余额
	 * @param creditorRightsId
	 * @return
	 */
	BigDecimal getRemainingRightsPrice(Long creditorRightsApplyId);
	/**
	 * 根据id查询
	 * @param creditorRightsApplyId
	 * @return
	 */
	CreditorRightsTransferApplication findById(long creditorRightsApplyId);


	/**
	 * 获得当前借款申请对应转让中的债权
	 * @param loanApplicationId
	 */
	List<CreditorRightsTransferApplication> getEffectiveTransferApplyByApplyByLoanApplicationId(Long loanApplicationId);

	/**
	 * 撤销转让申请
	 * @param creditorRightsTransferApplication
	 * @param undoType 值为1时表示手动撤销,为2时表示到期撤销
     */
	void undoCreditorRightsTransferApplication(CreditorRightsTransferApplication creditorRightsTransferApplication, int undoType);

	/**
	 * 根据 转让申请类型, 申请转让时间和转让申请状态 获取转让申请数据
	 * @param type
	 * @param applyTime
	 * @param status
     * @return
     */
	List<CreditorRightsTransferApplication> getByTypeAndApplyTimeAndStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppType type, TimeInterval applyTime,
															   CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus... status);

	/**
	 * 根据ID获取转让申请
	 * @param applyCrId
	 * @return
	 */
	CreditorRightsTransferApplication getAndLockedCreditorRightsTransferAppById(Long creditorRightsApplyId, boolean isLock);

	/**
	 * 根据条件查询CreditorRightsDealDetail
	 * @param paramsApply
	 * @return
	 */
	CreditorRightsDealDetail getCreditorRightsDealDetailByParam(Map<String, Object> paramsApply);
	
	/**
	 * 获取债权转让申请列表
	 * @param pageSize
	 * @param pageNo
	 * @param params
	 * @return
	 */
	public Pagination<CreditorRightTransferApplyVO> getTransferApplyList(int pageSize, int pageNo, Map<String, Object> params);
	
}
