package com.xt.cfp.core.service.crm;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.xt.cfp.core.pojo.CrmOrg;
import com.xt.cfp.core.pojo.ext.crm.FranchiseVO;
import com.xt.cfp.core.pojo.ext.crm.OrganizationVO;

public interface OrganizationService {
	/**
	 * 查询所有父级组织机构
	 * @return
	 */
	List<OrganizationVO> findAllMainOrgs();
	
	/**
	 * 根据oid查出所有子机构
	 * @param oId
	 * @return
	 */
	List<CrmOrg> findChildOrgs(Long oId);
	
	/**
	 * 查询出所有组织机构
	 * @return
	 */
	List<CrmOrg> selectAllOrgs();
	
	/**
	 * 根据oid查出所有子机构及本身
	 * @param oId
	 * @return
	 */
	List<CrmOrg> findAllchildsBySelf(Long oId);
	
	/**
	 * 为sql查询拼接 格式类似于 o.org_id=x or o.org_id=x .... 
	 * 此功能根据组织id查询其本身级所有子机构的id串
	 * @param oId 组织id
	 * @param table 表的别名 类似于 CRM_ORG o 其中的o
	 * @return
	 */
	String findAllChildsBySelfForSQLSelect(Long oId,String table);
	
	CrmOrg findOrgById(Long oId);
	
	String saveOrg(Long orgId,Long pid,String orgName,String orgDesc,String type,String attr,Long francId,Long staffId);
	
	String delOrg(Long orgId);
	
	/**
	 * 组织机构修改时加载所有节点
	 * @param oId
	 * @return
	 */
	List<OrganizationVO> loadOrgs(String oId);
	
	/**
	 * 根据员工id找出对应的组织机构
	 * @param staffId
	 * @return
	 */
	CrmOrg findByStaffId(Long staffId);
	
	/**
	 * 根据组织机构名字查找
	 * @param orgName
	 * @return
	 */
	List<CrmOrg> findByOrgName(String orgName);
	
	/**
	 * 根据员工id找出所有子节点
	 * @param oId
	 * @param staffId
	 * @return
	 */
	List<OrganizationVO> loadOrgForStaff(String oId,Long staffId);
	
	/**
	 * 读取2级下拉列表
	 * @param constantDefineId
	 * @return
	 */
	List<Map<String,Object>> loadOptions(String constantDefineId,String oId);
	
	/**
	 * 读取城市列表
	 * @param provinceId
	 * @return
	 */
	List<Map<String,Object>> loadCitys(Long provinceId);
	
	/**
	 * 读取区县列表
	 * @param cityId
	 * @return
	 */
	List<Map<String,Object>> loadArea(Long cityId);
	
	/**
	 * 读取所有没有
	 * @return
	 */
	List<FranchiseVO> loadFrancByOrgId(Long orgId);
	
	/**
	 * 找出根节点
	 * @return
	 */
	CrmOrg findFirstOrg();
	
	/**
	 * 导入员工列表
	 * @param multipartFile
	 * @return
	 */
	String uploadStaffList(MultipartFile multipartFile);
	
	/**
	 * 根据节点ID找出父级节点属性为加盟商的节点对应的加盟商id
	 * @param oid
	 * @return
	 */
	Long findFranIdByOId(Long oid);
	
	/**
	 * 根据code查询组织机构及子机构
	 * @param code
	 * @return
	 */
	List<CrmOrg> findByCode(String code);
}
