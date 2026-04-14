package com.cosmeticssafety.dto;

public class UserAccountStatusUpdateRequest {

	private boolean enabled;
	private boolean accountLocked;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
}
