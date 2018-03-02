package com.xt.cfp.core.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.BidErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.LoanApplicationPublishStateEnum;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.constants.RepaymentPlanStateEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.Address;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CustomerBasicSnapshot;
import com.xt.cfp.core.pojo.CustomerCarSnapshot;
import com.xt.cfp.core.pojo.CustomerContactsSnapshot;
import com.xt.cfp.core.pojo.CustomerHouseSnapshot;
import com.xt.cfp.core.pojo.CustomerUploadSnapshot;
import com.xt.cfp.core.pojo.CustomerWorkSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCarLoanSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCreditSnapshot;
import com.xt.cfp.core.pojo.EnterpriseFactoringSnapshot;
import com.xt.cfp.core.pojo.EnterpriseFoundationSnapshot;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.EnterprisePledgeSnapshot;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanApplicationFeesItem;
import com.xt.cfp.core.pojo.LoanOrientation;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.LoanProductFeesItem;
import com.xt.cfp.core.pojo.LoanPublish;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.pojo.MainLoanPublish;
import com.xt.cfp.core.pojo.MortgageCarSnapshot;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.LoanPublishVO;
import com.xt.cfp.core.service.AddressService;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CustomerBasicSnapshotService;
import com.xt.cfp.core.service.CustomerCarSnapshotService;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.CustomerContactsSnapshotService;
import com.xt.cfp.core.service.CustomerHouseSnapshotService;
import com.xt.cfp.core.service.CustomerWorkSnapshotService;
import com.xt.cfp.core.service.EnterpriseCarLoanSnapshotService;
import com.xt.cfp.core.service.EnterpriseCreditSnapshotService;
import com.xt.cfp.core.service.EnterpriseFactoringSnapshotService;
import com.xt.cfp.core.service.EnterpriseFoundationSnapshotService;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.EnterprisePledgeSnapshotService;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LendLoanBindingService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationFeesItemService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanProductFeesItemService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.MainLoanApplicationService;
import com.xt.cfp.core.service.MortgageCarSnapshotService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.util.ClassReflection;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.MD5Util;

/**
 * Created by ren yulin on 15-7-11.
 */
@Service
public class LoanPublishServiceImpl implements LoanPublishService {

	@Autowired
	private LoanApplicationService loanApplicationService;
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private LoanProductFeesItemService loanProductFeesItemService;
	@Autowired
	private LoanApplicationFeesItemService loanApplicationFeesItemService;
	@Autowired
	private FeesItemService feesItemService;
	@Autowired
	private ConstantDefineService constantDefineService;
	@Autowired
	private LoanProductService loanProductService;
	@Autowired
	private LendLoanBindingService lendLoanBindingService;
	@Autowired
	private LendProductService lendProductService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private CustomerCardService customerCardService;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
	private PayService payService;
	@Autowired
	private CreditorRightsService creditorRightsService;
	@Autowired
	private LendOrderBidDetailService lendOrderBidDetailService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	@Autowired
	private EnterpriseCarLoanSnapshotService enterpriseCarLoanSnapshotService;
	@Autowired
	private EnterpriseCreditSnapshotService enterpriseCreditSnapshotService;
	@Autowired
	private EnterpriseFactoringSnapshotService enterpriseFactoringSnapshotService;
	@Autowired
	private EnterpriseFoundationSnapshotService enterpriseFoundationSnapshotService;
	@Autowired
	private EnterprisePledgeSnapshotService enterprisePledgeSnapshotService;
	@Autowired
	private CustomerBasicSnapshotService customerBasicSnapshotService;
	@Autowired
	private CustomerWorkSnapshotService customerWorkSnapshotService;
	@Autowired
	private CustomerHouseSnapshotService customerHouseSnapshotService;
	@Autowired
	private CustomerContactsSnapshotService customerContactsSnapshotService;
	@Autowired
	private MortgageCarSnapshotService mortgageCarSnapshotService;
	@Autowired
	private CustomerCarSnapshotService customerCarSnapshotService;

	@Autowired
	private MyBatisDao myBatisDao;
	@Value(value = "${ZIP_PATH}")
	private String zipPath;

	// get by main id
	@Override
	public MainLoanPublish findMainLoanPublishById(Long mainLoanApplicationId) {
		return myBatisDao.get("MAIN_LOAN_PUBLISH.selectByPrimaryKey", mainLoanApplicationId);
	}

	// update by main id
	@Override
	public MainLoanPublish updateMainLoanPublish(MainLoanPublish mainLoanPublish) {
		myBatisDao.update("MAIN_LOAN_PUBLISH.updateByPrimaryKeySelective", mainLoanPublish);
		return mainLoanPublish;
	}

	// add by main id
	@Override
	public MainLoanPublish addMainLoanPublish(MainLoanPublish mainLoanPublish) {
		myBatisDao.update("MAIN_LOAN_PUBLISH.insert", mainLoanPublish);
		return mainLoanPublish;
	}

	@Override
	public LoanPublish insertLoanPublish(LoanPublish loanPublish) {
		myBatisDao.update("LOANPUBLISH.insert", loanPublish);
		return loanPublish;
	}

	@Override
	public LoanPublish findById(long loanApplicationId) {
		return myBatisDao.get("LOANPUBLISH.selectByPrimaryKey", loanApplicationId);
	}

	// 编辑发标描述操作 by main
	@Override
	@Transactional
	public void addLoanPublish(MainLoanPublish mainLoanPublish, Address address) {
		if (address != null) {
			addressService.addAddress(address);
			mainLoanPublish.setHourseAddress(address.getAddressId());
		}

		MainLoanPublish old = findMainLoanPublishById(mainLoanPublish.getMainLoanApplicationId());// main
		if (null == old) {// 如果是第一次编辑发标描述
			mainLoanPublish = this.addMainLoanPublish(mainLoanPublish);
		} else {// 不是第一次，编辑发标描述
			mainLoanPublish = this.updateMainLoanPublish(mainLoanPublish);
		}

		MainLoanApplication mainLoanApplication = mainLoanApplicationService
				.findById(mainLoanPublish.getMainLoanApplicationId());
		mainLoanApplication.setPublishState(LoanApplicationPublishStateEnum.AUDITING.getValue());// set3:发标复审中
        mainLoanApplication.setLoanUseageDesc(mainLoanPublish.getLoanUseageDesc());
		mainLoanApplicationService.updateMainLoanApplication(mainLoanApplication);// main
	}

	@Override
	public void update(LoanPublish loanPublish) {
		myBatisDao.update("LOANPUBLISH.updateByPrimaryKeySelective2", loanPublish);
	}

	@Override
	public void updateByMap(Map paraMap) {
		myBatisDao.update("LOANPUBLISH.updateByMap", paraMap);
	}

	/**
	 * 执行：发标保存操作 by mainid doing 修改借款申请的发标状态和借款申请状态 修改发标信息 生成费用清单
	 */
	@Override
	@Transactional
	public String publish(MainLoanPublish mainLoanPublish, List<Map<String, Object>> parseExcelReturnUser,
			String opassword,String newUserRadio) throws Exception {

		Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("mainLoanApplicationId", mainLoanPublish.getMainLoanApplicationId());// main
		loanMap.put("publishState", LoanApplicationPublishStateEnum.SUCCESS.getValue());// 4
		loanMap.put("applicationState", LoanApplicationStateEnum.BIDING.getValue());// 3
		loanMap.put("publishTime", new Date());
		loanMap.put("maxBuyBalance", mainLoanPublish.getMaxBuyBalance());
		loanMap.put("publish_target", mainLoanPublish.getPublishTarget());
		mainLoanApplicationService.update(loanMap);// by mainid

		// 补全开始
        MainLoanPublish mainLoanPublishTemp = this.findMainLoanPublishById(mainLoanPublish.getMainLoanApplicationId());//临时查询出数据库中的信息，并补全到MainLoanPublish页面对象中
        mainLoanPublish.setGuaranteeChannel(mainLoanPublishTemp.getGuaranteeChannel());
        mainLoanPublish.setGuaranteeType(mainLoanPublishTemp.getGuaranteeType());
        mainLoanPublish.setAuthInfos(mainLoanPublishTemp.getAuthInfos());
        mainLoanPublish.setOverduePayPoint(mainLoanPublishTemp.getOverduePayPoint());

        mainLoanPublish.setHourseAddress(mainLoanPublishTemp.getHourseAddress());
        mainLoanPublish.setHourseSize(mainLoanPublishTemp.getHourseSize());
        mainLoanPublish.setAssessValue(mainLoanPublishTemp.getAssessValue());
        mainLoanPublish.setMarketValue(mainLoanPublishTemp.getMarketValue());
        mainLoanPublish.setHourseDesc(mainLoanPublishTemp.getHourseDesc());

        // 补全结束

		mainLoanPublish.setPublishTime(new Date());
		this.updateMainLoanPublish(mainLoanPublish);// by mainid

		// todoed 生成借款申请费用清单（转移到下面）
		MainLoanApplication mainLoanApplication = mainLoanApplicationService
				.findById(mainLoanPublish.getMainLoanApplicationId());// main

		// 更改借款主表信息
		mainLoanApplication.setMainState("1");// 主状态，(0.未发标；1.发标中；2.发标完成)

		// 借款金额验证：-1表示小于，0是等于，1是大于
		if (new BigDecimal(mainLoanPublish.getThisPublishBalance()).add(mainLoanApplication.getMainPublishBalance())
				.compareTo(mainLoanApplication.getMainLoanBalance()) == 1) {
			throw new SystemException(BidErrorCode.AMOUNT_OVERFLOW);
		} else
			if (new BigDecimal(mainLoanPublish.getThisPublishBalance()).add(mainLoanApplication.getMainPublishBalance())
					.compareTo(mainLoanApplication.getMainLoanBalance()) == 0) {
			mainLoanApplication.setMainState("2");// 主状态，(0.未发标；1.发标中；2.发标完成)
		}

		// 更新已经发表金额
		mainLoanApplication.setMainPublishBalance(new BigDecimal(mainLoanPublish.getThisPublishBalance())
				.add(mainLoanApplication.getMainPublishBalance()));
		mainLoanApplication.setMainUpdateTime(new Date());// 主最后更改时间
		mainLoanApplicationService.updateMainLoanApplication(mainLoanApplication);

		// *******************************************************************************************************
		// ***** 执行借款申请，拆标COPY操作。【开始】
		// *************************************************************************
		// 说明：借款申请类型：0.信贷；1.房贷；2.企业车贷；3.企业信贷；4.企业保理；5.基金 ；6.企业标（质押标）
		// 规则：根据主借款申请ID，查询相关表数据，创建子借款申请数据，并将相关数据COPY一份关联到子借款申请。
		//
		// *******************************************************************************************************

		// 借款申请表
		// LOAN_APPLICATION(共用)
		LoanApplication loanApplication = new LoanApplication();
		ClassReflection.reflectionAttr(mainLoanApplication, loanApplication);// 实体类赋值

		loanApplication.setPublishState(LoanApplicationPublishStateEnum.SUCCESS.getValue());// 4
		loanApplication.setApplicationState(LoanApplicationStateEnum.BIDING.getValue());// 3
		loanApplication.setPublishTime(new Date());
		loanApplication.setMaxBuyBalance(mainLoanPublish.getMaxBuyBalance());
		loanApplication.setPublishTarget(mainLoanPublish.getPublishTarget());

		loanApplication.setLoanApplicationName(mainLoanPublish.getThisPublishTitle());// 本次借款标题
		loanApplication.setLoanBalance(new BigDecimal(mainLoanPublish.getThisPublishBalance()));// 本次借款金额
		loanApplication.setConfirmBalance(new BigDecimal(mainLoanPublish.getThisPublishBalance()));// 本次批复金额
		// 计算本次借款利息
		LoanProduct loanProduct = loanProductService.findById(mainLoanApplication.getLoanProductId());
		loanApplication.setInterestBalance(
				InterestCalculation.getAllInterest(loanApplication.getConfirmBalance(), loanApplication.getAnnualRate(),
						loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(),
						loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue()));

		// 子标的编号
		Integer num = 1;
		Integer totleCount = loanApplicationService
				.getLoanAppCountByMainId(mainLoanApplication.getMainLoanApplicationId());
		if (null != totleCount && totleCount > 0) {
			num = num + totleCount;
		}
		loanApplication.setLoanApplicationCode(loanApplication.getLoanApplicationCode() + "-" + num);// 表的后缀添加编号
		loanApplication = loanApplicationService.addLoanApplication(loanApplication);

		// 借款发标表
		// LOAN_PUBLISH(共用)
		LoanPublish loanPublish = new LoanPublish();
		ClassReflection.reflectionAttr(mainLoanPublish, loanPublish);// 实体类赋值
		loanPublish.setLoanApplicationId(loanApplication.getLoanApplicationId());// set外键
		loanPublish.setLoanTitle(loanApplication.getLoanApplicationName());
		loanPublish.setLoanUseageDesc(loanApplication.getLoanUseageDesc());
		loanPublish.setMaxBuyBalance(loanApplication.getMaxBuyBalance());
		loanPublish = this.insertLoanPublish(loanPublish);

		// 借款申请费用表
		// LOAN_APPLICATION_FEES_ITEM(共用)
		List<LoanProductFeesItem> loanProductFeesItems = loanProductFeesItemService
				.getByProductId(mainLoanApplication.getLoanProductId());
		for (LoanProductFeesItem productFeesItem : loanProductFeesItems) {
			LoanApplicationFeesItem applicationFeesItem = new LoanApplicationFeesItem();
			applicationFeesItem.setLoanApplicationId(loanApplication.getLoanApplicationId());// set外键
			applicationFeesItem.setChargeCycle(productFeesItem.getChargeCycle());
			applicationFeesItem.setWorkflowRatio(productFeesItem.getWorkflowRatio());
			FeesItem feesItem = feesItemService.findById(productFeesItem.getFeesItemId());
			applicationFeesItem.setItemName(feesItem.getItemName());
			applicationFeesItem.setItemType(feesItem.getItemType());
			applicationFeesItem.setFeesRate(BigDecimal.valueOf(feesItem.getFeesRate()));
			applicationFeesItem.setRadicesType(feesItem.getRadicesType());
			applicationFeesItem.setRadiceLogic(feesItem.getRadiceLogic());
			applicationFeesItem.setRadiceName(feesItem.getRadiceName());

			loanApplicationFeesItemService.insert(applicationFeesItem);
		}

		// 客户上传凭证快照
		// CUSTOMER_UPLOAD_SNAPSHOT (共用)
		List<CustomerUploadSnapshot> customerUploadSnapshotList = loanApplicationService
				.getCustomerUploadSnapshotListByMainId(mainLoanApplication.getMainLoanApplicationId(), null);
		if (null != customerUploadSnapshotList && customerUploadSnapshotList.size() > 0) {
			for (CustomerUploadSnapshot upload : customerUploadSnapshotList) {
				upload.setSnapshotId(null);
				upload.setMainLoanApplicationId(null);
				upload.setLoanApplicationId(loanApplication.getLoanApplicationId());// set外键
				loanApplicationService.insertCustomerUploadSnapshot(upload);
			}
		}

		// 个人借款-预先处理
		if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(mainLoanApplication.getLoanType())) {

			// 客户基本信息快照
			// CUSTOMER_BASIC_SNAPSHOT
			CustomerBasicSnapshot basic = customerBasicSnapshotService
					.getBasicByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			basic.setSnapshotId(null);
			basic.setMainLoanApplicationId(null);
			basic.setLoanApplicationId(loanApplication.getLoanApplicationId());
			customerBasicSnapshotService.addBasic(basic);

			// 客户工作信息快照
			// CUSTOMER_WORK_SNAPSHOT
			CustomerWorkSnapshot work = customerWorkSnapshotService
					.getWorkByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			work.setSnapshotId(null);
			work.setMainLoanApplicationId(null);
			work.setLoanApplicationId(loanApplication.getLoanApplicationId());
			customerWorkSnapshotService.addWork(work);

			if(LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(mainLoanApplication.getLoanType())){
				//客户车辆抵押信息快照
				// CUSTOMER_CAR_SNAPSHOT
				CustomerCarSnapshot car = customerCarSnapshotService
						.getCarByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
				car.setSnapshotId(null);
				car.setMainLoanApplicationId(null);
				car.setLoanApplicationId(loanApplication.getLoanApplicationId());
				customerCarSnapshotService.addCar(car);
			}else{
				// 客户房产抵押信息快照
				// CUSTOMER_HOUSE_SNAPSHOT
				CustomerHouseSnapshot house = customerHouseSnapshotService
						.getHouseByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
				house.setSnapshotId(null);
				house.setMainLoanApplicationId(null);
				house.setLoanApplicationId(loanApplication.getLoanApplicationId());
				customerHouseSnapshotService.addHouse(house);
			}

			// 客户联系人快照
			// CUSTOMER_CONTACTS_SNAPSHOT
			List<CustomerContactsSnapshot> customerContactsSnapshotList = customerContactsSnapshotService
					.getContactsByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			if (null != customerContactsSnapshotList && customerContactsSnapshotList.size() > 0) {
				for (CustomerContactsSnapshot contacts : customerContactsSnapshotList) {
					contacts.setSnapshotId(null);
					contacts.setMainLoanApplicationId(null);
					contacts.setLoanApplicationId(loanApplication.getLoanApplicationId());
					customerContactsSnapshotService.addContacts(contacts);
				}
			}
		}

		// 企业借款-预先处理
		if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(mainLoanApplication.getLoanType())
				|| LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(mainLoanApplication.getLoanType())) {

			// 企业借款申请关联表
			// ENTERPRISE_LOAN_APPLICATION
			EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService
					.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			enterpriseLoanApplication.setEnterpriseLoanApplicationId(null);
			enterpriseLoanApplication.setMainLoanApplicationId(null);
			enterpriseLoanApplication.setLoanApplicationId(loanApplication.getLoanApplicationId());
			enterpriseLoanApplicationService.addEnterpriseLoanApplication(enterpriseLoanApplication);

		}

		// 【0】个人-信贷-借款处理
		if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(mainLoanApplication.getLoanType())) {
			// do nothing
		}

		// 【1】个人-房贷-借款处理
		if (LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(mainLoanApplication.getLoanType())) {
			// do nothing
		}

		// 【2】企业-车贷-借款处理
		if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(mainLoanApplication.getLoanType())) {
			// 企业车贷快照
			// ENTERPRISE_CAR_LOAN_SNAPSHOT
			EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService
					.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			Long enterpriseCarLoanIdOld = carLoanSnapshot.getEnterpriseCarLoanId();
			carLoanSnapshot.setEnterpriseCarLoanId(null);
			carLoanSnapshot.setMainLoanApplicationId(null);
			carLoanSnapshot.setLoanApplicationId(loanApplication.getLoanApplicationId());
			EnterpriseCarLoanSnapshot carLoanSnapshotNew = enterpriseCarLoanSnapshotService.addEnterpriseCarLoanSnapshot(carLoanSnapshot);

			//抵押车信息处理【开始】
			List<MortgageCarSnapshot> mortgageCarSnapshotOldList = mortgageCarSnapshotService.getListByCarLoanId(enterpriseCarLoanIdOld);
			for (MortgageCarSnapshot mortgageCarSnapshotOld : mortgageCarSnapshotOldList) {
				mortgageCarSnapshotOld.setMortgageCarId(null);//清理主键
				mortgageCarSnapshotOld.setCarLoanId(carLoanSnapshotNew.getEnterpriseCarLoanId());
				mortgageCarSnapshotService.addMortgageCarSnapshot(mortgageCarSnapshotOld);
			}
			//抵押车信息处理【结束】

		}

		// 【3】企业-信贷-借款处理
		if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(mainLoanApplication.getLoanType())) {
			// 企业信用贷快照
			// ENTERPRISE_CREDIT_SNAPSHOT
			EnterpriseCreditSnapshot creditSnapshot = enterpriseCreditSnapshotService
					.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			creditSnapshot.setEnterpriseCreditId(null);
			creditSnapshot.setMainLoanApplicationId(null);
			creditSnapshot.setLoanApplicationId(loanApplication.getLoanApplicationId());
			enterpriseCreditSnapshotService.addEnterpriseCreditSnapshot(creditSnapshot);
		}

		// 【4】企业-保理-借款处理
		if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(mainLoanApplication.getLoanType())) {
			// 企业保理快照
			// ENTERPRISE_FACTORING_SNAPSHOT
			EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService
					.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			factoringSnapshot.setEnterpriseFactoringId(null);
			factoringSnapshot.setMainLoanApplicationId(null);
			factoringSnapshot.setLoanApplicationId(loanApplication.getLoanApplicationId());
			enterpriseFactoringSnapshotService.addEnterpriseFactoringSnapshot(factoringSnapshot);
		}

		// 【5】企业-基金-借款处理
		if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(mainLoanApplication.getLoanType())) {
			// 企业基金快照
			// ENTERPRISE_FOUNDATION_SNAPSHOT
			EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService
					.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			foundationSnapshot.setEnterpriseFoundationId(null);
			foundationSnapshot.setMainLoanApplicationId(null);
			foundationSnapshot.setLoanApplicationId(loanApplication.getLoanApplicationId());
			enterpriseFoundationSnapshotService.addEnterpriseFoundationSnapshot(foundationSnapshot);
		}


		// 【6】企业标-质押标-借款处理
		if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(mainLoanApplication.getLoanType())) {
			// 企业标-质押标快照
			// ENTERPRISE_CREDIT_SNAPSHOT
			EnterprisePledgeSnapshot pledgeSnapshot = enterprisePledgeSnapshotService
					.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
			pledgeSnapshot.setEnterprisePledgeId(null);
			pledgeSnapshot.setMainLoanApplicationId(null);
			pledgeSnapshot.setLoanApplicationId(loanApplication.getLoanApplicationId());
			enterprisePledgeSnapshotService.addEnterprisePledgeSnapshot(pledgeSnapshot);
		}

		// 【7】个人-房产直投-借款处理
		if (LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(mainLoanApplication.getLoanType())) {
			// do nothing
		}

		// 【8】个人-个人车贷-借款处理
		if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(mainLoanApplication.getLoanType())) {
			// do nothing
		}

		// *******************************************************************************************************
		// ***** 执行借款申请，拆标COPY操作。【结束】
		// *************************************************************************
		// *******************************************************************************************************

		// 定向标处理
		Map<String, String> map = this.vaDataInExcelAndPassword(opassword, parseExcelReturnUser,
				loanApplication.getLoanApplicationId(), "",newUserRadio);
		String type = map.get("type");
		if (!"".equals(type) && type != null) { // password user null fail
												// repart
			if (type.equals("repart")) {
				throw new SystemException(ValidationErrorCode.ERROR_PUBLISH_REPART);
				// return "repart";//定向用户信息不匹配
			} else if (type.trim().equals("fail")) {
				// return "fail";// 定向设置保存失败 fail
				throw new SystemException(ValidationErrorCode.ERROR_PUBLISH_FAIL);
			} else if (type.trim().equals("exist")) {
				// return "exist";//已经定向设置
				throw new SystemException(ValidationErrorCode.ERROR_PUBLISH_EXIST);
			} else if (type.trim().equals("notEquals")) {
				throw new SystemException(ValidationErrorCode.ERROR_PUBLISH_NOTEQUALS);
				// return "notEquals";//用户名与数据库用户名不匹配
			}
		}
		return "success";
	}

	/**
	 * 创建还款计划(供上面 publish 方法使用)
	 *
	 * @param product
	 * @param loanApp
	 * @param channelType
	 * @return
	 * @throws Exception
	 */
	private List<RepaymentPlan> createRepaymentPLan(LoanProduct product, LoanApplication loanApp, String channelType,
			Date firstRepaymentDate) throws Exception {
		List<RepaymentPlan> results = new ArrayList<RepaymentPlan>();
		results.addAll(
				repaymentPlanService.generateRepaymentPlan(product.getLoanProductId(), loanApp.getConfirmBalance()));
		Date theDay = firstRepaymentDate;
		Date startDate = new Date();
		for (RepaymentPlan repaymentPlan : results) {
			repaymentPlan.setCustomerAccountId(loanApp.getCustomerAccountId());
			repaymentPlan.setLoanApplicationId(loanApp.getLoanApplicationId());
			repaymentPlan.setChannelType(channelType.charAt(0));
			repaymentPlan.setRepaymentDay(theDay);
			repaymentPlan.setPlanState(RepaymentPlanStateEnum.UNCOMPLETE.value2Char());
			repaymentPlan.setStartDate(startDate);
			startDate = theDay;
			if (product.getDueTimeType() == LoanProduct.DUETIMETYPE_DAY) {
				theDay = DateUtil.addDate(theDay, Calendar.DAY_OF_MONTH, product.getCycleValue());
			} else if (product.getDueTimeType() == LoanProduct.DUETIMETYPE_MONTH) {
				theDay = DateUtil.addDate(theDay, Calendar.MONTH, 1);
			}
			repaymentPlanService.create(repaymentPlan);
		}
		return results;
	}

	@Override
	public LoanPublishVO findLoanPublishVO(Long loanApplicationId) {
		LoanPublishVO lpv = myBatisDao.get("LOANPUBLISH.findLoanPublishVO", loanApplicationId);
		if (null != lpv) {
			ConstantDefine constantDefine_ = new ConstantDefine();
			if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(lpv.getLoanType())
					|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(lpv.getLoanType())
					|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(lpv.getLoanType())
					|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(lpv.getLoanType())) {
				constantDefine_.setConstantTypeCode("loanUseage");
			} else {
				constantDefine_.setConstantTypeCode("enterpriseLoanUseage");
			}
			constantDefine_.setConstantValue(lpv.getLoanUseage());
			ConstantDefine constantDefine = constantDefineService.findConstantByTypeCodeAndValue(constantDefine_);
			if (constantDefine != null)
				lpv.setLoanUseageName(constantDefine.getConstantName());
		}
		return lpv;
	}

	/**
	 * @param opassword
	 *            定向密码
	 * @param parseExcelReturnUser
	 *            定向用户
	 * @return 定向是否成功 及失败消息
	 * @author wangyadong
	 * @since
	 */
	@Override
	@Transactional
	public Map<String, String> vaDataInExcelAndPassword(String opassword,
			List<Map<String, Object>> parseExcelReturnUser, Long applicationId, String type,String newUserRadio) {
		Map<String, String> map = new HashMap<String, String>();
		LoanOrientation lo = new LoanOrientation();
		String otype ="";
		int count = myBatisDao.get("LOAN_ORIENTATION.selectByPrimaryKeyCount", applicationId);
		try {
			if (count != 0) {//修改
				otype = myBatisDao.get("getCountOtypeByLoanApplicationId", applicationId);
				if (opassword != null && !"".equals(opassword)|| parseExcelReturnUser != null && parseExcelReturnUser.size() > 0
						||newUserRadio!=null&&!"".equals(newUserRadio)) {
					myBatisDao.delete("LOAN_ORIENTATION.deleteByPrimaryKey", applicationId);
				} else {
					myBatisDao.delete("LOAN_ORIENTATION.deleteByPrimaryKey", applicationId);
					lo.setLoanApplicationId(applicationId);
					lo.setoType(0l);
					myBatisDao.update("LOAN_ORIENTATION.updateByPrimaryKeySelective", lo);
				}
			}
			if (opassword != null && !"".equals(opassword)) {
				lo.setoPassVo(opassword);
				lo.setLoanApplicationId(applicationId);// 借款申请ID
				lo.setoPass(MD5Util.MD5Encode(opassword.trim(), "utf-8"));// 定向密码
				lo.setoType(1l);// 1为定向密码
			    map = updateOrientPass(lo,1);
			} else if (parseExcelReturnUser != null) {
				// 验证符不符合要求，并且保存
				lo.setLoanApplicationId(applicationId);
				map = updateOrientUser(parseExcelReturnUser, lo);

			}else if(newUserRadio!=null&& !"".equals(newUserRadio)){
				lo.setLoanApplicationId(applicationId);// 借款申请ID
				lo.setoType(3l);// 1为定向密码
			    map = updateOrientPass(lo,3);
			} else if (type != null) {
				myBatisDao.delete("LOAN_ORIENTATION.deleteByPrimaryKey", applicationId);
				lo.setLoanApplicationId(applicationId);
				lo.setoType(0l);
				myBatisDao.update("LOANPUBLISH.updateByPrimaryKeySelectiveByType", lo);
			} else {
				map.put("type", "null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("type", "fail");
		}
		return map;
	}

	/**
	 * 设置定向密码和定向新手用户
	 *
	 * @param otype
	 * @return
	 */
	public Map<String, String> updateOrientPass(LoanOrientation lo,int otype) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if (otype == 1) {//定向密码
			myBatisDao.insert("LOAN_ORIENTATION.insertSelective", lo);
			myBatisDao.update("LOANPUBLISH.updateByPrimaryKeySelectiveByType", lo);
			map.put("type", "password");
		} else if (otype == 3){//新手用户
			myBatisDao.insert("LOAN_ORIENTATION.insertSelective", lo);
			myBatisDao.update("LOANPUBLISH.updateByPrimaryKeySelectiveByType", lo);
			map.put("type", "newUserRadio");
		}
		return map;
	}

	/**
	 * 设置定向用户
	 *
	 * @param parseExcelReturnUser
	 * @param otype
	 * @param applicationId
	 * @return
	 * @author 王亚东
	 */
	public Map<String, String> updateOrientUser(List<Map<String, Object>> parseExcelReturnUser, LoanOrientation lo
			) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		for (Map<String, Object> map1 : parseExcelReturnUser) {
			if (map1 != null) {
				String userName = (String) map1.get("用户姓名");
				String phone = (String) map1.get("手机号");
				if (phone != null && !"".equals(phone.trim()) && userName != null && !"".equals(userName.trim())) {
					UserInfo userInfo = myBatisDao.get("USER_INFO.getUserByConditionPhone", phone);
					if (userInfo != null) {
						UserInfoExt userInfoExt = myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey",userInfo.getUserId());
						if(userInfoExt.getRealName()==null||userInfoExt.getRealName().length()<=0||"".equals(userInfoExt.getRealName())){
							map.put("type", "notEquals");
							return map;
						}
						if (userInfoExt.getRealName().trim().equals(userName.trim())) {
							lo.setoType(2l);
							updateOrientUser(lo, userInfo, userName, phone);
						} else {
							map.put("type", "notEquals");
							return map;
						}
//						if (!type.trim().equals("") && type != null) {// 判断是不是修改操作
//							if (userInfoExt.getRealName().trim().equals(userName.trim())) {
//								lo.setLoanApplicationId(applicationId);
//								updateOrientUser(lo, userInfo, userName, phone);
//							} else {
//								map.put("type", "notEquals");
//								return map;
//							}
//						} else {
//							if (userInfoExt.getRealName().trim().equals(userName.trim())) {
//								lo.setLoanApplicationId(applicationId);
//								updateOrientUser(lo, userInfo, userName, phone);
//							} else {
//								map.put("type", "notEquals");
//								return map;
//							}
//						}
					} else {
						map.put("type", "repart");
						map.put("repart", phone);
						return map;
					}
				}
			}
		}
		myBatisDao.update("LOANPUBLISH.updateByPrimaryKeySelectiveByType", lo);
		map.put("type", "user"); // 定向用户
		return map;
	}

	/**
	 * 定向用户设置 王亚东
	 *
	 * @param lo
	 * @return
	 */
	public String updateOrientUser(LoanOrientation lo, UserInfo userInfo, String userName, String phone) {
		try {
			lo.setoUserid(userInfo.getUserId());// 定向用户ID
			lo.setLogName(userInfo.getLoginName());
			lo.setUserName(userName);
			lo.setPhone(phone);
			System.out.println("userName = " + userName);
			System.out.println("phone = " + phone);
			lo.setLoanApplicationId(lo.getLoanApplicationId());// 借款申请ID
			lo.setoType(2l);// 2为定向用户
			myBatisDao.insert("LOAN_ORIENTATION.insertSelective", lo);
		} catch (Exception e) {
			e.printStackTrace();
			return "FAL";
		}
		return "SUC";
	}

	// by mainid
	@Override
	public LoanPublishVO findLoanPublishVOByMainId(Long mainLoanApplicationId) {
		LoanPublishVO lpv = myBatisDao.get("LOANPUBLISH.findLoanPublishVOByMainId", mainLoanApplicationId);
		if (null != lpv) {
			ConstantDefine constantDefine_ = new ConstantDefine();
			if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(lpv.getLoanType())
					|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(lpv.getLoanType())
					|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(lpv.getLoanType())
					|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(lpv.getLoanType())) {
				constantDefine_.setConstantTypeCode("loanUseage");
			} else {
				constantDefine_.setConstantTypeCode("enterpriseLoanUseage");
			}
			constantDefine_.setConstantValue(lpv.getLoanUseage());
			ConstantDefine constantDefine = constantDefineService.findConstantByTypeCodeAndValue(constantDefine_);
			if (constantDefine != null)
				lpv.setLoanUseageName(constantDefine.getConstantName());
		}
		return lpv;
	}

	@Override
	public void deleteOrient(Long loanApplicationId) {
		// TODO Auto-generated method stub
		myBatisDao.delete("LOAN_ORIENTATION.deleteByPrimaryKey", loanApplicationId);
	}

	@Override
	public String updateOrientByAll(Long loanApplicationId) {
		// TODO Auto-generated method stub
		LoanOrientation lo = new LoanOrientation();
		lo.setoType(0l);
		lo.setLoanApplicationId(loanApplicationId);
		try{
			myBatisDao.delete("LOAN_ORIENTATION.deleteByPrimaryKey", loanApplicationId);
			myBatisDao.update("LOANPUBLISH.updateByPrimaryKeySelectiveByType", lo);
			return "suc";
		}catch(Exception e){
			e.printStackTrace();
		}

		return "fal";
	}
}
