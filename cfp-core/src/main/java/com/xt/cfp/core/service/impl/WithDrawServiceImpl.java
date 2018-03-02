package com.xt.cfp.core.service.impl;

import com.external.llpay.LLPayUtil;
import com.external.yeepay.TZTService;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.Exception.code.ext.WithDrawErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.context.ParaContext;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.CustomerCardVO;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.pojo.ext.crm.CRMWithdrawVO;
import com.xt.cfp.core.pojo.ext.phonesell.WithdrawVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.message.WechatMessageBody;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.*;
import jodd.io.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by luqinglin on 2015/6/25.
 */
@Property
@Service
@Transactional
public class WithDrawServiceImpl extends CommissionFee implements WithDrawService {

    private static Logger logger = Logger.getLogger(WithDrawServiceImpl.class);

    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private RedisCacheManger redisCacheManger;
    @Autowired
    private UserOpenIdService userOpenIdService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private CityInfoService cityInfoService;
    @Autowired
    private OrderResourceService orderResourceService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CapitalFlowService capitalFlowService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Value(value = "${register_smscode_time}")
    public Integer smsCodeTime;
    @Value(value = "${withDrawTimes}")
    public Integer withDrawTimes;
    @Value(value = "${withDrawTransferFlag}")
    public Integer withDrawTransferFlag;

    @Value(value = "${withDrawRefreshFlag}")
    public Integer withDrawRefreshFlag;

    private static BigDecimal fee;

    public Integer getWithDrawRefreshFlag() {
        return withDrawRefreshFlag;
    }

    public void setWithDrawRefreshFlag(Integer withDrawRefreshFlag) {
        this.withDrawRefreshFlag = withDrawRefreshFlag;
    }

    public Integer getWithDrawTransferFlag() {
        return withDrawTransferFlag;
    }

    public void setWithDrawTransferFlag(Integer withDrawTransferFlag) {
        this.withDrawTransferFlag = withDrawTransferFlag;
    }

    public Integer getWithDrawTimes() {
        return withDrawTimes;
    }

    public void setWithDrawTimes(Integer withDrawTimes) {
        this.withDrawTimes = withDrawTimes;
    }

    public Integer getSmsCodeTime() {
        return smsCodeTime;
    }

    public void setSmsCodeTime(Integer smsCodeTime) {
        this.smsCodeTime = smsCodeTime;
    }

    @Override
    @Transactional
    public WithDraw withDrawByThirdPart(WithDrawExt withDrawExt, AccountConstants.AccountChangedTypeEnum accountChangedType) {
        // 记录客户卡信息
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(withDrawExt.getUserId(), PayConstants.PayChannel.LL);
        //等于0表示用历史卡
        if (!(StringUtils.isNotEmpty(withDrawExt.getIsCardUsed()) && withDrawExt.getIsCardUsed().equals("0"))) {
            // 历史卡禁用，添加新卡
            CustomerCard cc = new CustomerCard();
            if (customerCard != null) {
                customerCardService.removeCustomerCard(customerCard.getCustomerCardId());
            }
            cc.setCardType(CardType.DRAW_CARD.getValue());
            cc.setBankCode(Long.valueOf(withDrawExt.getBankCode()));
            cc.setCardCode(withDrawExt.getCardNo());
            cc.setCardcustomerName(withDrawExt.getCompanyName());
            cc.setUserId(withDrawExt.getUserId());
            cc.setRegisteredBank(withDrawExt.getRegisteredBankDetail());
            cc.setStatus(CustomerCardStatus.NORMAL.getValue());
            customerCard = customerCardService.addCustomerCard(cc);
        }
        //生成提现单,冻结账户
        withDrawExt.setCustomerCardId(customerCard.getCustomerCardId());
        WithDraw withDraw = withDraw(false,withDrawExt, accountChangedType, null);
        return withDraw;
    }

    @Override
    @Transactional
    public WithDraw withDraw(boolean useVoucher,WithDraw withDraw, AccountConstants.AccountChangedTypeEnum accountChangedType, ClientEnum client) {
        //判断参数是否为null
        if (withDraw == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("withDraw", "null");
        if (accountChangedType == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accountChangedType", "null");


        UserAccount cashAccount = userAccountService.getCashAccount(withDraw.getUserId());
        if (withDraw.getWithdrawAmount().compareTo(new BigDecimal("100")) < 0) {
            throw new SystemException(WithDrawErrorCode.MORE_THAN_HUNDRED).set("withdrawAmount", "small then 100");
        }
        BigDecimal withDrawAmount = getWithDrawAmountByUserId(withDraw.getUserId());
        if (new BigDecimal("500000").compareTo(withDrawAmount.add(withDraw.getWithdrawAmount()))<0){
            throw new SystemException(WithDrawErrorCode.WITHDRAW_AMOUNT_OVERLIMIT).set("withdrawAmount", "bigger then 500000");
        }
        if (withDraw.getWithdrawAmount().compareTo(new BigDecimal("500000")) > 0) {
            throw new SystemException(WithDrawErrorCode.WITHDRAW_AMOUNT_OVERLIMIT).set("withdrawAmount", "bigger then 500000");
        }
        if (cashAccount.getAvailValue2().compareTo(withDraw.getWithdrawAmount()) < 0) {
            throw new SystemException(WithDrawErrorCode.WITHDRAW_AMOUNT_NOT_ENOUGH).set("cashAccount", "cashAccount not enough");
        }

        withDraw.setCreateTime(new Date());
        withDraw.setVerifyStatus(VerifyStatus.APPROVAL.getValue());
        withDraw.setTransStatus(WithDrawTransferStatus.UN_TRANSFER.getValue());
        //添加提现券逻辑
        if (useVoucher){
            Voucher voucherWithDraw = getVoucherWithDraw(withDraw.getUserId());
            if (voucherWithDraw==null){
                throw new SystemException(WithDrawErrorCode.WITHDRAW_VOUCHER_NULL).set("voucherWithDrawCount",0);
            }
            withDraw.setCommissionFee(BigDecimal.ZERO);
            myBatisDao.insert("WITHDRAW.insertSelective", withDraw);
            //冻结提现券
            voucherService.frozeVoucherWithDraw(voucherWithDraw.getVoucherId(),withDraw);
        }else{
            withDraw.setCommissionFee(InterestCalculation.getWithDrawFee(withDraw.getWithdrawAmount()));
            myBatisDao.insert("WITHDRAW.insertSelective", withDraw);
        }
        String source = PropertiesUtils.getInstance().get("SOURCE_ORTHER");
        if(client != null){
        	source = PropertiesUtils.getInstance().get(client.getResource());
        }
      //记录订单来源
        OrderResource or=orderResourceService.selectBYDesc(source);
        orderResourceService.addResourceFrom(withDraw.getWithdrawId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_WITHDRAW.getValue()), or.getResourceId(),withDraw.getCreateTime());

        //获取用户的出借账户
        UserAccount userAccount = userAccountService.getCashAccount(withDraw.getUserId());

        //向渠道的平台账户执行冻结操作
        AccountValueChangedQueue accountValueChangedQueue = new AccountValueChangedQueue();
        AccountValueChanged accountValueChanged = new AccountValueChanged(userAccount.getAccId(), withDraw.getWithdrawAmount(), withDraw.getWithdrawAmount(), AccountConstants.AccountOperateEnum.FREEZE.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASHFREEZEN.getValue(), AccountConstants.AccountChangedTypeEnum.WITHDRAW.getValue(), "1", withDraw.getWithdrawId(), accountChangedType.getValue(), withDraw.getUserId(), new Date(),
                com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.WITHDRAW_FREEZE, withDraw.getWithdrawAmount()), true);
        accountValueChangedQueue.addAccountValueChanged(accountValueChanged);
        userAccountOperateService.execute(accountValueChangedQueue);
        return withDraw;
    }

    @Override
    @Transactional
    public WithDraw withDraw(ParaContext paraContext) throws Exception {
        WithDraw withDraw = paraContext.get("withDraw");
        AccountValueChangedQueue avcq = paraContext.get("accountValueChangedQueue");
        //判断参数是否为null
        if (withDraw == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("withDraw", "null");
        withDraw.setCreateTime(new Date());
        if (com.xt.cfp.core.util.StringUtils.isNull(withDraw.getVerifyStatus())) {
            withDraw.setVerifyStatus(VerifyStatus.APPROVAL.getValue());
        }
        withDraw.setTransStatus(WithDrawTransferStatus.UN_TRANSFER.getValue());
        withDraw.setCommissionFee(InterestCalculation.getWithDrawFee(withDraw.getWithdrawAmount()));
        myBatisDao.insert("WITHDRAW.insertSelective", withDraw);

        AccountValueChanged avcSystemIncomeFees = new AccountValueChanged((Long) paraContext.get("accountId"), (BigDecimal) paraContext.get("balance"),
                (BigDecimal) paraContext.get("balance"), AccountConstants.AccountOperateEnum.FREEZE.getValue(), (String) paraContext.get("businessType"),
                "WithDraw", (String) paraContext.get("display"), withDraw.getWithdrawId(),
                (String) paraContext.get("ownerType"), (Long) paraContext.get("ownerId"), new Date(), (String) paraContext.get("desc"), (Boolean) paraContext.get("needCheck"));
        avcq.addAccountValueChanged(avcSystemIncomeFees);
        return withDraw;
    }

    @Override
    public Pagination<WithDrawExt> getWithDrawPaging(int pageNo, int pageSize, WithDraw withDraw, Map<String, Object> customParams) {
        Pagination<WithDrawExt> re = new Pagination<WithDrawExt>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("withDraw", withDraw);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getWithDrawPaging", params);
        List<WithDrawExt> uah = this.myBatisDao.getListForPaging("getWithDrawPaging", params, pageNo, pageSize);

        //如来源为系统自动，则显示相关借款申请的标的标题（Loan_publish表中的loan_title）
        for (WithDrawExt wd : uah) {
            if (wd != null)
                if (WithDrawSource.SYSTEM_WITHDRAW.getValue().equals(wd.getHappenType())) {
                    LoanWithdrawRelations lwr = myBatisDao.get("LOAN_WITHDRAW_RELATIONS.selectByWithDrawId", wd.getWithdrawId());
                    if (lwr != null) {
                        LoanPublish lp = loanPublishService.findById(lwr.getLoanApplicationId());
                        wd.setHappenType(lp.getLoanTitle());
                    }
                }
        }

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    /**
     * 获取所有提现记录数据(导出excel报表专用)
     */
    @Override
    public List<LinkedHashMap<String, Object>> getWithDrawAllList(WithDraw withDraw, Map<String, Object> customParams) {
    	List<LinkedHashMap<String, Object>> linkedHashMapList = new LinkedList<LinkedHashMap<String, Object>>();

    	try {
    		Map<String, Object> params = new HashMap<String, Object>();
            params.put("withDraw", withDraw);
            params.put("customParams", customParams);
            List<WithDrawExt> uah = this.myBatisDao.getList("getWithDrawPaging", params);

            for (WithDrawExt wd : uah) {
                if (wd != null){
                	//如来源为系统自动，则显示相关借款申请的标的标题（Loan_publish表中的loan_title）
                    if (WithDrawSource.SYSTEM_WITHDRAW.getValue().equals(wd.getHappenType())) {
                        LoanWithdrawRelations lwr = myBatisDao.get("LOAN_WITHDRAW_RELATIONS.selectByWithDrawId", wd.getWithdrawId());
                        if (lwr != null) {
                            LoanPublish lp = loanPublishService.findById(lwr.getLoanApplicationId());
                            wd.setHappenType(lp.getLoanTitle());
                        }
                    }

                    // 存数数据
                	LinkedHashMap<String, Object> map = new LinkedHashMap<>();

                	map.put("提现单号", wd.getWithdrawId());
                	map.put("交易流水号", wd.getWithDrawFlowId());
                	map.put("操作人用户名", wd.getOperateName());
                	map.put("平台用户名", wd.getLoginName());
                	map.put("提现金额", wd.getWithdrawAmount());

                	map.put("提现手续费", wd.getCommissionFee());
                	map.put("开户行", wd.getBankName());
                	map.put("银行卡号", wd.getCardNo());
                	map.put("提现用户名", wd.getRealName());
                	map.put("开户名", wd.getUserName());

                	map.put("来源", wd.getHappenType());

                	String belongChannel = wd.getBelongChannel();
                	String belongChannelDisplayStr = "";
                	if("0".equals(belongChannel)){
                		belongChannelDisplayStr = "易宝";
                	}else if ("1".equals(belongChannel)) {
                		belongChannelDisplayStr = "连连";
    				}
                	map.put("支付渠道", belongChannelDisplayStr);

                	String verifyStatus = wd.getVerifyStatus();
                	String verifyStatusDisplayStr = "";
                	if("0".equals(verifyStatus)){
                		verifyStatusDisplayStr = "待审核";
                	}else if("1".equals(verifyStatus)){
                    	verifyStatusDisplayStr = "审核通过";
                    }else if("2".equals(verifyStatus)){
                    	verifyStatusDisplayStr = "驳回";
                    }
                	map.put("审核状态", verifyStatusDisplayStr);

                	String transStatus = wd.getTransStatus();
                	String transStatusDisplayStr = "";
                	if("0".equals(transStatus)){
                		transStatusDisplayStr = "未打款";
                	}else if("1".equals(transStatus)){
                    	transStatusDisplayStr = "未提交";
                    }else if("2".equals(transStatus)){
                    	transStatusDisplayStr = "处理中";
                    }else if("3".equals(transStatus)){
                    	transStatusDisplayStr = "打款成功";
                    }else if("4".equals(transStatus)){
                    	transStatusDisplayStr = "打款失败";
                    }else if("5".equals(transStatus)){
                    	transStatusDisplayStr = "提交失败";
                    }
                	map.put("打款状态", transStatusDisplayStr);

                	map.put("提交时间", DateUtil.getPlusTime(wd.getCreateTime()));
                	map.put("审核时间", DateUtil.getPlusTime(wd.getOperateTime()));
                	map.put("完成时间", DateUtil.getPlusTime(wd.getResultTime()));
                	map.put("确认打款时间", DateUtil.getPlusTime(wd.getConfirmWithdrawTime()));
                	map.put("结果描述", wd.getResultDesc());

                    linkedHashMapList.add(map);
                }
            }

		} catch (Exception e) {
			e.printStackTrace();
		}
    	return linkedHashMapList;
    }

    @Override
	public Pagination<WithdrawVO> phonesellWithDrawPaging(int pageNo,int pageSize,Map<String, Object> customParams) {
    	Pagination<WithdrawVO> re = new Pagination<WithdrawVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        if(customParams.size()>0){
        	Set<String> keys=customParams.keySet();
        	for (String key : keys) {
        		params.put(key, customParams.get(key));
			}
        }

        int totalCount = this.myBatisDao.count("PhoneSellWithDrawPaging", params);
        List<WithdrawVO> uah = this.myBatisDao.getListForPaging("PhoneSellWithDrawPaging", params, pageNo, pageSize);
        List<String> nums = getAllWithDrawAccounts(customParams);
        for (WithdrawVO p : uah) {
			p.setNums(nums);
		}
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}

    @Override
	public List<String> getAllWithDrawAccounts(Map<String, Object> customParams) {
    	Map<String, Object> params = new HashMap<String, Object>();
        if(customParams.size()>0){
        	Set<String> keys=customParams.keySet();
        	for (String key : keys) {
        		params.put(key, customParams.get(key));
			}
        }
        List<String> nums = this.myBatisDao.getList("getAllWithDrawAccounts", params);
		return nums;
	}

    @Override
    @Transactional
    public void verify(WithDraw withDraw) {
        //判断参数是否为null
        if (withDraw == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("withDraw", "null");

        if (withDraw.getWithdrawId() == null)
            throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("withDrawId", "null");

        //锁定
        WithDraw with_draw = this.getWithDrawByWithDrawId(withDraw.getWithdrawId(),true);
        if (!VerifyStatus.APPROVAL.getValue().equals(with_draw.getVerifyStatus())){
            throw new SystemException(WithDrawErrorCode.WITHDRAW_VERIFIED).set("verifyStatus", with_draw.getVerifyStatus());
        }

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
        if (sessionAdminInfo == null) {
            throw new SystemException(UserErrorCode.LONGIN_EXIST);
        }
        withDraw.setOperateTime(new Date());
        withDraw.setVerifierId(sessionAdminInfo.getAdminId());

        myBatisDao.update("updateWithDraw", withDraw);
        //如果是驳回需要提现金额解冻
        if (withDraw.getVerifyStatus().equals(VerifyStatus.UN_PAST.getValue())) {
            //后续资金处理
            withDrawFailure(withDraw.getWithdrawId());
        } else {
            WithDraw detail = this.getWithDrawByWithDrawId(withDraw.getWithdrawId());
            //发送提现成功短信
            try {
                UserInfo user = userInfoService.getUserByUserId(detail.getUserId());
                VelocityContext context = new VelocityContext();
                context.put("amount", detail.getWithdrawAmount().subtract(detail.getCommissionFee() == null ? BigDecimal.ZERO : detail.getCommissionFee()));
                String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_WITHDRAW_SUCCESS_VM, context);
                smsService.sendMsg(user.getMobileNo(), content);
              //发送微信消息
                try {
                    UserOpenId userOpenId = userOpenIdService.getOpenIdByCondition(user.getUserId(), null, null);
                    if(userOpenId != null && !"".equals(userOpenId.getOpenId())){
                    	 List<ConstantDefine> tokenSurvivalTimeList = constantDefineService.getConstantDefinesByType("tokenSurvivalTime");
                         List<ConstantDefine> accessTokenList = constantDefineService.getConstantDefinesByType("accessToken");
                         CustomerCardVO customerCardVO = customerCardService.getCustomerCardVOById(detail.getCustomerCardId());
                         Date date = new Date();
                         if("0".equals(tokenSurvivalTimeList.get(0).getConstantValue().toString()) || (date.getTime()/1000) > Long.valueOf(tokenSurvivalTimeList.get(0).getConstantValue().toString())){
                         	//to do重新获取
     			        	String access_token = Sign.getAccessToken();
     			        	Date resultTime = new Date();
     			        	tokenSurvivalTimeList.get(0).setConstantValue(String.valueOf(resultTime.getTime()/1000+3600));
     			        	accessTokenList.get(0).setConstantValue(access_token);
     			        	constantDefineService.updateConstantDefine(tokenSurvivalTimeList.get(0));
     			        	constantDefineService.updateConstantDefine(accessTokenList.get(0));
                         }
                         WechatMessageBody wechatMessageBody =  new WechatMessageBody();
                     	 wechatMessageBody.setBankNo(customerCardVO.getEncryptCardNo());
                     	 wechatMessageBody.setOpenId(userOpenId.getOpenId());
                     	 wechatMessageBody.setFlag("1");
                     	 wechatMessageBody.setBalance(detail.getWithdrawAmount().subtract(detail.getCommissionFee() == null ? BigDecimal.ZERO : detail.getCommissionFee()));
                     	 wechatMessageBody.setDate(DateUtil.getDateShortLongTimeCn(new Date()));
                     	 wechatMessageBody.setAccess_token(accessTokenList.get(0).getConstantValue());
                     	 Sign.sendWechatMsg(wechatMessageBody);
                    }
				} catch (Exception e) {
					e.printStackTrace();
	                logger.error("提现发送微信消息失败.错误信息:" + e.getMessage());
				}
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("提现发送短信失败.错误信息:" + e.getMessage());
            }
        }
    }

    @Transactional
    public WithDraw getWithDrawByWithDrawId(Long withdrawId, boolean lock) {
        //
        if (lock)
            return myBatisDao.get("WITHDRAW.selectByPrimaryKeyLocked", withdrawId);
        return getWithDrawByWithDrawId(withdrawId);
    }

    /**
     * 获得提现结果后操作
     *
     * @param withDraws
     */
    @Transactional
    private void withDraw(List<WithDraw> withDraws) {
        AccountValueChangedQueue accountValueChangedQueue = new AccountValueChangedQueue();
        long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
        Long userInId = getSystemUserInfo();
        List<WithDraw> failureList = new ArrayList<WithDraw>();


        for (WithDraw withDraw : withDraws) {
            Schedule schedule = insertMainHistoryForDataBase(withDraw.getWithdrawId());
            //锁住提现单，如果提现单已经是终态，则跳出不处理
        	logger.info("【获得提现结果后操作】锁住提现单，如果提现单已经是终态，则跳出不处理");
            WithDraw with = myBatisDao.get("WITHDRAW.lockByPrimaryKey", withDraw.getWithdrawId());
            if (with.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_FAILE.getValue()) || with.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_SUCCESS.getValue())) {
                continue;
            }
            //打款成功和打款失败,记录完成时间
            logger.info("【获得提现结果后操作】打款成功和打款失败,记录完成时间");
            if (withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_FAILE.getValue()) || withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_SUCCESS.getValue())) {
                withDraw.setResultTime(new Date());
            }
            //更新状态
            logger.info("【获得提现结果后操作】更新状态");
            myBatisDao.update("updateWithDraw", withDraw);
            //不是打款成功和打款失败，跳出，不执行资金操作
            logger.info("【获得提现结果后操作】不是打款成功和打款失败，跳出，不执行资金操作");
            if (!withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_FAILE.getValue()) && !withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_SUCCESS.getValue()))
                continue;
            //------------------------------------------------------提现资金流操作-----------------------------------------------------------------------//
            WithDrawExt detail = detail(withDraw.getWithdrawId());
            UserInfo user = userInfoService.getUserByUserId(detail.getUserId());
            //进行解冻操作
            logger.info("【获得提现结果后操作】进行解冻操作");
            String dec = "";
            if (withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_SUCCESS.getValue())) {
                dec = com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.WITHDRAW_UNFREEZE_SUCCESS, with.getWithdrawAmount());
            } else {
                dec = com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.WITHDRAW_UNFREEZE_FAILE_, with.getWithdrawAmount());
            }
            logger.info("【获得提现结果后操作】dec:" + dec);
            UserAccount crashAccount = userAccountService.getCashAccount(detail.getUserId());
            AccountValueChanged accountValueChanged_1 = new AccountValueChanged(crashAccount.getAccId(), detail.getWithdrawAmount(), detail.getWithdrawAmount(), AccountConstants.AccountOperateEnum.UNFREEZE.getValue(),
                    AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASHUNFREEZEN.getValue(), AccountConstants.AccountChangedTypeEnum.WITHDRAW.getValue(), "1", detail.getWithdrawId(),
                    UserType.getUserTypeByValue(user.getType()).getAccountChangedType().getValue(), detail.getUserId(), new Date(), dec, true);
            accountValueChangedQueue.addAccountValueChanged(accountValueChanged_1);

            //付款成功
            if (withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_SUCCESS.getValue())) {
            	logger.info("【获得提现结果后操作】付款成功");
                BigDecimal amount = detail.getWithdrawAmount();
                BigDecimal commFee = detail.getCommissionFee();
                if (commFee.compareTo(BigDecimal.ZERO) > 0) {//平台收取手续费
                	logger.info("【获得提现结果后操作】平台收取手续费：" + commFee);
//                    long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
                    AccountValueChanged accountValueChanged_2 = new AccountValueChanged(systemAccountId, commFee, commFee, AccountConstants.AccountOperateEnum.INCOM.getValue(),
                            AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASH.getValue(), AccountConstants.AccountChangedTypeEnum.WITHDRAW.getValue(), "1", detail.getWithdrawId(), AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                            systemAccountId, new Date(),
                            com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.WITHDRAW_PLATFORM_FEE,commFee), true);
                    //支付平台手续费
                    AccountValueChanged accountValueChanged_3 = new AccountValueChanged(crashAccount.getAccId(), commFee, commFee, AccountConstants.AccountOperateEnum.PAY.getValue(),
                            AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASH.getValue(), AccountConstants.AccountChangedTypeEnum.WITHDRAW.getValue(), "1", detail.getWithdrawId(),
                            UserType.getUserTypeByValue(user.getType()).getAccountChangedType().getValue(), detail.getUserId(), new Date(),
                            com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.WITHDRAW_FEE, commFee), true);
                    accountValueChangedQueue.addAccountValueChanged(accountValueChanged_2);
                    accountValueChangedQueue.addAccountValueChanged(accountValueChanged_3);
                }

                //直接将用户账户支出
                logger.info("【获得提现结果后操作】直接将用户账户支出");
                AccountValueChanged accountValueChanged_4 = new AccountValueChanged(crashAccount.getAccId(), amount.subtract(commFee), amount.subtract(commFee), AccountConstants.AccountOperateEnum.PAY.getValue(),
                        AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASH.getValue(), AccountConstants.AccountChangedTypeEnum.WITHDRAW.getValue(), "1", detail.getWithdrawId(),
                        UserType.getUserTypeByValue(user.getType()).getAccountChangedType().getValue(), detail.getUserId(), new Date(),
                        com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.WITHDRAW_SUCCESS, amount.subtract(commFee)), true);
                accountValueChangedQueue.addAccountValueChanged(accountValueChanged_4);


                //修改来源为系统自动的提现单对应的借款申请状态为打款成功
                logger.info("【获得提现结果后操作】修改来源为系统自动的提现单对应的借款申请状态为打款成功");
                if (detail.getHappenType().equals(WithDrawSource.SYSTEM_WITHDRAW.getValue())) {
                    LoanWithdrawRelations lwr = myBatisDao.get("LOAN_WITHDRAW_RELATIONS.selectByWithDrawId", withDraw.getWithdrawId());
                    Map<String, Object> loanAppMap = new HashMap<String, Object>();
                    loanAppMap.put("lendState", LoanAppLendAuditStatusEnums.PASS.getValue());
                    loanAppMap.put("loanApplicationId", lwr.getLoanApplicationId());
                    loanApplicationService.update(loanAppMap);
                }

                //如果有提现券标记提现券已使用
                logger.info("【获得提现结果后操作】如果有提现券标记提现券已使用");
                List<Voucher> voucherList = voucherService.getVoucherList(withDraw.getWithdrawId(),VoucherConstants.UsageScenario.WITHDRAW);
                if (voucherList!=null&&voucherList.size()>0){
                    voucherService.useVoucher(voucherList);
                    insertSubTaskByLoan(withDraw.getUserId(),withDraw.getUserId(),with.getWithdrawAmount().subtract(detail.getCommissionFee() ),schedule.getScheduleId(),HFOperationEnum.WITHDRAW.getValue(),Long.valueOf(HFOperationEnum.WITHDRAW.getValue()));
                }else{
                    insertSubTaskByLoan(withDraw.getUserId(),withDraw.getUserId(),with.getWithdrawAmount().subtract(detail.getCommissionFee() ),schedule.getScheduleId(),HFOperationEnum.WITHDRAW.getValue(),Long.valueOf(HFOperationEnum.WITHDRAW.getValue()));
                    insertSubTaskByLoan(withDraw.getUserId(),userInId,detail.getCommissionFee() ,schedule.getScheduleId(),HFOperationEnum.WITHDRAW.getValue(),Long.valueOf(HFOperationEnum.WITHDRAW.getValue()));

                }
            }else{
                //提现失败
            	logger.info("【获得提现结果后操作】提现失败");
                failureList.add(detail);
            }
                  }
        //执行账户操作
        logger.info("【获得提现结果后操作】执行账户操作");
        userAccountOperateService.execute(accountValueChangedQueue);
        //发送提现失败验证码
        logger.info("【获得提现结果后操作】发送提现失败验证码");
        sendWithDrawFailureMsg(failureList);

    }

    /**
     * 提现失败短信发送
     * @param failureList
     */
    private void sendWithDrawFailureMsg(List<WithDraw> failureList) {
        if (failureList == null)
            return ;
        for (WithDraw withDraw:failureList){
            sendWithDrawFailureMsg(withDraw);
        }

    }

    private void sendWithDrawFailureMsg(WithDraw withDraw) {
        if (withDraw==null)
            return ;
        UserInfo userInfo = userInfoService.getUserByUserId(withDraw.getUserId());
        if (userInfo.getType().equals(UserType.COMMON.getValue())){
            VelocityContext context = new VelocityContext();
            context.put("amount", withDraw.getWithdrawAmount());
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_WITHDRAW_FAILURE_VM, context);

            smsService.sendMsg(userInfo.getMobileNo(), content);
        }

        //如果有提现券解冻
        List<Voucher> voucherList = voucherService.getVoucherList(withDraw.getWithdrawId(),VoucherConstants.UsageScenario.WITHDRAW);
        if (voucherList!=null&&voucherList.size()>0){
            voucherService.backVoucher(withDraw.getWithdrawId(),"因提现失败而返还",voucherList.get(0).getVoucherId());
        }
    }

    @Override
    public WithDrawExt detail(Long withdrawId) {
        //判断参数是否为null
        if (withdrawId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("withdrawId", "null");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("withdrawId", withdrawId);

        WithDrawExt withDraw = myBatisDao.get("getWithDrawDetail", params);
        withDraw.setAmountWithoutFee(withDraw.getWithdrawAmount().subtract(withDraw.getCommissionFee()));
        return withDraw;
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        String[] title = {"提现单号", "提现人姓名", "开户行", "卡号", "打款金额", "打款结果", "失败原因"};
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        List<WithDrawExt> withDrawExtList = this.getTransferWithDrawVerified();
        for (WithDrawExt wde : withDrawExtList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(title[0], wde.getWithdrawId() + "");
            map.put(title[1], wde.getRealName());
            map.put(title[2], wde.getBankName());
            map.put(title[3], wde.getCardNo());
            map.put(title[4], wde.getWithdrawAmount().subtract(wde.getCommissionFee()) + "");
            map.put(title[5], WithDrawTransferStatus.UN_TRANSFER.getDesc());
            dataMap.add(map);
        }
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "待打款提现单";
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");// 设定输出文件头     待打款提现单
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(title, dataMap, "待打款提现单");
            wb.write(os);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void importExcel(MultipartFile importFile) {
        String[] title = {"提现单号", "提现人姓名", "开户行", "卡号", "打款金额", "打款结果", "失败原因"};
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

            List<WithDraw> sheetWithDraw = this.getSheetWithDraw(title, result);
            tempFile.delete();
            withDraw(sheetWithDraw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<WithDraw> getWithdrawListByCardId(Long customerCardId) {
        return this.myBatisDao.getList("WITHDRAW.getWithdrawListByCardId", customerCardId);
    }

    @Override
    public void sendWithDrawApplyMsg(String mobileNo) {
        VelocityContext context = new VelocityContext();
        String verifyCode = com.xt.cfp.core.util.StringUtils.generateVerifyCode();
        //将验证码存入redis方便后台查询
        ProcessValidCodeUtil.saveValidCode(ValidCodeEnum.WITHDRAW_MSG, redisCacheManger, verifyCode, mobileNo);
        context.put("code", verifyCode);
        context.put("date", DateUtil.getDateLongMD(new Date()));
        String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_WITHDRAW_APPLY_VM, context);
        //把验证码放到redis里,过期时间为1分钟
        System.out.print(smsCodeTime);
        boolean result = redisCacheManger.setRedisCacheInfo(TemplateType.SMS_WITHDRAW_APPLY_VM.getPrekey() + mobileNo, verifyCode, smsCodeTime);
        if (result)
            smsService.sendMsgByWXTL(mobileNo, content);
    }

    @Override
    @Transactional
    public String doWithDrawNew(String withDrawId) {
        Map<String, String> withDrawParams = new HashMap<String, String>();
        Map<String, String> resultMap = new HashMap<String, String>();
        //组织提现请求参数

        WithDrawExt detail = this.detail(Long.valueOf(withDrawId));
        CustomerCard customerCard = customerCardService.findById(detail.getCustomerCardId());
        UserInfoVO cardUserExt = this.userInfoService.getUserExtByUserId(customerCard.getUserId());



        //提现成功后的操作
        String status = resultMap.get("status");
        String customError = resultMap.get("customError");

        List<WithDraw> withDraws = new ArrayList<WithDraw>();
        WithDraw withDraw = new WithDraw();
        withDraw.setConfirmWithdrawTime(new Date());//确认打款时间
        String resultMsg = "";
        switch (withDrawTransferFlag) {
            case 0://正式环境
                if ("SUCCESS".equals(status)) {
                    //提交成功
                    submitSuccess(withDrawId, resultMap, withDraw);
                    resultMsg = "success";
                } else {
                    //提交不成功，统一归为处理中(添加：确认时间，处理中状态，结果描述)
                	submitDoing(withDrawId, resultMap, withDraw);
                    return "提现请求同步处理失败：" + withDraw.getResultDesc();
                }
                break;
            case 1://模拟成功
                resultMap.put("ybdrawflowid", "0000000000001");
                submitSuccess(withDrawId, resultMap, withDraw);
                resultMsg = "success";
                break;
            case 2://模拟失败
                resultMap.put("error_code", "00000001");
                resultMap.put("error_msg", "模拟失败");
                submitFailure(withDrawId, resultMap, withDraw);
                resultMsg = "提现请求发送失败：" + withDraw.getResultDesc();
                break;
        }

        withDraws.add(withDraw);
        this.withDraw(withDraws);
        return resultMsg;
    }

    /**
     * 提交结果处理中
     * @param withDrawId 本次处理的提现单号
     * @param resultMap 同步返回结果
     * @param withDraw 封装实体
     */
    private void submitDoing(String withDrawId, Map<String, String> resultMap, WithDraw withDraw) {
        String errorCode = resultMap.get("error_code");
        String errorMsg = resultMap.get("error_msg");
        withDraw.setResultDesc("【" + errorCode + "】:【" + errorMsg + "】");
        withDraw.setWithdrawId(Long.valueOf(withDrawId));
        withDraw.setTransStatus(WithDrawTransferStatus.TRANSFER_ING.getValue());
        myBatisDao.update("updateWithDraw", withDraw);
        logger.info("提现请求处理中：" + withDraw.getResultDesc());
    }

    /**
     * 提交失败
     *
     * @param withDrawId
     * @param resultMap
     * @param withDraw
     */
    private void submitFailure(String withDrawId, Map<String, String> resultMap, WithDraw withDraw) {
        String errorCode = resultMap.get("error_code");
        String errorMsg = resultMap.get("error_msg");
        withDraw.setResultDesc("【" + errorCode + "】:【" + errorMsg + "】");
        String tranStatus = WithDrawTransferStatus.TRANSFER_FAILE.getValue();
        withDraw.setWithdrawId(Long.valueOf(withDrawId));
        withDraw.setTransStatus(tranStatus);
        //todo 提现失败后续处理
        logger.info("提现请求失败：" + withDraw.getResultDesc());
    }

    /**
     * 提交成功
     *
     * @param withDrawId
     * @param resultMap
     * @param withDraw
     */
    private void submitSuccess(String withDrawId, Map<String, String> resultMap, WithDraw withDraw) {
        String flowId = resultMap.get("ybdrawflowid");
        String tranStatus = WithDrawTransferStatus.TRANSFER_ING.getValue();
        withDraw.setWithdrawId(Long.valueOf(withDrawId));
        withDraw.setTransStatus(tranStatus);
        withDraw.setWithDrawFlowId(flowId);
    }

    @Override
    public WithDraw getWithDrawByWithDrawId(Long withDrawId) {
        return myBatisDao.get("WITHDRAW.selectByPrimaryKey", withDrawId);
    }

    @Override
    @Transactional
    public void reWithDraw(Long withDrawId, Long admimId) throws Exception {

        WithDraw withDraw = getWithDrawByWithDrawId(withDrawId);
        LoanWithdrawRelations lwr = myBatisDao.get("LOAN_WITHDRAW_RELATIONS.selectByWithDrawId", withDraw.getWithdrawId());

        loanApplicationService.reMakeLoan(lwr.getLoanApplicationId(), admimId);
    }

    /**
     * 提现失败，基础操作操作
     *
     * @param withDrawId
     */
    @Transactional
    public WithDraw withDrawFailure(Long withDrawId) {
        WithDraw detail = this.getWithDrawByWithDrawId(withDrawId);
        if (detail.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_FAILE.getValue())) {
            return detail;
        }

        //打款状态，提现失败
        detail.setTransStatus(WithDrawTransferStatus.TRANSFER_FAILE.getValue());
        myBatisDao.update("updateWithDraw", detail);

        //获取用户的出借账户
        UserAccount userAccount = userAccountService.getCashAccount(detail.getUserId());
        UserInfo user = userInfoService.getUserByUserId(detail.getUserId());
        //提现解冻
        AccountValueChangedQueue accountValueChangedQueue = new AccountValueChangedQueue();
        AccountValueChanged accountValueChanged = new AccountValueChanged(userAccount.getAccId(), detail.getWithdrawAmount(), detail.getWithdrawAmount(), AccountConstants.AccountOperateEnum.UNFREEZE.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASHUNFREEZEN.getValue(), AccountConstants.AccountChangedTypeEnum.WITHDRAW.getValue(), "1", detail.getWithdrawId(),
                UserType.getUserTypeByValue(user.getType()).getAccountChangedType().getValue(), detail.getUserId(), new Date(),
                com.xt.cfp.core.util.StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.WITHDRAW_UNFREEZE_FAILE_, detail.getWithdrawAmount()), true);
        accountValueChangedQueue.addAccountValueChanged(accountValueChanged);
        userAccountOperateService.execute(accountValueChangedQueue);

        //提现失败
        sendWithDrawFailureMsg(detail);
        return detail;
    }


    private List<WithDraw> getSheetWithDraw(String[] title, List<Map<String, Object>> result) {
        List<WithDraw> withDraws = new ArrayList<WithDraw>();
        for (Map<String, Object> map : result) {
            String withDrawId = (String) map.get(title[0]);
            String tranStatusDesc = (String) map.get(title[5]);
            String tranStatus = WithDrawTransferStatus.getAccountOperateByDesc(tranStatusDesc).getValue();
            String resultDesc = (String) map.get(title[6]);
            WithDraw withDraw = new WithDraw();
            withDraw.setWithdrawId(Long.valueOf(withDrawId));
            withDraw.setTransStatus(tranStatus);
            withDraw.setResultDesc(resultDesc);
            withDraws.add(withDraw);
        }
        return withDraws;
    }

    /**
     * 查询获得已审核，待打款的提现单
     *
     * @return
     */
    private List<WithDrawExt> getTransferWithDrawVerified() {
        List<WithDrawExt> list = myBatisDao.getList("getTransferWithDrawVerified", null);
        return list;
    }

    public static BigDecimal getFeePercent() {
        return fee;
    }

    @Property(name = "withDraw.fee")
    public static void setFee(BigDecimal fee) {
        WithDrawServiceImpl.fee = fee;
    }

    /**
     * 根据用户卡ID，获得提现总额
     *
     * @param customerCardId 用户卡ID
     */
    @Override
    public BigDecimal getWithdrawAmountSumByCardId(Long customerCardId) {
        return myBatisDao.get("WITHDRAW.getWithdrawAmountSumByCardId", customerCardId);
    }

    @Override
    @Transactional
    public String refreshNew(String withDrawId) {
    	Map<String, String> resultMap = new HashMap<String,String>();
        WithDraw withDraw = myBatisDao.get("WITHDRAW.selectByPrimaryKey", withDrawId);
        String flowId = withDraw.getWithDrawFlowId();
        if (StringUtils.isEmpty(flowId))
            throw new SystemException(WithDrawErrorCode.WITHDRAW_FLOW_NULL).set("flowId", flowId);

        CustomerCard customerCard = customerCardService.findById(withDraw.getCustomerCardId());
        if(PayConstants.PayChannel.YB.getValue().equals(customerCard.getBelongChannel())){
        	resultMap = TZTService.queryWithdraw(withDrawId, flowId);
        }else if(PayConstants.PayChannel.LL.getValue().equals(customerCard.getBelongChannel())){
        	Map<String,String> params = new HashMap<String,String>();
            params.put("no_order", withDraw.getWithdrawId()+"");
            resultMap = LLPayUtil.withDrawOrderQuery(params);
        }
        logger.info("【刷新】执行提现回调withDrawProcessing");
        return withDrawProcessing(resultMap, withDrawId);
    }

    public String withDrawProcessing(Map<String,String> resultMap, String withDrawId){
    	//DOING：处理中
        //FAILURE：提现失败
        //REFUND：提现退回
        //SUCCESS：提现成功
        //UNKNOW：未知

        if (resultMap.containsKey("customError")){
            return "DOING";
        }

        List<WithDraw> withDraws = new ArrayList<WithDraw>();
        String status = resultMap.get("status");
        WithDraw _withDraw = new WithDraw();
        switch (withDrawRefreshFlag) {
            case 0://正式环境
                if ("SUCCESS".equals(status)) {
                    refreshSuccess(withDrawId, withDraws, _withDraw);
                } else if ("FAILURE".equals(status)) {
                    refreshFailure(withDrawId, withDraws, _withDraw);
                } else {
                    if (!"0000".equals(resultMap.get("ret_code"))){
                        return "【" + resultMap.get("ret_code") + "】:【" + resultMap.get("ret_msg") + "】";
                    }
                    return "DOING";
                }
                break;
            case 1://模拟成功
                refreshSuccess(withDrawId, withDraws, _withDraw);
                status = "SUCCESS";
                break;
            case 2://模拟失败
                refreshFailure(withDrawId, withDraws, _withDraw);
                status = "FAILURE";
                break;
        }
        this.withDraw(withDraws);
        return status;
    }


    @Override
    @Transactional
    public String withDrawCallback(Map<String, String> resultMap) {
        WithDraw withDraw = myBatisDao.get("WITHDRAW.selectByPrimaryKey", resultMap.get("orderid"));
        String withDrawId = resultMap.get("orderid");
        String flowId = withDraw.getWithDrawFlowId();
        if (StringUtils.isEmpty(flowId))
            throw new SystemException(WithDrawErrorCode.WITHDRAW_FLOW_NULL).set("flowId", flowId);

        logger.info("【withDrawCallback】执行提现回调withDrawProcessing");
        return withDrawProcessing(resultMap, withDrawId);
    }


    private void refreshFailure(String withDrawId, List<WithDraw> withDraws, WithDraw _withDraw) {
        String tranStatus = WithDrawTransferStatus.TRANSFER_FAILE.getValue();
        _withDraw.setWithdrawId(Long.valueOf(withDrawId));
        _withDraw.setTransStatus(tranStatus);
        withDraws.add(_withDraw);
    }

    private void refreshSuccess(String withDrawId, List<WithDraw> withDraws, WithDraw _withDraw) {
        String tranStatus = WithDrawTransferStatus.TRANSFER_SUCCESS.getValue();
        _withDraw.setTransStatus(tranStatus);
        _withDraw.setWithdrawId(Long.valueOf(withDrawId));
        withDraws.add(_withDraw);
    }

    @Override
    public int getWithDrawTimesByUserId(Long userId) {
        if (userId == null)
            return 0;
        return myBatisDao.get("WITHDRAW.getWithDrawTimesByUserId", userId);
    }
    @Override
    public BigDecimal getWithDrawAmountByUserId(Long userId) {
        if (userId == null)
            return BigDecimal.ZERO;
        return myBatisDao.get("WITHDRAW.getWithDrawAmountByUserId", userId);
    }

    @Override
    public int getWithDrawTimesDue() {
        return this.getWithDrawTimes();
    }

	@Override
	public List<WithDraw> findBy(WithDraw withDraw) {
		return myBatisDao.getList("WITHDRAW.findBy", withDraw);
	}

    @Override
    public BigDecimal getAllWithDrawAmountByUserId(Long userId) {
        return getAllWithDrawAmountByUserId(userId, "");
    }

    @Override
    public BigDecimal getAllWithDrawAmountByUserId(Long userId,String month) {

        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(month)){
            params.put("month", month);
        }

        return this.myBatisDao.get("WITHDRAW.getAllWithDrawAmountByUserId", params);
    }

    @Override
    @Transactional
    public void outLineWithDraw(String sysMobileNo,Long bankid ,String cardNo,String mobileNo,BigDecimal amount) {

        UserInfo userInfo = userInfoService.getUserByMobileNo(sysMobileNo);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(userInfo.getUserId());
        //添加银行卡
        CustomerCard customerCard = new CustomerCard();
        customerCard.setUserId(userInfo.getUserId());
        customerCard.setCardType(CardType.FULL_CARD.getValue());
        customerCard.setMobile(mobileNo);
        customerCard.setStatus(CustomerCardStatus.DISABLED.getValue());
        customerCard.setCardCode(cardNo);
        customerCard.setCardcustomerName(userExt.getRealName());
        customerCard.setUpdateTime(new Date());
        customerCard.setBankCode(bankid);
        customerCard = customerCardService.addCustomerCard(customerCard);
        //生成提现单
        WithDraw withDraw = new WithDraw();
        withDraw.setWithdrawAmount(amount);
        withDraw.setHappenType(WithDrawSource.SYSTEM_OPERT_WITHDRAW.getValue());
        withDraw.setUserId(userInfo.getUserId());
        withDraw.setCustomerCardId(customerCard.getCustomerCardId());
        withDraw.setCreateTime(new Date());
        withDraw(false,withDraw, AccountConstants.AccountChangedTypeEnum.PLATFORM_USER, null);
    }

    @Override
    public int getVoucherWithDrawCount(Long userId) {
        return myBatisDao.get("VOUCHER.getVoucherWithDrawCount",userId);
    }

    /**
     * 快过期的提现券
     * @param userId
     * @return
     */
    @Override
    public Voucher getVoucherWithDraw(Long userId) {
        return myBatisDao.get("VOUCHER.getVoucherWithDraw",userId);
    }

	@Override
	public Pagination<CRMWithdrawVO> getCRMWithdrawListPaging(int pageNo, int pageSize, Map<String, Object> customParams) {
		Pagination<CRMWithdrawVO> re = new Pagination<CRMWithdrawVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        if(customParams.size()>0){
        	Set<String> keys=customParams.keySet();
        	for (String key : keys) {
        		params.put(key, customParams.get(key));
			}
        }
        int totalCount = this.myBatisDao.count("CRMWithdrawList", params);
        List<CRMWithdrawVO> uah = this.myBatisDao.getListForPaging("CRMWithdrawList", params, pageNo, pageSize);
        for (CRMWithdrawVO w : uah) {
			if(w.getVerifyStatus().equals("0")&&w.getTransStatus().equals("0")){
				w.setStatus("审核中");
			}else if(w.getVerifyStatus().equals("2")){
				w.setStatus("审核驳回");
			}else if(w.getVerifyStatus().equals("1")&&(
						w.getTransStatus().equals("0")||w.getTransStatus().equals("1")||w.getTransStatus().equals("2")
					)
				){
				w.setStatus("审核通过待打款");
			}else if(w.getVerifyStatus().equals("1")&&w.getTransStatus().equals("3")){
				w.setStatus("提现成功");
			}else if(w.getVerifyStatus().equals("1")&&w.getTransStatus().equals("4")){
				w.setStatus("提现失败");
			}
			w.setWithdrawCard(w.getBankName()!=null?w.getBankName():""+(w.getCardNo()!=null?"("+w.getCardNo().substring(w.getCardNo().length()-4, w.getCardNo().length())+")":""));
		}
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}

	@Override
	public void exportCRMWithdrawList(HttpServletResponse response, Map<String, Object> customParams) {
		String[] title = {"客户姓名", "客户手机号", "提现金额", "提现卡号", "提现状态", "提交时间", "完成时间", "邀请员工", "邀请码", "所在部门", "加盟商"};
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        if(customParams.size()>0){
        	Set<String> keys=customParams.keySet();
        	for (String key : keys) {
        		params.put(key, customParams.get(key));
			}
        }
        List<CRMWithdrawVO> uah = this.myBatisDao.getList("CRMWithdrawList", params);
        for (CRMWithdrawVO w : uah) {
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put(title[0], w.getCustomerName());
        	map.put(title[1], w.getCustomerMobile());
        	map.put(title[2], w.getAmount());
        	map.put(title[3], w.getBankName()+"("+w.getCardNo().substring(w.getCardNo().length()-4, w.getCardNo().length())+")");
        	if(w.getVerifyStatus().equals("0")&&w.getTransStatus().equals("0")){
				w.setStatus("审核中");
			}else if(w.getVerifyStatus().equals("2")){
				w.setStatus("审核驳回");
			}else if(w.getVerifyStatus().equals("1")&&(
						w.getTransStatus().equals("0")||w.getTransStatus().equals("1")||w.getTransStatus().equals("2")
					)
				){
				w.setStatus("审核通过待打款");
			}else if(w.getVerifyStatus().equals("1")&&w.getTransStatus().equals("3")){
				w.setStatus("提现成功");
			}else if(w.getVerifyStatus().equals("1")&&w.getTransStatus().equals("4")){
				w.setStatus("提现失败");
			}
        	map.put(title[4], w.getStatus());
        	map.put(title[5], DateUtil.getDateLong(w.getCreateTime()));
        	map.put(title[6], DateUtil.getDateLong(w.getResultTime()));
        	map.put(title[7], w.getStaffName());
        	map.put(title[8], w.getInvitationCode());
        	map.put(title[9], w.getOrgName());
        	map.put(title[10], w.getFranchiseName());
        	dataMap.add(map);
		}
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "提现列表";
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(title, dataMap, fileName);
            wb.write(os);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

    /**
     * 新新建放款主任务
     * @param withDrawId
     * @return
     */
    private Schedule insertMainHistoryForDataBase(Long withDrawId){
        Schedule schedule = new Schedule();
        schedule.setBusinessId(withDrawId);
        schedule.setStartTime(new Date());
        schedule.setStatus(Integer.valueOf(ScheduleEnum.BUSINESS_SUCCESS.getValue()));//准备中
        schedule.setBusinessType(Integer.valueOf(AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASH.getValue()));
        scheduleService.addSchedule(schedule);
        return schedule;
    }

    /**
     *
     * @param outUserId
     * @param inUserId
     * @param money
     * @param scheduleId
     * @param type
     */
    private void insertSubTaskByLoan(Long outUserId,Long inUserId,BigDecimal money,Long scheduleId,String type,long businessId) {
        CapitalFlow capitalFlow = new CapitalFlow();
        capitalFlow.setAmount(money);
        capitalFlow.setScheduleId(scheduleId);
        capitalFlow.setStartTime(new Date());
        capitalFlow.setToUser(inUserId);
        capitalFlow.setFromUser(outUserId);
        capitalFlow.setResult(Integer.valueOf(ScheduleEnum.RESULT_SUCCESS.getValue()));
        capitalFlow.setOperationType(Integer.valueOf(type));
        capitalFlow.setBusinessId(businessId);
        this.capitalFlowService.addCapital(capitalFlow);
    }

    /**
     * 获取公司平台账户
     *
     * @param systemAccountId
     * @return
     */
    private Long getSystemUserInfo() {
        UserAccount account = this.userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
        return account.getUserId();
    }
}
