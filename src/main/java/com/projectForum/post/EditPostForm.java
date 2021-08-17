package com.projectForum.post;

import javax.validation.constraints.Min;

/** Object to contain updated information of a post */
public class EditPostForm {
	
	private String content;
	
	@Min(value=1)
	private int topicId;
	
	@Min(value=1)
	private int postId;
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getTopicId() {
		return topicId;
	}
	
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	public int getPostId() {
		return postId;
	}
	
	public void setPostId(int postId) {
		this.postId = postId;
	}
}
