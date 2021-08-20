package com.projectForum.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.forum.DisplayTopicForm;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;

/**
 * 	This class will gives services for most forum's actions
 */

@Service
public class ForumServices {

	@Autowired
	private ForumRepository	forumRepo;
	@Autowired
	private TopicServices topicServices;
	@Autowired
	private PostServices	postServices;

	
	/**
	 * This method will return a Forum by forumId
	 * @param forumId
	 * @throws EntityRequestException
	 */
	public Forum findFourmById(int forumId) throws EntityRequestException{
		Forum forum = null;
		
		try {
			forum = forumRepo.findById(forumId);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a forum by id");
		}
		
		return forum;
	}
	
	/**
	 * This method will return a Forum by priority
	 * @param priority
	 * @throws EntityRequestException
	 */
	public Forum findForumByPriority(int priority) throws EntityRequestException{
		Forum forum = null;
		
		try {
			forum = forumRepo.findByPriority(priority);
		} catch (Exception e) {
			throw new EntityRequestException("Could not find a forum by priority");
		}
		
		return forum;
	}
	
	/**
	 * This method will return List<Forum> Order By Priority ascending
	 * @throws EntityRequestException
	 */
	public List<Forum> findForumsByPriorityAsc() throws EntityRequestException{
		List<Forum> forums = null;
		
		try {
			forums = forumRepo.findByOrderByPriorityAsc();
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all forums");
		}
		
		return forums;
	}
	
	/**
	 * This method will return List<Forum> 
	 * @throws EntityRequestException
	 */
	public List<Forum> findAll() throws EntityRequestException{
		List<Forum> forums = null;
		
		try {
			forums = forumRepo.findAll();
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all forums");
		}
		
		return forums;
	}
	
	/**
	 * This method will save a new Forum in database
	 * @param forum
	 * @throws EntityRequestException
	 */
	public void save(Forum forum) throws EntityRequestException{
		try {
			forumRepo.save(forum);
		} catch (Exception e) {
			throw new EntityRequestException("Could not save new Forum");
		}
	}
	
	public void delete(Forum forum) throws EntityRequestException{
		try {
			forumRepo.delete(forum);
		} catch (Exception e) {
			throw new EntityRequestException("Could not delete Forum");
		}
	}
	
	/* Controller actions */
	
	/**
	 * This method will convert topics to DisplayTopicForm
	 */
	public List<DisplayTopicForm> displayTopics(int forumId){
		Forum forum = this.findFourmById(forumId);
		//List<Topic> topics = topicServices.findTopicsByForum(forum);
		List<Topic> topics = topicServices.findTopicsOrderByLastActivity(forum);
		List<DisplayTopicForm> displayTopics = new ArrayList<DisplayTopicForm>();
		List<Post> posts;
		
		Post lastPost = null;
		String summary = "";
				
		for(int i=0; i<topics.size(); i++) {
			posts = postServices.findPostsByTopic(topics.get(i));
			
			if(!posts.isEmpty()) {
				lastPost = posts.get(posts.size()-1);	
			}
			
			displayTopics.add(new DisplayTopicForm(topics.get(i),
								posts.size()
									, lastPost,
									topics.get(i).getLastActivity()));
			
			// updating summary
			if(topics.get(i).getContent().length() > 100) {
				summary = topics.get(i).getContent().substring(0,100) + "...";
				displayTopics.get(i).setSummary(summary);
			}
			else 
				displayTopics.get(i).setSummary(topics.get(i).getContent());
			
			// reset values
			summary = "";
			lastPost = null;	
		}
		return displayTopics;
	}
}
