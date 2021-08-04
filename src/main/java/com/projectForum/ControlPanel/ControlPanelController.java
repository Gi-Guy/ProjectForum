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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectForum.Security.Role;
import com.projectForum.Security.RoleRepository;
import com.projectForum.Services.ControlPanelServices;
import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.forum.EditForumForm;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;
import com.projectForum.user.Profile.UserProfileServices;

@Controller
@RequestMapping("/a/")
//@RequestMapping("/admin/")
public class ControlPanelController {

	private ControlPanelServices 	controlService;
	private ForumRepository 		forumRepo;
	private UserRepository			userRepo;
	private UserProfileServices		userService;
	private DeleteService			deleteService;
	private EditServices			editService;
	private RoleRepository			roleRepo;
	
	
	
	@Autowired
	public ControlPanelController(ControlPanelServices controlService, ForumRepository forumRepo,
			DeleteService deleteService, EditServices editService, UserRepository userRepo,
			RoleRepository roleRepo) {
		this.controlService	=	controlService;
		this.forumRepo		=	forumRepo;
		this.deleteService	=	deleteService;
		this.editService	=	editService;
		this.userRepo		=	userRepo;
		this.roleRepo		=	roleRepo;
	}
	
	
	/*
	 * Displaying page area
	 * */
	
	@GetMapping("/controlPanel")
	public String displayControlPanel(Model model) {

		/*
		 * Forums section
		 * */
		
		List<Forum> forums = forumRepo.findByOrderByPriorityAsc();
		model.addAttribute("forums", controlService.createForumDisplayList(forums));
		
		/*
		 * Users section
		 * */
		SearchUserForm searchUserForm = new SearchUserForm();
		model.addAttribute("searchUserForm",searchUserForm);
		
		
		return "controlPanel";
	}
	
	
	/* ######################################################
	 * Forum administration section
	 * ###################################################### */
	
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
		return "redirect:/a/controlPanel";
	}
	
	
	@GetMapping("forum/edit/{forumId}")
	public String editForum(@PathVariable int forumId, Model model) {
		EditForumForm editForum = new EditForumForm();
		editForum.setForumId(forumId);
		
		model.addAttribute("editForum", editForum);
		return "edit_Forum_page";
	}
	
	@PostMapping("editForum")
	public String editForum(@Valid @ModelAttribute("editForum") EditForumForm editForum,
							BindingResult bindingResult, Authentication authentication,
							Model model) {
		Forum forum = forumRepo.findById(editForum.getForumId());
		
		// Making sure that Admin in action
		if(userRepo.findByUsername(authentication.getName()).getRoles().iterator().next().getName().equals("ADMIN")) 
			editService.updateForum(forum, editForum);
		
		return "redirect:/a/controlPanel";
	}
	
	/**
	 *  This method will set forumId to a higher priority level.
	 *  */
	@GetMapping("addPriority/{forumId}")
	public String addForumPriority(@PathVariable int forumId) {
		controlService.updatePriorityUp(forumId);
		
		return "redirect:/a/controlPanel";
	}
	/**
	 *  This method will set forumId to a lower priority level.
	 *  */
	@GetMapping("downPriority/{forumId}")
	public String downForumPriority(@PathVariable int forumId) {
		controlService.updatePriorityDown(forumId);
		
		return "redirect:/a/controlPanel";
	}
	/**This method will delete a forum
	 * A forum can't be deleted until all topics attached to it are exists*/
	@GetMapping("forum/delete/{forumId}")
	public String deleteForum(@PathVariable int forumId, Authentication authentication,
								RedirectAttributes model) {
		Forum forum = forumRepo.findById(forumId);
		// Making sure that Admin in action
		if(userRepo.findByUsername(authentication.getName()).getRoles().iterator().next().getName().equals("ADMIN")) {
			deleteService.deleteForum(forum);
			model.addFlashAttribute("message", "Forum has been removed.");	
		}
		return "redirect:/a/controlPanel";
	}
	
	/* ######################################################
	 * User administration section
	 * ######################################################*/
	
	/** This method will get Admin to search an user.*/
	@GetMapping("searchUser")
	public String displayUserByUsername(@RequestParam(name = "username") String username, Model model) {
		
		SearchUserForm searchUser = controlService.findSearchUserByUsername(username);
		List<Role> roles = roleRepo.findAll();
		
		model.addAttribute("searchUser",searchUser);
		model.addAttribute("editUser",new SearchUserForm());
		model.addAttribute("roles", roles);
		
		return "edit_User_page";
	}
	/** This method will apply changes into db*/
	@PostMapping("editUser")
	public String applyUserChanges(@Valid @ModelAttribute("editUser") SearchUserForm editUser,
							BindingResult bindingResult, Authentication authentication,
							Model model) {
		System.err.println("Hello! I'm here!");
		return "redirect:/a/controlPanel";
	}
	/** This method will remove a User entity from database.
	 * 	This method will not delete an Admin user or dummy User.
	 * 	In default all user's posts and topics will not removed, but will be attched to an dummy user.
	 * 	to delete all user's activity put SearchUserForm.keepActivity=false.
	 * */
	@GetMapping("user/delete/{username}")
	public String deleteUser(@PathVariable String username, Authentication authentication,
								RedirectAttributes model) {
		User user = userRepo.findByUsername(username);
		
		if(authentication == null || user == null
				|| !userRepo.findByUsername(authentication.getName()).getRoles().iterator().next().getName().equals("ADMIN"))
			return "redirect:/";
		
		deleteService.deleteUserKeepActivity(user);
		return "/controlPanel/";
	}
	
	/** This method will edit the role of an exists user*/
	public String updateUserRole() {
		
		return "";
	}
	
	/* ######################################################
	 * Homepage administration section
	 * ######################################################*/
}
