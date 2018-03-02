package com.xt.cfp.core.service.impl.crm;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CrmCustomerBusHis;
import com.xt.cfp.core.service.crm.CrmCustomerBusHisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CrmCustomerBusHisServiceImpl implements CrmCustomerBusHisService {

	private static Logger logger = Logger.getLogger(CrmCustomerBusHisServiceImpl.class);

	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;

	@Transactional
	@Override
	public void updateOrInsertHis(CrmCustomerBusHis crmCustomerBusHis) {
		if(null!=crmCustomerBusHis.getId()){
			myBatisDao.insert("CRM_CUSTOMER_BUS_HIS.updateByPrimaryKeySelective",crmCustomerBusHis);
		}else{
			myBatisDao.insert("CRM_CUSTOMER_BUS_HIS.insertSelective",crmCustomerBusHis);
		}
	}

	@Override
	public List<CrmCustomerBusHis> selectByCondition(Map map) {
		return myBatisDaoRead.getList("CRM_CUSTOMER_BUS_HIS.selectByCondition",map);
	}
}
