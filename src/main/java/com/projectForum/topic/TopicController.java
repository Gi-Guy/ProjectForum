package com.projectForum.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectForum.post.PostRepository;
import com.projectForum.user.UserRepository;


/**
 * This controller will handle the next actions:
 * Find Topic by id
 * find Topic by Username
 * Add new post to an exists Topic
 * Create new Topic
 * Delete topic - If this method isn't here, Check controlPanel Controller.*/

@Controller
@RequestMapping("/topic/")
public class TopicController {
	
	private UserRepository 	userReop;
	private TopicRepository topicRepo;
	private PostRepository 	postRepo;
	
	
	@Autowired
	public TopicController(UserRepository userReop, TopicRepository topicRepo, PostRepository postRepo) {
		this.userReop = userReop;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
	}
	// TODO finish this class
	@GetMapping("{topicId}")
	public String getTopicById() {
		
		return "";
	}
	
	@GetMapping("{username}")
	public String getTopicByUsername() {
		
		return "";
	}
	
	/**This method will add a new post to an exists topic*/
	@PostMapping("{topicId}")
	public String addNewPost() {
		
		return "";
	}
	
	/**This method will return a model and navigate the user to newTopic page*/
	@GetMapping("newTopic")
	public String createNewTopic(Model model) {
		
		return "";
	}
	
	/**This method will create a new topic and navigate the user to the new topic page.*/
	@PostMapping("newTopic")
	public String proccesNewTopic() {
		
		return "";
	}
	
	// TODO Move this method to control panel?
	/**This method will delete an exists method*/
	@GetMapping("delete/{topicId}")
	public String deleteTopic() {
		return "";
	}
	
}
