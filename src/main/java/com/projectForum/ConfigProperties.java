package com.projectForum;


import javax.validation.Valid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * This class will reload first configuration information from application.properties file
 */

@Component
@ConfigurationProperties(prefix = "config")
@Valid
public class ConfigProperties {
	
	private String forumName;
	private String forumDescription;
	private	int counterForDelete;
	private int limitOfPrivateMessages;
	
	public String getForumName() {
		return forumName;
	}
	public void setForumName(String forumName) {
		this.forumName = forumName;
	}
	public String getForumDescription() {
		return forumDescription;
	}
	public void setForumDescription(String forumDescription) {
		this.forumDescription = forumDescription;
	}
	public int getCounterForDelete() {
		return counterForDelete;
	}
	public void setCounterForDelete(int counterForDelete) {
		this.counterForDelete = counterForDelete;
	}
	public int getLimitOfPrivateMessages() {
		return limitOfPrivateMessages;
	}
	public void setLimitOfPrivateMessages(int limitOfPrivateMessages) {
		this.limitOfPrivateMessages = limitOfPrivateMessages;
	}
}
