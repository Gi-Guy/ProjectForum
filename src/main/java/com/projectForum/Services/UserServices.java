package com.projectForum.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectForum.REST.AddUserForm;
import com.projectForum.Security.RoleRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

/**
 *  This class will provide services to all users activity
 *  */

@Service
public class UserServices {
	
	private UserRepository	userRepo;
	private RoleRepository	roleRepo;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServices(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	/**
	 * 	This method will add a new user from JSON to database and return a new user.
	 * */
	public User addNewUser(AddUserForm addUser) {
		User user = userRepo.findByUsername(addUser.getUsername());
		
		// Checking if user already exists by username
		if(user != null)
			return null;
		// Checking if user already exists by email		
		user = userRepo.findByEmail(addUser.getEmail());
		if(user != null)
			return null;	
		
		// user isn't exists by username or email.
		// Creating new User
		user = new User();
		user.setUsername(addUser.getUsername());
		user.setFirstName(addUser.getFirstName());
		user.setLastName(addUser.getLastName());
		user.setEmail(addUser.getEmail());
		user.setPassword(passwordEncoder.encode(addUser.getPassword()));
		user.setRole(roleRepo.findRoleByName("USER"));
		userRepo.save(user);
		return userRepo.findByUsername(user.getUsername());
	}
	
	/**
	 * 	This method will create a new user and return true if succses,
	 * 	If user already exists by username or email, it will return false.
	 * 
	 * 	This method is used only in register page, so then most information should be in it.
	 * */
	public void createNewUser(User user) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(roleRepo.findRoleByName("USER"));
		userRepo.save(user);	
	}
	/** 
	 * 	This method will return true if user is exists in database via Email,
	 * 	else will return false.*/
	public boolean isUserExistsByEmail(String email) {
		if(userRepo.findByEmail(email) == null)
			return false;
		return true;
	}
	/** 
	 * 	This method will return true if user is exists in database via username,
	 * 	else will return false.*/
	public boolean isUserExistsByUsername(String username) {
		if(userRepo.findByUsername(username) == null)
			return false;
		return true;
	}
	
}
