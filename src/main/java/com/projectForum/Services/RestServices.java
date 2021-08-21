package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.Configuration.ForumInformation;
import com.projectForum.REST.AddUserForm;
import com.projectForum.REST.DeleteUserForm;
import com.projectForum.REST.EditForumForm;
import com.projectForum.REST.UpdateUser;
import com.projectForum.Security.Role;
import com.projectForum.forum.Forum;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;


/**
 * This class will gives services to the Rest controller.
 */

@Service
public class RestServices {

	private RoleServices	roleServices;
	private UserServices	userServices;
	private ForumServices	forumServices;
	private TopicServices	topicServices;
	private PostServices	postServices;
	private DeleteService	deleteService;
	private EditServices	editServices;
	private ForumInformationServices	forumInformationServices;
	
	
	@Autowired
	public RestServices(RoleServices roleServices, UserServices userServices, ForumServices forumServices,
			TopicServices topicServices, PostServices postServices, DeleteService deleteService,
			EditServices editServices, ForumInformationServices forumInformationServices) {
		this.roleServices = roleServices;
		this.userServices = userServices;
		this.forumServices = forumServices;
		this.topicServices = topicServices;
		this.postServices = postServices;
		this.deleteService = deleteService;
		this.editServices = editServices;
		this.forumInformationServices = forumInformationServices;
	}

	/*
	 * ################################################################
	 * 						Forums Configurations
	 * ################################################################
	 */
	
	/**
	 * This method will return the current configurations of the application.
	 */
	public ForumInformation getForumConfigurations() {
		return forumInformationServices.getForumInformation();
	}
	
	/**
	 * This method will update the current application configurations with new configurations.
	 * Note: user can not change the id, if user will try to change the id,
	 * the services will not save the new configurations.
	 */
	public ForumInformation updateForumConfigurations(ForumInformation updatedInformation) {
		return forumInformationServices.updateAndReturn(updatedInformation);
		
	}
	
	/*
	 * ################################################################
	 * 							USERS
	 * ################################################################
	 */
	
	/**
	 * This method will return a list of user, order by roles (Admins first)
	 */
	public List<User> getAllUsers(){
		List<User> users = userServices.findAllUsersByRoleAsc();
		
		return users;
	}
	
	/**
	 * This method will return a User object by its username.
	 */
	public User getUser(String username) {
		User user = userServices.findUserByUsername(username);
		
		return user;
	}
	
	/**
	 * This method will return an example form of AddUserForm.
	 */
	public AddUserForm getExampleUser() {
		AddUserForm addUser = new AddUserForm();
		addUser.setUsername("Example");
		addUser.setFirstName("Example");
		addUser.setLastName("Example");
		addUser.setEmail("Example@Example.Example");
		addUser.setPassword("password");
		
		return addUser;
	}
	
	/**
	 * This method will add a new user from JSON to database and return a new user.
	 */
	public User addNewUser(AddUserForm addUser) {
		return userServices.addNewUser(addUser);
	}
	
	/**
	 * This method will delete an example user.
	 */
	public DeleteUserForm removeUserExample() {
		DeleteUserForm deleteUser = new DeleteUserForm();
		deleteUser.setUsername("example username");
		deleteUser.setUserId(0);
		deleteUser.setKeepActivity(true);
		
		return deleteUser;
	}
	
	/**
	 * This method will delete a user by their username.
	 * @return boolean true if user deleted
	 */
	public boolean removeUser(DeleteUserForm deleteUser) {
		return deleteService.deleteUser(deleteUser);
	}
	
	/**
	 * This method will return an User object via UpdateUser object.
	 * @param 	updateUser
	 * @return	User
	 */
	public User findUserByUpdateUser(UpdateUser updateUser) {
		User findUser = null;
		
		// Need to find the user by id/username/email
		
		// Looking for user by id 
		findUser = userServices.findUserByUserId(updateUser.getId());
		// is user exist?
		if(findUser != null) 
			return findUser;
		
		// Looking for user by username
		findUser = userServices.findUserByUsername(updateUser.getUsername());
		// is user exist?
		if(findUser != null)
			return findUser;
			
		// Looking for user by email 
		findUser = userServices.findUserByUserEmail(updateUser.getEmail());
		// is user exist?
		if(findUser != null) 
			return findUser;
		
		return null;
	}
	
	/**
	 * This method will update an existing user.
	 * @return boolean false if user doesn't exist.
	 */
	public boolean updateUser(UpdateUser updateUser) {
		User findUser = null;
		
		// Need to find the user by id/username/email and then find out what new.
		// Looking for user by id 
		findUser = userServices.findUserByUserId(updateUser.getId());
		// is user exist?
		if(findUser != null) {
			this.updateUser(findUser, updateUser);
			return true;
		}
		// Looking for user by username
		findUser = userServices.findUserByUsername(updateUser.getUsername());
		// is user exist?
		if(findUser != null) {
			this.updateUser(findUser, updateUser);
			return true;
		}
		
		// Looking for user by email 
		findUser = userServices.findUserByUserEmail(updateUser.getEmail());
		// is user exist?
		if(findUser != null) {
			this.updateUser(findUser, updateUser);
			return true;
		}
		
		// user doesn't exist!
		return false;
	}
	
	/**
	 * 	This method will update target user with new information from user object.
	 */
	private void updateUser(User target, UpdateUser updateUser) {
		// At this point user must exist.
		// To use EditServices updateUser must be transform into USER object.
		User user = new User();
		user.setId(target.getId());
		user.setUsername(updateUser.getUsername());
		user.setEmail(updateUser.getEmail());
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		user.setPassword(updateUser.getPassword());
		
		// Setting Role
		if(!updateUser.getRole().isBlank()) {
			Role role = roleServices.findRoleByName(updateUser.getRole());
			user.setRole(role);
		}
		else if(target.getRole()==null) {
			Role role = roleServices.findRoleByName("USER");
			user.setRole(role);
		}
		else user.setRole(target.getRole());
		
		// Sending new user information to editUser
		editServices.updateUser(user);
	}
	
	/**
	 * This method will return an example form for updating user.
	 * @return UpdateUser
	 */
	public UpdateUser updateUserExample() {
		UpdateUser updateUser = new UpdateUser();
		updateUser.setId(0);
		updateUser.setUsername("example username");
		updateUser.setEmail("Example@Example.example");
		updateUser.setFirstName("Example firstName");
		updateUser.setLastName("Example lastName");
		updateUser.setRole("ADMIN/USER");
		updateUser.setPassword("Example password");
		
		return updateUser;
	}
	
	/*
	 * ################################################################
	 * 							FORUMS
	 * ################################################################
	 */
	/**
	 * This method will return a list of forums, order by priority
	 */
	public List<Forum> getAllForums(){
		List<Forum> forums = forumServices.findForumsByPriorityAsc();
		
		return forums;
	}
	
	/**
	 * This method will return a Forum object by forumId
	 */
	public Forum getForumById(int forumId) {
		Forum forum = forumServices.findFourmById(forumId);
		return forum;
	}
	
	/**
	 * This method will return a Forum object by forum priority
	 */
	public Forum getForumByPriority(int priority) {
		Forum forum = forumServices.findForumByPriority(priority);
		return forum;
	}
	
	/**
	 * This method will return an example form of Forum
	 */
	public String getExampleForum() {
		final String example = "{\n"
						+ " \"name\":" + "\"Example\","
						+ " \"description\":" + "\" Example\""
						+ "}";
							
		return example;
	}
	
	/**
	 * This method will add a new forum to the database
	 */
	public Forum addNewForum(Forum forum) {
		
		// Checking if name or description are blanked
		if(forum.getName().isBlank() || forum.getDescription().isBlank())
			return null;
		
		// Each forum must have a priority value, 1 is the lowest.
		
		List<Forum> forums = forumServices.findAll();
		if(forums.isEmpty()) {
			forum.setPriority(1);	
		}
		else {
			forum.setPriority(forums.size() + 1);	
		}
		forumServices.save(forum);
		return forumServices.findForumByPriority(forums.size() + 1);
	}
	
	/**
	 * This method will return to user an example of editforum form.
	 * @return EditForumForm
	 */
	public EditForumForm updateForumExample() {
		EditForumForm newEdit = new EditForumForm();
		
		newEdit.setForumId(0);
		newEdit.setName("Example Name");
		newEdit.setDescription("Example Description");
		
		return newEdit;
	}
	
	/**
	 * This method will update an existing forum.
	 */
	public boolean updateforum(EditForumForm updateForum) {
		
		// Checking if forum id is valid
		if(updateForum.getForumId() == 0) {
			return false;
		}
		
		Forum forum = forumServices.findFourmById(updateForum.getForumId());
			
		if(forum!=null) {
			editServices.updateForum(forum, updateForum);
			return true;
		}
		return false;
	}
	
	/**
	 * This method will delete a Forum by forumId.
	 */
	public void deleteForum(int forumId) {
		deleteService.deleteForum(forumId);
	}
	
	/*
	 * ################################################################
	 * 							TOPICS
	 * ################################################################
	 */
	
	/**
	 * This method will return a list of topics
	 */
	public List<Topic> getAllTopics(){
		List<Topic> topics = topicServices.findAll();
		return topics;
	}
	
	/**
	 * This method will return a topic by topicId
	 */
	public Topic getTopicById(int topicId) {
		Topic topic = topicServices.findTopicById(topicId);
		return topic;
	}
	
	/**
	 * This method will delete an existing Topic with all it posts.
	 * @param Int topicId.
	 * @return boolean false if topic doesn't exist.
	 */
	public boolean deleteTopic(int topicId) {
		
		// Checking if topic exists
		Topic topic = topicServices.findTopicById(topicId);
		
		if(topic != null) {
			deleteService.deleteTopic(topic);
			return true;
		}
		return false;
	}
	
	/*
	 * ################################################################
	 * 							POSTS
	 * ################################################################
	 */
	
	/**
	 * This method will return a list of posts
	 */
	public List<Post> getAllPosts(){
		List<Post> posts = postServices.findAll();
		return posts;
	}
	
	/**
	 * This method will return a post by postId
	 */
	public Post getPostById(int postId) {
		Post post = postServices.findPostById(postId);
		return post;
	}
	
	/**
	 * This method will delete an existing post.
	 * @param Int postId.
	 * @return boolean false if post doesn't exist.
	 */
	public boolean deletePost(int postId) {
		
		// Checking if topic exists
		Post post = postServices.findPostById(postId);
		
		if(post != null) {
			deleteService.deletePost(post);
			return true;
		}
		return false;
	}
}