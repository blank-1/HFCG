package com.xt.cfp.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendProductFeesItem;
import com.xt.cfp.core.pojo.LoanProductFeesItem;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanProductFeesItemService;
import com.xt.cfp.core.service.ProductFeesCached;

@Service
public class ProductFeesCachedImpl implements ProductFeesCached {
    private static Map<Long,List<LoanProductFeesItem>> loanFeesItemMap = null;
    private static Map<Long,List<LendProductFeesItem>> lendFeesItemMap = null;
    private static List<FeesItem> feesItems = null;


    @Autowired
    private FeesItemService feesItemService;
    @Autowired
    private LoanProductFeesItemService loanProductFeesItemService;
    @Autowired
    private LendProductService lendProductService;

    @Override
    public List<LoanProductFeesItem> getFeesItemsByLoanProductId(long loanProductId) {
        if (loanFeesItemMap == null) {
            getAllLoanProductFeesItem();
        }
        return loanFeesItemMap.containsKey(loanProductId) ? loanFeesItemMap.get(loanProductId) : new ArrayList<LoanProductFeesItem>();
    }

    @Override
    public LoanProductFeesItem getLoanProductFeesItemById(long loanProductFeesItemId) {
        if (loanFeesItemMap == null) {
            getAllLoanProductFeesItem();
        }
        for (List<LoanProductFeesItem> loanProductFeesItems:loanFeesItemMap.values()) {
            for (LoanProductFeesItem productFeesItem:loanProductFeesItems) {
                if (productFeesItem.getLpfiId() == loanProductFeesItemId) {
                    return productFeesItem;
                }
            }
        }
        return null;
    }


    @Override
    public List<LendProductFeesItem> getFeesItemsByLendProductId(long lendProductId) {
        if (lendFeesItemMap == null) {
            getAlllendProductFeesItem();
        }
        return lendFeesItemMap.get(lendProductId);
    }



    @Override
    public void resetLoanProductFeesItem() {
        loanFeesItemMap = null;
        getAllLoanProductFeesItem();
    }

    @Override
    public void resetLendProductFeesItem() {
        lendFeesItemMap = null;
        getAlllendProductFeesItem();
    }

    @Override
    public void resetFeesItems() {
        feesItems = null;
        feesItems = getAllFeesItems();
    }

    @Override
    public FeesItem getFeesItemById(long feeItemsId) {
//        if (feesItems == null) {
            feesItems = getAllFeesItems();
//        }
        for (FeesItem feesItem:feesItems) {
            if (feesItem.getFeesItemId() == feeItemsId) {
                return feesItem;
            }
        }
        return null;
    }

    private List<FeesItem> getAllFeesItems() {
        return feesItemService.findAll(null);
    }

    private void getAllLoanProductFeesItem() {
        List<LoanProductFeesItem> productFeesItems = loanProductFeesItemService.findAll();
        loanFeesItemMap = new HashMap<Long, List<LoanProductFeesItem>>();
        for (LoanProductFeesItem productFeesItem:productFeesItems) {
            List<LoanProductFeesItem> feesItemList = new ArrayList<LoanProductFeesItem>();
            if (loanFeesItemMap.get(productFeesItem.getLoanProductId()) == null) {
                feesItemList = new ArrayList<LoanProductFeesItem>();
            } else {
                feesItemList = loanFeesItemMap.get(productFeesItem.getLoanProductId());
            }
            feesItemList.add(productFeesItem);
            loanFeesItemMap.put(productFeesItem.getLoanProductId(), feesItemList);
        }
    }

    private void getAlllendProductFeesItem() {
        List<LendProductFeesItem> productFeesItems = lendProductService.findAllProductFeesItems();
        lendFeesItemMap = new HashMap<Long, List<LendProductFeesItem>>();
        for (LendProductFeesItem productFeesItem:productFeesItems) {
            List<LendProductFeesItem> feesItemList = null;
            if (lendFeesItemMap.get(productFeesItem.getLendProductId()) == null) {
                feesItemList = new ArrayList<LendProductFeesItem>();
            } else {
                feesItemList = lendFeesItemMap.get(productFeesItem.getLendProductId());
            }
            feesItemList.add(productFeesItem);
            lendFeesItemMap.put(productFeesItem.getLendProductId(), feesItemList);
        }
    }
}
