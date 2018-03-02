package com.xt.cfp.core.service.impl;


import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.DisActivityEnums.DarCommiPaidNodeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.event.AheadRepaymentEvent;
import com.xt.cfp.core.event.EventHandle;
import com.xt.cfp.core.event.RepaymentEvent;
import com.xt.cfp.core.event.service.EventTriggerInfoService;
import com.xt.cfp.core.event.service.TaskExecLogService;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.CommitProfitVO;
import com.xt.cfp.core.pojo.ext.LoanFeeDetailVO;
import com.xt.cfp.core.pojo.ext.RepaymentPlanVO;
import com.xt.cfp.core.pojo.ext.RepaymentVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.util.*;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class RepaymentPlanServiceImpl implements RepaymentPlanService {

    private static Logger logger = Logger.getLogger(LoanApplicationServiceImpl.class);
    @Resource
    private MyBatisDao myBatisDao;
    @Resource
    private LoanApplicationService loanApplicationService;
    @Resource
    private LoanProductService loanProductService;
    @Resource
    private ProductFeesCached productFeesCache;
    @Resource
    private DefaultInterestDetailService interestDetailService;
    @Resource
    private LoanFeesDetailService loanFeesDetailService;
    @Resource
    private ProductFeesCached productFeesCached;
    @Autowired
    private SmsService smsService;
    @Autowired
    private LoanApplicationFeesItemService loanApplicationFeesItemService;
    @Autowired
    private FeesItemService feesItemService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    @Autowired
    private RepaymentRecordService repaymentRecordService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private RightsPrepayDetailService rightsPrepayDetailService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private LendLoanBindingService lendLoanBindingService;
    @Autowired
    private AwardDetailService awardDetailService;
    @Autowired
    private LendOrderDetailFeesService lendOrderDetailFeesService;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private PayService payService;
    @Autowired
    private TaskExecLogService taskExecLogService;
    @Autowired
    private EventTriggerInfoService eventTriggerInfoService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private UserOpenIdService userOpenIdService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private CommiProfitService commiProfitService;
    @Autowired
    private CommisionService commisionService;
    @Autowired
    private WhiteTabsService whiteTabsService;
    @Autowired
    private RateLendOrderService rateLendOrderService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CapitalFlowService capitalFlowService;


    private final static String Y_M_D = "yyyy-MM-dd";

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public void create(RepaymentPlan plan) throws Exception {
        myBatisDao.insert("REPAYMENTPLAN.insert", plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public void update(RepaymentPlan plan) throws Exception {
        myBatisDao.update("REPAYMENTPLAN.update", plan);
    }
    
    @Override
    public void updateNTX(RepaymentPlan plan) throws Exception {
        myBatisDao.update("REPAYMENTPLAN.updateNTX", plan);
    }

    @Override
    public void update(Map repaymentPlanMap) {
        myBatisDao.update("REPAYMENTPLAN.updateMap", repaymentPlanMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public void remove(RepaymentPlan plan) throws Exception {
        myBatisDao.delete("REPAYMENTPLAN.delete", plan.getRepaymentPlanId());
    }

    @Override
    public List<RepaymentPlan> getRepaymentPlansByloanApplicationId(long loanApplicationId) {
        return myBatisDao.getList("REPAYMENTPLAN.getRepaymentPlansByloanApplicationId", loanApplicationId);
    }
    @Override
    public List<RepaymentPlan> getRepaymentPlansByloanApplicationId(long loanApplicationId,ChannelTypeEnum channelTypeEnum) {
        Map map = new HashMap();
        map.put("loanApplicationId", loanApplicationId);
        map.put("channelType", channelTypeEnum.getValue());
        return myBatisDao.getList("REPAYMENTPLAN.getRepaymentPlansByloanApplicationIdAndChannelType", map);
    }

    @Override
    public List<RepaymentPlan> getRepaymentPlanByLoanAppIdAndState(long loanApplicationId, char planState) {
        Map map = new HashMap();
        map.put("loanApplicationId", loanApplicationId);
        map.put("planState", planState);
        return myBatisDao.getList("REPAYMENTPLAN.getRepaymentPlanByLoanAppIdAndState", map);
    }

    @Override
    public List<RepaymentPlan> getRepaymentPlanByDate(long loanApplicationId, Date repaymentDate, char planState) {
        Map map = new HashMap();
        map.put("loanApplicationId", loanApplicationId);
//        map.put("repaymentDate", repaymentDate);
        map.put("planState", planState);
        return myBatisDao.getList("REPAYMENTPLAN.getRepaymentPlanByDate", map);
    }


    @Override
    public RepaymentPlan getFirstUnCompletePlan(long loanApplicationId) {
        return myBatisDao.get("REPAYMENTPLAN.getFirstUnCompletePlan", loanApplicationId);
    }

    @Override
    public List<RepaymentPlan> getByLoanApplicationIdAtLast(Map parameters)
            throws Exception {
        return myBatisDao.getList("REPAYMENTPLAN.findByLoanApplicationIdAtLast", parameters);
    }

    public BigDecimal calcRepaymentBalance(long loanApplicationId, String repaymentDay, boolean isInterest) throws Exception {
        LoanApplication loanApp = loanApplicationService.findById(loanApplicationId);
        LoanProduct loanProduct = loanProductService.findById(loanApp.getLoanProductId());
        UserAccount loanAccount = userAccountService.getCashAccount(loanApp.getRepaymentAccountId());
        BigDecimal repayment = BigDecimal.ZERO;
        BigDecimal summaryShouldBalance = BigDecimal.ZERO;
        BigDecimal summaryFactBalance = BigDecimal.ZERO;
        BigDecimal summaryShouldInterestBalance = BigDecimal.ZERO;
        BigDecimal summaryFactInterestBalance = BigDecimal.ZERO;
        BigDecimal unpayInterestBalance = BigDecimal.ZERO;
        BigDecimal breachBalance = BigDecimal.ZERO;
        BigDecimal fees = BigDecimal.ZERO;
        List<String> sectionCodes = new ArrayList<String>();
        try {

			/*
             * 获取指定还款日期内的未还记录
			 */
            Map parameters = new HashMap();
            parameters.put("loanApplicationId", loanApplicationId);
            parameters.put("repaymentDay", repaymentDay);

            List<RepaymentPlan> collections = getByLoanApplicationIdAtLast(parameters);

            /*
			 * 计算管理费
			 */
            List<LoanProductFeesItem> loanProductFeesItems = productFeesCached.getFeesItemsByLoanProductId(loanApp.getLoanProductId());

            BigDecimal allInterest = BigDecimal.ZERO;
            BigDecimal balance = loanApp.getConfirmBalance();
            BigDecimal annualRate = loanApp.getAnnualRate();
            char dueTimeType = loanProduct.getDueTimeType();
            char repaymentMethod = loanProduct.getRepaymentMethod();
            char repaymentType = loanProduct.getRepaymentType();
            char repaymentCycle = loanProduct.getRepaymentCycle();
            Integer dueTime = loanProduct.getDueTime();
            Integer cycleValue = loanProduct.getCycleValue();
            allInterest = InterestCalculation.getAllInterest(balance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue);
            
			/*
			 * 计算应还金额、已还金额
			 */
            List<LoanProductFeesItem> productFeesItems = new ArrayList<LoanProductFeesItem>();
            productFeesItems.addAll(productFeesCached.getFeesItemsByLoanProductId(loanApp.getLoanProductId()));

            for (Iterator<RepaymentPlan> it = collections.iterator(); it.hasNext(); ) {
                RepaymentPlan plan = it.next();
                BigDecimal target = BigDecimal.ZERO;
                sectionCodes.add(String.valueOf(plan.getSectionCode()));

                summaryShouldBalance = summaryShouldBalance.add(plan.getShouldBalance());
                summaryFactBalance = summaryFactBalance.add(plan.getFactBalance());

                List<LoanFeesDetail> feesDetails = new ArrayList<LoanFeesDetail>();
                Map feesItemsParameters = new HashMap();
                feesItemsParameters.put("loanApplicationId", loanApplicationId);
                feesItemsParameters.put("sectionCode", plan.getSectionCode());
                feesDetails.addAll(loanFeesDetailService.getFeesItemBy(feesItemsParameters));


                List<LoanApplicationFeesItem> loanApplicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(loanApplicationId);

                for (LoanApplicationFeesItem productFeeItem : loanApplicationFeesItems) {
                    boolean exists = false;
                    if (productFeeItem.getChargeCycle() == FeesItem.FEESCYCLE_ATCYCLE) {
                        for (LoanFeesDetail feeDetail : feesDetails) {
                            if (productFeeItem.getLoanApplicationFeesItemId() == feeDetail.getLoanApplicationFeesItemId()) {
                                if (feeDetail.getFeesState() != LoanFeesDetail.FEESSTATE_PAID) {
                                    target = feeDetail.getFees().subtract(feeDetail.getPaidFees());
                                }
                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                            target = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(productFeeItem, loanApp.getConfirmBalance(), allInterest, plan.getShouldCapital(), plan.getShouldInterest(), BigDecimal.ZERO, BigDecimal.ZERO);
                            target = BigDecimalUtil.up(target, 2);
                        }

                        fees = fees.add(target);
                    }
                }
            }


            if (isInterest) {

                List<LoanFeesDetail> results = new ArrayList<LoanFeesDetail>();
                Map conditionParameters = new HashMap();
                conditionParameters.put("loanApplicationId", loanApplicationId);
                conditionParameters.put("feesCycle", FeesItem.FEESCYCLE_ATDELAY_FIRSTDAY);
                conditionParameters.put("sectionCodes", sectionCodes);
                conditionParameters.put("feesState", LoanFeesDetail.FEESSTATE_UNPAY);
                results.addAll(loanFeesDetailService.getFeesItemBy(conditionParameters));
                if (!results.isEmpty()) {
                    for (LoanFeesDetail detail : results) {
                        breachBalance = breachBalance.add(detail.getFees2().subtract(detail.getPaidFees()));
                    }
                	
        			/*
        			 * 计算未还罚息金额
        			 */
                    for (RepaymentPlan repaymentPlan : collections) {
                        Map repaymentPlanConditionParameters = new HashMap();
                        repaymentPlanConditionParameters.put("repaymentPlanId", repaymentPlan.getRepaymentPlanId());
                        repaymentPlanConditionParameters.put("repaymentState", DefaultInterestDetail.REPAYMENTSTATE_UNCOMPLETE);
                        List<DefaultInterestDetail> interests = interestDetailService.findBy(repaymentPlanConditionParameters);
                        for (DefaultInterestDetail interest : interests) {
                            summaryShouldInterestBalance = summaryShouldInterestBalance.add(interest.getInterestBalance2());
                            summaryFactInterestBalance = summaryFactInterestBalance.add(interest.getRepaymentBalance());
                        }
                    }
                    unpayInterestBalance = summaryShouldInterestBalance.subtract(summaryFactInterestBalance);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        repayment = summaryShouldBalance.subtract(summaryFactBalance).add(fees).add(unpayInterestBalance).add(breachBalance);
        return repayment;
    }

    public BigDecimal calcBreachBalance(long loanApplicationId, BigDecimal feesRate, int radicesType, long feeItemId) throws Exception {
        BigDecimal breachBalance = BigDecimal.ZERO;
        List<RepaymentPlan> repayments = getRepaymentPlansByloanApplicationId(loanApplicationId);
        BigDecimal confirmBalance = loanApplicationService.findById(loanApplicationId).getConfirmBalance();
        feesRate = feesRate.divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING);
        Map parameters = new HashMap();
        parameters.put("loanApplicationId", loanApplicationId);
        parameters.put("feesItemId", feeItemId);
        List<LoanFeesDetail> details = new ArrayList<LoanFeesDetail>();
        switch (radicesType) {
            case 1:
			/*
			 * 本金（批复金额）*收费比例
			 */
                breachBalance = confirmBalance.multiply(feesRate);
                break;
            case 2:
			/*
			 * 当前利息*收费比例
			 */
                for (RepaymentPlan plan : repayments) {
                    parameters.put("sectionCode", plan.getSectionCode());
                    details.addAll(loanFeesDetailService.getFeesItemBy(parameters));

                    if (details.isEmpty())
                        continue;

                    breachBalance = breachBalance.add(plan.getShouldInterest().multiply(feesRate));
                }
            case 3:
			/*
			 * 全部利息*收费比例
			 */
                for (RepaymentPlan plan : repayments) {
                    parameters.put("sectionCode", plan.getSectionCode());
                    details.addAll(loanFeesDetailService.getFeesItemBy(parameters));

                    if (details.isEmpty())
                        continue;
                    breachBalance = breachBalance.add(plan.getShouldInterest());
                }
                breachBalance = breachBalance.multiply(feesRate);
            case 4:
			/*
			 * (当期本金+利息)*收费比例
			 */
                for (RepaymentPlan plan : repayments) {
                    parameters.put("sectionCode", plan.getSectionCode());
                    details.addAll(loanFeesDetailService.getFeesItemBy(parameters));

                    if (details.isEmpty())
                        continue;
                    breachBalance = breachBalance.add(plan.getShouldBalance().multiply(feesRate));
                }
            case 5:
			/*
			 * (全部本金+利息)*收费比例
			 */
                for (RepaymentPlan plan : repayments) {
                    parameters.put("sectionCode", plan.getSectionCode());
                    details.addAll(loanFeesDetailService.getFeesItemBy(parameters));

                    if (details.isEmpty())
                        continue;
                    breachBalance = breachBalance.add(plan.getShouldBalance());
                }
                breachBalance = breachBalance.multiply(feesRate);
            case 8:
			/*
			 * 当期本金*收费比例
			 */
                for (RepaymentPlan plan : repayments) {
                    parameters.put("sectionCode", plan.getSectionCode());
                    details.addAll(loanFeesDetailService.getFeesItemBy(parameters));

                    if (details.isEmpty())
                        continue;
                    breachBalance = breachBalance.add(plan.getShouldCapital().multiply(feesRate));
                }
            default:
        }
        return breachBalance;
    }

    @Deprecated
    public BigDecimal calcFeesDetailBalance(long loanApplicationId, LoanProductFeesItem productFeeItem, BigDecimal shouldCapital, BigDecimal shouldInterest) throws Exception {
        BigDecimal fees = BigDecimal.ZERO;
        BigDecimal target = BigDecimal.ZERO;

        LoanApplication loanApp = loanApplicationService.findById(loanApplicationId);
        LoanProduct loanProduct = loanProductService.findById(loanApp.getLoanProductId());
        List<RepaymentPlan> collections = getRepaymentPlansByloanApplicationId(loanApplicationId);

        BigDecimal allInterest = BigDecimal.ZERO;
        BigDecimal balance = loanApp.getConfirmBalance();
        BigDecimal annualRate = loanApp.getAnnualRate();
        char dueTimeType = loanProduct.getDueTimeType();
        char repaymentMethod = loanProduct.getRepaymentMethod();
        char repaymentType = loanProduct.getRepaymentType();
        char repaymentCycle = loanProduct.getRepaymentCycle();
        Integer dueTime = loanProduct.getDueTime();
        Integer cycleValue = loanProduct.getCycleValue();
        allInterest = InterestCalculation.getAllInterest(balance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue);

        target = feesItemService.calculateFeesBalance(productFeeItem.getFeesItemId(), loanApp.getConfirmBalance(), allInterest, shouldCapital, shouldInterest, BigDecimal.ZERO, BigDecimal.ZERO);

        fees = fees.add(target);

        return fees;
    }

    @Override
    public void updateOverdueStatus(Map map) {
        myBatisDao.update("RepaymentPlan.updateOverdueStatus", map);
    }

    @Override
    public List<RepaymentPlan> findBy(Map parameters) {
        return myBatisDao.getList("REPAYMENTPLAN.findBy", parameters);
    }

    @Override
    public List<LoanApplication> findLoansBy(Map parameters) {
        return myBatisDao.getList("RepaymentPlan.findLoansBy", parameters);
    }

    @Override
    public List<RepaymentPlan> getByLoanApplicationIdAtBefore(Map parameters)
            throws Exception {
        return myBatisDao.getList("RepaymentPlan.findByLoanApplicationIdAtBefore", parameters);
    }

    public List<RepaymentPlan> generateRepaymentPlan(long loanProductId, BigDecimal confirmBalance) throws Exception {
        List<RepaymentPlan> results = new ArrayList<RepaymentPlan>();

        LoanProduct loanProduct = loanProductService.findById(loanProductId);
        BigDecimal annualRate = loanProduct.getAnnualRate();
        char dueTimeType = loanProduct.getDueTimeType();
        char repaymentMethod = loanProduct.getRepaymentMethod();
        char repaymentType = loanProduct.getRepaymentType();
        char repaymentCycle = loanProduct.getRepaymentCycle();
        Integer dueTime = loanProduct.getDueTime();
        Integer cycleValue = loanProduct.getCycleValue();
        Map<Integer, Map<String, BigDecimal>> mapping = new HashMap<Integer, Map<String, BigDecimal>>();
        mapping = InterestCalculation.getCalitalAndInterest(confirmBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue);
        BigDecimal allInterest = InterestCalculation.getAllInterest(confirmBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue);

        if (mapping.isEmpty())
            throw new Exception("调用利率计算算法异常");

        BigDecimal allShouldCapital = BigDecimal.ZERO;
        BigDecimal allShouldInterest = BigDecimal.ZERO;

        int sectionIndex = 0;
        BigDecimal prevCapital = BigDecimal.ZERO;
        BigDecimal prevInterest = BigDecimal.ZERO;
        BigDecimal prevBalance = BigDecimal.ZERO;
        for (Iterator<Integer> it = mapping.keySet().iterator(); it.hasNext(); ) {
            Integer code = it.next();
            RepaymentPlan plan = new RepaymentPlan();
            plan.setSectionCode(code);
            plan.setPlanState(RepaymentPlanStateEnum.UNCOMPLETE.value2Char());

            switch (loanProduct.getRepaymentType()) {
                case LoanProduct.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST:
                    plan.setShouldBalance(mapping.get(code).get("balance"));
                    plan.setShouldInterest(mapping.get(code).get("interest"));
                    plan.setShouldCapital(mapping.get(code).get("calital"));
                    break;
                case LoanProduct.REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL:
                    plan.setShouldInterest(mapping.get(code).get("interest"));
                    allShouldCapital = allShouldCapital.add(mapping.get(code).get("calital"));
                    if (it.hasNext()) {
                        plan.setShouldBalance(mapping.get(code).get("interest"));
                        plan.setShouldCapital(BigDecimal.ZERO);
                    } else {
                        plan.setShouldBalance(allShouldCapital.add(plan.getShouldInterest()));
                        plan.setShouldCapital(allShouldCapital);
                    }
                    break;
                case LoanProduct.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_INTEREST_DUE:
                    plan.setShouldBalance(mapping.get(code).get("calital"));
                    allShouldInterest = allShouldInterest.add(mapping.get(code).get("interest"));
                    if (it.hasNext()) {
                        plan.setShouldInterest(BigDecimal.ZERO);
                        plan.setShouldCapital(mapping.get(code).get("calital"));
                    } else {
                        plan.setShouldBalance(allShouldInterest.add(plan.getShouldCapital()));
                        plan.setShouldInterest(allShouldInterest);
                    }
                    break;
                default:
            }
            if (it.hasNext()) {
                plan.setShouldBalance2(BigDecimalUtil.up(plan.getShouldBalance(), 2));
                plan.setShouldCapital2(BigDecimalUtil.up(plan.getShouldCapital(), 2));

                prevCapital = prevCapital.add(plan.getShouldCapital2());
                prevInterest = prevInterest.add(plan.getShouldInterest2());
                prevBalance = prevBalance.add(plan.getShouldBalance2());
            } else {
                plan.setShouldBalance2(BigDecimalUtil.up(confirmBalance.add(allInterest).subtract(prevBalance), 2));
                plan.setShouldCapital2(BigDecimalUtil.up(confirmBalance.subtract(prevCapital), 2));
            }
            plan.setShouldInterest2(plan.getShouldBalance2().subtract(plan.getShouldCapital2()));
            sectionIndex++;
            results.add(plan);
        }

        return results;
    }

    @Override
    public RepaymentPlan findById(long repaymentPlanId) {
        return myBatisDao.get("REPAYMENTPLAN.findById", repaymentPlanId);
    }
    
    @Override
    public RepaymentPlan findByIdBLock(long repaymentPlanId, boolean lock) {
    	if(!lock){
    		return myBatisDao.get("REPAYMENTPLAN.findById", repaymentPlanId);
    	}
    	return myBatisDao.get("REPAYMENTPLAN.findByIdLock", repaymentPlanId);
    }

    /**
     * 添加-还款计划。
     */
    @Override
    public RepaymentPlan addRepaymentPlan(RepaymentPlan repaymentPlan) {
        myBatisDao.insert("REPAYMENTPLAN.insert", repaymentPlan);
        return repaymentPlan;
    }

    @Override
    public BigDecimal getAllRepaymentAmountByUserId(Long userId) {
        Map<String, BigDecimal> map = myBatisDao.get("REPAYMENTPLAN.getAllRepaymentAmountByUserId", userId);
        BigDecimal subtract = map.get("SBLANCE").subtract(map.get("FBLANCE"));
        return subtract;
    }

    @Override
    public BigDecimal getRepaymentCapitalByUserId(Long userId) {
        Map<String, BigDecimal> map = myBatisDao.get("REPAYMENTPLAN.getRepaymentCapitalByUserId", userId);
        BigDecimal subtract = map.get("SBLANCE").subtract(map.get("FBLANCE"));
        return subtract;
    }

    @Override
    public BigDecimal getRepaymentCapitalByLoanApplicationId(Long loanApplicationId) {
        Map<String, BigDecimal> map = myBatisDao.get("REPAYMENTPLAN.getRepaymentCapitalByLoanApplicationId", loanApplicationId);
        BigDecimal subtract = map.get("SBLANCE").subtract(map.get("FBLANCE"));
        return subtract;
    }

    @Override
    public BigDecimal getRepaymentInterestByUserId(Long userId) {
        Map<String, BigDecimal> map = myBatisDao.get("REPAYMENTPLAN.getRepaymentInterestByUserId", userId);
        BigDecimal subtract = map.get("SBLANCE").subtract(map.get("FBLANCE"));
        return subtract;
    }

    @Override
    public BigDecimal getRepaymentInterestByLoanApplicationId(Long loanApplicationId) {
        Map<String, BigDecimal> map = myBatisDao.get("REPAYMENTPLAN.getRepaymentInterestByLoanApplicationId", loanApplicationId);
        BigDecimal subtract = map.get("SBLANCE").subtract(map.get("FBLANCE"));
        return subtract;
    }

    @Override
    public List<RepaymentPlan> getRepaymentPlanList(long loanApplicationId, Date repaymentDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loanApplicationId", loanApplicationId);
        map.put("repaymentDate", repaymentDate);
        List<RepaymentPlan> list = myBatisDao.getList("REPAYMENTPLAN.getRepaymentPlanByDate", map);
        return list;
    }
    
    @Override
    /**
     * 1.借款人账户充值
     * 2.还款
     */
    @Transactional
    public Map<Long,Map<Long,Map<String,BigDecimal>>> repayment(long loanApplicationId,long replaymentPlanId, Date repaymentDate, BigDecimal balance, BigDecimal paidBalance, long adminId, boolean ignoreDelay, String desc ,boolean isAhead,List<Long> repaymentIdList) throws Exception {
    	//【省心计划需求：将还款部分对应债权产生的奖励和利息计算出来，转入到资金账户】
    	//resultMap : 一级key - userAccountId(省心账户或者现金账户) , 
    	//一级value -  Map<Long,Map<String,BigDecimal>>  二级key - rightsRepaymentDetailId , 二级 value - Map<String,BigDecimal>(类型-值）; 
    	//三级key - String (interest/awards/capital) 三级value - BigDecimal(18位)
    	Map<Long,Map<Long,Map<String,BigDecimal>>> resutlMap = new HashMap<>();
		// 判断是否有债权正在转让 并撤销转让申请 
		List<CreditorRightsTransferApplication> applyRecords = creditorRightsTransferAppService.getEffectiveTransferApplyByApplyByLoanApplicationId(loanApplicationId);
		if (applyRecords != null && applyRecords.size() > 0) {
			for (CreditorRightsTransferApplication apply : applyRecords) {
				// 撤销 转让申请
				creditorRightsTransferAppService.getAndLockedCreditorRightsTransferAppById(apply.getCreditorRightsApplyId(), true);
				creditorRightsTransferAppService.undoCreditorRightsTransferApplication(apply, 1);
			}
		}

        Schedule sch=new Schedule();
        sch.setBusinessId(replaymentPlanId);
        sch.setBusinessType(Integer.parseInt(AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue()));
        sch.setDesc("还款计划ID:"+replaymentPlanId+",还款金额"+balance+",还款日期"+DateUtil.getFormattedDateUtil(repaymentDate,"yyyy-MM-dd HH:mm:ss"));
        sch.setStartTime(new Date());
        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_PREPARE.getValue()));
        scheduleService.addSchedule(sch);
        UserAccount userAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);

		Map parameters = new HashMap();
		parameters.put("loanApplicationId",loanApplicationId);
		parameters.put("channelType",ChannelTypeEnum.ONLINE.getValue());
		
    	AccountValueChangedQueue avcq = new AccountValueChangedQueue();
        Date now = new Date();
    	
    	LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);

        LoanPublish loanPublish = loanPublishService.findById(loanApplicationId);

        BigDecimal allInterest = loanApplication.getInterestBalance();
        long repaymentAccountId = loanApplication.getRepaymentAccountId();
        UserAccount repaymentUserAccount = userAccountService.getUserAccountByAccId(repaymentAccountId);
        UserAccount repaymentCashAccount;
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.LOAN.getValue())) {
            repaymentCashAccount = userAccountService.getCashAccount(repaymentUserAccount.getUserId());
        } else {
            repaymentCashAccount = userAccountService.getUserAccountByAccId(repaymentAccountId);
        }

        long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
        RepaymentPlan thePlan  = this.findById(replaymentPlanId);
        List<RepaymentPlan> repaymentPlans = new ArrayList<>();
        if (isAhead) {
            repaymentPlans = getRepaymentPlanList(loanApplicationId, null);
        }else {
            repaymentPlans = getRepaymentPlanList(loanApplicationId, thePlan.getRepaymentDay());
        }
        //借款账户(资金账户)充值
        PayOrder payOrder = income(paidBalance, adminId, loanApplication, repaymentCashAccount, desc);
        //如果借款的还款账户与借款的资金账户不是一个账户，则需进行一步转账，将借款人的资金账户的钱转至借款的还款账户
        if (repaymentAccountId != repaymentCashAccount.getAccId()) {
            //借款人从资金账户转账至借款账户
            String descTurnOut = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOANAPPLICATION_CASH2LOAN, loanPublish.getLoanTitle());
            if (isAhead){
                descTurnOut = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOANAPPLICATION_AHEAD_CASH2LOAN, loanPublish.getLoanTitle());
            }
            AccountValueChanged avcTurnOut = new AccountValueChanged(repaymentCashAccount.getAccId(), balance,
                    balance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                    "none", VisiableEnum.DISPLAY.getValue(), 0L, AccountConstants.AccountChangedTypeEnum.LOAN.getValue(), loanApplicationId, now, descTurnOut, true);
            avcq.addAccountValueChanged(avcTurnOut);
            String descTurnIn = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOANAPPLICATION_INCOME, loanPublish.getLoanTitle());
            if (isAhead){
                descTurnIn = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOANAPPLICATION_AHEAD_INCOME, loanPublish.getLoanTitle());
            }
            AccountValueChanged avcTurnIn = new AccountValueChanged(repaymentAccountId, balance,
                    balance, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                    "none", VisiableEnum.DISPLAY.getValue(), 0L, AccountConstants.AccountChangedTypeEnum.LOAN.getValue(), loanApplicationId, now, descTurnIn, true);
            avcq.addAccountValueChanged(avcTurnIn);
        }
        //借款账户资金冻结
        String descFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOANAPPLICATION_2REPAYMENT, loanPublish.getLoanTitle());
        if (isAhead){
            descFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOANAPPLICATION_AHEAD_2REPAYMENT, loanPublish.getLoanTitle());
        }
        AccountValueChanged avcFreeze = new AccountValueChanged(repaymentAccountId, balance,
                balance, AccountConstants.AccountOperateEnum.FREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENTREEZEN.getValue(),
                "none", VisiableEnum.DISPLAY.getValue(), 0L, AccountConstants.AccountChangedTypeEnum.LOAN.getValue(), loanApplicationId, new Date(), descFreeze, true);
        avcq.addAccountValueChanged(avcFreeze);


        CapitalFlow cap=new CapitalFlow();
        cap.setScheduleId(sch.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.FROZEN_PERSON_TO_PERSON.getValue()));
        cap.setFromUser(loanApplication.getUserId());
        cap.setAmount(balance);
        cap.setStartTime(new Date());
        cap.setBusinessId(sch.getBusinessId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);


        //普通项目费用列表
        List<LoanApplicationFeesItem> commonFeesItems = new ArrayList<LoanApplicationFeesItem>();
        //违约项目费用列表
        List<LoanApplicationFeesItem> breakFessItems = new ArrayList<LoanApplicationFeesItem>();
        List<LoanApplicationFeesItem> loanApplicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(loanApplicationId);
        for (LoanApplicationFeesItem applicationFeesItem : loanApplicationFeesItems) {
            if (constantDefineCached.getpConstantByChild("itemChildType", applicationFeesItem.getItemType()).getConstantValue().equals(String.valueOf(FeesItem.PARENTITEMTYPE_FEES))) {
                commonFeesItems.add(applicationFeesItem);
            } else if (constantDefineCached.getpConstantByChild("itemChildType", applicationFeesItem.getItemType()).getConstantValue().equals(String.valueOf(FeesItem.PARENTITEMTYPE_BREACH))) {
                breakFessItems.add(applicationFeesItem);
            }
        }
        List<RepaymentPlan> aheadPart = new ArrayList<RepaymentPlan>();
        //各出借人的各个订单 的Map
        Map<Long, Long> lendsOrderMap = new HashMap<Long, Long>();

        List<CreditorRights> creditorRightsList = creditorRightsService.getByLoanApplicationId(loanApplicationId);
        
        for (CreditorRights creditorRights : creditorRightsList) {
            //线上债权、已生效
            if (creditorRights.getChannelType() == ChannelTypeEnum.ONLINE.value2Long() && creditorRights.getRightsState() == CreditorRightsStateEnum.EFFECTIVE.value2Char()) {
                lendsOrderMap.put(creditorRights.getLendOrderId(), creditorRights.getLendAccountId());
            }
        }
        
        int count = 1; 
        boolean isLastRepaymentPlan = false;
        for (RepaymentPlan repaymentPlan : repaymentPlans) {
        	repaymentIdList.add(repaymentPlan.getRepaymentPlanId());
        	if(count == repaymentPlans.size()){
        		isLastRepaymentPlan = true ;
        	}
            //各出借订单出资占用比例
            Map<Long, BigDecimal> lendsRatioMap = new HashMap<Long, BigDecimal>();

            for (CreditorRights creditorRights : creditorRightsList) {
                //线上债权、已生效
                if (creditorRights.getChannelType() == ChannelTypeEnum.ONLINE.value2Long() && creditorRights.getRightsState() == CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char()) {
                    RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(creditorRights.getCreditorRightsId(), repaymentPlan.getSectionCode());
                    if (!lendsRatioMap.containsKey(creditorRights.getLendOrderId())) {
                        lendsRatioMap.put(creditorRights.getLendOrderId(), rightsRepaymentDetail.getProportion());
                    } else {
                        lendsRatioMap.put(creditorRights.getLendOrderId(), lendsRatioMap.get(creditorRights.getLendOrderId()).add(rightsRepaymentDetail.getProportion()));
                    }
                    lendsOrderMap.put(creditorRights.getLendOrderId(), creditorRights.getLendAccountId());
                }
            }
           
            //提前还款start
            if (!thePlan.getRepaymentDay().before(repaymentPlan.getRepaymentDay())){

                BigDecimal zeroBigDecimal = BigDecimal.valueOf(0l);
                BigDecimal _balance = zeroBigDecimal;
                if(isAhead){

                    BigDecimal loanBalance = loanApplication.getLoanBalance();
                    List<LoanApplicationFeesItem> applicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATCYCLE);
                    List<LoanApplicationFeesItem> delayFees = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATDELAY_FIRSTDAY);
                    List<LoanFeesDetail> loanFeesDetails = loanFeesDetailService.getLoanFeesDetailByLoanId(loanApplicationId);

                    BigDecimal capital = zeroBigDecimal;
                    BigDecimal defaultInterest = zeroBigDecimal;
                    BigDecimal feesBalance = zeroBigDecimal;


                    //当期以及前期未结清部分计算
                    _balance = _balance.add(repaymentPlan.getShouldBalance2()).subtract(repaymentPlan.getFactBalance());

                    for (LoanApplicationFeesItem feesItem : applicationFeesItems) {
                        feesBalance = feesBalance.add(getDefaultBalance(feesItem.getFeesRate(), feesItem.getRadicesType(), repaymentPlan.getShouldCapital2(), repaymentPlan.getShouldInterest2(),
                                loanBalance, allInterest, repaymentPlan.getFactCalital(), repaymentPlan.getFactInterest(), zeroBigDecimal, zeroBigDecimal));
                        for (LoanFeesDetail feesDetail : loanFeesDetails) {
                            if (feesDetail.getLoanApplicationFeesItemId() == feesItem.getLoanApplicationFeesItemId() && feesDetail.getSectionCode() == repaymentPlan.getSectionCode()) {
                                feesBalance = feesBalance.subtract(feesDetail.getPaidFees());
                            }
                        }
                    }
                    for (LoanApplicationFeesItem feesItem : delayFees) {
                        if (feesItem.getChargeCycle() == FeesPointEnum.ATDELAY_FIRSTDAY.value2Char() && repaymentPlan.getPlanState() == RepaymentPlanStateEnum.DEFAULT.value2Char()) {
                            for (LoanFeesDetail feesDetail : loanFeesDetails) {
                                if (feesDetail.getLoanApplicationFeesItemId() == feesItem.getLoanApplicationFeesItemId() && feesDetail.getSectionCode() == repaymentPlan.getSectionCode()) {
                                    feesBalance = feesBalance.add(feesDetail.getFees2().subtract(feesDetail.getPaidFees() != null ? feesDetail.getPaidFees() : zeroBigDecimal));
                                }
                            }
                        }
                    }


                    Map<String, Object> paraMap = new HashMap<String, Object>();
                    paraMap.put("UNCOMPLETE_WAIVER", "true");
                    paraMap.put("repaymentPlanId", repaymentPlan.getRepaymentPlanId());
                    try {
                        List<DefaultInterestDetail> defaultInterestDetails = defaultInterestDetailService.findBy(paraMap);
                        for (DefaultInterestDetail interestDetail : defaultInterestDetails) {
                            defaultInterest = defaultInterest.add(interestDetail.getInterestBalance2().subtract(interestDetail.getRepaymentBalance() != null ? interestDetail.getRepaymentBalance() : BigDecimal.ZERO));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    _balance = _balance.add(feesBalance).add(defaultInterest);
                }else{
                    _balance = balance;
                }


                ///////
                RepaymentEvent repaymentEvent = new RepaymentEvent(loanApplication, loanPublish, repaymentPlan, payOrder.getPayId(), repaymentDate ,_balance, adminId,
                        repaymentAccountId, systemAccountId, repaymentDate, allInterest, ignoreDelay, commonFeesItems, breakFessItems, lendsRatioMap, lendsOrderMap, avcq,
                        this, creditorRightsService, loanFeesDetailService, loanApplicationFeesItemService, userAccountService, loanApplicationService, repaymentRecordService,
                        defaultInterestDetailService, rightsRepaymentDetailService, lendOrderService, rightsPrepayDetailService, lendOrderReceiveService, lendLoanBindingService,
                        feesItemService, lendOrderDetailFeesService, lendProductService,
                        loanPublishService, userInfoService, awardDetailService, userAccountOperateService, loanProductService, constantDefineCached, userOpenIdService, 
                        constantDefineService,isLastRepaymentPlan,isAhead,thePlan.getRepaymentPlanId(),rateLendOrderService,resutlMap,capitalFlowService,sch);
                repaymentEvent.setEventTriggerInfoService(eventTriggerInfoService);
                repaymentEvent.setTaskExecLogService(taskExecLogService);
                EventHandle.fireEvent(repaymentEvent);
                if(isAhead){
                	balance = balance.subtract(_balance);
                }else{
                	balance = repaymentEvent.getBalance();
                }
                
                if (!isAhead && repaymentEvent.getBalance().doubleValue() <= 0) {
    				break;
    			}

            }else{
                //提前还款部分
            	if (isAhead){
            		aheadPart.add(repaymentPlan);
            	}
            }
             //提前还款end

           
            count ++ ;
        }
        //todo 提前还款部分
        if (isAhead&&aheadPart.size()>0){

            AheadRepaymentEvent aheadRepaymentEvent = new AheadRepaymentEvent(loanApplication, loanPublish, aheadPart, payOrder.getPayId(), repaymentDate ,balance, adminId,
                    repaymentAccountId, systemAccountId, repaymentDate, allInterest, ignoreDelay, commonFeesItems, breakFessItems, lendsOrderMap, avcq,
                    this, creditorRightsService, loanFeesDetailService, loanApplicationFeesItemService, userAccountService, loanApplicationService, repaymentRecordService,
                    defaultInterestDetailService, rightsRepaymentDetailService, lendOrderService, rightsPrepayDetailService, lendOrderReceiveService, lendLoanBindingService,
                    feesItemService, lendOrderDetailFeesService, lendProductService,
                    loanPublishService, userInfoService, awardDetailService, userAccountOperateService, loanProductService, constantDefineCached, userOpenIdService, 
                    constantDefineService,rateLendOrderService,isAhead,thePlan,resutlMap,capitalFlowService,sch);

            aheadRepaymentEvent.setEventTriggerInfoService(eventTriggerInfoService);
            aheadRepaymentEvent.setTaskExecLogService(taskExecLogService);
            EventHandle.fireEvent(aheadRepaymentEvent);

        }
        //接入存管后暂不发放佣金
       /*List<RepaymentPlan> allRepaymentPlanList = this.findBy(parameters);
		for (int i = 0; i < repaymentIdList.size() ; i++) {
			Long repayId = repaymentIdList.get(i);
			RepaymentPlan repaymentPlan = findById(repayId);
			// 如果还清，进行佣金发放
			RepaymentPlan rpnew = findById(repaymentPlan.getRepaymentPlanId());
			if (rpnew.getPlanState() == RepaymentPlanStateEnum.COMPLETE
					.value2Char() ) {
				// 进行佣金分配和更新
				repayCommision(repaymentPlan.getSectionCode(),
						allRepaymentPlanList.size(), lendsOrderMap, avcq,
						loanPublish);
			}
			//如果是提前还款，还完本次佣金之后，修改预期最大佣金为实还佣金
			if(isAhead && (i == repaymentIdList.size()-1)){
				try {
					updateCommiProfitForIsAhead(lendsOrderMap);
				} catch (Exception e) {
					logger.error("【提前还款】更新佣金统计记录异常：", e);
				}
			}
		}*/
        userAccountOperateService.execute(avcq);
        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_WAITING.getValue()));
        scheduleService.updateSchedule(sch);
		return resutlMap ;
    }

	private void updateCommiProfitForIsAhead(Map<Long, Long> lendsOrderMap) {
		// 提前还款时，更新佣金记录
		for (Long lendOrderId : lendsOrderMap.keySet()) {
			LendOrder lendOrder = lendOrderService.findById(lendOrderId);
			List<CommitProfitVO> commitProfitList = commiProfitService
					.getCommiProfitByLendOrderId(lendOrder.getLendOrderId(),
							lendOrder.getLendUserId());
			try {
				logger.info("【提前还款】提前还款，更新佣金统计记录，佣金数据长度："
						+ commitProfitList.size());
				for (CommitProfitVO commi : commitProfitList) {
					CommiProfit com = new CommiProfit();
					com.setComiProId(commi.getComiProId());
					com.setShoulProfit(commi.getFactProfit());
					commiProfitService.updateCommiProfit(com);
					logger.info("【提前还款】更新佣金统计记录，推荐人：" + com.getUserId()
							+ "，佣金统计ID：" + com.getComiProId() + "，佣金应还金额和实还金额："
							+ commi.getFactProfit());
				}
			} catch (Exception e) {
				logger.error("【提前还款】更新佣金统计记录异常：", e);
			}
		}
	}

	/**
     * 进行佣金分配和更新
     * @param int sectionCode 期号
     * @param int allCode 总分期数
     * @param Map lendsOrderMap 订单信息
     * */
    @Transactional
	public void repayCommision(int sectionCode, int allCode,
			Map<Long, Long> lendsOrderMap, AccountValueChangedQueue avcq,
			LoanPublish loanPublish) {
		for (Long lendOrderId : lendsOrderMap.keySet()) {
			LendOrder lendOrder = lendOrderService.findById(lendOrderId);
			// 更新统计表，获取实还金额小于应还金额的统计数据
			List<CommitProfitVO> commiProfitList = commiProfitService
					.getCommiProfitByLendOrderId(lendOrderId,
							lendOrder.getLendUserId());
			// 本次实还佣金
			BigDecimal factProfit = BigDecimal.ZERO;
			// 如果是最后一期,实还金额等于应还金额减已还金额
			for (CommitProfitVO com : commiProfitList) {
				if (com.getCommiPaidNode().equals(DarCommiPaidNodeEnum.REPAYMENT.getValue())) {
					// 推荐人ID
					Long inviterId = com.getUserId();
					factProfit = BigDecimal.ZERO;// 清零
					// 此次实还=此次总实还-目前为止已还金额
					CommiProfit commi = commiProfitService.selectByPrimaryKey(com.getComiProId());
					
					//如果出借人不在参与白名单，不返还佣金，佣金结算完毕
					int countWT = whiteTabsService.countUserId(com.getUserId());
					if(countWT==0){
						commi.setShoulProfit(commi.getFactProfit());
						commiProfitService.updateCommiProfit(commi);
						continue;
					}
					
					if (sectionCode == allCode) {
						BigDecimal thisFactProfit = com.getShoulProfit();
						factProfit = BigDecimalUtil.down(thisFactProfit.subtract(com.getFactProfit()), 2);
						commi.setFactProfit(thisFactProfit);
						// 如果不是最后一期，实还金额等于订单金额乘以当前用户级别的佣金比例
					} else {
						BigDecimal thisFactProfit = BigDecimal.ZERO;
						factProfit = BigDecimalUtil.down(
								com.getShoulProfit()
										.multiply(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(allCode), 18, BigDecimal.ROUND_CEILING)), 2);
						thisFactProfit = BigDecimalUtil.down(factProfit.add(com.getFactProfit()), 2);
						commi.setFactProfit(thisFactProfit);
					}
					commiProfitService.updateCommiProfit(commi);
					// 生成佣金记录
					BigDecimal comiRate = BigDecimal.ZERO;
					switch (com.getDisLevel()) {
					case '1': {
						comiRate = com.getFirstRate();
						break;
					}
					case '2': {
						comiRate = com.getSecondRate();
						break;
					}
					case '3': {
						comiRate = com.getThirdRate();
						break;
					}
					}
					Commision commision = new Commision();
					commision.setBalance(factProfit);
					commision.setChangeDate(new Date());
					commision.setComiRate(comiRate);
					commision.setComiRatioBalance(lendOrder.getBuyBalance());
					commision.setComiRatioType(com.getCommiRatioType());
					commision.setLendOrderId(lendOrderId);
					commision.setLendOrderName(lendOrder.getLendOrderName());
					commision.setLendProductId(lendOrder.getLendProductId());
					commision.setRulesId(com.getRulesId());
					commision.setUserId(inviterId);
					commision.setUserLevel(String.valueOf(com.getDisLevel()));
					commisionService.insert(commision);
					Date now = new Date();
					// 流水
					long systemOperationIdAccountId = constantDefineCached.getSystemAccount().get(
							AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
					// 平台支出佣金
					String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.COMMISION_PAY, com.getLoginName(),
							loanPublish.getLoanTitle());
					AccountValueChanged avcPay = new AccountValueChanged(systemOperationIdAccountId, factProfit, factProfit,
							AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_COMMISION.getValue(),
							"Commision", VisiableEnum.DISPLAY.getValue(), commision.getCommisionId(),
							AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(), systemOperationIdAccountId, now, descPay, false);
					avcq.addAccountValueChanged(avcPay);
					// 推荐人用户收入佣金
					UserAccount cashUserAccount = userAccountService.getCashAccount(inviterId);
					String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.COMMISION_INCOME, loanPublish.getLoanTitle(),
							CommisionRatioType.COMI_REPAYMENT_CAPITAL.getDesc());
					AccountValueChanged avcIncome = new AccountValueChanged(cashUserAccount.getAccId(), factProfit, factProfit,
							AccountConstants.AccountOperateEnum.INCOM.getValue(),
							AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_COMMISION.getValue(), "Commision", VisiableEnum.DISPLAY.getValue(),
							commision.getCommisionId(), AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(), cashUserAccount.getAccId(),
							now, descIncome, true);
					avcq.addAccountValueChanged(avcIncome);
				}
			}
		}

	}

    @Override
    public void reCreateTurnRight(Long loanApplicationId){
    	List<CreditorRights> creditorRightsList = creditorRightsService.getByLoanApplicationId(loanApplicationId);
    	//筛选出此次还款涉及到的债权，寻找债权对应的退出中的省心计划，符合条件的债权重新申请转让
		List<Long> idList= new ArrayList<>();
		Map<Long,Long> idMap = new HashMap<>();
		for(CreditorRights creditorRights : creditorRightsList){
			if(creditorRights.getRightsState() == CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char()){
				LendOrderBidDetail detail = lendOrderBidDetailService.getLendOrderBidDetailByCreditorRightsId(creditorRights.getCreditorRightsId());
				idMap.put(detail.getLendOrderId(), detail.getLendOrderId());
			}
		}
		idList.addAll(idMap.keySet());
		List<Long> creditorRightIdList = lendOrderService.findQuitFinanceOrderByIds(idList);
		//重新生成债权转让申请
		for(Long creditorRightId : creditorRightIdList){
			financePlanProcessModule.reApplyTurnRights(creditorRightId);
		}
    }
    /**
     * 借款账户充值
     *
     * @param balance
     * @param adminId
     * @param loanApplication
     * @param repaymentUserAccount
     */
    public PayOrder income(BigDecimal balance, long adminId, LoanApplication loanApplication,
                           UserAccount repaymentUserAccount, String desc) {

        Date now = new Date();
        Pair payPair = new Pair(PayConstants.AmountType.OFFLINE.getValue(), balance);
        PayOrder payOrder = payService.addPayOrder(balance, now, repaymentUserAccount.getUserId(), PayConstants.BusTypeEnum.LOAN_INCOM, payPair);
        //借款合同充值
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setAdminId(adminId);
        rechargeOrder.setAmount(balance);
        rechargeOrder.setUserId(repaymentUserAccount.getUserId());
        rechargeOrder.setDesc(desc);
        rechargeOrder.setPayId(payOrder.getPayId());
        rechargeOrder.setChannelCode("2");//现金
        rechargeOrderService.recharge(rechargeOrder, repaymentUserAccount.getAccId(), AccountConstants.OwnerTypeEnum.LOAN.getValue(), loanApplication.getLoanApplicationId());
        return payOrder;
    }

	@Override
	public Pagination<RepaymentVO> getRepaymentList(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<RepaymentVO> pagination = new Pagination<RepaymentVO>();
		pagination.setCurrentPage(pageNo);
		pagination.setPageSize(pageSize);
		List<RepaymentVO> repayments = myBatisDao.getListForPaging("REPAYMENTPLAN.getRepaymentList", params, pageNo, pageSize);
		pagination.setRows(repayments);
		pagination.setTotal(myBatisDao.count("getRepaymentList", params));
		return pagination;
	}

	@Override
	public Pagination<RepaymentVO> getRepaymentDetailListByLoanAppId(int pageNo, int pageSize, Long loanApplicationId) {
		Pagination<RepaymentVO> pagination = new Pagination<RepaymentVO>();
		pagination.setCurrentPage(pageNo);
		pagination.setPageSize(pageSize);
		List<RepaymentVO> details = myBatisDao.getListForPaging("REPAYMENTPLAN.getRepaymentDetailListByLoanAppId", loanApplicationId, pageNo, pageSize);
		pagination.setRows(details);
		pagination.setTotal(myBatisDao.count("getRepaymentDetailListByLoanAppId", loanApplicationId));
		return pagination;
	}

    @Override
    public void sendRepaymentSuccessMsg(BigDecimal balance,LoanApplication loanApplication,RepaymentRecord repaymentRecord,RepaymentPlan repaymentPlan) {
       /* CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByLoanApplicationId(loanApplication.getLoanApplicationId());
        VelocityContext context = new VelocityContext();
        try{
            try {
                context.put("date", DateUtil.getDateLongMD(repaymentRecord.getFaceDate()));
            } catch (Exception e) {
                logger.error("生成还款成功短信失败",e);
            }
            context.put("amount",balance);
            context.put("sectionCode",repaymentPlan.getSectionCode());
            context.put("count",loanProductService.findById(loanApplication.getLoanProductId()).getCycleCounts());
            //生成还款短信，发送还款短信
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_REPAYMENT_SUCCESS_VM, context);
            smsService.sendMsg(basicSnapshot.getMobilePhone(),content);
        }catch (Exception e){
            logger.error("发送还款成功短信失败", e);
        }*/
    }

    @Override
    public void sendRepaymentEndMsg(String title,String mobileNo) {
        VelocityContext context = new VelocityContext();
        try{
            context.put("name",title);
            //生成还款短信，发送还款短信
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_REPAYMENT_END_VM, context);
            smsService.sendMsg(mobileNo,content);
        }catch (Exception e){
            logger.error("发送还款结束短信失败", e);
        }
    }
    
    @Override
    public void processOverduePenalty () {
    	
    	//更改为逾期状态
    	updateOverduePenlty();
    	
    	//交付违约金
    	processPenalty();
    	
    	//罚息明细
    	processDefaulInsterest();
    	
    }
    
    /**
     * 处理违约金
     * */
	private void processPenalty() {
		try {
			List<LoanFeeDetailVO> list = myBatisDao.getList("queryPenaltyData");
			RepaymentPlanServiceImpl bean = ApplicationContextUtil.getBean(RepaymentPlanServiceImpl.class);
			if (list != null && list.size() > 0) {
				for (LoanFeeDetailVO detail : list) {
					try {
						if (detail.getItemType() != null
								&& detail
										.getItemType()
										.equals(FeesItemTypeEnum.ITEMTYPE_DELAYREPAYMENT
												.getValue())
								&& detail.getLoanFeesDetailId() == null) {
							bean.handlePenalty(detail);
						}
					} catch (Exception e) {
						logger.error(
								"处理单条违约金数据异常，还款id："
										+ detail.getRepaymentPlanId()
										+ "， 异常原因：", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("处理违约金异常，异常原因：", e);
		}

	}

	@Transactional(rollbackFor=Exception.class)
	public void handlePenalty(LoanFeeDetailVO detail) throws Exception {
		// 查询还款状态，并锁表
		RepaymentPlan repay = findByIdBLock(detail.getRepaymentPlanId(),
				true);
		if (repay.getPlanState() != RepaymentPlanStateEnum.DEFAULT
				.getValueChar()) {
			return;
		}
		// 借款具有违约费，并且未生成过违约金记录，需要增加违约金
		LoanFeesDetail feeDetail = new LoanFeesDetail();
		LoanApplicationFeesItem applicationFeesItem = new LoanApplicationFeesItem();
		applicationFeesItem.setFeesRate(detail.getFeesRate());
		applicationFeesItem.setRadicesType(detail.getRadicesType());
		BigDecimal fees = getDefaultBalance(detail.getFeesRate(),
				detail.getRadicesType(), detail.getShouldCapital(),
				detail.getShouldInterest(), detail.getConfirmBalance(),
				detail.getLoanInterestBalance(), detail.getFactCalital(),
				detail.getFactInterest(), BigDecimal.ZERO, BigDecimal.ZERO);
		feeDetail.setFees(fees);
		feeDetail.setFees2(BigDecimalUtil.up(feeDetail.getFees(), 2));
		feeDetail.setFeesCycle(detail.getFeesCycle());
		feeDetail.setFeesState(FeesDetailEnum.UNPAY.value2Char());
		feeDetail.setLoanApplicationFeesItemId(detail
				.getLoanApplicationFeesItemId());
		feeDetail.setLoanApplicationId(detail.getLoanApplicationId());
		feeDetail.setSectionCode(detail.getSectionCode());
		loanFeesDetailService.insert(feeDetail);
	}

	/**
     * 处理罚息
     * */
	public void processDefaulInsterest() {
		try {
			FeesItemTypeEnum.ITEMTYPE_DEFAULTINTREST.getValue();
			List<LoanFeeDetailVO> detailList = myBatisDao
					.getList("queryDefaultInterestData");
			String pattern = "yyyy-MM-dd";
			String currentStr = DateUtil.getFormattedDateUtil(new Date(
					new Date().getTime() - 24 * 60 * 60 * 1000), pattern);
			RepaymentPlanServiceImpl bean = ApplicationContextUtil
					.getBean(RepaymentPlanServiceImpl.class);
			for (LoanFeeDetailVO detail : detailList) {
				try {
					if (detail.getItemType() != null
							&& detail.getItemType().equals(
									FeesItemTypeEnum.ITEMTYPE_DEFAULTINTREST
											.getValue())) {
							bean.handleDefaultInterest(detail, currentStr,
									pattern);
					}
				} catch (Exception e) {
					logger.error(
							"处理罚息异常，当前还款计划id：" + detail.getRepaymentPlanId()
									+ " ， 异常原因：", e);
				} 
			}
		} catch (Exception e) {
			logger.error("处理罚息异常，异常原因：", e);
		}

	}
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = Exception.class)
	public void handleDefaultInterest(LoanFeeDetailVO detail,
			String currentStr, String pattern) throws Exception {
		RepaymentPlan repay = findByIdBLock(detail.getRepaymentPlanId(),
				true);
		if (repay.getPlanState() != RepaymentPlanStateEnum.DEFAULT
				.getValueChar()) {
			return;
		}
		// 罚息类型不为空，进行罚息计算
		Map parameters = new HashMap();
		parameters.put("repaymentPlanId", detail.getRepaymentPlanId());
		List<DefaultInterestDetail> interestList = defaultInterestDetailService.findBy(parameters);
		// 筛选出需要计算罚息的日期
		List<String> dateList = filterSumFeesDate(interestList,
				detail.getRepaymentDay(), currentStr, pattern);
		for (String selectDayStr : dateList) {
			DefaultInterestDetail newInterest = new DefaultInterestDetail();
			newInterest.setCustomerAccountId(detail.getAccountId());
			BigDecimal balance = getDefaultBalance(detail.getFeesRate(),
					detail.getRadicesType(), detail.getShouldCapital(),
					detail.getShouldInterest(), detail.getConfirmBalance(),
					detail.getLoanInterestBalance(), detail.getFactCalital(),
					detail.getFactInterest(), BigDecimal.ZERO, BigDecimal.ZERO);
			newInterest.setInterestBalance(balance);
			newInterest.setInterestBalance2(BigDecimalUtil.up(balance, 2));
			newInterest.setInterestCapital(detail.getShouldCapital());
			newInterest.setInterestDate(DateUtil.parseStrToDate(selectDayStr,
					pattern));
			newInterest.setInterestRatio(detail.getFeesRate());
			newInterest.setLoanApplicationId(detail.getLoanApplicationId());
			newInterest.setRepaymentPlanId(detail.getRepaymentPlanId());
			newInterest
					.setRepaymentState(DefaultInterestDetail.REPAYMENTSTATE_UNCOMPLETE);
			newInterest.setRepaymentBalance(BigDecimal.ZERO);
			defaultInterestDetailService.insert(newInterest);
		}
	}
	
	private List<String> filterSumFeesDate(List<DefaultInterestDetail> interestList, Date repaymentDay, String yesterStr,
			String pattern) throws ParseException { 
		List<String> dateList = new ArrayList<String>();
		Map<String,String> dateMap = new HashMap<String,String>();
		for(DefaultInterestDetail detail : interestList ) {
			if(detail.getInterestDate()!=null){
				String interestDate = DateUtil.getFormattedDateUtil(detail.getInterestDate(), pattern);
				dateMap.put(interestDate,interestDate);
			}
		}
		int days = DateUtil.daysBetween(repaymentDay, DateUtil.parseStrToDate(yesterStr, pattern));
		if(days == interestList.size() && StringUtils.isNull(dateMap.get(yesterStr))){
			dateList.add(yesterStr);
			return dateList;
		}
		Date start = repaymentDay;
		String addDateStr = DateUtil.getFormattedDateUtil(start, pattern);
		String nowStr = DateUtil.getFormattedDateUtil(new Date(), pattern);
		for(;!addDateStr.equals(nowStr);addDateStr = DateUtil.getFormattedDateUtil(start, pattern)){
			if(StringUtils.isNull(dateMap.get(addDateStr))){
				dateList.add(addDateStr);
			}
			long addTime = start.getTime() + 24 * 60 * 60 * 1000;
			start = new Date(addTime);
		}
		return dateList;
	}

	@Override
	public BigDecimal getDefaultBalance(BigDecimal rate, int radicesType,
			BigDecimal currentCalital, BigDecimal currentInterest,
			BigDecimal allCalital, BigDecimal allInterest,
			BigDecimal currentPaidCalital, BigDecimal currentPaidInterest,
			BigDecimal currentProfit, BigDecimal allProfit) {
		BigDecimal result = BigDecimal.ZERO;

		BigDecimal feesRate = filterParams(rate).divide(new BigDecimal("100"), 18,
				BigDecimal.ROUND_CEILING);
		if (radicesType == RadiceTypeEnum.PRINCIPAL.value2Int()) {
			result = filterParams(allCalital).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.ALLINTEREST.value2Int()) {
			result = filterParams(allInterest).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.ALLPROFIT.value2Int()) {
			result = filterParams(allProfit).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.ALLPI.value2Int()) {
			result = filterParams(allCalital).add(filterParams(allInterest)).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.SUMPROFIT.value2Int()) {
			result = filterParams(currentProfit).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.CURRENTINTEREST.value2Int()) {
			result = filterParams(currentInterest).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.CURRENTPRINCIPAL.value2Int()) {
			result = filterParams(currentCalital).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.CURRENTPI.value2Int()) {
			result = filterParams(currentCalital).add(filterParams(currentInterest)).multiply(feesRate);
		} else if (radicesType == RadiceTypeEnum.CURRENTLEFTPI.value2Int()) {
			result = filterParams(currentCalital).subtract(filterParams(currentPaidCalital)).add(filterParams(currentInterest).subtract(filterParams(currentPaidInterest))).multiply(feesRate);
 		}

		return result;
	}
    
	private BigDecimal filterParams(BigDecimal numbers) {
			if(numbers == null){
				numbers = BigDecimal.ZERO;
			}
			return numbers;
	}

	/**
	 * 更新逾期的还款计划状态
	 * */
	private void updateOverduePenlty() {
		Date now = DateUtil.parseStrToDate(
				DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd"),
				"yyyy-MM-dd");
		List<Long> repayList = getOverduePenalty(now);
		if (repayList != null && repayList.size() > 0) {
			RepaymentPlanServiceImpl bean = ApplicationContextUtil
					.getBean(RepaymentPlanServiceImpl.class);
			for (Long repayId : repayList) {
				try {
					bean.handleOverDuePenltyState(repayId);
				} catch (Exception e) {
					logger.error(
							"更新逾期状态失败，逾期还款计划id：" + repayId	+ "，失败原因：", e);
				}
			}
		}
	}

	@Transactional(rollbackFor=Exception.class)
	private void handleOverDuePenltyState(Long repayId) throws Exception {
		RepaymentPlan repay = findByIdBLock(repayId, true);
		repay.setPlanState(RepaymentPlanStateEnum.DEFAULT
				.getValueChar());
		updateNTX(repay);
	}

	private  List<Long> getOverduePenalty(Date now){
    	return myBatisDao.getList("findOverdueByTime",now);
    }
	
	@Override
	public Long getTermDay(List<RightsRepaymentDetail> detailRightsList) {
		long termDay = 0;
		Date date = DateUtil.getShortDateWithZeroTime(new Date());
		// 一期
		if (detailRightsList.size() == 1) {
			termDay = (DateUtil.getShortDateWithZeroTime(detailRightsList.get(0).getRepaymentDayPlanned()).getTime() - date.getTime())
					/ (1000 * 3600 * 24) - 5;
		} else {
			// 多期
			for (int i = 0; i < detailRightsList.size(); i++) {
				RightsRepaymentDetail detailRights = detailRightsList.get(i);
				Date repaymentDayPlanned = DateUtil.getShortDateWithZeroTime(detailRights.getRepaymentDayPlanned());
				if (String.valueOf(detailRights.getIsPayOff()).equals(PayOffEnum.NO.getValue())) {
					try {
//						if (i == 0) {
//							int i1 = DateUtil.secondBetween(date, repaymentDayPlanned);
//							if (i1 >= 0) {
//								// 当前时间没有到达第一期还款时间
//								termDay = (int) Math.floor((repaymentDayPlanned.getTime() - date.getTime()) / (1000 * 3600 * 24)) - 5;
//								break;
//							}
//						} else if (i == (detailRightsList.size() - 1)) {
//							// 最后一次还款
//							termDay = (int) Math.floor((repaymentDayPlanned.getTime() - date.getTime()) / (1000 * 3600 * 24)) - 5;
//							break;
//						} else {
//							int i3 = DateUtil.secondBetween(date, repaymentDayPlanned);
//							if (i3 > 0) {
//								// 当前时间没有达到当前期
//								termDay = (int) Math.floor((repaymentDayPlanned.getTime() - date.getTime()) / (1000 * 3600 * 24)) - 5;
//								break;
//							}
//						}
						
						int i1 = DateUtil.secondBetween(date, repaymentDayPlanned);
						if (i1 >= 0) {
							termDay =  (repaymentDayPlanned.getTime() - date.getTime()) / (1000 * 3600 * 24)  - 5;
							break;
						}else{
							termDay = -1;
							break;
						}
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
					
			}
		}
		return termDay;
	}

	@Override
	public int getTheMinRepaymentForCreditorRights(long creditorRightsId) {
		return myBatisDao.get("REPAYMENTPLAN.getTheMinRepaymentForCreditorRights", creditorRightsId);
	}

	@Override
	@Transactional
	public void handleFinanceChildOrder(Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap) {
		BigDecimal zero = BigDecimal.ZERO ;
		for(Long accountId : resultMap.keySet()){
			boolean isExecuteCash = false; 
			UserAccount account = userAccountService.getUserAccountByAccId(accountId);
			AccountValueChangedQueue avcq = new AccountValueChangedQueue();
			//订单账户既是省心账户
			if(account.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
				UserAccount cashAccount = userAccountService.getCashAccount(account.getUserId());
				// valueMap 转账数值的map
				Map<Long,Map<String,BigDecimal>> valueMaps = resultMap.get(accountId) ;
				if(valueMaps == null){
					continue ;
				}
				for(Long rightsRepaymentId : valueMaps.keySet()){
					RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.findById(rightsRepaymentId);
					CreditorRights creditorRights = creditorRightsService.findById(rightsRepaymentDetail.getCreditorRightsId(), false) ;
					LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());
					// 该订单是省心计划子订单，订单收益回款至余额，省心计划不能在退出中
					if(lendOrder.getLendOrderPId() != null){
						LendOrder pOrder = lendOrderService.findById(lendOrder.getLendOrderPId());
						if (pOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
								&& pOrder.getAgreementEndDate().after(new Date())
								&&(pOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.HASPAID.getValue())
										||pOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue()) )
								&& pOrder.getProfitReturnConfig() != null 
								&& pOrder.getProfitReturnConfig().equals(LendOrderConstants.FinanceProfitReturnEnum.TO_CASH_ACCOUNT.getValue())) { 
							isExecuteCash = true ;
							//省心账户资金向县级账户转账  Map<String,BigDecimal> key:interest/capital/awards/fees
							Map<String,BigDecimal> caculaterMap = valueMaps.get(rightsRepaymentId);
							BigDecimal interestValue = caculaterMap.get(RightsRepaymentDetail.INTEREST)!=null?caculaterMap.get(RightsRepaymentDetail.INTEREST):zero ;
							BigDecimal awardsValue = caculaterMap.get(RightsRepaymentDetail.AWARDS)!=null?caculaterMap.get(RightsRepaymentDetail.AWARDS):zero ;
							BigDecimal feesValue = caculaterMap.get(RightsRepaymentDetail.FEES)!=null?caculaterMap.get(RightsRepaymentDetail.FEES):zero ;
							BigDecimal turnValue = interestValue.add(awardsValue).subtract(feesValue);
							BigDecimal turnValue2 = BigDecimalUtil.down(turnValue, 2);
							if( turnValue.compareTo(BigDecimal.ZERO) <= 0 || turnValue2.compareTo(BigDecimal.ZERO) <= 0){
								continue ;
							}
							if( turnValue.compareTo(account.getAvailValue()) > 0){
								logger.error("省心账户余额不足！");
								continue ;
							}
							//省心计划订单加锁
							LendOrder pOrderLock = lendOrderService.findAndLockById(lendOrder.getLendOrderPId());
							// 省心计划账户支出
							AccountValueChanged pay = new AccountValueChanged(
									accountId,
									turnValue,
									turnValue2,
									AccountConstants.AccountOperateEnum.PAY.getValue(),
									AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEOUT
											.getValue(),
									AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
									AccountConstants.VisiableEnum.DISPLAY.getValue(),
									pOrderLock.getLendOrderId(),
									AccountConstants.OwnerTypeEnum.USER.getValue(),
									account.getUserId(),
									new Date(),
									StringUtils
											.t2s(DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_PROFIT_TURN_OUT,
													turnValue2), false);
							AccountValueChanged income = new AccountValueChanged(
									cashAccount.getAccId(),
									turnValue,
									turnValue2,
									AccountConstants.AccountOperateEnum.INCOM.getValue(),
									AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEINTO
											.getValue(),
									AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT
											.getValue(),
									AccountConstants.VisiableEnum.DISPLAY.getValue(),
									pOrderLock.getLendOrderId(),
									AccountConstants.OwnerTypeEnum.USER.getValue(),
									cashAccount.getUserId(),
									new Date(),
									StringUtils
											.t2s(DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_PROFIT_QUIT_IN,
													turnValue2), false);
							avcq.addAccountValueChanged(pay);
							avcq.addAccountValueChanged(income);
						}
					}
				}
			}else{
				continue;
			}
			//如果符合省心计划收益回至现金账户的逻辑，则执行现金流操作
			if(isExecuteCash){
				userAccountOperateService.execute(avcq);
			}
		}
	}

	@Override
	public RepaymentPlanVO getRepaymentPlanByLoanApplicationId(Long loanApplicationId) {
		return myBatisDao.get("REPAYMENTPLAN.getRepaymentPlanByLoanApplicationId", loanApplicationId);
	}

}
