package com.xt.cfp.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.xt.cfp.core.pojo.Attachment;
import com.xt.cfp.core.pojo.CustomerUploadSnapshot;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;

public interface EnterpriseLoanApplicationService {
    /**
     * 添加关联
     */
	EnterpriseLoanApplication addEnterpriseLoanApplication(EnterpriseLoanApplication enterpriseLoanApplication);

    /**
     * 根据关联ID加载一条数据 
     */
	EnterpriseLoanApplication getEnterpriseLoanApplicationById(Long enterpriseLoanApplicationId);

    /**
     * 编辑关联
     */
	EnterpriseLoanApplication editEnterpriseLoanApplication(EnterpriseLoanApplication enterpriseLoanApplication);
	
	/**
	 * 根据借款申请ID，加载一条关联数据
	 */
	EnterpriseLoanApplication getByLoanApplicationId(Long loanApplicationId);
	
	/**
	 * 根据借款申请【主】ID，加载一条关联数据
	 */
	EnterpriseLoanApplication getByMainLoanApplicationId(Long mainLoanApplicationId);

	/**
	 * 保存上传图片
	 */
	Map<String, Object> saveLoanUploadSnapshot(String loanApplicationId,
			MultipartFile file, String type, String msgName, String typeList,
			String rootPath, String isCode) throws IOException;

	/**
	 * 获得企业快照最大序号
	 */
	int getCustomerSeqNum(Long loanApplicationId, String type);
	
	/**
	 * 获得企业快照最大序号(main)
	 */
	int getCustomerSeqNumByMainId(Long mainLoanApplicationId, String type);

	/**
	 * 数据库保存图片数据
	 */
	CustomerUploadSnapshot saveRelatedAccessories(String fileName,
			String imgPath, String url, String type, String status,
			Long seqNum, Long loanApplicationId, String thumbnailUrl,
			String isCode);

	/**
	 * 根据上传快照ID，获取一条附件信息
	 */
	Attachment getAttachmentBycusId(Long cusId);

	/**
	 * 删除图片操作
	 */
	void delLoanImg(Long cusId, String status, Attachment atta, String rootPath)
			throws IOException;

	/**
	 * 根据上传快照ID，加载一条快照信息
	 */
	CustomerUploadSnapshot getcustomerUploadSnapshotDetails(Long cusId);

	/**
	 * 根据借款申请ID，借款类型，加载上传快照列表
	 */
	List<CustomerUploadSnapshot> getcustomerUploadSnapshotList(
			Long loanApplicationId, String type);
	
	//by main
	List<CustomerUploadSnapshot> getCustomerUploadSnapshotListByMainId(
            Long mainLoanApplicationId, String type);

}
