package com.projectForum;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.projectForum.ControlPanel.Configuration.ForumInformation;
import com.projectForum.Services.ForumInformationServices;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class ProjectForumApplication extends SpringBootServletInitializer {

	@Autowired
	private ConfigProperties configProperties;
	@Autowired
	private ForumInformationServices infoServices;
	
	public static void main(String[] args) {
		SpringApplication.run(ProjectForumApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ProjectForumApplication.class);
	}
	

	/**
	 * Saving forum's first configuration from the application.properties file
	 */
	@PostConstruct
	private void postInit(){
		ForumInformation forumInformation = infoServices.getForumInformation();
		if(forumInformation == null) {
			ForumInformation newForumInformation = 
					new ForumInformation(configProperties.getForumName(), 
							configProperties.getForumDescription(), 
							configProperties.getCounterForDelete(),
							configProperties.getLimitOfPrivateMessages());
			infoServices.save(newForumInformation);
		}
	}
}
