package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.DistributionInvite;
import com.xt.cfp.core.pojo.ext.Distributor;
import com.xt.cfp.core.pojo.ext.DistributorVO;
import com.xt.cfp.core.util.Pagination;

/**
 * 
 * @author wangyadong 先查询出三级（先计算自己在邀请关系中算几级） 在分别计算邀请等级（需要计算邀请等级） 计算规则为：
 *         如果不存在上级就是1级 （本身自己不算） 如果邀请关系为1~2邀请级别 （被邀请者为3级） （父级分别为1~2）
 *         如果邀请关系存在1级、（父级别是一级）（被邀请者为2级） 如果三级在邀请 客户（客户是1级）
 *
 */
public interface DistributionInviteService {

	/**
	 * 设置邀请关系
	 * 
	 * @author wangyadong
	 * @param currentUserId
	 * @param userId
	 * @return
	 */
	public String settingDistribtionRelation(Long currentUserId, Long userId);

	/**
	 * 邀请关系分页列表
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            页数
	 * @param params
	 *            查询条件
	 * @author wangyadong
	 */
	Pagination<Distributor> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);

	/**
	 * 添加邀请关系
	 * 
	 * @param distribtionInvite
	 *            邀请关系对象
	 * @author wangyadong
	 */
	DistributionInvite addDistribtionInviten(DistributionInvite distribtionInvite);

	/**
	 * 根据邀请关系加载一条数据
	 * 
	 * @param distribtionInviteId
	 *            邀请关系ID
	 * @author wangyadong
	 */
	DistributionInvite getAdminById(Long distribtionInviteId);

	/**
	 * 编辑邀请关系信息
	 * 
	 * @param distribtionInviteId
	 *            邀请关系ID
	 * @author wangyadong
	 */
	AdminInfo editAdmin(DistributionInvite distribtionInvite);

	/**
	 * 删除邀请关系信息
	 * 
	 * @param distribtionInviteId
	 *            邀请关系ID
	 * @author wangyadong
	 */
	void deleteDistribtionInvite(Long distribtionInviteId);

	/**
     * 
     */
	public void settingRelationByInvative();
	
	/**
	 * 根据用户id统计出他的邀请数量
	 * @author hu
	 * @param userId
	 * @return 邀请的用户数量
	 */
	Integer countUserCustomerByUserId(Long userId);
	
	/**
	 * 根据被邀请人ID，查询分销关系列表
	 * @param userId 被邀请人ID
	 * @param isWhiteTabs
	 * @return 分销关系列表
	 */
	List<DistributionInvite> getDistributionInviteByUserId(Long userId, boolean isWhiteTabs);
	
	/**
	 * 分销客户列表
	 * @author hu
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	Pagination<DistributorVO> findAllDistributionCustomerByPage(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 根据用户id查询出客户详细信息
	 * @author hu
	 * @param userId
	 * @return
	 */
	DistributorVO findDistributionCustomerDetailByUserId(Long userId);
	
	/**
	 * 根据对应的用户查询其所有的客户
	 * @author hu
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	Pagination<DistributorVO> findUserCustomersByPage(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 根据userPid获取该用户邀请过和子用户邀请的所有用户的ID
	 * @param List<Long> userPidList
	 * @return List<Long> 
	 * */
	List<Long> getUserIdByUserPids(List<Long> userPidList) ;

	/**
	 * 工具类：找到通过手机号找到平台普通用户信息userType UserType.COMMON
	 * */
	public Map<Long, Object> findUserAllInfoByMobiles(
			List importWhiteTabsMobiles);
	
}
