package com.xt.cfp.core.service.impl.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcraft.jsch.SftpException;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.ReportStatusEnum;
import com.xt.cfp.core.pojo.TradeReport;
import com.xt.cfp.core.service.TradeReportService;
import com.xt.cfp.core.service.task.LoanReportTask;
import com.xt.cfp.core.util.PageData;
import com.xt.cfp.core.util.ReportResult;
import com.xt.cfp.core.util.SFTPUtil;

@Service(value = "loanReportTask")
@Transactional
public class LoanReportTaskImpl implements LoanReportTask {
	private static Logger logger = Logger.getLogger(LoanReportTaskImpl.class);

	// SFTP 服务器地址IP地址
	@Value("${sftp.host}")
	private String host;
	// SFTP 端口
	@Value("${sftp.port}")
	private int port;
	// SFTP 登录用户名
	@Value("${sftp.userName}")
	private String username;
	// SFTP 登录密码
	@Value("${sftp.password}")
	private String password;
	// SFTP文件上传路径
	@Value("${sftp.checkPath}")
	private String checkPath;
	// 报备文件本地生成路径
	@Value("${sftp.localcheckPath}")
	private String localCheckPath;
	// SFTP文件回盘路径
	@Value("${sftp.overCheckPath}")
	private String overcheckPath;
	// 回盘文件本地生成路径
	@Value("${sftp.localOverCheckPath}")
	private String localOverCheckPath;
	// 商户号
	@Value("${cg.hf.mchnt_cd}")
	private String mchnt_cd;
	// 第三方支付公司ID
	@Value("${cg.hf.account_id}")
	private String account_id;

	@Autowired
	private TradeReportService tradeReportService;

	@Override
	public void excute() {
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", "PWXM");
		pd.put("create_date", date);
		List<TradeReport> list = tradeReportService.selectTradeReport(pd);
		if (list == null || list.size() == 0) {
			String fileNum = "0000";
			reportUpload(fileNum);
		} else {
			TradeReport tradeReport = list.get(0);
			String fileNum = tradeReport.getFile_num();
			String reportStatus = tradeReport.getReport_status();
			Long reportId = tradeReport.getId();
			if (reportStatus.equals(ReportStatusEnum.UP.getValue())) {
				logger.info("开始报备文件回盘核检");
				reportDownload(fileNum, reportId);
			} else {
				logger.info("项目报备结束");
			}
		}
	}

	/**
	 * 项目报备
	 */
	private void reportUpload(String fileNum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		PageData pd = new PageData();
		pd.put("create_time", date);
		pd.put("states", getState4Select());
		List<PageData> list = tradeReportService.selectLoans(pd);
		logger.info("新增项目:" + list.size());
		TradeReport tradeReport = new TradeReport();
		tradeReport.setReport_type("PWXM");
		tradeReport.setCreate_date(new Date());
		if (list.size() > 0) {
			int num = getFileNum(fileNum);
			fileNum = writeReportAndUpload(fileNum, list, num);
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
		} else {
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
		}
		tradeReport.setFile_num(fileNum);
		tradeReportService.addTradeReport(tradeReport);
	}

	/**
	 * 椤圭洰琛ユ姤
	 */
	private void reportUpload(String fileNum, Long reportId, List<Long> loanIds) {
		List<PageData> list = getLoansByIds(loanIds);
		logger.info("项目补报" + list.size());
		int num = getFileNum(fileNum);
		fileNum = writeReportAndUpload(fileNum, list, num);
		TradeReport tradeReport = new TradeReport();
		tradeReport.setId(reportId);
		tradeReport.setFile_num(fileNum);
		tradeReportService.updateByPrimaryKeySelective(tradeReport);
	}

	/**
	 * 项目编号集合查询项目列表， 一次查询上限100条
	 * 
	 * @param loanCodes
	 * @return
	 */
	public List<PageData> getLoansByIds(List<Long> loanIds) {
		List<PageData> list = new ArrayList<>();
		int count = loanIds.size();
		int index = 1;
		int max = 100;
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		pd.put("create_time", date);
		pd.put("states", getState4Select());
		while (count >= index * max) {
			List<Long> loanIds1 = loanIds.subList((index - 1) * max, index * max);
			pd.put("ids", loanIds1);
			List<PageData> list1 = tradeReportService.selectLoans(pd);
			list.addAll(list1);
			index++;
		}
		if (count > (index - 1) * max) {
			List<Long> loanIds2 = loanIds.subList((index - 1) * max, count);
			pd.put("ids", loanIds2);
			List<PageData> list2 = tradeReportService.selectLoans(pd);
			list.addAll(list2);
		}
		return list;
	}

	/**
	 * 报备核检
	 */
	private void reportDownload(String fileNum, Long reportId) {
		List<String> fileNameList = downLoanReportFile(fileNum);
		if (fileNameList.size() == 0) {
			logger.info("项目报备文件等待核检...");
			return;
		}
		// 处理回盘问价
		List<Long> loanIds = new ArrayList<>();// 需要补报的项目编号列表
		for (int i = 0; i < fileNameList.size(); i++) {
			List<Long> ids = readFileByLines(fileNameList.get(i));
			loanIds.addAll(ids);
		}
		if (loanIds.size() > 0) { // 核检失败
			reportUpload(fileNum, reportId, loanIds);
		} else { // 报备成功
			TradeReport tradeReport = new TradeReport();
			tradeReport.setId(reportId);
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		}
	}

	/**
	 * 获取报备文件序号
	 * 
	 * @param fileNum
	 */
	private List<String> downLoanReportFile(String fileNum) {
		// 创建本地回盘文件夹
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date1 = sdf.format(new Date());
		String path = localOverCheckPath + date1 + "/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);
		sftpUtil.login();
		String[] fileNumArr = fileNum.split("#");
		List<String> fileNameList = new ArrayList<>();// 回盘文件名称列表
		logger.info("开始下载项目报备回盘文件...");
		for (int i = 0; i < fileNumArr.length; i++) {
			// 文件名称
			String downloadFile = date1 + "_P2P_PWXM_" + date1 + "_" + fileNumArr[i] + ".txt";
			// 上传本地文件
			String saveFile = path + downloadFile;
			if (sftpUtil.exist(overcheckPath, downloadFile)) {
				try {
					sftpUtil.download(overcheckPath, downloadFile, saveFile);
					logger.info("回盘文件" + downloadFile + " 获取成功！");
					fileNameList.add(saveFile);
				} catch (FileNotFoundException | SftpException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("回盘文件" + downloadFile + " 不存在");
				fileNameList = new ArrayList<>();
				break;
			}
		}
		logger.info("下载项目报备回盘文件结束");
		sftpUtil.logout();
		return fileNameList;
	}

	private String writeReportAndUpload(String fileNum, List<PageData> list, int num) {
		String fileNameCode = "P2P_PWXM_";
		int len = list.size();
		if (len > 0) {
			fileNum = addZeroForNum(String.valueOf(num));
		}
		int index = 0;// 循环变量
		int count = 0;// 计数器
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 链接sftp
		File file = createFile(fileNameCode, String.valueOf(num));
		PrintStream pStream = null;
		try {
			pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
			while (index < len) {
				PageData pd = list.get(index);
				String text = writeReportTxt(pd);
				pStream.append(text);
				index++;
				count++;
				// 记录数超过2000文件序号+1
				if (count == 2000 && index < len) {
					sftpUtil.upload(checkPath, file);
					num++;
					fileNum += "#" + addZeroForNum(String.valueOf(num));
					file = createFile(fileNameCode, String.valueOf(num));
					pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
					count = 0;
				}
			}
			if (pStream != null) {
				pStream.close();
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sftpUtil.upload(checkPath, file);
		sftpUtil.logout(); // 退出sftp.
		return fileNum;
	}

	/**
	 * 编辑报备内容
	 * 
	 * @param pd
	 * @return
	 */
	private String writeReportTxt(PageData pd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		String loan_id = String.valueOf(pd.get("LOAN_ID"));
		String serial_number = date + loan_id;
		String text = mchnt_cd + "|" + serial_number + "|" + pd.get("LOAN_ID") + "|" + pd.get("LOAN_TYPE") + "|"
				+ pd.get("LOAN_TITLE") + "|" + "" + "|" + pd.get("LOAN_PURPOSE") + "|" + pd.get("LOAN_AMOUNT") + "|"
				+ pd.get("RECEIVE_AMOUNT") + "|" + pd.get("PRODUCT_NAME") + "|" + pd.get("PAYMENT_TYPE") + "|"
				+ pd.get("LOAN_END") + "|" + pd.get("LOAN_START") + "|" + pd.get("TENDER_AMOUNT_EACH") + "|"
				+ pd.get("TENDER_COUNT_MIN") + "|" + pd.get("TENDER_AMOUNT_MAX") + "|" + pd.get("BORROW_NAME_CG") + "|"
				+ pd.get("BORROW_PHONE_CG") + "|" + pd.get("LOAN_DESC") + "|" + pd.get("LOAN_FEE") + "|" + "" + "|"
				+ pd.get("PAYMENT_COUNT") + "|" + "" + "|" + account_id + "\r\n";
		return text;
	}

	/**
	 * 以行为单位读取文件,获取报备失败的项目变号
	 */
	private List<Long> readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		List<Long> list = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] strArr = tempString.split("\\|");
				String status = strArr[strArr.length - 2];// 核检状态
				if (!status.equals("0000") && !status.equals("0003")) {
					// 核检失败，取有效字段，补报
					list.add(Long.valueOf(strArr[2]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return list;
	}

	/**
	 * 获取下一个文件序号
	 * 
	 * @return
	 */
	private int getFileNum(String fileNum) {
		int num = 0;
		if (fileNum != null) {
			String[] fileNumArr = fileNum.split("#");
			for (int i = 0; i < fileNumArr.length; i++) {
				int a = Integer.valueOf(fileNumArr[i]);
				if (num < a) {
					num = a;
				}
			}
		}
		return num + 1;
	}

	/**
	 * 创建.txt文件
	 * 
	 * @param localcheckPath
	 * @param fileNameCode
	 */
	private File createFile(String fileNameCode, String num) {
		// 创建本地文件夹
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now = sdf.format(new Date());
		String path = localCheckPath + now + "/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String numStr = addZeroForNum(num);
		String fileName = fileNameCode + now + "_" + numStr + ".txt";
		File file = new File(path + fileName);
		try {
			file.createNewFile();
			logger.info("生成项目报备本地文件" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 生成文件序号1->0001,11->0011
	 * 
	 * @param str
	 * @return
	 */
	private static String addZeroForNum(String num) {
		String str = "0000";
		int len = num.length();
		str = str.substring(0, str.length() - len) + num;
		return str;
	}

	/**
	 * 拼接标的查询状态
	 * 
	 * @param strs
	 * @return
	 */
	private List<String> getState4Select() {
		List<String> list = new ArrayList<>();
		list.add(LoanApplicationStateEnum.PUBLISHAUDITING.getValue());
		list.add(LoanApplicationStateEnum.BIDING.getValue());
		list.add(LoanApplicationStateEnum.LOANAUDIT.getValue());
		list.add(LoanApplicationStateEnum.WAITMAKELOANAUDIT.getValue());
		list.add(LoanApplicationStateEnum.REPAYMENTING.getValue());
		list.add(LoanApplicationStateEnum.COMPLETED.getValue());
		list.add(LoanApplicationStateEnum.EARLYCOMPLETE.getValue());
		list.add(LoanApplicationStateEnum.FAILURE.getValue());//流标
		return list;
	}

	// ==========================手动补报======================================
	@Override
	public ReportResult loanReportTxt() {
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", "PWXM");
		pd.put("create_date", date);
		List<TradeReport> loanReportList = tradeReportService.selectTradeReport(pd);
		List<PageData> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date1 = sdf.format(calendar.getTime());
		if (null == loanReportList || loanReportList.size() == 0) {
			// 没有报备记录
			pd = new PageData();
			pd.put("create_time", date1);
			pd.put("states", getState4Select());
			list = tradeReportService.selectLoans(pd);
			if (null == list || list.size() == 0) {
				TradeReport tradeReport = new TradeReport();
				tradeReport.setFile_num("0000");
				tradeReport.setReport_type("PWXM");
				tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
				tradeReport.setCreate_date(new Date());
				tradeReportService.addTradeReport(tradeReport);
				reportResult.setMessage("项目报备结束");
				return reportResult;
			}
		} else {
			// 检查报备状态
			String reportStatus = loanReportList.get(0).getReport_status();
			if (ReportStatusEnum.UP.getValue().equals(reportStatus)) {
				String reportFileNum = loanReportList.get(0).getFile_num();
				List<String> fileNameList = downLoanReportFile(reportFileNum);
				if (fileNameList.size() == 0) {
					reportResult.setMessage("项目报备文件等待核检...");
					return reportResult;
				}
				List<Long> loanIds = new ArrayList<>();// 需要补报的个人用户平台登录账号
				for (int i = 0; i < fileNameList.size(); i++) {
					List<Long> ids = readFileByLines(fileNameList.get(i));
					loanIds.addAll(ids);
				}
				if (loanIds.size() == 0) {
					TradeReport tradeReport = new TradeReport();
					tradeReport.setId(loanReportList.get(0).getId());
					tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
					tradeReportService.updateByPrimaryKeySelective(tradeReport);
					reportResult.setMessage("项目报备核检成功");
					return reportResult;
				}
				// 根据ids查询项目
				pd = new PageData();
				pd.put("create_time", date1);
				pd.put("states", getState4Select());
				pd.put("ids", loanIds);
				list = tradeReportService.selectLoans(pd);
			} else {
				reportResult.setMessage("项目报备结束");
				return reportResult;
			}
		}
		String text = "";
		for (int i = 0; i < list.size(); i++) {
			pd = list.get(i);
			text += writeReportTxt(pd);
		}
		reportResult.setText(text);
		return reportResult;
	}

	@Override
	public ReportResult loanReportUp(String text) {
		String[] textArr = text.split("\n");
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", "PWXM");
		pd.put("create_date", date);
		Long reportId = null;
		String fileNum = null;
		List<TradeReport> loanReportList = tradeReportService.selectTradeReport(pd);
		if (null != loanReportList && loanReportList.size() > 0) {
			reportId = loanReportList.get(0).getId();
			fileNum = loanReportList.get(0).getFile_num();
		}
		int num = getFileNum(fileNum);
		fileNum = addZeroForNum(String.valueOf(num));
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 连接sftp服务器
		File file = createFile("P2P_PWXM_", String.valueOf(num));
		int len = textArr.length;
		int index = 0;// 循环变量
		int count = 0;// 计数器
		PrintStream pStream = null;
		try {
			pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
			while (index < len) {
				String str = textArr[index];
				pStream.append(str.trim()+"\r\n");
				index++;
				count++;
				// 记录数超过2000文件序号+1
				if (count == 2000 && index < len) {
					sftpUtil.upload(checkPath, file);
					num++;
					fileNum += "#" + addZeroForNum(String.valueOf(num));// 更新文件序号;
					file = createFile("P2P_PWXM_", String.valueOf(num));
					pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
					count = 0;
				}
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (pStream != null) {
				pStream.close();
			}
		}
		sftpUtil.upload(checkPath, file);
		sftpUtil.logout(); // 退出sftp服务器
		logger.info("项目手动报备文件上传成功");
		TradeReport tradeReport = new TradeReport();
		tradeReport.setFile_num(fileNum);
		if (null != loanReportList && loanReportList.size() > 0) {
			tradeReport.setId(reportId);
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		} else {
			tradeReport.setReport_type("PWXM");
			tradeReport.setCreate_date(new Date());
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
			tradeReportService.addTradeReport(tradeReport);
		}
		reportResult.setMessage("项目手动报备上传成功，等待核检...");
		return reportResult;
	}

}
