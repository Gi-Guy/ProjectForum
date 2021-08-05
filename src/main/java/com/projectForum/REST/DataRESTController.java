package com.projectForum.REST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectForum.Services.RestServices;
import com.projectForum.forum.ForumRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

//@CrossOrigin(origins = "http://localhost:8080") // TODO check this.
@RestController
@RequestMapping("/api/")
public class DataRESTController {
	
	
	private	UserRepository 	userRepo;
	private	ForumRepository	forumRepo;
	private RestServices	restService;
	
	
	@Autowired
	public DataRESTController(UserRepository userRepo, ForumRepository forumRepo, RestServices restService) {
		this.userRepo = userRepo;
		this.forumRepo = forumRepo;
		this.restService = restService;
	}

	/**
	 *  This method will return a JSON of all users in database,
	 *  Order by roles.
	 *  Admins first.*/
	@GetMapping("getAllUsers")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = restService.getAllUsers();
		
		if(users == null) 
			return new ResponseEntity<List<User>> (HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<User>> (users, HttpStatus.OK);
	}
	
	@GetMapping("getUser/{username}")
	public ResponseEntity<User> getUser(@PathVariable("username") String username){
		User user = restService.getUser(username);
		
		if (user == null)
			return new ResponseEntity<User> (HttpStatus.NOT_FOUND);
		return new ResponseEntity<User> (user, HttpStatus.OK);
	}
	
	/**
	 *  This method will print to user how to add user to database.*/
	@GetMapping("/addUser/example")
	public ResponseEntity<AddUserForm> giveExample(){
		
		return new ResponseEntity<AddUserForm>(restService.getExampleUser(), HttpStatus.OK);
	}
	
	@PostMapping("/addUser/add")
	public ResponseEntity<User> addUser(@RequestBody AddUserForm addUser) {
		User user = restService.addNewUser(addUser);
		
		if(user == null)
			return new ResponseEntity<User>(HttpStatus.METHOD_FAILURE);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
}
