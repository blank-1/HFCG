package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.CustomerContactsSnapshot;

public interface CustomerContactsSnapshotService {
	/**
	 * 添加客户联系人
	 */
	CustomerContactsSnapshot addContacts(CustomerContactsSnapshot customerContactsSnapshot);
	
	/**
	 * 修改客户联系人
	 */
	CustomerContactsSnapshot updateContacts(CustomerContactsSnapshot customerContactsSnapshot);
	
	/**
	 * 根据ID加载一条客户联系人
	 * @param snapshotId 联系人ID
	 */
	CustomerContactsSnapshot getContactsById(Long snapshotId);
	
	/**
	 * 根据借款申请ID加载联系人列表
	 * @param loanApplicationId 借款申请ID
	 */
	List<CustomerContactsSnapshot> getContactsByloanApplicationId(Long loanApplicationId);
	
	// by mainid
	List<CustomerContactsSnapshot> getContactsByMainLoanApplicationId(Long mainLoanApplicationId);
	
	/**
	 * 根据ID删除联系人
	 * @param snapshotId 联系人ID
	 */
	void deleteById(Long snapshotId);
}
