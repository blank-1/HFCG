package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * Created by lenovo on 2015/10/14.
 */
public class SalesUserInfo extends UserInfo {

    //用户名
    private String realName;
    //最后登陆时间
    private Date lastLoginDate;
    //员工Id
    private Long adminId;
    //员工编号
    private String salesAdminCode;
    //登录名
    private String salesAdminLoginName;
    //员工姓名
    private String salesAdminName;
    //身份证号
    private String idCardNo;
    //上级组织节点
    private String superiorsOrganize;
    //团队领导
    private String superiorsAdmin;

    public String getIdCardNoStr(){
        String idCardNo = this.getIdCardNo();
        if (StringUtils.isEmpty(idCardNo))
            return null;
        return com.xt.cfp.core.util.StringUtils.getEncryptIdCard(idCardNo);
    }

    public String getMobileNoStr(){
        String mobileNo = this.getMobileNo();
        if (StringUtils.isEmpty(mobileNo))
            return null;
        return com.xt.cfp.core.util.StringUtils.getEncryptMobileNo(mobileNo);
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getSuperiorsOrganize() {
        return superiorsOrganize;
    }

    public void setSuperiorsOrganize(String superiorsOrganize) {
        this.superiorsOrganize = superiorsOrganize;
    }

    public String getSuperiorsAdmin() {
        return superiorsAdmin;
    }

    public void setSuperiorsAdmin(String superiorsAdmin) {
        this.superiorsAdmin = superiorsAdmin;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getSalesAdminCode() {
        return salesAdminCode;
    }

    public void setSalesAdminCode(String salesAdminCode) {
        this.salesAdminCode = salesAdminCode;
    }

    public String getSalesAdminName() {
        return salesAdminName;
    }

    public void setSalesAdminName(String salesAdminName) {
        this.salesAdminName = salesAdminName;
    }

	public String getSalesAdminLoginName() {
		return salesAdminLoginName;
	}

	public void setSalesAdminLoginName(String salesAdminLoginName) {
		this.salesAdminLoginName = salesAdminLoginName;
	}
}
