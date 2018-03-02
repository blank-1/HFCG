package com.xt.cfp.core.service.crm;

import java.util.List;

import com.xt.cfp.core.pojo.CrmFranchise;
import com.xt.cfp.core.pojo.CrmOrgFranchise;
import com.xt.cfp.core.pojo.ext.crm.FranchiseVO;

public interface FranchiseService {
	
	/**
	 * 根据员工id查加盟商 如果有加盟商则只有所属加盟商，没有则查所有
	 * @param staffId
	 * @return
	 */
	List<CrmFranchise> findByStaffId(Long staffId);
	
	/**
	 * 查询所有加盟商
	 * @return
	 */
	List<CrmFranchise> findAllFranc();
	
	/**
	 * 查询所有没有组织机构的加盟商
	 * @return
	 */
	List<FranchiseVO> findAllHasNoOrg();
	
	/**
	 * 根据组织机构找出所有加盟商
	 * @param orgId
	 * @return
	 */
	List<FranchiseVO> findByOrgId(Long orgId);
	
	/**
	 * 更新加盟商的组织机构 (组织机构中添加加盟商--只插入)
	 * @param oId
	 * @param francId
	 */
	String updateFrancOrg(Long oId,Long francId,String name);
	
	/**
	 * 根据组织机构id查询第一个加盟商
	 * @param oId
	 * @return
	 */
	FranchiseVO selectTopOneFrancByOrgId(Long oId);
	
	/**
	 * 根据映射主键查询映射记录
	 * @param id
	 * @return
	 */
	CrmOrgFranchise selectOrgMappingById(Long id);
}