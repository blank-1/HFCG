package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RoleFunction;
import com.xt.cfp.core.pojo.SalesRoleFunction;
import com.xt.cfp.core.service.RoleFunctionService;
import com.xt.cfp.core.service.SalesRoleFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SalesRoleFunctionServiceImpl implements SalesRoleFunctionService {
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 根据角色ID获取角色权限列表
	 * @param roleId 角色ID
	 */
	@Override
	public List<SalesRoleFunction> getRoleFunctionByRoleId(Long roleId){
		return myBatisDao.getList("SL_ROLE_FUNCTION.getRoleFunctionByRoleId", roleId);
	}
	
	/**
	 * 添加角色权限
	 * @param roleFunction 角色权限对象
	 * @param functionIdStr 权限集合
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addRoleFunction(SalesRoleFunction roleFunction,String functionIdStr) {
		//获取选中的functionId  
		String[] functionIds;
		functionIds = functionIdStr.split("-");
		//对比表单提交的跟原来已有的，做增删操作
		List<SalesRoleFunction> roleFunctions = myBatisDao.getList("SL_ROLE_FUNCTION.getRoleFunctionByRoleId", roleFunction.getRoleId());
		List<Long> functionIdListSaved = new ArrayList();
		
		//页面选中的不包含保存的，执行删除
		List<String> functionIdList = Arrays.asList(functionIds);
		for(SalesRoleFunction roleFunctionSaved : roleFunctions) {
			//记录保存的functionId
			functionIdListSaved.add(roleFunctionSaved.getFunctionId());
			String savedId = roleFunctionSaved.getFunctionId() + "";
			if(!functionIdList.contains(savedId)){//判断保存的是否在获取过来数据中存在，不存在干掉，存在不动，行增的添加
				myBatisDao.delete("SL_ROLE_FUNCTION.deleteByPrimaryKey", roleFunctionSaved.getRoleFunctionId());
			}
		}
		
		if(functionIds != null && functionIds.length != 0 && !"".equals(functionIds[0])) {
			//保存的不包含页面选中的,执行新增
			for(String id : functionIds) {
				long functionId = Long.parseLong(id);
				if(!functionIdListSaved.contains(functionId)) {
					roleFunction.setFunctionId(functionId);
					myBatisDao.insert("SL_ROLE_FUNCTION.insert", roleFunction);
				}
			}
		}
	}
}
