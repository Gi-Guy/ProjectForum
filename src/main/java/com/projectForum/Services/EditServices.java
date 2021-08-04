package com.projectForum.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.EditUserForm;
import com.projectForum.Security.Role;
import com.projectForum.Security.RoleRepository;
import com.projectForum.forum.EditForumForm;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.EditPostForm;
import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.NewTopicPageForm;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;



//TODO TEST THIS FILE
/* ###########################################################
* 		WARNING: THIS SERVICE FILE ISN'T TESTED YET!
* ###########################################################*/

/** This class will use as a service to all edit actions in the application*/
@Service
public class EditServices {

	private UserRepository	userRepo;
	private PostRepository	postRepo;
	private TopicRepository	topicRepo;
	private ForumRepository	forumRepo;
	private RoleRepository	roleRepo;
	
	@Autowired
	public EditServices(UserRepository userRepo, PostRepository postRepo, TopicRepository topicRepo,
			ForumRepository forumRepo, RoleRepository	roleRepo) {
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.topicRepo = topicRepo;
		this.forumRepo = forumRepo;
		this.roleRepo = roleRepo;
	}
	
	/** This method will update exists post with new Content.
	 * 	In case that the content is blank, there will be no update.
	 * @param Post to update
	 * @param EditPostForm new Content*/
	public void updatePost(Post post, EditPostForm editPostForm) {
		
		// Checking if there is a new content to update
		if(!editPostForm.getContent().isBlank()) 
			post.setContent(editPostForm.getContent());
		
		postRepo.save(post);
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
		
		topicRepo.save(topic);
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
		
		forumRepo.save(forum);
	}
	
	/**	This method will update exists User with new user information.
	 * 	In case that there is any blank information, there will be no update.
	 * 	@param User to update.
	 * 	@param */
	public void updateUser(User user) {
		
		userRepo.save(user);
	}
	
	/** This method will update user's roles to editUser role traget.
	 * */
	public void updateUserRole(EditUserForm editUser) {
		Role role = roleRepo.findRoleByName(editUser.getRole());
		User user = userRepo.findUserById(editUser.getId());
		
		user.removeRole();
		user.setRole(role);
		userRepo.save(user);
	}
}
