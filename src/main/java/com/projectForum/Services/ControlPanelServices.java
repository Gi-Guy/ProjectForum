package com.projectForum.Services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.EditUserForm;
import com.projectForum.ControlPanel.ForumForm;
import com.projectForum.forum.DisplayTopicForm;
import com.projectForum.forum.Forum;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;

//	TODO TEST THIS FILE
/* ###########################################################
 * 		WARNING: THIS SERVICE FILE ISN'T TESTED YET!
 * ###########################################################*/


@Service
public class ControlPanelServices {

	private UserServices	userServices;
	private TopicServices	topicServices;
	private ForumServices	forumServices;
	private PostServices	postServices;
	
	@Autowired
	public ControlPanelServices(UserServices userServices, TopicServices topicServices, ForumServices forumServices,
			PostServices postServices) {
		this.userServices = userServices;
		this.topicServices = topicServices;
		this.forumServices = forumServices;
		this.postServices = postServices;
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
		
		// update Topics counter and last activity
		List<Topic> topics = topicServices.findTopicsOrderByLastActivity(forum); 
		int topicsCounter = topics.size();
		newForm.setNumOfTopics(topicsCounter);
		if(!topics.isEmpty()) {
			newForm.setLastActivity(topics.get(0).getLastActivity());
			
			// Saving last user activity
			List<Post> posts = postServices.findPostsByTopic(topics.get(0));
			if(!posts.isEmpty()) {
				
				Post lastPost = posts.get(posts.size()-1);
				newForm.setLastUserActivity(lastPost.getUser());
			}
			else
				newForm.setLastUserActivity(null);
		}
		else {
			newForm.setLastActivity(null);
			newForm.setLastUserActivity(null);
		}
		
		// update short Description
		String description = forum.getDescription();
		String shortDes = "";
		if(description.isBlank())
			newForm.setShortDescription("");
		
		else if(description.length() < 40)
			shortDes = description;
		
		else
			shortDes = description.substring(0, 40) + "..." ;
		
		newForm.setShortDescription(shortDes);
		
		return newForm;
	}
	
	/** This method will update the priority of a two forums, higher priority and lower priority.
	 * 	It will set the traget forum to the higher priority.
	 * 	It will set :: targetForum.priority > lowerForum.priority
	 * @param forum
	 * @param priority
	 */
	private void updatePriorityUp(Forum targetForum, Forum lowerForum) {
		// Making sure that forums are exsits
		if(targetForum == null || lowerForum == null)
			return;
		// updating prioritys
		int tempPriority = targetForum.getPriority();
		targetForum.setPriority(lowerForum.getPriority());
		lowerForum.setPriority(tempPriority);
		
		forumServices.save(targetForum);
		forumServices.save(lowerForum);
	}
	/** This method will update the priority of a two forums, higher priority and lower priority.
	 *  It will set the traget forum to the higher priority.
	 *  
	 *  @param int forumId
	 * */
	public void updatePriorityUp(int forumId) {
		Forum forum = forumServices.findFourmById(forumId);
		this.updatePriorityUp(forum , forumServices.findForumByPriority(forum.getPriority() - 1));
	}
	/** This method will update the priority of a two forums, higher priority and lower priority.
	 * 	It will set the traget forum to the higher priority.
	 * 	It will set :: targetForum.priority < higherForum.priority
	 * @param forum
	 * @param priority
	 */
	private void updatePriorityDown(Forum targetForum, Forum higherForum) {
		// Making sure that forums are exsits
		if(targetForum == null || higherForum == null)
			return;
		// updating prioritys
		int tempPriority = targetForum.getPriority();
		targetForum.setPriority(higherForum.getPriority());
		higherForum.setPriority(tempPriority);
		
		forumServices.save(targetForum);
		forumServices.save(higherForum);		
	}
	/** This method will update the priority of a two forums, higher priority and lower priority.
	 *  It will set the traget forum to the lower priority.
	 *  
	 *  @param int forumId
	 * */
	public void updatePriorityDown(int forumId) {
		Forum forum = forumServices.findFourmById(forumId);
		this.updatePriorityDown(forum , forumServices.findForumByPriority(forum.getPriority() + 1));
	}
	
	public EditUserForm editUserForm(String username) {
		return this.editUserFormById(userServices.findUserByUsername(username).getId());
	}

	private EditUserForm editUserFormById(int id) {
		EditUserForm editForm = new EditUserForm();
		User user = userServices.findUserByUserId(id);
		
		// insert all relevent information
		editForm.setId(user.getId());
		editForm.setUsername(user.getUsername());
		editForm.setEmail(user.getEmail());
		editForm.setRole(user.getRole().getName());
		editForm.setJoiningDate(user.getJoiningDate());
		
		return editForm;
	}
	
}
