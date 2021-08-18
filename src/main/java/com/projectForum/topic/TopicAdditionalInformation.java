package com.projectForum.topic;

import com.projectForum.post.Post;

/**
 * 	This class will add additional information for an exists Topic.
 * */
public class TopicAdditionalInformation {

	private int totalOfPosts;
	private Post post;
	private Topic topic;
	
	public TopicAdditionalInformation() {
	}
	
	public TopicAdditionalInformation(int totalOfPosts, Post post) {
		this.totalOfPosts = totalOfPosts;
		this.post = post;
	}

	public TopicAdditionalInformation(int totalOfPosts, Topic topic) {
		this.totalOfPosts = totalOfPosts;
		this.topic = topic;
	}

	public int getTotalOfPosts() {
		return totalOfPosts;
	}

	public void setTotalOfPosts(int totalOfPosts) {
		this.totalOfPosts = totalOfPosts;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	

}
