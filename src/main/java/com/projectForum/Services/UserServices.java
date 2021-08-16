package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
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
	/**
	 * 	This method will return a User entity by username
	 * @param username
	 * @return user
	 * @throws EntityRequestException
	 */
	public User findUserByUsername(String username) throws EntityRequestException{
		User user = null;
		try {
			user = userRepo.findByUsername(username);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find user by username");
		}
		return user;
	}
	/**
	 * 	This method will return a User entity by userId
	 * @param UserId
	 * @return user
	 * @throws EntityRequestException
	 * */
	public User findUserByUserId(int userId) throws EntityRequestException{
		User user = null;
		try {
			user = userRepo.findUserById(userId);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find user by userID");
		}
		return user;
	}
	/**
	 * 	This method will return a User entity by Email
	 * 
	 * @param Email
	 * @return user
	 * @throws EntityRequestException
	 */
	public User findUserByUserEmail(String Email) throws EntityRequestException{
		User user = null;
		try {
			user = userRepo.findByEmail(Email);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find user by Email");
		}
		return user;
	}
	/**
	 * 	This method will return a List<User> of all users in the database, order by Roles ASC
	 * 
	 * @return users
	 * @throws EntityRequestException
	 */
	public List<User> findAllUsersByRoleAsc() throws EntityRequestException{
		List<User> users = null;
		
		try {
			users = userRepo.findByOrderByRolesAsc();
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all users");
		}
	//	if (users == null)
		//	throw new EntityRequestException("Could not find all users");
		return users;
	}
	public List<User> findAll() throws EntityRequestException{
		List<User> users = null;
		
		try {
			users = userRepo.findAll();
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all users");
		}
		
	//	if (users == null)
	//		throw new EntityRequestException("Could not find all users");
		return users;
	}
	public void save(User user) throws EntityRequestException{
		try {
			userRepo.save(user);
		} catch (Exception e) {
			throw new EntityRequestException("Could not save new User");
		}
	}
	
	public User saveAndreturn(User user) throws EntityRequestException{
		try {
			return userRepo.save(user);
		} catch (Exception e) {
			throw new EntityRequestException("Could not save new User");
		}
	}
	
	public void delete(User user) throws EntityRequestException{
		try {
			userRepo.delete(user);
		} catch (Exception e) {
			throw new EntityRequestException("Could not delete user");
		}
	}
}
