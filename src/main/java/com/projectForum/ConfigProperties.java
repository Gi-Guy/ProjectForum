package com.projectForum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
@ConfigurationProperties(prefix = "settings")
public class ConfigProperties {
	@Value("${forumName}")
	private String forumName;
	
	@Value("${forumDescription}")
	private String forumDescription;
	
	@Value("${counterForDelete}")
	private	int counterForDelete;
	
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
	
	
}
