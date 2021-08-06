package com.projectForum.REST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
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
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
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
	/*
	 * ################################################################
	 * 							USERS
	 * ################################################################
	 * */
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
	public ResponseEntity<AddUserForm> giveUserExample(){
		
		return new ResponseEntity<AddUserForm>(restService.getExampleUser(), HttpStatus.OK);
	}
	
	@PostMapping("/addUser/add")
	public ResponseEntity<User> addUser(@RequestBody AddUserForm addUser) {
		User user = restService.addNewUser(addUser);
		
		if(user == null)
			return new ResponseEntity<User>(HttpStatus.METHOD_FAILURE);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	/*
	 * ################################################################
	 * 							FORUMS
	 * ################################################################
	 * */
	@GetMapping("getAllForums")
	public ResponseEntity<List<Forum>> getAllForums() {
		List<Forum> forums = restService.getAllForums();
		
		if(forums == null) 
			return new ResponseEntity<List<Forum>> (HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<Forum>> (forums, HttpStatus.OK);
	}
	@GetMapping("getforumById/{forumId}")
	public ResponseEntity<Forum> getForumById(@PathVariable("forumId") int forumId){
		Forum forum = restService.getForumById(forumId);
		
		if (forum == null)
			return new ResponseEntity<Forum> (HttpStatus.NOT_FOUND);
		return new ResponseEntity<Forum> (forum, HttpStatus.OK);
	
	}
	
	@GetMapping("getforumByPriority/{priority}")
	public ResponseEntity<Forum> getForumByPriority(@PathVariable("priority") int priority){
		Forum forum = restService.getForumByPriority(priority);
		
		if (forum == null)
			return new ResponseEntity<Forum> (HttpStatus.NOT_FOUND);
		return new ResponseEntity<Forum> (forum, HttpStatus.OK);
	
	}
	
	@GetMapping("/addForum/example")
	public ResponseEntity<String> giveForumExample(){
		// TODO solve how to send JSON and not String.
		return new ResponseEntity<String>(restService.getExampleForum(), HttpStatus.OK);
	}
	@PostMapping("/addForum/add")
	public ResponseEntity<Forum> addForum(@RequestBody Forum forum) {
		//User user = restService.addNewUser(addUser);
		
		//if(user == null)
			//return new ResponseEntity<User>(HttpStatus.METHOD_FAILURE);
		//return new ResponseEntity<User>(user, HttpStatus.CREATED);
		
		System.err.println("hey!");
		return null;
	}
	/*
	 * ################################################################
	 * 							TOPICS
	 * ################################################################
	 * */
	@GetMapping("getAllTopics")
	public ResponseEntity<List<Topic>> getAllTopics() {
		List<Topic> topics = restService.getAllTopics();
		
		if(topics == null) 
			return new ResponseEntity<List<Topic>> (HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<Topic>> (topics, HttpStatus.OK);
	}
	
	@GetMapping("getTopicById/{topicId}")
	public ResponseEntity<Topic> getTopicById(@PathVariable("topicId") int topicId){
		Topic topic = restService.getTopicById(topicId);
		
		if (topic == null)
			return new ResponseEntity<Topic> (HttpStatus.NOT_FOUND);
		return new ResponseEntity<Topic> (topic, HttpStatus.OK);
	
	}
	/*
	 * ################################################################
	 * 							POSTS
	 * ################################################################
	 * */
	@GetMapping("getAllPosts")
	public ResponseEntity<List<Post>> getAllPosts() {
		List<Post> posts = restService.getAllPosts();
		
		if(posts == null) 
			return new ResponseEntity<List<Post>> (HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<Post>> (posts, HttpStatus.OK);
	}
	
	@GetMapping("getPostById/{postId}")
	public ResponseEntity<Post> getPosyById(@PathVariable("postId") int postId){
		Post post = restService.getPostById(postId);
		
		if (post == null)
			return new ResponseEntity<Post> (HttpStatus.NOT_FOUND);
		return new ResponseEntity<Post> (post, HttpStatus.OK);
	
	}
}
