package com.projectForum.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.projectForum.user.UserRepository;

@Controller
public class tempIndex {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/test")
	public String toTest() {
		return "404";
	}

}
