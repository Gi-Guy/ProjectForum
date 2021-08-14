package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.forum.Forum;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;


/**
 * 	This class will gives services for most topic's actions
 * */

@Service
public class TopicServices {
	
	@Autowired
	private TopicRepository	topicRepo;
	@Autowired
	private ForumServices	forumServices;
	
	/**
	 * 	This method will return a Topic by topicId
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
		/*if (topic == null)
			throw new EntityRequestException("Could not find a topic by id");*/
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
		/*if(topics == null)
			throw new EntityRequestException("Could not find all topics by User");*/
		
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
	/*	if(topics == null)
			throw new EntityRequestException("Could not find all topics by forum");*/
		
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
}
