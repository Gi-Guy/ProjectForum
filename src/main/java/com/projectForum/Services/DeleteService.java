package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

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
	
	/** This method will be used to delete a user*/
	public void deleteUser(int userId) {
		// TODO find a solution to delete user but not topics and posts
		User user = userRepo.findUserById(userId);
		
		userRepo.delete(user);
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
		while(!posts.isEmpty()) {
			this.deletePost(posts.get(0));
		}
			
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
		while(!topics.isEmpty()) {
			this.deleteTopic(topics.get(0));
		}
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
		// Get all topics in Forum
		List<Topic> topics = topicRepo.findTopicsByForum(forum);
		
		// Delete all topics in forum
		this.deleteTopics(topics);
		
		// Forum has no topics, removing forum
		forumRepo.delete(forum);
	}
	
	/** This method will delete a list of forums by List<forum>*/
	public void deleteForums(List<Forum> forums) {
		while(!forums.isEmpty()) {
			this.deleteForum(forums.get(0));
		}
	}
	
	/** This method will delete all the forums + Topics + Posts in database.
	 * @warning THIS CAN'T BE UNDONE*/
	public void deleteAll() {
		List<Forum> forums = forumRepo.findAll();
		this.deleteForums(forums);
		
	}
}
