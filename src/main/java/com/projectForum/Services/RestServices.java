package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectForum.REST.AddUserForm;
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
