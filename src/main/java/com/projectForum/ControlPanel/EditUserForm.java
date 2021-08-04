package com.projectForum.ControlPanel;

import java.time.LocalDateTime;


public class EditUserForm {

	private int id;
	private String username;
	private String email;
	private String role;
	private boolean keepActivity = true;
	private LocalDateTime joiningDate;
	
	public EditUserForm() {
	}

	public EditUserForm(int id, String username, String email, String role, boolean keepActivity,
			LocalDateTime joiningDate) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.keepActivity = keepActivity;
		this.joiningDate = joiningDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isKeepActivity() {
		return keepActivity;
	}

	public void setKeepActivity(boolean keepActivity) {
		this.keepActivity = keepActivity;
	}

	public LocalDateTime getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDateTime joiningDate) {
		this.joiningDate = joiningDate;
	}
	
	
}
