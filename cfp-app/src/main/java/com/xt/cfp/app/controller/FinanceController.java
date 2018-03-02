package com.xt.cfp.app.controller;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.app.annotation.DoNotNeedLogin;
import com.xt.cfp.app.vo.APPLenderRecordVO;
import com.xt.cfp.app.vo.APPLoanApplicationListVO;
import com.xt.cfp.app.vo.APPMortgageCarSnapshotVO;
import com.xt.cfp.app.vo.APPMyTurnCreditRightListVO;
import com.xt.cfp.app.vo.APPTurnCreditRightListVO;
import com.xt.cfp.app.vo.CreditorRightsExtVO;
import com.xt.cfp.app.vo.RightsRepaymentDetailVO;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.AttachmentIsCode;
import com.xt.cfp.core.constants.AwardPointEnum;
import com.xt.cfp.core.constants.ChannelTypeEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailIsPayOffEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailStateEnum;
import com.xt.cfp.core.constants.CreditorRightsFromWhereEnum;
import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.FeesItemTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.constants.PaymentMethodEnum;
import com.xt.cfp.core.constants.RepaymentPlanStateEnum;
import com.xt.cfp.core.pojo.Attachment;
import com.xt.cfp.core.pojo.AwardDetail;
import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.CoLtd;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.CustomerBasicSnapshot;
import com.xt.cfp.core.pojo.CustomerHouseSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCarLoanSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCreditSnapshot;
import com.xt.cfp.core.pojo.EnterpriseFactoringSnapshot;
import com.xt.cfp.core.pojo.EnterpriseFoundationSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.EnterprisePledgeSnapshot;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendLoanBinding;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendProductFeesItem;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanApplicationFeesItem;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanPublish;
import com.xt.cfp.core.pojo.MortgageCarSnapshot;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.AddressVO;
import com.xt.cfp.core.pojo.ext.CreditorRightsExtVo;
import com.xt.cfp.core.pojo.ext.CustomerUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.EnterpriseUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.service.AddressService;
import com.xt.cfp.core.service.AwardDetailService;
import com.xt.cfp.core.service.CityInfoService;
import com.xt.cfp.core.service.CoLtdService;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CreditorRightsTransferAppService;
import com.xt.cfp.core.service.CustomerBasicSnapshotService;
import com.xt.cfp.core.service.CustomerHouseSnapshotService;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import com.xt.cfp.core.service.EnterpriseCarLoanSnapshotService;
import com.xt.cfp.core.service.EnterpriseCreditSnapshotService;
import com.xt.cfp.core.service.EnterpriseFactoringSnapshotService;
import com.xt.cfp.core.service.EnterpriseFoundationSnapshotService;
import com.xt.cfp.core.service.EnterpriseInfoService;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.EnterprisePledgeSnapshotService;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.GuaranteeCompanyService;
import com.xt.cfp.core.service.LendLoanBindingService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderReceiveService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationFeesItemService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.MortgageCarSnapshotService;
import com.xt.cfp.core.service.ProvinceInfoService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.RightsRepaymentDetailService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.PropertiesUtils;
import com.xt.cfp.core.util.TokenUtils;

@Controller
@RequestMapping(value = "/finance")
public class FinanceController extends BaseController{

	private static Logger logger = Logger.getLogger(FinanceController.class);
	@Autowired
	private LendProductService lendProductService;
	@Autowired
	private LoanApplicationService loanApplicationService;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
	private CustomerBasicSnapshotService customerBasicSnapshotService;
	@Autowired
	private CustomerHouseSnapshotService customerHouseSnapshotService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private CreditorRightsService creditorRightsService;
	@Autowired
	private LoanPublishService loanPublishService;
	@Autowired
	private LendOrderBidDetailService lendOrderBidDetailService;
	@Autowired
	private LoanProductService loanProductService;
	@Autowired
	private ConstantDefineService constantdefineservice;
	@Autowired
	private EnterpriseInfoService enterpriseInfoService;
	@Autowired
	private MortgageCarSnapshotService mortgageCarSnapshotService;
	@Autowired
	private EnterpriseCarLoanSnapshotService enterpriseCarLoanSnapshotService;
	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	@Autowired
	private EnterpriseFactoringSnapshotService enterpriseFactoringSnapshotService;
	@Autowired
	private EnterpriseCreditSnapshotService enterpriseCreditSnapshotService;
	@Autowired
	private ProvinceInfoService provinceInfoService;
	@Autowired
	private CityInfoService cityInfoService;
	@Autowired
	private CoLtdService coLtdService;
	@Autowired
	private ConstantDefineCached constantDefineCached;
	@Autowired
	private LendLoanBindingService lendLoanBindingService;
	@Autowired
	private FeesItemService feesItemService;
	@Autowired
	private RightsRepaymentDetailService rightsRepaymentDetailService; 
	@Autowired
	private LendOrderReceiveService lendOrderReceiveService;
	@Autowired
	private DefaultInterestDetailService defaultInterestDetailService;
	@Autowired
	private EnterpriseFoundationSnapshotService enterpriseFoundationSnapshotService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private LoanApplicationFeesItemService loanApplicationFeesItemService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private GuaranteeCompanyService guaranteeCompanyService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private CreditorRightsTransferAppService creditorRightsTransferAppService;
	@Autowired
	private UserInfoExtService userInfoExtService;
	@Autowired
	private EnterprisePledgeSnapshotService enterprisePledgeSnapshotService;
	@Autowired
	private AwardDetailService awardDetailService;
	
	/**
	 * 【APP接口】
	 * 投标列表
	 * @param request
	 * @param loanApplicationId
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value = "/getLender")// [APP]
	@ResponseBody
	@DoNotNeedLogin
	public Object getLender(HttpServletRequest request, Long loanApplicationId, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 获取借款申请状态
			LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
			Pagination<LenderRecordVO> pagination = null;
			if ("345".indexOf(loanApplication.getApplicationState()) != -1) {
				// 发标中、放款审核中、待放款
				// 查询出借订单明细表中 投标中 的状态数据
				pagination = lendOrderBidDetailService.findLendOrderDetailPaging(pageNo, pageSize, loanApplicationId, LendOrderBidStatusEnum.BIDING, DisplayEnum.DISPLAY);
			} else {
				// 6、7、8 还款中、已结清、已结清(提前还贷)
				// 到债权表查询 已生效，还款中 状态的数据
				pagination = lendOrderBidDetailService.findLendOrderDetailPaging(pageNo, pageSize,loanApplicationId, LendOrderBidStatusEnum.BIDSUCCESS, DisplayEnum.DISPLAY);
//				pagination = creditorRightsService.findLendOrderDetailPaging(pageNo, pageSize, pageNo, pageSize, loanApplicationId,
//						CreditorRightsStateEnum.EFFECTIVE, CreditorRightsStateEnum.COMPLETE, CreditorRightsStateEnum.EARLYCOMPLETE );
			}

			List<LenderRecordVO> lendLists = pagination.getRows();
			List<APPLenderRecordVO> datas = new ArrayList<APPLenderRecordVO>();

			if (null != lendLists && lendLists.size() > 0) {
				for (int i = 0; i < lendLists.size(); i++) {
					LenderRecordVO vo = lendLists.get(i);
					APPLenderRecordVO appVO = new APPLenderRecordVO();
					appVO.setLenderName(vo.getLenderName());
					appVO.setLendAmount(String.valueOf(vo.getLendAmount()));
					appVO.setLendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getLendTime()));
					
					//新版投标时间格式【开始】------------------------------------------------------
					//投标时间：
					//1.24小时以内：HH:mm:ss
					//2.大于24小时小于365天：MM-dd
					//3.大约365天：yyyy-MM-dd
//					int days = DateUtil.daysBetween(vo.getLendTime(), new Date());
//					if(days == 0){
//						appVO.setLendTime(new SimpleDateFormat("HH:mm:ss").format(vo.getLendTime()));
//					}else if (days > 0 && days < 365) {
//						appVO.setLendTime(new SimpleDateFormat("MM-dd").format(vo.getLendTime()));
//					}else if (days >= 365) {
//						appVO.setLendTime(new SimpleDateFormat("yyyy-MM-dd").format(vo.getLendTime()));
//					}else {
//						appVO.setLendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getLendTime()));
//					}
					//新版投标时间格式【结束】------------------------------------------------------
					datas.add(appVO);
				}
			}
			resultMap.put("pageSize", pagination.getPageSize());
			resultMap.put("pageNo", pagination.getCurrentPage());
			resultMap.put("total", pagination.getTotal());
			resultMap.put("rows", datas);

		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
		return returnResultMap(true, resultMap, null, null);
	}

	/**
	 * 【APP接口】
	 * 出借列表
	 * @param request
	 * @param loanApplicationListVO
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value = "/loanList")// [APP]
	@ResponseBody
	public Object loanList(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		LoanApplicationListVO loanApplicationListVO = new LoanApplicationListVO();
		DecimalFormat df = new DecimalFormat("#,##0.00");
		try {
			Pagination<LoanApplicationListVO> pagination = loanApplicationService.getLoanApplicationPaging(pageNo, pageSize, loanApplicationListVO,
					null);
			List<LoanApplicationListVO> loanAppLists = pagination.getRows();

			List<APPLoanApplicationListVO> datas = new ArrayList<APPLoanApplicationListVO>();

			if (null != loanAppLists && loanAppLists.size() > 0) {
				APPLoanApplicationListVO loanListVO = null;
				for (LoanApplicationListVO loanApp : loanAppLists) {
					loanListVO = new APPLoanApplicationListVO();

					loanListVO.setLoanApplicationTitle(loanApp.getLoanApplicationTitle());
					loanListVO.setAnnualRate(String.valueOf(loanApp.getAnnualRate()));//年化利率
					loanListVO.setRewardsPercent(String.valueOf(loanApp.getRewardsPercent()));//奖励利率
					loanListVO.setCycleCount(String.valueOf(loanApp.getCycleCount()));
					loanListVO.setStartAmount(String.valueOf(loanApp.getStartAmount()));
					loanListVO.setTotalAmountOfLoan(df.format(loanApp.getConfirmBalance()));//借款总额
					loanListVO.setLoanApplicationId(String.valueOf(loanApp.getLoanApplicationId()));
					loanListVO.setBegin(loanApp.isBegin());
					loanListVO.setPopenTime(String.valueOf(loanApp.getPopenTime().getTime()));
					loanListVO.setApplicationState(loanApp.getApplicationState());
					loanListVO.setLoanType(loanApp.getLoanType());
					loanListVO.setTargetType(loanApp.getoType()==null?"0":loanApp.getoType());//0无定向；1定向密码；2定向用户
					//计算距离开标时间
					Date openTime = loanApp.getPopenTime();
					int secondBetween = 0;
					if (null != openTime) {
						secondBetween = DateUtil.secondBetween(new Date(), openTime);
					}
					loanListVO.setSecondBetwween(String.valueOf(secondBetween));
					datas.add(loanListVO);
				}
			}

			resultMap.put("pageSize", pagination.getPageSize());
			resultMap.put("pageNo", pagination.getCurrentPage());
			resultMap.put("total", pagination.getTotal());
			resultMap.put("rows", datas);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
		return returnResultMap(true, resultMap, null, null);

	}

	/**
	 * 【APP接口工具】
	 * 预期收益计算
	 * @param loanApplicationId 标ID
	 * @param amount 借款金额
	 * @return
	 */
	private BigDecimal getExpectProfit(Long loanApplicationId, BigDecimal amount){
		BigDecimal profit = BigDecimal.ZERO;
		try {
			LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
			LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
			profit = InterestCalculation.getAllInterest(amount,loanApplication.getAnnualRate(),loanProduct.getDueTimeType(),
                    loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
			profit = BigDecimalUtil.down(profit,18);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
        }
		return profit;
	}
	
	/**
	 * 【APP接口工具】
	 * 奖励预期收益计算
	 * @param loanApplicationId 标ID
	 * @param amount 借款金额
	 * @return
	 */
	private BigDecimal getAwardProfit(Long loanApplicationId, BigDecimal amount){
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
		LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
		BigDecimal award = BigDecimal.ZERO;
		LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
		if (loanPublish.getAwardRate()!=null && BigDecimal.ZERO.compareTo(loanPublish.getAwardRate())!=0){
			try {
				award = InterestCalculation.getAllInterest(amount,loanPublish.getAwardRate(),loanProduct.getDueTimeType(),
						loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
				award = BigDecimalUtil.down(award,18);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return award;
	}

	/**
	 * 【APP接口】
	 * 投标详情页
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@DoNotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/bidding")// [APP]
	public Object bidding(HttpServletRequest request, @RequestParam(value = "loanApplicationNo", required = false) Long loanApplicationNo) {
		if (loanApplicationNo == null) {
			// 非法请求
			return returnResultMap(false, null, "check", "请求参数异常");
		}
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
		if (loanApplicationListVO == null) {
			// 非法请求
			return returnResultMap(false, null, "check", "请求参数异常");
		}

		DecimalFormat df = new DecimalFormat("#,##0.00");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Date openTime = loanApplicationListVO.getPopenTime();

			int secondBetween = 0;
			if (null != openTime) {
				secondBetween = DateUtil.secondBetween(new Date(), openTime);
			}
			resultMap.put("secondBetwween",String.valueOf( secondBetween));

			// token
			String token = TokenUtils.setNewToken(request);
			resultMap.put("token", token);
			
			//标的状态REPAYMENTING("6","还款中"), COMPLETED("7","已结清"), EARLYCOMPLETE("8","已结清(提前还贷)"),
		    //增加还款信息展示
		    //未还款时计算还款信息
			//已还本息
			String strFormatTo = "yyyy-MM-dd";
			Map<String,Object> repayInfoMap = new HashMap<>();
			BigDecimal hasPaidBalance = BigDecimal.ZERO ;
			//待还本息
			BigDecimal waitPaidBalance = BigDecimal.ZERO ;
			Map<Object,String> stateMap = new HashMap<>();
			for(RepaymentPlanStateEnum r : RepaymentPlanStateEnum.values()){
				stateMap.put(r.value2Char(), r.getDesc());
			}
			repayInfoMap.put("stateMap", stateMap);
			if (LoanApplicationStateEnum.REPAYMENTING.getValue().equals(loanApplicationListVO.getApplicationState())
				|| LoanApplicationStateEnum.COMPLETED.getValue().equals(loanApplicationListVO.getApplicationState())
				|| LoanApplicationStateEnum.EARLYCOMPLETE.getValue().equals(loanApplicationListVO.getApplicationState())){
				List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationNo,ChannelTypeEnum.ONLINE);
				if(repaymentPlanList!=null &&repaymentPlanList.size()>0){
					for (RepaymentPlan plan : repaymentPlanList){
						plan.setRepaymentDayDisplay(DateUtil.getFormattedDateUtil(plan.getRepaymentDay(), strFormatTo));
						hasPaidBalance = hasPaidBalance.add(plan.getFactBalance());
						if(plan.getPlanState() == RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char()){
							waitPaidBalance = waitPaidBalance.add(plan.getShouldCapital2().subtract(plan.getFactCalital()));
						}else if(plan.getPlanState() == RepaymentPlanStateEnum.COMPLETE.value2Char()){
							
						}else{
							waitPaidBalance = waitPaidBalance.add(plan.getShouldBalance2().subtract(plan.getFactBalance()));
						}
					}
				}
				waitPaidBalance = BigDecimalUtil.up(waitPaidBalance, 2);
				repayInfoMap.put("showRepaymentList", repaymentPlanList);
				repayInfoMap.put("waitPaidBalance", waitPaidBalance);
				repayInfoMap.put("hasPaidBalance", hasPaidBalance);
				repayInfoMap.put("isRepaying", true);
			}else{
				LoanProduct product = loanProductService.findById(loanApplicationListVO.getLoanProductId());
				LoanApplication loanApp = loanApplicationService.findById(loanApplicationListVO.getLoanApplicationId());
				List<RepaymentPlan> repaymentList = new ArrayList<>();
				try {
					repaymentList = loanApplicationService.getRepaymentPLanData(product, loanApp);
				} catch (Exception e) {
					logger.error("查询异常",e);
					e.printStackTrace();
				}
				 for(RepaymentPlan plan : repaymentList){
					 plan.setRepaymentDayDisplay(DateUtil.getFormattedDateUtil(plan.getRepaymentDay(), strFormatTo));
					 waitPaidBalance = waitPaidBalance.add(plan.getShouldBalance2());
				 }
				repayInfoMap.put("showRepaymentList", repaymentList);
				waitPaidBalance = BigDecimalUtil.up(waitPaidBalance, 2);
				repayInfoMap.put("waitPaidBalance", waitPaidBalance);
				repayInfoMap.put("hasPaidBalance", hasPaidBalance);
				repayInfoMap.put("isRepaying", false);
			}
			resultMap.put("repayInfoMap", repayInfoMap);
					
			
			// 组织页面返回值
			// 还款方式
			String repayMethod = null;
			if ("1".equals(loanApplicationListVO.getRepayMethod())) {
				if (PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getDesc();
				} else if (PaymentMethodEnum.AVERAGE_CAPTIAL.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPTIAL.getDesc();
				}
			} else {
				ConstantDefine contant = new ConstantDefine();
				contant.setConstantValue(loanApplicationListVO.getRepayMethod());
				contant.setConstantTypeCode("repaymentMode");
				ConstantDefine cd = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				if(null != cd){
					repayMethod = cd.getConstantName();
				}
			}
			resultMap.put("repayMethod", repayMethod);
			
			//保障物
			resultMap.put("guaranteeType", loanApplicationListVO.getGuaranteeType());
			// 用户是否登陆
			loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
			
			// 借款人基本信息
			CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByLoanApplicationId(loanApplicationListVO
					.getLoanApplicationId());
			if (basicSnapshot != null) {
				resultMap.put("sex", basicSnapshot.getSex());
				resultMap.put("maxCreditValue", null== basicSnapshot.getMaxCreditValue()?"":df.format(basicSnapshot.getMaxCreditValue()) + "元");// 信用卡额度
				resultMap.put("isMarried", basicSnapshot.getIsMarried());// 婚姻状况
				resultMap.put("childStatus", basicSnapshot.getChildStatus());// 子女状况
				resultMap.put("monthlyIncome", basicSnapshot.getMonthlyIncome()==null?"":df.format(Long.valueOf(basicSnapshot.getMonthlyIncome())) + "元");// 月平均收入
				resultMap.put("education", basicSnapshot.getEducation());// 最高学历
				// 居住地
				AddressVO address = addressService.getAddressVOById(basicSnapshot.getResidenceAddr());
				resultMap.put("address", address.getProvinceStr() + address.getCityStr() + address.getDistrictStr() + address.getDetail());
			}
			// 发标信息
			LoanPublish loanPublish = loanPublishService.findLoanPublishVO(loanApplicationNo);
			if(loanPublish.getAssessValue() != null){
				resultMap.put("assessValue", df.format(loanPublish.getAssessValue()) + "万元");// 总评估值
			}else {
				resultMap.put("assessValue", df.format(0) + "万元");// 总评估值
			}
			if(loanPublish.getMarketValue() != null){
				resultMap.put("marketValue", df.format(loanPublish.getMarketValue()) + "万元");// 市值
			}else {
				resultMap.put("marketValue", df.format(0) + "万元");// 市值
			}
			if(loanPublish.getHourseSize() != null){
				resultMap.put("hourseSize", df.format(loanPublish.getHourseSize()) + "平方米");// 房屋面积
			}else {
				resultMap.put("hourseSize", df.format(0) + "平方米");// 房屋面积
			}
			
			resultMap.put("hourseDesc", loanPublish.getHourseDesc());// 抵押物说明
			
			// 认证报告
			String authInfos = loanPublish.getAuthInfos();
			if (StringUtils.isNotEmpty(authInfos)) {
				resultMap.put("authInfo", authInfos.split(","));
			}
			
			resultMap.put("loanApplicationTitle", loanApplicationListVO.getLoanApplicationTitle());// 标题
			resultMap.put("expectProfit", String.valueOf(this.getExpectProfit(loanApplicationListVO.getLoanApplicationId(), new BigDecimal("100"))));// 计算预期收益（以100为基数）
			resultMap.put("awardProfit", String.valueOf(this.getAwardProfit(loanApplicationListVO.getLoanApplicationId(), new BigDecimal("100"))));//奖励收益（以100为基数）
			resultMap.put("loanType", loanApplicationListVO.getLoanType());// 借款类型
			resultMap.put("annualRate", String.valueOf(loanApplicationListVO.getAnnualRate()));// 预期年化收益
			resultMap.put("rewardsPercent", String.valueOf(loanApplicationListVO.getRewardsPercent()));// 奖励利率
			resultMap.put("cycleCount", String.valueOf(loanApplicationListVO.getCycleCount()));// 期限
			resultMap.put("confirmBalance", String.valueOf(loanApplicationListVO.getConfirmBalance()));// 借款金额
			resultMap.put("startAmount", String.valueOf(loanApplicationListVO.getStartAmount()));// 起投金额
			resultMap.put("desc", loanApplicationListVO.getDesc());// 借款描述
			resultMap.put("applicationState", loanApplicationListVO.getApplicationState());// 标的状态
			resultMap.put("begin", loanApplicationListVO.isBegin());// 是否余热仲
			resultMap.put("remain", String.valueOf(loanApplicationListVO.getRemain()));// 剩余金额
			resultMap.put("maxBuyBalanceNow", String.valueOf(loanApplicationListVO.getMaxBuyBalanceNow()));// 限投
			resultMap.put("ratePercent", loanApplicationListVO.getRatePercent());// 进度
			resultMap.put("loanUseage", loanApplicationListVO.getLoanUseage());// 借款用途
			if(null != loanApplicationListVO.getLoanUseage()){
				ConstantDefine cdf = new ConstantDefine();
				cdf.setConstantValue(loanApplicationListVO.getLoanUseage());
				cdf.setConstantTypeCode("loanUseage");
				ConstantDefine cdfRest = constantdefineservice.findConstantByTypeCodeAndValue(cdf);
				if(null != cdfRest){
					resultMap.put("loanUseage", cdfRest.getConstantName());// 借款用途
				}
			}
			resultMap.put("useageDesc", loanApplicationListVO.getUseageDesc());// 用途描述
			
			// 附件快照（企业标 个人标 通用）
			List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(
					loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
			int customerSnapshots = customerUploadSnapshots.size();
//			String basepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			String[] customerThumbnail = new String[customerSnapshots];
			String[] customerFileName = new String[customerSnapshots];
			for (int i = 0; i < customerSnapshots; i++) {
				customerThumbnail[i] = this.getWWW_BASEPATH(request) + customerUploadSnapshots.get(i).getAttachment().getThumbnailUrl();
				customerFileName[i] = customerUploadSnapshots.get(i).getAttachment().getFileName();
			}
			resultMap.put("customerThumbnail", customerThumbnail);// 缩略图
			resultMap.put("customerFileName", customerFileName);// 附件名
			resultMap.put("riskControlInformation", loanApplicationListVO.getRiskControlInformation());//风控步骤
			if(LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())){
				//个人投标详情页
				// 房产抵押
				CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
				String mortgageTypeStr = "";
				if("1".equals(house.getMortgageType())){
					mortgageTypeStr = "一抵";
				}else if ("2".equals(house.getMortgageType())) {
					mortgageTypeStr = "二抵";
				}
				resultMap.put("mortgageType", mortgageTypeStr);// 抵押物类型
				
				AddressVO hAdd = addressService.getAddressVOById(loanPublish.getHourseAddress());
				resultMap.put("houseAddress", null == hAdd ? "" : hAdd.getProvinceStr() + hAdd.getCityStr() + hAdd.getDistrictStr() + hAdd.getDetail());// 房屋地址
			} else {
				// 企业投标详情页
				enterpriseLoanApplication(request, resultMap, loanApplicationListVO, loanPublish);
			}
			 
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
		return returnResultMap(true, resultMap, null, null);
	}
	
	/**
	 * 【APP接口工具】
	 * 企业投标详情页
	 * @param request
	 * @param loanApplicationListVO
	 * @param loanPublish
	 */
	private void enterpriseLoanApplication(HttpServletRequest request,Map<String, Object> resultMap, LoanApplicationListVO loanApplicationListVO, LoanPublish loanPublish) {
		//借款企业
		EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
		EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
		
		DecimalFormat df = new DecimalFormat("#,##0.00");
		resultMap.put("enterpriseName",enterpriseInfo.getEnterpriseName());//还款来源
		resultMap.put("jmEnterpriseName",enterpriseInfo.getJmEnterpriseName());//加密企业名称
		resultMap.put("jmOrganizationCode",enterpriseInfo.getJmOrganizationCode());//加密组织机构代码
		resultMap.put("operatingPeriod",String.valueOf(enterpriseInfo.getOperatingPeriod()));//精英年限
		resultMap.put("registeredCapital",df.format(enterpriseInfo.getRegisteredCapital()));//注册资金
		resultMap.put("information",enterpriseInfo.getInformation());//企业信息
		
		ConstantDefine contant = new ConstantDefine();
		contant.setConstantValue(loanApplicationListVO.getLoanUseage());
		contant.setConstantTypeCode("enterpriseLoanUseage");
		ConstantDefine cd = constantdefineservice.findConstantByTypeCodeAndValue(contant);
		if(null != cd){
			resultMap.put("loanUseage",cd.getConstantName());// 借款用途
		}
		
		//企业图
		List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
		int enterpriseSnapshots = enterpriseInfoSnapshots.size();
//		String basepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String[] enterpriseThumbnail = new String[enterpriseSnapshots];
		String[] enterpriseFileName = new String[enterpriseSnapshots];
		for (int i = 0; i < enterpriseSnapshots; i++) {
			enterpriseThumbnail[i] = this.getWWW_BASEPATH(request) + enterpriseInfoSnapshots.get(i).getAttachment().getThumbnailUrl();
			enterpriseFileName[i] = enterpriseInfoSnapshots.get(i).getAttachment().getFileName();
		}
		resultMap.put("enterpriseThumbnail", enterpriseThumbnail);// 公司证明缩略图
		resultMap.put("enterpriseFileName", enterpriseFileName);// 公司证明附件名
		
		switch (loanApplicationListVO.getLoanType()){
			case "2":
				//企业车贷
				EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				String address = getAddress(enterpriseCarLoanSnapshot.getProvince(),enterpriseCarLoanSnapshot.getCity());
				BigDecimal totalPrice= mortgageCarSnapshotService.getTotalPriceByCardLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
				resultMap.put("totalPrice",String.valueOf(totalPrice));//汽车列表： 总计
				resultMap.put("address",address);
				//resultMap.put("enterpriseCarLoanSnapshot",enterpriseCarLoanSnapshot);
				
				resultMap.put("creditLimit",enterpriseCarLoanSnapshot.getCreditLimit()==null?"":String.valueOf(enterpriseCarLoanSnapshot.getCreditLimit()));// 授信上限
				resultMap.put("creditLimitRate",enterpriseCarLoanSnapshot.getCreditLimitRate()==null?"":String.valueOf(enterpriseCarLoanSnapshot.getCreditLimitRate()));// 授信比例
				resultMap.put("mortgageDescription",enterpriseCarLoanSnapshot.getMortgageDescription()==null?"":enterpriseCarLoanSnapshot.getMortgageDescription());// 描述
				resultMap.put("projectDescription",enterpriseCarLoanSnapshot.getProjectDescription());//项目描述
				resultMap.put("riskControlInformation",enterpriseCarLoanSnapshot.getRiskControlInformation());//风控信息
				
				contant.setConstantValue(enterpriseCarLoanSnapshot.getInternalRating());
				contant.setConstantTypeCode("internalRating");
				ConstantDefine cd1 = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				resultMap.put("internalRating", null == cd1 ? "" : cd1.getConstantName());// 内部评级
				
				//抵押信息
				List<APPMortgageCarSnapshotVO> carLists = getCardList( loanApplicationListVO.getLoanApplicationId());
				resultMap.put("carLists", carLists);
				
				break;
			case "3":
				//企业信贷
				EnterpriseCreditSnapshot enterpriseCreditSnapshot = enterpriseCreditSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				address = getAddress(enterpriseCreditSnapshot.getProvince(),enterpriseCreditSnapshot.getCity());
				//resultMap.put("enterpriseCarLoanSnapshot",enterpriseCreditSnapshot);
				resultMap.put("address",address);
				
				contant.setConstantValue(enterpriseCreditSnapshot.getInternalRating());
				contant.setConstantTypeCode("internalRating");
				ConstantDefine cd2 = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				resultMap.put("internalRating", null == cd2 ? "" : cd2.getConstantName());// 内部评级
				
				resultMap.put("projectDescription",enterpriseCreditSnapshot.getProjectDescription());//项目描述
				resultMap.put("riskControlInformation",enterpriseCreditSnapshot.getRiskControlInformation());//风控信息
				break;
			case "4":
				//企业保理
				EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				resultMap.put("enterpriseName",factoringSnapshot.getSourceOfRepayment());//还款来源
				resultMap.put("loanBalance", df.format(loanApplicationListVO.getLoanBalance()) + "元");//授信金额
				resultMap.put("financingAmount", df.format(factoringSnapshot.getFinancingAmount()) + "元");//融资金额
				resultMap.put("accountReceivableDescription",factoringSnapshot.getAccountReceivableDescription());//应收账款说明
				resultMap.put("moneyRiskAssessment",factoringSnapshot.getMoneyRiskAssessment());//款项风险评价
				resultMap.put("projectComprehensiveEvaluati",factoringSnapshot.getProjectComprehensiveEvaluati());//项目综合评价
				resultMap.put("fieldAdjustmentValue","0".equals(factoringSnapshot.getFieldAdjustmentMark())? factoringSnapshot.getFieldAdjustmentValue():"");// 360度实地尽调－大数据思维保障项目质量
				resultMap.put("repaymentGuaranteeValue","0".equals(factoringSnapshot.getRepaymentGuaranteeMark())?factoringSnapshot.getRepaymentGuaranteeValue():"");//还款保证金 - 构建风险缓释空间
				resultMap.put("aidFundValue","0".equals(factoringSnapshot.getAidFundMark())? factoringSnapshot.getAidFundValue():"");// 法律援助基金 - 平台资金支持护航维权启动
				//融资方
				CoLtd financeParty = coLtdService.getCoLtdById(factoringSnapshot.getFinancingParty());
				resultMap.put("companyNameStr",financeParty.getCompanyNameStr());// 融资方
				break;
			case "5":
				//企业基金
				EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				if(null != foundationSnapshot){
					ConstantDefine cdf = new ConstantDefine();
					cdf.setConstantValue(foundationSnapshot.getInvestmentType());
					cdf.setConstantTypeCode("INVESTMENT_TYPE");
					ConstantDefine cdfRest = constantdefineservice.findConstantByTypeCodeAndValue(cdf);
					if(null != cdfRest){
						resultMap.put("investmentTypeName", cdfRest.getConstantName());//定向委托投资标的
					}else {
						resultMap.put("investmentTypeName", "");
					}
				}else {
					resultMap.put("investmentTypeName", "");
				}
				
				//托管机构
				CoLtd coltd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
				if(null != coltd){
					resultMap.put("coltdCompanyName", coltd.getCompanyName());//托管机构
				}else {
					resultMap.put("coltdCompanyName", "");
				}
				
				//标的详情说明
				Attachment attachment = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getAttachId());
				if(null != attachment){
					resultMap.put("attachmentUrl", this.getWWW_BASEPATH(request) + attachment.getUrl());//标的说明 下载地址
				}else {
					resultMap.put("attachmentUrl", "");
				}
				
				//交易说明书
				Attachment tradeBook = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getTradeBookId());
				if(null != tradeBook){
					resultMap.put("tradeBookUrl", this.getWWW_BASEPATH(request) + tradeBook.getUrl());//查看交易说明书 下载地址
				}else {
					resultMap.put("tradeBookUrl", "");
				}
				
				//风险提示函
				Attachment riskTip = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getRiskTipId());
				if(null != riskTip){
					resultMap.put("riskTipUrl", this.getWWW_BASEPATH(request) + riskTip.getUrl());//风险提示函 下载地址
				}else {
					resultMap.put("riskTipUrl", "");
				}
				
				//免责声明 下载地址
				resultMap.put("disclaimerUrl", this.getWWW_BASEPATH(request) + "/finance/download/disclaimer");
				
				// 下面是借款详情模块的字段
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				if(null != loanApplicationListVO.getPaymentDate()){
					resultMap.put("paymentDate", sdf.format(loanApplicationListVO.getPaymentDate()));//收益起始日
				}else {
					resultMap.put("paymentDate", "资金筹集完成时间");
				}
				
				if(null != loanApplicationListVO.getLastRepaymentDate()){
					resultMap.put("lastRepaymentDate", sdf.format(loanApplicationListVO.getLastRepaymentDate()));//收益到期日 和  预期收款日
				}else {
					resultMap.put("lastRepaymentDate", "资金筹集完成后显示");
				}
				
				break;
			case "6":
				//企业贷
				EnterprisePledgeSnapshot enterprisePledgeSnapshot = enterprisePledgeSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				address = getAddress(enterprisePledgeSnapshot.getProvince(), enterprisePledgeSnapshot.getCity());
				resultMap.put("address",address);
				contant.setConstantValue(enterprisePledgeSnapshot.getInternalRating());
				contant.setConstantTypeCode("internalRating");
				ConstantDefine cd6 = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				resultMap.put("internalRating", null == cd6 ? "" : cd6.getConstantName());// 内部评级
				resultMap.put("projectDescription", enterprisePledgeSnapshot.getProjectDescription());//项目描述
				resultMap.put("riskControlInformation", enterprisePledgeSnapshot.getRiskControlInformation());//风控信息
				break;
		}

	}
	
	/**
	 * 【APP接口工具】
	 * 汽车列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCardList")// [APP]
	@ResponseBody
	@DoNotNeedLogin
	public List<APPMortgageCarSnapshotVO> getCardList(Long loanApplicationId) {
		// 获取借款申请状态
		EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplicationId);
		if (enterpriseCarLoanSnapshot == null)
			return null;
		MortgageCarSnapshot mortgageCarSnapshot = new MortgageCarSnapshot();
		mortgageCarSnapshot.setCarLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
		List<MortgageCarSnapshot> carLists = mortgageCarSnapshotService.getMortgageCarSnapshotList(mortgageCarSnapshot);
		List<APPMortgageCarSnapshotVO> resultLists = new ArrayList<APPMortgageCarSnapshotVO>();
		for (int i = 0; i < carLists.size(); i++) {
			APPMortgageCarSnapshotVO vo = new APPMortgageCarSnapshotVO();
			MortgageCarSnapshot car = carLists.get(i);
			vo.setArrived(car.getArrived());
			vo.setAutomobileBrand(car.getAutomobileBrand());
			vo.setCarModel(car.getCarModel());
			vo.setChangeDesc(null == car.getChangeDesc() ? "" : car.getChangeDesc());
			vo.setFrameNumber(car.getFrameNumber());
			vo.setMarketPrice(String.valueOf(car.getMarketPrice()));
			resultLists.add(vo);
		}
		return resultLists;
	}

	/**
	 * 【APP接口工具】
	 * @param provinceId
	 * @param cityId
	 * @return
	 */
	private String getAddress(Long provinceId,Long cityId) {//[APP]
		ProvinceInfo province =null ;
		CityInfo city = null;
		String address = "";
		if (provinceId!=null) {
            province = provinceInfoService.findById(provinceId);
        }
		if (cityId!=null){
            city = cityInfoService.findById(cityId);
        }
		if (province==null) return null;

		if (city==null)
			return province.getProvinceName();

		if (city.getCityName().equals(province.getProvinceName())){
			address = city.getCityName();
		}else {
			address = province.getProvinceName()+city.getCityName();
		}
		return address;
	}
	
	/**
	 * 【APP接口】
	 * 已投债权，分页查询列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCreditRightList", method = RequestMethod.POST)//[APP]
	@ResponseBody
	public Object getCreditRightList(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "creditorRightsStatus" ,defaultValue = "", required = false) String creditorRightsStatus,
			@RequestParam(value = "backDate" ,defaultValue = "", required = false) String backDate,
			@RequestParam(value = "buyDate" ,defaultValue = "", required = false) String buyDate) {
		
		try {
			//登录验证
			UserInfo currentUser = getCurrentUser(request);
			if(null == currentUser){
				return returnResultMap(false, null, "needlogin", "请先登录");
			}
			
			CreditorRightsExtVo vo = new CreditorRightsExtVo();
			vo.setLendUserId(currentUser.getUserId());
//			vo.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());//购买来源(注意：债权转让上线，所以查询所有的)
			vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

			//封装参数
			Map<String, Object> customParams = new HashMap<String, Object>();

			String[] queryStatus = getQueryStatus(creditorRightsStatus);
			if(queryStatus != null){
				customParams.put("queryStatus",queryStatus);
			}
			customParams.put("orderBy",getOrderBy(backDate, buyDate));
			Pagination<CreditorRightsExtVo> pagination = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);
			
			//返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            List<CreditorRightsExtVO> creVOs = new ArrayList<>();
            CreditorRightsExtVO creVO = null;
            DecimalFormat df = new DecimalFormat("0.00");
            
            List<CreditorRightsExtVo> crevList = pagination.getRows();
            if(null != crevList && crevList.size() > 0){
            	for (CreditorRightsExtVo v : crevList) {
            		creVO = new CreditorRightsExtVO();
            		
            		creVO.setCreditorRightsName(v.getCreditorRightsName());//债权名称(借款标题)
            		creVO.setLoanLoginName(v.getLoanLoginName());//借款人
            		creVO.setBuyPrice(df.format(v.getBuyPrice()));//出借金额（元）
            		creVO.setWaitTotalpayMent(df.format(v.getWaitTotalpayMent()));//待收回款（元）
            		creVO.setFactBalance(df.format(v.getFactBalance()));//已收回款（元）
            		creVO.setCurrentPayDate(DateUtil.getDateLong(v.getCurrentPayDate()));//最近回款日
            		if(CreditorRightsStateEnum.EFFECTIVE.getValue().equals(String.valueOf(v.getRightsState())) 
            				|| CreditorRightsStateEnum.TRANSFERING.getValue().equals(String.valueOf(v.getRightsState()))){
            			creVO.setRightsState("回款中");//债权状态:如果值为 0已生效 或 8转让中，则统一显示 回款中
            		}else if(CreditorRightsStateEnum.TURNOUT.getValue().equals(String.valueOf(v.getRightsState()))){
            			creVO.setRightsState("已结清");//债权状态:如果值为 2已转出，则统一显示 3已结清
            		}else{
            			creVO.setRightsState(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(v.getRightsState())).getDesc());//债权状态
            		}
            		creVO.setBuyDate(DateUtil.getDateLong(v.getBuyDate()));//投标日期
            		creVO.setExpectProfit(df.format(v.getExpectProfit()));//预期收益
            		creVO.setCreditorRightsId(String.valueOf(v.getCreditorRightsId()));//债权ID
            		creVO.setCycleCount(v.getLoanApplicationListVO().getCycleCount() + "个月");//出借时长
            		creVO.setAnnualRate(v.getLoanApplicationListVO().getAnnualRate() + "%");//年化利率
            		creVO.setRepayMentMethod(v.getLoanApplicationListVO().getRepayMentMethod());//还款方式
            		creVO.setCreditorRightsApplyId(String.valueOf(v.getCreditorRightsApplyId()));//债权转让申请ID
            		
            		// 距离到期多少天
            		List<RightsRepaymentDetail> details = v.getRightsRepaymentDetailList();
            		if(null != details && details.size() > 0){
            			int daysBetween = DateUtil.daysBetween(new Date(), details.get((details.size()-1)).getRepaymentDayPlanned());
            			creVO.setExpireDays(String.valueOf(daysBetween));
            		}
            		creVO.setFromWhere(String.valueOf(v.getFromWhere()));//1购买；2转让
            		
            		creVOs.add(creVO);
				}
            }
            
            resultMap.put("pageSize", pagination.getPageSize());
            resultMap.put("pageNo", pagination.getCurrentPage());
            resultMap.put("total", pagination.getTotal());
            resultMap.put("rows", creVOs);
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
	/**
	 * 【APP接口】
	 * 已投债权，查询详情
	 * @param request
	 * @param creditorRightsId
	 * @param creditorRightsApplyId
	 * @return
	 */
	@RequestMapping(value = "/getCreditRightDetail", method = RequestMethod.POST)//[APP]
	@ResponseBody
	public Object getCreditRightDetail(HttpServletRequest request,
			@RequestParam(value = "creditorRightsId", defaultValue = "") String creditorRightsId,
			@RequestParam(value = "creditorRightsApplyId", defaultValue = "") String creditorRightsApplyId) {
		
		try {
			//登录验证
			UserInfo currentUser = getCurrentUser(request);
			if(null == currentUser){
				return returnResultMap(false, null, "needlogin", "请先登录");
			}
			//参数验证
			if(null == creditorRightsId || "".equals(creditorRightsId)){
				return returnResultMap(false, null, "check", "参数不能为空");
			}
			//根据债权ID加载一条债权数据
			CreditorRightsExtVo vo = creditorRightsService.getCreditorRightsDetailById(Long.valueOf(creditorRightsId));
			if(null == vo){
				return returnResultMap(false, null, "check", "参数不合法");
			}
			
			// 【获取数据-开始】
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
            //需要计算回款时的费用项
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
            List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
            //最近还款日期
            try {
                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("最近还款日期计算出错", e);
            }
            
            //处理一下明细，添加待缴费用
            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), currentUser.getUserId());
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
            
            //获取投标类待回款(利息+本金)
            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(vo.getLendOrderId(), currentUser.getUserId(), LendProductTypeEnum.RIGHTING);
            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(vo.getLendOrderId(), currentUser.getUserId(), LendProductTypeEnum.RIGHTING);
            vo.setRightsRepaymentDetailList(detailRightsList);
            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
            //计算预期收益
            vo.setExpectProfit(exceptProfit);
            
            //如果来源是转让的标，将出借时长，改为剩余债权还款明细的条数
            if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
				vo.getLoanApplicationListVO().setCycleCount(detailRightsList.size());
			}
            
			// 【获取数据-结束】
            
			//返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            CreditorRightsExtVO creVO = new CreditorRightsExtVO();
            DecimalFormat df = new DecimalFormat("0.00");
            //债权详情
            creVO.setCreditorRightsName(vo.getCreditorRightsName());//债权名称(借款标题)
    		creVO.setLoanLoginName(vo.getLoanLoginName());//借款人
    		creVO.setBuyPrice(df.format(vo.getBuyPrice()));//出借金额（元）
    		creVO.setWaitTotalpayMent(df.format(vo.getWaitTotalpayMent()));//待收回款（元）
    		creVO.setFactBalance(df.format(vo.getFactBalance()));//已收回款（元）
    		creVO.setCurrentPayDate(DateUtil.getDateLong(vo.getCurrentPayDate()));//最近回款日
    		if(CreditorRightsStateEnum.EFFECTIVE.getValue().equals(String.valueOf(vo.getRightsState())) 
    				|| CreditorRightsStateEnum.TRANSFERING.getValue().equals(String.valueOf(vo.getRightsState()))){
    			creVO.setRightsState("回款中");//债权状态:如果值为 0已生效 或 8转让中，则统一显示 回款中
    		}else if(CreditorRightsStateEnum.TURNOUT.getValue().equals(String.valueOf(vo.getRightsState()))){
    			creVO.setRightsState("已结清");//债权状态:如果值为 2已转出，则统一显示 3已结清
    		}else{
    			creVO.setRightsState(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(vo.getRightsState())).getDesc());//债权状态
    		}
    		creVO.setBuyDate(DateUtil.getDateLong(vo.getBuyDate()));//投标日期
    		creVO.setExpectProfit(df.format(vo.getExpectProfit()));//预期收益
    		creVO.setCreditorRightsId(String.valueOf(vo.getCreditorRightsId()));//债权ID
    		creVO.setCycleCount(vo.getLoanApplicationListVO().getCycleCount() + "个月");//出借时长
    		creVO.setAnnualRate(vo.getLoanApplicationListVO().getAnnualRate() + "%");//年化利率
    		creVO.setRepayMentMethod(vo.getLoanApplicationListVO().getRepayMentMethod());//还款方式
    		
//    		//规则：购买标 ||（债权转让标 && ！奖励发放时机是放款）
//    		if(CreditorRightsFromWhereEnum.BUY.getValue().equals(String.valueOf(vo.getFromWhere())) 
//    				|| (CreditorRightsFromWhereEnum.TURN.getValue().equals(String.valueOf(vo.getFromWhere())) && !AwardPointEnum.ATMAKELOAN.getValue().equals(vo.getAwardPoint()))){
//    			creVO.setAwardRate(vo.getAwardRate() + "%");//奖励利率
//        		creVO.setAwardExpectProfit(df.format(this.getAwardProfit(vo.getLoanApplicationListVO().getLoanApplicationId(), vo.getBuyPrice())));//奖励预期收益
//    		}else {
//    			creVO.setAwardRate("0%");
//        		creVO.setAwardExpectProfit("0.00");
//			}
    		
    		//规则：购买标 ||（债权转让标 && ！奖励发放时机是放款）
    		String awardRate = "0%";//奖励利率
    		String awardExpectProfit = "0.00";//奖励预期收益
    		if((CreditorRightsFromWhereEnum.TURN.getValue().equals(String.valueOf(vo.getFromWhere())) && !AwardPointEnum.ATMAKELOAN.getValue().equals(vo.getAwardPoint()))){
    			if(null != vo.getAwardRate() && !"".equals(vo.getAwardRate())){
    				awardRate = vo.getAwardRate()+"%";
    				if(null != creditorRightsApplyId && !"".equals(creditorRightsApplyId) && !"null".equals(creditorRightsApplyId)){
    					awardExpectProfit = df.format(new BigDecimal(this.getAwardRightProfit(Long.valueOf(creditorRightsApplyId), vo.getBuyPrice())));
    				}
    			}
    		}else if (CreditorRightsFromWhereEnum.BUY.getValue().equals(String.valueOf(vo.getFromWhere()))) {
    			if(null != vo.getAwardRate() && !"".equals(vo.getAwardRate())){
    				awardRate = vo.getAwardRate()+"%";
    				awardExpectProfit = df.format(this.getAwardProfit(vo.getLoanApplicationListVO().getLoanApplicationId(), vo.getBuyPrice()));				
    			}
			}
    		creVO.setAwardRate(awardRate);
        	creVO.setAwardExpectProfit(awardExpectProfit);
        	
    		
            //回款列表
    		List<RightsRepaymentDetailVO> detailVOList = new ArrayList<RightsRepaymentDetailVO>();
    		RightsRepaymentDetailVO detailVO = null;
    		BigDecimal patentedProfit = BigDecimal.ZERO;//存储实际收益总和
    		if(null != detailRightsList && detailRightsList.size() > 0){
    			
    			//存储后期累计还款金额
    			BigDecimal ljAmount = BigDecimal.ZERO;
    			//提前还款 当期标记
                int tag = 0;
    			for (int i = 0; i < detailRightsList.size(); i++) {
    				if(RightsRepaymentDetailStateEnum.BEFORE_COMPLETE.getValue().equals(String.valueOf(detailRightsList.get(i).getRightsDetailState()))){
    					ljAmount = ljAmount.add(detailRightsList.get(i).getShouldCapital2());
    				}else {
						tag = i;
					}
				}
    			
    			RightsRepaymentDetail detail = null;
    			for (int i = 0; i < detailRightsList.size(); i++) {
    				detail = new RightsRepaymentDetail();
    				detailVO = new RightsRepaymentDetailVO();
    				detail = detailRightsList.get(i);
					
    				detailVO.setSectionCode(String.valueOf(detail.getSectionCode()));//回款期
    				detailVO.setRepaymentDayPlanned(DateUtil.getDateLong(detail.getRepaymentDayPlanned()));//回款日期
    				detailVO.setShouldCapital2(df.format(detail.getShouldCapital2()));//应回本金（元）
    				detailVO.setShouldInterest2(df.format(detail.getShouldInterest2()));//应回利息（元）
    				detailVO.setDefaultInterest(df.format(detail.getDefaultInterest()));//罚息（元）
    				detailVO.setShouldFee(df.format(detail.getShouldFee()));//应缴费用（元）
//    				detailVO.setAllBackMoney(df.format(detail.getShouldCapital2().add(detail.getShouldInterest2()).add(detail.getDefaultInterest()).subtract(detail.getShouldFee())));//应回款总额（元）
    				detailVO.setAllBackMoney(df.format(detail.getShouldCapital2().add(detail.getShouldInterest2()).add(detail.getDefaultInterest())));//应回款总额（元）
    				detailVO.setFactMoney(df.format(detail.getFactBalance().add(detail.getDepalFine())));//已回款总额（元）
    				detailVO.setRightsDetailState(RightsRepaymentDetailStateEnum.getRightsRepaymentDetailStateEnumByValue(String.valueOf(detail.getRightsDetailState())).getDesc());//状态
    				
    				//【开始】处理提前还款
    				if(RightsRepaymentDetailStateEnum.BEFORE_COMPLETE.getValue().equals(String.valueOf(detail.getRightsDetailState()))){
    					detailVO.setShouldFee("--");//[提前还款特殊处理]应缴费用（元）
        				detailVO.setFactMoney("--");//[提前还款特殊处理]已回款总额（元）
        				ljAmount = ljAmount.add(detail.getShouldCapital2());
    				}else {
    					if(tag == i){
    						detailVO.setFactMoney(df.format(detail.getFactBalance().add(ljAmount).add(detail.getDepalFine())));//[提前还款之前的特殊处理]已回款总额（元）
    					}
					}
    				//【结束】处理提前还款
    				
    				detailVOList.add(detailVO);
    				
    				//如果本期已结清，累加实际收益总和
    				if(RightsRepaymentDetailStateEnum.COMPLETE.getValue().equals(String.valueOf(detail.getRightsDetailState()))){
    					patentedProfit = patentedProfit.add(detail.getShouldInterest2().subtract(detail.getShouldFee()));
    				}
				}
    		}
    		creVO.setPatentedProfit(String.valueOf(patentedProfit));//实际收益总和 = 应回利息（shouldInterest2） - 应缴费用（shouldFee）
    		
    		//计算实际奖励收益
    		BigDecimal awardBalance = BigDecimal.ZERO;
    		if(CreditorRightsFromWhereEnum.BUY.getValue().equals(String.valueOf(vo.getFromWhere())) 
    				|| (CreditorRightsFromWhereEnum.TURN.getValue().equals(String.valueOf(vo.getFromWhere())) && !AwardPointEnum.ATMAKELOAN.getValue().equals(vo.getAwardPoint()))){
    			List<AwardDetail> awardDetailList = awardDetailService.findByLendOrderId(vo.getLendOrderId());
        		if(null != awardDetailList && awardDetailList.size() > 0){
        			for (AwardDetail ad : awardDetailList) {
        				awardBalance = awardBalance.add(ad.getAwardBalance());
    				}
        		}
    		}
    		creVO.setPatentedAwardProfit(df.format(awardBalance));//实际奖励收益总和
    		
    		resultMap.put("creditorRightsDetail", creVO);
            resultMap.put("repaymentList", detailVOList);
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
	/**
	 * 【APP接口】【注意：提前还款修改之前的备份】
	 * 已投债权，查询详情
	 * @param request
	 * @param creditorRightsId
	 * @return
	 */
	@RequestMapping(value = "/getCreditRightDetail_Bak", method = RequestMethod.POST)//[APP]
	@ResponseBody
	public Object getCreditRightDetail_Bak(HttpServletRequest request,
			@RequestParam(value = "creditorRightsId", defaultValue = "") String creditorRightsId) {
		
		try {
			//登录验证
			UserInfo currentUser = getCurrentUser(request);
			if(null == currentUser){
				return returnResultMap(false, null, "needlogin", "请先登录");
			}
			//参数验证
			if(null == creditorRightsId || "".equals(creditorRightsId)){
				return returnResultMap(false, null, "check", "参数不能为空");
			}
			//根据债权ID加载一条债权数据
			CreditorRightsExtVo vo = creditorRightsService.getCreditorRightsDetailById(Long.valueOf(creditorRightsId));
			if(null == vo){
				return returnResultMap(false, null, "check", "参数不合法");
			}
			
			// 【获取数据-开始】
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
            //需要计算回款时的费用项
            List<LendProductFeesItem> atcycleFeeitems = new ArrayList<LendProductFeesItem>();
            for (LendProductFeesItem feesItem : feeitems) {
                if (FeesPointEnum.ATCYCLE.value2Char() == feesItem.getChargePoint()) {
                    atcycleFeeitems.add(feesItem);
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
            List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
            //最近还款日期
            try {
                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("最近还款日期计算出错", e);
            }
            //处理一下明细，添加待缴费用
            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), currentUser.getUserId());
            for (RightsRepaymentDetail detail : detailRightsList) {
                //计算费用
                BigDecimal fees = BigDecimal.ZERO;
                if (atcycleFeeitems!=null&&atcycleFeeitems.size()>0){
                    for (LendProductFeesItem item : atcycleFeeitems) {
                        BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                        fees = fees.add(fee);
                    }
                }
                if(fis!=null&&fis.size()>0){
                    for(FeesItem fi:fis){
                        BigDecimal fee = feesItemService.calculateFeesBalance(fi.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
                                detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                        fees = fees.add(fee);
                    }
                }
                detail.setShouldFee(BigDecimalUtil.up(fees,2));
                BigDecimal defaultInterest = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(detail.getRepaymentPlanId());
                detail.setDefaultInterest(defaultInterest);
            }
            //获取投标类待回款(利息+本金)
            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(vo.getLendOrderId(), currentUser.getUserId(), LendProductTypeEnum.RIGHTING);
            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(vo.getLendOrderId(), currentUser.getUserId(), LendProductTypeEnum.RIGHTING);
            vo.setRightsRepaymentDetailList(detailRightsList);
            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
            //计算预期收益
            vo.setExpectProfit(exceptProfit);
            
            //如果来源是转让的标，将出借时长，改为剩余债权还款明细的条数
            if (String.valueOf(vo.getFromWhere()).equals(CreditorRightsFromWhereEnum.TURN.getValue())) {
				vo.getLoanApplicationListVO().setCycleCount(detailRightsList.size());
			}
            
			// 【获取数据-结束】
            
			//返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            CreditorRightsExtVO creVO = new CreditorRightsExtVO();
            DecimalFormat df = new DecimalFormat("0.00");
            //债权详情
            creVO.setCreditorRightsName(vo.getCreditorRightsName());//债权名称(借款标题)
    		creVO.setLoanLoginName(vo.getLoanLoginName());//借款人
    		creVO.setBuyPrice(df.format(vo.getBuyPrice()));//出借金额（元）
    		creVO.setWaitTotalpayMent(df.format(vo.getWaitTotalpayMent()));//待收回款（元）
    		creVO.setFactBalance(df.format(vo.getFactBalance()));//已收回款（元）
    		creVO.setCurrentPayDate(DateUtil.getDateLong(vo.getCurrentPayDate()));//最近回款日
    		if(CreditorRightsStateEnum.EFFECTIVE.getValue().equals(String.valueOf(vo.getRightsState())) 
    				|| CreditorRightsStateEnum.TRANSFERING.getValue().equals(String.valueOf(vo.getRightsState()))){
    			creVO.setRightsState("回款中");//债权状态:如果值为 0已生效 或 8转让中，则统一显示 回款中
    		}else if(CreditorRightsStateEnum.TURNOUT.getValue().equals(String.valueOf(vo.getRightsState()))){
    			creVO.setRightsState("已结清");//债权状态:如果值为 2已转出，则统一显示 3已结清
    		}else{
    			creVO.setRightsState(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(vo.getRightsState())).getDesc());//债权状态
    		}
    		creVO.setBuyDate(DateUtil.getDateLong(vo.getBuyDate()));//投标日期
    		creVO.setExpectProfit(df.format(vo.getExpectProfit()));//预期收益
    		creVO.setCreditorRightsId(String.valueOf(vo.getCreditorRightsId()));//债权ID
    		creVO.setCycleCount(vo.getLoanApplicationListVO().getCycleCount() + "个月");//出借时长
    		creVO.setAnnualRate(vo.getLoanApplicationListVO().getAnnualRate() + "%");//年化利率
    		creVO.setRepayMentMethod(vo.getLoanApplicationListVO().getRepayMentMethod());//还款方式
            //回款列表
    		List<RightsRepaymentDetailVO> detailVOList = new ArrayList<RightsRepaymentDetailVO>();
    		RightsRepaymentDetailVO detailVO = null;
    		BigDecimal patentedProfit = BigDecimal.ZERO;//存储实际收益总和
    		if(null != detailRightsList && detailRightsList.size() > 0){
    			for (RightsRepaymentDetail detail : detailRightsList) {
    				detailVO = new RightsRepaymentDetailVO();
					
    				detailVO.setSectionCode(String.valueOf(detail.getSectionCode()));//回款期
    				detailVO.setRepaymentDayPlanned(DateUtil.getDateLong(detail.getRepaymentDayPlanned()));//回款日期
    				detailVO.setShouldCapital2(df.format(detail.getShouldCapital2()));//应回本金（元）
    				detailVO.setShouldInterest2(df.format(detail.getShouldInterest2()));//应回利息（元）
    				detailVO.setDefaultInterest(df.format(detail.getDefaultInterest()));//罚息（元）
    				detailVO.setShouldFee(df.format(detail.getShouldFee()));//应缴费用（元）
    				detailVO.setAllBackMoney(df.format(detail.getShouldCapital2().add(detail.getShouldInterest2()).add(detail.getDefaultInterest()).subtract(detail.getShouldFee())));//应回款总额（元）
    				detailVO.setFactMoney(df.format(detail.getFactBalance().add(detail.getDepalFine())));//已回款总额（元）
    				detailVO.setRightsDetailState(RightsRepaymentDetailStateEnum.getRightsRepaymentDetailStateEnumByValue(String.valueOf(detail.getRightsDetailState())).getDesc());//状态
    				
    				//【开始】处理提前还款
    				if(RightsRepaymentDetailStateEnum.BEFORE_COMPLETE.getValue().equals(String.valueOf(detail.getRightsDetailState()))){
    					detailVO.setShouldFee("--");//应缴费用（元）
        				detailVO.setFactMoney("--");//已回款总额（元）
    				}
    				//【结束】处理提前还款
    				
    				detailVOList.add(detailVO);
    				
    				//如果本期已结清，累加实际收益总和
    				if(RightsRepaymentDetailStateEnum.COMPLETE.getValue().equals(String.valueOf(detail.getRightsDetailState()))){
    					patentedProfit = patentedProfit.add(detail.getShouldInterest2().subtract(detail.getShouldFee()));
    				}
				}
    		}
    		creVO.setPatentedProfit(String.valueOf(patentedProfit));//实际收益总和 = 应回利息（shouldInterest2） - 应缴费用（shouldFee）
    		resultMap.put("creditorRightsDetail", creVO);
            resultMap.put("repaymentList", detailVOList);
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
    /**
     * 【APP接口工具】
     * 和当前日期比对
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

    /**
     * 【APP接口工具】
     * @param backDate
     * @param buyDate
     * @return
     */
	public String getOrderBy(String backDate,String buyDate){

		String orderBy = "";
		String str1 = StringUtils.isEmpty(backDate)?null:(backDate.equals("D")?"CURRENT_PAY_DATE desc":"CURRENT_PAY_DATE asc");
		String str2 = StringUtils.isEmpty(buyDate)?null:(buyDate.equals("D")?" LOBD.BUY_DATE  desc":" LOBD.BUY_DATE  asc");


		orderBy += str1==null?"":str1;
		orderBy += str2==null?"":(StringUtils.isEmpty(orderBy)?str2:","+str2);
		return orderBy;
	}

	/**
	 * 【APP接口工具】
	 * @param status
	 * @return
	 */
	private String[] getQueryStatus(String status){
		if(org.apache.commons.lang.StringUtils.isNotEmpty(status)){
			if(status.indexOf("0")!=-1){
				return null;
			}
			if(status.indexOf("1-2-3")!=-1){
				return null;
			}
			if(status.indexOf("1")!=-1){
				status = status.replace("1","0-1");
			}
			if (status.indexOf("2")!=-1){
				status = status.replace("2","8");
			}

			if (status.indexOf("3")!=-1){
				status = status.replace("3","2-3-7");
			}
			return status.split("-");
		}
		return null;
	}
	
	/**
	 * 【APP接口】【新】
	 * 详情页
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@DoNotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/getBidding")// [APP]
	public Object getBidding(HttpServletRequest request, @RequestParam(value = "loanApplicationNo", required = false) Long loanApplicationNo) {
		try {
			//参数验证
			if (loanApplicationNo == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
			if (loanApplicationListVO == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			//借款标题
			resultMap.put("loanApplicationTitle", loanApplicationListVO.getLoanApplicationTitle());// 标题
			//还款方式
			String repayMethod = null;
			if ("1".equals(loanApplicationListVO.getRepayMethod())) {
				if (PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getDesc();
				} else if (PaymentMethodEnum.AVERAGE_CAPTIAL.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPTIAL.getDesc();
				}
			} else {
				ConstantDefine contant = new ConstantDefine();
				contant.setConstantValue(loanApplicationListVO.getRepayMethod());
				contant.setConstantTypeCode("repaymentMode");
				ConstantDefine cd = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				if(null != cd){
					repayMethod = cd.getConstantName();
				}
			}
			resultMap.put("repayMethod", repayMethod);//还款方式
			//抵押信息
			//年化收益率
			resultMap.put("annualRate", String.valueOf(loanApplicationListVO.getAnnualRate()));// 预期年化收益
			
			//剩余金额
			resultMap.put("remain", String.valueOf(loanApplicationListVO.getRemain()));// 剩余金额
			//借款期限
			resultMap.put("cycleCount", String.valueOf(loanApplicationListVO.getCycleCount()));// 期限
			//借款金额
			resultMap.put("confirmBalance", String.valueOf(loanApplicationListVO.getConfirmBalance()));// 借款金额
			//出借限额
			resultMap.put("maxBuyBalanceNow", String.valueOf(loanApplicationListVO.getMaxBuyBalance()));// 限投
			
			resultMap.put("expectProfit", String.valueOf(this.getExpectProfit(loanApplicationListVO.getLoanApplicationId(), new BigDecimal("100"))));// 计算预期收益（以100为基数）
			resultMap.put("awardProfit", String.valueOf(this.getAwardProfit(loanApplicationListVO.getLoanApplicationId(), new BigDecimal("100"))));//奖励收益（以100为基数）
			resultMap.put("loanType", loanApplicationListVO.getLoanType());// 借款类型
			resultMap.put("rewardsPercent", String.valueOf(loanApplicationListVO.getRewardsPercent()));// 奖励利率
			
			resultMap.put("startAmount", String.valueOf(loanApplicationListVO.getStartAmount()));// 起投金额
			resultMap.put("applicationState", loanApplicationListVO.getApplicationState());// 标的状态
			resultMap.put("begin", loanApplicationListVO.isBegin());// 是否预热中
			resultMap.put("ratePercent", loanApplicationListVO.getRatePercent());// 进度
			resultMap.put("loanApplicationNo", loanApplicationListVO.getLoanApplicationId());// 借款编号
			
			Date openTime = loanApplicationListVO.getPopenTime();
			int secondBetween = 0;
			if (null != openTime) {
				secondBetween = DateUtil.secondBetween(new Date(), openTime);
			}
			resultMap.put("secondBetwween",String.valueOf(secondBetween));//开标时间距离当前时间秒数
			
			//项目证明数（企业标 个人标 通用）
			List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
			int customerSnapshotsSize = 0;
			if(null != customerUploadSnapshots){
				customerSnapshotsSize = customerUploadSnapshots.size();
			}
			resultMap.put("customerSnapshotsSize", customerSnapshotsSize);
			
			//企业证明数
			int enterpriseSnapshotsSize = 0;
			if("2,3,4,5,6".indexOf(loanApplicationListVO.getLoanType())!=-1){
				EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
				if (enterpriseLoanApplication == null) {
					return returnResultMap(false, null, "check", "请求参数异常");
				}
				EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
				if (enterpriseInfo == null) {
					return returnResultMap(false, null, "check", "请求参数异常");
				}
				List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
				if(null != enterpriseInfoSnapshots){
					enterpriseSnapshotsSize = enterpriseInfoSnapshots.size();
				}
			}
			resultMap.put("enterpriseSnapshotsSize", enterpriseSnapshotsSize);
			
			//投标记录数
			Integer lenderSize = 0;
			if ("345".indexOf(loanApplicationListVO.getApplicationState()) != -1) {
				// 发标中、放款审核中、待放款
				// 查询出借订单明细表中 投标中 的状态数据
				lenderSize = lendOrderBidDetailService.findLendOrderDetailCount(loanApplicationListVO.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING);
			} else {
				// 6、7、8 还款中、已结清、已结清(提前还贷)
				// 到债权表查询 已生效，还款中 状态的数据
				lenderSize = lendOrderBidDetailService.findLendOrderDetailCount(loanApplicationListVO.getLoanApplicationId(), LendOrderBidStatusEnum.BIDSUCCESS);
//				lenderSize = creditorRightsService.findLendOrderDetailCount(loanApplicationListVO.getLoanApplicationId(),
//						CreditorRightsStateEnum.EFFECTIVE, CreditorRightsStateEnum.COMPLETE, CreditorRightsStateEnum.EARLYCOMPLETE );
			}
			resultMap.put("lenderSize", lenderSize);
			
			//【定向标-开始】
    		UserInfo currentUser = getCurrentUser(request);
			int targetType = loanApplicationService.getLoanApplicationType(loanApplicationNo);//根据借款申请ID，查询该标定向类型（0无定向，1定向密码，2定向用户）
			resultMap.put("targetType", String.valueOf(targetType));
			boolean isTargetUser = false;//当前用户，是否为定向用户
			if(targetType == 1){//定向密码
				//do nothing
			}else if(targetType == 2){//定向用户
				if(currentUser != null){
					int userCount = userInfoService.normalOrOrienta(currentUser.getUserId(),loanApplicationNo);//判断当前用户，是否为该标的定向用户
					if(userCount > 0){
						isTargetUser = true;
					}
				}
			}
			resultMap.put("isTargetUser", isTargetUser);
			//【定向标-结束】
		
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
	/**
	 * 【APP接口】【新】
	 * 投标详情页之借款详情
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@DoNotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/getBiddingDetail")// [APP]
	public Object getBiddingDetail(HttpServletRequest request, @RequestParam(value = "loanApplicationNo", required = false) Long loanApplicationNo) {
		//参数验证
		if (loanApplicationNo == null) {
			return returnResultMap(false, null, "check", "请求参数异常");
		}
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
		if (loanApplicationListVO == null) {
			return returnResultMap(false, null, "check", "请求参数异常");
		}

		DecimalFormat df = new DecimalFormat("#,##0.00");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Date openTime = loanApplicationListVO.getPopenTime();

			int secondBetween = 0;
			if (null != openTime) {
				secondBetween = DateUtil.secondBetween(new Date(), openTime);
			}
			resultMap.put("secondBetwween",String.valueOf( secondBetween));//开标时间距离当前时间秒数

			// token
			String token = TokenUtils.setNewToken(request);
			resultMap.put("token", token);//防止重复提交购买
			
			// 组织页面返回值
			// 还款方式
			String repayMethod = null;
			if ("1".equals(loanApplicationListVO.getRepayMethod())) {
				if (PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getDesc();
				} else if (PaymentMethodEnum.AVERAGE_CAPTIAL.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPTIAL.getDesc();
				}
			} else {
				ConstantDefine contant = new ConstantDefine();
				contant.setConstantValue(loanApplicationListVO.getRepayMethod());
				contant.setConstantTypeCode("repaymentMode");
				ConstantDefine cd = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				if(null != cd){
					repayMethod = cd.getConstantName();
				}
			}
			resultMap.put("repayMethod", repayMethod);//还款方式
			
			resultMap.put("guaranteeType", loanApplicationListVO.getGuaranteeType());//保障说明  0本息保障 1本金保障
			// 用户是否登陆
			loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
			
			// 借款人基本信息
			CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByLoanApplicationId(loanApplicationListVO
					.getLoanApplicationId());
			if (basicSnapshot != null) {
				resultMap.put("sex", basicSnapshot.getSex());//性别  1男  0女
				resultMap.put("maxCreditValue", null== basicSnapshot.getMaxCreditValue()?"":df.format(basicSnapshot.getMaxCreditValue()) + "元");// 信用卡额度
				resultMap.put("isMarried", basicSnapshot.getIsMarried());// 婚姻状况
				resultMap.put("childStatus", basicSnapshot.getChildStatus());// 子女状况
				resultMap.put("monthlyIncome", basicSnapshot.getMonthlyIncome()==null?"":df.format(Long.valueOf(basicSnapshot.getMonthlyIncome())) + "元");// 月平均收入
				resultMap.put("education", basicSnapshot.getEducation());// 最高学历
				// 居住地
				AddressVO address = addressService.getAddressVOById(basicSnapshot.getResidenceAddr());
				resultMap.put("address", address.getProvinceStr() + address.getCityStr() + address.getDistrictStr() + address.getDetail());
			}
			// 发标信息
			LoanPublish loanPublish = loanPublishService.findLoanPublishVO(loanApplicationNo);
			if(loanPublish.getAssessValue() != null){
				resultMap.put("assessValue", df.format(loanPublish.getAssessValue()) + "万元");// 总评估值
			}else {
				resultMap.put("assessValue", df.format(0) + "万元");// 总评估值
			}
			if(loanPublish.getMarketValue() != null){
				resultMap.put("marketValue", df.format(loanPublish.getMarketValue()) + "万元");// 市值
			}else {
				resultMap.put("marketValue", df.format(0) + "万元");// 市值
			}
			if(loanPublish.getHourseSize() != null){
				resultMap.put("hourseSize", df.format(loanPublish.getHourseSize()) + "平方米");// 房屋面积
			}else {
				resultMap.put("hourseSize", df.format(0) + "平方米");// 房屋面积
			}
			
			resultMap.put("hourseDesc", loanPublish.getHourseDesc());// 抵押物说明
			
			// 认证报告
			String authInfos = loanPublish.getAuthInfos();
			if (StringUtils.isNotEmpty(authInfos)) {
				resultMap.put("authInfo", authInfos.split(","));
			}
			
			resultMap.put("loanApplicationTitle", loanApplicationListVO.getLoanApplicationTitle());// 标题
			resultMap.put("expectProfit", String.valueOf(this.getExpectProfit(loanApplicationListVO.getLoanApplicationId(), new BigDecimal("100"))));// 计算预期收益（以100为基数）
			resultMap.put("loanType", loanApplicationListVO.getLoanType());// 借款类型
			resultMap.put("annualRate", String.valueOf(loanApplicationListVO.getAnnualRate()));// 预期年化收益
			resultMap.put("rewardsPercent", String.valueOf(loanApplicationListVO.getRewardsPercent()));// 奖励利率
			resultMap.put("cycleCount", String.valueOf(loanApplicationListVO.getCycleCount()));// 期限
			resultMap.put("confirmBalance", String.valueOf(loanApplicationListVO.getConfirmBalance()));// 借款金额
			resultMap.put("startAmount", String.valueOf(loanApplicationListVO.getStartAmount()));// 起投金额
			resultMap.put("desc", loanApplicationListVO.getDesc());// 借款描述
			resultMap.put("applicationState", loanApplicationListVO.getApplicationState());// 标的状态
			resultMap.put("begin", loanApplicationListVO.isBegin());// 是否余热仲
			resultMap.put("remain", String.valueOf(loanApplicationListVO.getRemain()));// 剩余金额
			resultMap.put("maxBuyBalanceNow", String.valueOf(loanApplicationListVO.getMaxBuyBalanceNow()));// 限投
			resultMap.put("ratePercent", loanApplicationListVO.getRatePercent());// 进度
			resultMap.put("loanUseage", loanApplicationListVO.getLoanUseage());// 借款用途
			if(null != loanApplicationListVO.getLoanUseage()){
				ConstantDefine cdf = new ConstantDefine();
				cdf.setConstantValue(loanApplicationListVO.getLoanUseage());
				cdf.setConstantTypeCode("loanUseage");
				ConstantDefine cdfRest = constantdefineservice.findConstantByTypeCodeAndValue(cdf);
				if(null != cdfRest){
					resultMap.put("loanUseage", cdfRest.getConstantName());// 借款用途
				}
			}
			resultMap.put("useageDesc", loanApplicationListVO.getUseageDesc());// 用途描述
			
			if(LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())){
				//个人投标详情页
				// 房产抵押
				CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
				String mortgageTypeStr = "";
				if("1".equals(house.getMortgageType())){
					mortgageTypeStr = "一抵";
				}else if ("2".equals(house.getMortgageType())) {
					mortgageTypeStr = "二抵";
				}
				resultMap.put("mortgageType", mortgageTypeStr);// 抵押物类型
				
				AddressVO hAdd = addressService.getAddressVOById(loanPublish.getHourseAddress());
				resultMap.put("houseAddress", null == hAdd ? "" : hAdd.getProvinceStr() + hAdd.getCityStr() + hAdd.getDistrictStr() + hAdd.getDetail());// 房屋地址
			} else {
				// 企业投标详情页
				enterpriseLoanApplicationDetail(request, resultMap, loanApplicationListVO, loanPublish);
			}
			 
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
		return returnResultMap(true, resultMap, null, null);
	}
	
	/**
	 * 【APP接口工具】【新】
	 * 企业投标详情页
	 * @param request
	 * @param loanApplicationListVO
	 * @param loanPublish
	 */
	private void enterpriseLoanApplicationDetail(HttpServletRequest request,Map<String, Object> resultMap, LoanApplicationListVO loanApplicationListVO, LoanPublish loanPublish) {
		//借款企业
		EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
		EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
		
		DecimalFormat df = new DecimalFormat("#,##0.00");
		resultMap.put("enterpriseName",enterpriseInfo.getEnterpriseName());//还款来源
		resultMap.put("jmEnterpriseName",enterpriseInfo.getJmEnterpriseName());//加密企业名称
		resultMap.put("jmOrganizationCode",enterpriseInfo.getJmOrganizationCode());//加密组织机构代码
		resultMap.put("operatingPeriod",String.valueOf(enterpriseInfo.getOperatingPeriod()));//精英年限
		resultMap.put("registeredCapital",df.format(enterpriseInfo.getRegisteredCapital()));//注册资金
		resultMap.put("information",enterpriseInfo.getInformation());//企业信息
		
		ConstantDefine contant = new ConstantDefine();
		contant.setConstantValue(loanApplicationListVO.getLoanUseage());
		contant.setConstantTypeCode("enterpriseLoanUseage");
		ConstantDefine cd = constantdefineservice.findConstantByTypeCodeAndValue(contant);
		if(null != cd){
			resultMap.put("loanUseage",cd.getConstantName());// 借款用途
		}
		
		switch (loanApplicationListVO.getLoanType()){
			case "2":
				//企业车贷
				EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				String address = getAddress(enterpriseCarLoanSnapshot.getProvince(),enterpriseCarLoanSnapshot.getCity());
				BigDecimal totalPrice= mortgageCarSnapshotService.getTotalPriceByCardLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
				resultMap.put("totalPrice",String.valueOf(totalPrice));//汽车列表： 总计
				resultMap.put("address",address);
				
				resultMap.put("creditLimit",enterpriseCarLoanSnapshot.getCreditLimit()==null?"":String.valueOf(enterpriseCarLoanSnapshot.getCreditLimit()));// 授信上限
				resultMap.put("creditLimitRate",enterpriseCarLoanSnapshot.getCreditLimitRate()==null?"":String.valueOf(enterpriseCarLoanSnapshot.getCreditLimitRate()));// 授信比例
				resultMap.put("mortgageDescription",enterpriseCarLoanSnapshot.getMortgageDescription()==null?"":enterpriseCarLoanSnapshot.getMortgageDescription());// 描述
				resultMap.put("projectDescription",enterpriseCarLoanSnapshot.getProjectDescription());//项目描述
				resultMap.put("riskControlInformation",enterpriseCarLoanSnapshot.getRiskControlInformation());//风控信息
				
				contant.setConstantValue(enterpriseCarLoanSnapshot.getInternalRating());
				contant.setConstantTypeCode("internalRating");
				ConstantDefine cd1 = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				resultMap.put("internalRating", null == cd1 ? "" : cd1.getConstantName());// 内部评级
				
				//抵押信息
				List<APPMortgageCarSnapshotVO> carLists = getCardList( loanApplicationListVO.getLoanApplicationId());
				resultMap.put("carLists", carLists);
				
				break;
			case "3":
				//企业信贷
				EnterpriseCreditSnapshot enterpriseCreditSnapshot = enterpriseCreditSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				address = getAddress(enterpriseCreditSnapshot.getProvince(),enterpriseCreditSnapshot.getCity());
				resultMap.put("address",address);
				
				contant.setConstantValue(enterpriseCreditSnapshot.getInternalRating());
				contant.setConstantTypeCode("internalRating");
				ConstantDefine cd2 = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				resultMap.put("internalRating", null == cd2 ? "" : cd2.getConstantName());// 内部评级
				
				resultMap.put("projectDescription",enterpriseCreditSnapshot.getProjectDescription());//项目描述
				resultMap.put("riskControlInformation",enterpriseCreditSnapshot.getRiskControlInformation());//风控信息
				break;
			case "4":
				//企业保理
				EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				resultMap.put("enterpriseName",factoringSnapshot.getSourceOfRepayment());//还款来源
				resultMap.put("loanBalance", df.format(loanApplicationListVO.getLoanBalance()) + "元");//授信金额
				resultMap.put("financingAmount", df.format(factoringSnapshot.getFinancingAmount()) + "元");//融资金额
				resultMap.put("accountReceivableDescription",factoringSnapshot.getAccountReceivableDescription());//应收账款说明
				resultMap.put("moneyRiskAssessment",factoringSnapshot.getMoneyRiskAssessment());//款项风险评价
				resultMap.put("projectComprehensiveEvaluati",factoringSnapshot.getProjectComprehensiveEvaluati());//项目综合评价
				resultMap.put("fieldAdjustmentValue","0".equals(factoringSnapshot.getFieldAdjustmentMark())? factoringSnapshot.getFieldAdjustmentValue():"");// 360度实地尽调－大数据思维保障项目质量
				resultMap.put("repaymentGuaranteeValue","0".equals(factoringSnapshot.getRepaymentGuaranteeMark())?factoringSnapshot.getRepaymentGuaranteeValue():"");//还款保证金 - 构建风险缓释空间
				resultMap.put("aidFundValue","0".equals(factoringSnapshot.getAidFundMark())? factoringSnapshot.getAidFundValue():"");// 法律援助基金 - 平台资金支持护航维权启动
				//融资方
				CoLtd financeParty = coLtdService.getCoLtdById(factoringSnapshot.getFinancingParty());
				resultMap.put("companyNameStr",financeParty.getCompanyNameStr());// 融资方
				break;
			case "5":
				//企业基金
				EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				if(null != foundationSnapshot){
					ConstantDefine cdf = new ConstantDefine();
					cdf.setConstantValue(foundationSnapshot.getInvestmentType());
					cdf.setConstantTypeCode("INVESTMENT_TYPE");
					ConstantDefine cdfRest = constantdefineservice.findConstantByTypeCodeAndValue(cdf);
					if(null != cdfRest){
						resultMap.put("investmentTypeName", cdfRest.getConstantName());//定向委托投资标的
					}else {
						resultMap.put("investmentTypeName", "");
					}
				}else {
					resultMap.put("investmentTypeName", "");
				}
				
				//托管机构
				CoLtd coltd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
				if(null != coltd){
					resultMap.put("coltdCompanyName", coltd.getCompanyName());//托管机构
				}else {
					resultMap.put("coltdCompanyName", "");
				}
				
				//标的详情说明
				Attachment attachment = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getAttachId());
				if(null != attachment){
					resultMap.put("attachmentUrl", this.getWWW_BASEPATH(request) + attachment.getUrl());//标的说明 下载地址
				}else {
					resultMap.put("attachmentUrl", "");
				}
				
				//交易说明书
				Attachment tradeBook = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getTradeBookId());
				if(null != tradeBook){
					resultMap.put("tradeBookUrl", this.getWWW_BASEPATH(request) + tradeBook.getUrl());//查看交易说明书 下载地址
				}else {
					resultMap.put("tradeBookUrl", "");
				}
				
				//风险提示函
				Attachment riskTip = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getRiskTipId());
				if(null != riskTip){
					resultMap.put("riskTipUrl", this.getWWW_BASEPATH(request) + riskTip.getUrl());//风险提示函 下载地址
				}else {
					resultMap.put("riskTipUrl", "");
				}
				
				//免责声明 下载地址
				resultMap.put("disclaimerUrl", this.getWWW_BASEPATH(request) + "/finance/download/disclaimer");
				
				// 下面是借款详情模块的字段
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				if(null != loanApplicationListVO.getPaymentDate()){
					resultMap.put("paymentDate", sdf.format(loanApplicationListVO.getPaymentDate()));//收益起始日
				}else {
					resultMap.put("paymentDate", "资金筹集完成时间");
				}
				
				if(null != loanApplicationListVO.getLastRepaymentDate()){
					resultMap.put("lastRepaymentDate", sdf.format(loanApplicationListVO.getLastRepaymentDate()));//收益到期日 和  预期收款日
				}else {
					resultMap.put("lastRepaymentDate", "资金筹集完成后显示");
				}
				
				break;
			case "6":
				//企业贷
				EnterprisePledgeSnapshot enterprisePledgeSnapshot = enterprisePledgeSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				address = getAddress(enterprisePledgeSnapshot.getProvince(), enterprisePledgeSnapshot.getCity());
				resultMap.put("address",address);
				contant.setConstantValue(enterprisePledgeSnapshot.getInternalRating());
				contant.setConstantTypeCode("internalRating");
				ConstantDefine cd6 = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				resultMap.put("internalRating", null == cd6 ? "" : cd6.getConstantName());// 内部评级
				resultMap.put("projectDescription", enterprisePledgeSnapshot.getProjectDescription());//项目描述
				resultMap.put("riskControlInformation", enterprisePledgeSnapshot.getRiskControlInformation());//风控信息
				break;
				
		}

	}
	
	/**
	 * 【APP接口】【新】
	 * 详情页之项目证明图片
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@DoNotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/getCustomerSnapshots")// [APP]
	public Object getCustomerSnapshots(HttpServletRequest request, @RequestParam(value = "loanApplicationNo", required = false) Long loanApplicationNo) {
		try {
			//参数验证
			if (loanApplicationNo == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
			if (loanApplicationListVO == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			// 项目证明（企业标 个人标 通用）
			List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(
					loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
			int customerSnapshots = customerUploadSnapshots.size();
			String[] customerUrl = new String[customerSnapshots];
			String[] customerThumbnail = new String[customerSnapshots];
			String[] customerFileName = new String[customerSnapshots];
			for (int i = 0; i < customerSnapshots; i++) {
				customerUrl[i] = this.getWWW_BASEPATH(request) + customerUploadSnapshots.get(i).getAttachment().getUrl();
				customerThumbnail[i] = this.getWWW_BASEPATH(request) + customerUploadSnapshots.get(i).getAttachment().getThumbnailUrl();
				customerFileName[i] = customerUploadSnapshots.get(i).getAttachment().getFileName();
			}
			resultMap.put("customerUrl", customerUrl);// 原图
			resultMap.put("customerThumbnail", customerThumbnail);// 缩略图
			resultMap.put("customerFileName", customerFileName);// 附件名
		
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
	/**
	 * 【APP接口】【新】
	 * 详情页之企业证明图片
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@DoNotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/getEnterpriseSnapshots")// [APP]
	public Object getEnterpriseSnapshots(HttpServletRequest request, @RequestParam(value = "loanApplicationNo", required = false) Long loanApplicationNo) {
		try {
			//参数验证
			if (loanApplicationNo == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
			if (loanApplicationListVO == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
			if (enterpriseLoanApplication == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
			if (enterpriseInfo == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			//企业证明
			List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
			int enterpriseSnapshots = enterpriseInfoSnapshots.size();
			String[] enterpriseUrl = new String[enterpriseSnapshots];
			String[] enterpriseThumbnail = new String[enterpriseSnapshots];
			String[] enterpriseFileName = new String[enterpriseSnapshots];
			for (int i = 0; i < enterpriseSnapshots; i++) {
				enterpriseUrl[i] = this.getWWW_BASEPATH(request) + enterpriseInfoSnapshots.get(i).getAttachment().getUrl();
				enterpriseThumbnail[i] = this.getWWW_BASEPATH(request) + enterpriseInfoSnapshots.get(i).getAttachment().getThumbnailUrl();
				enterpriseFileName[i] = enterpriseInfoSnapshots.get(i).getAttachment().getFileName();
			}
			resultMap.put("enterpriseUrl", enterpriseUrl);// 公司证明原图
			resultMap.put("enterpriseThumbnail", enterpriseThumbnail);// 公司证明缩略图
			resultMap.put("enterpriseFileName", enterpriseFileName);// 公司证明附件名
		
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
	
	/**
	 * 【APP接口】[zq01]
	 * 市场债权转让列表
	 * @param request
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value = "/turnCreditRightList", method = RequestMethod.POST)// [APP]
	@ResponseBody
	public Object turnCreditRightList(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, 
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		LoanApplicationListVO loanApplicationListVO = new LoanApplicationListVO();
		DecimalFormat df = new DecimalFormat("#,##0.00");
		DecimalFormat dfp = new DecimalFormat("0.##");
		try {
			Pagination<LoanApplicationListVO> pagination = loanApplicationService.getTurnCreditRightPaging(pageNo, pageSize, loanApplicationListVO, null);
			List<LoanApplicationListVO> loanAppLists = pagination.getRows();

			List<APPTurnCreditRightListVO> datas = new ArrayList<APPTurnCreditRightListVO>();

			if (null != loanAppLists && loanAppLists.size() > 0) {
				APPTurnCreditRightListVO rightListVO = null;
				for (LoanApplicationListVO loan : loanAppLists) {
					rightListVO = new APPTurnCreditRightListVO();
					
					rightListVO.setCreditorRightsId(String.valueOf(loan.getCreditorRightsId()));//债权ID
					rightListVO.setCreditorRightsApplyId(String.valueOf(loan.getCreditorRightsApplyId()));//债权转让申请ID
					rightListVO.setLoanApplicationTitle(loan.getLoanApplicationTitle());//借款标题
					rightListVO.setAnnualRate(String.valueOf(loan.getAnnualRate()));//年化利率
					rightListVO.setCycleCount(String.valueOf(loan.getCycleCount()));//剩余期限 cycleCount 个月
					rightListVO.setWhenWorth(df.format(loan.getWhenWorth()));//剩余本金 whenWorth 元
					rightListVO.setApplyPrice(df.format(loan.getApplyPrice()));//转出价格 applyPrice 元
					BigDecimal ratePercent = loan.getTotalAmountOfLoan().divide(loan.getApplyPrice(),5,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
					if(BigDecimal.ZERO.compareTo(ratePercent) != 1){
						rightListVO.setRatePercent(dfp.format(ratePercent));//进度 ratePercent = totalAmountOfLoan / applyPrice %
					}else {
						rightListVO.setRatePercent("0");
					}
					rightListVO.setTotalAmountOfLoan(df.format(loan.getTotalAmountOfLoan()));//已经转出的价格
					rightListVO.setLoanType(loan.getLoanType());//借款类型
					if(null != loan.getAwardPoint() && !"1".equals(loan.getAwardPoint())){//奖励发放机制
						rightListVO.setRewardsPercent(String.valueOf(loan.getRewardsPercent()));//奖励利率
					}else {
						rightListVO.setRewardsPercent("0");
					}
					
					datas.add(rightListVO);
				}
			}

			resultMap.put("pageSize", pagination.getPageSize());
			resultMap.put("pageNo", pagination.getCurrentPage());
			resultMap.put("total", pagination.getTotal());
			resultMap.put("rows", datas);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
		return returnResultMap(true, resultMap, null, null);

	}
	
	/**
	 * 【APP接口】[zq02]
	 * 市场债权转让详情
	 * @param request
	 * @param creditorRightsId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/turnCreditRightDetail", method = RequestMethod.POST)// [APP]
	public Object turnCreditRightDetail(HttpServletRequest request, 
			@RequestParam(value = "creditorRightsApplyId", required = false) Long creditorRightsApplyId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//参数验证
			if (creditorRightsApplyId == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(creditorRightsApplyId);
			Long creditorRightsId = crta.getApplyCrId();
			CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
			long loanApplicationNo = creditorRights.getLoanApplicationId();
			LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
			if (loanApplicationListVO == null) {
				return returnResultMap(false, null, "check", "请求参数异常");
			}
			
			//已转出债权
			BigDecimal lendRightsBalance = creditorRightsTransferAppService.getRemainingRightsPrice(creditorRightsApplyId);
			
			//借款编号loanApplicationNo
			resultMap.put("loanApplicationNo", String.valueOf(loanApplicationListVO.getLoanApplicationId()));
			
			//借款标题 loanApplicationTitle
			resultMap.put("loanApplicationTitle", loanApplicationListVO.getLoanApplicationTitle());
			
			//债权ID
			resultMap.put("creditorRightsId", creditorRightsId);
			
			//债权申请ID
			resultMap.put("creditorRightsApplyId", crta.getCreditorRightsApplyId());
			
			//还款方式 repayMethod
			String repayMethod = null;
			if ("1".equals(loanApplicationListVO.getRepayMethod())) {
				if (PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPITAL_PLUS_INTEREST.getDesc();
				} else if (PaymentMethodEnum.AVERAGE_CAPTIAL.getValue().equals(loanApplicationListVO.getRepayMentMethod())) {
					repayMethod = PaymentMethodEnum.AVERAGE_CAPTIAL.getDesc();
				}
			} else {
				ConstantDefine contant = new ConstantDefine();
				contant.setConstantValue(loanApplicationListVO.getRepayMethod());
				contant.setConstantTypeCode("repaymentMode");
				ConstantDefine cd = constantdefineservice.findConstantByTypeCodeAndValue(contant);
				if(null != cd){
					repayMethod = cd.getConstantName();
				}
			}
			resultMap.put("repayMethod", repayMethod);
			
			//年化收益率 (预期年化收益)annualRate
			resultMap.put("annualRate", String.valueOf(loanApplicationListVO.getAnnualRate()));
			
			//剩余金额 remain【换】
			resultMap.put("lendRightsBalance", String.valueOf(crta.getApplyPrice().subtract(lendRightsBalance)));//（页面剩余金额）
			
			//借款期限 cycleCount【换】
			List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
			BigDecimal shouldCapital = BigDecimal.ZERO;
			int surpMonth = 0;
			for(RightsRepaymentDetail detailRights:detailRightsList){
				if(RightsRepaymentDetailIsPayOffEnum.ISPAYOFF_NO.getValue().equals(String.valueOf(detailRights.getIsPayOff()))){
					shouldCapital = shouldCapital.add(detailRights.getShouldCapital2()); 
					surpMonth++;
				}
			}
			resultMap.put("surpMonth", String.valueOf(surpMonth));//(页面剩余期限)
			
			//借款金额 confirmBalance
			resultMap.put("confirmBalance", String.valueOf(crta.getApplyPrice()));//(页面出售金额))
			
			//【新】转让金额shouldCapital
			resultMap.put("shouldCapital", String.valueOf(creditorRights.getRightsWorth()));//（页面转让金额）
			
			//出借限额 maxBuyBalanceNow
			resultMap.put("maxBuyBalanceNow", String.valueOf(lendRightsBalance));
			
			//计算预期收益（以1为基数） expectProfit
			String expectProfit = this.getExpectRightProfit(creditorRightsApplyId, new BigDecimal("1"));//返回格式：“预期收益,奖励收益” 或 “预期收益”
			if(expectProfit.indexOf(",")==-1){
				resultMap.put("expectProfit", expectProfit);
			}else {
				resultMap.put("expectProfit", expectProfit.substring(0,expectProfit.indexOf(",")));
			}
			resultMap.put("awardProfit", String.valueOf(this.getAwardRightProfit(creditorRightsApplyId, new BigDecimal("1"))));//奖励收益（以1为基数）
			
			//借款类型 loanType
			resultMap.put("loanType", loanApplicationListVO.getLoanType());
			
			//奖励利率 rewardsPercent
			if(null != loanApplicationListVO.getAwardPoint() && !"1".equals(loanApplicationListVO.getAwardPoint())){//奖励发放机制
				resultMap.put("rewardsPercent", String.valueOf(loanApplicationListVO.getRewardsPercent()));//奖励利率
			}else {
				resultMap.put("rewardsPercent", "0");
			}

			//起投金额startAmount
			resultMap.put("startAmount", "0");
			
			//标的状态applicationState
			resultMap.put("applicationState", loanApplicationListVO.getApplicationState());
			
			//是否预热中begin
			resultMap.put("begin", false);
			
			//【新】转让人
			UserInfoExt creditLend = userInfoExtService.getUserInfoExtById(creditorRights.getLendUserId());
			resultMap.put("lendCustomerName", creditLend.getJMRealName());
			
			//进度ratePercent
			String ratePercent = null;
			if (crta.getBusStatus().equals(CreditorRightsTransferAppStatus.SUCCESS.getValue())) {
				ratePercent = "100";
			} else if (crta.getBusStatus().equals(CreditorRightsTransferAppStatus.OVERDUE.getValue()) || crta.getBusStatus().equals(CreditorRightsTransferAppStatus.CANCEL.getValue())) {
				ratePercent = "0";
			} else {
				ratePercent = lendRightsBalance.multiply(new BigDecimal("100")).divide(crta.getApplyPrice(), 2, BigDecimal.ROUND_HALF_UP) + "";
				ratePercent = ratePercent.replaceAll("\\.00", "");
			}
			resultMap.put("ratePercent", ratePercent);

			//项目证明数（企业标 个人标 通用）customerSnapshotsSize
			List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
			int customerSnapshotsSize = 0;
			if(null != customerUploadSnapshots){
				customerSnapshotsSize = customerUploadSnapshots.size();
			}
			resultMap.put("customerSnapshotsSize", String.valueOf(customerSnapshotsSize));
			
			//企业证明数 enterpriseSnapshotsSize
			int enterpriseSnapshotsSize = 0;
			if("2,3,4,5,6".indexOf(loanApplicationListVO.getLoanType())!=-1){
				EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
				if (enterpriseLoanApplication == null) {
					return returnResultMap(false, null, "check", "请求参数异常");
				}
				EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
				if (enterpriseInfo == null) {
					return returnResultMap(false, null, "check", "请求参数异常");
				}
				List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
				if(null != enterpriseInfoSnapshots){
					enterpriseSnapshotsSize = enterpriseInfoSnapshots.size();
				}
			}
			resultMap.put("enterpriseSnapshotsSize", String.valueOf(enterpriseSnapshotsSize));
			
			//投标记录数
			Integer lenderSize = lendOrderBidDetailService.getCreditorRightsLenderCount(creditorRightsApplyId);
			resultMap.put("lenderSize", String.valueOf(lenderSize));
			
			return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
	/**
	 * 计算债权转让标预期收益
	 * @param creditorRightsApplyId 债权转让申请ID
	 * @param amount 投标金额
	 * @return
	 */
	private String getExpectRightProfit(Long creditorRightsApplyId, BigDecimal amount) {
		CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(creditorRightsApplyId);
		CreditorRights creditorRights = creditorRightsService.findById(crta.getApplyCrId(), false);
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(creditorRights.getLoanApplicationId());
		List<RepaymentPlan> plans = repaymentPlanService.getRepaymentPlanList(loanApplication.getLoanApplicationId(), new Date());
		LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());

		if (null != crta) {
			amount = amount.divide(crta.getApplyPrice(), 18, BigDecimal.ROUND_CEILING).multiply(crta.getWhenWorth());
			BigDecimal partProfit = BigDecimal.ZERO;
			for (RepaymentPlan repaymentPlan : plans) {
				if (RepaymentPlanStateEnum.PART.getValue().equals(String.valueOf(repaymentPlan.getPlanState()))) {
					partProfit = amount.divide(crta.getWhenWorth(), 18, BigDecimal.ROUND_CEILING).multiply(
							repaymentPlan.getShouldInterest2().subtract(repaymentPlan.getFactInterest()));
					amount = amount.subtract(amount.divide(crta.getWhenWorth(), 18, BigDecimal.ROUND_CEILING).multiply(
									repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital())));
					crta.setTimeLimit(crta.getTimeLimit() - 1);
					break;
				}
			}
			BigDecimal profit = BigDecimal.ZERO;
			
			try {
				profit = InterestCalculation.getAllInterest(amount, loanApplication.getAnnualRate(), loanProduct.getDueTimeType(),
						loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), crta.getTimeLimit(),
						loanProduct.getCycleValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			profit = profit.add(partProfit);
			
			return profit.toString();

		} else {
			return "0.00";
		}
	}
	
	/**
	 * 计算债权转让标奖励预期收益
	 * @param creditorRightsApplyId 债权转让申请ID
	 * @param amount 投标金额
	 * @return
	 */
	private String getAwardRightProfit(Long creditorRightsApplyId, BigDecimal amount) {
		CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(creditorRightsApplyId);
		CreditorRights creditorRights = creditorRightsService.findById(crta.getApplyCrId(), false);
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(creditorRights.getLoanApplicationId());
		List<RepaymentPlan> plans = repaymentPlanService.getRepaymentPlanList(loanApplication.getLoanApplicationId(), new Date());
		LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());

		if (null != crta) {
			amount = BigDecimalUtil.down(amount.divide(crta.getApplyPrice(), 18, BigDecimal.ROUND_CEILING).multiply(crta.getWhenWorth()), 2);
			for (RepaymentPlan repaymentPlan : plans) {
				if (RepaymentPlanStateEnum.PART.getValue().equals(String.valueOf(repaymentPlan.getPlanState()))) {
					amount = BigDecimalUtil.down(
							amount.subtract(amount.divide(crta.getWhenWorth(), 18, BigDecimal.ROUND_CEILING).multiply(
									repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()))), 2);
					crta.setTimeLimit(crta.getTimeLimit() - 1);
					break;
				}
			}
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
				award = BigDecimalUtil.down(award, 18);
			}
			
			if (loanPublish.getAwardPoint() != null && loanPublish.getAwardPoint().equals(AwardPointEnum.ATMAKELOAN.getValue())){
				return "0.00";
			}else {
				return award.toString();
			}

		} else {
			return "0.00";
		}
	}
	
	/**
	 * 【APP接口】
	 * 债权转让投标列表
	 * @param request
	 * @param loanApplicationId
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value = "/getCreditorRightsLender")// [APP]
	@ResponseBody
	public Object getCreditorRightsLender(HttpServletRequest request, Long creditorRightsApplyId,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Pagination<LenderRecordVO> pagination = lendOrderBidDetailService.getCreditorRightsLenderPaging(pageNo, pageSize, creditorRightsApplyId);
			List<LenderRecordVO> lendLists = pagination.getRows();
			List<APPLenderRecordVO> datas = new ArrayList<APPLenderRecordVO>();

			if (null != lendLists && lendLists.size() > 0) {
				for (int i = 0; i < lendLists.size(); i++) {
					LenderRecordVO vo = lendLists.get(i);
					APPLenderRecordVO appVO = new APPLenderRecordVO();
					appVO.setLenderName(vo.getLenderName());
					appVO.setLendAmount(String.valueOf(vo.getLendAmount()));
					appVO.setLendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getLendTime()));
					datas.add(appVO);
				}
			}
			resultMap.put("pageSize", pagination.getPageSize());
			resultMap.put("pageNo", pagination.getCurrentPage());
			resultMap.put("total", pagination.getTotal());
			resultMap.put("rows", datas);

		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
		return returnResultMap(true, resultMap, null, null);
	}
	
	/**
	 * 【APP接口】[zq03]
	 * 我的债券转让查询列表
	 * @param request
	 * @param pageSize
	 * @param pageNo
	 * @param creditorRightsStatus
	 * @return
	 */
	@RequestMapping(value = "/myTurnCreditRightList", method = RequestMethod.POST)//[APP]
	@ResponseBody
	public Object myTurnCreditRightList(HttpServletRequest request, 
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "creditorRightsStatus", defaultValue = "") String creditorRightsStatus) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//登录验证
			UserInfo currentUser = getCurrentUser(request);
			if(null == currentUser){
				return returnResultMap(false, null, "needlogin", "请先登录");
			}
			
			//组织查询条件
			CreditorRightsExtVo vo = new CreditorRightsExtVo();
			vo.setLendUserId(currentUser.getUserId());
			vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上
			
			//封装参数
			Map<String, Object> customParams = new HashMap<String, Object>();

			if (creditorRightsStatus.equals("6")) {
				customParams.put("rightsflag", "0");// 转让的标什
			}
			if (com.xt.cfp.core.util.StringUtils.isNull(creditorRightsStatus)) {
				String[] queryStatus = { CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.getValue(),//0=已生效
						CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT.getValue(),//2=已转出
						//CreditorRightsConstants.CreditorRightsStateEnum.COMPLETE.getValue(),//不查询，3=已结清的
						CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.getValue(),//5=申请转出
						CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.getValue() };//8=转让中
				customParams.put("queryStatus", queryStatus);
			} else if (creditorRightsStatus.equals("8")) {
				customParams.put("fromWhere", CreditorRightsFromWhereEnum.TURN.getValue());
			} else {
				String[] queryStatus = getQueryStatus(creditorRightsStatus);
				if (queryStatus != null)
					customParams.put("queryStatus", queryStatus);
			}
			customParams.put("productTypeEnum", "productTypeEnum");
			Pagination<CreditorRightsExtVo> pagination = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);
			List<CreditorRightsExtVo> rightsExtVoList = pagination.getRows();
			
			// 封装数据
			DecimalFormat df = new DecimalFormat("0.00");
			List<APPMyTurnCreditRightListVO> datas = new ArrayList<APPMyTurnCreditRightListVO>();
			if(null != rightsExtVoList && rightsExtVoList.size() > 0){
				APPMyTurnCreditRightListVO myRightVO = null;
				for (CreditorRightsExtVo rightsExtVo : rightsExtVoList) {
					myRightVO = new APPMyTurnCreditRightListVO();
					
					//****判断是否可转让【开始】
					List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(rightsExtVo.getCreditorRightsId());
					// 债权转让按钮显示/隐藏
					boolean rightBtn = true;
					Long termDay = repaymentPlanService.getTermDay(detailRightsList);
					if (termDay <= 0) {
						rightBtn = false;
					}
					if (rightBtn) {
						// 债权生成时间是否大于30天
						int betweenDate = DateUtil.daysBetween(rightsExtVo.getCreateTime(), new Date());
						if (betweenDate < 30) {
							rightBtn = false;
						}
					}
					if (rightBtn) {
						// 判断是否逾期
						List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlanByLoanAppIdAndState(rightsExtVo.getLoanApplicationId(),
								RepaymentPlanStateEnum.DEFAULT.getValueChar());
						if (repaymentPlanList.size() != 0) {
							rightBtn = false;
						}
					}
//					if(!rightBtn){
//						continue;//不如何条件，退出本次循环
//					}
					myRightVO.setRightShow(rightBtn);//该债权转让是否显示
					//****判断是否可转让【结束】
					
					//ID
					myRightVO.setCreditorRightsId(String.valueOf(rightsExtVo.getCreditorRightsId()));
					
					//借款标题
					myRightVO.setLoanApplicationTitle(rightsExtVo.getLoanApplicationListVO().getLoanApplicationTitle());
					
					//借款期限
					myRightVO.setCycleCount(String.valueOf(rightsExtVo.getLoanApplicationListVO().getCycleCount()));
					
					//转出资金
					myRightVO.setBuyPrice(String.valueOf(rightsExtVo.getBuyPrice()));
					
					//实际收益
					myRightVO.setExpectProfit(df.format(rightsExtVo.getExpectProfit()));
					
					//来源
					myRightVO.setFromWhere(String.valueOf(rightsExtVo.getFromWhere()));//1购买；2转让
					
					//状态
					String rightsState = String.valueOf(rightsExtVo.getRightsState());
					myRightVO.setRightsState(rightsState);
					
					//【转让中】= 申请转让 =8
					//【已转入】= 查询条件的转入记录（0+查询条件）*（买入详情）
					//【可转让】= 已经生效=0
					//【已转让】= 2 * （转出详情）
					if(CreditorRightsStateEnum.TRANSFERING.getValue().equals(rightsState)){//8转让中
						myRightVO.setRightsStateDisplay("转让中");
					}else if (CreditorRightsStateEnum.EFFECTIVE.getValue().equals(rightsState)) {//0已生效
						if(CreditorRightsFromWhereEnum.TURN.getValue().equals(String.valueOf(rightsExtVo.getFromWhere()))){//转让
							myRightVO.setRightsStateDisplay("已转入");//[手机控制]rightBtn true 显示可转让；false 显示已转入
						}else {
							myRightVO.setRightsStateDisplay("可转让");//[手机控制]rightBtn true 显示；false 不显示
						}
					}else if (CreditorRightsStateEnum.TURNOUT.getValue().equals(rightsState)) {//2已转出
						myRightVO.setRightsStateDisplay("已转让");
					}else {
						myRightVO.setRightsStateDisplay(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(rightsState).getDesc());
					}
					
					//申请债权转让ID
					myRightVO.setCreditorRightsApplyId(String.valueOf(rightsExtVo.getCreditorRightsApplyId()));
					
					datas.add(myRightVO);
				}
				
			}
			
			resultMap.put("pageSize", pagination.getPageSize());
			resultMap.put("pageNo", pagination.getCurrentPage());
			resultMap.put("total", pagination.getTotal());
			resultMap.put("rows", datas);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
		return returnResultMap(true, resultMap, null, null);
		
	}
	
	/**
	 * 【APP接口】[zq04]
	 * 我的债权转让详情
	 * @param request
	 * @param creditorRightsId 我的债权ID
	 * @param creditorRightsApplyId 转让人的，债权申请转让ID
	 * @return
	 */
	@RequestMapping(value = "/myTurnCreditRightDetail", method = RequestMethod.POST)//[APP]
	@ResponseBody
	public Object myTurnCreditRightDetail(HttpServletRequest request,
			@RequestParam(value = "creditorRightsId", defaultValue = "") String creditorRightsId,
			@RequestParam(value = "creditorRightsApplyId", defaultValue = "") String creditorRightsApplyId) {
		
		try {
			//登录验证
			UserInfo currentUser = getCurrentUser(request);
			if(null == currentUser){
				return returnResultMap(false, null, "needlogin", "请先登录");
			}
			//参数验证
			if(null == creditorRightsId || "".equals(creditorRightsId)){
				return returnResultMap(false, null, "check", "参数不能为空");
			}
			//根据债权ID加载一条债权数据
			CreditorRightsExtVo vo = creditorRightsService.getCreditorRightsDetailById(Long.valueOf(creditorRightsId));
			if(null == vo){
				return returnResultMap(false, null, "check", "参数不合法");
			}
			
			// 【获取数据-开始】
			String point=com.xt.cfp.core.util.StringUtils.isNull(vo.getAwardPoint())?"":null;
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
//        	vo.setAwardRate(vo.getAwardRate()!=null&&!vo.getAwardRate().equals("")&&!vo.getAwardRate().equals("0")?"+"+vo.getAwardRate()+"%":"");
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
            List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
            //最近还款日期
            try {
                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("最近还款日期计算出错", e);
            }
            //处理一下明细，添加待缴费用
            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), currentUser.getUserId());
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
            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(vo.getLendOrderId(), currentUser.getUserId());
            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(vo.getLendOrderId(), currentUser.getUserId());
            vo.setRightsRepaymentDetailList(detailRightsList);
            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
            //计算预期收益
            vo.setExpectProfit(exceptProfit);
            
			// 债权转让按钮显示/隐藏
			boolean rightBtn = true;
			Long termDay = repaymentPlanService.getTermDay(detailRightsList);
			if (termDay <= 0) {
				rightBtn = false;
			}
			if (rightBtn) {
				// 债权生成时间是否大于30天
				int betweenDate = DateUtil.daysBetween(vo.getCreateTime(), new Date());
				if (betweenDate < 30) {
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
            
			// 【获取数据-结束】
            
			//返回结果
            Map<String,Object> resultMap = new HashMap<String,Object>();
            CreditorRightsExtVO creVO = new CreditorRightsExtVO();
            DecimalFormat df = new DecimalFormat("0.00");
            //债权详情
            creVO.setCreditorRightsName(vo.getCreditorRightsName());//债权名称(借款标题)
    		creVO.setLoanLoginName(vo.getLoanLoginName());//借款人
    		creVO.setBuyPrice(df.format(vo.getBuyPrice()));//出借金额（元）
    		creVO.setWaitTotalpayMent(df.format(vo.getWaitTotalpayMent()));//待收回款（元）
    		creVO.setFactBalance(df.format(vo.getFactBalance()));//已收回款（元）
    		creVO.setCurrentPayDate(DateUtil.getDateLong(vo.getCurrentPayDate()));//最近回款日
    		creVO.setRightsState(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(vo.getRightsState())).getDesc());//债权状态
    		creVO.setBuyDate(DateUtil.getDateLong(vo.getBuyDate()));//投标日期
    		creVO.setExpectProfit(df.format(vo.getExpectProfit()));//预期收益
    		creVO.setCreditorRightsId(String.valueOf(vo.getCreditorRightsId()));//债权ID
    		creVO.setCycleCount(vo.getLoanApplicationListVO().getCycleCount() + "个月");//出借时长
    		creVO.setAnnualRate(vo.getLoanApplicationListVO().getAnnualRate() + "%");//年化利率
    		creVO.setRepayMentMethod(vo.getLoanApplicationListVO().getRepayMentMethod());//还款方式
    		
    		//规则：购买标 ||（债权转让标 && ！奖励发放时机是放款）
    		String awardRate = "0%";//奖励利率
    		String awardExpectProfit = "0.00";//奖励预期收益
    		if((CreditorRightsFromWhereEnum.TURN.getValue().equals(String.valueOf(vo.getFromWhere())) && !AwardPointEnum.ATMAKELOAN.getValue().equals(vo.getAwardPoint()))){
    			if(null != vo.getAwardRate() && !"".equals(vo.getAwardRate())){
    				awardRate = vo.getAwardRate()+"%";
    				if(null != creditorRightsApplyId && !"".equals(creditorRightsApplyId) && !"null".equals(creditorRightsApplyId)){
    					awardExpectProfit = df.format(new BigDecimal(this.getAwardRightProfit(Long.valueOf(creditorRightsApplyId), vo.getBuyPrice())));
    				}
    			}
    		}else if (CreditorRightsFromWhereEnum.BUY.getValue().equals(String.valueOf(vo.getFromWhere()))) {
    			if(null != vo.getAwardRate() && !"".equals(vo.getAwardRate())){
    				awardRate = vo.getAwardRate()+"%";
    				awardExpectProfit = df.format(this.getAwardProfit(vo.getLoanApplicationListVO().getLoanApplicationId(), vo.getBuyPrice()));				
    			}
			}
    		creVO.setAwardRate(awardRate);
        	creVO.setAwardExpectProfit(awardExpectProfit);
        	
    		//【新】转让人【开始】
    		if(null != creditorRightsApplyId && !"".equals(creditorRightsApplyId) && !"null".equals(creditorRightsApplyId)){
    			CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(Long.valueOf(creditorRightsApplyId));
        		CreditorRights creditorRights = creditorRightsService.findById(crta.getApplyCrId(), false);
        		UserInfoExt creditLend = userInfoExtService.getUserInfoExtById(creditorRights.getLendUserId());
    			creVO.setLendCustomerName(creditLend.getJMRealName());
    		}else {
    			creVO.setLendCustomerName("");
			}
			//【新】转让人【结束】
			
            //回款列表
    		List<RightsRepaymentDetailVO> detailVOList = new ArrayList<RightsRepaymentDetailVO>();
    		RightsRepaymentDetailVO detailVO = null;
    		BigDecimal patentedProfit = BigDecimal.ZERO;//存储实际收益总和
    		if(null != detailRightsList && detailRightsList.size() > 0){
    			for (RightsRepaymentDetail detail : detailRightsList) {
    				detailVO = new RightsRepaymentDetailVO();
					
    				detailVO.setSectionCode(String.valueOf(detail.getSectionCode()));//回款期
    				detailVO.setRepaymentDayPlanned(DateUtil.getDateLong(detail.getRepaymentDayPlanned()));//回款日期
    				detailVO.setShouldCapital2(df.format(detail.getShouldCapital2()));//应回本金（元）
    				detailVO.setShouldInterest2(df.format(detail.getShouldInterest2()));//应回利息（元）
    				detailVO.setDefaultInterest(df.format(detail.getDefaultInterest()));//罚息（元）
    				detailVO.setShouldFee(df.format(detail.getShouldFee()));//应缴费用（元）
    				detailVO.setAllBackMoney(df.format(detail.getShouldCapital2().add(detail.getShouldInterest2()).add(detail.getDefaultInterest()).subtract(detail.getShouldFee())));//应回款总额（元）
    				detailVO.setFactMoney(df.format(detail.getFactBalance().add(detail.getDepalFine())));//已回款总额（元）
    				detailVO.setRightsDetailState(RightsRepaymentDetailStateEnum.getRightsRepaymentDetailStateEnumByValue(String.valueOf(detail.getRightsDetailState())).getDesc());//状态
    				
    				//如果本期已结清，累加实际收益总和
    				if(RightsRepaymentDetailStateEnum.COMPLETE.getValue().equals(String.valueOf(detail.getRightsDetailState()))){
    					patentedProfit = patentedProfit.add(detail.getShouldInterest2().subtract(detail.getShouldFee()));
    				}
    				
    				detailVOList.add(detailVO);
				}
    		}
    		creVO.setPatentedProfit(String.valueOf(patentedProfit));//实际收益总和 = 应回利息（shouldInterest2） - 应缴费用（shouldFee）
    		
    		//计算实际奖励收益
    		BigDecimal awardBalance = BigDecimal.ZERO;
    		if(CreditorRightsFromWhereEnum.BUY.getValue().equals(String.valueOf(vo.getFromWhere()))
    				|| CreditorRightsFromWhereEnum.TURN.getValue().equals(String.valueOf(vo.getFromWhere())) && !AwardPointEnum.ATMAKELOAN.getValue().equals(vo.getAwardPoint())){
    			List<AwardDetail> awardDetailList = awardDetailService.findByLendOrderId(vo.getLendOrderId());
        		if(null != awardDetailList && awardDetailList.size() > 0){
        			for (AwardDetail ad : awardDetailList) {
        				awardBalance = awardBalance.add(ad.getAwardBalance());
    				}
        		}
    		}
    		creVO.setPatentedAwardProfit(df.format(awardBalance));//实际奖励收益总和
    		
    		resultMap.put("creditorRightsDetail", creVO);
            resultMap.put("repaymentList", detailVOList);
            return returnResultMap(true, resultMap, null, null);
		} catch (SystemException se){
        	logger.error(se.getMessage(),se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e){
        	logger.error(e.getMessage(),e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
	}
	
}
