package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.VoucherProductVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.Pagination;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VoucherService {


    /**
     * 财富券产品列表
     * @param pageNo
     * @param pageSize
     * @param voucherProduct
     * @param customParams
     * @return
     */
    Pagination<VoucherProductVO> getVoucherProductPaging(int pageNo, int pageSize, VoucherProduct voucherProduct, Map<String, Object> customParams);

    /**
     * 添加财富券产品
     * @param voucherProduct
     */
    VoucherProduct addVoucherProduct(VoucherProduct voucherProduct);

    /**
     * 详情
     * @param voucherProductId
     * @return
     */
    VoucherProductVO getVoucherProductById(Long voucherProductId);

    /**
     * 修改财富券产品状态
     * @param voucherProductId
     * @param usage
     */
    void changeVoucherProductStatus(Long voucherProductId, VoucherConstants.VoucherProductStatus usage);

    /**
     * 获取财富券列表
     * @param pageNo
     * @param pageSize
     * @param voucherVO
     * @param customParams
     * @return
     */
    Pagination<VoucherVO> getVoucherPaging(int pageNo, int pageSize, VoucherVO voucherVO, Map<String, Object> customParams);

    /**
     * 获得可用财富券产品列表
     * @return
     */
    List<VoucherProduct> getAvalibleHandOutVoucherProductList();

    /**
     * 根据传入日期，刷新财富券产品状态
     * @param date
     */
    void refreshProductStatus(Date date);

    /**
     * 根据传入日期，刷新财富券状态
     * @param date
     */
    void refreshStatus(Date date);

    /**
     * 发放财富券
     */
    Voucher handOut(Long voucherProductId,Long userId,String sourceType,String sourceDesc);

    /**
     * 10.1注册礼包发放
     * @param voucherProductId
     * @param userId
     * @param sourceType
     * @param sourceDesc
     * @return
     */
    Voucher sendVourcherOnOct(Long voucherProductId,Long userId,String sourceType,String sourceDesc);
    /**
     * 批量发放财富券
     */
    void handOut(Long voucherProductId,List<Long> userIds,String sourceType,String sourceDesc);
    /**
     * 批量发放财富券
     */
    Voucher addVoucherBatch(List<Voucher> voucherList);

    /**
     * 给所有人发放财富券
     */
    void handOutToEveryOne(Long voucherProductId,String sourceType,String sourceDesc);

    /**
     * 根据id查询财富券详情
     * @param voucherId
     * @return
     */
    VoucherVO getVoucherById(Long voucherId);

    /**
     * 获得个人可用财富券总额
     * @param userId
     * @return
     */
    BigDecimal getAllVoucherValue(Long userId);

    /**
     * 获得相应状态个人可用财富券总额
     * @param userId
     * @return
     */
    BigDecimal getAllVoucherValue(Long userId,VoucherConstants.VoucherStatus voucherStatus);
    /**
     * 获得个人可用财富券总额
     * @param userId
     * @return
     */
    List<VoucherVO> getAllVoucherList(Long userId,BigDecimal buyBalance,VoucherConstants.UsageScenario ... usageScenarios);

    /**
     * 冻结财富券
     * @param payOrder
     * @return 返回冻结财富券金额
     */
    BigDecimal frozeVoucher(PayOrder payOrder);
    /**
     * 计算财富券
     * @param payOrder
     * @return 返回财富券金额
     */
    BigDecimal calcVoucherValue(PayOrder payOrder,VoucherConstants.VoucherStatus status);

    /**
     * 计算某个支付订单使用了多少财富劵支付
     * @param payOrder
     * @return
     */
    BigDecimal calcVoucherValue(PayOrder payOrder);


    /**
     * 财富券是否使用异常
     * @param payOrder
     * @return 返回财富券金额
     */
    boolean isVoucherAvaliable(PayOrder payOrder);

    /**
     * 搭建财富券与支付单
     * @param payOrder
     * @param voucherVOs
     */
    void linkVoucher(PayOrder payOrder, List<VoucherVO> voucherVOs);

    /**
     * 返还财富券
     * @param voucherId
     */
    void backVoucher(Long detailId,String remark,Long ... voucherId);

    /**
     * 获取财富券支付明细
     * @param voucherId
     * @return
     */
    List<VoucherPayOrderDetail> getVoucherPayOrderDetail(Long voucherId);

    /**
     * 获取财富券支付明细(最近一条记录)
     * @param voucherId
     * @return
     */
    VoucherPayOrderDetail getRecentVoucherPayOrderDetail(Long voucherId);

    /**
     * 记录财富券历史
     * @param voucherPayOrderDetail
     * @return
     */
    VoucherPayOrderDetail recordVoucherPayOrderDetail(VoucherPayOrderDetail voucherPayOrderDetail);

    /**
     * 获取该支付明细下的所有财富券
     * @param detailId
     * @return
     */
    List<Voucher> getVoucherList(Long detailId,VoucherConstants.UsageScenario ... usageScenario);


    /**
     * 校验财富券是否可用
     * @param currentUser
     * @param lendOrder
     * @param voucherList
     * @return
     */
    JsonView voucherValidate(UserInfo currentUser,LendOrder lendOrder,List<VoucherVO> voucherList);

    /**
     * 修改财富券状态
     * @param voucherStatus
     */
    void changeVoucherStatus(Long voucherId,VoucherConstants.VoucherStatus voucherStatus);

    /**
     * 成功使用财富券
     * @param voucherList
     */
    void useVoucher(List<Voucher> voucherList);

    /**
     * 根据支付订单发放财富券(购买时发放)
     * @param payOrder
     */
    void publishVoucher(PayOrder payOrder);

    /**
     * 财富券导出
     * @param response
     * @param voucherVO
     * @param customParams
     */
    void exportExcel(HttpServletResponse response, VoucherVO voucherVO, Map<String, Object> customParams);

    /**
     * 提现券冻结
     * @param voucherId
     * @param withDraw
     * @return
     */
    void frozeVoucherWithDraw(Long voucherId,WithDraw withDraw);

    /**
     * 统计该用户下符合条件的、可使用的代金券产品
     * @param userId
     * @return
     */
    List<VoucherProductVO> getVoucherStatistics(Long userId,VoucherConstants.UsageScenario ... usageScenarios);
    /**
     * 微信发送财富劵
     * @param voucherProductId
     * @param userId
     */
    public void sendVoucherMessage(Long voucherProductId, Long userId, Date endDate);
    /**
     * 微信定时推送财富卷到期提醒
     * @param date
     * @return
     */
    public void wechatVoucherExpireMsg(Date date);

    /**
     * 短信发送获取财富券
     * @param userId
     * @param voucher
     */
	void sendGetVoucherSms(Long userId, Voucher voucher);

	/**
	 * 短信发送即将到期财富券
	 * @param mobileNo
	 * @param count
	 * @param value
	 */
	void sendSmsExpireVoucherMsg(String mobileNo, Integer count,
			BigDecimal value);

	/**
	 * 根据用户ID，查询该用户即将过期的财富券
	 * @param userId
	 * @return
	 */
	List<Voucher> getVoucherExpireByUserId(Long userId);

	/**
	 * 定时器调用，查询即将到期的财富券，并发送提示信息
	 */
	void sendExpireVoucherMsgForTimer();

    /**
     * 根据支付单id查找相应的财富劵（根据财富劵表的支付明细id查找）
     * @param payId
     * @return
     */
    Voucher getVoucherByPayId(Long payId);

    /**
     * 解冻财富劵
     * @param voucherId
     */
    void unFreezeVoucher(Long voucherId);

    /**
     * 取消已使用的财富劵
     * @param voucherId
     */
    void rollbackVoucherUse(Long voucherId);
    
    /**
     * 用户注册发放财富券
     * @param userID
     */
    void registerRelease(Long userID);
    
    /**
     * 用户实名发放财富券
     * @param userID
     */
	void AuditRelease(Long userId); 
}
