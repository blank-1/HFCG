package com.xt.cfp.core.service.impl.crm;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CrmFunction;
import com.xt.cfp.core.pojo.CrmRole;
import com.xt.cfp.core.pojo.CrmRoleFunction;
import com.xt.cfp.core.pojo.ext.crm.RoleVO;
import com.xt.cfp.core.service.crm.FunctionService;
import com.xt.cfp.core.service.crm.RoleService;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
	
	private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
	
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;
	@Autowired
	private FunctionService crmFunctionService;
	

	@Override
	public List<CrmFunction> findFunctionByRoleId(Long id) {
		List<CrmFunction> funcs=crmFunctionService.findFuncByRoleId(id);
		return funcs;
	}

	@Override
	public List<CrmRole> findRoleByStaffId(Long staffId) {
		List<CrmRole> roles=myBatisDaoRead.getList("CRM_ROLE.findRoleByStaffId",staffId);
		return roles;
	}

	@Override
	public Pagination<RoleVO> findAllRolesAndFunction(int pageNo, int pageSize,String name) {
		Pagination<RoleVO> re = new Pagination<RoleVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        if(!StringUtils.isNull(name)){
        	params.put("roleName", name);
        }

        int totalCount = this.myBatisDaoRead.count("selectByPaging", params);
        List<CrmRole> uah = this.myBatisDaoRead.getListForPaging("selectByPaging", params, pageNo, pageSize);
        List<RoleVO> roles = new ArrayList<RoleVO>();
        
        for (CrmRole role : uah) {
        	RoleVO vo=new RoleVO();
        	vo.setRoleDesc(role.getRoleDesc());
        	vo.setRoleId(role.getRoleId());
        	vo.setRoleName(role.getRoleName());
        	vo.setScope(role.getScope());
        	String funcStr=null;
        	List<CrmFunction> funcs=findFunctionByRoleId(role.getRoleId());
        	if(funcs!=null&&funcs.size()>0){
        		StringBuffer sb=new StringBuffer();
        		for (CrmFunction func : funcs) {
					sb.append(func.getFunName()+",");
				}
        		funcStr=sb.toString();
        		funcStr=funcStr.substring(0,funcStr.length()-1);
        	}
			vo.setFuncs(funcStr);
        	roles.add(vo);
        }

        re.setTotal(totalCount);
        re.setRows(roles);

        return re;
	}

	@Override
	public CrmRole findById(Long roleId) {
		CrmRole role=null;
		if(roleId!=null&&roleId!=-1){
			role=this.myBatisDaoRead.get("CRM_ROLE.selectByPrimaryKey", roleId);
		}
		return role;
	}

	@Transactional
	@Override
	public String save(Long roleId, String roleName, String desc,String type,String francId) {
		String result="操作成功";
		CrmRole role=myBatisDao.get("selectByRoleName",roleName);
		try {
			if(roleId!=null&&roleId!=-1){
				role=myBatisDao.get("CRM_ROLE.selectByPrimaryKey", roleId);
				role.setRoleDesc(desc);
				role.setRoleName(roleName);
				role.setScope(type);
				if(!StringUtils.isNull(francId)&&!francId.equals("-1")){
					role.setFranchiseId(Long.parseLong(francId));
				}
				myBatisDao.update("CRM_ROLE.updateByPrimaryKeySelective", role);
			}else{
				if(role!=null){
					result="角色名已存在,请更换角色名";
					return result;
				}
				role=new CrmRole();
				role.setRoleDesc(desc);
				role.setRoleName(roleName);
				role.setScope(type);
				if(!StringUtils.isNull(francId)&&!francId.equals("-1")){
					role.setFranchiseId(Long.parseLong(francId));
				}
				myBatisDao.insert("CRM_ROLE.insert", role);
			}
		} catch (Exception e) {
			result="更新失败";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean checkFuncs(Long funcId,Long roleId) {
		List<CrmFunction> funcs=crmFunctionService.findFuncByRoleId(roleId);
		for (CrmFunction func : funcs) {
			if(func.getFunId().compareTo(funcId)==0){
				return true;
			}
		}
		return false;
	}

	@Transactional
	@Override
	public String upadteAccredit(String ids, Long roleId) {
		String result="操作成功";
		try {
			myBatisDao.delete("CRM_ROLE_FUNCTION.deleteFuncsByRoleId", roleId);
			if(!StringUtils.isNull(ids)){
				String[] sData=ids.split(",");
				List<CrmFunction> nowDatas=new ArrayList<CrmFunction>();
				for (int i = 0; i < sData.length; i++) {
					CrmFunction f=crmFunctionService.findById(Long.parseLong(sData[i]));
					nowDatas.add(f);
				}
				List<CrmFunction> news=crmFunctionService.findFuncAndParentFunc(nowDatas);
				if(news!=null&&news.size()>0){
					for (CrmFunction f : news) {
						CrmRoleFunction func=new CrmRoleFunction();
						func.setMenuId(f.getFunId());
						func.setRoleId(roleId);
						myBatisDao.insert("CRM_ROLE_FUNCTION.insert", func);
					}
				}
			}
		} catch (NumberFormatException e) {
			result="更新失败";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<CrmRole> findByFrancId(Long franId) {
		List<CrmRole> roles=new ArrayList<CrmRole>();
		if(franId!=null){
			List<CrmRole> pris=(myBatisDaoRead.getList("CRM_ROLE.selectAllPrivateRole", franId));
			roles.addAll(pris);
		}
		List<CrmRole> pubs=findAllPublicRole();
		if(pubs!=null&&pubs.size()>0){
			roles.addAll(pubs);
		}
		return roles;
	}

	@Override
	public List<CrmRole> findAllPublicRole() {
		List<CrmRole> roles=myBatisDaoRead.getList("CRM_ROLE.selectAllPublicRole");
		return roles;
	}

	@Override
	public List<CrmRole> selectAllRole() {
		List<CrmRole> roles=myBatisDaoRead.getList("CRM_ROLE.selectAllRole");
		return roles;
	}

}
