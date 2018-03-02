package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.OrderResource;
import com.xt.cfp.core.pojo.OrderResourceMapping;
import com.xt.cfp.core.service.OrderResourceService;

@Service
public class OrderResourceServiceImpl implements OrderResourceService {
	
	private static final Logger logger = Logger.getLogger(OrderResourceServiceImpl.class);

    @Autowired
    private MyBatisDao myBatisDao;

	@Override
	public OrderResource selectBYDesc(String desc) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("resourceDesc", desc);
		OrderResource os=myBatisDao.get("ORDERRESOURCE.getOrderResourceByCondition", map);
		return os;
	} 
	
	@Override
	public void addResourceFrom(long orderId, long type, long resourceId,Date date) {
		OrderResourceMapping om=new OrderResourceMapping(resourceId, type, orderId,date);
		myBatisDao.insert("ORDERRESOURCEMAPPING.insert", om);
	}
}
