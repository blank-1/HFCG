package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.AttachmentIsCode;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.EnterpriseFoundationSnapshotService;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.MainLoanApplicationService;
import com.xt.cfp.core.util.FileUtil;
import com.xt.cfp.core.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class EnterpriseFoundationSnapshotServiceImpl implements EnterpriseFoundationSnapshotService{

	@Value(value = "${LOAN_DESC_PATH}")
	private String loanDescPath;
	@Value(value = "${ROOT_PATH}")
	private String rootP;

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	
	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	
	@Override
	public EnterpriseFoundationSnapshot addEnterpriseFoundationSnapshot(
			EnterpriseFoundationSnapshot enterpriseFoundationSnapshot) {
		myBatisDao.insert("ENTERPRISE_FOUNDATION_SNAPSHOT.insert", enterpriseFoundationSnapshot);
		return enterpriseFoundationSnapshot;
	}

	@Override
	public EnterpriseFoundationSnapshot getEnterpriseFoundationSnapshotById(
			Long enterpriseFoundId) {
		return myBatisDao.get("ENTERPRISE_FOUNDATION_SNAPSHOT.selectByPrimaryKey", enterpriseFoundId);
	}

	@Override
	public EnterpriseFoundationSnapshot editEnterpriseFoundationSnapshot(
			EnterpriseFoundationSnapshot enterpriseFoundationSnapshot) {
		myBatisDao.update("ENTERPRISE_FOUNDATION_SNAPSHOT.updateByPrimaryKey", enterpriseFoundationSnapshot);
		return enterpriseFoundationSnapshot;
	}

	/**
	 * 保存第一步：企业基金（main修改）
	 */
	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication,
			EnterpriseInfo enterpriseInfo,
			EnterpriseFoundationSnapshot enterpriseFoundationSnapshot) {
		
		// 借款申请 添加
		mainLoanApplication = mainLoanApplicationService.addMainLoanApplication(mainLoanApplication);
		
		// 关联表 添加
		EnterpriseLoanApplication enterpriseLoanApplication = new EnterpriseLoanApplication();
		enterpriseLoanApplication.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		enterpriseLoanApplication.setEnterpriseId(enterpriseInfo.getEnterpriseId());//企业ID
		enterpriseLoanApplicationService.addEnterpriseLoanApplication(enterpriseLoanApplication);
		
		// 保理快照表 添加
		enterpriseFoundationSnapshot.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		this.addEnterpriseFoundationSnapshot(enterpriseFoundationSnapshot);

		return mainLoanApplication;
	}

	@Override
	public EnterpriseFoundationSnapshot getByloanApplicationId(
			Long loanApplicationId) {
		return myBatisDao.get("ENTERPRISE_FOUNDATION_SNAPSHOT.getByloanApplicationId", loanApplicationId);
	}
	
	@Override
	public EnterpriseFoundationSnapshot getByMainLoanApplicationId(
			Long mainLoanApplicationId) {
		return myBatisDao.get("ENTERPRISE_FOUNDATION_SNAPSHOT.getByMainLoanApplicationId", mainLoanApplicationId);
	}

	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoanFoundation(MainLoanApplication mainLoanApplication, EnterpriseFoundationSnapshot enterpriseFoundationSnapshot,MultipartFile importFile,MultipartFile importFileTrade,MultipartFile importFileRiskTip){

		if (enterpriseFoundationSnapshot.getAttachId()==null){
			saveLoanApplicationDesc(mainLoanApplication,enterpriseFoundationSnapshot,importFile);
		}
		if (enterpriseFoundationSnapshot.getTradeBookId()==null){
			saveTradeBook(mainLoanApplication,enterpriseFoundationSnapshot,importFileTrade);
		}
		if (enterpriseFoundationSnapshot.getRiskTipId()==null){
			saveRiskTip(mainLoanApplication,enterpriseFoundationSnapshot,importFileRiskTip);
		}

		mainLoanApplication = mainLoanApplicationService.updateMainLoanApplication(mainLoanApplication);
		
		this.editEnterpriseFoundationSnapshot(enterpriseFoundationSnapshot);
		
		return mainLoanApplication;
	}

	@Override
	@Transactional
	public void saveLoanApplicationDesc(MainLoanApplication mainLoanApplication,EnterpriseFoundationSnapshot foundationSnapshot, MultipartFile importFile) {
		Attachment att = saveFileAttachment(mainLoanApplication, foundationSnapshot, importFile,"9");//表示标的详情
		foundationSnapshot.setAttachId(att.getAttachId());
	}

	@Override
	public void saveTradeBook(MainLoanApplication mainLoanApplication, EnterpriseFoundationSnapshot foundationSnapshot, MultipartFile importFile) {
		Attachment att1 = saveFileAttachment(mainLoanApplication, foundationSnapshot, importFile,"10");//交易说明书
		foundationSnapshot.setTradeBookId(att1.getAttachId());
	}

	@Override
	public void saveRiskTip(MainLoanApplication mainLoanApplication, EnterpriseFoundationSnapshot foundationSnapshot, MultipartFile importFile) {
		Attachment att1 = saveFileAttachment(mainLoanApplication, foundationSnapshot, importFile,"11");//风险提示函
		foundationSnapshot.setRiskTipId(att1.getAttachId());
	}


	private Attachment saveFileAttachment(MainLoanApplication mainLoanApplication, EnterpriseFoundationSnapshot foundationSnapshot, MultipartFile importFile,String type) {
		try {
			String rootPath = WebUtil.getHttpServletRequest().getSession().getServletContext().getRealPath("");
			String imgPath = loanDescPath;
			String fileName = importFile.getOriginalFilename();
			// 获取文件后缀
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			//新文件名字
			String newName = UUID.randomUUID().toString().replace("-", "") + suffix;

			StringBuffer url = new StringBuffer();
			url.append(imgPath.substring(imgPath.indexOf("/")));
			url.append("/" + newName);
			InputStream input = importFile.getInputStream();
			//上传图片
			new FileUtil(). save(input, rootPath + imgPath, File.separator + newName);

			Attachment att = new Attachment();
			att.setType(type);//表示标的详情
			att.setUrl(url.toString());
			att.setPhysicalAddress(imgPath);
			att.setFileName(fileName);
			att.setCreateTime(new Date());
			att.setIsCode(AttachmentIsCode.NO_CODE.getValue());
			att.setUserId(mainLoanApplication.getUserId());
			myBatisDao.insert("ATTACHMENT.insert", att);

			return att;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Attachment getAttachmentById(Long attachId) {
		if (attachId==null)
			return null;
		Attachment attachment = myBatisDao.get("ATTACHMENT.selectByPrimaryKey", attachId);
		return attachment;
	}

	public String getLoanDescPath() {
		return loanDescPath;
	}

	public void setLoanDescPath(String loanDescPath) {
		this.loanDescPath = loanDescPath;
	}
}
