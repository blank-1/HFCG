package com.xt.cfp.core.pojo;

/**
 * 员工角色
 */
public class AdminRole {
	private Long adminRoleId;//员工角色ID[主键]
	private Long roleId;//角色ID[外键]
	private Long adminId;//员工ID[外键]
	private Integer roleState;//角色状态
	
	/**
     * 状态（1.可用；2.不可用）
     */
    public static final int STATE_ENABLE = 1;
    public static final int STATE_DISABLE = 2;
    
	public Long getAdminRoleId() {
		return adminRoleId;
	}
	public void setAdminRoleId(Long adminRoleId) {
		this.adminRoleId = adminRoleId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public Integer getRoleState() {
		return roleState;
	}
	public void setRoleState(Integer roleState) {
		this.roleState = roleState;
	}
}
