package com.projectForum.forum;

import javax.validation.constraints.Min;

/** This method will use to edit an exists Forum.*/
public class EditForumForm {
	
	private String name;
	private String description;
	@Min(value=1)
	private int forumId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getForumId() {
		return forumId;
	}
	public void setForumId(int forumId) {
		this.forumId = forumId;
	}
	
	
}
