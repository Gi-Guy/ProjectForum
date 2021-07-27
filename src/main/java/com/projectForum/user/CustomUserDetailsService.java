package com.projectForum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService  implements UserDetailsService {

	@Autowired
	private UserRepository repo;
	
	
	/**
	 * Find User by username*/
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repo.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Could not find user");}
		return new CustomUserDetails(user);
	}
	
	/**
	 * Find User by Email*/
	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		User user = repo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Could not find user");}
		return new CustomUserDetails(user);
	}

}
