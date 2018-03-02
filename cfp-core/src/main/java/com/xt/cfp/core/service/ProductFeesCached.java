package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendProductFeesItem;
import com.xt.cfp.core.pojo.LoanProductFeesItem;

public interface ProductFeesCached {
    public List<LoanProductFeesItem> getFeesItemsByLoanProductId(long loanProductId);

    public LoanProductFeesItem getLoanProductFeesItemById(long loanProductFeesItemId);

    public List<LendProductFeesItem> getFeesItemsByLendProductId(long lendProductId);

    public void resetLoanProductFeesItem();

    public void resetLendProductFeesItem();

    public void resetFeesItems();

    public FeesItem getFeesItemById(long feeItemsId);




}
