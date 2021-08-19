package com.projectForum.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectForum.Services.UserServices;

/**
 * This Class manages all User registration process to the database and the web application 
 */
@Controller
public class RegisterController {

	private UserServices	userServices;
	
	@Autowired
	public RegisterController(UserServices userServices) {

		this.userServices = userServices;
	}

	/**
	 * Mapping to register page
	 * @provides Model of user object
	 */
	@GetMapping("/register")
	public String displayRegisterPage(@ModelAttribute User user, Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	/**
	 * Mapping to register error page.
	 * Passes flags: whether the email or username were taken.
	 * @provides Model of user object
	 */
	@GetMapping("/registerError")
	public String registerError(@ModelAttribute User user, @RequestParam("emailExists") String emailExists,
		@RequestParam("userExists") String userExists, Model model) {

		model.addAttribute("user", new User());
		model.addAttribute("emailExists", emailExists);
		model.addAttribute("userExists", userExists);
	
		return "register";
	}
	
	/**
	 * Creating new user in database. 
	 * If successful then move to register success page.
	 * If unsuccessful then move to register error page.
	 */
	@PostMapping("/register")
	public String processRegistration(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if(!bindingResult.hasErrors()) {
			
			// If neither email nor username exist, create new user
			if (!userServices.isUserExistsByEmail(user.getEmail())
				&& !userServices.isUserExistsByUsername(user.getUsername())) {
				userServices.createNewUser(user);
				return "register_success";
			}
		}
		
		// If user exists add user exists flag
		if (userServices.isUserExistsByEmail(user.getEmail()))
			redirectAttributes.addAttribute("emailExists", true);
		else
			redirectAttributes.addAttribute("emailExists", false);
			
		// If email exists add email exists flag
		if (userServices.isUserExistsByUsername(user.getUsername()))
			redirectAttributes.addAttribute("userExists", true);
		else
			redirectAttributes.addAttribute("userExists", false);
		
		return "redirect:/registerError";
	}
}