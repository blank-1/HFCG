package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.ReissuedAwardVO;
import com.xt.cfp.core.pojo.ext.RepairRightsRepaymentDetailData;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.*;

import jodd.io.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yulei on 2015/7/27.
 */
@Service
public class UtilsServiceImpl implements UtilsService {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    private AwardDetailService awardDetailService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    @Autowired
    private DistributionInviteService distributionInviteService ;
    @Autowired
    private WhiteTabsService whiteTabsService ;
    @Autowired
    private InviteWhiteTabsService inviteWhiteTabsService ;
    @Autowired
    private CommiProfitService commiProfitService ;
    @Autowired
    private RepaymentPlanService repaymentPlanService ;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService ;
    @Autowired
    private PayService payService;
    
    @Override
    @Transactional
    public Map<String,Object> doRecharge(String reason, Long userId, BigDecimal amount, Map<String, Object> params, 
    		AdminInfo currentUser, String externalNo, String channelCode) {
        Date now = new Date();

        if (reason.equals("1")) //活动打款
            activityAwards(userId, amount, params, now);

        if (reason.equals("2")){ //线下充值
        	RechargeOrder param = new RechargeOrder();
        	param.setExternalNo(externalNo);
        	param.setChannelCode(channelCode);
        	List<RechargeOrder>  roList = rechargeOrderService.findBy(param);
             if(roList != null && roList.size() > 0) {
            	 Map<String,Object> map = new HashMap<String,Object>();
            	 map.put("flag",false);
            	 map.put("info", "该交易流水号已经充值过！");
            	 return  map ;
             }
            offlineRecharge(userId, amount, params, now, currentUser,externalNo,channelCode);
        }
        
        return null;
    }

    @Override
    @Transactional
    public void doRechargeForAward(String[] title, List<Map<String, Object>> result, Map<String, Object> params) {
        ValidationUtil.checkRequiredEntry(params, "awardName");

        Date now = new Date();
        String userRealName;
        String mobileNo;
        BigDecimal amount;
        UserInfo userInfo;
        UserInfoExt infoExt;
        AccountValueChangedQueue queue = new AccountValueChangedQueue();
        for (Map<String, Object> map : result) {
            userRealName = map.get(title[0]).toString();
            mobileNo = map.get(title[1]).toString();
            amount = new BigDecimal(map.get(title[2]).toString());
            System.out.println("userRealName= " +userRealName +" mobileNo= " +mobileNo +" amount"+amount);

            userInfo = userInfoService.getUserByMobileNo(mobileNo);
            if (userInfo == null)
                throw new SystemException(UserErrorCode.USER_NOT_EXIST).set("mobileNo", mobileNo);

            if (!userInfo.getType().equals(UserType.COMMON.getValue()))
                throw new SystemException(UserErrorCode.USERTYPE_ERROR).set("userId", userInfo.getUserId()).set("mobileNo", mobileNo).set("userType", userInfo.getType());

            infoExt = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
            if (!userRealName.equals(infoExt.getRealName()))
                throw new SystemException(UserErrorCode.INFO_NOT_EQUAL).set("userId", userInfo.getUserId()).set("inputRealName", userRealName).set("actualRealName", infoExt.getRealName());

            Long systemAccId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
            UserAccount sysAcc = this.userAccountService.getUserAccountByAccId(systemAccId);
            AccountValueChanged outChanged = new AccountValueChanged(
                    systemAccId,
                    amount, amount, AccountConstants.AccountOperateEnum.PAY.getValue(),
                    AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.PLATFORM_USER.getValue(),
                    AccountConstants.VisiableEnum.DISPLAY.getValue(), sysAcc.getUserId(),
                    AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(), sysAcc.getAccId(), now,
                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.ACTIVITY_AWARDS_PAY, params.get("awardName"), userInfo.getUserId(), amount),
                    false
            );

            UserAccount userAccount = userAccountService.getCashAccount(userInfo.getUserId());
            AccountValueChanged inChanged = new AccountValueChanged(
                    userAccount.getAccId(), amount, amount, AccountConstants.AccountOperateEnum.INCOM.getValue(),
                    AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(),
                    sysAcc.getAccId(), AccountConstants.OwnerTypeEnum.USER.getValue(), userAccount.getUserId(), now,
                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.ACTIVITY_AWARDS_INCOME, params.get("awardName"), amount),
                    false
            );

            queue.addAccountValueChanged(outChanged);
            queue.addAccountValueChanged(inChanged);
        }

        this.userAccountOperateService.execute(queue);
    }

    @Override
    @Transactional
    public void reMakeWithdrawForApply(Collection<Long> ids) {
        LoanApplication loanApplication;
        for (Long id : ids) {
            try {
                loanApplicationService.reMakeLoan(id, 21l);
            } catch (Exception e) {
                throw SystemException.wrap(e, SystemErrorCode.UNKNOW_ERROR);
            }
        }
    }

    @Override
    @Transactional
    public void reSendAward(Date sDate, Date eDate) {
        //这个方法的处理逻辑是:先找到需要补交奖励的[债权回款明细]数据,然后依次补发奖励
        List<RightsRepaymentDetail> rs = this.getNeedReAwardRs(sDate, eDate);
        Map<Long, CreditorRights> crs = this.getCreditorRightsByRs(rs);
        Map<Long, LoanPublish> lps = this.getLoanPublishByCrs(crs.values());
        Map<Long, LoanApplication> las = this.getLoanApplicationByCrs(crs.values());
        if (rs != null && rs.size() > 0) {
            Date now = new Date();
            CreditorRights cr = null;
            LoanPublish loanPublish = null;
            LoanApplication loanApplication = null;
            BigDecimal hundred = new BigDecimal("100");
            AccountValueChangedQueue avcq = new AccountValueChangedQueue();
            long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
            for (RightsRepaymentDetail detail : rs) {
                cr = crs.get(detail.getCreditorRightsId());
                loanPublish = lps.get(cr.getLoanApplicationId());
                loanApplication = las.get(cr.getLoanApplicationId());
                BigDecimal monthAwardRate = loanPublish.getAwardRate().divide(hundred).divide(new BigDecimal("12"),18,
                        BigDecimal.ROUND_CEILING);
                BigDecimal awardBalance = loanApplication.getConfirmBalance().multiply(monthAwardRate);
                if(detail.getRightsDetailState() == RightsRepaymentDetail.RIGHTSDETAILSTATE_COMPLETE ){
                    if (awardDetailService.findByRightsRepaymentDetailId(detail.getRightsRepaymentDetailId()) == null) {
                        BigDecimal theAward2Lend = BigDecimalUtil.down(awardBalance.multiply(detail.getProportion().divide(hundred)),2);
                        LendOrder lendOrder = lendOrderService.findById(cr.getLendOrderId());
                        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(lendOrder.getLendUserId());

                        AwardDetail awardDetail = awardDetailService.insertAwardDetail(now, detail, theAward2Lend,
                                lendOrder, loanApplication.getLoanApplicationId(), AwardPointEnum.ATREPAYMENT,RateLendOrderTypeEnum.AWARD);

                        //平台支出奖励
                        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_PAY, userInfoVO.getLoginName(), loanPublish.getLoanTitle());
                        AccountValueChanged avcPay = new AccountValueChanged(systemAccountId, theAward2Lend,
                                theAward2Lend, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(),
                                systemAccountId, now, descPay, false);
                        avcq.addAccountValueChanged(avcPay);

                        UserAccount userAccountWh = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
                        UserAccount cashUserAccount = null;
                        //如果是理财账户，奖励发放至理财账户中；如果是非理财账户，奖励发放到资金账户
                        if(userAccountWh.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
                            cashUserAccount = userAccountWh;
                        }else{
                            //出借人资金账户收入奖励
                            cashUserAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
                        }
                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_INCOME, loanPublish.getLoanTitle(), AwardPointEnum.ATREPAYMENT.getDesc());
                        AccountValueChanged avcIncome = new AccountValueChanged(cashUserAccount.getAccId(), theAward2Lend,
                                theAward2Lend, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                                cashUserAccount.getAccId(), now, descIncome, true);
                        avcq.addAccountValueChanged(avcIncome);
                    }
                }
            }

            userAccountOperateService.execute(avcq);
        }
    }

    private Map<Long, LoanApplication> getLoanApplicationByCrs(Collection<CreditorRights> crs) {
        Set<Long> laIds = new HashSet<>();
        for (CreditorRights creditorRights : crs) {
            laIds.add(creditorRights.getLoanApplicationId());
        }
        Map params = new HashMap();
        params.put("laIds", laIds);
        List<LoanApplication> loanApplications = this.myBatisDao.getList("bugfix_getLoanApplicationByCrs", params);
        Map<Long, LoanApplication> result = new HashMap<>();
        for (LoanApplication loanApplication : loanApplications) {
            result.put(loanApplication.getLoanApplicationId(), loanApplication);
        }
        return result;
    }

    private Map<Long, LoanPublish> getLoanPublishByCrs(Collection<CreditorRights> crs) {
        Set<Long> laIds = new HashSet<>();
        for (CreditorRights creditorRights : crs) {
            laIds.add(creditorRights.getLoanApplicationId());
        }
        Map params = new HashMap();
        params.put("laIds", laIds);
        List<LoanPublish> loanPublishs = this.myBatisDao.getList("bugfix_getLoanPublishByCrs", params);
        Map<Long, LoanPublish> result = new HashMap<>();
        for (LoanPublish loanPublish : loanPublishs) {
            result.put(loanPublish.getLoanApplicationId(), loanPublish);
        }
        return result;
    }

    private Map<Long, CreditorRights> getCreditorRightsByRs(Collection<RightsRepaymentDetail> rs) {
        Set<Long> crIds = new HashSet<>();
        for (RightsRepaymentDetail detail : rs) {
            crIds.add(detail.getCreditorRightsId());
        }
        Map params = new HashMap();
        params.put("crIds", crIds);
        List<CreditorRights> creditorRightses = this.myBatisDao.getList("bugfix_getCreditorRightsByRs", params);
        Map<Long, CreditorRights> result = new HashMap<>();
        for (CreditorRights creditorRights : creditorRightses) {
            result.put(creditorRights.getCreditorRightsId(), creditorRights);
        }
        return result;
    }

    private List<RightsRepaymentDetail> getNeedReAwardRs(Date sDate, Date eDate) {
        Map params = new HashMap();
        params.put("sDate", sDate);
        params.put("eDate", eDate);
        return this.myBatisDao.getList("bugfix_getNeedReAwardRs", params);
    }

    private void offlineRecharge(Long userId, BigDecimal amount, Map<String, Object> params, Date now, AdminInfo currentUser, String externalNo, String channelCode) {
        ValidationUtil.checkRequiredEntry(params, "offlineOrderNo");

        //新建充值订单
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setRechargeCode(UUID
				.randomUUID().toString().replace("-", ""));
        rechargeOrder.setChannelCode(channelCode);
        rechargeOrder.setUserId(userId);
        rechargeOrder.setDetailId(null);
        rechargeOrder.setPayId(null);
        rechargeOrder.setAmount(amount);
        rechargeOrder.setStatus(RechargeStatus.SUCCESS.getValue());
        rechargeOrder.setBankCode("");
        rechargeOrder.setCardNo("");
        rechargeOrder.setCreateTime(now);
        rechargeOrder.setResultTime(now);
        rechargeOrder.setAdminId(currentUser.getAdminId());
        rechargeOrder.setExternalNo(externalNo);
        rechargeOrderService.createRechargeOrderByAdminOperation(rechargeOrder);


        UserAccount cashAccount = userAccountService.getCashAccount(userId);
        //新建现金流
        AccountValueChangedQueue queue = new AccountValueChangedQueue();
        AccountValueChanged inChanged = new AccountValueChanged(
                cashAccount.getAccId(), amount, amount, AccountConstants.AccountOperateEnum.INCOM.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_OFFLINE_RECHARGE.getValue(),
                AccountConstants.AccountChangedTypeEnum.RECHARGE.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(),
                rechargeOrder.getRechargeId(), AccountConstants.OwnerTypeEnum.USER.getValue(), userId, now,
                StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.OFFINE_RECHARGE_INCOME, amount, params.get("offlineOrderNo")), true
        );
        queue.addAccountValueChanged(inChanged);
        userAccountOperateService.execute(queue);
    }

    private void activityAwards(Long userId, BigDecimal amount, Map<String, Object> params, Date now) {
        ValidationUtil.checkRequiredEntry(params, "awardName");

        AccountValueChangedQueue queue = new AccountValueChangedQueue();

        Long systemAccId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
        UserAccount sysAcc = this.userAccountService.getUserAccountByAccId(systemAccId);
        AccountValueChanged outChanged = new AccountValueChanged(
                systemAccId,
                amount, amount, AccountConstants.AccountOperateEnum.PAY.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                AccountConstants.AccountChangedTypeEnum.PLATFORM_USER.getValue(),
                AccountConstants.VisiableEnum.DISPLAY.getValue(), sysAcc.getUserId(),
                AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(), sysAcc.getAccId(), now,
                StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.ACTIVITY_AWARDS_PAY, params.get("awardName"), userId, amount),
                false
                );

        UserAccount userAccount = userAccountService.getCashAccount(userId);
        AccountValueChanged inChanged = new AccountValueChanged(
                userAccount.getAccId(), amount, amount, AccountConstants.AccountOperateEnum.INCOM.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(),
                sysAcc.getAccId(), AccountConstants.OwnerTypeEnum.USER.getValue(), userAccount.getUserId(), now,
                StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.ACTIVITY_AWARDS_INCOME, params.get("awardName"), amount),
                false
        );

        queue.addAccountValueChanged(outChanged);
        queue.addAccountValueChanged(inChanged);

        this.userAccountOperateService.execute(queue);
    }
    
    
    public void repairRightsRepaymentDetailData(){
    	List<RepairRightsRepaymentDetailData> rList = rightsRepaymentDetailService.repairData();
    	List<Long> oldRightsIdList = new ArrayList<>();
    	Map<Long,List<RepairRightsRepaymentDetailData>> resultMap = new HashMap<Long,List<RepairRightsRepaymentDetailData>>();
    	for(RepairRightsRepaymentDetailData r : rList){
    		List<RepairRightsRepaymentDetailData> list = null ;
    		if(resultMap.get(r.getOldrightsid())== null ){
    			list = new ArrayList<>();
    			list.add(r);
    			resultMap.put(r.getOldrightsid(), list);
    			oldRightsIdList.add(r.getOldrightsid());
    		}else{
    			list = resultMap.get(r.getOldrightsid());
    			list.add(r);
    			resultMap.put(r.getOldrightsid(), list);
    		}
    	}
    	
    	for(Long r : oldRightsIdList){
    		
    		List<RepairRightsRepaymentDetailData> list = resultMap.get(r) ;
    		BigDecimal oldSumProportion = BigDecimal.ZERO;
    		for(int i = 0; i < list.size() ; i++)
    		{
    			RepairRightsRepaymentDetailData detail = list.get(i);
    			Map rightsDetailMap = new HashMap();
    			rightsDetailMap.put("creditorRightsId", detail.getNewrightsid());
    			BigDecimal newProportion = BigDecimal.ZERO;
    			
    			if(i == (list.size()-1)){
    				newProportion = detail.getOldproportion().subtract(oldSumProportion);
    			}else{
    				newProportion = BigDecimalUtil.down(detail.getNewproportion(), 2);
    				oldSumProportion = oldSumProportion.add(newProportion);
    			}
    			
    			rightsDetailMap.put("proportion", newProportion );
    			rightsRepaymentDetailService.updateMulty(rightsDetailMap);
    		}
    		
    	}
    }

	@Override
	@Transactional
	public void reissuedAwardByTime(String mobilesStr) {
		String mobiles[] = mobilesStr.split(",");
		List<String> mobileList = new ArrayList<>();
//		if(mobiles == null || StringUtils.isNull(mobilesStr)){
//			return ;
//		}
		for(String mobile : mobiles ){
			if(!StringUtils.isNull(mobile))
				mobileList.add(mobile);
		}
		if(mobileList.size() == 0){
			mobileList = null;
		}
		List<ReissuedAwardVO> awardList = awardDetailService
				.getReissuedAwardListByMobile(mobileList);
		Date now = new Date();
		Long systemOperationIdAccountId = constantDefineCached
				.getSystemAccount().get(
						AccountConstants.AccountTypeEnum.PLATFORM_OPERATING
								.getValue());
		BigDecimal awardShouldRepay = BigDecimal.ZERO ;
		for (ReissuedAwardVO awardVO : awardList) {
			awardShouldRepay = BigDecimalUtil.down(awardVO.getShouldrepay(),2);
			// 保存奖励
			AwardDetail awardDetail = awardDetailService.findByRightsRepaymentDetailIdLock(awardVO.getRIGHTS_REPAYMENT_DETAIL_ID(),true);
			awardDetail.setAwardBalance(awardVO.getShouldaward());
			awardDetail.setAwardTime(now);
			awardDetailService.update(awardDetail);

			UtilsServiceImpl bean = ApplicationContextUtil.getBean(UtilsServiceImpl.class);
			//执行流水
			bean.executeAwardAccountHis(awardVO.getLOGIN_NAME(),
					awardVO.getLOAN_PUBLISH_TITLE(),
					systemOperationIdAccountId, awardShouldRepay,
					awardDetail, now, awardVO.getUSER_ACCOUNT_ID(),
					awardVO.getUSER_ID());
		}
	}

	@Override
	@Transactional
	public void executeAwardAccountHis(String loginName, String loanPublishTitle ,Long systemOperationIdAccountId, BigDecimal theAward2Lend ,AwardDetail awardDetail ,
			Date now, Long customerAccoutId, Long userId){
		AccountValueChangedQueue avcq = new AccountValueChangedQueue();
		 //平台支出奖励
        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_PAY, loginName, loanPublishTitle);
        AccountValueChanged avcPay = new AccountValueChanged(systemOperationIdAccountId, theAward2Lend,
                theAward2Lend, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(),
                systemOperationIdAccountId, now, descPay, false);
        avcq.addAccountValueChanged(avcPay);

        UserAccount userAccountWh = userAccountService.getUserAccountByAccId(customerAccoutId);
        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_INCOME, loanPublishTitle, AwardPointEnum.ATREPAYMENT.getDesc());
        AccountValueChanged avcIncome = new AccountValueChanged(userAccountWh.getAccId(), theAward2Lend,
                theAward2Lend, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                userAccountWh.getAccId(), now, descIncome, true);
        avcq.addAccountValueChanged(avcIncome);
        userAccountOperateService.execute(avcq);
	}

	/**
	 * 佣金白名单
	 */
	@Override
	public void importToWhiteTabs(MultipartFile importFile) {
        String[] title = {"用户ID"};
        //解析excel
        try {
            	String type="1";
            	if(importFile.getName().indexOf(".xlsx")>=0){
            		type="2";
            	}
                File tempFile = FileUtil.createTempFile();
                FileUtil.appendBytes(tempFile, importFile.getBytes());

                List<List<Map<String, Object>>> lists = ExcelUtil.analysisExcel(tempFile,type);
                List<Map<String, Object>> result = lists.get(0);//sheet-0
                //获取UserID
                Set<Long> userIdSet = this.getSheetWhiteTabs(title, result);
                List<Long> list = new ArrayList<>();
                list.addAll(userIdSet);
                List<Long> inviteList = distributionInviteService.getUserIdByUserPids(list);
                userIdSet.addAll(inviteList);
                whiteTabsService.insertImpotAll(userIdSet);
                tempFile.delete();
                //取出userId
        } catch (Exception e) {
            e.printStackTrace();
        }
    
		
	}
	
	private Set<Long> getSheetWhiteTabs(String[] title, List<Map<String, Object>> result) {
        Set<Long> whiteTabsList = new HashSet<Long>();
        for (Map<String, Object> map : result) {
            String userId = (String) map.get(title[0]);
            whiteTabsList.add(Long.valueOf(userId));
        }
        return whiteTabsList;
    }
	private Set<Map<String,Object>>  getSheetWhiteTabsStr(String[] title, List<Map<String, Object>> result) {
		Set<Map<String,Object>>  set = new HashSet<>();
		Map<String,Object> userMap = null ;
		for (Map<String, Object> map : result) {
			userMap = new HashMap<>();
			String userId = (String) map.get(title[0]);
			String type = (String) map.get(title[1]);
			userMap.put("userId",userId);
			userMap.put("type", type);
			set.add(userMap);
		}
		return set;
	}

	@Override
	@Transactional
	public String capitalOperate(Long accountId, BigDecimal money,String type, String desc) throws Exception {
		String result="success";
		UserAccount ua=userAccountService.getUserAccountByAccId(accountId);
		if(ua==null){
			result="未查询到对应的账户";
			return result;
		}
		boolean typeFlag=false;
		String temp="";
		String businessType="";
		switch (type) {
		case "1":
			temp=DescTemplate.desc.AccountChanngedDesc.BACKSTAGE_FOR_IN;//流水类型：收入
			businessType=AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEINTO.getValue();//费用类型：资金转入
			typeFlag=true;
			break;
		case "2":
			temp=DescTemplate.desc.AccountChanngedDesc.BACKSTAGE_FOR_OUT;//支出
			businessType=AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEOUT.getValue();//资金转出
			typeFlag=true;
			break;
		case "4":
			temp=DescTemplate.desc.AccountChanngedDesc.FREEZE_FOR_BUS_HANDLING;//冻结
			businessType=AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCE_CHANGE_FREEZE.getValue();//资金冻结
			typeFlag=true;
			break;
		case "5":
			temp=DescTemplate.desc.AccountChanngedDesc.UNFREEZE_FOR_BUS_HANDLING_SUCCESS;//解冻
			businessType=AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCE_CHANGE_THAW.getValue();//资金解冻
			typeFlag=true;
			break;
		}
		if(!typeFlag){
			throw new Exception("type参数异常");
		}
		boolean sqlFlag=StringUtils.checkSQLStr(desc);
		if(sqlFlag){
			throw new Exception("非法参数");
		}
		AccountValueChangedQueue queue = new AccountValueChangedQueue();
        AccountValueChanged changed = new AccountValueChanged(
						        		accountId,money,money,type,
						        		businessType,
						        		AccountConstants.AccountChangedTypeEnum.CAPITAL_REPAIR.getValue(), 
						                AccountConstants.VisiableEnum.DISPLAY.getValue(),
						                0l, 
						                AccountConstants.OwnerTypeEnum.USER.getValue(), 
						                ua.getUserId(), 
						                new Date(),
						                StringUtils.t2s(
						                		temp,
						                		desc,
						                        money), 
						                false
					                );
        queue.addAccountValueChanged(changed);
        userAccountOperateService.execute(queue);
		return result;
	}

	@Override
	public void createCommiProfitTools(String lendOrderIds,Date ccpStartDate, Date ccpEndDate) throws Exception {
		String lendOrderIdArr[] = lendOrderIds.split(",");
		a: for (String id : lendOrderIdArr) {
			if (!StringUtils.isNull(id)) {
				Long lendOrderId = Long.valueOf(id);
				List<LendOrderBidDetail> bidDetail = lendOrderBidDetailService
						.findByLendOrderId(lendOrderId,
								LendOrderBidStatusEnum.BIDSUCCESS);
				if (bidDetail != null && bidDetail.size() > 0) {
					List<RepaymentPlan> repaymentList = repaymentPlanService
							.getRepaymentPlansByloanApplicationId(bidDetail
									.get(0).getLoanApplicationId());
					for (RepaymentPlan r : repaymentList) {
						if (r.getPlanState() == RepaymentPlanStateEnum.UNCOMPLETE
								.value2Char()
								|| (r.getPlanState() == RepaymentPlanStateEnum.DEFAULT
										.value2Char() && r.getFactBalance()
										.doubleValue() == 0d)) {
							
						}else{
							continue a;
						}
					}
				}
				if(ccpStartDate==null||ccpEndDate==null){
					commiProfitService.createCommiProfit(lendOrderId);
				}else{
					commiProfitService.createCommiProfit(lendOrderId, ccpStartDate,ccpEndDate);
				}
			}
		}
	}
	
	/**
	 * 销售白名单
	 */
	@Override
	public void importToInviteWhiteTabs(MultipartFile importFile) {
        String[] title = {"用户ID","用户类型"};
        //解析excel
        try {
            	String type="1";
            	if(importFile.getName().indexOf(".xlsx")>=0){
            		type="2";
            	}
                File tempFile = FileUtil.createTempFile();
                FileUtil.appendBytes(tempFile, importFile.getBytes());

                List<List<Map<String, Object>>> lists = ExcelUtil.analysisExcel(tempFile,type);
                List<Map<String, Object>> result = lists.get(0);//sheet-0
                //获取UserID
                Set<Map<String,Object>> userSet = this.getSheetWhiteTabsStr(title, result);
                inviteWhiteTabsService.insertImpotAll(userSet);
                tempFile.delete();
                //取出userId
        } catch (Exception e) {
            e.printStackTrace();
        }
    
		
	}

    @Override
    @Transactional
    public void handleUndonePayOrder() {
        payService.handleUndonePayOrderForTimer();
    }

    @Override
    @Transactional
    public void repairAcc() {
        List<Long> eIds = this.userAccountService.getErrorHisIds();
        Date now = new Date();

        AccountValueChangedQueue queue = new AccountValueChangedQueue();
        Long systemAccId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
        UserAccount sysAcc = this.userAccountService.getUserAccountByAccId(systemAccId);
        for (Long id : eIds) {
            UserAccountHis userAccountHisById = this.userAccountService.getUserAccountHisById(id);

            AccountValueChanged cancelOutChanged = new AccountValueChanged(
                    systemAccId, userAccountHisById.getChangeValue2(), userAccountHisById.getChangeValue2(), AccountConstants.AccountOperateEnum.INCOM.getValue(),
                    AccountConstants.BusinessTypeEnum.FEESTYPE_PLATFORM_INCOME_ACTIVITY_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.PLATFORM_USER.getValue(),
                    AccountConstants.VisiableEnum.DISPLAY.getValue(), sysAcc.getUserId(),
                    AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(), sysAcc.getAccId(), now,
                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.PLATFORM_INCOME_ACTIVITY_AWARD, userAccountHisById.getChangeValue2()),
                    false
            );

            UserAccount userAccount = userAccountService.getUserAccountByAccId(userAccountHisById.getAccId());
            AccountValueChanged cancelInChanged = new AccountValueChanged(
                    userAccount.getAccId(), userAccountHisById.getChangeValue2(), userAccountHisById.getChangeValue2(), AccountConstants.AccountOperateEnum.PAY.getValue(),
                    AccountConstants.BusinessTypeEnum.FEESTYPE_ERROR_PAY_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(), AccountConstants.VisiableEnum.HIDDEN.getValue(),
                    sysAcc.getAccId(), AccountConstants.OwnerTypeEnum.USER.getValue(), userAccount.getUserId(), now,
                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.ERROR_PAY_AWARD, userAccountHisById.getChangeValue2(), userAccountHisById.getHisId()),
                    true
            );

            queue.addAccountValueChanged(cancelOutChanged);
            queue.addAccountValueChanged(cancelInChanged);

        }

        this.userAccountOperateService.execute(queue);

    }

    /**
	 * 参与白名单和邀请白名单通过手机号录入工具
	 * flag:0 -- 只查询 ， 1 -- 查询并导入
	 * */
	@Override
	@Transactional
	public Map<String,Object> checkAndImportWhiteTabsByMobiles(String importWhiteTabsMobiles,
			char flag) {
		Map<String,Object> rsMap = new HashMap<>();
		rsMap.put("status", "fail");
		if(org.apache.commons.lang.StringUtils.isNotBlank(importWhiteTabsMobiles)){
			List<String> mobileList = new ArrayList<>();
			for(String mobile : importWhiteTabsMobiles.split(",")){
				mobileList.add(mobile);
			}
			if(flag == '0'){
				Map<Long,Object> userMap = null ;
				try {
					userMap = distributionInviteService.findUserAllInfoByMobiles(mobileList);
				} catch (Exception e) {
					throw new SystemException( e , SystemErrorCode.SYSTEM_ERROR_CODE);
				}
				rsMap.put("status", "success");
				rsMap.put("userInfo", userMap.values());
			}else if (flag == '1'){
				Map<Long,Object> userMap = null ;
				try {
					userMap = distributionInviteService.findUserAllInfoByMobiles(mobileList);
					//导入参与佣金白名单
					whiteTabsService.insertImpotAll(userMap.keySet());
					//导入邀请白名单
					inviteWhiteTabsService.insertImpotAllByUserId(userMap.keySet());
					
				} catch (Exception e) {
					throw new SystemException( e , SystemErrorCode.SYSTEM_ERROR_CODE);
				}
				rsMap.put("status", "success");
			}
		}else{
			throw new SystemException("手机号格式错误！",SystemErrorCode.SYSTEM_ERROR_CODE);
		}
		return rsMap ;
		
	}

	@Override
	public void exeSXJHCreateAgreement(Date startTime , Date endTime) {
		loanApplicationService.exeSXJHCreateAgreementAll(startTime , endTime);
	}
	
	/**
	 * 多级邀请关系表
	 */
	@Override
	public void importMultilevelInvitationExcel(MultipartFile importFile) {
        String[] title = {"用户ID"};
        //解析excel
        try {
            	String type="1";
            	if(importFile.getName().indexOf(".xlsx")>=0){
            		type="2";
            	}
                File tempFile = FileUtil.createTempFile();
                FileUtil.appendBytes(tempFile, importFile.getBytes());

                List<List<Map<String, Object>>> lists = ExcelUtil.analysisExcel(tempFile,type);
                List<Map<String, Object>> result = lists.get(0);//sheet-0
                
                //基础数据
                Set<Long> userIdSet = this.getSheetWhiteTabs(title, result);
                
                for (Long baseId : userIdSet) {
                	
                	addMul(baseId, baseId, baseId, 0l);
					
                	this.getUserByPids(baseId);
				}
                
                tempFile.delete();
                //取出userId
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void getUserByPids(Long recUserId){
		List<UserInfoExt> lists = getUserByPid(recUserId);
		if(null != lists && lists.size() > 0){
			for (UserInfoExt ext : lists) {
				
				MultilevelInvitation inv = userInfoService.getMultilevelInvitationByUserId(ext.getRecUserId());
				
				addMul(ext.getUserId(), ext.getRecUserId(), inv.getSaleByUserId(), inv.getHierarchy()+1);
				
				getUserByPids(ext.getUserId());
				
			}
			
		}
	}
	
	private List<UserInfoExt> getUserByPid(Long recUserId){
		return userInfoExtService.getUserInfoExtByRecUserId(recUserId);
	}
	
	private void addMul(Long userId, Long recommendUserId, Long saleByUserId, Long hierarchy){
		MultilevelInvitation inv = userInfoService.getMultilevelInvitationByUserId(userId);
		if(null == inv){
			MultilevelInvitation newInv = new MultilevelInvitation();
			newInv.setUserId(userId);//用户id
			newInv.setRecommendUserId(recommendUserId);//推荐用户id
			newInv.setSaleByUserId(saleByUserId);//所属销售的用户id
			newInv.setHierarchy(hierarchy);//层级(从1开始)
			newInv.setCreateTime(new Date());//创建时间
			
			myBatisDao.insert("MULTILEVEL_INVITATION.insert", newInv);
		}
	}

	@Override
	public void addPlatformBill(PlatformBill platformBill) {
		myBatisDao.insert("PLATFORM_BILL.insert", platformBill);
	}

	@Override
	public PlatformBill getPlatformBillByUserId(Long userId) {
		return myBatisDao.get("PLATFORM_BILL.selectByPrimaryKey", userId);
	}

	@Override
	public void updatePlatformBill(PlatformBill platformBill) {
		myBatisDao.update("PLATFORM_BILL.updateByPrimaryKeySelective", platformBill);
	}
	
}
