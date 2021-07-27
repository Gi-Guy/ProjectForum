package com.projectForum.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//THIS IS FOR TESTING FOR NOW
public class PasswordEncoder {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "Guy123456";
		String encoderPassword = encoder.encode(rawPassword);
		System.out.println(encoderPassword);
		
	}

}
