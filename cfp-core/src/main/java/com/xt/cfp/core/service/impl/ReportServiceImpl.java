package com.xt.cfp.core.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.service.ReportService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.ResponseUtil;

/**
 * Created by yulei on 2015/8/25 0025.
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MyBatisDao myBatisDao;

    @Override
    public void exportAllianceExcel(HttpServletResponse response, String date) {
        Date date1 = DateUtil.parseStrToDate(date, "yyyy-MM-dd");
        date1 = DateUtil.addDate(date1, Calendar.DATE, 1);
        String endDate = DateUtil.getFormattedDateUtil(date1, "yyyy-MM-dd");
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        params.put("endDate", endDate);
        List<LinkedHashMap<String, Object>> list = myBatisDao.getList("LENDORDER.exportAllianceExcel", params);
        ResponseUtil.sendExcel(response, list, "加盟商数据报表-" + date);
    }
    
    @Override
    public void exportAllianceExcelBetween(HttpServletResponse response, String startdate,String enddate) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", startdate);
        params.put("endDate", enddate);
        List<LinkedHashMap<String, Object>> list = myBatisDao.getList("LENDORDER.exportBewteenAllianceExcel", params);
        String name =startdate+"至"+enddate;
        ResponseUtil.sendExcel(response, list, "加盟商数据报表-" + name);
    }

    /**
     * 导出移动端数据报表
     */
	@Override
	public void exportMobileInfoExcel(HttpServletResponse response, Map<String, Object> customParams) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("customParams", customParams);
        
        List<LinkedHashMap<String, Object>> list = null;
        String excelFileName = "数据报表";
        if("1".equals(customParams.get("sourceType"))){
        	excelFileName = "PC";
        }else if ("2".equals(customParams.get("sourceType"))) {
        	excelFileName = "Wechat";
		}else if ("3".equals(customParams.get("sourceType"))) {
			excelFileName = "Android";
		}else if ("4".equals(customParams.get("sourceType"))) {
			excelFileName = "IOS";
		}
        
        if("1".equals(customParams.get("exportType"))){
        	excelFileName += "充值";
        	list = myBatisDao.getList("RECHARGE_ORDER.exportMobileInfoExcel_rechargeOrder",params);
        }else if ("0".equals(customParams.get("exportType"))) {
        	excelFileName += "投资";
        	list = myBatisDao.getList("LENDORDER.exportMobileInfoExcel_lendOrder",params);
		}else if ("2".equals(customParams.get("exportType"))) {
			excelFileName += "提现";
			list = myBatisDao.getList("WITHDRAW.exportMobileInfoExcel_withdraw",params);
		}
        
        ResponseUtil.sendExcel(response, list, excelFileName + "数据报表");
		
	}

	/**
	 * 导出到期还款数据报表
	 */
	@Override
	public void exportRepaymentListExcel(HttpServletResponse response, List list) {
		ResponseUtil.sendExcel(response, list, "到期还款数据报表");
	}
	
	/**
	 * 导出提现申请列表数据报表
	 */
	@Override
	public void exportWithDrawListExcel(HttpServletResponse response, List list) {
		ResponseUtil.sendExcel(response, list, "提现申请列表数据报表");
	}
	/**
	 * 导出excel
	 * @author wangyadong
	 * 
	 */
	@Override
	public void toExportEccelProduct(HttpServletResponse response,Map<String, Object> customParams) {
		   List<LinkedHashMap<String, Object>> list = myBatisDao.getList("selectByPrimaryKeyByIdForExportCommitProfit", customParams);
	        ResponseUtil.sendExcel(response, list, "产品详情报表");
		
	}
}
