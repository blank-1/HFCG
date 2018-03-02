package com.xt.cfp.core.service.crm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.CrmFranCustomer;
import com.xt.cfp.core.pojo.CrmFranEmp;
import com.xt.cfp.core.pojo.CrmOrg;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.crm.StaffTop;
import com.xt.cfp.core.pojo.ext.crm.StaffVO;
import com.xt.cfp.core.pojo.ext.crm.TransforStaff;
import com.xt.cfp.core.util.Pagination;

public interface StaffService {
	
	Pagination<StaffVO> findStaffPaging(int pageNo, int pageSize,Long orgId,Long franId,String staffName, String staff_moblie,String orgCode);
	
	StaffVO findById(Long staffId);
	
	CrmFranEmp findStaffById(Long staffId);

	/**
	 * 手机号查询员工信息
	 * @param mobile
	 * @return
	 */
	CrmFranEmp findCrmFramEmpByMobileNo(String mobile);
	
	String updateStatus(Long staffId,String status);
	
	CrmOrg findByStaffId(Long staffId);
	
	String saveStaff(Long orgId,Long staffId,String staffName,String mobile,Long roleId);

	/**
	 * 根据员工id查询
	 * @param staffId
	 * @return
	 */
	CrmFranEmp getCrmFranEmpByStaffId(Long staffId);
	/**
	 *
	 * @param crmFranEmp
	 * @return
	 */
	CrmFranEmp updateStaff(CrmFranEmp crmFranEmp);
	
	/**
	 * 根据组织id找出所属员工，不包含子节点下的
	 * @param oId
	 * @return
	 */
	List<StaffVO> findStaffByOId(Long oId);
	
	/**
	 * 根据组织id找出所属员工,包含子节点
	 * @param oId
	 * @return
	 */
	List<CrmFranEmp> findOrgStaffByOId(Long oId);
	
	/**
	 * 读取属于该节点的员工，不包含子节点下的
	 * @param pageNo
	 * @param pageSize
	 * @param orgId
	 * @return
	 */
	Pagination<StaffVO> findOrgStaffPaging(int pageNo, int pageSize,Long orgId);
    /**
     * @param userId
     * @return
     *@date 2015年12月9日 下午5:06:16 
     *@auhthor wangyadong
     */
	Long findCrmFranById(Long userId);

	/**
	 *@param staffId
     * @return
     *@date 2016年1月8日 下午15:06:16 
     *@auhthor wangyadong
	 */
	Long getCrmFranEmpOrgIdByStaffId(Long staffId);
	
	/**
	 * 销售排行
	 * @param pageNo
	 * @param pageSize
	 * @param time
	 * @param franId
	 * @param order 排序方式
	 * @return
	 */
	Pagination<StaffTop> findTopStaffPaging(int pageNo, int pageSize,String time,String timeZone,Long franId,String order,String orgCode,Long userId);
	
	/**
	 * 根据员工id查出他的所有直接客户
	 * @param staffUid		员工的userId
	 * @param customerId	客户的userId
	 * @return
	 */
	List<CrmFranCustomer> findStaffCustomers(Long staffUid,Long customerId);
	
	/**
	 * 保存员工的客户关系
	 * @param customer
	 * @return
	 */
	String saveCustomer(CrmFranCustomer customer);
	
	String updateCustomer(CrmFranCustomer customer);
	
	/**
	 * 用户注册后，保存用户和员工关系
	 * @param staffUid  	员工的userId
	 * @param customerId	客户的userId
	 * @return
	 */
	String saveCustomerAfterRegister(Long staffUid,Long customerId);
	
	/**
	 * 根据userId找出对应的员工
	 * @param userId
	 * @return
	 */
	CrmFranEmp findByUserId(Long userId);
	
	/**
	 * 找出所有有recId的员工
	 * @return
	 */
	List<UserInfoExt> findStaffInUEAndHaveRecId();
	
	/**
	 * 找出员工的客户
	 * @param recId
	 * @return
	 */
	List<UserInfoExt> findStaffCustomer(Long recId);
	
	/**
	 * 
	 * @param staffId 员工id
	 * @param mobile  客户手机号	
	 * @param type    add 补录客户   transfor 客户迁移
	 * @return
	 */
	String addCustomer(Long staffId,String mobile,String type);
	
	/**
	 * 根据手机号查询对应的员工
	 * @param mobile
	 * @return
	 */
	List<TransforStaff> findtransforStaff(String mobile);
	
	/**
	 * 计算用户的总投资额度
	 * @param map
	 * @return
	 */
	BigDecimal countUserOrderAmount(Map map);
	/**
	 * 计算用户的折标金额
	 * @param map
	 * @return
	 */
	BigDecimal countUserOrderDisAmount(Map map);
	
	/**
	 * 根据客户ID，查询加盟商客户表条数
	 * @param customerId 客户ID
	 * @return 条数
	 */
	Integer getCrmFranCustomerCountByCustomerId(Long customerId);
}
