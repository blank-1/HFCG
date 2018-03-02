package com.xt.cfp.core.service.impl.crm;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CrmCustomerBus;
import com.xt.cfp.core.service.crm.CrmCustomerBusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CrmCustomerBusServiceImpl implements CrmCustomerBusService {

	private static Logger logger = Logger.getLogger(CrmCustomerBusServiceImpl.class);

	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;

	@Override
	public CrmCustomerBus selectByCondition(Long userId, Long pid) {
		Map map=new HashMap();
		map.put("userId",userId);
		map.put("pId",pid);
		return myBatisDaoRead.get("CRM_CUSTOMER_BUS.selectByCondition", map);
	}

	@Override
	public List<CrmCustomerBus> selectByPId(Long pid) {
		Map map=new HashMap();
		map.put("pId",pid);
		map.put("flag","yes");
		return myBatisDaoRead.getList("CRM_CUSTOMER_BUS.selectByCondition", map);
	}

	@Transactional
	@Override
	public void insertCrmCustomerBus(CrmCustomerBus crmCustomerBus) {
		myBatisDao.insert("CRM_CUSTOMER_BUS.insertSelective",crmCustomerBus);
	}

	@Transactional
	@Override
	public void updateCrmCustomerBus(CrmCustomerBus crmCustomerBus) {
		myBatisDao.update("CRM_CUSTOMER_BUS.updateByPrimaryKeySelective",crmCustomerBus);
	}
}
