package com.projectForum.Services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.EditUserForm;
import com.projectForum.REST.EditForumForm;
import com.projectForum.Security.Role;
import com.projectForum.forum.Forum;
import com.projectForum.post.EditPostForm;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;


/** This class will use as a service to all edit actions in the application */
@Service
public class EditServices {

	private UserServices	userServices;
	private PostServices	postServices;
	private TopicServices	topicServices;
	private ForumServices	forumServices;
	private RoleServices	roleServices;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public EditServices(UserServices userServices, PostServices postServices, TopicServices topicServices,
			ForumServices forumServices, RoleServices roleServices, PasswordEncoder passwordEncoder) {
		this.userServices = userServices;
		this.postServices = postServices;
		this.topicServices = topicServices;
		this.forumServices = forumServices;
		this.roleServices = roleServices;
		this.passwordEncoder = passwordEncoder;
	}

	
	/** 
	 * This method will update an existing post with new Content.
	 * In case that the content is blank, there will be no update.
	 * @param Post to update
	 * @param EditPostForm new Content
	 */
	public void updatePost(Post post, EditPostForm editPostForm) {
		
		// Checking if there is a new content to update
		if(!editPostForm.getContent().isBlank()) 
			post.setContent(editPostForm.getContent());
		
		postServices.save(post);
	}
	
	/** 
	 * This method will update an existing post with new Content.
	 * In case that the content is blank, there will be no update.
	 * @param target to update
	 * @param post new Content
	 */
	public void updatePost(Post target, Post post) {
		
		// Checking if there is a new content to update
		if(!post.getContent().isBlank() && !target.getContent().equals(post.getContent())) 
			target.setContent(post.getContent());
		
		postServices.save(target);
	}

	/**
	 * This method will update a target topic with new Content and a new Title.
	 * In case that the Title or Content are blank, there will be no update.
	 * @param editTopic - will update target by Topic id.
	 */
	public void updateTopic(Topic editTopic) {
		Topic target = topicServices.findTopicById(editTopic.getId());
		// Checking id there is a new title to update
		if(!editTopic.getTitle().isBlank())
			target.setTitle(editTopic.getTitle());
		
		// Checking if there is new content to update
		if (!editTopic.getContent().isBlank())
			target.setContent(editTopic.getContent());
		
		topicServices.save(target);
	}
	
	/** 
	 * #REST#
	 * This method will update an existing Forum with a new Name and New Description.
	 * In case that the Name or Description are blank, there will be no update.
	 * @param Forum to update
	 */
	public void updateForum(Forum forum, EditForumForm editForum) {
		
		// Checking if there is a new name or description to update
		if(!editForum.getName().isBlank())
			forum.setName(editForum.getName());
		
		if(!editForum.getDescription().isBlank())
			forum.setDescription(editForum.getDescription());
		
		forumServices.save(forum);
	}
	/** 
	 * This method will update a target Forum with a new Name and New Description.
	 * In case that the Name or Description are blank, there will be no update.
	 * @param Forum to update
	 */
	public void updateForum(Forum editForum) {
		Forum target = forumServices.findFourmById(editForum.getId());
		// Checking if there is a new name or description to update
		if(!editForum.getName().isBlank())
			target.setName(editForum.getName());
		
		if(!editForum.getDescription().isBlank())
			target.setDescription(editForum.getDescription());
		
		forumServices.save(target);
	}
	
	/**	
	 * This method will update an existing User with new user information.
	 * In case that there is any blank information, there will be no update.
	 * @param User to update.
	 */
	public void updateUser(User updateUser) {
		// Checking if user exists
		User targetUser = userServices.findUserByUserId(updateUser.getId());
		
		if (targetUser == null) {
			System.err.println("ERROR: USER DOESN'T EXIST!");
		}
		
		// User exists:
		// Checking if users has different information
		if(!targetUser.equals(updateUser)) {
			
			// ### Nor user nor admin can change user's username. ###
			
			// updating email
			if(!targetUser.getEmail().equals(updateUser.getEmail())) {
				if(!updateUser.getEmail().isBlank())
					targetUser.setEmail(updateUser.getEmail());
			}
			// updating firstName
			if(!targetUser.getFirstName().equals(updateUser.getFirstName())) {
				if(!updateUser.getFirstName().isBlank())
					targetUser.setFirstName(updateUser.getFirstName());
			}
			
			// updating lastName
			if(!targetUser.getLastName().equals(updateUser.getLastName())) {
				if(!updateUser.getLastName().isBlank())
					targetUser.setLastName(updateUser.getLastName());
			}
			// updating password
			if(!updateUser.getPassword().isBlank()) {
				// Matching between current password to new password
				if(!passwordEncoder.matches(updateUser.getPassword(), targetUser.getPassword()))
					targetUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
			}
			// Updating Role
			if(!targetUser.getRole().equals(updateUser.getRole())) {
				targetUser.removeRole();
				targetUser.setRole(updateUser.getRole());
			}
					
			// Saving all changes
			userServices.save(targetUser);
		}
		
		// There are no new changes.
	}
	
	/**
	 * This method will update a user's last login date
	 * @param username
	 */
	public void setLastlogin(String username) {
		User user = userServices.findUserByUsername(username);
		user.setLastLogin(LocalDateTime.now());
		userServices.save(user);
	}
	
	
	/** 
	 * This method will update the user's role.
	 * @param EditUserForm
	 */
	public void updateUserRole(EditUserForm editUser) {
		Role role = roleServices.findRoleByName(editUser.getRole());
		User user = userServices.findUserByUserId(editUser.getId());
		
		user.removeRole();
		user.setRole(role);
		userServices.save(user);
	}
	
	/**
	 * This method will block a user
	 */
	public void setUserBlocked(User user) {
		Role blocked = roleServices.findRoleByName("BLOCKED");
		user.removeRole();
		user.setRole(blocked);
		userServices.save(user);
	}
}