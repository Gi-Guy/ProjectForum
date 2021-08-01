package com.projectForum.post;

import javax.validation.constraints.Min;


/** This method will use to edit an exists post.*/
public class editPostForm {
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
