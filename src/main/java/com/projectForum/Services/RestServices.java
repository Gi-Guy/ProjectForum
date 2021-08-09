package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectForum.REST.AddUserForm;
import com.projectForum.REST.DeleteUserForm;
import com.projectForum.REST.UpdateUser;
import com.projectForum.Security.Role;
import com.projectForum.Security.RoleRepository;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

/**
 *  This class will gives services to the Rest controller.
 *  */

@Service
public class RestServices {

	@Autowired
	private UserRepository	userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository	roleRepo;
	@Autowired
	private UserServices	userServices;
	@Autowired
	private ForumRepository	forumRepo;
	@Autowired
	private TopicRepository	topicRepo;
	@Autowired
	private PostRepository	postRepo;
	@Autowired
	private DeleteService	deleteService;
	@Autowired
	private EditServices	editServices;
	
	
	/*
	 * ################################################################
	 * 							USERS
	 * ################################################################
	 * */
	/**
	 *  This method will return a list of user, order by roles (Admins first)
	 *  */
	public List<User> getAllUsers(){
		List<User> users = userRepo.findByOrderByRolesAsc();
		
		return users;
	}
	
	/**
	 *	This method will return a User object by username String.
	 **/
	public User getUser(String username) {
		User user = userRepo.findByUsername(username);
		
		return user;
	}
	
	/**
	 * 	This method will return an example form of AddUserForm.
	 * */
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
	 * 	This method will add a new user from JSON to database and return a new user.
	 * */
	public User addNewUser(AddUserForm addUser) {
		return userServices.addNewUser(addUser);
	}
	
	/**
	 * 	This method will give a user an example to remove user.
	 * */
	public DeleteUserForm removeUserExample() {
		DeleteUserForm deleteUser = new DeleteUserForm();
		deleteUser.setUsername("example username");
		deleteUser.setUserId(0);
		deleteUser.setKeepActivity(true);
		
		return deleteUser;
	}
	
	/**
	 * 	This method will delete a user by username.
	 * 
	 * @return boolean true if user deleted*/
	public boolean removeUser(DeleteUserForm deleteUser) {
		return deleteService.deleteUser(deleteUser);
	}
	
	
	/**
	 * 	This method will return an User object via UpdateUser object.
	 * 
	 * @param 	updateUser
	 * @return	User*/
	public User findUserByUpdateUser(UpdateUser updateUser) {
		User findUser = null;
		
		// Need to find the user by id/username/email
		
		// Looking for user by id 
		findUser = userRepo.findUserById(updateUser.getId());
		// is user exist?
		if(findUser != null) 
			return findUser;
		
		// Looking for user by username
		findUser = userRepo.findByUsername(updateUser.getUsername());
		// is user exist?
		if(findUser != null)
			return findUser;
			
		// Looking for user by email 
		findUser = userRepo.findByEmail(updateUser.getEmail());
		// is user exist?
		if(findUser != null) 
			return findUser;
		
		return null;
	}
	/**
	 * This method will update an exists user.
	 * 
	 * @return boolean false if user isn't exists.*/
	
	public boolean updateUser(UpdateUser updateUser) {
		User findUser = null;
		
		// Need to find the user by id/username/email and then find out what new.
		// Looking for user by id 
		findUser = userRepo.findUserById(updateUser.getId());
		// is user exist?
		if(findUser != null) {
			this.updateUser(findUser, updateUser);
			return true;
		}
		// Looking for user by username
		findUser = userRepo.findByUsername(updateUser.getUsername());
		// is user exist?
		if(findUser != null) {
			this.updateUser(findUser, updateUser);
			return true;
		}
		
		// Looking for user by email 
		findUser = userRepo.findByEmail(updateUser.getEmail());
		// is user exist?
		if(findUser != null) {
			this.updateUser(findUser, updateUser);
			return true;
		}
		
		// user's isn't exists!
		return false;
	}
	
	/**
	 * 	This method will update target user with new information from user object.*/
	private void updateUser(User target, UpdateUser updateUser) {
		// At this point user must be exists.
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
			Role role = roleRepo.findRoleByName(updateUser.getRole());
			user.setRole(role);
		}
		else if(target.getRole()==null) {
			Role role = roleRepo.findRoleByName("USER");
			user.setRole(role);
		}
		else user.setRole(target.getRole());
		
		// Sending new user information to editUser
		editServices.updateUser(user);
	}
	
	/**
	 *  This method will return an example form for updating user.
	 *  
	 *  @return UpdateUser*/
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
	 * */
	/**
	 *  This method will return a list of forums, order by priority
	 *  */
	public List<Forum> getAllForums(){
		List<Forum> forums = forumRepo.findByOrderByPriorityAsc();
		
		return forums;
	}
	/**
	 *	This method will return a Forum object by forumId
	 **/
	public Forum getForumById(int forumId) {
		Forum forum = forumRepo.findById(forumId);
		return forum;
	}
	/**
	 *	This method will return a Forum object by forum priority
	 **/
	public Forum getForumByPriority(int priority) {
		Forum forum = forumRepo.findByPriority(priority);
		return forum;
	}
	/**
	 * 	This method will return an example form of Forum
	 * */
	public String getExampleForum() {
		final String example = "{\n"
						+ " \"name\":" + "\"Example\","
						+ " \"description\":" + "\" Example\""
						+ "}";
							
		
		return example;
	}
	
	/**
	 * 	This method will add a new forum to the database
	 * */
	public Forum addNewForum(Forum forum) {
		
		// Checking if name or description are blanked
		if(forum.getName().isBlank() || forum.getDescription().isBlank())
			return null;
		
		//Each forum must have a priority value, 1 is the lowest.
		
		List<Forum> forums = forumRepo.findAll();
		if(forums.isEmpty()) {
			forum.setPriority(1);	
		}
		else {
			forum.setPriority(forums.size() + 1);	
		}
		forumRepo.save(forum);
		return forumRepo.findByPriority(forums.size() + 1);
	}
	
	/**
	 * 	This method will delete a Forum by forumId.
	 * */
	public void deleteForum(int forumId) {
		deleteService.deleteForum(forumId);
	}
	/*
	 * ################################################################
	 * 							TOPICS
	 * ################################################################
	 * */
	/**
	 *  This method will return a list of topics
	 *  */
	public List<Topic> getAllTopics(){
		List<Topic> topics = topicRepo.findAll();
		
		return topics;
	}
	
	/**
	 * 	This method will return a topic by topicId
	 * */
	public Topic getTopicById(int topicId) {
		Topic topic = topicRepo.findTopicById(topicId);
		
		return topic;
	}
	/*
	 * ################################################################
	 * 							POSTS
	 * ################################################################
	 * */
	/**
	 *  This method will return a list of posts
	 *  */
	public List<Post> getAllPosts(){
		List<Post> posts = postRepo.findAll();
		
		return posts;
	}
	/**
	 * 	This method will return a post by postId
	 * */
	public Post getPostById(int postId) {
		Post post = postRepo.findById(postId);
		
		return post;
	}
}
