package com.xt.cfp.core.service.impl.crm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.crm.CodeUtil;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CrmFranEmp;
import com.xt.cfp.core.pojo.CrmFranchise;
import com.xt.cfp.core.pojo.CrmOrg;
import com.xt.cfp.core.pojo.CrmOrgFranchise;
import com.xt.cfp.core.pojo.ext.crm.FranchiseVO;
import com.xt.cfp.core.service.crm.FranchiseService;
import com.xt.cfp.core.service.crm.StaffService;

@Service
public class FranchiseServiceImpl implements FranchiseService{

	private static Logger logger = Logger.getLogger(FranchiseServiceImpl.class);
	
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;
	@Autowired
	private StaffService staffService;
	
	@Override
	public List<CrmFranchise> findByStaffId(Long staffId) {
		Map map=new HashMap();
		if(staffId!=null){
			CrmFranEmp emp=staffService.findStaffById(staffId);
			if(emp.getFranchiseId()!=null){
				map.put("staffId", staffId);
			}
		}
		List<CrmFranchise> list=myBatisDao.getList("CRM_FRANCHISE.selectByStaffId", map);
		return list;
	}

	@Override
	public List<FranchiseVO> findAllHasNoOrg() {
		List<FranchiseVO> list=myBatisDao.getList("CRM_FRANCHISE.selectFranchiseNoOrg");
		return list;
	}

	@Override
	public List<FranchiseVO> findByOrgId(Long orgId) {
		List<FranchiseVO> list=myBatisDao.getList("CRM_FRANCHISE.selectFranchiseByOrgId",orgId);
		return list;
	}

	@Transactional
	@Override
	public String updateFrancOrg(Long oId, Long francId,String name) {
		String result="操作成功";
		CrmFranchise franc=myBatisDao.get("CRM_FRANCHISE.selectByPrimaryKey", francId);
		if(franc==null){
			result="加盟商id异常";
			return result;
		}
		try {
			/*//更新加盟商信息
			CrmOrgFranchise fo=new CrmOrgFranchise();
			fo.setFranchiseId(francId);
			fo.setJoinTime(new Date());
			fo.setOrgId(oId);
			myBatisDao.insert("CRM_ORG_FRANCHISE.insert", fo);*/
			//加入新的节点
			CrmOrg org=new CrmOrg();
			org.setCreateTime(new Date());
			org.setOrgStatus("0");
			org.setOrganizationParentId(oId);
			org.setLastUpdateTime(new Date());
			org.setOrgDesc(null);
			org.setOrgName(name);
			org.setType("2");
			org.setAttr("加盟商-"+name);
			String code="";
			Map map=new HashMap();
			map.put("pid", oId);
			Integer num=myBatisDao.get("CRM_ORG.countNumsByPid", map);
			CrmOrg pOrg=myBatisDao.get("CRM_ORG.selectByPrimaryKey", oId);
			code+=pOrg.getCode()+CodeUtil.setCode(num);
			org.setCode(code);
			myBatisDao.insert("CRM_ORG.insert", org);
			//加入映射关系
			CrmOrgFranchise orgMap=new CrmOrgFranchise();
			orgMap.setFranchiseId(francId);
			orgMap.setJoinTime(new Date());
			orgMap.setOrgId(org.getOrgId());
			myBatisDao.insert("CRM_ORG_FRANCHISE.insert", orgMap);
		} catch (Exception e) {
			e.printStackTrace();
			result="更新异常";
		}
		return result;
	}

	@Override
	public List<CrmFranchise> findAllFranc() {
		return myBatisDaoRead.getList("CRM_FRANCHISE.selectAllFranc");
	}

	@Override
	public FranchiseVO selectTopOneFrancByOrgId(Long oId) {
		return myBatisDaoRead.get("CRM_FRANCHISE.selectTopOneFrancByOrgId", oId);
	}

	@Override
	public CrmOrgFranchise selectOrgMappingById(Long id) {
		return myBatisDaoRead.get("CRM_FRANCHISE.selectOrgMappingById", id);
	}

}
