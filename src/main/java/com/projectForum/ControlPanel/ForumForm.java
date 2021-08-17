package com.projectForum.ControlPanel;

import java.time.LocalDateTime;

import com.projectForum.forum.Forum;
import com.projectForum.general.Formatter;
import com.projectForum.user.User;

/**
 * This class used for displaying a forum in control panel.
 */
public class ForumForm {
	
	
	private Forum forum;
	private int numOfTopics;
	private String shortDescription;
	private User lastUserActivity;
	private LocalDateTime lastActivity;
	
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

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}
	
	public String getFormattedLastActivity() {
		return Formatter.toTimeDate(lastActivity);
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}
}