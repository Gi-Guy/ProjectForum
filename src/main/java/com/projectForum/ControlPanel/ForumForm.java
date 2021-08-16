package com.projectForum.ControlPanel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.projectForum.forum.Forum;
import com.projectForum.user.User;

/**
 * This class used for displaying a forum in control panel.*/
public class ForumForm {
	
	
	private Forum forum;
	private int numOfTopics;
	private String shortDescription;
	private User lastUserActivity;
	private LocalDateTime lastActivity;

	
	// Defining Date format
	DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
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

	public User getLastUserActivity() {
		return lastUserActivity;
	}

	public void setLastUserActivity(User lastUserActivity) {
		this.lastUserActivity = lastUserActivity;
	}

	public String getLastActivity() {
		return lastActivity.format(formatter);
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}


	
	

}
