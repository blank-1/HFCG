package com.xt.cfp.core.service.crm;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.CrmFunction;
import com.xt.cfp.core.pojo.CrmRole;
import com.xt.cfp.core.pojo.ext.crm.RoleVO;
import com.xt.cfp.core.util.Pagination;

public interface RoleService {
	/**
	 * 根据角色id找出所有权限
	 * @param id
	 * @return
	 */
	List<CrmFunction> findFunctionByRoleId(Long id);

	/**
	 * 获得员工角色
	 * @param staffId
	 * @return
	 */
	public List<CrmRole> findRoleByStaffId(Long staffId);
	/**
	 * 查找角色
	 * @param pageNo
	 * @param pageSize
	 * @param name
	 * @return
	 */
	Pagination<RoleVO> findAllRolesAndFunction(int pageNo, int pageSize,String name);
	
	CrmRole findById(Long roleId);
	
	String save(Long roleId,String roleName,String desc,String type,String francId);
	
	/**
	 * 判断功能节点是否选中
	 * @param funcId
	 * @return
	 */
	boolean checkFuncs(Long funcId,Long roleId);
	
	/**
	 * 根据角色更新功能
	 * @param ids
	 * @param roleId
	 * @return
	 */
	String upadteAccredit(String ids,Long roleId);
	
	/**
	 * 根据加盟商id找出加盟商私有角色
	 * @param franId
	 * @return
	 */
	List<CrmRole> findByFrancId(Long franId);
	
	/**
	 * 查询所有公有角色
	 * @return
	 */
	List<CrmRole> findAllPublicRole();
	
	/**
	 * 查询所有角色
	 * @return
	 */
	List<CrmRole> selectAllRole();
}
