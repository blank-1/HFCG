package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.*;
import org.springframework.web.multipart.MultipartFile;

public interface EnterpriseFoundationSnapshotService {
    /**
     * 添加保理
     */
	EnterpriseFoundationSnapshot addEnterpriseFoundationSnapshot(EnterpriseFoundationSnapshot enterpriseFoundationSnapshot);

    /**
     * 根据保理ID加载一条数据
     */
	EnterpriseFoundationSnapshot getEnterpriseFoundationSnapshotById(Long enterpriseFoundationId);

    /**
     * 编辑保理
     */
	EnterpriseFoundationSnapshot editEnterpriseFoundationSnapshot(EnterpriseFoundationSnapshot enterpriseFoundationSnapshot);
	
	/**
	 * 保存企业保理，第一步
	 * @param loanApplication 借款申请
	 * @param enterpriseInfo 企业信息
	 * @param enterpriseFoundationSnapshot 企业保理快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication, EnterpriseInfo enterpriseInfo, EnterpriseFoundationSnapshot enterpriseFoundationSnapshot);
	
	/**
	 * 根据借款申请ID，加载一条保理信息
	 */
	EnterpriseFoundationSnapshot getByloanApplicationId(Long loanApplicationId);
	
	/**
	 * 根据借款申请(主)ID，加载一条保理信息
	 */
	EnterpriseFoundationSnapshot getByMainLoanApplicationId(Long mainLoanApplicationId);
	
	/**
	 * 保存企业保理，第二步
	 * @param loanApplication 借款申请
	 * @param enterpriseFoundationSnapshot 企业保理信息
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoanFoundation(MainLoanApplication loanApplication, EnterpriseFoundationSnapshot enterpriseFoundationSnapshot,MultipartFile importFile,MultipartFile importFileTrade,MultipartFile importFileRiskTip);

	/**
	 * 保存标的详情说明
	 * @param foundationSnapshot
	 * @param importFile
	 */
	void saveLoanApplicationDesc(MainLoanApplication mainLoanApplication,EnterpriseFoundationSnapshot foundationSnapshot, MultipartFile importFile);
	/**
	 * 保存交易说明书
	 * @param foundationSnapshot
	 * @param importFile
	 */
	void saveTradeBook(MainLoanApplication mainLoanApplication,EnterpriseFoundationSnapshot foundationSnapshot, MultipartFile importFile);

	/**
	 * 标的详情
	 * @param attachId
	 * @return
	 */
	Attachment getAttachmentById(Long attachId);

	/**
	 * 保存风险提示函
	 * @param foundationSnapshot
	 * @param importFileRiskTip
	 */
	void saveRiskTip(MainLoanApplication mainLoanApplication, EnterpriseFoundationSnapshot foundationSnapshot, MultipartFile importFileRiskTip);
}
