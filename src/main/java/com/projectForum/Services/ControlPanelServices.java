package com.projectForum.Services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.EditUserForm;
import com.projectForum.ControlPanel.ForumForm;
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
		
		forumRepo.save(targetForum);
		forumRepo.save(lowerForum);
		
	}
	/** This method will update the priority of a two forums, higher priority and lower priority.
	 *  It will set the traget forum to the higher priority.
	 *  
	 *  @param int forumId
	 * */
	public void updatePriorityUp(int forumId) {
		Forum forum = forumRepo.findById(forumId);
		this.updatePriorityUp(forum , forumRepo.findByPriority(forum.getPriority() - 1));
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
		
		forumRepo.save(targetForum);
		forumRepo.save(higherForum);
		
	}
	/** This method will update the priority of a two forums, higher priority and lower priority.
	 *  It will set the traget forum to the lower priority.
	 *  
	 *  @param int forumId
	 * */
	public void updatePriorityDown(int forumId) {
		Forum forum = forumRepo.findById(forumId);
		this.updatePriorityDown(forum , forumRepo.findByPriority(forum.getPriority() + 1));
	}
	
	public EditUserForm editUserForm(String username) {
		return this.editUserFormById(userRepo.findByUsername(username).getId());
	}

	private EditUserForm editUserFormById(int id) {
		EditUserForm editForm = new EditUserForm();
		User user = userRepo.findUserById(id);
		
		// insert all relevent information
		editForm.setId(user.getId());
		editForm.setUsername(user.getUsername());
		editForm.setEmail(user.getEmail());
		editForm.setRole(user.getRole().getName());
		editForm.setJoiningDate(user.getJoiningDate());
		
		return editForm;
	}
	
}
