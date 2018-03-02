package com.xt.cfp.core.service.crm;

import com.xt.cfp.core.pojo.CrmCustomerBus;

import java.util.List;

public interface CrmCustomerBusService {

	CrmCustomerBus selectByCondition(Long userId,Long pid);

	List<CrmCustomerBus> selectByPId(Long pid);

	void insertCrmCustomerBus(CrmCustomerBus crmCustomerBus);

	void updateCrmCustomerBus(CrmCustomerBus crmCustomerBus);

}
