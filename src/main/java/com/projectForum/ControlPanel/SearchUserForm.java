package com.projectForum.ControlPanel;

import java.util.List;


import com.projectForum.post.Post;
import com.projectForum.topic.Topic;
import com.projectForum.user.User;

public class SearchUserForm {
	
	private User user;

	private List<Topic> topics;
	private List<Post> posts;
	
	
	public SearchUserForm(User user, List<Topic> topics, List<Post> posts) {
		this.user = user;
		this.topics = topics;
		this.posts = posts;
	}


	public SearchUserForm() {
		
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
	
	
	

}
