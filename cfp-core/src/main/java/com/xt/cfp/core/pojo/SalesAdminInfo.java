package com.xt.cfp.core.pojo;

import java.util.Date;

public class SalesAdminInfo {
    private Long adminId;

    private String displayName;

    private String adminCode;

    private String loginName;

    private String loginPwd;

    private Date createTime;

    private Integer adminState;

    private String telephone;

    private String email;

    private String adminIdCode;

    private Date updateTime;

    //辅组字段
    private String roleName;


    /**
     * 状态（1.可用；2.不可用）
     */
    public static final int STATE_ENABLE = 1;
    public static final int STATE_DISABLE = 2;

    /**
     * SESSION中记录的当前用户信息:
     *
     * LOGINUSER ：记录当前用户对象
     * CURRENTROLE ：记录当前角色对象
     * USERFUNCTIONURL : 当前用户权限url
     * CURRENTPERMISSION ：记录当前角色对应的权限code集
     */
    public static final String CURRENTROLE = "currentRole";
    public static final String CURRENTPERMISSION = "currentPermission";


    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode == null ? null : adminCode.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd == null ? null : loginPwd.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAdminState() {
        return adminState;
    }

    public void setAdminState(Integer adminState) {
        this.adminState = adminState;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getAdminIdCode() {
        return adminIdCode;
    }

    public void setAdminIdCode(String adminIdCode) {
        this.adminIdCode = adminIdCode == null ? null : adminIdCode.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}