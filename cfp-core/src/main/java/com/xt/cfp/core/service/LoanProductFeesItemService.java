package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.LoanProductFeesItem;

public interface LoanProductFeesItemService {
	public List<LoanProductFeesItem> getByProductId(long productId) throws Exception;
	public List<ConstantDefine> findLoanProductFeesById(long productId);
	public List<LoanProductFeesItem> findAll();
}
