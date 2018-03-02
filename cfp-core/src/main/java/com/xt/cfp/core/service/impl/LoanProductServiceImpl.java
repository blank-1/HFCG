package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.xt.cfp.core.constants.DueTimeTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LoanProductFeesItemService;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.InterestCalculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanProductFeesItem;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.ProductFeesCached;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;

@Service
public class LoanProductServiceImpl implements LoanProductService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
    @Autowired
    private ProductFeesCached productFeesCached;
    @Autowired
    private LoanProductFeesItemService loanProductFeesItemService;
    @Autowired
    private FeesItemService feesItemService;

    @Override
    public Pagination<LoanProduct> findAllByPage(int pageNo, int pageSize, LoanProduct loanProduct) {
        Pagination<LoanProduct> pagination = new Pagination<LoanProduct>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<LoanProduct> loanProducts = myBatisDao.getListForPaging("findAllLoanProductByPage", loanProduct, pageNo, pageSize);
        pagination.setRows(loanProducts);
        pagination.setTotal(myBatisDao.count("findAllLoanProductByPage", loanProduct));
        return pagination;
    }
    
	@Override
	public LoanProduct saveLoanProduct(LoanProduct loanProduct){
		myBatisDao.insert("LOANPRODUCT.insert", loanProduct);
		return loanProduct;
	}
	
    @Override
    public List<LoanProduct> findLoanApplicationState(long loanProductId) {
        return myBatisDao.getList("LOANPRODUCT.findLoanApplicationState", loanProductId);
    }
    
    @Override
    public LoanProduct findById(long loanProductId) {
        return myBatisDao.get("LOANPRODUCT.findById", loanProductId);
    }
    
    @Override
    public List<FeesItem> feesItemByLoanProductId(FeesItem feesItem) {
        return myBatisDao.getList("FEESITEM.feesItemByLoanProductId", feesItem);
    }
    
    @Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
    public void enableOrDisableLoanProduct(LoanProduct loanProduct) {
    	myBatisDao.update("LOANPRODUCT.enableOrDisableLoanProduct", loanProduct);
    }
    
    @Override
    public List<LoanProduct> findAnnualRate() {
        return myBatisDao.getList("LOANPRODUCT.findAnnualRate");
    }
    
    @Override
    public List<LoanProduct> findDueTimeMonth() {
        return myBatisDao.getList("LOANPRODUCT.findDueTimeMonth");
    }
    
    @Override
    public List<LoanProduct> findDueTimeDay() {
        return myBatisDao.getList("LOANPRODUCT.findDueTimeDay");
    }
    
    @Override
    public List<LoanProduct> doProductStateByName(LoanProduct loanproduct) {
        return myBatisDao.getList("LOANPRODUCT.doProductStateByName");
    }
    
    @Override
    public List<LoanProduct> doProductVersionByName(LoanProduct loanproduct) {
        return myBatisDao.getList("LOANPRODUCT.doProductVersionByName");
    }
    
    @Override
    @Transactional(rollbackFor=Exception.class,propagation= Propagation.REQUIRED,readOnly = false)
    public LoanProduct addLoanProduct(LoanProduct loanProduct, List<LoanProductFeesItem> loanProductFeesItems) {
        loanProduct.setProductCode(StringUtils.getDateTimeRadomStr(3));
        loanProduct.setCreateTime(new Date());
        loanProduct.setLastMdfTime(new Date());
        loanProduct.setProductState(LoanProduct.PUBLISHSTATE_VALID);
        int cycleCounts = 0;
        if(loanProduct.getCycleValue() != 0){
            if (loanProduct.getDueTime() % loanProduct.getCycleValue() == 0) {
                cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue();
            } else {
                cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue() + 1;
            }
        }
        loanProduct.setCycleCounts(cycleCounts);
        myBatisDao.insert("LOANPRODUCT.insert", loanProduct);
        for (LoanProductFeesItem loanProductFeesItem:loanProductFeesItems) {
            loanProductFeesItem.setLoanProductId(loanProduct.getLoanProductId());
            myBatisDao.insert("LOANPRODUCTFEESITEM.insert", loanProductFeesItem);
        }
        productFeesCached.resetLoanProductFeesItem();
        return loanProduct;
    }
    
    @Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED,readOnly = false)
    public LoanProduct updateLoanProduct(LoanProduct loanProduct, List<LoanProductFeesItem> loanProductFeesItems) {
        loanProduct.setLastMdfTime(new Date());
        int cycleCounts = 0;
        if(loanProduct.getCycleValue() != 0){
            if (loanProduct.getDueTime() % loanProduct.getCycleValue() == 0) {
                cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue();
            } else {
                cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue() + 1;
            }
        }
        loanProduct.setCycleCounts(cycleCounts);
        myBatisDao.update("LOANPRODUCT.updateByPrimaryKeySelective", loanProduct);
        myBatisDao.delete("LOANPRODUCTFEESITEM.deleteById", loanProduct.getLoanProductId());
        for (LoanProductFeesItem loanProductFeesItem:loanProductFeesItems) {
            loanProductFeesItem.setLoanProductId(loanProduct.getLoanProductId());
            myBatisDao.insert("LOANPRODUCTFEESITEM.insert", loanProductFeesItem);
        }
        productFeesCached.resetLoanProductFeesItem();
        return loanProduct;
    }
    
    @Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void modifyLoanProduct(LoanProduct loanProduct) {
		myBatisDao.update("LOANPRODUCT.updateByPrimaryKeySelective", loanProduct);
	}

	@Override
	public void updateByLendProductId(long lendProductId) {
		myBatisDao.update("LOANPRODUCT.updateByLendProductId", lendProductId);
	}
	
	@Override
	public List<LoanProduct> findNoLendProduct() {
		return myBatisDao.getList("LOANPRODUCT.findNoLendProduct");
	}

	@Override
	public List<LoanProduct> findAll() {
		return myBatisDao.getList("LOANPRODUCT.findAll");
	}

	/**
	 * 获取所有有效借款产品列表
	 */
	@Override
	public List<LoanProduct> findAllByValid() {
		return myBatisDao.getList("LOANPRODUCT.findAllByValid");
	}

    @Override
    public List<LoanProduct> findByDueTimeMonth(int dueTimeMonth) {
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setDueTimeType(DueTimeTypeEnum.MONTH.value2Char());
        loanProduct.setDueTime(dueTimeMonth);
        return myBatisDao.getList("LOANPRODUCT.findAll",loanProduct);
    }

    @Override
    public List<Map> repaymentPlan(long loanProductId, BigDecimal balance) {
        LoanProduct loanProduct = this.findById(loanProductId);
        Map<Integer,Map<String,BigDecimal>> map = null;
        try {
            map = InterestCalculation.getCalitalAndInterest(balance, loanProduct.getAnnualRate(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigDecimal allInterest = BigDecimal.valueOf(0);
        try {
            allInterest = InterestCalculation.getAllInterest(balance, loanProduct.getAnnualRate(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map> list = new ArrayList<Map>();
        try {
            List<LoanProductFeesItem> productFeesItems = loanProductFeesItemService.getByProductId(loanProduct.getLoanProductId());
            Iterator<Integer> monthInfos = map.keySet().iterator();
            while (monthInfos.hasNext()) {
                int theMonth = monthInfos.next();
                Map<String, BigDecimal> monthMap = map.get(theMonth);
                Map<String,Object> theMap = new HashMap<String,Object>();
                BigDecimal fees = BigDecimal.valueOf(0l);
                for (LoanProductFeesItem productFeesItem : productFeesItems) {
                    if (productFeesItem.getChargeCycle() == FeesPointEnum.ATCYCLE.value2Char()) {
                        fees = fees.add(feesItemService.calculateFeesBalance(productFeesItem.getFeesItemId(), balance, allInterest, BigDecimalUtil.up(monthMap.get("calital"), 2), BigDecimalUtil.up(monthMap.get("interest"), 2), BigDecimal.valueOf(0), BigDecimal.valueOf(0)));
                    }
                }
                theMap.put("fees", fees);
                theMap.put("balance",monthMap.get("balance").add(fees));
                theMap.put("secCode",theMonth);
                theMap.put("calital",monthMap.get("calital"));
                theMap.put("interest",monthMap.get("interest"));
                theMap.put("balanceNofee", monthMap.get("balance"));
                list.add(theMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

	@Override
	public LoanProduct findLoanProductByOrerId(Long lendOrderId) {
		// TODO Auto-generated method stub selectLoanProductByOrderId
		return myBatisDao.get("selectLoanProductByOrderId", lendOrderId);
	}

}
