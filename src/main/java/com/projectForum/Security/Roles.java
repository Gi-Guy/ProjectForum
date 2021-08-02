package com.projectForum.Security;

public enum Roles {
	
	UNDEFINED_USER("UNDEFINED_USER"),
	USER("USER"),
	MODERATE("MODERATE"),
	AMDIN("ADMIN");
	
	
	Roles(String name) {
		this.name = name;
	}

	private String name;
	
	
	
	
}
