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
import com.xt.cfp.core.constants.TradeTypeEnum;
import com.xt.cfp.core.pojo.TradeReport;
import com.xt.cfp.core.service.TradeReportService;
import com.xt.cfp.core.service.task.UserReportTask;
import com.xt.cfp.core.util.PageData;
import com.xt.cfp.core.util.ReportResult;
import com.xt.cfp.core.util.SFTPUtil;

@Service(value = "userReportTask")
@Transactional
public class UserReportTaskImpl implements UserReportTask {
	private static Logger logger = Logger.getLogger(UserReportTaskImpl.class);

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

		// 个人用户报备、核检
		userReport();

		// 法人用户报备、核检
		corpReport();
	}

	// ========================个人用户报备、核检===========================
	private void userReport() {
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", TradeTypeEnum.UserRegist.getValue());
		pd.put("create_date", date);
		List<TradeReport> userTradeReportList = tradeReportService.selectTradeReport(pd);
		if (userTradeReportList == null || userTradeReportList.size() == 0) {
			String userReportFileNum = "0000";
			userReportUpload(userReportFileNum);
		} else {
			TradeReport tradeReport = userTradeReportList.get(0);
			String userReportFileNum = tradeReport.getFile_num();
			String userReportStatus = tradeReport.getReport_status();
			Long userReportId = tradeReport.getId();
			if (userReportStatus.equals(ReportStatusEnum.UP.getValue())) {
				logger.info("开始个人开户回盘文件核检");
				userReportDownload(userReportFileNum, userReportId);
			} else {
				logger.info("个人开户报备结束");
			}
		}
	}

	/**
	 * 用户报备
	 */
	private void userReportUpload(String userReportFileNum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		PageData pd = new PageData();
		pd.put("message_id", TradeTypeEnum.UserRegist.getValue());
		pd.put("trade_status", ResponseStatusEnum.Success.getValue());
		pd.put("trade_date", date);
		List<PageData> list = tradeReportService.selectUsers(pd);
		logger.info("个人用户新开：" + list.size());
		TradeReport tradeReport = new TradeReport();
		tradeReport.setCreate_date(new Date());
		tradeReport.setReport_type(TradeTypeEnum.UserRegist.getValue());
		if (list.size() > 0) {
			int num = getFileNum(userReportFileNum);// 文件序号
			userReportFileNum = individualWriteAndUpload(userReportFileNum, list, "PW10", num);
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
		} else {
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
		}
		tradeReport.setFile_num(userReportFileNum);
		tradeReportService.addTradeReport(tradeReport);
	}

	/**
	 * 用户补报
	 * 
	 * @param serialNumbers
	 *            流水号
	 */
	private void userReportUpload(String userReportFileNum, Long userReportId, List<String> loginNames) {
		List<PageData> list = getUserRegistByLoginNames(loginNames);
		logger.info("个人用户补报：" + list.size());
		int num = getFileNum(userReportFileNum);
		userReportFileNum = individualWriteAndUpload(userReportFileNum, list, "PW10", num);
		TradeReport tradeReport = new TradeReport();
		tradeReport.setId(userReportId);
		tradeReport.setFile_num(userReportFileNum);
		tradeReportService.updateByPrimaryKeySelective(tradeReport);
	}

	/**
	 * 平台用户登录账号集合查询新增个人用户， 一次查询上限100条
	 * 
	 * @param loginNames
	 * @return
	 */
	private List<PageData> getUserRegistByLoginNames(List<String> loginNames) {
		List<PageData> list = new ArrayList<>();
		int count = loginNames.size();
		int index = 1;
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		pd.put("message_id", TradeTypeEnum.UserRegist.getValue());
		pd.put("trade_status", ResponseStatusEnum.Success.getValue());
		pd.put("trade_date", date);
		while (count >= index * 100) {
			List<String> loginNames1 = loginNames.subList((index - 1) * 100, index * 100);
			pd.put("loginNames", loginNames1);
			List<PageData> list1 = tradeReportService.selectUsers(pd);
			list.addAll(list1);
			index++;
		}
		if (count > (index - 1) * 100) {
			List<String> loginNames2 = loginNames.subList((index - 1) * 100, count);
			pd.put("loginNames", loginNames2);
			List<PageData> list2 = tradeReportService.selectUsers(pd);
			list.addAll(list2);
		}
		return list;
	}

	/**
	 * 个人用户报备文件回盘核检
	 */
	private void userReportDownload(String userReportFileNum, Long userReportId) {
		List<String> fileNameList = downLoadUserReportFiles(userReportFileNum);// 回盘文件名称列表
		if (fileNameList.size() == 0) {
			logger.info("个人开户报备文件等待核检...");
			return;
		}
		// 处理回盘文件
		List<String> loginNames = new ArrayList<>();// 需要补报的个人用户平台登录账号
		for (int i = 0; i < fileNameList.size(); i++) {
			List<String> strList = readFileByLines(fileNameList.get(i), 3);
			loginNames.addAll(strList);
		}
		if (loginNames.size() > 0) { // 回盘报错
			userReportUpload(userReportFileNum, userReportId, loginNames);
		} else { // 报备成功
			TradeReport tradeReport = new TradeReport();
			tradeReport.setId(userReportId);
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		}
	}

	/**
	 * 下载个人开户报备回盘文件
	 * 
	 * @return 回盘文件名
	 */
	private List<String> downLoadUserReportFiles(String userReportFileNum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		String path = localOverCheckPath + date + "/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);
		sftpUtil.login();
		String[] fileNumArr = userReportFileNum.split("#");
		List<String> fileNameList = new ArrayList<>();// 回盘文件名称列表
		logger.info("开始下载个人开户报备回盘文件...");
		for (int i = 0; i < fileNumArr.length; i++) {
			// 文件名称
			String downloadFile = date + "_P2P_PW10_" + date + "_" + fileNumArr[i] + ".txt";
			// 生成本地文件
			String saveFile = path + downloadFile;
			if (sftpUtil.exist(overcheckPath, downloadFile)) {
				try {
					sftpUtil.download(overcheckPath, downloadFile, saveFile);
					logger.info("回盘文件:" + downloadFile + "获取成功");
					fileNameList.add(saveFile);
				} catch (FileNotFoundException | SftpException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("回盘文件：" + downloadFile + "不存在");
				fileNameList = new ArrayList<>();
				break;
			}
		}
		logger.info("下载个人开户报备回盘文件结束");
		sftpUtil.logout();
		return fileNameList;
	}

	/**
	 * 生成本地个人平台开户报备文件并上传
	 * 
	 * @param list
	 * @param fileNameCode
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	private String individualWriteAndUpload(String userReportFileNum, List<PageData> list, String fileNameCode,
			int num) {
		int len = list.size();
		if (len > 0) {
			userReportFileNum = addZeroForNum(String.valueOf(num));
		}
		int index = 0;// 循环变量
		int count = 0;// 计数器
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 连接sftp服务器
		File file = createFile(fileNameCode, String.valueOf(num));
		PrintStream pStream = null;
		try {
			pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
			while (index < len) {
				PageData pd = list.get(index);
				String text = writeUserReportTxt(pd);
				pStream.append(text);
				index++;
				count++;
				// 记录数超过2000文件序号+1
				if (count == 2000 && index < len) {
					sftpUtil.upload(checkPath, file);
					num++;
					userReportFileNum += "#" + addZeroForNum(String.valueOf(num));
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
		sftpUtil.logout(); // 退出sftp服务器
		return userReportFileNum;
	}

	/**
	 * 编辑个人开户报备内容
	 * 
	 * @param pd
	 * @return
	 */
	private String writeUserReportTxt(PageData pd) {
		String text = mchnt_cd + "|" + pd.get("SERIAL_NUMBER") + "|" + pd.get("LOGIN_NAME") + "|" + pd.get("PHONE_CG")
				+ "|" + "" + "|" + pd.get("REAL_NAME") + "|" + pd.get("CARD_TYPE") + "|" + pd.get("CARD_NO") + "|"
				+ pd.get("SEX") + "|" + pd.get("PHONE") + "|" + "" + "|" + pd.get("ROLE") + "|" + pd.get("CREATE_DATE")
				+ "|" + account_id + "|" + "ADD|新开用户\r\n";
		return text;
	}

	// ==========================法人用户报备、核检==============================
	private void corpReport() {
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", TradeTypeEnum.CorpRegist.getValue());
		pd.put("create_date", date);
		List<TradeReport> corpTradeReportList = tradeReportService.selectTradeReport(pd);
		if (corpTradeReportList == null || corpTradeReportList.size() == 0) {
			String corpReportFileNum = "0000";
			corpReportUpload(corpReportFileNum);
		} else {
			TradeReport tradeReport = corpTradeReportList.get(0);
			String corpReportFileNum = tradeReport.getFile_num();
			String corpReportStatus = tradeReport.getReport_status();
			Long corpReportId = tradeReport.getId();
			if (corpReportStatus.equals(ReportStatusEnum.UP.getValue())) {
				logger.info("开始法人开户回盘文件核检");
				corpReportDownload(corpReportFileNum, corpReportId);
			} else {
				logger.info("法人开户报备结束");
			}
		}
	}

	/**
	 * 用户报备
	 */
	private void corpReportUpload(String corpReportFileNum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		PageData pd = new PageData();
		pd.put("trade_date", date);
		pd.put("message_id", TradeTypeEnum.CorpRegist.getValue());
		pd.put("trade_status", ResponseStatusEnum.Success.getValue());
		List<PageData> list = tradeReportService.selectCorps(pd);
		logger.info("法人用户新开：" + list.size());
		TradeReport tradeReport = new TradeReport();
		tradeReport.setReport_type(TradeTypeEnum.CorpRegist.getValue());
		tradeReport.setCreate_date(new Date());
		if (list.size() > 0) {
			int num = getFileNum(corpReportFileNum);// 文件序号
			corpReportFileNum = corporationWriteAndUpload(corpReportFileNum, list, "PW11", num);
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
		} else {
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
		}
		tradeReport.setFile_num(corpReportFileNum);
		tradeReportService.addTradeReport(tradeReport);
	}

	/**
	 * 用户补报
	 * 
	 * @param loginNameCGs
	 *            法人用户存管系统登录号
	 * 
	 */
	private void corpReportUpload(String corpReportFileNum, Long corpReportId, List<String> loginNameCGs) {
		List<PageData> list = getCorpRegistByLoginNameCGs(loginNameCGs);
		logger.info("法人用户补报：" + list.size());
		int num = getFileNum(corpReportFileNum);
		corpReportFileNum = corporationWriteAndUpload(corpReportFileNum, list, "PW11", num);
		TradeReport tradeReport = new TradeReport();
		tradeReport.setId(corpReportId);
		tradeReport.setFile_num(corpReportFileNum);
		tradeReportService.updateByPrimaryKeySelective(tradeReport);
	}

	/**
	 * 根据流水号集合查询新增法人用户，一次查询上限100条
	 * 
	 * @param serialNumbers
	 * @return
	 */
	private List<PageData> getCorpRegistByLoginNameCGs(List<String> loginNameCGs) {
		List<PageData> list = new ArrayList<>();
		int count = loginNameCGs.size();
		int index = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date = sdf.format(calendar.getTime());
		PageData pd = new PageData();
		pd.put("trade_date", date);
		pd.put("message_id", TradeTypeEnum.CorpRegist.getValue());
		pd.put("trade_status", ResponseStatusEnum.Success.getValue());
		while (count >= index * 100) {
			List<String> loginNameCGs1 = loginNameCGs.subList((index - 1) * 100, index * 100);
			pd.put("loginNameCGs", loginNameCGs1);
			List<PageData> list1 = tradeReportService.selectCorps(pd);
			list.addAll(list1);
			index++;
		}
		if (count > (index - 1) * 100) {
			List<String> loginNameCGs2 = loginNameCGs.subList((index - 1) * 100, count);
			pd.put("loginNameCGs", loginNameCGs2);
			List<PageData> list2 = tradeReportService.selectCorps(pd);
			list.addAll(list2);
		}
		return list;
	}

	/**
	 * 用户报备文件回盘核检
	 */
	private void corpReportDownload(String corpReportFileNum, Long corpReportId) {
		List<String> fileNameList = downLoadCorpReportFiles(corpReportFileNum);// 回盘文件名称列表
		if (fileNameList.size() == 0) {
			logger.info("法人开户报备文件等待核检...");
			return;
		}
		// 处理回盘文件
		List<String> loginNameCGs = new ArrayList<>();// 需要补报的法人用户存管系统登录帐号
		for (int i = 0; i < fileNameList.size(); i++) {
			List<String> strList = readFileByLines(fileNameList.get(i), 15);
			loginNameCGs.addAll(strList);
		}
		if (loginNameCGs.size() > 0) { // 回盘核检报错
			corpReportUpload(corpReportFileNum, corpReportId, loginNameCGs);
		} else { // 报备成功
			TradeReport tradeReport = new TradeReport();
			tradeReport.setId(corpReportId);
			tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		}
	}

	/**
	 * 下载法人开户报备回盘文件
	 * 
	 * @param corpReportFileNum
	 * @return 回盘文件名称
	 */
	private List<String> downLoadCorpReportFiles(String corpReportFileNum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date1 = sdf.format(new Date());
		String path = localOverCheckPath + date1 + "/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);
		sftpUtil.login();
		String[] fileNumArr = corpReportFileNum.split("#");
		List<String> fileNameList = new ArrayList<>();// 回盘文件名称列表
		logger.info("开始下载法人开户报备回盘文件...");
		for (int i = 0; i < fileNumArr.length; i++) {
			// 文件名称
			String downloadFile = date1 + "_P2P_PW11_" + date1 + "_" + fileNumArr[i] + ".txt";
			// 生成本地文件
			String saveFile = path + downloadFile;
			logger.info("法人开户回盘文件：" + downloadFile);
			if (sftpUtil.exist(overcheckPath, downloadFile)) {
				try {
					sftpUtil.download(overcheckPath, downloadFile, saveFile);
					logger.info("法人开户报备回盘文件：" + downloadFile + " 获取成功");
					fileNameList.add(saveFile);
				} catch (FileNotFoundException | SftpException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("回盘文件：" + downloadFile + "不存在");
				fileNameList = new ArrayList<>();
				break;
			}
		}
		logger.info("下载法人开户报备回盘文件结束");
		sftpUtil.logout();
		return fileNameList;
	}

	/**
	 * 生成本地法人平台开户报备文件并上传
	 * 
	 * @param list
	 * @param fileNameCode
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	private String corporationWriteAndUpload(String corpReportFileNum, List<PageData> list, String fileNameCode,
			int num) {
		int len = list.size();
		if (len > 0) {
			corpReportFileNum = addZeroForNum(String.valueOf(num));
		}
		int index = 0;// 循环变量
		int count = 0;// 计数器
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 连接sftp服务器
		File file = createFile(fileNameCode, String.valueOf(num));
		PrintStream pStream = null;
		try {
			pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
			while (index < len) {
				PageData pd = list.get(index);
				String text = writeCorpReportTxt(pd);
				pStream.append(text);
				index++;
				count++;
				// 记录数超过2000文件序号+1
				if (count == 2000 && index < len) {
					sftpUtil.upload(checkPath, file);
					num++;
					corpReportFileNum += "#" + addZeroForNum(String.valueOf(num));
					file = createFile(fileNameCode, String.valueOf(num));
					pStream = new PrintStream(new FileOutputStream(file), true, "GBK");
					count = 0;
				}
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sftpUtil.upload(checkPath, file);
		sftpUtil.logout(); // 退出sftp服务器
		return corpReportFileNum;
	}

	/**
	 * 编辑法人开户报备内容
	 * 
	 * @return
	 */
	private String writeCorpReportTxt(PageData pd) {
		String text = mchnt_cd + "|" + pd.get("SERIAL_NUMBER") + "|" + pd.get("ENTERPRISE_NAME_CG") + "|"
				+ pd.get("CREATE_DATE") + "|" + pd.get("REAL_NAME_CG") + "|" + pd.get("CARD_NO") + "|"
				+ pd.get("PHONE_CG") + "|" + "" + "|" + account_id + "|" + "ADD" + "|"
				+ pd.get("BUSINESS_REGISTRATION_NUMBER") + "|" + pd.get("REGISTRATION_CODE") + "|"
				+ pd.get("ORGANIZATION_CODE") + "|" + pd.get("ENTERPRISE_NAME") + "|" + pd.get("LOGIN_NAME_CG")
				+ "|企业开户|" + pd.get("ROLE") + "|" + "" + "|" + "" + "|" + pd.get("SOCIAL_CREDIT_CODE") + "\r\n";
		return text;
	}

	// ====================================公用的方法============================================
	/**
	 * 以行为单位读取文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param Index
	 *            行属性下标
	 */
	private List<String> readFileByLines(String fileName, int index) {
		File file = new File(fileName);
		BufferedReader reader = null;
		List<String> list = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] strArr = tempString.split("\\|");
				// 核检状态
				String str = strArr[strArr.length - 2];
				if (!str.equals("0000") && !str.equals("0003")) {
					// 核检失败，取有效字段，补报
					list.add(strArr[index - 1]);
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
	 * 创建.txt文件
	 * 
	 * @param localcheckPath
	 * @param fileNameCode
	 */
	private File createFile(String fileNameCode, String num) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now = sdf.format(new Date());
		String path = localCheckPath + now + "/";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String numStr = addZeroForNum(num);
		String fileName = "P2P_" + fileNameCode + "_" + now + "_" + numStr + ".txt";
		File file = new File(path + fileName);
		try {
			file.createNewFile();
			logger.info("生成新开户本地报备文件：" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
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

	// ===================================手动补报====================================
	// 个人用户
	@Override
	public ReportResult userReportText() {
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", TradeTypeEnum.UserRegist.getValue());
		pd.put("create_date", date);
		List<TradeReport> userTradeReportList = tradeReportService.selectTradeReport(pd);
		List<PageData> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date1 = sdf.format(calendar.getTime());
		if (null == userTradeReportList || userTradeReportList.size() == 0) {
			// 没有报备记录
			pd = new PageData();
			pd.put("message_id", TradeTypeEnum.UserRegist.getValue());
			pd.put("trade_status", ResponseStatusEnum.Success.getValue());
			pd.put("trade_date", date1);
			list = tradeReportService.selectUsers(pd);
			if (null == list || list.size() == 0) {
				TradeReport tradeReport = new TradeReport();
				tradeReport.setFile_num("0000");
				tradeReport.setReport_type(TradeTypeEnum.UserRegist.getValue());
				tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
				tradeReport.setCreate_date(new Date());
				tradeReportService.addTradeReport(tradeReport);
				reportResult.setMessage("个人开户报备结束");
				return reportResult;
			}
		} else {
			// 检查报备状态
			String reportStatus = userTradeReportList.get(0).getReport_status();
			if (ReportStatusEnum.UP.getValue().equals(reportStatus)) {
				String reportFileNum = userTradeReportList.get(0).getFile_num();
				List<String> fileNameList = downLoadUserReportFiles(reportFileNum);
				if (fileNameList.size() == 0) {
					reportResult.setMessage("个人开户报备文件等待核检...");
					return reportResult;
				}
				List<String> loginNames = new ArrayList<>();// 需要补报的个人用户平台登录账号
				for (int i = 0; i < fileNameList.size(); i++) {
					List<String> strList = readFileByLines(fileNameList.get(i), 3);
					loginNames.addAll(strList);
				}
				if (loginNames.size() == 0) {
					TradeReport tradeReport = new TradeReport();
					tradeReport.setId(userTradeReportList.get(0).getId());
					tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
					tradeReportService.updateByPrimaryKeySelective(tradeReport);
					reportResult.setMessage("个人开户报备文件核检成功");
					return reportResult;
				}
				// 根据loginname查询个人用户
				pd = new PageData();
				pd.put("message_id", TradeTypeEnum.UserRegist.getValue());
				pd.put("trade_status", ResponseStatusEnum.Success.getValue());
				pd.put("trade_date", date1);
				pd.put("loginNames", loginNames);
				list = tradeReportService.selectUsers(pd);
			} else {
				reportResult.setMessage("个人开户报备结束");
				return reportResult;
			}
		}
		String text = "";
		for (int i = 0; i < list.size(); i++) {
			pd = list.get(i);
			text += writeUserReportTxt(pd);
		}
		reportResult.setText(text);
		return reportResult;
	}

	@Override
	public ReportResult userReportUp(String text) {
		String[] textArr = text.split("\n");
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", TradeTypeEnum.UserRegist.getValue());
		pd.put("create_date", date);
		List<TradeReport> userTradeReportList = tradeReportService.selectTradeReport(pd);
		Long reportId = null;
		String fileNum = null;
		if (null != userTradeReportList && userTradeReportList.size() > 0) {
			reportId = userTradeReportList.get(0).getId();
			fileNum = userTradeReportList.get(0).getFile_num();
		}
		int num = getFileNum(fileNum);
		fileNum = addZeroForNum(String.valueOf(num));
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 连接sftp服务器
		File file = createFile("PW10", String.valueOf(num));
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
					file = createFile("PW10", String.valueOf(num));
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
		logger.info("个人开户手动报备文件上传成功");
		TradeReport tradeReport = new TradeReport();
		tradeReport.setFile_num(fileNum);
		if (null != userTradeReportList && userTradeReportList.size() > 0) {
			tradeReport.setId(reportId);
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		} else {
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
			tradeReport.setCreate_date(new Date());
			tradeReport.setReport_type(TradeTypeEnum.UserRegist.getValue());
			tradeReportService.addTradeReport(tradeReport);
		}
		reportResult.setMessage("个人开户手动报备上传成功，等待核检...");
		return reportResult;
	}

	// 法人用户
	@Override
	public ReportResult corpReportText() {
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", TradeTypeEnum.CorpRegist.getValue());
		pd.put("create_date", date);
		List<TradeReport> corpTradeReportList = tradeReportService.selectTradeReport(pd);
		List<PageData> list = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 得到前一天
		String date1 = sdf.format(calendar.getTime());
		if (null == corpTradeReportList || corpTradeReportList.size() == 0) {
			// 没有报备记录
			pd = new PageData();
			pd.put("trade_date", date1);
			pd.put("message_id", TradeTypeEnum.CorpRegist.getValue());
			pd.put("trade_status", ResponseStatusEnum.Success.getValue());
			list = tradeReportService.selectCorps(pd);
			if (null == list || list.size() == 0) {
				TradeReport tradeReport = new TradeReport();
				tradeReport.setFile_num("0000");
				tradeReport.setReport_type(TradeTypeEnum.CorpRegist.getValue());
				tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
				tradeReport.setCreate_date(new Date());
				tradeReportService.addTradeReport(tradeReport);
				reportResult.setMessage("法人开户报备结束");
				return reportResult;
			}
		} else {
			// 检查报备状态
			String reportStatus = corpTradeReportList.get(0).getReport_status();
			if (ReportStatusEnum.UP.getValue().equals(reportStatus)) {
				String reportFileNum = corpTradeReportList.get(0).getFile_num();
				List<String> fileNameList = downLoadCorpReportFiles(reportFileNum);
				if (fileNameList.size() == 0) {
					reportResult.setMessage("法人开户报备文件等待核检...");
					return reportResult;
				}
				List<String> loginNameCgs = new ArrayList<>();// 需要补报的个人用户平台登录账号
				for (int i = 0; i < fileNameList.size(); i++) {
					List<String> strList = readFileByLines(fileNameList.get(i), 15);
					loginNameCgs.addAll(strList);
				}
				if (loginNameCgs.size() == 0) {
					TradeReport tradeReport = new TradeReport();
					tradeReport.setId(corpTradeReportList.get(0).getId());
					tradeReport.setReport_status(ReportStatusEnum.OK.getValue());
					tradeReportService.updateByPrimaryKeySelective(tradeReport);
					reportResult.setMessage("法人开户报备文件核检成功");
					return reportResult;
				}
				// 根据loan_name_cg查询法人用户
				pd = new PageData();
				pd.put("trade_date", date1);
				pd.put("message_id", TradeTypeEnum.CorpRegist.getValue());
				pd.put("trade_status", ResponseStatusEnum.Success.getValue());
				pd.put("loginNameCGs", loginNameCgs);
				list = tradeReportService.selectCorps(pd);
			} else {
				reportResult.setMessage("法人开户报备结束");
				return reportResult;
			}
		}
		String text = "";
		for (int i = 0; i < list.size(); i++) {
			pd = list.get(i);
			text += writeCorpReportTxt(pd);
		}
		reportResult.setText(text);
		return reportResult;
	}

	@Override
	public ReportResult corpReportUp(String text) {
		String[] textArr = text.split("\n");
		ReportResult reportResult = new ReportResult();
		PageData pd = new PageData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		pd.put("report_type", TradeTypeEnum.CorpRegist.getValue());
		pd.put("create_date", date);
		Long reportId = null;
		String fileNum = null;
		List<TradeReport> corpTradeReportList = tradeReportService.selectTradeReport(pd);
		if (null != corpTradeReportList && corpTradeReportList.size() > 0) {
			reportId = corpTradeReportList.get(0).getId();
			fileNum = corpTradeReportList.get(0).getFile_num();
		}
		int num = getFileNum(fileNum);
		fileNum = addZeroForNum(String.valueOf(num));
		SFTPUtil sftpUtil = new SFTPUtil(host, port, username, password);// sftp上传工具
		sftpUtil.login();// 连接sftp服务器
		File file = createFile("PW11", String.valueOf(num));
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
					file = createFile("PW11", String.valueOf(num));
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
		logger.info("法人开户手动报备文件上传成功");
		TradeReport tradeReport = new TradeReport();
		tradeReport.setFile_num(fileNum);
		if (null != corpTradeReportList && corpTradeReportList.size() > 0) {
			tradeReport.setId(reportId);
			tradeReportService.updateByPrimaryKeySelective(tradeReport);
		} else {
			tradeReport.setReport_status(ReportStatusEnum.UP.getValue());
			tradeReport.setCreate_date(new Date());
			tradeReport.setReport_type(TradeTypeEnum.CorpRegist.getValue());
			tradeReportService.addTradeReport(tradeReport);
		}
		reportResult.setMessage("法人开户手动报备上传成功，等待核检...");
		return reportResult;
	}
}
