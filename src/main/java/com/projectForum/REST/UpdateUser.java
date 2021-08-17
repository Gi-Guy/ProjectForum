package com.projectForum.REST;

/**
 * 	This class used only for updating user via REST services.
 * */
public class UpdateUser {
	private int 	id = 0;
	private String 	username = "";
	private String 	email = "";
	private String 	role = "";
	private String 	firstName = "";
	private	String 	lastName = "";
	private String	password = "";
	
	public UpdateUser() {
	}

	public UpdateUser(int id, String username, String email, String role, String firstName, String lastName,
			String password) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
