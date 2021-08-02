package com.projectForum.ControlPanel;

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

import com.projectForum.Security.Roles;
import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.user.Profile.UserProfile;
import com.projectForum.user.Profile.UserProfileServices;

@Controller
@RequestMapping("/controlPanel/")
public class ControlPanelController {

	private ControlServices 	controlService;
	private ForumRepository 	forumRepo;
	private UserProfileServices	userService;
	private DeleteService		deleteService;
	private EditServices		editService;
	
	
	
	@Autowired
	public ControlPanelController(ControlServices controlService, ForumRepository forumRepo,
			DeleteService deleteService, EditServices editService) {
		this.controlService = controlService;
		this.forumRepo = forumRepo;
		this.deleteService = deleteService;
		this.editService = editService;
	}
	
	/*
	 * Forum administration section
	 * */
	/**This method will lead user to create new forum page
	 * This should be only in Control panel*/
	@GetMapping("newForum")
	public String createNewForum(Model model) {
		
		model.addAttribute("newForum", new Forum());		
		return "new_Forum_page";
	}
	/**This method will create a new forum*/
	@PostMapping("newForum")
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
	
	
	@GetMapping("edit/{forumId}")
	public String editForum(@PathVariable int forumId, Model model) {
		Forum forum = new Forum();
		
		forum.setId(forumId);
		model.addAttribute("editForum", forum);
		// TODO update this after you create the control panel
		return "";
	}
	
	@PostMapping("editForum")
	public String editForum(@Valid @ModelAttribute("editForum") Forum editForum, BindingResult bindingResult, Authentication authentication, Model model) {
		Forum forum = forumRepo.findById(editForum.getId());
		
		// TODO solve how to check authentication == ADMIN
		return "";
	}
	
	/**This method will delete a forum
	 * A forum can't be deleted until all topics attached to it are exists*/
	@GetMapping("delete/{forumId}")
	public String deleteForum(@PathVariable int forumId, Authentication authentication,
								RedirectAttributes model) {
		Forum forum = forumRepo.findById(forumId);
		
		deleteService.deleteForum(forum);
		model.addFlashAttribute("message", "Forum has been removed.");
		return "/controlPanel/";
	}
	
	/*
	 * User administration section
	 * */
	
	@GetMapping("searchUser")
	public String displayUserByUsername(@PathVariable String username, Model model) {
		UserProfile userProfile = userService.findUserByUsername(username);
		model.addAttribute("userProfile",userProfile);
		return "searchUser";
	}
	
	/*
	 * Homepage administration section
	 * */
}
