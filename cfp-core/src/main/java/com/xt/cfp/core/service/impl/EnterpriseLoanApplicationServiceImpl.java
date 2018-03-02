package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.Attachment;
import com.xt.cfp.core.pojo.CustomerUploadSnapshot;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.MainLoanApplicationService;
import com.xt.cfp.core.util.FileUtil;
import com.xt.cfp.core.util.ThumbnailGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class EnterpriseLoanApplicationServiceImpl implements
		EnterpriseLoanApplicationService {

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
    private LoanApplicationService loanApplicationService;
    
    @Autowired
    private MainLoanApplicationService mainLoanApplicationService;
	
	@Override
	public EnterpriseLoanApplication addEnterpriseLoanApplication(
			EnterpriseLoanApplication enterpriseLoanApplication) {
		myBatisDao.insert("ENTERPRISE_LOAN_APPLICATION.insert", enterpriseLoanApplication);
		return enterpriseLoanApplication;
	}

	@Override
	public EnterpriseLoanApplication getEnterpriseLoanApplicationById(
			Long enterpriseLoanApplicationId) {
		return myBatisDao.get("ENTERPRISE_LOAN_APPLICATION.selectByPrimaryKey", enterpriseLoanApplicationId);
	}

	@Override
	public EnterpriseLoanApplication editEnterpriseLoanApplication(
			EnterpriseLoanApplication enterpriseLoanApplication) {
		myBatisDao.update("ENTERPRISE_LOAN_APPLICATION.updateByPrimaryKey", enterpriseLoanApplication);
		return enterpriseLoanApplication;
	}

	@Override
	public EnterpriseLoanApplication getByLoanApplicationId(
			Long loanApplicationId) {
		return myBatisDao.get("ENTERPRISE_LOAN_APPLICATION.getByLoanApplicationId", loanApplicationId);
	}
	
	@Override
	public EnterpriseLoanApplication getByMainLoanApplicationId(
			Long mainLoanApplicationId) {
		return myBatisDao.get("ENTERPRISE_LOAN_APPLICATION.getByMainLoanApplicationId", mainLoanApplicationId);
	}
	
	/**
	 * 保存上传图片
	 */
	@Override
    public Map<String, Object> saveLoanUploadSnapshot(String loanApplicationId, MultipartFile file, 
    		String type, String msgName, String typeList, String rootPath, String isCode) throws IOException {
        if (loanApplicationId == null || "".equals(loanApplicationId))
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("loanApplicationId", loanApplicationId);

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
        int flagNum = getCustomerSeqNumByMainId(Long.valueOf(loanApplicationId), type);
        //保存
        CustomerUploadSnapshot cus = saveRelatedAccessories(msgName, imgPath, url.toString(), type, CustomerUploadSnapshot.CUSTOMERUPLOADSNAPSHOT_NOTDELETED, (long)flagNum + 1, Long.valueOf(loanApplicationId), thumbnailUrl.toString(), isCode);

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
	
    @Override
    public Attachment getAttachmentBycusId(Long cusId) {
        return myBatisDao.get("ATTACHMENT.getAttachmentBycusId", cusId);
    }
    
    @Override
    @Transactional
    public void delLoanImg(Long cusId, String status, Attachment atta, String rootPath) throws IOException {
        String fileName = atta.getUrl().substring(atta.getUrl().lastIndexOf("/"));
        String thumbnailFileName = atta.getThumbnailUrl().substring(atta.getThumbnailUrl().lastIndexOf("/"));
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
        myBatisDao.update("CUSTOMER_UPLOAD_SNAPSHOT.logicalDel", params);

    }
    
    @Override
    public CustomerUploadSnapshot getcustomerUploadSnapshotDetails(Long cusId) {
        return myBatisDao.get("CUSTOMER_UPLOAD_SNAPSHOT.selectByPrimaryKey", cusId);
    }
    
    @Override
    public List<CustomerUploadSnapshot> getcustomerUploadSnapshotList(
            Long loanApplicationId, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanApplicationId", loanApplicationId);
        params.put("type", type);
        return myBatisDao.getList("CUSTOMER_UPLOAD_SNAPSHOT.getcustomerUploadSnapshotList", params);
    }
    
    // main
    @Override
    public List<CustomerUploadSnapshot> getCustomerUploadSnapshotListByMainId(
            Long mainLoanApplicationId, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mainLoanApplicationId", mainLoanApplicationId);
        params.put("type", type);
        return myBatisDao.getList("CUSTOMER_UPLOAD_SNAPSHOT.getCustomerUploadSnapshotListByMainId", params);
    }
	
	/**
	 * 获得企业快照指定类型最大序号
	 */
    @Override
    public int getCustomerSeqNum(Long loanApplicationId, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loanApplicationId", loanApplicationId);
        map.put("type", type);
        return myBatisDao.get("CUSTOMER_UPLOAD_SNAPSHOT.getCustomerSeqNum", map);
    }
    
    // main
    @Override
    public int getCustomerSeqNumByMainId(Long mainLoanApplicationId, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mainLoanApplicationId", mainLoanApplicationId);
        map.put("type", type);
        return myBatisDao.get("CUSTOMER_UPLOAD_SNAPSHOT.getCustomerSeqNumByMainId", map);
    }
    
    /**
	 * 数据库保存图片数据
	 */
    @Override
    @Transactional
    public CustomerUploadSnapshot saveRelatedAccessories(String fileName, String imgPath, String url, String type,
        String status, Long seqNum, Long loanApplicationId, String thumbnailUrl, String isCode) {
    	MainLoanApplication mainLoanApplication = mainLoanApplicationService.findById(loanApplicationId);
        Attachment att = new Attachment();
        att.setType(type);
        att.setUrl(url);
        att.setPhysicalAddress(imgPath);
        att.setFileName(fileName);
        att.setThumbnailUrl(thumbnailUrl);
        att.setCreateTime(new Date());
        att.setIsCode(isCode);
        att.setUserId(mainLoanApplication.getUserId());
        myBatisDao.insert("ATTACHMENT.insert", att);

        CustomerUploadSnapshot ent = new CustomerUploadSnapshot();
        ent.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());
        ent.setSeqNum(seqNum);
        ent.setAttachId(att.getAttachId());
        ent.setStatus(status);
        ent.setType(type);
        myBatisDao.insert("CUSTOMER_UPLOAD_SNAPSHOT.insert", ent);
        return ent;
    }

}
