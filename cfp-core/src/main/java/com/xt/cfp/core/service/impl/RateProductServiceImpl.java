/**
 * 
 */
package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.RateEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RateProduct;
import com.xt.cfp.core.service.RateProductService;
import com.xt.cfp.core.pojo.ext.RateProductVO;
import com.xt.cfp.core.util.Pagination;


/**
 * @author wangyadong
 *
 */
@Service
@Transactional
public class RateProductServiceImpl implements RateProductService {

	@Autowired
	private MyBatisDao myBatisDao ;
	/* (non-Javadoc)
	 * @see com.xt.cfp.core.service.RateUserService#findByRateProductId(java.lang.Long)
	 */

	/**
	 * 更新加息卷产品状态
	 */
	@Override
	public void updateRateProductStatus(Date now) {
		RateProduct rp = new RateProduct();
		rp.setEndDate(now);
		myBatisDao.update("RATE_PRODUCT.updateRateProductStatusByPrimaryKey",
				rp);
	}

	/**
	 * 更新加息卷发放状态
	 * 
	 * @param now
	 */
	@Override
	public void updateRateUserStatus(Date now) {
		RateProduct rp = new RateProduct();
		rp.setEndDate(now);
		 
		
		myBatisDao.update("RATE_PRODUCT.updateRateUsersStatusByPrimaryKey",
				rp);
	}
	
	@Override
	public RateProduct addRateProduct(RateProduct rateProduct) {
		myBatisDao.insert("RATE_PRODUCT.insert", rateProduct);
		return rateProduct;
	}

	@Override
	public RateProduct updateRateProduct(RateProduct rateProduct) {
		myBatisDao.update("RATE_PRODUCT.updateByPrimaryKey", rateProduct);
		return rateProduct;
	}

	@Override
	public RateProduct getRateProductById(Long rateProductId) {
		return myBatisDao.get("RATE_PRODUCT.selectByPrimaryKey", rateProductId);
	}

	@Override
	public Pagination<RateProductVO> getRateProductPaging(int pageNo, int pageSize, RateProduct rateProduct, Map<String, Object> customParams) {
		Pagination<RateProductVO> page = new Pagination<RateProductVO>();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rateProduct", rateProduct);
		params.put("customParams", customParams);
		int totalCount = this.myBatisDao.count("getRateProductPaging", params);
		List<RateProductVO> list = this.myBatisDao.getListForPaging("getRateProductPaging", params, pageNo, pageSize);
		page.setTotal(totalCount);
		page.setRows(list);
		return page;
	}
	
	@Override
	public void changeRateProductStatus(Long rateProductId, RateEnum.RateProductStatusEnum rpse) {
		//判断参数是否为null
		if (rateProductId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("rateProductId", "null");

		if (rpse == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("rpse", "null");

		//修改状态
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rateProductId", rateProductId);
		params.put("status", rpse.getValue());
		
		myBatisDao.update("RATE_PRODUCT.changeRateProductStatus", params);
	}
	
	@Override
	public List<RateProduct> getEfficientRateProductList() {
		return myBatisDao.getList("RATE_PRODUCT.getEfficientRateProductList", new Date());
	}
	
}
