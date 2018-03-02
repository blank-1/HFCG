package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.LoanProductFeesItem;
import com.xt.cfp.core.service.LoanProductFeesItemService;

@Service
public class LoanProductFeesItemServiceImpl implements LoanProductFeesItemService {
    
	@Autowired
	private MyBatisDao myBatisDao;
	
	@Override
    public List<LoanProductFeesItem> getByProductId(long productId) throws Exception {
        return myBatisDao.getList("LOANPRODUCTFEESITEM.findByProductId", productId);
    }
	
    @Override
    public List<ConstantDefine> findLoanProductFeesById(long productId) {
        return myBatisDao.getList("LOANPRODUCTFEESITEM.findLoanProductFeesById", productId);
    }
    
    @Override
    public List<LoanProductFeesItem> findAll() {
    	return myBatisDao.getList("LOANPRODUCTFEESITEM.findAll");
    }
}
