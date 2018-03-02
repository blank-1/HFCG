package com.xt.cfp.core.service.crm;

import com.xt.cfp.core.pojo.CrmCustomerBusHis;

import java.util.List;
import java.util.Map;

public interface CrmCustomerBusHisService {

    void updateOrInsertHis(CrmCustomerBusHis crmCustomerBusHis);

    List<CrmCustomerBusHis> selectByCondition(Map map);
}
