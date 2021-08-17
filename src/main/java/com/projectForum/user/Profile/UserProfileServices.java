package com.projectForum.user.Profile;

import com.projectForum.REST.UpdateUser;
import com.projectForum.Services.PostServices;
import com.projectForum.Services.RestServices;
import com.projectForum.Services.TopicServices;
import com.projectForum.Services.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.projectForum.user.User;

/**
 * This class will gives services to all userProfile actions,
 * Since there is no Repository to userProfile.class
 */
@Service
public class UserProfileServices {

	private PostServices	postServices;
	private TopicServices	topicServices;
	private RestServices 	restServices;
	private UserServices	userService;


	@Autowired
	public UserProfileServices(PostServices postServices, TopicServices topicServices,
							   RestServices restServices, UserServices userService) {
		this.postServices = postServices;
		this.topicServices = topicServices;
		this.restServices = restServices;
		this.userService = userService;
	}
	
	/**
	 * This method will return an userProfile object, including User object.
	 * @param  Int - userId
	 * @return UserProfile object
	 * @include User object
	 */
	public UserProfile findUserProfileById(int userId) {
		UserProfile userProfile = new UserProfile();
		User user = userService.findUserByUserId(userId);
		
		userProfile.setUser(user);
		userProfile.setTopics(topicServices.findTopicsByUser(user));
		userProfile.setPosts(postServices.findPostsByUser(user));
		return userProfile;
	}
	
	/**
	 * This method will return a UserProfile object by calling to 'findUserProfileById' method,
	 * @param String username
	 * @return UserProfile object
	 */
	public UserProfile findUserByUsername(String username) {
		UserProfile userProfile = this.findUserProfileById(userService.findUserByUsername(username).getId());
		return userProfile;
	}
	
	/**
	 * This method will return an edit form of user.
	 * @param User user to give edit form
	 * @return UpdateUser form
	 */
	public UpdateUser getUpdateForm(User user) {
		UpdateUser updateUser = new UpdateUser();

		updateUser.setId(user.getId());
		updateUser.setUsername(user.getUsername());
		updateUser.setFirstName(user.getFirstName());
		updateUser.setLastName(user.getLastName());
		updateUser.setEmail(user.getEmail());
		updateUser.setRole(user.getRole().getName());
		updateUser.setPassword(user.getPassword());
		return updateUser;
	}
	
	/**
	 * This method will update an existing user by UpdateUser object.
	 * @param UpdateUser
	 */
	public void updateUser(UpdateUser updateUser) {
		restServices.updateUser(updateUser);
	}
}