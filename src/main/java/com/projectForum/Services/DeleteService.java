package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.EditUserForm;
import com.projectForum.REST.DeleteUserForm;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;


//TODO TEST THIS FILE
/* ###########################################################
* 		WARNING: THIS SERVICE FILE ISN'T TESTED YET!
* ###########################################################*/


/**
 *  This Class will use as a service to all deletion option in the application */

@Service
public class DeleteService {
	
	private UserRepository	userRepo;
	private PostRepository	postRepo;
	private TopicRepository	topicRepo;
	private ForumRepository	forumRepo;
	
	@Autowired
	public DeleteService(UserRepository userRepo, PostRepository postRepo, TopicRepository topicRepo,
			ForumRepository forumRepo) {
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.topicRepo = topicRepo;
		this.forumRepo = forumRepo;
	}
	/** This method will delete an exists user.
	 *  It will remove user's data dependent on Keep activity boolean.
	 *  
	 *  @see REST ONLY
	 *  @return boolean true if deleted user.*/
	public boolean deleteUser(DeleteUserForm deleteUser) {
		User userByID = null;
		User userByUsername = null;
		if (deleteUser.getUserId() != 0)
			userByID = userRepo.findUserById(deleteUser.getUserId());
		if(!deleteUser.getUsername().isBlank())
			userByUsername = userRepo.findByUsername(deleteUser.getUsername());
		
		// Checking if username and ID lead to same user
		if(userByID!=null && userByUsername!=null) {
			if(!userByID.equals(userByUsername))
				// ID and username are not the same user!
				return false;
		}
		
		// Checking if both are null
		if(userByID == null && userByUsername == null) {
			// User isn't exists!
			return false;
		}
		// At this point one of the users isn't null
		if(userByUsername != null) {
			if(deleteUser.isKeepActivity())
				this.deleteUserKeepActivity(userByUsername);
			else
				this.deleteUserDontKeepActivity(userByUsername);	
		}
		else if(userByID != null) {
			if(deleteUser.isKeepActivity())
				this.deleteUserKeepActivity(userByID);
			else
				this.deleteUserDontKeepActivity(userByID);
		}
		
		return true;
	}
	
	/** This method will delete an exists user.
	 *  It will remove user's data dependent on Keep activity boolean.*/
	public void deleteUser(EditUserForm editUser) {
		User user = userRepo.findUserById(editUser.getId());
		
		if(user == null)
			return;
		
		if(editUser.isKeepActivity())
			this.deleteUserKeepActivity(user);
		else
			this.deleteUserDontKeepActivity(user);
	}
	
	/** This method will delete an exists user.
	 * 	However it won't delete it posts and topics, but will attached user's posts and topics
	 * 	to an Dummy User.
	 * 
	 * @param User
	 * */
	private void deleteUserKeepActivity(User user) {
		
		User dummyUser = userRepo.findByUsername("Unknown");
		
		// Making sure that dummy user won't be deleted or Admin user
		if(dummyUser.equals(user) || user.getRoles().iterator().next().getName().equals("ADMIN"))
			return;
		
		// Removing Role (many To many, need to be removed)
		user.removeRole();
		
		// Change the topic and post to dummy owner.
		List<Post>	userPosts	=	postRepo.findPostsByUser(user);
		List<Topic>	userTopics	=	topicRepo.findTopicsByUser(user);
		
		// posts:
		if(!userPosts.isEmpty())
			for (int i=0; i<userPosts.size(); i++) {
				userPosts.get(i).setUser(dummyUser);
			}
		// topics:
		if(!userTopics.isEmpty())
			for (int i=0; i<userTopics.size(); i++) {
				userTopics.get(i).setUser(dummyUser);
			}
		
		userRepo.delete(user);
	}
	
	/** This method will delete an exists user.
	 * 	it will also delete it posts and topics.
	 * 	to an Dummy User.
	 * 
	 * @param User
	 * */
	private void deleteUserDontKeepActivity(User user) {
		List<Post>	userPosts	=	postRepo.findPostsByUser(user);
		List<Topic>	userTopics	=	topicRepo.findTopicsByUser(user);
		
		User dummyUser = userRepo.findByUsername("Unknown");
		
		// Making sure that dummy user won't be deleted or Admin user
		if(dummyUser.equals(user) || user.getRoles().iterator().next().getName().equals("ADMIN"))
			return;
		
		// Removing Role (many To many, need to be removed)
		user.removeRole();
		
		// Removing all posts and topics
		//posts:
		if(!userPosts.isEmpty()) {
			this.deletePosts(userPosts);	
		}
		//topics:
		if(!userTopics.isEmpty()) {
			this.deleteTopics(userTopics);	
		}
		// Removing user
		userRepo.delete(user);
	}

	/** This method will delete an exists user.
	 * 	However it won't delete it posts and topics, but will attached user's posts and topics
	 * 	to an Dummy User.
	 * 
	 * @param String
	 * */
	public void deleteUser(String username) {
		this.deleteUserKeepActivity(userRepo.findByUsername(username));
	}
	
	/** This method will delete an exists user.
	 * 	However it won't delete it posts and topics, but will attached user's posts and topics
	 * 	to an Dummy User.
	 * 
	 * @param int
	 * */
	public void deleteUser(int userId) {
		this.deleteUserKeepActivity(userRepo.findUserById(userId));
	}
	
	/** This method will delete a post by postId
	 * @param int postId*/
	public void deletePost (int postId) {
		Post post = postRepo.findById(postId);
		postRepo.delete(post);
	}
	
	/** This method will delete a post by Post object.
	 * @param Post - post to delete*/
	public void deletePost(Post post) {
		postRepo.delete(post);
	}
	
	/** This method will delete a list of posts by List<Post>.
	 * @param List<Post>*/
	
	public void deletePosts(List<Post> posts) {

		if(posts.isEmpty())
			return;
		for(int i=0; i<posts.size(); i++)
			this.deletePost(posts.get(i));
			
	}
	
	/** This method will delete a topic and it posts by topicId.
	 * @param int topicId*/
	public void deleteTopic(int topidId) {
		Topic topic = topicRepo.findTopicById(topidId);
		
		this.deleteTopic(topic);
	}
	/** This method will delete a topic and it posts by topic object.
	 * @param Topic*/
	public void deleteTopic(Topic topic) {

		// Get all topic's posts
		List<Post> posts = postRepo.findPostsByTopic(topic);
		
		// If post != null then send it to deletion procces
		if(!posts.isEmpty())
			this.deletePosts(posts);
		
		// Topic has no posts, removing posts
		topicRepo.delete(topic);
	}
	
	/** This method will delete list of topics by List<Topic>
	 *  @param List<Topic>*/
	public void deleteTopics(List<Topic> topics) {

		if(topics.isEmpty())
			return;
		
		for (int i=0; i<topics.size(); i++)
			this.deleteTopic(topics.get(i));
	}
	
	/** This method will delete a forum by forumId
	 *  @param int forumId*/
	public void deleteForum(int forumId) {
		Forum forum = forumRepo.findById(forumId);
		this.deleteForum(forum);
		
	}
	/** This method will delete a forum by forum object
	 *  @param Forum*/
	public void deleteForum(Forum forum) {
		if(forum == null)
			return;
		this.updateAllLowerPriority(forum);
		// Get all topics in Forum
		List<Topic> topics = topicRepo.findTopicsByForum(forum);
		
		// Delete all topics in forum
		this.deleteTopics(topics);
		
		// Forum has no topics, removing forum
		forumRepo.delete(forum);
	}
	
	/** This method will delete a list of forums by List<forum>*/
	public void deleteForums(List<Forum> forums) {

		if(forums.isEmpty())
			return;
		for(int i=0; i<forums.size(); i++)
			this.deleteForum(forums.get(i));
	}
	
	/** This method will delete all the forums + Topics + Posts in database.
	 * @warning THIS CAN'T BE UNDONE
	 * 
	 * There is a bug in this method, it isn't working.*/
	public void deleteAllForums() {
		
		List<Forum> forums = forumRepo.findAll();
		this.deleteForums(forums);
		
	}
	
	/**
	 * When deleting a forum there is need to update all lower priority */
	private void updateAllLowerPriority(Forum forum) {
		final int forumsSize = forumRepo.findByOrderByPriorityAsc().size();
		
		// if true then no need to update any other forum's priority
		if (forum.getPriority() == forumsSize)
			return;
		// forum isn't in last priority, need to update all lower priority forums
		
		for (int i = forum.getPriority() + 1; i <= forumsSize; i++) {
			Forum updateForum = forumRepo.findByPriority(i);
			updateForum.setPriority(i - 1);
			forumRepo.save(updateForum);
		}
	}
}
