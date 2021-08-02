package com.projectForum.topic;

import javax.validation.constraints.Min;

/**
 * This class is for new topic page only.
 * The topic controller need a way to keep a forum id.
 * */
public class NewTopicPageForm {
	
	private String title;
	private String content;
	private int topicId;
	
	@Min(value=1)
	private int forumId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getForumId() {
		return forumId;
	}

	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	
}
