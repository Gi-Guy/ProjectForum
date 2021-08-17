package com.projectForum.REST;

public class DeleteUserForm {
	private int userId = 0;
	private String username = "";
	private boolean keepActivity = true;
	
	public DeleteUserForm() {
	}

	public DeleteUserForm(int userId, String username, boolean keepActivity) {
		this.userId = userId;
		this.username = username;
		this.keepActivity = keepActivity;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isKeepActivity() {
		return keepActivity;
	}

	public void setKeepActivity(boolean keepActivity) {
		this.keepActivity = keepActivity;
	}
	
	
	

	
}
