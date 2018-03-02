package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.TradeReport;
import com.xt.cfp.core.service.TradeReportService;
import com.xt.cfp.core.util.PageData;

@Service
public class TradeReportServiceImpl implements TradeReportService {

	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public TradeReport addTradeReport(TradeReport tradeReport) {
		myBatisDao.insert("TRADE_REPORT.insert", tradeReport);
		return tradeReport;
	}

	@Override
	public TradeReport addTradeReportSelective(TradeReport tradeReport) {
		myBatisDao.insert("TRADE_REPORT.insertSelective", tradeReport);
		return tradeReport;
	}

	@Override
	public TradeReport updateByPrimaryKey(TradeReport tradeReport) {
		myBatisDao.update("TRADE_REPORT.updateByPrimaryKey", tradeReport);
		return tradeReport;
	}

	@Override
	public TradeReport updateByPrimaryKeySelective(TradeReport tradeReport) {
		myBatisDao.update("TRADE_REPORT.updateByPrimaryKeySelective", tradeReport);
		return tradeReport;
	}

	@Override
	public void delTradeReportById(Long id) {
		myBatisDao.delete("TRADE_REPORT.deleteByPrimaryKey", id);
	}

	@Override
	public TradeReport getTradeReportById(Long id) {
		return myBatisDao.get("TRADE_REPORT.selectByPrimaryKey", id);
	}

	@Override
	public List<TradeReport> selectTradeReport(PageData pd) {
		return myBatisDao.getList("TRADE_REPORT.selectTradeReport", pd);
	}

	/**
	 * 查询交易记录
	 */
	@Override
	public List<PageData> selectTrades(PageData pd) {
		return myBatisDao.getList("TRADE_REPORT.selectTrade4Report", pd);
	}

	// ===========================个人、法人开户、项目报备数据查询===============================
	/**
	 * 查询个人开户记录
	 * 
	 * @param pd.message_id(交易流水类型，可能为空)
	 * @param pd.trade_status(交易状态，可能为空)
	 * @param pd.trade_date(存管系统开户时间，格式：yyyy-MM-dd，可能为空)
	 * @param pd.loginNames(平台用户登录账号List<String>转拼接字符串集合，可能为空)
	 * 
	 * @return pd.SERIAL_NUMBER(存管系统开户注册流水)
	 * @return pd.LOGIN_NAME(平台用户名)
	 * @return pd.PHONE_CG(存管系统开户注册手机号)
	 * @return pd.REAL_NAME(平台用户真实姓名)
	 * @return pd.CARD_TYPE(证件类型，0：居民身份证 1：护照 2：军官证 7：其他)
	 * @return pd.CARD_NO(证件号)
	 * @return pd.SEX(性别，0:男，1:女)
	 * @return pd.PHONE(平台用户手机号)
	 * @return pd.ROLE(用户属性，1:借款人2：贷款人 3：借贷合一)
	 * @return pd.CREATE_DATE(存管系统开户日期，格式：yyyyMMdd)
	 * 
	 */
	@Override
	public List<PageData> selectUsers(PageData pd) {
		List<PageData> list = myBatisDao.getList("TRADE_REPORT.selectUser4Report", pd);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		for (PageData pData : list) {
			pData.put("PHONE_CG", pData.get("PHONE"));
			pData.put("CARD_TYPE", 0);
			Object cardNo = pData.get("CARD_NO");
			int sex = 0;
			if(null != cardNo){
				sex = checkSexByCardNo(String.valueOf(cardNo.toString()));
			}
			pData.put("SEX", sex);
			pData.put("ROLE", "2");
			Object time = pData.get("CREATE_DATE");
			if (null != time) {
				date = sdf.format(time);
			}
			pData.put("CREATE_DATE", date);
		}
		return list;
	}

	/**
	 * 查询法人开户记录
	 * 
	 * @param pd.message_id(交易流水类型，可能为空)
	 * @param pd.trade_status(交易状态，可能为空)
	 * @param pd.trade_date(存管系统法人开户时间，格式：yyyy-MM-dd，可能为空)
	 * @param pd.loginNameCgs(存管系统登录账号List<String>转拼接字符串集合，可能为空)
	 * 
	 * @return pd.SERIAL_NUMBER(存管系统法人注册流水)
	 * @return pd.ENTERPRISE_NAME_CG(存管系统企业注册名称)
	 * @return pd.CREATE_DATE(存管系统法人注册日期，格式yyyyMMdd)
	 * @return pd.REAL_NAME_CG(存管系统法人注册真实姓名)
	 * @return pd.CARD_NO(法人证件号)
	 * @return pd.PHONE_CG(存管系统法人注册手机号)
	 * @return pd.BUSINESS_REGISTRATION_NUMBER(企业营业执照登记号)
	 * @return pd.REGISTRATION_CODE(企业税务登记号)
	 * @return pd.ORGANIZATION_CODE(企业组织机构代码)
	 * @return pd.ENTERPRISE_NAME(平台企业名称)
	 * @return pd.LOGIN_NAME_CG(存管系统法人登录用户名)
	 * @return pd.ROLE(用户属性，1:借款人2：贷款人 3：借贷合一)
	 * @return pd.SOCIAL_CREDIT_CODE(统一社会信用代码，本字段可代替三证：营业执照编号、组织机构代码、税务登记号，必填其一)
	 * 
	 */
	@Override
	public List<PageData> selectCorps(PageData pd) {
		List<PageData> list = myBatisDao.getList("TRADE_REPORT.selectCorp4Report", pd);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		for (PageData pData : list) {
			pData.put("ENTERPRISE_NAME_CG", pData.get("ENTERPRISE_NAME"));
			Object time = pData.get("CREATE_DATE");
			if (null != time) {
				date = sdf.format(time);
			}
			pData.put("CREATE_DATE", date);
			pData.put("REAL_NAME_CG", pData.get("REAL_NAME"));
			pData.put("PHONE_CG", pData.get("PHONE"));
			pData.put("LOGIN_NAME_CG", pData.get("PHONE"));
			pData.put("ROLE", "1");
			pData.put("SOCIAL_CREDIT_CODE", pData.get("BUSINESS_REGISTRATION_NUMBER"));
		}
		return list;
	}

	/**
	 * 查询新增项目
	 * 
	 * @param pd.create_time(项目添加时间，格式：yyyy-MM-dd，可能为空)
	 * @param pd.states(审核状态，LoanApplicationStateEnum:2345678，可能为空)
	 * @param pd.ids(项目ID的List<String>集合转拼接字符串，可能为空)
	 * 
	 * @return pd.LOAN_ID(项目ID)
	 * @return pd.LOAN_TYPE(借款类型，0：抵押标 1：担保标 2：信用标 3：净值标 4：流转标 5：秒标 6：其他)
	 * @return pd.LOAN_TITLE(借款标题，若无，自定义)
	 * @return pd.LOAN_PURPOSE(借款用途，若无，自定义)
	 * @return pd.LOAN_AMOUNT(借款金额，单位：分（整数），String)
	 * @return pd.RECEIVE_AMOUNT(预期收益，15.56%传1556，取值整数，String)
	 * @return pd.PRODUCT_NAME(产品名称，若无，自定义)
	 * @return pd.PAYMENT_TYPE(还款方式，0.一次性还本付息 1.先息后本 2.等额本息/等额本金 3.其他)
	 * @return pd.LOAN_END(借款期限，即项目期限，项目到期的最后一天，格式yyyyMMdd)
	 * @return pd.LOAN_START(筹标起始日,即项目起始日,格式yyyyMMdd)
	 * @return pd.TENDER_AMOUNT_EACH(每份投标金额,单位：分（整数），String)
	 * @return pd.TENDER_COUNT_MIN(最低投标份数)
	 * @return pd.TENDER_AMOUNT_MAX(最多投标金额)
	 * @return pd.BORROW_NAME_CG(借款人，个人用户返回存管系统登录账号,企业用户返回存管系统企业注册名称)
	 * @return pd.BORROW_PHONE_CG(借款人在存管系统的开户注册手机号)
	 * @return pd.LOAN_DESC(借款人项目概述)
	 * @return pd.LOAN_FEE(费用项，单位：分（整数）没有就传0，做采集，String)
	 * @return pd.PAYMENT_COUNT(还款期数)
	 * 
	 */
	@Override
	public List<PageData> selectLoans(PageData pd) {
		List<PageData> list = myBatisDao.getList("TRADE_REPORT.selectLoan4Report", pd);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		for(PageData pData : list){
			pData.put("LOAN_TYPE", 6);
			pData.put("LOAN_TITLE", pData.get("PRODUCT_NAME"));
			int loan_amount = 0;
			Object loan_balance = pData.get("LOAN_BALANCE");
			if(null != loan_balance){
				loan_amount = (int)(Float.valueOf(loan_balance.toString()) * 100);
			}
			pData.put("LOAN_AMOUNT", loan_amount);
			int receive_amount = 0;
			Object annual_rate = pData.get("ANNUAL_RATE");
			if(null != annual_rate){
				receive_amount = (int)((new BigDecimal(String.valueOf(annual_rate)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) * 100);
			}
			pData.put("RECEIVE_AMOUNT", receive_amount);
			pData.put("PAYMENT_TYPE", 3);
			Object open_time = pData.get("CREATE_TIME");
			if(null != open_time){
				date = sdf.format(open_time);
			}
			pData.put("LOAN_START", date);
			try {
				calendar.setTime(sdf.parse(date));
				calendar.add(Calendar.DATE, 7);// +7日
				date = sdf.format(calendar.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			pData.put("LOAN_END", date);
			pData.put("TENDER_AMOUNT_EACH", 1);
			pData.put("TENDER_COUNT_MIN", 1);
			pData.put("TENDER_AMOUNT_MAX", loan_amount);
			pData.put("TENDER_AMOUNT_MAX", pData.get("LOAN_AMOUNT"));
			Object enterprise_name_obj = pData.get("ENTERPRISE_NAME");
			if(null != enterprise_name_obj){
				pData.put("BORROW_NAME_CG", enterprise_name_obj);
			}else{
				pData.put("BORROW_NAME_CG", pData.get("PHONE"));
			}
			pData.put("BORROW_PHONE_CG", pData.get("PHONE"));
			pData.put("LOAN_DESC", pData.get("LOAN_PURPOSE"));
			pData.put("LOAN_FEE", 0);
		}
		return list;
	}

	/**
	 * 根据身份证号判断性别 
	 * 15位身份证倒数第1位奇数为男性，返回0，偶数为女性，返回1 
	 * 18位身份证倒数第2位奇数为男性，返回0，偶数为女性，返回1
	 * 
	 * @param idCardNo
	 * @return
	 */
	private int checkSexByCardNo(String idCardNo) {
		int temp = 0;
		if (idCardNo.length() == 15) {
			temp = Integer.parseInt(idCardNo.substring(14));
		} else if (idCardNo.length() == 18) {
			temp = Integer.parseInt(idCardNo.substring(16, 17));
		}
		if (temp % 2 == 0) {
			return 1; // 女
		} else {
			return 0; // 男
		}
	}
	
}
