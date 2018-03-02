package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanProductFeesItem;
import com.xt.cfp.core.util.Pagination;

public interface LoanProductService {
	public Pagination<LoanProduct> findAllByPage(int pageNo, int pageSize, LoanProduct loanProduct);
	public LoanProduct saveLoanProduct(LoanProduct loanProduct);
    public List<LoanProduct> findLoanApplicationState(long loanProductId);
    public LoanProduct findById(long loanProductId);
    public List<FeesItem> feesItemByLoanProductId(FeesItem feesItem);
    public void enableOrDisableLoanProduct(LoanProduct loanProduct);
    public List<LoanProduct> findAnnualRate();
    public List<LoanProduct> findDueTimeMonth();
    public List<LoanProduct> findDueTimeDay();
    public List<LoanProduct> doProductStateByName(LoanProduct loanproduct);
    public List<LoanProduct> doProductVersionByName(LoanProduct loanproduct);
    public LoanProduct addLoanProduct(LoanProduct loanProduct, List<LoanProductFeesItem> loanProductFeesItems);
    public LoanProduct updateLoanProduct(LoanProduct loanProduct, List<LoanProductFeesItem> loanProductFeesItems);
    public void updateByLendProductId(long lendProductId);
	public void modifyLoanProduct(LoanProduct loanProduct);
	public List<LoanProduct> findAll();
	public List<LoanProduct> findNoLendProduct();
	
	/**
	 * 获取所有有效借款产品列表
	 */
	List<LoanProduct> findAllByValid();

    List<LoanProduct> findByDueTimeMonth(int dueTimeMonth);

    List<Map> repaymentPlan(long loanProductId, BigDecimal balance);
    /**
     * 根据订单查询借款产品
     * @param lendOrderId
     * @return
     */
	public LoanProduct findLoanProductByOrerId(Long lendOrderId);
    
}
