package com.projectForum.Services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.ForumForm;
import com.projectForum.ControlPanel.SearchUserForm;
import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

//	TODO TEST THIS FILE
/* ###########################################################
 * 		WARNING: THIS SERVICE FILE ISN'T TESTED YET!
 * ###########################################################*/


@Service
public class ControlPanelServices {

	private UserRepository	userRepo;
	private PostRepository	postRepo;
	private TopicRepository	topicRepo;
	private ForumRepository	forumRepo;
	
	@Autowired
	public ControlPanelServices(UserRepository userRepo, PostRepository postRepo, TopicRepository topicRepo,
			ForumRepository forumRepo) {
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.topicRepo = topicRepo;
		this.forumRepo = forumRepo;
	}
	
	/**
	 *  This method will create a list of List<ForumForm> to display additional information in conrtol Panel.
	 * 
	 *  @param List<Forum>*/
	public List<ForumForm> createForumDisplayList(List<Forum> forums){
		
		List<ForumForm> forumsForm = new ArrayList<ForumForm>();
		
		// updating new list
		for(Iterator<Forum> i = forums.iterator(); i.hasNext(); ) {
			forumsForm.add(createDisplayForum(i.next()));
		}
		
		return forumsForm;
	}
	
	private ForumForm createDisplayForum(Forum forum) {
		
		ForumForm newForm = new ForumForm();
		
		// update forum
		newForm.setForum(forum);
		
		// update Topics counter
		int topicsCounter = topicRepo.findTopicsByForum(forum).size();
		newForm.setNumOfTopics(topicsCounter);
		
		// update short Description
		String description = forum.getDescription();
		String shortDes = description.substring(0, description.length() - 5) + "..." ;
		if(description.isBlank())
			newForm.setShortDescription("");
		
		else if(description.length() < 40)
			shortDes = description;
		
		else
			shortDes = description.substring(0, 40) + "..." ;
		
		newForm.setShortDescription(shortDes);
		
		return newForm;
	}
	
	/**This method will update the priority of a two forums, higher priority and lower priority*/
	/**
	 * @param forum
	 * @param priority
	 */
	public void updatePriority(Forum forum, int priority) {
		int tempPriority = forum.getPriority();
		
		if(tempPriority != priority) {
			Forum oldForum = forumRepo.findByPriority(priority);
			forum.setPriority(priority);
			oldForum.setPriority(tempPriority);
			
			forumRepo.save(forum);
			forumRepo.save(oldForum);
		}
	}
	
	/**This method will update the role of exsits user.*/
	/**
	 * @param user
	 * @param searchUserForm
	 */
	public void updateUserRole(User user, SearchUserForm searchUserForm) {
		//user.setRole(searchUserForm.getRole());
		userRepo.save(user);
		
	}
	/**
	 * @param username
	 * @return
	 */
	public SearchUserForm findSearchUserByUsername(String username) {
		return this.findSearchUserById(userRepo.findByUsername(username).getId());
	}
	/**
	 * @param userId
	 * @return
	 */
	public SearchUserForm findSearchUserById(int userId) {
		User user = userRepo.findUserById(userId);
		SearchUserForm searchUser = new SearchUserForm();
		
		searchUser.setUser(user);
		//searchUser.setRole(user.getRole());
		searchUser.setPosts(postRepo.findPostsByUser(user));
		searchUser.setTopics(topicRepo.findTopicsByUser(user));
		return searchUser;
	}
}
