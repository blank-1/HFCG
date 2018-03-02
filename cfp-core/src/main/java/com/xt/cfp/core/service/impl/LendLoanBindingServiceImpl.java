package com.xt.cfp.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.LendProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.LendLoanBinding;
import com.xt.cfp.core.service.LendLoanBindingService;

@Service("lendLoanBindingService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class LendLoanBindingServiceImpl implements LendLoanBindingService {
	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public List<LendLoanBinding> fullLoanAndFeesByLendProductId(long lendProductId) {
		return myBatisDao.getList("LEND_LOAN_BINDING.fullLoanAndFeesByLendProductId", lendProductId);
	}

    @Override
    public List<LendLoanBinding> findByLendProductId(long lendProductId) {
        return myBatisDao.getList("LEND_LOAN_BINDING.findByLendProductId",lendProductId);
    }

    @Override
    public List<LendLoanBinding> findByLendAndLoan(Long lendProductId, Long loanProductId) {
        Map map = new HashMap();
        map.put("loanProductId",loanProductId);
        map.put("lendProductId",lendProductId);
        return myBatisDao.getList("LEND_LOAN_BINDING.findByLendAndLoan",map);
    }

    @Override
    public LendProduct findRightsProduct(long loanProductId) {
        LendLoanBinding lendLoanBinding = myBatisDao.get("LEND_LOAN_BINDING.findRightsProduct",loanProductId);
        return myBatisDao.get("LEND_PRODUCT.findById",lendLoanBinding.getLendProductId());
    }

	@Override
	public String getLoanProductDueTimeByLendProductId(Long lendProductId) {
		return myBatisDao.get("LEND_LOAN_BINDING.getLoanProductDueTimeByLendProductId", lendProductId);
	}

}
