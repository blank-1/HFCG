package com.xt.cfp.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.pojo.WithDraw;
import com.xt.cfp.core.pojo.ext.CommitProfitVO;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.ReportService;
import com.xt.cfp.core.service.WithDrawService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.FilterUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.TimeInterval;

/**
 * Created by lenovo on 2015/8/20.
 */
@Controller
@RequestMapping(value = "/report")
public class ReportController extends BaseController {
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private WithDrawService withDrawService; 

    /**
     * 跳转报表页面
     * @return
     */
    @RequestMapping(value = "showReportList")
    public String showReportList(){
        return "jsp/report/showReportList";
    }

    /**
     * 导出出借人数据报表
     * @param request
     * @param response
     */
    @RequestMapping(value = "exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response,
                            @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                            @RequestParam(value = "endDate" ,defaultValue = "", required = false) String endDate){
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));


        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        lendOrderService.exportLenderExcel(response,customParams);
    }

    /**
     * 导出加盟商数据报表
     * @param date
     * @param response
     */
    @RequestMapping(value = "exportAllianceExcel")
    public void exportAllianceExcel(@RequestParam(value = "alliance_form_date") String date, HttpServletResponse response) {
        this.reportService.exportAllianceExcel(response, date);
    }
    
    /**
     * 导出三级分销详情列表
     * @param date
     * @param response
     * @throws UnsupportedEncodingException 
     */
	@RequestMapping(value = "/toExportEccelProduct")
	public void toExportEccelProduct(
			HttpServletResponse response,
			@RequestParam(value = "productId", defaultValue = "", required = false) String productId,
			@RequestParam(value = "mobileNo", defaultValue = "", required = false) String mobileNo,
			@RequestParam(value = "name", defaultValue = "", required = false) String name) throws UnsupportedEncodingException {
		String nameUtf =null;
			Map<String, Object> customParams = new HashMap<String, Object>();
			if(productId!=null&&!"".equals(productId)) 
				customParams.put("productId", productId);
			if(mobileNo!=null&&!"".equals(mobileNo)) 
				customParams.put("mobileNo", mobileNo);
			if(name!=null&&!"".equals(name)) 
				 nameUtf =new String(name.getBytes("ISO8859-1"),"utf-8");
				customParams.put("custName", nameUtf);
//			disActivityPaging =  disActivityRulesService.getDisActivityRulesByProductIds(customParams);
			this.reportService.toExportEccelProduct(response,customParams);
	
	}


    
    
    
 

	/**
     * 导出加盟商数据报表
     * @param date
     * @param response 
     */
    @RequestMapping(value = "exportAllianceExcelBetween")
    public void exportAllianceExcelBwteen(@RequestParam(value = "startDate") String startDate,
    		@RequestParam(value = "endDate") String endDate, HttpServletResponse response) {
        this.reportService.exportAllianceExcelBetween(response, startDate,endDate);
    }
    
    /**
     * 导出移动端数据报表
     * @param request
     * @param response
     * @param startDate
     * @param endDate
     */
    @RequestMapping(value = "exportMobileInfoExcel")
    public void exportMobileInfoExcel(HttpServletRequest request,HttpServletResponse response,
                            @RequestParam(value = "mobile_startDate", defaultValue = "", required = false) String startDate,
                            @RequestParam(value = "mobile_endDate", defaultValue = "", required = false) String endDate,
                            @RequestParam(value = "exportType", defaultValue = "", required = false) String exportType,
                            @RequestParam(value = "sourceType", defaultValue = "", required = false) String sourceType){
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));

        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        customParams.put("exportType", exportType);
        customParams.put("sourceType", sourceType);
        reportService.exportMobileInfoExcel(response, customParams);
    }
    
    /**
     * 财务管理-到期还款-导出Excel文件
     * @param request
     * @param response
     * @param loanApplicationCode
     * @param loanApplicationName
     * @param channel
     * @param loanType
     * @param realName
     * @param idCard
     * @param mobileNo
     * @param planState
     * @param beginRepaymentDay
     * @param endRepaymentDay
     */
    @RequestMapping(value = "exportRepaymentListExcel")
    public void exportRepaymentListExcel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "export_loanApplicationCode", required = false) String loanApplicationCode,
			@RequestParam(value = "export_loanApplicationName", required = false) String loanApplicationName,
			@RequestParam(value = "export_channel", required = false) String channel, 
			@RequestParam(value = "export_loanType", required = false) String loanType,
			@RequestParam(value = "export_realName", required = false) String realName, 
			@RequestParam(value = "export_idCard", required = false) String idCard,
			@RequestParam(value = "export_mobileNo", required = false) String mobileNo,
			@RequestParam(value = "export_planState", required = false) String planState,
			@RequestParam(value = "export_beginRepaymentDay", required = false) String beginRepaymentDay,
			@RequestParam(value = "export_endRepaymentDay", required = false) String endRepaymentDay){
    	//模糊查询字段，去空格
    	boolean isLegal = true;
    	if(null != loanApplicationCode && !"".equals(loanApplicationCode)){
    		loanApplicationCode = loanApplicationCode.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(loanApplicationCode)){
    			isLegal = false;
            }
    	}
    	if(null != loanApplicationName && !"".equals(loanApplicationName)){
    		loanApplicationName = loanApplicationName.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(loanApplicationName)){
    			isLegal = false;
            }
    	}
    	if(null != realName && !"".equals(realName)){
    		realName = realName.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(realName)){
    			isLegal = false;
            }
    	}
    	if(null != mobileNo && !"".equals(mobileNo)){
    		mobileNo = mobileNo.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(mobileNo)){
    			isLegal = false;
            }
    	}
    	if(!isLegal){
    		reportService.exportRepaymentListExcel(response, null);
    		return;
    	}
        List list = loanApplicationService.getRepaymentAllList(loanApplicationCode, loanApplicationName, channel, loanType, realName, 
        		idCard, mobileNo, planState, beginRepaymentDay, endRepaymentDay);
        reportService.exportRepaymentListExcel(response, list);
    }
    
    /**
     * 财务管理-提现申请列表-导出Excel文件
     * @param request
     * @param response
     * @param verifyStatus
     * @param transStatus
     * @param startDate
     * @param endDate
     * @param operateName
     * @param belongChannel
     * @param orderResource
     */
    @RequestMapping(value = "exportWithDrawListExcel")
    public void exportWithDrawListExcel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "export_verifyStatus", required = false) String verifyStatus,
			@RequestParam(value = "export_transStatus", required = false) String transStatus,
			@RequestParam(value = "export_startDate", defaultValue = "", required = false) String startDate,
            @RequestParam(value = "export_endDate" ,defaultValue = "", required = false) String endDate,
            @RequestParam(value = "export_operateName" , defaultValue = "", required = false) String operateName,
            @RequestParam(value = "export_belongChannel", required = false) String belongChannel,
            @RequestParam(value = "orderResource", required = false) String orderResource){
    	
    	WithDraw withDraw = new WithDraw();
    	withDraw.setVerifyStatus(verifyStatus);
    	withDraw.setTransStatus(transStatus);
    	
    	//开户名，去除空格
    	boolean isLegal = true;
        if(null != operateName && !"".equals(operateName)){
        	operateName = operateName.replaceAll(" ", "");
        	if(FilterUtil.isSpecialCharacters(operateName)){
        		isLegal = false;
            }
        }
    	//封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));

        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        customParams.put("operateName", operateName);
        customParams.put("belongChannel", belongChannel);
        customParams.put("resource", orderResource);
        
        if(!isLegal){
        	reportService.exportWithDrawListExcel(response, null);
        	return;
    	}
        List list = withDrawService.getWithDrawAllList(withDraw, customParams);
        reportService.exportWithDrawListExcel(response, list);
    }

}
