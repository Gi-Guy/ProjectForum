package com.projectForum.Security;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

@Component
public class SetupDataLoader implements
				ApplicationListener<ContextRefreshedEvent>{

	boolean alreadySetup = false;
	
	@Autowired
	private UserRepository 	userRepo;
	@Autowired
	private	RoleRepository	roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		// Checking if roles are already sets up , and if Admin user is exists.
		if(!roleRepo.findAll().isEmpty())
			return;
		
		if(alreadySetup) {
			return;
		}
		/* Defining roles - USER & ADMIN */
		this.setRoles();
		
        /* Creating an Admin user */
        //Role adminRole = roleRepo.findByName("ROLE_ADMIN");
		final Role adminRole = roleRepo.findRoleByName("ADMIN");
		final Role userRole = roleRepo.findRoleByName("USER");

        // Create initial user
		// Admin:
        createUser("Admin", "admin", "admin", "admin@admin.admin", "admin", adminRole);
        // Dummy user:
        createUser("Unknown", "unknown", "unknown", "unknown@unknown.unknown", "ThisisUnknownUser", userRole);
        
        alreadySetup = true;
	}
	
	@Transactional
	void setRoles() {
		List<String> roles = Arrays.asList("ADMIN", "USER", "UNDEFINED_USER");
		
		for(int i=0; i<roles.size(); i++) {
			this.setRole(roles.get(i));
		}
	}
	@Transactional
	void setRole(String name) {
		Role role = new Role(name);
		roleRepo.save(role);
	}
	@Transactional
	User createUser(String username, String firstName, String lastName, String email, String password, Role role) {
		User user = userRepo.findByEmail(email);
		
		if(user == null) {
			user = new User();
			user.setUsername(username);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole(role);
		}
		user = userRepo.save(user);
		return user;
	}
}
