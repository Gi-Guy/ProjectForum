package com.projectForum.forum;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	private UserRepository 	userRepo;
	private TopicRepository topicRepo;
	private PostRepository 	postRepo;
	private ForumRepository forumRepo;
	
	@Autowired
	public ForumController(UserRepository userReop, TopicRepository topicRepo, PostRepository postRepo,
			ForumRepository forumRepo) {
		this.userRepo = userReop;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
		this.forumRepo = forumRepo;
	}
	
	/**This method will display all topics that attached to {forumId}*/
	@GetMapping("{forumId}")
	public String getTopicsById(@PathVariable int forumId, Model model) {
		model.addAttribute("forum", forumRepo.findById(forumId));
		model.addAttribute("topics", topicRepo.findTopicsByForumId(forumId));
		return "forum";
	}
	
	/**This method will lead user to create new forum page
	 * This should be only in Control panel*/
	@GetMapping("newForum")
	public String createNewForum(@Valid @ModelAttribute Forum forum, Model model) {
		model.addAttribute("newForum", new Forum());		
		
		return "new_Forum_page";
	}
	/**This method will create a new forum*/
	@PostMapping("newForum")
	public String proccesNewForum(@Valid @ModelAttribute Forum forum, BindingResult bindingResult, Authentication authentication, Model model) {
		
		if(bindingResult.hasErrors()) {
			// TODO complete this
			System.out.println("I'm here (New Forum) and I don't know what to do with my life.");
		}
		forumRepo.save(forum);
		return "redirect:/forum/" + forum.getId();
	}
	
	/**This method will delete a forum
	 * A forum can't be deleted until all topics attached to it are exists*/
	@GetMapping("delete/{forumId}")
	public String deleteForum() {
		// TODO Build this method and fix deleteTopic method in TopicControllet.
		return "";
		
	}
}	
