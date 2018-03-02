package com.xt.cfp.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.AccountConstants.AccountTypeEnum;
import com.xt.cfp.core.constants.AttachmentIsCode;
import com.xt.cfp.core.constants.EnterpriseConstants;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.constants.SubjectTypeEnum;
import com.xt.cfp.core.constants.VerifyType;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.Attachment;
import com.xt.cfp.core.pojo.BondSource;
import com.xt.cfp.core.pojo.CarChangeSnapshot;
import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.CoLtd;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.CustomerUploadSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCarLoanSnapshot;
import com.xt.cfp.core.pojo.EnterpriseCreditSnapshot;
import com.xt.cfp.core.pojo.EnterpriseFactoringSnapshot;
import com.xt.cfp.core.pojo.EnterpriseFoundationSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.EnterprisePledgeSnapshot;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanProduct;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.pojo.MortgageCarSnapshot;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.ext.BondSourceUser;
import com.xt.cfp.core.pojo.ext.CustomerUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.VerifyVO;
import com.xt.cfp.core.service.BondSourceService;
import com.xt.cfp.core.service.CityInfoService;
import com.xt.cfp.core.service.CoLtdService;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.EnterpriseCarLoanSnapshotService;
import com.xt.cfp.core.service.EnterpriseCreditSnapshotService;
import com.xt.cfp.core.service.EnterpriseFactoringSnapshotService;
import com.xt.cfp.core.service.EnterpriseFoundationSnapshotService;
import com.xt.cfp.core.service.EnterpriseInfoService;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.EnterprisePledgeSnapshotService;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.MainLoanApplicationService;
import com.xt.cfp.core.service.MortgageCarSnapshotService;
import com.xt.cfp.core.service.ProvinceInfoService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.VerifyService;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;

@Controller
@RequestMapping("/jsp/enterprise/loan")
public class EnterpriseLoanController extends BaseController {

	@Autowired
	private EnterpriseInfoService enterpriseInfoService;

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
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;

	@Autowired
	private MortgageCarSnapshotService mortgageCarSnapshotService;

	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	
	@Autowired
	private ProvinceInfoService provinceInfoService;
	
	@Autowired
	private CityInfoService cityInfoService;

	@Autowired
	private CoLtdService coLtdService;

	@Autowired
	private FeesItemService feesItemService;
	
	@Autowired
	private LoanProductService loanProductService;
	
	@Autowired
	private CustomerCardService customerCardService;
	
	@Autowired
	private VerifyService verifyService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private ConstantDefineCached constantDefineCached;
	
	@Autowired
    private BondSourceService bondSourceService;
	
	/**
	 * 跳转到：企业借款列表
	 */
	@RequestMapping(value = "/to_loan_list")
    public ModelAndView to_loan_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/enterprise/loan/loan_list");
        return mv;
    }
	
	/**
	 * 执行：企业借款列表【main】
	 */
    @RequestMapping(value = "/loan_list")
    @ResponseBody
    public Object loan_list(HttpServletRequest request,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
    		@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "loanApplicationCode", required = false) String loanApplicationCode,
			@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,
			@RequestParam(value = "loanType", required = false) String loanType,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "idCard", required = false) String idCard, 
			@RequestParam(value = "mobileNo", required = false) String mobileNo,
			@RequestParam(value = "applicationState", required = false) String applicationState) {
    	
    	// 填充参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationCode", loanApplicationCode);
        params.put("loanApplicationName", loanApplicationName);
        params.put("loanType", loanType);
        params.put("realName", realName);
        params.put("idCard", idCard);
        params.put("mobileNo", mobileNo);
        params.put("applicationState", applicationState);
        return loanApplicationService.findAllEnterpriseLoanByPage(pageNo, pageSize, params);
    }
    
    @RequestMapping(value = "/exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
    		@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "loanApplicationCode", required = false) String loanApplicationCode,
			@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,
			@RequestParam(value = "loanType", required = false) String loanType,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "idCard", required = false) String idCard, 
			@RequestParam(value = "mobileNo", required = false) String mobileNo,
			@RequestParam(value = "applicationState", required = false) String applicationState) {
    	
    	// 填充参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationCode", loanApplicationCode);
        params.put("loanApplicationName", loanApplicationName);
        params.put("loanType", loanType);
        params.put("realName", realName);
        params.put("idCard", idCard);
        params.put("mobileNo", mobileNo);
        params.put("applicationState", applicationState);
        
        loanApplicationService.exportExcel(response, params);
    }
    
    /**
     * 跳转到：企业借款
     */
    @RequestMapping(value = "/to_enterprise_loan")
    public ModelAndView to_enterprise_load() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/enterprise/enterprise_loan");
        return mv;
    }
	
    /**
     * 跳转到：车贷、信贷、保理【main】
     */
    @RequestMapping(value = "/to_part")
    public ModelAndView to_car_loan_part(
    		@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId,
    		@RequestParam(value = "actionType", required = false) String actionType,
    		@RequestParam(value = "loanType", required = false) String loanType) {
    	ModelAndView mv = new ModelAndView();
    	
		if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanType)){
			mv.setViewName("jsp/enterprise/car/car_loan_part");
		}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType)){
			mv.setViewName("jsp/enterprise/credit/credit_part");
		}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanType)){
			mv.setViewName("jsp/enterprise/factoring/factoring_part");
		}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)){
			mv.setViewName("jsp/enterprise/foundation/foundation_part");
		}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)){
			mv.setViewName("jsp/enterprise/pledge/pledge_part");
		}
    	
		MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
		mv.addObject("applicationState", mainLoanApplication.getApplicationState());
    	mv.addObject("loanApplicationId", loanApplicationId);
    	mv.addObject("actionType", actionType);
        return mv;
    }
    
    /**
     * 跳转到：车贷 add1【main】
     */
    @RequestMapping(value = "/to_car_loan_add_part1")
    public ModelAndView to_car_loan_add_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(loanApplicationId);
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("loanApplicationId", loanApplicationId);
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("organizationCode", enterpriseInfo.getOrganizationCode());
    	mv.addObject("legalPersonName", enterpriseInfo.getLegalPersonName());
        mv.setViewName("jsp/enterprise/car/car_loan_add_part1");
        return mv;
    }
    
    /**
     * 跳转到：车贷 add2
     */
    @RequestMapping(value = "/to_car_loan_add_part2")
    public ModelAndView to_car_loan_add_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/enterprise/car/car_loan_add_part2");
        return mv;
    }
    
    /**
     * 跳转到：车贷add3
     */
    @RequestMapping(value = "/to_car_loan_add_part3")
    public ModelAndView to_car_loan_add_part3(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/enterprise/car/car_loan_add_part3");
        return mv;
    }
    
    /**
     * 跳转到：信贷add1【main】
     */
    @RequestMapping(value = "/to_credit_add_part1")
    public ModelAndView to_credit_add_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(loanApplicationId);
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("loanApplicationId", loanApplicationId);
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("organizationCode", enterpriseInfo.getOrganizationCode());
    	mv.addObject("legalPersonName", enterpriseInfo.getLegalPersonName());
        mv.setViewName("jsp/enterprise/credit/credit_add_part1");
        return mv;
    }
    
    /**
     * 跳转到：信贷add2
     */
    @RequestMapping(value = "/to_credit_add_part2")
    public ModelAndView to_credit_add_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/enterprise/credit/credit_add_part2");
        return mv;
    }
    
    /**
     * 跳转到：保理add1【main】
     */
    @RequestMapping(value = "/to_factoring_add_part1")
    public ModelAndView to_factoring_add_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(loanApplicationId);
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("loanApplicationId", loanApplicationId);
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
        mv.setViewName("jsp/enterprise/factoring/factoring_add_part1");
        return mv;
    }

	/**
     * 跳转到：基金add1【main】
     */
    @RequestMapping(value = "/to_foundation_add_part1")
    public ModelAndView to_foundation_add_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(loanApplicationId);
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("loanApplicationId", loanApplicationId);
    	mv.addObject("enterpriseInfo", enterpriseInfo);
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
        mv.setViewName("jsp/enterprise/foundation/foundation_add_part1");
        return mv;
    }
    
    /**
     * 跳转到：保理add2
     */
    @RequestMapping(value = "/to_factoring_add_part2")
    public ModelAndView to_factoring_add_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/enterprise/factoring/factoring_add_part2");
        return mv;
    }
	/**
     * 跳转到：基金add2
     */
    @RequestMapping(value = "/to_foundation_add_part2")
    public ModelAndView to_foundation_add_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/enterprise/foundation/foundation_add_part2");
        return mv;
    }

    /**
     * 跳转到：车贷 edit1【main】
     */
    @RequestMapping(value = "/to_car_loan_edit_part1")
    public ModelAndView to_car_loan_edit_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	//借款申请
    	MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
    	mv.addObject("loan", mainLoanApplication);
    	//企业
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("organizationCode", enterpriseInfo.getOrganizationCode());
    	mv.addObject("legalPersonName", enterpriseInfo.getLegalPersonName());
    	//车贷快照
    	EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	mv.addObject("car", carLoanSnapshot);
    	//省份
    	List<ProvinceInfo> provinceInfos = provinceInfoService.getAllProvinceInfo();
    	mv.addObject("provinceInfos", provinceInfos);
    	//城市
    	List<CityInfo> bornCitys = cityInfoService.getCityByProvinceIdAndPId(carLoanSnapshot.getProvince(), 0l);
    	mv.addObject("bornCitys", bornCitys);
        mv.setViewName("jsp/enterprise/car/car_loan_edit_part1");
        return mv;
    }
    
    /**
     * 跳转到：车贷 edit2【main】
     */
    @RequestMapping(value = "/to_car_loan_edit_part2")
    public ModelAndView to_car_loan_edit_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
    	//车贷快照
    	EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(loanApplicationId);
    	mv.addObject("car", carLoanSnapshot);
    	//抵押车信息列表
    	List<MortgageCarSnapshot> mortgageCarSnapshotList = mortgageCarSnapshotService.getListByCarLoanId(carLoanSnapshot.getEnterpriseCarLoanId());
    	//获取首条，单独传送
    	MortgageCarSnapshot mortgageCarSnapshot = null;
    	if(null != mortgageCarSnapshotList && mortgageCarSnapshotList.size() > 0){
    		mortgageCarSnapshot = mortgageCarSnapshotList.get(0);
    		mortgageCarSnapshotList.remove(0);
    	}
    	mv.addObject("mortgageCar", mortgageCarSnapshot);
    	mv.addObject("mortgageCarList", JSONObject.toJSON(mortgageCarSnapshotList));
        mv.setViewName("jsp/enterprise/car/car_loan_edit_part2");
        return mv;
    }
    
    /**
     * 跳转到：车贷edit3【main】
     */
    @RequestMapping(value = "/to_car_loan_edit_part3")
    public ModelAndView to_car_loan_edit_part3(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
    	//企业借款上传快照
        List<CustomerUploadSnapshotVO>  cusvoList = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId, AttachmentIsCode.NO_CODE.getValue());
        for(CustomerUploadSnapshotVO cusvo : cusvoList){
        	cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/")+1,cusvo.getAttachment().getUrl().lastIndexOf(".")));
        }
        mv.addObject("cusvoList",cusvoList);
        mv.setViewName("jsp/enterprise/car/car_loan_edit_part3");
        return mv;
    }
    
    /**
     * 跳转到：信贷edit1【main】
     */
    @RequestMapping(value = "/to_credit_edit_part1")
    public ModelAndView to_credit_edit_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	//借款申请
    	MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
    	mv.addObject("loan", mainLoanApplication);
    	//企业
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("organizationCode", enterpriseInfo.getOrganizationCode());
    	mv.addObject("legalPersonName", enterpriseInfo.getLegalPersonName());
    	//信贷快照
    	EnterpriseCreditSnapshot creditSnapshot = enterpriseCreditSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	mv.addObject("credit", creditSnapshot);
    	//省份
    	List<ProvinceInfo> provinceInfos = provinceInfoService.getAllProvinceInfo();
    	mv.addObject("provinceInfos", provinceInfos);
    	//城市
    	List<CityInfo> bornCitys = cityInfoService.getCityByProvinceIdAndPId(creditSnapshot.getProvince(), 0l);
    	mv.addObject("bornCitys", bornCitys);
        mv.setViewName("jsp/enterprise/credit/credit_edit_part1");
        return mv;
    }
    
    /**
     * 跳转到：信贷edit2【main】
     */
    @RequestMapping(value = "/to_credit_edit_part2")
    public ModelAndView to_credit_edit_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
    	//企业借款上传快照
        List<CustomerUploadSnapshotVO>  cusvoList = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId, AttachmentIsCode.NO_CODE.getValue());
        for(CustomerUploadSnapshotVO cusvo : cusvoList){
        	cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/")+1,cusvo.getAttachment().getUrl().lastIndexOf(".")));
        }
        mv.addObject("cusvoList",cusvoList);
        mv.setViewName("jsp/enterprise/credit/credit_edit_part2");
        return mv;
    }
    
    /**
     * 跳转到：企业贷edit1【main】
     */
    @RequestMapping(value = "/to_pledge_edit_part1")
    public ModelAndView to_pledge_edit_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	//借款申请
    	MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
    	mv.addObject("loan", mainLoanApplication);
    	//企业
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("organizationCode", enterpriseInfo.getOrganizationCode());
    	mv.addObject("legalPersonName", enterpriseInfo.getLegalPersonName());
    	//信贷快照
    	EnterprisePledgeSnapshot pledgeSnapshot = enterprisePledgeSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	mv.addObject("pledge", pledgeSnapshot);
    	//省份
    	List<ProvinceInfo> provinceInfos = provinceInfoService.getAllProvinceInfo();
    	mv.addObject("provinceInfos", provinceInfos);
    	//城市
    	List<CityInfo> bornCitys = cityInfoService.getCityByProvinceIdAndPId(pledgeSnapshot.getProvince(), 0l);
    	mv.addObject("bornCitys", bornCitys);
        mv.setViewName("jsp/enterprise/pledge/pledge_edit_part1");
        return mv;
    }
    
    /**
     * 跳转到：企业贷edit2【main】
     */
    @RequestMapping(value = "/to_pledge_edit_part2")
    public ModelAndView to_pledge_edit_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
    	//企业借款上传快照
        List<CustomerUploadSnapshotVO>  cusvoList = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId, AttachmentIsCode.NO_CODE.getValue());
        for(CustomerUploadSnapshotVO cusvo : cusvoList){
        	cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/")+1,cusvo.getAttachment().getUrl().lastIndexOf(".")));
        }
        mv.addObject("cusvoList",cusvoList);
        mv.setViewName("jsp/enterprise/pledge/pledge_edit_part2");
        return mv;
    }
    
    
    /**
     * 跳转到：保理edit1【main】
     */
    @RequestMapping(value = "/to_factoring_edit_part1")
    public ModelAndView to_factoring_edit_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	//借款申请
    	MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
    	mv.addObject("loan", mainLoanApplication);
    	//企业
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
    	//保理快照
    	EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	mv.addObject("factoring", factoringSnapshot);
        mv.setViewName("jsp/enterprise/factoring/factoring_edit_part1");
        return mv;
    }
    
	/**
     * 跳转到：基金edit1【main】
     */
    @RequestMapping(value = "/to_foundation_edit_part1")
    public ModelAndView to_foundation_edit_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	//借款申请
    	MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
    	mv.addObject("loan", mainLoanApplication);
    	//企业
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
		mv.addObject("enterpriseInfo", enterpriseInfo);
    	//基金快照
		EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
    	mv.addObject("foundation", foundationSnapshot);
		//详情
		Attachment attachment = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getAttachId());
		mv.addObject("attachment", attachment);
		//交易说明书
		Attachment tradeBook = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getTradeBookId());
		mv.addObject("tradeBook", tradeBook);
		//风险提示函
		Attachment riskTip = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getRiskTipId());
		mv.addObject("riskTip", riskTip);
        mv.setViewName("jsp/enterprise/foundation/foundation_edit_part1");
        return mv;
    }
    
    /**
     * 跳转到：保理edit2【main】
     */
    @RequestMapping(value = "/to_factoring_edit_part2")
    public ModelAndView to_factoring_edit_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
    	//企业借款上传快照
        List<CustomerUploadSnapshotVO>  cusvoList = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId, AttachmentIsCode.NO_CODE.getValue());
        for(CustomerUploadSnapshotVO cusvo : cusvoList){
        	cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/")+1,cusvo.getAttachment().getUrl().lastIndexOf(".")));
        }
        mv.addObject("cusvoList",cusvoList);
        mv.setViewName("jsp/enterprise/factoring/factoring_edit_part2");
        return mv;
    }

	/**
     * 跳转到：基金edit2【main】
     */
    @RequestMapping(value = "/to_foundation_edit_part2")
    public ModelAndView to_foundation_edit_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
    	//企业借款上传快照
        List<CustomerUploadSnapshotVO>  cusvoList = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId, AttachmentIsCode.NO_CODE.getValue());
        for(CustomerUploadSnapshotVO cusvo : cusvoList){
        	cusvo.setFileName(cusvo.getAttachment().getUrl().substring(cusvo.getAttachment().getUrl().lastIndexOf("/")+1,cusvo.getAttachment().getUrl().lastIndexOf(".")));
        }
        mv.addObject("cusvoList",cusvoList);
        mv.setViewName("jsp/enterprise/foundation/foundation_edit_part2");
        return mv;
    }
    
    /**
     * 跳转到：企业贷add1【main】
     */
    @RequestMapping(value = "/to_pledge_add_part1")
    public ModelAndView to_pledge_add_part1(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(loanApplicationId);
    	EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
    	mv.addObject("loanApplicationId", loanApplicationId);
    	mv.addObject("enterpriseId", enterpriseInfo.getEnterpriseId());
    	mv.addObject("enterpriseName", enterpriseInfo.getEnterpriseName());
    	mv.addObject("organizationCode", enterpriseInfo.getOrganizationCode());
    	mv.addObject("legalPersonName", enterpriseInfo.getLegalPersonName());
        mv.setViewName("jsp/enterprise/pledge/pledge_add_part1");
        return mv;
    }
    
    /**
     * 跳转到：企业贷add2
     */
    @RequestMapping(value = "/to_pledge_add_part2")
    public ModelAndView to_pledge_add_part2(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	mv.addObject("loanApplicationId", loanApplicationId);
        mv.setViewName("jsp/enterprise/pledge/pledge_add_part2");
        return mv;
    }
    
    /**
     * 执行：保存1页。【main】
     */
    @RequestMapping("/save_enterprise_loan")
    @ResponseBody
    public Object save_enterprise_loan(
    		@RequestParam(value = "subjectType", required = false) String subjectType,//标的类型（1借款;0债权）
            @RequestParam(value = "channel", required = false) String channel,//来源类型（1.渠道：channel;2.门店：store）
            @RequestParam(value = "channelId", required = false) String channelId,//渠道
            @RequestParam(value = "originalUserId", required = false) String originalUserId,//原始债权
            @RequestParam(value = "store_channelId", required = false) String store_channelId,//门店
    		@RequestParam(value = "loanType", required = false) String loanType,//借款类型（2.企业车贷、3.企业信用贷、4.企业保理、5.基金、6.企业标）
            @RequestParam(value = "enterpriseId", required = false) String enterpriseId) {//企业ID
        Map<String, Comparable> resultMap = new HashMap<String, Comparable>();
        try {
        	// 获取当前登录用户信息
        	AdminInfo adminInfo = getCurrentUser();
            if(adminInfo == null){
            	return returnResultMap(false, null, "check", "获取当前用户信息失败！");
            }
            
            // 债权标验证（开始）
            // 标的类型
            if (null == subjectType || "".equals(subjectType)) {
                return returnResultMap(false, null, "check", "标的类型不能为空！");
            }
            
            // 如果是债权标则必须是渠道
            if (LoanApplication.SUBJECTTYPE_RIGHTSMARK.equals(subjectType)) {
                if (!LoanApplication.CHANNEL_CHANNEL.equals(channel)) {// 渠道
                    return returnResultMap(false, null, "check", "债权标必须是渠道类型！");
                }
                
                // 来源类型
                if (LoanApplication.CHANNEL_CHANNEL.equals(channel)) {// 渠道
                    if (null == channelId || "".equals(channelId)) {
                        return returnResultMap(false, null, "check", "渠道不能为空！");
                    }
                    if (subjectType.equals(SubjectTypeEnum.CREDITOR.getValue())) {//债权标，原始债权人不能为空
                        if (null == originalUserId || "".equals(originalUserId)) {
                            return returnResultMap(false, null, "check", "原始债权不能为空！");
                        }
                    }
                } else {// 门店
                    if (null == store_channelId || "".equals(store_channelId)) {
                        return returnResultMap(false, null, "check", "门店不能为空！");
                    }
                }
            }
            // 债权标验证（结束）
            
        	// 合法性验证。
 			if(null == enterpriseId || "".equals(enterpriseId)){
 				return returnResultMap(false, null, "check", "企业信息不能为空！");
 			}
 			if(null == loanType || "".equals(loanType)){
 				return returnResultMap(false, null, "check", "借款类型不能为空！");
 			}else if (!LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanType) &&
 					!LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType) &&
					!LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)&&
 					!LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanType)&&
 					!LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)) {
 				return returnResultMap(false, null, "check", "参数值异常！");
			}
 			
 			// 企业
 			EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(Long.valueOf(enterpriseId));
 			if(null == enterpriseInfo){
 				return returnResultMap(false, null, "check", "企业信息获取失败！");
 			}
 			
 			// 借款申请，字段填充
 			MainLoanApplication mainLoanApplication = new MainLoanApplication();
 			
 			// 债权标（开始）
 			mainLoanApplication.setSubjectType(subjectType);
 			if(LoanApplication.SUBJECTTYPE_RIGHTSMARK.equals(subjectType)){
 				mainLoanApplication.setChannel(channel);
 	            if (LoanApplication.CHANNEL_CHANNEL.equals(channel)) {// 渠道
 	                if (null != channelId && !"".equals(channelId)) {
 	                	mainLoanApplication.setChannelId(Long.valueOf(channelId));
 	                }
 	                if (null != originalUserId && !"".equals(originalUserId)) {
 	                	mainLoanApplication.setOriginalUserId(Long.valueOf(originalUserId));
 	                }
 	                if (SubjectTypeEnum.CREDITOR.getValue().equals(subjectType)) {
 	                    if (null != channelId && !"".equals(channelId)) {
 	                        BondSource bondSource = bondSourceService.getBondSourceByBondSourceId(Long.valueOf(channelId));
 	                        UserAccount bondSourceAccount = userAccountService.getCashAccount(bondSource.getUserId());
 	                        mainLoanApplication.setRepaymentAccountId(bondSourceAccount.getAccId());
 	                    }
 	                    if (null != originalUserId && !"".equals(originalUserId)) {
 	                        BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(Long.valueOf(originalUserId));
 	                        UserAccount bondSourceUserAccount = userAccountService.getCashAccount(bondSourceUser.getUserId());
 	                        mainLoanApplication.setCustomerAccountId(bondSourceUserAccount.getAccId());
 	                    }
 	                }
 	            } else {// 门店
 	                if (null != store_channelId && !"".equals(store_channelId)) {
 	                	mainLoanApplication.setChannelId(Long.valueOf(store_channelId));
 	                }
 	            }
 			}else{
 				// 借款人账户
 	            UserAccount customerAccount = userAccountService.getUserAccountByUserIdAndAccountTypeCode(enterpriseInfo.getUserId(), AccountTypeEnum.BORROW_ACCOUNT.getValue());
 	            if(null != customerAccount){
 	            	mainLoanApplication.setCustomerAccountId(customerAccount.getAccId());//放款账户id
 	            	mainLoanApplication.setRepaymentAccountId(customerAccount.getAccId());//还款账户id
 	            }
 			}
 			// 债权标（结束）
 			
 			mainLoanApplication.setLoanType(loanType);//借款类型
 			mainLoanApplication.setRecordTime(new Date());//录入时间
 			mainLoanApplication.setCreateTime(new Date());//创建时间
 			mainLoanApplication.setApplicationState(LoanApplication.APPLICATIONSTATE_DRAFT);//借款状态
 			mainLoanApplication.setRecorderPersonnel(adminInfo.getAdminId());//录入人员id
 			mainLoanApplication.setRecorderName(adminInfo.getDisplayName());//录入人员姓名
 			mainLoanApplication.setUserId(enterpriseInfo.getUserId());//注：将企业虚拟用户赋值给借款申请
 			
            //生成借款申请编号（开始）,规则：借款类型(QC/QX/QB/QF)YYMMDD+5位随机数
            StringBuffer loanApplicationCode = new StringBuffer();
            if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanType)) {
                loanApplicationCode.append("QC");
            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType)) {
                loanApplicationCode.append("QX");
            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanType)) {
                loanApplicationCode.append("QB");
            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)){
				loanApplicationCode.append("QF");
			} else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)) {
                loanApplicationCode.append("QZ");
            } 
            loanApplicationCode.append(new SimpleDateFormat("yyMMdd").format(new Date()));
            loanApplicationCode.append(StringUtils.generateRandomNumber(5));
            // 生成借款申请编号（结束）
            mainLoanApplication.setLoanApplicationCode(loanApplicationCode.toString());//借款申请编号
            
     		// 【***补充主借款信息-开始***】
            mainLoanApplication.setMainCode("Z-"+loanApplicationCode.toString());//主编号
            mainLoanApplication.setMainState("0");//主状态，(0.未发标；1.发标中；2.发标完成)
            mainLoanApplication.setMainCreateTime(new Date());//主创建时间
            mainLoanApplication.setMainUpdateTime(new Date());//主最后更改时间
            mainLoanApplication.setMainAdminId(adminInfo.getAdminId());//主最后操作人
            // 【***补充主借款信息-结束***】
            
 			// 【2.企业车贷】
 			if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanType)){
 	 			EnterpriseCarLoanSnapshot carLoanSnapshot = new EnterpriseCarLoanSnapshot();
 	 			carLoanSnapshot.setCreateTime(new Date());
 	 			carLoanSnapshot.setLastUpdateTime(new Date());
 	 			mainLoanApplication = enterpriseCarLoanSnapshotService.saveEnterpriseLoan(mainLoanApplication, enterpriseInfo, carLoanSnapshot);
 			}
 			// 【3.企业信用贷】
 			else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType)){
 	 			EnterpriseCreditSnapshot creditSnapshot = new EnterpriseCreditSnapshot();
 	 			creditSnapshot.setCreateTime(new Date());
 	 			creditSnapshot.setLastUpdateTime(new Date());
 	 			mainLoanApplication = enterpriseCreditSnapshotService.saveEnterpriseLoan(mainLoanApplication, enterpriseInfo, creditSnapshot);
 			}
 			// 【4.企业保理】
 			else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanType)){
 	 			EnterpriseFactoringSnapshot factoringSnapshot = new EnterpriseFactoringSnapshot();
 	 			factoringSnapshot.setCreateTime(new Date());
 	 			factoringSnapshot.setLastUpdateTime(new Date());
 	 			mainLoanApplication = enterpriseFactoringSnapshotService.saveEnterpriseLoan(mainLoanApplication, enterpriseInfo, factoringSnapshot);
 			}
			// 【5.企业基金】
			else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)){
				EnterpriseFoundationSnapshot foundationSnapshot = new EnterpriseFoundationSnapshot();
				foundationSnapshot.setCreateTime(new Date());
				foundationSnapshot.setLastUpdateTime(new Date());
				mainLoanApplication = enterpriseFoundationSnapshotService.saveEnterpriseLoan(mainLoanApplication, enterpriseInfo, foundationSnapshot);
			}
 			// 【6.企业标】
 			else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)){
 	 			EnterprisePledgeSnapshot pledgeSnapshot = new EnterprisePledgeSnapshot();
 	 			pledgeSnapshot.setCreateTime(new Date());
 	 			pledgeSnapshot.setLastUpdateTime(new Date());
 	 			mainLoanApplication = enterprisePledgeSnapshotService.saveEnterpriseLoan(mainLoanApplication, enterpriseInfo, pledgeSnapshot);
 			}

            resultMap.put("loanApplicationId", mainLoanApplication.getMainLoanApplicationId());
            resultMap.put("loanType", mainLoanApplication.getLoanType());
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, JSONObject.toJSON(resultMap), null, null);
    }
    
    /**
     * 执行：保存车贷【main】
     */
    @RequestMapping("/save_enterprise_loan_car")
    @ResponseBody
    public Object save_enterprise_loan_car(
    		@RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
    		@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,//借款申请名称
    		@RequestParam(value = "province", required = false) String province,//省
    		@RequestParam(value = "city", required = false) String city,//市
    		@RequestParam(value = "loanUseage", required = false) String loanUseage,//借款用途
    		@RequestParam(value = "useDescription", required = false) String useDescription,//用途描述
    		@RequestParam(value = "loanProductId", required = false) String loanProductId,//选择产品
    		@RequestParam(value = "loanBalance", required = false) String loanBalance,//借款金额
    		@RequestParam(value = "offlineApplyCode", required = false) String offlineApplyCode,//线下合同/订单号
    		@RequestParam(value = "projectDescription", required = false) String projectDescription,//项目描述
    		@RequestParam(value = "internalRating", required = false) String internalRating,//内部评级
    		@RequestParam(value = "riskControlInformation", required = false) String riskControlInformation,//风险控制信息
            @RequestParam(value = "inCardId", required = false) String inCardId) {//打款卡
        try {
        	// 合法性验证。
 			if(null == loanApplicationId || "".equals(loanApplicationId)){
 				return returnResultMap(false, null, "check", "借款申请ID不能为空！");
 			}
 			if(null == loanUseage || "".equals(loanUseage)){
 				return returnResultMap(false, null, "check", "借款用途不能为空！");
 			}
 			if(null == loanProductId || "".equals(loanProductId)){
 				return returnResultMap(false, null, "check", "产品ID不能为空！");
 			}
 			if(null == loanBalance || "".equals(loanBalance)){
 				return returnResultMap(false, null, "check", "借款金额不能为空！");
 			}
 			if(null == inCardId || "".equals(inCardId)){
 				return returnResultMap(false, null, "check", "打款卡不能为空！");
 			}
 			
 			// 根据借款申请【主】ID，加载一条借款数据
 			MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
 			if(null == mainLoanApplication){
 				return returnResultMap(false, null, "check", "获取借款申请信息失败！");
 			}
 			
 			mainLoanApplication.setLoanUseage(loanUseage);//借款用途
 			mainLoanApplication.setLoanProductId(Long.valueOf(loanProductId));//产品ID
 			LoanProduct loanProduct = loanProductService.findById(Long.valueOf(loanProductId));
 			mainLoanApplication.setAnnualRate(loanProduct.getAnnualRate());//年利率
 			if(null != loanBalance && !"".equals(loanBalance)){
 				mainLoanApplication.setLoanBalance(new BigDecimal(loanBalance));//借款金额
 				mainLoanApplication.setConfirmBalance(new BigDecimal(loanBalance));//批复金额
 				mainLoanApplication.setMainLoanBalance(new BigDecimal(loanBalance));//修改总借款金额（应对提交初审之后，还更新借款金额的情况）
 			}
 			mainLoanApplication.setOfflineApplyCode(offlineApplyCode);//线下合同号
 			if(null != inCardId && !"".equals(inCardId)){
 				mainLoanApplication.setInCardId(Long.valueOf(inCardId));//打款卡
 			}
 			mainLoanApplication.setLoanApplicationName(loanApplicationName);//借款申请名称
 			
 			// 根据借款申请【主】ID，加载一条车贷快照数据
 			EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
 			if(null == carLoanSnapshot){
 				return returnResultMap(false, null, "check", "获取车贷信息失败！");
 			}
 			
 			if(null != province && !"".equals(province)){
 				carLoanSnapshot.setProvince(Long.valueOf(province));//省份
 			}
 			if(null != city && !"".equals(city)){
 				carLoanSnapshot.setCity(Long.valueOf(city));//城市
 			}
 			carLoanSnapshot.setUseDescription(useDescription);//用途描述
 			projectDescription = projectDescription.replaceAll("\r\n", "<br>");
 			carLoanSnapshot.setProjectDescription(projectDescription);//项目描述
 			carLoanSnapshot.setInternalRating(internalRating);//内部评级
 			carLoanSnapshot.setRiskControlInformation(riskControlInformation);//风险控制信息
 			carLoanSnapshot.setLastUpdateTime(new Date());//最后更改时间
 			
 			mainLoanApplication = enterpriseCarLoanSnapshotService.saveEnterpriseLoanCar(mainLoanApplication, carLoanSnapshot);
 			
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：保存车贷，抵押车【main】
     */
    @RequestMapping("/save_enterprise_loan_mortgage_car")
    @ResponseBody
    public Object save_enterprise_loan_mortgage_car(
    		@RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
    		@RequestParam(value = "carInfoArray", required = false) String carInfoArray,//车信息数组
    		@RequestParam(value = "creditLimit", required = false) String creditLimit,//授信上限
    		@RequestParam(value = "mortgageDescription", required = false) String mortgageDescription) {//描述
        try {
        	// 合法性验证。
 			if(null == loanApplicationId || "".equals(loanApplicationId)){
 				return returnResultMap(false, null, "check", "借款申请ID不能为空！");
 			}
 			
 			// 根据借款申请【主】ID，加载一条借款数据
 			MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
 			if(null == mainLoanApplication){
 				return returnResultMap(false, null, "check", "获取借款申请信息失败！");
 			}
 			
 			// 根据借款申请【主】ID，加载一条车贷快照数据
 			EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
 			if(null == carLoanSnapshot){
 				return returnResultMap(false, null, "check", "获取车贷信息失败！");
 			}
 			
 			BigDecimal totalMortgageValue = BigDecimal.ZERO;//抵押总价值
 			
            // 抵押车【开始】。
 			List<MortgageCarSnapshot> mortgageCarSnapshotList = new ArrayList<MortgageCarSnapshot>();
            if (null != carInfoArray && !"".equals(carInfoArray)) {
                JSONArray array = JSONArray.parseArray(carInfoArray);
                if (null != array) {
                    for (int i = 0; i < array.size(); i++) {
                        MortgageCarSnapshot mortgageCarSnapshot = new MortgageCarSnapshot();
                        JSONObject object = array.getJSONObject(i);
                        if(null != object){
                        	//抵押类型
                            if (null != object.get("arrived") && !"".equals(object.get("arrived"))) {
                            	mortgageCarSnapshot.setArrived(object.get("arrived").toString());
                            }
                            //汽车品牌
                            if (null != object.get("automobileBrand") && !"".equals(object.get("automobileBrand"))) {
                            	mortgageCarSnapshot.setAutomobileBrand(object.get("automobileBrand").toString());
                            }
                            //汽车型号
                            if (null != object.get("carModel") && !"".equals(object.get("carModel"))) {
                            	mortgageCarSnapshot.setCarModel(object.get("carModel").toString());
                            }
                            //市场价格
                            if (null != object.get("marketPrice") && !"".equals(object.get("marketPrice"))) {
                            	mortgageCarSnapshot.setMarketPrice(new BigDecimal(object.get("marketPrice").toString()));
                            	totalMortgageValue = totalMortgageValue.add(new BigDecimal(object.get("marketPrice").toString()));
                            }
                            //车架号
                            if (null != object.get("frameNumber") && !"".equals(object.get("frameNumber"))) {
                            	mortgageCarSnapshot.setFrameNumber(object.get("frameNumber").toString());
                            }
                            mortgageCarSnapshot.setCreateTime(new Date());
                            mortgageCarSnapshot.setLastUpdateTime(new Date());
                            mortgageCarSnapshot.setState(MortgageCarSnapshot.STATE_ENABLE);
                            mortgageCarSnapshotList.add(mortgageCarSnapshot);
                        }
                    }
                }
            }
            // 抵押车【结束】。
            
            carLoanSnapshot.setTotalMortgageValue(totalMortgageValue);//抵押总价值
            if(null != creditLimit && !"".equals(creditLimit)){
            	carLoanSnapshot.setCreditLimit(new BigDecimal(creditLimit));//授信上限
            }
            carLoanSnapshot.setMortgageDescription(mortgageDescription);//描述
            carLoanSnapshot.setLastUpdateTime(new Date());//最后更改时间
 			
 			mortgageCarSnapshotService.saveMortgageCarSnapshot(carLoanSnapshot, mortgageCarSnapshotList);
 			
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：保存信贷【main】
     */
    @RequestMapping("/save_enterprise_loan_credit")
    @ResponseBody
    public Object save_enterprise_loan_credit(
    		@RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
    		@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,//借款申请名称
    		@RequestParam(value = "province", required = false) String province,//省
    		@RequestParam(value = "city", required = false) String city,//市
    		@RequestParam(value = "loanUseage", required = false) String loanUseage,//借款用途
    		@RequestParam(value = "useDescription", required = false) String useDescription,//用途描述
    		@RequestParam(value = "loanProductId", required = false) String loanProductId,//选择产品
    		@RequestParam(value = "loanBalance", required = false) String loanBalance,//借款金额
    		@RequestParam(value = "offlineApplyCode", required = false) String offlineApplyCode,//线下合同/订单号
    		@RequestParam(value = "projectDescription", required = false) String projectDescription,//项目描述
    		@RequestParam(value = "internalRating", required = false) String internalRating,//内部评级
    		@RequestParam(value = "riskControlInformation", required = false) String riskControlInformation,//风险控制信息
            @RequestParam(value = "inCardId", required = false) String inCardId) {//打款卡
        try {
        	// 合法性验证。
 			if(null == loanApplicationId || "".equals(loanApplicationId)){
 				return returnResultMap(false, null, "check", "借款申请ID不能为空！");
 			}
 			if(null == loanUseage || "".equals(loanUseage)){
 				return returnResultMap(false, null, "check", "借款用途不能为空！");
 			}
 			if(null == loanProductId || "".equals(loanProductId)){
 				return returnResultMap(false, null, "check", "产品ID不能为空！");
 			}
 			if(null == loanBalance || "".equals(loanBalance)){
 				return returnResultMap(false, null, "check", "借款金额不能为空！");
 			}
 			if(null == inCardId || "".equals(inCardId)){
 				return returnResultMap(false, null, "check", "打款卡不能为空！");
 			}
 			
 			// 根据借款申请【主】ID，加载一条借款数据
 			MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
 			if(null == mainLoanApplication){
 				return returnResultMap(false, null, "check", "获取借款申请信息失败！");
 			}
 			
 			mainLoanApplication.setLoanUseage(loanUseage);//借款用途
 			mainLoanApplication.setLoanProductId(Long.valueOf(loanProductId));//产品ID
 			LoanProduct loanProduct = loanProductService.findById(Long.valueOf(loanProductId));
 			mainLoanApplication.setAnnualRate(loanProduct.getAnnualRate());//年利率
 			if(null != loanBalance && !"".equals(loanBalance)){
 				mainLoanApplication.setLoanBalance(new BigDecimal(loanBalance));//借款金额
 				mainLoanApplication.setConfirmBalance(new BigDecimal(loanBalance));//批复金额
 				mainLoanApplication.setMainLoanBalance(new BigDecimal(loanBalance));//修改总借款金额（应对提交初审之后，还更新借款金额的情况）
 			}
 			mainLoanApplication.setOfflineApplyCode(offlineApplyCode);//线下合同号
 			if(null != inCardId && !"".equals(inCardId)){
 				mainLoanApplication.setInCardId(Long.valueOf(inCardId));//打款卡
 			}
 			mainLoanApplication.setLoanApplicationName(loanApplicationName);//借款申请名称
 			
 			// 根据借款申请【主】ID，加载一条信贷快照数据
 			EnterpriseCreditSnapshot creditSnapshot = enterpriseCreditSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
 			if(null == creditSnapshot){
 				return returnResultMap(false, null, "check", "获取信贷信息失败！");
 			}
 			
 			if(null != province && !"".equals(province)){
 				creditSnapshot.setProvince(Long.valueOf(province));
 			}
 			if(null != city && !"".equals(city)){
 				creditSnapshot.setCity(Long.valueOf(city));//城市
 			}
 			creditSnapshot.setUseDescription(useDescription);//用途描述
 			projectDescription = projectDescription.replaceAll("\r\n", "<br>");
 			creditSnapshot.setProjectDescription(projectDescription);//项目描述
 			creditSnapshot.setInternalRating(internalRating);//内部评级
 			creditSnapshot.setRiskControlInformation(riskControlInformation);//风险控制信息
 			creditSnapshot.setLastUpdateTime(new Date());//最后更改时间
 			
 			mainLoanApplication = enterpriseCreditSnapshotService.saveEnterpriseLoanCredit(mainLoanApplication, creditSnapshot);
 			
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：保存保理【main】
     */
    @RequestMapping("/save_enterprise_loan_factoring")
    @ResponseBody
    public Object save_enterprise_loan_factoring(
    		@RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
    		@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,//借款申请名称
    		@RequestParam(value = "loanUseage", required = false) String loanUseage,//借款用途
    		@RequestParam(value = "loanProductId", required = false) String loanProductId,//产品ID
    		@RequestParam(value = "financingAmount", required = false) String financingAmount,//融资金额
    		@RequestParam(value = "loanBalance", required = false) String loanBalance,//授信金额
    		@RequestParam(value = "financingParty", required = false) String financingParty,//融资方
    		
    		@RequestParam(value = "paymentParty", required = false) String paymentParty,//付款方
    		@RequestParam(value = "sourceOfRepayment", required = false) String sourceOfRepayment,//还款来源
    		@RequestParam(value = "offlineApplyCode", required = false) String offlineApplyCode,//线下合同/订单号
    		@RequestParam(value = "accountReceivableDescription", required = false) String accountReceivableDescription,//应收账款说明
    		@RequestParam(value = "projectComprehensiveEvaluati", required = false) String projectComprehensiveEvaluati,//项目综合评价
    		
    		@RequestParam(value = "moneyRiskAssessment", required = false) String moneyRiskAssessment,//款项风险评价
    		@RequestParam(value = "fieldAdjustmentMark", required = false) String fieldAdjustmentMark,//360度实地尽调(标记)
    		@RequestParam(value = "fieldAdjustmentValue", required = false) String fieldAdjustmentValue,//360度实地尽调(值)
    		@RequestParam(value = "repaymentGuaranteeMark", required = false) String repaymentGuaranteeMark,//还款保障金(标记)
    		@RequestParam(value = "repaymentGuaranteeValue", required = false) String repaymentGuaranteeValue,//还款保障金(值)
    		
    		@RequestParam(value = "aidFundMark", required = false) String aidFundMark,//法律援助基金(标记)
    		@RequestParam(value = "aidFundValue", required = false) String aidFundValue,//法律援助基金(值)
    		@RequestParam(value = "inCardId", required = false) String inCardId) {//打款卡
        try {
        	// 合法性验证。
 			if(null == loanApplicationId || "".equals(loanApplicationId)){
 				return returnResultMap(false, null, "check", "借款申请ID不能为空！");
 			}
 			if(null == loanUseage || "".equals(loanUseage)){
 				return returnResultMap(false, null, "check", "借款用途不能为空！");
 			}
 			if(null == loanProductId || "".equals(loanProductId)){
 				return returnResultMap(false, null, "check", "产品ID不能为空！");
 			}
 			if(null == financingAmount || "".equals(financingAmount)){
 				return returnResultMap(false, null, "check", "融资金额不能为空！");
 			}
 			if(null == loanBalance || "".equals(loanBalance)){
 				return returnResultMap(false, null, "check", "授信金额不能为空！");
 			}
 			if(null == financingParty || "".equals(financingParty)){
 				return returnResultMap(false, null, "check", "融资方不能为空！");
 			}
 			if(null == paymentParty || "".equals(paymentParty)){
 				return returnResultMap(false, null, "check", "付款方不能为空！");
 			}
 			if(null == accountReceivableDescription || "".equals(accountReceivableDescription)){
 				return returnResultMap(false, null, "check", "应收账款说明不能为空！");
 			}
 			if(null == inCardId || "".equals(inCardId)){
 				return returnResultMap(false, null, "check", "打款卡不能为空！");
 			}
 			
 			// 根据借款申请【主】ID，加载一条借款数据
 			MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
 			if(null == mainLoanApplication){
 				return returnResultMap(false, null, "check", "获取借款申请信息失败！");
 			}
 			
 			// 验证单笔最大限额
 			EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
 			enterpriseLoanApplication.getEnterpriseId();
 			EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
 			if(enterpriseInfo.getSingleMaximumAmount() != null){
 				if(new BigDecimal(loanBalance).compareTo(enterpriseInfo.getSingleMaximumAmount()) == 1){//可以通过BigDecimal的compareTo方法来进行比较.返回的结果是int类型,-1表示小于,0是等于,1是大于.
 					return returnResultMap(false, null, "check", "授信金额大于单笔最大限额，该企业单笔最大限额为：" + enterpriseInfo.getSingleMaximumAmount());
 				}
 			}
 			
 			mainLoanApplication.setLoanUseage(loanUseage);//借款用途
 			mainLoanApplication.setLoanProductId(Long.valueOf(loanProductId));//产品ID
 			LoanProduct loanProduct = loanProductService.findById(Long.valueOf(loanProductId));
 			mainLoanApplication.setAnnualRate(loanProduct.getAnnualRate());//年利率
 			mainLoanApplication.setLoanBalance(new BigDecimal(loanBalance));//借款金额
 			mainLoanApplication.setConfirmBalance(new BigDecimal(loanBalance));//批复金额
 			mainLoanApplication.setMainLoanBalance(new BigDecimal(loanBalance));//修改总借款金额（应对提交初审之后，还更新借款金额的情况）
 			mainLoanApplication.setOfflineApplyCode(offlineApplyCode);//线下合同号
 			if(null != inCardId && !"".equals(inCardId)){
 				mainLoanApplication.setInCardId(Long.valueOf(inCardId));//打款卡
 			}
 			mainLoanApplication.setLoanApplicationName(loanApplicationName);//借款申请名称
 			
 			// 根据借款申请【主】ID，加载一条保理快照数据
 			EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
 			if(null == factoringSnapshot){
 				return returnResultMap(false, null, "check", "获取保理信息失败！");
 			}
 			
 			if(null != financingAmount && !"".equals(financingAmount)){
 				factoringSnapshot.setFinancingAmount(new BigDecimal(financingAmount));//融资金额
 			}
 			if(null != financingParty && !"".equals(financingParty)){
 				factoringSnapshot.setFinancingParty(Long.valueOf(financingParty));//融资方
 			}
 			if(null != paymentParty && !"".equals(paymentParty)){
 				factoringSnapshot.setPaymentParty(Long.valueOf(paymentParty));//付款方
 			}
 			factoringSnapshot.setSourceOfRepayment(sourceOfRepayment);//还款来源
 			factoringSnapshot.setAccountReceivableDescription(accountReceivableDescription);//应收账款说明
 			factoringSnapshot.setProjectComprehensiveEvaluati(projectComprehensiveEvaluati);//项目综合评价 
 			factoringSnapshot.setMoneyRiskAssessment(moneyRiskAssessment);//款项风险评价
 			factoringSnapshot.setFieldAdjustmentMark(fieldAdjustmentMark);//360度实地尽调(标记)
 			factoringSnapshot.setFieldAdjustmentValue(fieldAdjustmentValue);//360度实地尽调(值)
 			factoringSnapshot.setRepaymentGuaranteeMark(repaymentGuaranteeMark);//还款保障金(标记)
 			factoringSnapshot.setRepaymentGuaranteeValue(repaymentGuaranteeValue);//还款保障金(值)
 			factoringSnapshot.setAidFundMark(aidFundMark);//法律援助基金(标记)
 			factoringSnapshot.setAidFundValue(aidFundValue);//法律援助基金(值)
 			factoringSnapshot.setLastUpdateTime(new Date());//最后更改时间
 			
 			mainLoanApplication = enterpriseFactoringSnapshotService.saveEnterpriseLoanFactoring(mainLoanApplication, factoringSnapshot);
 			
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：保存基金【main】
     */
    @RequestMapping("/save_enterprise_loan_foundation")
    @ResponseBody
    public Object save_enterprise_loan_foundation(
    		@RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
    		@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,//借款申请名称
    		@RequestParam(value = "loanProductId", required = false) String loanProductId,//产品ID
			@RequestParam(value = "loanBalance", required = false) String loanBalance,//借款金额
			@RequestParam(value = "applicationDesc", required = false) String applicationDesc,//描述
    		@RequestParam(value = "offlineApplyCode", required = false) String offlineApplyCode,//线下合同/订单号
    		@RequestParam(value = "coId", required = false) String coId,//托管机构
    		@RequestParam(value = "investmentType", required = false) String investmentType,//定向委托投资类型
			MultipartFile importFile,//标的详情说明
			MultipartFile importFileTrade,//交易说明书
			MultipartFile importFileRiskTip,//风险提示函
			String flag,
			String tradeBookId,
			String riskTipId,
    		@RequestParam(value = "inCardId", required = false) String inCardId) {//打款卡

		Object result = null;

        try {
        	// 合法性验证。
 			if(null == loanApplicationId || "".equals(loanApplicationId)){
				result = returnResultMap(false, null, "check", "借款申请ID不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}

 			if(null == loanProductId || "".equals(loanProductId)){
				result =  returnResultMap(false, null, "check", "产品ID不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}
 			if(null == loanBalance || "".equals(loanBalance)){
				result =  returnResultMap(false, null, "check", "借款金额不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}

 			if(null == applicationDesc || "".equals(applicationDesc)){
				result =  returnResultMap(false, null, "check", "项目描述不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}
 			if(null == coId || "".equals(coId)){
				result =  returnResultMap(false, null, "check", "托管机构不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}
 			if(null == investmentType || "".equals(investmentType)){
				result =  returnResultMap(false, null, "check", "定向委托投资类型！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}
 			if(null == importFile){
				result =  returnResultMap(false, null, "check", "标的详情说明不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}
			if(null == importFileTrade){
				result =  returnResultMap(false, null, "check", "交易说明书不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}
			if(null == inCardId || "".equals(inCardId)){
				result =  returnResultMap(false, null, "check", "打款卡不能为空！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}

 			// 根据借款申请【主】ID，加载一条借款数据
 			MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
 			if(null == mainLoanApplication){
				result =  returnResultMap(false, null, "check", "获取借款申请信息失败！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}

 			mainLoanApplication.setLoanProductId(Long.valueOf(loanProductId));//产品ID
 			LoanProduct loanProduct = loanProductService.findById(Long.valueOf(loanProductId));
 			mainLoanApplication.setAnnualRate(loanProduct.getAnnualRate());//年利率
 			mainLoanApplication.setLoanBalance(new BigDecimal(loanBalance));//借款金额
 			mainLoanApplication.setConfirmBalance(new BigDecimal(loanBalance));
 			mainLoanApplication.setMainLoanBalance(new BigDecimal(loanBalance));//修改总借款金额（应对提交初审之后，还更新借款金额的情况）
 			mainLoanApplication.setOfflineApplyCode(offlineApplyCode);//线下合同号
 			applicationDesc = applicationDesc.replaceAll("\r\n", "<br>");
 			mainLoanApplication.setApplicationDesc(applicationDesc);
 			if(null != inCardId && !"".equals(inCardId)){
 				mainLoanApplication.setInCardId(Long.valueOf(inCardId));//打款卡
 			}
 			mainLoanApplication.setLoanApplicationName(loanApplicationName);//借款申请名称

 			// 根据借款申请（主）ID，加载一条保理快照数据
			EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
 			if(null == foundationSnapshot){
				result =  returnResultMap(false, null, "check", "获取基金信息失败！");
				return getResultJavaScript(((JSONObject) result).toJSONString());
 			}

			foundationSnapshot.setLastUpdateTime(new Date());//最后更改时间
			foundationSnapshot.setInvestmentType(investmentType);
			foundationSnapshot.setCoId(Long.valueOf(coId));
			//保存标的详情文件
			if(org.apache.commons.lang.StringUtils.isNotEmpty(flag)){
				foundationSnapshot.setAttachId(Long.valueOf(flag));
			}

			//保存交易说明书
			if(org.apache.commons.lang.StringUtils.isNotEmpty(tradeBookId)){
				foundationSnapshot.setTradeBookId(Long.valueOf(tradeBookId));
			}

			//保存风险提示函
			if(org.apache.commons.lang.StringUtils.isNotEmpty(tradeBookId)){
				foundationSnapshot.setRiskTipId(Long.valueOf(riskTipId));
			}

			mainLoanApplication = enterpriseFoundationSnapshotService.saveEnterpriseLoanFoundation(mainLoanApplication, foundationSnapshot,importFile,importFileTrade,importFileRiskTip);

        } catch (Exception e) {
            e.printStackTrace();
			result =  returnResultMap(false, null, null, e.getMessage());
			return getResultJavaScript(((JSONObject) result).toJSONString());
        }
		result =  returnResultMap(true, null, null, null);
		return getResultJavaScript(((JSONObject) result).toJSONString());
    }
    
    /**
     * 执行：保存信贷【main】
     */
    @RequestMapping("/save_enterprise_loan_pledge")
    @ResponseBody
    public Object save_enterprise_loan_pledge(
    		@RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,//借款申请ID
    		@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,//借款申请名称
    		@RequestParam(value = "province", required = false) String province,//省
    		@RequestParam(value = "city", required = false) String city,//市
    		@RequestParam(value = "loanUseage", required = false) String loanUseage,//借款用途
    		@RequestParam(value = "useDescription", required = false) String useDescription,//用途描述
    		@RequestParam(value = "loanProductId", required = false) String loanProductId,//选择产品
    		@RequestParam(value = "loanBalance", required = false) String loanBalance,//借款金额
    		@RequestParam(value = "offlineApplyCode", required = false) String offlineApplyCode,//线下合同/订单号
    		@RequestParam(value = "projectDescription", required = false) String projectDescription,//项目描述
    		@RequestParam(value = "internalRating", required = false) String internalRating,//内部评级
    		@RequestParam(value = "riskControlInformation", required = false) String riskControlInformation,//风险控制信息
            @RequestParam(value = "inCardId", required = false) String inCardId) {//打款卡
        try {
        	// 合法性验证。
 			if(null == loanApplicationId || "".equals(loanApplicationId)){
 				return returnResultMap(false, null, "check", "借款申请ID不能为空！");
 			}
 			if(null == loanUseage || "".equals(loanUseage)){
 				return returnResultMap(false, null, "check", "借款用途不能为空！");
 			}
 			if(null == loanProductId || "".equals(loanProductId)){
 				return returnResultMap(false, null, "check", "产品ID不能为空！");
 			}
 			if(null == loanBalance || "".equals(loanBalance)){
 				return returnResultMap(false, null, "check", "借款金额不能为空！");
 			}
 			if(null == inCardId || "".equals(inCardId)){
 				return returnResultMap(false, null, "check", "打款卡不能为空！");
 			}
 			
 			// 根据借款申请【主】ID，加载一条借款数据
 			MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
 			if(null == mainLoanApplication){
 				return returnResultMap(false, null, "check", "获取借款申请信息失败！");
 			}
 			
 			mainLoanApplication.setLoanUseage(loanUseage);//借款用途
 			mainLoanApplication.setLoanProductId(Long.valueOf(loanProductId));//产品ID
 			LoanProduct loanProduct = loanProductService.findById(Long.valueOf(loanProductId));
 			mainLoanApplication.setAnnualRate(loanProduct.getAnnualRate());//年利率
 			if(null != loanBalance && !"".equals(loanBalance)){
 				mainLoanApplication.setLoanBalance(new BigDecimal(loanBalance));//借款金额
 				mainLoanApplication.setConfirmBalance(new BigDecimal(loanBalance));//批复金额
 				mainLoanApplication.setMainLoanBalance(new BigDecimal(loanBalance));//修改总借款金额（应对提交初审之后，还更新借款金额的情况）
 			}
 			mainLoanApplication.setOfflineApplyCode(offlineApplyCode);//线下合同号
 			if(null != inCardId && !"".equals(inCardId)){
 				mainLoanApplication.setInCardId(Long.valueOf(inCardId));//打款卡
 			}
 			mainLoanApplication.setLoanApplicationName(loanApplicationName);//借款申请名称
 			
 			// 根据借款申请【主】ID，加载一条信贷快照数据
 			EnterprisePledgeSnapshot pledgeSnapshot = enterprisePledgeSnapshotService.getByMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
 			if(null == pledgeSnapshot){
 				return returnResultMap(false, null, "check", "获取信贷信息失败！");
 			}
 			
 			if(null != province && !"".equals(province)){
 				pledgeSnapshot.setProvince(Long.valueOf(province));
 			}
 			if(null != city && !"".equals(city)){
 				pledgeSnapshot.setCity(Long.valueOf(city));//城市
 			}
 			pledgeSnapshot.setUseDescription(useDescription);//用途描述
 			projectDescription = projectDescription.replaceAll("\r\n", "<br>");
 			pledgeSnapshot.setProjectDescription(projectDescription);//项目描述
 			pledgeSnapshot.setInternalRating(internalRating);//内部评级
 			pledgeSnapshot.setRiskControlInformation(riskControlInformation);//风险控制信息
 			pledgeSnapshot.setLastUpdateTime(new Date());//最后更改时间
 			
 			mainLoanApplication = enterprisePledgeSnapshotService.saveEnterpriseLoanPledge(mainLoanApplication, pledgeSnapshot);
 			
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    

	private Object getResultJavaScript(String result) {
		Gson gson = new Gson();
		ResultMap resultMap = gson.fromJson(result, ResultMap.class);

		if(resultMap.getResult().equals("success")){
			return "<script type='text/javascript'>parent.$.messager.alert(\"操作提示\", \"保存成功！\", \"info\");</script>";
		}else if(resultMap.getResult().equals("error")){
			if(resultMap.getErrCode().equals("check")){
				return "<script type='text/javascript'>parent.$.messager.alert(\"验证提示\", "+resultMap.getErrMsg()+", \"info\");</script>";
			}else{
				return "<script type='text/javascript'>parent.$.messager.alert(\"系统提示\", "+resultMap.getErrMsg()+", \"warning\");</script>";
			}
		}else{
			return "<script type='text/javascript'>parent.$.messager.alert(\"系统提示\", \"网络异常，请稍后操作！\", \"error\");</script>";
		}
	}

	/**
     * 跳转到图片添加页
     */
    @RequestMapping(value = "/loanUploadSnapshotAdd")
    public ModelAndView loanUploadSnapshotAdd(@RequestParam(value = "state", required = false) String state,
                                          @RequestParam(value = "typeList", required = false) String typeList,
                                          @RequestParam(value = "userId", required = false) String userId,
                                          @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,
                                          @RequestParam(value = "isCode", required = false) String isCode,
                                          @RequestParam(value = "loanType", required = false) String loanType) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("state", state);
        mv.addObject("typeList", typeList);
        mv.addObject("loanApplicationId", loanApplicationId);
        mv.addObject("isCode",isCode);
        mv.addObject("loanType",loanType);
        mv.setViewName("jsp/enterprise/picture/loanUploadSnapshotAdd");
        return mv;
    }
    
    /**
     * 保存图片【main】
     */
    @RequestMapping(value = "/saveLoanUploadSnapshot")
    @ResponseBody
    public Object saveLoanUploadSnapshot(HttpServletRequest request, HttpSession session) throws IOException {
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
        if (file != null){
            return JSONObject.toJSONString(enterpriseLoanApplicationService.saveLoanUploadSnapshot(loanApplicationId, file, state, msgName, typeList, rootPath, isCode));
        } 
       return JSONObject.toJSONString(resultMap);
    }
    
    /**
     * 跳转到大图显示页
     */
    @RequestMapping(value = "/toLoanShowBigPicture")
    public ModelAndView toLoanShowBigPicture(@RequestParam(value = "cusId", required = false) Long cusId) {
        ModelAndView mv = new ModelAndView();
        Attachment atta = enterpriseLoanApplicationService.getAttachmentBycusId(cusId);
        mv.addObject("cusId", cusId);
        mv.addObject("url", atta.getUrl());
        mv.addObject("imgName", atta.getFileName());
        mv.setViewName("jsp/enterprise/picture/loanShowBigPicture");
        return mv;
    }
    
    /**
     * 删除图片
     */
    @RequestMapping(value = "/delLoanImg")
    @ResponseBody
    public Object delLoanImg(HttpServletRequest request, HttpSession session, 
    		@RequestParam(value = "cusId", required = false) Long cusId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        	String rootPath = request.getSession().getServletContext().getRealPath("");
            Attachment atta = enterpriseLoanApplicationService.getAttachmentBycusId(cusId);
            String url = atta.getUrl();
            enterpriseLoanApplicationService.delLoanImg(cusId, CustomerUploadSnapshot.CUSTOMERUPLOADSNAPSHOT_DELETED, atta, rootPath);
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
     * 图片分页显示【main】
     */
    @RequestMapping(value = "/imgLoanPaging")
    @ResponseBody
    public Object imgLoanPaging(@RequestParam(value = "pageState", required = false) String pageState,
                            @RequestParam(value = "cusId", required = false) Long cusId) {
        CustomerUploadSnapshot cus = enterpriseLoanApplicationService.getcustomerUploadSnapshotDetails(cusId);
        List<CustomerUploadSnapshot> cusList = enterpriseLoanApplicationService.getCustomerUploadSnapshotListByMainId(cus.getMainLoanApplicationId(), cus.getType());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cusId", cusId);
        for (int i = 0; i < cusList.size(); i++) {
            if (cusId.equals(cusList.get(i).getSnapshotId())) {
                if ("0".equals(pageState)) {
                    if ((i - 1) >= 0) {
                        Attachment atta = enterpriseLoanApplicationService.getAttachmentBycusId(cusList.get(i - 1).getSnapshotId());
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
                        Attachment atta = enterpriseLoanApplicationService.getAttachmentBycusId(cusList.get(i + 1).getSnapshotId());
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
     * 执行：提交初审。【main】
     * @param loanApplicationId 借款申请ID
     * @param loanType 借款类型
     */
    @RequestMapping("/save_loan_submit")
    @ResponseBody
    public Object saveLoanSubmit(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "loanApplicationId", required = false) String loanApplicationId,
                                 @RequestParam(value = "loanType", required = false) String loanType) {
        try {
            // 合法性验证。
            if (null == loanApplicationId || "".equals(loanApplicationId)) {
                return returnResultMap(false, null, "check", "借款申请ID不能为空！");
            }

            // 根据【主】ID加载一条借款申请信息。
            MainLoanApplication loan = mainLoanApplicationService.findById(Long.valueOf(loanApplicationId));
            if (null == loan) {
                return returnResultMap(false, null, "check", "借款信息不能为空！");
            }
            
            // 三种类型公共验证字段
			if(!LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)){
				if(null == loan.getLoanUseage() || "".equals(loan.getLoanUseage())){
					return returnResultMap(false, null, "check", "借款用途不能为空！");
				}
			}
 			if(null == loan.getLoanProductId() || "".equals(loan.getLoanProductId())){
 				return returnResultMap(false, null, "check", "产品ID不能为空！");
 			}
 			if(null == loan.getLoanBalance() || "".equals(loan.getLoanBalance())){
 				return returnResultMap(false, null, "check", "借款金额不能为空！");
 			}
 			if(null == loan.getInCardId() || "".equals(loan.getInCardId())){
 				return returnResultMap(false, null, "check", "打款卡不能为空！");
 			}

            // 这里根据借款类型，验证相应的必填写项目
            if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanType)){
            	// 根据借款申请ID，加载一条车贷快照数据
     			EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(loan.getMainLoanApplicationId());
     			if(null == carLoanSnapshot){
     				return returnResultMap(false, null, "check", "获取车贷信息失败！");
     			}
    		}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType)){
    			// 根据借款申请ID，加载一条信贷快照数据
     			EnterpriseCreditSnapshot creditSnapshot = enterpriseCreditSnapshotService.getByMainLoanApplicationId(loan.getMainLoanApplicationId());
     			if(null == creditSnapshot){
     				return returnResultMap(false, null, "check", "获取信贷信息失败！");
     			}
    		}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanType)){
    			// 根据借款申请ID，加载一条保理快照数据
     			EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByMainLoanApplicationId(loan.getMainLoanApplicationId());
     			if(null == factoringSnapshot){
     				return returnResultMap(false, null, "check", "获取保理信息失败！");
     			}
     			if(null == factoringSnapshot.getFinancingAmount() || "".equals(factoringSnapshot.getFinancingAmount())){
     				return returnResultMap(false, null, "check", "融资金额不能为空！");
     			}
     			if(null == loan.getLoanBalance() || "".equals(loan.getLoanBalance())){
     				return returnResultMap(false, null, "check", "授信金额不能为空！");
     			}
     			if(null == factoringSnapshot.getFinancingParty() || "".equals(factoringSnapshot.getFinancingParty())){
     				return returnResultMap(false, null, "check", "融资方不能为空！");
     			}
     			if(null == factoringSnapshot.getPaymentParty() || "".equals(factoringSnapshot.getPaymentParty())){
     				return returnResultMap(false, null, "check", "付款方不能为空！");
     			}
     			if(null == factoringSnapshot.getAccountReceivableDescription() || "".equals(factoringSnapshot.getAccountReceivableDescription())){
     				return returnResultMap(false, null, "check", "应收账款说明不能为空！");
     			}
    		}else  if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)){
				//do nothing
			}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)){
    			// 根据借款申请ID，加载一条企业标快照数据
     			EnterprisePledgeSnapshot pledgeSnapshot = enterprisePledgeSnapshotService.getByMainLoanApplicationId(loan.getMainLoanApplicationId());
     			if(null == pledgeSnapshot){
     				return returnResultMap(false, null, "check", "获取信贷信息失败！");
     			}
    		}else {
    			return returnResultMap(false, null, "check", "借款类型参数异常！");
			}

            // 更改借款申请状态
            loan.setApplicationState(LoanApplication.APPLICATIONSTATE_ISSUING_AUDIT);

            String rootPath = request.getSession().getServletContext().getRealPath("");
            loanApplicationService.submitLoanAppcalication(loan,rootPath);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 跳转到：企业借款申请详情【main】
     */
	@RequestMapping(value = "/to_loan_detail")
    public ModelAndView to_loan_detail(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
    	ModelAndView mv = new ModelAndView();
    	//获取借款申请详情
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        mv.addObject("loanApplication", loanApplication);
        mv.addObject("loanApplicationState", LoanApplicationStateEnum.values());
        mv.setViewName("jsp/enterprise/loan/loan_detail");
        return mv;
    }
	
	/**
     * 跳转到：企业借款详情页之标的信息页
     */
    @RequestMapping(value = "/showConcreteDetails")
    public String showConcreteDetails(HttpServletRequest request, Long loanApplicationId) {
        try {
            //借款申请信息
            LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
            request.setAttribute("loanApplication", loanApplication);

            if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanApplication.getLoanType())) {// 车贷
                EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplication.getLoanApplicationId());
                request.setAttribute("carLoanSnapshot", carLoanSnapshot);
                // 省
                if (carLoanSnapshot.getProvince() != null) {
                    ProvinceInfo provinceInfo = provinceInfoService.findById(carLoanSnapshot.getProvince());
                    carLoanSnapshot.setProvinceName(provinceInfo.getProvinceName());
                }
                // 市
                if (carLoanSnapshot.getCity() != null) {
                    CityInfo cityInfo = cityInfoService.findById(carLoanSnapshot.getCity());
                    carLoanSnapshot.setCityName(cityInfo.getCityName());
                }
            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanApplication.getLoanType())) {// 信贷
                EnterpriseCreditSnapshot creditSnapshot = enterpriseCreditSnapshotService.getByloanApplicationId(loanApplication.getLoanApplicationId());
                request.setAttribute("creditSnapshot", creditSnapshot);
                // 省
                if (creditSnapshot.getProvince() != null) {
                    ProvinceInfo provinceInfo = provinceInfoService.findById(creditSnapshot.getProvince());
                    creditSnapshot.setProvinceName(provinceInfo.getProvinceName());
                }
                // 市
                if (creditSnapshot.getCity() != null) {
                    CityInfo cityInfo = cityInfoService.findById(creditSnapshot.getCity());
                    creditSnapshot.setCityName(cityInfo.getCityName());
                }
            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanApplication.getLoanType())) {// 保理
                EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByloanApplicationId(loanApplication.getLoanApplicationId());
                request.setAttribute("factoringSnapshot", factoringSnapshot);
            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanApplication.getLoanType())) {// 基金
                EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByloanApplicationId(loanApplication.getLoanApplicationId());
                request.setAttribute("foundationSnapshot", foundationSnapshot);
                CoLtd coLtd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
                request.setAttribute("coLtd", coLtd);
            } else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanApplication.getLoanType())) {// 信贷
                EnterprisePledgeSnapshot pledgeSnapshot = enterprisePledgeSnapshotService.getByloanApplicationId(loanApplication.getLoanApplicationId());
                request.setAttribute("creditSnapshot", pledgeSnapshot);
                // 省
                if (pledgeSnapshot.getProvince() != null) {
                    ProvinceInfo provinceInfo = provinceInfoService.findById(pledgeSnapshot.getProvince());
                    pledgeSnapshot.setProvinceName(provinceInfo.getProvinceName());
                }
                // 市
                if (pledgeSnapshot.getCity() != null) {
                    CityInfo cityInfo = cityInfoService.findById(pledgeSnapshot.getCity());
                    pledgeSnapshot.setCityName(cityInfo.getCityName());
                }
            }

            //借款产品
            if (loanApplication.getLoanProductId() != null) {
                //草稿状态没有借款产品
                LoanProduct product = loanProductService.findById(loanApplication.getLoanProductId());
                request.setAttribute("product", product);
            }

            //放款卡
            if (loanApplication.getInCardId() != null) {
                CustomerCard inCard = customerCardService.findById(loanApplication.getInCardId());
                request.setAttribute("inCard", inCard);
            }

            if(!"8".equals(loanApplication.getLoanType()) || !"9".equals(loanApplication.getLoanType())){
                // 企业信息
                EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplication.getLoanApplicationId());
                if(enterpriseLoanApplication != null){
                    EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
                    request.setAttribute("enterpriseInfo", enterpriseInfo);
                }
            }

            //审核信息
            List<VerifyVO> loanVerifyInfo = verifyService.getVerifyByApplicationId(loanApplication.getLoanApplicationId());
            request.setAttribute("loanVerifyInfo", loanVerifyInfo);
            request.setAttribute("verifyType", VerifyType.values());
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return "jsp/enterprise/loan/detail_bidInfo";
    }
    
    /**
     * 跳转到：企业借款详情页之抵押信息页【main】
     */
    @RequestMapping(value = "/showChargeInfo")
    public String showChargeInfo(HttpServletRequest request, Long loanApplicationId) {
    	//车贷快照
    	EnterpriseCarLoanSnapshot carLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(loanApplicationId);
    	if(null != carLoanSnapshot){
    		request.setAttribute("car", carLoanSnapshot);
        	
        	//抵押车信息列表
        	List<MortgageCarSnapshot> mortgageCarSnapshotList = mortgageCarSnapshotService.getListByCarLoanId(carLoanSnapshot.getEnterpriseCarLoanId());
        	request.setAttribute("mortgageCarList", mortgageCarSnapshotList);
    	}
        return "jsp/enterprise/loan/detail_chargeInfo";
    }
    
    /**
     * 跳转到：抵押车变更页
     */
	@RequestMapping(value = "/to_chargeInfo_change")
    public ModelAndView to_chargeInfo_change(@RequestParam(value = "mortgageCarId", required = false) Long mortgageCarId) {
    	ModelAndView mv = new ModelAndView();
    	MortgageCarSnapshot mortgageCarSnapshot = mortgageCarSnapshotService.getMortgageCarSnapshotById(mortgageCarId);
    	mv.addObject("mortgageCarSnapshot", mortgageCarSnapshot);
        mv.setViewName("jsp/enterprise/loan/detail_chargeInfo_change");
        return mv;
    }
	
	/**
	 * 执行：抵押车变更保存
	 */
    @RequestMapping("/save_chargeInfo_change")
    @ResponseBody
    public Object save_chargeInfo_change(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "mortgageCarId", required = false) String mortgageCarId,
                                 @RequestParam(value = "changeReason", required = false) String changeReason,
                                 @RequestParam(value = "automobileBrand", required = false) String automobileBrand,
                                 @RequestParam(value = "carModel", required = false) String carModel,
                                 @RequestParam(value = "frameNumber", required = false) String frameNumber) {
        try {
            // 合法性验证。
            if (null == mortgageCarId || "".equals(mortgageCarId)) {
                return returnResultMap(false, null, "check", "抵押信息不能为空！");
            }
            if (null == changeReason || "".equals(changeReason)) {
                return returnResultMap(false, null, "check", "变更原因不能为空！");
            }
            if (null == automobileBrand || "".equals(automobileBrand)) {
                return returnResultMap(false, null, "check", "汽车品牌不能为空！");
            }
            if (null == carModel || "".equals(carModel)) {
                return returnResultMap(false, null, "check", "汽车型号不能为空！");
            }
            if (null == frameNumber || "".equals(frameNumber)) {
                return returnResultMap(false, null, "check", "车架号不能为空！");
            }

            //1.修改变更前抵押车 状态 为不可用
            MortgageCarSnapshot beforeCar = mortgageCarSnapshotService.getMortgageCarSnapshotById(Long.valueOf(mortgageCarId));
            if (null == beforeCar) {
                return returnResultMap(false, null, "check", "抵押信息不能为空！");
            }
            beforeCar.setState(MortgageCarSnapshot.STATE_DISABLE);
            beforeCar.setLastUpdateTime(new Date());
            
            String changeReasonName = constantDefineCached.getConstantByValue("loanUseage", changeReason).getConstantName();
            beforeCar.setChangeDesc("因"+changeReasonName+"变更为"+automobileBrand+"-"+carModel+"-"+frameNumber);// 因{原因}变更为{品牌}-{型号}-{车架号}
            
            //2.创建新抵押车 状态可用 并含有 变更信息
            MortgageCarSnapshot afterCar = new MortgageCarSnapshot();
            afterCar.setCarLoanId(beforeCar.getCarLoanId());
            afterCar.setArrived(beforeCar.getArrived());
            afterCar.setAutomobileBrand(automobileBrand);
            afterCar.setCarModel(carModel);
            afterCar.setMarketPrice(beforeCar.getMarketPrice());
            afterCar.setFrameNumber(frameNumber);
            afterCar.setCreateTime(new Date());
            afterCar.setLastUpdateTime(new Date());
            afterCar.setState(MortgageCarSnapshot.STATE_ENABLE);
            
            //3.创建变更记录
            CarChangeSnapshot changeSnapshot = new CarChangeSnapshot();
            changeSnapshot.setChangeReason(Long.valueOf(changeReason));
            changeSnapshot.setChangeTime(new Date());
            
            mortgageCarSnapshotService.changeMortgageCar(beforeCar, afterCar, changeSnapshot);

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    

	//-----------------------------------------合作公司start-------------------------------------------//
	/**
	 * 跳转至合作公司列表页面
	 */
	@RequestMapping("/toCoLtdList")
	public Object toCoLtdList(@RequestParam(value = "enterpriseId", required=false) Long enterpriseId) {
		return "jsp/enterprise/detail/coltdList";
	}

	/**
	 * 合作公司列表
	 */
	@RequestMapping("/coLtdList")
	@ResponseBody
	public Object coLtdList(@RequestParam(value = "enterpriseId", required=false) Long enterpriseId,
							@RequestParam(value = "rows", defaultValue = "10") int pageSize,
							@RequestParam(value = "page", defaultValue = "1") int pageNo) {
		CoLtd coltd = new CoLtd();
		coltd.setEnterpriseId(enterpriseId);
		Pagination<CoLtd> coltdPaging = coLtdService.getCoLtdPaging(pageNo, pageSize, coltd, null);
		return coltdPaging;
	}

	/**
	 * 跳转至添加合作公司页面
	 */
	@RequestMapping("/toAddCoLtd")
	public Object toAddCoLtd(@RequestParam(value = "enterpriseId", required=false) Long enterpriseId,Long colId) {
		if (colId!=null){
			CoLtd coLtd = coLtdService.getCoLtdById(colId);
			getRequest().setAttribute("coLtd",coLtd);
		}
		return "jsp/enterprise/detail/coltdAdd";
	}

	/**
	 * 跳转至添加合作公司页面
	 */
	@RequestMapping("/saveColtd")
	@ResponseBody
	public Object saveColtd(@ModelAttribute CoLtd coLtd) {
		try {
			if (coLtd.getCoId()==null) {
				coLtd.setState(EnterpriseConstants.CoLtdStatusEnum.NORMAL.getValue());
				coLtd.setCreateTime(new Date());
				coLtdService.addCoLtd(coLtd);
			} else{
				coLtdService.updateCoLtd(coLtd);
			}
			return "success";
		}catch (SystemException se){
			logger.error(se.getDetailDesc(),se);
			se.printStackTrace();
			return se.getMessage();
		}
	}

	/**
	 * 禁用渠道
	 * @param request
	 * @param colId
	 * @return
	 */
	@RequestMapping(value = "deleteColtd")
	@ResponseBody
	public String deleteColtd(HttpServletRequest request,Long colId){
		//修改状态为禁用
		coLtdService.changeStatus(colId, EnterpriseConstants.CoLtdStatusEnum.UN_USAGE);
		return "success";
	}

	/**
	 * 解禁渠道
	 * @param request
	 * @param colId
	 * @return
	 */
	@RequestMapping(value = "free")
	@ResponseBody
	public String startUse(HttpServletRequest request,Long colId){
		//修改状态为正常
		coLtdService.changeStatus(colId, EnterpriseConstants.CoLtdStatusEnum.NORMAL);
		return "success";
	}


	/**
	 *行业
	 * @return array
	 */
	@RequestMapping(value = "/condifine", method = RequestMethod.POST)
	@ResponseBody
	public Object condifine(String code) {
		List<ConstantDefine> constantDefine = feesItemService.findConstantTypeCode(code);
		return constantDefine;
	}
	/**
	 *子行业
	 * @return array
	 */
	@RequestMapping(value = "/condifineChild", method = RequestMethod.POST)
	@ResponseBody
	public Object condifineChild(String constantValue,String code) {
		ConstantDefine constantDefine = new ConstantDefine();
		constantDefine.setConstantValue(constantValue);
		constantDefine.setConstantTypeCode(code);
		List<ConstantDefine> constantDefineList = feesItemService.findConstantValueOrCode(constantDefine);
		return constantDefineList;
	}

	/**
	 *省份
	 * @return array
	 */
	@RequestMapping(value = "/province", method = RequestMethod.POST)
	@ResponseBody
	public Object province() {
		List<ProvinceInfo> list = provinceInfoService.getAllProvinceInfo();
		return list;
	}

	/**
	 *城市
	 * @return array
	 */
	@RequestMapping(value = "/city", method = RequestMethod.POST)
	@ResponseBody
	public Object city(Long provinceId) {
		List<CityInfo> list = cityInfoService.getCityByProvinceIdAndPId(provinceId, 0L);
		return list;
	}
	//-----------------------------------------合作公司 end -------------------------------------------//
}
