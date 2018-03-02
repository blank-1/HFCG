package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.ActivityBinding;
import com.xt.cfp.core.pojo.PrizePool;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserShare;
import com.xt.cfp.core.pojo.VoucherPhone;
import com.xt.cfp.core.pojo.ext.VoucherPhoneVO;

public interface VoucherPhoneService {

	/**
	 * 添加手机号
	 */
	VoucherPhone addVoucherPhone(VoucherPhone voucherPhone);
	
	/**
	 * 查找手机号
	 */
	VoucherPhone getVoucherPhone(String phone);
	/**
	 * 查找手机号通过绑定openid的手机号
	 */
	List<VoucherPhone> getVoucherPhoneBindingPhone(String phone, String activityId);
	/**
	 * 修改
	 */
	public void updateVoucherPhone(VoucherPhone voucherPhone);
	/**
	 * 查找手机号+活动编号
	 */
	List<VoucherPhone> getVoucherPhoneByPhone(String phone, String activityId);
	/**
	 * 获得活动信息是否 手机号和微信绑定
	 * @param openId
	 * @param activityId
	 * @return
	 */
	public VoucherPhone getVoucherPhone(String openId, String activityId);
	/**
	 * 查看是否有绑定(根据openid)
	 * @param openId
	 * @return
	 */
	public ActivityBinding getActivityBindingByOpenId(String openId);
	/**
	 * 查看是否有绑定(根据openid)
	 * @param openId
	 * @return
	 */
	public ActivityBinding getActivityBindingByPhone(String phone);
	/**
	 * 没有绑定添加绑定和活动信息
	 * @param openId
	 * @param phone
	 * @param activityId
	 */
	public ActivityBinding addActivityBindingAndVoucherPhone(String openId, String phone, String activityId);
	/**
	 * 发财富卷
	 * @param voucherPhone
	 * @param prize
	 */
	public void updateVoucherPhoneAndPrize(int prizeNum, Long userId, String activityId, Long bindingId);
	/**
	 * 修改绑定
	 * @param ab
	 */
	public void updateActivityBinding(ActivityBinding ab);
	/**
	 * 发送提现卷
	 */
	public void sendVoucherWithDraw(Long userId, String activityId, ActivityBinding ab, Long bindingId);
	/**
	 * 发送财富劵(不带上限)
	 * @param prizeNum
	 * @param userId
	 * @param activityId
	 * @param bindingId
	 */
	public void AddVoucherPhoneAndPrize(int prizeNum, Long userId, String activityId, Long bindingId);
	/**
	 * 添加分享信息
	 * @param userShare
	 * @return
	 */
	public UserShare addUserShare(UserShare userShare);
	/**
	 * 获取是否分享和分享次数
	 * @param userId
	 * @return
	 */
	public UserShare getUserSHareByUserId(Long userId, String activityId);
	/**
	 * 更新
	 * @param userShare
	 */
	void updateUserShare(UserShare userShare);
	/**
	 * 获取奖池列表
	 * @return
	 */
	List<PrizePool> getPrizePoolList(Boolean lock);
	/**
	 * 获取奖池中的奖品
	 * @return
	 */
	PrizePool getPrizePoolById(Long prizePoolId, Boolean lock);
	/**
	 * 抽奖
	 * @param userInfo
	 * @param prizePool
	 * @param userShare
	 */
	int turntableDraw(UserInfo userInfo, UserShare userShare, BigDecimal allBuyBalance);
	
	/**
	 * 查询用户抽奖次数
	 * @param map
	 * @return
	 */
	int getUserDrawNum(Map map);
	
	/**
	 * 查询活动中奖用户
	 * @param map
	 * @return
	 */
	List<VoucherPhoneVO> getHasLotteryDrawUsers(Map map);
	
	/**
	 * 加息券抽奖开始
	 * @param userId
	 * @return
	 */
	Map startLotteryDraw(UserInfo user);
}
