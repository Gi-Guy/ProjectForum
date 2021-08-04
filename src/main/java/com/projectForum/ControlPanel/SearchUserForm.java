package com.projectForum.ControlPanel;

import java.util.List;

import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;


public class SearchUserForm {
	
	private User user;

	public String getRole() {
		return role;
	}


	private List<Topic> topics;
	private List<Post> posts;
	private boolean keepActivity = true;
	private String role;
	private String username;
	public SearchUserForm(User user, List<Topic> topics, List<Post> posts) {
		this.user = user;
		this.topics = topics;
		this.posts = posts;
	}


	public SearchUserForm() {
		
	}
	

	public SearchUserForm(String role) {
		this.role = role;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public void setKeepActivity(boolean keepActivity) {
		this.keepActivity = keepActivity;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}



	public List<Topic> getTopics() {
		return topics;
	}


	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}


	public List<Post> getPosts() {
		return posts;
	}


	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public void keepActivity() {
		this.keepActivity = true;
	}
	
	public void noKeepActivity() {
		this.keepActivity = false;
	}
	
	public boolean getKeepActivity() {
		return this.keepActivity;
	}

	
	public void setRole(String role) {
		this.role = role;
	}


}
