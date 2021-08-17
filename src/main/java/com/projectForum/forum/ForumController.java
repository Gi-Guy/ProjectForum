package com.projectForum.forum;

import java.util.List;

import javax.servlet.http.HttpSession;
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

import com.projectForum.Exceptions.AccessDeniedRequestException;
import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.Services.ControlPanelServices;
import com.projectForum.Services.ForumInformationServices;
import com.projectForum.Services.ForumServices;
import com.projectForum.Services.TopicServices;
import com.projectForum.Services.UserServices;


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
	

	private TopicServices	topicservices;
	private ForumServices	forumServices;
	private ControlPanelServices controlPanelService;
	private UserServices	userServices;
	private ForumInformationServices forumInformationServices;
	
	
	private AccessDeniedRequestException accessDeniedRequestException = new AccessDeniedRequestException();
	private final String localUrl = "/forum/";
	
	
	@Autowired
	public ForumController(TopicServices topicservices, ForumServices forumServices,
			ControlPanelServices controlPanelService, UserServices userServices,
			ForumInformationServices forumInformationServices) {
		this.topicservices = topicservices;
		this.forumServices = forumServices;
		this.controlPanelService = controlPanelService;
		this.userServices = userServices;
		this.forumInformationServices = forumInformationServices;
	}
	
	/**
	 * This method will display all forums in a List<Forum>.
	 */
	@GetMapping("")
	public String displayForums(HttpSession session, Model model) {
		// returning a List<Forum> order by priority {highest priority = 1}
		model.addAttribute("forums", controlPanelService.createForumDisplayList(forumServices.findForumsByPriorityAsc()));
		
		//	Displaying forum's information
		session.setAttribute("forumInformation", forumInformationServices.getForumInformation());
		return "forums";
	}
	 

	/** This method will display all topics that attached to {forumId}. */
	@GetMapping("/forum/{forumId}")
	public String getTopicsById(@PathVariable int forumId, Model model) {
		
		Forum forum = forumServices.findFourmById(forumId);
		// Checking if Forum is exists
		if (forum == null)
			throw new EntityRequestException("could not find Forum id :: " + forumId);
		List<DisplayTopicForm> topics = forumServices.displayTopics(forumId);
		model.addAttribute("forum", forum);
		model.addAttribute("topics", topics);
		return "forum";
	}
	
	/** 
	 * This method will lead user to create a new forum page.
	 * This should be only in Control panel.
	 */
	@GetMapping("/forum/newForum")
	public String createNewForum(Model model, Authentication authentication) {
		
		if(!userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			// User not allowed to access this page
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "newForum");
			
		model.addAttribute("newForum", new Forum());		
		return "new_Forum_page";
	}
	
	/** This method will create a new forum according to the request of a user */
	@PostMapping("/forum/newForum")
	public String proccesNewForum(@Valid @ModelAttribute Forum forum, BindingResult bindingResult, Authentication authentication, Model model) {
		
		if(authentication == null)
			accessDeniedRequestException.throwNewAccessDenied("unknown", localUrl + "newForum");
		
		if(!userServices.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "newForum");
		
		if(bindingResult.hasErrors()) {
			System.err.println("ERROR :: Forum Controller - proccesNewForum (POST)");
			return "new_Forum_page";
		}
		
		// Checking if title or description are blanked
		if(forum.getName().isBlank() || forum.getDescription().isBlank())
			return "new_Forum_page";
		
		//Each forum must have a priority value, 1 is the lowest.
		List<Forum> forums = forumServices.findAll();
		if(forums.isEmpty()) {
			forum.setPriority(1);	
		}
		else {
			forum.setPriority(forums.size() + 1);	
		}
		forumServices.save(forum);
		return "redirect:/forum/" + forum.getId();
	}

}	
