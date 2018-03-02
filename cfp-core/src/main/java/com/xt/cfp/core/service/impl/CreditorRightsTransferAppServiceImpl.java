package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.PayConstants;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CreditorRightsDealDetail;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.PayOrderDetail;
import com.xt.cfp.core.pojo.RateLendOrder;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.Voucher;
import com.xt.cfp.core.pojo.ext.CreditorRightTransferApplyVO;
import com.xt.cfp.core.pojo.ext.RepaymentPlanVO;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CreditorRightsTransferAppService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.RateLendOrderService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.UserAccountOperateService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TimeInterval;

@Service
public class CreditorRightsTransferAppServiceImpl implements CreditorRightsTransferAppService {
	
	@Autowired
    private LendOrderService lendOrderService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private LendOrderBidDetailService lendOrderBidDetailService;
	@Autowired
	private PayService payService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private UserAccountOperateService userAccountOperateService;
	@Autowired
	private CreditorRightsService creditorRightsService;
	@Autowired
	private RateLendOrderService rateLendOrderService;
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Override
	public List<CreditorRightsTransferApplication> getTransferApplyByApplyCreditorRightsId(Long creditorRightsId) {
		return myBatisDao.getList("CREDITORRIGHT_TRANSFER_APPLY.getTransferApplyByApplyCreditorRightsId",creditorRightsId);
	}

	@Override
	public CreditorRightsTransferApplication getEffectiveTransferApplyByCreditorRightsId(Long creditorRightsId) {
		return myBatisDao.get("CREDITORRIGHT_TRANSFER_APPLY.getEffectiveTransferApplyByCreditorRightsId",creditorRightsId);
	}

	@Override
	public List<CreditorRightsDealDetail> getCreditorRightsDealDetailByTransferApplyId(Long creditorRightsApplyId,CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus creditorRightsTransferDetailStatus) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (creditorRightsTransferDetailStatus!=null)
			param.put("states", creditorRightsTransferDetailStatus.getValue());
		param.put("creditorRightsApplyId", creditorRightsApplyId);
		return myBatisDao.getList("CREDITORRIGHT_DEAL_DETAIL.getCreditorRightsDealDetailByTransferApplyId",param);
	}

	@Override
	@Transactional
	public void addCreditorRightsDealDetail(CreditorRightsDealDetail creditorRightsDealDetail) {
		creditorRightsDealDetail.setBuyDate(new Date());
		myBatisDao.insert("CREDITORRIGHT_DEAL_DETAIL.insertSelective", creditorRightsDealDetail);
	}

	@Override
	@Transactional
	public void addCreditorRightsDealDetail(CreditorRightsTransferApplication crta, LendOrder lendOrder) {
		CreditorRightsDealDetail creditorRightsDealDetail = new CreditorRightsDealDetail();
		creditorRightsDealDetail.setCreditorRightsApplyId(crta.getCreditorRightsApplyId());
		creditorRightsDealDetail.setLendOrderId(lendOrder.getLendOrderId());
		creditorRightsDealDetail.setBuyBalance(lendOrder.getBuyBalance());
		creditorRightsDealDetail.setStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus.BUYING.getValue());
		// creditorRightsDealDetail.setBuyWorth(lendOrder.getBuyBalance().multiply(crta.getWhenWorth()).divide(crta.getApplyPrice(), 2));// 购买的价格＊债权价值／申请总价格
		BigDecimal buyMoney = lendOrder.getBuyBalance().multiply(crta.getWhenWorth()).divide(crta.getApplyPrice(), 2, BigDecimal.ROUND_HALF_UP);
		creditorRightsDealDetail.setBuyWorth(buyMoney);// 购买的价格＊债权价值／申请总价格
		creditorRightsDealDetail.setCarryUserId(lendOrder.getLendUserId());
		addCreditorRightsDealDetail(creditorRightsDealDetail);
	}

	@Override
	@Transactional
	public void addCreditorRightsTransferApplication(CreditorRightsTransferApplication creditorRightsTransferApplication) {
		myBatisDao.insert("CREDITORRIGHT_TRANSFER_APPLY.insertSelective", creditorRightsTransferApplication);
	}

	@Override
	public CreditorRightsTransferApplication getCreditorRightsTransferApplication(
			Map<String, Object> params) {
		return myBatisDao.get("CREDITORRIGHT_TRANSFER_APPLY.getCreditorRightsTransferApplication", params);
	}

	@Override
	public void update(
			CreditorRightsTransferApplication creditorRightsTransferApplication) {
		myBatisDao.update("CREDITORRIGHT_TRANSFER_APPLY.updateByPrimaryKeySelective", creditorRightsTransferApplication);
	}

	@Override
	public void update(CreditorRightsDealDetail creditorRightsDealDetail) {
		myBatisDao.update("CREDITORRIGHT_DEAL_DETAIL.updateByPrimaryKeySelective", creditorRightsDealDetail);
	}

	@Override
	public List<CreditorRightsTransferApplication> getCreditorRightsTransferApplicationForMatch(Long loanProductId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("busStatus", CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.TRANSFERRING.getValue());
		param.put("loanProductId", loanProductId);
		return myBatisDao.getList("CREDITORRIGHT_TRANSFER_APPLY.getCreditorRightsTransferApplicationForMatch",param);
	}

	@Override
	public CreditorRightsTransferApplication getTransferApplicationByLendOrderId(Long lendOrderId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("lendOrderId", lendOrderId);
		return myBatisDao.get("CREDITORRIGHT_TRANSFER_APPLY.getTransferApplicationByLendOrderId", param);
	}

	@Override
	public CreditorRightsDealDetail getCreditorRightsDetailByLendOrderId(Long lendOrderId) {
		return myBatisDao.get("CREDITORRIGHT_DEAL_DETAIL.getCreditorRightsDetailByLendOrderId", lendOrderId);
	}
	@Override
	public BigDecimal getRemainingRightsPrice(Long creditorRightsApplyId){
		return myBatisDao.get("CREDITORRIGHT_DEAL_DETAIL.getRemainingRightsPrice", creditorRightsApplyId);
	}

	@Override
	public CreditorRightsTransferApplication findById(long creditorRightsApplyId) {
		return myBatisDao.get("CREDITORRIGHT_TRANSFER_APPLY.selectByPrimaryKey", creditorRightsApplyId);
	}

	@Override
	public List<CreditorRightsTransferApplication> getEffectiveTransferApplyByApplyByLoanApplicationId(Long loanApplicationId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("loanApplicationId", loanApplicationId);
		return myBatisDao.getList("CREDITORRIGHT_TRANSFER_APPLY.getEffectiveTransferApplyByApplyByLoanApplicationId", param);
	}

	@Override
	@Transactional
	public void undoCreditorRightsTransferApplication(CreditorRightsTransferApplication creditorRightsTransferApplication, int undoType) {
		if (undoType != 1 && undoType != 2)
			throw new SystemException(ValidationErrorCode.ERROR_PARAM_ILLEGAL).set("undoType可接受的值", "1, 2").set("undoType实际值", undoType);

		CreditorRightsTransferApplication param = new CreditorRightsTransferApplication();
		param.setCreditorRightsApplyId(creditorRightsTransferApplication.getCreditorRightsApplyId());
		if (undoType == 1)
			param.setBusStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.CANCEL.getValue());
		if (undoType == 2)
			param.setBusStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.OVERDUE.getValue());
		myBatisDao.update("CREDITORRIGHT_TRANSFER_APPLY.updateByPrimaryKeySelective", param);
		CreditorRights creditorRights =creditorRightsService.findById(creditorRightsTransferApplication.getApplyCrId(), false);
		creditorRights.setCreditorRightsId(creditorRightsTransferApplication.getApplyCrId());
		creditorRights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char());
		creditorRights.setTurnState(CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char());
		myBatisDao.update("CREDITORRIGHTS.updateByPrimaryKeySelective", creditorRights);
		
		// 更新债权转让交易明细状态
		processCreditorRightsOrderForFailLoan(creditorRightsTransferApplication);
	}
	
	private void processCreditorRightsOrderForFailLoan(CreditorRightsTransferApplication creditorRightsTransferApplication) {
		AccountValueChangedQueue queue = new AccountValueChangedQueue();
        Date now = new Date();
		
		List<CreditorRightsDealDetail> details = getCreditorRightsDealDetailByTransferApplyId(creditorRightsTransferApplication.getCreditorRightsApplyId(), CreditorRightsStateEnum.CreditorRightsTransferDetailStatus.SUCCESS);
		for (CreditorRightsDealDetail detail : details) {
			LendOrder lendOrder = lendOrderService.findAndLockById(detail.getLendOrderId());
			if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
				Map<String, Object> lendOrderMap = new HashMap<String, Object>();
				lendOrder.setOrderState(LendOrderConstants.RightsOrderStatusEnum.REVOCATION.getValue());
				lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
				lendOrderMap.put("orderState", lendOrder.getOrderState());
				lendOrderService.update(lendOrderMap);
				
				// 订单无效时继承的投标奖励置为无效
				RateLendOrder rateLendOrder = rateLendOrderService.findByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderTypeEnum.AWARD.getValue(),RateLendOrderStatusEnum.VALID.getValue());
				if (null != rateLendOrder) {
					rateLendOrder.setStatus(RateLendOrderStatusEnum.UN_VALID.getValue());
					rateLendOrderService.updateRateLendOrder(rateLendOrder);
				}
		
				// 解锁现金流
				UserAccount cashAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
		
				// 处理所有出借订单使用的财富券
				List<PayOrderDetail> paymentOrderDetailList = payService.getPaymentOrderDetail(lendOrder.getLendOrderId());
				BigDecimal voucherPayValue = BigDecimal.ZERO;
				for (PayOrderDetail payOrderDetail : paymentOrderDetailList) {
					if (PayConstants.AmountType.VOUCHERS.getValue().equals(payOrderDetail.getAmountType())) {
						// 此订单支付明细包含财富券
						List<Voucher> voucherList = voucherService
								.getVoucherList(payOrderDetail.getDetailId(), VoucherConstants.UsageScenario.WHOLE,
										VoucherConstants.UsageScenario.FINANCE, VoucherConstants.UsageScenario.LOAN);
						// 财富券解冻
						Long[] voucherIdList = new Long[voucherList.size()];
						for (int i = 0; i < voucherList.size(); i++) {
							// 记录返还财富券列表
							Voucher voucher = voucherList.get(i);
							if (!VoucherConstants.VoucherStatus.FREEZE.getValue().equals(voucher.getStatus())) {
								voucherIdList = null;
								break;
							}
							voucherIdList[i] = voucher.getVoucherId();
							voucherPayValue = voucherPayValue.add(voucher.getVoucherValue());
						}
						// 财富券返还
						voucherService.backVoucher(payOrderDetail.getDetailId(), "因债权转让撤销已返还", voucherIdList);
					}
				}
				
				List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.BIDING);
				for (LendOrderBidDetail lendOrderBidDetail : lendOrderBidDetails) {
					AccountValueChanged changed = new AccountValueChanged(cashAccount.getAccId(), 
																		  lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue), 
																		  lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue),
																		  AccountConstants.AccountOperateEnum.UNFREEZE.getValue(),
																		  AccountConstants.BusinessTypeEnum.FEESTYPE_TURNRIGHTS_UNDO.getValue(),
																		  AccountConstants.AccountChangedTypeEnum.LEND.getValue(), 
																		  AccountConstants.VisiableEnum.DISPLAY.getValue(),
																		  cashAccount.getAccId(), 
																		  AccountConstants.OwnerTypeEnum.LOAN.getValue(), 
																		  lendOrderBidDetail.getLoanApplicationId(), 
																		  now,
																		  StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.CREDITOR_TURNOUT_UNDO_OUT,lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue)), 
																		  true);
					queue.addAccountValueChanged(changed);
			
					// 更新投标明细的状态
					lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.BIDFAILURE.value2Char());
					lendOrderBidDetailService.updateStatus(lendOrderBidDetail.getDetailId(), LendOrderBidStatusEnum.OUT_TIME.value2Char(), 0l);
				}
			}
		}
		userAccountOperateService.execute(queue);
	}

	@Override
	public List<CreditorRightsTransferApplication> getByTypeAndApplyTimeAndStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppType type, TimeInterval applyTime, CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus... status) {
		Map params = new HashMap();
		params.put("type", type);
		params.put("applyTime", applyTime);
		params.put("status", status);
		return myBatisDao.getList("CREDITORRIGHT_TRANSFER_APPLY.getByTypeAndApplyTimeAndStatus", params);
	}

	@Override
	public CreditorRightsTransferApplication getAndLockedCreditorRightsTransferAppById(Long creditorRightsApplyId, boolean isLock) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("creditorRightsApplyId", creditorRightsApplyId);
		if (isLock)
			param.put("isLock", isLock);
		return myBatisDao.get("CREDITORRIGHT_TRANSFER_APPLY.getAndLockedCreditorRightsTransferAppById", param);
	}

	@Override
	public CreditorRightsDealDetail getCreditorRightsDealDetailByParam(Map<String, Object> paramsApply) {
		return myBatisDao.get("CREDITORRIGHT_DEAL_DETAIL.getCreditorRightsDealDetailByParam", paramsApply);
	}
	
	@Override
    public Pagination<CreditorRightTransferApplyVO> getTransferApplyList(int pageSize, int pageNo, Map<String, Object> params) {
        Pagination<CreditorRightTransferApplyVO> re = new Pagination<CreditorRightTransferApplyVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("getTransferApplyListByPage", params);
        List<CreditorRightTransferApplyVO> creditorRightTransferApplyVOList = this.myBatisDao.getListForPaging("getTransferApplyListByPage", params, pageNo, pageSize);

        for (CreditorRightTransferApplyVO transferApplyVO : creditorRightTransferApplyVOList) {
        	transferApplyVO.setDiscount(transferApplyVO.getApplyPrice().divide(transferApplyVO.getWhenWorth(),2,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal("100")));
        	transferApplyVO.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(transferApplyVO.getRightsState()).getDesc());
        	transferApplyVO.setTurnState(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.getCreditorRightsTransferAppStatusByValue(transferApplyVO.getTurnState()).getDesc());
        	
        	RepaymentPlanVO planVO = repaymentPlanService.getRepaymentPlanByLoanApplicationId(transferApplyVO.getLoanApplicationId());
        	if(null != planVO){
        		transferApplyVO.setApplyEndTime(planVO.getNextRepaymentDay());
            	transferApplyVO.setPeriodsNumber(planVO.getNowSectionCode()+ "/" +planVO.getSumSectionCode());
        	}
		}
        
        re.setTotal(totalCount);
        re.setRows(creditorRightTransferApplyVOList);
        return re;
    }
}
