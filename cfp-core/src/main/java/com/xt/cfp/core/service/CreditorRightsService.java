package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.constants.ChannelTypeEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CreditorRightsHistory;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.ext.CreditorRightsCount;
import com.xt.cfp.core.pojo.ext.CreditorRightsExtVo;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LenderVO;
import com.xt.cfp.core.service.matchrules.MatchCreditorVO;
import com.xt.cfp.core.util.Pagination;

public interface CreditorRightsService {

    /**
     * 根据债权来源查找所有债权
     * @param channelTypeEnum
     * @return
     */
    List<CreditorRights> findAll(ChannelTypeEnum channelTypeEnum);

    /**
     * 根据借款申请ID查询债权列表
     * @param loanApplicationId
     * @return
     */
    List<CreditorRights> getByLoanApplicationId(long loanApplicationId);
    List<CreditorRights> getAvailidRightsByLoanApplicationId(long loanApplicationId);

    CreditorRights getSubjectRightsByLoanApplicationId(long loanApplicationId);

    /**
     * 根据订单id和债权状态查询债权列表
     * @param lendOrderId
     * @param statusEnum
     * @return
     */
    List<CreditorRights> getByLendOrderId(Long lendOrderId, List<String> statusEnum);

    /**
     * 根据接口id查询出借人列表(分页)
     * @param loanApplicationId
     * @return
     */
    Pagination<LenderVO> getLenderListByApplicationId(int pageNo, int pageSize,long loanApplicationId,CreditorRightsConstants.CreditorRightsStateEnum... status);

    /**
     * 修改MAP中的字段
     * @param creditorMap
     */
    void update(Map creditorMap);

    /**
     * 修改债权
     * @param creditorRights
     */
    void update(CreditorRights creditorRights);

    /**
     * 新增债权
     * @param creditorRights
     */
    void insert(CreditorRights creditorRights);

    /**
     * 根据借款产品id和出借订单id，统计购买总额
     * @param loanProductId
     * @param lendOrderId
     * @param customerAccountId
     * @return
     */
    BigDecimal getSumCreByOrderAndLoanPdt(long loanProductId, long lendOrderId, long customerAccountId);

    /**
	 * 分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
    Pagination<CreditorRightsExtVo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 根据id查找一条债权数据，并确定是否枷锁
     * @param creditorRightsId
     * @param lock
     * @return
     */
    CreditorRights findById(Long creditorRightsId, boolean lock);

    /**
     * 创建债权编号
     * @param fromWhere
     * @param lendOrderCode
     * @param loanApplicationCode
     * @return
     */
    String createRightsCode(char fromWhere, String lendOrderCode, String loanApplicationCode);

    //todo 此方法需要整理，用法不明确，名称也不对应
    List<CreditorRights> findRollOutRights(Map paraMap);

    /**
     * 生成债权
     * 创建债权
     * 根据还款计划生成债权分配明细
     * @param lendOrder
     * @param loanApplication
     * @param rightsCode
     * @param buyBalance
     * @param c
     * @param proportion
     * @param repaymentPlans
     * @param creditorRightsStateEnum
     * @param channelTypeEnum
     * @return
     * @throws Exception
     */
    CreditorRights createCreditorRights(LendOrder lendOrder, LoanApplication loanApplication, String rightsCode, BigDecimal buyBalance,
                                        char c, BigDecimal proportion, List<RepaymentPlan> repaymentPlans, CreditorRightsConstants.CreditorRightsStateEnum creditorRightsStateEnum, ChannelTypeEnum channelTypeEnum) throws Exception;


    /**
     * 生成债权变更历史
     * @param creditorRightsHistory
     */
    void newCreditorRightsHistory(CreditorRightsHistory creditorRightsHistory);

    /**
     * 添加债权记录
     * @param creditorRights
     * @return
     */
    CreditorRights addCreditorRights(CreditorRights creditorRights);

    /**
     * 出借债券列表页面(前台使用)
     * @param pageNo
     * @param pageSize
     * @param creditorRights
     * @param customParams
     * @return
     */
    Pagination<CreditorRightsExtVo> getCreditorRightsPaging(int pageNo, int pageSize,  CreditorRightsExtVo creditorRights, Map<String, Object> customParams);
    
    /**
     * 用户所有债权查询
     * @param creditorRights
     * @param customParams
     * @return
     */
    List<CreditorRightsExtVo> getUserAllCreditorRights(CreditorRightsExtVo creditorRights, Map<String, Object> customParams);
    
    CreditorRightsCount selectUserRightsByDaiHui(Long userId);
    
    /**
     * 查询 分页 投标记录
     * @param pageNo
     * @param pageSize
     * @param pageNo1
     * @param pageSize1
     * @param loanApplicationId
     * @param effective
     * @return
     */
    Pagination<LenderRecordVO> findLendOrderDetailPaging(int pageNo, int pageSize, int pageNo1, int pageSize1, Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective);
    
    /**
     * 获取投资记录数量
     * @param loanApplicationId
     * @param effective
     * @return
     */
    Integer findLendOrderDetailCount(Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective);
    
	/**
	 * 获取最近还款日
	 * @param detailRightsList
	 * @return
	 * @throws ParseException
	 */
	Date getRecentPayDate(List<RightsRepaymentDetail> detailRightsList) throws ParseException;
	
	/**
	 * 根据债权ID，获取一条扩展信息
	 * @param creditorRightsId 债权ID
	 * @return
	 */
	CreditorRightsExtVo getCreditorRightsDetailById(Long creditorRightsId);
	
    public List<CreditorRights> getByLoanApplicationId(long loanApplicationId, ChannelTypeEnum... channelTypeEnum);
    /**
     * 计算债权剩余价值
     * 由于此方法用于计算债权瞬时的剩余价值，因此要考虑其结果不具有延续性、最终性，若要用此方法的结果参与逻辑运算，应在计算的最外层加锁
     * @param creditorRightsId
     * @param calculateDayInterest
     * @return
     */
    BigDecimal calSurplusPrice(Long creditorRightsId, boolean calculateDayInterest);

    /**
     * 根据债权id获取当前期债权还款明细记录
     * @param creditorRightsId
     * @return
     */
    RightsRepaymentDetail getCurrentRpdByCrId(Long creditorRightsId);

    /**
     * 根据债权id获取最近的一笔待还债权还款明细记录
     * @param creditorRightsId
     * @return
     */
    RightsRepaymentDetail getNearestNeedRepayRpdByCrId(Long creditorRightsId);

    /**
     * 根据债权id和日期字段，获取相应期债权还款明细记录
     * @param creditorRightsId
     * @param date
     * @return
     */
    RightsRepaymentDetail getRelevantRpdByCrIdAndDate(Long creditorRightsId, Date date);
    /**
     * 转让债券进入债券市场
     * @param creditorRightsId
     */
    void turnCreditor(BigDecimal rightAcount, Long creditorRightsId, int surpMonth);

    /**
     * 根据转让申请和转让明细，执行债权转让
     * @param applyRecord
     */
    void turnCreditor(CreditorRightsTransferApplication applyRecord)throws Exception;

    /**
     * 省心计划处理债权转让
     * @param lendOrder
     * @param matchCreditorVO
     * @param transferApplication
     */
    void newRightsForFinanceLendOrder(LendOrder lendOrder, MatchCreditorVO matchCreditorVO, CreditorRightsTransferApplication transferApplication);

    /**
     * 债权转让预计收益
     * @param creditorRightsId
     * @param amount
     * @return
     */
	String getExpectRightProfit(Long creditorRightsApplyId, BigDecimal amount);
    /**
     * 微信转让列表
     * @param pageNo
     * @param pageSize
     * @param vo
     * @param customParams
     * @return
     */
	Pagination<CreditorRightsExtVo> getCreditorRightsPagingByWeiXin(int pageNo,
			int pageSize, CreditorRightsExtVo vo,
			Map<String, Object> customParams);

	/**
	 * 我的理财-省心计划-出借明细 列表查询
	 * @param pageNo 第几页
	 * @param pageSize 每页条数
	 * @param creditorRights 债权字段查询
	 * @param customParams 自定义查询
	 * @return
	 */
	Pagination<CreditorRightsExtVo> getSXJHCreditorRightsDetailPaging(
			int pageNo, int pageSize, CreditorRightsExtVo creditorRights,
			Map<String, Object> customParams);
	
	/**
	 * 【省心计划】根据省心计划主出借订单ID，查询已经匹配到子标的债权列表
	 * @param lendOrderId 省心计划出借订单ID
	 * @return
	 */
	List<CreditorRights> getCreditorRightsByLendOrderPid(Long lendOrderId);
	/**
	 * 【省心计划】根据用户ID，查询已经匹配到子标的债权列表
	 * @param userId 用户ID
	 * @return
	 */
	List<CreditorRights> getCreditorRightsByUserId(Long userId);

	/**
	 * [省心计划工具]获取子标，预期投标奖励
	 * @param loanApplicationId 标的ID
	 * @param amount 投资金额
	 * @return
	 */
	BigDecimal getExpectAward(Long loanApplicationId, BigDecimal amount);
	
	/**
	 * [省心计划工具]查询某人省心计划，获取总的预期投标奖励（用户ID --> 省心订单ID --> 子订单List --> 债权List）
	 * @param userId 用户ID
	 * @return
	 */
	BigDecimal getTotalExpectAwardByUserId(Long userId);
}
