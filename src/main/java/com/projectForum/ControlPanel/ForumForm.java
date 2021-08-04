package com.projectForum.ControlPanel;

import com.projectForum.forum.Forum;

/**
 * This class used for displaying a forum in control panel.*/
public class ForumForm {
	
	
	private Forum forum;
	
	private int numOfTopics;
	
	private String shortDescription;

	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}

	public int getNumOfTopics() {
		return numOfTopics;
	}

	public void setNumOfTopics(int numOfTopics) {
		this.numOfTopics = numOfTopics;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	

}
