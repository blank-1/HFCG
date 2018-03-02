package com.xt.cfp.core.pojo;

import com.xt.cfp.core.util.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户表
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;//用户ID
    private String loginName;//登录名
    private String loginPass;//登录密码
    private String bidPass;//交易密码
    private String mobileNo;//手机号
    private String source;//注册来源
    private Date createTime;//注册时间
    private String type;//用户类型
    private String status;//用户状态

    //辅助字段
    private Date lastLoginTime;//最近登录时间
    private String lastLoginTimeStr;//最近登录时间
    private String encryptMobileNo;//加密后的手机号
    private String appSource;//APP接口跳入微信来源(2IOS;3ANDROID)
    
    
    private String blance ;//账户余额
    private String ManageCount ;//投资总额
    private String withDrawCount ;//提现总额
//    private String lastLoginTime ;//最后登录时间
    private String lastManageTime ;//最后投资时间
    private String realName ;//真实姓名
    private String icCode ;//邀请码
    private String firstManageTime;//首投时间
    private String franchiseName;//加盟商名称
    
    
    public String getFranchiseName() {
		return franchiseName;
	}

	public void setFranchiseName(String franchiseName) {
		this.franchiseName = franchiseName;
	}

	public String getBlance() {
		return blance;
	}

	public void setBlance(String blance) {
		this.blance = blance;
	}

	public String getManageCount() {
		return ManageCount;
	}

	public void setManageCount(String manageCount) {
		ManageCount = manageCount;
	}

	public String getWithDrawCount() {
		return withDrawCount;
	}

	public void setWithDrawCount(String withDrawCount) {
		this.withDrawCount = withDrawCount;
	}

	public String getLastManageTime() {
		return lastManageTime;
	}

	public void setLastManageTime(String lastManageTime) {
		this.lastManageTime = lastManageTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIcCode() {
		return icCode;
	}

	public void setIcCode(String icCode) {
		this.icCode = icCode;
	}

	public String getFirstManageTime() {
		return firstManageTime;
	}

	public void setFirstManageTime(String firstManageTime) {
		this.firstManageTime = firstManageTime;
	}

	
    
    
    
    
    

    public String getEncryptMobileNo() {
        if (this.getMobileNo() == null || this.getMobileNo() == "") {
            return null;
        }
        return StringUtils.getEncryptMobileNo(this.getMobileNo());
    }

    public void setEncryptMobileNo(String encryptMobileNo) {
        this.encryptMobileNo = encryptMobileNo;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        setLastLoginTimeStr(sdf.format(lastLoginTime));
    }

    public String getLastLoginTimeStr() {
        return lastLoginTimeStr;
    }

    public void setLastLoginTimeStr(String lastLoginTimeStr) {
        this.lastLoginTimeStr = lastLoginTimeStr;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass == null ? null : loginPass.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBidPass() {
        return bidPass;
    }

    public void setBidPass(String bidPass) {
        this.bidPass = bidPass;
    }

	public String getAppSource() {
		return appSource;
	}

	public void setAppSource(String appSource) {
		this.appSource = appSource;
	}


}
