package com.projectForum.user;

import org.springframework.stereotype.Controller;

//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value ="/user")
public class UserController {

	//Video: 30:33<-----------------DELETE THIS
	
	//TODO: Add all action with user: (Crud actions) CREATE, READ, UPDATE, DELETE
	//Maybe should I make a separated class for register users? (CREATE)
	
	private UserRepository userRepository;
	//Returns all Users in database
	
}
