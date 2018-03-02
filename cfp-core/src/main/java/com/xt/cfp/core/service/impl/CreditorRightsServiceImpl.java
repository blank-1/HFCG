package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.CreditorErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.AwardPointEnum;
import com.xt.cfp.core.constants.ChannelTypeEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppType;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus;
import com.xt.cfp.core.constants.CreditorRightsFromWhereEnum;
import com.xt.cfp.core.constants.DelayStateEnum;
import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.FeesItemTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.constants.LendOrderReceiveStateEnum;
import com.xt.cfp.core.constants.LenderTypeEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.constants.PaymentMethodEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RepaymentPlanStateEnum;
import com.xt.cfp.core.constants.SubjectTypeEnum;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AwardDetail;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CreditorRightsDealDetail;
import com.xt.cfp.core.pojo.CreditorRightsHistory;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendLoanBinding;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.LendOrderReceive;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LendProductFeesItem;
import com.xt.cfp.core.pojo.LendProductPublish;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanApplicationFeesItem;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanPublish;
import com.xt.cfp.core.pojo.PayOrder;
import com.xt.cfp.core.pojo.RateLendOrder;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.ext.CreditorRightsCount;
import com.xt.cfp.core.pojo.ext.CreditorRightsExtVo;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LenderVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.PayResult;
import com.xt.cfp.core.pojo.ext.RateLendOrderVO;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.AwardDetailService;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CreditorRightsTransferAppService;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LendLoanBindingService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderReceiveService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationFeesItemService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.RateLendOrderService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.RightsPrepayDetailService;
import com.xt.cfp.core.service.RightsRepaymentDetailService;
import com.xt.cfp.core.service.UserAccountOperateService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.matchrules.MatchCreditorVO;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.PropertiesUtils;
import com.xt.cfp.core.util.StringUtils;

@Service
public class CreditorRightsServiceImpl implements CreditorRightsService {

    private static Logger logger = Logger.getLogger("creditorrightsLogger");
    @Autowired
    private MyBatisDao myBatisDao;

    @Autowired
    private LendOrderService lendOrderService;

    @Autowired
    private LendLoanBindingService lendLoanBindingService;

    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private RightsPrepayDetailService rightsPrepayDetailService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private FeesItemService feesItemService;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private LoanApplicationFeesItemService loanApplicationFeesItemService;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    public CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    public PayService payService;
    @Autowired
    public RateLendOrderService rateLendOrderService;

    @Value(value = "${BUY_RIGHTS_CRITICAL_POINT}")
    protected BigDecimal criticalPointBalance;

    @Value(value = "${BUY_RIGHTS_MIN_BALANCE}")
    protected BigDecimal minBalance;

    @Value(value = "${LAST_BUY_RIGHTS_BALANCE}")
    protected BigDecimal lastBuyBalance;
    @Autowired
    private AwardDetailService awardDetailService;

    @Override
    public List<CreditorRights> findAll(ChannelTypeEnum channelTypeEnum) {
        return myBatisDao.getList("CREDITORRIGHTS.findAll",channelTypeEnum.getValue());
    }

    @Override
    public List<CreditorRights> getByLoanApplicationId(long loanApplicationId) {
        return myBatisDao.getList("CREDITORRIGHTS.getByLoanApplicationId", loanApplicationId);
    }

    @Override
    public List<CreditorRights> getByLendOrderId(Long lendOrderId, List<String>  statusEnum) {
        Map params = new HashMap();
        params.put("lendOrderId", lendOrderId);
        params.put("statusEnum", statusEnum);

        return myBatisDao.getList("CREDITORRIGHTS.getByLendOrderId", params);
    }

    @Override
    public Pagination<LenderVO> getLenderListByApplicationId(int pageNo, int pageSize, long loanApplicationId,CreditorRightsConstants.CreditorRightsStateEnum... status) {
        Pagination<LenderVO> re = new Pagination<LenderVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);


        List<String> statusList = new ArrayList<String>();
        if (status == null || status.length == 0) {
            statusList = null;
        } else {
            for (CreditorRightsConstants.CreditorRightsStateEnum _status : status) {
                statusList.add(_status.getValue());
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("statusList", statusList);
        params.put("loanApplicationId", loanApplicationId);


        int totalCount = this.myBatisDao.count("getLenderListByApplicationId", params);
        List<LenderVO> lenderList = this.myBatisDao.getListForPaging("getLenderListByApplicationId", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(lenderList);

        return re;
    }

    @Override
    public CreditorRights getSubjectRightsByLoanApplicationId(long loanApplicationId) {
        return myBatisDao.get("CREDITORRIGHTS.getSubjectRights", loanApplicationId);
    }
	
    @Override
    public void update(Map creditorMap) {
        myBatisDao.update("CREDITORRIGHTS.updateByMap", creditorMap);
    }

    @Override
    public void update(CreditorRights creditorRights) {
        myBatisDao.update("CREDITORRIGHTS.updateByPrimaryKeySelective", creditorRights);
    }

    @Override
    public void insert(CreditorRights creditorRights) {
        myBatisDao.insert("CREDITORRIGHTS.insert", creditorRights);
    }

    /**
     * 查询理财订单中针对指定借款产品的累积出资金额
     *
     * @return
     */
    @Override
    public BigDecimal getSumCreByOrderAndLoanPdt(long loanProductId, long lendOrderId, long customerAccountId) {
        return lendOrderBidDetailService.sumByLoanPdtIdAndLendOrderId(loanProductId, lendOrderId,customerAccountId);
    }

    /**
     * 查询该客户购买了指定借款申请的累积金额
     *
     * @param userId
     * @param loanApplicationId
     * @return
     */
    public BigDecimal getSumCreByUserAndLoanApp(long userId, long loanApplicationId) {
        return lendOrderBidDetailService.sumCreByUserAndLoanApp(userId, loanApplicationId);
    }

    /**
     * 分页列表
     *
     * @param pageNo   页码
     * @param pageSize 页数
     * @param params   查询条件
     */
    @Override
    public Pagination<CreditorRightsExtVo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
        Pagination<CreditorRightsExtVo> pagination = new Pagination<CreditorRightsExtVo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<CreditorRightsExtVo> creditorRights = myBatisDao.getListForPaging("findAllCreditorRightsByPage", params, pageNo, pageSize);
        for (CreditorRightsExtVo crediter : creditorRights) {
        	if(!StringUtils.isNull(crediter.getLoanType())){
        		crediter.setLoanType(LoanTypeEnum.getLoanTypeEnumByCode(crediter.getLoanType()).getDesc());
        	}
        	if(!StringUtils.isNull(String.valueOf(crediter.getRightsState()))){
        		crediter.setRightsStateDesc(CreditorRightsConstants.CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(crediter.getRightsState())).getDesc());
        	}
        	if(!StringUtils.isNull(crediter.getBusStatus())){
        		crediter.setTurnStateDesc(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.getCreditorRightsTransferAppStatusByValue(crediter.getBusStatus()).getDesc());
        	}
		}
        pagination.setRows(creditorRights);
        pagination.setTotal(myBatisDao.count("findAllCreditorRightsByPage", params));
        return pagination;
    }

    /**
     * 根据id加载一条数据
     *
     * @param creditorRightsId
     */
    public CreditorRights findById(Long creditorRightsId) {
        return findById(creditorRightsId,false);
    }

    @Override
    public CreditorRights findById(Long creditorRightsId, boolean lock) {
        Map map = new HashMap();
        map.put("creditorRightsId", creditorRightsId);
        if(lock)
        	map.put("lock", lock);

        return myBatisDao.get("CREDITORRIGHTS.findById", map);
    }

    @Override
    public String createRightsCode(char fromWhere, String lendOrderCode, String loanApplicationCode) {
//        return fromWhere + "_" + loanApplicationCode + "_" + lendOrderCode + "_" + StringUtils.getRadomStr(6);
        //todo 算法规则待定
        return StringUtils.getRadomStr(32);
    }

    @Override
    public List<CreditorRights> findRollOutRights(Map paraMap) {
        return myBatisDao.getList("CREDITORRIGHTS.findRollOutRights", paraMap);
    }

    @Override
    @Transactional
    public void newRightsForFinanceLendOrder(LendOrder lendOrder, MatchCreditorVO matchCreditorVO, CreditorRightsTransferApplication transferApplication) {
        Date now = new Date();

        CreditorRights creditorRights = findById(transferApplication.getApplyCrId());
        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(creditorRights.getLoanApplicationId());
        LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
        //生成支付订单
        PayOrder payOrder = null;
        //如果是出借产品，就建立出借明细数据
        payOrder = payService.createRightsPayOrderForFinanceLendOrder(lendOrder, creditorRights.getLoanApplicationId(), lendProductPublish.getLendProductPublishId(), matchCreditorVO.getBalance(), new Date(), lendProduct, PropertiesUtils.getInstance().get("SOURCE_PC"), transferApplication);
        //执行订单支付
        PayResult payResult = payService.doPay(payOrder.getPayId(), now);

//        if(payResult.isProcessResult()){
//            //修改订单待理财金额
//            Map<String, Object> lendOrderMap = new HashMap();
//            lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
//            lendOrderMap.put("forLendBalance", lendOrder.getForLendBalance().subtract(matchCreditorVO.getBalance()));
//            lendOrderService.update(lendOrderMap);
//        }
    }
    /**
     * @param avcq
     * @param lendOrder
     * @param now
     * @param matchCreditorVO
     * @throws Exception
     * @deprecated 出借生成新债权
     */
    public void newCreditorRights(AccountValueChangedQueue avcq, LendOrder lendOrder, Date now, MatchCreditorVO matchCreditorVO, CreditorRightsConstants.CreditorRightsStateEnum creditorRightsStateEnum, ChannelTypeEnum channelTypeEnum) throws Exception {
        LendOrderBidDetail orderBidDetail = insertBidDetail(lendOrder.getLendOrderId(), matchCreditorVO.getBalance(), matchCreditorVO.getTheId());
        LoanApplication loanApplication = loanApplicationService.findById(matchCreditorVO.getTheId());
        LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
        List<RepaymentPlan> repaymentPlans = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplication.getLoanApplicationId());
        //todo 倒数第二个参数为债权明细占比，需要考虑如何计算
        BigDecimal ratio100 = BigDecimal.valueOf(100);
        BigDecimal creditorBalance = lendOrderBidDetailService.sumCreByLoanApp(loanApplication.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING);

        BigDecimal proportion = null;
        if (BigDecimalUtil.compareTo(creditorBalance, loanApplication.getConfirmBalance(), 2) == 0) {

        } else {
            proportion = lendOrder.getBuyBalance().divide(loanApplication.getConfirmBalance(), BigDecimal.ROUND_DOWN);
            proportion = BigDecimalUtil.down(proportion, 4).multiply(ratio100);
        }

        CreditorRights creditorRights = createCreditorRights(lendOrder, loanApplication,
                this.createRightsCode(CreditorRightsFromWhereEnum.BUY.value2Char(), lendOrder.getOrderCode(), loanApplication.getLoanApplicationCode()),
                matchCreditorVO.getBalance(), DisplayEnum.DISPLAY.value2Char(), null, repaymentPlans, creditorRightsStateEnum, channelTypeEnum);
        //出借订单冻结出资金额
        String desc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.BUYRIGHTS_FREEZE, loanPublish.getLoanTitle());
        AccountValueChanged avcFreeze = new AccountValueChanged(lendOrder.getCustomerAccountId(), matchCreditorVO.getBalance(),
                matchCreditorVO.getBalance(), AccountConstants.AccountOperateEnum.FREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BUYRIGHTS.getValue(),
                "CreditorRights", VisiableEnum.DISPLAY.getValue(), creditorRights.getCreditorRightsId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                lendOrder.getLendOrderId(), now, desc, true);
        avcq.addAccountValueChanged(avcFreeze);
        //todo 判断借款申请是否满标，如满标要进行满标处理
        loanApplicationService.notice2FullBid(loanApplication, now);
    }


    private LendOrderBidDetail insertBidDetail(long lendOrderId, BigDecimal buyBalance, long loanAppId) {
        LendOrderBidDetail orderBidDetail = new LendOrderBidDetail();
        orderBidDetail.setLendOrderId(lendOrderId);
        orderBidDetail.setLoanApplicationId(loanAppId);
        orderBidDetail.setBuyBalance(buyBalance);
        orderBidDetail.setStatus(LendOrderBidStatusEnum.BIDING.value2Char());
        orderBidDetail.setBuyDate(new Date());
        lendOrderBidDetailService.insert(orderBidDetail);
        return orderBidDetail;
    }


    public CreditorRights createCreditorRights(LendOrder lendOrder, LoanApplication loanApplication,
                                               String rightsCode, BigDecimal balance, char displayState, BigDecimal proportion,
                                               List<RepaymentPlan> repaymentPlanList, CreditorRightsConstants.CreditorRightsStateEnum creditorRightsStateEnum, ChannelTypeEnum channelTypeEnum) throws Exception {
        Date today = new Date();
        LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
        UserInfoVO loanUser = userInfoService.getUserExtByUserId(loanApplication.getUserId());
        UserInfoVO lendUser = userInfoService.getUserExtByUserId(lendOrder.getLendUserId());
        CreditorRights creditorRights = new CreditorRights();
        creditorRights.setLendOrderId(lendOrder.getLendOrderId());
        creditorRights.setLoanApplicationId(loanApplication.getLoanApplicationId());
        creditorRights.setLendAccountId(lendOrder.getCustomerAccountId());
        creditorRights.setLoanAccountId(loanApplication.getCustomerAccountId());
        creditorRights.setRepaymentAccountId(loanApplication.getRepaymentAccountId());
        creditorRights.setLendUserId(lendUser.getUserId());
        creditorRights.setLoanUserId(loanUser.getUserId());
        creditorRights.setCreditorRightsCode(rightsCode);
        creditorRights.setLendTime(new Date());
        creditorRights.setCreateTime(new Date());
        creditorRights.setApplyRollOutTime(null); //申请转出时间
        creditorRights.setAgreementStartDate(today);
        creditorRights.setFirstRepaymentDate(null);
        creditorRights.setLastRepaymentDate(null);
        creditorRights.setAnnualRate(loanApplication.getAnnualRate());
        creditorRights.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());

        creditorRights.setRightsWorth(balance);
        creditorRights.setLendPrice(BigDecimalUtil.up(creditorRights.getRightsWorth(), 2));
        creditorRights.setBuyPrice(creditorRights.getLendPrice());
        creditorRights.setAnnualRate(loanApplication.getAnnualRate());
        creditorRights.setRepaymentCycle(loanProduct.getRepaymentCycle());
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.LOAN.getValue())) {
            creditorRights.setRightsState(creditorRightsStateEnum.value2Char());//债权状态为已购买
            creditorRights.setLenderType(LenderTypeEnum.LENDER.value2Char());

//            //修改出借产品的已售金额
//            logger.debug("准备修改出借产品的已售金额");
//            LendProductPublish lendProductPublish = lendProductService.getLendProductPublishById(lendOrder.getLendProductPublishId());
//            logger.debug("出借产品发布名称：" + lendProductPublish.getPublishName() + " 原已售:" + lendProductPublish.getSoldBalance().toPlainString());
//            lendProductPublish.setSoldBalance(lendProductPublish.getSoldBalance().add(balance));
//            logger.debug("已售金额改为:" + lendProductPublish.getSoldBalance().toPlainString());
        } else {
            creditorRights.setRightsState(creditorRightsStateEnum.value2Char());//债权状态为申请转出
            creditorRights.setLenderType(LenderTypeEnum.THECHANNEL.value2Char());
            creditorRights.setApplyRollOutTime(new Date());//当前时间即为申请转出时间

        }
        creditorRights.setChannelType(channelTypeEnum.value2Long());
        creditorRights.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());
        creditorRights.setIsDelay(DelayStateEnum.NODELAY.value2Char());
        creditorRights.setDisplayState(displayState);
        creditorRights.setLoanCustomerCerCode(loanUser.getIdCard());
        creditorRights.setLoanCustomerName(loanUser.getRealName());
        creditorRights.setLoanSystemLoginName(loanUser.getLoginName());
        creditorRights.setLendCustomerCerCode(lendUser.getIdCard());
        creditorRights.setLendCustomerName(lendUser.getRealName());
        creditorRights.setLendSystemLoginName(lendUser.getLoginName());

        this.insert(creditorRights);


        //计算借款利息总和
        Map<Integer, Map<String, BigDecimal>> repayInfo = null;
        BigDecimal lendBalance = lendOrder.getBuyBalance();
        BigDecimal annualRate = loanApplication.getAnnualRate();
        char dueTimeType = loanProduct.getDueTimeType();
        char repaymentMethod = loanProduct.getRepaymentMethod();
        char repaymentType = loanProduct.getRepaymentType();
        char repaymentCycle = loanProduct.getRepaymentCycle();
        Integer dueTime = loanProduct.getDueTime();
        Integer cycleValue = loanProduct.getCycleValue();
        repayInfo = InterestCalculation.getCalitalAndInterest(lendBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue);
        BigDecimal allInterest = InterestCalculation.getAllInterest(lendBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue);

        int planIndex = 0;
        BigDecimal rightsBalance = BigDecimal.ZERO;//已有债权金额
        BigDecimal rightsInterest = BigDecimal.ZERO;//已有债权利息
        BigDecimal rightsCapital = BigDecimal.ZERO;//已有债权本金

        BigDecimal rightsBalance2 = BigDecimal.ZERO;//已有债权金额（2位）
        BigDecimal rightsInterest2 = BigDecimal.ZERO;//已有债权利息（2位）
        BigDecimal rightsCapital2 = BigDecimal.ZERO;//已有债权本金（2位）
        for (RepaymentPlan repaymentPlan : repaymentPlanList) {
            RightsRepaymentDetail rightsRepaymentDetail = new RightsRepaymentDetail();
            rightsRepaymentDetail.setCreditorRightsId(creditorRights.getCreditorRightsId());
            rightsRepaymentDetail.setRepaymentPlanId(repaymentPlan.getRepaymentPlanId());
            rightsRepaymentDetail.setLendOrderId(lendOrder.getLendOrderId());
            rightsRepaymentDetail.setLoanApplicationId(loanApplication.getLoanApplicationId());
            rightsRepaymentDetail.setLoanAccountId(loanApplication.getRepaymentAccountId());
            rightsRepaymentDetail.setLendAccountId(lendOrder.getCustomerAccountId());
            rightsRepaymentDetail.setSectionCode(repaymentPlan.getSectionCode());
            rightsRepaymentDetail.setProportion(proportion);
            rightsRepaymentDetail.setRepaymentDayPlanned(repaymentPlan.getRepaymentDay());


            //当前还款计划 是否是最后一期还款
            if (planIndex == repaymentPlanList.size() - 1) {
                //18位
                rightsRepaymentDetail.setShouldBalance(lendBalance.add(allInterest).subtract(rightsBalance));
                rightsRepaymentDetail.setShouldCapital(lendBalance.subtract(rightsCapital));
                rightsRepaymentDetail.setShouldInterest(allInterest.subtract(rightsInterest));
                //2位
                rightsRepaymentDetail.setShouldBalance2(lendBalance.add(BigDecimalUtil.down(allInterest, 2)).subtract(rightsBalance2));
                rightsRepaymentDetail.setShouldCapital2(lendBalance.subtract(rightsCapital2));
                rightsRepaymentDetail.setShouldInterest2(rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getShouldCapital2()));
            } else {
                Map repaymentDecimal = repayInfo.get(repaymentPlan.getSectionCode());
                //18位
                rightsRepaymentDetail.setShouldBalance((BigDecimal) repaymentDecimal.get("balance"));
                rightsRepaymentDetail.setShouldCapital((BigDecimal) repaymentDecimal.get("calital"));
                rightsRepaymentDetail.setShouldInterest((BigDecimal) repaymentDecimal.get("interest"));
                //记录18位总和
                rightsBalance = rightsBalance.add(rightsRepaymentDetail.getShouldBalance());
                rightsInterest = rightsInterest.add(rightsRepaymentDetail.getShouldInterest());
                rightsCapital = rightsCapital.add(rightsRepaymentDetail.getShouldCapital());

                //2位
                rightsRepaymentDetail.setShouldBalance2(BigDecimalUtil.down(rightsRepaymentDetail.getShouldBalance(), 2));
                rightsRepaymentDetail.setShouldCapital2(BigDecimalUtil.down(rightsRepaymentDetail.getShouldCapital(), 2));
                rightsRepaymentDetail.setShouldInterest2(rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getShouldCapital2()));
                //记录2位总和
                rightsBalance2 = rightsBalance2.add(rightsRepaymentDetail.getShouldBalance2());
                rightsInterest2 = rightsInterest2.add(rightsRepaymentDetail.getShouldInterest2());
                rightsCapital2 = rightsCapital2.add(rightsRepaymentDetail.getShouldCapital2());
            }


            rightsRepaymentDetail.setIsDelay(RightsRepaymentDetail.ISDELAY_NO);
            rightsRepaymentDetail.setIsPayOff(RightsRepaymentDetail.ISPAYOFF_NO);
            rightsRepaymentDetailService.addRightsRepaymentDetail(rightsRepaymentDetail);
            planIndex++;

        }

        return creditorRights;
    }

    @Override
    public void newCreditorRightsHistory(CreditorRightsHistory creditorRightsHistory) {
        myBatisDao.insert("CREDITOR_RIGHTS_HISTORY.insert", creditorRightsHistory);
    }

    /**
     * 添加-债权信息。
     */
    @Override
    public CreditorRights addCreditorRights(CreditorRights creditorRights) {
        myBatisDao.insert("CREDITORRIGHTS.insert", creditorRights);
        return creditorRights;
    }
    
    @Override
	public CreditorRightsCount selectUserRightsByDaiHui(Long userId) {
    	Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        CreditorRightsCount count=this.myBatisDao.get("selectUserRightsByDaiHui", params);
        UserInfo user=userInfoService.getUserByUserId(userId);
        count.setUserName(user.getLoginName());
		return count;
	}
    
    @Override
	public List<CreditorRightsExtVo> getUserAllCreditorRights(CreditorRightsExtVo creditorRights, Map<String, Object> customParams) {
    	Map<String, Object> params = new HashMap<String, Object>();
        params.put("creditorRights", creditorRights);
        params.put("customParams", customParams);
        List<CreditorRightsExtVo> lenderList = this.myBatisDao.getList("getCreditorRightsPaging", params);
        //获取有债权信息的奖励中间表
        List<RateLendOrderVO> rateOrderList = rateLendOrderService.getRateLendOrderCreditByUserId(creditorRights.getLendUserId());
        Map<Long,List<RateLendOrderVO>> rateMap = new HashMap<>();
        for(RateLendOrderVO rateVO:rateOrderList){
        	if(rateMap.get(rateVO.getCreditRightsId()) == null || rateMap.get(rateVO.getCreditRightsId()).size()==0){
        		List<RateLendOrderVO> list = new ArrayList<>();
        		list.add(rateVO);
        		rateMap.put(rateVO.getCreditRightsId(),  list);
        	}else{
        		rateMap.get(rateVO.getCreditRightsId()).add(rateVO);
        	}
        }
        //获得债券明细
        for (CreditorRightsExtVo vo : lenderList) {
        	vo.setRightsStateStr(vo.getRightsState()+"");
        	vo.setFromWhereStr(vo.getFromWhere()+"");
        	List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
        	String point=StringUtils.isNull(vo.getAwardPoint())?"":null;
        	if(point==null){
        		if(vo.getAwardPoint().equals("1")){
        			point=AwardPointEnum.ATMAKELOAN.getDesc();
        		}else if(vo.getAwardPoint().equals("2")){
        			point=AwardPointEnum.ATREPAYMENT.getDesc();
        		}else{
        			point=AwardPointEnum.ATCOMPLETE.getDesc();
        		}
        	}
        	vo.setAwardPoint(point);
        	vo.setAwardRate(vo.getAwardRate()!=null&&!vo.getAwardRate().equals("")&&!vo.getAwardRate().equals("0")?"+"+vo.getAwardRate()+"%":"");
            vo.setCreditorRightsCode(PropertiesUtils.getInstance().get("AGREEMENT_VISIT_PATH") + vo.getCreditorRightsCode());
            //获得借款申请信息
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(vo.getLoanApplicationId());
            //计算预期收益
            LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
            //处理还款方式
            if (loanApplicationListVO.getRepayMethod().equals("1")) {
                loanApplicationListVO.setRepayMentMethod(PaymentMethodEnum.getPaymentMethod(loanApplicationListVO.getRepayMentMethod()).getDesc());
            } else {
                ConstantDefine repaymentMode = constantDefineCached.getConstantByValue("repaymentMode", loanApplicationListVO.getRepayMethod());
                loanApplicationListVO.setRepayMentMethod(repaymentMode.getConstantName());
            }
            vo.setLoanApplicationListVO(loanApplicationListVO);
            //获得出借产品所有费用项
            List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(vo.getLendOrderId());
            //需要计算回款时的费用项（周期费用）
            List<LendProductFeesItem> atcycleFeeitems = new ArrayList<LendProductFeesItem>();
            //需要计算回款时的费用项（到期费用）
            List<LendProductFeesItem> complateFeeitems = new ArrayList<LendProductFeesItem>();
            
            LoanApplicationFeesItem defaultInterestFees = null ;
            for (LendProductFeesItem feesItem : feeitems) {
                if (FeesPointEnum.ATCYCLE.value2Char() == feesItem.getChargePoint()) {
                    atcycleFeeitems.add(feesItem);
                }
                if (FeesPointEnum.ATCOMPLETE.value2Char() == feesItem.getChargePoint()) {
                    complateFeeitems.add(feesItem);
                }
                
            }
            //获得费用
            LendOrder lendOrder = lendOrderService.findById(vo.getLendOrderId());
            List<LendLoanBinding> byLendAndLoan = lendLoanBindingService.findByLendAndLoan(lendOrder.getLendProductId(), loanProduct.getLoanProductId());
            List<FeesItem> fis = new ArrayList<FeesItem>();
            if(byLendAndLoan!=null){
                for(LendLoanBinding llb:byLendAndLoan){
                    if (llb.getFeesItemId() != null && llb.getFeesItemId() > 0) {
                        fis.add(feesItemService.findById(llb.getFeesItemId()));
                    }
                }
            }
            //最近还款日期
            try {
                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("最近还款日期计算出错", e);
            }
            //处理一下明细，添加待缴费用
            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
            for (RightsRepaymentDetail detail : detailRightsList) {

                //计算到期费用
                BigDecimal fees1 = BigDecimal.ZERO;
                if (detail.getSectionCode()==detailRightsList.size()){
                    if (complateFeeitems!=null&&complateFeeitems.size()>0){
                        for (LendProductFeesItem item : complateFeeitems) {
                            BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                            fees1 = fees1.add(fee);
                        }
                        fees1 =  BigDecimalUtil.up(fees1, 2);
                    }
                }
                //计算周期费用
                BigDecimal fees2 = BigDecimal.ZERO;
                if (atcycleFeeitems!=null&&atcycleFeeitems.size()>0){
                    for (LendProductFeesItem item : atcycleFeeitems) {
                        BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                        fees2 = fees2.add(fee);
                    }
                    fees2 =  BigDecimalUtil.up(fees2, 2);
                }
                BigDecimal fees3 = BigDecimal.ZERO;
                if(fis!=null&&fis.size()>0){
                    for(FeesItem fi:fis){
                        BigDecimal fee = feesItemService.calculateFeesBalance(fi.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
                                detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                        fees3 = fees3.add(fee);
                    }
                    fees3 = BigDecimalUtil.up(fees3, 2);
                }
                
                //计算罚息实还金额
                List<LoanApplicationFeesItem> loanFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(vo.getLoanApplicationId());
                for(LoanApplicationFeesItem fees :loanFeesItems ){
                	if(fees.getItemType().equals(FeesItemTypeEnum.ITEMTYPE_DEFAULTINTREST.getValue())){
                		RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
                		BigDecimal defaultInterestPaidAll = defaultInterestDetailService.getDefaultInterestPaidByRepaymentPlanId(detail.getRepaymentPlanId());
                		if(defaultInterestPaidAll!=null && defaultInterestPaidAll.compareTo(BigDecimal.ZERO) == 1 ){
	                		BigDecimal newPaid = defaultInterestPaidAll.multiply(BigDecimal.ONE.subtract(fees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
	                				BigDecimal.ROUND_CEILING)));
	                		detail.setDepalFine(BigDecimalUtil.down(newPaid, 2));
	                		defaultInterestFees = fees;
                		}
                	}
                }
       		 
                BigDecimal defaultInterestAll = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(detail.getRepaymentPlanId());
                BigDecimal defaultInterest = BigDecimal.ZERO ;
                
                if(defaultInterestFees != null && defaultInterestAll != null && !defaultInterestAll.equals(BigDecimal.ZERO)) { 
                	RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
                	//个人罚息补偿：罚息总额*（1-平台占比）*出借债权出资占比
                	defaultInterest = defaultInterestAll.multiply(BigDecimal.ONE.subtract(defaultInterestFees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
            				BigDecimal.ROUND_CEILING)));
                	defaultInterest =  BigDecimalUtil.down(defaultInterest, 2);
                }
                
                detail.setShouldFee(fees1.add(fees2).add(fees3));
                detail.setDefaultInterest(defaultInterest);
                if(!detail.getFactBalance().equals(BigDecimal.ZERO)) {
                	detail.setFactBalance(BigDecimalUtil.down(detail.getFactBalance(), 2));
                }
                
            }
			if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
				vo.getLoanApplicationListVO().setCycleCount(detailRightsList.size());
			}
            
            //获取投标类待回款(利息+本金)
            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
            vo.setRightsRepaymentDetailList(detailRightsList);
            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
            //计算预期收益
            vo.setExpectProfit(exceptProfit);
            
			// 债权转让按钮显示/隐藏
			boolean rightBtn = true;
			Long termDay = repaymentPlanService.getTermDay(detailRightsList);
			if (termDay < 0) {
				rightBtn = false;
			}
			if (rightBtn) {
				// 债权生成时间是否大于30天
				int betweenDate = DateUtil.daysBetween(vo.getCreateTime(), new Date());
				if (betweenDate <= 30) {
					rightBtn = false;
				}
			}
			if (rightBtn) {
				// 判断是否逾期
				List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlanByLoanAppIdAndState(vo.getLoanApplicationId(),
						RepaymentPlanStateEnum.DEFAULT.getValueChar());
				if (repaymentPlanList.size() != 0) {
					rightBtn = false;
				}
			}
			vo.setRightsBtn(rightBtn);
			
			if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
				Map<String, Object> paramsApply = new HashMap<String, Object>();
				paramsApply.put("carryCrId", vo.getCreditorRightsId());
				paramsApply.put("status", CreditorRightsTransferDetailStatus.SUCCESS.getValue());
				CreditorRightsDealDetail crta = creditorRightsTransferAppService.getCreditorRightsDealDetailByParam(paramsApply);
				vo.setCreditorRightsApplyId(crta == null ? null : crta.getCreditorRightsApplyId());
			}

			//设置奖励订单中间表信息
			vo.setRateList(rateMap.get(vo.getCreditorRightsId()));
			String rateValue2="",rateValue="";
			if(vo.getRateList()!=null&&vo.getRateList().size()>0){
				for (RateLendOrderVO lo : vo.getRateList()) {
					if(lo.getRateType().equals("1")){
						rateValue2="<i class='borlisdeta2'>券</i>您已使用"+lo.getRateValue().toString()+"%加息券";
					}
					if(lo.getRateType().equals("2")){
						rateValue="<i class='borlisdeta2'>庆</i>一周年庆加息"+lo.getRateValue().toString()+"%";
					}
				}
			}
			vo.setRateValue(rateValue);
			vo.setRateValue2(rateValue2);
			
			//将js逻辑中的处理逻辑放在java代码中
			//提前还款 当期标记
			int tag = 0;
			//后期累计回款本金
			BigDecimal ljAmount = new BigDecimal(0);
			List<RightsRepaymentDetail> list=vo.getRightsRepaymentDetailList();
			for (int j = 0; j < list.size(); j++) {
				if ((list.get(j).getRightsDetailState() + "").equals("4")) {
					ljAmount = ljAmount.add(list.get(j).getShouldCapital2());
				} else {
					tag = j;
				}
			}
			Object[] ylist=new Object[list.size()];
			for (int j = 0; j < list.size(); j++) {
				RightsRepaymentDetail detail = list.get(j);
				BigDecimal capital = detail.getShouldCapital2();
				BigDecimal interest = detail.getShouldInterest2();
				BigDecimal dInterest = detail.getDefaultInterest();
				Object fee = detail.getShouldFee();
              if((detail.getRightsDetailState()+"").equals("4")){
                  fee = "---";
              }
              BigDecimal allBackMoney =  detail.getShouldCapital2().add(detail.getShouldInterest2().add(detail.getDefaultInterest()));
              Object factMoney = detail.getFactBalance().add(detail.getDepalFine());
              if((detail.getRightsDetailState()+"").equals("4")){
                  factMoney = "---";
              }else{
                  if(tag == j){
                      factMoney = detail.getFactBalance().add(ljAmount.add(detail.getDepalFine()));
                  }
              }
              ylist[j] = new Object[]{detail.getSectionCode(),detail.getRepaymentDayPlanned(),capital.toString(),interest.toString(),dInterest.toString(),fee.toString(),allBackMoney.toString(),factMoney.toString(),detail.getRightsDetailState()+"","index.do?financeId=1"};
			}
			vo.setYlist(ylist);
        }
		return lenderList;
	}

    @Override
    public Pagination<CreditorRightsExtVo> getCreditorRightsPaging(int pageNo, int pageSize, CreditorRightsExtVo creditorRights, Map<String, Object> customParams) {
        Pagination<CreditorRightsExtVo> re = new Pagination<CreditorRightsExtVo>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("creditorRights", creditorRights);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getCreditorRightsPaging", params);
        //获得债券信息
        List<CreditorRightsExtVo> lenderList = this.myBatisDao.getListForPaging("getCreditorRightsPaging", params, pageNo, pageSize);
        
      //获取有债权信息的奖励中间表
        List<RateLendOrderVO> rateOrderList = rateLendOrderService.getRateLendOrderCreditByUserId(creditorRights.getLendUserId());
        Map<Long,List<RateLendOrderVO>> rateMap = new HashMap<>();
        for(RateLendOrderVO rateVO:rateOrderList){
        	if(rateMap.get(rateVO.getCreditRightsId()) == null || rateMap.get(rateVO.getCreditRightsId()).size()==0){
        		List<RateLendOrderVO> list = new ArrayList<>();
        		list.add(rateVO);
        		rateMap.put(rateVO.getCreditRightsId(),  list);
        	}else{
        		rateMap.get(rateVO.getCreditRightsId()).add(rateVO);
        	}
        }
        
        //获得债券明细
        for (CreditorRightsExtVo vo : lenderList) {
        	List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
        	String point=StringUtils.isNull(vo.getAwardPoint())?"":null;
        	if(point==null){
        		if(vo.getAwardPoint().equals("1")){
        			point=AwardPointEnum.ATMAKELOAN.getDesc();
        		}else if(vo.getAwardPoint().equals("2")){
        			point=AwardPointEnum.ATREPAYMENT.getDesc();
        		}else{
        			point=AwardPointEnum.ATCOMPLETE.getDesc();
        		}
        	}
        	vo.setAwardPoint(point);
        	vo.setAwardRate(vo.getAwardRate()!=null&&!vo.getAwardRate().equals("")&&!vo.getAwardRate().equals("0")?"+"+vo.getAwardRate()+"%":"");
            vo.setCreditorRightsCode(PropertiesUtils.getInstance().get("AGREEMENT_VISIT_PATH") + vo.getCreditorRightsCode());
            //获得借款申请信息
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(vo.getLoanApplicationId());
            //计算预期收益
            LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
//            try {
//                BigDecimal expectProfit = InterestCalculation.getAllInterest(vo.getBuyPrice(), vo.getAnnualRate(), loanProduct.getDueTimeType(), loanApplicationListVO.getRepayMentMethod().charAt(0), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), detailRightsList.size(), loanProduct.getCycleValue());
//
//                vo.setExpectProfit(expectProfit);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            //处理还款方式
            if (loanApplicationListVO.getRepayMethod().equals("1")) {
                loanApplicationListVO.setRepayMentMethod(PaymentMethodEnum.getPaymentMethod(loanApplicationListVO.getRepayMentMethod()).getDesc());
            } else {
                ConstantDefine repaymentMode = constantDefineCached.getConstantByValue("repaymentMode", loanApplicationListVO.getRepayMethod());
                loanApplicationListVO.setRepayMentMethod(repaymentMode.getConstantName());
            }
            vo.setLoanApplicationListVO(loanApplicationListVO);
            //获得出借产品所有费用项
            List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(vo.getLendOrderId());
            //需要计算回款时的费用项（周期费用）
            List<LendProductFeesItem> atcycleFeeitems = new ArrayList<LendProductFeesItem>();
            //需要计算回款时的费用项（到期费用）
            List<LendProductFeesItem> complateFeeitems = new ArrayList<LendProductFeesItem>();
            
            LoanApplicationFeesItem defaultInterestFees = null ;
            for (LendProductFeesItem feesItem : feeitems) {
                if (FeesPointEnum.ATCYCLE.value2Char() == feesItem.getChargePoint()) {
                    atcycleFeeitems.add(feesItem);
                }

                if (FeesPointEnum.ATCOMPLETE.value2Char() == feesItem.getChargePoint()) {
                    complateFeeitems.add(feesItem);
                }
                
            }
            //获得费用
            LendOrder lendOrder = lendOrderService.findById(vo.getLendOrderId());
            List<LendLoanBinding> byLendAndLoan = lendLoanBindingService.findByLendAndLoan(lendOrder.getLendProductId(), loanProduct.getLoanProductId());
            List<FeesItem> fis = new ArrayList<FeesItem>();
            if(byLendAndLoan!=null){
                for(LendLoanBinding llb:byLendAndLoan){
                    if (llb.getFeesItemId() != null && llb.getFeesItemId() > 0) {
                        fis.add(feesItemService.findById(llb.getFeesItemId()));
                    }

                }
            }
            
            //最近还款日期
            try {
                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("最近还款日期计算出错", e);
            }
            //处理一下明细，添加待缴费用
            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
            for (RightsRepaymentDetail detail : detailRightsList) {

                //计算到期费用
                BigDecimal fees1 = BigDecimal.ZERO;
                if (detail.getSectionCode()==detailRightsList.size()){
                    if (complateFeeitems!=null&&complateFeeitems.size()>0){
                        for (LendProductFeesItem item : complateFeeitems) {
                            BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                            fees1 = fees1.add(fee);
                        }
                        fees1 =  BigDecimalUtil.up(fees1, 2);
                    }
                }
                //计算周期费用
                BigDecimal fees2 = BigDecimal.ZERO;
                if (atcycleFeeitems!=null&&atcycleFeeitems.size()>0){
                    for (LendProductFeesItem item : atcycleFeeitems) {
                        BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                        fees2 = fees2.add(fee);
                    }
                    fees2 =  BigDecimalUtil.up(fees2, 2);
                }
                BigDecimal fees3 = BigDecimal.ZERO;
                if(fis!=null&&fis.size()>0){
                    for(FeesItem fi:fis){
                        BigDecimal fee = feesItemService.calculateFeesBalance(fi.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
                                detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                        fees3 = fees3.add(fee);
                    }
                    fees3 = BigDecimalUtil.up(fees3, 2);
                }
                
                //计算罚息实还金额
                List<LoanApplicationFeesItem> loanFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(vo.getLoanApplicationId());
                for(LoanApplicationFeesItem fees :loanFeesItems ){
                	if(fees.getItemType().equals(FeesItemTypeEnum.ITEMTYPE_DEFAULTINTREST.getValue())){
                		RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
                		BigDecimal defaultInterestPaidAll = defaultInterestDetailService.getDefaultInterestPaidByRepaymentPlanId(detail.getRepaymentPlanId());
                		if(defaultInterestPaidAll!=null && defaultInterestPaidAll.compareTo(BigDecimal.ZERO) == 1 ){
	                		BigDecimal newPaid = defaultInterestPaidAll.multiply(BigDecimal.ONE.subtract(fees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
	                				BigDecimal.ROUND_CEILING)));
	                		detail.setDepalFine(BigDecimalUtil.down(newPaid, 2));
	                		defaultInterestFees = fees;
                		}
                	}
                }
       		 
                BigDecimal defaultInterestAll = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(detail.getRepaymentPlanId());
                BigDecimal defaultInterest = BigDecimal.ZERO ;
                
                if(defaultInterestFees != null && defaultInterestAll != null && !defaultInterestAll.equals(BigDecimal.ZERO)) { 
                	RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
                	//个人罚息补偿：罚息总额*（1-平台占比）*出借债权出资占比
                	defaultInterest = defaultInterestAll.multiply(BigDecimal.ONE.subtract(defaultInterestFees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
            				BigDecimal.ROUND_CEILING)));
                	defaultInterest =  BigDecimalUtil.down(defaultInterest, 2);
//                	fees3 = fees3.add(defaultInterest);
                }
                
                detail.setShouldFee(fees1.add(fees2).add(fees3));
                detail.setDefaultInterest(defaultInterest);
                if(!detail.getFactBalance().equals(BigDecimal.ZERO)) {
                	detail.setFactBalance(BigDecimalUtil.down(detail.getFactBalance(), 2));
                }
                
            }
			if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
				vo.getLoanApplicationListVO().setCycleCount(detailRightsList.size());
			}
            
            //获取投标类待回款(利息+本金)
            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
            vo.setRightsRepaymentDetailList(detailRightsList);
            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
            //计算预期收益
            vo.setExpectProfit(exceptProfit);
            
			// 债权转让按钮显示/隐藏
			boolean rightBtn = true;
			Long termDay = repaymentPlanService.getTermDay(detailRightsList);
			if (termDay < 0) {
				rightBtn = false;
			}
			if (rightBtn) {
				// 债权生成时间是否大于30天
				int betweenDate = DateUtil.daysBetween(vo.getCreateTime(), new Date());
				if (betweenDate <= 30) {
					rightBtn = false;
				}
			}
			if (rightBtn) {
				// 判断是否逾期
				List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlanByLoanAppIdAndState(vo.getLoanApplicationId(),
						RepaymentPlanStateEnum.DEFAULT.getValueChar());
				if (repaymentPlanList.size() != 0) {
					rightBtn = false;
				}
			}
			vo.setRightsBtn(rightBtn);
			
			if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
				Map<String, Object> paramsApply = new HashMap<String, Object>();
				paramsApply.put("carryCrId", vo.getCreditorRightsId());
				paramsApply.put("status", CreditorRightsTransferDetailStatus.SUCCESS.getValue());
				CreditorRightsDealDetail crta = creditorRightsTransferAppService.getCreditorRightsDealDetailByParam(paramsApply);
				vo.setCreditorRightsApplyId(crta == null ? null : crta.getCreditorRightsApplyId());
			}

			//设置奖励订单中间表信息
			vo.setRateList(rateMap.get(vo.getCreditorRightsId()));
        }
    	re.setRows(lenderList);
    	re.setTotal(totalCount);

        return re;
    }
    
    /**
     * 和当前日期比对
     *
     * @param detailRightsList
     * @return
     */
    public Date getRecentPayDate(List<RightsRepaymentDetail> detailRightsList) throws ParseException {
        Date date = new Date();
        //一期
        if (detailRightsList.size()==1)
            return detailRightsList.get(0).getRepaymentDayPlanned();
        //多期
        for (int i = 0; i < detailRightsList.size(); i++) {
            Date repaymentDayPlanned = detailRightsList.get(i).getRepaymentDayPlanned();

            if (i == 0) {
                int i1 = DateUtil.secondBetween(date, repaymentDayPlanned);
                if (i1 > 0) {
                    //当前时间没有到达第一期还款时间
                    return repaymentDayPlanned;
                }
            } else if (i == (detailRightsList.size() - 1)) {
                //最后一次还款
                return repaymentDayPlanned;
            } else {
                int i3 = DateUtil.secondBetween(date, repaymentDayPlanned);
                if (i3 > 0) {
                    //当前时间没有达到当前期
                    return repaymentDayPlanned;
                }
            }
        }
        return null;
    }

    @Override
    public Pagination<LenderRecordVO> findLendOrderDetailPaging(int pageNo, int pageSize, int pageNo1, int pageSize1, Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective) {
        Pagination<LenderRecordVO> re = new Pagination<LenderRecordVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        List<String> statusList = new ArrayList<String>();
        if (effective == null || effective.length == 0) {
            statusList = null;
        } else {
            for (CreditorRightsConstants.CreditorRightsStateEnum status : effective) {
                statusList.add(status.getValue());
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("statusList", statusList);

        int totalCount = this.myBatisDao.count("getLendOrderDetailPaging", params);
        List<LenderRecordVO> users = this.myBatisDao.getListForPaging("getLendOrderDetailPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(users);

        return re;
    }
    
    /**
     * 获取投资记录数量
     * @param loanApplicationId
     * @param effective
     * @return
     */
    @Override
    public Integer findLendOrderDetailCount(Long loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum... effective) {
        List<String> statusList = new ArrayList<String>();
        if (effective == null || effective.length == 0) {
            statusList = null;
        } else {
            for (CreditorRightsConstants.CreditorRightsStateEnum status : effective) {
                statusList.add(status.getValue());
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("statusList", statusList);

        int totalCount = this.myBatisDao.count("getLendOrderDetailPaging", params);
        return totalCount;
    }
    
    /**
     * 根据债权ID，获取一条扩展信息
     */
	@Override
	public CreditorRightsExtVo getCreditorRightsDetailById(Long creditorRightsId) {
		return myBatisDao.get("CREDITORRIGHTS.getCreditorRightsDetailById", creditorRightsId);
	}

	@Override
    public List<CreditorRights> getByLoanApplicationId(long loanApplicationId, ChannelTypeEnum... channelTypeEnum) {
        Map map = new HashMap();
        map.put("loanApplicationId", loanApplicationId);
        List<String> nums=null ;
        if(channelTypeEnum != null && channelTypeEnum.length > 0){
        	nums = new ArrayList<String>();
        }
        for (ChannelTypeEnum ce : channelTypeEnum) {
        	nums.add(ce.getValue());
		}
    	map.put("channelTypeEnum", nums);
        return myBatisDao.getList("CREDITORRIGHTS.getByLoanApplicationId1", map);
    }
    @Override
    public BigDecimal calSurplusPrice(Long creditorRightsId, boolean calculateDayInterest) {
        BigDecimal result = BigDecimal.ZERO;

        List<RightsRepaymentDetail> detailListByRightsId = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
        CreditorRights creditorRights = findById(creditorRightsId);
        int index = 1;
        Date preRepayDate = null;BigDecimal dayInterest;Date now = new Date();BigDecimal needPayAmount = null;
        MathContext mathContext = new MathContext(2, RoundingMode.DOWN);
        /**
         * 这里是要根据每一期未还清的债权还款明细记录计算代收总额，这个总额就是计算出来的剩余价格
         */
        for (RightsRepaymentDetail rightsRepaymentDetail : detailListByRightsId) {
            if (CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailStateEnum.UNCOMPLETE.value2Char() == rightsRepaymentDetail.getRightsDetailState()
                    || CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailStateEnum.PART.value2Char() == rightsRepaymentDetail.getRightsDetailState()
                    || CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailStateEnum.DEFAULT.value2Char() == rightsRepaymentDetail.getRightsDetailState()) {
                result = result.add(rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getFactBalance()));
                /**
                 * 如果需要给出让人计算当期已得收益，则需要根据当期日利息、当期所占天数、已获利息算出应给出让人的收益
                 */
                if (index == 1 && calculateDayInterest) {
                    //计算当前期还款日与上一期还款日间相差的天数
                    if (preRepayDate == null)
                        preRepayDate = loanApplicationService.findById(creditorRights.getLoanApplicationId()).getPaymentDate();
                    int daysBetween = DateUtil.daysBetween(preRepayDate, rightsRepaymentDetail.getRepaymentDayPlanned());

                    //根据相差天数计算当期还款日利息
                    dayInterest = rightsRepaymentDetail.getShouldInterest2().divide(new BigDecimal(String.valueOf(daysBetween)), mathContext);

                    //计算出让人所占天数
                    int needPayDays = DateUtil.daysBetween(preRepayDate, now);

                    //计算出让人应得收益，计算方法为日利息 * 出让人所占天数 - 出让人已获利息
                    needPayAmount = dayInterest.multiply(new BigDecimal(String.valueOf(needPayDays)), mathContext).subtract(rightsRepaymentDetail.getFactInterest(), mathContext);
                    result = result.add(needPayAmount);
                }
            }
            index++;
            preRepayDate = rightsRepaymentDetail.getRepaymentDayPlanned();
        }

        return result;
    }

    @Override
    public RightsRepaymentDetail getCurrentRpdByCrId(Long creditorRightsId) {
        RightsRepaymentDetail result = null;

        Date date = new Date();
        List<RightsRepaymentDetail> repaymentDetails = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
        for (RightsRepaymentDetail rightsRepaymentDetail : repaymentDetails) {
            if (rightsRepaymentDetail.getRepaymentDayPlanned().compareTo(date) > 0) {
                result = rightsRepaymentDetail;
                break;
            }
        }

        return result;
    }

    @Override
    public RightsRepaymentDetail getNearestNeedRepayRpdByCrId(Long creditorRightsId) {
        RightsRepaymentDetail result = null;

        List<RightsRepaymentDetail> repaymentDetails = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
        for (RightsRepaymentDetail rightsRepaymentDetail : repaymentDetails) {
            if (CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailIsPayOffEnum.ISPAYOFF_NO.value2Char() == rightsRepaymentDetail.getIsPayOff()) {
                result = rightsRepaymentDetail;
                break;
            }
        }

        return result;
    }

    @Override
    public RightsRepaymentDetail getRelevantRpdByCrIdAndDate(Long creditorRightsId, Date date) {
        RightsRepaymentDetail result = null;

        List<RightsRepaymentDetail> repaymentDetails = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
        for (RightsRepaymentDetail rightsRepaymentDetail : repaymentDetails) {
            if (rightsRepaymentDetail.getRepaymentDayPlanned().compareTo(date) > 0) {
                result = rightsRepaymentDetail;
                break;
            }
        }

        return result;
    }

	@Override
	@Transactional
	public void turnCreditor(BigDecimal rightAcount, Long creditorRightsId, int surpMonth) {
        CreditorRights creditorRights = this.findById(creditorRightsId, true);
        if(creditorRights == null)
        	throw new SystemException(CreditorErrorCode.CREDITOR_TURNAPPLY_NOT_EXIST).set("creditorRights", creditorRights);
        if(creditorRights.getRightsState() != CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char()){
            throw new SystemException(CreditorErrorCode.CREDITORCANTTURN).set("status",creditorRights.getRightsState());
        }
		int betweenDate = DateUtil.daysBetween(creditorRights.getCreateTime(), new Date());
		if (betweenDate < 30) {
			throw new SystemException(CreditorErrorCode.CREDITORCANTTURN).set("betweendate", betweenDate);
		}
		List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlanByLoanAppIdAndState(creditorRights.getLoanApplicationId(),
				RepaymentPlanStateEnum.DEFAULT.getValueChar());
		if (repaymentPlanList.size() != 0) {
			throw new SystemException(CreditorErrorCode.CREDITORCANTTURN).set("default", RepaymentPlanStateEnum.DEFAULT.getDesc());
		}
        creditorRights.setCreditorRightsId(creditorRights.getCreditorRightsId());
        creditorRights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.value2Char());
        creditorRights.setTurnState(CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.value2Char());
        creditorRights.setApplyRollOutTime(new Date());
        this.update(creditorRights);
		CreditorRightsTransferApplication creditorRightsTransferApplication = new CreditorRightsTransferApplication();
		creditorRightsTransferApplication.setApplyCrId(creditorRights.getCreditorRightsId());
		creditorRightsTransferApplication.setApplyUserId(creditorRights.getLendUserId());
		creditorRightsTransferApplication.setApplyPrice(rightAcount);
		creditorRightsTransferApplication.setApplyTime(new Date());
		creditorRightsTransferApplication.setWhenWorth(creditorRights.getRightsWorth());
		creditorRightsTransferApplication.setTimeLimit(surpMonth);
		creditorRightsTransferApplication.setTransType(CreditorRightsTransferAppType.MANUAL.getValue());
		creditorRightsTransferApplication.setBusStatus(CreditorRightsTransferAppStatus.TRANSFERRING.getValue());
		creditorRights.setRightsState(CreditorRightsStateEnum.APPLYTURNOUT.getValue().charAt(0));
		creditorRightsTransferAppService.addCreditorRightsTransferApplication(creditorRightsTransferApplication);
	}
	
	@Override
	@Transactional
	public void turnCreditor(CreditorRightsTransferApplication applyRecord) throws Exception {
        List<CreditorRightsDealDetail> creditorRightsDealDetails = creditorRightsTransferAppService.getCreditorRightsDealDetailByTransferApplyId(applyRecord.getCreditorRightsApplyId(),CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus.SUCCESS);
        List<CreditorRights> newCreditorRights = new ArrayList<>();
            //检查债权是否可转让
            Date date = new Date();
            //转让
            CreditorRights oldRights = this.findById(applyRecord.getApplyCrId(),false);
            if (oldRights == null)
                return;
            List<RightsRepaymentDetail> rightsRepaymentDetails = rightsRepaymentDetailService.getDetailListByRightsId(oldRights.getCreditorRightsId());
            int repaymnetIndex = 0;
            Date nextRepaymentDate = null;
            for (RightsRepaymentDetail detail : rightsRepaymentDetails) {
                repaymnetIndex++;
                //未还清
                if (detail.getIsPayOff() != RightsRepaymentDetail.ISPAYOFF_NO) {
                    continue;
                }

                if (DateUtil.before(detail.getRepaymentDayPlanned(), date, DateUtil.PATTERNTYPE_DATE)) {
                    nextRepaymentDate = detail.getRepaymentDayPlanned(); //逾期
                } else {
                    RightsRepaymentDetail theLastRightsRepaymentDetail = rightsRepaymentDetails.get(repaymnetIndex - 1);
                    nextRepaymentDate = theLastRightsRepaymentDetail.getRepaymentDayPlanned();//下期还款日
                }
                break;
            }

            //创建新债权
            int lendOrderIndex = 1;
            Map<Long,Map<String,BigDecimal>> map = new HashMap<Long,Map<String,BigDecimal>>();
            AccountValueChangedQueue avcq = new AccountValueChangedQueue();
            
            // 占比
            BigDecimal sumProportion = new BigDecimal("0");
            BigDecimal oldProportion = rightsRepaymentDetails.get(0).getProportion();
             
            for (CreditorRightsDealDetail creditorRightsDealDetail:creditorRightsDealDetails){

                LoanApplication loanApplication = loanApplicationService.findById(oldRights.getLoanApplicationId());
                //新债权
                CreditorRights newRights = new CreditorRights();
                //新订单
                LendOrder newLendOrder = lendOrderService.findById(creditorRightsDealDetail.getLendOrderId());
                UserAccount userAccount = userAccountService.getUserAccountByAccId(newLendOrder.getCustomerAccountId());
                UserInfoVO newLenderUser = userInfoService.getUserExtByUserId(newLendOrder.getLendUserId());
                BeanUtils.copyProperties(oldRights, newRights);

                newRights.setLendOrderId(newLendOrder.getLendOrderId());
                newRights.setAgreementStartDate(date);
                newRights.setRightsWorth(creditorRightsDealDetail.getBuyWorth());
                newRights.setLendPrice(creditorRightsDealDetail.getBuyWorth().setScale(2, BigDecimal.ROUND_UP));
                newRights.setBuyPrice(creditorRightsDealDetail.getBuyBalance());
                newRights.setLendTime(date);
                newRights.setCreateTime(date);
                newRights.setFirstRepaymentDate(nextRepaymentDate);
                char newLenderType = CreditorRights.LENDERTYPE_LENDER;
                newRights.setLenderType(newLenderType);
                newRights.setLendSystemLoginName(newLenderUser.getLoginName());
                newRights.setLendCustomerName(newLenderUser.getRealName());
                newRights.setLendCustomerCerCode(newLenderUser.getIdCard());
                newRights.setLendUserId(newLenderUser.getUserId());
                String rightsCode = createRightsCode(CreditorRightsFromWhereEnum.TURN.value2Char(), newLendOrder.getOrderCode(), loanApplication.getLoanApplicationCode());
                newRights.setCreditorRightsCode(rightsCode);
                newRights.setLendAccountId(newLendOrder.getCustomerAccountId());
                newRights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char());
                newRights.setFromWhere(CreditorRightsFromWhereEnum.TURN.value2Char());
                newRights.setTurnState(CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char());
                newRights.setLoanApplicationId(loanApplication.getLoanApplicationId());
                if (userAccount.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
                    newRights.setDisplayState(DisplayEnum.HIDDEN.value2Char());
                }else{
                    newRights.setDisplayState(DisplayEnum.DISPLAY.value2Char());
                }
                //生成新的债权
                insert(newRights);
                newCreditorRights.add(newRights);

                //保存债权变更历史
                CreditorRightsHistory creditorRightsHistory = new CreditorRightsHistory();
                creditorRightsHistory.setCreditorRightsIdBeforeChange(oldRights.getCreditorRightsId());
                creditorRightsHistory.setCreditorRightsIdAfterChange(newRights.getCreditorRightsId());
                creditorRightsHistory.setChangeType(CreditorRightsHistory.CHANGETYPE_TURNOUT);
                creditorRightsHistory.setChangeTime(date);
                newCreditorRightsHistory(creditorRightsHistory);

                //新增新债权分配明细
                Map<Integer, Map<String, BigDecimal>> repayInfo = null;
                BigDecimal annualRate = loanApplication.getAnnualRate();
                LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
                char dueTimeType = loanProduct.getDueTimeType();
                char repaymentMethod = loanProduct.getRepaymentMethod();
                char repaymentType = loanProduct.getRepaymentType();
                char repaymentCycle = loanProduct.getRepaymentCycle();
                Integer dueTime = loanProduct.getDueTime();
                Integer cycleValue = loanProduct.getCycleValue();
                Integer complateValue = 0;
                
				List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplication.getLoanApplicationId(), ChannelTypeEnum.ONLINE);
				BigDecimal shouldAllCapital = loanApplication.getConfirmBalance();
				BigDecimal factAllCapital = BigDecimal.ZERO;
				for (RepaymentPlan repaymentPlan : repaymentPlanList) {
					if (repaymentPlan.getPlanState() == RepaymentPlanStateEnum.COMPLETE.value2Char()
							|| repaymentPlan.getPlanState() == RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char()
							|| repaymentPlan.getPlanState() == RepaymentPlanStateEnum.PART.value2Char()) {
						complateValue = complateValue + 1;
						factAllCapital = factAllCapital.add(repaymentPlan.getFactBalance());
					}
				}

                int planIndex = 0;
                BigDecimal rightsBalance = BigDecimal.ZERO;//已有债权金额
                BigDecimal rightsInterest = BigDecimal.ZERO;//已有债权利息
                BigDecimal rightsCapital = BigDecimal.ZERO;//已有债权本金

                BigDecimal rightsBalance2 = BigDecimal.ZERO;//已有债权金额（2位）
                BigDecimal rightsInterest2 = BigDecimal.ZERO;//已有债权利息（2位）
                BigDecimal rightsCapital2 = BigDecimal.ZERO;//已有债权本金（2位）

                List<Integer> sectionCode = new ArrayList<Integer>();
                BigDecimal lendBalance = newRights.getRightsWorth();
                repayInfo = InterestCalculation.getCalitalAndInterest(lendBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime-complateValue, cycleValue);
                BigDecimal allInterest = InterestCalculation.getAllInterest(lendBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime-complateValue, cycleValue);
                Map<String,BigDecimal> detailMap = map.get(oldRights.getCreditorRightsId());
                if (detailMap==null){
                    detailMap = new HashMap<String,BigDecimal>();
                    map.put(oldRights.getCreditorRightsId(),detailMap);
                }
                
                BigDecimal theRatio = null;
                if (lendOrderIndex == creditorRightsDealDetails.size()) {
					theRatio = oldProportion.subtract(sumProportion);
				} else {
					theRatio = newLendOrder.getBuyBalance().divide(loanApplication.getConfirmBalance(), 4, BigDecimal.ROUND_DOWN);
					theRatio = BigDecimalUtil.down(theRatio, 4).multiply(new BigDecimal("100"));
				}
				sumProportion = sumProportion.add(theRatio);

                for (RepaymentPlan repaymentPlan : repaymentPlanList) {

                    if (repaymentPlan.getPlanState() != RepaymentPlanStateEnum.COMPLETE.value2Char()&&repaymentPlan.getPlanState() != RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char()){
                        RightsRepaymentDetail rightsRepaymentDetail = new RightsRepaymentDetail();
                        rightsRepaymentDetail.setCreditorRightsId(newRights.getCreditorRightsId());
                        rightsRepaymentDetail.setRepaymentPlanId(repaymentPlan.getRepaymentPlanId());
                        rightsRepaymentDetail.setLendOrderId(newRights.getLendOrderId());
                        rightsRepaymentDetail.setLoanApplicationId(loanApplication.getLoanApplicationId());
                        rightsRepaymentDetail.setLoanAccountId(loanApplication.getRepaymentAccountId());
                        rightsRepaymentDetail.setLendAccountId(newLendOrder.getCustomerAccountId());
                        rightsRepaymentDetail.setSectionCode(repaymentPlan.getSectionCode());
						rightsRepaymentDetail.setProportion(theRatio);
						rightsRepaymentDetail.setRepaymentDayPlanned(repaymentPlan.getRepaymentDay());

                        if (repaymentPlan.getPlanState() == RepaymentPlanStateEnum.PART.value2Char()){

                            BigDecimal turnRatio = creditorRightsDealDetail.getBuyWorth().divide(oldRights.getRightsWorth(), 18, BigDecimal.ROUND_CEILING);
                            RightsRepaymentDetail detail = rightsRepaymentDetails.get(repaymnetIndex - 1);
                            BigDecimal sumBalance = detailMap.get("b" + rightsRepaymentDetail.getSectionCode());
                            if(sumBalance==null)
                                sumBalance = BigDecimal.ZERO;
                            BigDecimal sumInterest = detailMap.get("i" + rightsRepaymentDetail.getSectionCode());
                            if(sumInterest==null)
                                sumInterest = BigDecimal.ZERO;

                            BigDecimal sumInterest2 = detailMap.get("i2" + rightsRepaymentDetail.getSectionCode());
                            if(sumInterest2==null)
                                sumInterest2 = BigDecimal.ZERO;
                            BigDecimal sumBalance2 = detailMap.get("b2" + rightsRepaymentDetail.getSectionCode());
                            if(sumBalance2==null)
                                sumBalance2 = BigDecimal.ZERO;
                            if (lendOrderIndex < creditorRightsDealDetails.size()) {
                                rightsRepaymentDetail.setShouldInterest(detail.getShouldInterest().subtract(detail.getFactInterest()).multiply(turnRatio));
                                rightsRepaymentDetail.setShouldBalance(detail.getShouldBalance().subtract(detail.getFactBalance()).multiply(turnRatio));
                                rightsRepaymentDetail.setShouldCapital(rightsRepaymentDetail.getShouldBalance().subtract(rightsRepaymentDetail.getShouldInterest()));

                                rightsRepaymentDetail.setShouldBalance2(BigDecimalUtil.down(rightsRepaymentDetail.getShouldBalance(), 2));
                                rightsRepaymentDetail.setShouldInterest2(BigDecimalUtil.down(rightsRepaymentDetail.getShouldInterest(), 2));
                                rightsRepaymentDetail.setShouldCapital2(rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getShouldInterest2()));

                            } else {
                                //最后一个订单做减法
                                rightsRepaymentDetail.setShouldBalance(detail.getShouldBalance().subtract(detail.getFactBalance()).subtract(sumBalance));
                                rightsRepaymentDetail.setShouldInterest(detail.getShouldInterest().subtract(detail.getFactInterest()).subtract(sumInterest));
                                rightsRepaymentDetail.setShouldCapital(rightsRepaymentDetail.getShouldBalance().subtract(rightsRepaymentDetail.getShouldInterest()));

                                rightsRepaymentDetail.setShouldBalance2(detail.getShouldBalance2().subtract(detail.getFactBalance()).subtract(sumBalance2));
                                rightsRepaymentDetail.setShouldInterest2(detail.getShouldInterest2().subtract(detail.getFactInterest()).subtract(sumInterest2));
                                rightsRepaymentDetail.setShouldCapital2(rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getShouldInterest2()));
                            }

                            detailMap.put("b"+rightsRepaymentDetail.getSectionCode(),rightsRepaymentDetail.getShouldBalance().add(sumBalance));
                            detailMap.put("i"+rightsRepaymentDetail.getSectionCode(),rightsRepaymentDetail.getShouldInterest().add(sumInterest));

                            detailMap.put("i2"+rightsRepaymentDetail.getSectionCode(),rightsRepaymentDetail.getShouldInterest2().add(sumInterest2));
                            detailMap.put("b2"+rightsRepaymentDetail.getSectionCode(),rightsRepaymentDetail.getShouldBalance2().add(sumBalance2));

                            lendBalance = newRights.getRightsWorth().subtract(rightsRepaymentDetail.getShouldCapital2());
                            repayInfo = InterestCalculation.getCalitalAndInterest(lendBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime-complateValue, cycleValue);
                            allInterest = InterestCalculation.getAllInterest(lendBalance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime-complateValue, cycleValue);

                        }else{
                            //当前还款计划 是否是最后一期还款
                            if (planIndex == repaymentPlanList.size() - 1) {
                                //18位
                                rightsRepaymentDetail.setShouldBalance(lendBalance.add(allInterest).subtract(rightsBalance));
                                rightsRepaymentDetail.setShouldCapital(lendBalance.subtract(rightsCapital));
                                rightsRepaymentDetail.setShouldInterest(allInterest.subtract(rightsInterest));
                                //2位
                                rightsRepaymentDetail.setShouldBalance2(lendBalance.add(BigDecimalUtil.down(allInterest, 2)).subtract(rightsBalance2));
                                rightsRepaymentDetail.setShouldCapital2(lendBalance.subtract(rightsCapital2));
                                rightsRepaymentDetail.setShouldInterest2(rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getShouldCapital2()));
                            } else {
                                Map repaymentDecimal = repayInfo.get(repaymentPlan.getSectionCode()-complateValue);
                                //18位
                                rightsRepaymentDetail.setShouldBalance((BigDecimal) repaymentDecimal.get("balance"));
                                rightsRepaymentDetail.setShouldCapital((BigDecimal) repaymentDecimal.get("calital"));
                                rightsRepaymentDetail.setShouldInterest((BigDecimal) repaymentDecimal.get("interest"));
                                //记录18位总和
                                rightsBalance = rightsBalance.add(rightsRepaymentDetail.getShouldBalance());
                                rightsInterest = rightsInterest.add(rightsRepaymentDetail.getShouldInterest());
                                rightsCapital = rightsCapital.add(rightsRepaymentDetail.getShouldCapital());

                                //2位
                                rightsRepaymentDetail.setShouldBalance2(BigDecimalUtil.down(rightsRepaymentDetail.getShouldBalance(), 2));
                                rightsRepaymentDetail.setShouldCapital2(BigDecimalUtil.down(rightsRepaymentDetail.getShouldCapital(), 2));
                                rightsRepaymentDetail.setShouldInterest2(rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getShouldCapital2()));
                                //记录2位总和
                                rightsBalance2 = rightsBalance2.add(rightsRepaymentDetail.getShouldBalance2());
                                rightsInterest2 = rightsInterest2.add(rightsRepaymentDetail.getShouldInterest2());
                                rightsCapital2 = rightsCapital2.add(rightsRepaymentDetail.getShouldCapital2());
                            }


                        }
                        rightsRepaymentDetail.setIsDelay(RightsRepaymentDetail.ISDELAY_NO);
                        rightsRepaymentDetail.setIsPayOff(RightsRepaymentDetail.ISPAYOFF_NO);

                        rightsRepaymentDetailService.addRightsRepaymentDetail(rightsRepaymentDetail);

                        //新增回款计划
                        LendOrderReceive lendOrderReceiveDetail = new LendOrderReceive();
                        lendOrderReceiveDetail.setLendOrderId(newLendOrder.getLendOrderId());
                        lendOrderReceiveDetail.setSectionCode(rightsRepaymentDetail.getSectionCode());
                        lendOrderReceiveDetail.setReceiveState(LendOrderReceiveStateEnum.UNRECEIVE.value2Char());

                        Date receiveDate = rightsRepaymentDetail.getRepaymentDayPlanned();

                        lendOrderReceiveDetail.setShouldCapital(rightsRepaymentDetail.getShouldCapital());
                        lendOrderReceiveDetail.setShouldInterest(rightsRepaymentDetail.getShouldInterest());
                        lendOrderReceiveDetail.setShouldCapital2(rightsRepaymentDetail.getShouldCapital2());
                        lendOrderReceiveDetail.setShouldInterest2(rightsRepaymentDetail.getShouldInterest2());

                        lendOrderReceiveDetail.setReceiveDate(receiveDate);
                        lendOrderReceiveService.insert(lendOrderReceiveDetail);

                        //移植奖励
                        for (RightsRepaymentDetail detail : rightsRepaymentDetails) {
                            if ((detail.getSectionCode()-complateValue) == rightsRepaymentDetail.getSectionCode()){
                                AwardDetail awardDetail = awardDetailService.findByRightsRepaymentDetailId(detail.getRightsRepaymentDetailId());
                                if (awardDetail!=null){
                                    AwardDetail newAward = new AwardDetail();
                                    BeanUtils.copyProperties(awardDetail, newAward);

                                    newAward.setRightsRepaymentDetailId(rightsRepaymentDetail.getRightsRepaymentDetailId());
                                    newAward.setLendOrderId(newLendOrder.getLendOrderId());
                                    newAward.setUserAccountId(newLendOrder.getCustomerAccountId());

                                    awardDetailService.insert(newAward);
                                }
                            }
                        }
                        
                        sectionCode.add(repaymentPlan.getSectionCode());
                        
                    }
                    planIndex++;
                }
                
				// 原债权分配明细修改为已转出
				if (lendOrderIndex == creditorRightsDealDetails.size()) {
					// 最后一个明细生成后，修改原债权明细
					Map<String, Object> oldDetailParam = new HashMap<String, Object>();
					oldDetailParam.put("sectionCodeList", sectionCode);
					oldDetailParam.put("creditorRightsId", oldRights.getCreditorRightsId());
					List<RightsRepaymentDetail> details = rightsRepaymentDetailService.findBy(oldDetailParam);
					for (RightsRepaymentDetail detail : details) {
						Map<String, Object> detail1Map = new HashMap<String, Object>();
						detail1Map.put("rightsRepaymentDetailId", detail.getRightsRepaymentDetailId());
						detail1Map.put("rightsDetailState", RightsRepaymentDetail.RIGHTSDETAILSTATE_TURNOUT);
						rightsRepaymentDetailService.update(detail1Map);
					}
	
					// 原债权关联订单回款计划修改状态
					List<LendOrderReceive> oldLendOrderReceive = lendOrderReceiveService.getByLendOrderId(oldRights.getLendOrderId());
					for (LendOrderReceive lor : oldLendOrderReceive) {
						Map<String, Object> receviceMap = new HashMap<String, Object>();
						receviceMap.put("receiveId", lor.getReceiveId());
						receviceMap.put("receiveState", LendOrderReceiveStateEnum.TURNOUT.value2Char());
						lendOrderReceiveService.update(receviceMap);
					}
				}

                //修改转让明细状态
                creditorRightsDealDetail.setStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferDetailStatus.SUCCESS.getValue());
                creditorRightsTransferAppService.update(creditorRightsDealDetail);

                //资金操作
                //处理财富券变现
                BigDecimal voucherVoucher = loanApplicationService.changeVoucherToAmount(newLendOrder, avcq);


                String descUnfreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.CREDITOR_TURNOUT_OUT,creditorRightsDealDetail.getBuyBalance().subtract(voucherVoucher));
                AccountValueChanged avcUnfrozen = new AccountValueChanged(userAccount.getAccId(), creditorRightsDealDetail.getBuyBalance().subtract(voucherVoucher),
                        creditorRightsDealDetail.getBuyBalance().subtract(voucherVoucher), AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BUYRIGHTS.getValue(),
                        "LendOrder", VisiableEnum.DISPLAY.getValue(), newLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                        newLendOrder.getLendOrderId(), date, descUnfreeze, true);
                avcq.addAccountValueChanged(avcUnfrozen);
                String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.CREDITOR_TURNOUT_PAY,creditorRightsDealDetail.getBuyBalance().subtract(voucherVoucher));
                AccountValueChanged avcPay = new AccountValueChanged(userAccount.getAccId(), creditorRightsDealDetail.getBuyBalance().subtract(voucherVoucher),
                        creditorRightsDealDetail.getBuyBalance().subtract(voucherVoucher), AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BUYRIGHTS.getValue(),
                        "LendOrder", VisiableEnum.DISPLAY.getValue(), newLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                        newLendOrder.getLendOrderId(), date, descPay, true);
                avcq.addAccountValueChanged(avcPay);
                logger.debug("新债权状态为生效，因此债权新理财订单账户从冻结金额中支出：" + creditorRightsDealDetail.getBuyBalance());
                if (userAccount.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
                    //省心计划产生订单
                    LendOrder financeOrder = lendOrderService.findById(newLendOrder.getLendOrderPId());
                    Map<String,Object> financeOrderMap = new HashMap<String,Object>();
                    financeOrderMap.put("lendOrderId",financeOrder.getLendOrderId());
                    financeOrderMap.put("forLendBalance",financeOrder.getForLendBalance().subtract(newLendOrder.getBuyBalance()));
                    lendOrderService.update(financeOrderMap);
                }


                //修改订单明细状态
                LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetailService.findByLendOrderId(newLendOrder.getLendOrderId(), LendOrderBidStatusEnum.BIDING).get(0);
                lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.BIDSUCCESS.value2Char());
                lendOrderBidDetail.setCreditorRightsId(newRights.getCreditorRightsId());
                myBatisDao.update("LENDORDERBIDDETAIL.update", lendOrderBidDetail);

                //记录承接债权id
                CreditorRightsDealDetail crdd = new CreditorRightsDealDetail();
                crdd.setRightsTransferDetailId(creditorRightsDealDetail.getRightsTransferDetailId());
                crdd.setCarryCrId(newRights.getCreditorRightsId());
                creditorRightsTransferAppService.update(crdd);


                lendOrderIndex++;
            }
            //原债权已转出
            oldRights.setLendPrice(applyRecord.getApplyPrice());
            oldRights.setRightsWorth(BigDecimal.ZERO);
            oldRights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT.value2Char());
            update(oldRights);
            //转让申请状态改为转让成功
            applyRecord.setBusStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.SUCCESS.getValue());
            creditorRightsTransferAppService.update(applyRecord);
            //转出方资金操作
            LendOrder oldLendOrder = lendOrderService.findById(oldRights.getLendOrderId());
            UserAccount userAccount = userAccountService.getUserAccountByAccId(oldLendOrder.getCustomerAccountId());
            String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.CREDITOR_TURNOUT_INCOME,applyRecord.getApplyPrice());
            AccountValueChanged avcPay = new AccountValueChanged(userAccount.getAccId(), applyRecord.getApplyPrice(),
                    applyRecord.getApplyPrice(), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_TURNRIGHTS.getValue(),
                    "LendOrder", VisiableEnum.DISPLAY.getValue(), oldLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    oldLendOrder.getLendOrderId(), new Date(), descIncome, true);
            avcq.addAccountValueChanged(avcPay);
            if (userAccount.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
                //省心计划产生订单
                Map<String,Object> financeOrderMap = new HashMap<String,Object>();
                financeOrderMap.put("lendOrderId",oldLendOrder.getLendOrderId());
                financeOrderMap.put("forLendBalance",oldLendOrder.getForLendBalance().add(applyRecord.getApplyPrice()));
                lendOrderService.update(financeOrderMap);
            }else{
                //前台产生的订单
                //转让是否有费用
                List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(oldLendOrder.getLendOrderId());
                BigDecimal fee = BigDecimal.ZERO;
                for (LendProductFeesItem feesItem : feeitems) {
                    if (FeesPointEnum.TURNOUT.value2Char() == feesItem.getChargePoint()) {
                        fee = fee.add(lendProductService.calculateLendProductFeesItemBalance(feesItem, BigDecimal.ZERO, BigDecimal.ZERO, applyRecord.getWhenWorth(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
                    }
                }
                if(fee.compareTo(BigDecimal.ZERO)>0){
                    //平台收支账户
                    long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
                    String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.CREDITOR_TURNOUT_FEEPAY,fee);
                    AccountValueChanged avacPay = new AccountValueChanged(userAccount.getAccId(), fee,
                            fee, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_TURNRIGHTS_FEE.getValue(),
                            "LendOrder", VisiableEnum.DISPLAY.getValue(), oldLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                            oldLendOrder.getLendOrderId(), new Date(), descPay, true);
                    avcq.addAccountValueChanged(avacPay);
                    String descIncom = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.CREDITOR_TURNOUT_FEEINCOME,fee);
                    AccountValueChanged avacIncom = new AccountValueChanged(systemAccountId, fee,
                            fee, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_TURNRIGHTS_FEE.getValue(),
                            "LendOrder", VisiableEnum.DISPLAY.getValue(), oldLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                            oldLendOrder.getLendOrderId(), new Date(), descIncom, true);
                    avcq.addAccountValueChanged(avacIncom);
                }

            }
            userAccountOperateService.execute(avcq);

            //转让成功后  转让申请相关订单标识已结清
            Map<String,Object> oldLendOrderMap = new HashMap<String,Object>();
            oldLendOrderMap.put("lendOrderId",oldLendOrder.getLendOrderId());
            oldLendOrderMap.put("orderState", LendOrderConstants.RightsOrderStatusEnum.CLEAR.getValue());
            lendOrderService.update(oldLendOrderMap);
            
			// 订单无效时继承的投标奖励置为无效
			List<RateLendOrder> rateLendOrders = rateLendOrderService.findAllByLendOrderId(oldLendOrder.getLendOrderId(), RateLendOrderStatusEnum.VALID.getValue());
			for (RateLendOrder rateLendOrder : rateLendOrders) {
				rateLendOrder.setStatus(RateLendOrderStatusEnum.UN_VALID.getValue());
				rateLendOrderService.updateRateLendOrder(rateLendOrder);
			}
	
			// 生成合同
			loanApplicationService.createAgreementTurnRights(oldRights.getLoanApplicationId(), newCreditorRights);
	}

	@Override
	public String getExpectRightProfit(Long creditorRightsApplyId, BigDecimal amount) {
		CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(creditorRightsApplyId);
		CreditorRights creditorRights = findById(crta.getApplyCrId(), false);
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(creditorRights.getLoanApplicationId());
		List<RepaymentPlan> plans = repaymentPlanService.getRepaymentPlanList(loanApplication.getLoanApplicationId(), new Date());
		LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());

		if (null != crta) {
			amount = BigDecimalUtil.down(amount.divide(crta.getApplyPrice(), 18, BigDecimal.ROUND_CEILING).multiply(crta.getWhenWorth()), 2);
			BigDecimal partProfit = BigDecimal.ZERO;
			for (RepaymentPlan repaymentPlan : plans) {
				if (RepaymentPlanStateEnum.PART.getValue().equals(String.valueOf(repaymentPlan.getPlanState()))) {
					partProfit = amount.divide(crta.getWhenWorth(), 18, BigDecimal.ROUND_CEILING).multiply(
							repaymentPlan.getShouldInterest2().subtract(repaymentPlan.getFactInterest()));
					amount = BigDecimalUtil.down(
							amount.subtract(amount.divide(crta.getWhenWorth(), 18, BigDecimal.ROUND_CEILING).multiply(
									repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()))), 2);
					crta.setTimeLimit(crta.getTimeLimit() - 1);
					break;
				}
			}
			BigDecimal profit = BigDecimal.ZERO;
			BigDecimal award = BigDecimal.ZERO;
			LoanPublish loanPublish = loanPublishService.findById(creditorRights.getLoanApplicationId());
			if (loanPublish.getAwardRate() != null && loanPublish.getAwardRate().compareTo(BigDecimal.ZERO) != 0) {
				try {
					award = InterestCalculation.getAllInterest(amount, loanPublish.getAwardRate(), loanProduct.getDueTimeType(),
							loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), crta.getTimeLimit(),
							loanProduct.getCycleValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				award = BigDecimalUtil.down(award, 2);
			}
			try {
				profit = InterestCalculation.getAllInterest(amount, loanApplication.getAnnualRate(), loanProduct.getDueTimeType(),
						loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), crta.getTimeLimit(),
						loanProduct.getCycleValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			profit = BigDecimalUtil.down(profit.add(partProfit), 2);
			if (award.compareTo(BigDecimal.ZERO) > 0) {
				// 债权转让奖励判断
				if (loanPublish.getAwardPoint() != null && loanPublish.getAwardPoint().equals(AwardPointEnum.ATMAKELOAN.getValue())) {
					return profit.toString();
				} else {
					return profit + "," + award + "";
				}
			} else
				return profit.toString();

		} else {
			return "0.00";
		}
	}

	@Override
	public List<CreditorRights> getAvailidRightsByLoanApplicationId(
			long loanApplicationId) {
		return myBatisDao.getList("CREDITORRIGHTS.getTurnRightsAvailByLoanApplicationId", loanApplicationId);
	}
	   @Override
	    public Pagination<CreditorRightsExtVo> getCreditorRightsPagingByWeiXin(int pageNo, int pageSize, CreditorRightsExtVo creditorRights, Map<String, Object> customParams) {
	        Pagination<CreditorRightsExtVo> re = new Pagination<CreditorRightsExtVo>();
	        re.setCurrentPage(pageNo);
	        re.setPageSize(pageSize);
	        customParams.put("newDate", new Date());
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("creditorRights", creditorRights);
	        params.put("customParams", customParams);

	        int totalCount = this.myBatisDao.count("getCreditorRightsPagingByWeiXin", params);
	        //获得债券信息
	        List<CreditorRightsExtVo> lenderList = this.myBatisDao.getListForPaging("getCreditorRightsPagingByWeiXin", params, pageNo, pageSize);
	        
	      //获取有债权信息的奖励中间表
	        List<RateLendOrderVO> rateOrderList = rateLendOrderService.getRateLendOrderCreditByUserId(creditorRights.getLendUserId());
	        Map<Long,List<RateLendOrderVO>> rateMap = new HashMap<>();
	        for(RateLendOrderVO rateVO:rateOrderList){
	        	if(rateMap.get(rateVO.getCreditRightsId()) == null || rateMap.get(rateVO.getCreditRightsId()).size()==0){
	        		List<RateLendOrderVO> list = new ArrayList<>();
	        		list.add(rateVO);
	        		rateMap.put(rateVO.getCreditRightsId(),  list);
	        	}else{
	        		rateMap.get(rateVO.getCreditRightsId()).add(rateVO);
	        	}
	        }
	        
	        //获得债券明细
	        for (CreditorRightsExtVo vo : lenderList) {
	        	List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
	        	String point=StringUtils.isNull(vo.getAwardPoint())?"":null;
	        	if(point==null){
	        		if(vo.getAwardPoint().equals("1")){
	        			point=AwardPointEnum.ATMAKELOAN.getDesc();
	        		}else if(vo.getAwardPoint().equals("2")){
	        			point=AwardPointEnum.ATREPAYMENT.getDesc();
	        		}else{
	        			point=AwardPointEnum.ATCOMPLETE.getDesc();
	        		}
	        	}
	        	vo.setAwardPoint(point);
	        	vo.setAwardRate(vo.getAwardRate()!=null&&!vo.getAwardRate().equals("")&&!vo.getAwardRate().equals("0")?"+"+vo.getAwardRate()+"%":"");
	            vo.setCreditorRightsCode(PropertiesUtils.getInstance().get("AGREEMENT_VISIT_PATH") + vo.getCreditorRightsCode());
	            //获得借款申请信息
	            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(vo.getLoanApplicationId());
	            //计算预期收益
	            LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
//	            try {
//	                BigDecimal expectProfit = InterestCalculation.getAllInterest(vo.getBuyPrice(), vo.getAnnualRate(), loanProduct.getDueTimeType(), loanApplicationListVO.getRepayMentMethod().charAt(0), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), detailRightsList.size(), loanProduct.getCycleValue());
	//
//	                vo.setExpectProfit(expectProfit);
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
	            //处理还款方式
	            if (loanApplicationListVO.getRepayMethod().equals("1")) {
	                loanApplicationListVO.setRepayMentMethod(PaymentMethodEnum.getPaymentMethod(loanApplicationListVO.getRepayMentMethod()).getDesc());
	            } else {
	                ConstantDefine repaymentMode = constantDefineCached.getConstantByValue("repaymentMode", loanApplicationListVO.getRepayMethod());
	                loanApplicationListVO.setRepayMentMethod(repaymentMode.getConstantName());
	            }
	            vo.setLoanApplicationListVO(loanApplicationListVO);
	            //获得出借产品所有费用项
	            List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(vo.getLendOrderId());
	            //需要计算回款时的费用项（周期费用）
	            List<LendProductFeesItem> atcycleFeeitems = new ArrayList<LendProductFeesItem>();
	            //需要计算回款时的费用项（到期费用）
	            List<LendProductFeesItem> complateFeeitems = new ArrayList<LendProductFeesItem>();
	            
	            LoanApplicationFeesItem defaultInterestFees = null ;
	            for (LendProductFeesItem feesItem : feeitems) {
	                if (FeesPointEnum.ATCYCLE.value2Char() == feesItem.getChargePoint()) {
	                    atcycleFeeitems.add(feesItem);
	                }

	                if (FeesPointEnum.ATCOMPLETE.value2Char() == feesItem.getChargePoint()) {
	                    complateFeeitems.add(feesItem);
	                }
	                
	            }
	            //获得费用
	            LendOrder lendOrder = lendOrderService.findById(vo.getLendOrderId());
	            List<LendLoanBinding> byLendAndLoan = lendLoanBindingService.findByLendAndLoan(lendOrder.getLendProductId(), loanProduct.getLoanProductId());
	            List<FeesItem> fis = new ArrayList<FeesItem>();
	            if(byLendAndLoan!=null){
	                for(LendLoanBinding llb:byLendAndLoan){
	                    if (llb.getFeesItemId() != null && llb.getFeesItemId() > 0) {
	                        fis.add(feesItemService.findById(llb.getFeesItemId()));
	                    }

	                }
	            }
	            
	            //最近还款日期
	            try {
	                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
	            } catch (ParseException e) {
	                e.printStackTrace();
	                logger.error("最近还款日期计算出错", e);
	            }
	            //处理一下明细，添加待缴费用
	            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
	            for (RightsRepaymentDetail detail : detailRightsList) {

	                //计算到期费用
	                BigDecimal fees1 = BigDecimal.ZERO;
	                if (detail.getSectionCode()==detailRightsList.size()){
	                    if (complateFeeitems!=null&&complateFeeitems.size()>0){
	                        for (LendProductFeesItem item : complateFeeitems) {
	                            BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
	                            fees1 = fees1.add(fee);
	                        }
	                        fees1 =  BigDecimalUtil.up(fees1, 2);
	                    }
	                }
	                //计算周期费用
	                BigDecimal fees2 = BigDecimal.ZERO;
	                if (atcycleFeeitems!=null&&atcycleFeeitems.size()>0){
	                    for (LendProductFeesItem item : atcycleFeeitems) {
	                        BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
	                        fees2 = fees2.add(fee);
	                    }
	                    fees2 =  BigDecimalUtil.up(fees2, 2);
	                }
	                BigDecimal fees3 = BigDecimal.ZERO;
	                if(fis!=null&&fis.size()>0){
	                    for(FeesItem fi:fis){
	                        BigDecimal fee = feesItemService.calculateFeesBalance(fi.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
	                                detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
	                        fees3 = fees3.add(fee);
	                    }
	                    fees3 = BigDecimalUtil.up(fees3, 2);
	                }
	                
	                //计算罚息实还金额
	                List<LoanApplicationFeesItem> loanFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(vo.getLoanApplicationId());
	                for(LoanApplicationFeesItem fees :loanFeesItems ){
	                	if(fees.getItemType().equals(FeesItemTypeEnum.ITEMTYPE_DEFAULTINTREST.getValue())){
	                		RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
	                		BigDecimal defaultInterestPaidAll = defaultInterestDetailService.getDefaultInterestPaidByRepaymentPlanId(detail.getRepaymentPlanId());
	                		if(defaultInterestPaidAll!=null && defaultInterestPaidAll.compareTo(BigDecimal.ZERO) == 1 ){
		                		BigDecimal newPaid = defaultInterestPaidAll.multiply(BigDecimal.ONE.subtract(fees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
		                				BigDecimal.ROUND_CEILING)));
		                		detail.setDepalFine(BigDecimalUtil.down(newPaid, 2));
		                		defaultInterestFees = fees;
	                		}
	                	}
	                }
	       		 
	                BigDecimal defaultInterestAll = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(detail.getRepaymentPlanId());
	                BigDecimal defaultInterest = BigDecimal.ZERO ;
	                
	                if(defaultInterestFees != null && defaultInterestAll != null && !defaultInterestAll.equals(BigDecimal.ZERO)) { 
	                	RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
	                	//个人罚息补偿：罚息总额*（1-平台占比）*出借债权出资占比
	                	defaultInterest = defaultInterestAll.multiply(BigDecimal.ONE.subtract(defaultInterestFees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
	            				BigDecimal.ROUND_CEILING)));
	                	defaultInterest =  BigDecimalUtil.down(defaultInterest, 2);
//	                	fees3 = fees3.add(defaultInterest);
	                }
	                
	                detail.setShouldFee(fees1.add(fees2).add(fees3));
	                detail.setDefaultInterest(defaultInterest);
	                if(!detail.getFactBalance().equals(BigDecimal.ZERO)) {
	                	detail.setFactBalance(BigDecimalUtil.down(detail.getFactBalance(), 2));
	                }
	                
	            }
				if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
					vo.getLoanApplicationListVO().setCycleCount(detailRightsList.size());
				}
	            
	            //获取投标类待回款(利息+本金)
	            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
	            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
	            vo.setRightsRepaymentDetailList(detailRightsList);
	            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
	            //计算预期收益
	            vo.setExpectProfit(exceptProfit);
	            
				// 债权转让按钮显示/隐藏
				boolean rightBtn = true;
				Long termDay = repaymentPlanService.getTermDay(detailRightsList);
				if (termDay < 0) {
					rightBtn = false;
				}
				if (rightBtn) {
					// 债权生成时间是否大于30天
					int betweenDate = DateUtil.daysBetween(vo.getCreateTime(), new Date());
					if (betweenDate <= 30) {
						rightBtn = false;
					}
				}
				if (rightBtn) {
					// 判断是否逾期
					List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlanByLoanAppIdAndState(vo.getLoanApplicationId(),
							RepaymentPlanStateEnum.DEFAULT.getValueChar());
					if (repaymentPlanList.size() != 0) {
						rightBtn = false;
					}
				}
				vo.setRightsBtn(rightBtn);
				
				if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
					Map<String, Object> paramsApply = new HashMap<String, Object>();
					paramsApply.put("carryCrId", vo.getCreditorRightsId());
					paramsApply.put("status", CreditorRightsTransferDetailStatus.SUCCESS.getValue());
					CreditorRightsDealDetail crta = creditorRightsTransferAppService.getCreditorRightsDealDetailByParam(paramsApply);
					vo.setCreditorRightsApplyId(crta == null ? null : crta.getCreditorRightsApplyId());
				}

				//设置奖励订单中间表信息
				vo.setRateList(rateMap.get(vo.getCreditorRightsId()));
	        }
	    	re.setRows(lenderList);
	    	re.setTotal(totalCount);

	        return re;
	    }
	   
	    /**
		 * 我的理财-省心计划-出借明细 列表查询
		 * @param pageNo 第几页
		 * @param pageSize 每页条数
		 * @param creditorRights 债权字段查询
		 * @param customParams 自定义查询
		 * @return
		 */
	    @Override
	    public Pagination<CreditorRightsExtVo> getSXJHCreditorRightsDetailPaging(int pageNo, int pageSize, CreditorRightsExtVo creditorRights, Map<String, Object> customParams) {
	        Pagination<CreditorRightsExtVo> re = new Pagination<CreditorRightsExtVo>();
	        re.setCurrentPage(pageNo);
	        re.setPageSize(pageSize);

	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("creditorRights", creditorRights);
	        params.put("customParams", customParams);

	        int totalCount = this.myBatisDao.count("getSXJHCreditorRightsDetailPaging", params);
	        //获得债券信息
	        List<CreditorRightsExtVo> lenderList = this.myBatisDao.getListForPaging("getSXJHCreditorRightsDetailPaging", params, pageNo, pageSize);
	        
	        //获取有债权信息的奖励中间表
	        List<RateLendOrderVO> rateOrderList = rateLendOrderService.getRateLendOrderCreditByUserId(creditorRights.getLendUserId());
	        Map<Long,List<RateLendOrderVO>> rateMap = new HashMap<>();
	        for(RateLendOrderVO rateVO:rateOrderList){
	        	if(rateMap.get(rateVO.getCreditRightsId()) == null || rateMap.get(rateVO.getCreditRightsId()).size()==0){
	        		List<RateLendOrderVO> list = new ArrayList<>();
	        		list.add(rateVO);
	        		rateMap.put(rateVO.getCreditRightsId(),  list);
	        	}else{
	        		rateMap.get(rateVO.getCreditRightsId()).add(rateVO);
	        	}
	        }
	        
	        //获得债券明细
	        for (CreditorRightsExtVo vo : lenderList) {
	        	List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
	        	String point=StringUtils.isNull(vo.getAwardPoint())?"":null;
	        	if(point==null){
	        		if(vo.getAwardPoint().equals("1")){
	        			point=AwardPointEnum.ATMAKELOAN.getDesc();
	        		}else if(vo.getAwardPoint().equals("2")){
	        			point=AwardPointEnum.ATREPAYMENT.getDesc();
	        		}else{
	        			point=AwardPointEnum.ATCOMPLETE.getDesc();
	        		}
	        	}
	        	vo.setAwardPoint(point);
	        	vo.setAwardRate(vo.getAwardRate()!=null&&!vo.getAwardRate().equals("")&&!vo.getAwardRate().equals("0")?"+"+vo.getAwardRate()+"%":"");
	            vo.setCreditorRightsCode(PropertiesUtils.getInstance().get("AGREEMENT_VISIT_PATH") + vo.getCreditorRightsCode());
	            //获得借款申请信息
	            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(vo.getLoanApplicationId());
	            //计算预期收益
	            LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
	            //处理还款方式
	            if (loanApplicationListVO.getRepayMethod().equals("1")) {
	                loanApplicationListVO.setRepayMentMethod(PaymentMethodEnum.getPaymentMethod(loanApplicationListVO.getRepayMentMethod()).getDesc());
	            } else {
	                ConstantDefine repaymentMode = constantDefineCached.getConstantByValue("repaymentMode", loanApplicationListVO.getRepayMethod());
	                loanApplicationListVO.setRepayMentMethod(repaymentMode.getConstantName());
	            }
	            vo.setLoanApplicationListVO(loanApplicationListVO);
	            //获得出借产品所有费用项
	            List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(vo.getLendOrderId());
	            //需要计算回款时的费用项（周期费用）
	            List<LendProductFeesItem> atcycleFeeitems = new ArrayList<LendProductFeesItem>();
	            //需要计算回款时的费用项（到期费用）
	            List<LendProductFeesItem> complateFeeitems = new ArrayList<LendProductFeesItem>();
	            
	            LoanApplicationFeesItem defaultInterestFees = null ;
	            for (LendProductFeesItem feesItem : feeitems) {
	                if (FeesPointEnum.ATCYCLE.value2Char() == feesItem.getChargePoint()) {
	                    atcycleFeeitems.add(feesItem);
	                }

	                if (FeesPointEnum.ATCOMPLETE.value2Char() == feesItem.getChargePoint()) {
	                    complateFeeitems.add(feesItem);
	                }
	                
	            }
	            //获得费用
	            LendOrder lendOrder = lendOrderService.findById(vo.getLendOrderId());
	            List<LendLoanBinding> byLendAndLoan = lendLoanBindingService.findByLendAndLoan(lendOrder.getLendProductId(), loanProduct.getLoanProductId());
	            List<FeesItem> fis = new ArrayList<FeesItem>();
	            if(byLendAndLoan!=null){
	                for(LendLoanBinding llb:byLendAndLoan){
	                    if (llb.getFeesItemId() != null && llb.getFeesItemId() > 0) {
	                        fis.add(feesItemService.findById(llb.getFeesItemId()));
	                    }

	                }
	            }
	            
	            //最近还款日期
	            try {
	                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
	            } catch (ParseException e) {
	                e.printStackTrace();
	                logger.error("最近还款日期计算出错", e);
	            }
	            //处理一下明细，添加待缴费用
	            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
	            for (RightsRepaymentDetail detail : detailRightsList) {

	                //计算到期费用
	                BigDecimal fees1 = BigDecimal.ZERO;
	                if (detail.getSectionCode()==detailRightsList.size()){
	                    if (complateFeeitems!=null&&complateFeeitems.size()>0){
	                        for (LendProductFeesItem item : complateFeeitems) {
	                            BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
	                            fees1 = fees1.add(fee);
	                        }
	                        fees1 =  BigDecimalUtil.up(fees1, 2);
	                    }
	                }
	                //计算周期费用
	                BigDecimal fees2 = BigDecimal.ZERO;
	                if (atcycleFeeitems!=null&&atcycleFeeitems.size()>0){
	                    for (LendProductFeesItem item : atcycleFeeitems) {
	                        BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
	                        fees2 = fees2.add(fee);
	                    }
	                    fees2 =  BigDecimalUtil.up(fees2, 2);
	                }
	                BigDecimal fees3 = BigDecimal.ZERO;
	                if(fis!=null&&fis.size()>0){
	                    for(FeesItem fi:fis){
	                        BigDecimal fee = feesItemService.calculateFeesBalance(fi.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
	                                detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
	                        fees3 = fees3.add(fee);
	                    }
	                    fees3 = BigDecimalUtil.up(fees3, 2);
	                }
	                
	                //计算罚息实还金额
	                List<LoanApplicationFeesItem> loanFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(vo.getLoanApplicationId());
	                for(LoanApplicationFeesItem fees :loanFeesItems ){
	                	if(fees.getItemType().equals(FeesItemTypeEnum.ITEMTYPE_DEFAULTINTREST.getValue())){
	                		RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
	                		BigDecimal defaultInterestPaidAll = defaultInterestDetailService.getDefaultInterestPaidByRepaymentPlanId(detail.getRepaymentPlanId());
	                		if(defaultInterestPaidAll!=null && defaultInterestPaidAll.compareTo(BigDecimal.ZERO) == 1 ){
		                		BigDecimal newPaid = defaultInterestPaidAll.multiply(BigDecimal.ONE.subtract(fees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
		                				BigDecimal.ROUND_CEILING)));
		                		detail.setDepalFine(BigDecimalUtil.down(newPaid, 2));
		                		defaultInterestFees = fees;
	                		}
	                	}
	                }
	       		 
	                BigDecimal defaultInterestAll = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(detail.getRepaymentPlanId());
	                BigDecimal defaultInterest = BigDecimal.ZERO ;
	                
	                if(defaultInterestFees != null && defaultInterestAll != null && !defaultInterestAll.equals(BigDecimal.ZERO)) { 
	                	RightsRepaymentDetail rightsRepaymentDetail = rightsRepaymentDetailService.getDetailByRightsId(vo.getCreditorRightsId(), detail.getSectionCode());
	                	//个人罚息补偿：罚息总额*（1-平台占比）*出借债权出资占比
	                	defaultInterest = defaultInterestAll.multiply(BigDecimal.ONE.subtract(defaultInterestFees.getWorkflowRatio()).multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"), 18,
	            				BigDecimal.ROUND_CEILING)));
	                	defaultInterest =  BigDecimalUtil.down(defaultInterest, 2);
	                }
	                
	                detail.setShouldFee(fees1.add(fees2).add(fees3));
	                detail.setDefaultInterest(defaultInterest);
	                if(!detail.getFactBalance().equals(BigDecimal.ZERO)) {
	                	detail.setFactBalance(BigDecimalUtil.down(detail.getFactBalance(), 2));
	                }
	                
	            }
				if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
					vo.getLoanApplicationListVO().setCycleCount(detailRightsList.size());
				}
	            
	            //获取投标类待回款(利息+本金)
	            BigDecimal capitalRecive = lendOrderReceiveService.getChildCapitalReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
	            BigDecimal interestRecive = lendOrderReceiveService.getChildInterestReciveByUserId(vo.getLendOrderId(), creditorRights.getLendUserId());
	            vo.setRightsRepaymentDetailList(detailRightsList);
	            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
	            //计算预期收益
	            vo.setExpectProfit(exceptProfit);
	            
				// 债权转让按钮显示/隐藏
				boolean rightBtn = true;
				Long termDay = repaymentPlanService.getTermDay(detailRightsList);
				if (termDay < 0) {
					rightBtn = false;
				}
				if (rightBtn) {
					// 债权生成时间是否大于30天
					int betweenDate = DateUtil.daysBetween(vo.getCreateTime(), new Date());
					if (betweenDate <= 30) {
						rightBtn = false;
					}
				}
				if (rightBtn) {
					// 判断是否逾期
					List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlanByLoanAppIdAndState(vo.getLoanApplicationId(),
							RepaymentPlanStateEnum.DEFAULT.getValueChar());
					if (repaymentPlanList.size() != 0) {
						rightBtn = false;
					}
				}
				vo.setRightsBtn(rightBtn);
				
				if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
					Map<String, Object> paramsApply = new HashMap<String, Object>();
					paramsApply.put("carryCrId", vo.getCreditorRightsId());
					paramsApply.put("status", CreditorRightsTransferDetailStatus.SUCCESS.getValue());
					CreditorRightsDealDetail crta = creditorRightsTransferAppService.getCreditorRightsDealDetailByParam(paramsApply);
					vo.setCreditorRightsApplyId(crta == null ? null : crta.getCreditorRightsApplyId());
				}

				//设置奖励订单中间表信息
				vo.setRateList(rateMap.get(vo.getCreditorRightsId()));
	        }
	    	re.setRows(lenderList);
	    	re.setTotal(totalCount);

	        return re;
	    }

		@Override
		public List<CreditorRights> getCreditorRightsByLendOrderPid(Long lendOrderId) {
			return myBatisDao.getList("CREDITORRIGHTS.getCreditorRightsByLendOrderPid", lendOrderId);
		}

		@Override
		public List<CreditorRights> getCreditorRightsByUserId(Long userId) {
			return myBatisDao.getList("CREDITORRIGHTS.getCreditorRightsByUserId", userId);
		}
		
		/**
		 * [省心计划工具]查询某人省心计划，获取总的预期投标奖励（用户ID --> 省心订单ID --> 子订单List --> 债权List）
		 * @param userId 用户ID
		 * @return
		 */
		@Override
		public BigDecimal getTotalExpectAwardByUserId(Long userId){
			BigDecimal totalAward = BigDecimal.ZERO;
			try {
				List<CreditorRights> creditorRightList = this.getCreditorRightsByUserId(userId);
				if(creditorRightList != null){
					for (CreditorRights cr : creditorRightList) {
						if(CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char() == cr.getRightsState()// 已生效
							||  CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.value2Char() == cr.getRightsState()//申请转出
							||  CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.value2Char() == cr.getRightsState()){//转让中
							totalAward = totalAward.add(this.getExpectAward(cr.getLoanApplicationId(), cr.getBuyPrice()));//算预期的奖励
						}
					}
				}
				totalAward = totalAward.add(userAccountService.getUserTotalFinanceAwardByUserId(userId));
			} catch (Exception e) {
				logger.error("【错误】获取总的预期投标奖励,userId=" + userId + ",message=" + e.getMessage(),e);
			}
			return totalAward;
		}
		
		
		/**
		 * [省心计划工具]获取子标，预期投标奖励
		 * @param loanApplicationId 标的ID
		 * @param amount 投资金额
		 * @return
		 */
		@Override
		public BigDecimal getExpectAward(Long loanApplicationId, BigDecimal amount){
			BigDecimal award = BigDecimal.ZERO;
			LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
			LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
			LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
			if (loanPublish.getAwardRate()!=null){
				try {
					award = InterestCalculation.getAllInterest(amount,loanPublish.getAwardRate(),loanProduct.getDueTimeType(),
							loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
					award = BigDecimalUtil.down(award,2);
				} catch (Exception e) {
					logger.error("【错误】获取子标，预期投标奖励,loanApplication=" + loanApplication + ",amount=" + amount + ",message=" + e.getMessage());
				}
			}
			return award;
		}
		
}
