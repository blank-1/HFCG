package com.xt.cfp.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.xt.cfp.core.pojo.Attachment;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterpriseUploadSnapshot;
import com.xt.cfp.core.pojo.QuotaRecord;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.WithDraw;
import com.xt.cfp.core.pojo.ext.EnterpriseInfoExt;
import com.xt.cfp.core.pojo.ext.EnterpriseUploadSnapshotVO;
import com.xt.cfp.core.util.Pagination;

public interface EnterpriseInfoService {

	/**
	 * 企业分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	Pagination<EnterpriseInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 添加企业
     * @param enterpriseInfo 企业对象
     */
	EnterpriseInfo addEnterpriseInfo(EnterpriseInfo enterpriseInfo);
	
    /**
     * 添加企业并创建用户
     * @param enterpriseInfo 企业对象
     */
	EnterpriseInfo addEnterpriseAndUser(EnterpriseInfo enterpriseInfo, UserInfo user);

    /**
     * 根据企业ID加载一条数据 
     * @param enterpriseId 企业ID
     */
	EnterpriseInfo getEnterpriseById(Long enterpriseId);

    /**
     * 编辑企业信息
     * @param enterpriseInfo 企业对象
     */
	EnterpriseInfo editEnterprise(EnterpriseInfo enterpriseInfo);
	
	/**
	 * 更改企业额度
	 * @param enterpriseInfo 企业对象
	 * @param quotaRecord 额度对象
	 */
	void editQuotaRecord(EnterpriseInfo enterpriseInfo, QuotaRecord quotaRecord);

	/**
	 * 保存上传图片
	 */
	Map<String, Object> saveUploadSnapshot(String enterpriseId,
			MultipartFile file, String type, String msgName, String typeList,
			String rootPath, String isCode) throws IOException;

	/**
	 * 获得企业快照指定类型最大序号
	 */
	int getCustomerSeqNum(Long enterpriseId, String type);

	/**
	 * 数据库保存图片数据
	 */
	EnterpriseUploadSnapshot saveRelatedAccessories(String fileName,
			String imgPath, String url, String type, String status,
			Integer seqNum, Long enterpriseId, String thumbnailUrl,
			String isCode);
	
	/**
	 * 根据ID记载一条附件
	 */
	Attachment getAttachmentByentId(Long cusId);

	/**
	 * 删除一条附件
	 */
	void delImg(Long cusId, String status, Attachment atta, String rootPath)
			throws IOException;
	
	/**
	 * 根据ID加载一条企业快照信息
	 */
	EnterpriseUploadSnapshot getEnterpriseUploadSnapshotDetails(Long cusId);

	/**
	 * 根据企业ID和类型，加载快照列表
	 */
	List<EnterpriseUploadSnapshot> getEnterpriseUploadSnapshotList(
			Long enterpriseId, String type);
	
	/**
	 * 根据企业ID获取，快照及附件列表
	 */
	List<EnterpriseUploadSnapshotVO> getEnterpriseUploadAttachment(
			Long enterpriseId, String isCode);
	
	/**
	 * 编辑快照信息
	 */
	EnterpriseUploadSnapshot editEnterpriseUploadSnapshot(EnterpriseUploadSnapshot enterpriseUploadSnapshot);
	
	/**
	 * 获取所有企业信息
	 */
	List<EnterpriseInfo> getAllEnterpriseInfo(EnterpriseInfo enterpriseInfo);
	
	/**
	 * 获取企业详情信息
	 */
	EnterpriseInfoExt getEnterpriseInfoDetail(Long enterpriseId);

	/**
	 * 企业充值
	 */
	RechargeOrder enterpriseRecharge(RechargeOrder rechargeOrder);
	
	/**
     * 企业提现申请
     */
    WithDraw enterpriseWithDraw(WithDraw withDraw);
    
    /**
     * 根据附件ID，加载一条数据
     */
    Attachment getAttachmentById(Long attachId);
    
    /**
     * 更改附件信息
     */
    Attachment updateAttachment(Attachment attachment);
    
    /**
     * 更改企业上传快照，及附件信息
     */
    EnterpriseUploadSnapshot updateEnterpriseUploadSnapshot(EnterpriseUploadSnapshot enterpriseUploadSnapshot, Attachment attachment);

	/**
	 * 根据账户类型id查询企业
	 * @param accTypeUpdate
	 * @return
	 */
    EnterpriseInfo getEnterpriseByPlatId(String accTypeUpdate);
}
