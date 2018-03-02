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
import com.xt.cfp.core.constants.ReportStatusEnum;
import com.xt.cfp.core.constants.ResponseStatusEnum;
import com.xt.cfp.core.constants.TradeOperateEnum;
import com.xt.cfp.core.constants.TradeTypeEnum;
import com.xt.cfp.core.pojo.TradeReport;
import com.xt.cfp.core.service.TradeReportService;
import com.xt.cfp.core.service.task.TradeReportTask;
import com.xt.cfp.core.util.PageData;
import com.xt.cfp.core.util.ReportResult;
import com.xt.cfp.core.util.SFTPUtil;

@Service(value = "tradeReportTask")
@Transactional
public class TradeReportTaskImpl implements TradeReportTask {
	private static Logger logger = Logger.getLogger(TradeReportTaskImpl.class);

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
		String reportType = TradeTypeEnum.Tender.getValue();
		tradeReport(reportType);
	}

	/**
	 * 根据交易类型执行对应的文件报备操作
	 * 
	 * @param tradeTypeEnum
	 */
	private void changeReportType(TradeTypeEnum tradeTypeEnum) {
		switch (tradeTypeEnum) {
		case Tender:
			tradeReport(TradeTypeEnum.Full.getValue());
			break;
		case Full:
			tradeReport(TradeTypeEnum.Thansfer.getValue());
			break;
		case Thansfer:
			tradeReport(TradeTypeEnum.Repayment.getValue());
			break;
		case Repayment:
			tradeReport(TradeTypeEnum.Other.getValue());
			break;
		case Other:
			tradeReport(TradeTypeEnum.Loss.getValue());
			break;
		case Loss:
			reportOver();
			break;
		default:
			throw new IllegalArgumentException("错误的reportType参数: " + tradeTypeEnum.getValue());
		}
	}

	private void reportOver() {
		logger.info("交易报备结束");
	}

	/**
	 * 交易报备
	 */
	private void tradeReport(String tradeType) {
		String fileDesc = TradeTypeEnum.value2Enum(tradeType).getDesc();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", tradeType);
		pd.put("create_date", date);
		List<TradeReport> list = tradeReportService.selectTradeReport(pd);
		if (list == null || list.size() == 0) {
			String fileNum = "0000";
			reportUpload(fileNum, tradeType);
		} else {
			TradeReport tradeReport = list.get(0);
			String fileNum = tradeReport.getFile_num();
			String reportStatus = tradeReport.getReport_status();
			Long reportId = tradeReport.getId();
			if (reportStatus.equals(ReportStatusEnum.UP.getValue())) {
				logger.info("开始" + fileDesc + "报备文件回盘核检");
				reportDownload(fileNum, reportId, tradeType);
			} else {
				logger.info(fileDesc + "报备文件回盘核检结束");
			}
		}
		// 进入下一项报备
		changeReportType(TradeTypeEnum.value2Enum(tradeType));
	}

	/**
	 * 交易报备
	 */
	private void reportUpload(String fileNum, String tradeType) {
		String fileDesc = TradeTypeEnum.value2Enum(tradeType).getDesc();
		// 获取上报数据
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		pd.put("message_id", tradeType);
		pd.put("trade_status", ResponseStatusEnum.Success.getValue());
		pd.put("trade_date", date);
		List<PageData> list = tradeReportService.selectTrades(pd);
		logger.info(fileDesc + "报备：" + list.size());
		TradeReport tradeReport = new TradeReport();
		tradeReport.setReport_type(tradeType);
		tradeReport.setCreate_date(new Date());
		if (list.size() > 0) {
			int num = getFileNum();
			fileNum = writeReportAndUpload(fileNum, list, num, tradeType);
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
		} else {
			logger.info(TradeTypeEnum.value2Enum(tradeType).getDeclaringClass() + "报备没有数据");
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
		}
		tradeReport.setFile_num(fileNum);
		tradeReportService.addTradeReport(tradeReport);
	}

	/**
	 * 补报
	 * 
	 * @param serialNumbers
	 *            交易流水列表
	 */
	private void reportUpload(String fileNum, Long reportId, List<String> serialNumbers, String tradeType) {
		String fileDesc = TradeTypeEnum.value2Enum(tradeType).getDesc();
		// 获取上报数据
		List<PageData> list = getTradeListBySerialNumbers(tradeType, serialNumbers);
		logger.info(fileDesc + "补报：" + list.size());
		// 上传报备文件
		int num = getFileNum();
		fileNum = writeReportAndUpload(fileNum, list, num, tradeType);
		TradeReport tradeReport = new TradeReport();
		tradeReport.setId(reportId);
		tradeReport.setFile_num(fileNum);
		// 更新报备记录
		tradeReportService.updateByPrimaryKeySelective(tradeReport);
	}

	/**
	 * 根据流水号集合获取交易流水记录,一次查询上限100条
	 * 
	 * @param serialNumbers
	 * @return
	 */
	private List<PageData> getTradeListBySerialNumbers(String tradeType, List<String> serialNumbers) {
		List<PageData> list = new ArrayList<>();
		int count = serialNumbers.size();
		int index = 1;
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		pd.put("trade_date", date);
		pd.put("message_id", tradeType);
		pd.put("trade_status", ResponseStatusEnum.Success.getValue());
		while (count >= index * 100) {
			List<String> serialNumbers1 = serialNumbers.subList((index - 1) * 100, index * 100);
			pd.put("serialNumbers", serialNumbers1);
			List<PageData> list1 = tradeReportService.selectTrades(pd);
			list.addAll(list1);
			index++;
		}
		if (count > (index - 1) * 100) {
			List<String> serialNumbers2 = serialNumbers.subList((index - 1) * 100, count);
			pd.put("serialNumbers", serialNumbers2);
			List<PageData> list2 = tradeReportService.selectTrades(pd);
			list.addAll(list2);
		}
		return list;
	}

	private String writeReportAndUpload(String fileNum, List<PageData> list, int num, String tradeType) {
		if (list.size() > 0) {
			fileNum = addZeroForNum(String.valueOf(num));// 更新文件序号
		}
		String fileNameCode = "P2P_PWJY_";
		int len = list.size();
		int index = 0;// 循环变量
		int count = 0;// 计数器
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 连接sftp服务器
		File file = createFile(fileNameCode, String.valueOf(num), tradeType);
		PrintStream pStream = null;
		try {
			pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
			while (index < len) {
				PageData pd = list.get(index);
				String text = writeReportTxt(pd, tradeType);
				pStream.append(text);
				index++;
				count++;
				// 记录数超过2000文件序号+1
				if (count == 2000 && index < len) {
					sftpUtil.upload(checkPath, file);
					num++;
					fileNum += "#" + addZeroForNum(String.valueOf(num));// 更新文件序号;
					file = createFile(fileNameCode, String.valueOf(num), tradeType);
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
		return fileNum;
	}

	private String writeReportTxt(PageData pd, String tradeType) {
		String trade_operate = "2";
		Object trade_operate_obj = pd.get("TRADE_OPERATE");
		if (null != trade_operate_obj) {
			trade_operate = trade_operate_obj.toString();
		}
		String pay_account = "";
		String pay_organization = "";
		String receive_account = "";
		String receive_organization = "";
		if (TradeOperateEnum.Freeze.getValue().equals(trade_operate) || TradeOperateEnum.Thaw.getValue().equals(trade_operate)) { // 冻结
			pay_account = pd.getString("REQUEST_TRADING_ACCOUNT"); // 出账人
			pay_organization = pd.getString("REQUEST_ORGANIZATION");
		}else {
			pay_account = pd.getString("REQUEST_TRADING_ACCOUNT"); // 出账人
			pay_organization = pd.getString("REQUEST_ORGANIZATION");
			receive_account = pd.getString("RESPONSE_TRADING_ACCOUNT"); // 入账人
			receive_organization = pd.getString("RESPONSE_ORGANIZATION");
		}
		String loanType = getLoanType(TradeOperateEnum.value2Enum(trade_operate));
		String busiType = bussinessType(TradeTypeEnum.value2Enum(tradeType),trade_operate);
		String loan_id = String.valueOf(pd.get("LOAN_ID"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		String code = date + loan_id;
		String text = mchnt_cd + "|" + pd.get("SERIAL_NUMBER") + "|" + sdf.format(pd.get("TRADE_DATE")) + "|" + loanType
				+ "|" + loan_id + "|" + code + "|" + pay_account + "|" + pay_organization + "|"
				+ pd.get("REQUEST_TRADING_AMOUNT") + "|" + "" + "|" + receive_account + "|" + receive_organization + "|"
				+ "" + "|" + "" + "|" + busiType + "|" + account_id + "\r\n";
		return text;
	}

	private void reportDownload(String fileNum, Long reportId, String tradeType) {
		String fileDesc = TradeTypeEnum.value2Enum(tradeType).getDesc();
		List<String> fileNameList = downReportFile(fileNum, tradeType);// 回盘文件名称列表
		if (fileNameList.size() == 0) {
			logger.info(fileDesc + "报备文件等待核检...");
			return;
		}
		// 处理回盘文件
		List<String> serialNumbers = new ArrayList<>();// 需要补报的流水号
		for (int i = 0; i < fileNameList.size(); i++) {
			List<String> strList = readFileByLines(fileNameList.get(i));
			serialNumbers.addAll(strList);
		}
		if (serialNumbers.size() > 0) { // 回盘报错
			reportUpload(fileNum, reportId, serialNumbers, tradeType);
		} else { // 报备成功
			logger.info(fileDesc + "核检成功");
			TradeReport tradeReport = new TradeReport();
			tradeReport.setId(reportId);
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		}
	}

	private List<String> downReportFile(String fileNum, String tradeType) {
		// 创建本地文件夹
		String fileDesc = TradeTypeEnum.value2Enum(tradeType).getDesc();
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
		logger.info("开始下载" + fileDesc + "回盘文件...");
		for (int i = 0; i < fileNumArr.length; i++) {
			// 文件名称
			String downloadFile = date1 + "_P2P_PWJY_" + date1 + "_" + fileNumArr[i] + ".txt";
			// 生成本地文件
			String saveFile = path + downloadFile;
			if (sftpUtil.exist(overcheckPath, downloadFile)) {
				try {
					sftpUtil.download(overcheckPath, downloadFile, saveFile);
					logger.info(fileDesc + "回盘文件：" + downloadFile + "获取成功");
					fileNameList.add(saveFile);
				} catch (FileNotFoundException | SftpException e) {
					e.printStackTrace();
				}
			} else {
				logger.info(fileDesc + "报备回盘文件：" + downloadFile + "不存在");
				fileNameList = new ArrayList<>();
				break;
			}
		}
		logger.info("下载" + fileDesc + "回盘文件结束");
		sftpUtil.logout();
		return fileNameList;
	}

	/**
	 * 以行为单位读取文件,获取报备失败的流水号
	 */
	private List<String> readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		List<String> list = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] strArr = tempString.split("\\|");
				String str = strArr[strArr.length - 2];
				if (!str.equals("0000") && !str.equals("0003")) {
					list.add(strArr[1]);
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
	private int getFileNum() {
		int num = 0;
		String fileNum = selectFileNum();
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
	 * 查询交易报备文件序号
	 * 
	 * @return
	 */
	private String selectFileNum() {
		String fileNum = "0000";
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		pd.put("types", getTypes());
		pd.put("create_date", sdf.format(new Date()));
		List<TradeReport> list = tradeReportService.selectTradeReport(pd);
		for (int i = 0; i < list.size(); i++){
			fileNum += "#" + list.get(i).getFile_num();
		}
		return fileNum;
	}

	/**
	 * 交易类型
	 * 
	 * @param messageId
	 * @return
	 */
	private String getLoanType(TradeOperateEnum tradeOperateEnum) {
		switch (tradeOperateEnum) {
		case Flowage:
			return "PWDZ";
		case Thansfer:
			return "PWDZ";
		case Thaw:
			return "PWJD";
		case Freeze:
			return "PWDJ";
		case Initiative:
			return "PWDZ";
		default:
			throw new IllegalArgumentException("错误的trade_operate参数: " + tradeOperateEnum.getValue());
		}
	}

	private String bussinessType(TradeTypeEnum tradeTypeEnum,String trade_operate) {
		switch (tradeTypeEnum) {
		case Tender:
			return "0";
		case Loss:
			return "5";
		case Full:
			if (TradeOperateEnum.Thaw.getValue().equals(trade_operate)) {
				return "4";
			}else{
				return "1";
			}
		case Thansfer:
			return "2";
		case Repayment:
			return "3";
		case Other:
			return "4";
		default:
			throw new IllegalArgumentException("错误的tradeType参数: " + tradeTypeEnum.getValue());
		}
	}

	/**
	 * 交易类型集合
	 * 
	 * @return
	 */
	private List<String> getTypes() {
		List<String> list = new ArrayList<>();
		list.add(TradeTypeEnum.Tender.getValue());
		list.add(TradeTypeEnum.Loss.getValue());
		list.add(TradeTypeEnum.Full.getValue());
		list.add(TradeTypeEnum.Thansfer.getValue());
		list.add(TradeTypeEnum.Repayment.getValue());
		list.add(TradeTypeEnum.Other.getValue());
		return list;
	}

	/**
	 * 创建.txt文件
	 * 
	 * @param localcheckPath
	 * @param fileNameCode
	 */
	private File createFile(String fileNameCode, String num, String tradeType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now = sdf.format(new Date());
		String path = localCheckPath + now + "/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String numStr = addZeroForNum(num); // 文件序号
		String fileName = fileNameCode + now + "_" + numStr + ".txt";
		File file = new File(path + fileName);
		try {
			file.createNewFile();
			logger.info("生成" + TradeTypeEnum.value2Enum(tradeType).getDesc() + "本地报备文件：" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 生成文件序号 如：1->0001,11->0011
	 * 
	 * @param str
	 * @return
	 */
	private String addZeroForNum(String num) {
		String str = "0000";
		int len = num.length();
		str = str.substring(0, str.length() - len) + num;
		return str;
	}

	// ============================ 手动报备 ===========================
	@Override
	public ReportResult tradeReportTxt(String tradeType) {
		String fileDesc = TradeTypeEnum.value2Enum(tradeType).getDesc();
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", tradeType);
		pd.put("create_date", date);
		List<TradeReport> tradeReportList = tradeReportService.selectTradeReport(pd);
		List<PageData> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date1 = sdf.format(calendar.getTime());
		if (null == tradeReportList || tradeReportList.size() == 0) {
			// 没有报备记录
			pd = new PageData();
			pd.put("trade_date", date1);
			pd.put("message_id", tradeType);
			pd.put("trade_status", ResponseStatusEnum.Success.getValue());
			list = tradeReportService.selectTrades(pd);
			if (null == list || list.size() == 0) {
				TradeReport tradeReport = new TradeReport();
				tradeReport.setFile_num("0000");
				tradeReport.setReport_type(tradeType);
				tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
				tradeReport.setCreate_date(new Date());
				tradeReportService.addTradeReport(tradeReport);
				reportResult.setMessage(fileDesc + "报备结束");
				return reportResult;
			}
		} else {
			// 检查报备状态
			String reportStatus = tradeReportList.get(0).getReport_status();
			if (ReportStatusEnum.UP.getValue().equals(reportStatus)) {
				String reportFileNum = tradeReportList.get(0).getFile_num();
				List<String> fileNameList = downReportFile(reportFileNum, tradeType);
				if (fileNameList.size() == 0) {
					reportResult.setMessage(fileDesc + "报备文件等待核检...");
					return reportResult;
				}
				List<String> serialNumbers = new ArrayList<>();// 需要补报的个人用户平台登录账号
				for (int i = 0; i < fileNameList.size(); i++) {
					List<String> strList = readFileByLines(fileNameList.get(i));
					serialNumbers.addAll(strList);
				}
				if (serialNumbers.size() == 0) {
					TradeReport tradeReport = new TradeReport();
					tradeReport.setId(tradeReportList.get(0).getId());
					tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
					tradeReportService.updateByPrimaryKeySelective(tradeReport);
					reportResult.setMessage(fileDesc + "报备文件核检成功");
					return reportResult;
				}
				// 根据ids查询项目
				pd = new PageData();
				pd.put("message_id", tradeType);
				pd.put("trade_date", date1);
				pd.put("trade_status", ResponseStatusEnum.Success.getValue());
				pd.put("serialNumbers", serialNumbers);
				list = tradeReportService.selectTrades(pd);
			} else {
				reportResult.setMessage(fileDesc + "报备结束");
				return reportResult;
			}
		}
		String text = "";
		for (int i = 0; i < list.size(); i++) {
			pd = list.get(i);
			text += writeReportTxt(pd, tradeType);
		}
		reportResult.setText(text);
		return reportResult;
	}

	@Override
	public ReportResult tradeReportUp(String tradeType, String text) {
		String[] textArr = text.split("\n");
		String fileDesc = TradeTypeEnum.value2Enum(tradeType).getDesc();
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", tradeType);
		pd.put("create_date", date);
		Long reportId = null;
		String fileNum = null;
		List<TradeReport> tradeReportList = tradeReportService.selectTradeReport(pd);
		if (null != tradeReportList && tradeReportList.size() > 0) {
			reportId = tradeReportList.get(0).getId();
			fileNum = tradeReportList.get(0).getFile_num();
		}
		int num = getFileNum();
		fileNum = addZeroForNum(String.valueOf(num));
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 连接sftp服务器
		File file = createFile("P2P_PWJY_", String.valueOf(num), tradeType);
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
					file = createFile("P2P_PWJY_", String.valueOf(num), tradeType);
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
		logger.info(fileDesc + "手动报备文件上传成功");
		TradeReport tradeReport = new TradeReport();
		tradeReport.setFile_num(fileNum);
		if (null != tradeReportList && tradeReportList.size() > 0) {
			tradeReport.setId(reportId);
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		} else {
			tradeReport.setReport_type(tradeType);
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
			tradeReport.setCreate_date(new Date());
			tradeReportService.addTradeReport(tradeReport);
		}
		reportResult.setMessage(fileDesc + "手动报备上传成功，等待核检...");
		return reportResult;
	}

}
