package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.BidErrorCode;
import com.xt.cfp.core.Exception.code.ext.RateErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.constants.RateEnum.RateUsageHisStateEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.pojo.ext.phonesell.LendOrderVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.util.*;
import jodd.util.NameValue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;


@Service
public class LendOrderServiceImpl implements LendOrderService {

    private static Logger logger = Logger.getLogger(LendOrderServiceImpl.class);

    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private OrderResourceService orderResourceService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    @Autowired
    private CommiProfitService commiProfitService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private RateLendOrderService rateLendOrderService;
    @Autowired
    private RateUserService rateUserService;
    @Autowired
    private RateUsageHistoryService rateUsageHistoryService;
    @Autowired
    private LendLoanBindingService lendLoanBindingService;
    
    @Property(name = "daysForStartCalInterest")
    private Long daysForStartCalInterest;

    @Override
    public Pagination<LendOrderExtProduct> getFinancialPlanList(int pageSize,
                                                                int pageNo,
                                                                String searchFinancialName,
                                                                String searchPeriods,
                                                                String searchLeanUserName,
                                                                String searchT,
                                                                String searchBeginTime,
                                                                String searchEndTime,
                                                                String searchState,
                                                                Long userId) {
        Pagination<LendOrderExtProduct> re = new Pagination<LendOrderExtProduct>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchFinancialName", searchFinancialName);
        params.put("searchPeriods", searchPeriods);
        params.put("searchLeanUserName", searchLeanUserName);
        params.put("searchT", searchT);
        params.put("searchBeginTime", searchBeginTime);
        params.put("searchEndTime", searchEndTime);
        params.put("searchState", searchState);
        params.put("userId", userId);

        int totalCount = this.myBatisDao.count("getFinancialPlanListByPage", params);
        List<LendOrderExtProduct> lendOrderInfo = this.myBatisDao.getListForPaging("getFinancialPlanListByPage", params, pageNo, pageSize);

        for (LendOrderExtProduct lendOrderExtProduct : lendOrderInfo) {
            ValidationUtil.checkRequiredPara(
                    new NameValue<String, Object>("buyBalance", lendOrderExtProduct.getBuyBalance()),
                    new NameValue<String, Object>("profitRate", lendOrderExtProduct.getProfitRate()),
                    //new NameValue<String, Object>("timeLimitType", lendOrderExtProduct.getTimeLimitType()),
                    new NameValue<String, Object>("senderId", lendOrderExtProduct.getTimeLimit()));

            lendOrderExtProduct.setExpectProfit(InterestCalculation.getExpectedInteresting(lendOrderExtProduct.getBuyBalance(), lendOrderExtProduct.getProfitRate(), (lendOrderExtProduct.getTimeLimitType() == null ? 0 : lendOrderExtProduct.getTimeLimitType().toCharArray()[0]), lendOrderExtProduct.getTimeLimit()));
            lendOrderExtProduct.setTotalPayMent(lendOrderExtProduct.getBuyBalance().add(lendOrderExtProduct.getExpectProfit()));

        }
        re.setTotal(totalCount);
        re.setRows(lendOrderInfo);
        return re;
    }

    @Override
    public LendOrder findById(long lendOrderId) {
        return myBatisDao.get("LENDORDER.selectByPrimaryKey", lendOrderId);
    }

    public void insert(LendOrder lendOrder) {
        myBatisDao.insert("LENDORDER.insert", lendOrder);
    }

    @Override
    public LendOrderExtProduct findFinancialPlanById(Long lendOrderId) {
        if (lendOrderId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("lendOrderId", lendOrderId);
        LendOrderExtProduct lendOrderExtProduct = myBatisDao.get("LENDORDER.findFinancialPlanById", lendOrderId);
        if(lendOrderExtProduct == null)
            throw new SystemException(SystemErrorCode.SESSION_NOT_EXISIT).set("lendOrderId", lendOrderId);
        ValidationUtil.checkRequiredPara(
                new NameValue<String, Object>("buyBalance", lendOrderExtProduct.getBuyBalance()),
                new NameValue<String, Object>("profitRate", lendOrderExtProduct.getProfitRate()),
                new NameValue<String, Object>("timeLimitType", lendOrderExtProduct.getTimeLimitType()),
                new NameValue<String, Object>("senderId", lendOrderExtProduct.getTimeLimit()));
        // 查询某条省心计划，在投资金预期收益 
        BigDecimal expectProfit = this.getFinancialWaitInterestByLendOrderId(lendOrderId);
        lendOrderExtProduct.setExpectProfit(expectProfit);
        lendOrderExtProduct.setTotalPayMent(lendOrderExtProduct.getBuyBalance().add(lendOrderExtProduct.getExpectProfit()));
        return lendOrderExtProduct;
    }

    @Override
    public Pagination<CreditorRightsExtVo> findCreditorRightsByDetailList(int pageSize, int pageNo,
                                                                          Long lendOrderId) {
        if (lendOrderId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("lendOrderId", lendOrderId);
        Pagination<CreditorRightsExtVo> re = new Pagination<CreditorRightsExtVo>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("findCreditorRightsByDetailList", lendOrderId);
        List<CreditorRightsExtVo> creditorRightsInfo = this.myBatisDao.getListForPaging("findCreditorRightsByDetailList", lendOrderId, pageNo, pageSize);
        for (CreditorRightsExtVo creditorRightsExtVo : creditorRightsInfo) {
            ValidationUtil.checkRequiredPara(
                    new NameValue<String, Object>("buyPrice", creditorRightsExtVo.getBuyBalance()),
                    new NameValue<String, Object>("annualRate", creditorRightsExtVo.getAnnualRate()),
                    new NameValue<String, Object>("dueTimeType", creditorRightsExtVo.getDueTimeType()),
                    new NameValue<String, Object>("dueTime", creditorRightsExtVo.getDueTime()));
            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(creditorRightsExtVo.getLendOrderId(), creditorRightsExtVo.getLendUserId());
            creditorRightsExtVo.setExpectProfit(exceptProfit);
            creditorRightsExtVo.setNewWaitTotalpayMent(creditorRightsExtVo.getShouldBalance().subtract(creditorRightsExtVo.getFactBalance()));
        }

        re.setTotal(totalCount);
        re.setRows(creditorRightsInfo);
        return re;
    }

    @Override
    public List<LendOrder> findHaveBalanceOrder(Map<String, Object> lendMap) {
        return myBatisDao.getList("LENDORDER.findHaveBalanceOrder", lendMap);
    }

    @Override
    public BigDecimal getTotalLendAmount(Long userId, Long loanApplicationId) {
        //判断参数是否为null
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("loanApplicationId", loanApplicationId);
        //todo 状态字段未填入
        return myBatisDao.get("LENDORDER.getTotalLendAmount", params);
    }

    @Override
    public BigDecimal getTotalLendAmount(Long userId) {
        //判断参数是否为null
        return getTotalLendAmount(userId,"");
    }

    @Override
    public BigDecimal getTotalLendAmount(Long userId,String month) {

        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(month)){
            params.put("month", month);
        }

        return myBatisDao.get("LENDORDER.getTotalLendAmountByUserId", params);
    }

    @Override
    public void update(Map map) {
        myBatisDao.update("LENDORDER.updateMap", map);
    }

    @Override
    @Transactional
    public LendOrder addLendOrder(Long userId, String lendOrderName, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct,String resource, String productType,String isUseVoucher,String profitReturnConfig) {
        //新建购买订单
        LendOrder lendOrder = new LendOrder();
        lendOrder.setBuyBalance(amount);
        lendOrder.setBuyTime(now);
        lendOrder.setCurrentProfit2(BigDecimal.ZERO);
        lendOrder.setCurrentProfit(BigDecimal.ZERO);
        lendOrder.setClosingDate(lendProduct.getClosingDate());
        lendOrder.setClosingType(lendProduct.getClosingType());
        if (lendProduct.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
            lendOrder.setCustomerAccountId(userAccountService.initUserAccount(userId, AccountConstants.AccountTypeEnum.ORDER_ACCOUNT).getAccId());
            lendOrder.setForLendBalance(amount);
            lendOrder.setProfitReturnConfig(profitReturnConfig);
        } else {
            lendOrder.setCustomerAccountId(userAccountService.getCashAccount(userId).getAccId());
            lendOrder.setForLendBalance(BigDecimal.ZERO);
        }
        lendOrder.setDisplayState("1");
        lendOrder.setLendUserId(userId);
        lendOrder.setLendProductPublishId(productPublishId);
        lendOrder.setLendOrderName(lendOrderName);
        lendOrder.setLendProductId(lendProduct.getLendProductId());
        //todo 确认订单编号的生成规则
        String prefix = lendProduct.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()) ? "F" : "L";
        lendOrder.setOrderCode(prefix + DateUtil.getFormattedDateUtil(now, "yyyyMMddHHmmssSSS"));
        lendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue());
        lendOrder.setProductType(productType);
        lendOrder.setProfitRate(lendProduct.getProfitRate());
        lendOrder.setRenewal(lendProduct.getRenewal());
        lendOrder.setRenewalCycleType(lendProduct.getRenewalCycleType());
        lendOrder.setRenewalType(lendProduct.getRenewalType());
        lendOrder.setTheReturnMethod("0");
        lendOrder.setTimeLimit(lendProduct.getTimeLimit());
        lendOrder.setTimeLimitType(lendProduct.getTimeLimitType());
        lendOrder.setToInterestPoint(lendProduct.getToInterestPoint());
        //省心计划增加对财富券的使用标记
        lendOrder.setIsUseVoucher(isUseVoucher);
        myBatisDao.insert("LENDORDER.insert", lendOrder);
        //记录订单来源
        OrderResource or=orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(lendOrder.getLendOrderId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_LEND.getValue()), or.getResourceId(),lendOrder.getBuyTime());
        return lendOrder;
    }

    @Override
    @Transactional
    public LendOrder addFinanceOrder(Long userId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct,String resource,String isUseVoucher,String profitReturnConfig) {
        LendProductPublish productPublish = this.lendProductService.getLendProductPublishByPublishId(productPublishId);
        LendOrder lendOrder =  this.addLendOrder(userId, productPublish.getPublishName(), productPublishId, amount, now, lendProduct,resource,LendProductTypeEnum.FINANCING.getValue(),isUseVoucher, profitReturnConfig);
        
        LendOrderBidDetail lendOrderBidDetail = new LendOrderBidDetail();
        lendOrderBidDetail.setBuyBalance(lendOrder.getBuyBalance());
        lendOrderBidDetail.setBuyDate(lendOrder.getBuyTime());
        lendOrderBidDetail.setLendOrderId(lendOrder.getLendOrderId());
        lendOrderBidDetail.setLoanApplicationId(null);
        lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.WAITING_PAY.getValue().toCharArray()[0]);
        lendOrderBidDetailService.insert(lendOrderBidDetail);

        return lendOrder;
    }

    @Override
    @Transactional
    public LendOrder addLoanOrder(Long userId, Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct,String resource) {
        LoanPublish loanPublish = loanApplicationService.getLoanPublishByAppId(loanApplicationId);
        LendOrder lendOrder = this.addLendOrder(userId, loanPublish.getLoanTitle(), productPublishId, amount, now, lendProduct, resource, LendProductTypeEnum.RIGHTING.getValue(),null,null);
        
        LendOrderBidDetail lendOrderBidDetail = new LendOrderBidDetail();
        lendOrderBidDetail.setBuyBalance(lendOrder.getBuyBalance());
        lendOrderBidDetail.setBuyDate(lendOrder.getBuyTime());
        lendOrderBidDetail.setLendOrderId(lendOrder.getLendOrderId());
        lendOrderBidDetail.setLoanApplicationId(loanApplicationId);
        lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.WAITING_PAY.getValue().toCharArray()[0]);
        lendOrderBidDetailService.insert(lendOrderBidDetail);

        return lendOrder;
    }

    @Override
    @Transactional
    public LendOrder addCreditorRightsOrder(Long userId, Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct, String resource, CreditorRightsTransferApplication crta) {
        LoanPublish loanPublish = loanApplicationService.getLoanPublishByAppId(loanApplicationId);
		// 新建购买订单
        LendOrder lendOrder = new LendOrder();
        lendOrder.setBuyBalance(amount);
        lendOrder.setBuyTime(now);
        lendOrder.setCurrentProfit2(BigDecimal.ZERO);
        lendOrder.setCurrentProfit(BigDecimal.ZERO);
        lendOrder.setClosingDate(lendProduct.getClosingDate());
        lendOrder.setClosingType(lendProduct.getClosingType());
        if (lendProduct.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
            lendOrder.setCustomerAccountId(userAccountService.initUserAccount(userId, AccountConstants.AccountTypeEnum.ORDER_ACCOUNT).getAccId());
            lendOrder.setForLendBalance(amount);
        } else {
            lendOrder.setCustomerAccountId(userAccountService.getCashAccount(userId).getAccId());
            lendOrder.setForLendBalance(BigDecimal.ZERO);
        }
        lendOrder.setDisplayState("1");
        lendOrder.setLendUserId(userId);
        lendOrder.setLendProductPublishId(productPublishId);
        lendOrder.setLendOrderName(loanPublish.getLoanTitle());
        lendOrder.setLendProductId(lendProduct.getLendProductId());
        //todo 确认订单编号的生成规则
        String prefix = lendProduct.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()) ? "F" : "L";
        lendOrder.setOrderCode(prefix + DateUtil.getFormattedDateUtil(now, "yyyyMMddHHmmssSSS"));
        lendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue());
        lendOrder.setProductType(LendProductTypeEnum.CREDITOR_RIGHTS.getValue());
        lendOrder.setProfitRate(lendProduct.getProfitRate());
        lendOrder.setRenewal(lendProduct.getRenewal());
        lendOrder.setRenewalCycleType(lendProduct.getRenewalCycleType());
        lendOrder.setRenewalType(lendProduct.getRenewalType());
        lendOrder.setTheReturnMethod("0");
        lendOrder.setTimeLimit(crta.getTimeLimit());
        lendOrder.setTimeLimitType(lendProduct.getTimeLimitType());
        lendOrder.setToInterestPoint(lendProduct.getToInterestPoint());
        myBatisDao.insert("LENDORDER.insert", lendOrder);
        //记录订单来源
        OrderResource or=orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(lendOrder.getLendOrderId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_LEND.getValue()), or.getResourceId(),lendOrder.getBuyTime());
        
        //转让订单明细
        LendOrderBidDetail lendOrderBidDetail = new LendOrderBidDetail();
        lendOrderBidDetail.setBuyBalance(lendOrder.getBuyBalance());
        lendOrderBidDetail.setBuyDate(lendOrder.getBuyTime());
        lendOrderBidDetail.setLendOrderId(lendOrder.getLendOrderId());
        lendOrderBidDetail.setLoanApplicationId(loanApplicationId);
        lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.WAITING_PAY.getValue().toCharArray()[0]);
        lendOrderBidDetailService.insert(lendOrderBidDetail);
        //转让申请明细
        creditorRightsTransferAppService.addCreditorRightsDealDetail(crta,lendOrder);
        return lendOrder;
    }
    
    private LendOrder getAndLockById(Long orderId) {
        return this.myBatisDao.get("LENDORDER.getAndLockById", orderId);
    }
    /**
     * 转让债权(金额已被冻结)
     * @throws Exception
     */
    @Override
    @Transactional
    public void turnCreditorRights(Long lendOrderId, Date date, PayResult payResult) {

        LendOrder lendOrder = this.getAndLockById(lendOrderId);
        lendOrder.setPayTime(date);

        //校验-如果订单已经被处理过
        if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
            payResult.setProcessResult(false);
            payResult.setFailDesc("订单已处理过");
            return;
        }

        //是否满足转让条件
        //锁定转让申请
		CreditorRightsTransferApplication rightApp = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(lendOrderId);
		CreditorRightsTransferApplication applyRecord = creditorRightsTransferAppService.getAndLockedCreditorRightsTransferAppById(rightApp.getCreditorRightsApplyId() , true);
        List<CreditorRightsDealDetail> creditorRightsDealDetails = creditorRightsTransferAppService.getCreditorRightsDealDetailByTransferApplyId(applyRecord.getCreditorRightsApplyId(),CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus.SUCCESS);
        BigDecimal sumTurnBalance = BigDecimal.ZERO;
        if (creditorRightsDealDetails!=null){
            for (CreditorRightsDealDetail dealDetail:creditorRightsDealDetails){
                sumTurnBalance = sumTurnBalance.add(dealDetail.getBuyBalance());
            }
            sumTurnBalance = sumTurnBalance.add(lendOrder.getBuyBalance());
            if (sumTurnBalance.compareTo(applyRecord.getApplyPrice())>0){
                payResult.setProcessResult(false);
                payResult.setFailDesc("已超过个人最大可购买债权");
                return;
            }
        }


        //更新订单状态
        lendOrder.setOrderState(LendOrderConstants.RightsOrderStatusEnum.PAID.getValue());
        myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);

        //更新订单明细状态
        LendOrderBidDetail lendOrderBidDetail = this.lendOrderBidDetailService.findByLendOrderId(lendOrderId, LendOrderBidStatusEnum.WAITING_PAY).get(0);
        lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.BIDING.value2Char());
        myBatisDao.update("LENDORDERBIDDETAIL.update", lendOrderBidDetail);

        //更新转让明细状态
        CreditorRightsDealDetail creditorRightsDetail = creditorRightsTransferAppService.getCreditorRightsDetailByLendOrderId(lendOrderId);
        creditorRightsDetail.setStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus.SUCCESS.getValue());
        myBatisDao.update("CREDITORRIGHT_DEAL_DETAIL.updateByPrimaryKeySelective", creditorRightsDetail);

        //冻结财富券
        PayOrder payOrder = new PayOrder();
        payOrder.setPayId(payResult.getPayId());
        voucherService.frozeVoucher(payOrder);
        //转让前，//更新支付单为成功状态
        PayOrder updatePayOrder = new PayOrder();
        updatePayOrder.setPayId(payResult.getPayId());
        updatePayOrder.setStatus(PayConstants.OrderStatus.SUCCESS.getValue());
        updatePayOrder.setProcessStatus(PayConstants.ProcessStatus.SUCCESS.getValue());
		myBatisDao.update("PAY_ORDER.updateByPrimaryKeySelective", updatePayOrder);
		
		/*---------------------------债权转让购买成功后，继承投标奖励-----------------------------*/
		// 发标奖励insert
		LoanPublish lp = loanPublishService.findById(lendOrderBidDetail.getLoanApplicationId());
		if (!StringUtils.isNull(lp.getAwardPoint()) && lp.getAwardRate() != null && lp.getAwardRate().compareTo(BigDecimal.ZERO) != 0) {
			rateLendOrderService.createRateLendOrder(null, lendOrder.getLendOrderId(), lendOrderBidDetail.getLoanApplicationId(), RateLendOrderTypeEnum.AWARD, lp.getAwardPoint(), lp.getAwardRate(), RateLendOrderStatusEnum.VALID);
		}
		
        try {
            //检查债权是否可转让
            if (applyRecord.getApplyPrice().compareTo(sumTurnBalance) == 0){
                creditorRightsService.turnCreditor(applyRecord);
                // 未支付订单改为已过期
				List<LendOrderBidDetail> orderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(lendOrderBidDetail.getLoanApplicationId(), LendOrderBidStatusEnum.WAITING_PAY);
				for (LendOrderBidDetail orderBidDetail : orderBidDetails) {
					orderBidDetail.setStatus(LendOrderBidStatusEnum.OUT_TIME.value2Char());
					lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.OUT_TIME.value2Char(), 0l);
					LendOrder waitingPayLendOrder = findById(orderBidDetail.getLendOrderId());
					waitingPayLendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.OUT_TIME.getValue());
					update(waitingPayLendOrder);
				}
                
                //债权转出时，更新佣金记录
                CreditorRights creditorRight = creditorRightsService.findById(rightApp.getApplyCrId(), false);
                LendOrder lendOrderOld = findById(creditorRight.getLendOrderId());
                List<CommitProfitVO>  commitProfitList = commiProfitService.getCommiProfitByLendOrderId(lendOrderOld.getLendOrderId(), lendOrderOld.getLendUserId());
                try {
                	logger.info("【债权转让】债权转出，更新佣金统计记录，佣金数据长度：" + commitProfitList.size());
                	for(CommitProfitVO commi : commitProfitList){
                    	CommiProfit com = new CommiProfit();
                    	com.setComiProId(commi.getComiProId());
                    	com.setShoulProfit(commi.getFactProfit());
                    	commiProfitService.updateCommiProfit(com);
                    	logger.info("【债权转让】更新佣金统计记录，推荐人：" + com.getUserId() + "，佣金统计ID：" + com.getComiProId() + "，佣金应还金额和实还金额：" + commi.getFactProfit() );
                    }
				} catch (Exception e) {
					logger.error("【债权转让】更新佣金统计记录异常：" , e);
				}
            }
        } catch (Exception e) {
            logger.error(LogUtils.createSimpleLog("转让处理失败", "在处理转让时出现未知异常"), e);
            throw new SystemException(SystemErrorCode.SYSTEM_ERROR_CODE).set("转让处理失败", "在处理转让时出现未知异常");
        }
        //todo 生成合同
        payResult.setProcessResult(true);
    }
    @Override
    @Transactional
    public void confirmFinanceOrderHasPaid(Long orderId, Date now, PayResult payResult) {
        LendOrder lendOrder = this.getAndLockById(orderId);
        lendOrder.setPayTime(now);
        UserAccount userAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
        LendProductPublish productPublish = lendProductService.getAndLockPublishById(lendOrder.getLendProductPublishId());
        List<LendOrderBidDetail> lobdLists = lendOrderBidDetailService.getByLendOrderId(orderId, LendOrderBidStatusEnum.WAITING_PAY);

        boolean canContinue = true;
        //如果订单状态已经不是未支付的状态
        if (canContinue && lendOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.UNPAY)) {
            payResult.setProcessResult(false);
            payResult.setFailDesc("订单已被处理过");
            canContinue = false;
        }

        //如果省心计划已不在发售期
        if (canContinue && productPublish.getPublishState() != LendProductPublishStateEnum.SELLING.value2Char()) {
            lendOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.OUT_TIME.getValue());
            myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);
            payResult.setProcessResult(false);
            payResult.setFailDesc("省心计划已不在销售期");
            canContinue = false;
        }

        //判断出借产品的可投资额，如果大于订单金额，算作购买成功，否则认为购买失败
        if (productPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC && canContinue && productPublish.getPublishBalance().subtract(productPublish.getSoldBalance()).compareTo(lendOrder.getBuyBalance()) < 0) {
            lendOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.OUT_TIME.getValue());
            myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);
            payResult.setProcessResult(false);
            payResult.setFailDesc("省心计划的可购买余额已不足");
            canContinue = false;
        }

        //如果发现不应该继续，就返回
        if (!canContinue)
            return;

        AccountValueChangedQueue avcq = new AccountValueChangedQueue();
        //财富券变现
        BigDecimal voucherAmount = loanApplicationService.changeVoucherToAmount(lendOrder, avcq);
        //转账
        AccountValueChanged outChanged = new AccountValueChanged(userAccount.getAccId(), lendOrder.getBuyBalance().subtract(voucherAmount), lendOrder.getBuyBalance().subtract(voucherAmount),
                AccountConstants.AccountOperateEnum.PAY_FROZEN.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BUYFINANCE_TRANSFER.getValue(),
                AccountConstants.AccountChangedTypeEnum.LEND.getValue(), VisiableEnum.DISPLAY.getValue(),
                lendOrder.getLendOrderId(), AccountConstants.OwnerTypeEnum.USER.getValue(), lendOrder.getLendUserId(), now,
                StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_TRANSFER_OUT, lendOrder.getBuyBalance()), true);

        AccountValueChanged inChanged = new AccountValueChanged(lendOrder.getCustomerAccountId(), lendOrder.getBuyBalance(), lendOrder.getBuyBalance(),
                AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BUYFINANCE_TRANSFER.getValue(),
                AccountConstants.AccountChangedTypeEnum.LEND.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(),
                lendOrder.getLendOrderId(), AccountConstants.OwnerTypeEnum.ORDER.getValue(), lendOrder.getLendOrderId(), now,
                StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_TRANSFER_IN, lendOrder.getBuyBalance()), true);
        avcq.addAccountValueChanged(outChanged);
        avcq.addAccountValueChanged(inChanged);
        userAccountOperateService.execute(avcq);

        //更新订单状态
        lendOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.HASPAID.getValue());
        myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);
        
		// 更新订单投标明细状态
		for (LendOrderBidDetail lobd : lobdLists) {
			lobd.setStatus(LendOrderBidStatusEnum.BIDSUCCESS.value2Char());
			myBatisDao.update("LENDORDERBIDDETAIL.update", lobd);
		}

        //更新省心计划发布记录（已售金额、状态）
        productPublish.setSoldBalance(productPublish.getSoldBalance().add(lendOrder.getBuyBalance()));
        if (productPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {
            if (productPublish.getPublishBalance().compareTo(productPublish.getSoldBalance()) == 0)
                productPublish.setPublishState(LendProductPublishStateEnum.SOLDOUT.value2Char());
        }
        this.myBatisDao.update("LEND_PRODUCT_PUBLISH.updateByPrimaryKeySelective", productPublish);
        payResult.setProcessResult(true);
    }

    @Override
    public BigDecimal getTotalProfit(Long userId) {
        //判断参数是否为null
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        return myBatisDao.get("LENDORDER.getAllProfit", userId);
    }

    @Override
    public BigDecimal getTotalHoldFinancePlan(Long userId) {
        //判断参数是否为null
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        return myBatisDao.get("LENDORDER.getTotalHoldFinancePlan", params);
    }

    @Override
    public List<LendOrderExtProduct> getCycleBuySituation(Long lendProductPublishId) {
        return myBatisDao.getList("LENDORDER.getCycleBuySituation", lendProductPublishId);
    }

    @Override
    public List<LendProductPublish> getCycleBuySituationHistory(LendProductPublish lpp) {
        return myBatisDao.getList("LEND_PRODUCT_PUBLISH.findAllBy", lpp);
    }

    @Override
    public LendOrder getLendOrderByPayId(Long payId, boolean assertExist) {
        LendOrder lendOrder = myBatisDao.get("LENDORDER.getLendOrderByPayId", payId);
        if (assertExist)
            if (lendOrder == null)
                throw new SystemException(BidErrorCode.LENDORDER_NOT_FOUND).set("payId", payId);

        return lendOrder;
    }
    
    @Override
    public List<LendOrder> getLendOrderRecent(Long userId, int rows) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("rows", rows);
        return myBatisDao.getList("LENDORDER.getLendOrderRecent", params);
    }

    @Override
    public Pagination<LendOrder> getLendOrderPaging(int pageNum, int pageSize, LendOrder lendOrder, Map<String, Object> customParams) {
        Pagination<LendOrder> re = new Pagination<LendOrder>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lendOrder", lendOrder);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getLendOrderPaging", params);
        List<LendOrder> users = this.myBatisDao.getListForPaging("getLendOrderPaging", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }

    @Override
    public String genertOrderCode(String productType) {
        try {
            return productType + DateUtil.getNowLongTime();
        } catch (Exception e) {
            return productType + DateUtil.getDateLong(new Date());
        }
    }

    @Override
    public String genertOrderName(LendOrder lendOrder) {
        LendProductTypeEnum productTypeEnum = LendProductTypeEnum.getByValue(lendOrder.getProductType());
        return productTypeEnum.getDesc() + "_" + BigDecimalUtil.down(lendOrder.getBuyBalance(), 2);
    }

    @Override
    public LendOrder newRightsLendOrder(LendProduct rightLendProduct, LendProductPublish lendPublish, LoanApplication loanApplication,
                                        UserAccount lendAccount, long customerCardId) {
        BigDecimal zeroBigdecimal = BigDecimal.valueOf(0);
        Date now = new Date();
        LendOrder lendOrder = new LendOrder();
        //出借订单的出借账户为债权原始债权人
        lendOrder.setCustomerAccountId(lendAccount.getAccId());
        lendOrder.setLendProductId(rightLendProduct.getLendProductId());
        lendOrder.setLendProductPublishId(lendPublish.getLendProductPublishId());
        lendOrder.setInCardId(customerCardId);
        lendOrder.setCurrentProfit(zeroBigdecimal);
        lendOrder.setCurrentProfit2(zeroBigdecimal);
        lendOrder.setOutCardId(customerCardId);
        lendOrder.setLoanApplicationId(loanApplication.getLoanApplicationId());
        lendOrder.setProductType(rightLendProduct.getProductType());
        lendOrder.setLendUserId(lendAccount.getUserId());
        lendOrder.setBuyBalance(loanApplication.getConfirmBalance());
        lendOrder.setOrderCode(this.genertOrderCode(lendOrder.getProductType()));
        lendOrder.setLendOrderName(this.genertOrderName(lendOrder));

        lendOrder.setBuyTime(now);
        lendOrder.setAgreementStartDate(now);
        lendOrder.setForLendBalance(lendOrder.getBuyBalance());
        lendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue());
        lendOrder.setDisplayState(DisplayEnum.HIDDEN.getValue());
        lendOrder.setRecordTime(now);
        lendOrder.setProfitRate(loanApplication.getAnnualRate());
        lendOrder.setClosingType(rightLendProduct.getClosingType());
        lendOrder.setClosingDate(rightLendProduct.getClosingDate());
        lendOrder.setTimeLimit(rightLendProduct.getTimeLimit());
        lendOrder.setToInterestPoint(rightLendProduct.getToInterestPoint());
        lendOrder.setRenewalCycleType(rightLendProduct.getRenewalCycleType());
        lendOrder.setRenewal(rightLendProduct.getRenewal());
        lendOrder.setRenewalType(rightLendProduct.getRenewalType());
//        lendOrder.setTheReturnMethod();//到期返还方式 暂默认为都返还至账户
        this.insert(lendOrder);
        //todoed 新增出借订单明细
        LendOrderBidDetail orderBidDetail = new LendOrderBidDetail();
        orderBidDetail.setLendOrderId(lendOrder.getLendOrderId());
        orderBidDetail.setLoanApplicationId(loanApplication.getLoanApplicationId());
        orderBidDetail.setBuyBalance(lendOrder.getBuyBalance());
        orderBidDetail.setBuyDate(now);
        orderBidDetail.setStatus(LendOrderBidStatusEnum.BIDSUCCESS.value2Char());
        lendOrderBidDetailService.insert(orderBidDetail);
        return lendOrder;

    }

    @Override
    public Pagination<LendOrderExtProduct> getLenderInformationById(int pageSize,
                                                                    int pageNo, long loanApplicationId) {
        Pagination<LendOrderExtProduct> re = new Pagination<LendOrderExtProduct>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("getLenderInformationPaging", loanApplicationId);
        List<LendOrderExtProduct> lenderInfo = this.myBatisDao.getListForPaging("getLenderInformationPaging", loanApplicationId, pageNo, pageSize);
        re.setTotal(totalCount);
        re.setRows(lenderInfo);
        return re;
    }


    @Override
    public Pagination<LendOrderExtProduct> getAllMyFinanceList(int pageSize,
    		int pageNo, Long userId, String queryState, String queryType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("bondState", queryState);
        params.put("timeLimit", queryType);
        
        Pagination<LendOrderExtProduct> re = new Pagination<LendOrderExtProduct>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        
        int totalCount = this.myBatisDao.count("getAllMyFinanceListPaging", params);
        Date date = new Date();
        List<LendOrderExtProduct> lenderInfo = this.myBatisDao.getListForPaging("getAllMyFinanceListPaging", params, pageNo, pageSize);
        for (LendOrderExtProduct lendOrderExtProduct : lenderInfo) {
            ValidationUtil.checkRequiredPara(
                    new NameValue<String, Object>("buyBalance", lendOrderExtProduct.getBuyBalance()),
                    new NameValue<String, Object>("profitRate", lendOrderExtProduct.getProfitRate()),
                    new NameValue<String, Object>("timeLimitType", lendOrderExtProduct.getTimeLimitType()),
                    new NameValue<String, Object>("senderId", lendOrderExtProduct.getTimeLimit()));
            lendOrderExtProduct.setExpectProfit(InterestCalculation.getExpectedInteresting(lendOrderExtProduct.getBuyBalance(), lendOrderExtProduct.getProfitRate(), lendOrderExtProduct.getTimeLimitType().toCharArray()[0], lendOrderExtProduct.getTimeLimit()));
            //lendOrderExtProduct.setTotalPayMent(lendOrderExtProduct.getBuyBalance().add(lendOrderExtProduct.getExpectProfit()));
            
            // 如果已获收益为null，则替换为0
            if(null == lendOrderExtProduct.getCurrentProfit2()){
            	lendOrderExtProduct.setCurrentProfit2(BigDecimal.ZERO);
            }
            lendOrderExtProduct.setNewDate(date);
            // 根据出借产品ID，查询已经匹配的借款产品期限时长范围(如：3-6)
            String dueTimeScope = lendLoanBindingService.getLoanProductDueTimeByLendProductId(lendOrderExtProduct.getLendProductId());
            lendOrderExtProduct.setDueTimeScope(dueTimeScope);//标的期限范围
//            lendOrderExtProduct.set
        }
        re.setTotal(totalCount);
        re.setRows(lenderInfo);
        re.setUrl(queryState);
//        re.setMaxPage(new Date().get);
        return re;
    }


    @Override
    public BigDecimal getNewestProfit(long lendOrderId) {
        LendOrder lendOrder = findById(lendOrderId);
        if (lendOrder == null) {
            return BigDecimal.valueOf(0);
        }
        return lendOrder.getCurrentProfit();
    }

    @Override
    public void updateNewestProfit(long lendOrderId, BigDecimal profit) {
        Map<String, Object> map = new HashMap();
        map.put("lendOrderId", lendOrderId);
        map.put("currentProfit", profit);
        map.put("currentProfit2", BigDecimalUtil.down(profit, 2));
        myBatisDao.update("LENDORDER.updateMap", map);
    }


    @Override
    public BigDecimal getRealChangeInterest(Long lendOrderId) {

        return this.myBatisDao.get("LENDORDER.getRealChangeInterest", lendOrderId);
    }

    @Override
    public Pagination<LendOrderExtProduct> getLendOrderListBy(int pageNum, int pageSize, Map<String, Object> paramMap) {
        Pagination<LendOrderExtProduct> re = new Pagination<LendOrderExtProduct>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("getLendOrderListBy", paramMap);
        List<LendOrderExtProduct> lendOrderExtProduct = this.myBatisDao.getListForPaging("getLendOrderListBy", paramMap, pageNum, pageSize);
        re.setTotal(totalCount);
        re.setRows(lendOrderExtProduct);
        return re;
    }

    @Override
    public void confirmBidLoanOrderHasPaid(Long lendOrderId, Date date, PayResult payResult) {
        LendOrder lendOrder = this.getAndLockById(lendOrderId);
        lendOrder.setPayTime(date);

        //校验-如果订单已经被处理过
        if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
            payResult.setProcessResult(false);
            payResult.setFailDesc("订单已处理过");
            return;
        }

        //校验-如果可购买金额已不够
        LendOrderBidDetail lendOrderBidDetail = this.lendOrderBidDetailService.findByLendOrderId(lendOrderId, LendOrderBidStatusEnum.WAITING_PAY).get(0);
        LoanApplication loanApplication = this.loanApplicationService.findLockById(lendOrderBidDetail.getLoanApplicationId());
        //标的状态验证
        if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplication.getApplicationState())){
            payResult.setProcessResult(false);
            payResult.setFailDesc(BidErrorCode.BID_STATIS_NOT_BIDDING.getDesc());
            return;
        }

        //预热标验证
        LoanPublish lp = loanPublishService.findById(loanApplication.getLoanApplicationId());
        LoanApplicationListVO loanApplicationListVO = new LoanApplicationListVO();
        loanApplicationListVO.setPopenTime(lp.getOpenTime());
        LendOrder pLendOrder = null ;
        if(lendOrder.getLendOrderPId()!=null){
        	pLendOrder = this.findById(lendOrder.getLendOrderPId());
        }
        if (!loanApplicationListVO.isBegin()){
            payResult.setProcessResult(false);
            payResult.setFailDesc(BidErrorCode.BID_STATIS_NOT_SELLING.getDesc());
            return;
        }

        //【投标规则验证：发布规则（0仅手动、1省心优先、2仅省心）, coulumn publishRule】
        if(pLendOrder == null || !pLendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())){
        	//非省心计划子标（手动投标）
        	if(lp.getPublishRule().equals(PublishRule.ONLY_AUTOMATIC.getValue())){
        		payResult.setProcessResult(false);
                payResult.setFailDesc(BidErrorCode.NOT_RIGHT_PUBLISH_RULE_AUTO.getDesc());
                return;
        	}
        }else{
        	//省心计划子标
        	if(lp.getPublishRule().equals(PublishRule.ONLY_MANUAL.getValue())){
        		payResult.setProcessResult(false);
        		payResult.setFailDesc(BidErrorCode.NOT_RIGHT_PUBLISH_RULE_MANUAL.getDesc());
        		return;
        	}
        }
        lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.BIDING.value2Char());
        BigDecimal bidSum = lendOrderBidDetailService.sumCreByLoanApp(loanApplication.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING);
        BigDecimal availSum = loanApplication.getConfirmBalance().subtract(bidSum);
        if (lendOrder.getBuyBalance().compareTo(availSum) > 0) {
            lendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.OUT_TIME.getValue());
            myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);
            lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.BIDFAILURE.value2Char());
            payResult.setProcessResult(false);
            payResult.setFailDesc(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH.getDesc());
            return;
        }

        //校验-如果个人限额足够
        BigDecimal totalLendAmountPerson = getTotalLendAmount(lendOrder.getLendUserId(), loanApplication.getLoanApplicationId());
        if (totalLendAmountPerson != null) {
            BigDecimal maxBuyBalance = loanApplication.getMaxBuyBalance()!=null?loanApplication.getMaxBuyBalance():loanApplication.getConfirmBalance();
            totalLendAmountPerson = totalLendAmountPerson.add(lendOrder.getBuyBalance());
            if (totalLendAmountPerson.compareTo(maxBuyBalance) > 0) {
                lendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.OUT_TIME.getValue());
                myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);
                lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.BIDFAILURE.value2Char());
                payResult.setProcessResult(false);
                payResult.setFailDesc("已超过个人最大可购买金额");
                return;
            }
        }

        //更新订单状态
        lendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.PAID.getValue());
        myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);

        //更新订单明细状态
        myBatisDao.update("LENDORDERBIDDETAIL.update", lendOrderBidDetail);
        
        //投标创建佣金收益统计（一笔最多三条）【开始】
        if(LendProductTypeEnum.RIGHTING.getValue().equals(lendOrder.getProductType()) && pLendOrder == null){//如果出借产品类型是投标类,附：理财订单的子订单投标不计算佣金
        	try {
				commiProfitService.createCommiProfit(lendOrder.getLendOrderId());
			} catch (Exception e) {
				logger.error(LogUtils.createSimpleLog("【佣金收益统计】创建失败", "在创建佣金收益统计时出现未知异常"), e);
			}
        }
        //投标创建佣金收益统计（一笔最多三条）【结束】
        
		/*---------------------------八月活动start-----------------------------*/
		// 发标奖励insert
		if (!StringUtils.isNull(lp.getAwardPoint()) && lp.getAwardRate() != null && lp.getAwardRate().compareTo(BigDecimal.ZERO) != 0) {
			rateLendOrderService.createRateLendOrder(null, lendOrder.getLendOrderId(), loanApplication.getLoanApplicationId(), RateLendOrderTypeEnum.AWARD, lp.getAwardPoint(), lp.getAwardRate(), RateLendOrderStatusEnum.VALID);
		}
		
		// 8月周年活动insert
		rateLendOrderService.createActivity(lendOrder.getLendUserId(), lendOrder, loanApplication.getLoanApplicationId());

		// 加息券更改为有效
		RateLendOrder rateLendOrder = rateLendOrderService.findByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderTypeEnum.RATE_COUPON.getValue(), RateLendOrderStatusEnum.UN_VALID.getValue());
		if (null != rateLendOrder) {
			RateUser lockRateUser = rateUserService.findByRateUserId(rateLendOrder.getRateUserId(), true);
			if (lockRateUser.getStatus().equals(RateUserStatusEnum.USEUP.getValue()) || lockRateUser.getStatus().equals(RateUserStatusEnum.TIMEOUT.getValue()) || lockRateUser.getSurplusTimes() == 0) {
				logger.error("订单ID:" + lendOrder.getLendOrderId() + "加息券ID:" + lockRateUser.getRateUserId() + RateErrorCode.RATE_USEUP.getDesc());
				throw new SystemException(RateErrorCode.RATE_USEUP);
			}
			rateUserService.subtractRateUser(lockRateUser);
			if (lockRateUser.getStatus().equals(RateUserStatusEnum.UNUSED.getValue())) {
				lockRateUser.setStatus(RateUserStatusEnum.USING.getValue());
			}
			if (lockRateUser.getSurplusTimes() == 0) {
				lockRateUser.setStatus(RateUserStatusEnum.USEUP.getValue());
			}
			rateUserService.updateRateUser(lockRateUser);
			rateLendOrder.setStatus(RateLendOrderStatusEnum.VALID.getValue());
			rateLendOrderService.updateRateLendOrder(rateLendOrder);
			rateUsageHistoryService.insertRateUsageHis(lockRateUser, lendOrder.getLendUserId(), lendOrder.getLendOrderId(), loanApplication.getLoanApplicationId(), RateUsageHisStateEnum.VALID);
		}
		/*---------------------------八月活动end-----------------------------*/
        
        
        //如果满标，将借款申请数据置为满标状态
        try {
            loanApplicationService.notice2FullBid(loanApplication, date);
        } catch (Exception e) {
            logger.error(LogUtils.createSimpleLog("满标处理失败", "在处理满标操作时出现未知异常"), e);
        }
        //todo 生成合同
        payResult.setProcessResult(true);
    }

    /**
     * 获得累计出借总额
     *
     * @param userId 也可以指定某个用户的购买金额
     */
    @Override
    public BigDecimal getAllBuyBalance(Long userId) {
        return myBatisDao.get("LENDORDER.getAllBuyBalance", userId);
    }

    @Override
    public BigDecimal getTotalAward(Long userId) {
        return myBatisDao.get("LENDORDER.getTotalAward", userId);
    }

    /**
     * 获得累计收益
     *
     * @param userId 也可以指定某个用户的收益
     */
    @Override
    public BigDecimal getAllProfit(Long userId) {
        return myBatisDao.get("LENDORDER.getAllProfit", userId);
    }

    @Override
    @Transactional
    public void updateLendOrderStatus(Long lendOrderId, LendOrderConstants.FinanceOrderStatusEnum financeOrderStatusEnum) {
        LendOrder lendOrder = new LendOrder();
        lendOrder.setLendOrderId(lendOrderId);
        lendOrder.setOrderState(financeOrderStatusEnum.getValue());

        this.myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);
    }

    @Override
    public LendOrder findAndLockById(Long lendOrderId) {
        return myBatisDao.get("LENDORDER.findAndLockById", lendOrderId);
    }

    @Override
    @Transactional
    public void refreshLendOrderReceive() {
        List<CreditorRights> creditorRightses = creditorRightsService.findAll(ChannelTypeEnum.ONLINE);
        for (CreditorRights creditorRights:creditorRightses) {
            refreshLendOrderReceive(creditorRights);
        }

    }

    @Override
    public BigDecimal getTotalLendAmountByLoanApplicationId(Long loanApplicationId) {
        //判断参数是否为null
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        BigDecimal bidSum = lendOrderBidDetailService.sumCreByLoanApp(loanApplicationId, LendOrderBidStatusEnum.BIDING);
        return bidSum;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void startCalculateFinanceOrderInterest(Long financeOrderId, Date date) {
        LendOrder financeOrder = this.getAndLockById(financeOrderId);

        //校验-订单状态是否在理财中
        if (!financeOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue())) {
            logger.info(LogUtils.createLogWithParams("计息失败", DescTemplate.Log.FinanceTemplate.FAILED_START_CALCULATE_FINANCE_INTEREST, financeOrderId, "订单状态已不是理财中"));
            return;
        }

        //校验-订单是否已经可以计息
        boolean canStart = false;
        if (!canStart && DateUtil.daysBetweenDates(date, financeOrder.getBuyTime()) >= daysForStartCalInterest)
            canStart = true;

        if (!canStart && financeOrder.getBuyBalance().subtract(financeOrder.getForLendBalance()).compareTo(BigDecimal.ZERO) == 0)
            canStart = true;

        if (!canStart) {
            logger.info(LogUtils.createLogWithParams("计息失败", DescTemplate.Log.FinanceTemplate.FAILED_START_CALCULATE_FINANCE_INTEREST, financeOrderId, "还未满足开始计息的条件，购买理财未满" + daysForStartCalInterest + "天，或还未将全部本金出借"));
            return;
        }

        //更新省心计划相关属性
        financeOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue());
        financeOrder.setAgreementStartDate(date);
//        FinanceCalculateUtils.calAndSetAgreementEndDate(financeOrder);
//        FinanceCalculateUtils.calAndSetClosingOverDate(financeOrder);
        this.update(financeOrder);

        //生成省心计划的回款明细
        genarateReceiveDetail(financeOrder);
    }

    private void genarateReceiveDetail(LendOrder financeOrder) {
        String toInterestPoint = financeOrder.getToInterestPoint();
        Date agreementStartDate = financeOrder.getAgreementStartDate();
        Date agreementEndDate = financeOrder.getAgreementEndDate();
        int days = DateUtil.daysBetweenDates(agreementEndDate, agreementStartDate);

//        if (toInterestPoint.equals(LendProductInterestReturnType.DAY.getValue())) {
//            for (int i = 1; i <= days; i++) {
//                LendOrderReceive lendOrderReceive = new LendOrderReceive();
//                lendOrderReceive.setLendOrderId(financeOrder.getLendOrderId());
//                lendOrderReceive.setSectionCode(i);
//                lendOrderReceive.setShouldCapital();
//            }
//        }
    }

    @Override
    public void update(LendOrder lendOrder) {
        this.myBatisDao.update("LENDORDER.updateByPrimaryKeySelective", lendOrder);
    }

    public void refreshLendOrderReceive(CreditorRights creditorRights) {

        try {
            LendOrder lendOrder = this.findById(creditorRights.getLendOrderId());

            List<LendOrderReceive> oldReceives = lendOrderReceiveService.getByLendOrderId(lendOrder.getLendOrderId());
            for (LendOrderReceive lendOrderReceive:oldReceives) {
                lendOrderReceiveService.delete(lendOrderReceive.getReceiveId());
            }

            LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(creditorRights.getLoanApplicationId());
            LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
            List<RepaymentPlan> repaymentPlans = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplication.getLoanApplicationId());
            if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
                //                    int periods = getPeriods(lendOrder.getTimeLimitType().charAt(0), lendOrder.getTimeLimit(), lendOrder.getToInterestPoint().charAt(0));
                BigDecimal sumCapital = new BigDecimal("0");
                BigDecimal sumInterest = new BigDecimal("0");
                BigDecimal sumBalance = new BigDecimal("0");
                Map<Integer, Map<String, BigDecimal>> lendMap = InterestCalculation.getCalitalAndInterest(lendOrder.getBuyBalance(), lendOrder.getProfitRate(),
                        loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                BigDecimal theLendAllInterest = InterestCalculation.getAllInterest(lendOrder.getBuyBalance(), lendOrder.getProfitRate(),
                        loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                Iterator<Integer> iterator = lendMap.keySet().iterator();
                int i = 1;
                while (iterator.hasNext()) {
                    int sec = iterator.next();
                    Map<String, BigDecimal> theMap = lendMap.get(sec);

                    LendOrderReceive lendOrderReceiveDetail = new LendOrderReceive();
                    lendOrderReceiveDetail.setLendOrderId(lendOrder.getLendOrderId());
                    lendOrderReceiveDetail.setSectionCode(i);
                    lendOrderReceiveDetail.setReceiveState(LendOrderReceiveStateEnum.UNRECEIVE.value2Char());
                    Date receiveDate = null;

                    for (RepaymentPlan repaymentPlan : repaymentPlans) {
                        if (repaymentPlan.getSectionCode() == sec && repaymentPlan.getChannelType() == ChannelTypeEnum.ONLINE.value2Char()) {
                            receiveDate = repaymentPlan.getRepaymentDay();
                            break;
                        }
                    }

                    BigDecimal balance = new BigDecimal("0");
                    if (i == lendMap.size()) {
                        balance = lendOrder.getBuyBalance().add(theLendAllInterest).subtract(sumBalance);

                        lendOrderReceiveDetail.setShouldCapital(lendOrder.getBuyBalance().subtract(sumCapital));
                        lendOrderReceiveDetail.setShouldInterest(theLendAllInterest.subtract(sumInterest));

                    } else {
                        balance = theMap.get("balance");

                        lendOrderReceiveDetail.setShouldCapital(theMap.get("calital"));
                        lendOrderReceiveDetail.setShouldInterest(balance.subtract(lendOrderReceiveDetail.getShouldCapital()));

                        sumCapital = sumCapital.add(lendOrderReceiveDetail.getShouldCapital());
                        sumInterest = sumInterest.add(lendOrderReceiveDetail.getShouldInterest());
                        sumBalance = sumBalance.add(balance);
                    }
                    balance = BigDecimalUtil.down(balance,2);
                    lendOrderReceiveDetail.setShouldCapital2(BigDecimalUtil.down(lendOrderReceiveDetail.getShouldCapital(), 2));
                    lendOrderReceiveDetail.setShouldInterest2(balance.subtract(lendOrderReceiveDetail.getShouldCapital2()));
                    lendOrderReceiveDetail.setReceiveDate(receiveDate);

                    lendOrderReceiveService.insert(lendOrderReceiveDetail);

                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 第二部分（上） 省心计划情况
     */
    public LendOrderDetailVO getOrderDetail2ALC(Long lendOrderId){
    	return myBatisDao.get("LENDORDER.getOrderDetail2ALC", lendOrderId);
    }
    
    /**
     * 第二部分（下） 省心计划情况
     */
    public LendOrderDetailVO getOrderDetail2BLC(Long lendOrderId){
    	return myBatisDao.get("LENDORDER.getOrderDetail2BLC", lendOrderId);
    }
    
    /**
     * 第二部分（上） 债权情况
     */
    public LendOrderDetailVO getOrderDetail2AZQ(Long lendOrderId){
    	return myBatisDao.get("LENDORDER.getOrderDetail2AZQ", lendOrderId);
    }
    
    /**
     * 第二部分（下） 债权情况
     */
    public LendOrderDetailVO getOrderDetail2BZQ(Long lendOrderId){
    	return myBatisDao.get("LENDORDER.getOrderDetail2BZQ", lendOrderId);
    }
    
    /**
     * 第三部分（上）
     */
    public LendOrderDetailVO getOrderDetail3A(Long lendOrderId){
    	return myBatisDao.get("LENDORDER.getOrderDetail3A", lendOrderId);
    }
    
    /**
     * 第三部分（下）
     */
    public List<LendOrderDetailVO> getOrderDetail3B(Long lendOrderId){
    	return myBatisDao.getList("LENDORDER.getOrderDetail3B", lendOrderId);
    }

    @Override
    public void exportLenderExcel(HttpServletResponse response, Map<String, Object> customParams) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customParams", customParams);
        List<LinkedHashMap<String, Object>> list = myBatisDao.getList("LENDORDER.exportLenderExcel",params);
        ResponseUtil.sendExcel(response, list, "出借人数据报表");
    }

    @Override
    public boolean isFirstLend(Long userId, LendOrder lendOrder) {
        BigDecimal totalAmount =  myBatisDao.get("LENDORDER.isFirstLend", userId);
        if (totalAmount.compareTo(lendOrder.getBuyBalance())==0){
            return true;
        }
        return false;
    }

    @Override
	public Pagination<LendOrderVO> getPhonesellOrder(int pageNo, int pageSize, Map<String, Object> customParams) {
		Pagination<LendOrderVO> re = new Pagination<LendOrderVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        if(customParams.size()>0){
        	Set<String> keys=customParams.keySet();
        	for (String key : keys) {
        		params.put(key, customParams.get(key));
			}
        }
        int totalCount = this.myBatisDao.count("getPhonesellOrders", params);
        List<LendOrderVO> uah = this.myBatisDao.getListForPaging("getPhonesellOrders", params, pageNo, pageSize);
        List<String> nums = getPhonesellOrdersAccount(customParams);
        for (LendOrderVO vo : uah) {
        	vo.setNums(nums);
        	Integer status=Integer.parseInt(vo.getStatus());
			switch(status){
				case 0:
					vo.setState("未支付");
					break;
				case 1:
					if(vo.getProductType().equals("2")){
						vo.setState("理财中");
					}else{
						vo.setState("还款中");
					}
					break;
				case 2:
					vo.setState("已结清");
					break;
				case 3:
					vo.setState("已过期");
					break;
				case 4:
					vo.setState("已撤销");
					break;
				case 5:
					if(vo.getProductType().equals("2")){
						vo.setState("匹配中");
					}else{
						vo.setState("已支付");
					}
					break;
				case 6:
					vo.setState("退出中");
					break;
				case 7:
					vo.setState("流标");
					break;
			}
		}
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}
	
	@Override
	public List<String> getPhonesellOrdersAccount(Map<String, Object> customParams) {
		 Map<String, Object> params = new HashMap<String, Object>();
         if(customParams.size()>0){
         	Set<String> keys=customParams.keySet();
         	for (String key : keys) {
         		params.put(key, customParams.get(key));
 			}
         }
    	List<String> nums = this.myBatisDao.getList("getPhonesellOrdersAccount", params);
		return nums;
	}
	
	@Override
	public FeesItem getFeesByLendProId(long lendProId) {
		Map map=new HashMap();
		map.put("lendProId", lendProId);
		return myBatisDao.get("LENDORDER.findFeesByLoanId",map);
	}

    @Override
    public void scanFinanceOrderAndDoQuit() {
        List<LendOrder> financeOrders = this.myBatisDao.getList("getNeedToQuitFinanceOrder", new Date());

        if (financeOrders != null) {
            for (LendOrder lendOrder : financeOrders) {
                try {
                	financePlanProcessModule.exitPlan(lendOrder.getLendOrderId());
				} catch (Exception e) {
					logger.error("省心计划退出异常，异常原因：",e);
				}
            }
        }
    }

	@Override
	public LendOrder getLendOrderByPid(Long lendOrderPid) {
		return this.myBatisDao.get("LENDORDER.getLendOrderByPid", lendOrderPid);
	}
	
	/**
     * 获取省心计划订单中退出中状态并且所有债权都是已结清的
     * */
	@Override
	public List<LendOrder> findFinanceClearForQuit(){
		return myBatisDao.getList("LENDORDER.findFinanceClearForQuit");
	}
	
	/**
     * 根据订单id获取省心计划退出中的订单
     * */
	@Override
	public List<Long> findQuitFinanceOrderByIds(List<Long> lendOrderIdList){
		Map map = new HashMap();
		map.put("lendOrderIdList", lendOrderIdList);
		return myBatisDao.getList("LENDORDER.findQuitFinanceOrderByIds", map);
	}

	@Override
	public Integer getUserOrderNumByCondition(Map map) {
		return myBatisDao.get("LENDORDER.selectUserOrderNumByCondition", map);
	}

	@Override
	public BigDecimal getUserAllBuyBalanceByCondition(Map map) {
		return myBatisDao.get("LENDORDER.selectUserAllBuyBalanceByCondition", map);
	}

	@Override
	public List<LendOrder> findUnRepayOrdersByFinanceOrder(LendOrder lendOrder) {
		return myBatisDao.getList("LENDORDER.findUnRepayOrdersByFinanceOrder",lendOrder.getLendOrderId());
	}

	@Override
	public List<LendOrder> getFinancialPlanListByUserId(Long userId) {
		return myBatisDao.getList("LENDORDER.getFinancialPlanListByUserId",userId);
	}
	
	@Override
	public BigDecimal getFinancialWaitInterestByUserId(Long userId) {
		return myBatisDao.get("LENDORDER.getFinancialWaitInterestByUserId",userId);
	}
	
	@Override
	public BigDecimal getFinancialWaitInterestByLendOrderId(Long lendOrderId) {
		return myBatisDao.get("LENDORDER.getFinancialWaitInterestByLendOrderId",lendOrderId);
	}

	@Override
	public BigDecimal getAllBuyBalanceByLendOrderPid(Long lendOrderPId) {
		return myBatisDao.get("LENDORDER.getAllBuyBalanceByLendOrderPid",lendOrderPId);
	}

	@Override
	public BigDecimal getAwardBalanceByLendOrderPid(Long lendOrderPId) {
		return myBatisDao.get("LENDORDER.getAwardBalanceByLendOrderPid",lendOrderPId);
	}
	
	@Override
	public BigDecimal getAwardBalanceByLendOrderId(Long lendOrderId) {
		return myBatisDao.get("LENDORDER.getAwardBalanceByLendOrderId",lendOrderId);
	}
	@Override
	public int countMakedLoan(Long userId, String[] querys) {
		Map <String ,Object> map = new HashMap<String,Object>();
		// TODO Auto-generated method stub
		map.put("userId", userId);
		map.put("querys", querys);
		return myBatisDao.get("LENDORDER.selectMakedLoanAlso", map);
	}
	
	/**
	 * 查询全部有效的省心计划（已支付，理财中，授权期到期，已结清）
	 * */
	@Override
	public List<LendOrder> findAllValidFinanceOrder(Map<String,Date> param) {
		return myBatisDao.getList("LENDORDER.findAllValidFinanceOrder",param);
	}
}
