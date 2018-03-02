package com.xt.cfp.core.service.matchrules.ext;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.SortByEnum;
import com.xt.cfp.core.constants.SortTypeEnum;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.matchrules.CreditorMatchRules;
import com.xt.cfp.core.service.matchrules.MatchCreditorVO;

/**
 * Created by ren yulin on 15-7-10.
 */
@Service("MAXLIMIT")
public class MaxLimitMatchRules extends CreditorMatchRules {

    @Autowired
    public CreditorRightsService creditorRightsService;

    @Override
    @Transactional
    public List<MatchCreditorVO> execute(LendOrder lendOrder) throws Exception {
        //已出资占比
        CalcLoanProductSumBalance calcLoanProductSumBalance = new CalcLoanProductSumBalance(lendOrder).invoke();
        List<LoanProduct> loanProducts = calcLoanProductSumBalance.getLoanProducts();
        CreditorMatchRules.sortLoanProduct(loanProducts, SortByEnum.ANNUALRATE, SortTypeEnum.ASC,new Date(),lendOrder.getAgreementEndDate());

        return getMatchCreditorVOs(lendOrder, loanProducts,true);
    }



}
