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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectForum.ControlPanel.Configuration.ForumInformation;
import com.projectForum.Exceptions.AccessDeniedRequestException;
import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.Security.Role;
import com.projectForum.Services.ControlPanelServices;
import com.projectForum.Services.DeleteService;
import com.projectForum.Services.EditServices;
import com.projectForum.Services.ForumInformationServices;
import com.projectForum.Services.ForumServices;
import com.projectForum.Services.RoleServices;
import com.projectForum.Services.UserServices;
import com.projectForum.forum.EditForumForm;
import com.projectForum.forum.Forum;
import com.projectForum.user.User;

@Controller
@RequestMapping("/a/")
public class ControlPanelController {

	private ControlPanelServices 	controlService;
	private ForumServices			forumService;
	private UserServices			userService;
	private DeleteService			deleteService;
	private EditServices			editService;
	private ForumInformationServices forumInformationServices;
	private RoleServices			roleServices;
	
	private AccessDeniedRequestException accessDeniedRequestException = new AccessDeniedRequestException();
	private final String localUrl = "/a/";
	
	@Autowired
	public ControlPanelController(ControlPanelServices controlService, ForumServices forumService,
			DeleteService deleteService, EditServices editService,
			RoleServices roleServices, UserServices userService,
			ForumInformationServices forumInformationServices) {
		this.controlService	=	controlService;
		this.forumService 	=	forumService;
		this.deleteService	=	deleteService;
		this.editService	=	editService;
		this.roleServices	=	roleServices;
		this.userService	=	userService;
		this.forumInformationServices = forumInformationServices;
	}
	
	
	/*
	 * Displaying page area
	 * */
	
	@GetMapping("/controlPanel")
	public String displayControlPanel(Model model, Authentication authentication) {
		User user = userService.findUserByUsername(authentication.getName());
		final Role adminRole = roleServices.findRoleByName("Admin");
		// Checking if user allowed to access control panel
		if(!user.getRole().equals(adminRole)) {
			// User isn't allowed to access to control panel
			accessDeniedRequestException.throwNewAccessDenied(user.getUsername(), localUrl + "controlPanel");
		}
		/*
		 * Forums section
		 * */
		
		List<Forum> forums = forumService.findForumsByPriorityAsc();
		model.addAttribute("forumForms", controlService.createForumDisplayList(forums));
		
		/*
		 * Users section
		 * */
		
		/*
		 * Forum control section
		 * */
		ForumInformation forumInformation = forumInformationServices.getForumInformation();
		model.addAttribute("forumInformation", forumInformation);
		
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
		List<Forum> forums = forumService.findAll();
		if(forums.isEmpty()) {
			forum.setPriority(1);	
		}
		else {
			forum.setPriority(forums.size() + 1);	
		}
		forumService.save(forum);
		// User will be redirected to the place in the control panel where the forum they made is shown.
		return "redirect:controlPanel" + '#' + forum.getId(); 
	}
	
	/**
	 * This mehod will lead Admin into forum's editing page.*/
	@GetMapping("forum/edit/{forumId}")
	public String editForum(@PathVariable int forumId, Model model) {
		EditForumForm editForum = new EditForumForm();
		editForum.setForumId(forumId);
		
		model.addAttribute("editForum", editForum);
		return "edit_Forum_page";
	}
	/**
	 * This method will process the actions in the forum's editing page.
	 * */
	@PostMapping("editForum")
	public String editForum(@Valid @ModelAttribute("editForum") EditForumForm editForum,
							BindingResult bindingResult, Authentication authentication,
							Model model) {
		Forum forum = forumService.findFourmById(editForum.getForumId());
		
		// Making sure that Admin in action
		if(userService.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			editService.updateForum(forum, editForum);
		else
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "editForum");
		
		return "redirect:controlPanel" + '#' + forum.getId();
	}
	
	/**
	 *  This method will set forumId to a higher priority level.
	 *  */
	@GetMapping("addPriority/{forumId}")
	public String addForumPriority(@PathVariable int forumId) {
		controlService.updatePriorityUp(forumId);
		
		return "redirect:/a/controlPanel" + '#' + forumId;
	}
	/**
	 *  This method will set forumId to a lower priority level.
	 *  */
	@GetMapping("downPriority/{forumId}")
	public String downForumPriority(@PathVariable int forumId) {
		controlService.updatePriorityDown(forumId);
		
		return "redirect:/a/controlPanel" + '#' + forumId;
	}
	/**This method will delete a forum
	 * A forum can't be deleted until all topics attached to it are exists*/
	@GetMapping("forum/delete/{forumId}")
	public String deleteForum(@PathVariable int forumId, Authentication authentication,
								RedirectAttributes model) {
		Forum forum = forumService.findFourmById(forumId);
		// Making sure that Admin in action
		if(userService.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN")) {
			deleteService.deleteForum(forum);
			model.addFlashAttribute("message", "Forum has been removed.");	
		}
		else
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "forum/delete/" + forumId);
		return "redirect:/a/controlPanel";
	}
	
	/* ######################################################
	 * User administration section
	 * ######################################################*/
	
	/** This method will return List<User> of all users in database and display it. */
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/list_users")
	public String listofUsers(Model model) {
		// Display all useres by role rank.
		model.addAttribute("listofUsers", userService.findAllUsersByRoleAsc());
		return "users";
	}
	
	/**
	 *  This method will display to Admin an User editing page.
	 *  Admin can edit user's role rank or delete User.*/
	@RequestMapping("/editUser")
	ModelAndView showUserEditForm(@RequestParam(name = "username") String username) {
		ModelAndView mav = new ModelAndView("edit_User_form");
		
		List<Role> roles = roleServices.findAll();
		EditUserForm editUser = null;
		try {
			editUser = controlService.editUserForm(username);
		} catch (NullPointerException e) {
			throw new EntityRequestException("Could not find user :: " + username);
		}

		mav.addObject("editUser",editUser);
		mav.addObject("roles",roles);
		
		return mav;
	}
	/**
	 *  This method will process all admin actions in user editing page.
	 *  */
	@RequestMapping("/updateUser")
	public String updateUser(@Valid @ModelAttribute("editUser") EditUserForm editUser, BindingResult bindingResult, 
								Authentication authentication, Model mode) {
		
		User user = userService.findUserByUserId(editUser.getId());

		// Checking if user is exists
		if(authentication == null || user == null) {
			throw new EntityRequestException("Could not update User :: " + editUser.getUsername());
		}
		if(!userService.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "updateUser" + editUser.getUsername());
		
		// Checking if need to update role
		if(!user.getRole().getName().equals(editUser.getRole()) && !editUser.getRole().equals("UNDEFINED_USER")) {
			if(editUser.getRole().equals(roleServices.findRoleByName("BLOCKED").getName()))
				editService.setUserBlocked(user);
			// Updating user's role
			else 
				editService.updateUserRole(editUser);
		}
		
		// Checking if needed to delete user
		if(editUser.isDelete()) {
			// first block user
			editService.setUserBlocked(user);
			// Deleting user
			deleteService.deleteUser(editUser);
		}
		// if user still admin, then return to control panel
		if(userService.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			return "redirect:/a/controlPanel";
		
		// Otherwise (if they demoted themselves) log them out. 
		else
			return "redirect:/logout";
	}
	
	/** This method will remove a User entity from database.
	 * 	This method will not delete an Admin user or dummy User.
	 * 	In default all user's posts and topics will not removed, but will be attched to an dummy user.
	 * 	to delete all user's activity put SearchUserForm.keepActivity=false.
	 * */
	@GetMapping("user/delete/{username}")
	public String deleteUser(@PathVariable String username, Authentication authentication,
								RedirectAttributes model) {
		User user = userService.findUserByUsername(username);
		
		// Checking if user exists
		if(authentication == null || user == null) {
			throw new EntityRequestException("Could not find User :: " + username);
		}
		else if(!userService.findUserByUsername(authentication.getName()).getRole().getName().equals("ADMIN"))
			accessDeniedRequestException.throwNewAccessDenied(authentication.getName(), localUrl + "user/delete/" + username);
		
		deleteService.deleteUser(username);
		return "/controlPanel/";
	}
	
	
	/* ######################################################
	 * Homepage administration section
	 * ######################################################*/
	
	/**
	 * This method will update the forum's information and configurations.
	 * This method will not updated an information if there are
	 * no changes.
	 * */
	@PostMapping("updateForumInformation")
	public String updateForumInformation(@Valid @ModelAttribute("updatedInformation") ForumInformation updatedInformation, BindingResult bindingResult, 
													Authentication authentication, Model mode) {
		forumInformationServices.updateForumInformation(updatedInformation);
		return "redirect:/a/controlPanel";
	}
	
}
