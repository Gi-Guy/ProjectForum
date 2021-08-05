package com.projectForum.user;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.projectForum.Services.UserServices;


/**
 * This Class manages all User registration process to the database and the web application */
@Controller
public class RegisterController {
	

	private UserServices	userServices;
	
	@Autowired
	public RegisterController(UserServices userServices) {

		this.userServices = userServices;
	}
	
	/**Mapping to register page
	 * @provides Model of user object*/
	@GetMapping("/register")
	public String displayRegisterPage(@ModelAttribute User user, Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	/**
	 * Creating new user in database. If success then moving to completed page*/
	//TODO: Solve the issue with exists users. YOU HAVE TO NOTIFY THE USER IF ACCOUNT ALREADY EXIST.
	// TODO fix this method.
	@PostMapping("/register")
	public String processRegistration(@Valid User user, BindingResult bindingResult) {
		
		if(!bindingResult.hasErrors()) {
			
			//Checking if Email is exist in database
			if (userServices.isUserExistsByEmail(user.getEmail())){
				
				// Checking if username exists in the database
				if(userServices.isUserExistsByUsername(user.getUsername())) {
					// User isn't exists
					userServices.createNewUser(user);
					}
				
				// Username already exists.
				else {
					bindingResult.addError(new FieldError("user", "username", "Username already exists, Please try new username."));
					return "redirect:/register";
					}
				}
			
			// Email already exists
			else {
				bindingResult.addError(new FieldError("user", "email", "Email already exists, Please try new Email."));
				return "redirect:/register";
			}
		}

		return "register_success";
	}
}