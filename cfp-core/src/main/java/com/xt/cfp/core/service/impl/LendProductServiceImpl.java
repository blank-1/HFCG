package com.xt.cfp.core.service.impl;

import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.PayConstants.AmountType;
import com.xt.cfp.core.constants.PayConstants.BusTypeEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderPointEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.context.ParaContext;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.ApplicationContextUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Renyulin on 14-4-9 下午3:16.
 */
@Service("lendProductService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class LendProductServiceImpl implements LendProductService {
    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private ProductFeesCached productFeesCached;
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private PayService payService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private RateLendOrderService rateLendOrderService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CapitalFlowService capitalFlowService;
    @Autowired
    private UserAccountService userAccountService;


    @Override
    public BigDecimal calculateLendProductFeesItemBalance(LendProductFeesItem lendProductFeesItem, BigDecimal allCalital, BigDecimal allInterest, BigDecimal currentCalital, BigDecimal currentInterest, BigDecimal allProfit, BigDecimal currentProfit) {
        BigDecimal result = BigDecimal.ZERO;
        FeesItem feesItem = lendProductFeesItem.getFeesItem();
        BigDecimal feesRate = new BigDecimal(String.valueOf(feesItem.getFeesRate())).divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING);
        if (feesItem.getRadicesType() == RadiceTypeEnum.PRINCIPAL.value2Int()) {
            result = allCalital.multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.ALLINTEREST.value2Int()) {
            result = allInterest.multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.ALLPROFIT.value2Int()) {
            result = allProfit.multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.ALLPI.value2Int()) {
            result = allCalital.add(allInterest).multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.SUMPROFIT.value2Int()) {
            result = currentProfit.multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.CURRENTINTEREST.value2Int()) {
            result = currentInterest.multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.CURRENTPRINCIPAL.value2Int()) {
            result = currentCalital.multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.CURRENTPI.value2Int()) {
            result = currentCalital.add(currentInterest).multiply(feesRate);
        } else if (feesItem.getRadicesType() == RadiceTypeEnum.TRANSFERPRINCIPAL.value2Int()) {
            result = currentCalital.multiply(feesRate);
        }

        return result;
    }

    /**
     * 新增出借产品，新增出借产品阶段信息，新增出借产品阶段优惠，新增出借产品绑定借款产品 如出借产品的产品类型为债权类，则同时修改借款产品的出借产品ID
     *
     * @param lendProduct
     * @param lendProductFeesItems
     * @param ladderDiscounts
     * @param lendLoanBindings
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public void addLendProduct(LendProduct lendProduct, List<LendProductFeesItem> lendProductFeesItems,
                               List<LendProductLadderDiscount> ladderDiscounts, List<LendLoanBinding> lendLoanBindings) {
        myBatisDao.insert("LEND_PRODUCT.insert", lendProduct);

        Map<Long, LendProductFeesItem> lpFeesItem = new HashMap();
        for (LendProductFeesItem lendProductFeesItem : lendProductFeesItems) {
            lendProductFeesItem.setLendProductId(lendProduct.getLendProductId());
            myBatisDao.insert("LEND_PRODUCT_FEES_ITEM.insert", lendProductFeesItem);
            lpFeesItem.put(lendProductFeesItem.getFeesItemId(), lendProductFeesItem);
        }
        /**
         * 取消阶段优惠
         *
         for (LendProductLadderDiscount lendProductLadderDiscount : ladderDiscounts) {
         lendProductLadderDiscount.setLendProductId(lendProduct.getLendProductId());
         myBatisDao.insert("LEND_PRODUCT_LADDER_DISCOUNT.insert", lendProductLadderDiscount);
         List<LendProductLadderDiscountFees> ladderDiscountFeeses = lendProductLadderDiscount.getLendProductLadderDiscountFeesList();
         for (LendProductLadderDiscountFees discountFees : ladderDiscountFeeses) {
         discountFees.setLpldId(lendProductLadderDiscount.getLpldId());
         discountFees.setLpfiId(lpFeesItem.get(discountFees.getFeesItemId()).getLpfiId());
         myBatisDao.insert("LEND_PRODUCT_LDISCOUNT_FEES.insert", discountFees);
         }
         }
         */
        for (LendLoanBinding lendLoanBinding : lendLoanBindings) {
            lendLoanBinding.setLendProductId(lendProduct.getLendProductId());
            lendLoanBinding.setChargePoint(FeesPointEnum.ATCYCLE.getValue());// 绑定时的收费，只在还款时收
            myBatisDao.insert("LEND_LOAN_BINDING.insert", lendLoanBinding);
			if (lendProduct.getProductType().equals(LendProduct.PRODUCTTYPE_RIGHTS)) {
                LoanProduct loanProduct = loanProductService.findById(lendLoanBinding.getLoanProductId());
                loanProduct.setLendProductId(lendProduct.getLendProductId());
                loanProduct.setLastMdfTime(new Date());
                loanProductService.modifyLoanProduct(loanProduct);
            }
        }
        productFeesCached.resetLendProductFeesItem();
    }

    public Pagination<LendProductVO> findAllByPage(int page, int limit, LendProductVO parameter) {
        Pagination<LendProductVO> pagination = new Pagination<LendProductVO>();
        pagination.setCurrentPage(page);
        pagination.setPageSize(limit);
        List<LendProductVO> lendProducts = myBatisDao.getListForPaging("LEND_PRODUCT.findAll", parameter, pagination.getCurrentPage(),
                pagination.getLimit());
        for (LendProductVO productVO : lendProducts) {
            Map map = new HashMap();
            map.put("lendProductId", productVO.getLendProductId());
            List<LendProductPublish> productPublishs = myBatisDao.getList("LEND_PRODUCT_PUBLISH.findBy", map);
            BigDecimal publishBalance = BigDecimal.ZERO;
            for (LendProductPublish productPublish : productPublishs) {
                if (productPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {// 当前发售金额类型为指定金额
                    publishBalance = publishBalance.add(productPublish.getPublishBalance());
                } else {
                    Map<String, Object> mapparam = new HashMap<String, Object>();
                    mapparam.put("lendProductId", productVO.getLendProductId());
                    List<CreditorRights> creditorRightsLists = myBatisDao.getList("CREDITORRIGHTS.findBy", mapparam);
                    for (CreditorRights creditorRights : creditorRightsLists) {// （债权列表）所有根据出借产品id关联的所有债权
                        publishBalance = publishBalance.add(creditorRights.getRightsWorth());// 债权价值

                    }
                }
            }
            productVO.setSumPublishBalance(publishBalance);
        }
        pagination.setRows(lendProducts);
        pagination.setTotal((Integer) myBatisDao.get("LEND_PRODUCT.countFindAll", parameter));
        return pagination;
    }

    public Pagination<LendProductPublish> findAllPublishByPage(int page, int limit, Map map) {
        Pagination<LendProductPublish> pagination = new Pagination<LendProductPublish>();
        pagination.setCurrentPage(page);
        pagination.setPageSize(limit);
        List<LendProductPublish> publishByPage = myBatisDao.getListForPaging("LEND_PRODUCT_PUBLISH.findAll", map, pagination.getCurrentPage(),
                pagination.getLimit());
        pagination.setRows(publishByPage);
        pagination.setTotal((Integer) myBatisDao.get("LEND_PRODUCT_PUBLISH.countFindAll", map));
        return pagination;
    }

    public LendProduct findById(long lendProductId) {
        return myBatisDao.get("LEND_PRODUCT.findById", lendProductId);
    }

    // 处理出借产品查看详情
    public List<ConstantDefine> findLendProductAndFees(long lendProductId) {
        return myBatisDao.getList("LEND_PRODUCT.findLendProductAndFees", lendProductId);
    }

    public void updateProductState(LendProduct lendProduct) {
        myBatisDao.update("LEND_PRODUCT.updateProductState", lendProduct);
    }

    public List<LendProduct> findTimeLimit(LendProduct lendProduct) {
        return myBatisDao.getList("LEND_PRODUCT.findTimeLimit", lendProduct);
    }

    public List<LendProduct> findProfitRate() {
        return myBatisDao.getList("LEND_PRODUCT.findProfitRate");
    }

    @Transactional
    public void addProductPublish(LendProductPublish lendProductPublish, List<LPPublishChannelDetail> lpPublishChannelDetails) {
        myBatisDao.insert("LEND_PRODUCT_PUBLISH.insert", lendProductPublish);
        for (LPPublishChannelDetail channelDetail : lpPublishChannelDetails) {
            channelDetail.setLendProductPublishId(lendProductPublish.getLendProductPublishId());
            myBatisDao.insert("LEND_PRODUCT_PUBLISH.insertChannelDetail", channelDetail);
        }
    }

    /**
     * 修改出借产品，修改出借产品阶段信息，修改出借产品阶段优惠，修改出借产品绑定借款产品 如出借产品的产品类型为债权类，则同时修改借款产品的出借产品ID
     *
     * @param lendProduct
     * @param lendProductFeesItems
     * @param ladderDiscounts
     * @param lendLoanBindings
     */

    public void updateLendProduct(LendProduct lendProduct, List<LendProductFeesItem> lendProductFeesItems,
                                  List<LendProductLadderDiscount> ladderDiscounts, List<LendLoanBinding> lendLoanBindings) {
        myBatisDao.update("LEND_PRODUCT.update", lendProduct);
        myBatisDao.delete("LEND_PRODUCT_FEES_ITEM.deleteById", lendProduct.getLendProductId());// 删除出借产品相关费用
        Map<Long, LendProductFeesItem> lpFeesItem = new HashMap();
        for (LendProductFeesItem lendProductFeesItem : lendProductFeesItems) {// 添加出借产品费用
            lendProductFeesItem.setLendProductId(lendProduct.getLendProductId());
            myBatisDao.insert("LEND_PRODUCT_FEES_ITEM.insert", lendProductFeesItem);
            lpFeesItem.put(lendProductFeesItem.getFeesItemId(), lendProductFeesItem);
        }
        /**
         *
         * 取消阶梯优惠
         // 更新阶梯优惠
         List<LendProductLadderDiscount> lendProductLadderList = myBatisDao.getList("LEND_PRODUCT_LADDER_DISCOUNT.findByLendProductId",
         lendProduct.getLendProductId());
         for (LendProductLadderDiscount lendProductLadder : lendProductLadderList) {
         myBatisDao.delete("LEND_PRODUCT_LDISCOUNT_FEES.deleteById", lendProductLadder.getLpldId());// 根据要跟新的阶段优惠ID删除每个产品阶段优惠费用，新添加的就不用删除了
         }
         myBatisDao.delete("LEND_PRODUCT_LADDER_DISCOUNT.deleteById", lendProduct.getLendProductId());
         for (LendProductLadderDiscount lendProductLadderDiscount : ladderDiscounts) {
         lendProductLadderDiscount.setLendProductId(lendProduct.getLendProductId());
         myBatisDao.insert("LEND_PRODUCT_LADDER_DISCOUNT.insert", lendProductLadderDiscount);
         List<LendProductLadderDiscountFees> ladderDiscountFeeses = lendProductLadderDiscount.getLendProductLadderDiscountFeesList();
         for (LendProductLadderDiscountFees discountFees : ladderDiscountFeeses) {
         discountFees.setLpldId(lendProductLadderDiscount.getLpldId());
         discountFees.setLpfiId(lpFeesItem.get(discountFees.getFeesItemId()).getLpfiId());// 费用项id
         myBatisDao.insert("LEND_PRODUCT_LDISCOUNT_FEES.insert", discountFees);
         }
         }
         */
        // 更新适用债权
        myBatisDao.delete("LEND_LOAN_BINDING.deleteById", lendProduct.getLendProductId());
        loanProductService.updateByLendProductId(lendProduct.getLendProductId());
        for (LendLoanBinding lendLoanBinding : lendLoanBindings) {
            lendLoanBinding.setLendProductId(lendProduct.getLendProductId());
            lendLoanBinding.setChargePoint(FeesPointEnum.ATCYCLE.getValue());// 绑定时的收费，只在还款时收
            myBatisDao.insert("LEND_LOAN_BINDING.insert", lendLoanBinding);
			if (lendProduct.getProductType().equals(LendProduct.PRODUCTTYPE_RIGHTS)) {
                LoanProduct loanProduct = loanProductService.findById(lendLoanBinding.getLoanProductId());
                loanProduct.setLendProductId(lendProduct.getLendProductId());
                loanProduct.setLastMdfTime(new Date());
                loanProductService.modifyLoanProduct(loanProduct);
            }
        }
        productFeesCached.resetLendProductFeesItem();
    }

    public List<LendProduct> findProductVersionByName(LendProduct lendProduct) {
        return myBatisDao.getList("LEND_PRODUCT.findProductVersionByName", lendProduct);
    }

    public List<LendProductPublish> findLendProductPublishsByProductId(long lendProductId) {
        return myBatisDao.getList("LEND_PRODUCT_PUBLISH.findByLendProductId", lendProductId);
    }

    public int getMaxPublishCodeByLendProductId(long lendProductId) {
        return myBatisDao.get("LEND_PRODUCT_PUBLISH.getMaxPublishCodeByLendProductId", lendProductId);
    }

    public List<LendProductFeesItem> findAllProductFeesItems() {
        return myBatisDao.getList("LEND_PRODUCT_FEES_ITEM.findAll");
    }



    @Override
    public List<LendProductFeesItem> findAllProductFeesItemsByLendOrderId(long lendOrderId) {
        return myBatisDao.getList("LEND_PRODUCT_FEES_ITEM.findAllProductFeesItemsByLendOrderId", lendOrderId);
    }

    @Override
    public List<LendProductLadderDiscountFees> findByLendProductLDFId(long lendProductId) {
        return myBatisDao.getList("LEND_PRODUCT_LDISCOUNT_FEES.findByLendProductId", lendProductId);
    }

//	@Override
//	public List<LendProductPublish> findLendProducPublishBy(Map map) {
//		return myBatisDao.getList("LEND_PRODUCT_PUBLISH.findBy", map);
//	}

    @Override
    public LendProductPublish findCurrentProductPublishByProductId(Long productId) {
        List<LendProductPublish> productPublishs = this.findLendProductPublishsByProductId(productId);
        int maxPublishCode = this.getMaxPublishCodeByLendProductId(productId);
        for (LendProductPublish lendProductPublish : productPublishs) {
            if (lendProductPublish.getPublishCode().equals(maxPublishCode))
                return lendProductPublish;
        }

        return null;
    }

    @Override
    public List<LproductWithBalanceStatus> findFinanceProductListForWeb() {
        return myBatisDao.getList("LEND_PRODUCT.findFinanceListForWeb");
    }

    @Override
    public List<LendProductPublish> findLendProductPublishBy(Map map) {
        return myBatisDao.getList("LEND_PRODUCT_PUBLISH.findBy", map);
    }

    @Override
    public LendProductPublish getLendProductPublishByPublishId(Long publishId) {
        return myBatisDao.get("LEND_PRODUCT_PUBLISH.findById", publishId);
    }

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public PayResult buyFinanceByAccountBalance(Long userId, Long financePublishId, BigDecimal amount, String source,String isUseVoucher,String profitReturnConfig) {
		Date now = new Date();
		Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(AmountType.ACCOUNT.getValue(), amount);
		// 新建省心计划
		PayOrder payOrder = addOrdersForBuyProduct(userId, financePublishId, new ParaContext(), amount, now, source,isUseVoucher,profitReturnConfig, amountDetail);
		// 执行订单支付
		return payService.doPay(payOrder.getPayId(), now);
	}

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public PayResult payFinanceByAccountBalance(Long userId, Long lendOrderId) {
        LendOrder lendOrder = this.lendOrderService.findById(lendOrderId);

        ParaContext paraContext = new ParaContext();
        Date now = new Date();
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(AmountType.ACCOUNT.getValue(), lendOrder.getBuyBalance());

        LendProductServiceImpl bean = ApplicationContextUtil.getBean(LendProductServiceImpl.class);
        //新建支付订单
        PayResult payResult = bean.addPayOrderForBuyProduct(userId, lendOrder, paraContext, now, amountDetail);

        if (payResult.isPayResult()) {
            //执行订单支付
            return payService.doPay(payResult.getPayId(), now);
        }

        return payResult;
    }


    @Override
    @Transactional(propagation = Propagation.NEVER)
    public PayResult payFinanceByAccountBalanceWeb(Long userId, Long lendOrderId,RateUser rateUser,RateProduct rateProduct,String ... voucherIds) {
        LendOrder lendOrder = this.lendOrderService.findById(lendOrderId);

        BigDecimal voucherPayValue = BigDecimal.ZERO;
        List<VoucherVO> vouchers = new ArrayList<VoucherVO>();
        if (null != voucherIds){
            for (String voucherId:voucherIds){
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                vouchers.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }
        }

        ParaContext paraContext = new ParaContext();
        Date now = new Date();
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(AmountType.ACCOUNT.getValue(), lendOrder.getBuyBalance().subtract(voucherPayValue));
        Pair<String, BigDecimal> voucherDetail = new Pair<String, BigDecimal>(AmountType.VOUCHERS.getValue(),voucherPayValue);

        //新建支付订单
        PayResult payResult = addPayOrderForBuyProductWeb(userId, lendOrder, paraContext, now,vouchers,rateUser,rateProduct ,amountDetail, voucherDetail);

        if (payResult.isPayResult()) {
            //执行订单支付
            return payService.doPay(payResult.getPayId(), now);
        }

        return payResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayResult addPayOrderForBuyProductWeb(Long userId, LendOrder lendOrder, ParaContext paraContext, Date now,List<VoucherVO> vouchers,RateUser rateUser,RateProduct rateProduct, Pair<String, BigDecimal>... amountDetails) {
        PayResult payResult = new PayResult(null);
        LendProductPublish productPublish = this.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
        //如果是省心计划，且有购买限额，要对可购买金额进行处理
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                && productPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {
            BigDecimal waitingBuyBalance = productPublish.getPublishBalance().subtract(productPublish.getSoldBalance());
            if (waitingBuyBalance.compareTo(lendOrder.getBuyBalance()) < 0) {
                LendOrder orderLock = lendOrderService.findAndLockById(lendOrder.getLendOrderId());
                if (orderLock.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.UNPAY.getValue())) {
                    lendOrderService.updateLendOrderStatus(lendOrder.getLendOrderId(), LendOrderConstants.FinanceOrderStatusEnum.OUT_TIME);
                    payResult.setPayResult(false);
                    payResult.setLendOrderId(lendOrder.getLendOrderId());
                    payResult.setFailDesc("由于剩余可购买的金额不足，该订单已过期");
                    return payResult;
                }
            }
        }

        //生成支付单并记录订单和支付单的关系
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())){
            busTypeEnum = BusTypeEnum.BUY_FINANCE;
        }
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())){
            busTypeEnum = BusTypeEnum.BID_LOAN;
        }
        if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())){
            busTypeEnum = BusTypeEnum.BUY_RIGHTS;
        }
        PayOrder payOrder = payService.addPayOrder(lendOrder.getBuyBalance(), now, userId, busTypeEnum, amountDetails);

        BigDecimal acount=BigDecimal.ZERO;
        if(null!= vouchers&&vouchers.size()>0){
            for (int i = 0; i < vouchers.size(); i++) {
                acount=acount.add(vouchers.get(i).getAmount());
            }
        }
        Schedule sch=new Schedule();
        sch.setBusinessId(payOrder.getPayId());
        sch.setBusinessType(Integer.parseInt(AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getValue()));
        sch.setDesc("用户ID:"+payOrder.getUserId()+AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getDesc()+payOrder.getAmount().subtract(acount));
        sch.setStartTime(new Date());
        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_PREPARE.getValue()));
        scheduleService.addSchedule(sch);
        CapitalFlow cap;
        if(null!= vouchers&&vouchers.size()>0){
            UserAccount userAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
            for (int i = 0; i < vouchers.size(); i++) {
                cap=new CapitalFlow();
                cap.setScheduleId(sch.getScheduleId());
                cap.setOperationType(Integer.parseInt(HFOperationEnum.FROZEN_PERSON_TO_COMPANY.getValue()));
                cap.setFromUser(userAccount.getUserId());
                cap.setAmount(vouchers.get(i).getAmount());
                cap.setStartTime(new Date());
                cap.setBusinessId(sch.getBusinessId());
                cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                capitalFlowService.addCapital(cap);
            }
        }
        cap=new CapitalFlow();
        cap.setScheduleId(sch.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.FROZEN_PERSON_TO_PERSON.getValue()));
        cap.setFromUser(userId);
        cap.setAmount(payOrder.getAmount().subtract(acount));
        cap.setStartTime(new Date());
        cap.setBusinessId(sch.getBusinessId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);

        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);

        //支付单明细和财富券保存关系
        voucherService.linkVoucher(payOrder,vouchers);

        // 创建加息券和订单关联表
		if (null != rateUser && null != rateProduct) {
			RateLendOrder existsRatelendOrder = rateLendOrderService.findByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderTypeEnum.RATE_COUPON.getValue(), RateLendOrderStatusEnum.UN_VALID.getValue());
			if (null == existsRatelendOrder) {
				List<LendOrderBidDetail> details = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.WAITING_PAY);
				Long loanApplicationId = details.get(0).getLoanApplicationId();
				rateLendOrderService.createRateLendOrder(rateUser, lendOrder.getLendOrderId(), loanApplicationId, RateLendOrderTypeEnum.RATE_COUPON, RateLendOrderPointEnum.CYCLE_RAPAYMENT.getValue(), rateProduct.getRateValue(), RateLendOrderStatusEnum.UN_VALID);
			}
		}
        payResult.setPayResult(true);
        payResult.setPayId(payOrder.getPayId());
        payResult.setLendOrderId(lendOrder.getLendOrderId());

        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_WAITING.getValue()));
        scheduleService.updateSchedule(sch);
        return payResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayOrder addPayOrderForBuyHF(Long userId, LendOrder lendOrder, ParaContext paraContext, Date now,List<VoucherVO> vouchers,RateUser rateUser,RateProduct rateProduct, Pair<String, BigDecimal>... amountDetails) {
        PayResult payResult = new PayResult(null);
        LendProductPublish productPublish = this.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
        //如果是省心计划，且有购买限额，要对可购买金额进行处理
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                && productPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {
            BigDecimal waitingBuyBalance = productPublish.getPublishBalance().subtract(productPublish.getSoldBalance());
            if (waitingBuyBalance.compareTo(lendOrder.getBuyBalance()) < 0) {
                LendOrder orderLock = lendOrderService.findAndLockById(lendOrder.getLendOrderId());
                if (orderLock.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.UNPAY.getValue())){
                    lendOrderService.updateLendOrderStatus(lendOrder.getLendOrderId(), LendOrderConstants.FinanceOrderStatusEnum.OUT_TIME);
                }
            }
        }

        //生成支付单并记录订单和支付单的关系
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())){
            busTypeEnum = BusTypeEnum.BUY_FINANCE;
        }
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())){
            busTypeEnum = BusTypeEnum.BID_LOAN;
        }
        if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())){
            busTypeEnum = BusTypeEnum.BUY_RIGHTS;
        }


        PayOrder payOrder = payService.addPayOrder(lendOrder.getBuyBalance(), now, userId, busTypeEnum, amountDetails);

        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);

        //支付单明细和财富券保存关系
        voucherService.linkVoucher(payOrder,vouchers);

        //加入存管资金流任务
        BigDecimal acount=BigDecimal.ZERO;
        if(null!= vouchers&&vouchers.size()>0){
            for (int i = 0; i < vouchers.size(); i++) {
                acount=acount.add(vouchers.get(i).getAmount());
            }
        }
        Schedule sch=new Schedule();
        sch.setBusinessId(payOrder.getPayId());
        sch.setBusinessType(Integer.parseInt(AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getValue()));
        sch.setDesc("用户ID:"+payOrder.getUserId()+AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getDesc()+payOrder.getAmount().subtract(acount));
        sch.setStartTime(new Date());
        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_PREPARE.getValue()));
        scheduleService.addSchedule(sch);
        CapitalFlow cap;
        if(null!= vouchers&&vouchers.size()>0){
            UserAccount userAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
            for (int i = 0; i < vouchers.size(); i++) {
                cap=new CapitalFlow();
                cap.setScheduleId(sch.getScheduleId());
                cap.setOperationType(Integer.parseInt(HFOperationEnum.FROZEN_PERSON_TO_COMPANY.getValue()));
                cap.setFromUser(userAccount.getUserId());
                cap.setAmount(vouchers.get(i).getAmount());
                cap.setStartTime(new Date());
                cap.setBusinessId(sch.getBusinessId());
                cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                capitalFlowService.addCapital(cap);
            }
        }

        // 创建加息券和订单关联表
        if (null != rateUser && null != rateProduct) {
            RateLendOrder existsRatelendOrder = rateLendOrderService.findByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderTypeEnum.RATE_COUPON.getValue(), RateLendOrderStatusEnum.UN_VALID.getValue());
            if (null == existsRatelendOrder) {
                List<LendOrderBidDetail> details = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.WAITING_PAY);
                Long loanApplicationId = details.get(0).getLoanApplicationId();
                rateLendOrderService.createRateLendOrder(rateUser, lendOrder.getLendOrderId(), loanApplicationId, RateLendOrderTypeEnum.RATE_COUPON, RateLendOrderPointEnum.CYCLE_RAPAYMENT.getValue(), rateProduct.getRateValue(), RateLendOrderStatusEnum.UN_VALID);
            }
        }
        return payOrder;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayResult addPayOrderForBuyProduct(Long userId, LendOrder lendOrder, ParaContext paraContext, Date now, Pair<String, BigDecimal>... amountDetails) {
        PayResult payResult = new PayResult(null);
        LendProductPublish productPublish = this.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
        //如果是省心计划，且有购买限额，要对可购买金额进行处理
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                && productPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {
            BigDecimal waitingBuyBalance = productPublish.getPublishBalance().subtract(productPublish.getSoldBalance());
            if (waitingBuyBalance.compareTo(lendOrder.getBuyBalance()) < 0) {
                LendOrder orderLock = lendOrderService.findAndLockById(lendOrder.getLendOrderId());
                if (orderLock.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.UNPAY.getValue()))
                    lendOrderService.updateLendOrderStatus(lendOrder.getLendOrderId(), LendOrderConstants.FinanceOrderStatusEnum.OUT_TIME);

                payResult.setPayResult(false);
                payResult.setLendOrderId(lendOrder.getLendOrderId());
                payResult.setFailDesc("由于剩余可购买的金额不足，该订单已过期");
                return payResult;
            }
        }

        //生成支付单并记录订单和支付单的关系
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
            busTypeEnum = BusTypeEnum.BUY_FINANCE;

        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()))
            busTypeEnum = BusTypeEnum.BID_LOAN;

        PayOrder payOrder = payService.addPayOrder(lendOrder.getBuyBalance(), now, userId, busTypeEnum, amountDetails);

        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);

        payResult.setPayResult(true);
        payResult.setPayId(payOrder.getPayId());
        payResult.setLendOrderId(lendOrder.getLendOrderId());
        return payResult;
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
	public PayResult bidLoanByAccountBalance(Long loanApplicationId, Long userId, Long financePublishId, BigDecimal amount, String resource ) {
        Date now = new Date();
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(AmountType.ACCOUNT.getValue(), amount);
        ParaContext paraContext = new ParaContext();
        paraContext.put("loanApplicationId", loanApplicationId);

        //新建省心计划
		PayOrder payOrder = addOrdersForBuyProduct(userId, financePublishId, paraContext, amount, now, resource,  null,null,amountDetail);

        //执行订单支付
        return payService.doPay(payOrder.getPayId(), now);
    }
    @Override
    @Transactional(propagation = Propagation.NEVER)
	public PayResult creditorrightsByAccountBalance(CreditorRightsTransferApplication crta, Long loanApplicationId, Long userId, Long financePublishId, BigDecimal amount, String resource) {
        Date now = new Date();
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(AmountType.ACCOUNT.getValue(), amount);
        
        ParaContext paraContext = new ParaContext();
        paraContext.put("loanApplicationId", loanApplicationId);

        LendProductServiceImpl bean = ApplicationContextUtil.getBean(LendProductServiceImpl.class);
        //新建省心计划
		PayOrder payOrder = bean.addOrdersForBuyRightsProduct(userId, financePublishId, paraContext, amount, now, resource, crta, amountDetail);

        //执行订单支付
        return payService.doPay(payOrder.getPayId(), now);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public PayResult payByOrder(LendOrder lendOrder) {
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(AmountType.ACCOUNT.getValue(), lendOrder.getBuyBalance());
        Date now = new Date();
        //生成支付单并记录订单和支付单的关系
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
            busTypeEnum = BusTypeEnum.BUY_FINANCE;

        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()))
            busTypeEnum = BusTypeEnum.BID_LOAN;

        PayOrder payOrder = payService.addPayOrder(lendOrder.getBuyBalance(), now, lendOrder.getLendUserId(), busTypeEnum, amountDetail);

        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);
        //执行订单支付
        return payService.doPay(payOrder.getPayId(), now);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayOrder addOrdersForBuyProduct(Long userId, Long financePublishId, ParaContext paraContext, BigDecimal amount, Date now,String source,String isUseVoucher,String profitReturnConfig, Pair<String, BigDecimal>... amountDetails) {
        LendProductPublish productPublish = this.getLendProductPublishByPublishId(financePublishId);
        LendProduct product = this.findById(productPublish.getLendProductId());
        //如果是省心计划，且有购买限额，要对可购买金额进行处理
        if (product.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                && productPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {
            BigDecimal waitingBuyBalance = productPublish.getPublishBalance().subtract(productPublish.getSoldBalance());
            if (waitingBuyBalance.compareTo(amount) < 0)
                amount = waitingBuyBalance;
        }

        //生成订单
        LendOrder lendOrder = null;
        //如果是出借产品，就建立出借明细数据
        if (product.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            lendOrder = this.lendOrderService.addLoanOrder(userId, (Long) paraContext.get("loanApplicationId"), financePublishId, amount, now, product,source);
        } else {
            lendOrder = this.lendOrderService.addFinanceOrder(userId, financePublishId, amount, now, product,source,isUseVoucher,profitReturnConfig);
        }

        //生成支付单并记录订单和支付单的关系
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
        	busTypeEnum = BusTypeEnum.BUY_FINANCE;
        
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()))
        	busTypeEnum = BusTypeEnum.BID_LOAN;
        	
        PayOrder payOrder = payService.addPayOrder(amount, now, userId, busTypeEnum, amountDetails);

        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);

        //加入存管资金流任务
        Schedule sch=new Schedule();
        sch.setBusinessId(payOrder.getPayId());
        sch.setBusinessType(Integer.parseInt(AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getValue()));
        sch.setDesc("用户ID:"+payOrder.getUserId()+AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getDesc()+amount);
        sch.setStartTime(new Date());
        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_PREPARE.getValue()));
        scheduleService.addSchedule(sch);
        CapitalFlow cap=new CapitalFlow();
        cap.setScheduleId(sch.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.FROZEN_PERSON_TO_PERSON.getValue()));
        cap.setFromUser(userId);
        cap.setAmount(amount);
        cap.setStartTime(new Date());
        cap.setBusinessId(sch.getBusinessId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);
        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_WAITING.getValue()));
        scheduleService.updateSchedule(sch);
        return payOrder;
    }
    
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public PayOrder addOrdersForBuyRightsProduct(Long userId, Long financePublishId, ParaContext paraContext, BigDecimal amount, Date now, String source, CreditorRightsTransferApplication crtf, Pair<String, BigDecimal>... amountDetails) {
		LendProductPublish productPublish = this.getLendProductPublishByPublishId(financePublishId);
		LendProduct product = this.findById(productPublish.getLendProductId());

		// 生成订单
		LendOrder lendOrder = this.lendOrderService.addCreditorRightsOrder(userId, (Long) paraContext.get("loanApplicationId"), financePublishId, amount, now, product, source, crtf);
		
		PayOrder payOrder = payService.addPayOrder(amount, now, userId, BusTypeEnum.BUY_RIGHTS, amountDetails);

		// 关联订单和支付单
		OrderPayRelations orderPayRelations = new OrderPayRelations();
		orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
		orderPayRelations.setPayId(payOrder.getPayId());
		myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);
		return payOrder;
	}

    @Override
    public LproductWithBalanceStatus findFinanceProductDetailForWeb(long lendProductPublishId) {
        return myBatisDao.get("LEND_PRODUCT.findFinanceProductDetailForWeb", lendProductPublishId);
    }

    @Override
    public LendProductPublish getAndLockPublishById(Long financePublishId) {
        return myBatisDao.get("LEND_PRODUCT_PUBLISH.getAndLockPublishById", financePublishId);
    }

	/**
	 * 根据发布状态和借款产品ID，查询出借产品发布明细
	 */
	@Override
	public List<LendProductPublish> getByPublishStateAndLendProductId(char publishState, Long lendProductId) {
		LendProductPublish lendProductPublish = new LendProductPublish();
		lendProductPublish.setLendProductId(lendProductId);
		lendProductPublish.setPublishState(publishState);
		return myBatisDao.getList("LEND_PRODUCT_PUBLISH.getByPublishStateAndLendProductId", lendProductPublish);
	}

	@Override
	public Pagination<LendAndLoanVO> findLendListByUserId(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<LendAndLoanVO> pagination = new Pagination<LendAndLoanVO>();
		pagination.setCurrentPage(pageNo);
		pagination.setPageSize(pageSize);
		List<LendAndLoanVO> loanList = myBatisDao.getListForPaging("findLendListByUserId", params, pagination.getCurrentPage(),
				pagination.getLimit());
		pagination.setRows(loanList);
		int totalCount = this.myBatisDao.count("findLendListByUserId", params);
        pagination.setTotal(totalCount);
		return pagination;
	}

	/***
	 * 
	 */
	@Override
	public Pagination<LendProductPublishVO> findFinanceProductListForWebCondition(int pageSize, int pageNo,
			LendProductPublishVO lendProductPublish) {
	        Pagination<LendProductPublishVO> re = new Pagination<LendProductPublishVO>();
	        re.setPageSize(pageSize);
	        lendProductPublish.setNow(new Date());
	        int totalCount = this.myBatisDao.count("FindSXFinanceListForWebConditionCount", lendProductPublish);
	        List<LendProductPublishVO> uah = this.myBatisDao.getListForPaging("LEND_PRODUCT_PUBLISH.findSXFinanceListForWebCondition", lendProductPublish, pageNo, pageSize);
	        re.setTotal(totalCount);
	        re.setRows(uah);

	        return re;
	}

	@Override
	public List<LendProduct> findLendProductByPublishState(LendProductPublishStateEnum stateEnum) {
		return myBatisDao.getList("LEND_PRODUCT.findLendProductByPublishState", stateEnum.getValue());
	}

	@Override
	/*//select lpp.*
	  from lend_product_publish lpp, lend_product lp
	  where lp.lend_product_id = lpp.lend_product_id
	    and lp.product_type = '2'
	    and lpp.publish_state in ('1', '2') 
	    and (lpp.start_date is not null or lpp.end_date is not null)*/
	public void refreshFinanceBidStatus() {
		List<LendProductPublish> publishList = myBatisDao.getList("LEND_PRODUCT_PUBLISH.getFinanceBids") ;
		if(publishList == null || publishList.size() < 1) return ;
		Date now = new Date();
		for(LendProductPublish lendProductPublish : publishList){
			switch(lendProductPublish.getPublishState()){
				//未开始发售状态
				//如果开始时间小于现在或者开始时间为空，并且结束时间大于现在或结束时间为空，状态刷新为发售中
				case '1': {
					if ((lendProductPublish.getStartDate() == null || lendProductPublish
							.getStartDate().before(now))
							&& (lendProductPublish.getEndDate() == null || lendProductPublish
									.getEndDate().after(now))) {
						lendProductPublish.setPublishState(LendProductPublishStateEnum.SELLING.value2Char());
						this.myBatisDao.update("LEND_PRODUCT_PUBLISH.updateByPrimaryKeySelective", lendProductPublish);
					}
					break;
				}
				//发售中 状态
				//如果结束时间小于现在，状态刷新为已过期
				case '2': {
					if (lendProductPublish.getEndDate() != null
							&& lendProductPublish.getEndDate().before(now)) {
						lendProductPublish.setPublishState(LendProductPublishStateEnum.TIMEOUT.value2Char());
						this.myBatisDao.update("LEND_PRODUCT_PUBLISH.updateByPrimaryKeySelective", lendProductPublish);
					}
					break;
				}
			}
		}
	}

	@Override
	public List<Integer> getLendProductTimeLimitByLendUserId(Long lendUserId) {
		return myBatisDao.getList("LEND_PRODUCT.getLendProductTimeLimitByLendUserId", lendUserId);
	}

	/**
	 * 新手用户
	 * @param pageSize
	 * @param pageNo
	 * @param lendProductPublish
	 * @author wangyadong
	 * @return
	 */
	@Override
	public Pagination<LproductWithBalanceStatus> findSpecialFinanceProductListForWebCondition(
			int pageSize, int pageNo, LendProductPublish lendProductPublish) {
        Pagination<LproductWithBalanceStatus> re = new Pagination<LproductWithBalanceStatus>();
        int totalCount = this.myBatisDao.count("getLoanApplicationSpecialPaging", lendProductPublish);
        List<LproductWithBalanceStatus> uah = this.myBatisDao.getListForPaging("getLoanApplicationSpecialPaging", lendProductPublish, pageNo, pageSize);
        re.setTotal(totalCount);
        re.setRows(uah);
        return re;
	}     

}
