package com.projectForum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.projectForum.Exceptions.AccessDeniedRequestException;
import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.REST.UpdateUser;
import com.projectForum.Services.DeleteService;
import com.projectForum.Services.UserServices;
import com.projectForum.user.Profile.UserProfile;
import com.projectForum.user.Profile.UserProfileServices;

import javax.validation.Valid;

/**
 * This controller will handle the next actions:
 * Find a User entity and display it profile page.
 * Return list of all users.
 * Create user profile page.
 */
@Controller
//@RequestMapping(value ="/user")
public class UserController {

	private UserServices	userServices;
	private UserProfileServices userProfileService;

	
	private AccessDeniedRequestException accessDeniedRequestException = new AccessDeniedRequestException();
	private final String localUrl = "/user/";
	
	@Autowired
	public UserController(UserServices userServices, UserProfileServices userProfileService, DeleteService deleteService) {
		this.userServices = userServices;
		this.userProfileService = userProfileService;
	}
	
	// TODO figure out how to:
	// change login/logout pages
	
	/** This method will find a userProfile entity (including User object) by a username and display it profile page.
	 * @param username
	 * @param model
	 */
	@GetMapping("/user/{username}")
	public String findUserByUsernameAndDisplay(@PathVariable String username, Model model) {
		
		//	Checking if user exists
		User user = userServices.findUserByUsername(username);
		if(user == null)
			//	 user isn't exists or something went wrong
			throw new EntityRequestException("could not reload user :: " + username);
		
		UserProfile userProfile = userProfileService.findUserByUsername(username);
		model.addAttribute("userProfile", userProfile);
		
		return "profile";
	}
	@GetMapping("/user/edit/{username}")
	public String editUser(@PathVariable String username, Model model, Authentication authentication) {
		User user = userServices.findUserByUsername(username);

		// Only register user allowed to do acitons
		if (authentication == null )
			accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "edit/" + username);
		
		// Checking if user allowed editing user information
		if(!user.getUsername().equals(authentication.getName()))
			//	User isn't allowed to edit user profile
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "edit/" + username);

		//User allowed to edit
		model.addAttribute("user",userProfileService.getUpdateForm(user));
		return "edit_UserProfile_form";
	}

	@PostMapping("/user/editUser")
	public String editUser(@Valid @ModelAttribute("updateUser") UpdateUser updateUser, BindingResult bindingResult,
						   Authentication authentication, Model model) {
		
		// Only register user allowed to do acitons
		if (authentication == null )
			accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "edit/" + updateUser.getUsername());
		
		User user = userServices.findUserByUsername(updateUser.getUsername());
		
		//	Checking if user exists
		if (user == null)
			throw new EntityRequestException("could not reload user :: " + updateUser.getUsername());
		
		//	setting user id
		updateUser.setId(user.getId());
		
		// Checking if there are any errors
		if(bindingResult.hasErrors()) {
			// Something went wrong
			model.addAttribute("user",userProfileService.getUpdateForm(user));
			return "edit_UserProfile_form";
		}
		// Checking if user allowed to edit
		if(!updateUser.getUsername().equals(authentication.getName())) {
			// User not allowed to edit user information
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "editUser/" + updateUser.getUsername());
		}

		// At this point user allowed to edit user information
		userProfileService.updateUser(updateUser);
		return "redirect:/user/" + updateUser.getUsername();
	}
}