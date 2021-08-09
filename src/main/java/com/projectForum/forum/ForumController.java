package com.projectForum.forum;

import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectForum.Services.ControlPanelServices;
import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.UserRepository;

/**
 * This Controller will handle the next actions:
 * Displaying a list of all forums
 * Creation of a new forum
 * Find all topics of a forum and display a forum page
 * Delete a forum
 * 
 * Notice:
 * Topic creation is in TopicController where the topic will also be attached to a forum .*/

	@Controller
public class ForumController {
	
	private UserRepository 	userRepo;
	private TopicRepository topicRepo;
	private PostRepository 	postRepo;
	private ForumRepository forumRepo;
	private DeleteService	deleteService;
	private EditServices	editService;
	private ControlPanelServices controlPanelService;
	
	@Autowired
	public ForumController(UserRepository userReop, TopicRepository topicRepo, PostRepository postRepo,
			ForumRepository forumRepo, DeleteService deleteService, EditServices editService, ControlPanelServices controlPanelService) {
		this.userRepo = userReop;
		this.topicRepo = topicRepo;
		this.postRepo = postRepo;
		this.forumRepo = forumRepo;
		this.deleteService = deleteService;
		this.editService = editService;
		this.controlPanelService = controlPanelService; 
	}
	
	/**
	 * This method will display all forums in a List<Forum>.
	 */
	@GetMapping("")
	public String displayForums(Model model) {
		// returning a List<Forum> order by priority {highest priority = 1}
		model.addAttribute("forums", controlPanelService.createForumDisplayList(forumRepo.findByOrderByPriorityAsc()));
		return "forums";
	}
	 
	/** This method will display all topics that attached to {forumId}. */
	@GetMapping("/forum/{forumId}")
	public String getTopicsById(@PathVariable int forumId, Model model) {
		model.addAttribute("forum", forumRepo.findById(forumId));
		model.addAttribute("topics", topicRepo.findTopicsByForumId(forumId));
		return "forum";
	}
	
	/** 
	 * This method will lead user to create a new forum page.
	 * This should be only in Control panel.
	 */
	@GetMapping("/forum/newForum")
	public String createNewForum(Model model) {
		
		model.addAttribute("newForum", new Forum());		
		return "new_Forum_page";
	}
	
	/** This method will create a new forum according to the request of a user */
	@PostMapping("/forum/newForum")
	public String proccesNewForum(@Valid @ModelAttribute Forum forum, BindingResult bindingResult, Authentication authentication, Model model) {
		
		if(bindingResult.hasErrors()) {
			System.err.println("ERROR :: Forum Controller - proccesNewForum (POST)");
			return "new_Forum_page";
		}
		
		//Each forum must have a priority value, 1 is the lowest.
		List<Forum> forums = forumRepo.findAll();
		if(forums.isEmpty()) {
			forum.setPriority(1);	
		}
		else {
			forum.setPriority(forums.size() + 1);	
		}
		forumRepo.save(forum);
		return "redirect:/forum/" + forum.getId();
	}
	
	/**
	 * This method will lead a user to edit a forum
	 */
	@GetMapping("/forum/edit/{forumId}")
	public String editForum(@PathVariable int forumId, Model model) {
		Forum forum = new Forum();
		
		forum.setId(forumId);
		model.addAttribute("editForum", forum);
		// TODO update this after you create the control panel
		return "";
	}
	
	/**
	 * This method will update the changes a user made to a forum 
	 */
	@PostMapping("/forum/editForum")
	public String editForum(@Valid @ModelAttribute("editForum") Forum editForum, BindingResult bindingResult, Authentication authentication, Model model) {
		Forum forum = forumRepo.findById(editForum.getId());
		
		// TODO solve how to check authentication == ADMIN
		return "";
	}
	
}	
