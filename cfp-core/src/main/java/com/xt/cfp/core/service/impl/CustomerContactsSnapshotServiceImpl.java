package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CustomerContactsSnapshot;
import com.xt.cfp.core.service.CustomerContactsSnapshotService;

@Service
public class CustomerContactsSnapshotServiceImpl implements CustomerContactsSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 添加客户联系人
	 */
	@Override
	public CustomerContactsSnapshot addContacts(CustomerContactsSnapshot customerContactsSnapshot) {
		myBatisDao.insert("CUSTOMER_CONTACTS_SNAPSHOT.insert", customerContactsSnapshot);
		return customerContactsSnapshot;
	}

	/**
	 * 修改客户联系人
	 */
	@Override
	public CustomerContactsSnapshot updateContacts(CustomerContactsSnapshot customerContactsSnapshot) {
		myBatisDao.update("CUSTOMER_CONTACTS_SNAPSHOT.updateByPrimaryKey", customerContactsSnapshot);
		return customerContactsSnapshot;
	}

	/**
	 * 根据ID加载一条客户联系人
	 * @param snapshotId 联系人ID
	 */
	@Override
	public CustomerContactsSnapshot getContactsById(Long snapshotId) {
		return myBatisDao.get("CUSTOMER_CONTACTS_SNAPSHOT.selectByPrimaryKey", snapshotId);
	}

	/**
	 * 根据借款申请ID加载联系人列表
	 * @param loanApplicationId 借款申请ID
	 */
	@Override
	public List<CustomerContactsSnapshot> getContactsByloanApplicationId(Long loanApplicationId) {
		return myBatisDao.getList("CUSTOMER_CONTACTS_SNAPSHOT.getContactsByloanApplicationId", loanApplicationId);
	}
	
	// by mainid
	@Override
	public List<CustomerContactsSnapshot> getContactsByMainLoanApplicationId(Long mainLoanApplicationId) {
		return myBatisDao.getList("CUSTOMER_CONTACTS_SNAPSHOT.getContactsByMainLoanApplicationId", mainLoanApplicationId);
	}

	/**
	 * 根据ID删除联系人
	 * @param snapshotId 联系人ID
	 */
	@Override
	public void deleteById(Long snapshotId) {
		myBatisDao.delete("CUSTOMER_CONTACTS_SNAPSHOT.deleteByPrimaryKey", snapshotId);
	}
	
}
