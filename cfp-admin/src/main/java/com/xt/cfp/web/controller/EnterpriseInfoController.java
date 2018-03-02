package com.xt.cfp.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.fydatasource.LegalPersonDataSource;
import com.external.deposites.model.response.OpenAccount4PCEnterpriseResponse;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.pojo.ext.EnterpriseInfoExt;
import com.xt.cfp.core.pojo.ext.EnterpriseUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.util.StringUtils;

@Controller
@RequestMapping("/jsp/enterprise")
public class EnterpriseInfoController extends BaseController {

    @Autowired
    private EnterpriseInfoService enterpriseInfoService;

    @Autowired
    private CoLtdService coLtdService;

    @Autowired
    private QuotaRecordService quotaRecordService;

    @Autowired
    private EnterpriseUserService enterpriseUserService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoExtService userInfoExtService;

    @Autowired
    private CustomerCardService customerCardService;

    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private IhfApi hfApi;
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TradeService tradeService;

    /**
     * @param rechargeResponse
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "enterpriseOpenAccount")
    public Object enterpriseOpenAccount(OpenAccount4PCEnterpriseResponse rechargeResponse) {
        Map<String, Object> a = new HashMap<>(1);
        if (!rechargeResponse.isSuccess()) {
            String resp_desc = rechargeResponse.getResp_desc();
            logger.warn("法人开户不成功");
            a.put("error", "不成功");
            return a;
        }
        if (rechargeResponse.verifySign(true)) {
            a.put("personalOpenAccount", rechargeResponse);
        } else {
            a.put("personalOpenAccount", "数据有问题");
            logger.warn("数据有问题");
            return a;
        }
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(rechargeResponse.getUser_id_from()));
        UserInfo userInfo = this.userInfoService.getUserByUserId(enterpriseInfo.getUserId());
        if (null == userInfo) {
            userInfo = this.userInfoService.createPlatFormUserForEnterprise("", UserType.ENTERPRISE_USER, rechargeResponse.getMobile_no().toString());
        } else {
            userInfo.setMobileNo(rechargeResponse.getMobile_no());
            this.userInfoService.updateUser(userInfo);
            UserInfoExt userInfoExt = this.userInfoExtService.getUserInfoExtById(userInfo.getUserId());
            userInfoExt.setMobileNo(userInfo.getMobileNo());
            userInfoExt.setIsVerified(UserIsVerifiedEnum.BIND.getValue());
            this.userInfoExtService.updateUserInfoExt(userInfoExt);
        }

        customerCardService.changeUserCard(userInfo.getUserId(), rechargeResponse.getCapAcntNo(), rechargeResponse.getParent_bank_id(), rechargeResponse.getBank_nm(), "3");
        enterpriseInfo.setUserId(userInfo.getUserId());
        enterpriseInfoService.editEnterprise(enterpriseInfo);
        // 报备事宜
        getTradeUpdate(rechargeResponse);

        return a;
    }

    /**
     * 异步
     *
     * @param rechargeResponse
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "aEnterpriseOpenAccount")
    public Object aEnterpriseOpenAccount(OpenAccount4PCEnterpriseResponse rechargeResponse) {
        Enumeration<String> parameterNames = this.getRequest().getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = this.getRequest().getParameter(element);
            System.out.println("enterpriseOpenAccount receive parameters : " + element + " == " + parameter);
        }
        Map<String, Object> a = new HashMap<>(1);
        if (!rechargeResponse.isSuccess()) {
            logger.warn("不成功");
            a.put("error", "不成功");
            return a;
        }
        if (rechargeResponse.verifySign(true)) {
            a.put("enterpriseOpenAccount", rechargeResponse);
        } else {
            logger.warn("数据有问题");
        }
//		userInfoExtService.updateIdentityInfo(rechargeResponse.getCertif_id(), rechargeResponse.getCust_nm(), Long.valueOf(rechargeResponse.getUser_id_from()), rechargeResponse.getCapAcntNo());
//		customerCardService.changeUserCard(Long.parseLong(rechargeResponse.getUser_id_from()), rechargeResponse.getCapAcntNo(), rechargeResponse.getParent_bank_id(), rechargeResponse.getBank_nm());

        return a;
    }

    @RequestMapping(value = "/doLegalPersonOpenAcc")
    public Object testLegalPersonOpenAcc(LegalPersonDataSource dataSource, Model model, Long enterpriseId) {
        try {
            EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
            model.addAttribute("enterpriseInfo", enterpriseInfo);
            //设置回调地址
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.enterprise.self.page_notify_url"));
            dataSource.setCertif_id(enterpriseInfo.getLegalPersonCode());
            dataSource.setArtif_nm(enterpriseInfo.getLegalPersonName());
            dataSource.setCust_nm(enterpriseInfo.getEnterpriseName());
            dataSource.setUser_id_from(enterpriseInfo.getEnterpriseId().toString());
            dataSource = hfApi.legalPersonOpenAccountBySelf(dataSource);
            model.addAttribute("params", dataSource);
            addTrade(dataSource);
        } catch (final HfApiException e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
        }
        return "jsp/api/doOpenAccount2";
    }


    /**
     * 跳转到：企业-列表
     */
    @RequestMapping(value = "/to_enterprise_list")
    public ModelAndView to_enterprise_list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/enterprise/enterprise_list");
        return mv;
    }

    /**
     * 跳转到：企业-管理
     */
    @RequestMapping(value = "/to_enterprise_manage")
    public ModelAndView to_enterprise_manage(@RequestParam(value = "enterpriseId", required = false) String enterpriseId) {
        ModelAndView mv = new ModelAndView();
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
        mv.addObject("enterpriseInfo", enterpriseInfo);
        mv.setViewName("jsp/enterprise/enterprise_manage");
        return mv;
    }

    /**
     * 跳转到：企业-添加
     */
    @RequestMapping(value = "/to_enterprise_add_part")
    public ModelAndView to_enterprise_add_part() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/enterprise/enterprise_add_part");
        return mv;
    }

    /**
     * 跳转到：企业-添加1
     */
    @RequestMapping(value = "/to_enterprise_add_part1")
    public ModelAndView to_enterprise_add_part1() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/enterprise/enterprise_add_part1");
        return mv;
    }

    /**
     * 跳转到：企业-添加2
     */
    @RequestMapping(value = "/to_enterprise_add_part2")
    public ModelAndView to_enterprise_add_part2() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/enterprise/enterprise_add_part2");
        return mv;
    }

    /**
     * 跳转到：企业-编辑
     */
    @RequestMapping(value = "/to_enterprise_edit_part")
    public ModelAndView to_enterprise_edit_part(@RequestParam(value = "enterpriseId", required = false) String enterpriseId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("enterpriseId", enterpriseId);
        mv.setViewName("jsp/enterprise/enterprise_edit_part");
        return mv;
    }

    /**
     * 跳转到：企业-编辑1
     */
    @RequestMapping(value = "/to_enterprise_edit_part1")
    public ModelAndView to_enterprise_edit_part1(@RequestParam(value = "enterpriseId", required = false) String enterpriseId) {
        ModelAndView mv = new ModelAndView();
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
        mv.addObject("enterpriseInfo", enterpriseInfo);

        mv.setViewName("jsp/enterprise/enterprise_edit_part1");
        return mv;
    }

    /**
     * 执行：分页列表
     *
     * @param pageSize 页数
     * @param pageNo   页码
     */
    @RequestMapping(value = "/enterprise_list")
    @ResponseBody
    public Object enterprise_list(HttpServletRequest request, HttpSession session,
                                  @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNo,
                                  @RequestParam(value = "searchEnterpriseName", required = false) String searchEnterpriseName,
                                  @RequestParam(value = "searchEnterpriseType", required = false) String searchEnterpriseType,
                                  @RequestParam(value = "searchState", required = false) String searchState) throws Exception {

        // 填充参数
        Map params = new HashMap();
        if (null != searchEnterpriseName && !"".equals(searchEnterpriseName)) {
            params.put("searchEnterpriseName", searchEnterpriseName);
        }
        if (null != searchEnterpriseType && !"".equals(searchEnterpriseType)) {
            params.put("searchEnterpriseType", searchEnterpriseType);
        }
        if (null != searchState && !"".equals(searchState)) {
            params.put("searchState", searchState);
        }
        return enterpriseInfoService.findAllByPage(pageNo, pageSize, params);
    }

    /**
     * 执行：启用、禁用
     *
     * @param enterpriseId 企业ID
     * @param state        0启用、1禁用
     */
    @RequestMapping("/doState")
    @ResponseBody
    public Object doState(HttpServletRequest request, HttpSession session,
                          @RequestParam(value = "enterpriseId", required = false) String enterpriseId,
                          @RequestParam(value = "state", required = false) String state) {
        try {
            // 合法性验证。
            if (null == enterpriseId || "".equals(enterpriseId)) {
                return returnResultMap(false, null, "check", "企业ID不能为空！");
            }
            if (null == state || "".equals(state) || (!"0".equals(state) && !"1".equals(state))) {
                return returnResultMap(false, null, "check", "状态参数异常！");
            }

            // 根据ID加载一条数据。
            EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
            if (null == enterpriseInfo) {
                return returnResultMap(false, null, "check", "参数异常！");
            }

            // 更改相关数据值
            enterpriseInfo.setState(state);
            enterpriseInfo.setLastUpdateTime(new Date());
            enterpriseInfoService.editEnterprise(enterpriseInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 执行：管理保存
     *
     * @param enterpriseId        企业ID
     * @param contractBegin       合同开始时间
     * @param contractEnd         合同结束时间
     * @param singleMaximumAmount 单笔限额
     * @param annualMaximumLimit  年度限额
     */
    @RequestMapping("/doManage")
    @ResponseBody
    public Object doManage(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "enterpriseId", required = false) String enterpriseId,
                           @RequestParam(value = "contractBegin", required = false) String contractBegin,
                           @RequestParam(value = "contractEnd", required = false) String contractEnd,
                           @RequestParam(value = "singleMaximumAmount", required = false) String singleMaximumAmount,
                           @RequestParam(value = "annualMaximumLimit", required = false) String annualMaximumLimit) {
        try {
            // 合法性验证。
            if (null == enterpriseId || "".equals(enterpriseId)) {
                return returnResultMap(false, null, "check", "企业ID不能为空！");
            }
            if (null == contractBegin || "".equals(contractBegin)) {
                return returnResultMap(false, null, "check", "合同开始期限不能为空！");
            }
            if (null == contractEnd || "".equals(contractEnd)) {
                return returnResultMap(false, null, "check", "合同结束期限不能为空！");
            }
            if (null == singleMaximumAmount || "".equals(singleMaximumAmount)) {
                return returnResultMap(false, null, "check", "单笔限额不能为空！");
            }
            if (null == annualMaximumLimit || "".equals(annualMaximumLimit)) {
                return returnResultMap(false, null, "check", "年度限额不能为空！");
            }

            // 根据ID加载一条数据。
            EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
            if (null == enterpriseInfo) {
                return returnResultMap(false, null, "check", "参数异常！");
            }

            // 更改相关数据值
            enterpriseInfo.setContractBegin(new SimpleDateFormat("yyyy-MM-dd").parse(contractBegin));
            enterpriseInfo.setContractEnd(new SimpleDateFormat("yyyy-MM-dd").parse(contractEnd));
            enterpriseInfo.setSingleMaximumAmount(new BigDecimal(singleMaximumAmount));
            enterpriseInfo.setAnnualMaximumLimit(new BigDecimal(annualMaximumLimit));
            enterpriseInfo.setLastUpdateTime(new Date());

            // 添加额度记录
            QuotaRecord quotaRecord = new QuotaRecord();
            quotaRecord.setEnterpriseId(enterpriseInfo.getEnterpriseId());
            quotaRecord.setContractBegin(new SimpleDateFormat("yyyy-MM-dd").parse(contractBegin));
            quotaRecord.setContractEnd(new SimpleDateFormat("yyyy-MM-dd").parse(contractEnd));
            quotaRecord.setSingleMaximumAmount(new BigDecimal(singleMaximumAmount));
            quotaRecord.setAnnualMaximumLimit(new BigDecimal(annualMaximumLimit));
            quotaRecord.setCreateTime(new Date());
            quotaRecord.setLastUpdateTime(new Date());

            enterpriseInfoService.editQuotaRecord(enterpriseInfo, quotaRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 执行：添加企业信息
     */
    @RequestMapping("/addEnterprise")
    @ResponseBody
    public Object addEnterprise(HttpServletRequest request, HttpSession session,
                                @RequestParam(value = "enterpriseType", required = false) String enterpriseType,//公司类型
                                @RequestParam(value = "enterpriseName", required = false) String enterpriseName,//公司名称
                                @RequestParam(value = "organizationCode", required = false) String organizationCode,//组织机构代码
                                @RequestParam(value = "legalPersonName", required = false) String legalPersonName,//法人姓名
                                @RequestParam(value = "legalPersonCode", required = false) String legalPersonCode,//法人身份证号

                                @RequestParam(value = "accountNumber", required = false) String accountNumber,//开户许可证号
                                @RequestParam(value = "taxRegistrationCode", required = false) String taxRegistrationCode,//税务登记证代码
                                @RequestParam(value = "businessRegistrationNumber", required = false) String businessRegistrationNumber,//营业执照注册号
                                @RequestParam(value = "operatingPeriod", required = false) String operatingPeriod,//经营年限
                                @RequestParam(value = "registeredCapital", required = false) String registeredCapital,//注册资金
                                @RequestParam(value = "operatingRange", required = false) String operatingRange,//经营范围
                                @RequestParam(value = "information", required = false) String information,//企业信息

                                @RequestParam(value = "mainRevenue", required = false) String mainRevenue,//主营收入
                                @RequestParam(value = "grossProfit", required = false) String grossProfit,//毛利润
                                @RequestParam(value = "netProfit", required = false) String netProfit,//净利润
                                @RequestParam(value = "totalAssets", required = false) String totalAssets,//总资产
                                @RequestParam(value = "netAssets", required = false) String netAssets) {//净资产
        EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
        try {
            // 合法性验证。
            if (null == enterpriseType || "".equals(enterpriseType)) {
                return returnResultMap(false, null, "check", "公司类型不能为空！");
            }
            if (null == enterpriseName || "".equals(enterpriseName)) {
                return returnResultMap(false, null, "check", "公司名称不能为空！");
            } else if (enterpriseName.length() < 4) {
                return returnResultMap(false, null, "check", "公司名称不能小于4个字符！");
            }
            if (null == organizationCode || "".equals(organizationCode)) {
                return returnResultMap(false, null, "check", "组织机构代码不能为空！");
            }
            if (null == legalPersonName || "".equals(legalPersonName)) {
                return returnResultMap(false, null, "check", "法人姓名不能为空！");
            }
            if (null == legalPersonCode || "".equals(legalPersonCode)) {
                return returnResultMap(false, null, "check", "法人身份证号不能为空！");
            }

            if (null == accountNumber || "".equals(accountNumber)) {
                return returnResultMap(false, null, "check", "开户许可证号不能为空！");
            }
            if (null == taxRegistrationCode || "".equals(taxRegistrationCode)) {
                return returnResultMap(false, null, "check", "税务登记证代码不能为空！");
            }
            if (null == businessRegistrationNumber || "".equals(businessRegistrationNumber)) {
                return returnResultMap(false, null, "check", "营业执照注册号不能为空！");
            }
            if (null == operatingPeriod || "".equals(operatingPeriod)) {
                return returnResultMap(false, null, "check", "经营年限不能为空！");
            }
            if (null == registeredCapital || "".equals(registeredCapital)) {
                return returnResultMap(false, null, "check", "注册资金不能为空！");
            }
            if (null == operatingRange || "".equals(operatingRange)) {
                return returnResultMap(false, null, "check", "经营范围不能为空！");
            }
            if (null == information || "".equals(information)) {
                return returnResultMap(false, null, "check", "企业信息不能为空！");
            }

// 			if(null == mainRevenue || "".equals(mainRevenue)){
// 				return returnResultMap(false, null, "check", "主营收入不能为空！");
// 			}
// 			if(null == grossProfit || "".equals(grossProfit)){
// 				return returnResultMap(false, null, "check", "毛利润不能为空！");
// 			}
// 			if(null == netProfit || "".equals(netProfit)){
// 				return returnResultMap(false, null, "check", "净利润不能为空！");
// 			}
// 			if(null == totalAssets || "".equals(totalAssets)){
// 				return returnResultMap(false, null, "check", "总资产不能为空！");
// 			}
// 			if(null == netAssets || "".equals(netAssets)){
// 				return returnResultMap(false, null, "check", "净资产不能为空！");
// 			}

            // 填充参数
            enterpriseInfo.setEnterpriseName(enterpriseName);
            enterpriseInfo.setEnterpriseType(enterpriseType);
            enterpriseInfo.setLegalPersonName(legalPersonName);
            enterpriseInfo.setLegalPersonCode(legalPersonCode);
            enterpriseInfo.setAccountNumber(accountNumber);
            enterpriseInfo.setOrganizationCode(organizationCode);
            enterpriseInfo.setTaxRegistrationCode(taxRegistrationCode);
            enterpriseInfo.setBusinessRegistrationNumber(businessRegistrationNumber);
            enterpriseInfo.setOperatingPeriod(Integer.valueOf(operatingPeriod));
            enterpriseInfo.setRegisteredCapital(Long.valueOf(registeredCapital));
            enterpriseInfo.setOperatingRange(operatingRange);
            enterpriseInfo.setInformation(information);
            if (null != mainRevenue && !"".equals(mainRevenue)) {
                enterpriseInfo.setMainRevenue(new BigDecimal(mainRevenue));
            }
            if (null != grossProfit && !"".equals(grossProfit)) {
                enterpriseInfo.setGrossProfit(new BigDecimal(grossProfit));
            }
            if (null != netProfit && !"".equals(netProfit)) {
                enterpriseInfo.setNetProfit(new BigDecimal(netProfit));
            }
            if (null != totalAssets && !"".equals(totalAssets)) {
                enterpriseInfo.setTotalAssets(new BigDecimal(totalAssets));
            }
            if (null != netAssets && !"".equals(netAssets)) {
                enterpriseInfo.setNetAssets(new BigDecimal(netAssets));
            }
            enterpriseInfo.setContractBegin(null);
            enterpriseInfo.setContractEnd(null);
            enterpriseInfo.setSingleMaximumAmount(new BigDecimal("0"));
            enterpriseInfo.setAnnualMaximumLimit(new BigDecimal("0"));
            enterpriseInfo.setAnnualResidualAmount(new BigDecimal("0"));
            enterpriseInfo.setState(EnterpriseInfo.STATE_DISABLE);
            enterpriseInfo.setCreateTime(new Date());
            enterpriseInfo.setLastUpdateTime(new Date());

            //用户信息
            UserInfo user = new UserInfo();
            user.setLoginName(String.valueOf(new Date().getTime()));
            user.setMobileNo(String.valueOf(new Date().getTime()).substring(2));
            user.setLoginPass(String.valueOf(new Date().getTime()));
            user.setCreateTime(new Date());

            // 执行添加
            enterpriseInfo = enterpriseInfoService.addEnterpriseAndUser(enterpriseInfo, user);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, enterpriseInfo.getEnterpriseId(), null, null);
    }

    /**
     * 执行：编辑企业信息
     */
    @RequestMapping("/editEnterprise")
    @ResponseBody
    public Object editEnterprise(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "enterpriseId", required = false) String enterpriseId,//企业ID
                                 @RequestParam(value = "enterpriseType", required = false) String enterpriseType,//公司类型
                                 @RequestParam(value = "enterpriseName", required = false) String enterpriseName,//公司名称
                                 @RequestParam(value = "organizationCode", required = false) String organizationCode,//组织机构代码
                                 @RequestParam(value = "legalPersonName", required = false) String legalPersonName,//法人姓名
                                 @RequestParam(value = "legalPersonCode", required = false) String legalPersonCode,//法人身份证号

                                 @RequestParam(value = "accountNumber", required = false) String accountNumber,//开户许可证号
                                 @RequestParam(value = "taxRegistrationCode", required = false) String taxRegistrationCode,//税务登记证代码
                                 @RequestParam(value = "businessRegistrationNumber", required = false) String businessRegistrationNumber,//营业执照注册号
                                 @RequestParam(value = "operatingPeriod", required = false) String operatingPeriod,//经营年限
                                 @RequestParam(value = "registeredCapital", required = false) String registeredCapital,//注册资金
                                 @RequestParam(value = "operatingRange", required = false) String operatingRange,//经营范围
                                 @RequestParam(value = "information", required = false) String information,//企业信息

                                 @RequestParam(value = "mainRevenue", required = false) String mainRevenue,//主营收入
                                 @RequestParam(value = "grossProfit", required = false) String grossProfit,//毛利润
                                 @RequestParam(value = "netProfit", required = false) String netProfit,//净利润
                                 @RequestParam(value = "totalAssets", required = false) String totalAssets,//总资产
                                 @RequestParam(value = "netAssets", required = false) String netAssets,

                                 @RequestParam(value = "accType", required = false) String accType,
                                 @RequestParam(value = "accTypeUpdate", required = false) String accTypeUpdate
    ) {//净资产
        try {
            // 合法性验证。
            if (null == enterpriseId || "".equals(enterpriseId)) {
                return returnResultMap(false, null, "check", "企业ID不能为空！");
            }
            if (null == enterpriseType || "".equals(enterpriseType)) {
                return returnResultMap(false, null, "check", "公司类型不能为空！");
            }
            if (null == enterpriseName || "".equals(enterpriseName)) {
                return returnResultMap(false, null, "check", "公司名称不能为空！");
            } else if (enterpriseName.length() < 4) {
                return returnResultMap(false, null, "check", "公司名称不能小于4个字符！");
            }
            if (null == organizationCode || "".equals(organizationCode)) {
                return returnResultMap(false, null, "check", "组织机构代码不能为空！");
            }
            if (null == legalPersonName || "".equals(legalPersonName)) {
                return returnResultMap(false, null, "check", "法人姓名不能为空！");
            }
            if (null == legalPersonCode || "".equals(legalPersonCode)) {
                return returnResultMap(false, null, "check", "法人身份证号不能为空！");
            }

            if (null == accountNumber || "".equals(accountNumber)) {
                return returnResultMap(false, null, "check", "开户许可证号不能为空！");
            }
            if (null == taxRegistrationCode || "".equals(taxRegistrationCode)) {
                return returnResultMap(false, null, "check", "税务登记证代码不能为空！");
            }
            if (null == businessRegistrationNumber || "".equals(businessRegistrationNumber)) {
                return returnResultMap(false, null, "check", "营业执照注册号不能为空！");
            }
            if (null == operatingPeriod || "".equals(operatingPeriod)) {
                return returnResultMap(false, null, "check", "经营年限不能为空！");
            }
            if (null == registeredCapital || "".equals(registeredCapital)) {
                return returnResultMap(false, null, "check", "注册资金不能为空！");
            }
            if (null == operatingRange || "".equals(operatingRange)) {
                return returnResultMap(false, null, "check", "经营范围不能为空！");
            }
            if (null == information || "".equals(information)) {
                return returnResultMap(false, null, "check", "企业信息不能为空！");
            }

// 			if(null == mainRevenue || "".equals(mainRevenue)){
// 				return returnResultMap(false, null, "check", "主营收入不能为空！");
// 			}
// 			if(null == grossProfit || "".equals(grossProfit)){
// 				return returnResultMap(false, null, "check", "毛利润不能为空！");
// 			}
// 			if(null == netProfit || "".equals(netProfit)){
// 				return returnResultMap(false, null, "check", "净利润不能为空！");
// 			}
// 			if(null == totalAssets || "".equals(totalAssets)){
// 				return returnResultMap(false, null, "check", "总资产不能为空！");
// 			}
// 			if(null == netAssets || "".equals(netAssets)){
// 				return returnResultMap(false, null, "check", "净资产不能为空！");
// 			}

            // 根据ID加载一条数据。
            EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
            if (null == enterpriseInfo) {
                return returnResultMap(false, null, "check", "参数异常！");
            }

            if (null == accType && !"".equals(accType)) {
                return returnResultMap(false, null, "check", "参数异常！");
            }
            if (null != enterpriseInfo.getUserId()) {
                UserInfo userByUserId = this.userInfoService.getUserByUserId(enterpriseInfo.getUserId());

                if (null != userByUserId) {
                    //根据原有的accCode查询出账户类型，但是accCode如果等于0，说明是第一次初始化，但是也要做用最新的accCode查询出原来的账户，
                    // 如果根据最新的accCode 查询出了账户类型，将用户的id赋值到account中
                    if (null != accTypeUpdate && !"".equals(accTypeUpdate) && !"0".equals(accTypeUpdate)) {
                        //原来就是系统平台账户,将原来的系统账户设置为空，并设置新的关系
                        if (!accTypeUpdate.equals(accType)) {
                            UserAccount platformAccountByType = this.userAccountService.getPlatformAccountByType(getEnum(accTypeUpdate));
                            platformAccountByType.setUserId(0l);
                            EnterpriseInfo enterpriseInfo1 = this.enterpriseInfoService.getEnterpriseByPlatId(accTypeUpdate);
                            enterpriseInfo1.setPlatformId(0l);
                            this.enterpriseInfoService.editEnterprise(enterpriseInfo1);// 将原有的企业账户类型设置为0
                            this.userAccountService.updateUserAccount(platformAccountByType);//将原有的设置为0
                            enterpriseInfo.setPlatformId(Long.valueOf(accType));
                            if (!"0".equals(accType)) {
                                UserAccount platformAccountByType1 = this.userAccountService.getPlatformAccountByType(getEnum(accType));
                                platformAccountByType1.setUserId(enterpriseInfo.getUserId());
                                this.userAccountService.updateUserAccount(platformAccountByType1);//设置新的关系
                                enterpriseInfo.setPlatformId(Long.valueOf(accType));
                            }

                        }

                    } else {
                        if (!accType.equals("0")) {
                            //原来不是，需要根据accCode 查询出用户账户，然后替换userAccount中的userId
                            UserAccount platformAccountByType = this.userAccountService.getPlatformAccountByType(getEnum(accType));
//							this.enterpriseInfoService.get
//								platformAccountByType.getUserId()
                            EnterpriseInfo enterpriseByPlatId = this.enterpriseInfoService.getEnterpriseByPlatId(accType);
                            if (null != enterpriseByPlatId) {
                                enterpriseByPlatId.setPlatformId(0l);
                                this.enterpriseInfoService.editEnterprise(enterpriseByPlatId);
                            }
                            platformAccountByType.setUserId(enterpriseInfo.getUserId());
                            this.userAccountService.updateUserAccount(platformAccountByType);
                            enterpriseInfo.setPlatformId(Long.valueOf(accType));
                        } else {
                            enterpriseInfo.setPlatformId(0l);
                        }

                    }
                }

            }
            // 填充参数
            enterpriseInfo.setEnterpriseName(enterpriseName);
            enterpriseInfo.setEnterpriseType(enterpriseType);
            enterpriseInfo.setLegalPersonName(legalPersonName);
            enterpriseInfo.setLegalPersonCode(legalPersonCode);
            enterpriseInfo.setAccountNumber(accountNumber);
            enterpriseInfo.setOrganizationCode(organizationCode);
            enterpriseInfo.setTaxRegistrationCode(taxRegistrationCode);
            enterpriseInfo.setBusinessRegistrationNumber(businessRegistrationNumber);
            enterpriseInfo.setOperatingPeriod(Integer.valueOf(operatingPeriod));
            enterpriseInfo.setRegisteredCapital(Long.valueOf(registeredCapital));
            enterpriseInfo.setOperatingRange(operatingRange);
            enterpriseInfo.setInformation(information);
            if (null != mainRevenue && !"".equals(mainRevenue)) {
                enterpriseInfo.setMainRevenue(new BigDecimal(mainRevenue));
            } else {
                enterpriseInfo.setMainRevenue(new BigDecimal("0"));
            }
            if (null != grossProfit && !"".equals(grossProfit)) {
                enterpriseInfo.setGrossProfit(new BigDecimal(grossProfit));
            } else {
                enterpriseInfo.setGrossProfit(new BigDecimal("0"));
            }
            if (null != netProfit && !"".equals(netProfit)) {
                enterpriseInfo.setNetProfit(new BigDecimal(netProfit));
            } else {
                enterpriseInfo.setNetProfit(new BigDecimal("0"));
            }
            if (null != totalAssets && !"".equals(totalAssets)) {
                enterpriseInfo.setTotalAssets(new BigDecimal(totalAssets));
            } else {
                enterpriseInfo.setTotalAssets(new BigDecimal("0"));
            }
            if (null != netAssets && !"".equals(netAssets)) {
                enterpriseInfo.setNetAssets(new BigDecimal(netAssets));
            } else {
                enterpriseInfo.setNetAssets(new BigDecimal("0"));
            }
            enterpriseInfo.setLastUpdateTime(new Date());

            // 执行编辑
            enterpriseInfoService.editEnterprise(enterpriseInfo);


        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 显示图片添加页
     *
     * @param state
     * @param typeList
     * @param userId
     * @param enterpriseId
     * @param isCode
     * @return
     */
    @RequestMapping(value = "/uploadSnapshotAdd")
    public ModelAndView uploadSnapshotAdd(@RequestParam(value = "state", required = false) String state,
                                          @RequestParam(value = "typeList", required = false) String typeList,
                                          @RequestParam(value = "userId", required = false) String userId,
                                          @RequestParam(value = "enterpriseId", required = false) String enterpriseId,
                                          @RequestParam(value = "isCode", required = false) String isCode) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("state", state);
        mv.addObject("typeList", typeList);
        mv.addObject("enterpriseId", enterpriseId);
        mv.addObject("isCode", isCode);
        mv.setViewName("jsp/enterprise/uploadSnapshotAdd");
        return mv;
    }

    /**
     * 保存
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
        String enterpriseId = request.getParameter("enterpriseId");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFiles("imgFile").get(0);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("resultState", "success");
        if (file != null) {
            return JSONObject.toJSONString(enterpriseInfoService.saveUploadSnapshot(enterpriseId, file, state, msgName, typeList, rootPath, isCode));
        }
        return JSONObject.toJSONString(resultMap);
    }

    /**
     * 跳转到大图显示页
     */
    @RequestMapping(value = "/toShowBigPicture")
    public ModelAndView toShowBigPicture(@RequestParam(value = "cusId", required = false) Long cusId) {
        ModelAndView mv = new ModelAndView();
        Attachment atta = enterpriseInfoService.getAttachmentByentId(cusId);
        mv.addObject("cusId", cusId);
        mv.addObject("url", atta.getUrl());
        mv.addObject("imgName", atta.getFileName());
        mv.setViewName("jsp/enterprise/showBigPicture");
        return mv;
    }

    /**
     * 删除图片
     */
    @RequestMapping(value = "/delImg")
    @ResponseBody
    public Object delImg(HttpServletRequest request, HttpSession session,
                         @RequestParam(value = "cusId", required = false) Long cusId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String rootPath = request.getSession().getServletContext().getRealPath("");
            Attachment atta = enterpriseInfoService.getAttachmentByentId(cusId);
            String url = atta.getUrl();
            enterpriseInfoService.delImg(cusId, EnterpriseUploadSnapshot.STATUS_DISABLED, atta, rootPath);
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
     * 图片分页显示
     */
    @RequestMapping(value = "/imgPaging")
    @ResponseBody
    public Object imgPaging(@RequestParam(value = "pageState", required = false) String pageState,
                            @RequestParam(value = "cusId", required = false) Long cusId) {
        EnterpriseUploadSnapshot ent = enterpriseInfoService.getEnterpriseUploadSnapshotDetails(cusId);
        List<EnterpriseUploadSnapshot> cusList = enterpriseInfoService.getEnterpriseUploadSnapshotList(ent.getEnterpriseId(), ent.getType());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cusId", cusId);
        for (int i = 0; i < cusList.size(); i++) {
            if (cusId.equals(cusList.get(i).getSnapshotId())) {
                if ("0".equals(pageState)) {
                    if ((i - 1) >= 0) {
                        Attachment atta = enterpriseInfoService.getAttachmentByentId(cusList.get(i - 1).getSnapshotId());
                        map.put("url", atta.getUrl());
                        map.put("cusId", cusList.get(i - 1).getSnapshotId());
                        map.put("imgName", atta.getFileName());
                        map.put("resultState", "success");
                        break;
                    } else {
                        map.put("resultState", "noUpper");
                        break;
                    }
                } else {
                    if ((i + 1) < cusList.size()) {
                        Attachment atta = enterpriseInfoService.getAttachmentByentId(cusList.get(i + 1).getSnapshotId());
                        map.put("url", atta.getUrl());
                        map.put("cusId", cusList.get(i + 1).getSnapshotId());
                        map.put("imgName", atta.getFileName());
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

    /**
     * 跳转到：企业-编辑2
     */
    @RequestMapping(value = "/to_enterprise_edit_part2")
    public ModelAndView to_enterprise_edit_part2(HttpServletRequest request,
                                                 @RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        String isCode = null;//request.getParameter("isCode");//这里去掉从前台获取，是否打码标示，以便获取全部。

        List<EnterpriseUploadSnapshotVO> cusvoList = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseId, isCode);
        for (EnterpriseUploadSnapshotVO cusvo : cusvoList) {
            cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/") + 1, cusvo.getAttachment().getUrl().lastIndexOf(".")));
        }
        mv.addObject("cusvoList", cusvoList);
        mv.addObject("enterpriseId", enterpriseId);
        mv.setViewName("jsp/enterprise/enterprise_edit_part2");
        return mv;
    }

    /**
     * 是否在前台显示
     *
     * @param isDisplay  0显示，1不显示
     * @param snapshotId 企业凭证快照ID
     */
    @RequestMapping("/doIsDisplay")
    @ResponseBody
    public Object doIsDisplay(HttpServletRequest request, HttpSession session,
                              @RequestParam(value = "isDisplay", required = false) String isDisplay,
                              @RequestParam(value = "snapshotId", required = false) String snapshotId) {
        try {
            // 合法性验证。
            if (null == snapshotId || "".equals(snapshotId)) {
                return returnResultMap(false, null, "check", "快照ID不能为空！");
            }
            if (null == isDisplay || "".equals(isDisplay) || (!"0".equals(isDisplay) && !"1".equals(isDisplay))) {
                return returnResultMap(false, null, "check", "标记参数异常！");
            }

            // 根据企业上传快照ID，加载一条数据。
            EnterpriseUploadSnapshot snapshot = enterpriseInfoService.getEnterpriseUploadSnapshotDetails(Long.valueOf(snapshotId));
            if (null == snapshot) {
                return returnResultMap(false, null, "check", "参数异常！");
            }
            // 更改相关数据值
            snapshot.setIsdisplay(isDisplay);

            // 根据附件ID，加载一条数据
            Attachment attachment = enterpriseInfoService.getAttachmentById(snapshot.getAttachId());
            if (null == attachment) {
                return returnResultMap(false, null, "check", "参数异常！");
            }
            //更改相关数据值
            if ("0".equals(isDisplay)) {
                attachment.setIsCode(AttachmentIsCode.IS_CODE.getValue());
            } else {
                attachment.setIsCode(AttachmentIsCode.NO_CODE.getValue());
            }

            enterpriseInfoService.updateEnterpriseUploadSnapshot(snapshot, attachment);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 获取企业信息列表
     *
     * @param selectedDisplay 默认显示值
     * @return
     */
    @RequestMapping(value = "/loadEnterprise")
    @ResponseBody
    public Object loadEnterprise(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay,
                                 @RequestParam(value = "enterpriseType", required = false) String enterpriseType) {
        if ("selected".equals(selectedDisplay)) {
            selectedDisplay = "请选择";
        } else {
            selectedDisplay = "全部";
        }
        EnterpriseInfo info = new EnterpriseInfo();
        info.setEnterpriseType(enterpriseType);
        List<EnterpriseInfo> list = enterpriseInfoService.getAllEnterpriseInfo(info);
        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        map = new HashMap<Object, Object>();
        map.put("ENTERPRISENAME", selectedDisplay);
        map.put("ENTERPRISEID", "");
        map.put("selected", true);
        maps.add(map);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                EnterpriseInfo enterpriseInfo = list.get(i);
                map = new HashMap<Object, Object>();
                map.put("ENTERPRISENAME", enterpriseInfo.getEnterpriseName());
                map.put("ENTERPRISEID", enterpriseInfo.getEnterpriseId());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 跳转到：企业-详情
     */
    @RequestMapping(value = "/to_enterprise_detail_part")
    public ModelAndView to_enterprise_detail_part(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseId);
        mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
        mv.addObject("organizationCode", enterpriseInfo.getOrganizationCode());
        mv.addObject("legalPersonName", enterpriseInfo.getLegalPersonName());
        mv.addObject("enterpriseId", enterpriseId);
        mv.addObject("enterpriseInfo", enterpriseInfo);
        mv.setViewName("jsp/enterprise/detail/part");
        return mv;
    }

    /**
     * 跳转到：企业-基本信息
     */
    @RequestMapping(value = "/to_enterprise_detail_baseInfo")
    public ModelAndView to_enterprise_detail_baseInfo(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        // 企业信息
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseId);
        mv.addObject("enterprise", enterpriseInfo);

        // 附件信息
        List<EnterpriseUploadSnapshotVO> cusvoList = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseId, AttachmentIsCode.NO_CODE.getValue());
        for (EnterpriseUploadSnapshotVO cusvo : cusvoList) {
            cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/") + 1, cusvo.getAttachment().getUrl().lastIndexOf(".")));
        }
        mv.addObject("cusvoList", cusvoList);
        mv.setViewName("jsp/enterprise/detail/baseInfo");
        return mv;
    }

    /**
     * 获取合作公司列表
     *
     * @param selectedDisplay 默认显示值
     * @return
     */
    @RequestMapping(value = "/loadCoLtd")
    @ResponseBody
    public Object loadCoLtd(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay,
                            @RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        if ("selected".equals(selectedDisplay)) {
            selectedDisplay = "请选择";
        } else {
            selectedDisplay = "全部";
        }
        List<CoLtd> list = coLtdService.getAllCoLtd(enterpriseId);
        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        map = new HashMap<Object, Object>();
        map.put("COMPANYNAME", selectedDisplay);
        map.put("COID", "");
        map.put("selected", true);
        maps.add(map);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CoLtd coLtd = list.get(i);
                map = new HashMap<Object, Object>();
                map.put("COMPANYNAME", coLtd.getCompanyName());
                map.put("COID", coLtd.getCoId());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 跳转到：企业-额度记录-列表
     */
    @RequestMapping(value = "/toQuotaRecordList")
    public ModelAndView toQuotaRecordList(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("enterpriseId", enterpriseId);
        mv.setViewName("jsp/enterprise/detail/quotaRecordList");
        return mv;
    }

    /**
     * 执行：额度记录列表
     *
     * @param pageSize 页数
     * @param pageNo   页码
     */
    @RequestMapping(value = "/quotaRecordList")
    @ResponseBody
    public Object quotaRecordList(HttpServletRequest request, HttpSession session,
                                  @RequestParam(value = "enterpriseId", required = false) Long enterpriseId) throws Exception {

        return quotaRecordService.getAllByEnterpriseId(enterpriseId);
    }

    /**
     * 跳转到：企业-人员-列表
     */
    @RequestMapping(value = "/toUserList")
    public ModelAndView toUserList(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("enterpriseId", enterpriseId);
        mv.setViewName("jsp/enterprise/detail/userList");
        return mv;
    }

    /**
     * 执行：分页列表
     *
     * @param pageSize 页数
     * @param pageNo   页码
     */
    @RequestMapping(value = "/userList")
    @ResponseBody
    public Object userList(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                           @RequestParam(value = "page", defaultValue = "1") int pageNo,
                           @RequestParam(value = "enterpriseId", required = false) Long enterpriseId,
                           @RequestParam(value = "realName", required = false) String realName,
                           @RequestParam(value = "loginName", required = false) String loginName,
                           @RequestParam(value = "status", required = false) String status) throws Exception {

        // 填充参数
        Map params = new HashMap();
        params.put("enterpriseId", enterpriseId);
        if (null != realName && !"".equals(realName)) {
            params.put("realName", realName);
        }
        if (null != loginName && !"".equals(loginName)) {
            params.put("loginName", loginName);
        }
        if (null != status && !"".equals(status)) {
            params.put("status", status);
        }
        return enterpriseUserService.findAllByPage(pageNo, pageSize, params);
    }

    /**
     * 跳转到：企业-人员-添加
     */
    @RequestMapping(value = "/toAddUser")
    public ModelAndView toAddUser(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("enterpriseId", enterpriseId);
        mv.setViewName("jsp/enterprise/detail/userAdd");
        return mv;
    }

    /**
     * 跳转到：企业-人员-编辑
     */
    @RequestMapping(value = "/toEditUser")
    public ModelAndView toEditUser(
            @RequestParam(value = "enterpriseId", required = false) Long enterpriseId,
            @RequestParam(value = "enterpriseUserId", required = false) Long enterpriseUserId) {
        ModelAndView mv = new ModelAndView();
        EnterpriseUser enterpriseUser = enterpriseUserService.getById(enterpriseUserId);
        UserInfo userInfo = userInfoService.getUserExtByUserId(enterpriseUser.getUserId());
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(enterpriseUser.getUserId());
        mv.addObject("enterpriseId", enterpriseId);
        mv.addObject("enterpriseUserId", enterpriseUserId);
        mv.addObject("realName", userInfoExt.getRealName());
        mv.addObject("loginName", userInfo.getLoginName());
        mv.addObject("mobileNo", userInfo.getMobileNo());
        mv.addObject("email", userInfoExt.getEmail());
        mv.setViewName("jsp/enterprise/detail/userAdd");
        return mv;
    }

    /**
     * 执行：保存企业人员信息
     */
    @RequestMapping("/saveUser")
    @ResponseBody
    public Object addUser(HttpServletRequest request, HttpSession session,
                          @RequestParam(value = "enterpriseId", required = false) String enterpriseId,
                          @RequestParam(value = "enterpriseUserId", required = false) String enterpriseUserId,
                          @RequestParam(value = "realName", required = false) String realName,
                          @RequestParam(value = "loginName", required = false) String loginName,
                          @RequestParam(value = "mobileNo", required = false) String mobileNo,
                          @RequestParam(value = "email", required = false) String email) {
        try {
            // 合法性验证。
            if (null == enterpriseId || "".equals(enterpriseId)) {
                return returnResultMap(false, null, "check", "企业ID不能为空！");
            }
            if (null == realName || "".equals(realName)) {
                return returnResultMap(false, null, "check", "姓名不能为空！");
            }
            if (null == loginName || "".equals(loginName)) {
                return returnResultMap(false, null, "check", "用户名不能为空！");
            }
            if (null == mobileNo || "".equals(mobileNo)) {
                return returnResultMap(false, null, "check", "手机号不能为空！");
            }

            // 根据企业ID,加载一条
            EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
            if (null == enterpriseInfo) {
                return returnResultMap(false, null, "check", "获取企业信息失败！");
            }

            if (null == enterpriseUserId || "".equals(enterpriseUserId)) {
                //用户信息
                UserInfo user = new UserInfo();
                user.setLoginName(loginName);
                user.setMobileNo(mobileNo);
                user.setLoginPass(String.valueOf(new Date().getTime()));
                user.setCreateTime(new Date());
                //用户扩展
                UserInfoExt ext = new UserInfoExt();
                ext.setRealName(realName);
                ext.setEmail(email);
                ext.setMobileNo(mobileNo);

                // 执行添加
                enterpriseUserService.addEnterpriseUser(enterpriseInfo.getEnterpriseId(), user, ext);
            } else {
                EnterpriseUser enterpriseUser = enterpriseUserService.getById(Long.valueOf(enterpriseUserId));
                if (null == enterpriseUser) {
                    return returnResultMap(false, null, "check", "企业用户关联不存在！");
                }
                UserInfo userInfo = userInfoService.getUserExtByUserId(enterpriseUser.getUserId());
                userInfo.setLoginName(loginName);
                userInfo.setMobileNo(mobileNo);
                UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(enterpriseUser.getUserId());
                userInfoExt.setRealName(realName);
                userInfoExt.setEmail(email);
                userInfoExt.setMobileNo(mobileNo);

                // 执行修改
                enterpriseUserService.editEnterpriseUser(userInfo, userInfoExt);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 获取该公司及该公司人员的银行卡
     *
     * @param selectedDisplay 默认显示值
     * @return
     */
    @RequestMapping(value = "/loadCard")
    @ResponseBody
    public Object loadCard(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay,
                           @RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        if ("selected".equals(selectedDisplay)) {
            selectedDisplay = "请选择";
        } else {
            selectedDisplay = "全部";
        }
        List<CustomerCard> list = customerCardService.getCardByEnterpriseId(enterpriseId);
        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        map = new HashMap<Object, Object>();
        map.put("CARDCODE", selectedDisplay);
        map.put("CARDID", "");
        map.put("selected", true);
        maps.add(map);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CustomerCard card = list.get(i);
                map = new HashMap<Object, Object>();
                map.put("CARDCODE", StringUtils.getPreStr(card.getCardCode(), 4) + "********" + StringUtils.getLastStr(card.getCardCode(), 4) + "(" + card.getCardCustomerName() + ")");
                map.put("CARDID", card.getCustomerCardId());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 跳至:企业充值页面
     */
    @RequestMapping(value = "/toIncome")
    public ModelAndView toIncome(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        EnterpriseInfoExt enterpriseInfoExt = enterpriseInfoService.getEnterpriseInfoDetail(enterpriseId);
        mv.addObject("enterpriseInfoExt", enterpriseInfoExt);
        mv.setViewName("jsp/enterprise/enterprise_income");
        return mv;
    }

    /**
     * 执行：企业充值
     */
    @RequestMapping(value = "income")
    @ResponseBody
    public String income(@ModelAttribute RechargeOrder rechargeOrder) {
        try {
            enterpriseInfoService.enterpriseRecharge(rechargeOrder);
            return "success";
        } catch (SystemException se) {
            se.printStackTrace();
            return se.getMessage();
        }
    }

    /**
     * 跳至：企业提现页面
     */
    @RequestMapping(value = "/toWithDraw")
    public ModelAndView toWithDraw(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId) {
        ModelAndView mv = new ModelAndView();
        //企业信息
        EnterpriseInfoExt enterpriseInfoExt = enterpriseInfoService.getEnterpriseInfoDetail(enterpriseId);
        mv.addObject("enterpriseInfoExt", enterpriseInfoExt);

        //准备银行信息
        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType(ConstantDefineCode.BANK.getValue());
        mv.addObject("constantDefines", constantDefines);

        //查询渠道最近使用的客户卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(enterpriseInfoExt.getUserId(), PayConstants.PayChannel.LL);
        mv.addObject("customerCard", customerCard);

        mv.setViewName("jsp/enterprise/enterprise_withdraw");
        return mv;
    }

    /**
     * 执行：企业提现
     */
    @RequestMapping(value = "withDraw")
    @ResponseBody
    public String withDraw(@ModelAttribute WithDrawExt withDraw) {
        try {
            withDraw.setHappenType(WithDrawSource.USER_WITHDRAW.getValue());
            enterpriseInfoService.enterpriseWithDraw(withDraw);
            return "success";
        } catch (SystemException se) {
            se.printStackTrace();
            return se.getMessage();
        }


    }

    private AccountConstants.AccountTypeEnum getEnum(String accType) {
        if (AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue().equals(accType))
            return AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT;
        if (AccountConstants.AccountTypeEnum.PLATFORM_PAYMENT.getValue().equals(accType))
            return AccountConstants.AccountTypeEnum.PLATFORM_PAYMENT;
        if (AccountConstants.AccountTypeEnum.PLATFORM_RISK.getValue().equals(accType))
            return AccountConstants.AccountTypeEnum.PLATFORM_RISK;
        if (AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue().equals(accType))
            return AccountConstants.AccountTypeEnum.PLATFORM_OPERATING;
        return null;
    }

    /**
     * 报备表中的数据设置为已开户成功
     *
     * @param rechargeResponse
     */
    private void getTradeUpdate(OpenAccount4PCEnterpriseResponse rechargeResponse) {
        Trade trade = new Trade();
        rechargeResponse.getMchnt_txn_ssn();
        rechargeResponse.getSignature();
        String inputStr = rechargeResponse.regSignVal();
        trade.setResponse_message(inputStr);
        trade.setSerial_number(rechargeResponse.getMchnt_txn_ssn());
        trade.setTrade_status(ResponseStatusEnum.Success.getValue());
        trade.setResponse_message(inputStr);
        tradeService.updateByPrimaryKeySelective(trade);
    }

    /**
     * 向报备表中增加数据
     *
     * @param dataSource
     */
    private void addTrade(LegalPersonDataSource dataSource) {
        Trade trade = new Trade();
        String outputStr = dataSource.regSignVal();
        trade.setMessage_id(TradeTypeEnum.CorpRegist.getValue());
        trade.setTrade_status(ResponseStatusEnum.Unresponsive.getValue());
        trade.setRequest_message(outputStr);
        trade.setTrade_date(new Date());
        trade.setSerial_number(dataSource.getMchnt_txn_ssn());
        trade.setUser_id(Long.parseLong(dataSource.getUser_id_from()));
        tradeService.addTrade(trade);
    }
}
