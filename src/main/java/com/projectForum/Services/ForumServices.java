package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.Exceptions.EntityRequestException;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;


/**
 * 	This class will gives services for most forum's actions
 * */

@Service
public class ForumServices {

	@Autowired
	private ForumRepository	forumRepo;
	
	/**
	 * 	This method will return an Forum by forumId
	 * @param forumId
	 * @return
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
	 * 	This method will return an Forum by priority
	 * @param priority
	 * @return
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
	 * 	This method will return List<Forum> Order By Priority Asc
	 * @return
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
	 * 	This method will return List<Forum> 
	 * @return
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
	 * 	This method will save new Forum in database
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
}
