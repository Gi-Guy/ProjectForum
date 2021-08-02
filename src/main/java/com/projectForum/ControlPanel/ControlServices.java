package com.projectForum.ControlPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.UserRepository;

@Service
public class ControlServices {

	private UserRepository	userRepo;
	private PostRepository	postRepo;
	private TopicRepository	topicRepo;
	private ForumRepository	forumRepo;
	
	@Autowired
	public ControlServices(UserRepository userRepo, PostRepository postRepo, TopicRepository topicRepo,
			ForumRepository forumRepo) {
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.topicRepo = topicRepo;
		this.forumRepo = forumRepo;
	}
	
	/**This method will update the priority of a two forums, higher priority and lower priority*/
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
}
