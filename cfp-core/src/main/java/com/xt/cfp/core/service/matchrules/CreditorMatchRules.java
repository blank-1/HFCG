package com.xt.cfp.core.service.matchrules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.xt.cfp.core.constants.CRMatchRuleEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.DueTimeTypeEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.constants.LenderTypeEnum;
import com.xt.cfp.core.constants.PublishRule;
import com.xt.cfp.core.constants.PublishTarget;
import com.xt.cfp.core.constants.SortByEnum;
import com.xt.cfp.core.constants.SortTypeEnum;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CreditorRightsDealDetail;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.LendLoanBinding;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanPublish;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.ext.LoanApplicationVO;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CreditorRightsTransferAppService;
import com.xt.cfp.core.service.LendLoanBindingService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.util.ApplicationContextUtil;
import com.xt.cfp.core.util.BigDecimalUtil;

/**
 * Created by ren yulin on 15-7-10.
 */
public abstract class CreditorMatchRules {

    @Autowired
    protected CreditorRightsService creditorRightsService;
    @Autowired
    protected LendProductService lendProductService;
    @Autowired
    protected LendLoanBindingService lendLoanBindingService;
    @Autowired
    protected LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    protected LendOrderService lendOrderService;
    @Autowired
    protected LoanPublishService loanPublishService;
    @Autowired
    protected LoanApplicationService loanApplicationService;
    @Autowired
    protected UserAccountService userAccountService;
    @Autowired
    public CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Value(value = "${BUY_RIGHTS_CRITICAL_POINT}")
    protected BigDecimal criticalPointBalance;

    @Value(value = "${BUY_RIGHTS_MIN_BALANCE}")
    protected BigDecimal minBalance;

    @Value(value = "${LAST_BUY_RIGHTS_BALANCE}")
    protected BigDecimal lastBuyBalance;
    
    /**
     * 自动匹配时浮动收益
     * */
    @Value(value = "${MATCH_FLOAT_PERSENT}")
    protected BigDecimal matchFloatPersent;

    public static List<MatchCreditorVO> match(LendOrder lendOrder, String ruleEnum) throws Exception {
        CRMatchRuleEnum matchRuleEnum = CRMatchRuleEnum.getEnumByValue(ruleEnum);
        CreditorMatchRules rules = ApplicationContextUtil.getBean(matchRuleEnum.name());
        return rules.execute(lendOrder);
    }

    public static void sortLoanProduct(List<LoanProduct> loanProducts, SortByEnum sortByEnum, SortTypeEnum sortTypeEnum) {
        if (sortByEnum.getValue().equals(SortByEnum.ANNUALRATE.getValue())) {
            if (sortTypeEnum.getValue().equals(SortTypeEnum.DESC.getValue())) {
                sortByAnnualRateDesc(loanProducts);
            } else {
                sortByAnnualRateAsc(loanProducts);
            }
        }
    }
    
    public static void sortLoanProduct(List<LoanProduct> loanProducts, SortByEnum sortByEnum, SortTypeEnum sortTypeEnum,Date now , Date endDate) {
        if (sortByEnum.getValue().equals(SortByEnum.ANNUALRATE.getValue())) {
            if (sortTypeEnum.getValue().equals(SortTypeEnum.DESC.getValue())) {
                sortByAnnualRateDesc(loanProducts);
            } else {
                sortByAnnualRateAsc(loanProducts);
            }
        }
        if (sortByEnum.getValue().equals(SortByEnum.TIMELIMIT.getValue())) {
        	if (sortTypeEnum.getValue().equals(SortTypeEnum.ASC.getValue())) {
        		sortByBuyLimitAsc(loanProducts, now , endDate);
        	} else {
//        		sortByBuyLimitAsc(loanProducts);
        	}
        }
    }

    public static void sortCreditor(List<Object> list,SortByEnum sortByEnum,SortTypeEnum sortTypeEnum) {
        if (sortByEnum.getValue().equals(SortByEnum.BALANCECREATEDATE.getValue())) {
            if (sortTypeEnum.getValue().equals(SortTypeEnum.ASC.getValue())) {
                sortByBalanceAndDateAsc(list);
            }
        }
    }

    /**
     * 将借款产品列表按年利率降序排列
     *
     * @param loanProducts
     */
    private static void sortByAnnualRateDesc(List<LoanProduct> loanProducts) {
        Collections.sort(loanProducts, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                LoanProduct lp1 = (LoanProduct) o1;
                LoanProduct lp2 = (LoanProduct) o2;
                try {
                    return BigDecimalUtil.compareTo(lp2.getAnnualRate(), lp1.getAnnualRate(), 0);
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    /**
     * 将借款产品列表按年利率升序排列
     *
     * @param loanProducts
     */
    private static void sortByAnnualRateAsc(List<LoanProduct> loanProducts) {
        Collections.sort(loanProducts, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                LoanProduct lp1 = (LoanProduct) o1;
                LoanProduct lp2 = (LoanProduct) o2;
                try {
                    return BigDecimalUtil.compareTo(lp1.getAnnualRate(), lp2.getAnnualRate(), 0);
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }
    
    /**
     * 将借款产品列表可投期限降序排列
     *
     * @param loanProducts
     */
    private static void sortByBuyLimitAsc(List<LoanProduct> loanProducts, final Date now , final Date endDate) {
        Collections.sort(loanProducts, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                LoanProduct lp1 = (LoanProduct) o1;
                LoanProduct lp2 = (LoanProduct) o2;
                Long de1 = 0L ;
                Long de2 = 0L ;
                Long de = endDate.getTime();
                if(lp1.getDueTimeType() == DueTimeTypeEnum.MONTH.value2Char()){
                	try {
                		Date current = now ;
                    	current.setMonth(current.getMonth() + lp1.getDueTime());
                    	de1 = current.getTime();
                    	current = now ;
                    	current.setMonth(current.getMonth() + lp2.getDueTime());
                    	de2 = current.getTime();
                    	if(de1 < de && de2 < de){
                    		return de2.compareTo(de1);
                    	}else if(de1 > de && de2 > de){
                    		return de1.compareTo(de2);
                    	}else{
                    		return de1.compareTo(de2);
                    	}
                    } catch (Exception e) {
                        return 0;
                    }
                }else if(lp1.getDueTimeType() == DueTimeTypeEnum.DAY.value2Char()){
                	try {
                    	de1 = now.getTime() + lp1.getDueTime()*24*60*60*1000 ;
                    	de2 = now.getTime() + lp2.getDueTime()*24*60*60*1000 ;
                    	if(de1 < de && de2 < de){
                    		return de2.compareTo(de1);
                    	}else if(de1 > de && de2 > de){
                    		return de1.compareTo(de2);
                    	}else{
                    		return de1.compareTo(de2);
                    	}
                    } catch (Exception e) {
                        return 0;
                    }
                }
                try {
                    return BigDecimalUtil.compareTo(lp2.getAnnualRate(), lp1.getAnnualRate(), 0);
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    /**
     * 将借款产品列表按可投期限升序排列
     *
     * @param loanProducts
     */
//    private static void sortByAnnualRateAsc(List<LoanProduct> loanProducts) {
//        Collections.sort(loanProducts, new Comparator<Object>() {
//            @Override
//            public int compare(Object o1, Object o2) {
//                LoanProduct lp1 = (LoanProduct) o1;
//                LoanProduct lp2 = (LoanProduct) o2;
//                try {
//                    return BigDecimalUtil.compareTo(lp1.getAnnualRate(), lp2.getAnnualRate(), 0);
//                } catch (Exception e) {
//                    return 0;
//                }
//            }
//        });
//    }

    /**
     * 给列表按可投金额与提交（提出转让）时间正序排列
     *
     * @param sumList
     */
    private static void sortByBalanceAndDateAsc(List<Object> sumList) {
        Collections.sort(sumList, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                BigDecimal b1 = BigDecimal.ZERO;
                Date d1 = null;
                if (o1 instanceof CreditorRights) {
                    CreditorRights r1 = (CreditorRights) o1;
                    b1 = r1.getLendPrice();
                    d1 = r1.getApplyRollOutTime();
                } else if (o1 instanceof LoanApplicationVO) {
                    LoanApplicationVO l1 = (LoanApplicationVO) o1;
                    b1 = l1.getConfirmBalance().subtract(l1.getRightsBalance());
                    d1 = l1.getCreateTime();
                }
                BigDecimal b2 = BigDecimal.ZERO;
                Date d2 = null;
                if (o2 instanceof CreditorRights) {
                    CreditorRights r2 = (CreditorRights) o2;
                    b2 = r2.getRightsWorth();
                    d2 = r2.getApplyRollOutTime();
                } else if (o2 instanceof LoanApplicationVO) {
                    LoanApplicationVO l2 = (LoanApplicationVO) o2;
                    b2 = l2.getConfirmBalance().subtract(l2.getRightsBalance());
                    d2 = l2.getCreateTime();
                }
                try {
                    int result = BigDecimalUtil.compareTo(b1, b2, true, 2);
                    if (result == 0) {
                        return d1.compareTo(d2);
                    } else {
                        return result;
                    }

                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    /**
     * 查询在该省心计划中，指定借款产品的出资占比
     *
     * @param lendOrder
     * @param loanProductId
     * @return
     */

    public BigDecimal getRatio(LendOrder lendOrder, long loanProductId) {
        Map<Long, Map<String, BigDecimal>> ratioMap = getCreditorRatioByProductId(lendOrder.getLendProductId(), lendOrder.getLendOrderId(), lendOrder.getBuyBalance(),lendOrder.getCustomerAccountId());
        BigDecimal ratio = BigDecimal.valueOf(0);
        if (ratioMap.containsKey(loanProductId)) {
            Map<String, BigDecimal> theMap = ratioMap.get(loanProductId);
            ratio = theMap.get("ratio");
        }
        return ratio;
    }

    public Map getCreditorRatioByProductId(LendOrder lendOrder) {
        return getCreditorRatioByProductId(lendOrder.getLendProductId(), lendOrder.getLendOrderId(), lendOrder.getBuyBalance(),lendOrder.getCustomerAccountId());
    }

    /**
     * 用户已投标总额
     * */
    public BigDecimal sumBalanceByUser(long userId, long loanApplicationId) {
        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(loanApplicationId);
        BigDecimal sumBalance = BigDecimal.valueOf(0);
        for (LendOrderBidDetail orderBidDetail : lendOrderBidDetails) {
            if (orderBidDetail.getStatus() == LendOrderBidStatusEnum.BIDING.value2Char()) {
                LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
                if (lendOrder.getLendUserId() == userId) {
                    sumBalance = sumBalance.add(orderBidDetail.getBuyBalance());
                }
            }

        }
        return sumBalance;
    }

    /**
     * 查询指定的省心计划中，各借款产品的占有比例及累积出资金额
     *
     * @param lendProductId
     * @param lendOrderId
     * @param balance
     * @return
     */

    public Map getCreditorRatioByProductId(long lendProductId, long lendOrderId, BigDecimal balance, long customerAccountId) {
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendProductId(lendProductId);
        Map<Long, Map<String, BigDecimal>> existRatio = new HashMap<Long, Map<String, BigDecimal>>();
        for (LendLoanBinding binding : lendLoanBindings) {

            BigDecimal sumCreditorsByLendOrderAndLoanProduct = creditorRightsService.getSumCreByOrderAndLoanPdt(binding.getLoanProductId(), lendOrderId,customerAccountId);
            if (sumCreditorsByLendOrderAndLoanProduct == null) {
                sumCreditorsByLendOrderAndLoanProduct = BigDecimal.ZERO;
            }
            Map resultMap = new HashMap();
            BigDecimal value = sumCreditorsByLendOrderAndLoanProduct.setScale(2, BigDecimal.ROUND_CEILING);
            resultMap.put("balance", value);
            try {
                if (BigDecimalUtil.compareTo(balance, new BigDecimal("0"), true, 2) > 0) {
                    BigDecimal decimal = sumCreditorsByLendOrderAndLoanProduct.divide(balance, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));

                    resultMap.put("ratio", decimal.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    resultMap.put("ratio", BigDecimal.ZERO);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("ratio", BigDecimal.ZERO);
            }
            existRatio.put(binding.getLoanProductId(), resultMap);
        }
        return existRatio;
    }

    protected void getSysTurnCre(Map<String, Object> loanCreditorParaMap, List<Object> sumList) {
        List<CreditorRights> creditorRightses = creditorRightsService.findRollOutRights(loanCreditorParaMap);
        for (CreditorRights rights : creditorRightses) {
            sumList.add(rights);
        }
    }

    protected void getNewLoanApp(Map<String, Object> loanAppParaMap, List<Object> sumList) {
        List<LoanApplicationVO> loanApplications = loanApplicationService.findByProductIdOrderBy((Long)loanAppParaMap.get("loanProductId"), loanAppParaMap);
        for (LoanApplicationVO application : loanApplications) {
            sumList.add(application);
        }
    }

    protected MatchCreditorVO addMatchCreditorVO(boolean isCreditor,boolean isFromChannel,long objId,BigDecimal balance,List<MatchCreditorVO> results) {
        if (BigDecimal.ZERO.compareTo(balance)<0){

            MatchCreditorVO matchCreditorVO = new MatchCreditorVO();
            matchCreditorVO.setBalance(balance);
            matchCreditorVO.setCreditorRights(isCreditor);
            matchCreditorVO.setTheId(objId);
            matchCreditorVO.setFromChannel(isFromChannel);

            results.add(matchCreditorVO);

            return matchCreditorVO;
        }
        return null;
    }

    /**
     *  计算可购买债权金额
     * @param balance
     * @param creditorRights
     * @param applyRecord
     * @return
     * @throws Exception

     */
    public BigDecimal getCanBuyBalance(BigDecimal balance,CreditorRights creditorRights,CreditorRightsTransferApplication applyRecord ) throws Exception {
        BigDecimal lendPrice = BigDecimal.ZERO;
        BigDecimal turnBalance = BigDecimal.ZERO;
        List<CreditorRightsDealDetail> creditorRightsDealDetails = creditorRightsTransferAppService.
        		getCreditorRightsDealDetailByTransferApplyId(applyRecord.getCreditorRightsApplyId(),CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus.SUCCESS);
        //检查债权是否可转让
        if (creditorRightsDealDetails!=null){
            for (CreditorRightsDealDetail dealDetail:creditorRightsDealDetails){
                turnBalance = turnBalance.add(dealDetail.getBuyBalance());
            }
        }
        if(applyRecord.getTransType().equals(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppType.MANUAL.getValue())){
            lendPrice = creditorRights.getLendPrice().subtract(turnBalance);//前台标价格不变
            if (BigDecimalUtil.compareTo(lendPrice, creditorRights.getRightsWorth(), 2) > 0) {
                //债权价格大于债权价值
                return BigDecimal.ZERO;
            }
        }else{
            //后台标价格等于债权价值
            lendPrice = creditorRights.getRightsWorth().subtract(turnBalance);
        }
        BigDecimal canBuyBalance = BigDecimal.ZERO;
        if (criticalPointBalance.compareTo(lendPrice)>0){
            //此债权不可拆，需全部转让(债权小于1万)
            if (BigDecimalUtil.compareTo(lendPrice, balance, 2) > 0) {
                return BigDecimal.ZERO;
            }
            canBuyBalance = lendPrice;
        }else{
            turnBalance = BigDecimal.ZERO;
            //此债权可以拆
                //剩余金额
                canBuyBalance = lendPrice.subtract(turnBalance);
                if (balance.compareTo(canBuyBalance)<0){
                    //无法拆解债权
                    if (balance.compareTo(canBuyBalance.subtract(lastBuyBalance))>0){
                    	//将可购买的金额转成100的整数倍
                        canBuyBalance = canBuyBalance.subtract(lastBuyBalance).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(100));
                    }else{
                        canBuyBalance = balance.divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(100));	//将可购买的金额转成100的整数倍
                    }
            }
        }

        return canBuyBalance;
    }

    protected List<MatchCreditorVO> getMatchCreditorVOs(LendOrder lendOrder, List<LoanProduct> loanProducts, boolean isHigh) throws Exception {

        List<MatchCreditorVO> results = new ArrayList<MatchCreditorVO>();
        UserAccount userAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
        BigDecimal forLendBalance = userAccount.getAvailValue2();
//        if (BigDecimal.ZERO.compareTo(forLendBalance.subtract(new BigDecimal(100)))>=0)
//            return results;


        //匹配新债权(标)
        if (BigDecimalUtil.compareTo(forLendBalance, BigDecimal.valueOf(100), 2) < 0) {
            return results;
        }
        Map<String, Object> loanAppParaMap = new HashMap<String, Object>();
        //todo crossChannel暂时忽略
        loanAppParaMap.put("createTimeForOrder", "asc");
        loanAppParaMap.put("forLoanBalance", " > 0");
        List<String> targets = new ArrayList<String>();
        targets.add(PublishTarget.BACKGROUND.getValue());
        targets.add(PublishTarget.FRONT.getValue());
        targets.add(PublishTarget.FRONT2BACKGROUND.getValue());
        loanAppParaMap.put("publishTargets", targets);
        loanAppParaMap.put("forLoanBalanceForOrder", " asc");
        for (LoanProduct loanProduct : loanProducts) {
            
            if (BigDecimalUtil.compareTo(forLendBalance, BigDecimal.valueOf(100), 2) < 0) {
                break;
            }
            //已投资金额和占比，当前产品已投资总额，用于计算限投金额
            BigDecimal thisProductLendBalance = forLendBalance;
          //此次计算投资总额
            BigDecimal theLoanProductSumBalance = BigDecimal.valueOf(0);

            List<Object> sumList = new ArrayList<Object>();

            //todoed 查询渠道转让的债权
            Map<String, Object> loanCreditorParaMap = new HashMap<String, Object>();
            loanCreditorParaMap.put("applyRollOutTime", "asc");
            loanCreditorParaMap.put("rightsWorth", "asc");
            loanCreditorParaMap.put("rightsState", CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.getValue());
            loanCreditorParaMap.put("lenderType", LenderTypeEnum.THECHANNEL.getValue());
            loanCreditorParaMap.put("loanProductId", loanProduct.getLoanProductId());
            List<String> ruleList = new ArrayList<>();
            ruleList.add(PublishRule.FIRST_AUTOMATIC.getValue());
            ruleList.add(PublishRule.ONLY_AUTOMATIC.getValue());
            loanCreditorParaMap.put("publishRule", ruleList);
//            loanCreditorParaMap.put("openTime", new Date());
            getSysTurnCre(loanCreditorParaMap, sumList);
            //todoed 查询未满标的新借款
            loanAppParaMap.put("loanProductId", loanProduct.getLoanProductId());
            loanAppParaMap.put("openTime", new Date());
            loanAppParaMap.put("oType","0");
            loanAppParaMap.put("publishRule",ruleList);
            getNewLoanApp(loanAppParaMap, sumList);
            sortCreditor(sumList, SortByEnum.BALANCECREATEDATE, SortTypeEnum.ASC);


            for (Object obj : sumList) {
                if (BigDecimalUtil.compareTo(forLendBalance, BigDecimal.valueOf(100), 2) < 0) {
                    break;
                }
                long loanApplicationId = 0l;
                if (obj instanceof CreditorRights) {

                    CreditorRights creditorRights = (CreditorRights) obj;
                    loanApplicationId = creditorRights.getLoanApplicationId();
                    LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
                    if (loanApplication.getLoanBalance() != null) {
                        //个人累计投标金额 投标中
                        BigDecimal userSumBalance = sumBalanceByUser(lendOrder.getLendUserId(), loanApplicationId);
                        //标的已投金额
                        BigDecimal bidedBalance = loanApplicationService.getBidedBalance(loanApplicationId);
                        //标的剩余金额
                        BigDecimal waitingBidBalance = creditorRights.getRightsWorth().subtract(bidedBalance);

                        LoanPublish loanPublish = loanPublishService.findById(loanApplicationId);
                        //标的限制-单人最大可投金额
                        if (loanPublish.getMaxBuyBalance() != null) {
                            if (BigDecimalUtil.compareTo(userSumBalance, loanPublish.getMaxBuyBalance(), 2) >= 0) {
                            	continue;
                            }
                        }

                        BigDecimal theTurnBalance = BigDecimal.ZERO;
                        //标的限制-单人剩余可投
                        BigDecimal lastUserBalance = loanPublish.getMaxBuyBalance().subtract(userSumBalance);
                        if (BigDecimalUtil.compareTo(thisProductLendBalance,lastUserBalance,2) < 0) {
                            theTurnBalance = thisProductLendBalance;
                        }else{
                            theTurnBalance = lastUserBalance;
                        }
                        
                        //标的起投金额限制
                        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanApplication.getLoanProductId());
                        if(lendProduct.getStartsAt().compareTo(thisProductLendBalance) > 0){
                        	continue ;
                        }

                        //订单剩余可投
                        if (BigDecimalUtil.compareTo(theTurnBalance, thisProductLendBalance, 2) >= 0){
                            theTurnBalance = thisProductLendBalance.divide(lendProduct.getUpAt()).setScale(0,BigDecimal.ROUND_DOWN).multiply(lendProduct.getUpAt());	//将可购买的金额转成lendProduct.getUpAt()的整数倍;
                        }

                        if (BigDecimalUtil.compareTo(theTurnBalance, waitingBidBalance, 2) >= 0) {
                            if (BigDecimalUtil.compareTo(userSumBalance.add(waitingBidBalance), loanPublish.getMaxBuyBalance(), 2) > 0) {
                            	continue;
                            }
                            matchCreditorProcess(lendOrder, addMatchCreditorVO(true, true, creditorRights.getCreditorRightsId(), waitingBidBalance, results));
                            forLendBalance = forLendBalance.subtract(waitingBidBalance);
                            thisProductLendBalance = thisProductLendBalance.subtract(waitingBidBalance);
                            theLoanProductSumBalance = theLoanProductSumBalance.add(waitingBidBalance);
//                            theLoanProductMaxBalance = theLoanProductMaxBalance.subtract(waitingBidBalance);
                        } else if(BigDecimalUtil.compareTo(theTurnBalance, BigDecimal.ZERO, 2) > 0&& BigDecimalUtil.compareTo(theTurnBalance, waitingBidBalance, 2) < 0){
                            if (BigDecimalUtil.compareTo(userSumBalance.add(theTurnBalance), loanPublish.getMaxBuyBalance(), 2) > 0) {
                            	continue;
                            }
                            matchCreditorProcess(lendOrder, addMatchCreditorVO(true, true, creditorRights.getCreditorRightsId(), theTurnBalance, results));
                            forLendBalance = forLendBalance.subtract(theTurnBalance);
                            thisProductLendBalance = thisProductLendBalance.subtract(theTurnBalance);
                            theLoanProductSumBalance = theLoanProductSumBalance.add(theTurnBalance);
//                            theLoanProductMaxBalance = theLoanProductMaxBalance.subtract(theTurnBalance);
                        }
                    }

                } else {
                    LoanApplication loanApplication = (LoanApplication) obj;
                    if (loanApplication.getConfirmBalance() != null) {
                        loanApplicationId = loanApplication.getLoanApplicationId();
                        BigDecimal userSumBalance = sumBalanceByUser(lendOrder.getLendUserId(), loanApplicationId);
                        BigDecimal bidedBalance = loanApplicationService.getBidedBalance(loanApplicationId);
                        BigDecimal waitingBidBalance = loanApplication.getConfirmBalance().subtract(bidedBalance);

                        LoanPublish loanPublish = loanPublishService.findById(loanApplicationId);
                        if (loanPublish.getMaxBuyBalance() != null) {
                            if (BigDecimalUtil.compareTo(userSumBalance, loanPublish.getMaxBuyBalance(), 2) > 0) {
                            	continue;
                            }
                        }

                        //借款标的单人剩余可投
                        BigDecimal theLendBalance = BigDecimal.ZERO;
                        BigDecimal lastUserBalance = loanPublish.getMaxBuyBalance().subtract(userSumBalance);
                        if (BigDecimalUtil.compareTo(thisProductLendBalance,lastUserBalance,2) < 0) {
                            theLendBalance = thisProductLendBalance;
                        }else{
                            theLendBalance = lastUserBalance;
                        }

                        //标的起投金额限制
                        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanApplication.getLoanProductId());
                        if(lendProduct.getStartsAt().compareTo(thisProductLendBalance) > 0){
                        	continue ;
                        }
                        
                        //省心计划分配产品的剩余可投
                        if (BigDecimalUtil.compareTo(theLendBalance, thisProductLendBalance, 2) >= 0){
                        	theLendBalance = thisProductLendBalance.divide(lendProduct.getUpAt()).setScale(0,BigDecimal.ROUND_DOWN).multiply(lendProduct.getUpAt());	//将可购买的金额转成lendProduct.getUpAt()的整数倍;
                        }

                        if (BigDecimalUtil.compareTo(theLendBalance, waitingBidBalance, 2) >= 0) {
                            if (BigDecimalUtil.compareTo(userSumBalance.add(waitingBidBalance), loanPublish.getMaxBuyBalance(), 2) > 0) {
                            	continue;
                            }
                            matchCreditorProcess(lendOrder, addMatchCreditorVO(false, false, loanApplicationId, waitingBidBalance, results));
                            forLendBalance = forLendBalance.subtract(waitingBidBalance);
                            thisProductLendBalance = thisProductLendBalance.subtract(waitingBidBalance);
                            theLoanProductSumBalance = theLoanProductSumBalance.add(waitingBidBalance);
//                            theLoanProductMaxBalance = theLoanProductMaxBalance.subtract(waitingBidBalance);
                        }else if (BigDecimalUtil.compareTo(theLendBalance, BigDecimal.ZERO, 2) > 0&& BigDecimalUtil.compareTo(theLendBalance, waitingBidBalance, 2) < 0){
                            if (BigDecimalUtil.compareTo(userSumBalance.add(theLendBalance), loanPublish.getMaxBuyBalance(), 2) > 0) {
                            	continue;
                            }

                           matchCreditorProcess(lendOrder, addMatchCreditorVO(false, false, loanApplicationId, theLendBalance, results));
                            forLendBalance = forLendBalance.subtract(theLendBalance);
                            thisProductLendBalance = thisProductLendBalance.subtract(theLendBalance);
                            theLoanProductSumBalance = theLoanProductSumBalance.add(theLendBalance);
//                            theLoanProductMaxBalance = theLoanProductMaxBalance.subtract(theLendBalance);
                        }
                    }
                }

            }

        }
            //修改订单待理财金额
//        Map<String, Object> lendOrderMap = new HashMap<String, Object>();
//        lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
        UserAccount financeAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
//        lendOrderMap.put("forLendBalance", financeAccount.getAvailValue2());
        lendOrder.setForLendBalance(financeAccount.getAvailValue2());
        lendOrderService.update(lendOrder);
        
        return results;
    }


    /**
     * 处理匹配的债权（标）
     * @param lendOrder
     * @param matchCreditorVO
     * @return
     * @throws Exception
     */
    BigDecimal matchCreditorProcess(LendOrder lendOrder, MatchCreditorVO matchCreditorVO) throws Exception{
        if(matchCreditorVO == null)
            return BigDecimal.ZERO;
        Date now = new Date();
        BigDecimal newRightBalance = BigDecimal.valueOf(0);
        if (matchCreditorVO.isCreditorRights()) {
            //如果是普通出借用户，则直接债权转让，如果是渠道债权，则进行普通投标处理
            if (!matchCreditorVO.isFromChannel()) {
                CreditorRightsTransferApplication transferApplication = creditorRightsTransferAppService.findById(matchCreditorVO.getTheId());
                //todoed 债权转让
                creditorRightsService.newRightsForFinanceLendOrder(lendOrder, matchCreditorVO, transferApplication);
            } else {
                CreditorRights creditorRights = creditorRightsService.findById(matchCreditorVO.getTheId(), false);
                matchCreditorVO.setTheId(creditorRights.getLoanApplicationId());
                loanApplicationService.newLendBidForFinanceLendOrder(lendOrder,matchCreditorVO);
            }
        } else {
            loanApplicationService.newLendBidForFinanceLendOrder(lendOrder,matchCreditorVO);
        }
        newRightBalance = newRightBalance.add(matchCreditorVO.getBalance());
        return newRightBalance;
    }

    protected abstract List<MatchCreditorVO> execute(LendOrder lendOrder) throws Exception;



    protected class CalcLoanProductSumBalance {
        private LendOrder lendOrder;
        private List<LoanProduct> loanProducts;

        public CalcLoanProductSumBalance(LendOrder lendOrder) {
            this.lendOrder = lendOrder;
        }

        public List<LoanProduct> getLoanProducts() {
            return loanProducts;
        }

        /**
         * 计算出该订单所有产品的最大可投金额和金额
         * loanProductSumBalanceMap 已投金额
         * loanProductMaxBalanceMap 最大可投金额
         * */
        public CalcLoanProductSumBalance invoke() {
            List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendProductId(lendOrder.getLendProductId());
            loanProducts = new ArrayList<LoanProduct>();
            for (LendLoanBinding lendLoanBinding : lendLoanBindings) {
                loanProducts.add(lendLoanBinding.getLoanProduct());
            }
            return this;
        }
    }
}
