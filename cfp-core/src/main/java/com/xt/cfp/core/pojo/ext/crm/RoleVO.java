package com.xt.cfp.core.pojo.ext.crm;

public class RoleVO {
	
	/*private CrmRole role;
	private List<CrmFunction> funcs;*/
	
	private Long roleId;

    private String roleName;

    private String roleDesc;

    private String scope;
    
    private String funcs;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getFuncs() {
		return funcs;
	}

	public void setFuncs(String funcs) {
		this.funcs = funcs;
	}
}
