package com.xt.cfp.core.service.impl.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.crm.CodeUtil;
import com.xt.cfp.core.constants.crm.OrgTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CrmFranEmp;
import com.xt.cfp.core.pojo.CrmOrg;
import com.xt.cfp.core.pojo.CrmOrgFranchise;
import com.xt.cfp.core.pojo.CrmRole;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.crm.FranchiseVO;
import com.xt.cfp.core.pojo.ext.crm.OrganizationVO;
import com.xt.cfp.core.service.CityInfoService;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.ProvinceInfoService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.crm.FranchiseService;
import com.xt.cfp.core.service.crm.OrganizationService;
import com.xt.cfp.core.service.crm.RoleService;
import com.xt.cfp.core.service.crm.StaffService;
import com.xt.cfp.core.util.ExcelUtil;
import com.xt.cfp.core.util.StringUtils;

@Service
public class OrganizationServiceImpl implements OrganizationService {
	
	private static Logger logger = Logger.getLogger(OrganizationServiceImpl.class);
	
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;
	@Autowired
	private ConstantDefineService constantDefineService;
	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	private ProvinceInfoService provinceInfoService;
	@Autowired
	private CityInfoService cityInfoService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserInfoService userInfoService;
	
	
	@Override
	public List<OrganizationVO> findAllMainOrgs() {
		List<OrganizationVO> orgs=new ArrayList<OrganizationVO>();
		List<CrmOrg> main=myBatisDaoRead.getList("selectAllMainOrg");
		if(main!=null&&main.size()>0){
			for (CrmOrg org : main) {
				OrganizationVO vo=new OrganizationVO();
				vo.setOrg(org);
				List<CrmOrg> list=findChildOrgs(org.getOrgId());
				List<OrganizationVO> childs=new ArrayList<OrganizationVO>();
				if(list!=null&&list.size()>0){
					for (CrmOrg cf : list) {
						OrganizationVO child=new OrganizationVO();
						child.setOrg(cf);
						childs.add(child);
					}
					vo.setChilds(childs);
				}
				orgs.add(vo);
			}
		}
		
		return orgs;
	}

	@Override
	public List<CrmOrg> findChildOrgs(Long oId) {
		List<CrmOrg> orgs=myBatisDaoRead.getList("findChildOrg", oId);
		return orgs;
	}

	@Override
	public CrmOrg findOrgById(Long oId) {
		CrmOrg org=null;
		if(oId!=null){
			org=myBatisDaoRead.get("CRM_ORG.selectByPrimaryKey", oId);
		}
		return org;
	}

	@Transactional
	@Override
	public String saveOrg(Long orgId,Long pid,String orgName,String orgDesc,String type,String attr,Long francId,Long staffId) {
		String result="操作成功";
		/*if(type.equals(OrgTypeEnum.ORG_TYPE_GROUP.getValue())||type.equals(OrgTypeEnum.ORG_TYPE_PEOPLE.getValue())){
			if(staffId==null){
				result="员工id异常";
				return result;
			}
		}*/
		CrmOrg org=null;
		if(orgId!=null&&orgId!=-1){
			org=myBatisDao.get("CRM_ORG.selectByPrimaryKey", orgId);
		}else{
			org=new CrmOrg();
			org.setCreateTime(new Date());
			org.setOrgStatus("0");
		}
		if(pid!=null&&pid!=0){
			org.setOrganizationParentId(pid);
		}else{
			//将没有根节点的数据加入到第一个根节点下
			CrmOrg pOrg=findFirstOrg();
			if(pOrg!=null){
				if(org.getOrgId()==null){
					org.setOrganizationParentId(pOrg.getOrgId());
				}
			}
		}
		org.setLastUpdateTime(new Date());
		org.setOrgDesc(orgDesc);
		if(type!=null&&type.equals("2")){
			org.setOrgName(attr.split("-")[1]);
		}else{
			org.setOrgName(orgName);
		}
		org.setType(type);
		org.setAttr(attr);
		try {
			if(orgId!=null&&orgId!=-1){
				myBatisDao.update("CRM_ORG.updateByPrimaryKeySelective", org);
			}else{
				String code="";
				Map map=new HashMap();
				if(pid!=null&&pid!=0){
					map.put("pid", pid);
					Integer num=myBatisDao.get("CRM_ORG.countNumsByPid", map);
					CrmOrg pOrg=myBatisDao.get("CRM_ORG.selectByPrimaryKey", pid);
					code+=pOrg.getCode()+CodeUtil.setCode(num);
				}else{
					Integer num=myBatisDao.get("CRM_ORG.countNumsByPid", map); 
					code+=CodeUtil.setCode(num);
				}
				org.setCode(code);
				myBatisDao.insert("CRM_ORG.insert", org);
			}
			if(type.equals(OrgTypeEnum.ORG_TYPE_FRANC.getValue())){
				//加入映射关系
				if(francId!=null){
					FranchiseVO franVO = franchiseService.selectTopOneFrancByOrgId(orgId);
					if(franVO!=null){
						CrmOrgFranchise orgMap=franchiseService.selectOrgMappingById(franVO.getmOid());
						orgMap.setFranchiseId(francId);
						orgMap.setJoinTime(new Date());
						orgMap.setOrgId(org.getOrgId());
						myBatisDao.insert("CRM_ORG_FRANCHISE.updateByPrimaryKeySelective", orgMap);
					}else{
						CrmOrgFranchise orgMap=new CrmOrgFranchise();
						orgMap.setFranchiseId(francId);
						orgMap.setJoinTime(new Date());
						orgMap.setOrgId(org.getOrgId());
						myBatisDao.insert("CRM_ORG_FRANCHISE.insert", orgMap);
					}
				}
			}else if(type.equals(OrgTypeEnum.ORG_TYPE_GROUP.getValue())||type.equals(OrgTypeEnum.ORG_TYPE_PEOPLE.getValue())){
				CrmFranEmp emp=staffService.findStaffById(staffId);
				if(emp!=null){
					emp.setOrgId(org.getOrgId());
					staffService.updateStaff(emp);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result="添加失败";
		}
		return result;
	}

	@Transactional
	@Override
	public String delOrg(Long orgId) {
		String result="操作成功";
		if(orgId==null||orgId<1){
			result="非法的参数";
			return result;
		}
		List<CrmOrg> childs=findChildOrgs(orgId);
		if(childs!=null&&childs.size()>0){
			result="请先删除节点下的子节点";
			return result;
		}
		try {
			CrmOrg o=findFirstOrg();
			//修改组织机构下员工的oid
			List<CrmFranEmp> emps=staffService.findOrgStaffByOId(orgId);
			for (CrmFranEmp e : emps) {
				e.setOrgId(o.getOrgId());
				staffService.updateStaff(e);
			}
			Map<String, Object> map=new HashMap<String, Object>();
			//删除关联表
			map.put("orgId", orgId);
			myBatisDao.delete("CRM_ORG.deleteOrgMapping", map);
			map.clear();
			StringBuffer sb=new StringBuffer("ORG_ID='"+orgId+"' or");
			/*if(childs!=null&&childs.size()>0){
				sb=findChilds(childs, sb);
			}*/
			String ids=sb.toString();
			ids=ids.substring(0,ids.length()-2);
			
			map.put("ids", ids);
			myBatisDao.delete("CRM_ORG.deleteOrgs", map);
		} catch (Exception e) {
			e.printStackTrace();
			result="删除失败";
		}
		return result;
	}
	
	private StringBuffer findChilds(List<CrmOrg> list,StringBuffer sb){
		for (CrmOrg org : list) {
			sb.append(" ORG_ID='"+org.getOrgId()+"' or");
			List<CrmOrg> childs=findChildOrgs(org.getOrgId());
			if(childs!=null&&childs.size()>0){
				findChilds(childs, sb);
			}
		}
		return sb;
	}
	
	@Override
	public List<OrganizationVO> loadOrgs(String oId) {
		List<OrganizationVO> orgs=new ArrayList<OrganizationVO>();
		List<CrmOrg> main=null;
		if(StringUtils.isNull(oId)){
			main=myBatisDaoRead.getList("selectAllMainOrg");
		}else{
			main=findChildOrgs(Long.parseLong(oId));
		}
		if(main!=null&&main.size()>0){
			for (CrmOrg org : main) {
				OrganizationVO vo=new OrganizationVO();
				vo.setOrg(org);
				List<CrmOrg> list=findChildOrgs(org.getOrgId());
				List<OrganizationVO> childs=new ArrayList<OrganizationVO>();
				if(list!=null&&list.size()>0){
					for (CrmOrg cf : list) {
						OrganizationVO child=new OrganizationVO();
						child.setOrg(cf);
						childs.add(child);
					}
					vo.setChilds(childs);
				}
				orgs.add(vo);
			}
		}
		return orgs;
	}

	@Override
	public List<CrmOrg> findAllchildsBySelf(Long oId) {
		List<CrmOrg> childs=findChildOrgs(oId);
		StringBuffer sb=new StringBuffer("ORG_ID='"+oId+"' or");
		if(childs!=null&&childs.size()>0){
			sb=findChilds(childs, sb);
		}
		String ids=sb.toString();
		ids=ids.substring(0,ids.length()-2);
		Map<String, String> map=new HashMap<String, String>();
		map.put("ids", ids);
		List<CrmOrg> all=myBatisDaoRead.getList("CRM_ORG.selectOrgsBySelf", map);
		return all;
	}
	
	@Override
	public String findAllChildsBySelfForSQLSelect(Long oId,String table){
		StringBuffer sb=new StringBuffer();
		List<CrmOrg> all=findAllchildsBySelf(oId);
		if(all!=null&&all.size()>0){
			for (CrmOrg org : all) {
				sb.append(" "+table+".ORG_ID='"+org.getOrgId()+"' or");
			}
		}
		String ids=sb.toString().substring(0,sb.toString().length()-2);
		return ids;
	}

	@Override
	public CrmOrg findByStaffId(Long staffId) {
		CrmOrg org=null;
		if(staffId!=null){
			org=myBatisDaoRead.get("CRM_ORG.selectOrgByStaffId", staffId);
		}
		return org;
	}

	@Override
	public List<OrganizationVO> loadOrgForStaff(String oId, Long staffId) {
		List<OrganizationVO> orgs=new ArrayList<OrganizationVO>();
		//第一次进入时只加载父节点
		if(StringUtils.isNull(oId)){
			//根据登录用户id找出组织机构
			CrmOrg org=findByStaffId(staffId);
			//没有组织机构的都挂在根节点下
			if(org==null){
				org=findAllMainOrgs().get(0).getOrg();
			}
			OrganizationVO vo=new OrganizationVO();
			vo.setOrg(org);
			List<CrmOrg> list=findChildOrgs(org.getOrgId());
			List<OrganizationVO> childs=new ArrayList<OrganizationVO>();
			if(list!=null&&list.size()>0){
				for (CrmOrg cf : list) {
					OrganizationVO child=new OrganizationVO();
					child.setOrg(cf);
					childs.add(child);
				}
				vo.setChilds(childs);
			}
			orgs.add(vo);
		}else{//加载子节点
			List<CrmOrg> main=findChildOrgs(Long.parseLong(oId));
			if(main!=null&&main.size()>0){
				for (CrmOrg org : main) {
					OrganizationVO vo=new OrganizationVO();
					vo.setOrg(org);
					List<CrmOrg> list=findChildOrgs(org.getOrgId());
					List<OrganizationVO> childs=new ArrayList<OrganizationVO>();
					if(list!=null&&list.size()>0){
						for (CrmOrg cf : list) {
							OrganizationVO child=new OrganizationVO();
							child.setOrg(cf);
							childs.add(child);
						}
						vo.setChilds(childs);
					}
					orgs.add(vo);
				}
			}
		}
		return orgs;
	}

	@Override
	public List<Map<String, Object>> loadOptions(String constantDefineId,String oId) {
		List<Map<String, Object>> ops=new ArrayList<Map<String,Object>>();
		if(StringUtils.isNull(constantDefineId)){
			return ops;
		}
		String[] args=constantDefineId.split("-");//类型-id
		if(args[0].equals(OrgTypeEnum.ORG_TYPE_CITY.getValue())||args[0].equals(OrgTypeEnum.ORG_TYPE_FRANC.getValue())
				||args[0].equals(OrgTypeEnum.ORG_TYPE_GROUP.getValue())||args[0].equals(OrgTypeEnum.ORG_TYPE_PEOPLE.getValue())){
			if(args[0].equals(OrgTypeEnum.ORG_TYPE_CITY.getValue())){
				//读取省市表
				List<ProvinceInfo> pros=provinceInfoService.getAllProvinceInfo();
				for (ProvinceInfo p : pros) {
					Map<String,Object>  item = new HashMap<String,Object>();  
					item.put("id",p.getProvinceId());  
		            item.put("text", p.getProvinceName());
		            ops.add(item);
				}
			}
			if(args[0].equals(OrgTypeEnum.ORG_TYPE_FRANC.getValue())){
				//读取加盟商表中没有组织机构的
				List<FranchiseVO> list=franchiseService.findAllHasNoOrg();
				for (FranchiseVO fran : list) {
					Map<String,Object>  item = new HashMap<String,Object>();  
					item.put("id",fran.getFranchiseId());  
		            item.put("text", fran.getFranchiseName());
		            ops.add(item);
				}
				//读取组织结构下的第一个加盟商
				if(!StringUtils.isNull(oId)){
					FranchiseVO franc=franchiseService.selectTopOneFrancByOrgId(Long.parseLong(oId));
					if(franc!=null){
						Map<String,Object>  item = new HashMap<String,Object>();
						item.put("id",franc.getFranchiseId());  
			            item.put("text", franc.getFranchiseName());
						ops.add(0, item);
					}
				}
			}
			//业务组或业务员
			if(args[0].equals(OrgTypeEnum.ORG_TYPE_GROUP.getValue())||args[0].equals(OrgTypeEnum.ORG_TYPE_PEOPLE.getValue())){
				List<CrmFranEmp> emps=staffService.findOrgStaffByOId(Long.parseLong(oId));
				for (CrmFranEmp e : emps) {
					Map<String,Object>  item = new HashMap<String,Object>();
					item.put("id",e.getStaffId());  
		            item.put("text", e.getName());
					ops.add(0, item);
				}
			}
		}else{
			List<ConstantDefine> list=constantDefineService.findsById(Long.parseLong(args[1]));
			for (ConstantDefine con : list) {
				Map<String,Object>  item = new HashMap<String,Object>();  
				item.put("id",con.getConstantDefineId());  
	            item.put("text", con.getConstantName());
	            ops.add(item);
			}
		}
		
		return ops;
	}

	@Override
	public List<Map<String, Object>> loadCitys(Long provinceId) {
		List<Map<String, Object>> ops=new ArrayList<Map<String,Object>>();
		List<CityInfo> list=cityInfoService.findCitysByPid(provinceId);
		for (CityInfo c : list) {
			Map<String,Object>  item = new HashMap<String,Object>();  
			item.put("id",c.getCityId());  
            item.put("text", c.getCityName());
            ops.add(item);
		}
		return ops;
	}
	
	@Override
	public List<Map<String, Object>> loadArea(Long cityId) {
		List<Map<String, Object>> ops=new ArrayList<Map<String,Object>>();
		List<CityInfo> list=cityInfoService.findAreaByCid(cityId);
		for (CityInfo c : list) {
			Map<String,Object>  item = new HashMap<String,Object>();  
			item.put("id",c.getCityId());  
            item.put("text", c.getCityName());
            ops.add(item);
		}
		return ops;
	}

	@Override
	public List<FranchiseVO> loadFrancByOrgId(Long orgId) {
		if(orgId!=null){
			return franchiseService.findByOrgId(orgId);
		}
		return null;
	}

	@Override
	public CrmOrg findFirstOrg() {
		List<CrmOrg> list=myBatisDaoRead.getList("CRM_ORG.findFirstOrg");
		return list!=null&&list.size()>0?list.get(0):null;
	}
	
	@Override
	public String uploadStaffList(MultipartFile multipartFile) {
		String result="success";
		List<Map<String, Object>> uploadExcelData=null;
		try {
			uploadExcelData = ExcelUtil.getUploadExcelData(multipartFile);
		} catch (Exception e) {
			e.printStackTrace();
			result="Excel解析失败";
			return result;
		}
		if(uploadExcelData==null||uploadExcelData.size()==0){
			result="导入数据为空";
			return result;
		}
		//清除list中的null
		for (int i = 0; i < uploadExcelData.size(); i++) {
			Map<String, Object> map=uploadExcelData.get(i);
			if(map.get("姓名")==null||map.get("角色")==null||map.get("组织机构代码")==null||map.get("手机号")==null){
				uploadExcelData.remove(i);
				i--;
			}
		}
		if(uploadExcelData.size()==0){
			result="导入数据格式不正确";
			return result;
		}
		//常规校验
		int i=1;
		boolean flag=true;
		StringBuffer sb=new StringBuffer();
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[^4,\\D]))\\d{8}$");
		Matcher m = null;
		for (Map<String, Object> map : uploadExcelData) {
			if(map.get("姓名")==null||map.get("角色")==null||map.get("组织机构代码")==null||map.get("手机号")==null){
				break;
			}
			if(StringUtils.isNull(map.get("姓名").toString())){
				flag=false;
				sb.append("第"+i+"行姓名为空<br>");
			}else{
				if(!StringUtils.isNull(map.get("姓名").toString())){
					if(StringUtils.checkSQLStr(map.get("姓名").toString())){
						flag=false;
						sb.append("第"+i+"行姓名包含非法字符<br>");
					}
				}
			}
			if(StringUtils.isNull(map.get("角色").toString())){
				flag=false;
				sb.append("第"+i+"行角色为空<br>");
			}else{
				if(!StringUtils.isNull(map.get("角色").toString())){
					if(StringUtils.checkSQLStr(map.get("角色").toString())){
						flag=false;
						sb.append("第"+i+"角色名包含非法字符<br>");
					}
				}
			}
			if(StringUtils.isNull(map.get("组织机构代码").toString())){
				flag=false;
				sb.append("第"+i+"行组织机构代码为空<br>");
			}else{
				if(!StringUtils.isNull(map.get("组织机构代码").toString())){
					if(StringUtils.checkSQLStr(map.get("组织机构代码").toString())){
						flag=false;
						sb.append("第"+i+"组织机构代码包含非法字符<br>");
					}
				}
			}
			if(StringUtils.isNull(map.get("手机号").toString())){
				flag=false;
				sb.append("第"+i+"行手机号为空<br>");
			}else{
				m=pattern.matcher(map.get("手机号").toString());
				if(!m.matches()){
					flag=false;
					sb.append("第"+i+"行手机号"+map.get("手机号").toString()+"不正确<br>");
				}
			}
			i++;
		}
		/*if(!flag){
			result=sb.toString();
			return result;
		}*/
		i=1;
		flag=true;
		//重复手机号校验
		for (int j = 0; j <uploadExcelData.size(); j++) {
			for (int k = j+1; k<uploadExcelData.size(); k++) {
				if(uploadExcelData.get(j).get("手机号").toString().equals(uploadExcelData.get(k).get("手机号").toString())){
					flag=false;
					sb.append("第"+(j+1)+"行"+uploadExcelData.get(j).get("手机号").toString()+"手机号和第"+(k+1)+"行手机号重复<br>");
				}
			}
		}
		/*if(!flag){
			result=sb.toString();
			return result;
		}*/
		//数据库数据校验
		i=1;
		flag=true;
		List<CrmOrg> orgs=selectAllOrgs();
		List<CrmRole> roles=roleService.selectAllRole();
		for (Map<String, Object> map : uploadExcelData) {
			UserInfoExt ue=null;
			try {
				ue = userInfoService.getUserExtByMobileNo(map.get("手机号").toString());
			} catch (SystemException e) {
				e.printStackTrace();
				flag=false;
				sb.append("第"+i+"个用户"+map.get("手机号").toString()+"在平台上有重复帐号<br>");
				continue;
			}
			if(ue==null){
				flag=false;
				sb.append("第"+i+"个用户"+map.get("手机号").toString()+"没有在财富派平台注册<br>");
			}else{
				if(ue.getIsVerified().equals("0")){
					flag=false;
					sb.append("第"+i+"个用户"+map.get("手机号").toString()+"没有进行实名认证<br>");
				}else{
					if(!ue.getRealName().equals(map.get("姓名").toString())){
						flag=false;
						sb.append("第"+i+"个用户"+map.get("手机号").toString()+"姓名不匹配<br>");
					}
				}
			}
			CrmFranEmp emp=staffService.findCrmFramEmpByMobileNo(map.get("手机号").toString());
			if(emp!=null){
				flag=false;
				sb.append("第"+i+"个用户"+map.get("手机号").toString()+"crm中已存在该手机号<br>");
			}
			String code=map.get("组织机构代码").toString();
			boolean flag2=false;
			for (CrmOrg org : orgs) {
				if(org.getCode().equals(code)){
					flag2=true;
					break;
				}
			}
			if(!flag2){
				flag=false;
				sb.append("第"+i+"个用户"+map.get("手机号").toString()+"错误的组织机构代码<br>");
			}
			flag2=false;
			String roleName=map.get("角色").toString();
			if("超级管理员".equals(roleName)){
				flag=false;
				sb.append("第"+i+"个用户"+map.get("手机号").toString()+"非法角色名<br>");
				i++;
				continue;
			}
			for (CrmRole role : roles) {
				if(role.getRoleName().equals(roleName)){
					flag2=true;
					break;
				}
			}
			if(!flag2){
				flag=false;
				sb.append("第"+i+"个"+map.get("手机号").toString()+"不存在的角色<br>");
			}
			i++;
		}
		if(!flag){
			result=sb.toString();
			return result;
		}
		//插入数据库
		for (Map<String, Object> map : uploadExcelData) {
			String code=map.get("组织机构代码").toString();
			CrmOrg o=null;
			for (CrmOrg org : orgs) {
				if(org.getCode().equals(code)){
					o=org;
					break;
				}
			}
			String roleName=map.get("角色").toString();
			CrmRole r=null;
			for (CrmRole role : roles) {
				if(role.getRoleName().equals(roleName)){
					r=role;
					break;
				}
			}
			//CrmFranEmp emp=staffService.findCrmFramEmpByMobileNo(map.get("手机号").toString());
			staffService.saveStaff(o.getOrgId(), null, map.get("姓名").toString(), map.get("手机号").toString(), r.getRoleId());
		}
		return result;
	}

	@Override
	public List<CrmOrg> selectAllOrgs() {
		return myBatisDaoRead.getList("CRM_ORG.selectAllOrgs");
	}

	@Override
	public List<CrmOrg> findByOrgName(String orgName) {
		return myBatisDaoRead.getList("CRM_ORG.selectByOrgName",orgName);
	}

	@Override
	public Long findFranIdByOId(Long oid) {
		CrmOrg org=myBatisDaoRead.get("CRM_ORG.selectByPrimaryKey",oid);
		if(org.getOrganizationParentId()==null){
			return null;
		}
		if(org.getType().equals(OrgTypeEnum.ORG_TYPE_FRANC.getValue())){
			return myBatisDaoRead.get("CRM_ORG.findFranIdByOId",oid);
		}
		return findFranIdByOId(org.getOrganizationParentId());
	}

	@Override
	public List<CrmOrg> findByCode(String code) {
		Map map=new HashMap();
		map.put("code", code);
		return myBatisDaoRead.getList("CRM_ORG.selectByOrgCode",map);
	}
	
}
