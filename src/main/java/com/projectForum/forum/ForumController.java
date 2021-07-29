package com.projectForum.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projectForum.post.PostRepository;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.UserRepository;

/**
 * This Controller will handle the next actions:
 * Creation of a new forum
 * Find all topics of a forum and display a forum page
 * Delete a forum
 * 
 * Notice:
 * Topic creation is in a Topic Controller, A topic will be attched to a forum by it controller.*/

	@Controller
	@RequestMapping("/forum/")
public class ForumController {
	
	private UserRepository 	userReop;
	private TopicRepository topicRepo;
	private PostRepository 	postRepo;
	private ForumRepository forumRepo;
	
	@Autowired
	public ForumController(UserRepository userReop, TopicRepository topicRepo, PostRepository postRepo,
			ForumRepository forumRepo) {
		this.userReop = userReop;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
		this.forumRepo = forumRepo;
	}
	
	/**This method will display all topics that attached to {forumId}*/
	@GetMapping("{forumId}")
	public String getTopicsById(@PathVariable int forumId, Model model) {
		model.addAttribute("forums", forumRepo.findById(forumId));
		model.addAttribute("topics", topicRepo.findTopicsByForumId(forumId));
		return "forum";
	}
	
	/**This method will lead user to create new forum page
	 * This should be only in Control panel*/
	@GetMapping("newForum")
	public String createNewForum() {
		return "";
	}
	/**This method will create a new forum*/
	@PostMapping("newForum")
	public String proccesNewForum() {
		return "";
	}
	
	/**This method will delete a forum
	 * A forum can't be deleted until all topics attached to it are exists*/
	@GetMapping("delete/{forumId}")
	public String deleteForum() {
		return "";
		
	}
}	
