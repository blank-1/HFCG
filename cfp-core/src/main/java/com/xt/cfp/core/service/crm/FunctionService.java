package com.xt.cfp.core.service.crm;

import java.util.List;

import com.xt.cfp.core.pojo.CrmFunction;
import com.xt.cfp.core.pojo.ext.crm.MenusVO;

public interface FunctionService {
	
	/**
	 * 找出所有主菜单及二级菜单
	 * @return
	 */
	List<MenusVO> findAllMainMenus(Long staffId);
	
	/**
	 * 根据父级id找出所有子菜单
	 * @param parentId
	 * @return
	 */
	List<CrmFunction> findChildMenus(Long parentId);
	
	/**
	 * 根据父级id找出所有子功能
	 * @param parentId
	 * @return
	 */
	List<CrmFunction> findChildFuncs(Long parentId);
	
	/**
	 * 根据id找出对应的菜单
	 * @param parentId
	 * @return
	 */
	CrmFunction findById(Long funId);
	
	/**
	 * 保存功能
	 * @param funId
	 * @param pid
	 * @param code
	 * @param name
	 * @param type
	 * @param url
	 * @return
	 */
	String saveFuntion(Long funId,Long pid,String code,String name,String type,String url);
	
	/**
	 * 删除功能
	 * @param funId
	 * @return
	 */
	String delFunction(Long funId);
	
	/**
	 * 动态加载子菜单
	 * @param funId
	 * @return
	 */
	List<MenusVO> loadMenus(String funId);
	
	/**
	 * 动态加载所有功能
	 * @param funId
	 * @return
	 */
	List<MenusVO> loadFuncs(String funId);
	
	/**
	 * 根据角色id找出权限
	 * @param roleId
	 * @return
	 */
	List<CrmFunction> findFuncByRoleId(Long roleId);
	
	/**
	 * 根据给定的功能查找其所有级联的父级功能及菜单
	 * @param funcs
	 * @return
	 */
	List<CrmFunction> findFuncAndParentFunc(List<CrmFunction> funcs);
	
	/**
	 * 查询出所有功能
	 * @return
	 */
	List<CrmFunction> findAllFuncs();
}
