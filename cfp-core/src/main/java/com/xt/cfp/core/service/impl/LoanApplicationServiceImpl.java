package com.xt.cfp.core.service.impl;

import com.external.deposites.utils.HfUtils;
import com.itextpdf.text.DocumentException;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.*;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.AgreementEnum.AgreementStatusEnum;
import com.xt.cfp.core.constants.AgreementEnum.AgreementTypeEnum;
import com.xt.cfp.core.constants.AgreementEnum.AreementCodeTypeEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.PayConstants.PayChannel;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.context.ParaContext;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.matchrules.MatchCreditorVO;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by lenovo on 15-6-23.
 */
@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private static Logger logger = Logger.getLogger(LoanApplicationServiceImpl.class);
    @Autowired
    private MyBatisDao myBatisDao;
    @Value(value = "${DELETE_PATH}")
    private String deletePath;
    @Value(value = "${FORMAL_PATH}")
    private String formalPath;
    @Value(value = "${TEMPORARY_PATH}")
    private String temporaryPath;
    @Value(value = "${ROOT_PATH}")
    private String rootP;

    @Autowired
    private RedisCacheManger redisCacheManger;


    public String getDeletePath() {
        return deletePath;
    }

    public void setDeletePath(String deletePath) {
        this.deletePath = deletePath;
    }

    public String getFormalPath() {
        return formalPath;
    }

    public void setFormalPath(String formalPath) {
        this.formalPath = formalPath;
    }

    public String getTemporaryPath() {
        return temporaryPath;
    }

    public void setTemporaryPath(String temporaryPath) {
        this.temporaryPath = temporaryPath;
    }

    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private LoanApplicationFeesItemService loanApplicationFeesItemService;
    @Autowired
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private LoanFeesDetailService loanFeesDetailService;

    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private LendLoanBindingService lendLoanBindingService;
    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CustomerBasicSnapshotService basicSnapshotService;
    @Autowired
    private UserInfoExtService userInfoExtService;

    @Autowired
    private CustomerBasicSnapshotService customerBasicSnapshotService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerWorkSnapshotService customerWorkSnapshotService;
    @Autowired
    private CustomerContactsSnapshotService customerContactsSnapshotService;
    @Autowired
    private CustomerHouseSnapshotService customerHouseSnapshotService;
    @Autowired
    private RightsPrepayDetailService rightsPrepayDetailService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private BondSourceService bondSourceService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private AwardDetailService awardDetailService;
    @Autowired
    private ProvinceInfoService provinceInfoService;
    @Autowired
    private CityInfoService cityInfoService;
    @Autowired
    private AgreementInfoService agreementInfoService;
    @Autowired
    private PayService payService;
    @Autowired
    private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
    @Autowired
    private EnterpriseInfoService enterpriseInfoService;
    @Autowired
    private GuaranteeCompanyService guaranteeCompanyService;
    @Autowired
    private EnterpriseFoundationSnapshotService enterpriseFoundationSnapshotService;
    @Autowired
    private CoLtdService coLtdService;

    @Autowired
    private MainLoanApplicationService mainLoanApplicationService;

    @Autowired
    private FinancePlanAgreementService financePlanAgreementService;

    @Autowired
    private CommiProfitService commiProfitService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;

    @Autowired
    private RateLendOrderService rateLendOrderService;
    @Autowired
    private RateUserService rateUserService;
    @Autowired
    private RateUsageHistoryService rateUsageHistoryService;
    @Autowired
    private CustomerCarSnapshotService customerCarSnapshotService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CapitalFlowService capitalFlowService;

    @Value(value = "${VOUCHER_INVITATION_ID}")
    private Long voucherTen;
    @Value(value = "${INVITATION_TOWARD_INCOM}")
    private BigDecimal towardsIncome;

    @Override
    public LoanApplication findById(long loanApplicationId) {
        return myBatisDao.get("LOANAPPLICATION.selectByPrimaryKey", loanApplicationId);
    }

    @Override
    public LoanApplication findLockById(long loanApplicationId) {
        return myBatisDao.get("LOANAPPLICATION.lockByPrimaryKey", loanApplicationId);
    }

    public void update(LoanApplication loanApplication) {
        myBatisDao.update("LOANAPPLICATION.updateLoanApplication", loanApplication);
    }


    public void update(Map loanAppMap) {
        myBatisDao.update("LOANAPPLICATION.updateByMap", loanAppMap);
    }

    @Override
    public List<LoanApplication> findByStates(LoanApplicationStateEnum... enums) {
        List<String> states = null;
        if (enums != null) {
            states = new ArrayList<String>();
            for (LoanApplicationStateEnum anEnum : enums) {
                states.add(anEnum.getValue());
            }
        }
        Map map = new HashMap();
        map.put("states", states);
        return myBatisDao.getList("LOANAPPLICATION.findByStates", map);
    }

    @Override
    @Transactional
    public void updateLoanAndAddVerify(LoanApplication loanApplication, long adminId, String fullContent) {
        //todoed 补充满标时未来得及处理的待支付的订单，订单状态改为超时，出借明细状态改为超时
        List<LendOrderBidDetail> orderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(loanApplication.getLoanApplicationId());
        for (LendOrderBidDetail orderBidDetail : orderBidDetails) {
            if (orderBidDetail.getStatus() == LendOrderBidStatusEnum.WAITING_PAY.value2Char()) {
                orderBidDetail.setStatus(LendOrderBidStatusEnum.OUT_TIME.value2Char());
                lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.OUT_TIME.value2Char(), 0l);

                LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
                lendOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.OUT_TIME.getValue());
                Map<String, Object> lendOrderMap = new HashMap<String, Object>();
                lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
                lendOrderMap.put("orderState", lendOrder.getOrderState());
                lendOrderService.update(lendOrderMap);
            }
        }

        Verify verify = new Verify();
        verify.setLoanApplicationId(loanApplication.getLoanApplicationId());
        verify.setResult(VerifyStatus.PAST.getValue());
        verify.setUserId(adminId);
        verify.setVerifySuggestion(fullContent);
        verify.setVerifyTime(new Date());
        verify.setVerifyType(VerifyType.FULL_SCALE_REVIEW.getValue());

        Map<String, Object> loanMap = new HashMap<String, Object>();
        loanMap.put("loanApplicationId", loanApplication.getLoanApplicationId());
        loanMap.put("lendState", loanApplication.getLendState());
        loanMap.put("applicationState", loanApplication.getApplicationState());
        loanMap.put("verify", verify);

        this.update(loanMap);
        myBatisDao.insert("VERIFY.insertSelective", verify);

    }

    @Override
    public Pagination<LoanApplicationExtOne> getAllLoanApplicationList(int pageNum,
                                                                       int pageSize, String loanApplicationCode,
                                                                       String loanApplicationName, String channel, String loanType,
                                                                       String realName, String idCard, String mobileNo,
                                                                       String applicationState) {
        Pagination<LoanApplicationExtOne> re = new Pagination<LoanApplicationExtOne>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationCode", loanApplicationCode);
        params.put("loanApplicationName", loanApplicationName);
        params.put("loanType", loanType);
        params.put("channel", channel);
        params.put("realName", realName);
        params.put("idCard", idCard);
        params.put("mobileNo", mobileNo);
        params.put("applicationState", applicationState);

        int totalCount = this.myBatisDao.count("getAllLoanApplicationListPaging", params);
        List<LoanApplicationExtOne> loanApplicationInfo = this.myBatisDao.getListForPaging("getAllLoanApplicationListPaging", params, pageNum, pageSize);
        re.setTotal(totalCount);
        re.setRows(loanApplicationInfo);
        return re;
    }

    @Override
    public void exportExcel(HttpServletResponse response,
                            String loanApplicationCode, String loanApplicationName,
                            String channel, String loanType, String realName, String idCard,
                            String mobileNo, String applicationState) {
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        String[] title = {"类型", "来源", "标的编号", "借款名称", "标的名称", "标的期限", "借款人姓名", "身份证号", "手机号", "借款金额", "批复金额", "已投", "标的状态", "是否逾期", "创建时间", "发标时间", "满标时间", "放款日期", "最后一期还款日", "合同状态"};
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationCode", loanApplicationCode);
        params.put("loanApplicationName", loanApplicationName);
        params.put("loanType", loanType);
        params.put("channel", channel);
        params.put("realName", realName);
        params.put("idCard", idCard);
        params.put("mobileNo", mobileNo);
        params.put("applicationState", applicationState);
        List<LoanApplicationExtOne> loanApplicationInfo = this.myBatisDao.getList("getAllLoanApplicationListPaging", params);
        DecimalFormat df = new DecimalFormat("0.00");
        for (LoanApplicationExtOne le : loanApplicationInfo) {
            Map<String, Object> map = new HashMap<String, Object>();
            String loanT = "";
            switch (le.getLoanType()) {
                case "0":
                    loanT = "信贷";
                    break;
                case "1":
                    loanT = "房贷";
                    break;
                case "2":
                    loanT = "企业车贷";
                    break;
                case "3":
                    loanT = "企业信贷";
                    break;
                case "4":
                    loanT = "企业保理";
                    break;
                case "6":
                    loanT = "企业标";
                    break;
                case "7":
                    loanT = "个人房产直投";
                    break;
            }
            map.put(title[0], loanT);
            String ch = "";
            if (le.getChannel() != null) {
                ch = le.getChannel().equals("1") ? "渠道" : "门店";
            }
            map.put(title[1], ch);
            map.put(title[2], le.getLoanApplicationCode());
            map.put(title[3], le.getLoanApplicationName());
            map.put(title[4], le.getLoanTitle());
            map.put(title[5], le.getDurationTime());
            map.put(title[6], le.getUserRealName());
            map.put(title[7], le.getIdCard());
            map.put(title[8], le.getMobileNo());
            map.put(title[9], df.format(BigDecimalUtil.ToBigDecimalValue(le.getLoanBalance())));
            map.put(title[10], df.format(BigDecimalUtil.ToBigDecimalValue(le.getConfirmBalance())));
            String hc = "0.00";
            switch (le.getApplicationState()) {
                case "0":
                case "1":
                case "2":
                case "9":
                case "A":
                    hc = "0.00";
                    break;
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                    hc = df.format(BigDecimalUtil.ToBigDecimalValue(le.getConfirmBalance()));
                    break;
                case "3":
                    hc = df.format(le.getHaveCast());
                    break;
            }
            map.put(title[11], hc);
            String as = "";
            switch (le.getApplicationState()) {
                case "0":
                    as = "草稿";
                    break;
                case "1":
                    as = "风控审核中";
                    break;
                case "2":
                    as = "发标审核中";
                    break;
                case "3":
                    as = "投标中";
                    break;
                case "4":
                    as = "放款审核中";
                    break;
                case "5":
                    as = "待放款";
                    break;
                case "6":
                    as = "还款中";
                    break;
                case "7":
                    as = "已结清";
                    break;
                case "8":
                    as = "提前还贷";
                    break;
                case "9":
                    as = "取消";
                    break;
                case "A":
                    as = "流标";
                    break;
            }
            map.put(title[12], as);
            map.put(title[13], le.getIsDelay() == 0 ? "否" : "是");
            try {
                map.put(title[14], DateUtil.getPlusTime(le.getCreateTime()));
                map.put(title[15], DateUtil.getPlusTime(le.getPublishTime()));
                map.put(title[16], DateUtil.getPlusTime(le.getFullTime()));
                map.put(title[17], DateUtil.getPlusTime(le.getPaymentDate()));
                map.put(title[18], DateUtil.getPlusTime(le.getOpertionDate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put(title[19], le.getAgreementStatus());
            dataMap.add(map);
        }
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "个人借款列表";
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(title, dataMap, fileName);
            wb.write(os);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Pagination<LoanApplicationExtOne> getAllLoanApplicationList(int pageNum, int pageSize, LoanApplication loanApplication, String startTime,
                                                                       String endTime) {
        Pagination<LoanApplicationExtOne> re = new Pagination<LoanApplicationExtOne>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("applicationState", loanApplication.getApplicationState());
        params.put("loanType", loanApplication.getLoanType());
        params.put("userId", loanApplication.getUserId());
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        int totalCount = this.myBatisDao.count("getAllLoanApplicationListPaging", params);
        List<LoanApplicationExtOne> loanApplicationInfo = this.myBatisDao.getListForPaging("getAllLoanApplicationListPaging", params, pageNum,
                pageSize);
        re.setTotal(totalCount);
        re.setRows(loanApplicationInfo);
        return re;
    }

    //编辑发标描述列表
    @Override
    public Object getAllLoanAppPublishDescEditList(int pageNum, int pageSize, LoanApplicationExtOne loanApplicationExtOne) {
        Pagination<LoanApplicationExtOne> re = new Pagination<LoanApplicationExtOne>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("getAllLoanAppPublishDescEditListPaging", loanApplicationExtOne);
        List<LoanApplicationExtOne> loanApplicationInfo = this.myBatisDao.getListForPaging("getAllLoanAppPublishDescEditListPaging", loanApplicationExtOne, pageNum, pageSize);
        re.setTotal(totalCount);
        re.setRows(loanApplicationInfo);
        return re;
    }

    // 编辑发标描述列表，待发标列表
    @Override
    public Object getLoanAppPagingByMainId(int pageNum, int pageSize, LoanApplicationExtOne loanApplicationExtOne) {
        Pagination<LoanApplicationExtOne> re = new Pagination<LoanApplicationExtOne>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("getLoanAppPagingByMainId", loanApplicationExtOne);
        List<LoanApplicationExtOne> loanApplicationInfo = this.myBatisDao.getListForPaging("getLoanAppPagingByMainId", loanApplicationExtOne, pageNum, pageSize);
        re.setTotal(totalCount);
        re.setRows(loanApplicationInfo);
        return re;
    }

    // get count by mainid
    @Override
    public Integer getLoanAppCountByMainId(Long mainLoanApplicationId) {
        return this.myBatisDao.count("getLoanAppPagingByMainId", mainLoanApplicationId);
    }

    // by mainid
    @Override
    @Transactional
    public CustomerUploadSnapshot saveRelatedAccessories(Long userId,
                                                         String fileName, String imgPath, String url, String type,
                                                         String status, Long seqNum, Long loanApplicationId, String thumbnailUrl, String isCode) {
        Attachment att = new Attachment();
        att.setUserId(userId);
        att.setType(type);
        att.setUrl(url);
        att.setPhysicalAddress(imgPath);
        att.setFileName(fileName);
        att.setThumbnailUrl(thumbnailUrl);
        att.setCreateTime(new Date());
        att.setIsCode(isCode);
        myBatisDao.insert("ATTACHMENT.insert", att);

        CustomerUploadSnapshot cus = new CustomerUploadSnapshot();
        cus.setMainLoanApplicationId(loanApplicationId);//main
        cus.setSeqNum(seqNum);
        cus.setAttachId(att.getAttachId());
        cus.setStatus(status);
        cus.setType(type);
        myBatisDao.insert("CUSTOMER_UPLOAD_SNAPSHOT.insert", cus);
        return cus;
    }

    @Override
    public CustomerUploadSnapshot insertCustomerUploadSnapshot(CustomerUploadSnapshot customerUploadSnapshot) {
        myBatisDao.insert("CUSTOMER_UPLOAD_SNAPSHOT.insert", customerUploadSnapshot);
        return customerUploadSnapshot;
    }

    @Override
    public Attachment getAttachmentBycusId(Long cusId) {

        return myBatisDao.get("ATTACHMENT.getAttachmentBycusId", cusId);
    }

    @Override
    public void submitMakeLoan(long loanApplicationId, long adminId, String desc, char channel, Date now, Date financialLendDate) throws Exception {
        LoanApplication loanApplication = this.findById(loanApplicationId);
        //判断放款状态 如果该申请已放款，则直接返回
        if (loanApplication.getLendState().equals(LoanAppLendAuditStatusEnums.REJECT.getValue())
                || !loanApplication.getApplicationState().equals(LoanApplicationStateEnum.WAITMAKELOANAUDIT.getValue())) {
            return;
        }

        LoanApplicationServiceImpl bean = ApplicationContextUtil.getBean(LoanApplicationServiceImpl.class);
        //执行放款
       /* List<Map> toAgreementList = bean.makeLoan(loanApplicationId, adminId, desc, channel, now, financialLendDate);*/
        bean.makeLoan(loanApplicationId, adminId, desc, channel, now, financialLendDate);

        //生成合同
//        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.LOAN.getValue())) {
//            //todo 生成出借合同
//            for (Map<String, Object> agreementMap : toAgreementList) {
//                String type = (String) agreementMap.get("type");
//                if (type.equals("BUY")) {
//                    try {
//                        createAgreement(now, agreementMap, AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        } else {
//            //todo 生成债权转让合同
//        }
    }

    /**
     * <pre>
     * 借款申请放款
     * 1.修改借款信息
     * 2.生成还款计划
     * 3.如借款申请是借款标则生成债权
     * 4.如借款申请是债权标则进行债权转让
     * 5.借款人账户收入批复金额
     * 6.查询放款收费项，如存在收费项，则创建收费明细，借款人账户支出，平台账户收入
     * 7.提交提现申请
     * 8.如借款申请是借款标则生成出借合同，如借款申请是债权标则生成债权转让合同
     * </pre>
     *
     * @param loanApplicationId
     * @param adminId
     * @param channel
     * @param now
     * @param financialLendDate
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Map> makeLoan(long loanApplicationId, long adminId, String theDesc, char channel, Date now, Date financialLendDate) throws Exception {
        //查询借款申请
        UserAccount accountByType = this.userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);

        LoanApplication loanApplication = findLockById(loanApplicationId);
        if (!loanApplication.getApplicationState().equals(LoanApplicationStateEnum.WAITMAKELOANAUDIT.getValue()))
            return null;
        Map map = new HashMap();
        map.put("businessId", loanApplicationId);
        List<Schedule> schedulesItem = scheduleService.findByCondition(map);
        if (!schedulesItem.isEmpty())//为老用户
        {
            boolean flag = getFreeSchedule(loanApplicationId);
            if (!flag) {
                return null;
            }
        }
        LoanPublish loanPublish = loanPublishService.findById(loanApplicationId);
        LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
        Schedule schedule = insertMainHistoryForDataBase(loanApplicationId);//主任务
        //修改合同生效日，首期还款日，末期还款日，和放款状态
        Date firstRepaymentDate = null;
        Date lastDate = null;
        if (String.valueOf(loanProduct.getDueTimeType()).equals(DueTimeTypeEnum.DAY.getValue())) {
            firstRepaymentDate = DateUtil.addDate(now, Calendar.DAY_OF_MONTH, loanProduct.getCycleValue());
            int cycleCounts = 0;
            if (loanProduct.getDueTime() % loanProduct.getCycleValue() == 0) {
                cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue();
            } else {
                cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue() + 1;
            }
            lastDate = DateUtil.addDate(firstRepaymentDate, Calendar.DAY_OF_MONTH, cycleCounts * loanProduct.getCycleValue() - 1);
        } else if (String.valueOf(loanProduct.getDueTimeType()).equals(DueTimeTypeEnum.MONTH.getValue())) {
            firstRepaymentDate = DateUtil.addDate(now, Calendar.MONTH, 1);
            lastDate = DateUtil.addDate(firstRepaymentDate, Calendar.MONTH, loanProduct.getDueTime() - 1);
        }

        //todoed 创建还款计划
        List<RepaymentPlan> repaymentPlans = createRepaymentPLan(loanProduct, loanApplication, ChannelTypeEnum.ONLINE.getValue(), firstRepaymentDate);

        //******************************************************************************************
        //【开始】创建还款计划（数据来源：下线） 和 创建债权及债权明细（数据来源：下线）。
        long creditorLenderUserId = 0;
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.CREDITOR.getValue())) {
            //如果是债权标
            //todoed 新增还款计划
            loanApplication.setConfirmBalance(loanApplication.getLoanBalance());
            //债权还款计划只保留一套，债权标（原始债权人不再建立）
            //List<RepaymentPlan> repaymentPlans = createRepaymentPLan(loanProduct, loanApplication, ChannelTypeEnum.OFFLINE.getValue(), firstRepaymentDate);
            //todoed 新增出借订单
            long lendAccountId = loanApplication.getCustomerAccountId();
            LendProduct rightLendProduct = lendLoanBindingService.findRightsProduct(loanProduct.getLoanProductId());

            //注:这里应该只查当前有效的，如果有多条有效的，就使用最后一个
            LendProductPublish lendPublish = null;
            List<LendProductPublish> lendPublishList = lendProductService.getByPublishStateAndLendProductId(LendProductPublishStateEnum.SELLING.value2Char(), rightLendProduct.getLendProductId());
            if (null != lendPublishList && lendPublishList.size() > 0) {
                for (LendProductPublish lendProductPublish : lendPublishList) {
                    lendPublish = lendProductPublish;
                }
            }

            UserAccount lendUserAccount = userAccountService.getUserAccountByAccId(lendAccountId);
            creditorLenderUserId = lendUserAccount.getUserId();
            CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(lendUserAccount.getUserId(), PayChannel.HF);
            LendOrder lendOrder = lendOrderService.newRightsLendOrder(rightLendProduct, lendPublish, loanApplication, lendUserAccount, customerCard.getCustomerCardId());

            //todoed 新增出借订单支付单并修改支付单的状态为已支付
            Pair<String, BigDecimal> offAmount = new Pair(PayConstants.AmountType.RECHARGE.getValue(), loanApplication.getConfirmBalance());
            PayOrder payOrder = payService.addPayOrder(loanApplication.getConfirmBalance(), new Date(),
                    lendUserAccount.getUserId(), PayConstants.BusTypeEnum.BUY_FINANCE, offAmount);
            PayOrder updatePayOrder = new PayOrder();
            updatePayOrder.setPayId(payOrder.getPayId());
            updatePayOrder.setStatus(PayConstants.OrderStatus.SUCCESS.getValue());
            updatePayOrder.setProcessStatus(PayConstants.ProcessStatus.SUCCESS.getValue());
            myBatisDao.update("PAY_ORDER.updateByPrimaryKeySelective", updatePayOrder);

            // 关联订单和支付单【开始】
            OrderPayRelations orderPayRelations = new OrderPayRelations();
            orderPayRelations.setPayId(payOrder.getPayId());
            orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
            myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);
            // 新增订单关联关系表数据【结束】

            //todoed 修改订单状态为理财中
            lendOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue());
            Map<String, Object> lendOrderMap = new HashMap<String, Object>();
            lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
            lendOrderMap.put("orderState", lendOrder.getOrderState());
            lendOrderService.update(lendOrderMap);
            //todoed 新增债权 & 新增债权明细
            String rightsCode = creditorRightsService.createRightsCode(CreditorRightsFromWhereEnum.BUY.value2Char(), lendOrder.getOrderCode(), loanApplication.getLoanApplicationCode());
            CreditorRights creditorRights = creditorRightsService.createCreditorRights(lendOrder, loanApplication, rightsCode, lendOrder.getBuyBalance(),
                    DisplayEnum.DISPLAY.value2Char(), BigDecimal.valueOf(0), repaymentPlans, CreditorRightsStateEnum.APPLYTURNOUT, ChannelTypeEnum.OFFLINE);
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
            LendOrderBidDetail orderBidDetail = lendOrderBidDetails.get(0);
            orderBidDetail.setCreditorRightsId(creditorRights.getCreditorRightsId());
            //todoed 修改出借明细的状态及关联债权ID
            lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.BIDSUCCESS.value2Char(), creditorRights.getCreditorRightsId());

        }
        //【结束】创建还款计划（数据来源：下线） 和 创建债权及债权明细（数据来源：下线）。
        //******************************************************************************************

        Map<String, Object> loanAppMap = new HashMap<String, Object>();
        CustomerCard customerCard = customerCardService.findById(loanApplication.getInCardId());

        List<Map> toAgreementList = new ArrayList<Map>();
        long loanAccountId = loanApplication.getCustomerAccountId();
        long cashAccountId = 0l;
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.LOAN.getValue())) {
            UserAccount cashAccount = userAccountService.getCashAccount(loanApplication.getUserId());
            cashAccountId = cashAccount.getAccId();
        } else {
            cashAccountId = loanAccountId;
        }

        long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());

        BigDecimal loanBalance = loanApplication.getLoanBalance();
        BigDecimal allInterest = InterestCalculation.getAllInterest(loanBalance, loanApplication.getAnnualRate(),
                loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(),
                loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());

        AccountValueChangedQueue avcq = new AccountValueChangedQueue();

        CreditorRights oldCreditorRights = null;
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.CREDITOR.getValue())) {

            //修改渠道持有的债权状态以及债权明细状态
            CreditorRights subjectRights = creditorRightsService.getSubjectRightsByLoanApplicationId(loanApplicationId);
            subjectRights.setRightsState(CreditorRightsStateEnum.TURNOUT.value2Char());
            subjectRights.setRightsWorth(BigDecimal.valueOf(0));
            subjectRights.setLendPrice(BigDecimal.valueOf(0));
            subjectRights.setCompleteTime(new Date());
            Map<String, Object> creditorMap = new HashMap<String, Object>();
            creditorMap.put("creditorRightsId", subjectRights.getCreditorRightsId());
            creditorMap.put("rightsState", subjectRights.getRightsState());
            creditorMap.put("rightsWorth", subjectRights.getRightsWorth());
            creditorMap.put("lendPrice", subjectRights.getLendPrice());
            creditorRightsService.update(creditorMap);
            List<RightsRepaymentDetail> details = rightsRepaymentDetailService.getDetailListByRightsId(subjectRights.getCreditorRightsId());
            for (RightsRepaymentDetail detail : details) {
                detail.setRightsDetailState(CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailStateEnum.TURNOUT.value2Char());
                Map rightsDetailMap = new HashMap();
                rightsDetailMap.put("rightsRepaymentDetailId", detail.getRightsRepaymentDetailId());
                rightsDetailMap.put("rightsDetailState", detail.getRightsDetailState());
                rightsRepaymentDetailService.update(rightsDetailMap);
            }
            oldCreditorRights = subjectRights;

            Map<String, Object> agreementMap = new HashMap<String, Object>();
            agreementMap.put("type", "TURN");
            agreementMap.put("oldCreditorRightsId", subjectRights.getCreditorRightsId());
            toAgreementList.add(agreementMap);
        }

        //todoed 创建债权及明细并修改投标明细状态
        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(loanApplicationId, LendOrderBidStatusEnum.BIDING);
        BigDecimal sumProportion = new BigDecimal("0");
        BigDecimal ratio100 = new BigDecimal("100");
        int bidIndex = 0;
        for (LendOrderBidDetail orderBidDetail : lendOrderBidDetails) {
            if (orderBidDetail.getStatus() == LendOrderBidStatusEnum.BIDING.value2Char()) {
                LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
                BigDecimal theRatio = null;
                if (bidIndex == lendOrderBidDetails.size() - 1) {
                    theRatio = ratio100.subtract(sumProportion);
                } else {
                    theRatio = lendOrder.getBuyBalance().divide(loanApplication.getConfirmBalance(), 4, BigDecimal.ROUND_DOWN);
                    theRatio = BigDecimalUtil.down(theRatio, 4).multiply(ratio100);
                }
                sumProportion = sumProportion.add(theRatio);
                CreditorRights creditorRights = creditorRightsService.createCreditorRights(lendOrder, loanApplication,
                        creditorRightsService.createRightsCode(CreditorRightsFromWhereEnum.BUY.value2Char(), lendOrder.getOrderCode(), loanApplication.getLoanApplicationCode()),
                        orderBidDetail.getBuyBalance(), DisplayEnum.DISPLAY.value2Char(), theRatio, repaymentPlans, CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE, ChannelTypeEnum.ONLINE);
                if (oldCreditorRights != null) {
                    createCreditorHistory(oldCreditorRights, creditorRights);
                }
                //划拨操作,出借人转到借款人

                //放款之前处理财富券变现
                BigDecimal voucherVoucher = changeVoucherToAmount(lendOrder, avcq);

                //todoed 出借人解冻并支出
                String descUnFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.UNFREE);
                AccountValueChanged avcUnFreeze = new AccountValueChanged(lendOrder.getCustomerAccountId(), orderBidDetail.getBuyBalance().subtract(voucherVoucher),
                        orderBidDetail.getBuyBalance().subtract(voucherVoucher), AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BUYRIGHTS.getValue(),
                        "CreditorRights", VisiableEnum.DISPLAY.getValue(), creditorRights.getCreditorRightsId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                        lendOrder.getLendOrderId(), new Date(), descUnFreeze, false);
                avcq.addAccountValueChanged(avcUnFreeze);

                String desc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.MAKELOAN_PAY, loanPublish.getLoanTitle());
                AccountValueChanged avc = new AccountValueChanged(lendOrder.getCustomerAccountId(), orderBidDetail.getBuyBalance(),
                        orderBidDetail.getBuyBalance(), AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BUYRIGHTS.getValue(),
                        "CreditorRights", VisiableEnum.DISPLAY.getValue(), creditorRights.getCreditorRightsId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                        lendOrder.getLendOrderId(), new Date(), desc, false);
                avcq.addAccountValueChanged(avc);
                //修改出借订单明细状态
                lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.BIDSUCCESS.value2Char(), creditorRights.getCreditorRightsId());
                /**
                 * 出借人转到借款人 冻结状态
                 */
                insertSubTaskByLoan(creditorRights.getLendUserId(), creditorRights.getLoanUserId(), avcUnFreeze.getChangeValue(), schedule.getScheduleId(), HFOperationEnum.FROZEN_TO_FROZEN_PERSON_TO_PERSON.getValue(), loanApplicationId);
                /**
                 * 公司平台转账到用户
                 */
                if (voucherVoucher.compareTo(new BigDecimal("0")) > 0) {
                    insertSubTaskByLoan(accountByType.getUserId(), creditorRights.getLendUserId(), voucherVoucher, schedule.getScheduleId(), HFOperationEnum.ALLOCATION_COMPANY_TO_PERSON.getValue(), loanApplicationId);
                }

                /**
                 * 解冻操作
                 */
                insertSubTaskByLoan(creditorRights.getLoanUserId(), null, avcUnFreeze.getChangeValue(), schedule.getScheduleId(), HFOperationEnum.UNFROZEN.getValue(), loanApplicationId);

                if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
                    // 获取债权明细
                    List<RightsRepaymentDetail> rightsRepaymentDetails = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
                    for (RightsRepaymentDetail rrd : rightsRepaymentDetails) {
                        LendOrderReceive lendOrderReceiveDetail = new LendOrderReceive();
                        lendOrderReceiveDetail.setLendOrderId(lendOrder.getLendOrderId());
                        lendOrderReceiveDetail.setSectionCode(rrd.getSectionCode());
                        lendOrderReceiveDetail.setReceiveState(LendOrderReceiveStateEnum.UNRECEIVE.value2Char());

                        Date receiveDate = null;

                        for (RepaymentPlan repaymentPlan : repaymentPlans) {
                            if (repaymentPlan.getSectionCode() == rrd.getSectionCode()) {
                                receiveDate = repaymentPlan.getRepaymentDay();
                                break;
                            }
                        }

                        lendOrderReceiveDetail.setShouldCapital(rrd.getShouldCapital());
                        lendOrderReceiveDetail.setShouldInterest(rrd.getShouldInterest());
                        lendOrderReceiveDetail.setShouldCapital2(rrd.getShouldCapital2());
                        lendOrderReceiveDetail.setShouldInterest2(rrd.getShouldInterest2());

                        lendOrderReceiveDetail.setReceiveDate(receiveDate);
                        lendOrderReceiveService.insert(lendOrderReceiveDetail);
                    }

                    UserInfo lendUserInfo = userInfoService.getUserByUserId(lendOrder.getLendUserId());
//                    UserAccount cashUserAccount = userAccountService.getCashAccount(lendUserInfo.getUserId());
                    UserAccount cashUserAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
                    //todo 是否放款奖励
                    if (loanPublish.getAwardPoint() != null && loanPublish.getAwardPoint().equals(AwardPointEnum.ATMAKELOAN.getValue())) {
                        BigDecimal awardBalance = BigDecimal.valueOf(0);
                        if (loanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())) {
                            awardBalance = InterestCalculation.getDaysInfoOnce(lendOrder.getBuyBalance(), loanPublish.getAwardRate().divide(
                                    new BigDecimal(100)), loanProduct.getCycleValue()).get(1).get("interest");
                            awardBalance = BigDecimalUtil.down(awardBalance, 2);
                        } else {
                            awardBalance = InterestCalculation.getAllInterest(lendOrder.getBuyBalance(), loanPublish.getAwardRate(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                            awardBalance = BigDecimalUtil.down(awardBalance, 2);
                        }

                        AwardDetail awardDetail = awardDetailService.insertAwardDetail(now, null, awardBalance, lendOrder, loanApplicationId, AwardPointEnum.ATMAKELOAN, RateLendOrderTypeEnum.AWARD);
                        RateLendOrder rateOrder = rateLendOrderService.findByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderTypeEnum.AWARD.getValue(), RateLendOrderStatusEnum.VALID.getValue());
                        rateOrder.setStatus(RateLendOrderStatusEnum.UN_VALID.getValue());
                        rateLendOrderService.updateRateLendOrder(rateOrder);
                        //todo 平台支付奖励金额
                        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_PAY, lendUserInfo.getLoginName(), loanPublish.getLoanTitle());
                        Long payAwardSystemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
                        AccountValueChanged avcPay = new AccountValueChanged(payAwardSystemAccountId, awardBalance,
                                awardBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(),
                                payAwardSystemAccountId, now, descPay, false);
                        avcq.addAccountValueChanged(avcPay);
                        //todo 出借人收入奖励
                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_INCOME, loanPublish.getLoanTitle(), AwardPointEnum.ATMAKELOAN.getDesc());
                        AccountValueChanged avcIncome = new AccountValueChanged(cashUserAccount.getAccId(), awardBalance,
                                awardBalance, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                                cashUserAccount.getAccId(), now, descIncome, true);
                        avcq.addAccountValueChanged(avcIncome);
                        if (awardBalance.compareTo(new BigDecimal("0")) > 0) {
                            insertSubTaskByLoan(accountByType.getUserId(), creditorRights.getLendUserId(), awardBalance, schedule.getScheduleId(), HFOperationEnum.ALLOCATION_COMPANY_TO_PERSON.getValue(), loanApplicationId);
                        }
                        //找出需要把奖励转到余额的理财子订单、并处理
                        if (lendOrder.getLendOrderPId() != null) {
                            LendOrder pOrder = lendOrderService.findById(lendOrder.getLendOrderPId());
                            if (pOrder != null && pOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                                    && pOrder.getProfitReturnConfig().equals(LendOrderConstants.FinanceProfitReturnEnum.TO_CASH_ACCOUNT.getValue())) {
                                // 获取省心计划账户
                                UserAccount financeAccount = userAccountService.getUserAccountByAccId(pOrder.getCustomerAccountId());
                                UserAccount cashAccount = userAccountService.getCashAccount(pOrder.getLendUserId());

                                AccountValueChanged pay = new AccountValueChanged(
                                        financeAccount.getAccId(),
                                        awardBalance,
                                        awardBalance,
                                        AccountConstants.AccountOperateEnum.PAY.getValue(),
                                        AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEOUT
                                                .getValue(),
                                        AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                                        AccountConstants.VisiableEnum.DISPLAY.getValue(),
                                        pOrder.getLendOrderId(),
                                        AccountConstants.OwnerTypeEnum.USER.getValue(),
                                        financeAccount.getUserId(),
                                        new Date(),
                                        StringUtils
                                                .t2s(DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_PROFIT_TURN_OUT,
                                                        awardBalance), false);
                                AccountValueChanged income = new AccountValueChanged(
                                        cashAccount.getAccId(),
                                        awardBalance,
                                        awardBalance,
                                        AccountConstants.AccountOperateEnum.INCOM.getValue(),
                                        AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEINTO
                                                .getValue(),
                                        AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT
                                                .getValue(),
                                        AccountConstants.VisiableEnum.DISPLAY.getValue(),
                                        pOrder.getLendOrderId(),
                                        AccountConstants.OwnerTypeEnum.USER.getValue(),
                                        cashAccount.getUserId(),
                                        new Date(),
                                        StringUtils
                                                .t2s(DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_PROFIT_QUIT_IN,
                                                        awardBalance), false);
                                avcq.addAccountValueChanged(pay);
                                avcq.addAccountValueChanged(income);
                            }
                        }
                    }

                }
                bidIndex++;

                Map<String, Object> agreementMap = new HashMap<String, Object>();
                agreementMap.put("type", "BUY");
                agreementMap.put("creditorRightsId", creditorRights.getCreditorRightsId());
                agreementMap.put("lendUserId", creditorRights.getLendUserId());
                toAgreementList.add(agreementMap);
            }
        }


        //todoed 借款的打款账户充值
        AccountValueChanged avcIncome = new AccountValueChanged(loanAccountId, loanApplication.getConfirmBalance(),
                loanApplication.getConfirmBalance(), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue(),
                "none", VisiableEnum.DISPLAY.getValue(), 0l, AccountConstants.AccountChangedTypeEnum.LOAN.getValue(), loanApplicationId, new Date(), "放款复审通过，债权放款", false);
        avcq.addAccountValueChanged(avcIncome);
        if (loanAccountId != cashAccountId) {
            //todoed 借款账户转账至用户的资金账户
            AccountValueChanged avcTrunToCash = new AccountValueChanged(loanAccountId, loanApplication.getConfirmBalance(),
                    loanApplication.getConfirmBalance(), AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASH.getValue(),
                    "none", VisiableEnum.DISPLAY.getValue(), 0l, AccountConstants.AccountChangedTypeEnum.LOAN.getValue(), loanApplicationId, new Date(), "借款申请放款，资金转至资金账户", false);
            avcq.addAccountValueChanged(avcTrunToCash);

            //todoed 借款人的资金账户收入
            AccountValueChanged avcCashIncome = new AccountValueChanged(cashAccountId, loanApplication.getConfirmBalance(),
                    loanApplication.getConfirmBalance(), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue(),
                    "none", VisiableEnum.DISPLAY.getValue(), 0l, AccountConstants.AccountChangedTypeEnum.LOAN.getValue(), loanApplicationId, new Date(), "借款申请放款", false);
            avcq.addAccountValueChanged(avcCashIncome);

        }


        //todoed 创建收费明细：保存费用清单,计算放款时需扣金额
        BigDecimal resultBalance = loanApplication.getConfirmBalance();
        List<LoanApplicationFeesItem> loanApplicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(loanApplicationId);
        for (LoanApplicationFeesItem applicationFeesItem : loanApplicationFeesItems) {
            if (applicationFeesItem.getChargeCycle() == FeesPointEnum.ATMAKELOAN.value2Char()) {
                LoanFeesDetail loanFeesDetail = new LoanFeesDetail();
                loanFeesDetail.setLoanApplicationId(loanApplicationId);
                loanFeesDetail.setLoanApplicationFeesItemId(applicationFeesItem.getLoanApplicationFeesItemId());
                loanFeesDetail.setFeesCycle(applicationFeesItem.getChargeCycle());

                BigDecimal feesBalance = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(applicationFeesItem, loanBalance, allInterest, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

                loanFeesDetail.setFees(feesBalance);
                loanFeesDetail.setFees2(BigDecimalUtil.up(feesBalance, 2));
                loanFeesDetail.setPaidFees(loanFeesDetail.getFees2());
                feesBalance = loanFeesDetail.getFees2();
                loanFeesDetail.setFeesState(FeesDetailEnum.PAID.value2Char());
                loanFeesDetailService.insert(loanFeesDetail);
                if (feesBalance.compareTo(BigDecimal.ZERO) > 0) {
                    //增加借款人费用支出流水

                    AccountValueChanged avcFees = new AccountValueChanged(cashAccountId, feesBalance,
                            loanFeesDetail.getFees2(), AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                            "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(), loanFeesDetail.getLoanFeesDetailId(), AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                            loanApplicationId, new Date(), "借款合同【" + loanPublish.getLoanTitle() + "】 放款扣费", true);
                    avcq.addAccountValueChanged(avcFees);

                    //放款时，忽略平台收取比例，所有的费用都归平台
                    BigDecimal systemFeesBalance = feesBalance;
                    BigDecimal systemFeesBalance2 = loanFeesDetail.getFees2();
                    //平台账户增加收入流水
                    AccountValueChanged avcSystemIncomeFees = new AccountValueChanged(systemAccountId, systemFeesBalance,
                            systemFeesBalance2, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                            "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(), loanFeesDetail.getLoanFeesDetailId(), AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                            systemAccountId, new Date(), "借款合同【" + loanPublish.getLoanTitle() + "】 放款收费", false);
                    avcq.addAccountValueChanged(avcSystemIncomeFees);
                    resultBalance = resultBalance.subtract(systemFeesBalance2);
                    UserInfoExt loanUserInfo = getSystemUserInfo(cashAccountId);//借款人账户
                    insertSubTaskByLoan(loanUserInfo.getUserId(), accountByType.getUserId(), systemFeesBalance,
                            schedule.getScheduleId(), HFOperationEnum.FROZEN_TO_FROZEN_PERSON_TO_COMPANY.getValue(), loanApplicationId);
                }
            }
        }


        //todoed 修改投标订单的状态为还款中
        for (LendOrderBidDetail orderBidDetail : lendOrderBidDetails) {
            LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
//            if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            Map<String, Object> lendOrderMap = new HashMap<String, Object>();
            lendOrderMap.put("lendOrderId", orderBidDetail.getLendOrderId());
            lendOrderMap.put("orderState", LendOrderConstants.LoanOrderStatusEnum.REPAYMENTING.getValue());
            lendOrderMap.put("agreementStartDate", now);
            lendOrderMap.put("agreementEndDate", lastDate);
            lendOrderService.update(lendOrderMap);

            //奖励财富券、现金
            towardsVoucher(orderBidDetail, avcq);
//            }
        }

        //todoed 修改借款申请的放款金额
        loanAppMap.put("firstRepaymentDate", firstRepaymentDate);
        loanAppMap.put("lastRepaymentDate", lastDate);
        loanAppMap.put("lendState", LoanAppLendAuditStatusEnums.PASS.getValue());
        loanAppMap.put("interestBalance", allInterest);
        loanAppMap.put("resultBalance", resultBalance);
        loanAppMap.put("lastMdfTime", now);
        loanAppMap.put("applicationState", LoanApplicationStateEnum.REPAYMENTING.getValue());
        loanAppMap.put("loanApplicationId", loanApplicationId);
        loanAppMap.put("paymentDate", now);
        this.update(loanAppMap);
        //todoed 生成提现申请单
        ParaContext paraContext = new ParaContext();
        paraContext.put("loanApplicationId", loanApplicationId);
        paraContext.put("accountId", cashAccountId);
        paraContext.put("adminId", adminId);
        //提现单用户的选择规则是,如果借款申请为债权标,就选择债券持有人作为提现用户,否则选择借款申请用户作为提现用户
        long wdUserId = loanApplication.getUserId();
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.CREDITOR.getValue())) {
            wdUserId = creditorLenderUserId;
        }
        paraContext.put("userId", wdUserId);
        paraContext.put("customerCard", customerCard);
        paraContext.put("balance", resultBalance);
        paraContext.put("businessType", AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASHFREEZEN.getValue());
        paraContext.put("display", VisiableEnum.DISPLAY.getValue());
        paraContext.put("ownerType", AccountConstants.AccountChangedTypeEnum.LOAN.getValue());
        paraContext.put("ownerId", loanApplicationId);
        paraContext.put("desc", "借款合同 【" + loanPublish.getLoanTitle() + "】 放款提现");
        paraContext.put("needCheck", true);
        paraContext.put("accountValueChangedQueue", avcq);

        //生成提现单
        WithDraw withDraw = commitCash2Card(paraContext);

        //保存借款申请与提现单关系表
        LoanWithdrawRelations loanWithdrawRelations = new LoanWithdrawRelations();
        loanWithdrawRelations.setLoanApplicationId(loanApplicationId);
        loanWithdrawRelations.setWithdrawId(withDraw.getWithdrawId());
        myBatisDao.insert("LOAN_WITHDRAW_RELATIONS.insert", loanWithdrawRelations);

        userAccountOperateService.execute(avcq);
        //生成电子合同
        createAgreement(loanApplicationId);
        //发送计息短信
        sendMakeLoanMessage(loanPublish, lendOrderBidDetails);
        schedule.setStatus(Integer.valueOf(ScheduleEnum.BUSINESS_WAITING.getValue()));
        scheduleService.updateSchedule(schedule);
        return toAgreementList;
    }

    /**
     * 给邀请人发放财富券奖励
     *
     * @param orderBidDetail
     */
    @Transactional
    private void towardsVoucher(LendOrderBidDetail orderBidDetail, AccountValueChangedQueue avcq) {
        LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            PayOrder payOrder = payService.getPaidPayOrderByLendOrderId(lendOrder.getLendOrderId());
            UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
            UserInfoExt uie = userInfoExtService.getUserInfoExtById(user.getUserId());
            //发放财富券
            voucherService.publishVoucher(payOrder);
            //10.1活动暂停发放奖励
            //-----start--------
            Date now = new Date();
            if (now.after(DateUtil.parseStrToDate("2017-11-01", "yyyy-MM-dd")) && now.before(DateUtil.parseStrToDate("2017-12-01", "yyyy-MM-dd"))) {
                return;
            }
            //-----end--------
            //是否是首次投标
            if (!lendOrderService.isFirstLend(user.getUserId(), lendOrder)) {
                return;
            }
        /*    //首次投标奖励 发放5元现金奖励
            UserAccount _userAccount = userAccountService.getCashAccount(user.getUserId());
            String _descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.FIRST_BID_INCOME, towardsIncome);
            AccountValueChanged _avcIncome = new AccountValueChanged(_userAccount.getAccId(), towardsIncome,
                    towardsIncome, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), VisiableEnum.DISPLAY.getValue(), lendOrder.getLendOrderId(),
                    AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                    lendOrder.getLendOrderId(), new Date(), _descIncome, true);
            avcq.addAccountValueChanged(_avcIncome);
            long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
            String _descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.FIRST_BID_PAY, towardsIncome);
            AccountValueChanged _avcPay = new AccountValueChanged(systemAccountId, towardsIncome,
                    towardsIncome, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), VisiableEnum.DISPLAY.getValue(), lendOrder.getLendOrderId(),
                    AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                    systemAccountId, new Date(), _descPay, false);
            avcq.addAccountValueChanged(_avcPay);

            //是否存在邀请人
            if (uie.getRecUserId() == null)
                return;
            //是首次投标的话发送给邀请人10元财富券
            voucherService.handOut(voucherTen, uie.getRecUserId(), VoucherConstants.SourceType.OTHER.getValue(), "邀请奖励");
            //给邀请人发放5元现金奖励
            UserAccount userAccount = userAccountService.getCashAccount(uie.getRecUserId());
            String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.INVITATION_INCOME, towardsIncome);
            AccountValueChanged avcPay = new AccountValueChanged(userAccount.getAccId(), towardsIncome,
                    towardsIncome, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), VisiableEnum.DISPLAY.getValue(), lendOrder.getLendOrderId(),
                    AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                    lendOrder.getLendOrderId(), new Date(), descPay, true);
            avcq.addAccountValueChanged(avcPay);
            String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.INVITATION_PAY, towardsIncome);
            AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, towardsIncome,
                    towardsIncome, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), VisiableEnum.DISPLAY.getValue(), lendOrder.getLendOrderId(),
                    AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                    systemAccountId, new Date(), descIncome, false);
            avcq.addAccountValueChanged(avcIncome);*/

        }
    }

    private void sendMakeLoanMessage(LoanPublish loanPublish, List<LendOrderBidDetail> lendOrderBidDetails) {
        LendOrder pOrder = null;
        for (LendOrderBidDetail orderBidDetail : lendOrderBidDetails) {
            if (orderBidDetail.getStatus() == LendOrderBidStatusEnum.BIDING.value2Char()) {
                LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
                if (lendOrder.getLendOrderPId() != null) {
                    pOrder = lendOrderService.findById(lendOrder.getLendOrderPId());
                }
                if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                        || (pOrder != null && pOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))) {
                    continue;
                }
                UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
                VelocityContext context = new VelocityContext();
                context.put("date", DateUtil.getDateLongMD(orderBidDetail.getBuyDate()));
                context.put("name", loanPublish.getLoanTitle());
                String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_MAKELOAN_VM, context);
                smsService.sendMsg(user.getMobileNo(), content);
            }
        }
    }

    /**
     * 财富券变现
     *
     * @param lendOrder
     * @param queue
     */
    @Transactional
    public BigDecimal changeVoucherToAmount(LendOrder lendOrder, AccountValueChangedQueue queue) {
        BigDecimal voucherValue = BigDecimal.ZERO;
        List<PayOrderDetail> paymentOrderDetailList = payService.getPaymentOrderDetail(lendOrder.getLendOrderId());
        for (PayOrderDetail payOrderDetail : paymentOrderDetailList) {
            if (PayConstants.AmountType.VOUCHERS.getValue().equals(payOrderDetail.getAmountType())) {
                UserAccount cashAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
                List<Voucher> voucherList = null;
                if (payOrderDetail != null) {
                    voucherList = voucherService.getVoucherList(payOrderDetail.getDetailId(), VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE, VoucherConstants.UsageScenario.LOAN);
                    if (voucherList == null || voucherList.size() == 0)
                        //有财富券类支付明细，一定有对应的财富券，否则数据有问题
                        //"财富券充值失败了"
                        throw new SystemException(VoucherErrorCode.VOUCHER_USED).set("detailId", payOrderDetail.getDetailId());
                    //检查所有财富券是否可用
                    boolean checked = true;
                    long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
                    for (Voucher voucher : voucherList) {
                        if (VoucherConstants.VoucherStatus.FREEZE.getValue().equals(voucher.getStatus())) {
                            voucherValue = voucherValue.add(voucher.getVoucherValue());
                            //平台支出一笔与财富券等值
                            AccountValueChanged changed_1 = new AccountValueChanged(systemAccountId, voucher.getVoucherValue(), voucher.getVoucherValue(), AccountConstants.AccountOperateEnum.PAY.getValue(),
                                    AccountConstants.BusinessTypeEnum.VOUCHER_PLATFORM_PAY.getValue(),
                                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(), payOrderDetail.getPayId(), AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                                    systemAccountId, new Date(),
                                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.VOUCHER_PLATFORM_PAY, voucher.getVoucherValue()), false);
                            //用户账户充值一笔与财富券等值
                            AccountValueChanged changed_2 = new AccountValueChanged(cashAccount.getAccId(), voucher.getVoucherValue(), voucher.getVoucherValue(), AccountConstants.AccountOperateEnum.INCOM.getValue(),
                                    AccountConstants.BusinessTypeEnum.VOUCHER_INCOME.getValue(),
                                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(), payOrderDetail.getPayId(),
                                    AccountConstants.OwnerTypeEnum.USER.getValue(), lendOrder.getLendUserId(), new Date(),
                                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.VOUCHER_INCOME, voucher.getVoucherValue()), true);
                            queue.addAccountValueChanged(changed_1);
                            queue.addAccountValueChanged(changed_2);
                        } else {
                            checked = false;
                            break;
                        }
                    }
                    //如果财富券存在不可用的状态则失败
                    if (!checked) {
//                        todo
                        throw new SystemException(PayErrorCode.VOUCHER_PAY_ORDER);
                    }
                    //记录财富券已使用
                    voucherService.useVoucher(voucherList);
                }
            }
        }
        return voucherValue;
    }

    @Override
    @Transactional
    public void newLendBidForFinanceLendOrder(LendOrder lendOrder, MatchCreditorVO matchCreditorVO) throws Exception {
        Date now = new Date();
        if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
            return;
        }

        System.out.println("匹配的标的id为：" + matchCreditorVO.getTheId());
        ParaContext paraContext = new ParaContext();
        paraContext.put("loanApplicationId", matchCreditorVO.getTheId());

        LendProductServiceImpl bean = ApplicationContextUtil.getBean(LendProductServiceImpl.class);
        LendProductPublish lendProductPublish = getLendProductPublishByLoanApplicationId(matchCreditorVO.getTheId());
        //新建投标订单
        LendProductPublish productPublish = bean.getLendProductPublishByPublishId(lendProductPublish.getLendProductPublishId());
        LendProduct product = bean.findById(productPublish.getLendProductId());
        //生成支付订单
        PayOrder payOrder = null;
        //如果是出借产品，就建立出借明细数据
        payOrder = payService.createBidPayOrderForFinanceLendOrder(lendOrder, matchCreditorVO.getTheId(), lendProductPublish.getLendProductPublishId(), matchCreditorVO.getBalance(), now, product, PropertiesUtils.getInstance().get("SOURCE_PC"));
        //执行订单支付
        PayResult payResult = payService.doPay(payOrder.getPayId(), now);

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


    @Override
    public void reMakeLoan(Long loanApplicationId, Long adminId) throws Exception {
        //判断参数是否为null
        if (loanApplicationId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("loanApplicationId", "null");
        LoanApplication loanApplication = getLoanApplicationById(loanApplicationId);
        //借款申请打款状态不为放款通过
        if (!loanApplication.getLendState().equals(LoanAppLendAuditStatusEnums.PASS.getValue())) {
            throw new SystemException(WithDrawErrorCode.LOANAPPLICATION_STATUS_NOT_SUPPORT);
        }
        //获取当前借款申请的所有提现单，如果所有提现单都为提现失败则进行重新放款操作
        List<LoanWithdrawRelations> lwrs = myBatisDao.getList("LOAN_WITHDRAW_RELATIONS.selectByLoanApplicationId", loanApplicationId);
        for (LoanWithdrawRelations lwr : lwrs) {
            WithDraw withDraw = withDrawService.getWithDrawByWithDrawId(lwr.getWithdrawId());
            if (!withDraw.getTransStatus().equals(WithDrawTransferStatus.TRANSFER_FAILE.getValue())) {
                //有一个不为提现失败，则不处理
                throw new SystemException(WithDrawErrorCode.WITHDRAW_STATUS_NOT_END);
            }
        }

        //重新放款提现
        CustomerCard customerCard = customerCardService.findById(loanApplication.getInCardId());
        LoanPublish loanPublish = loanPublishService.findById(loanApplicationId);
        AccountValueChangedQueue accountValueChangedQueue = new AccountValueChangedQueue();

        Long userId = loanApplication.getUserId();
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.CREDITOR.getValue())) {
            //债权标
            BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(loanApplication.getOriginalUserId());
            userId = bondSourceUser.getUserId();
        }

        UserAccount userAccount = userAccountService.getCashAccount(userId);
        ParaContext paraContext = new ParaContext();
        paraContext.put("loanApplicationId", loanApplicationId);
        paraContext.put("accountId", userAccount.getAccId());
        paraContext.put("adminId", adminId);
        //提现单用户的选择规则是,如果借款申请为债权标,就选择债券持有人作为提现用户,否则选择借款申请用户作为提现用户
        paraContext.put("userId", userId);
        paraContext.put("customerCard", customerCard);
        paraContext.put("balance", loanApplication.getResultBalance());
        paraContext.put("businessType", AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASHFREEZEN.getValue());
        paraContext.put("display", VisiableEnum.DISPLAY.getValue());
        paraContext.put("ownerType", AccountConstants.AccountChangedTypeEnum.LOAN.getValue());
        paraContext.put("ownerId", loanApplicationId);
        paraContext.put("desc", "借款合同 【" + loanPublish.getLoanTitle() + "】 放款提现");
        paraContext.put("needCheck", true);
        paraContext.put("accountValueChangedQueue", accountValueChangedQueue);

        //生成提现单
        WithDraw withDraw = commitCash2Card(paraContext);


        //保存借款申请与提现单关系表
        LoanWithdrawRelations loanWithdrawRelations = new LoanWithdrawRelations();
        loanWithdrawRelations.setLoanApplicationId(loanApplicationId);
        loanWithdrawRelations.setWithdrawId(withDraw.getWithdrawId());
        myBatisDao.insert("LOAN_WITHDRAW_RELATIONS.insert", loanWithdrawRelations);

        //向渠道的平台账户执行冻结操作
        userAccountOperateService.execute(accountValueChangedQueue);


    }


    @Override
    @Transactional
    public void createAllAgreement(long loanApplicationId) throws SystemException {
        try {
            // 借款合同
            createAgreement(loanApplicationId);

            // 债权转让合同
            List<CreditorRights> turnCreList = new ArrayList<CreditorRights>();
            List<CreditorRights> creditorRightses = creditorRightsService.getByLoanApplicationId(loanApplicationId);
            for (CreditorRights cre : creditorRightses) {
                if (cre.getFromWhere() == CreditorRightsFromWhereEnum.TURN.value2Char()) {
                    turnCreList.add(cre);
                }
            }
            createAgreementTurnRights(loanApplicationId, turnCreList);

        } catch (Exception e) {
            e.printStackTrace();
            throw SystemException.wrap(e, BidErrorCode.GENERATE_AGREEMENT_FAILED).set("loanApplicationId", loanApplicationId);
        }
    }


    @Override
    public void createAgreement() {
        List<LoanApplication> loanApplications = this.findByStates(LoanApplicationStateEnum.REPAYMENTING, LoanApplicationStateEnum.COMPLETED, LoanApplicationStateEnum.EARLYCOMPLETE);
        for (LoanApplication loanApplication : loanApplications) {
            createAgreement(loanApplication.getLoanApplicationId());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void createAgreement(long loanApplicationId) throws SystemException {
        List<CreditorRights> creditorRightses = creditorRightsService.getByLoanApplicationId(loanApplicationId);
        LoanApplication loanApplication = this.findById(loanApplicationId);
        String subjectType = loanApplication.getSubjectType();
        for (CreditorRights creditorRights : creditorRightses) {
            // 债权转让的合同单独生成
            if (creditorRights.getFromWhere() == CreditorRightsFromWhereEnum.TURN.value2Char()) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("creditorRightsId", creditorRights.getCreditorRightsId());
            map.put("lendUserId", creditorRights.getLendUserId());
            // //使以前的相关协议失效
            // cancelEarlyAgreement(creditorRights.getCreditorRightsId());

            // 新建未生成协议文件的协议元数据
            try {
                Map<String, Object> rsMap = createUnGenFilesAgreementInfo(
                        new Date(), subjectType,
                        creditorRights, loanApplication.getLoanType());
                List<AgreementInfo> agreementList = (List<AgreementInfo>) rsMap.get("agreements");
                String agreementPdfsPath = null;
                if (rsMap.get("version") != null && (Integer) rsMap.get("version") > 1) {
                    agreementPdfsPath = PropertiesUtils.getInstance().get(
                            "AGREEMENT_PATH")
                            + creditorRights.getCreditorRightsCode() + "-" + rsMap.get("version");
                } else {
                    agreementPdfsPath = PropertiesUtils.getInstance().get(
                            "AGREEMENT_PATH")
                            + creditorRights.getCreditorRightsCode();
                }
                FileUtil.mkDirs(agreementPdfsPath);
                String[] de = DateUtil.getNowFormateDate().split("-");
                for (AgreementInfo agreementInfo : agreementList) {
                    if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplication.getLoanType()) || LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplication.getLoanType())) {
                        StringBuffer sb = new StringBuffer("HJRD-PC-TJK-");
                        if (LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplication.getLoanType())) {
                            sb = new StringBuffer("HJRD-PL-TJK-");
                        }
                        sb.append(de[0].substring(2) + de[1] + "-");
                        Integer vs = agreementInfo.getVersion() != null && agreementInfo.getVersion() > 1 ? agreementInfo.getVersion() : 1;
                        if (vs > 9999) {
                            sb.append("0" + vs);
                        } else {
                            if (vs > 999) {
                                sb.append("01" + vs);
                            } else if (vs > 99) {
                                sb.append("010" + vs);
                            } else if (vs > 9) {
                                sb.append("0100" + vs);
                            } else {
                                sb.append("01000" + vs);
                            }
                        }
                        map.put("agreementCode", sb.toString());
                    } else {
                        map.put("agreementCode", agreementInfo.getAgreementCode() != null ? agreementInfo.getAgreementCode() : agreementInfo.getAgreementName()
                                + (agreementInfo.getVersion() != null && agreementInfo.getVersion() > 1 ? ("-" + agreementInfo.getVersion()) : ""));
                    }

                    if (subjectType.equals(SubjectTypeEnum.CREDITOR.getValue())) {
                        // 债权标-生成债权转让协议
                        // createBidAgreementForLoanTrans(loanApplication.getLoanProductId(),
                        // agreementPdfsPath, map);
                        if (agreementInfo.getAgreementType().equals(
                                AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT
                                        .getValue())) {

                            // 如果是企业标类型的债权标，生成企业标原来的借款协议
                            if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanApplication.getLoanType())) {
                                createBidAgreementForEnterpriseLoanApply(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                            } else {// 生成通用的个人的债权标借款协议
                                // 债权标借款
                                if (LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(
                                        loanApplication.getLoanType())) {
                                    createBidAgreementForCashLoan(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                                } else {
                                    createBidAgreementForLoan(
                                            loanApplication.getLoanProductId(),
                                            agreementPdfsPath, map);
                                }

                            }
                        } else if (agreementInfo.getAgreementType().equals(
                                AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT
                                        .getValue())) {
                            // 债权标转让
                            if (!LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplication.getLoanType())) {
                                createBidAgreementForLoanAssignment(
                                        loanApplication.getLoanProductId(),
                                        agreementPdfsPath, map);
                            }

                        }

                    }

                    if (subjectType.equals(SubjectTypeEnum.LOAN.getValue())) {
                        // 借款标-生成借款协议
                        if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue()
                                .equals(loanApplication.getLoanType())
                                || LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT
                                .getValue().equals(
                                        loanApplication.getLoanType())
                                || LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING
                                .getValue().equals(
                                        loanApplication.getLoanType())
                                || LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE
                                .getValue().equals(
                                        loanApplication.getLoanType())) {
                            createBidAgreementForEnterpriseLoanApply(
                                    loanApplication.getLoanProductId(),
                                    agreementPdfsPath, map);
                        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION
                                .getValue().equals(
                                        loanApplication.getLoanType())) {
                            createBidAgreementForFund(
                                    creditorRights.getLendOrderId(),
                                    agreementPdfsPath, map);
                        } else if (LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplication.getLoanType())) {
                            //借款标，个人房产直投(借款及服务协议)
                            createBidAgreementForLoanDirectHouse(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                            //借款标，个人房产直投(授权委托书)
                            createBidAgreementForLoanDirectHouseEntrust(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                        } else if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(
                                loanApplication.getLoanType())) {
                            createBidAgreementForPersonCar(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                        } else if (LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(
                                loanApplication.getLoanType())) {
                            createBidAgreementForCashLoan(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                        } else {
                            createBidAgreementForLoanApply(
                                    loanApplication.getLoanProductId(),
                                    agreementPdfsPath, map);
                        }
                    }

                    // 修改合同信息(状态为已生成)
                    AgreementInfo theAgreementInfo = new AgreementInfo();
                    theAgreementInfo
                            .setAgreementStatus(AgreementEnum.AgreementStatusEnum.CREATED
                                    .getValue());
                    theAgreementInfo.setAgreementId(agreementInfo
                            .getAgreementId());
                    agreementInfoService.update(theAgreementInfo);
                }

                if (rsMap.get("version") != null && (Integer) rsMap.get("version") > 1) {
                    FileUtil.zipFiles(
                            new File(agreementPdfsPath).listFiles(),
                            PropertiesUtils.getInstance().get("AGREEMENT_PATH"),
                            creditorRights.getCreditorRightsCode() + "-" + rsMap.get("version"));
                } else {
                    FileUtil.zipFiles(
                            new File(agreementPdfsPath).listFiles(),
                            PropertiesUtils.getInstance().get("AGREEMENT_PATH"),
                            creditorRights.getCreditorRightsCode());
                }
                FileUtil.deleteDirectory(agreementPdfsPath);
            } catch (Exception e) {
                e.printStackTrace();
                throw SystemException
                        .wrap(e, BidErrorCode.GENERATE_AGREEMENT_FAILED)
                        .set("loanApplicationId", loanApplicationId)
                        .set("creditorRightsId",
                                creditorRights.getCreditorRightsId());
//						.set("agreementType", agreementInfo.getAgreementType());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void createAgreementTurnRights(long loanApplicationId, List<CreditorRights> newCreditorRights) throws SystemException {
        // List<CreditorRights> creditorRightses = creditorRightsService.getAvailidRightsByLoanApplicationId(loanApplicationId);
        LoanApplication loanApplication = this.findById(loanApplicationId);
        String subjectType = loanApplication.getSubjectType();
        for (CreditorRights creditorRights : newCreditorRights) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("creditorRightsId", creditorRights.getCreditorRightsId());
            map.put("lendUserId", creditorRights.getLendUserId());

            // 新建未生成协议文件的协议元数据
            try {
                Map<String, Object> rsMap = createUnGenFilesAgreementInfo(loanApplication.getFullTime(), subjectType, creditorRights, loanApplication.getLoanType());
                List<AgreementInfo> agreementList = (List<AgreementInfo>) rsMap.get("agreements");
                String agreementPdfsPath = null;
                if (rsMap.get("version") != null && (Integer) rsMap.get("version") > 1) {
                    agreementPdfsPath = PropertiesUtils.getInstance().get("AGREEMENT_PATH") + creditorRights.getCreditorRightsCode() + "-" + rsMap.get("version");
                } else {
                    agreementPdfsPath = PropertiesUtils.getInstance().get("AGREEMENT_PATH") + creditorRights.getCreditorRightsCode();
                }
                FileUtil.mkDirs(agreementPdfsPath);
                for (AgreementInfo agreementInfo : agreementList) {
                    map.put("agreementCode", agreementInfo.getAgreementCode() + (agreementInfo.getVersion() != null && agreementInfo.getVersion() > 1 ? ("-" + agreementInfo.getVersion()) : ""));
                    if (subjectType.equals(SubjectTypeEnum.CREDITOR.getValue())) {
                        // 债权标-生成债权转让协议
                        // createBidAgreementForLoanTrans(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                        if (agreementInfo.getAgreementType().equals(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue())) {
                            // 债权标借款
                            createBidAgreementForLoan(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                        } else if (agreementInfo.getAgreementType().equals(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue())) {
                            // 债权标转让
                            createBidAgreementForLoanAssignment(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                        }

                    } else if (subjectType.equals(SubjectTypeEnum.LOAN.getValue())) {
                        // 借款标-生成借款协议
                        if (agreementInfo.getAgreementType().equals(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue())) {
                            // 借款标借款
                            if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanApplication.getLoanType())
                                    || LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanApplication.getLoanType())
                                    || LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanApplication.getLoanType())) {
                                createBidAgreementForEnterpriseLoanApply(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanApplication.getLoanType())) {
                                createBidAgreementForFund(creditorRights.getLendOrderId(), agreementPdfsPath, map);
                            } else {
                                createBidAgreementForLoanApply(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                            }
                        } else if (agreementInfo.getAgreementType().equals(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue())) {
                            // 借款标转让
                            createBidAgreementForLoanAssignment(loanApplication.getLoanProductId(), agreementPdfsPath, map);
                        }
                    }

                    // 修改合同信息(状态为已生成)
                    AgreementInfo theAgreementInfo = new AgreementInfo();
                    theAgreementInfo.setAgreementStatus(AgreementEnum.AgreementStatusEnum.CREATED.getValue());
                    theAgreementInfo.setAgreementId(agreementInfo.getAgreementId());
                    agreementInfoService.update(theAgreementInfo);
                }

                if (rsMap.get("version") != null && (Integer) rsMap.get("version") > 1) {
                    FileUtil.zipFiles(new File(agreementPdfsPath).listFiles(), PropertiesUtils.getInstance().get("AGREEMENT_PATH"), creditorRights.getCreditorRightsCode() + "-" + rsMap.get("version"));
                } else {
                    FileUtil.zipFiles(new File(agreementPdfsPath).listFiles(), PropertiesUtils.getInstance().get("AGREEMENT_PATH"), creditorRights.getCreditorRightsCode());
                }
                FileUtil.deleteDirectory(agreementPdfsPath);
            } catch (Exception e) {
                e.printStackTrace();
                throw SystemException.wrap(e, BidErrorCode.GENERATE_AGREEMENT_FAILED).set("loanApplicationId", loanApplicationId)
                        .set("creditorRightsId", creditorRights.getCreditorRightsId());
                // .set("agreementType", agreementInfo.getAgreementType());
            }
        }
    }

    /**
     * 生成债权标合同
     */
    public void createBidAgreementForLoanTrans(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor";
        String pdf_name = "借款及服务协议(债权)";

        Map<String, Object> jsonMap_assignment = new HashMap<String, Object>();
        String htmlUrl_assignment = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor_assignment";
        String pdf_assignment_name = "债权转让及受让协议";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());

        // 借款发标表信息
        LoanPublish loanPublish = loanPublishService.findById(loan.getLoanApplicationId());
        if (null != loanPublish.getCompanyId()) {// 如果有担保公司
            htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor_guarantee";
            pdf_name = "借款及服务协议(债权)(含保证人)";
            htmlUrl_assignment = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor_assignment_guarantee";
            pdf_assignment_name = "债权转让及受让协议(含保证人)";
            // 担保公司信息
            GuaranteeCompany guaranteeCompany = guaranteeCompanyService.getGuaranteeCompanyByCompanyId(loanPublish.getCompanyId());
            jsonMap.put("guaranteeCompany", guaranteeCompany);
            jsonMap_assignment.put("guaranteeCompany", guaranteeCompany);
        }

        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人、身份证号
        jsonMap.put("loanRealName", loanUserInfoVO.getRealName());
        jsonMap.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款金额（大写+小写）
        if (null != creditorRights.getBuyPrice()) {
            jsonMap.put("resultBalanceBig", BigDecimalUtil.change(new Double(creditorRights.getBuyPrice().toString())));
        }
        jsonMap.put("resultBalance", creditorRights.getBuyPrice());

        // 借款期限（几个月、开始时间、结束时间）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }
        jsonMap.put("dueTime", loanProduct.getDueTime());

        // 还款方式
        jsonMap.put("repaymentType", loanProduct.getRepaymentType());

        // 借款利率
        jsonMap.put("annualRate", loan.getAnnualRate());

        // 借款用途
        if (null != loan.getLoanUseage() && !"".equals(loan.getLoanUseage())) {
            ConstantDefine constantDefine = new ConstantDefine();
            constantDefine.setConstantValue(loan.getLoanUseage());
            constantDefine.setConstantTypeCode("loanUseage");
            constantDefine.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(constantDefine);
            if (null != define) {
                jsonMap.put("loanUseage", define.getConstantName());
            }
        }

        // 借款人    通信地址：
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 邮箱地址：
        jsonMap.put("loanEmail", loanUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("loanMobileNo", loanUserInfoVO.getMobileNo());

        CustomerCard loanCustomerCard = customerCardService.getCustomerBindCardByUserId(loanUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != loanCustomerCard) {
            // 银行账号：
            jsonMap.put("loanCardCode", loanCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(loanCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("loanBankName", define.getConstantName());
            }
        }

        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        CustomerCard lendCustomerCard = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != lendCustomerCard) {
            // 银行账号：
            jsonMap.put("lendCardCode", lendCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("lendBankName", define.getConstantName());
            }
        }

        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap.put("repaymentPlanList", repaymentPlanList);

        // 每月还款日、每月还款金额(还款明细表 第一个月的)
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            RightsRepaymentDetail plan = repaymentPlanList.get(0);
            jsonMap.put("shouldBalance2", BigDecimalUtil.change(new Double(plan.getShouldBalance2().toString())));
            jsonMap.put("repaymentDay", DateUtil.getDay(plan.getRepaymentDayPlanned()));
        }

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------

        // 合同编号
        jsonMap_assignment.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap_assignment.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 转让人（原债权人）：
        BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(loan.getOriginalUserId());
        UserInfoVO sourceUserInfoVO = userInfoService.getUserExtByUserId(bondSourceUser.getUserId());
        // 联系电话：
        jsonMap_assignment.put("sourceMobileNo", sourceUserInfoVO.getMobileNo());
        // 邮箱地址：
        jsonMap_assignment.put("sourceEmail", sourceUserInfoVO.getEmail());
        // 姓名
        jsonMap_assignment.put("sourceRealName", sourceUserInfoVO.getRealName());

        // 受让人（新债权人=出借人）：
        UserInfoVO lendUserInfoVO_ass = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 联系电话：
        jsonMap_assignment.put("lendMobileNo", lendUserInfoVO_ass.getMobileNo());
        // 邮箱地址：
        jsonMap_assignment.put("lendEmail", lendUserInfoVO_ass.getEmail());
        // 姓名
        jsonMap_assignment.put("lendRealName", lendUserInfoVO_ass.getRealName());

        // 借款ID
        jsonMap_assignment.put("loanApplicationCode", loan.getLoanApplicationCode());
        // 借款人姓名
        jsonMap_assignment.put("loanRealName", loanUserInfoVO.getRealName());
        // 借款人身份证号
        jsonMap_assignment.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款本金数额
        jsonMap_assignment.put("resultBalance", loan.getLoanBalance());

        // 原借款期限
        jsonMap_assignment.put("dueTime", loanProduct.getDueTime());


        // 受让债权明细
        LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());

        // 受让本金-投资额
        jsonMap_assignment.put("buyBalance", lendOrder.getBuyBalance());
        if (null != lendOrder.getBuyBalance()) {
            jsonMap_assignment.put("buyBalanceBig", BigDecimalUtil.change(new Double(lendOrder.getBuyBalance().toString())));
        }

        // 借款利息
        jsonMap_assignment.put("annualRate", loan.getAnnualRate());

        // 受让日期-投标日
        if (null != creditorRights.getLendTime()) {
            jsonMap_assignment.put("lendTime", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getLendTime()));
        }

        // 起息日
        if (null != loan.getPaymentDate()) {
            jsonMap_assignment.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }

        // 到期日期
        if (null != loan.getLastRepaymentDate()) {
            jsonMap_assignment.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }

        // 还款方式
        jsonMap_assignment.put("repaymentType", loanProduct.getRepaymentTypeStr(loanProduct.getRepaymentType()));

        // 还款期数
        jsonMap_assignment.put("dueTime", loanProduct.getDueTime());

        // 还款明细表
        jsonMap_assignment.put("repaymentPlanList", repaymentPlanList);

        // 受让人缴纳服务费
        jsonMap_assignment.put("feesItems", lendLoanBindings);

        // 第五条 受让人收益账户:帐户名、账号、开户名
        CustomerCard lendCustomerCard_ass = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != lendCustomerCard_ass) {
            // 银行账号：
            jsonMap_assignment.put("lendCardCode", lendCustomerCard_ass.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard_ass.getBankCode());
            if (null != define) {
                jsonMap_assignment.put("lendBankName", define.getConstantName());
            }
        }

        Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));
        Pair pair_assignment = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap_assignment));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, pdf_name, pair);
        GeneratePDF.create(htmlUrl_assignment, storageFolder, pdf_assignment_name, pair_assignment);
    }


    /**
     * 生成借款标合同
     */
    private void createBidAgreementForLoanApply(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws Exception, IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_loan";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());
        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人、身份证号
        jsonMap.put("loanRealName", loanUserInfoVO.getRealName());
        jsonMap.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款金额（大写+小写）
        if (null != creditorRights.getBuyPrice()) {
            jsonMap.put("resultBalanceBig", BigDecimalUtil.change(new Double(creditorRights.getBuyPrice().toString())));
        }
        jsonMap.put("resultBalance", creditorRights.getBuyPrice());

        // 借款期限（几个月、开始时间、结束时间=(借款产品表)DUE_TIME）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }

        // 借款期限
        jsonMap.put("dueTime", loanProduct.getDueTime());

        // 还款方式
        jsonMap.put("repaymentType", loanProduct.getRepaymentType());

        // 借款利率
        jsonMap.put("annualRate", loan.getAnnualRate());

        // 借款用途
        if (null != loan.getLoanUseage() && !"".equals(loan.getLoanUseage())) {
            ConstantDefine constantDefine = new ConstantDefine();
            constantDefine.setConstantValue(loan.getLoanUseage());
            constantDefine.setConstantTypeCode("loanUseage");
            constantDefine.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(constantDefine);
            if (null != define) {
                jsonMap.put("loanUseage", define.getConstantName());
            }
        }

        // 借款人    通信地址：
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 邮箱地址：
        jsonMap.put("loanEmail", loanUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("loanMobileNo", loanUserInfoVO.getMobileNo());

        CustomerCard loanCustomerCard = customerCardService.getCustomerBindCardByUserId(loanUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != loanCustomerCard) {
            // 银行账号：
            jsonMap.put("loanCardCode", loanCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(loanCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("loanBankName", define.getConstantName());
            }
        }

        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        CustomerCard lendCustomerCard = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != lendCustomerCard) {
            // 银行账号：
            jsonMap.put("lendCardCode", lendCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("lendBankName", define.getConstantName());
            }
        }

        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap.put("repaymentPlanList", repaymentPlanList);

        // 每月还款日、每月还款金额(还款明细表 第一个月的)
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            RightsRepaymentDetail plan = repaymentPlanList.get(0);
            jsonMap.put("shouldBalance2", BigDecimalUtil.change(new Double(plan.getShouldBalance2().toString())));
            jsonMap.put("repaymentDay", DateUtil.getDay(plan.getRepaymentDayPlanned()));
        }

        // 【注：先去掉这个!!!!】附件三：编号、总服务费、支付方式、借款金额
        // TODO...

        Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, "借款及服务协议(借款)", pair);

    }

    /**
     * 企业借款合同
     */
    private void createBidAgreementForEnterpriseLoanApply(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws Exception, IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/enterprise_service_loan";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());
        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人信息=改为企业信息：公司名、组织机构代码、法人代表、法人身份证、通讯地址
        EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loan.getLoanApplicationId());
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
        jsonMap.put("enterpriseName", enterpriseInfo.getEnterpriseName());
        jsonMap.put("organizationCode", enterpriseInfo.getOrganizationCode());
        jsonMap.put("legalPersonName", enterpriseInfo.getLegalPersonName());
        jsonMap.put("legalPersonCode", enterpriseInfo.getLegalPersonCode());

        // 借款人    通信地址：（这里暂时获取法人的）
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 借款金额（大写+小写）
        if (null != creditorRights.getBuyPrice()) {
            jsonMap.put("resultBalanceBig", BigDecimalUtil.change(new Double(creditorRights.getBuyPrice().toString())));
        }
        jsonMap.put("resultBalance", creditorRights.getBuyPrice());

        // 借款期限（几个月、开始时间、结束时间=(借款产品表)DUE_TIME）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }
        if (creditorRights.getFromWhere() == CreditorRightsFromWhereEnum.BUY.value2Char()) {
            jsonMap.put("dueTime", loanProduct.getDueTime());
        } else {
            jsonMap.put("dueTime", paramsMap.get("dueTime"));
        }

        // 还款方式
        jsonMap.put("repaymentType", loanProduct.getRepaymentType());

        // 借款利率
        jsonMap.put("annualRate", loan.getAnnualRate());

        // 借款用途
        if (null != loan.getLoanUseage() && !"".equals(loan.getLoanUseage())) {
            ConstantDefine constantDefine = new ConstantDefine();
            constantDefine.setConstantValue(loan.getLoanUseage());
            constantDefine.setConstantTypeCode("enterpriseLoanUseage");
            constantDefine.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(constantDefine);
            if (null != define) {
                jsonMap.put("loanUseage", define.getConstantName());
            }
        }

        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        CustomerCard lendCustomerCard = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != lendCustomerCard) {
            // 银行账号：
            jsonMap.put("lendCardCode", lendCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("lendBankName", define.getConstantName());
            }
        }

        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap.put("repaymentPlanList", repaymentPlanList);

        // 每月还款日、每月还款金额(还款明细表 第一个月的)
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            RightsRepaymentDetail plan = repaymentPlanList.get(0);
            jsonMap.put("shouldBalance2", BigDecimalUtil.change(new Double(plan.getShouldBalance2().toString())));
            jsonMap.put("repaymentDay", DateUtil.getDay(plan.getRepaymentDayPlanned()));
        }

        // 【注：先去掉这个!!!!】附件三：编号、总服务费、支付方式、借款金额
        // TODO...

        Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, "借款及服务协议(企业)", pair);

    }

    /**
     * 定向委托投资管理协议
     */
    private void createBidAgreementForFund(Long lendOId, String storageFolder, Map<String, Object> paramsMap) throws Exception, IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/directional_commissioned";

        //订单id
        LendOrder order = lendOrderService.findById(lendOId);
        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());
        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(order.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 出借人、身份证号、财富派用户名
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());
        jsonMap.put("lendUserName", lendUserInfoVO.getLoginName());

        // 借款人信息=改为企业信息：公司名、财富派用户名
        EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loan.getLoanApplicationId());
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
        jsonMap.put("enterpriseName", enterpriseInfo.getEnterpriseName());
        jsonMap.put("loanUserName", loanUserInfoVO.getLoginName());

        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        jsonMap.put("productName", loan.getLoanApplicationName());

        //托标成立日
        jsonMap.put("loanStartTime", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        //托标到期日
        jsonMap.put("loanEndTime", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        //托标收益率
        jsonMap.put("yearRate", loan.getAnnualRate().toString() + "%");
        //总金额,转让份额，转让价款
        jsonMap.put("accountMoney", loan.getConfirmBalance().toString());
        //甲方投资金额
        jsonMap.put("investmentMoney", order.getBuyBalance().toString());
        //托管机构
        EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByloanApplicationId(loan.getLoanApplicationId());
        CoLtd coltd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
        jsonMap.put("companyName", coltd.getCompanyName());
        //乙方管理费
        FeesItem fee = lendOrderService.getFeesByLendProId(order.getLendProductId());
        jsonMap.put("fee", fee != null ? fee.getShowTypeContent() : "无");

        //甲方预期收益率
        jsonMap.put("profitRate", order.getProfitRate().toString() + "%");

        Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, "定向委托投资管理协议", pair);

    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> createUnGenFilesAgreementInfo(Date createTime, String subjectType, CreditorRights creditorRights, String loanType) {
        Map<String, Object> map = new HashMap<String, Object>();

        String storageFolder = PropertiesUtils.getInstance().get("AGREEMENT_PATH");
        String href = PropertiesUtils.getInstance().get("AGREEMENT_VISIT_PATH");

        // 创建合同信息(状态为未生成)
        List<AgreementInfo> agreementList = new ArrayList<AgreementInfo>();
        Map<String, Object> rsMap = setAgreementCodeAndVersion(creditorRights, subjectType, loanType);
        List<AgreementInfo> agreements = (List<AgreementInfo>) rsMap.get("agreements");
        //使以前的相关协议失效
        cancelEarlyAgreement(creditorRights.getCreditorRightsId());
        for (AgreementInfo agreementInfo : agreements) {
            String fileName = null;
            if (rsMap.get("version") != null && (Integer) rsMap.get("version") > 1) {
                fileName = creditorRights.getCreditorRightsCode() + "-" + rsMap.get("version");
            } else {
                fileName = creditorRights.getCreditorRightsCode();
            }
            agreementInfo.setAgreementName(fileName);
            agreementInfo.setCreateTime(createTime);
            agreementInfo.setAgreementStatus(AgreementEnum.AgreementStatusEnum.UNCREATE.getValue());
            agreementInfo.setCreditorRightsId(creditorRights.getCreditorRightsId());
            agreementInfo.setStorgePath(storageFolder + fileName + ".zip");
            agreementInfo.setHref(href + fileName + ".zip");
            agreementInfoService.insert(agreementInfo);
            agreementList.add(agreementInfo);
        }
        map.put("agreements", agreementList);
        map.put("version", rsMap.get("version"));
        return map;
    }

    private Map<String, Object> setAgreementCodeAndVersion(CreditorRights creditorRights, String subjectType, String loanType) {
        Map<String, Object> rsMap = new HashMap<String, Object>();
        List<AgreementInfo> agreements = new ArrayList<AgreementInfo>();
        int version = 0;
        String lendPreCode = null;
        String turnPreCode = null;
        List<AgreementInfo> resList = agreementInfoService.findAgreeListByCreditorRightsId(creditorRights.getCreditorRightsId(), AgreementStatusEnum.CREATED);

        if (null != resList && resList.size() != 0) {
            AgreementInfo res = resList.get(0);
            if (res.getAgreementId() != null && res.getVersion() > 0) {
                version = res.getVersion() + 1;
            } else {
                version = 1;
            }
        } else {
            version = 1;
        }
        for (int i = 0; i < resList.size(); i++) {
            if (resList.get(i).getAgreementType().equals(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue())) {
                lendPreCode = resList.get(i).getAgreementCode();
            } else if (resList.get(i).getAgreementType().equals(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue())) {
                turnPreCode = resList.get(i).getAgreementCode();
            }
        }

        if (subjectType.equals(SubjectTypeEnum.LOAN.getValue())) {
            // 借款标合同只生成一份(除：个人房产直投标)
            AgreementInfo agree = new AgreementInfo();
            if (version > 1) {
                agree.setAgreementCode(lendPreCode);
            } else {
                if (LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanType)) {//如果是房产直投，则生成直投自己的借款协议
                    setLoanNumber(agree, loanType, lendPreCode, AgreementTypeEnum.LEND_AGREEMENT.getValue());
                } else {//通用的借款协议
                    setLoanNumber(agree, loanType, lendPreCode, null);
                }
            }
            agree.setVersion(version);
            agree.setAgreementType(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue());
            agreements.add(agree);

            //个人房产直投（开始）
            if (LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanType)) {
                AgreementInfo agreeDirect = new AgreementInfo();
                if (version > 1) {
                    agreeDirect.setAgreementCode(lendPreCode);
                } else {
                    setLoanNumber(agreeDirect, loanType, lendPreCode, AgreementTypeEnum.ENTRUST_AGREEMENT.getValue());//直投自己的委托协议
                }
                agreeDirect.setVersion(version);
                agreeDirect.setAgreementType(AgreementEnum.AgreementTypeEnum.ENTRUST_AGREEMENT.getValue());//个人房产直投的授权委托书
                agreements.add(agreeDirect);
            }
            //个人房产直投（结束）

            // 债权转让时生成转让合同
            if (creditorRights.getFromWhere() == CreditorRightsFromWhereEnum.TURN.value2Char()) {
                AgreementInfo agreeTurn = new AgreementInfo();
                if (version > 1) {
                    agreeTurn.setAgreementCode(turnPreCode);
                } else {
                    // setCreditNumber(agreeTurn, loanType, preCode);
                    setCreditNumber(agreeTurn, loanType, turnPreCode, AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue());
                }
                agreeTurn.setVersion(version);
                agreeTurn.setAgreementType(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue());
                agreements.add(agreeTurn);
            }

        } else if (subjectType.equals(SubjectTypeEnum.CREDITOR.getValue())) {
            AgreementInfo agreeLoan = new AgreementInfo();
            AgreementInfo agreeCredit = new AgreementInfo();

            if (version > 1) {
                agreeLoan.setAgreementCode(lendPreCode);
                agreeCredit.setAgreementCode(lendPreCode);
            } else {
                setCreditNumber(agreeLoan, loanType, turnPreCode, AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue());
                setCreditNumber(agreeCredit, loanType, turnPreCode, AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue());
            }
            agreeLoan.setAgreementType(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue());
            agreeCredit.setAgreementType(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue());
            agreeLoan.setVersion(version);
            agreeCredit.setVersion(version);
            agreements.add(agreeCredit);
            agreements.add(agreeLoan);

        }

        rsMap.put("agreements", agreements);
        rsMap.put("version", version);
        return rsMap;

    }

    private void setCreditNumber(AgreementInfo agree, String loanType,
                                 String creditorRightsCode, String agreementType) {
        // 债权标标 -- 1 债权转让 0 债权借款
//			if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue()
//					.equals(loanType)) {
//				// 企业车贷
//				type1 =AgreementEnum.AreementCodeTypeEnum.COMPANY_CAR_CREDITOR_ASSIGNMENT.getValue();
//				type=AgreementEnum.AreementCodeTypeEnum.COMPANY_CAR_CREDITOR_LOAN.getValue();
//			} else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue()
//					.equals(loanType)) {
//				// 企业保理
//				type1 = AgreementEnum.AreementCodeTypeEnum.COMPANY_FACTORING_CREDITOR_ASSIGNMENT.getValue();
//				type=AgreementEnum.AreementCodeTypeEnum.COMPANY_FACTORING_CREDITOR_LOAN.getValue();
//			} else
        if (LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanType)) {
            // 个人房贷
            if (agreementType.equals(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_HOUSE_CREDITOR_LOAN.getValue()));
            } else if (agreementType.equals(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_HOUSE_CREDITOR_ASSIGNMENT.getValue()));
            }
        } else if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanType)) {
            // 个人信贷
            if (agreementType.equals(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_CREDIT_CREDITOR_LOAN.getValue()));
            } else if (agreementType.equals(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_CREDIT_CREDITOR_ASSIGNMENT.getValue()));
            }
        } else if (LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanType)) {
            // 个人房产直投
            if (AgreementTypeEnum.LEND_AGREEMENT.getValue().equals(agreementType)) {//借款标，个人房产直投(借款及服务协议)
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_DIRECT_HOUSE_LOAN_LOAN.getValue()));
            } else if (AgreementTypeEnum.TURN_AGREEMENT.getValue().equals(agreementType)) {//通用的房贷，债权转让及受让协议
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_HOUSE_CREDITOR_ASSIGNMENT.getValue()));
            }
        } else if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanType)) {
            // 个人车贷
            if (agreementType.equals(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_CAR_CREDITOR_LOAN.getValue()));
            } else if (agreementType.equals(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_CAR_CREDITOR_ASSIGNMENT.getValue()));
            }
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)) {
            // 企业标
            if (agreementType.equals(AgreementEnum.AgreementTypeEnum.LEND_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.COMPANY_PLEDGE_CREDITOR_LOAN.getValue()));
            } else if (agreementType.equals(AgreementEnum.AgreementTypeEnum.TURN_AGREEMENT.getValue())) {
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.COMPANY_PLEDGE_CREDITOR_ASSIGNMENT.getValue()));
            }
        } else {
            agree.setAgreementCode(creditorRightsCode);
        }
    }

    private void setLoanNumber(AgreementInfo agree, String loanType, String defaultCode, String agreementType) {
        // 借款标-生成借款协议
        if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue()
                .equals(loanType)) {
            // 企业车贷
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.COMPANY_CAR_LOAN_LOAN.getValue()));
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue()
                .equals(loanType)) {
            // 企业保理
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.COMPANY_FACTORING_LOAN_LOAN.getValue()));
        } else if (LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanType)) {
            // 个人房贷
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_HOUSE_LOAN_LOAN.getValue()));
        } else if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanType)) {
            // 个人信贷
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_CREDIT_LOAN_LOAN.getValue()));
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType)) {
            //企业信贷
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.COMPANY_CREDIT_LOAN_LOAN.getValue()));
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)) {
            //企业基金
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.COMPANY_FOUN_LOAN_LOAN.getValue()));
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)) {
            //企业标（质押标）
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.COMPANY_PLEDGE_LOAN_LOAN.getValue()));
        } else if (LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanType)) {
            // 个人房产直投
            if (AgreementTypeEnum.LEND_AGREEMENT.getValue().equals(agreementType)) {//借款标，个人房产直投(借款及服务协议)
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_DIRECT_HOUSE_LOAN_LOAN.getValue()));
            } else if (AgreementTypeEnum.ENTRUST_AGREEMENT.getValue().equals(agreementType)) {//借款标，个人房产直投(授权委托书)
                agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_DIRECT_HOUSE_LOAN_ENTRUST.getValue()));
            }
        } else if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanType)) {
            // 个人车贷
            agree.setAgreementCode(getAgreementCodeByType(AgreementEnum.AreementCodeTypeEnum.PERSON_CAR_LOAN_LOAN.getValue()));
        } else {
            agree.setAgreementCode(defaultCode);
        }

    }

    //redis获取指定类型的合同编号
    private String getAgreementCodeByType(String type) {
        String value = "";
        try {
            String yyyymm = DateUtil.getNowShortDate();
            int mm = Integer.valueOf(yyyymm);
            if (mm >= 201611) {//进入201611月份，合同编号走新的方法
                String key = type + "-" + yyyymm.substring(2, 6);
                long longValue = redisCacheManger.setIncreaseValidTime(key, 40 * 24 * 60 * 60);
                //返回没有0，需要减1
                int startValue = 5000;
                DecimalFormat df = new DecimalFormat("000000");
                value = df.format(startValue + longValue - 1);
                value = value.substring(value.length() - 6, value.length());
                logger.info("获取合同编号为：" + key + "-" + value);
                return key + "-" + value;
            } else {
                String key = type + "-" + yyyymm.substring(2, 6);
                value = redisCacheManger.getRedisCacheInfo(key);
                if (StringUtils.isNull(value)) {
                    value = "005000";
                } else {
                    DecimalFormat df = new DecimalFormat("000000");
                    value = df.format(Integer.valueOf(value) + 1);
                    value = value.substring(value.length() - 6, value.length());
                }
                //缓存有效期40天
                redisCacheManger.destroyRedisCacheInfo(key);
                long lock = redisCacheManger.setRedisCacheInfonx(key, value, 40 * 24 * 60 * 60);
                if (lock == 1) {
                    logger.info("获取合同编号为：" + key + "-" + value);
                    return key + "-" + value;
                } else {
                    logger.info("获取合同编号失败，请重新获取!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAgreementCodeByTypeNew(String type) {
        String value = "";
        try {
            String yyyymm = DateUtil.getNowShortDate();
            String key = type + "-" + yyyymm.substring(2, 6);
            long longValue = redisCacheManger.setIncreaseValidTime(key,
                    40 * 24 * 60 * 60);
            // 返回没有0，需要减1
            int startValue = 5000;
            DecimalFormat df = new DecimalFormat("000000");
            value = df.format(startValue + longValue - 1);
            value = value.substring(value.length() - 6, value.length());
            logger.info("获取合同编号为：" + key + "-" + value);
            return key + "-" + value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cancelEarlyAgreement(Long creditorRightsId) {
        List<AgreementInfo> agreementInfos = agreementInfoService.findByCreditorRightsId(creditorRightsId);

        //todoed 如果该债权之前已生成合同，则将原有合同状态置有无效状态
        if (agreementInfos != null || agreementInfos.size() > 0) {
            for (AgreementInfo info : agreementInfos) {
                AgreementInfo theAgreementInfo = new AgreementInfo();
                theAgreementInfo.setAgreementStatus(AgreementEnum.AgreementStatusEnum.INVALID.getValue());
                theAgreementInfo.setAgreementId(info.getAgreementId());
                agreementInfoService.update(theAgreementInfo);
            }
        }
    }

    /**
     * 生成债权转让标的出借协议
     *
     * @param loanProductId
     * @param storageFolder
     * @param paramsMap     包含creditorRightsId，lendUserId
     * @throws IOException
     * @throws DocumentException
     */
    public void createBidAgreementForLoanTrans_bak(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws IOException, DocumentException {
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId((Long) paramsMap.get("lendUserId"));

        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/fetchTransAgreementHtml";

        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);
        jsonMap.put("buyDate", creditorRights.getCreateTime());
        jsonMap.put("realName", userInfoVO.getRealName());
        jsonMap.put("idCard", userInfoVO.getIdCard());
        jsonMap.put("mobileNO", userInfoVO.getMobileNo());
        jsonMap.put("balance", creditorRights.getRightsWorth().toPlainString());
        String address = "";
        if (userInfoVO.getProvince() != null) {
            address += provinceInfoService.findById(userInfoVO.getProvince()).getProvinceName();
        }
        if (userInfoVO.getCity() != null) {
            address += cityInfoService.findById(userInfoVO.getCity()).getCityName();
        }
        if (userInfoVO.getDetail() != null) {
            address += userInfoVO.getDetail();
        }
        jsonMap.put("address", address);


        Pair p = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, "出借咨询与服务协议", p);
    }

    public void createCreditorHistory(CreditorRights oldCreditorRights, CreditorRights creditorRights) {
        CreditorRightsHistory creditorRightsHistory = new CreditorRightsHistory();
        creditorRightsHistory.setCreditorRightsIdAfterChange(creditorRights.getCreditorRightsId());
        creditorRightsHistory.setCreditorRightsIdBeforeChange(oldCreditorRights.getCreditorRightsId());
        creditorRightsHistory.setChangeBalance(creditorRights.getRightsWorth());
        creditorRightsHistory.setChangeTime(new Date());
        creditorRightsHistory.setRightsWorthBeforeChange(oldCreditorRights.getRightsWorth());
        creditorRightsHistory.setRightsWorthAfterChange(creditorRights.getRightsWorth());
        creditorRightsHistory.setLendPriceBeforeChange(oldCreditorRights.getLendPrice());
        creditorRightsHistory.setLendPriceAfterChange(creditorRights.getLendPrice());
        creditorRightsHistory.setChangeType(CreditorRightsHistory.CHANGETYPE_TURNOUT);
        creditorRightsHistory.setAnnualRateBeforeChange(oldCreditorRights.getAnnualRate());
        creditorRightsHistory.setAnnualRateAfterChange(creditorRights.getAnnualRate());
        creditorRightsHistory.setRepaymentCycleBeforeChange(oldCreditorRights.getRepaymentCycle());
        creditorRightsHistory.setRepaymentCycleAftreChange(oldCreditorRights.getRepaymentCycle());
        creditorRightsHistory.setRightsStateBeforeChange(oldCreditorRights.getRightsState());
        creditorRightsHistory.setRightsStateAftreChange(creditorRights.getRightsState());
        creditorRightsHistory.setDisplayStateBeforeChange(oldCreditorRights.getDisplayState());
        creditorRightsHistory.setDisplayStateAfterChange(creditorRights.getDisplayState());
        creditorRightsHistory.setIsDelayBeforeChange(oldCreditorRights.getIsDelay());
        creditorRightsHistory.setIsDelayAfterChnage(creditorRights.getIsDelay());
        creditorRightsService.newCreditorRightsHistory(creditorRightsHistory);

    }


    @Override
    public WithDraw commitCash2Card(ParaContext paraContext) throws Exception {

        WithDraw withDraw = new WithDraw();

        CustomerCard customerCard = paraContext.get("customerCard");
        withDraw.setUserId(Long.parseLong(paraContext.get("userId").toString()));
        withDraw.setCommissionFee(BigDecimal.ZERO);
        withDraw.setWithdrawAmount((BigDecimal) paraContext.get("balance"));
        withDraw.setCustomerCardId(customerCard.getCustomerCardId());
        withDraw.setOperateTime(new Date());
        withDraw.setOperatorId((Long) paraContext.get("adminId"));
        withDraw.setRemark((String) paraContext.get("desc"));
        withDraw.setTransStatus(WithDrawTransferStatus.UN_TRANSFER.getValue());
        withDraw.setVerifyStatus(VerifyStatus.PAST.getValue());
        withDraw.setHappenType(WithDrawSource.SYSTEM_WITHDRAW.getValue());

        paraContext.put("withDraw", withDraw);
        paraContext.put("fromType", "WithDraw");
        withDrawService.withDraw(paraContext);
        return withDraw;

    }


    /**
     * 创建还款计划
     *
     * @param product
     * @param loanApp
     * @param channelType
     * @return
     * @throws Exception
     */
    protected List<RepaymentPlan> createRepaymentPLan(LoanProduct product, LoanApplication loanApp, String channelType, Date firstRepaymentDate) throws Exception {
        List<RepaymentPlan> results = new ArrayList<RepaymentPlan>();
        results.addAll(repaymentPlanService.generateRepaymentPlan(product.getLoanProductId(), loanApp.getConfirmBalance()));
        Date theDay = firstRepaymentDate;
        Date startDate = new Date();
        for (RepaymentPlan repaymentPlan : results) {
            repaymentPlan.setCustomerAccountId(loanApp.getRepaymentAccountId());
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

    /**
     * 创建还款计划数据
     *
     * @param product
     * @param loanApp
     * @return
     * @throws Exception
     */
    public List<RepaymentPlan> getRepaymentPLanData(LoanProduct product, LoanApplication loanApp) throws Exception {
        List<RepaymentPlan> results = new ArrayList<RepaymentPlan>();
        results.addAll(repaymentPlanService.generateRepaymentPlan(product.getLoanProductId(), loanApp.getConfirmBalance()));
        return results;
    }

    /**
     * 添加借款申请
     */
    @Override
    public LoanApplication addLoanApplication(LoanApplication loanApplication) {
        myBatisDao.insert("LOANAPPLICATION.insert", loanApplication);
        return loanApplication;
    }

    /**
     * 修改借款申请
     */
    @Override
    public LoanApplication updateLoanApplication(LoanApplication loanApplication) {
        myBatisDao.update("LOANAPPLICATION.updateByPrimaryKeySelective", loanApplication);
        return loanApplication;
    }

    /**
     * 根据ID加载一条借款申请
     *
     * @param loanApplicationId 借款申请ID
     */
    @Override
    public LoanApplication getLoanApplicationById(Long loanApplicationId) {
        return myBatisDao.get("LOANAPPLICATION.selectByPrimaryKey", loanApplicationId);
    }

    @Override
    public List<LoanApplicationVO> findByProductIdOrderBy(long loanProductId, Map<String, Object> loanAppParaMap) {
        if (loanProductId != -1l) {
            loanAppParaMap.put("loanProductId", loanProductId);
        }
        return myBatisDao.getList("LOANAPPLICATION.findByProductIdOrderBy", loanAppParaMap);
    }

    /**
     * 添加借款第一页保存【main】
     *
     * @param mainLoan
     * @param basic      基础信息
     * @param user       用户信息
     * @param ext        用户扩展
     * @param existsUser 是否存在该用户
     * @return
     */
    @Override
    @Transactional
    public MainLoanApplication saveLoanPart1(MainLoanApplication mainLoan, CustomerBasicSnapshot basic, UserInfo user, UserInfoExt ext, Boolean existsUser) {
        try {

            // 如果不存在该用户，则新创建
            if (!existsUser) {
                // 用户信息
                user.setType(UserType.LINE.getValue());// 设置用户类型线下用户
                user = userInfoService.regist(user, UserSource.PLATFORM);

                // 用户扩展
                ext.setUserId(user.getUserId());
                userInfoExtService.updateUserInfoExt(ext);
            }

            // 借款信息
            mainLoan.setUserId(user.getUserId());
            mainLoan.setCreateTime(new Date());
            mainLoan = mainLoanApplicationService.addMainLoanApplication(mainLoan);

            // 基础信息
            basic.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
            customerBasicSnapshotService.addBasic(basic);

            ///////////// 下面是初始化添加  ///////////////
            //银行卡信息
            if (SubjectTypeEnum.LOAN.getValue().equals(mainLoan.getSubjectType())) {// 1借款标
                CustomerCard card = new CustomerCard();
                card.setUserId(user.getUserId());
                card = customerCardService.addCustomerCard(card);
                mainLoan.setInCardId(card.getCustomerCardId());//打款卡ID
                mainLoan.setOutCardId(card.getCustomerCardId());//还款划扣卡ID
            }

            //借款信息
            //如借款申请为借款标，则取得借款用户的借款账户ID
            if (mainLoan.getSubjectType().equals(SubjectTypeEnum.LOAN.getValue())) {
                UserAccount userAccount = userAccountService.getUserAccountByUserIdAndAccountTypeCode(user.getUserId(), AccountConstants.AccountTypeEnum.BORROW_ACCOUNT.getValue());
                mainLoan.setCustomerAccountId(userAccount.getAccId());
                mainLoan.setRepaymentAccountId(userAccount.getAccId());
            }
            mainLoan = mainLoanApplicationService.updateMainLoanApplication(mainLoan);

            //籍贯地址
            Address bornAddr = new Address();
            bornAddr = addressService.addAddress(bornAddr);

            //户口所在地
            Address registAddr = new Address();
            registAddr = addressService.addAddress(registAddr);

            //现住址
            Address residenceAddr = new Address();
            residenceAddr = addressService.addAddress(residenceAddr);

            //单位地址
            Address workingAddr = new Address();
            workingAddr = addressService.addAddress(workingAddr);

            //基础信息
            basic.setBornAddr(bornAddr.getAddressId());
            basic.setRegistAddr(registAddr.getAddressId());
            basic.setResidenceAddr(residenceAddr.getAddressId());
            basic = customerBasicSnapshotService.updateBasic(basic);

            //工作信息
            CustomerWorkSnapshot work = new CustomerWorkSnapshot();
            work.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
            work.setWorkingAddr(workingAddr.getAddressId());
            work = customerWorkSnapshotService.addWork(work);

            //房屋地址
            Address houseAddr = new Address();
            houseAddr = addressService.addAddress(houseAddr);

            //抵押信息
            CustomerHouseSnapshot house = new CustomerHouseSnapshot();
            house.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
            house.setHouseAddr(houseAddr.getAddressId());
            house = customerHouseSnapshotService.addHouse(house);
            if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(mainLoan.getLoanType())) {
                CustomerCarSnapshot customerCarSnapshot = new CustomerCarSnapshot();
                customerCarSnapshot.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
                customerCarSnapshotService.addCar(customerCarSnapshot);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainLoan;
    }


    @Override
    public int getCustomerSeqNum(Long loanApplicationId, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loanApplicationId", loanApplicationId);
        map.put("type", type);
        return myBatisDao.get("CUSTOMER_UPLOAD_SNAPSHOT.getCustomerSeqNum", map);
    }

    // by mainid
    @Override
    public int getCustomerSeqNumByMainId(Long mainLoanApplicationId, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mainLoanApplicationId", mainLoanApplicationId);
        map.put("type", type);
        return myBatisDao.get("CUSTOMER_UPLOAD_SNAPSHOT.getCustomerSeqNumByMainId", map);
    }

    @Override
    public List<CustomerUploadSnapshot> getcustomerUploadSnapshotList(
            Long loanApplicationId, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("type", type);
        return myBatisDao.getList("CUSTOMER_UPLOAD_SNAPSHOT.getcustomerUploadSnapshotList", params);
    }

    // by mainid
    @Override
    public List<CustomerUploadSnapshot> getCustomerUploadSnapshotListByMainId(
            Long mainLoanApplicationId, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mainLoanApplicationId", mainLoanApplicationId);
        params.put("type", type);
        return myBatisDao.getList("CUSTOMER_UPLOAD_SNAPSHOT.getCustomerUploadSnapshotListByMainId", params);
    }

    @Override
    public CustomerUploadSnapshot getcustomerUploadSnapshotDetails(Long cusId) {

        return myBatisDao.get("CUSTOMER_UPLOAD_SNAPSHOT.selectByPrimaryKey", cusId);
    }

    @Override
    @Transactional
    public void delImg(Long cusId, String status, Attachment atta, String rootPath) throws IOException {
        //System.out.println("111111");
        String fileName = atta.getUrl().substring(atta.getUrl().lastIndexOf("/"));
        String thumbnailFileName = atta.getThumbnailUrl().substring(atta.getThumbnailUrl().lastIndexOf("/"));
        logger.info("读取图片的地址：" + rootPath + this.temporaryPath + fileName);
        logger.info("目的地址：" + rootPath + this.deletePath);
        //如果图片物理文件不存在,不应该影响数据库数据的处理
        try {
            FileUtil fileUtil = new FileUtil();
            fileUtil.moveFile(rootPath + this.temporaryPath, fileName, rootPath + this.deletePath);
            fileUtil.moveFile(rootPath + this.temporaryPath, thumbnailFileName, rootPath + this.deletePath);
            fileUtil.deleteFile(rootPath + this.temporaryPath, fileName);
            fileUtil.deleteFile(rootPath + this.temporaryPath, thumbnailFileName);
        } catch (Exception e) {
            logger.error(LogUtils.createSimpleLog("删除或移动图片时出现异常", "可能是因为该图片物理文件已经不存在"));
        }

        atta.setUrl(this.deletePath + fileName);
        atta.setThumbnailUrl(this.deletePath + thumbnailFileName);
        atta.setPhysicalAddress(this.deletePath);
        myBatisDao.update("ATTACHMENT.updateByPrimaryKeySelective", atta);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cusId", cusId);
        params.put("status", status);
        myBatisDao.update("CUSTOMER_UPLOAD_SNAPSHOT.logicalDel", params);

    }


    /**
     * 添加借款第二页保存【main】
     *
     * @param mainLoan
     * @param bornAddr      籍贯地址
     * @param registAddr    户口所在地
     * @param residenceAddr 现住址
     * @param workingAddr   单位地址
     * @param basic         基础信息
     * @param ext           用户扩展
     * @param work          工作信息
     * @param contactsList  联系人列表
     * @param card          银行卡信息
     * @param feesItems     借款申请费用表集合
     * @return
     */
    @Override
    @Transactional
    public MainLoanApplication saveLoanPart2(MainLoanApplication mainLoan, Address bornAddr, Address registAddr,
                                             Address residenceAddr, Address workingAddr, CustomerBasicSnapshot basic, UserInfoExt ext,
                                             CustomerWorkSnapshot work, List<CustomerContactsSnapshot> contactsList, CustomerCard card,
                                             List<LoanApplicationFeesItem> feesItems) {

        //银行卡信息
        if (SubjectTypeEnum.LOAN.getValue().equals(mainLoan.getSubjectType())) {// 1借款标

            // 如果该用户的该卡已经存在，则关联原卡数据，不存在则创建新的
            CustomerCard customerCard = new CustomerCard();
            customerCard.setUserId(mainLoan.getUserId());
            customerCard.setCardCode(card.getCardCode());
            customerCard.setStatus(CustomerCardStatus.NORMAL.getValue());
            List<CustomerCard> cardList = customerCardService.getAllCustomerCard(customerCard);

            Long customerCardId = null;

            if (null != cardList && cardList.size() > 0) {//存在该卡号
                customerCardId = cardList.get(0).getCustomerCardId();
                for (CustomerCard cusCard : cardList) {
                    if (CustomerCardBindStatus.BINDED.getValue().equals(cusCard.getBindStatus())) {
                        customerCardId = cusCard.getCustomerCardId();
                    }
                }
            } else {//不存在该卡号
                card.setUserId(mainLoan.getUserId());
                CustomerCard cardNew = customerCardService.addCustomerCard(card);
                customerCardId = cardNew.getCustomerCardId();
            }

            mainLoan.setInCardId(customerCardId);//打款卡ID
            mainLoan.setOutCardId(customerCardId);//还款划扣卡ID
        }

        //借款信息
        LoanProduct loanProduct = loanProductService.findById(mainLoan.getLoanProductId());
        mainLoan.setAnnualRate(loanProduct.getAnnualRate());
        mainLoan = mainLoanApplicationService.updateMainLoanApplication(mainLoan);//main

        //籍贯地址
        bornAddr = addressService.updateAddress(bornAddr);

        //户口所在地
        registAddr = addressService.updateAddress(registAddr);

        //现住址
        residenceAddr = addressService.updateAddress(residenceAddr);

        //单位地址
        workingAddr = addressService.updateAddress(workingAddr);

        //基础信息
        basic.setBornAddr(bornAddr.getAddressId());
        basic.setRegistAddr(registAddr.getAddressId());
        basic.setResidenceAddr(residenceAddr.getAddressId());
        basic = customerBasicSnapshotService.updateBasic(basic);

        //用户扩展
        ext.setResidentAddress(residenceAddr.getAddressId());
        ext = userInfoExtService.updateUserInfoExt(ext);

        //工作信息
        work.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//main
        work.setWorkingAddr(workingAddr.getAddressId());
        work = customerWorkSnapshotService.updateWork(work);

        //删除所有联系人
        List<CustomerContactsSnapshot> contactsSnapshots = customerContactsSnapshotService.getContactsByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//main
        if (null != contactsSnapshots && contactsSnapshots.size() > 0) {
            for (int i = 0; i < contactsSnapshots.size(); i++) {
                customerContactsSnapshotService.deleteById(contactsSnapshots.get(i).getSnapshotId());
            }
        }

        //联系人列表
        if (null != contactsList && contactsList.size() > 0) {
            for (int i = 0; i < contactsList.size(); i++) {
                CustomerContactsSnapshot contacts = contactsList.get(i);
                contacts.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//main
                customerContactsSnapshotService.addContacts(contacts);
            }
        }

        //借款申请费用表
        if (null != feesItems && feesItems.size() > 0) {
            for (int i = 0; i < feesItems.size(); i++) {
                LoanApplicationFeesItem feesItem = feesItems.get(i);

                //借款申请费用表
                feesItem.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//main
                feesItem = loanApplicationFeesItemService.addLoanApplicationFeesItem(feesItem);

            }
        }

        return mainLoan;
    }

    /**
     * 添加借款第三页保存
     *
     * @param houseAddr 房屋地址
     * @param house     抵押信息
     */
    @Override
    @Transactional
    public CustomerHouseSnapshot saveLoanPart3(Address houseAddr, CustomerHouseSnapshot house) {

        //房屋地址
        houseAddr = addressService.updateAddress(houseAddr);

        //抵押信息
        house.setHouseAddr(houseAddr.getAddressId());
        customerHouseSnapshotService.updateHouse(house);
        return house;
    }

    /**
     * 提交初审
     *
     * @param loan                       借款信息
     * @param lendOrder                  出借订单
     * @param lendOrderBidDetail         出借订单投标明细
     * @param payOrder                   支付订单
     * @param payOrderDetail             支付订单明细
     * @param creditorRights             债权信息
     * @param repaymentPlansAndDetailVOs 还款计划
     */
    @Override
    @Transactional
    public LoanApplication saveLoanSubmit(LoanApplication loan,
                                          LendOrder lendOrder, LendOrderBidDetail lendOrderBidDetail,
                                          PayOrder payOrder, PayOrderDetail payOrderDetail,
                                          CreditorRights creditorRights,
                                          List<RepaymentPlansAndDetailVO> repaymentPlansAndDetailVOs, String source) {

        this.updateLoanApplication(loan);

//		// 1.[订单]
//		// (1)出借订单表 LEND_ORDER
//		// <1>客户资金账户ID CUSTOMER_ACCOUNT_ID
//		UserAccount userAccount = userAccountService.getCashAccount(loan.getOriginalUserId());
//		lendOrder.setCustomerAccountId(userAccount.getUserId());
//
//		LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
//		// <2>出借产品ID LEND_PRODUCT_ID
//		lendOrder.setLendProductId(loanProduct.getLendProductId());
//
//		// <3>发布明细Id LEND_PRODUCT_PUBLISH_ID
//		LendProductPublish lendProductPublish = lendProductService.getByPublishStateAndLendProductId(LendProductPublish.PUBLISHSTATE_SELLING, loanProduct.getLendProductId());
//		lendOrder.setLendProductPublishId(lendProductPublish.getLendProductPublishId());
//
//		// <4>打款客户卡ID IN_CARD_ID
//		BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(loan.getOriginalUserId());
//		CustomerCard customerCard = customerCardService.getCustomerCardByUserId(bondSourceUser.getUserId());
//		lendOrder.setInCardId(customerCard.getCustomerCardId());
//		lendOrder = lendOrderService.addLendOrder(lendOrder);
//
//		// (2)出借订单投标明细 LEND_ORDER_BID_DETAIL
//		// <1>出借产品订单ID LEND_ORDER_ID
//		lendOrderBidDetail.setLendOrderId(lendOrder.getLendOrderId());
//		// <2>借款申请ID LOAN_APPLICATION_ID
//		lendOrderBidDetail.setLoanApplicationId(loan.getLoanApplicationId());
//		lendOrderBidDetail = lendOrderBidDetailService.addLendOrderBidDetail(lendOrderBidDetail);
//
//		// 2.[支付]
//		// (1)支付订单表 PAY_ORDER
//		// <1>用户ID USER_ID
//		payOrder.setUserId(loan.getOriginalUserId());
//		payOrder = payService.addPayOrder(payOrder);
//
//		// (2)支付订单明细表 PAY_ORDER_DETAIL
//		// <1>支付单ID PAY_ID
//		payOrderDetail.setPayId(payOrder.getPayId());
//		payOrderDetail = payService.addPayOrderDetail(payOrderDetail);

        // 【生成订单并支付 - 开始】
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());

        //注:这里应该只查当前有效的，如果有多条有效的，就使用最后一个
        LendProductPublish lendProductPublish = null;
        List<LendProductPublish> lendProductPublishList = lendProductService.getByPublishStateAndLendProductId(LendProductPublishStateEnum.SELLING.value2Char(), loanProduct.getLendProductId());
        if (null != lendProductPublishList && lendProductPublishList.size() > 0) {
            for (LendProductPublish lendProductPub : lendProductPublishList) {
                lendProductPublish = lendProductPub;
            }
        }

        BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(loan.getOriginalUserId());


        PayResult payResult = lendProductService.bidLoanByAccountBalance(loan.getLoanApplicationId(), bondSourceUser.getUserId(), lendProductPublish.getLendProductPublishId(), loan.getLoanBalance(), source);


        // 【生成订单并支付 - 结束】

        // 3.[债权]
        // (1)债权信息表 CREDITOR_RIGHTS
        // <1>出借产品订单ID LEND_ORDER_ID
        creditorRights.setLendOrderId(lendOrder.getLendOrderId());//[?]

        // <2>借款申请ID LOAN_APPLICATION_ID
        creditorRights.setLoanApplicationId(loan.getLoanApplicationId());

        // <3>借款放款账户ID LOAN_ACCOUNT_ID
        UserAccount userAccount2 = userAccountService.getCashAccount(loan.getOriginalUserId());
        creditorRights.setLoanAccountId(userAccount2.getAccId());

        // <4>借款还款账户ID REPAYMENT_ACCOUNT_ID
        BondSourceUser bondSourceUser2 = bondSourceService.getBondSourceUserById(loan.getOriginalUserId());
        UserAccount userAccount3 = userAccountService.getCashAccount(bondSourceUser2.getUserId());
        creditorRights.setRepaymentAccountId(userAccount3.getAccId());

        // <5>出借人资金账户ID LEND_ACCOUNT_ID
        UserAccount userAccount4 = userAccountService.getCashAccount(loan.getOriginalUserId());
        creditorRights.setLendAccountId(userAccount4.getAccId());

        // <6>出借人用户ID LEND_USER_ID
        BondSourceUser bondSourceUser3 = bondSourceService.getBondSourceUserById(loan.getOriginalUserId());
        creditorRights.setLendUserId(bondSourceUser3.getUserId());

        // <7>借款人用户ID LOAN_USER_ID
        creditorRights.setLoanUserId(loan.getUserId());
        creditorRights = creditorRightsService.addCreditorRights(creditorRights);

        if (null != repaymentPlansAndDetailVOs && repaymentPlansAndDetailVOs.size() > 0) {
            for (RepaymentPlansAndDetailVO repaymentPlansAndDetailVO : repaymentPlansAndDetailVOs) {

                // (3)还款计划表 REPAYMENT_PLAN（多条）
                RepaymentPlan repaymentPlan = repaymentPlansAndDetailVO.getRepaymentPlan();
                // <1>借款申请ID LOAN_APPLICATION_ID
                repaymentPlan.setLoanApplicationId(loan.getLoanApplicationId());
                // <2>还款账户ID CUSTOMER_ACCOUNT_ID
                repaymentPlan.setCustomerAccountId(userAccount3.getAccId());
                repaymentPlan = repaymentPlanService.addRepaymentPlan(repaymentPlan);

                // (2)债权还款明细 RIGHTS_REPAYMENT_DETAIL（多条）
                RightsRepaymentDetail rightsRepaymentDetail = repaymentPlansAndDetailVO.getRightsRepaymentDetail();
                // <1>债权ID CREDITOR_RIGHTS_ID
                rightsRepaymentDetail.setCreditorRightsId(creditorRights.getCreditorRightsId());

                // <2>借款还款账户ID LOAN_ACCOUNT_ID
                rightsRepaymentDetail.setLoanAccountId(userAccount3.getAccId());
                // <3>出借资金账户ID LEND_ACCOUNT_ID
                rightsRepaymentDetail.setLendAccountId(userAccount4.getAccId());

                // <4>还款计划ID REPAYMENT_PLAN_ID
                rightsRepaymentDetail.setRepaymentPlanId(repaymentPlan.getRepaymentPlanId());
                rightsRepaymentDetail = rightsRepaymentDetailService.addRightsRepaymentDetail(rightsRepaymentDetail);

            }
        }

        return loan;
    }

    /**
     * 借款申请提交初审【main】
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MainLoanApplication submitLoanAppcalication(MainLoanApplication mainLoanApplication, String rootPath) throws Exception {
        //借款申请为分借款标和债权标两个不同处理
        if (mainLoanApplication.getSubjectType().equals(SubjectTypeEnum.LOAN.getValue())) {

        } else {//如果是债权标的情况(注意：后续将这部分，转移到发标操作处)，创建还款计划（数据来源：下线） 和 创建债权及债权明细（数据来源：下线）。

//            LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
//            Date firstRepaymentDate = null;
//            Date lastDate = null;
//            Date now = new Date();
//            if (String.valueOf(loanProduct.getDueTimeType()).equals(DueTimeTypeEnum.DAY.getValue())) {
//                firstRepaymentDate = DateUtil.addDate(now, Calendar.DAY_OF_MONTH, loanProduct.getCycleValue());
//                int cycleCounts = 0;
//                if (loanProduct.getDueTime() % loanProduct.getCycleValue() == 0) {
//                    cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue();
//                } else {
//                    cycleCounts = loanProduct.getDueTime() / loanProduct.getCycleValue() + 1;
//                }
//                lastDate = DateUtil.addDate(firstRepaymentDate, Calendar.DAY_OF_MONTH, cycleCounts * loanProduct.getCycleValue() - 1);
//            } else if (String.valueOf(loanProduct.getDueTimeType()).equals(DueTimeTypeEnum.MONTH.getValue())) {
//                firstRepaymentDate = DateUtil.addDate(now, Calendar.MONTH, 1);
//                lastDate = DateUtil.addDate(firstRepaymentDate, Calendar.MONTH, loanProduct.getDueTime() - 1);
//            }
//
//            //todoed 新增还款计划
//
//            loanApplication.setConfirmBalance(loanApplication.getLoanBalance());
//            List<RepaymentPlan> repaymentPlans = createRepaymentPLan(loanProduct, loanApplication, ChannelTypeEnum.OFFLINE.getValue(), firstRepaymentDate);
//            //todoed 新增出借订单
//            long lendAccountId = loanApplication.getCustomerAccountId();
//            LendProduct rightLendProduct = lendLoanBindingService.findRightsProduct(loanProduct.getLoanProductId());
//
//            //注:这里应该只查当前有效的，如果有多条有效的，就使用最后一个
//            LendProductPublish lendPublish = null;
//            List<LendProductPublish> lendPublishList = lendProductService.getByPublishStateAndLendProductId(LendProductPublishStateEnum.SELLING.value2Char(), rightLendProduct.getLendProductId());
//            if (null != lendPublishList && lendPublishList.size() > 0) {
//                for (LendProductPublish lendProductPublish : lendPublishList) {
//                    lendPublish = lendProductPublish;
//                }
//            }
//
//            UserAccount lendUserAccount = userAccountService.getUserAccountByAccId(lendAccountId);
//            CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(lendUserAccount.getUserId(), PayConstants.PayChannel.LL);
//            LendOrder lendOrder = lendOrderService.newRightsLendOrder(rightLendProduct, lendPublish, loanApplication, lendUserAccount, customerCard.getCustomerCardId());
//
//            //todoed 新增出借订单支付单并修改支付单的状态为已支付
//            Pair<String, BigDecimal> offAmount = new Pair(PayConstants.AmountType.RECHARGE.getValue(), loanApplication.getConfirmBalance());
//            PayOrder payOrder = payService.addPayOrder(loanApplication.getConfirmBalance(), new Date(),
//                    lendUserAccount.getUserId(), PayConstants.BusTypeEnum.BUY_FINANCE, offAmount);
//            PayOrder updatePayOrder = new PayOrder();
//            updatePayOrder.setPayId(payOrder.getPayId());
//            updatePayOrder.setStatus(PayConstants.OrderStatus.SUCCESS.getValue());
//            updatePayOrder.setProcessStatus(PayConstants.ProcessStatus.SUCCESS.getValue());
//            myBatisDao.update("PAY_ORDER.updateByPrimaryKeySelective", updatePayOrder);
//
//            //todoed 修改订单状态为匹配中
//            lendOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.RIGHTSING.getValue());
//            Map<String, Object> lendOrderMap = new HashMap<String, Object>();
//            lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
//            lendOrderMap.put("orderState", lendOrder.getOrderState());
//            lendOrderService.update(lendOrderMap);
//            //todoed 新增债权 & 新增债权明细
//            String rightsCode = creditorRightsService.createRightsCode(CreditorRightsFromWhereEnum.BUY.value2Char(), lendOrder.getOrderCode(), loanApplication.getLoanApplicationCode());
//            CreditorRights creditorRights = creditorRightsService.createCreditorRights(lendOrder, loanApplication, rightsCode, lendOrder.getBuyBalance(),
//                    DisplayEnum.DISPLAY.value2Char(), BigDecimal.valueOf(0), repaymentPlans,CreditorRightsStateEnum.APPLYTURNOUT,ChannelTypeEnum.OFFLINE);
//            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
//            LendOrderBidDetail orderBidDetail = lendOrderBidDetails.get(0);
//            orderBidDetail.setCreditorRightsId(creditorRights.getCreditorRightsId());
//            //todoed 修改出借明细的状态及关联债权ID
//            lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.BIDSUCCESS.value2Char(), creditorRights.getCreditorRightsId());


        }
        mainLoanApplication.setMainLoanBalance(mainLoanApplication.getLoanBalance());//主借款总金额
        mainLoanApplication.setMainPublishBalance(new BigDecimal("0"));//总发标金额
        mainLoanApplication.setConfirmBalance(mainLoanApplication.getLoanBalance());
        mainLoanApplication.setApplicationState(LoanApplicationStateEnum.PUBLISHAUDITING.getValue());
        mainLoanApplication.setPublishState(LoanApplicationPublishStateEnum.EDITDESC.getValue());
        mainLoanApplication.setLendState(LoanAppLendAuditStatusEnums.UN_COMMIT.getValue());
        mainLoanApplication.setVerifyState(LoanApplicationVerifyStateEnum.UNSUBMIT.getValue());
        LoanProduct loanProduct = loanProductService.findById(mainLoanApplication.getLoanProductId());
        mainLoanApplication.setInterestBalance(InterestCalculation.getAllInterest(mainLoanApplication.getConfirmBalance(), mainLoanApplication.getAnnualRate(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue()));
        Map<String, Object> loanMap = new HashMap<String, Object>();
        loanMap.put("mainLoanApplicationId", mainLoanApplication.getMainLoanApplicationId());
        loanMap.put("applicationState", mainLoanApplication.getApplicationState());
        loanMap.put("publishState", mainLoanApplication.getPublishState());
        loanMap.put("lendState", mainLoanApplication.getLendState());
        loanMap.put("confirmBalance", mainLoanApplication.getConfirmBalance());
        loanMap.put("interestBalance", mainLoanApplication.getInterestBalance());
        loanMap.put("verifyState", mainLoanApplication.getVerifyState());
        loanMap.put("mainLoanBalance", mainLoanApplication.getMainLoanBalance());////主借款总金额
        loanMap.put("mainPublishBalance", mainLoanApplication.getMainPublishBalance());////已发标借款
        mainLoanApplicationService.update(loanMap);//main

        imgToFormal(mainLoanApplication.getMainLoanApplicationId(), rootPath);

        return mainLoanApplication;
    }


    @Override
    public Pagination<LoanApplicationListVO> getLoanApplicationPaging(int pageNo, int pageSize, LoanApplicationListVO loanVo, Map<String, Object> customParams) {
        Pagination<LoanApplicationListVO> re = new Pagination<LoanApplicationListVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanVo", loanVo);
        params.put("preheatTime", new Date());
        if (null != customParams && customParams.containsKey("isNewUserLoan")) {
            params.put("isNewUserLoan", customParams.get("isNewUserLoan"));
        }

        int totalCount = this.myBatisDao.count("getLoanApplicationPaging", params);
        List<LoanApplicationListVO> uah = this.myBatisDao.getListForPaging("getLoanApplicationPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    @Override
    public Pagination<LoanApplicationListVO> getTurnCreditRightPaging(int pageNo, int pageSize, LoanApplicationListVO loanVo, Map<String, Object> customParams) {
        Pagination<LoanApplicationListVO> re = new Pagination<LoanApplicationListVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanVo", loanVo);
        params.put("preheatTime", new Date());


        int totalCount = this.myBatisDao.count("getTurnCreditRightPaging", params);
        List<LoanApplicationListVO> uah = this.myBatisDao.getListForPaging("getTurnCreditRightPaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    @Override
    public List<BigDecimal> getLoanRateTypes(LoanApplicationStateEnum... loanApplicationStateEnum) {
        List<String> statusList = new ArrayList<String>();
        if (loanApplicationStateEnum == null || loanApplicationStateEnum.length == 0) {
            statusList = null;
        } else {
            for (LoanApplicationStateEnum applicationStateEnum : loanApplicationStateEnum) {
                statusList.add(applicationStateEnum.getValue());
            }
        }


        Map<String, Object> param = new HashMap<String, Object>();
        param.put("statusList", statusList);
        List<BigDecimal> reuslt = myBatisDao.getList("getLoanRateTypes", param);
        return reuslt;
    }

    @Override
    public List<Integer> getDurationTypes(LoanApplicationStateEnum... loanApplicationStateEnum) {
        List<String> statusList = new ArrayList<String>();
        if (loanApplicationStateEnum == null || loanApplicationStateEnum.length == 0) {
            statusList = null;
        } else {
            for (LoanApplicationStateEnum applicationStateEnum : loanApplicationStateEnum) {
                statusList.add(applicationStateEnum.getValue());
            }
        }


        Map<String, Object> param = new HashMap<String, Object>();
        param.put("statusList", statusList);
        List<Integer> reuslt = myBatisDao.getList("getDurationTypes", param);
        return reuslt;
    }

    @Override
    public LoanApplicationListVO getLoanApplicationVoById(Long loanApplicationNo) {
        return myBatisDao.get("LOANAPPLICATION.getLoanApplicationVoById", loanApplicationNo);
    }

    // by mainid
    @Override
    public LoanApplicationListVO getLoanApplicationVoByMainId(Long loanApplicationNo) {
        return myBatisDao.get("LOANAPPLICATION.getLoanApplicationVoByMainId", loanApplicationNo);
    }

    @Override
    public BigDecimal getTotalLoanAmount(Long userId) {
        //判断参数是否为null
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        return myBatisDao.get("LOANAPPLICATION.getTotalLoanAmount", params);
    }

    @Override
    public List<Integer> getAuthInfo(Long loanApplicationNo) {
        return myBatisDao.getList("LOANAPPLICATION.getAuthInfo", loanApplicationNo);
    }

    @Override
    public List<CustomerUploadSnapshotVO> getcustomerUploadAttachment(Long loanApplicationId, String isCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("isCode", isCode);
        return myBatisDao.getList("CUSTOMER_UPLOAD_SNAPSHOT.getcustomerUploadAttachment", params);
    }

    //main
    @Override
    public List<CustomerUploadSnapshotVO> getCustomerUploadAttachmentByMainId(Long mainLoanApplicationId, String isCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mainLoanApplicationId", mainLoanApplicationId);
        params.put("isCode", isCode);
        return myBatisDao.getList("CUSTOMER_UPLOAD_SNAPSHOT.getCustomerUploadAttachmentByMainId", params);
    }

    @Override
    public BigDecimal getBidedBalance(long loanApplicationId) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("loanApplicationId", loanApplicationId);
        paraMap.put("status", LendOrderBidStatusEnum.BIDING.getValue());
        return myBatisDao.get("LENDORDERBIDDETAIL.sumByLoanApplicationId", paraMap);
    }

    @Override
    public Pagination getRepaymentList(int pageNum, int pageSize, String loanApplicationCode, String loanApplicationName, String channel,
                                       String loanType, String realName, String idCard, String mobileNo, String planState, String beginRepaymentDay, String endRepaymentDay) {
        Pagination<RepaymentPlanVO> re = new Pagination<RepaymentPlanVO>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationCode", loanApplicationCode);
        params.put("loanApplicationName", loanApplicationName);
        params.put("channel", channel);
        params.put("loanType", loanType);
        params.put("userRealName", realName);
        params.put("idCard", idCard);
        params.put("mobileNo", mobileNo);
        params.put("planState", planState);
        params.put("beginRepaymentDay", beginRepaymentDay);
        params.put("endRepaymentDay", endRepaymentDay);

        int totalCount = this.myBatisDao.count("getRepaymentPlanList", params);
        List<RepaymentPlanVO> repaymentPlanVOs = this.myBatisDao.getListForPaging("getRepaymentPlanList", params, pageNum, pageSize);

        for (RepaymentPlanVO repaymentPlanVO : repaymentPlanVOs) {
            long loanApplicationId = repaymentPlanVO.getLoanApplicationId();
            BigDecimal zeroBigDecimal = BigDecimal.valueOf(0l);

            //todoed 计算应还费用，如为逾期则加上逾期费
            List<LoanApplicationFeesItem> applicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATCYCLE);
            if (applicationFeesItems == null) {
                applicationFeesItems = new ArrayList<LoanApplicationFeesItem>();
            }
            String nowStr = "";
            try {
                nowStr = DateUtil.getNowShortDate();
            } catch (Exception e) {
                logger.error("日期转换异常！", e);
            }
//            if (repaymentPlanVO.getPlanState() == RepaymentPlanStateEnum.DEFAULT.value2Char()) {
            if (repaymentPlanVO.getRepaymentDay().getTime() < DateUtil.parseStrToDate(nowStr, "yyyyMMdd").getTime()) {
                List<LoanApplicationFeesItem> delayFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATDELAY_FIRSTDAY);
                if (delayFeesItems != null) {
                    applicationFeesItems.addAll(delayFeesItems);
                }

            }
            //计算应还费用，逾期费和罚息单独计算
            BigDecimal sumShouldFees = BigDecimal.valueOf(0l);
            for (LoanApplicationFeesItem feesItem : applicationFeesItems) {
                if (feesItem.getChargeCycle() != FeesPointEnum.ATDELAY_FIRSTDAY.value2Char() && feesItem.getChargeCycle() != FeesPointEnum.ATDELAY.value2Char()) {
                    sumShouldFees = sumShouldFees.add(repaymentPlanService.getDefaultBalance(feesItem.getFeesRate(), feesItem.getRadicesType(), repaymentPlanVO.getShouldCapital2()
                            , repaymentPlanVO.getShouldInterest2(), zeroBigDecimal, zeroBigDecimal, zeroBigDecimal,
                            zeroBigDecimal, zeroBigDecimal, zeroBigDecimal));
                }
//                sumShouldFees = sumShouldFees.add(loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(feesItem, zeroBigDecimal, zeroBigDecimal,
//                        repaymentPlanVO.getShouldCapital2(), repaymentPlanVO.getShouldInterest2(), zeroBigDecimal, zeroBigDecimal));
            }

            //todoed 计算实还费用
            Map<String, Object> feesDetailMap = new HashMap<String, Object>();
            feesDetailMap.put("loanApplicationId", loanApplicationId);
            feesDetailMap.put("feesCycle", FeesPointEnum.ATCYCLE.getValue());
            List<LoanFeesDetail> loanFeesDetails = loanFeesDetailService.getFeesItemBy(feesDetailMap);
            if (loanFeesDetails == null) {
                loanFeesDetails = new ArrayList<LoanFeesDetail>();
            }
            feesDetailMap.put("feesCycle", FeesPointEnum.ATDELAY_FIRSTDAY.getValue());
            List<Integer> sectionCodes = new ArrayList<Integer>();
            sectionCodes.add(repaymentPlanVO.getSectionCode());
            feesDetailMap.put("sectionCodes", sectionCodes);
            List<LoanFeesDetail> delayFeesDetails = loanFeesDetailService.getFeesItemBy(feesDetailMap);
            //有逾期费，计算应还和实还逾期费
            if (delayFeesDetails != null) {
                loanFeesDetails.addAll(delayFeesDetails);
//                if(shouldDelayFees != null){
//                	sumShouldFees =sumShouldFees.add(repaymentPlanService.getDefaultBalance(shouldDelayFees.getFeesRate(), shouldDelayFees.getRadicesType(), repaymentPlanVO.getShouldCapital2()
//        					, repaymentPlanVO.getShouldInterest2(), zeroBigDecimal,  zeroBigDecimal, repaymentPlanVO.getFactCalital(),
//        					repaymentPlanVO.getFactInterest(), zeroBigDecimal, zeroBigDecimal));
//                }
            }


            BigDecimal sumFactFees = BigDecimal.valueOf(0l);
            for (LoanFeesDetail loanFeesDetail : loanFeesDetails) {
                sumFactFees = sumFactFees.add(loanFeesDetail.getPaidFees() != null ? loanFeesDetail.getPaidFees() : BigDecimal.ZERO);
                sumShouldFees = sumShouldFees.add(loanFeesDetail.getFees2());
            }
            repaymentPlanVO.setFactFees(sumFactFees);
            repaymentPlanVO.setShouldFees(BigDecimalUtil.up(sumShouldFees, 2));
            //todoed 计算应还罚息
            BigDecimal defaultInterests = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(repaymentPlanVO.getRepaymentPlanId());
            repaymentPlanVO.setShouldDefaultInterest(BigDecimalUtil.up(defaultInterests, 2));
            //todoed 计算实还罚息
//            feesDetailMap.put("feesCycle", FeesPointEnum.ATDELAY.getValue());
//            List<LoanFeesDetail> defaultInterestsDetails = loanFeesDetailService.getFeesItemBy(feesDetailMap);
//            if (defaultInterestsDetails != null) {
//                defaultInterestsDetails = new ArrayList<LoanFeesDetail>();
//            }
            BigDecimal sumDefaultInterest = defaultInterestDetailService.getDefaultInterestPaidByRepaymentPlanId(repaymentPlanVO.getRepaymentPlanId());
//            for (LoanFeesDetail loanFeesDetail : defaultInterestsDetails) {
//                sumDefaultInterest = sumDefaultInterest.add(loanFeesDetail.getPaidFees()!=null?loanFeesDetail.getPaidFees():BigDecimal.ZERO);
//            }
            repaymentPlanVO.setFaceDefaultInterest(sumDefaultInterest);

            repaymentPlanVO.setShouldBalance2(repaymentPlanVO.getShouldBalance2().add(repaymentPlanVO.getShouldFees().add(repaymentPlanVO.getShouldDefaultInterest())));
            repaymentPlanVO.setFactBalance(repaymentPlanVO.getFactBalance().add(repaymentPlanVO.getFactFees().add(repaymentPlanVO.getFaceDefaultInterest())));

        }
        re.setTotal(totalCount);
        re.setRows(repaymentPlanVOs);

        return re;
    }

    /**
     * 导出所有到期还款数据(导出excel报表专用)
     */
    @Override
    public List<LinkedHashMap<String, Object>> getRepaymentAllList(String loanApplicationCode, String loanApplicationName, String channel,
                                                                   String loanType, String realName, String idCard, String mobileNo, String planState, String beginRepaymentDay, String endRepaymentDay) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationCode", loanApplicationCode);
        params.put("loanApplicationName", loanApplicationName);
        params.put("channel", channel);
        params.put("loanType", loanType);
        params.put("userRealName", realName);
        params.put("idCard", idCard);
        params.put("mobileNo", mobileNo);
        params.put("planState", planState);
        params.put("beginRepaymentDay", beginRepaymentDay);
        params.put("endRepaymentDay", endRepaymentDay);

        // 查询
        List<RepaymentPlanVO> repaymentPlanVOs = this.myBatisDao.getList("getRepaymentPlanList", params);

        // 存储
        List<LinkedHashMap<String, Object>> linkedHashMapList = new LinkedList<LinkedHashMap<String, Object>>();

        for (RepaymentPlanVO repaymentPlanVO : repaymentPlanVOs) {
            long loanApplicationId = repaymentPlanVO.getLoanApplicationId();
            BigDecimal zeroBigDecimal = BigDecimal.valueOf(0l);

            //todoed 计算应还费用，如为逾期则加上逾期费
            List<LoanApplicationFeesItem> applicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATCYCLE);
            if (applicationFeesItems == null) {
                applicationFeesItems = new ArrayList<LoanApplicationFeesItem>();
            }
            String nowStr = "";
            try {
                nowStr = DateUtil.getNowShortDate();
            } catch (Exception e) {
                logger.error("日期转换异常！", e);
            }

            if (repaymentPlanVO.getRepaymentDay().getTime() < DateUtil.parseStrToDate(nowStr, "yyyyMMdd").getTime()) {
                List<LoanApplicationFeesItem> delayFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATDELAY_FIRSTDAY);
                if (delayFeesItems != null) {
                    applicationFeesItems.addAll(delayFeesItems);
                }

            }

            //计算应还费用，逾期费和罚息单独计算
            BigDecimal sumShouldFees = BigDecimal.valueOf(0l);
            for (LoanApplicationFeesItem feesItem : applicationFeesItems) {
                if (feesItem.getChargeCycle() != FeesPointEnum.ATDELAY_FIRSTDAY.value2Char() && feesItem.getChargeCycle() != FeesPointEnum.ATDELAY.value2Char()) {
                    sumShouldFees = sumShouldFees.add(repaymentPlanService.getDefaultBalance(feesItem.getFeesRate(), feesItem.getRadicesType(), repaymentPlanVO.getShouldCapital2()
                            , repaymentPlanVO.getShouldInterest2(), zeroBigDecimal, zeroBigDecimal, zeroBigDecimal,
                            zeroBigDecimal, zeroBigDecimal, zeroBigDecimal));
                }
            }

            //todoed 计算实还费用
            Map<String, Object> feesDetailMap = new HashMap<String, Object>();
            feesDetailMap.put("loanApplicationId", loanApplicationId);
            feesDetailMap.put("feesCycle", FeesPointEnum.ATCYCLE.getValue());
            List<LoanFeesDetail> loanFeesDetails = loanFeesDetailService.getFeesItemBy(feesDetailMap);
            if (loanFeesDetails == null) {
                loanFeesDetails = new ArrayList<LoanFeesDetail>();
            }
            feesDetailMap.put("feesCycle", FeesPointEnum.ATDELAY_FIRSTDAY.getValue());
            List<Integer> sectionCodes = new ArrayList<Integer>();
            sectionCodes.add(repaymentPlanVO.getSectionCode());
            feesDetailMap.put("sectionCodes", sectionCodes);
            List<LoanFeesDetail> delayFeesDetails = loanFeesDetailService.getFeesItemBy(feesDetailMap);
            //有逾期费，计算应还和实还逾期费
            if (delayFeesDetails != null) {
                loanFeesDetails.addAll(delayFeesDetails);
            }

            BigDecimal sumFactFees = BigDecimal.valueOf(0l);
            for (LoanFeesDetail loanFeesDetail : loanFeesDetails) {
                sumFactFees = sumFactFees.add(loanFeesDetail.getPaidFees() != null ? loanFeesDetail.getPaidFees() : BigDecimal.ZERO);
                sumShouldFees = sumShouldFees.add(loanFeesDetail.getFees2());
            }
            repaymentPlanVO.setFactFees(sumFactFees);
            repaymentPlanVO.setShouldFees(BigDecimalUtil.up(sumShouldFees, 2));
            //todoed 计算应还罚息
            BigDecimal defaultInterests = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(repaymentPlanVO.getRepaymentPlanId());
            repaymentPlanVO.setShouldDefaultInterest(BigDecimalUtil.up(defaultInterests, 2));
            //todoed 计算实还罚息
            BigDecimal sumDefaultInterest = defaultInterestDetailService.getDefaultInterestPaidByRepaymentPlanId(repaymentPlanVO.getRepaymentPlanId());
            repaymentPlanVO.setFaceDefaultInterest(sumDefaultInterest);

            repaymentPlanVO.setShouldBalance2(repaymentPlanVO.getShouldBalance2().add(repaymentPlanVO.getShouldFees().add(repaymentPlanVO.getShouldDefaultInterest())));
            repaymentPlanVO.setFactBalance(repaymentPlanVO.getFactBalance().add(repaymentPlanVO.getFactFees().add(repaymentPlanVO.getFaceDefaultInterest())));

            // 存数数据
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("标的编号", repaymentPlanVO.getLoanApplicationCode());//标的编号
            map.put("应还款日", DateUtil.getDateLong(repaymentPlanVO.getRepaymentDay()));//应还款日
            map.put("借款名称", repaymentPlanVO.getLoanApplicationName());//借款名称
            map.put("标的名称", repaymentPlanVO.getLoanTitle());//标的名称
            map.put("期号", repaymentPlanVO.getSectionCode());//期号
            map.put("借款人姓名", repaymentPlanVO.getUserRealName());//借款人姓名
            map.put("应还本金", repaymentPlanVO.getShouldCapital2());//应还本金
            map.put("应还利息", repaymentPlanVO.getShouldInterest2());//应还利息
            map.put("应还费用", repaymentPlanVO.getShouldFees());//应还费用
            map.put("应还罚息", repaymentPlanVO.getShouldDefaultInterest());//应还罚息
            map.put("应还金额", repaymentPlanVO.getShouldBalance2());//应还金额
            map.put("已还本金", repaymentPlanVO.getFactCalital());//已还本金
            map.put("已还利息", repaymentPlanVO.getFactInterest());//已还利息
            map.put("已还费用", repaymentPlanVO.getFactFees());//已还费用
            map.put("已还罚息", repaymentPlanVO.getFaceDefaultInterest());//已还罚息
            map.put("已还金额", repaymentPlanVO.getFactBalance());//已还金额

            String planStateDisPlayStr = "";
            String planStateStr = String.valueOf(repaymentPlanVO.getPlanState());
            if (RepaymentPlanStateEnum.UNCOMPLETE.getValue().equals(planStateStr)) {//1.未还款
                planStateDisPlayStr = RepaymentPlanStateEnum.UNCOMPLETE.getDesc();
            } else if (RepaymentPlanStateEnum.PART.getValue().equals(planStateStr)) {//2.部分还款
                planStateDisPlayStr = RepaymentPlanStateEnum.PART.getDesc();
            } else if (RepaymentPlanStateEnum.COMPLETE.getValue().equals(planStateStr)) {//3.已还清
                planStateDisPlayStr = RepaymentPlanStateEnum.COMPLETE.getDesc();
            } else if (RepaymentPlanStateEnum.DEFAULT.getValue().equals(planStateStr)) {//4.逾期
                planStateDisPlayStr = RepaymentPlanStateEnum.DEFAULT.getDesc();
            } else if (RepaymentPlanStateEnum.BEFORE_COMPLETE.getValue().equals(planStateStr)) {//5.提前还款
                planStateDisPlayStr = RepaymentPlanStateEnum.BEFORE_COMPLETE.getDesc();
            }

            map.put("当期状态", planStateDisPlayStr);//当期状态
            linkedHashMapList.add(map);
        }

        return linkedHashMapList;
    }

    @Override
    public LendProductPublish getLendProductPublishByLoanApplicationId(Long loanApplicationId) {
        List<LendProductPublish> loanProductPublishs = myBatisDao.getList("getLendProductPublishByLoanApplicationId", loanApplicationId);
        if (loanProductPublishs != null) {
            return loanProductPublishs.get(0);
        }
        return null;
    }

    @Override
    public LoanPublish getLoanPublishByAppId(Long loanApplicationId) {
        List<LoanPublish> loanPublishList = myBatisDao.getList("LOANPUBLISH.getLoanPublishByAppId", loanApplicationId);
        return loanPublishList.get(0);
    }

    /**
     * 判断借款申请是否满标，如满标则修改借款申请状态，修改未支付出借订单状态及出借明细状态
     *
     * @param loanApplication
     * @param date
     * @throws Exception
     */
    @Override
    public void notice2FullBid(LoanApplication loanApplication, Date date) throws Exception {

        BigDecimal sumBid = lendOrderBidDetailService.sumCreByLoanApp(loanApplication.getLoanApplicationId(), LendOrderBidStatusEnum.BIDING);
        if (loanApplication.getConfirmBalance() != null) {
            if (BigDecimalUtil.compareTo(sumBid, loanApplication.getConfirmBalance(), 2) >= 0) {
                //todo 防并发操作
                loanApplication.setApplicationState(LoanApplicationStateEnum.LOANAUDIT.getValue());
                loanApplication.setLendState(LoanAppLendAuditStatusEnums.FULL_AUDITING.getValue());
                loanApplication.setFullTime(date);
                Map map = new HashMap();
                map.put("loanApplicationId", loanApplication.getLoanApplicationId());
                map.put("applicationState", loanApplication.getApplicationState());
                map.put("lendState", loanApplication.getLendState());
                map.put("fullTime", loanApplication.getFullTime());
                myBatisDao.update("LOANAPPLICATION.updateByMap", map);
                //todoed 满标时，未支付的订单做超时处理
                List<LendOrderBidDetail> orderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(loanApplication.getLoanApplicationId());
                for (LendOrderBidDetail orderBidDetail : orderBidDetails) {
                    if (orderBidDetail.getStatus() == LendOrderBidStatusEnum.WAITING_PAY.value2Char()) {
                        orderBidDetail.setStatus(LendOrderBidStatusEnum.OUT_TIME.value2Char());
                        lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.OUT_TIME.value2Char(), 0l);

                        LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
                        lendOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.OUT_TIME.getValue());
                        Map<String, Object> lendOrderMap = new HashMap<String, Object>();
                        lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
                        lendOrderMap.put("orderState", lendOrder.getOrderState());
                        lendOrderService.update(lendOrderMap);
                    }
                }

                List<CreditorRights> creditorRightses = creditorRightsService.getByLoanApplicationId(loanApplication.getLoanApplicationId());
                for (CreditorRights creditorRights : creditorRightses) {
                    if (creditorRights.getChannelType() == ChannelTypeEnum.OFFLINE.value2Long()) {
                        creditorRights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.LOCKED.value2Char());
                        Map<String, Object> paraMap = new HashMap<String, Object>();
                        paraMap.put("creditorRightsId", creditorRights.getCreditorRightsId());
                        paraMap.put("rightsState", creditorRights.getRightsState());
                        creditorRightsService.update(paraMap);
                    }
                }
            }
        }
    }

    private void processLoanOrderForFailLoan(LendOrder loanOrder, LendOrderBidDetail lendOrderBidDetail, LoanPublish loanPublish, Date date, AccountValueChangedQueue queue, TimerExecLog timerExecLog) {
        //更新订单状态
        Map<String, Object> lendOrderMap = new HashMap<String, Object>();
        loanOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.FAIL.getValue());
        lendOrderMap.put("lendOrderId", loanOrder.getLendOrderId());
        lendOrderMap.put("orderState", loanOrder.getOrderState());
        lendOrderService.update(lendOrderMap);

        //解锁现金流
        UserAccount cashAccount = this.userAccountService.getCashAccount(loanOrder.getLendUserId());

        //处理所有出借订单使用的财富券
        List<PayOrderDetail> paymentOrderDetailList = payService.getPaymentOrderDetail(loanOrder.getLendOrderId());
        BigDecimal voucherPayValue = BigDecimal.ZERO;
        for (PayOrderDetail payOrderDetail : paymentOrderDetailList) {
            if (PayConstants.AmountType.VOUCHERS.getValue().equals(payOrderDetail.getAmountType())) {
                //此订单支付明细包含财富券
                List<Voucher> voucherList = voucherService.getVoucherList(payOrderDetail.getDetailId(), VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE, VoucherConstants.UsageScenario.LOAN);
                //财富券解冻
                Long[] voucherIdList = new Long[voucherList.size()];
                for (int i = 0; i < voucherList.size(); i++) {
                    //记录返还财富券列表
                    Voucher voucher = voucherList.get(i);
                    if (!VoucherConstants.VoucherStatus.FREEZE.getValue().equals(voucher.getStatus())) {
                        voucherIdList = null;
                        break;
                    }
                    voucherIdList[i] = voucher.getVoucherId();
                    voucherPayValue = voucherPayValue.add(voucher.getVoucherValue());
                }
                //财富券返还
                voucherService.backVoucher(payOrderDetail.getDetailId(), "因流标已返还", voucherIdList);
            }
        }

        AccountValueChanged changed = new AccountValueChanged(cashAccount.getAccId(), lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue), lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue),
                AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_FAIL_LOAN.getValue(),
                AccountConstants.AccountChangedTypeEnum.TIMER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(), timerExecLog.getLogId(),
                AccountConstants.OwnerTypeEnum.LOAN.getValue(), lendOrderBidDetail.getLoanApplicationId(), date,
                StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.FAILLOAN_UNFREEZE, loanPublish.getLoanTitle(), lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue)), true
        );
        queue.addAccountValueChanged(changed);
    }

    private void processFinanceOrderForFailLoan(LendOrder financeOrder, LendOrderBidDetail lendOrderBidDetail, LoanPublish loanPublish, Date date, AccountValueChangedQueue queue, TimerExecLog timerExecLog) {
        //更新订单的待理财金额
        Map<String, Object> lendOrderMap = new HashMap<String, Object>();
        financeOrder.setForLendBalance(financeOrder.getForLendBalance().add(lendOrderBidDetail.getBuyBalance()));
        lendOrderMap.put("forLendBalance", financeOrder.getForLendBalance());
        lendOrderMap.put("lendOrderId", financeOrder.getLendOrderId());
        lendOrderService.update(lendOrderMap);

        //解锁省心计划账户的相应金额
        UserAccount orderAccount = this.userAccountService.getUserAccountByAccId(financeOrder.getCustomerAccountId());

        AccountValueChanged changed = new AccountValueChanged(orderAccount.getAccId(), lendOrderBidDetail.getBuyBalance(), lendOrderBidDetail.getBuyBalance(),
                AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_FAIL_LOAN.getValue(),
                AccountConstants.AccountChangedTypeEnum.TIMER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(), timerExecLog.getLogId(),
                AccountConstants.OwnerTypeEnum.LOAN.getValue(), lendOrderBidDetail.getLoanApplicationId(), date,
                StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.FAILLOAN_UNFREEZE, loanPublish.getLoanTitle(), lendOrderBidDetail.getBuyBalance()), true);
        queue.addAccountValueChanged(changed);
    }

    /**
     * 判断借款申请是否流标（即在指定时间内投标金额未达到借款批复金额）
     * 如流标，借款申请状态为流标，查询所有投标明细，投标状态为未支付的，投标明细状态为超时，出借订单状态为超时
     * 投标状态为投标中的，投标明细状态为投标失败，出借订单状态为流标
     *
     * @param temp
     * @param date
     * @param timerExecLog
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void noticeFailLoan(LoanApplication temp, Date date, TimerExecLog timerExecLog) throws Exception {
        LoanApplication loanApplication = this.findLockById(temp.getLoanApplicationId());
        LoanPublish loanPublish = this.getLoanPublishByAppId(loanApplication.getLoanApplicationId());
        //校验-如果不在投标中状态
        if (!loanApplication.getApplicationState().equals(LoanApplicationStateEnum.BIDING.getValue()))
            return;

        //更新借款申请的状态
        loanApplication.setApplicationState(LoanApplicationStateEnum.FAILURE.getValue());
        loanApplication.setLendState(LoanAppLendAuditStatusEnums.UN_COMMIT.getValue());
        loanApplication.setCancelTime(date);
        Map map = new HashMap();
        map.put("loanApplicationId", loanApplication.getLoanApplicationId());
        map.put("applicationState", loanApplication.getApplicationState());
        map.put("lendState", loanApplication.getLendState());
        map.put("cancelTime", loanApplication.getCancelTime());
        myBatisDao.update("LOANAPPLICATION.updateByMap", map);

        AccountValueChangedQueue queue = new AccountValueChangedQueue();
        List<LendOrder> needSendSmsOrders = new ArrayList<LendOrder>();

        //遍历相关订单，处理订单状态
        List<LendOrderBidDetail> orderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(loanApplication.getLoanApplicationId());
        for (LendOrderBidDetail orderBidDetail : orderBidDetails) {
            LendOrder lendOrder = lendOrderService.findAndLockById(orderBidDetail.getLendOrderId());
            if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())
                    && lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.PAID.getValue())) {
                processLoanOrderForFailLoan(lendOrder, orderBidDetail, loanPublish, date, queue, timerExecLog);
                needSendSmsOrders.add(lendOrder);
                //流标后处理加息券和奖励信息
                //加息券部分：a.使用次数减1，未使用次数加1，状态--使用次数为0为未使用，如果是使用完的改为使用中，如果过期改为已过期
                //加息历史表：更改状态
                //加息券和奖励部分：加息-出借订单-中间表，将状态更改为无效
                handleRateUserInfo(lendOrder);
            } else if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
                processFinanceOrderForFailLoan(lendOrder, orderBidDetail, loanPublish, date, queue, timerExecLog);
            }

            //更新投标明细的状态
            orderBidDetail.setStatus(LendOrderBidStatusEnum.BIDFAILURE.value2Char());
            lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.OUT_TIME.value2Char(), 0l);
        }

        this.userAccountOperateService.execute(queue);

        for (LendOrder loanOrder : needSendSmsOrders) {
            //发送流标短信
            sendFailureMsg(loanOrder);
            try {
                List<CommitProfitVO> commitProfitList = commiProfitService
                        .getCommiProfitByLendOrderId(
                                loanOrder.getLendOrderId(),
                                loanOrder.getLendUserId());
                // 流标后对应的订单全部失效,更新佣金应收的金额
                logger.info("【流标】流标，更新佣金统计记录，佣金数据长度：" + commitProfitList.size());
                for (CommitProfitVO commi : commitProfitList) {
                    CommiProfit com = new CommiProfit();
                    com.setComiProId(commi.getComiProId());
                    com.setShoulProfit(commi.getFactProfit());
                    commiProfitService.updateCommiProfit(com);
                    logger.info("【流标】更新佣金统计记录，推荐人：" + com.getUserId()
                            + "，佣金统计ID：" + com.getComiProId() + "，佣金应还金额和实还金额："
                            + commi.getFactProfit());
                }
            } catch (Exception e) {
                logger.error("【流标】更新佣金统计记录异常，异常原因：", e);
            }
        }


    }

    /**
     * 处理流标后的加息信息
     */
    @Transactional
    public void handleRateUserInfo(LendOrder lendOrder) {
        Date now = new Date();
        //省心计划不能使用加息券
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
            return;
        }
        List<RateLendOrder> rateList = rateLendOrderService.findAllByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderStatusEnum.VALID.getValue());
        for (RateLendOrder rateOrder : rateList) {
            //加息券修改
            if (rateOrder.getRateType().equals(RateLendOrderTypeEnum.RATE_COUPON.getValue())) {
                // 更新加息-用户数据和状态
                RateUser rateUser = rateUserService.findByRateUserId(rateOrder
                        .getRateUserId());
                if (rateUser != null) {
                    rateUser.setUsedTimes(rateUser.getUsedTimes() - 1);
                    rateUser.setSurplusTimes(rateUser.getSurplusTimes() + 1);
                    rateUser = rateUser.setStatus(rateUser);
                    rateUserService.updateRateUser(rateUser);
                    // 更新加息-历史 状态
                    List<RateUsageHistory> historyList = rateUsageHistoryService
                            .findByParams(lendOrder.getLendOrderId(),
                                    rateUser.getRateUserId(),
                                    RateEnum.RateUsageHisStateEnum.VALID.getValue());
                    if (historyList != null && historyList.size() > 0) {
                        for (RateUsageHistory h : historyList) {
                            h.setUpdateTime(now);
                            h.setStatus(RateEnum.RateUsageHisStateEnum.UN_VALID.getValue());
                            rateUsageHistoryService.updateRateUsageHistory(h);
                        }
                    }
                }
                rateOrder.setStatus(RateEnum.RateLendOrderStatusEnum.UN_VALID.getValue());
                rateLendOrderService.updateRateLendOrder(rateOrder);
            } else {
                //投标奖励和活动修改
                rateOrder.setStatus(RateEnum.RateLendOrderStatusEnum.UN_VALID.getValue());
                rateLendOrderService.updateRateLendOrder(rateOrder);
            }
        }

    }

    /**
     * 发送流标短信
     *
     * @param lendOrder
     */
    private void sendFailureMsg(LendOrder lendOrder) {
        UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
        VelocityContext context = new VelocityContext();
        try {
            try {
                context.put("date", DateUtil.getDateLongMD(lendOrder.getBuyTime()));
            } catch (Exception e) {
                logger.error("生成流标短信失败", e);
            }
            context.put("amount", lendOrder.getBuyBalance());

            //投标失败后发送投标失败短信
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_LOANAPPLICATION_FAILURE_VM, context);
            smsService.sendMsg(user.getMobileNo(), content);
        } catch (Exception e) {
            logger.error("发送流标短信失败", e);
        }
    }

    // main
    @Override
    public Map<String, Object> saveUploadSnapshot(String loanApplicationId, MultipartFile file, String type, String msgName, String typeList, String rootPath, String isCode) throws IOException {
        if (loanApplicationId == null || "".equals(loanApplicationId))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("loanApplicationId", loanApplicationId);
        MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));//main
        Long userId = mainLoanApplication.getUserId();

        //String imgPath = request.getSession().getServletContext()
        // .getRealPath("/") + "resources/picture";
        String imgPath = temporaryPath;
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //新文件名字
        String newName = UUID.randomUUID().toString().replace("-", "") + suffix;
        String ThumbnailName = newName.substring(0, newName.lastIndexOf(".")) + "_100" + suffix;
        // 保存attachment 大图地址
        StringBuffer url = new StringBuffer();
        /*url.append(request.getScheme());
        url.append(("://"));
		url.append(request.getHeader("host"));*/
        url.append(imgPath.substring(imgPath.indexOf("/")));
        url.append("/" + newName);
        //小图地址
        StringBuffer thumbnailUrl = new StringBuffer();
        thumbnailUrl.append(imgPath.substring(imgPath.indexOf("/")));
        thumbnailUrl.append("/" + ThumbnailName);
        InputStream input = file.getInputStream();
        //上传图片
        new FileUtil().save(input, rootPath + imgPath, File.separator + newName);
        //生成缩略图
        ThumbnailGenerate generate = new ThumbnailGenerate(rootPath + imgPath + File.separator + newName);
        generate.resizeFix(152, 202);
        //获得显示位置
        int flagNum = getCustomerSeqNumByMainId(Long.valueOf(loanApplicationId), type);//main
        //保存
        CustomerUploadSnapshot cus = saveRelatedAccessories(userId, msgName, imgPath, url.toString(), type, CustomerUploadSnapshot.CUSTOMERUPLOADSNAPSHOT_NOTDELETED, (long) flagNum + 1, Long.valueOf(loanApplicationId), thumbnailUrl.toString(), isCode);// main

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", type);
        map.put("msgName", msgName);
        map.put("resultState", "success");
        map.put("typeList", typeList);
        map.put("cusId", cus.getSnapshotId());
        map.put("url", url);
        map.put("id", newName.substring(0, newName.lastIndexOf(".")));
        return map;
    }

    @Override
    public Pagination<LoanApplicationExtOne> getWaitLoanList(int pageNum,
                                                             int pageSize, String loanApplicationCode,
                                                             String loanApplicationName, String channel, String loanType,
                                                             String realName, String idCard, String mobileNo) {
        Pagination<LoanApplicationExtOne> re = new Pagination<LoanApplicationExtOne>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationCode", loanApplicationCode);
        params.put("loanApplicationName", loanApplicationName);
        params.put("channel", loanType);
        params.put("realName", realName);
        params.put("idCard", idCard);
        params.put("mobileNo", mobileNo);


        int totalCount = this.myBatisDao.count("getWaitLoanListPaging", params);
        List<LoanApplicationExtOne> loanApplicationInfo = this.myBatisDao.getListForPaging("getWaitLoanListPaging", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(loanApplicationInfo);
        return re;
    }

    //main
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void imgToFormal(Long mainLoanApplicationId, String rootPath) throws IOException {
        List<CustomerUploadSnapshotVO> cusvos = getCustomerUploadAttachmentByMainId(mainLoanApplicationId, AttachmentIsCode.NO_CODE.getValue());
        for (CustomerUploadSnapshotVO cusvo : cusvos) {
            Attachment atta = cusvo.getAttachment();
            String fileName = atta.getUrl().substring(atta.getUrl().lastIndexOf("/"));
            String thumbnailFileName = atta.getThumbnailUrl().substring(atta.getThumbnailUrl().lastIndexOf("/"));
            FileUtil fileUtil = new FileUtil();
            fileUtil.moveFile(rootPath + this.temporaryPath, fileName, rootPath + this.formalPath);
            fileUtil.moveFile(rootPath + this.temporaryPath, thumbnailFileName, rootPath + this.formalPath);
            fileUtil.deleteFile(rootPath + this.temporaryPath, fileName);
            fileUtil.deleteFile(rootPath + this.temporaryPath, thumbnailFileName);

            atta.setUrl(this.formalPath + fileName);
            atta.setThumbnailUrl(this.formalPath + thumbnailFileName);
            atta.setPhysicalAddress(this.formalPath);
            myBatisDao.update("ATTACHMENT.updateByPrimaryKeySelective", atta);
        }
    }

    /**
     * 计算期数
     *
     * @param timeLimitType   期限类型
     * @param timeLimit       期限值
     * @param toInterestPoint 返息周期类型
     * @return
     */

    public int getPeriods(char timeLimitType, int timeLimit, char toInterestPoint) {
        int periods = 1;

        if (toInterestPoint == LendProduct.TOINTERESTPOINT_WEEK.charAt(0)) {
            if (timeLimitType == DueTimeTypeEnum.MONTH.value2Char()) {
                periods = timeLimit * LendProduct.TIMELIMITTYPE_DAY_MONTH % LendProduct.TIMELIMITTYPE_DAY_WEEK == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK : timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK + 1;
            } else if (timeLimitType == DueTimeTypeEnum.DAY.value2Char()) {
                periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_WEEK == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK : timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK + 1;
            }
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_MONTH.charAt(0)) {
            if (timeLimitType == DueTimeTypeEnum.MONTH.value2Char()) {
                periods = timeLimit;
            } else if (timeLimitType == DueTimeTypeEnum.DAY.value2Char()) {
                periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_MONTH == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_MONTH : timeLimit / LendProduct.TIMELIMITTYPE_DAY_MONTH + 1;
            }
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_QUARTER.charAt(0)) {
            if (timeLimitType == DueTimeTypeEnum.MONTH.value2Char()) {
                periods = timeLimit % 3 == 0 ? timeLimit / 3 : timeLimit / 3 + 1;
            } else if (timeLimitType == DueTimeTypeEnum.DAY.value2Char()) {
                periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_QUARTER == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_QUARTER : timeLimit / LendProduct.TIMELIMITTYPE_DAY_QUARTER + 1;
            }
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_HALFYEAR.charAt(0)) {
            if (timeLimitType == DueTimeTypeEnum.MONTH.value2Char()) {
                periods = timeLimit % 6 == 0 ? timeLimit / 6 : timeLimit / 6 + 1;
            } else if (timeLimitType == DueTimeTypeEnum.DAY.value2Char()) {
                periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_HALFYEAR == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_HALFYEAR : timeLimit / LendProduct.TIMELIMITTYPE_DAY_HALFYEAR + 1;
            }
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_YEAR.charAt(0)) {
            if (timeLimitType == DueTimeTypeEnum.MONTH.value2Char()) {
                periods = timeLimit % 12 == 0 ? timeLimit / 12 : timeLimit / 12 + 1;
            } else if (timeLimitType == DueTimeTypeEnum.DAY.value2Char()) {
                periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_YEAR == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_YEAR : timeLimit / LendProduct.TIMELIMITTYPE_DAY_YEAR + 1;
            }
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_BEQUIT.charAt(0)) {
            periods = 1;
        }

        return periods;

    }

    /**
     * 计算每一期的应还金额
     *
     * @param lendOrder       出借合同
     * @param profitRate      费率
     * @param toInterestPoint 返息周期
     * @return
     */

    public BigDecimal getShouldReceiveInterest(LendOrder lendOrder, BigDecimal profitRate, char toInterestPoint) {
        BigDecimal balance = lendOrder.getBuyBalance();
        BigDecimal shouldReceiveInterest = BigDecimal.ZERO;
        if (toInterestPoint == LendProduct.TOINTERESTPOINT_WEEK.charAt(0)) {
            shouldReceiveInterest = balance.multiply(profitRate)
                    .divide(new BigDecimal(String.valueOf(LendProduct.TIMELIMITTYPE_DAY_YEAR)), 18, BigDecimal.ROUND_CEILING)
                    .multiply(new BigDecimal(LendProduct.TIMELIMITTYPE_DAY_WEEK));
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_MONTH.charAt(0)) {
            shouldReceiveInterest = balance.multiply(profitRate)
                    .divide(new BigDecimal("12"), 18, BigDecimal.ROUND_CEILING);
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_QUARTER.charAt(0)) {
            shouldReceiveInterest = balance.multiply(profitRate)
                    .divide(new BigDecimal("12"), 18, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("3"));
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_HALFYEAR.charAt(0)) {
            shouldReceiveInterest = balance.multiply(profitRate)
                    .divide(new BigDecimal("12"), 18, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("6"));
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_YEAR.charAt(0)) {
            shouldReceiveInterest = balance.multiply(profitRate)
                    .divide(new BigDecimal("12"), 18, BigDecimal.ROUND_CEILING);
        } else if (toInterestPoint == LendProduct.TOINTERESTPOINT_BEQUIT.charAt(0)) {
            if (lendOrder.getTimeLimitType().equals(DueTimeTypeEnum.DAY.getValue())) {
                shouldReceiveInterest = balance.multiply(profitRate)
                        .divide(new BigDecimal(String.valueOf(LendProduct.TIMELIMITTYPE_DAY_YEAR)), 18, BigDecimal.ROUND_CEILING)
                        .multiply(new BigDecimal(String.valueOf(lendOrder.getTimeLimit())));
            } else if (lendOrder.getTimeLimitType().equals(DueTimeTypeEnum.MONTH.getValue())) {
                shouldReceiveInterest = balance.multiply(profitRate)
                        .divide(new BigDecimal("12"), 18, BigDecimal.ROUND_CEILING)
                        .multiply(new BigDecimal(String.valueOf(lendOrder.getTimeLimit())));
            }
        }
        return shouldReceiveInterest;
    }

    @Override
    public List<LoanApplication> getLoanAppListByUserId(Long userId) {
        return myBatisDao.getList("LOANAPPLICATION.getLoanAppListByUserId", userId);
    }

    /**
     * 企业借款申请分页列表
     *
     * @param pageNo   页码
     * @param pageSize 页数
     * @param params   查询条件
     */
    @Override
    public Pagination<EnterpriseInfo> findAllEnterpriseLoanByPage(int pageNo, int pageSize, Map<String, Object> params) {
        Pagination<EnterpriseInfo> pagination = new Pagination<EnterpriseInfo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<EnterpriseInfo> enterpriseInfos = myBatisDao.getListForPaging("findAllEnterpriseLoanByPage", params, pageNo, pageSize);
        pagination.setRows(enterpriseInfos);
        pagination.setTotal(myBatisDao.count("findAllEnterpriseLoanByPage", params));
        return pagination;
    }

    @Override
    public void exportExcel(HttpServletResponse response, Map<String, Object> params) {
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        String[] title = {"类型", "标的编号", "借款名称", "标的期限", "借款人姓名", "身份证号", "手机号", "借款金额", "批复金额", "已投", "标的状态", "是否逾期", "创建时间", "发标时间", "满标时间", "放款日期", "最后一期还款日", "合同状态"};
        List<LoanApplicationExtOne> enterpriseInfos = myBatisDao.getList("findAllEnterpriseLoanByPage", params);
        DecimalFormat df = new DecimalFormat("0.00");
        for (LoanApplicationExtOne le : enterpriseInfos) {
            Map<String, Object> map = new HashMap<String, Object>();
            String loanT = "";
            switch (le.getLoanType()) {
                case "2":
                    loanT = "企业车贷";
                    break;
                case "3":
                    loanT = "企业信贷";
                    break;
                case "4":
                    loanT = "企业保理";
                    break;
                case "5":
                    loanT = "企业基金";
                    break;
                case "6":
                    loanT = "企业标";
                    break;
            }
            map.put(title[0], loanT);
            map.put(title[1], le.getLoanApplicationCode());
            map.put(title[2], le.getLoanApplicationName());
            map.put(title[3], le.getDurationTime());
            map.put(title[4], le.getUserRealName());
            map.put(title[5], le.getIdCard());
            map.put(title[6], le.getMobileNo());
            map.put(title[7], df.format(le.getLoanBalance()));
            map.put(title[8], df.format(le.getConfirmBalance()));
            String hc = "0.00";
            switch (le.getApplicationState()) {
                case "0":
                case "1":
                case "2":
                case "9":
                case "A":
                    hc = "0.00";
                    break;
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                    hc = df.format(le.getConfirmBalance());
                    break;
                case "3":
                    hc = df.format(le.getHaveCast());
                    break;
            }
            map.put(title[9], hc);
            String as = "";
            switch (le.getApplicationState()) {
                case "0":
                    as = "草稿";
                    break;
                case "1":
                    as = "风控审核中";
                    break;
                case "2":
                    as = "发标审核中";
                    break;
                case "3":
                    as = "投标中";
                    break;
                case "4":
                    as = "放款审核中";
                    break;
                case "5":
                    as = "待放款";
                    break;
                case "6":
                    as = "还款中";
                    break;
                case "7":
                    as = "已结清";
                    break;
                case "8":
                    as = "提前还贷";
                    break;
                case "9":
                    as = "取消";
                    break;
                case "A":
                    as = "流标";
                    break;
            }
            map.put(title[10], as);
            map.put(title[11], le.getIsDelay() == 0 ? "否" : "是");
            try {
                map.put(title[12], DateUtil.getPlusTime(le.getCreateTime()));
                map.put(title[13], DateUtil.getPlusTime(le.getPublishTime()));
                map.put(title[14], DateUtil.getPlusTime(le.getFullTime()));
                map.put(title[15], DateUtil.getPlusTime(le.getPaymentDate()));
                map.put(title[16], DateUtil.getPlusTime(le.getOpertionDate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put(title[17], le.getAgreementStatus());
            dataMap.add(map);
        }
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "企业借款列表";
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(title, dataMap, fileName);
            wb.write(os);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Pagination<LoanApplicationVO> getLoanApplicationByUserId(int pageNo, int pageSize, LoanApplicationVO loanApplicationVO, Map<String, Object> customParams) {
        Pagination<LoanApplicationVO> re = new Pagination<LoanApplicationVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationVO", loanApplicationVO);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getLoanApplicationByUserId_paging", params);
        List<LoanApplicationVO> uah = this.myBatisDao.getListForPaging("getLoanApplicationByUserId_paging", params, pageNo, pageSize);
        organizationLoanApplication(uah);
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    private void organizationLoanApplication(List<LoanApplicationVO> uah) {
        for (LoanApplicationVO vo : uah) {
            Long loanApplicationId = vo.getLoanApplicationId();
            List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationId, ChannelTypeEnum.ONLINE);
            int allCount = repaymentPlanList.size();
            int complateCount = allCount;
            for (RepaymentPlan plan : repaymentPlanList) {
                if (plan.getPlanState() == RepaymentPlanStateEnum.UNCOMPLETE.value2Char()
                        || plan.getPlanState() == RepaymentPlanStateEnum.PART.value2Char()
                        || plan.getPlanState() == RepaymentPlanStateEnum.DEFAULT.value2Char()) {
                    complateCount = complateCount - 1;
                }
            }
            if (complateCount == allCount) {
                vo.setCurrentRepaymentCount((complateCount) + "/" + allCount);
            } else {
                vo.setCurrentRepaymentCount((complateCount + 1) + "/" + allCount);
            }
            //未还本金
            BigDecimal repaymentCapital = repaymentPlanService.getRepaymentCapitalByLoanApplicationId(loanApplicationId);
            vo.setShouldCapital(repaymentCapital);
            //待还利息
            BigDecimal replaymentInterest = repaymentPlanService.getRepaymentInterestByLoanApplicationId(loanApplicationId);
            vo.setShouldInterest(replaymentInterest);
            //待缴费用
            BigDecimal loanFeeNopaied = loanFeesDetailService.getLoanFeeNoPaied(vo.getUserId());
            vo.setShouldFee(loanFeeNopaied);
            //待还罚息
            BigDecimal interestPaid = defaultInterestDetailService.getDefaultInterestByUserId(vo.getUserId());
            vo.setShouldFaxi(interestPaid);
            vo.setShouldAll(repaymentCapital.add(replaymentInterest).add(loanFeeNopaied).add(interestPaid));

            BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(vo.getOriginalUserId());
            if (bondSourceUser != null) {
                vo.setBondUserRealName(bondSourceUser.getBondName());
                UserInfo user = userInfoService.getUserByUserId(bondSourceUser.getUserId());
                vo.setBondUserLoginName(user.getLoginName());
            }
        }
    }

    @Override
    public Pagination<LoanApplicationVO> getLoanApplicationByEnterpriseId(int pageNo, int pageSize, LoanApplicationVO loanApplicationVO, Map<String, Object> customParams) {
        Pagination<LoanApplicationVO> re = new Pagination<LoanApplicationVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationVO", loanApplicationVO);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getLoanApplicationByEnterpriseId_paging", params);
        List<LoanApplicationVO> uah = this.myBatisDao.getListForPaging("getLoanApplicationByEnterpriseId_paging", params, pageNo, pageSize);
        organizationLoanApplication(uah);
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    public Long getVoucherTen() {
        return voucherTen;
    }

    public void setVoucherTen(Long voucherTen) {
        this.voucherTen = voucherTen;
    }

    public BigDecimal getTowardsIncome() {
        return towardsIncome;
    }

    public void setTowardsIncome(BigDecimal towardsIncome) {
        this.towardsIncome = towardsIncome;
    }


    /**
     * 生成债权标借款合同
     */
    public void createBidAgreementForLoan(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor";
        String pdf_name = "借款及服务协议(债权)";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());

        // 借款发标表信息
        LoanPublish loanPublish = loanPublishService.findById(loan.getLoanApplicationId());
        if (null != loanPublish.getCompanyId()) {// 如果有担保公司
            htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor_guarantee";
            pdf_name = "借款及服务协议(债权)(含保证人)";
            // 担保公司信息
            GuaranteeCompany guaranteeCompany = guaranteeCompanyService.getGuaranteeCompanyByCompanyId(loanPublish.getCompanyId());
            jsonMap.put("guaranteeCompany", guaranteeCompany);
        }

        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人、身份证号
        jsonMap.put("loanRealName", loanUserInfoVO.getRealName());
        jsonMap.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款金额（大写+小写）
        if (null != creditorRights.getBuyPrice()) {
            jsonMap.put("resultBalanceBig", BigDecimalUtil.change(new Double(creditorRights.getBuyPrice().toString())));
        }
        jsonMap.put("resultBalance", creditorRights.getBuyPrice());

        // 借款期限（几个月、开始时间、结束时间）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }

        if (creditorRights.getFromWhere() == CreditorRightsFromWhereEnum.BUY.value2Char()) {
            jsonMap.put("dueTime", loanProduct.getDueTime());
        } else if (creditorRights.getFromWhere() == CreditorRightsFromWhereEnum.TURN.value2Char()) {
            jsonMap.put("dueTime", paramsMap.get("dueTime"));
        }

        // 还款方式
        jsonMap.put("repaymentType", loanProduct.getRepaymentType());

        // 借款利率
        jsonMap.put("annualRate", loan.getAnnualRate());

        // 借款用途
        if (null != loan.getLoanUseage() && !"".equals(loan.getLoanUseage())) {
            ConstantDefine constantDefine = new ConstantDefine();
            constantDefine.setConstantValue(loan.getLoanUseage());
            constantDefine.setConstantTypeCode("loanUseage");
            constantDefine.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(constantDefine);
            if (null != define) {
                jsonMap.put("loanUseage", define.getConstantName());
            }
        }

        // 借款人    通信地址：
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 邮箱地址：
        jsonMap.put("loanEmail", loanUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("loanMobileNo", loanUserInfoVO.getMobileNo());

        CustomerCard loanCustomerCard = customerCardService.getCustomerBindCardByUserId(loanUserInfoVO.getUserId(), PayChannel.LL);
        if (null != loanCustomerCard) {
            // 银行账号：
            jsonMap.put("loanCardCode", loanCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(loanCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("loanBankName", define.getConstantName());
            }
        }

        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        CustomerCard lendCustomerCard = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayChannel.LL);
        if (null != lendCustomerCard) {
            // 银行账号：
            jsonMap.put("lendCardCode", lendCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("lendBankName", define.getConstantName());
            }
        }

        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap.put("repaymentPlanList", repaymentPlanList);

        // 每月还款日、每月还款金额(还款明细表 第一个月的)
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            RightsRepaymentDetail plan = repaymentPlanList.get(0);
            jsonMap.put("shouldBalance2", BigDecimalUtil.change(new Double(plan.getShouldBalance2().toString())));
            jsonMap.put("repaymentDay", DateUtil.getDay(plan.getRepaymentDayPlanned()));
        }

        Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, pdf_name, pair);
    }


    /**
     * 生成债权标转让合同
     */
    public void createBidAgreementForLoanAssignment(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws IOException, DocumentException {

        Map<String, Object> jsonMap_assignment = new HashMap<String, Object>();
        String htmlUrl_assignment = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor_assignment";
        String pdf_assignment_name = "债权转让及受让协议";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());

        // 借款发标表信息
        LoanPublish loanPublish = loanPublishService.findById(loan.getLoanApplicationId());
        if (null != loanPublish.getCompanyId()) {// 如果有担保公司
            htmlUrl_assignment = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_creditor_assignment_guarantee";
            pdf_assignment_name = "债权转让及受让协议(含保证人)";
            // 担保公司信息
            GuaranteeCompany guaranteeCompany = guaranteeCompanyService.getGuaranteeCompanyByCompanyId(loanPublish.getCompanyId());
            jsonMap_assignment.put("guaranteeCompany", guaranteeCompany);
        }

        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);


        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------

        // 合同编号
        jsonMap_assignment.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap_assignment.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }


        // 受让人（新债权人=出借人）：
        UserInfoVO lendUserInfoVO_ass = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 联系电话：
        jsonMap_assignment.put("lendMobileNo", lendUserInfoVO_ass.getMobileNo());
        // 邮箱地址：
        jsonMap_assignment.put("lendEmail", lendUserInfoVO_ass.getEmail());
        // 姓名
        jsonMap_assignment.put("lendRealName", lendUserInfoVO_ass.getRealName());

        // 借款ID
        jsonMap_assignment.put("loanApplicationCode", loan.getLoanApplicationCode());
        // 借款人姓名
        jsonMap_assignment.put("loanRealName", loanUserInfoVO.getRealName());
        // 借款人身份证号
        jsonMap_assignment.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款本金数额
        jsonMap_assignment.put("resultBalance", loan.getLoanBalance());

        // 还款明细表
        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap_assignment.put("repaymentPlanList", repaymentPlanList);

        // 原借款期限
        jsonMap_assignment.put("dueTime", loanProduct.getDueTime());

        UserInfoVO sourceUserInfoVO = null;
        if (creditorRights.getFromWhere() == CreditorRightsFromWhereEnum.BUY.value2Char()) {
            // 还款期数
            jsonMap_assignment.put("cycleCount", loanProduct.getDueTime());
            // 转让人（原债权人）：
            BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(loan.getOriginalUserId());
            sourceUserInfoVO = userInfoService.getUserExtByUserId(bondSourceUser.getUserId());
            // 起息日
            if (null != loan.getPaymentDate()) {
                jsonMap_assignment.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
            }
        } else if (creditorRights.getFromWhere() == CreditorRightsFromWhereEnum.TURN.value2Char()) {
            // 还款期数
            jsonMap_assignment.put("cycleCount", repaymentPlanList.size());
            CreditorRightsTransferApplication creTranApp = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(creditorRights.getLendOrderId());
            sourceUserInfoVO = userInfoService.getUserExtByUserId(creTranApp.getApplyUserId());
            // 起息日
            jsonMap_assignment.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getLendTime()));
        }

        // 联系电话：
        jsonMap_assignment.put("sourceMobileNo", sourceUserInfoVO.getMobileNo());
        // 邮箱地址：
        jsonMap_assignment.put("sourceEmail", sourceUserInfoVO.getEmail());
        // 姓名
        jsonMap_assignment.put("sourceRealName", sourceUserInfoVO.getRealName());

        // 受让债权明细
        LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());

        // 受让本金-投资额
        jsonMap_assignment.put("buyBalance", lendOrder.getBuyBalance());
        if (null != lendOrder.getBuyBalance()) {
            jsonMap_assignment.put("buyBalanceBig", BigDecimalUtil.change(new Double(lendOrder.getBuyBalance().toString())));
        }

        // 借款利息
        jsonMap_assignment.put("annualRate", loan.getAnnualRate());

        // 受让日期-投标日
        if (null != creditorRights.getLendTime()) {
            jsonMap_assignment.put("lendTime", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getLendTime()));
        }

        // 到期日期
        if (null != loan.getLastRepaymentDate()) {
            jsonMap_assignment.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }

        // 还款方式
        jsonMap_assignment.put("repaymentType", loanProduct.getRepaymentTypeStr(loanProduct.getRepaymentType()));

        // 受让人缴纳服务费
        jsonMap_assignment.put("feesItems", lendLoanBindings);

        // 第五条 受让人收益账户:帐户名、账号、开户名
        CustomerCard lendCustomerCard_ass = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayChannel.LL);
        if (null != lendCustomerCard_ass) {
            // 银行账号：
            jsonMap_assignment.put("lendCardCode", lendCustomerCard_ass.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard_ass.getBankCode());
            if (null != define) {
                jsonMap_assignment.put("lendBankName", define.getConstantName());
            }
        }

        Pair pair_assignment = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap_assignment));

        //生成pdf
        GeneratePDF.create(htmlUrl_assignment, storageFolder, pdf_assignment_name, pair_assignment);
    }

    @Override
    public String insertOrUpdateOLoanPublish(String opassword, Long loanApplicationId, String excel, String object) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLoanApplicationType(Long loanApplicationNo) {
        // TODO Auto-generated method stub
        int object = myBatisDao.get("LOAN_ORIENTATION.selectByPrimaryKeyoType", loanApplicationNo);
        return object;
    }

    @Override
    public String getLoanApplicationPass(Long loanApplicationId) {
        // TODO Auto-generated method stub
        String object = myBatisDao.get("LOAN_ORIENTATION.selectByPrimaryKeyPass", loanApplicationId);
        return object;
    }

    @Override
    public int getLoanApplicationOrientById(Long loanApplicationId) {
        return myBatisDao.get("LOAN_ORIENTATION.selectByPrimaryKeyCount", loanApplicationId);

    }

    @Override
    public List<BigDecimal> getLendRateTypes() {
        // TODO Auto-generated method stub
        return myBatisDao.getList("LEND_PRODUCT_PUBLISH.selectProfitByPrimaryKeyGroup");
    }

    /**
     * 根据条件查询所有满标的借款申请
     */
    @Override
    public List<LoanApplication> findAllFullLoanApplication(Map<String, Object> params) {
        return myBatisDao.getList("LOANAPPLICATION.findAllFullLoanApplication", params);
    }

    @Override
    public void createFinanceAgreement(List<LendOrder> lendOrderList) {
        Date createTime = new Date();
        for (LendOrder lendOrder : lendOrderList) {
            try {
                createFinanceAgreementAndFile(lendOrder, createTime);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("生成省心计划合同异常，", e);
            }
        }
    }

    @Transactional
    @Override
    public void createFinanceAgreementAndFile(LendOrder lendOrder, Date createTime) throws Exception {
        createFinanceAgreement(lendOrder, createTime);
        createFinanceAgreementPDF(lendOrder);
    }

    public void createFinanceAgreement(LendOrder lendOrder, Date createTime) {
        String storageFolder = PropertiesUtils.getInstance().get(
                "AGREEMENT_PATH");
        String href = PropertiesUtils.getInstance().get("AGREEMENT_VISIT_PATH");

        // 创建合同信息(状态为未生成)
        //使以前的相关协议失效
        financePlanAgreementService.cancelAllAvalidAgreement(lendOrder.getLendOrderId(),
                AgreementEnum.FinancePlanAgreementStatusEnum.DISABLED_AGREEMENT.value2Char());
        //根据版本号倒叙排列
        List<FinancePlanAgreement> financeAgrees = financePlanAgreementService.findByLendOrderId(lendOrder.getLendOrderId());
        String fileName = createAgreementCode();
        String agreementCode = fileName;
        Integer version = 1;
        if (financeAgrees != null && financeAgrees.size() > 0) {
            version = financeAgrees.get(0).getVersion() + 1;
            fileName = financeAgrees.get(0).getFinanceAgreementCode() + "-" + version;
            agreementCode = financeAgrees.get(0).getFinanceAgreementCode();
        }
        FinancePlanAgreement agreement = new FinancePlanAgreement();
        agreement.setFinanceAgreementName(fileName);
        agreement.setFinanceAgreementCode(agreementCode);
        agreement.setCreateTime(createTime);
        agreement.setFinanceAgreementStatus(AgreementEnum.FinancePlanAgreementStatusEnum.UNCREATE_AGREEMENT.value2Char());
        agreement.setLendOrderId(lendOrder.getLendOrderId());
        agreement.setStorgePath(storageFolder + fileName + ".zip");
        agreement.setHref(href + fileName + ".zip");
        agreement.setFinanceAgreementId(null);
        agreement.setVersion(version);
        financePlanAgreementService.insert(agreement);
    }

    public void createFinanceAgreementPDF(LendOrder lendOrder) throws IOException, DocumentException {
        FinancePlanAgreement oldAgreement = financePlanAgreementService.findAvalidByLendOrderId(lendOrder.getLendOrderId(),
                AgreementEnum.FinancePlanAgreementStatusEnum.UNCREATE_AGREEMENT.value2Char());
        if (oldAgreement == null) {
            throw new SystemException("缺少未生成状态的合同信息", QueryErrorCode.ERROR_NULL);
        } else {

            createFinanceAgreementFile(lendOrder, oldAgreement);

            oldAgreement.setFinanceAgreementStatus(AgreementEnum.FinancePlanAgreementStatusEnum.AVALID_AGREEMENT.value2Char());
        }
        financePlanAgreementService.update(oldAgreement);
    }

    private void createFinanceAgreementFile(LendOrder lendOrder,
                                            FinancePlanAgreement oldAgreement) throws IOException, DocumentException {
        String agreementCode = oldAgreement.getFinanceAgreementCode() + "-" + oldAgreement.getVersion();
        String storageFolder = PropertiesUtils.getInstance().get(
                "AGREEMENT_PATH")
                + oldAgreement.getFinanceAgreementName();
        FileUtil.mkDirs(storageFolder);
        Map<String, Object> jsonMap_assignment = new HashMap<String, Object>();
        String htmlUrl_assignment = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/permission_financeplan";
        String pdf_assignment_name = "省心计划授权委托书";

        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(lendOrder.getLendUserId());
        jsonMap_assignment.put("realName", lendUserInfoVO.getRealName());
        jsonMap_assignment.put("idCard", lendUserInfoVO.getIdCard());
        jsonMap_assignment.put("mobileNo", lendUserInfoVO.getMobileNo());
        jsonMap_assignment.put("detail", lendUserInfoVO.getDetail());
        jsonMap_assignment.put("userId", lendOrder.getLendUserId().toString());
        jsonMap_assignment.put("loginName", lendUserInfoVO.getLoginName());
        jsonMap_assignment.put("limit", lendOrder.getTimeLimit());
        jsonMap_assignment.put("profitReturnConfig", lendOrder.getProfitReturnConfig());


        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------

        // 合同编号
        jsonMap_assignment.put("agreementCode", agreementCode);

        // 年、月、日
        if (null != lendOrder.getAgreementStartDate()) {
            jsonMap_assignment.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(lendOrder.getAgreementStartDate()));
        }


        // 受让本金-投资额
        jsonMap_assignment.put("buyBalance", lendOrder.getBuyBalance());
        if (null != lendOrder.getBuyBalance()) {
            jsonMap_assignment.put("buyBalanceBig", BigDecimalUtil.change(new Double(lendOrder.getBuyBalance().toString())));
        }

        Pair pair_assignment = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap_assignment));

        //生成pdf
        GeneratePDF.createForFinance(htmlUrl_assignment, storageFolder, pdf_assignment_name, pair_assignment);

        FileUtil.zipFiles(
                new File(storageFolder).listFiles(),
                PropertiesUtils.getInstance().get("AGREEMENT_PATH"),
                agreementCode);
        FileUtil.deleteDirectory(storageFolder);
    }

    /**
     * 初次生成合同时使用的方法
     */
    private String createAgreementCode() {
//      return fromWhere + "_" + loanApplicationCode + "_" + lendOrderCode + "_" + StringUtils.getRadomStr(6);
        //todo 算法规则待定
        return getAgreementCodeByTypeNew(AreementCodeTypeEnum.FINANCE_PLAN_PERMISSION.getValue());
    }

    @Override
    public List<LoanApplication> selectLoanByAuto(Long mainLoanApplicationId) {
        return myBatisDao.getList("LOANAPPLICATION.selectLoanByAuto", mainLoanApplicationId);
    }

    @Override
    public List<LoanApplication> selectLoanByBid(Long mainLoanApplicationId) {
        return myBatisDao.getList("LOANAPPLICATION.selectLoanByBid", mainLoanApplicationId);
    }


    /**
     * 【手动流标】
     * 判断借款申请是否流标（无时间限制）
     * 如流标，借款申请状态为流标，查询所有投标明细，投标状态为未支付的，投标明细状态为超时，出借订单状态为超时
     * 投标状态为投标中的，投标明细状态为投标失败，出借订单状态为流标
     *
     * @param temp
     * @param now
     * @param logDesc
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doNoticeFailLoan(LoanApplication temp, Date now, String logDesc) throws Exception {
        LoanApplicationLog loanApplicationLog = addLoanApplicationLog(LoanApplicationLogTypeNameEnum.FAILBID, now, logDesc);
        LoanApplication loanApplication = this.findLockById(temp.getLoanApplicationId());
        LoanPublish loanPublish = this.getLoanPublishByAppId(loanApplication.getLoanApplicationId());
        //校验-如果不在投标中状态
        if (!loanApplication.getApplicationState().equals(LoanApplicationStateEnum.BIDING.getValue())) {
            return;
        }

        //更新借款申请的状态
        loanApplication.setApplicationState(LoanApplicationStateEnum.FAILURE.getValue());
        loanApplication.setLendState(LoanAppLendAuditStatusEnums.UN_COMMIT.getValue());
        loanApplication.setCancelTime(now);
        Map map = new HashMap();
        map.put("loanApplicationId", loanApplication.getLoanApplicationId());
        map.put("applicationState", loanApplication.getApplicationState());
        map.put("lendState", loanApplication.getLendState());
        map.put("cancelTime", loanApplication.getCancelTime());
        myBatisDao.update("LOANAPPLICATION.updateByMap", map);

        AccountValueChangedQueue queue = new AccountValueChangedQueue();
        List<LendOrder> needSendSmsOrders = new ArrayList<LendOrder>();

        //遍历相关订单，处理订单状态
        List<LendOrderBidDetail> orderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(loanApplication.getLoanApplicationId());
        for (LendOrderBidDetail orderBidDetail : orderBidDetails) {
            LendOrder lendOrder = lendOrderService.findAndLockById(orderBidDetail.getLendOrderId());
            if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())
                    && lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.PAID.getValue())) {
                processLoanOrderForFailLoan(lendOrder, orderBidDetail, loanPublish, now, queue, loanApplicationLog);
                //省心计划的子标不发流标短信
                if (lendOrder.getLendOrderPId() == null) {
                    needSendSmsOrders.add(lendOrder);
                }
                //流标后处理加息券和奖励信息
                //加息券部分：a.使用次数减1，未使用次数加1，状态--使用次数为0为未使用，如果是使用完的改为使用中，如果过期改为已过期
                //加息历史表：更改状态
                //加息券和奖励部分：加息-出借订单-中间表，将状态更改为无效
                handleRateUserInfo(lendOrder);
            } else if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                    && lendOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.HASPAID.getValue())) {
                processFinanceOrderForFailLoan(lendOrder, orderBidDetail, loanPublish, now, queue, loanApplicationLog);
            }

            //更新投标明细的状态
            orderBidDetail.setStatus(LendOrderBidStatusEnum.BIDFAILURE.value2Char());
            lendOrderBidDetailService.updateStatus(orderBidDetail.getDetailId(), LendOrderBidStatusEnum.OUT_TIME.value2Char(), 0l);
        }

        this.userAccountOperateService.execute(queue);
        for (LendOrder loanOrder : needSendSmsOrders) {
            //发送流标短信
            sendFailureMsg(loanOrder);
            try {
                List<CommitProfitVO> commitProfitList = commiProfitService
                        .getCommiProfitByLendOrderId(
                                loanOrder.getLendOrderId(),
                                loanOrder.getLendUserId());
                // 流标后对应的订单全部失效,更新佣金应收的金额
                logger.info("【流标】流标，更新佣金统计记录，佣金数据长度：" + commitProfitList.size());
                for (CommitProfitVO commi : commitProfitList) {
                    CommiProfit com = new CommiProfit();
                    com.setComiProId(commi.getComiProId());
                    com.setShoulProfit(commi.getFactProfit());
                    commiProfitService.updateCommiProfit(com);
                    logger.info("【流标】更新佣金统计记录，推荐人：" + com.getUserId()
                            + "，佣金统计ID：" + com.getComiProId() + "，佣金应还金额和实还金额："
                            + commi.getFactProfit());
                }
            } catch (Exception e) {
                logger.error("【流标】更新佣金统计记录异常，异常原因：", e);
            }
        }
    }

    @Transactional
    public LoanApplicationLog addLoanApplicationLog(
            LoanApplicationLogTypeNameEnum failbid, Date now, String logDesc) {
        LoanApplicationLog loanLog = new LoanApplicationLog();
        loanLog.setExecTime(now);
        loanLog.setTypeName(failbid.getDesc());
        loanLog.setLogDesc(logDesc);
        myBatisDao.insert("LOAN_APPLICATION_LOG.insert", loanLog);
        return loanLog;
    }

    private void processFinanceOrderForFailLoan(LendOrder financeOrder,
                                                LendOrderBidDetail lendOrderBidDetail, LoanPublish loanPublish,
                                                Date date, AccountValueChangedQueue queue,
                                                LoanApplicationLog loanApplicationLog) {
        // 更新订单的待理财金额
        Map<String, Object> lendOrderMap = new HashMap<String, Object>();
        financeOrder.setForLendBalance(financeOrder.getForLendBalance().add(
                lendOrderBidDetail.getBuyBalance()));
        lendOrderMap.put("forLendBalance", financeOrder.getForLendBalance());
        lendOrderMap.put("lendOrderId", financeOrder.getLendOrderId());
        lendOrderService.update(lendOrderMap);

        // 解锁现金流
        UserAccount orderAccount = null;
        if (financeOrder.getLendOrderPId() != null) {
            orderAccount = this.userAccountService.getUserAccountByAccId(financeOrder.getCustomerAccountId());
        } else {
            orderAccount = this.userAccountService.getCashAccount(financeOrder.getLendUserId());
        }

        AccountValueChanged changed = new AccountValueChanged(
                orderAccount.getAccId(),
                lendOrderBidDetail.getBuyBalance(),
                lendOrderBidDetail.getBuyBalance(),
                AccountConstants.AccountOperateEnum.UNFREEZE.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_FAIL_LOAN.getValue(),
                AccountConstants.AccountChangedTypeEnum.TIMER.getValue(),
                AccountConstants.VisiableEnum.DISPLAY.getValue(),
                loanApplicationLog.getLogId(),
                AccountConstants.OwnerTypeEnum.LOAN.getValue(),
                lendOrderBidDetail.getLoanApplicationId(),
                date,
                StringUtils
                        .t2s(DescTemplate.desc.AccountChanngedDesc.FAILLOAN_UNFREEZE,
                                loanPublish.getLoanTitle(),
                                lendOrderBidDetail.getBuyBalance()), true);
        queue.addAccountValueChanged(changed);
    }

    private void processLoanOrderForFailLoan(LendOrder loanOrder,
                                             LendOrderBidDetail lendOrderBidDetail, LoanPublish loanPublish,
                                             Date date, AccountValueChangedQueue queue,
                                             LoanApplicationLog loanApplicationLog) {

        // 更新订单状态
        Map<String, Object> lendOrderMap = new HashMap<String, Object>();
        loanOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.FAIL
                .getValue());
        lendOrderMap.put("lendOrderId", loanOrder.getLendOrderId());
        lendOrderMap.put("orderState", loanOrder.getOrderState());
        lendOrderService.update(lendOrderMap);

        // 解锁现金流
        UserAccount cashAccount = null;
        if (loanOrder.getLendOrderPId() != null) {
            cashAccount = this.userAccountService.getUserAccountByAccId(loanOrder.getCustomerAccountId());
        } else {
            cashAccount = this.userAccountService.getCashAccount(loanOrder.getLendUserId());
        }

        // 处理所有出借订单使用的财富券
        List<PayOrderDetail> paymentOrderDetailList = payService
                .getPaymentOrderDetail(loanOrder.getLendOrderId());
        BigDecimal voucherPayValue = BigDecimal.ZERO;
        for (PayOrderDetail payOrderDetail : paymentOrderDetailList) {
            if (PayConstants.AmountType.VOUCHERS.getValue().equals(
                    payOrderDetail.getAmountType())) {
                // 此订单支付明细包含财富券
                List<Voucher> voucherList = voucherService.getVoucherList(
                        payOrderDetail.getDetailId(),
                        VoucherConstants.UsageScenario.WHOLE,
                        VoucherConstants.UsageScenario.FINANCE,
                        VoucherConstants.UsageScenario.LOAN);
                // 财富券解冻
                Long[] voucherIdList = new Long[voucherList.size()];
                for (int i = 0; i < voucherList.size(); i++) {
                    // 记录返还财富券列表
                    Voucher voucher = voucherList.get(i);
                    if (!VoucherConstants.VoucherStatus.FREEZE.getValue()
                            .equals(voucher.getStatus())) {
                        voucherIdList = null;
                        break;
                    }
                    voucherIdList[i] = voucher.getVoucherId();
                    voucherPayValue = voucherPayValue.add(voucher
                            .getVoucherValue());
                }
                // 财富券返还
                voucherService.backVoucher(payOrderDetail.getDetailId(),
                        "因流标已返还", voucherIdList);
            }
        }

        AccountValueChanged changed = new AccountValueChanged(
                cashAccount.getAccId(),
                lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue),
                lendOrderBidDetail.getBuyBalance().subtract(voucherPayValue),
                AccountConstants.AccountOperateEnum.UNFREEZE.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_FAIL_LOAN.getValue(),
                AccountConstants.AccountChangedTypeEnum.TIMER.getValue(),
                AccountConstants.VisiableEnum.DISPLAY.getValue(),
                loanApplicationLog.getLogId(),
                AccountConstants.OwnerTypeEnum.LOAN.getValue(),
                lendOrderBidDetail.getLoanApplicationId(),
                date,
                StringUtils
                        .t2s(DescTemplate.desc.AccountChanngedDesc.FAILLOAN_UNFREEZE,
                                loanPublish.getLoanTitle(),
                                lendOrderBidDetail.getBuyBalance().subtract(
                                        voucherPayValue)), true);
        queue.addAccountValueChanged(changed);

    }


    @Override
    public String countOtypeByLoanApplicationId(Long loanApplicationId) {
        // TODO Auto-generated method stub
        return myBatisDao.get("getCountOtypeByLoanApplicationId", loanApplicationId);
    }


    /**
     * 新手标
     */
    @Override
    public Pagination<LoanApplicationListVO> getLoanSpecialApplicationPaging(
            int pageNo, int pageSize, LoanApplicationListVO loanVo,
            Map<String, Object> paramsMap) {
        // TODO Auto-generated method stub
        Pagination<LoanApplicationListVO> re = new Pagination<LoanApplicationListVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanVo", loanVo);
        params.put("preheatTime", new Date());

        int totalCount = this.myBatisDao.count(
                "getLoanApplicationSpecialPaging", params);
        List<LoanApplicationListVO> uah = this.myBatisDao.getListForPaging(
                "getLoanApplicationSpecialPaging", params, pageNo, pageSize);
        re.setTotal(totalCount);
        re.setRows(uah);
        return re;
    }

    @Override
    public List<BigDecimal> getLoanRateTypesByNewUser(
            LoanApplicationStateEnum... loanApplicationStateEnum) {
        List<String> statusList = new ArrayList<String>();
        if (loanApplicationStateEnum == null || loanApplicationStateEnum.length == 0) {
            statusList = null;
        } else {
            for (LoanApplicationStateEnum applicationStateEnum : loanApplicationStateEnum) {
                statusList.add(applicationStateEnum.getValue());
            }
        }


        Map<String, Object> param = new HashMap<String, Object>();
        param.put("statusList", statusList);
        List<BigDecimal> reuslt = myBatisDao.getList("getLoanRateTypesByNewUser", param);
        return reuslt;
    }


    private void createBidAgreementForCashLoan(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws Exception, IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_loan_cash";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());
        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人、身份证号
        jsonMap.put("loanRealName", loanUserInfoVO.getRealName());
        jsonMap.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款金额（大写+小写）
        if (null != creditorRights.getBuyPrice()) {
            jsonMap.put("resultBalanceBig", BigDecimalUtil.change(new Double(creditorRights.getBuyPrice().toString())));
        }
        jsonMap.put("resultBalance", creditorRights.getBuyPrice());

        // 借款期限（几个月、开始时间、结束时间=(借款产品表)DUE_TIME）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(DateUtil.addDate(loan.getPaymentDate(), 1)));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getFirstRepaymentDate()));
        }

        // 借款期限
        jsonMap.put("dueTime", loanProduct.getDueTime());

        // 还款方式
        jsonMap.put("repaymentType", loanProduct.getRepaymentType());

        // 借款利率
        jsonMap.put("annualRate", loan.getAnnualRate());

        // 借款用途
        if (null != loan.getLoanUseage() && !"".equals(loan.getLoanUseage())) {
            ConstantDefine constantDefine = new ConstantDefine();
            constantDefine.setConstantValue(loan.getLoanUseage());
            constantDefine.setConstantTypeCode("loanUseage");
            constantDefine.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(constantDefine);
            if (null != define) {
                jsonMap.put("loanUseage", define.getConstantName());
            }
        }

        // 借款人    通信地址：
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 邮箱地址：
        jsonMap.put("loanEmail", loanUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("loanMobileNo", loanUserInfoVO.getMobileNo());

        CustomerCard loanCustomerCard = customerCardService.getCustomerBindCardByUserId(loanUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != loanCustomerCard) {
            // 银行账号：
            jsonMap.put("loanCardCode", loanCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(loanCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("loanBankName", define.getConstantName());
            }
        }

        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        CustomerCard lendCustomerCard = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != lendCustomerCard) {
            // 银行账号：
            jsonMap.put("lendCardCode", lendCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("lendBankName", define.getConstantName());
            }
        }

        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap.put("repaymentPlanList", repaymentPlanList);

        // 每月还款日、每月还款金额(还款明细表 第一个月的)
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            RightsRepaymentDetail plan = repaymentPlanList.get(0);
            jsonMap.put("shouldBalance2", BigDecimalUtil.change(new Double(plan.getShouldBalance2().toString())));
            jsonMap.put("interest2", BigDecimalUtil.change(plan.getShouldInterest2().doubleValue()));
            jsonMap.put("repaymentDay", DateUtil.getDay(plan.getRepaymentDayPlanned()));
        }

        // 【注：先去掉这个!!!!】附件三：编号、总服务费、支付方式、借款金额
        // TODO...

        //Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));
        File file = new File(storageFolder + "/借款及服务协议(借款).pdf");
        ConverterHtml2PDF converter = new ConverterHtml2PDF();
        StringBuffer params = new StringBuffer();
        params.append("json=").append(JsonUtil.getGson(false).toJson(jsonMap));
        converter.generateContract(file, converter.parseURL(htmlUrl, params.toString()));
        //生成pdf
        //GeneratePDF.create(htmlUrl, storageFolder, "借款及服务协议(借款)", pair);

    }

    /**
     * 个人车贷
     *
     * @param loanProductId
     * @param storageFolder
     * @param paramsMap
     * @throws Exception
     * @throws IOException
     * @throws DocumentException
     */
    private void createBidAgreementForPersonCar(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws Exception, IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_loan_person_car";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());
        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人、身份证号
        jsonMap.put("loanRealName", loanUserInfoVO.getRealName());
        jsonMap.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款金额（大写+小写）
        if (null != creditorRights.getBuyPrice()) {
            jsonMap.put("resultBalanceBig", BigDecimalUtil.change(new Double(creditorRights.getBuyPrice().toString())));
        }
        jsonMap.put("resultBalance", creditorRights.getBuyPrice());

        // 借款期限（几个月、开始时间、结束时间=(借款产品表)DUE_TIME）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }

        // 借款期限
        jsonMap.put("dueTime", loanProduct.getDueTime());

        // 还款方式
        jsonMap.put("repaymentType", loanProduct.getRepaymentType());

        // 借款利率
        jsonMap.put("annualRate", loan.getAnnualRate());

        // 借款用途
        if (null != loan.getLoanUseage() && !"".equals(loan.getLoanUseage())) {
            ConstantDefine constantDefine = new ConstantDefine();
            constantDefine.setConstantValue(loan.getLoanUseage());
            constantDefine.setConstantTypeCode("loanUseage");
            constantDefine.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(constantDefine);
            if (null != define) {
                jsonMap.put("loanUseage", define.getConstantName());
            }
        }

        // 借款人    通信地址：
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 邮箱地址：
        jsonMap.put("loanEmail", loanUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("loanMobileNo", loanUserInfoVO.getMobileNo());

        CustomerCard loanCustomerCard = customerCardService.getCustomerBindCardByUserId(loanUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != loanCustomerCard) {
            // 银行账号：
            jsonMap.put("loanCardCode", loanCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(loanCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("loanBankName", define.getConstantName());
            }
        }

        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        CustomerCard lendCustomerCard = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != lendCustomerCard) {
            // 银行账号：
            jsonMap.put("lendCardCode", lendCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("lendBankName", define.getConstantName());
            }
        }

        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap.put("repaymentPlanList", repaymentPlanList);

        // 每月还款日、每月还款金额(还款明细表 第一个月的)
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            RightsRepaymentDetail plan = repaymentPlanList.get(0);
            jsonMap.put("shouldBalance2", BigDecimalUtil.change(new Double(plan.getShouldBalance2().toString())));
            jsonMap.put("repaymentDay", DateUtil.getDay(plan.getRepaymentDayPlanned()));
        }

        // 【注：先去掉这个!!!!】附件三：编号、总服务费、支付方式、借款金额
        // TODO...

        //Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));
        File file = new File(storageFolder + "/借款及服务协议(借款).pdf");
        ConverterHtml2PDF converter = new ConverterHtml2PDF();
        StringBuffer params = new StringBuffer();
        params.append("json=").append(URLEncoder.encode(JsonUtil.getGson(false).toJson(jsonMap),"UTF-8"));
        converter.generateContract(file, converter.parseURL(htmlUrl, params.toString()));
        //生成pdf
        //GeneratePDF.create(htmlUrl, storageFolder, "借款及服务协议(借款)", pair);

    }

    /**
     * 借款标，个人房产直投(借款及服务协议)
     *
     * @param loanProductId 借款产品ID
     * @param storageFolder 合同文件生成的物理路径
     * @param paramsMap     参数
     * @throws Exception
     * @throws IOException
     * @throws DocumentException
     */
    private void createBidAgreementForLoanDirectHouse(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws Exception, IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_loan_direct_house";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());
        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());
        // 费率信息
        LendProduct lendProduct = lendLoanBindingService.findRightsProduct(loanProductId);
        List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendProduct.getLendProductId(), loanProductId);
        jsonMap.put("feesItems", lendLoanBindings);

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人、身份证号
        jsonMap.put("loanRealName", loanUserInfoVO.getRealName());
        jsonMap.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款金额（大写+小写）
        if (null != creditorRights.getBuyPrice()) {
            jsonMap.put("resultBalanceBig", BigDecimalUtil.change(new Double(creditorRights.getBuyPrice().toString())));
        }
        jsonMap.put("resultBalance", creditorRights.getBuyPrice());

        // 借款期限（几个月、开始时间、结束时间=(借款产品表)DUE_TIME）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }

        // 借款期限
        jsonMap.put("dueTime", loanProduct.getDueTime());

        // 还款方式
        jsonMap.put("repaymentType", loanProduct.getRepaymentType());

        // 借款利率
        jsonMap.put("annualRate", loan.getAnnualRate());

        // 借款用途
        if (null != loan.getLoanUseage() && !"".equals(loan.getLoanUseage())) {
            ConstantDefine constantDefine = new ConstantDefine();
            constantDefine.setConstantValue(loan.getLoanUseage());
            constantDefine.setConstantTypeCode("loanUseage");
            constantDefine.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(constantDefine);
            if (null != define) {
                jsonMap.put("loanUseage", define.getConstantName());
            }
        }

        // 借款人    通信地址：
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 邮箱地址：
        jsonMap.put("loanEmail", loanUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("loanMobileNo", loanUserInfoVO.getMobileNo());

        CustomerCard loanCustomerCard = customerCardService.getCustomerBindCardByUserId(loanUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != loanCustomerCard) {
            // 银行账号：
            jsonMap.put("loanCardCode", loanCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(loanCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("loanBankName", define.getConstantName());
            }
        }

        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        CustomerCard lendCustomerCard = customerCardService.getCustomerBindCardByUserId(lendUserInfoVO.getUserId(), PayConstants.PayChannel.LL);
        if (null != lendCustomerCard) {
            // 银行账号：
            jsonMap.put("lendCardCode", lendCustomerCard.getCardCode());
            // 开户行：
            ConstantDefine define = constantDefineService.findById(lendCustomerCard.getBankCode());
            if (null != define) {
                jsonMap.put("lendBankName", define.getConstantName());
            }
        }

        // 附件二：列表（还款明细表）
        List<RightsRepaymentDetail> repaymentPlanList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRights.getCreditorRightsId());
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RightsRepaymentDetail repaymentPlan : repaymentPlanList) {
                if (null != repaymentPlan.getRepaymentDayPlanned()) {
                    repaymentPlan.setRepaymentDayDisplay(new SimpleDateFormat("yyyy年MM月dd日").format(repaymentPlan.getRepaymentDayPlanned()));
                }
            }
        }
        jsonMap.put("repaymentPlanList", repaymentPlanList);

        // 每月还款日、每月还款金额(还款明细表 第一个月的)
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            RightsRepaymentDetail plan = repaymentPlanList.get(0);
            jsonMap.put("shouldBalance2", BigDecimalUtil.change(new Double(plan.getShouldBalance2().toString())));
            jsonMap.put("repaymentDay", DateUtil.getDay(plan.getRepaymentDayPlanned()));
        }

        // 【注：先去掉这个!!!!】附件三：编号、总服务费、支付方式、借款金额
        // TODO...

        Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, "借款及服务协议(借款)", pair);

    }

    /**
     * 借款标，个人房产直投(授权委托书)
     *
     * @param loanProductId 借款产品ID
     * @param storageFolder 合同文件生成的物理路径
     * @param paramsMap     参数
     * @throws Exception
     * @throws IOException
     * @throws DocumentException
     */
    private void createBidAgreementForLoanDirectHouseEntrust(Long loanProductId, String storageFolder, Map<String, Object> paramsMap) throws Exception, IOException, DocumentException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String htmlUrl = PropertiesUtils.getInstance().get("BACKGROUND_PATH") + "agreement/service_loan_direct_house_entrust";

        // 债权
        Long creditorRightsId = (Long) paramsMap.get("creditorRightsId");
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        // 借款申请
        LoanApplication loan = this.findById(creditorRights.getLoanApplicationId());
        // 房产抵押快照表
        CustomerHouseSnapshot customerHouseSnapshot = customerHouseSnapshotService.getHouseByLoanApplicationId(loan.getLoanApplicationId());
        // 地址表
        Address address = addressService.getAddressById(customerHouseSnapshot.getHouseAddr());
        // 出借人
        UserInfoVO lendUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLendUserId());
        // 借款人
        UserInfoVO loanUserInfoVO = userInfoService.getUserExtByUserId(creditorRights.getLoanUserId());
        // 借款产品
        LoanProduct loanProduct = loanProductService.findById(loan.getLoanProductId());

        // 合同编号
        jsonMap.put("agreementCode", paramsMap.get("agreementCode"));

        // 年、月、日
        if (null != creditorRights.getCreateTime()) {
            jsonMap.put("agreementStartDate", new SimpleDateFormat("yyyy年MM月dd日").format(creditorRights.getCreateTime()));
        }

        // 出借人、身份证号
        jsonMap.put("lendRealName", lendUserInfoVO.getRealName());
        jsonMap.put("lendIdCard", lendUserInfoVO.getIdCard());

        // 借款人、身份证号
        jsonMap.put("loanRealName", loanUserInfoVO.getRealName());
        jsonMap.put("loanIdCard", loanUserInfoVO.getIdCard());

        // 借款期限（几个月、开始时间、结束时间=(借款产品表)DUE_TIME）
        if (null != loan.getPaymentDate()) {
            jsonMap.put("paymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getPaymentDate()));
        }
        if (null != loan.getLastRepaymentDate()) {
            jsonMap.put("lastRepaymentDate", new SimpleDateFormat("yyyy年MM月dd日").format(loan.getLastRepaymentDate()));
        }

        // 借款期限
        jsonMap.put("dueTime", loanProduct.getDueTime());

        // 借款人    通信地址：
        String loanAddress = "";
        if (loanUserInfoVO.getProvince() != null) {
            loanAddress += provinceInfoService.findById(loanUserInfoVO.getProvince()).getProvinceName();
        }
        if (loanUserInfoVO.getCity() != null) {
            loanAddress += cityInfoService.findById(loanUserInfoVO.getCity()).getCityName();
        }
        if (loanUserInfoVO.getDetail() != null) {
            loanAddress += loanUserInfoVO.getDetail();
        }
        jsonMap.put("loanAddress", loanAddress);

        // 邮箱地址：
        jsonMap.put("loanEmail", loanUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("loanMobileNo", loanUserInfoVO.getMobileNo());


        // 出借人    通信地址：
        String lendAddress = "";
        if (lendUserInfoVO.getProvince() != null) {
            lendAddress += provinceInfoService.findById(lendUserInfoVO.getProvince()).getProvinceName();
        }
        if (lendUserInfoVO.getCity() != null) {
            lendAddress += cityInfoService.findById(lendUserInfoVO.getCity()).getCityName();
        }
        if (lendUserInfoVO.getDetail() != null) {
            lendAddress += lendUserInfoVO.getDetail();
        }
        jsonMap.put("lendAddress", lendAddress);

        // 邮箱地址：
        jsonMap.put("lendEmail", lendUserInfoVO.getEmail());

        // 联系电话：
        jsonMap.put("lendMobileNo", lendUserInfoVO.getMobileNo());

        // 房产地址
        if (null != address) {
            jsonMap.put("addressDetail", address.getDetail());
        }

        // 房产证字号
        jsonMap.put("houseCardNumber", customerHouseSnapshot.getHouseCardNumber());

        Pair pair = new Pair("json", JsonUtil.getGson(false).toJson(jsonMap));

        //生成pdf
        GeneratePDF.create(htmlUrl, storageFolder, "授权委托书(借款)", pair);

    }

    @Override
    @Transactional
    public void exeSXJHCreateAgreementAll(Date startTime, Date endTime) {
        Date now = new Date();
        Map<String, Date> param = new HashMap<>();
        if (startTime != null) {
            param.put("startTime", startTime);
        }
        if (endTime != null) {
            param.put("endTime", endTime);
        }
        List<LendOrder> financeList = lendOrderService.findAllValidFinanceOrder(param);
        for (LendOrder financeOrder : financeList) {
            try {
                createFinanceAgreementAndFile(financeOrder, now);
            } catch (Exception e) {
                logger.error("生成合同失败，订单编号：" + financeOrder.getOrderCode() + "，失败原因：", e);
            }
        }
    }

    /**
     * 个人车贷
     *
     * @param mainLoan
     * @param residenceAddr
     * @param basic
     * @param ext
     * @param card
     * @param feesItems
     * @return
     */
    @Override
    public MainLoanApplication saveLoanForCarPart2(MainLoanApplication mainLoan, Address residenceAddr, CustomerBasicSnapshot basic,
                                                   UserInfoExt ext, CustomerCard card, List<LoanApplicationFeesItem> feesItems, CustomerCarSnapshot customerCarSnapshot) {
        // 如果该用户的该卡已经存在，则关联原卡数据，不存在则创建新的
        CustomerCard customerCard = new CustomerCard();
        customerCard.setUserId(mainLoan.getUserId());
        customerCard.setCardCode(card.getCardCode());
        customerCard.setStatus(CustomerCardStatus.NORMAL.getValue());
        List<CustomerCard> cardList = customerCardService.getAllCustomerCard(customerCard);

        Long customerCardId = null;

        if (null != cardList && cardList.size() > 0) {//存在该卡号
            customerCardId = cardList.get(0).getCustomerCardId();
            for (CustomerCard cusCard : cardList) {
                if (CustomerCardBindStatus.BINDED.getValue().equals(cusCard.getBindStatus())) {
                    customerCardId = cusCard.getCustomerCardId();
                }
            }
        } else {//不存在该卡号
            card.setUserId(mainLoan.getUserId());
            CustomerCard cardNew = customerCardService.addCustomerCard(card);
            customerCardId = cardNew.getCustomerCardId();
        }
        //借款信息
        LoanProduct loanProduct = loanProductService.findById(mainLoan.getLoanProductId());
        mainLoan.setAnnualRate(loanProduct.getAnnualRate());
        mainLoan.setInCardId(customerCardId);
        mainLoan.setOutCardId(customerCardId);
        mainLoan = mainLoanApplicationService.updateMainLoanApplication(mainLoan);//main


        //现住址
        residenceAddr = addressService.updateAddress(residenceAddr);

        //基础信息
        basic.setResidenceAddr(residenceAddr.getAddressId());
        basic = customerBasicSnapshotService.updateBasic(basic);

        //用户扩展
        ext.setResidentAddress(residenceAddr.getAddressId());
        ext = userInfoExtService.updateUserInfoExt(ext);
        //借款申请费用表
        if (null != feesItems && feesItems.size() > 0) {
            for (int i = 0; i < feesItems.size(); i++) {
                LoanApplicationFeesItem feesItem = feesItems.get(i);

                //借款申请费用表
                feesItem.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//main
                feesItem = loanApplicationFeesItemService.addLoanApplicationFeesItem(feesItem);

            }
        }
        CustomerCarSnapshot carByMainLoan = customerCarSnapshotService.getCarByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        customerCarSnapshot.setSnapshotId(carByMainLoan.getSnapshotId());
        customerCarSnapshotService.updateCar(customerCarSnapshot);
        return mainLoan;
    }

    /**
     * 新新建放款主任务
     *
     * @param loanApplicationId
     * @return
     */
    private Schedule insertMainHistoryForDataBase(Long loanApplicationId) {
        Schedule schedule = new Schedule();
        schedule.setBusinessId(loanApplicationId);
        schedule.setStatus(Integer.valueOf(ScheduleEnum.BUSINESS_PREPARE.getValue()));//准备中
        schedule.setStartTime(new Date());
        schedule.setBusinessType(Integer.valueOf(AccountConstants.BusinessTypeEnum.FEESTYPE_MAKELOAN.getValue()));
        scheduleService.addSchedule(schedule);
        return schedule;
    }

    /**
     * @param outUserId
     * @param inUserId
     * @param money
     * @param scheduleId
     * @param type
     */
    private void insertSubTaskByLoan(Long outUserId, Long inUserId, BigDecimal money, Long scheduleId, String type, Long besiessId) {
        CapitalFlow capitalFlow = new CapitalFlow();
        capitalFlow.setResult(Integer.valueOf(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlow.setAmount(money);
        capitalFlow.setScheduleId(scheduleId);
        capitalFlow.setStartTime(new Date());
        if(null !=inUserId){
            capitalFlow.setToUser(inUserId);
        }
        capitalFlow.setBusinessId(besiessId);
        capitalFlow.setBusinessFlow(HfUtils.getUniqueSerialNum());
        capitalFlow.setFromUser(outUserId);
        capitalFlow.setOperationType(Integer.valueOf(type));
        this.capitalFlowService.addCapital(capitalFlow);

    }

    /**
     * 获取公司平台账户
     *
     * @param systemAccountId
     * @return
     */
    private UserInfoExt getSystemUserInfo(Long systemAccountId) {
        UserAccount sysUserAccount = userAccountService.getUserAccountByAccId(systemAccountId);
        UserInfoExt userInfoSystem = userInfoExtService.getUserInfoExtById(sysUserAccount.getUserId());
        return userInfoSystem;
    }


    private boolean getFreeSchedule(Long loanApplicationId) {
        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLoanApplicationId(loanApplicationId, LendOrderBidStatusEnum.BIDING);
        for (LendOrderBidDetail orderBidDetail : lendOrderBidDetails) {
            if (orderBidDetail.getStatus() == LendOrderBidStatusEnum.BIDING.value2Char()) {
                LendOrder lendOrder = lendOrderService.findById(orderBidDetail.getLendOrderId());
                List<PayOrderDetail> paymentOrderDetail = payService.getPaymentOrderDetail(lendOrder.getLendOrderId());
                Map map = new HashMap();
                map.put("businessId", paymentOrderDetail.get(0).getPayId());
                map.put("businessType", AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getValue());
                //businessType
                List<Schedule> byCondition = scheduleService.findByCondition(map);
                for (int i = 0; i < byCondition.size(); i++) {
                    if (byCondition.get(i).getBusinessType().equals(AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN)) {
                        map.clear();
                        map.put("scheduleId", byCondition.get(i).getScheduleId());
                        List<CapitalFlow> byCondition1 = capitalFlowService.findByCondition(map);
                        for (int j = 0; j < byCondition.size(); j++) {
                            if (!byCondition.get(j).getStatus().equals(ScheduleEnum.BUSINESS_SUCCESS.getValue())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

}
