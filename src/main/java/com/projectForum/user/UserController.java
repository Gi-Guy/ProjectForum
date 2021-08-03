package com.projectForum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.projectForum.user.Profile.UserProfile;
import com.projectForum.user.Profile.UserProfileServices;


/**
 * This controller will handle the next actions:
 * Find a User entity and display it profile page.
 * Return list of all users.
 * Create user profile page.
 */
@Controller
//@RequestMapping(value ="/user")
public class UserController {

	//Video: 30:33<-----------------DELETE THIS
	
	
	private UserRepository 	userRepo;
	private UserProfileServices userService;
	
	@Autowired
	public UserController(UserRepository userReop, UserProfileServices userService) {
		super();
		this.userRepo = userReop;
		this.userService = userService;
	}
	
	// TODO figure out how to:
	// change login/logout pages 
	// how to add profile pictures.
	// how to change password.
	
	/** This method will find a userProfile entity (including User object) by a username and display it profile page.
	 * @param String username
	 * @param Model
	 */
	@GetMapping("/user/{username}")
	public String findUserByUsernameAndDisplay(@PathVariable String username, Model model) {
		
		// TODO make an exception in case user doesn't exist
		
		UserProfile userProfile = userService.findUserByUsername(username);
		model.addAttribute("userProfile", userProfile);
		
		return "user";
	}
	
	/**
	 * This method will will find a userProfile entity and display it 'myProfile' page.
	 * @param Authentication object
	 * @param Model
	 */
	@RequestMapping("/myprofile")
	public String myProfileDisplay(Authentication authentication, Model model) {
		
		// Authentication should hold user information
		String username = authentication.getName();
		
		// TODO make an exception in case user doesn't exist
		UserProfile userProfile = userService.findUserByUsername(username);
		
		model.addAttribute(userProfile);
		return "user";
	}
	
	/** This method will return List<User> of all users in database and display it. */
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/list_users")
	public String listofUsers(Model model) {

		model.addAttribute("listofUsers", userRepo.findAll());
		return "users";
	}
}