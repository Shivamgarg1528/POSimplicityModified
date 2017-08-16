package com.Beans;

public class RoleInfo implements Comparable<RoleInfo>{

	private String roleName;
	private String rolePassword = "";
	private boolean isRoleActive;
	private String roleId;

	public RoleInfo(String roleName, String rolePassword, boolean isRoleActive,
			String roleId) {
		super();
		this.roleName = roleName;
		this.rolePassword = rolePassword;
		this.isRoleActive = isRoleActive;
		this.roleId = roleId;
	}



	public String getRoleId() {
		return roleId;
	}



	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}



	public RoleInfo() {}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRolePassword() {
		return rolePassword;
	}

	public void setRolePassword(String rolePassword) {
		this.rolePassword = rolePassword;
	}

	public boolean isRoleActive() {
		return isRoleActive;
	}

	public void setRoleActive(boolean isRoleActive) {
		this.isRoleActive = isRoleActive;
	}

	public boolean isRoleOk(){
		if(this != null && roleName != null && rolePassword != null)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "RoleInfo [roleName=" + roleName + ", rolePassword="
				+ rolePassword + ", isRoleActive=" + isRoleActive + "]";
	}



	@Override
	public int compareTo(RoleInfo another) {
		return Integer.parseInt(roleId) - Integer.parseInt(another.getRoleId());
	}
}
