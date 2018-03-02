package com.xt.cfp.web.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.PayConstants.CardChannel;
import com.xt.cfp.core.constants.PayConstants.PayChannel;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/jsp/loanManage/loan")
public class LoanApplicationController extends BaseController {

    @Autowired
    LoanApplicationService loanApplicationService;

    @Autowired
    MainLoanApplicationService mainLoanApplicationService;

    @Autowired
    private CustomerBasicSnapshotService customerBasicSnapshotService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private UserInfoService userInfoServices;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;


    @Autowired
    private CustomerWorkSnapshotService customerWorkSnapshotService;

    @Autowired
    private CustomerContactsSnapshotService customerContactsSnapshotService;

    @Autowired
    private CustomerHouseSnapshotService customerHouseSnapshotService;

    @Autowired
    private CustomerCarSnapshotService customerCarSnapshotService;

    @Autowired
    private LoanApplicationFeesItemService loanApplicationFeesItemService;

    @Autowired
    private CustomerCardService customerCardService;

    @Autowired
    private LoanProductService loanProductService;

    @Autowired
    private LoanProductFeesItemService loanProductFeesItemService;
    @Autowired
    private BondSourceService bondSourceService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private LoanFeesDetailService loanFeesDetailService;
    @Autowired
    private RepaymentRecordService repaymentRecordService;
    @Autowired
    private FeesItemService feesItemService;

    @Autowired
    private ProvinceInfoService provinceInfoService;

    @Autowired
    private CityInfoService cityInfoService;

    @Autowired
    private ConstantDefineService constantDefineService;

    @Autowired
    private StoreService storeService;

    @Autowired
    LendOrderService lendOrderService;

    @RequestMapping(value = "/toLoanApplicationList")
    public ModelAndView toLoanApplicationList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/loanManage/loan/contractList");
        return mv;
    }

    @RequestMapping(value = "/loanApplicationList")
    @ResponseBody
    public Object loanApplicationList(HttpServletRequest request,
                                      @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                      @RequestParam(value = "page", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "loanApplicationCode", required = false) String loanApplicationCode,
                                      @RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,
                                      @RequestParam(value = "channel", required = false) String channel, @RequestParam(value = "loanType", required = false) String loanType,
                                      @RequestParam(value = "realName", required = false) String realName,
                                      @RequestParam(value = "idCard", required = false) String idCard, @RequestParam(value = "mobileNo", required = false) String mobileNo,
                                      @RequestParam(value = "applicationState", required = false) String applicationState) {

        return loanApplicationService.getAllLoanApplicationList(pageNum, pageSize, loanApplicationCode, loanApplicationName, channel, loanType, realName, idCard, mobileNo, applicationState);
    }

    @RequestMapping(value = "exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "loanApplicationCode", required = false) String loanApplicationCode,
                            @RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,
                            @RequestParam(value = "channel", required = false) String channel,
                            @RequestParam(value = "loanType", required = false) String loanType,
                            @RequestParam(value = "realName", required = false) String realName,
                            @RequestParam(value = "idCard", required = false) String idCard,
                            @RequestParam(value = "mobileNo", required = false) String mobileNo,
                            @RequestParam(value = "applicationState", required = false) String applicationState) {

        loanApplicationService.exportExcel(response, loanApplicationCode, loanApplicationName, channel, loanType, realName, idCard, mobileNo, applicationState);
    }

    /**
     * 显示借款列表（渠道）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/showBorrow")
    public String showBorrow(HttpServletRequest request, Long bondSourceId) {
        request.setAttribute("bondSourceId", bondSourceId);
        return "jsp/loanManage/loan/borrow";
    }

    /**
     * 显示借款列表
     *
     * @return
     */
    @RequestMapping(value = "/showBorrowList")
    @ResponseBody
    public Object showBorrowList(Long bondSourceId, LoanApplicationVO vo, @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        if (bondSourceId != null) {
            vo.setChannel("1");
            vo.setChannelId(bondSourceId);
        }
        return loanApplicationService.getLoanApplicationByUserId(pageNum, pageSize, vo, null);
    }

    /**
     * 显示借款列表（企业）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/showEnterpriseBorrow")
    public String showEnterpriseBorrow(HttpServletRequest request, Long enterpriseId) {
        request.setAttribute("enterpriseId", enterpriseId);
        return "jsp/enterprise/detail/enterpriseBorrow";
    }

    /**
     * 显示借款列表(企业)
     *
     * @return
     */
    @RequestMapping(value = "/showEnterpriseBorrowList")
    @ResponseBody
    public Object showEnterpriseBorrowList(Long enterpriseId, LoanApplicationEnterpriseVO vo, @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        if (enterpriseId != null) {
            vo.setEnterpriseId(enterpriseId);
        }
        return loanApplicationService.getLoanApplicationByEnterpriseId(pageNum, pageSize, vo, null);
    }


    @RequestMapping(value = "/toRepayment")
    public String toRepayment() {

        return "jsp/financial/repaymentList";
    }


    /**
     * 跳转到：添加1页。
     */
    @RequestMapping(value = "/to_loan_add_part1")
    public ModelAndView to_loan_add_part1() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/loanManage/loan/loan_add_part1");
        return mv;
    }

    @RequestMapping(value = "/check_idcard")
    @ResponseBody
    public Object checkIDCard(@RequestParam(value = "trueName", required = true) String trueName,
                              @RequestParam(value = "idCard", required = true) String idCard) {
        boolean check = userInfoExtService.checkIDCard(idCard, trueName);
        return check;
    }

    /**
     * 执行：保存1页。【main】
     */
    @RequestMapping("/save_loan_part1")
    @ResponseBody
    public Object saveLoanPart1(HttpServletRequest request, HttpSession session,
                                @RequestParam(value = "subjectType", required = false) String subjectType,//标的类型（1借款：loanMark;2债权：rightsMark）
                                @RequestParam(value = "channel", required = false) String channel,//来源类型（1.渠道：channel;2.门店：store）
                                @RequestParam(value = "channelId", required = false) String channelId,//渠道
                                @RequestParam(value = "originalUserId", required = false) String originalUserId,//原始债权
                                @RequestParam(value = "store_channelId", required = false) String store_channelId,//门店
                                @RequestParam(value = "loanType", required = false) String loanType,//借款类型（1信用贷:credit;2.房贷:house;3.车贷:car）
                                @RequestParam(value = "idCard", required = false) String idCard,//借款人身份证号
                                @RequestParam(value = "trueName", required = false) String trueName,//借款人姓名
                                @RequestParam(value = "userType", required = false) String userType) {//对应用户类型
        Map resultMap = new HashMap();
        try {
            // 合法性验证。
            // 获取当前登录用户信息
            AdminInfo adminInfo = getCurrentUser();
            if (adminInfo == null) {
                return returnResultMap(false, null, "check", "获取当前用户信息失败！");
            }
            // 标的类型
            if (null == subjectType || "".equals(subjectType)) {
                return returnResultMap(false, null, "check", "标的类型不能为空！");
            }
            // 来源类型
            if (null == channel || "".equals(channel)) {
                return returnResultMap(false, null, "check", "来源类型不能为空！");
            }
            //判断渠道等于新疆某渠道，并且借款类型为个人信用车贷
            if (LoanApplication.CHANNEL_CHANNEL.equals(channel)) {// 渠道
                if (null == channelId || "".equals(channelId)) {
                    return returnResultMap(false, null, "check", "渠道不能为空！");
                }
                if (subjectType.equals(SubjectTypeEnum.CREDITOR.getValue())) {
                    if (null == originalUserId || "".equals(originalUserId)) {
                        return returnResultMap(false, null, "check", "原始债权不能为空！");
                    }
                }
            } else {// 门店
                if (null == store_channelId || "".equals(store_channelId)) {
                    return returnResultMap(false, null, "check", "门店不能为空！");
                }
            }
            // 如果是债权标则必须是渠道
            if (LoanApplication.SUBJECTTYPE_RIGHTSMARK.equals(subjectType)) {
                if (!LoanApplication.CHANNEL_CHANNEL.equals(channel)) {// 渠道
                    return returnResultMap(false, null, "check", "债权标必须是渠道类型！");
                }
            }
            // 借款类型
            if (null == loanType || "".equals(loanType)) {
                return returnResultMap(false, null, "check", "借款类型不能为空！");
            }
            // 对应用户类型
            if (null == userType || "".equals(userType)) {
                return returnResultMap(false, null, "check", "对应用户类型不能为空！");
            }
            // 借款人身份证号
            if (null == idCard || "".equals(idCard)) {
                return returnResultMap(false, null, "check", "身份证号不能为空！");
            }
            // 借款人姓名
            if (null == trueName || "".equals(trueName)) {
                return returnResultMap(false, null, "check", "姓名不能为空！");
            }

            // 【借款-开始】
            MainLoanApplication mainLoan = new MainLoanApplication();
            mainLoan.setSubjectType(subjectType);
            mainLoan.setChannel(channel);
            if (LoanApplication.CHANNEL_CHANNEL.equals(channel)) {// 渠道
                if (null != channelId && !"".equals(channelId)) {
                    mainLoan.setChannelId(Long.valueOf(channelId));
                }
                if (null != originalUserId && !"".equals(originalUserId)) {
                    mainLoan.setOriginalUserId(Long.valueOf(originalUserId));
                }
                if (SubjectTypeEnum.CREDITOR.getValue().equals(subjectType)) {
                    if (null != channelId && !"".equals(channelId)) {
                        BondSource bondSource = bondSourceService.getBondSourceByBondSourceId(Long.valueOf(channelId));
                        UserAccount bondSourceAccount = userAccountService.getCashAccount(bondSource.getUserId());
                        mainLoan.setRepaymentAccountId(bondSourceAccount.getAccId());
                    }
                    if (null != originalUserId && !"".equals(originalUserId)) {
                        BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(Long.valueOf(originalUserId));
                        UserAccount bondSourceUserAccount = userAccountService.getCashAccount(bondSourceUser.getUserId());
                        mainLoan.setCustomerAccountId(bondSourceUserAccount.getAccId());
                    }
                }
            } else {// 门店
                if (null != store_channelId && !"".equals(store_channelId)) {
                    mainLoan.setChannelId(Long.valueOf(store_channelId));
                }
            }
            mainLoan.setLoanType(loanType);// 借款类型
            mainLoan.setApplicationState(LoanApplication.APPLICATIONSTATE_DRAFT);
            mainLoan.setRecordTime(new Date());
            mainLoan.setCreateTime(new Date());
            mainLoan.setRecorderPersonnel(adminInfo.getAdminId());//录入人员id
            mainLoan.setRecorderName(adminInfo.getDisplayName());//录入人员姓名

            //生成借款申请编号（开始）,规则：借款类型(X/F)YYMMDD+5位随机数
            StringBuffer loanApplicationCode = new StringBuffer();
            if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanType)) {
                loanApplicationCode.append("X");
            } else if (LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanType)) {
                loanApplicationCode.append("F");
            } else if (LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanType)) {
                loanApplicationCode.append("ZF");
            }
            loanApplicationCode.append(new SimpleDateFormat("yyMMdd").format(new Date()));
            loanApplicationCode.append(StringUtils.generateRandomNumber(5));
            // 生成借款申请编号（结束）
            mainLoan.setLoanApplicationCode(loanApplicationCode.toString());//借款申请编号
            // 【借款-结束】

            // 【***补充主借款信息-开始***】
            mainLoan.setMainCode("Z-" + loanApplicationCode.toString());//主编号
            mainLoan.setMainState("0");//主状态，(0.未发标；1.发标中；2.发标完成)
            mainLoan.setMainCreateTime(new Date());//主创建时间
            mainLoan.setMainUpdateTime(new Date());//主最后更改时间
            mainLoan.setMainAdminId(adminInfo.getAdminId());//主最后操作人
            // 【***补充主借款信息-结束***】

            // 【基本信息-开始】
            CustomerBasicSnapshot basic = new CustomerBasicSnapshot();
            basic.setIdCard(idCard);
            basic.setTrueName(trueName);
            basic.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(StringUtils.getBirthdayByIdCode(idCard)));
            basic.setSex(StringUtils.getSexByByIdCode(idCard));
            // 【基本信息-结束】

            // 用户和扩展信息处理-开始
            UserInfo user = new UserInfo();
            UserInfoExt ext = new UserInfoExt();
            List<UserInfoExt> userInfoExtList = userInfoExtService.getUserInfoExtByIdCardAndSource(idCard);
            boolean existsUser = false;//标记是否存在该用户
            if (null != userInfoExtList && userInfoExtList.size() > 0) {
                ext = userInfoExtList.get((userInfoExtList.size() - 1));
                user = userInfoService.getUserByUserId(ext.getUserId());
                existsUser = true;
            } else {
                // 【用户信息-开始】
                user.setLoginName(String.valueOf(new Date().getTime()));
                user.setMobileNo(String.valueOf(new Date().getTime()).substring(2));
                user.setLoginPass(String.valueOf(new Date().getTime()));
                user.setCreateTime(new Date());
                // 【用户信息-结束】

                // 【用户扩展-开始】
                ext.setRealName(trueName);
                ext.setIdCard(idCard);
                ext.setSex(basic.getSex());
                ext.setBirthday(basic.getBirthday());
                ext.setIsVerified(UserIsVerifiedEnum.YES.getValue());
                ext.setMobileNo(String.valueOf(new Date().getTime()).substring(2));
                // 【用户扩展-结束】
            }
            // 用户和扩展信息处理-结束
            //线上用户借款申请
            if (UserType.COMMON.getValue().equals(userType)) {
                UserInfo ui = userInfoService.getUserTypeInfo(idCard, trueName, userType, UserIsVerifiedEnum.YES.getValue());
                if (ui != null) {
                    user = ui;
                    ext = userInfoExtService.getUserInfoExtById(user.getUserId());
                    existsUser = true;
                } else {
                    return returnResultMap(false, null, "check", "您所选的对应用户类型不正确！");
                }
            }
            // 执行保存操作。
            MainLoanApplication mainApplication = loanApplicationService.saveLoanPart1(mainLoan, basic, user, ext, existsUser);

            resultMap.put("loanApplicationId", mainApplication.getMainLoanApplicationId());
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, JSONObject.toJSON(resultMap), null, null);
    }

    /**
     * 跳转到：添加234页。【main】
     *
     * @param loanApplicationId 借款申请ID
     * @param actionType        add or edit
     */
    @RequestMapping(value = "/to_loan_add_part234")
    public ModelAndView to_loan_add_part234(
            @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,
            @RequestParam(value = "actionType", required = false) String actionType) {
        ModelAndView mv = new ModelAndView();
        MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
        mv.addObject("loanType", mainLoan.getLoanType());//借款类型
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.addObject("actionType", actionType);
        mv.addObject("applicationState", mainLoan.getApplicationState());
        if (mainLoan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue())) {
            mv.setViewName("jsp/loanManage/loan/loan_add_person_credit_car_part234");//类型判断 是否为个人借款车贷
        }else if(mainLoan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())){
            mv.setViewName("jsp/loanManage/loan/loan_add_cash_part234"); // 现金贷
        } else {
            mv.setViewName("jsp/loanManage/loan/loan_add_part234");
        }
        return mv;
    }

    /**
     * 跳转到：添加2页。【main】
     *
     * @param loanApplicationId 借款申请ID
     */
    @RequestMapping(value = "/to_loan_add_part2")
    public ModelAndView to_loan_add_part2(
            @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId) {
        ModelAndView mv = new ModelAndView();
        MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.addObject("subjectType", mainLoan.getSubjectType());//1借款：loanMark;2债权：rightsMark
        CustomerBasicSnapshot basic = customerBasicSnapshotService.getBasicByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        mv.addObject("idCard", basic.getIdCard());
        mv.addObject("trueName", basic.getTrueName());
        mv.addObject("birthday", new SimpleDateFormat("yyyy-MM-dd").format(basic.getBirthday()));
        mv.addObject("sex", basic.getSexStr(basic.getSex()));
        mv.addObject("originalUserId", mainLoan.getOriginalUserId());
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(mainLoan.getUserId());
        mv.addObject("userInfoExt", userInfoExt);
        // 普通用户，银行卡处理【开始】
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(mainLoan.getUserId(), PayChannel.HF);
        if (null != customerCard) {
            mv.addObject("customerCard", customerCard);

            //银行名称
            ConstantDefine define = constantDefineService.findById(customerCard.getBankCode());
            if (null != define) {
                mv.addObject("bankName", define.getConstantName());
            }
        }
        // 普通用户，银行卡处理【结束】
        if (mainLoan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue())) {
            mv.setViewName("jsp/loanManage/loan/loan_people_creditcar_add_part2");
        }else if(mainLoan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())){
            mv.setViewName("jsp/loanManage/loan/loan_cash_add_part2");
        } else {
            mv.setViewName("jsp/loanManage/loan/loan_add_part2");
        }

        return mv;
    }

    /**
     * 跳转到：编辑2页。【main】
     *
     * @param loanApplicationId 借款申请ID
     */
    @RequestMapping(value = "/to_loan_edit_part2")
    public ModelAndView to_loan_edit_part2(
            @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId) {
        ModelAndView mv = new ModelAndView();
        MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.addObject("subjectType", mainLoan.getSubjectType());//1借款：loanMark;2债权：rightsMark
        CustomerBasicSnapshot basic = customerBasicSnapshotService.getBasicByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        mv.addObject("idCard", basic.getIdCard());
        mv.addObject("trueName", basic.getTrueName());
        mv.addObject("birthday", new SimpleDateFormat("yyyy-MM-dd").format(basic.getBirthday()));
        mv.addObject("sex", basic.getSexStr(basic.getSex()));
        mv.addObject("originalUserId", mainLoan.getOriginalUserId());

        // 加载编辑信息-begin
        Address bornAddr = addressService.getAddressById(basic.getBornAddr());
        Address registAddr = addressService.getAddressById(basic.getRegistAddr());
        Address residenceAddr = addressService.getAddressById(basic.getResidenceAddr());
        CustomerWorkSnapshot work = customerWorkSnapshotService.getWorkByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        Address workingAddr = addressService.getAddressById(work.getWorkingAddr());
        List<CustomerContactsSnapshot> contactsSnapshots = customerContactsSnapshotService.getContactsByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        CustomerContactsSnapshot contactsSnapshot = null;
        if (null != contactsSnapshots && contactsSnapshots.size() > 0) {
            contactsSnapshot = contactsSnapshots.get(0);
            contactsSnapshots.remove(0);
        }

        if (!StringUtils.isNull(mainLoan.getApplicationDesc())) {
            mainLoan.setApplicationDesc(mainLoan.getApplicationDesc().replaceAll("<br>", "\r\n"));//借款描述
        }
        if (!StringUtils.isNull(mainLoan.getRiskControlInformation())) {
            mainLoan.setRiskControlInformation(mainLoan.getRiskControlInformation().replaceAll("<br>", "\r\n"));//风险控制信息
        }
        if (!StringUtils.isNull(mainLoan.getLoanUseageDesc())) {
            mainLoan.setLoanUseageDesc(mainLoan.getLoanUseageDesc().replaceAll("<br>", "\r\n"));//借款用途描述
        }

        mv.addObject("loan", mainLoan);
        mv.addObject("basic", basic);
        mv.addObject("bornAddr", bornAddr);
        mv.addObject("registAddr", registAddr);
        mv.addObject("residenceAddr", residenceAddr);
        mv.addObject("work", work);
        mv.addObject("workingAddr", workingAddr);
        mv.addObject("contactsSnapshots", JSONObject.toJSON(contactsSnapshots));
        mv.addObject("contactsSnapshot", contactsSnapshot);

        //银行卡（借款标情况）
        if (LoanApplication.SUBJECTTYPE_LOANMARK.equals(mainLoan.getSubjectType())) {
            CustomerCard card = customerCardService.findById(mainLoan.getInCardId());
            mv.addObject("card", card);
        }

        //关系类型
        List<ConstantDefine> relationTypeList = constantDefineService.findByTypeCodeAndParentConstant("relationType", 0l);
        mv.addObject("relationTypeJsonArray", JSONObject.toJSON(relationTypeList));

        //关系
        if (null != relationTypeList && relationTypeList.size() > 0) {
            for (ConstantDefine constantDefine : relationTypeList) {
                List<ConstantDefine> relationList = constantDefineService.findByTypeCodeAndParentConstant("relation", constantDefine.getConstantDefineId());
                mv.addObject("relationsJsonArray_" + constantDefine.getConstantDefineId(), JSONObject.toJSON(relationList));
            }
        }

        //关系类型
        List<ConstantDefine> relationTypes = constantDefineService.findByTypeCodeAndParentConstant("relationType", 0l);
        mv.addObject("relationTypes", relationTypes);
        // 联系人
        if (null != contactsSnapshot) {
            //关系
            ConstantDefine cdparam = new ConstantDefine();
            cdparam.setConstantTypeCode("relationType");
            cdparam.setConstantValue(contactsSnapshot.getRelationType());
            cdparam.setParentConstant(0l);
            ConstantDefine define = constantDefineService.findConstantByTypeCodeAndValue(cdparam);
            if (null != define) {
                List<ConstantDefine> relations = constantDefineService.findByTypeCodeAndParentConstant("relation", define.getConstantDefineId());
                mv.addObject("relations", relations);
            }
        }

        //省份
        List<ProvinceInfo> provinceInfos = provinceInfoService.getAllProvinceInfo();
        mv.addObject("provinceInfos", provinceInfos);

        //籍贯
        List<CityInfo> bornCitys = cityInfoService.getCityByProvinceIdAndPId(bornAddr.getProvince(), 0l);
        List<CityInfo> bornDistricts = cityInfoService.getCityByProvinceIdAndPId(bornAddr.getProvince(), bornAddr.getCity());
        mv.addObject("bornCitys", bornCitys);
        mv.addObject("bornDistricts", bornDistricts);

        //户口所在地
        List<CityInfo> registCitys = cityInfoService.getCityByProvinceIdAndPId(registAddr.getProvince(), 0l);
        List<CityInfo> registDistricts = cityInfoService.getCityByProvinceIdAndPId(registAddr.getProvince(), registAddr.getCity());
        mv.addObject("registCitys", registCitys);
        mv.addObject("registDistricts", registDistricts);

        //现住址
        List<CityInfo> residenceCitys = cityInfoService.getCityByProvinceIdAndPId(residenceAddr.getProvince(), 0l);
        List<CityInfo> residenceDistricts = cityInfoService.getCityByProvinceIdAndPId(residenceAddr.getProvince(), residenceAddr.getCity());
        mv.addObject("residenceCitys", residenceCitys);
        mv.addObject("residenceDistricts", residenceDistricts);

        // 单位地址
        List<CityInfo> workingCitys = cityInfoService.getCityByProvinceIdAndPId(workingAddr.getProvince(), 0l);
        List<CityInfo> workingDistricts = cityInfoService.getCityByProvinceIdAndPId(workingAddr.getProvince(), workingAddr.getCity());
        mv.addObject("workingCitys", workingCitys);
        mv.addObject("workingDistricts", workingDistricts);
        // 加载编辑信息-end

        mv.addObject("edit",true);
        if(mainLoan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())) {
            mv.setViewName("jsp/loanManage/loan/loan_cash_add_part2");
        }else{
            mv.setViewName("jsp/loanManage/loan/loan_edit_part2");
        }
        return mv;
    }

    /**
     * 执行：保存2页。【main】
     */
    @RequestMapping("/save_loan_part2")
    @ResponseBody
    public Object saveLoanPart2(HttpServletRequest request, HttpSession session,
                                // 借款信息
                                @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
                                @RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,//借款合同名称
                                @RequestParam(value = "loanUseage", required = false) String loanUseage,//借款用途
                                @RequestParam(value = "loanProductId", required = false) String loanProductId,//借款产品
                                @RequestParam(value = "loanBalance", required = false) String loanBalance,//借款金额
                                @RequestParam(value = "applicationDesc", required = false) String applicationDesc,//描述
                                @RequestParam(value = "riskControlInformation", required = false) String riskControlInformation,//风险控制信息
                                @RequestParam(value = "loanUseageDesc", required = false) String loanUseageDesc,//借款用途描述
                                @RequestParam(value = "offlineApplyCode", required = false) String offlineApplyCode,//线下编号
                                @RequestParam(value = "mobilePhone", required = false) String mobilePhone,//借款人手机号
                                @RequestParam(value = "email", required = false) String email,//借款人邮箱
                                @RequestParam(value = "education", required = false) String education,//最高学历
                                @RequestParam(value = "isMarried", required = false) String isMarried,//婚姻状况
                                @RequestParam(value = "childStatus", required = false) String childStatus,//有无子女
                                @RequestParam(value = "assetsInfo", required = false) String assetsInfo,//资产情况
                                @RequestParam(value = "monthlyIncome", required = false) String monthlyIncome,//月均收入
                                @RequestParam(value = "maxCreditValue", required = false) String maxCreditValue,//信用卡最高额度

                                @RequestParam(value = "bornAddr_provence", required = false) String bornAddr_provence,//籍贯_省
                                @RequestParam(value = "bornAddr_city", required = false) String bornAddr_city,//籍贯_市
                                @RequestParam(value = "bornAddr_district", required = false) String bornAddr_district,//籍贯_区
                                @RequestParam(value = "bornAddr_detail", required = false) String bornAddr_detail,//籍贯_详细地址

                                @RequestParam(value = "registAddr_provence", required = false) String registAddr_provence,//户口所在地_省
                                @RequestParam(value = "registAddr_city", required = false) String registAddr_city,//户口所在地_市
                                @RequestParam(value = "registAddr_district", required = false) String registAddr_district,//户口所在地_区
                                @RequestParam(value = "registAddr_detail", required = false) String registAddr_detail,//户口所在地_详细地址

                                @RequestParam(value = "residenceAddr_provence", required = false) String residenceAddr_provence,//现住址_省
                                @RequestParam(value = "residenceAddr_city", required = false) String residenceAddr_city,//现住址_市
                                @RequestParam(value = "residenceAddr_district", required = false) String residenceAddr_district,//现住址_区
                                @RequestParam(value = "residenceAddr_detail", required = false) String residenceAddr_detail,//现住址_详细地址

                                @RequestParam(value = "zipcode", required = false) String zipcode,//现住址邮政编码
                                // 工作信息
                                @RequestParam(value = "companyNature", required = false) String companyNature,//单位性质
                                @RequestParam(value = "companyName", required = false) String companyName,//单位名称

                                @RequestParam(value = "workingAddr_provence", required = false) String workingAddr_provence,//单位地址_省
                                @RequestParam(value = "workingAddr_city", required = false) String workingAddr_city,//单位地址_市
                                @RequestParam(value = "workingAddr_district", required = false) String workingAddr_district,//单位地址_区
                                @RequestParam(value = "workingAddr_detail", required = false) String workingAddr_detail,//单位地址_详细地址

                                @RequestParam(value = "companyPhone", required = false) String companyPhone,//单位电话
                                @RequestParam(value = "post", required = false) String post,//职务
                                @RequestParam(value = "joinDate", required = false) String joinDate,//入职时间
                                // 联系人信息
                                @RequestParam(value = "controlsArray", required = false) String controlsArray,//联系人数组
                                // 银行卡信息
                                @RequestParam(value = "bankCode", required = false) String bankCode,//所属银行
                                @RequestParam(value = "registeredBank", required = false) String registeredBank,//详细地址
                                @RequestParam(value = "cardCode", required = false) String cardCode,//卡号
                                @RequestParam(value = "cardCustomerName", required = false) String cardCustomerName,//开户名
                                @RequestParam(value = "inCardId", required = false) String inCardId,//打款卡
                                @RequestParam(value = "outCardId", required = false) String outCardId) {//划扣卡
        try {
            // 合法性验证。
            if (null == loanApplicationId || "".equals(loanApplicationId)) {
                return returnResultMap(false, null, "check", "借款申请ID不能为空！");
            }
            // 根据ID加载一条借款申请信息
            MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
            if (null == mainLoan) {
                return returnResultMap(false, null, "check", "借款信息不能为空！");
            }
            // 借款用途
            if (null == loanUseage || "".equals(loanUseage)) {
                return returnResultMap(false, null, "check", "借款用途不能为空！");
            }
            // 借款产品
            if (null == loanProductId || "".equals(loanProductId)) {
                return returnResultMap(false, null, "check", "借款产品不能为空！");
            }
            // 借款金额
            if (null == loanBalance || "".equals(loanBalance)) {
                return returnResultMap(false, null, "check", "借款金额不能为空！");
            }
            // 描述
            if (null == applicationDesc || "".equals(applicationDesc)) {
                return returnResultMap(false, null, "check", "描述不能为空！");
            }
            // 风险控制信息
            if (null == riskControlInformation || "".equals(riskControlInformation)) {
                return returnResultMap(false, null, "check", "风险控制信息不能为空！");
            }
            // 借款用途描述
            if (null == loanUseageDesc || "".equals(loanUseageDesc)) {
                return returnResultMap(false, null, "check", "借款用途描述不能为空！");
            }
            // 借款人手机
            if(!mainLoan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())){
                if (null == mobilePhone || "".equals(mobilePhone)) {
                    return returnResultMap(false, null, "check", "借款人手机号不能为空！");
                } else {
                    if (!StringUtils.isPattern("(^1[3|4|5|7|8][0-9]{9}$)", mobilePhone)) {
                        return returnResultMap(false, null, "check", "借款人手机号格式不正确！");
                    }
                }
            }

            if (LoanApplication.SUBJECTTYPE_LOANMARK.equals(mainLoan.getSubjectType())) {// 借款标
                    // 开户行
                    if (null == bankCode || "".equals(bankCode)) {
                        return returnResultMap(false, null, "check", "开户行不能为空！");
                    }
                    // 详细地址
                    if (null == registeredBank || "".equals(registeredBank)) {
                        return returnResultMap(false, null, "check", "详细地址不能为空！");
                    }
                    // 卡号
                    if (null == cardCode || "".equals(cardCode)) {
                        return returnResultMap(false, null, "check", "卡号不能为空！");
                    } else {
                        cardCode = cardCode.replace(" ", "");
                        if (cardCode.length() < 16 || cardCode.length() > 19 || !StringUtils.isPattern("^([0-9]+)$", cardCode)) {
                            return returnResultMap(false, null, "check", "银行卡号格式不正确！");
                        }
                    }
                    // 开户名
                    if (null == cardCustomerName || "".equals(cardCustomerName)) {
                        return returnResultMap(false, null, "check", "开户名不能为空！");
                    }

            } else {// 债权标
                // 打款卡
                if (null == inCardId || "".equals(inCardId)) {
                    return returnResultMap(false, null, "check", "打款卡不能为空！");
                }
                // 划扣卡
                if (null == outCardId || "".equals(outCardId)) {
                    return returnResultMap(false, null, "check", "划扣卡不能为空！");
                }
            }
            if (null != zipcode && !"".equals(zipcode)) {
                if (zipcode.length() != 6 || !StringUtils.isPattern("^([0-9]+)$", zipcode)) {
                    return returnResultMap(false, null, "check", "邮政编码格式不正确！");
                }
            }

// 【LoanApplication - 开始】*************************************************************************
            mainLoan.setLoanUseage(loanUseage);//借款用途
            if (null != loanProductId && !"".equals(loanProductId)) {
                mainLoan.setLoanProductId(Long.valueOf(loanProductId));//借款产品
                LoanProduct loanProduct = loanProductService.findById(Long.valueOf(loanProductId));
                mainLoan.setAnnualRate(loanProduct.getAnnualRate());//年利率
            }
            if (null != loanBalance && !"".equals(loanBalance)) {
                mainLoan.setLoanBalance(new BigDecimal(loanBalance));//借款金额
                if (!LoanApplication.APPLICATIONSTATE_DRAFT.equals(mainLoan.getApplicationState())) {//如果借款申请状态不为草稿，则将批复金额和借款金额改为一致
                    mainLoan.setConfirmBalance(new BigDecimal(loanBalance));//批复金额
                    mainLoan.setMainLoanBalance(new BigDecimal(loanBalance));//修改总借款金额（应对提交初审之后，还更新借款金额的情况）
                }
            }
            applicationDesc = applicationDesc.replaceAll("\r\n", "<br>");
            mainLoan.setApplicationDesc(applicationDesc);
            riskControlInformation = riskControlInformation.replaceAll("\r\n", "<br>");
            mainLoan.setRiskControlInformation(riskControlInformation);
            loanUseageDesc = loanUseageDesc.replaceAll("\r\n", "<br>");
            mainLoan.setLoanUseageDesc(loanUseageDesc);
            mainLoan.setOfflineApplyCode(offlineApplyCode);

            if (null != inCardId && !"".equals(inCardId)) {
                mainLoan.setInCardId(Long.valueOf(inCardId));//打款卡ID
            }
            if (null != outCardId && !"".equals(outCardId)) {
                mainLoan.setOutCardId(Long.valueOf(outCardId));//还款划扣卡ID
            }
            mainLoan.setLoanApplicationName(loanApplicationName);//借款合同名称=#{贷款用途}-#{产品名称}-#{时长}
            // 【LoanApplication - 结束】

// 【CustomerBasicSnapshot - 开始】***********************************************************************
            CustomerBasicSnapshot basic = customerBasicSnapshotService.getBasicByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
            basic.setMobilePhone(mobilePhone);
            basic.setEmail(email);
            basic.setEducation(education);
            basic.setIsMarried(isMarried);
            basic.setChildStatus(childStatus);
            basic.setAssetsInfo(assetsInfo);
            basic.setMonthlyIncome(monthlyIncome);
            if (null != maxCreditValue && !"".equals(maxCreditValue)) {
                basic.setMaxCreditValue(Long.valueOf(maxCreditValue));
            }
            basic.setZipcode(zipcode);
            // 【CustomerBasicSnapshot - 结束】

// 【UserInfoExt 用户扩展  - 开始】****************************************************************************
            UserInfoExt ext = userInfoExtService.getUserInfoExtById(mainLoan.getUserId());
            ext.setEmail(email);
            ext.setEducationLevel(education);
            ext.setMobileNo(mobilePhone);
            // 【UserInfoExt 用户扩展-结束】

// 【Address - 开始】***********************************************************************************
            //籍贯
            Address bornAddr = addressService.getAddressById(basic.getBornAddr());
            if (null != bornAddr_provence && !"".equals(bornAddr_provence)) {
                bornAddr.setProvince(Long.valueOf(bornAddr_provence));
            }
            if (null != bornAddr_city && !"".equals(bornAddr_city)) {
                bornAddr.setCity(Long.valueOf(bornAddr_city));
            }
            if (null != bornAddr_district && !"".equals(bornAddr_district)) {
                bornAddr.setDistrict(Long.valueOf(bornAddr_district));
            }
            bornAddr.setUserId(mainLoan.getUserId());
            bornAddr.setDetail(bornAddr_detail);
            bornAddr.setType("1");

            //户口所在地
            Address registAddr = addressService.getAddressById(basic.getRegistAddr());
            if (null != registAddr_provence && !"".equals(registAddr_provence)) {
                registAddr.setProvince(Long.valueOf(registAddr_provence));
            }
            if (null != registAddr_city && !"".equals(registAddr_city)) {
                registAddr.setCity(Long.valueOf(registAddr_city));
            }
            if (null != registAddr_district && !"".equals(registAddr_district)) {
                registAddr.setDistrict(Long.valueOf(registAddr_district));
            }
            registAddr.setUserId(mainLoan.getUserId());
            registAddr.setDetail(registAddr_detail);
            registAddr.setType("1");

            //现住址
            Address residenceAddr = addressService.getAddressById(basic.getResidenceAddr());
            if (null != residenceAddr_provence && !"".equals(residenceAddr_provence)) {
                residenceAddr.setProvince(Long.valueOf(residenceAddr_provence));
            }
            if (null != residenceAddr_city && !"".equals(residenceAddr_city)) {
                residenceAddr.setCity(Long.valueOf(residenceAddr_city));
            }
            if (null != residenceAddr_district && !"".equals(residenceAddr_district)) {
                residenceAddr.setDistrict(Long.valueOf(residenceAddr_district));
            }
            residenceAddr.setUserId(mainLoan.getUserId());
            residenceAddr.setDetail(residenceAddr_detail);
            residenceAddr.setType("1");
            // 【Address - 结束】

// 【CustomerWorkSnapshot - 开始】***********************************************************************************************
            CustomerWorkSnapshot work = customerWorkSnapshotService.getWorkByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
            work.setCompanyNature(companyNature);
            work.setCompanyName(companyName);
            work.setCompanyPhone(companyPhone);
            work.setPost(post);
            if (null != joinDate && !"".equals(joinDate)) {
                work.setJoinDate(new SimpleDateFormat("yyyy-MM-dd").parse(joinDate));
            }
            // 【CustomerWorkSnapshot - 结束】

            //单位地址
            Address workingAddr = addressService.getAddressById(work.getWorkingAddr());
            if (null != workingAddr_provence && !"".equals(workingAddr_provence)) {
                workingAddr.setProvince(Long.valueOf(workingAddr_provence));
            }
            if (null != workingAddr_city && !"".equals(workingAddr_city)) {
                workingAddr.setCity(Long.valueOf(workingAddr_city));
            }
            if (null != workingAddr_district && !"".equals(workingAddr_district)) {
                workingAddr.setDistrict(Long.valueOf(workingAddr_district));
            }
            workingAddr.setUserId(mainLoan.getUserId());
            workingAddr.setDetail(workingAddr_detail);
            workingAddr.setType("1");

// 【CustomerContactsSnapshot - 开始】*******************************************************************************************
            List<CustomerContactsSnapshot> contactsList = new ArrayList<CustomerContactsSnapshot>();
            // 联系人【开始】。
            if (null != controlsArray && !"".equals(controlsArray)) {
                JSONArray array = JSONArray.parseArray(controlsArray);
                if (null != array) {
                    for (int i = 0; i < array.size(); i++) {
                        CustomerContactsSnapshot contacts = new CustomerContactsSnapshot();
                        JSONObject object = array.getJSONObject(i);
                        if (null != object) {
                            //关系类型
                            if (null != object.get("relationType") && !"".equals(object.get("relationType"))) {
                                contacts.setRelationType(object.get("relationType").toString());
                            }
                            //关系
                            if (null != object.get("relation") && !"".equals(object.get("relation"))) {
                                contacts.setRelation(object.get("relation").toString());
                            }
                            //姓名
                            if (null != object.get("concactName") && !"".equals(object.get("concactName"))) {
                                contacts.setConcactName(object.get("concactName").toString());
                            }
                            //手机号
                            if (null != object.get("concatPhone") && !"".equals(object.get("concatPhone"))) {
                                contacts.setConcatPhone(object.get("concatPhone").toString());
                            }
                        }
                        contactsList.add(contacts);
                    }
                }
            }
            // 联系人【结束】。
            // 【CustomerContactsSnapshot - 结束】

// 【CustomerCard - 开始】***************************************************************************************************
            CustomerCard card = new CustomerCard();
            if (SubjectTypeEnum.LOAN.getValue().equals(mainLoan.getSubjectType())) {

                if (null != bankCode && !"".equals(bankCode)) {
                    card.setBankCode(Long.valueOf(bankCode));
                }
                card.setRegisteredBank(registeredBank);
                card.setCardCode(cardCode);
                card.setCardcustomerName(cardCustomerName);
                card.setStatus(CustomerCardStatus.NORMAL.getValue());
                card.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
                card.setBelongChannel(CardChannel.LL.getValue());
                card.setUpdateTime(new Date());
                card.setCardType(CardType.FULL_CARD.getValue());
            }
            // 【CustomerCard - 结束】


// 【借款申请费用表 LOAN_APPLICATION_FEES_ITEM - 开始 】**********************************************************************************
            List<LoanApplicationFeesItem> feesItems = new ArrayList<LoanApplicationFeesItem>();
            LoanApplicationFeesItem loanApplicationFeesItem;//[add]

            List<LoanProductFeesItem> productFeesItems = loanProductFeesItemService.getByProductId(Long.valueOf(loanProductId));//[select]
            if (null != productFeesItems && productFeesItems.size() > 0) {
                for (int i = 0; i < productFeesItems.size(); i++) {
                    LoanProductFeesItem productFeesItem = productFeesItems.get(i);
                    FeesItem feesItem = feesItemService.findById(productFeesItem.getFeesItemId());//[select]

                    loanApplicationFeesItem = new LoanApplicationFeesItem();
                    loanApplicationFeesItem.setChargeCycle(productFeesItem.getChargeCycle());//收费周期
                    loanApplicationFeesItem.setWorkflowRatio(productFeesItem.getWorkflowRatio());//平台收取比例
                    loanApplicationFeesItem.setItemName(feesItem.getItemName());//项目名称
                    loanApplicationFeesItem.setItemType(feesItem.getItemType());//费用类别
                    loanApplicationFeesItem.setFeesRate(new BigDecimal(String.valueOf(feesItem.getFeesRate())));//收费比例
                    loanApplicationFeesItem.setRadicesType(feesItem.getRadicesType());//基数
                    loanApplicationFeesItem.setRadiceLogic(feesItem.getRadiceLogic());//自定义基数逻辑
                    loanApplicationFeesItem.setRadiceName(feesItem.getRadiceName());//自定义基数名称

                }
            }

            // 【借款申请费用表 LOAN_APPLICATION_FEES_ITEM - 结束 】

            // 执行保存操作。
            mainLoan = loanApplicationService.saveLoanPart2(mainLoan, bornAddr, registAddr, residenceAddr, workingAddr, basic, ext, work, contactsList, card, feesItems);//main

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 跳转到：添加3页。
     *
     * @param loanApplicationId 借款申请ID
     */
    @RequestMapping(value = "/to_loan_add_part3")
    public ModelAndView to_loan_add_part3(
            @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId, String isCcreditCar) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/loanManage/loan/loan_add_part3");
        return mv;
    }

    /**
     * 跳转到：编辑3页。【main】
     *
     * @param loanApplicationId 借款申请ID
     */
    @RequestMapping(value = "/to_loan_edit_part3")
    public ModelAndView to_loan_edit_part3(
            @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId) {
        ModelAndView mv = new ModelAndView();

        // 返回编辑信息-begin
        MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));//借款信息
        CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//抵押信息
        Address houseAddr = addressService.getAddressById(house.getHouseAddr());//现住址
        mv.addObject("loan", mainLoan);
        mv.addObject("house", house);
        mv.addObject("houseAddr", houseAddr);

        //省份
        List<ProvinceInfo> provinceInfos = provinceInfoService.getAllProvinceInfo();
        mv.addObject("provinceInfos", provinceInfos);

        //单位
        List<CityInfo> houseCitys = cityInfoService.getCityByProvinceIdAndPId(houseAddr.getProvince(), 0l);
        List<CityInfo> houseDistricts = cityInfoService.getCityByProvinceIdAndPId(houseAddr.getProvince(), houseAddr.getCity());
        mv.addObject("houseCitys", houseCitys);
        mv.addObject("houseDistricts", houseDistricts);
        // 返回编辑信息-end

        mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/loanManage/loan/loan_edit_part3");
        return mv;
    }

    /**
     * 执行：保存3页。【main】
     */
    @RequestMapping("/save_loan_part3")
    @ResponseBody
    public Object saveLoanPart3(HttpServletRequest request, HttpSession session,
                                @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
                                @RequestParam(value = "mortgageType", required = false) String mortgageType,//产品类型
                                @RequestParam(value = "houseType", required = false) String houseType,//房屋类型

                                @RequestParam(value = "houseAddr_provence", required = false) String houseAddr_provence,//房屋地址_省
                                @RequestParam(value = "houseAddr_city", required = false) String houseAddr_city,//房屋地址_市
                                @RequestParam(value = "houseAddr_district", required = false) String houseAddr_district,//房屋地址_区
                                @RequestParam(value = "houseAddr_detail", required = false) String houseAddr_detail,//房屋地址_详细地址

                                @RequestParam(value = "houseSize", required = false) String houseSize,//房屋面积
                                @RequestParam(value = "buyDate", required = false) String buyDate,//购买时间
                                @RequestParam(value = "buyValue", required = false) String buyValue,//购买价格
                                @RequestParam(value = "marketValue", required = false) String marketValue,//市场价格
                                @RequestParam(value = "assessValue", required = false) String assessValue,//评估价格
                                @RequestParam(value = "houseCardNumber", required = false) String houseCardNumber,//房产证字号
                                @RequestParam(value = "desc", required = false) String desc) {//备注
        try {
            // 合法性验证。
            if (null == loanApplicationId || "".equals(loanApplicationId)) {
                return returnResultMap(false, null, "check", "借款申请ID不能为空！");
            }

            // 根据ID加载一条借款申请信息。
            MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));//main
            if (null == mainLoan) {
                return returnResultMap(false, null, "check", "借款信息不能为空！");
            }

            // 产品类型
            if (null == mortgageType || "".equals(mortgageType)) {
                return returnResultMap(false, null, "check", "产品类型不能为空！");
            }

            // 抵押信息。
            CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//main
            house.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());//main
            house.setMortgageType(mortgageType);
            house.setHouseType(houseType);
            if (null != houseSize && !"".equals(houseSize)) {
                house.setHouseSize(new BigDecimal(houseSize));
            }
            if (null != buyDate && !"".equals(buyDate)) {
                house.setBuyDate(new SimpleDateFormat("yyyy-MM-dd").parse(buyDate));
            }
            if (null != buyValue && !"".equals(buyValue)) {
                house.setBuyValue(new BigDecimal(buyValue));
            }
            if (null != marketValue && !"".equals(marketValue)) {
                house.setMarketValue(new BigDecimal(marketValue));
            }
            if (null != assessValue && !"".equals(assessValue)) {
                house.setAssessValue(new BigDecimal(assessValue));
            }
            house.setHouseCardNumber(houseCardNumber);
            house.setDesc(desc);

            // 现住址。
            Address houseAddr = addressService.getAddressById(house.getHouseAddr());
            if (null != houseAddr_provence && !"".equals(houseAddr_provence)) {
                houseAddr.setProvince(Long.valueOf(houseAddr_provence));
            }
            if (null != houseAddr_city && !"".equals(houseAddr_city)) {
                houseAddr.setCity(Long.valueOf(houseAddr_city));
            }
            if (null != houseAddr_district && !"".equals(houseAddr_district)) {
                houseAddr.setDistrict(Long.valueOf(houseAddr_district));
            }
            houseAddr.setUserId(mainLoan.getUserId());
            houseAddr.setDetail(houseAddr_detail);
            houseAddr.setType("1");

            // 执行保存操作。
            house = loanApplicationService.saveLoanPart3(houseAddr, house);

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 跳转到：添加4页。
     *
     * @param loanApplicationId 申请ID
     */
    @RequestMapping(value = "/to_loan_add_part4")
    public ModelAndView to_loan_add_part4(@RequestParam(value = "isCode", required = false) String isCode,
                                          @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId
            , String isCcreditCar) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.addObject("isCode", isCode);
        if (null != isCcreditCar && "1".equals(isCcreditCar)) {
            mv.setViewName("jsp/loanManage/loan/loan_people_creditcar_add_part4");
        }else if(null != isCcreditCar && "2".equals(isCcreditCar)){
            mv.setViewName("jsp/loanManage/loan/loan_cash_add_part4");
        } else {
            mv.setViewName("jsp/loanManage/loan/loan_add_part4");
        }

        return mv;
    }

    /**
     * 选择图片【main】
     *
     * @param state
     * @param typeList
     * @param userId
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/uploadSnapshotAdd")
    public ModelAndView uploadSnapshotAdd(@RequestParam(value = "state", required = false) String state,
                                          @RequestParam(value = "typeList", required = false) String typeList,
                                          @RequestParam(value = "userId", required = false) String userId,
                                          @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,
                                          @RequestParam(value = "isCode", required = false) String isCode) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("state", state);
        mv.addObject("typeList", typeList);
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.addObject("isCode", isCode);
        MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));//main
        mv.addObject("loanType", mainLoan.getLoanType());
        mv.setViewName("jsp/loanManage/loan/uploadSnapshotAdd");
        return mv;
    }

    /**
     * 执行：提交初审。【main】
     *
     * @param loanApplicationId 借款申请ID
     */
    @RequestMapping("/save_loan_submit")
    @ResponseBody
    public Object saveLoanSubmit(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId) {
        try {
            // 合法性验证。
            if (null == loanApplicationId || "".equals(loanApplicationId)) {
                return returnResultMap(false, null, "check", "借款申请ID不能为空！");
            }

            // 根据ID加载一条借款申请信息。
            MainLoanApplication loan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
            if (null == loan) {
                return returnResultMap(false, null, "check", "借款信息不能为空！");
            }

            // 借款用途
            if (null == loan.getLoanUseage() || "".equals(loan.getLoanUseage())) {
                return returnResultMap(false, null, "check", "借款用途不能为空！");
            }
            // 借款产品
            if (null == loan.getLoanProductId() || "".equals(loan.getLoanProductId())) {
                return returnResultMap(false, null, "check", "借款产品不能为空！");
            }
            // 借款金额
            if (null == loan.getLoanBalance() || "".equals(loan.getLoanBalance())) {
                return returnResultMap(false, null, "check", "借款金额不能为空！");
            }
            // 描述
            if (null == loan.getApplicationDesc() || "".equals(loan.getApplicationDesc())) {
                return returnResultMap(false, null, "check", "描述不能为空！");
            }
            // 借款用途描述
            if (null == loan.getLoanUseageDesc() || "".equals(loan.getLoanUseageDesc())) {
                return returnResultMap(false, null, "check", "借款用途描述不能为空！");
            }
            if(!loan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())){
                // 借款人手机
                CustomerBasicSnapshot basic = customerBasicSnapshotService.getBasicByMainLoanApplicationId(loan.getMainLoanApplicationId());//main
                if (null == basic.getMobilePhone() || "".equals(basic.getMobilePhone())) {
                    return returnResultMap(false, null, "check", "借款人手机不能为空！");
                }
            }


            if (LoanApplication.SUBJECTTYPE_LOANMARK.equals(loan.getSubjectType())) {// 借款标
                CustomerCard card = customerCardService.findById(loan.getInCardId());
                // 开户行
                if (null == card.getBankCode() || "".equals(card.getBankCode())) {
                    return returnResultMap(false, null, "check", "开户行不能为空！");
                }
                // 详细地址17390
//                if (null == card.getRegisteredBank() || "".equals(card.getRegisteredBank())) {
//                    return returnResultMap(false, null, "check", "详细地址不能为空！");
//                }
                // 卡号
                if (null == card.getCardCode() || "".equals(card.getCardCode())) {
                    return returnResultMap(false, null, "check", "卡号不能为空！");
                }
                // 开户名
                if (null == card.getCardcustomerName() || "".equals(card.getCardcustomerName())) {
                    return returnResultMap(false, null, "check", "开户名不能为空！");
                }
            } else {// 债权标
                // 打款卡
                if (null == loan.getInCardId() || "".equals(loan.getInCardId())) {
                    return returnResultMap(false, null, "check", "打款卡不能为空！");
                }
                // 划扣卡
                if (null == loan.getOutCardId() || "".equals(loan.getOutCardId())) {
                    return returnResultMap(false, null, "check", "划扣卡不能为空！");
                }
            }
            if (!loan.getLoanType().equals(LoanTypeEnum.LOANTYPE_CREDIT.getValue())) {
                if (!LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loan.getLoanType())&&
                        !LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loan.getLoanType())
                        ) {
                    // 抵押类型
                    CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByMainLoanApplicationId(loan.getMainLoanApplicationId());//main
                    if (null == house.getMortgageType() || "".equals(house.getMortgageType())) {
                        return returnResultMap(false, null, "check", "抵押类型不能为空！");
                    }
                }


            }

            // 更改借款申请状态
            loan.setApplicationState(LoanApplication.APPLICATIONSTATE_ISSUING_AUDIT);

            String rootPath = request.getSession().getServletContext().getRealPath("");
            loanApplicationService.submitLoanAppcalication(loan, rootPath);//main
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 保存【main】
     *
     * @param request
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/saveUploadSnapshot")
    @ResponseBody
    public Object saveUploadSnapshot(HttpServletRequest request, HttpSession session) throws IOException {
        String rootPath = request.getSession().getServletContext().getRealPath("");
        String msgName = request.getParameter("msgName");
        String typeList = request.getParameter("typeList");
        String state = request.getParameter("state");
        String isCode = request.getParameter("isCode");
        String loanApplicationId = request.getParameter("loanApplicationId");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFiles("imgFile").get(0);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("resultState", "success");
        if (file != null) {
            return JSONObject.toJSONString(loanApplicationService.saveUploadSnapshot(loanApplicationId, file, state, msgName, typeList, rootPath, isCode));//main
        }
        return JSONObject.toJSONString(resultMap);
    }

    @RequestMapping(value = "/toShowBigPicture")
    public ModelAndView toShowBigPicture(@RequestParam(value = "cusId", required = false) Long cusId) {
        ModelAndView mv = new ModelAndView();
        Attachment atta = loanApplicationService.getAttachmentBycusId(cusId);
        mv.addObject("cusId", cusId);
        mv.addObject("url", atta.getUrl());
        mv.addObject("imgName", atta.getFileName());
        mv.setViewName("jsp/loanManage/loan/showBigPicture");
        return mv;
    }

    //by mainid
    @RequestMapping(value = "/imgPaging")
    @ResponseBody
    public Object imgPaging(@RequestParam(value = "pageState", required = false) String pageState,
                            @RequestParam(value = "cusId", required = false) Long cusId) {
        //todo查库 一个List 然后用cusId判断 在List中的为然后 在进行下一步的操作
        CustomerUploadSnapshot cus = loanApplicationService.getcustomerUploadSnapshotDetails(cusId);
        List<CustomerUploadSnapshot> cusList = loanApplicationService.getCustomerUploadSnapshotListByMainId(cus.getMainLoanApplicationId(), cus.getType());//main
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cusId", cusId);
        for (int i = 0; i < cusList.size(); i++) {
            if (cusId.equals(cusList.get(i).getSnapshotId())) {
                if ("0".equals(pageState)) {
                    if ((i - 1) >= 0) {
                        Attachment atta = loanApplicationService.getAttachmentBycusId(cusList.get(i - 1).getSnapshotId());
                        map.put("url", atta.getUrl());
                        map.put("cusId", cusList.get(i - 1).getSnapshotId());
                        map.put("resultState", "success");
                        break;
                    } else {
                        map.put("resultState", "noUpper");
                        break;
                    }
                } else {
                    if ((i + 1) < cusList.size()) {
                        Attachment atta = loanApplicationService.getAttachmentBycusId(cusList.get(i + 1).getSnapshotId());
                        map.put("url", atta.getUrl());
                        map.put("cusId", cusList.get(i + 1).getSnapshotId());
                        map.put("resultState", "success");
                        break;
                    } else {
                        map.put("resultState", "noNext");
                        break;
                    }
                }
            }
        }
        return map;
    }

    @RequestMapping(value = "/delImg")
    @ResponseBody
    public Object delImg(HttpServletRequest request, HttpSession session,
                         @RequestParam(value = "cusId", required = false) Long cusId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rootPath = request.getSession().getServletContext().getRealPath("");
            Attachment atta = loanApplicationService.getAttachmentBycusId(cusId);
            String url = atta.getUrl();
            loanApplicationService.delImg(cusId, CustomerUploadSnapshot.CUSTOMERUPLOADSNAPSHOT_DELETED, atta, rootPath);
            map.put("divId", url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")));
            map.put("result", "success");
        } catch (Exception e) {

            e.printStackTrace();
            map.put("message", e.getMessage());
            map.put("result", "error");
        }
        return map;
    }


    /**
     * 借款申请详情
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/showLoanDetail")
    public String showLoanDetail(HttpServletRequest request, Long loanApplicationId) {
        //获取借款申请详情
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        request.setAttribute("loanApplication", loanApplication);
        request.setAttribute("loanApplicationState", LoanApplicationStateEnum.values());
        return "jsp/loanManage/loan/loanApplicationDetail";
    }

    /**
     * 定向设置详情
     *
     * @param request
     * @param loanApplicationId
     * @return
     * @author wangyadong
     */
    @RequestMapping(value = "/showLoanOrient")
    public String showLoanOrient(HttpServletRequest request, Long loanApplicationId) {
        //获取借款申请详情
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
        //countOtypeByLoanApplicationId

        String oType = loanApplicationService.countOtypeByLoanApplicationId(loanApplicationId);
        if (oType != null) {
            if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_PASSWORD.getValue().equals(oType)) {
                LoanOrientation loanOrientation1 = userInfoServices.countLoanOrientation(loanApplicationId);
                request.setAttribute("loanOrientation1", loanOrientation1);
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_USER.getValue().equals(oType)) {
                List<LoanOrientation> loanOrientation2 = userInfoServices.countLoanOrientationList(loanApplicationId);
                request.setAttribute("loanOrientation2", loanOrientation2);
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_NEWUSER.getValue().equals(oType)) {
                request.setAttribute("loanOrientation3", "3");
            }
            List<LoanOrientation> userInfo = userInfoServices.getUserInfoByLoanApplicationId(loanApplicationId);

            request.setAttribute("userInfo", userInfo);
        }


        request.setAttribute("loanApplication", loanApplication);
        return "jsp/loanManage/loan/loanApplicationOrient";
    }

    /**
     * 根据出借订单ID，获得借款申请ID，债权类型
     * 根据出借订单ID，获得借款申请ID，债权类型【main预留】
     *
     * @param request
     * @param lendOrderId
     * @returnwo
     */
    @RequestMapping(value = "/showLoanDetailByLendOrderId")
    public String showLoanDetailByLendOrderId(HttpServletRequest request, Long lendOrderId) {
        //获取借款申请详情
        LendOrderDetailVO detailVO_2AZQ = lendOrderService.getOrderDetail2AZQ(lendOrderId);
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(Long.valueOf(detailVO_2AZQ.getLoanApplicationId()));
        request.setAttribute("loanApplication", loanApplication);
        request.setAttribute("loanApplicationState", LoanApplicationStateEnum.values());
        return "jsp/loanManage/loan/loanApplicationDetail";
    }


    /**
     * 标的信息【main预留】
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/showConcreteDetails")
    public String showConcreteDetails(HttpServletRequest request, Long loanApplicationId) {
        //------------------------组织标的的显示参数
        //借款申请信息
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationVoById(loanApplicationId);
        request.setAttribute("loanApplication", loanApplication);
        //借款来源(暂时只处理渠道)
        if (LoanApplication.CHANNEL_CHANNEL.equals(loanApplication.getChannel())) {
            String bondSourceUserStr = "";
            BondSource bondSource = bondSourceService.getBondSourceByBondSourceId(loanApplication.getChannelId());
            if (bondSource != null) {
                if (loanApplication.getOriginalUserId() != null) {
                    BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserByUserSourceId(loanApplication.getOriginalUserId());
                    bondSourceUserStr = "-" + bondSourceUser.getBondName();
                }
                request.setAttribute("sourceStr", "渠道-" + bondSource.getSourceName() + bondSourceUserStr);
            }
        } else {
            //门店暂不处理
            Store store = storeService.getStoreById(loanApplication.getChannelId());
            if (null != store) {
                request.setAttribute("sourceStr", "门店-" + store.getStoreName());
            }
        }
        //借款产品
        if (loanApplication.getLoanProductId() != null) {
            //草稿状态没有借款产品
            LoanProduct product = loanProductService.findById(loanApplication.getLoanProductId());
            request.setAttribute("product", product);
        }

        //借款人基本信息
        CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByLoanApplicationId(loanApplicationId);
        if (basicSnapshot != null) {
            request.setAttribute("customer", basicSnapshot);
            //居住地
            Address adress = addressService.getAddressVOById(basicSnapshot.getResidenceAddr());
            request.setAttribute("customerAdress", adress);
        }
        //工作信息
        CustomerWorkSnapshot work = customerWorkSnapshotService.getWorkByLoanApplicationId(loanApplicationId);
        if (work != null) {
            request.setAttribute("work", work);
            Address workAdress = addressService.getAddressVOById(work.getWorkingAddr());
            request.setAttribute("workAdress", workAdress);
        }
        //联系人信息
        List<CustomerContactsSnapshot> contacts = customerContactsSnapshotService.getContactsByloanApplicationId(loanApplicationId);
        request.setAttribute("contacts", contacts);
        //放款卡
        if (loanApplication.getInCardId() != null) {

            CustomerCard inCard = customerCardService.findById(loanApplication.getInCardId());
            request.setAttribute("inCard", inCard);
            //划扣卡
            if (loanApplication.getInCardId() == loanApplication.getOutCardId()) {
                request.setAttribute("outCard", inCard);
            } else {
                if (null != loanApplication.getOutCardId()) {
                    CustomerCard outCard = customerCardService.findById(loanApplication.getOutCardId());
                    request.setAttribute("outCard", outCard);
                }
            }
        }
        List<VerifyVO> loanVerifyInfo = verifyService.getVerifyByApplicationId(loanApplicationId);
        request.setAttribute("loanVerifyInfo", loanVerifyInfo);
        request.setAttribute("verifyType", VerifyType.values());
        return "jsp/loanManage/loan/bidInfo";
    }


    /**
     * 抵押信息【main预留】
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/showChargeInfo")
    public String showChargeInfo(HttpServletRequest request, Long loanApplicationId) {

        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);

        if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplication.getLoanType())) {
            CustomerCarSnapshot car = customerCarSnapshotService.getCarByLoanApplicationId(loanApplicationId);
            request.setAttribute("car", car);
        } else {
            //------------------------组织标的的显示参数
            CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByLoanApplicationId(loanApplicationId);
            request.setAttribute("house", house);
            if (house != null) {
                Address houseAdress = addressService.getAddressVOById(house.getHouseAddr());
                request.setAttribute("houseAdress", houseAdress);
            }
        }

        return "jsp/loanManage/loan/loanChargeInfo";
    }

    /**
     * 跳转至出借人列表页面
     *
     * @return
     */
    @RequestMapping(value = "/showLendList")
    public String showLendList(HttpServletRequest request, Long loanApplicationId) {
        request.setAttribute("creditorRightStats", CreditorRightsConstants.CreditorRightsStateEnum.values());
        request.setAttribute("loanApplicationId", loanApplicationId);
        return "jsp/loanManage/loan/lendList";
    }

    /**
     * 跳转还款情况页面【main预留】
     *
     * @return
     */
    @RequestMapping(value = "/showRepaymentList")
    public String showRepaymentList(HttpServletRequest request, Long loanApplicationId) {
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        request.setAttribute("repaymentPlanState", RepaymentPlanStateEnum.values());
        //借款申请对应的还款计划
        List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationId, ChannelTypeEnum.ONLINE);
        List<RepaymentVO> list = new ArrayList<RepaymentVO>();
        List<LoanFeesDetail> loanFeesDetail = loanFeesDetailService.getLoanFeesDetailByLoanId(loanApplicationId);
        for (RepaymentPlan plan : repaymentPlanList) {
            RepaymentVO rvo = new RepaymentVO();
            //获取借款申请费用明细
            for (LoanFeesDetail lfd : loanFeesDetail) {
                if (lfd.getSectionCode() == plan.getSectionCode()) {
                    rvo.setShouldFee(lfd.getFees2());
                    rvo.setFactFee(lfd.getPaidFees());
                }
            }
            //获取罚息信息
            BigDecimal defaultInterest = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(plan.getRepaymentPlanId());
            rvo.setShouldFaxi(defaultInterest);
            BigDecimal defaultInterestPaid = defaultInterestDetailService.getDefaultInterestPaidByRepaymentPlanId(plan.getRepaymentPlanId());
            rvo.setFactfaxi(defaultInterestPaid);
            //获取最近还款日期
            RepaymentRecord recentRepaymentRecord = repaymentRecordService.getRecentRepaymentRecordByRepaymentId(plan.getRepaymentPlanId());
            if (recentRepaymentRecord != null) {
                rvo.setRecentRepaymetDate(recentRepaymentRecord.getFaceDate());
            }
            rvo.setRepaymentPlanId(plan.getRepaymentPlanId());
            plan.setFactBalance(plan.getFactCalital().add(plan.getFactInterest()).add(rvo.getFactFee()).add(rvo.getFactfaxi()));
            plan.setShouldBalance2(plan.getShouldCapital2().add(plan.getShouldInterest2()).add(rvo.getShouldFee()).add(rvo.getShouldFaxi()));
            list.add(rvo);
        }
        request.setAttribute("repaymentVos", list);
        request.setAttribute("repaymentPlanList", repaymentPlanList);

        //未还本金
        BigDecimal repaymentCapital = repaymentPlanService.getRepaymentCapitalByLoanApplicationId(loanApplicationId);
        request.setAttribute("repaymentCapital", repaymentCapital);
        //待还利息
        BigDecimal replaymentInterest = repaymentPlanService.getRepaymentInterestByLoanApplicationId(loanApplicationId);
        request.setAttribute("replaymentInterest", replaymentInterest);
        //待缴费用
        BigDecimal loanFeeNopaied = loanFeesDetailService.getLoanFeeNoPaied(loanApplication.getUserId());
        request.setAttribute("loanFeeNopaied", loanFeeNopaied);
        //待还罚息
        BigDecimal interestPaid = defaultInterestDetailService.getDefaultInterestByUserId(loanApplication.getUserId());
        request.setAttribute("interestPaid", interestPaid);
        //待还总金额
        request.setAttribute("allShouldPaid", repaymentCapital.add(replaymentInterest).add(loanFeeNopaied).add(interestPaid));
        return "jsp/loanManage/loan/repayMentList";
    }


    /**
     * 跳转还款计划【main预留】
     *
     * @return
     */
    @RequestMapping(value = "/repaymentPlan")
    public String repaymentPlan(HttpServletRequest request, Long loanApplicationId) {
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        request.setAttribute("repaymentPlanState", RepaymentPlanStateEnum.values());
        request.setAttribute("loanApplication", loanApplication);
        //借款申请对应的还款计划
        List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationId, ChannelTypeEnum.ONLINE);
        request.setAttribute("repaymentPlanList", repaymentPlanList);

        List<RepaymentVO> list = new ArrayList<RepaymentVO>();
        List<LoanFeesDetail> loanFeesDetail = loanFeesDetailService.getLoanFeesDetailByLoanId(loanApplicationId);
        for (RepaymentPlan plan : repaymentPlanList) {
            RepaymentVO rvo = new RepaymentVO();
            //获取借款申请费用明细
            for (LoanFeesDetail lfd : loanFeesDetail) {
                if (lfd.getSectionCode() == plan.getSectionCode()) {
                    rvo.setShouldFee(lfd.getFees2());
                }
            }
            //获取罚息信息
            BigDecimal defaultInterest = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(plan.getRepaymentPlanId());
            rvo.setShouldFaxi(defaultInterest);
            rvo.setRepaymentPlanId(plan.getRepaymentPlanId());
            plan.setFactBalance(plan.getFactCalital().add(plan.getFactInterest()).add(rvo.getFactFee()).add(rvo.getFactfaxi()));
            plan.setShouldBalance2(plan.getShouldCapital2().add(plan.getShouldInterest2()).add(rvo.getShouldFee()).add(rvo.getShouldFaxi()));
            list.add(rvo);
        }
        request.setAttribute("repaymentVos", list);
        //费用项
        request.setAttribute("feePoint", FeesPointEnum.values());
        List<LoanApplicationFeesItem> loanApplicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(loanApplicationId);
        for (LoanApplicationFeesItem item : loanApplicationFeesItems) {
            item.setAmount(loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(item, loanApplication.getConfirmBalance(), loanApplication.getInterestBalance(), BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        }
        request.setAttribute("loanApplicationFeesItems", loanApplicationFeesItems);
        return "jsp/loanManage/loan/repaymentPlan";
    }

    /**
     * 出借人投标列表【main预留】
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getLender")
    @ResponseBody
    public Object getLender(HttpServletRequest request, Long loanApplicationId,
                            @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                            @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        //获取借款申请状态
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        if ("345".indexOf(loanApplication.getApplicationState()) != -1) {
            //发标中、放款审核中、待放款
            //查询出借订单明细表中 投标中 的状态数据
            Pagination<LenderVO> result = lendOrderBidDetailService.findLendOrderDetail(pageNo, pageSize, loanApplicationId, LendOrderBidStatusEnum.BIDING);
            return result;
        } else {
            //6、7、8 还款中、已结清、已结清(提前还贷)
            //到债权表查询  已生效，还款中 状态的数据
            Pagination<LenderVO> lenderList = creditorRightsService.getLenderListByApplicationId(pageNo, pageSize, loanApplicationId,
                    CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE, CreditorRightsConstants.CreditorRightsStateEnum.COMPLETE,
                    CreditorRightsConstants.CreditorRightsStateEnum.EARLYCOMPLETE);
            return lenderList;
        }
    }

    /**
     * 相关附件信息页面
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/showAttachment")
    public Object showAttachment(HttpServletRequest request, Long loanApplicationId) {
        request.setAttribute("loanApplicationId", loanApplicationId);
        return "jsp/loanManage/loan/showAttachment";
    }

    /**
     * 相关附件树【main预留】
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/attachment")
    @ResponseBody
    public Object attachment(HttpServletRequest request, Long loanApplicationId) {
        List<CustomerUploadSnapshot> uploadSnapshots = loanApplicationService.getcustomerUploadSnapshotList(loanApplicationId, null);
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
        List<TreeNodes> list = convertTreeNodes(uploadSnapshots, loanApplication.getLoanType());
        return new EasyUITreeResponse(list);
    }

    /**
     * 相关附件具体信息【main预留】
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/detailAttachment")
    public Object detailAttachment(HttpServletRequest request, Long loanApplicationId, String type) {
        List<CustomerUploadSnapshotVO> result = new ArrayList<CustomerUploadSnapshotVO>();
        List<CustomerUploadSnapshotVO> uploadSnapshotVOs = loanApplicationService.getcustomerUploadAttachment(loanApplicationId, AttachmentIsCode.NO_CODE.getValue());
        for (CustomerUploadSnapshotVO customerUploadSnapshotVO : uploadSnapshotVOs) {
            if (customerUploadSnapshotVO.getType().equals(type)) {
                result.add(customerUploadSnapshotVO);
            }
        }
        request.setAttribute("result", result);
        return "jsp/loanManage/loan/detailAttachment";
    }

    private Node covert(CustomerUploadSnapshot category, String loanType) {
        Node node = null;

        if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanType)) {
            node = new Node(category.getType(), EnterpriseCarLoanSnapshotTypeEnum.getEnterpriseCarLoanSnapshotTypeByValue(category.getType()).getDesc());
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType)) {
            node = new Node(category.getType(), EnterpriseCreditSnapshotTypeEnum.getEnterpriseCreditSnapshotTypeByValue(category.getType()).getDesc());
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanType)) {
            node = new Node(category.getType(), EnterpriseFactoringSnapshotTypeEnum.getEnterpriseFactoringSnapshotTypeByValue(category.getType()).getDesc());
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)) {
            node = new Node(category.getType(), EnterpriseFoundationSnapshotTypeEnum.getEnterpriseFoundationSnapshotTypeByValue(category.getType()).getDesc());
        } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)) {
            node = new Node(category.getType(), EnterprisePledgeSnapshotTypeEnum.getEnterprisePledgeSnapshotTypeByValue(category.getType()).getDesc());
        }  else if (LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanType)) {
            node = new Node(category.getType(), CashCreditTypeEnum.byValue(category.getType()).getDesc());
        } else {
            node = new Node(category.getType(), CustomerUploadSnapshotTypeEnum.getCustomerUploadSnapshotTypeByValue(category.getType()).getDesc());
        }
        node.addAttributes("loanApplicationId", category.getLoanApplicationId());
        return node;
    }

    /**
     * 生成树
     *
     * @param uploadSnapshots
     * @return
     */
    private List<TreeNodes> convertTreeNodes(
            List<CustomerUploadSnapshot> uploadSnapshots,
            String loanType) {
        List<TreeNodes> list = new ArrayList<TreeNodes>();
        Map<String, List<Node>> map = new HashMap<String, List<Node>>();

        List<Node> children = new ArrayList<Node>();
        TreeNodes treeNodes = new TreeNodes("-1", "附件列表", children);
        list.add(treeNodes);
        map.put("-1", children);
        for (int i = 0; i < uploadSnapshots.size(); i++) {
            Node covert = covert(uploadSnapshots.get(i), loanType);
            List<Node> nodes = map.get("-1");
            List<Node> nodes_ = map.get(uploadSnapshots.get(i).getType());
            List<Node> node = new ArrayList<Node>();
            if (nodes_ == null) {
                node.add(covert);
                nodes.add(covert);
            }
            map.put(uploadSnapshots.get(i).getType(), node);
        }
        return list;
    }


    public static class EasyUITreeResponse {

        public List<TreeNodes> nodes;

        public EasyUITreeResponse(List<TreeNodes> nodes) {
            super();
            this.nodes = nodes;
        }

    }

    public static class TreeNodes {

        public String id;
        public String text;
        public String iconCls = "icon-reload";
        public List<Node> children;

        public TreeNodes(String id, String text, List<Node> children) {
            super();
            this.id = id;
            this.text = text;
            this.children = children;
        }
    }

    public static class Node {
        public String id;
        public String text;
        public List<Node> children = new ArrayList<Node>();

        public List<Node> getChildren() {
            return children;
        }

        public void setChildren(List<Node> children) {
            this.children = children;
        }

        private Map<String, Object> attributes = new HashMap<String, Object>();

        public Node(String id, String text) {
            super();
            this.id = id;
            this.text = text;
        }

        public Node(Long id, String text) {
            super();
            this.id = String.valueOf(id);
            this.text = text;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void addAttributes(String key, Object value) {
            attributes.put(key, value);
        }

    }

    /**
     * to发标描述编辑
     *
     * @return
     */
    @RequestMapping(value = "/toLoanAppPublishDescEdit")
    public ModelAndView toLoanAppPublishReview() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/loanManage/loan/loanAppPublishDescEdit");
        return mv;
    }

    //编辑发标描述列表
    @RequestMapping(value = "/loanAppPublishDescEditList")
    @ResponseBody
    public Object loanAppPublishDescEditList(@RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                             @RequestParam(value = "page", defaultValue = "1") int pageNum, LoanApplicationExtOne loanApplicationExtOne) {
        return loanApplicationService.getAllLoanAppPublishDescEditList(pageNum, pageSize, loanApplicationExtOne);
    }

    //编辑发标描述列表,待发标列表
    @RequestMapping(value = "/getLoanAppPagingByMainId")
    @ResponseBody
    public Object getLoanAppPagingByMainId(@RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "page", defaultValue = "1") int pageNum, LoanApplicationExtOne loanApplicationExtOne) {
        return loanApplicationService.getLoanAppPagingByMainId(pageNum, pageSize, loanApplicationExtOne);
    }

    /**
     * 跳转到修改附件【main】
     *
     * @return
     */
    @RequestMapping(value = "/toSnapshotEdit")
    public ModelAndView toSnapshotEdit(HttpServletRequest request,
                                       @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId,
                                       @RequestParam(value = "isCcreditCar", required = false) String isCcreditCar) {
        ModelAndView mv = new ModelAndView();
        String isCode = request.getParameter("isCode");

        List<CustomerUploadSnapshotVO> cusvoList = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId, isCode);//main
        for (CustomerUploadSnapshotVO cusvo : cusvoList) {
            cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/") + 1, cusvo.getAttachment().getUrl().lastIndexOf(".")));

        }
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationVoByMainId(loanApplicationId);
        mv.addObject("cusvoList", cusvoList);
        mv.addObject("loanApplicationId", loanApplicationId);
        if(null!=isCcreditCar){
        	mv.setViewName("jsp/loanManage/loan/snapshotCreditCarEdit");
        }else{
		 if(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplication.getLoanType())){
            mv.setViewName("jsp/loanManage/loan/snapshot_cash_Edit");
        }else{
			mv.setViewName("jsp/loanManage/loan/snapshotEdit");
		}
        
        }
        return mv;
        
    }

    @RequestMapping("/toShowRepaymentPlan")
    public ModelAndView toShowRepaymentPlan(@RequestParam(value = "balance", required = true) BigDecimal balance,
                                            @RequestParam(value = "loanProductId", required = true) int loanProductId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("loanProductId", loanProductId);
        modelAndView.addObject("balance", balance);
        modelAndView.setViewName("jsp/loanManage/loan/showRepaymentPlan");
        return modelAndView;

    }

    @RequestMapping("/showRepaymentPlan")
    @ResponseBody
    public Object showRepaymentPlan(@RequestParam(value = "balance", required = true) BigDecimal balance,
                                    @RequestParam(value = "loanProductId", required = true) int loanProductId) {

        return loanProductService.repaymentPlan(loanProductId, balance);

    }

    /**
     * @param loanApplicationId test 定向设置 可以定向设置密码 和定向用户
     * @return
     * @author wangyadong
     */
    @RequestMapping("/showOrientByLoanApplication")
    public ModelAndView showOrientByLoanApplication(Long loanApplicationId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String oType = loanApplicationService.countOtypeByLoanApplicationId(loanApplicationId);
        if (oType != null && !oType.equals("0")) {
            if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_PASSWORD.getValue().equals(oType)) {//定向密码
                LoanOrientation loanOrientation1 = userInfoServices.countLoanOrientation(loanApplicationId);
                request.setAttribute("loanOrientation1", loanOrientation1);
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_USER.getValue().equals(oType)) {
                List<LoanOrientation> loanOrientation2 = userInfoServices.countLoanOrientationList(loanApplicationId);
                request.setAttribute("loanOrientation2", loanOrientation2);
            } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_NEWUSER.getValue().equals(oType)) {
                request.setAttribute("loanOrientation3", "newUser");
            }
            List<LoanOrientation> userInfo = userInfoServices.getUserInfoByLoanApplicationId(loanApplicationId);
            request.setAttribute("userInfo", userInfo);
        }
        mv.setViewName("jsp/loanManage/loan/showOrientByLoanApplication");

        return mv;
    }

    /**
     * 执行自动投标：启用、关闭
     *
     * @param request
     * @param session
     * @param mainLoanApplicationId 主借款申请ID
     * @param status                （0关闭的；1开启的）
     * @return
     */
    @RequestMapping("/doAutoPublish")
    @ResponseBody
    public Object doAutoPublish(HttpServletRequest request, HttpSession session,
                                @RequestParam(value = "mainLoanApplicationId", required = false) Long mainLoanApplicationId,
                                @RequestParam(value = "status", required = false) String status) {
        try {
            // 合法性验证
            if (null == mainLoanApplicationId) {
                return returnResultMap(false, null, "check", "主借款申请ID不能为空");
            }
            if (null == status || "".equals(status)) {
                return returnResultMap(false, null, "check", "状态不能为空");
            } else if (!"0".equals(status) && !"1".equals(status)) {
                return returnResultMap(false, null, "check", "状态值无效");
            }

            // 验证是否满足开启条件:
            // 根据主标ID,查询主标状态是否为发标完成；
            // 如果是，验证是否还有预热中的子标；
            // 如果没有，提示不能开启。
            if ("1".equals(status)) {//[开启操作]

                MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(mainLoanApplicationId);
                if ("2".equals(mainLoanApplication.getMainState())) {//[发标完成]
                    List<LoanApplication> loanApplicationList = loanApplicationService.selectLoanByAuto(mainLoanApplicationId);
                    if (null == loanApplicationList || loanApplicationList.size() == 0) {
                        return returnResultMap(false, null, "check", "不符合开启条件，不能开启！");
                    }
                }
            }

            // 返回结果
            Map<String, Object> main = new HashMap<String, Object>();
            main.put("mainAutoPublish", status);
            main.put("mainLoanApplicationId", mainLoanApplicationId);
            mainLoanApplicationService.update(main);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

//    to_drop_loan?loanApplicationId=' + selRow.mainLoanApplicationId+'&r=' + Math.random();

    /**
     * 执行流标操作-跳转
     *
     * @param request
     * @param session
     * @param mainLoanApplicationId 主借款申请ID
     * @param status                （0关闭的；1开启的）
     * @return
     */
    @RequestMapping("/to_drop_loan")
    public Object to_drop_loan(HttpServletRequest request,
                               @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
        request.setAttribute("loanApplicationId", loanApplicationId);
        return "jsp/loanManage/loan/failReason";
    }

    /**
     * 执行流标操作
     *
     * @param request
     * @param session
     * @param mainLoanApplicationId 主借款申请ID
     * @param status                （0关闭的；1开启的）
     * @return
     */
    @RequestMapping("/drop_loan")
    @ResponseBody
    public Object drop_loan(HttpServletRequest request,
                            @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId,
                            @RequestParam(value = "logDesc", required = false) String logDesc) {
        try {
            Date now = new Date();
            LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
            loanApplicationService.doNoticeFailLoan(loanApplication, now, logDesc);
        } catch (Exception e) {
            logger.error("流标失败，失败原因：", e);
            return returnResultMap(false, null, null, e.getMessage());
        }

        return returnResultMap(true, null, null, null);
    }


    /**
     * 执行：信用车贷保存2页。【main】
     */
    @RequestMapping("/saveLoanPeopleCreditCarPart2")
    @ResponseBody
    public Object saveLoanPeopleCreditCarPart2(
            HttpServletRequest request, HttpSession session,
            // 借款信息
            @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
            @RequestParam(value = "loanUseage", required = false) String loanUseage,//借款用途
            @RequestParam(value = "loanUseageDesc", required = false) String loanUseageDesc,//借款用途描述
            @RequestParam(value = "loanProductId", required = false) String loanProductId,//借款产品
            @RequestParam(value = "loanBalance", required = false) String loanBalance,//借款金额
            @RequestParam(value = "applicationDesc", required = false) String applicationDesc,//描述
            @RequestParam(value = "offlineApplyCode", required = false) String offlineApplyCode,//线下编号
            //项目地区
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone,//借款人手机号
            @RequestParam(value = "monthlyIncome", required = false) String monthlyIncome,//月均收入
            @RequestParam(value = "residenceAddr_detail", required = false) String residenceAddr_detail,//现住址_详细地址
            @RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,//借款合同名称
            //风控步骤
            @RequestParam(value = "riskControlInformation", required = false) String riskControlInformation,//风险控制信息
            // 银行卡信息
            @RequestParam(value = "bankCode", required = false) String bankCode,//所属银行
            @RequestParam(value = "cardCode", required = false) String cardCode,//卡号
            @RequestParam(value = "cardCustomerName", required = false) String cardCustomerName,//开户名
            @RequestParam(value = "registeredBank", required = false) String registeredBank,  //开户行
            //现地址
            @RequestParam(value = "residenceAddr_provence", required = false) String residenceAddr_provence,//现住址_省
            @RequestParam(value = "residenceAddr_city", required = false) String residenceAddr_city,//现住址_市
            @RequestParam(value = "residenceAddr_district", required = false) String residenceAddr_district,//现住址_区
            //抵押信息
            @RequestParam(value = "carModel", required = false) String carModel,//车辆类型
            @RequestParam(value = "carMoney", required = false) BigDecimal carMoney,//车辆金额
            @RequestParam(value = "mileage", required = false) int mileage,//车辆里程
            @RequestParam(value = "appraisal", required = false) BigDecimal appraisal,//车辆估值
            @RequestParam(value = "originalPrice", required = false) BigDecimal originalPrice,//车辆价格
            @RequestParam(value = "buyTime", required = false) String buyTime//车辆购买时间



    ) {
        try {
            // 合法性验证。
            if (null == loanApplicationId || "".equals(loanApplicationId)) {
                return returnResultMap(false, null, "check", "借款申请ID不能为空！");
            }
            // 根据ID加载一条借款申请信息
            MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
            if (null == mainLoan) {
                return returnResultMap(false, null, "check", "借款信息不能为空！");
            }
            // 借款用途
            if (null == loanUseage || "".equals(loanUseage)) {
                return returnResultMap(false, null, "check", "借款用途不能为空！");
            }
            // 借款产品
            if (null == loanProductId || "".equals(loanProductId)) {
                return returnResultMap(false, null, "check", "借款产品不能为空！");
            }
            // 借款金额
            if (null == loanBalance || "".equals(loanBalance)) {
                return returnResultMap(false, null, "check", "借款金额不能为空！");
            }
            // 描述
            if (null == applicationDesc || "".equals(applicationDesc)) {
                return returnResultMap(false, null, "check", "描述不能为空！");
            }
            // 风险控制信息
            if (null == riskControlInformation || "".equals(riskControlInformation)) {
                return returnResultMap(false, null, "check", "风险控制信息不能为空！");
            }
            // 借款用途描述
            if (null == loanUseageDesc || "".equals(loanUseageDesc)) {
                return returnResultMap(false, null, "check", "借款用途描述不能为空！");
            }
            // 借款人手机
            if (null == mobilePhone || "".equals(mobilePhone)) {
                return returnResultMap(false, null, "check", "借款人手机号不能为空！");
            } else {
                if (!StringUtils.isPattern("(^1[3|4|5|7|8][0-9]{9}$)", mobilePhone)) {
                    return returnResultMap(false, null, "check", "借款人手机号格式不正确！");
                }
            }

            // 开户行
            if (null == bankCode || "".equals(bankCode)) {
                return returnResultMap(false, null, "check", "开户行不能为空！");
            }
            // 详细地址
            if (null == registeredBank || "".equals(registeredBank)) {
                return returnResultMap(false, null, "check", "详细地址不能为空！");
            }
            // 卡号
            if (null == cardCode || "".equals(cardCode)) {
                return returnResultMap(false, null, "check", "卡号不能为空！");
            } else {
                cardCode = cardCode.replace(" ", "");
                if (cardCode.length() < 16 || cardCode.length() > 19 || !StringUtils.isPattern("^([0-9]+)$", cardCode)) {
                    return returnResultMap(false, null, "check", "银行卡号格式不正确！");
                }
            }
            // 开户名
            if (null == cardCustomerName || "".equals(cardCustomerName)) {
                return returnResultMap(false, null, "check", "开户名不能为空！");
            }
            //抵押信息开始
            // 购买时间
            CustomerCarSnapshot customerCarSnapshot = new CustomerCarSnapshot();
            if (null != buyTime && !"".equals(buyTime)) {
                customerCarSnapshot.setBuyTime(new SimpleDateFormat("yyyy-MM-dd").parse(buyTime));//购买时间
            } else {
                return returnResultMap(false, null, "check", "车辆购买时间不可以为空！");
            }
            //车辆行驶里程数
            if (mileage > 0) {
                customerCarSnapshot.setMileage(new BigDecimal(String.valueOf(mileage)));
            } else {
                return returnResultMap(false, null, "check", "车辆行驶里程为0！");
            }
            //车辆型号
            if (null != carModel && !"".equals(carModel.trim())) {
                customerCarSnapshot.setCarModel(carModel);
            } else {
                return returnResultMap(false, null, "check", "车辆型号不可以为空！");
            }
            //车辆金额
            if (carMoney.intValue() > 0) {
                customerCarSnapshot.setCarMoney(carMoney);
            } else {
                return returnResultMap(false, null, "check", "车辆金额不可以为空！");
            }
            //车辆抵押类型
            if (originalPrice.intValue() > 0) {
                customerCarSnapshot.setOriginalPrice(originalPrice);
            } else {
                return returnResultMap(false, null, "check", "车辆抵押类型不可以为空！");
            }
            //车辆估值
            if (appraisal.intValue() > 0) {
                customerCarSnapshot.setAppraisal(appraisal);
            } else {
                return returnResultMap(false, null, "check", "车辆估值不可以为空！");
            }
            customerCarSnapshot.setMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
//            customerCarSnapshot.set


// 【LoanApplication - 开始】*************************************************************************
            mainLoan.setLoanUseage(loanUseage);//借款用途
            if (null != loanProductId && !"".equals(loanProductId)) {
                mainLoan.setLoanProductId(Long.valueOf(loanProductId));//借款产品
                LoanProduct loanProduct = loanProductService.findById(Long.valueOf(loanProductId));
                mainLoan.setAnnualRate(loanProduct.getAnnualRate());//年利率
            }
            if (null != loanBalance && !"".equals(loanBalance)) {
                mainLoan.setLoanBalance(new BigDecimal(loanBalance));//借款金额
                if (!LoanApplication.APPLICATIONSTATE_DRAFT.equals(mainLoan.getApplicationState())) {//如果借款申请状态不为草稿，则将批复金额和借款金额改为一致
                    mainLoan.setConfirmBalance(new BigDecimal(loanBalance));//批复金额
                    mainLoan.setMainLoanBalance(new BigDecimal(loanBalance));//修改总借款金额（应对提交初审之后，还更新借款金额的情况）
                }
            }
            applicationDesc = applicationDesc.replaceAll("\r\n", "<br>");
            mainLoan.setApplicationDesc(applicationDesc);
            riskControlInformation = riskControlInformation.replaceAll("\r\n", "<br>");
            mainLoan.setRiskControlInformation(riskControlInformation);
            loanUseageDesc = loanUseageDesc.replaceAll("\r\n", "<br>");
            mainLoan.setLoanUseageDesc(loanUseageDesc);
            mainLoan.setOfflineApplyCode(offlineApplyCode);

            mainLoan.setLoanApplicationName(loanApplicationName);//借款合同名称=#{贷款用途}-#{产品名称}-#{时长}
            // 【LoanApplication - 结束】

// 【CustomerBasicSnapshot - 开始】***********************************************************************
            CustomerBasicSnapshot basic = customerBasicSnapshotService.getBasicByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
            basic.setMobilePhone(mobilePhone);
            basic.setMonthlyIncome(monthlyIncome);
            // 【CustomerBasicSnapshot - 结束】

// 【UserInfoExt 用户扩展  - 开始】****************************************************************************
            UserInfoExt ext = userInfoExtService.getUserInfoExtById(mainLoan.getUserId());
            ext.setMobileNo(mobilePhone);
            // 【UserInfoExt 用户扩展-结束】
// 【Address - 开始】***********************************************************************************
            //现住址
            Address residenceAddr = addressService.getAddressById(basic.getResidenceAddr());
            residenceAddr.setUserId(mainLoan.getUserId());
            residenceAddr.setDetail(residenceAddr_detail);
            residenceAddr.setType("1");
            if (null != residenceAddr_provence && !"".equals(residenceAddr_provence)) {
                residenceAddr.setProvince(Long.valueOf(residenceAddr_provence));
            }
            if (null != residenceAddr_city && !"".equals(residenceAddr_city)) {
                residenceAddr.setCity(Long.valueOf(residenceAddr_city));
            }
            if (null != residenceAddr_district && !"".equals(residenceAddr_district)) {
                residenceAddr.setDistrict(Long.valueOf(residenceAddr_district));
            }
            // 【Address - 结束】
            CustomerCard card = new CustomerCard();
            if (SubjectTypeEnum.LOAN.getValue().equals(mainLoan.getSubjectType())) {
                if (null != bankCode && !"".equals(bankCode)) {
                    card.setBankCode(Long.valueOf(bankCode));
                }
                card.setRegisteredBank(registeredBank);
                card.setCardCode(cardCode);
                card.setCardcustomerName(cardCustomerName);
                card.setStatus(CustomerCardStatus.NORMAL.getValue());
                card.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
                card.setBelongChannel(CardChannel.LL.getValue());
                card.setUpdateTime(new Date());
                card.setCardType(CardType.FULL_CARD.getValue());
            }
            // 【CustomerCard - 结束】


// 【借款申请费用表 LOAN_APPLICATION_FEES_ITEM - 开始 】**********************************************************************************
            List<LoanApplicationFeesItem> feesItems = new ArrayList<LoanApplicationFeesItem>();
            LoanApplicationFeesItem loanApplicationFeesItem;//[add]

            List<LoanProductFeesItem> productFeesItems = loanProductFeesItemService.getByProductId(Long.valueOf(loanProductId));//[select]
            if (null != productFeesItems && productFeesItems.size() > 0) {
                for (int i = 0; i < productFeesItems.size(); i++) {
                    LoanProductFeesItem productFeesItem = productFeesItems.get(i);
                    FeesItem feesItem = feesItemService.findById(productFeesItem.getFeesItemId());//[select]

                    loanApplicationFeesItem = new LoanApplicationFeesItem();
                    loanApplicationFeesItem.setChargeCycle(productFeesItem.getChargeCycle());//收费周期
                    loanApplicationFeesItem.setWorkflowRatio(productFeesItem.getWorkflowRatio());//平台收取比例
                    loanApplicationFeesItem.setItemName(feesItem.getItemName());//项目名称
                    loanApplicationFeesItem.setItemType(feesItem.getItemType());//费用类别
                    loanApplicationFeesItem.setFeesRate(new BigDecimal(String.valueOf(feesItem.getFeesRate())));//收费比例
                    loanApplicationFeesItem.setRadicesType(feesItem.getRadicesType());//基数
                    loanApplicationFeesItem.setRadiceLogic(feesItem.getRadiceLogic());//自定义基数逻辑
                    loanApplicationFeesItem.setRadiceName(feesItem.getRadiceName());//自定义基数名称

                }
            }

            // 【借款申请费用表 LOAN_APPLICATION_FEES_ITEM - 结束 】

            // 执行保存操作。
            loanApplicationService.saveLoanForCarPart2(mainLoan, residenceAddr, basic, ext, card, feesItems, customerCarSnapshot);//main

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 跳转到：编辑2页。【main】
     *
     * @param loanApplicationId 借款申请ID
     */
    @RequestMapping(value = "/to_loan_edit_peoplecar_part2")
    public ModelAndView to_loan_edit_peoplecar_part2(
            @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId) {
        ModelAndView mv = new ModelAndView();
        MainLoanApplication mainLoan = conModelAndViewParamter(loanApplicationId, mv);
        CustomerBasicSnapshot basic = conModelAndViewParamter(loanApplicationId, mv, mainLoan);
        // 加载编辑信息-begin
        Address residenceAddr = addressService.getAddressById(basic.getResidenceAddr());
        List<CustomerContactsSnapshot> contactsSnapshots = customerContactsSnapshotService.getContactsByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        CustomerContactsSnapshot contactsSnapshot = null;
        if (null != contactsSnapshots && contactsSnapshots.size() > 0) {
            contactsSnapshot = contactsSnapshots.get(0);
            contactsSnapshots.remove(0);
        }
        if (!StringUtils.isNull(mainLoan.getApplicationDesc())) {
            mainLoan.setApplicationDesc(mainLoan.getApplicationDesc().replaceAll("<br>", "\r\n"));//借款描述
        }
        if (!StringUtils.isNull(mainLoan.getRiskControlInformation())) {
            mainLoan.setRiskControlInformation(mainLoan.getRiskControlInformation().replaceAll("<br>", "\r\n"));//风险控制信息
        }
        if (!StringUtils.isNull(mainLoan.getLoanUseageDesc())) {
            mainLoan.setLoanUseageDesc(mainLoan.getLoanUseageDesc().replaceAll("<br>", "\r\n"));//借款用途描述
        }

        mv.addObject("loan", mainLoan);
        mv.addObject("basic", basic);
        mv.addObject("residenceAddr", residenceAddr);
        mv.addObject("contactsSnapshots", JSONObject.toJSON(contactsSnapshots));
        mv.addObject("contactsSnapshot", contactsSnapshot);

        //银行卡（借款标情况）
        if (LoanApplication.SUBJECTTYPE_LOANMARK.equals(mainLoan.getSubjectType())) {
            CustomerCard card = customerCardService.findById(mainLoan.getInCardId());
            mv.addObject("card", card);
        }
        CustomerCarSnapshot customerCarSnapshot = customerCarSnapshotService.getCarByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        mv.addObject("customerCarSnapshot",customerCarSnapshot );
        if (customerCarSnapshot.getBuyTime() != null)
            mv.addObject("carBurTime", new SimpleDateFormat("yyyy-MM-dd").format(customerCarSnapshot.getBuyTime()));


        //现住址
        //省份
        List<ProvinceInfo> provinceInfos = provinceInfoService.getAllProvinceInfo();
        mv.addObject("provinceInfos", provinceInfos);
        List<CityInfo> residenceCitys = cityInfoService.getCityByProvinceIdAndPId(residenceAddr.getProvince(), 0l);
        List<CityInfo> residenceDistricts = cityInfoService.getCityByProvinceIdAndPId(residenceAddr.getProvince(), residenceAddr.getCity());
        mv.addObject("residenceCitys", residenceCitys);
        mv.addObject("residenceDistricts", residenceDistricts);
        mv.setViewName("jsp/loanManage/loan/to_loan_edit_peoplecar_part2");
        return mv;
    }

    private MainLoanApplication conModelAndViewParamter(String loanApplicationId, ModelAndView mv) {
        MainLoanApplication mainLoan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.addObject("subjectType", mainLoan.getSubjectType());//1借款：loanMark;2债权：rightsMark
        return mainLoan;
    }

    private CustomerBasicSnapshot conModelAndViewParamter(String loanApplicationId, ModelAndView mv, MainLoanApplication mainLoan) {
        CustomerBasicSnapshot basic = customerBasicSnapshotService.getBasicByMainLoanApplicationId(mainLoan.getMainLoanApplicationId());
        mv.addObject("idCard", basic.getIdCard());
        mv.addObject("trueName", basic.getTrueName());
        mv.addObject("birthday", new SimpleDateFormat("yyyy-MM-dd").format(basic.getBirthday()));
        mv.addObject("sex", basic.getSexStr(basic.getSex()));
        mv.addObject("originalUserId", mainLoan.getOriginalUserId());
        return basic;
    }


}
