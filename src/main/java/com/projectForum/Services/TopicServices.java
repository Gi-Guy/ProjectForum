package com.projectForum.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.forum.Forum;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicAdditionalInformation;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;


/**
 * 	This class will gives services for most topic's actions
 */

@Service
public class TopicServices {
	
	@Autowired
	private TopicRepository	topicRepo;
	@Autowired
	private ForumServices	forumServices;
	@Autowired
	private PostServices	postServices;
	
	/**
	 * This method will return a Topic by topicId
	 * @param topicId
	 * @return
	 * @throws EntityRequestException
	 */
	public Topic findTopicById(int topicId) throws EntityRequestException{
		Topic topic = null;
		
		try {
			topic = topicRepo.findTopicById(topicId);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a topic by id");
		}
		return topic;
	}
	
	/**
	 * This method will return List<Topic> by User
	 * @param user
	 * @return
	 * @throws EntityRequestException
	 */
	public List<Topic> findTopicsByUser(User user) throws EntityRequestException{
		List<Topic> topics = null;
		
		try {
			topics = topicRepo.findTopicsByUser(user);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all topics by User");
		}
		
		return topics;
	}
	
	/**
	 * This method will return List<Topic> by Forum ID
	 * @param forumId
	 * @return
	 */
	public List<Topic> findTopicsByForumIs(int forumId){
		return this.findTopicsByForum(forumServices.findFourmById(forumId));
	}
	
	/**
	 * This method will return List<Topic> by Forum
	 * @param forum
	 * @return
	 * @throws EntityRequestException
	 */
	public List<Topic> findTopicsByForum(Forum forum) throws EntityRequestException{
		List<Topic> topics = null;
		
		try {
			topics = topicRepo.findTopicsByForum(forum);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all topics by forum");
		}	
		
		return topics;
	}
	
	public List<Topic> findAll() throws EntityRequestException{
		List<Topic> topics = null;
		
		try {
			topics = topicRepo.findAll();
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all topics");
		}
		
		return topics;
	}
	
	/**
	 * This method will return a List<Topic> of topics that lastActivity < date.now - offset.
	 * For Example to get all topics that had lastActivity before 30 days, set offset = 30.
	 * @param offset
	 * @return topics
	 */
	public List<Topic> findTopicBeforeDate(int offset){
		// if offset = 0, no topics to delete
		if(offset == 0)
			return null;
		try {
			List<Topic> topics = topicRepo.findByLastActivityBefore(LocalDateTime.now().minusDays(offset));
			return topics;
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all topics by Date");
		}
	}
	
	public void save(Topic topic) throws EntityRequestException{
		try {
			topicRepo.save(topic);
		} catch (Exception e) {
			throw new EntityRequestException("Could not save new topic");
		}
	}
	
	public void delete(Topic topic) throws EntityRequestException{
		try {
			topicRepo.delete(topic);
		} catch (Exception e) {
			throw new EntityRequestException("Could not delete topic");
		}
	}
	
	/**
	 * This method will return List<Topic> of topics order by lastActivity
	 */
	public List<Topic> findTopicsOrderByLastActivity(Forum forum){
		try {
			return topicRepo.findByForumOrderByLastActivityDesc(forum);
		} catch (Exception e) {
			throw new EntityRequestException("Could not reload topics by date");
		}
	}
	
	/**
	 * This method will return the last active topic of a forum
	 */
	public Topic lastActiveTopicInForum(Forum forum) {
		List<Topic> topics = this.findTopicsOrderByLastActivity(forum);
		return topics.get(0);
	}

	/**
	 * 	This method will return an TopicAdditionalInformation object of Topic's author
	 * 	@param Topic to convert
	 */
	public TopicAdditionalInformation getTopicAdditionalInformation(Topic topic) {
		TopicAdditionalInformation topicAdditionalInformation = new TopicAdditionalInformation(
																this.findTopicsByUser(topic.getUser()).size()
																+ postServices.findPostsByUser(topic.getUser()).size()
																, topic);
		return topicAdditionalInformation;
	}
	
	/**
	 * 	This method will return a List<TopicAdditionalInformation> of converted posts.
	 * 	@param List<Post> posts to convert.
	 */
	public List<TopicAdditionalInformation> getTopicAdditionalInformation(List<Post> posts) {
		List<TopicAdditionalInformation> additionalInformation = new ArrayList<TopicAdditionalInformation>();
		
		if(!posts.isEmpty()) {
			for (Post post : posts) {
				TopicAdditionalInformation  additionalInfo = new TopicAdditionalInformation(
																this.findTopicsByUser(post.getUser()).size()
																+ postServices.findPostsByUser(post.getUser()).size()
																, post);
				additionalInformation.add(additionalInfo);
			}
		}
		return additionalInformation;
	}
	
}
