package com.projectForum.ControlPanel;

import java.util.List;

import com.projectForum.Security.Role;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;


public class SearchUserForm {
	
	private User user;

	private List<Topic> topics;
	private List<Post> posts;
	private boolean keepActivity = true;
	private Role role;
	private List<Role> roles;
	private String username;
	private String name;
	public SearchUserForm(User user, List<Topic> topics, List<Post> posts) {
		this.user = user;
		this.topics = topics;
		this.posts = posts;
	}


	public SearchUserForm() {
		
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

	
	public void setRole(Role role) {
		this.role = role;
	}


}
