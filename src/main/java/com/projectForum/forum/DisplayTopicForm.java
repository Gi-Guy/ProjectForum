package com.projectForum.forum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.projectForum.general.Formatter;
import com.projectForum.post.Post;
import com.projectForum.topic.Topic;

/**
 * This class is used to display a topic in forum page
 * */
public class DisplayTopicForm {
	
	private Topic topic;
	private int numberOfPosts = 0;
	private Post lastPost;
	private String summary = "";
	private LocalDateTime lastActivity;
	private boolean toDelete = false;
	
	public DisplayTopicForm() {
		
	}
	
	public DisplayTopicForm(Topic topic, int numberOfPosts, Post lastPost) {
		this.topic = topic;
		this.numberOfPosts = numberOfPosts;
		this.lastPost = lastPost;
	}
	
	public DisplayTopicForm(Topic topic, int numberOfPosts, Post lastPost, LocalDateTime lastActivity) {
		this.topic = topic;
		this.numberOfPosts = numberOfPosts;
		this.lastPost = lastPost;
		this.lastActivity = lastActivity;
	}

	public DisplayTopicForm(Topic topic, int numberOfPosts, Post lastPost, String summary) {
		this.topic = topic;
		this.numberOfPosts = numberOfPosts;
		this.lastPost = lastPost;
		this.summary = summary;
	}

	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public int getNumberOfPosts() {
		return numberOfPosts;
	}
	public void setNumberOfPosts(int numberOfPosts) {
		this.numberOfPosts = numberOfPosts;
	}
	public Post getLastPost() {
		return lastPost;
	}
	public void setLastPost(Post lastPost) {
		this.lastPost = lastPost;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}
	
	public String getFormattedLastActivity() {
		return Formatter.toTimeDate(lastActivity);
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}
	public boolean isToDelete() {
		return this.toDelete;
	}
	public void setDelete() {
		this.toDelete = true;
	}
	
	
}
