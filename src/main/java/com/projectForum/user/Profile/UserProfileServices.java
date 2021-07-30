package com.projectForum.user.Profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.post.PostRepository;
import com.projectForum.topic.TopicRepository;
import com.projectForum.user.User;
import com.projectForum.user.UserRepository;

/**
 * This class will gives services to all userProfile actions,
 * Since there is no Repository to userProfile.class*/
@Service
public class UserProfileServices {
	
	private UserRepository	userRepo;
	private PostRepository	postRepo;
	private TopicRepository	topicRepo;
	
	@Autowired
	public UserProfileServices(UserRepository userRepo, PostRepository postRepo, TopicRepository topicRepo) {
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.topicRepo = topicRepo;
	}
	
	/**This method will return an userProfile object, including User object.
	 * @param  Int - userId
	 * @return UserProfile object
	 * @include User object*/
	public UserProfile findUserProfileById(int userId) {
		UserProfile userProfile = new UserProfile();
		User user = userRepo.findUserById(userId);
		
		userProfile.setUser(user);
		userProfile.setTopics(topicRepo.findTopicsByUser(user));
		userProfile.setPosts(postRepo.findPostsByUser(user));
		return userProfile;
	}
	
	/**This method will return a UserProfile object by calling to 'findUserProfileById' method,
	 * @param String username
	 * @return UserProfile object*/
	public UserProfile findUserByUsername(String username) {
		UserProfile userProfile = this.findUserProfileById(userRepo.findByUsername(username).getId());
		return userProfile;
	}
	
	
}
