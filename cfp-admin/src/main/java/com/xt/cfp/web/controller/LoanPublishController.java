package com.xt.cfp.web.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.constants.AttachmentIsCode;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.constants.PublishOpenTypeEnum;
import com.xt.cfp.core.pojo.Address;
import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CustomerHouseSnapshot;
import com.xt.cfp.core.pojo.GuaranteeCompany;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.pojo.MainLoanPublish;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.pojo.ext.CustomerUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.LoanPublishVO;
import com.xt.cfp.core.service.AddressService;
import com.xt.cfp.core.service.CityInfoService;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.CustomerHouseSnapshotService;
import com.xt.cfp.core.service.GuaranteeCompanyService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.MainLoanApplicationService;
import com.xt.cfp.core.service.ProvinceInfoService;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.ExcelUtil;
import com.xt.cfp.core.util.FileUtil;
import com.xt.cfp.core.util.PropertiesUtils;
import com.xt.cfp.core.util.StringUtils;

@Controller
@RequestMapping("/jsp/loanPublish/loan")
public class LoanPublishController extends BaseController {

	@Autowired
	private LoanPublishService loanPublishService;
	@Autowired
	private LoanApplicationService loanApplicationService;
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	@Autowired
	private GuaranteeCompanyService guaranteeCompanyService;
	@Autowired
	private ConstantDefineService constantDefineService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerHouseSnapshotService customerHouseSnapshotService;
    @Autowired
    private ProvinceInfoService provinceInfoService;
    @Autowired
    private CityInfoService cityInfoService;
    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private FinancePlanService financePlanService;
	/**
	 * 获取所有担保公司
	 *
	 * @return
	 */
	@RequestMapping(value = "/getCompanyList")
	@ResponseBody
	public Object getCompanyList() {
		GuaranteeCompany com = new GuaranteeCompany();
		List<GuaranteeCompany> companys = guaranteeCompanyService.findAll(com);
		return companys;
	}

	/**
	 * 加载垫付时机
	 *
	 * @return
	 */
	@RequestMapping(value = "/getOverduePayPointList")
	@ResponseBody
	public Object getOverduePayPointList() {
		return constantDefineService.getConstantDefinesByType("overduePayPoint");
	}

	/**
	 * to 借款标的发标信息编辑tab【by mainid】
	 *
	 * @param loanApplicationId
	 * @return
	 */
	@RequestMapping(value = "/to_loan_publish_tab")
	public ModelAndView to_loan_publish_tab(Long loanApplicationId) {
		ModelAndView mv = new ModelAndView();
		MainLoanApplication mainLloanApplication = mainLoanApplicationService.findById(loanApplicationId);
		mv.addObject("loanApplicationId", loanApplicationId);
		mv.addObject("loanType", mainLloanApplication.getLoanType());
		mv.setViewName("jsp/loanManage/loan/loan_publish_tab");
		return mv;
	}
/**
	 * to 借款标的发标信息编辑tab
	 *
	 * @return
	 */
	@RequestMapping(value = "/loadAwardPoint")
    @ResponseBody
	public Object loadAwardPoint() {
		return constantDefineCached.getByTypeCode("awardPoint");
	}

    /**
     * to 发标信息编辑【by mainid】
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/to_loan_publish_add")
    public ModelAndView to_loan_publish_add(HttpServletRequest req) {
        ModelAndView modelAndView = new ModelAndView();
        Long loanApplicationId = Long.valueOf(req.getParameter("loanApplicationId"));
        LoanPublishVO loanPublish = loanPublishService.findLoanPublishVOByMainId(loanApplicationId);//main


        MainLoanApplication loanApplication = mainLoanApplicationService.findById(loanApplicationId);//main
		List<ConstantDefine> authReports =null;
		if (loanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue())) {
			authReports=constantDefineCached.getByTypeCode("authReportForPeopelCar");
		} else {
			authReports=constantDefineCached.getByTypeCode("authReport");
		}


        if (loanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_HOUSE.getValue())
        		|| loanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue())) {
            if (loanPublish == null) {

                CustomerHouseSnapshot houseSnapshot = customerHouseSnapshotService.getHouseByMainLoanApplicationId(loanApplicationId);//main
                Address address = addressService.getAddressById(houseSnapshot.getHouseAddr());
                Map<String, Object> hourseMap = new HashMap<String, Object>();
                if (address.getProvince() != null) {
                    ProvinceInfo provinceInfo = provinceInfoService.findById(address.getProvince());
                    hourseMap.put("provinceId", provinceInfo.getProvinceId());
                    hourseMap.put("provinceName", provinceInfo.getProvinceName());
                    if (address.getCity() != null) {
                        CityInfo cityInfo = cityInfoService.findById(address.getCity());
                        hourseMap.put("cityId", cityInfo.getCityId());
                        hourseMap.put("cityName", cityInfo.getCityName());
                    }
                    if (address.getDistrict() != null) {
                        CityInfo districtInfo = cityInfoService.findById(address.getDistrict());
                        hourseMap.put("districtId", districtInfo.getCityId());
                        hourseMap.put("districtName", districtInfo.getCityName());
                    }

                }

                hourseMap.put("hourseSize", houseSnapshot.getHouseSize());
                hourseMap.put("detail", address.getDetail());
                hourseMap.put("assessValue", houseSnapshot.getAssessValue());
                hourseMap.put("marketValue", houseSnapshot.getMarketValue());
                hourseMap.put("hourseDesc", houseSnapshot.getDesc());
                modelAndView.addObject("hourse", hourseMap);
            } else {

                Address address = addressService.getAddressById(loanPublish.getHourseAddress());
                Map<String, Object> hourseMap = new HashMap<String, Object>();
                if (address != null) {
                    ProvinceInfo provinceInfo = provinceInfoService.findById(address.getProvince());
                    CityInfo cityInfo = cityInfoService.findById(address.getCity());
                    CityInfo districtInfo = cityInfoService.findById(address.getDistrict());

                    hourseMap.put("provinceId", provinceInfo.getProvinceId());
                    hourseMap.put("provinceName", provinceInfo.getProvinceName());
                    hourseMap.put("cityId", cityInfo.getCityId());
                    hourseMap.put("cityName", cityInfo.getCityName());
                    hourseMap.put("districtId", districtInfo.getCityId());
                    hourseMap.put("districtName", districtInfo.getCityName());
                    hourseMap.put("detail", address.getDetail());
                }

                hourseMap.put("hourseSize", loanPublish.getHourseSize());

                hourseMap.put("assessValue", loanPublish.getAssessValue());
                hourseMap.put("marketValue", loanPublish.getMarketValue());
                hourseMap.put("hourseDesc", loanPublish.getHourseDesc());
                modelAndView.addObject("hourse", hourseMap);
            }
        }

        if (loanPublish == null) {
            loanPublish = new LoanPublishVO();
            if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanApplication.getLoanType()) ||
            		LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanApplication.getLoanType()) ||
            		LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanApplication.getLoanType())||
            		LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanApplication.getLoanType()) ){
            	loanPublish.setLoanUseageName(constantDefineCached.getConstantByValue("enterpriseLoanUseage",loanApplication.getLoanUseage()).getConstantName());
            }else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanApplication.getLoanType())){
                //do nothing
            }else{
            	loanPublish.setLoanUseageName(constantDefineCached.getConstantByValue("loanUseage",loanApplication.getLoanUseage()).getConstantName());
            }
            loanPublish.setLoanTitle(loanApplication.getLoanApplicationName());
            loanPublish.setApplicationDesc(loanApplication.getApplicationDesc());
            loanPublish.setUsageDesc(loanApplication.getLoanUseageDesc());
        } else {
            List<String> exist = new ArrayList<String>();
            String authInfo = loanPublish.getAuthInfos();
            if (!StringUtils.isNull(authInfo)) {
                String[] authInfos = authInfo.split(",");
                for (String auth:authInfos) {
                    exist.add(auth);
                }
            }
            modelAndView.addObject("exist",exist);
        }
        modelAndView.addObject("loanPublish", loanPublish);
        modelAndView.addObject("loanApplicationId", req.getParameter("loanApplicationId"));
        modelAndView.addObject("loanType", loanApplication.getLoanType());
        modelAndView.addObject("authReports",authReports);
        modelAndView.setViewName("jsp/loanManage/loan/loan_publish_add");
        return modelAndView;
    }

    /**
     * add 发标信息【by mainid】
     *
     * @param loanPublish
     * @return
     */
    @RequestMapping(value = "/saveLoanPublish")
    @ResponseBody
    public String saveLoanPublish(MainLoanPublish mainLoanPublish, HttpServletRequest request) {
        MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(mainLoanPublish.getMainLoanApplicationId());
        Address address = null;
        if (mainLoanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_HOUSE.getValue())
        		|| mainLoanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue())) {
            if (!StringUtils.isNull(request.getParameter("provinceId"))
                    && !StringUtils.isNull(request.getParameter("cityId"))
                    && !StringUtils.isNull(request.getParameter("districtId"))
                    ) {
                long provinceId = Long.valueOf(request.getParameter("provinceId"));
                long cityId = Long.valueOf(request.getParameter("cityId"));
                long districtId = Long.valueOf(request.getParameter("districtId"));
                String houseAddr_detail = request.getParameter("houseAddr_detail");
                address = new Address();
                address.setProvince(provinceId);
                address.setCity(cityId);
                address.setDistrict(districtId);
                address.setDetail(houseAddr_detail);
            }

        }
        loanPublishService.addLoanPublish(mainLoanPublish, address);//main
        return "success";
    }
	/**
	 * to 附件信息编辑【by mainid】
	 *
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/to_loan_app_attachment")
	public String to_loan_app_attachment(HttpServletRequest req ,String isCredit) {
		req.setAttribute("loanApplicationId", req.getParameter("loanApplicationId"));
		List<CustomerUploadSnapshotVO> attachmentList = loanApplicationService.getCustomerUploadAttachmentByMainId(Long.valueOf(req
				.getParameter("loanApplicationId")),AttachmentIsCode.IS_CODE.getValue());
		for (int i = 0; i < attachmentList.size(); i++) {
			String url = attachmentList.get(i).getAttachment().getUrl();
			String fileName = url.substring(url.lastIndexOf("/") + 1).split("\\.")[0];
			attachmentList.get(i).setFileName(fileName);
		}
        req.setAttribute("isCredit",isCredit);
		req.setAttribute("attachmentList", attachmentList);
		return "jsp/loanManage/loan/loan_app_attachment";
	}

	/**
	 * to 前台借款详情页面,编辑发标描述【by mainid】
	 *
	 * @param req
	 * @param loanApplicationId
	 * @return
	 */
	@RequestMapping(value = "/to_loan_app_front_bidding")
	public String to_loan_app_front_bidding(HttpServletRequest req, Long loanApplicationId) {
		MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
		req.setAttribute("loanApplication", mainLoanApplication);
		req.setAttribute("frontURL", PropertiesUtils.getInstance().get("FRONT_PATH"));//这里的参数改
		return "jsp/loanManage/loan/loan_app_front_bidding";
	}

	//to 发标展示页面 by mainid
    @RequestMapping(value = "/to_publish_review")
    public ModelAndView to_publish_review(@RequestParam(value = "loanApplicationId", required = true) long loanApplicationId) {
        MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
        MainLoanPublish mainLoanPublish = loanPublishService.findMainLoanPublishById(loanApplicationId);
        ModelAndView modelAndView = new ModelAndView();
        String stateName = LoanApplicationStateEnum.getByValue(mainLoanApplication.getApplicationState()).getDesc();
        modelAndView.addObject("loanApplication", mainLoanApplication);
        modelAndView.addObject("loanTitle", mainLoanPublish.getLoanTitle());
        modelAndView.addObject("stateName", stateName);
        modelAndView.setViewName("jsp/loanManage/loan/loan_review");
        return modelAndView;
    }

    // to 发标确认页面 by mainid
    @RequestMapping(value = "/to_publish")
    public ModelAndView to_publish(@RequestParam(value = "loanApplicationId", required = true) long loanApplicationId) {
        ModelAndView modelAndView = new ModelAndView();
        MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
        MainLoanPublish mainLoanPublish = loanPublishService.findMainLoanPublishById(loanApplicationId);
        modelAndView.addObject("desc", mainLoanPublish.getDesc());
//        modelAndView.addObject("confirmBalance", mainLoanApplication.getConfirmBalance());

        modelAndView.addObject("mainLoanBalance", mainLoanApplication.getMainLoanBalance());//主借款总金额
        modelAndView.addObject("mainPublishBalance", mainLoanApplication.getMainPublishBalance());//主已发标金额

        modelAndView.addObject("loanApplicationId", mainLoanApplication.getMainLoanApplicationId());

        //子标的编号
        Integer num = 1;
        Integer totleCount = loanApplicationService.getLoanAppCountByMainId(mainLoanApplication.getMainLoanApplicationId());
        if(null != totleCount && totleCount > 0){
        	num = num + totleCount;
        }
        modelAndView.addObject("thisPublishTitle", mainLoanApplication.getLoanApplicationCode() + "-" + num);//本次发标标题（默认值）
        modelAndView.setViewName("jsp/loanManage/loan/loan_publish");
        return modelAndView;
    }

    /***
     * 王亚东修改增加定向标的设置
     * 定向类型 otype 密码或者用户
     * 定向密码  opassword
     * 定向用户的excel
     * 增加
     *
     * @param loanPublish
     * @param openType
     * @return
     */


    // do 保存发标信息 by mainid
    @RequestMapping(value = "/publish")
    @ResponseBody
    public Object publish(@ModelAttribute(value = "loanPublish") MainLoanPublish mainLoanPublish,
                          @RequestParam(value = "openType") String openType
                         , @RequestParam(value = "opassword") String opassword
                         ,String newUserRadio
                         , HttpServletRequest request

    		) {

        try {
        	List<Map<String, Object>> parseExcelReturnUser = parseExcelReturnUser(request);

//        	Map <String,String> map =loanPublishService.vaDataInExcelAndPassword(opassword,
//        			parseExcelReturnUser,mainLoanPublish.getMainLoanApplicationId(),"");
//        	String type = map.get("type");
//        	if(!"".equals(type)&&type!=null){  //password user null fail repart
//        		if(type.equals("repart")){
//        			return "repart";//定向用户信息不匹配
//        		}else if(type.trim().equals("fail")){
//        			return "fail";// 定向设置保存失败 fail
//        		}else if(type.trim().equals("exist")){
//        			return "exist";//已经定向设置
//        		}else if(type.trim().equals("notEquals")){
//        			return "notEquals";//用户名与数据库用户名不匹配
//        		}
//        	}

            if (openType.equals(PublishOpenTypeEnum.NOWOPEN.getValue())) {
                Date now = new Date();
                mainLoanPublish.setPreheatTime(now);
                mainLoanPublish.setOpenTime(now);
            } else {
            	mainLoanPublish.setOpenTime(DateUtil.parseStrToDate(request.getParameter("openTime"),"yyyy-MM-dd HH:mm"));
            	mainLoanPublish.setPreheatTime(DateUtil.parseStrToDate(request.getParameter("preheatTime"),"yyyy-MM-dd HH:mm"));
            }
             String msg =  loanPublishService.publish(mainLoanPublish, parseExcelReturnUser, opassword,newUserRadio);
          //发标成功后，匹配省心计划
    		try{
    			Date now = new Date();
    			if(mainLoanPublish.getOpenTime().before(now)){
    				financePlanService.creditorRightsAutoMatch();
    			}
    		}catch(Exception e){
    			System.out.println("匹配省心计划失败！");
    			e.printStackTrace();
    		}
            return msg ;
//            return "success";
        } catch (SystemException se) {
            se.printStackTrace();
           // loanPublishService.deleteOrient(mainLoanPublish.getMainLoanApplicationId());
            return "failure";
        } catch (Exception e) {
            e.printStackTrace();
          //  loanPublishService.deleteOrient(mainLoanPublish.getMainLoanApplicationId());
            return "failure";
        }


    }
	/***
	 * 修改定向设置
	 * @author wangyadong
	 * @date
	 */
	@RequestMapping(value = "/updateOrient")
    @ResponseBody
    public Object updateOrient(@ModelAttribute(value = "loanApplicationId") Long loanApplicationId,
                          @RequestParam(value = "opassword") String opassword,
                          @RequestParam(value = "allUser") String allUser ,
                          String newUserRadio
                         , HttpServletRequest request ) {

	  	List<Map<String, Object>> parseExcelReturnUser = parseExcelReturnUser(request);
		if(opassword!=null && !"".equals(opassword) || parseExcelReturnUser!=null && parseExcelReturnUser.size()>0||
				allUser!=null && !"".equals(allUser)||newUserRadio!=null&&!"".equals(newUserRadio)){
			Map <String,String> map =loanPublishService.vaDataInExcelAndPassword(opassword,
			    	parseExcelReturnUser,loanApplicationId, allUser,newUserRadio);
			    	String type = map.get("type");
			    	if(!"".equals(type)&&type!=null){  //password user null fail repart
			    		if(type.equals("repart")){
			    			return "repart";//定向用户信息不匹配
			    		}else if(type.trim().equals("fail")){
			    			return "fail";// 定向设置保存失败 fail
			    		}else if(type.trim().equals("exist")){
			    			return "exist";//已经定向设置
			    		}else if(type.trim().equals("notEquals")){
			    			return "notEquals";//用户名与数据库用户名不匹配
			    		}
			    	}
	  	}else{
	  		loanPublishService.updateOrientByAll(loanApplicationId);
	  	}

		return "success";
	}

    /**
     * 解析定向标excel
     * 返回解析的数据
     * 王亚东
     * @throws IOException
     */
    public  List<Map<String, Object>> parseExcelReturnUser(HttpServletRequest request) {
    		  MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

    		  MultipartFile multipartFile = multipartRequest.getFile("file");
    		  if(multipartFile==null||multipartFile.getSize()==0){//如果没有则返回
    			  return null;
    		  }
    		  List<Map<String, Object>> uploadExcelData = ExcelUtil.getUploadExcelData(multipartFile);
    	      System.out.println(""+uploadExcelData);

    	      return uploadExcelData;


    }

	/**
	 * 查看用户是否上传照片附件【by mainid】
	 *
	 * @param loanApplicationId
	 * @return
	 */
	@RequestMapping(value = "/getcustomerUploadAttachment")
	@ResponseBody
	public Object getcustomerUploadAttachment(Long loanApplicationId) {
		List<CustomerUploadSnapshotVO> record = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId, AttachmentIsCode.NO_CODE.getValue());//main
		return record.size();
	}

	/**
	 * 下载用户照片附件【by mainid】
	 *
	 * @param loanApplicationId
	 * @param resp
	 */
	@RequestMapping(value = "/download")
	@ResponseBody
	public void download(Long loanApplicationId, HttpServletRequest req,HttpServletResponse resp) {
		List<CustomerUploadSnapshotVO> record = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationId,AttachmentIsCode.NO_CODE.getValue());//main
		String fileName = null;
		File[] files = null;
		String rootpath = req.getSession().getServletContext().getRealPath("");
		String zipPath = rootpath + PropertiesUtils.getInstance().get("ZIP_PATH");
		if (record.size() > 0) {
			fileName = record.get(0).getAttachment().getUserId().toString();
			files = new File[record.size()];
		}
		for (int i = 0; i < record.size(); i++) {
			String prefix = record.get(i).getAttachment().getPhysicalAddress();
			String url = record.get(i).getAttachment().getUrl();
			String imgName = url.substring(url.lastIndexOf("/"));
			String file = rootpath + prefix + imgName;
			files[i] = new File(file);
		}
		byte[] bytes = new byte[1024];
		if (files != null) {
			try {
				FileUtil.zipFiles(files, zipPath, fileName);
				FileUtil.download(bytes, zipPath, fileName, resp);
				FileUtil.deleteFile(zipPath + "/" + fileName + ".zip");
			} catch (IOException e) {
				throw new SystemException(SystemErrorCode.STREAM_CAN_NOT_CLOSE).set("downloadFileError", "下载压缩图片异常");
			}
		}
	}

	//-----------------------------------------企业借款申请，申请发标 start -------------------------------------------//
	/**
	 * to 企业借款申请，附件信息编辑【by mainid】
	 *
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/to_enterprise_loan_app_attachment")
	public String to_enterprise_loan_app_attachment(HttpServletRequest req) {
		req.setAttribute("loanApplicationId", req.getParameter("loanApplicationId"));
		List<CustomerUploadSnapshotVO> attachmentList = loanApplicationService.getCustomerUploadAttachmentByMainId(Long.valueOf(req
				.getParameter("loanApplicationId")),AttachmentIsCode.IS_CODE.getValue());
		for (int i = 0; i < attachmentList.size(); i++) {
			String url = attachmentList.get(i).getAttachment().getUrl();
			String fileName = url.substring(url.lastIndexOf("/") + 1).split("\\.")[0];
			attachmentList.get(i).setFileName(fileName);
		}

		// 根据借款类型，跳到不同编辑页
		String loanType = req.getParameter("loanType");
		String returnUrl = "";
		if(LoanTypeEnum.LOANTYPE_ENTERPRISE_CAR.getValue().equals(loanType)){
			returnUrl = "jsp/enterprise/car/loan_app_attachment";
		}else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_CREDIT.getValue().equals(loanType)) {
			returnUrl = "jsp/enterprise/credit/loan_app_attachment";
		}else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_FACTORING.getValue().equals(loanType)) {
			returnUrl = "jsp/enterprise/factoring/loan_app_attachment";
		}else if(LoanTypeEnum.LOANTYPE_ENTERPRISE_FOUNDATION.getValue().equals(loanType)) {
            returnUrl = "jsp/enterprise/foundation/loan_app_attachment";
        }else if (LoanTypeEnum.LOANTYPE_ENTERPRISE_PLEDGE.getValue().equals(loanType)) {
			returnUrl = "jsp/enterprise/pledge/loan_app_attachment";
		}

		req.setAttribute("attachmentList", attachmentList);
		return returnUrl;
	}
	//-----------------------------------------企业借款申请，申请发标 end ---------------------------------------------//


	/**
	 * 标的管理
	 */
    @RequestMapping(value = "/to_publish_manage")
    public ModelAndView to_publish_manage(@RequestParam(value = "loanApplicationId", required = true) long loanApplicationId) {
        MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
        MainLoanPublish mainLoanPublish = loanPublishService.findMainLoanPublishById(loanApplicationId);
//        String stateName = LoanApplicationStateEnum.getByValue(loanApplication.getApplicationState()).getDesc();
//        modelAndView.addObject("loanApplication", mainLoanApplication);
//        modelAndView.addObject("loanTitle",loanPublish.getLoanTitle());
//        modelAndView.addObject("stateName",stateName);


    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.addObject("loanApplicationId", loanApplicationId);
    	modelAndView.addObject("mainState", mainLoanApplication.getMainState());
    	if(null == mainLoanPublish){
    		modelAndView.addObject("mainIsPublish", 0);//主发标表，没有数据
    	}else {
    		modelAndView.addObject("mainIsPublish", 1);//主发标表，已有数据
		}
        modelAndView.setViewName("jsp/loanManage/loan/publish_manage");
        return modelAndView;
    }


}
