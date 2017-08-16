package com.Beans;

public class POSAuthority {

	private String settingName;
	private boolean isAdminHaveRights;
	private boolean isClerkHaveRights;
	private boolean isManagerHaveRights;
	private boolean isSuperVisorHaveRights;
	private boolean isSettingOverrideByManager;

	public POSAuthority() {
		super();
	}

	public POSAuthority(String settingName, boolean isAdminHaveRights,
			boolean isClerkHaveRights, boolean isManagerHaveRights,
			boolean isSuperVisorHaveRights, boolean isSettingOverrideByManager) {
		super();
		this.settingName = settingName;
		this.isAdminHaveRights = isAdminHaveRights;
		this.isClerkHaveRights = isClerkHaveRights;
		this.isManagerHaveRights = isManagerHaveRights;
		this.isSuperVisorHaveRights = isSuperVisorHaveRights;
		this.isSettingOverrideByManager = isSettingOverrideByManager;
	}

	public boolean isSettingOverrideByManager() {
		return isSettingOverrideByManager;
	}

	public void setSettingOverrideByManager(boolean isSettingOverrideByManager) {
		this.isSettingOverrideByManager = isSettingOverrideByManager;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public boolean isAdminHaveRights() {
		return isAdminHaveRights;
	}

	public void setAdminHaveRights(boolean isAdminHaveRights) {
		this.isAdminHaveRights = isAdminHaveRights;
	}

	public boolean isClerkHaveRights() {
		return isClerkHaveRights;
	}

	public void setClerkHaveRights(boolean isClerkHaveRights) {
		this.isClerkHaveRights = isClerkHaveRights;
	}

	public boolean isManagerHaveRights() {
		return isManagerHaveRights;
	}

	public void setManagerHaveRights(boolean isManagerHaveRights) {
		this.isManagerHaveRights = isManagerHaveRights;
	}

	public boolean isSuperVisorHaveRights() {
		return isSuperVisorHaveRights;
	}

	public void setSuperVisorHaveRights(boolean isSuperVisorHaveRights) {
		this.isSuperVisorHaveRights = isSuperVisorHaveRights;
	}

	@Override
	public String toString() {
		return "POSAuthority [settingName=" + settingName
				+ ", isAdminHaveRights=" + isAdminHaveRights
				+ ", isClerkHaveRights=" + isClerkHaveRights
				+ ", isManagerHaveRights=" + isManagerHaveRights
				+ ", isSuperVisorHaveRights=" + isSuperVisorHaveRights + "]";
	}
}
