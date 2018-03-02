package com.xt.cfp.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RoleFunction;
import com.xt.cfp.core.service.RoleFunctionService;

@Service
public class RoleFunctionServiceImpl implements RoleFunctionService {
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 根据角色ID获取角色权限列表
	 * @param roleId 角色ID
	 */
	@Override
	public List<RoleFunction> getRoleFunctionByRoleId(Long roleId){
		return myBatisDao.getList("ROLE_FUNCTION.getRoleFunctionByRoleId", roleId);
	}
	
	/**
	 * 添加角色权限
	 * @param roleFunction 角色权限对象
	 * @param functionIdStr 权限集合
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addRoleFunction(RoleFunction roleFunction,String functionIdStr) {
		//获取选中的functionId  
		String[] functionIds;
		functionIds = functionIdStr.split("-");
		//对比表单提交的跟原来已有的，做增删操作
		List<RoleFunction> roleFunctions = myBatisDao.getList("ROLE_FUNCTION.getRoleFunctionByRoleId", roleFunction.getRoleId());
		List<Long> functionIdListSaved = new ArrayList();
		
		//页面选中的不包含保存的，执行删除
		List<String> functionIdList = Arrays.asList(functionIds);
		for(RoleFunction roleFunctionSaved : roleFunctions) {
			//记录保存的functionId
			functionIdListSaved.add(roleFunctionSaved.getFunctionId());
			String savedId = roleFunctionSaved.getFunctionId() + "";
			if(!functionIdList.contains(savedId)){//判断保存的是否在获取过来数据中存在，不存在干掉，存在不动，行增的添加
				myBatisDao.delete("ROLE_FUNCTION.deleteByPrimaryKey", roleFunctionSaved.getRoleFunctionId());
			}
		}
		
		if(functionIds != null && functionIds.length != 0 && !"".equals(functionIds[0])) {
			//保存的不包含页面选中的,执行新增
			for(String id : functionIds) {
				long functionId = Long.parseLong(id);
				if(!functionIdListSaved.contains(functionId)) {
					roleFunction.setFunctionId(functionId);
					myBatisDao.insert("ROLE_FUNCTION.insert", roleFunction);
				}
			}
		}
	}
}
