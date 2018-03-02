package com.xt.cfp.core.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工
 */
public class AdminInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long adminId;//员工ID[主键]
	private String displayName;//员工名称
	private String adminCode;//工号
	private String loginName;//员工登录名
	private String loginPwd;//登录密码
	private Date createTime;//创建时间
	private Integer adminState;//员工状态
	private String telephone;//手机号
	private String email;//邮箱
	private String adminIdCode;//身份证号
	private Date updateTime;//最后更改时间
	
	/**
	 * 辅助字段。
	 */
	private String roleName;// 角色名称

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
    public static final String LOGINUSER = "loginUser";
    public static final String CURRENTROLE = "currentRole";
    public static final String USERFUNCTIONURL = "userFunctionUrl";
    public static final String CURRENTPERMISSION = "currentPermission";
    
	/**
	 * 系统初化化默认密码
	 */
	public static final String DEFAULT_PASSWORD = "123456";
    
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
		this.displayName = displayName;
	}
	public String getAdminCode() {
		return adminCode;
	}
	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
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
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdminIdCode() {
		return adminIdCode;
	}
	public void setAdminIdCode(String adminIdCode) {
		this.adminIdCode = adminIdCode;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
