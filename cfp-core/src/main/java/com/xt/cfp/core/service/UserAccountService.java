package com.xt.cfp.core.service;

import com.external.yongyou.entity.http.YongYouBean;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserAccountHis;
import com.xt.cfp.core.util.Pagination;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by yulei on 2015/6/11.
 */
public interface UserAccountService {

    /**
     * 为普通用户初始化所有用户账户
     *
     * @param userId
     */
    void initUserAccounts(Long userId);

    /**
     * 为用户初始化某类型账户
     *
     * @param accountType
     */
    UserAccount initUserAccount(Long userId, AccountConstants.AccountTypeEnum accountType);

    /**
     * 改变账户状态
     *
     * @param accId
     * @param accountStatus
     */
    void changeAccountStatus(Long accId, AccountConstants.AccountStatusEnum accountStatus);

    /**
     * 根据账户Id获取账户数据
     *
     * @param accId
     * @return
     */
    UserAccount getUserAccountByAccId(Long accId);

    /**
     * 根据用户Id和账户类型编码 获取用户账户
     *
     * @param userId
     * @param accountTypeCode
     * @return
     */
    UserAccount getUserAccountByUserIdAndAccountTypeCode(Long userId, String accountTypeCode);

    /**
     * 锁定账户
     *
     * @param accountId
     */
    UserAccount lock(Long accountId);

    /**
     * 修改账户信息
     *
     * @param userAccount
     * @return
     */
    void updateUserAccount(UserAccount userAccount);

    /**
     * 创建账户历史记录
     *
     * @return
     */
    UserAccountHis createUserAccountHis(UserAccountHis userAccountHis);

    /**
     * 获取对应账户的所有历史记录
     *
     * @param accId
     * @return
     */
    List<UserAccountHis> getUserAccountHisByAccId(Long accId);
    /**
     * 获取账户的流水
     * @param pageNo
     * @param pageSize
     * @param accId
     * @param flowType
     * @param searchDate
     * @param isVisible 查询：1显示，0隐藏，null全部
     * @return
     */
    Pagination<UserAccountHis> getUserAccountHisByAccId(int pageNo, int pageSize, Long accId, String[] flowType, String[] searchDate, AccountConstants.VisiableEnum isVisible);
    /**
     * 获取该用户下的 账户流水
     * @param pageNo
     * @param pageSize
     * @param accId
     *@param customParams  @return
     */
    Pagination<UserAccountHis> getCrashFlowPaging(int pageNo, int pageSize, Long accId, Map<String, Object> customParams);

    /**
     * 获取该账户户下的 账户流水
     * @param pageNo
     * @param pageSize
     * @param userAccountHis
     *@param customParams  @return
     */
    Pagination<UserAccountHis> getCrashFlowPaging(int pageNo, int pageSize,UserAccountHis userAccountHis, Map<String, Object> customParams);

    /**
     * 获取该用户的资金账户
     * @param userId
     * @return
     */
    UserAccount getCashAccount(Long userId);

    /**
     * 新建用户帐户
     * @param userAccount
     */
    void createUserAccount(UserAccount userAccount);

    /**
     * 平台账号流水
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
	Pagination<UserAccountHis> getSystemFlowList(int pageNo, int pageSize, Map<String, Object> params);


    /**
     * 修正精度
     */
    void correctionPrecision();

    /**
     * 获得累计奖励
     * @param userId
     * @return
     */
    BigDecimal getUserTotalAward(Long userId);
    
    /**
     * 根据历史ID，获取一条历史记录
     * @param hisId 历史ID
     * @return
     */
    UserAccountHis getUserAccountHisById(Long hisId);

    /**
     *根据时间获取对应流水数据
     * */
	YongYouBean getUserAccountHisForYongYou(String startTime,
			String endTime);
    
	/**
	 * 账户历史数据修复
	 */
	void cashFlowCalculate(Long accId);
	
    /**
     * 更改账户历史记录
     * @param userAccountHis
     * @return
     */
    UserAccountHis updateUserAccountHis(UserAccountHis userAccountHis);
    
    /**
     * 根据根据流水的业务类型获取账户的流水记录
     * */
    List<UserAccountHis> getUserAccountHisByParams(Map map);

    /**
     * 获取用户省心计划账户
     * */
	List<UserAccount> getUserFinanceAccount(Long userId);
	
	/**
	 * 获取用户理财账户转账金额
	 * */
	BigDecimal getFinanceAccountPayValue(Long userId);

	/**
	 * 获取用户总共的理财中金额
	 * */
	BigDecimal getUserFinancingAccountValueByUserId(Long userId);

	/**
	 * 获取用户理财订单的理财中金额
	 * */
	BigDecimal getUserFinancingAccountValueByAccId(Long accId);

    /**
     * 临时
     * @return
     */
    List<Long> getErrorHisIds();


	/**
	 * 获取用户所有理财账户的所有已获奖励 --userId
	 * */
	BigDecimal getUserTotalFinanceAwardByUserId(Long userId);
	/**
	 * 获取用户指定理财账户的所有已获奖励  --lendOrderId
	 * */
	BigDecimal getUserTotalFinanceAwardByLendOrderId(Long lendOrderId);

	/**
	 *  省心计划自动转入余额：获取最近一次转出至现在的所有收支记录--除本金以外的收入
	 * */
	BigDecimal getSXJHLastNeedTurnValueIncomeWithoutCapital (Long accId) ;
	
	/**
	 *  省心计划自动转入余额：获取最近一次转出至现在的所有收支记录--所有支出
	 * */
	BigDecimal getSXJHLastNeedTurnValuePay (Map<Object,Object> param) ;
	
	/**
	 *  省心计划：获取所有的省心计划所有支出
	 * */
	BigDecimal getSXJHTurnValuePay (Map<Object,Object> param) ;

	/**
	 * 获取用户减少的奖励（如：取消）
	 */
	BigDecimal getUserTotalReduceAward(Long userId);

	/**
	 * 根据账户类型查询系统平台对应的账户
	 * @param accountTypeEnum
	 * @return
	 */
	UserAccount getPlatformAccountByType(AccountConstants.AccountTypeEnum accountTypeEnum);
}


