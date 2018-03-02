package com.xt.cfp.core.service.impl;

import com.external.yongyou.entity.UserAccountHistory;
import com.external.yongyou.entity.http.YongYouBean;
import com.external.yongyou.util.YongYouUtil;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.AccountConstants.BusinessTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserAccountHis;
import com.xt.cfp.core.service.UserAccountOperateService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.Pagination;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulei on 2015/6/12.
 */
@Service
//@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private MyBatisDao myBatisDao;

    @Autowired
    private UserAccountOperateService userAccountOperateService;
    
    @Override
    @Transactional
    public void initUserAccounts(Long userId) {
        //初始化创建一个出借人账户和一个借款人账户
        UserAccount lendAccount = createUserAccount(userId, AccountConstants.AccountTypeEnum.LENDER_ACCOUNT);
        addUserAccount(lendAccount);
        UserAccount borrowAccount = createUserAccount(userId, AccountConstants.AccountTypeEnum.BORROW_ACCOUNT);
        addUserAccount(borrowAccount);
    }

    @Override
    public UserAccount initUserAccount(Long userId, AccountConstants.AccountTypeEnum accountType) {
        UserAccount userAccount = addUserAccount(createUserAccount(userId, accountType));
        return userAccount;
    }

    /**
     * 添加用户账户
     *
     * @param userAccount
     */
    @Transactional
    private UserAccount addUserAccount(UserAccount userAccount) {
        myBatisDao.insert("USER_ACCOUNT.insert", userAccount);
        return userAccount;
    }

    /**
     * 创建指定用户的指定类型的账户
     *
     * @param userId
     * @param accountType
     * @return
     */
    private UserAccount createUserAccount(Long userId, AccountConstants.AccountTypeEnum accountType) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userId);
        userAccount.setAccTypeCode(accountType.getValue());
        userAccount.setCreateTime(new Date());
        userAccount.setAccStatus(AccountConstants.AccountStatusEnum .NORMAL.getValue());//默认是正常的账户
        return userAccount;
    }


    @Override
    @Transactional
    public void changeAccountStatus(Long accId, AccountConstants.AccountStatusEnum  accountStatus) {
        //判断accid是否为null
        if (null == accId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accId", "null");

        //判断状态参数是否为null
        if (null == accountStatus)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accountStatus", "null");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accId", accId);
        params.put("accStatus", accountStatus.getValue());

        myBatisDao.update("USER_ACCOUNT.changeAccountStatus", params);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUserAccountByAccId(Long accId) {
        //判断参数是否为null
        if (null == accId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accId", "null");

        UserAccount userAccount = myBatisDao.get("USER_ACCOUNT.selectByPrimaryKey", accId);
        return userAccount;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUserAccountByUserIdAndAccountTypeCode(Long userId, String accountTypeCode) {
        //判断参数是否为null
        if (null == userId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");

        //判断参数是否为null
        if (StringUtils.isEmpty(accountTypeCode))
            throw new SystemException(ValidationErrorCode.ERROR_STRING_CAN_NOT_BE_EMPTY).set("accountStatusEnum", accountTypeCode);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("accTypeCode", accountTypeCode);

        UserAccount userAccount = myBatisDao.get("USER_ACCOUNT.getUserAccountByUserIdAndAccountTypeCode", params);
        return userAccount;
    }

    @Override
//    @Transactional(readOnly = true)
    public UserAccount lock(Long accId) {
        //判断参数是否为null
        if (null == accId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accId", "null");

        UserAccount userAccount = myBatisDao.get("USER_ACCOUNT.lockAccountByPrimaryKey", accId);
        return userAccount;
    }

    @Override
    @Transactional
    public void updateUserAccount(UserAccount userAccount) {
        //判断参数是否为null
        if (userAccount == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("bondSource", "null");

        //判断id属性是否为空
        if (userAccount.getAccId() == null)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("accId", "null");

        myBatisDao.update("USER_ACCOUNT.updateByPrimaryKeySelective", userAccount);
    }

    @Override
    @Transactional
    public UserAccountHis createUserAccountHis(UserAccountHis userAccountHis) {
        //判断参数是否为null
        if (userAccountHis == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userAccountHis", "null");

        myBatisDao.insert("USER_ACCOUNT_HIS.insert", userAccountHis);
        return userAccountHis;
    }

    @Override
    public List<UserAccountHis> getUserAccountHisByAccId(Long accId) {
        //判断参数是否为null
        if (null == accId)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accId", "null");

        //根据userId查询
        List<UserAccountHis> userAccountHisList = myBatisDao.getList("USER_ACCOUNT_HIS.selectByAccId", accId);
        return userAccountHisList;
    }

    @Override
    public Pagination<UserAccountHis> getCrashFlowPaging(int pageNo, int pageSize, Long accId,  Map<String, Object> customParams) {
        Pagination<UserAccountHis> re = new Pagination<UserAccountHis>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accId", accId);
        params.put("customParams", customParams);


        int totalCount = this.myBatisDao.count("getCrahsFlowPaging", params);
        List<UserAccountHis> uah = this.myBatisDao.getListForPaging("getCrahsFlowPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    @Override
    public Pagination<UserAccountHis> getCrashFlowPaging(int pageNo, int pageSize, UserAccountHis userAccountHis, Map<String, Object> customParams) {
        Pagination<UserAccountHis> re = new Pagination<UserAccountHis>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userAccountHis", userAccountHis);
        params.put("customParams", customParams);


        int totalCount = this.myBatisDao.count("getFlowPaging", params);
        List<UserAccountHis> uah = this.myBatisDao.getListForPaging("getFlowPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    @Override
    public UserAccount getCashAccount(Long userId) {
        return getUserAccountByUserIdAndAccountTypeCode(userId, AccountConstants.AccountTypeEnum.LENDER_ACCOUNT.getValue());
    }

    @Override
    public void createUserAccount(UserAccount userAccount) {
        addUserAccount(userAccount);
    }

    @Override
	public Pagination<UserAccountHis> getUserAccountHisByAccId(int pageNo,
			int pageSize, Long accId,String[] flowType, String[] searchDate, AccountConstants.VisiableEnum isVisible) {
		if(accId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accId", accId);
		Pagination<UserAccountHis> re = new Pagination<UserAccountHis>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("accId", accId);
        if(flowType != null && flowType.length != 0){
        	if(flowType.length == 5){
        		params.put("flowTypes", null);
        	}else{
        		params.put("flowTypes", flowType);
        	}
        	
        }
        else{
        	params.put("flowTypes", null);
        }    
        params.put("searchDate", "");
        if(searchDate != null && searchDate.length !=3){
	    	for(String searchDateStr : searchDate){
	    		if("t_6".equals(searchDateStr)){
	    			params.put("searchDate", "t_6");
	    			break;
	    		}else if("t_1".equals(searchDateStr)){
	    			params.put("searchDate", "t_1");
	    			break;
	    		}else if("t_7".equals(searchDateStr)){
	    			params.put("searchDate", "t_7");
	    			break;
	    		}
	    	}
        }
        
        if(null != isVisible){
        	params.put("isVisible", isVisible.getValue());
        }
      
        int totalCount = this.myBatisDao.count("getUserDisplayAccountHisByAccId", params);
        List<UserAccountHis> uah = this.myBatisDao.getListForPaging("getUserDisplayAccountHisByAccId", params, pageNo, pageSize);
        for(UserAccountHis userAccountHis:uah){
        	userAccountHis.setBusType(BusinessTypeEnum.getBusinessTypeByValue(userAccountHis.getBusType()).getDesc());
        }
        re.setTotal(totalCount);
        re.setRows(uah);
        if(flowType.length==0){
     	   re.setUrl("");
        }else{
     	  if(flowType.length==5){
     		   re.setUrl("");
     	  }else{
     		   re.setUrl(flowType[0]);
     	  }
     	
     }

        return re;
	}

	@Override
	public Pagination<UserAccountHis> getSystemFlowList(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<UserAccountHis> re = new Pagination<UserAccountHis>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("getFlowPaging", params);
        List<UserAccountHis> uah = this.myBatisDao.getListForPaging("getFlowPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}

    @Override
    public void correctionPrecision() {
        List<UserAccount> userAccountList = myBatisDao.getList("USER_ACCOUNT.correctionPrecision");


        for (int i=0;i<userAccountList.size();i++){
            UserAccount userAccount = userAccountList.get(i);
            System.out.println(userAccountList.size()+">>>>>>>>>>>"+userAccount.getAccId());
            AccountValueChangedQueue avcq = new AccountValueChangedQueue();

            BigDecimal value18 = userAccount.getAvailValue();
            BigDecimal value2 = userAccount.getAvailValue2();

            BigDecimal calc = value2.subtract(value18);


            AccountValueChanged avc = new AccountValueChanged(userAccount.getAccId(),calc,BigDecimal.ZERO, AccountConstants.AccountOperateEnum.INCOM.getValue(),
                    BusinessTypeEnum.CORRECTION_PRECISION.getValue(),AccountConstants.AccountChangedTypeEnum.CORRECTION_PRECISION.getValue(),
                    AccountConstants.VisiableEnum.HIDDEN.getValue(),-1L,AccountConstants.OwnerTypeEnum.CORRECTION_PRECISION.getValue(),-1L,new Date()," 系统精度修复，收入"+ calc+"元",true);
            avcq.addAccountValueChanged(avc);
            
            userAccountOperateService.execute(avcq);
        }

    }
    
    
	@Override
	public BigDecimal getUserTotalAward(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 判断参数是否为null
		if (null == userId)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
		params.put("userId", userId);
		String[] busTypes = new String[] { AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(), 
				AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_RATE_COUPON.getValue(),
				AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_ACTIVITY_AWARD.getValue() };
		params.put("busTypes", busTypes);
		BigDecimal result = myBatisDao.get("getUserAwards", params);
		return result;
	}
	
	@Override
	public BigDecimal getUserTotalReduceAward(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 判断参数是否为null
		if (null == userId)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
		params.put("userId", userId);
		String[] busTypes = new String[] { AccountConstants.BusinessTypeEnum.FEESTYPE_ERROR_PAY_AWARD.getValue()};
		params.put("busTypes", busTypes);
		BigDecimal result = myBatisDao.get("getUserAwards", params);
		return result;
	}

	@Override
	public UserAccount getPlatformAccountByType(AccountConstants.AccountTypeEnum accountTypeEnum) {
		return myBatisDao.get("USER_ACCOUNT.getPlatformAccountByType", accountTypeEnum.getValue());
	}

	/**
     * 根据历史ID，获取一条历史记录
     * @param hisId 历史ID
     * @return
     */
	@Override
	public UserAccountHis getUserAccountHisById(Long hisId) {
		return myBatisDao.get("USER_ACCOUNT_HIS.selectByPrimaryKey", hisId);
	}

	@Override
	public YongYouBean getUserAccountHisForYongYou(String startTime, String endTime) {
		YongYouBean respBean = new YongYouBean();
		Map<String, Object> dateMap = null;
		try {
			Date startTime_ , endTime_;
			dateMap = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
				String pattern1 = "yyyy-MM-dd HH:mm:ss";
				startTime_ = com.xt.cfp.core.util.DateUtil.parseStrToDate(startTime + " 00:00:00", pattern1);
				endTime_ = com.xt.cfp.core.util.DateUtil.parseStrToDate(endTime + " 23:59:59", pattern1);
				dateMap.put("startTime", startTime_);
				dateMap.put("endTime", endTime_);
			}
			
			dateMap.put("busTypes",  BusinessTypeEnum.values());
			List<UserAccountHistory> list = myBatisDao.getList("queryForYongYouHis", dateMap);
			respBean.setResponseList(list);
			respBean.setAppId(YongYouUtil.getAppId());
			
		} catch (Exception e) {
			throw new SystemException(SystemErrorCode.SYSTEM_ERROR_CODE);
		}
		return respBean;
	}

	/**
	 * 修复历史账户
	 */
	@Override
	@Transactional
	public void cashFlowCalculate(Long accId) {
		UserAccount userAccount = new UserAccount();
		userAccount.setValue(BigDecimal.ZERO);
		userAccount.setValue2(BigDecimal.ZERO);
		userAccount.setAvailValue(BigDecimal.ZERO);
		userAccount.setAvailValue2(BigDecimal.ZERO);
		userAccount.setFrozeValue(BigDecimal.ZERO);
		userAccount.setFrozeValue2(BigDecimal.ZERO);
		
	    BigDecimal valueBefore = BigDecimal.ZERO;
	    BigDecimal valueBefore2 = BigDecimal.ZERO;
	    BigDecimal availValueBefore = BigDecimal.ZERO;
	    BigDecimal availValueBefore2 = BigDecimal.ZERO;
	    BigDecimal frozeValueBefore = BigDecimal.ZERO;
	    BigDecimal frozeValueBefore2 = BigDecimal.ZERO;
		
		List<UserAccountHis> userAccountHisList = this.getUserAccountHisByAccId(accId);
		for (int i = 0; i < userAccountHisList.size(); i++) {
			UserAccountHis his = (UserAccountHis) userAccountHisList.get(i);
			if (his.getChangeType().equals("1")) {
				// 可用价值
				userAccount.setAvailValue(userAccount.getAvailValue().add(his.getChangeValue()));
				userAccount.setAvailValue2(userAccount.getAvailValue2().add(his.getChangeValue2()));
				// 价值总额
				userAccount.setValue(userAccount.getValue().add(his.getChangeValue()));
				userAccount.setValue2(userAccount.getValue2().add(his.getChangeValue2()));
			} else if (his.getChangeType().equals("2")) {
				// 可用价值
				userAccount.setAvailValue(userAccount.getAvailValue().subtract(his.getChangeValue()));
				userAccount.setAvailValue2(userAccount.getAvailValue2().subtract(his.getChangeValue2()));
				// 价值总额
				userAccount.setValue(userAccount.getValue().subtract(his.getChangeValue()));
				userAccount.setValue2(userAccount.getValue2().subtract(his.getChangeValue2()));
			} else if (his.getChangeType().equals("3")) {
				// 冻结价值
				userAccount.setFrozeValue(userAccount.getFrozeValue().subtract(his.getChangeValue()));
				userAccount.setFrozeValue2(userAccount.getFrozeValue2().subtract(his.getChangeValue2()));
				// 价值总额
				userAccount.setValue(userAccount.getValue().subtract(his.getChangeValue()));
				userAccount.setValue2(userAccount.getValue2().subtract(his.getChangeValue2()));
			} else if (his.getChangeType().equals("4")) {
				// 冻结价值
				userAccount.setFrozeValue(userAccount.getFrozeValue().add(his.getChangeValue()));
				userAccount.setFrozeValue2(userAccount.getFrozeValue2().add(his.getChangeValue2()));
				// 可用价值
				userAccount.setAvailValue(userAccount.getAvailValue().subtract(his.getChangeValue()));
				userAccount.setAvailValue2(userAccount.getAvailValue2().subtract(his.getChangeValue2()));
			} else if (his.getChangeType().equals("5")) {
				// 冻结价值
				userAccount.setFrozeValue(userAccount.getFrozeValue().subtract(his.getChangeValue()));
				userAccount.setFrozeValue2(userAccount.getFrozeValue2().subtract(his.getChangeValue2()));
				// 可用价值
				userAccount.setAvailValue(userAccount.getAvailValue().add(his.getChangeValue()));
				userAccount.setAvailValue2(userAccount.getAvailValue2().add(his.getChangeValue2()));
			}
			
			UserAccountHis newHis = new UserAccountHis();
			newHis.setHisId(his.getHisId());//主键ID
			// 设置after
			newHis.setValueAfter(userAccount.getValue());
			newHis.setValueAfter2(userAccount.getValue2());
			newHis.setAvailValueAfter(userAccount.getAvailValue());
			newHis.setAvailValueAfter2(userAccount.getAvailValue2());
			newHis.setFrozeValueAfter(userAccount.getFrozeValue());
			newHis.setFrozeValueAfter2(userAccount.getFrozeValue2());
			
			// 设置before
			newHis.setValueBefore(valueBefore);
			newHis.setValueBefore2(valueBefore2);
			newHis.setAvailValueBefore(availValueBefore);
			newHis.setAvailValueBefore2(availValueBefore2);
			newHis.setFrozeValueBefore(frozeValueBefore);
			newHis.setFrozeValueBefore2(frozeValueBefore2);
			
			// 执行添加
			UserAccountHis updateHis = this.updateUserAccountHis(newHis);
			
		    valueBefore = updateHis.getValueAfter();
		    valueBefore2 = updateHis.getValueAfter2();
		    availValueBefore = updateHis.getAvailValueAfter();
		    availValueBefore2 = updateHis.getAvailValueAfter2();
		    frozeValueBefore = updateHis.getFrozeValueAfter();
		    frozeValueBefore2 = updateHis.getFrozeValueAfter2();
			
		}

	}
	
	/**
	 * 修改历史账户
	 */
	@Override
	public UserAccountHis updateUserAccountHis(UserAccountHis userAccountHis) {
		myBatisDao.update("USER_ACCOUNT_HIS.updateByValue", userAccountHis);
		return userAccountHis;
	}

	@Override
	public List<UserAccountHis> getUserAccountHisByParams(Map map) {
		myBatisDao.getList("USER_ACCOUNT_HIS.getUserAccountHisByParams",map);
		return null;
	}

	@Override
	public List<UserAccount> getUserFinanceAccount(Long userId) {
		return myBatisDao.getList("USER_ACCOUNT.getUserFinanceAccount",userId);
	}

	@Override
	public BigDecimal getFinanceAccountPayValue(Long userId) {
		// TODO Auto-generated method stub
		return myBatisDao.get("USER_ACCOUNT_HIS.getFinanceAccountPayValue", userId);
	}
	
	@Override
	public BigDecimal getUserFinancingAccountValueByUserId(Long userId){
		return myBatisDao.get("USER_ACCOUNT.getUserFinancingAccountValueByUserId", userId);
	}
	
	@Override
	public BigDecimal getUserFinancingAccountValueByAccId(Long accId){
		return myBatisDao.get("USER_ACCOUNT.getUserFinancingAccountValueByAccId", accId);
	}

    @Override
    public List<Long> getErrorHisIds() {
        return myBatisDao.getList("USER_ACCOUNT.getErrorHisIds");
    }

	@Override
	public BigDecimal getUserTotalFinanceAwardByUserId(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 判断参数是否为null
		if (null == userId)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
		params.put("userId", userId);
		String[] busTypes = new String[] { AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
				AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_RATE_COUPON.getValue(),
				AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_ACTIVITY_AWARD.getValue() };
		params.put("busTypes", busTypes);
		BigDecimal result = myBatisDao.get("USER_ACCOUNT_HIS.getUserFinanceAwardsByUserId", params);
		return result;
	}

	@Override
	public BigDecimal getUserTotalFinanceAwardByLendOrderId(Long lendOrderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 判断参数是否为null
		if (null == lendOrderId)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("lendOrderId", "null");
		params.put("lendOrderId", lendOrderId);
		String[] busTypes = new String[] { AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
				AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_RATE_COUPON.getValue(),
				AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_ACTIVITY_AWARD.getValue() };
		params.put("busTypes", busTypes);
		BigDecimal result = myBatisDao.get("USER_ACCOUNT_HIS.getUserFinanceAwardsByLendOrderId", params);
		return result;
	}

	@Override
	public BigDecimal getSXJHLastNeedTurnValueIncomeWithoutCapital(Long accId) {
		BigDecimal value = myBatisDao.get("USER_ACCOUNT_HIS.getSXJHLastNeedTurnValueIncomeWithoutCapital", accId);
		return value == null? new BigDecimal(0):value;
	}

	@Override
	public BigDecimal getSXJHLastNeedTurnValuePay(Map<Object,Object> param) {
		BigDecimal value = myBatisDao.get("USER_ACCOUNT_HIS.getSXJHLastNeedTurnValuePay", param);
		return value == null? new BigDecimal(0):value;
	}
	
	@Override
	public BigDecimal getSXJHTurnValuePay(Map<Object,Object> param) {
		BigDecimal value = myBatisDao.get("USER_ACCOUNT_HIS.getSXJHTurnValuePay", param);
		return value == null? new BigDecimal(0):value;
	}


}
