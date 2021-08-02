package com.projectForum.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.forum.Forum;
import com.projectForum.forum.ForumRepository;
import com.projectForum.post.EditPostForm;
import com.projectForum.post.Post;
import com.projectForum.post.PostRepository;
import com.projectForum.topic.NewTopicPageForm;
import com.projectForum.topic.Topic;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

/** This class will use as a service to all edit actions in the application*/
@Service
public class EditServices {

	private UserRepository	userRepo;
	private PostRepository	postRepo;
	private TopicRepository	topicRepo;
	private ForumRepository	forumRepo;
	
	@Autowired
	public EditServices(UserRepository userRepo, PostRepository postRepo, TopicRepository topicRepo,
			ForumRepository forumRepo) {
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.topicRepo = topicRepo;
		this.forumRepo = forumRepo;
	}
	
	/** This method will update exists post with new Content.
	 * 	In case that the content is blank, there will be no update.
	 * @param Post to update
	 * @param EditPostForm new Content*/
	public void updatePost(Post post, EditPostForm editPostForm) {
		
		// Checking if there is a new content to update
		if(!editPostForm.getContent().isBlank()) 
			post.setContent(editPostForm.getContent());
		
		postRepo.save(post);
	}
	
	/** This method will update exists topic with new Content and new Title.
	 * 	In case that the Title or Contect are blank, there will be no update.
	 *  @param Topic to update
	 *  @param NewTopicPageForm*/
	public void updateTopic(Topic topic, NewTopicPageForm newTopic) {
		
		// Checking id there is a new title to update
		if(!newTopic.getTitle().isBlank())
			topic.setTitle(newTopic.getTitle());
		
		// Checking if there is a new content to update
		if (!newTopic.getContent().isBlank())
			topic.setContent(newTopic.getContent());
		
		topicRepo.save(topic);
	}
	
	/** This method will update exists Forum with new Name and New Description.
	 * 	In case that the Name or Description are blank, there will be no update.
	 * 	@param Forum to update
	 * 	@param */
	public void updateForum(Forum forum) {
		
		forumRepo.save(forum);
	}
	
	/**	This method will update exists User with new user information.
	 * 	In case that there is any blank information, there will be no update.
	 * 	@param User to update.
	 * 	@param */
	public void updateUser(User user) {
		
		
		
		userRepo.save(user);
	}
}
