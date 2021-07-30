package com.projectForum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectForum.forum.ForumRepository;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.TopicRepository;


/**
 * This controller will handle the next actions:
 * Find a User entity and display it profile page.
 * Return list of all users.
 * Create user profile page.
 * */


@Controller
@RequestMapping(value ="/user")
public class UserController {

	//Video: 30:33<-----------------DELETE THIS
	
	
	private UserRepository 	userReop;
	private TopicRepository topicRepo;
	private PostRepository 	postRepo;
	
	
	@Autowired
	public UserController(UserRepository userReop, TopicRepository topicRepo, PostRepository postRepo) {
		this.userReop = userReop;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
	}
	

	// TODO figure out how to change login/logout page and how to add profile pictures.
	
	/**This method will find a User entity by a username and display it profile page.*/
	@GetMapping("/user/{username}")
	public String findUserByUsernameAndDisplay(@PathVariable String username, Model model) {
		
		// I need List<Post> and List<Topic>, but putting it User entity has broke it? what todo?
		
		return "";
	}
	
	
}