package com.xt.cfp.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.constants.RateEnum;
import com.xt.cfp.core.pojo.RateProduct;
import com.xt.cfp.core.pojo.ext.RateProductVO;
import com.xt.cfp.core.util.Pagination;

/**
 * 
 * @author WangYaDong
 *
 */
public interface RateProductService {
	
	/**
	 * 更新
	 * @param now 
	 * */
	public void updateRateProductStatus(Date now);
   /**
    * 更新加息卷发放状态
    * @param now
    */
	public void updateRateUserStatus(Date now);
	
	/**
	 * 添加加息券产品
	 */
	RateProduct addRateProduct(RateProduct rateProduct);
	
	/**
	 * 修改加息券产品
	 */
	RateProduct updateRateProduct(RateProduct rateProduct);
	
	/**
	 * 根据ID加载一条加息券产品信息
	 * @param rateProductId 加息券产品ID
	 * @return
	 */
	RateProduct getRateProductById(Long rateProductId);
	
	/**
	 * 加息券产品分页列表
	 * @param pageNo
	 * @param pageSize
	 * @param rateProduct
	 * @param customParams
	 * @return
	 */
	Pagination<RateProductVO> getRateProductPaging(int pageNo, int pageSize, RateProduct rateProduct, Map<String, Object> customParams);
	
	/**
	 * 更改加息券产品状态
	 * @param rateProductId 加息券产品ID
	 * @param rpse 状态
	 */
	void changeRateProductStatus(Long rateProductId, RateEnum.RateProductStatusEnum rpse);
	
	/**
	 * 获取有效的加息券产品列表
	 * @return
	 */
	List<RateProduct> getEfficientRateProductList();
	
}
