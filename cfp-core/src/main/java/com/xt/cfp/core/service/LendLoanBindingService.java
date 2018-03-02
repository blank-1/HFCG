package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.LendLoanBinding;
import com.xt.cfp.core.pojo.LendProduct;

public interface LendLoanBindingService {

	// 关联费用和产品，查询适用债权
	public List<LendLoanBinding> fullLoanAndFeesByLendProductId(long lendProductId);

    List<LendLoanBinding> findByLendProductId(long lendProductId);

    List<LendLoanBinding> findByLendAndLoan(Long lendProductId, Long loanProductId);

    LendProduct findRightsProduct(long loanProductId);
    
    /**
     * 根据出借产品ID，查询已经匹配的借款产品期限时长范围(如：3-6)
     * @param lendProductId 出借产品ID 
     * @return 借款产品期限时长范围
     */
    String getLoanProductDueTimeByLendProductId(Long lendProductId);
}
