package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.xt.cfp.core.constants.RateEnum.RateUsageHisStateEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.pojo.RateProduct;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.ext.RateUserVO;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.Pagination;

public interface RateUserService {

	/**
	 * 根据主键查询一条数据
	 * */
	public RateUser findByRateUserId(Long rateUserId);

	/**
	 * 根据主键查询一条数据
	 * */
	public RateUser findByRateUserId(Long rateUserId, boolean lock);

	/**
	 * 更新RATE_USER一条数据
	 * */
	public void updateRateUser(RateUser rateUser);

	/**
	 * 添加RATE_USER一条数据
	 */
	RateUser addRateUser(RateUser rateUser);

	/**
	 * 根据ID加载一条RATE_USER数据
	 */
	RateUser getRateUserById(Long rateUserId);

	/**
	 * 加息券发放列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param rateUser
	 * @param customParams
	 * @return
	 */
	Pagination<RateUserVO> getRateUserPaging(int pageNo, int pageSize, RateUser rateUser, Map<String, Object> customParams);

	/**
	 * 加息券校验是否可以购买
	 * */
	public JsonView checkRateUser(RateUser rateUser, RateProduct rateProduct, Long userId, Integer cycleCount, String loanType, BigDecimal buyBalance);

	/**
	 * 加息券减一次
	 * */
	public RateUser subtractRateUser(RateUser rateUser);

	public void updateRateUserandHis(RateUser lockRateUser, Long userId, Long lendOrderId, Long loanApplicationId, RateUsageHisStateEnum state);

	/**
	 * 加息券发放导出报表
	 * 
	 * @param response
	 * @param rateUser
	 *            查询参数
	 * @param customParams
	 *            查询参数
	 */
	void exportExcel(HttpServletResponse response, RateUser rateUser, Map<String, Object> customParams);

	/**
	 * 发放加息券（一个人）
	 * 
	 * @param rateProductId
	 *            加息券ID
	 * @param userId
	 *            用户ID
	 * @param source
	 *            加息券来源ID
	 * @param adminId
	 *            管理员ID
	 */
	void handOutOne(Long rateProductId, Long userId, String source, Long adminId);

	/**
	 * 发放加息券（多个人）
	 * 
	 * @param rateProductId
	 *            加息券ID
	 * @param userIds
	 *            用户ID集合
	 * @param source
	 *            加息券来源ID
	 * @param adminId
	 *            管理员ID
	 */
	void handOutSome(Long rateProductId, List<Long> userIds, String source, Long adminId);

	/**
	 * 发放加息券（所有人）
	 * 
	 * @param rateProductId
	 *            加息券ID
	 * @param source
	 *            加息券来源ID
	 * @param adminId
	 *            rateProductId
	 */
	void handOutAll(Long rateProductId, String source, Long adminId);

	/**
	 * 批量添加RATE_USER数据
	 * 
	 * @param rateUserList
	 */
	void addRateUserBatch(List<RateUser> rateUserList);

	/**
	 * 找到所有的加息券
	 * 
	 * @param userId
	 * @return
	 */
	public List<RateUserVO> findRateUsersByUserId(Long userId, Integer cycleCount, String loanType, BigDecimal buyBalance, RateUserStatusEnum... statusEnum);
	
	/**
	 * 根据用户ID找到有效并且有可用次数的加息券
	 * */
	public List<RateUserVO> findRateUsersByUserId(Long userId, RateUserStatusEnum... statusEnums);
	
	/**
	 * 根据加息券产品ID，查询发放数量
	 * @param rateProductId
	 * @return
	 */
	Integer getCountByRateProductId(Long rateProductId);
	
	/**
	 * 发送微信加息券获取消息
	 * @param rateProductName 加息券名称
	 * @param userId 获得的用户ID
	 * @param endDate 加息券截止日期
	 */
	void sendWechatGetRateMsg(String rateProductName, Long userId, Date endDate);

	/**
	 * 修改加息券发放表状态
	 * @param rateUserId 加息券发放表ID
	 * @param ruse 发放表状态
	 */
	void changeRateUserStatus(Long rateUserId, RateUserStatusEnum ruse);

	/**
	 * 发送获取加息券短信
	 * @param mobileNo 手机号
	 * @param rateValue 加息值
	 * @param endDate 有效截止时间
	 */
	void sendSmsGetRateMsg(String mobileNo, BigDecimal rateValue, Date endDate);

	/**
	 * 查询用户即将到期加息券数量
	 * @param userId 用户ID
	 * @return
	 */
	Integer getRateUserExpireCountByUserId(Long userId);

	/**
	 * 发送即将到期加息券信息，针对定时器调用
	 */
	void sendExpireRateMsgForTimer();

	/**
	 * 发送即将到期加息券短信
	 * @param mobileNo 手机号
	 * @param count 到期数量
	 */
	void sendSmsExpireRateMsg(String mobileNo, Integer count);

	/**
	 * 发送微信加息券即将到期消息
	 * @param userId 用户ID
	 * @param count 到期数量
	 */
	void sendWechatExpireRateMsg(Long userId, Integer count);

}
