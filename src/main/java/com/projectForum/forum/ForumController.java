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
 * Finding all topics of a forum and displaying a forum page
 * Creating a new forum
 * Deleting a forum
 * 
 * Notice:
 * Topic creation is in TopicController where the topic will also be attached to a forum. 
 */

@Controller
public class ForumController {
	
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
		// Checking if the Forum exists
		if (forum == null)
			throw new EntityRequestException("could not find Forum id :: " + forumId);
		List<DisplayTopicForm> topics = forumServices.displayTopics(forumId);
		model.addAttribute("forum", forum);
		model.addAttribute("topics", topics);
		return "forum";
	}
}	
