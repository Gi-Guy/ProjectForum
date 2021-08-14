package com.projectForum.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.EditUserForm;
import com.projectForum.Security.Role;
import com.projectForum.Security.RoleRepository;
import com.projectForum.forum.EditForumForm;
import com.projectForum.forum.Forum;
import com.projectForum.post.EditPostForm;
import com.projectForum.post.Post;
import com.projectForum.topic.NewTopicPageForm;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;



//TODO TEST THIS FILE
/* ###########################################################
* 		WARNING: THIS SERVICE FILE ISN'T TESTED YET!
* ###########################################################*/

/** This class will use as a service to all edit actions in the application*/
@Service
public class EditServices {

	private UserServices	userServices;
	private PostServices	postServices;
	private TopicServices	topicServices;
	private ForumServices	forumServices;
	private RoleRepository	roleRepo;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public EditServices(UserServices userServices, PostServices postServices, TopicServices topicServices,
			ForumServices forumServices, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
		this.userServices = userServices;
		this.postServices = postServices;
		this.topicServices = topicServices;
		this.forumServices = forumServices;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}

	
	/** This method will update exists post with new Content.
	 * 	In case that the content is blank, there will be no update.
	 * @param Post to update
	 * @param EditPostForm new Content*/
	public void updatePost(Post post, EditPostForm editPostForm) {
		
		// Checking if there is a new content to update
		if(!editPostForm.getContent().isBlank()) 
			post.setContent(editPostForm.getContent());
		
		postServices.save(post);
	}
	

	/** This method will update exists topic with new Content and new Title.
	 * 	In case that the Title or Contect are blank, there will be no update.
	 *  @param Topic to update
	 *  @param NewTopicPageForm*/
	public void updateTopic(Topic topic, NewTopicPageForm newTopic) {
		
		// Checking id there is a new title to update
		if(!newTopic.getTitle().isBlank())
			topic.setTitle(newTopic.getTitle());
		
		// Checking if there is a new content to update
		if (!newTopic.getContent().isBlank())
			topic.setContent(newTopic.getContent());
		
		topicServices.save(topic);
	}
	
	/** This method will update exists Forum with new Name and New Description.
	 * 	In case that the Name or Description are blank, there will be no update.
	 * 	@param Forum to update
	 * 	@param */
	public void updateForum(Forum forum, EditForumForm editForum) {
		
		// Checking if there is a new name or description to update
		if(!editForum.getName().isBlank())
			forum.setName(editForum.getName());
		
		if(!editForum.getDescription().isBlank())
			forum.setDescription(editForum.getDescription());
		
		forumServices.save(forum);
	}
	
	/**	This method will update exists User with new user information.
	 * 	In case that there is any blank information, there will be no update.
	 * 	@param User to update.
	 * 	@param */
	public void updateUser(User updateUser) {
		// Checking if user is exists
		User targetUser = userServices.findUserByUserId(updateUser.getId());
		
		if (targetUser == null) {
			// TODO add exciption
			System.err.println("ERROR: USER ISN'T EXISTS!");
		}
		
		// User is exists:
		// Checking if users has different information
		if(!targetUser.equals(updateUser)) {
			
			// ###Nor user or admin can change user's username.###
			
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
	
	
	/** This method will update user's roles to editUser role traget.
	 * @param EditUserForm
	 * */
	public void updateUserRole(EditUserForm editUser) {
		Role role = roleRepo.findRoleByName(editUser.getRole());
		User user = userServices.findUserByUserId(editUser.getId());
		
		user.removeRole();
		user.setRole(role);
		userServices.save(user);
	}
}
