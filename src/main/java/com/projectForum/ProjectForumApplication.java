package com.projectForum;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.projectForum.ControlPanel.Configuration.ForumInformation;
import com.projectForum.Services.ForumInformationServices;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ProjectForumApplication extends SpringBootServletInitializer{

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
	
	@PostConstruct
	private void postInit(){
		
		/*
		 * Saving forum first configurations from application.properties file
		 * */
		ForumInformation forumInformation = infoServices.getForumInformation();
		if(forumInformation == null) {
			ForumInformation newForumInformation = 
					new ForumInformation(configProperties.getForumName(), 
							configProperties.getForumDescription(), 
							configProperties.getCounterForDelete());
			infoServices.save(newForumInformation);
		}
	}
}
