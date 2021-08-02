package com.projectForum.index;


import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projectForum.Security.RoleRepository;
import com.projectForum.Services.DeleteService;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

@Controller
public class IndexController {
	
	/**
	 * This controller is temporary and only exists for testing purpose.
	 * It will be deactivate and removed when Home controller will be ready.*/
	// Autowired - Spring framework automatically creates a new instance of user repository and inject it into this class
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private DeleteService deleteService;
	// Defining the home page of the application
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/index")
	public String returnHomePage() {
		return "index";
	}
	
	//This method has been moved to userController
	/**
	 * This method creates a page of all users 
	 * */
	
	
	/*
	@GetMapping("/list_users")
	public String listofUsers(Model model) {

	    //List<User> listofUsers = userReop.findAll();
	    //model.addAttribute("listofUsers", listofUsers);
		model.addAttribute("listofUsers", userRepo.findAll());
	    List<User> listofUsers = userRepo.findAll();
	    model.addAttribute("listofUsers", listofUsers);
	    return "users";
	}*/
	
	
	/**
	 * This method will delete a user in the users list by button active */
	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam(name="username") String username) {
		deleteService.deleteUser(username);
		
		return "redirect:/list_users";
	}
}
