package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectForum.REST.AddUserForm;
import com.projectForum.Security.RoleRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

/**
 *  This class will gives services to the Rest controller.
 *  */

@Service
public class RestServices {

	@Autowired
	private UserRepository	userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository	roleRepo;
	/**
	 *  This method will return a list of user, order by roles (Admins first)
	 *  */
	public List<User> getAllUsers(){
		List<User> users = userRepo.findByOrderByRolesAsc();
		
		return users;
	}
	
	/**
	 *	This method will return a User object by username String.
	 **/
	public User getUser(String username) {
		User user = userRepo.findByUsername(username);
		
		return user;
	}
	
	/**
	 * 	This method will return an example form of AddUserForm.
	 * */
	public AddUserForm getExampleUser() {
		AddUserForm addUser = new AddUserForm();
		addUser.setUsername("Example");
		addUser.setFirstName("Example");
		addUser.setLastName("Example");
		addUser.setEmail("Example@Example.Example");
		addUser.setPassword("password");
		
		return addUser;
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
}
