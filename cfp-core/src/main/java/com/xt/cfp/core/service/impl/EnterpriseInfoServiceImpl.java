package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.Exception.code.ext.WithDrawErrorCode;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.EnterpriseInfoExt;
import com.xt.cfp.core.pojo.ext.EnterpriseUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.FileUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.ThumbnailGenerate;
import com.xt.cfp.core.util.WebUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class EnterpriseInfoServiceImpl implements EnterpriseInfoService {
	
	private static Logger logger = Logger.getLogger(EnterpriseInfoServiceImpl.class);
	
	@Autowired
	private MyBatisDao myBatisDao;
	
    @Value(value = "${DELETE_PATH}")
    private String deletePath;
    @Value(value = "${FORMAL_PATH}")
    private String formalPath;
    @Value(value = "${TEMPORARY_PATH}")
    private String temporaryPath;
    @Value(value = "${ROOT_PATH}")
    private String rootP;
    
    public String getDeletePath() {
        return deletePath;
    }

    public void setDeletePath(String deletePath) {
        this.deletePath = deletePath;
    }

    public String getFormalPath() {
        return formalPath;
    }

    public void setFormalPath(String formalPath) {
        this.formalPath = formalPath;
    }

    public String getTemporaryPath() {
        return temporaryPath;
    }

    public void setTemporaryPath(String temporaryPath) {
        this.temporaryPath = temporaryPath;
    }
    
    @Autowired
    private QuotaRecordService quotaRecordService;
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private RechargeOrderService rechargeOrderService;
    
    @Autowired
    private UserAccountService userAccountService;
    
    @Autowired
    private WithDrawService withDrawService;
    
    @Autowired
    private UserInfoExtService userInfoExtService;
    
	/**
	 * 企业分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	@Override
	public Pagination<EnterpriseInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<EnterpriseInfo> pagination = new Pagination<EnterpriseInfo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<EnterpriseInfo> enterpriseInfos = myBatisDao.getListForPaging("findAllEnterpriseInfoByPage", params, pageNo, pageSize);
        pagination.setRows(enterpriseInfos);
        pagination.setTotal(myBatisDao.count("findAllEnterpriseInfoByPage", params));
        return pagination;
	}
	
    /**
     * 添加企业
     * @param enterpriseInfo 企业对象
     */
	@Override
	public EnterpriseInfo addEnterpriseInfo(EnterpriseInfo enterpriseInfo){
		myBatisDao.insert("ENTERPRISE_INFO.insert", enterpriseInfo);
		return enterpriseInfo;
	}
	
    /**
     * 根据企业ID加载一条数据 
     * @param enterpriseId 企业ID
     */
	@Override
	public EnterpriseInfo getEnterpriseById(Long enterpriseId){
		return myBatisDao.get("ENTERPRISE_INFO.selectByPrimaryKey", enterpriseId);
	}
	
    /**
     * 编辑企业信息
     * @param enterpriseInfo 企业对象
     */
	@Override
	public EnterpriseInfo editEnterprise(EnterpriseInfo enterpriseInfo){
		myBatisDao.update("ENTERPRISE_INFO.updateByPrimaryKey", enterpriseInfo);
		return enterpriseInfo;
	}
	
	/**
	 * 保存上传图片
	 */
	@Override
    public Map<String, Object> saveUploadSnapshot(String enterpriseId, MultipartFile file, String type, String msgName, String typeList, String rootPath, String isCode) throws IOException {
        if (enterpriseId == null || "".equals(enterpriseId))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("enterpriseId", enterpriseId);

        String imgPath = temporaryPath;
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //新文件名字
        String newName = UUID.randomUUID().toString().replace("-", "") + suffix;
        String ThumbnailName = newName.substring(0, newName.lastIndexOf(".")) + "_100" + suffix;
        // 保存attachment 大图地址
        StringBuffer url = new StringBuffer();
        url.append(imgPath.substring(imgPath.indexOf("/")));
        url.append("/" + newName);
        //小图地址
        StringBuffer thumbnailUrl = new StringBuffer();
        thumbnailUrl.append(imgPath.substring(imgPath.indexOf("/")));
        thumbnailUrl.append("/" + ThumbnailName);
        InputStream input = file.getInputStream();
        //上传图片
        new FileUtil().save(input, rootPath + imgPath, File.separator + newName);
        //生成缩略图
        ThumbnailGenerate generate = new ThumbnailGenerate(rootPath + imgPath + File.separator + newName);
        generate.resizeFix(152, 202);
        //获得显示位置
        int flagNum = getCustomerSeqNum(Long.valueOf(enterpriseId), type);
        //保存
        EnterpriseUploadSnapshot cus = saveRelatedAccessories(msgName, imgPath, url.toString(), type, EnterpriseUploadSnapshot.STATUS_ENABLED, flagNum + 1, Long.valueOf(enterpriseId), thumbnailUrl.toString(), isCode);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", type);
        map.put("msgName", msgName);
        map.put("resultState", "success");
        map.put("typeList", typeList);
        map.put("cusId", cus.getSnapshotId());
        map.put("url", url);
        map.put("id", newName.substring(0, newName.lastIndexOf(".")));
        return map;
    }
	
	/**
	 * 获得企业快照指定类型最大序号
	 */
    @Override
    public int getCustomerSeqNum(Long enterpriseId, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("enterpriseId", enterpriseId);
        map.put("type", type);
        return myBatisDao.get("ENTERPRISE_UPLOAD_SNAPSHOT.getEnterpriseSeqNum", map);
    }
    
	/**
	 * 数据库保存图片数据
	 */
    @Override
    @Transactional
    public EnterpriseUploadSnapshot saveRelatedAccessories(String fileName, String imgPath, String url, String type,
                                                         String status, Integer seqNum, Long enterpriseId, String thumbnailUrl, String isCode) {
        Attachment att = new Attachment();
        att.setType(type);
        att.setUrl(url);
        att.setPhysicalAddress(imgPath);
        att.setFileName(fileName);
        att.setThumbnailUrl(thumbnailUrl);
        att.setCreateTime(new Date());
        att.setIsCode(isCode);
        myBatisDao.insert("ATTACHMENT.insert", att);

        EnterpriseUploadSnapshot ent = new EnterpriseUploadSnapshot();
        ent.setEnterpriseId(enterpriseId);
        ent.setSeqNum(seqNum);
        ent.setAttachId(att.getAttachId());
        ent.setStatus(status);
        ent.setType(type);
        myBatisDao.insert("ENTERPRISE_UPLOAD_SNAPSHOT.insert", ent);
        return ent;
    }
    
	/**
	 * 根据ID记载一条附件
	 */
    @Override
    public Attachment getAttachmentByentId(Long cusId) {
        return myBatisDao.get("ATTACHMENT.getAttachmentByentId", cusId);
    }
    
	/**
	 * 删除一条附件
	 */
    @Override
    @Transactional
    public void delImg(Long cusId, String status, Attachment atta, String rootPath) throws IOException {
        String fileName = atta.getUrl().substring(atta.getUrl().lastIndexOf("/"));
        String thumbnailFileName = atta.getThumbnailUrl().substring(atta.getThumbnailUrl().lastIndexOf("/"));
        
        logger.info("读取图片的地址：" + rootPath + this.temporaryPath + fileName);
        logger.info("目的地址：" + rootPath + this.deletePath);
        
        FileUtil fileUtil = new FileUtil();
        fileUtil.moveFile(rootPath + this.temporaryPath, fileName, rootPath + this.deletePath);
        fileUtil.moveFile(rootPath + this.temporaryPath, thumbnailFileName, rootPath + this.deletePath);
        fileUtil.deleteFile(rootPath + this.temporaryPath, fileName);
        fileUtil.deleteFile(rootPath + this.temporaryPath, thumbnailFileName);

        atta.setUrl(this.deletePath + fileName);
        atta.setThumbnailUrl(this.deletePath + thumbnailFileName);
        atta.setPhysicalAddress(this.deletePath);
        myBatisDao.update("ATTACHMENT.updateByPrimaryKeySelective", atta);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cusId", cusId);
        params.put("status", status);
        myBatisDao.update("ENTERPRISE_UPLOAD_SNAPSHOT.logicalDel", params);

    }
    
	/**
	 * 根据ID加载一条企业快照信息
	 */
    @Override
    public EnterpriseUploadSnapshot getEnterpriseUploadSnapshotDetails(Long cusId) {
        return myBatisDao.get("ENTERPRISE_UPLOAD_SNAPSHOT.selectByPrimaryKey", cusId);
    }
    
	/**
	 * 根据企业ID和类型，加载快照列表
	 */
    @Override
    public List<EnterpriseUploadSnapshot> getEnterpriseUploadSnapshotList(Long enterpriseId, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("enterpriseId", enterpriseId);
        params.put("type", type);
        return myBatisDao.getList("ENTERPRISE_UPLOAD_SNAPSHOT.getEnterpriseUploadSnapshotList", params);
    }
    
	/**
	 * 根据企业ID获取，快照及附件列表
	 */
    @Override
    public List<EnterpriseUploadSnapshotVO> getEnterpriseUploadAttachment(Long enterpriseId, String isCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("enterpriseId", enterpriseId);
        params.put("isCode", isCode);
        return myBatisDao.getList("ENTERPRISE_UPLOAD_SNAPSHOT.getEnterpriseUploadAttachment", params);
    }

	/**
	 * 编辑快照信息
	 */
	@Override
	public EnterpriseUploadSnapshot editEnterpriseUploadSnapshot(
			EnterpriseUploadSnapshot enterpriseUploadSnapshot) {
		myBatisDao.update("ENTERPRISE_UPLOAD_SNAPSHOT.updateByPrimaryKey", enterpriseUploadSnapshot);
		return enterpriseUploadSnapshot;
	}

	@Override
	public List<EnterpriseInfo> getAllEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
		return myBatisDao.getList("ENTERPRISE_INFO.getAllEnterpriseInfo",enterpriseInfo);
	}

	@Override
	@Transactional
	public void editQuotaRecord(EnterpriseInfo enterpriseInfo,
			QuotaRecord quotaRecord) {
		this.editEnterprise(enterpriseInfo);
		quotaRecordService.addQuotaRecord(quotaRecord);
	}

	@Override
	@Transactional
	public EnterpriseInfo addEnterpriseAndUser(EnterpriseInfo enterpriseInfo, UserInfo user) {
		
		// 用户信息
    	user.setType(UserType.ENTERPRISE.getValue());//设置为企业虚拟用户
        user = userInfoService.regist(user, UserSource.PLATFORM);//来自平台
        
        // 企业信息
        enterpriseInfo.setUserId(user.getUserId());
        enterpriseInfo = this.addEnterpriseInfo(enterpriseInfo);
        
        // 根据新生成的用户ID，加载一条用户扩展信息
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(user.getUserId());
        userInfoExt.setRealName(enterpriseInfo.getEnterpriseName());// 将企业名称默认赋值给该企业的虚拟账户的真实姓名
        userInfoExtService.updateUserInfoExt(userInfoExt);
        
		return enterpriseInfo;
	}

	@Override
	public EnterpriseInfoExt getEnterpriseInfoDetail(Long enterpriseId) {
		return myBatisDao.get("ENTERPRISE_INFO.getEnterpriseInfoDetail", enterpriseId);
	}
	
    @Override
    @Transactional
    public RechargeOrder enterpriseRecharge(RechargeOrder rechargeOrder) {
        //判断参数是否为null
        if (rechargeOrder == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("sourceRecharge", "null");

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
        if(sessionAdminInfo==null){
            throw new SystemException(UserErrorCode.LONGIN_EXIST);
        }
        rechargeOrder.setAdminId(sessionAdminInfo.getAdminId());
        //企业充值
        rechargeOrder = rechargeOrderService.recharge(rechargeOrder, AccountConstants.AccountChangedTypeEnum.ENTERPRISE_SOURCE);
        return rechargeOrder;

    }

	@Override
	@Transactional
	public WithDraw enterpriseWithDraw(WithDraw withDraw) {
		//判断参数是否为null
        if (withDraw == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("withDraw", "null");
        if (withDraw.getWithdrawAmount() == null)
            throw new SystemException(WithDrawErrorCode.WITHDRAW_AMOUNT).set("withDrawAmount", "null");
        //提现金额的合法性判断
        UserAccount userAccount = userAccountService.getCashAccount(withDraw.getUserId());
        if (userAccount.getAvailValue().compareTo(withDraw.getWithdrawAmount())<0){
            throw new SystemException(WithDrawErrorCode.WITHDRAW_OWER_FLOW).set("availValue", userAccount.getAvailValue()).set("withDrawAmout",withDraw.getWithdrawAmount());
        }

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
        if(sessionAdminInfo==null){
            throw new SystemException(UserErrorCode.LONGIN_EXIST);
        }
        withDraw.setOperatorId(sessionAdminInfo.getAdminId());
        //第三方提现
        WithDraw _withDraw = withDrawService.withDrawByThirdPart((WithDrawExt) withDraw, AccountConstants.AccountChangedTypeEnum.ENTERPRISE_SOURCE);
        return _withDraw;
	}

	@Override
	public Attachment getAttachmentById(Long attachId) {
		return myBatisDao.get("ATTACHMENT.selectByPrimaryKey", attachId);
	}

	@Override
	public Attachment updateAttachment(Attachment attachment) {
		myBatisDao.update("ATTACHMENT.updateByPrimaryKey", attachment);
		return attachment;
	}

	@Override
	@Transactional
	public EnterpriseUploadSnapshot updateEnterpriseUploadSnapshot(
			EnterpriseUploadSnapshot enterpriseUploadSnapshot, Attachment attachment) {
		this.editEnterpriseUploadSnapshot(enterpriseUploadSnapshot);
		this.updateAttachment(attachment);
		return enterpriseUploadSnapshot;
	}

    @Override
    public EnterpriseInfo getEnterpriseByPlatId(String accTypeUpdate) {
        return this.myBatisDao.get("getEnterpriseByPlatId",accTypeUpdate);
    }
}
