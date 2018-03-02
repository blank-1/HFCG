package com.xt.cfp.core.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.xt.cfp.core.pojo.CrmFranchise;
import com.xt.cfp.core.pojo.InvitationCode;
import com.xt.cfp.core.pojo.ext.crm.CRMCustomerVO;
import com.xt.cfp.core.pojo.ext.crm.InvestmentVO;
import com.xt.cfp.core.pojo.ext.crm.PerformanceStatisticsVO;
import com.xt.cfp.core.util.Pagination;

/**
 * 
 * @author wangyadong
 * 组织结构
 */
public interface CrmFranchiseService {
	
	
	public Pagination<CrmFranchise>  getCrmFranchiseList(Object params,int pageNo, int pageSize);
	
	public void saveOrUpdate(CrmFranchise franchis);
	
	public void delete(Long id);
	
	public CrmFranchise getCrmFranchiseById(Long id);
	
	public 	Pagination<	CrmFranchise > getCrmFranchiseJsonList(Object object, int pageNo, int pageSize) ;
	
	public  Pagination<InvitationCode> getInvitationCode(Object params,int pageNo, int pageSize) ;
	
	
	public List<CrmFranchise> getCrmFranchiseList() ;

	Map<String, Object> getCustomerList(Map<String, String> map, Integer pageSize, Integer pageNo);

	Pagination<CRMCustomerVO> getCustomerByPagination(Map<String, String> map, Integer pageSize, Integer pageNo);

	/**
	 * 获取组织机构节点下的所有加盟商
	 * @param code
	 * @return
	 */
	List<CrmFranchise> getCrmFranchiseListFromOrgCode(String code);

	/**
	 * 获取统计列表
	 */
	Pagination<PerformanceStatisticsVO> performanceStatistice(int pageNo, int pageSize,String queryMethod, Map<String, Object> customParams);

	/**
	 * 获取投资列表
	 */
	Pagination<InvestmentVO> investmentList(int pageNo, int pageSize, Map<String, Object> customParams);
	
	/**
	 * 导出客户列表
	 * @param params
	 * @param response
	 */
	void exportCustomerExcel(Map<String, String> params,HttpServletResponse response);
	
	/**
	 * 导出新投资列表
	 * @param params
	 * @param response
	 */
	void exportInvestmentExcel(Map<String, Object> customParams,HttpServletResponse response);
}
