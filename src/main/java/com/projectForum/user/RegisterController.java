package com.projectForum.user;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	@PostMapping("/process_register")
	public String processRegistration(User user) {
		if (userRepo.findByEmail(user.getEmail()) == null){
			if(userRepo.findByUsername(user.getUsername()) == null) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setJoiningDate(LocalDateTime.now());
				userRepo.save(user);	
				}
			//TODO add failure messages.
			else return "register";
			}
		else return "register";

		return "register_success";
	}
	


}
