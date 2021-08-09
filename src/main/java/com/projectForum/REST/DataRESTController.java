package com.projectForum.REST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectForum.Services.RestServices;
import com.projectForum.forum.EditForumForm;
import com.projectForum.forum.Forum;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;

@RestController
@RequestMapping("/api/")
public class DataRESTController {
	
	@Autowired
	private RestServices	restService;
	
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
	@GetMapping("/removeUser/example")
	public ResponseEntity<DeleteUserForm> removeUserExample(){
		return new ResponseEntity<DeleteUserForm>(restService.removeUserExample(), HttpStatus.OK);
	}
	@DeleteMapping("/removeUser")
	public ResponseEntity<String> removeUser(@RequestBody DeleteUserForm deleteUser){
		
		if(!restService.removeUser(deleteUser))
			return new ResponseEntity<String>("Could not delete user.", HttpStatus.METHOD_FAILURE);
		return new ResponseEntity<String>(HttpStatus.OK);
	}	
	@GetMapping("updateUser/example")
	public ResponseEntity<UpdateUser> giveUpdateExample(){
		
		return new ResponseEntity<UpdateUser>(restService.updateUserExample(), HttpStatus.OK);
	}
	@PutMapping("/updateUser/")
	public ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser){
		
		if(restService.updateUser(updateUser)) {
			User user = restService.findUserByUpdateUser(updateUser);
			if(user != null)
				return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return new ResponseEntity<UpdateUser>(HttpStatus.NOT_FOUND);
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
		Forum newForum = restService.addNewForum(forum);
		
		if (newForum == null)
			return new ResponseEntity<Forum>(HttpStatus.FAILED_DEPENDENCY);
		return new ResponseEntity<Forum>(newForum, HttpStatus.CREATED);
	}
	@GetMapping("updateForum/example")
	public ResponseEntity<?> updateForumExample(){
		return new ResponseEntity<EditForumForm>(restService.updateForumExample(), HttpStatus.OK);
	}
	@PutMapping("updateForum")
	public ResponseEntity<?> updateForum(@RequestBody EditForumForm updateForum){
		
		if(restService.updateforum(updateForum))
			return new ResponseEntity<Forum>(restService.getForumById(updateForum.getForumId()),
															HttpStatus.OK);
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}
	@DeleteMapping("/deleteForum/{forumId}")
	public ResponseEntity<?> deleteForum(@PathVariable("forumId") int forumId){
		Forum forum = restService.getForumById(forumId);
		
		if (forum == null)
			return new ResponseEntity<Forum>(HttpStatus.NOT_FOUND);
		else
			restService.deleteForum(forumId);
		return new ResponseEntity<Forum> (HttpStatus.OK);
		
	}
	/*
	 * ################################################################
	 * 							TOPICS
	 * ################################################################
	 * */
	// Topics will not include editing option.
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
	@DeleteMapping("deleteTopic/{topicId}")
	public ResponseEntity<?> deleteTopic(@PathVariable("topicId") int topicId){
		if(restService.deleteTopic(topicId))
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}
	
	/*
	 * ################################################################
	 * 							POSTS
	 * ################################################################
	 * */
	// Posts will not include editing option.
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
	@DeleteMapping("deletePost/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable("postId") int postId){
		if(restService.deletePost(postId))
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}
}
