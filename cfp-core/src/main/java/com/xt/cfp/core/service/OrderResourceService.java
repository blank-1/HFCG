package com.xt.cfp.core.service;

import java.util.Date;

import com.xt.cfp.core.pojo.OrderResource;

public interface OrderResourceService {
	/**
	 * 根据属性值查找对应的源对象
	 * @param desc
	 * @return
	 */
	OrderResource selectBYDesc(String desc);
	/**
	 * 增加订单来源
	 * @param orderId
	 * @param type
	 * @param resourceId
	 */
	void addResourceFrom(long orderId,long type,long resourceId,Date date);
}
