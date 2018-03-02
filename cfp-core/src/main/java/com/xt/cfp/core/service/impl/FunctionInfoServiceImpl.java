package com.xt.cfp.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.FunctionInfo;
import com.xt.cfp.core.pojo.FunctionUrl;
import com.xt.cfp.core.service.FunctionInfoService;

@Service
public class FunctionInfoServiceImpl implements FunctionInfoService {
	
	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 获取权限信息列表
	 */
	@Override
	public List<FunctionInfo> findAllFunction(){
		return myBatisDao.getList("FUNCTION_INFO.findAllFunction");
	}
	
	/**
	 * 根据权限ID加载一条数据
	 * @param functionId 权限ID
	 */
	@Override
	public FunctionInfo getFunctionById(Long functionId){
		return myBatisDao.get("FUNCTION_INFO.selectByPrimaryKey", functionId);
	}
	
	/**
	 * 根据角色ID获取权限列表
	 * @param roleId 角色ID
	 */
	@Override
	public List<FunctionInfo> getFunctionByRoleId(Long roleId){
		return myBatisDao.getList("FUNCTION_INFO.findFunctionByRoleId", roleId);
	}
	
	/**
	 * 根据权限父ID获取权限列表
	 * @param pFunctionId 权限父ID
	 */
	@Override
	public List<FunctionInfo> getFunctionByPId(Long pFunctionId){
		return myBatisDao.getList("FUNCTION_INFO.findFunctionByPid", pFunctionId);
	}
	
	/**
	 * 根据权限编码加载一条权限信息
	 * @param code 权限编码
	 */
	@Override
	public FunctionInfo getFunctionByCode(String code){
		return myBatisDao.get("FUNCTION_INFO.findFunctionByCode", code);
	}
	
	/**
	 * 添加权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects) {
		myBatisDao.insert("FUNCTION_INFO.insert",functionInfo);
		if(!urlJsonObjects.isEmpty()) {	//如果有Url，保存Url
			for(JSONObject jsonObject : urlJsonObjects) {
				FunctionUrl functionUrl = new FunctionUrl();
				functionUrl.setFunctionId(functionInfo.getFunctionId());
				functionUrl.setUrlInfo(jsonObject.getString("urlInfo"));
				myBatisDao.insert("FUNCTION_URL.insert",functionUrl);
			}
		}
	}
	
	/**
	 * 编辑权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects) {
		myBatisDao.update("FUNCTION_INFO.updateByPrimaryKey", functionInfo);
		List<FunctionUrl> functionUrls = myBatisDao.getList("FUNCTION_URL.getUrlByFunctionId", functionInfo.getFunctionId());
		
		if(!urlJsonObjects.isEmpty()) {	//如果有表单提交的Url
			
			if(functionUrls.size() != 0) {	//原来保存了Url
				List<Long> formUrlIds = new ArrayList<Long>();	//封装表单提交的urlId
				for(JSONObject urlJsonObject : urlJsonObjects) {
					formUrlIds.add(urlJsonObject.getLong("urlId"));
				}
				for(FunctionUrl functionUrl : functionUrls) {
					long saveUrlId = functionUrl.getUrlId();
					if(!formUrlIds.contains(saveUrlId)) {	//判断是否有删
						myBatisDao.delete("FUNCTION_URL.deleteByPrimaryKey", functionUrl.getUrlId());
					}
				}
			}
			
			for(JSONObject jsonObject : urlJsonObjects) {
				long urlId = 0L;
				if(!"".equals(jsonObject.getString("urlId")) && jsonObject.getString("urlId") != null) {
					urlId = Long.parseLong(jsonObject.getString("urlId"));
				}
				
				FunctionUrl functionUrl = new FunctionUrl();
				functionUrl.setFunctionId(functionInfo.getFunctionId());
				functionUrl.setUrlId(urlId);
				functionUrl.setUrlInfo(jsonObject.getString("urlInfo"));
				
				if(urlId == 0L) {	//判断是否有增
					myBatisDao.insert("FUNCTION_URL.insert",functionUrl);
				}else {
					myBatisDao.update("FUNCTION_URL.updateByPrimaryKey", functionUrl);
				}
			}
		}else {	//表单没有提交Url，原来保存了Url，执行删除
			if(functionUrls.size() != 0) {
				for(FunctionUrl functionUrl : functionUrls) {
					myBatisDao.delete("FUNCTION_URL.deleteByPrimaryKey", functionUrl.getUrlId());
				}
			}
		}
	}

	/**
	 * 根据权限ID获取url列表
	 * @param functionId 权限ID
	 */
	@Override
	public List<FunctionUrl> getUrlByFunctionId(Long functionId){
		return myBatisDao.getList("FUNCTION_URL.getUrlByFunctionId", functionId);
	}
	
}
