package com.xt.cfp.wechat.controller;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.constants.DueTimeTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.SortByEnum;
import com.xt.cfp.core.constants.SortTypeEnum;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanProductFeesItem;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LoanProductFeesItemService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.matchrules.CreditorMatchRules;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ren yulin on 15-7-18.
 */
@Controller
@RequestMapping(value = "/calculator")
public class InterestController  extends BaseController {
    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private LoanProductFeesItemService loanProductFeesItemService;
    @Autowired
    private FeesItemService feesItemService;

    /**
     * 出借计算器
     * @param balance 出借金额
     * @param annualRate   年利率
     * @param months  出借期限
     * @param method 还款方式：1等额本金，2等额本息，3周期付息到期还款
     * @return
     * @throws Exception
     */
    @RequestMapping("/lend")
    @DoNotNeedLogin
    @ResponseBody
    public Object lend(@RequestParam(value = "balance", required = true) BigDecimal balance,
                       @RequestParam(value = "annualRate", required = false) BigDecimal annualRate,
                       @RequestParam(value = "months", required = false) int months,
                       @RequestParam(value = "method", required = false) char method
                       ) throws Exception {
        char repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST;
        char repaymentMethod = InterestCalculation.REPAYMENTMETHOD_AVERAGECAPITALPLUSINTEREST;
        if (method == '1') {
            repaymentMethod = InterestCalculation.REPAYMENTMETHOD_AVERAGECAPITAL;
            repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST;
        } else if (method == '2') {
            repaymentMethod = InterestCalculation.REPAYMENTMETHOD_AVERAGECAPITALPLUSINTEREST;
            repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST;
        } else if (method == '3') {
            repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL;
        }
        Map<Integer, Map<String, BigDecimal>> mapData = InterestCalculation.getCalitalAndInterest(balance, annualRate, DueTimeTypeEnum.MONTH.value2Char(), repaymentMethod, repaymentType,
                InterestCalculation.REPAYMENTCYCLE_MONTH, months, 1);
        Iterator<Integer> iterator = mapData.keySet().iterator();
        while (iterator.hasNext()) {
            int sec = iterator.next();
            Map<String,BigDecimal> theMap = mapData.get(sec);
            theMap.put("balance",BigDecimalUtil.down(theMap.get("balance"),2));
            theMap.put("calital",BigDecimalUtil.down(theMap.get("calital"),2));
            theMap.put("interest",BigDecimalUtil.down(theMap.get("interest"),2));
        }
        BigDecimal allInterest = InterestCalculation.getAllInterest(balance, annualRate, DueTimeTypeEnum.MONTH.value2Char(), repaymentMethod, repaymentType,
                InterestCalculation.REPAYMENTCYCLE_MONTH, months, 1);
//
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("balance",balance);
        map.put("interest",BigDecimalUtil.down(allInterest,2));
        map.put("all",BigDecimalUtil.down(balance.add(allInterest),2));
        map.put("list",mapData.entrySet());
        return map;
    }

    /**
     * 借款计算器
     * @param balance
     * @param annualRate
     * @param months
     * @param method
     * @return
     * @throws Exception
     */
    @RequestMapping("/loan")
    @DoNotNeedLogin
    @ResponseBody
    public Object loan(@RequestParam(value = "balance", required = true) BigDecimal balance,
                       @RequestParam(value = "annualRate", required = false) BigDecimal annualRate,
                       @RequestParam(value = "months", required = false) int months,
                       @RequestParam(value = "method", required = false) char method
    ) throws Exception {
        char repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST;
        char repaymentMethod = InterestCalculation.REPAYMENTMETHOD_AVERAGECAPITALPLUSINTEREST;
        if (method == '1') {
            repaymentMethod = InterestCalculation.REPAYMENTMETHOD_AVERAGECAPITAL;
            repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST;
        } else if (method == '2') {
            repaymentMethod = InterestCalculation.REPAYMENTMETHOD_AVERAGECAPITALPLUSINTEREST;
            repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST;
        } else if (method == '3') {
            repaymentType = InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL;
        }
        Map<Integer, Map<String, BigDecimal>> mapData = InterestCalculation.getCalitalAndInterest(balance, annualRate, DueTimeTypeEnum.MONTH.value2Char(), repaymentMethod, repaymentType,
                InterestCalculation.REPAYMENTCYCLE_MONTH, months, 1);
        Iterator<Integer> iterator = mapData.keySet().iterator();
        while (iterator.hasNext()) {
            int sec = iterator.next();
            Map<String,BigDecimal> theMap = mapData.get(sec);
            theMap.put("balance",BigDecimalUtil.down(theMap.get("balance"),2));
            theMap.put("calital",BigDecimalUtil.down(theMap.get("calital"),2));
            theMap.put("interest",BigDecimalUtil.down(theMap.get("interest"),2));
        }
        BigDecimal allInterest = InterestCalculation.getAllInterest(balance, annualRate, DueTimeTypeEnum.MONTH.value2Char(), repaymentMethod, repaymentType,
                InterestCalculation.REPAYMENTCYCLE_MONTH, months, 1);
//
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("balance",balance);
        map.put("interest",allInterest);
        map.put("all",balance.add(allInterest));
        map.put("list",mapData.entrySet());
        return map;
    }



    /**
     * @deprecated
     * 借款计算器
     * @param balance 借款期限
     * @param months  借款期数
     * @return
     */
    @RequestMapping("/theLoan")
    @DoNotNeedLogin
    @ResponseBody
    public Object theLoan(@RequestParam(value = "balance", required = false) BigDecimal balance,
                       @RequestParam(value = "months", required = false) Integer months) {
        if (months==null){
            throw new SystemException(SystemErrorCode.PARAM_MISS);
        }
        if(balance==null){
            throw new SystemException(SystemErrorCode.PARAM_MISS);
        }
        Map<Integer,Map<String,BigDecimal>> map = null;
        Map<String,BigDecimal> resultMap = new HashMap<String,BigDecimal>();
        try {
            List<LoanProduct> loanProducts = loanProductService.findByDueTimeMonth(months);
            map = new HashMap<Integer,Map<String,BigDecimal>>();
            BigDecimal zeroBigDecimal = BigDecimal.valueOf(0);
            if (loanProducts != null) {
                if (loanProducts.size() > 1) {
                    CreditorMatchRules.sortLoanProduct(loanProducts, SortByEnum.ANNUALRATE, SortTypeEnum.DESC);
                }
                LoanProduct loanProduct = loanProducts.get(0);
                try {
                    map = InterestCalculation.getCalitalAndInterest(balance, loanProduct.getAnnualRate(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BigDecimal allInterest = zeroBigDecimal;
                try {
                    allInterest = InterestCalculation.getAllInterest(balance, loanProduct.getAnnualRate(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    List<LoanProductFeesItem> productFeesItems =loanProductFeesItemService.getByProductId(loanProduct.getLoanProductId());
                    Iterator<Integer> monthInfos = map.keySet().iterator();
                    while (monthInfos.hasNext()) {
                        int theMonth = monthInfos.next();
                        Map<String,BigDecimal> monthMap = map.get(theMonth);
                        BigDecimal fees = BigDecimal.valueOf(0l);
                        for (LoanProductFeesItem productFeesItem:productFeesItems) {
                            if (productFeesItem.getChargeCycle() == FeesPointEnum.ATCYCLE.value2Char()) {
                                fees = fees.add(feesItemService.calculateFeesBalance(productFeesItem.getFeesItemId(),balance,allInterest,monthMap.get("calital"),monthMap.get("interest"), zeroBigDecimal, zeroBigDecimal));
                            }
                        }
                        monthMap.put("fees",fees);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            BigDecimal feesAndInterest = zeroBigDecimal;
            BigDecimal monthBalance = zeroBigDecimal;
            Iterator<Integer> monthInfos = map.keySet().iterator();
            while (monthInfos.hasNext()) {
                int theMonth = monthInfos.next();
                Map<String,BigDecimal> monthMap = map.get(theMonth);
                if (monthMap.containsKey("interest")) {
                    feesAndInterest = feesAndInterest.add(monthMap.get("interest"));
                }
                if (monthMap.containsKey("fees")) {
                    feesAndInterest = feesAndInterest.add(monthMap.get("fees"));
                }
                if (BigDecimalUtil.compareTo(monthBalance, zeroBigDecimal,2) == 0) {
                    monthBalance = monthMap.get("balance");
                }
            }
            resultMap.put("balance",balance);
            resultMap.put("fees",feesAndInterest);
            resultMap.put("monthBalance",monthBalance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;

    }


}
