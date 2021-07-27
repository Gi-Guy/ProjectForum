package com.projectForum.user;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This Class manages all User registration process to the database and the web application*/

@Controller
public class RegisterController {
	
	//@Autowired
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public RegisterController(UserRepository userRepository , PasswordEncoder passwordEncoder ) {
		this.userRepo = userRepository;
		this.passwordEncoder = passwordEncoder;
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
	
	//TODO: Sovle the issue with exists users. YOU HAVE TO NOTIFY THE USER IF ACCOUNT ALREADY EXIST.
	@PostMapping("/register")
	public String processRegistration(@Valid User user, BindingResult bindingResult) {
		if(!bindingResult.hasErrors()) {
			//Checking if Email is exist in database
			if (userRepo.findByEmail(user.getEmail()) == null){
				//Checking if username is exist in database
				if(userRepo.findByUsername(user.getUsername()) == null) {
					user.setPassword(passwordEncoder.encode(user.getPassword()));
					user.setJoiningDate(LocalDateTime.now());
					userRepo.save(user);	
					}
				else {//Username already exists.
					bindingResult.addError(new FieldError("user", "username", "Username already exists, Please try new username."));
					return "redirect:/register";
					}
				}
			else {//Email already exists
				bindingResult.addError(new FieldError("user", "email", "Email already exists, Please try new Email."));
				return "redirect:/register";
			}
		}

		return "register_success";
	}
	


}
